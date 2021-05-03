package com.bornfire.controller;


/*CREATED BY	: KALAIVANAN RAJENDRAN.R
CREATED ON	: 30-DEC-2019
PURPOSE		: IPS Connect 24 (Calling Connect 24 web service Debit/Credit Customer Account)
*/

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.bornfire.entity.AccountContactResponse;
import com.bornfire.entity.BukCreditTransferRequest;
import com.bornfire.entity.BulkDebitRemitterAccount;
import com.bornfire.entity.C24FTRequest;
import com.bornfire.entity.C24FTResponse;
import com.bornfire.entity.C24RequestAcount;
import com.bornfire.entity.MCCreditTransferRequest;
import com.bornfire.entity.ManualFndTransferRequest;
import com.bornfire.entity.RegPublicKey;
import com.bornfire.entity.SettlementAccount;
import com.bornfire.entity.SettlementAccountRep;
import com.bornfire.entity.TranCBSTable;
import com.bornfire.entity.TranMonitorStatus;
import com.bornfire.entity.TransactionMonitor;
import com.bornfire.entity.TransactionMonitorRep;
import com.google.gson.Gson;

import static com.bornfire.exception.ErrorResponseCode.*;

@Component
public class Connect24Service {
	private static final Logger logger = LoggerFactory.getLogger(Connect24Service.class);
	
	@Autowired
	TransactionMonitorRep tranRep;

	@Autowired
	IPSDao ipsDao;

	@Autowired
	Environment env;

	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	SettlementAccountRep settlAcctRep;

	////Debit Fund Transfer
	public ResponseEntity<C24FTResponse> DbtFundRequest(String senderParticipantBIC, String participantSOL,
			MCCreditTransferRequest mcCreditTransferRequest, String sysTraceAuditNumber, String seqUniqueID,String trRmks)
			{
		
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.set("Partipant_BIC", env.getProperty("bob.bankcode"));
		httpHeaders.set("Partipant_SOL", participantSOL);
		httpHeaders.set("SYS_TRACE_AUDIT_NUMBER", sysTraceAuditNumber);

		C24FTRequest c24ftRequest = new C24FTRequest();
		C24RequestAcount c24RequestAcount = new C24RequestAcount();
		c24RequestAcount.setAcctNumber(mcCreditTransferRequest.getFrAccount().getAcctNumber());
		c24RequestAcount.setSchmType(mcCreditTransferRequest.getFrAccount().getSchmType());
		c24RequestAcount.setSettlAcctNumber(settlAcctRep.findById(env.getProperty("settl.payable")).get().getAccount_number());
		
		c24ftRequest.setAccount(c24RequestAcount);
		c24ftRequest.setCurrency_Code(mcCreditTransferRequest.getCurrencyCode());
		c24ftRequest.setPAN(mcCreditTransferRequest.getPan());
		c24ftRequest.setTrAmt(mcCreditTransferRequest.getTrAmt());
		c24ftRequest.setTrRmks(trRmks);

		HttpEntity<C24FTRequest> entity = new HttpEntity<>(c24ftRequest, httpHeaders);
		ResponseEntity<C24FTResponse> response = null;
		
		ipsDao.updateINOUT(seqUniqueID,"CBS_IN");


		SettlementAccount settlAcct=settlAcctRep.findById(env.getProperty("settl.payable")).get();
		try {
			logger.info("Sending message to connect24 ");
			
			response = restTemplate.postForEntity(env.getProperty("connect24.url") + "/api/ws/dbtActfndTransfer?",
					entity, C24FTResponse.class);
			C24FTResponse c24ftResponse = new C24FTResponse("SUCCESS", response.getBody().getBalance(),
					response.getBody().getTranCurrency());

			//// update table
			ipsDao.updateTranCBS(sysTraceAuditNumber, mcCreditTransferRequest.getFrAccount().getAcctNumber(),
					mcCreditTransferRequest.getTrAmt(), "DEBIT", mcCreditTransferRequest.getCurrencyCode(), "SUCCESS",
					"", seqUniqueID,"SYSTEM",settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));
			
			ipsDao.updateINOUT(seqUniqueID,"CBS_OUT");

			return new ResponseEntity<>(c24ftResponse, HttpStatus.OK);
		} catch (HttpClientErrorException ex) {

			logger.info("HttpClientErrorException --------->" + ex.getStatusCode());
			logger.info(ex.getLocalizedMessage());
			logger.error(ex.getLocalizedMessage());
			
	
			
			if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", SERVER_ERROR_CODE,
						Collections.singletonList(TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString()));

				///// Update Table
				ipsDao.updateTranCBS(sysTraceAuditNumber, mcCreditTransferRequest.getFrAccount().getAcctNumber(),
						mcCreditTransferRequest.getTrAmt(), "DEBIT", mcCreditTransferRequest.getCurrencyCode(),
						"FAILURE", TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(), seqUniqueID,"SYSTEM"
						,settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));
				
				ipsDao.updateINOUT(seqUniqueID,"CBS_OUT");

				return new ResponseEntity<>(c24ftResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				C24FTResponse c24ftResponse1 = new Gson().fromJson(ex.getResponseBodyAsString(), C24FTResponse.class);
				C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", c24ftResponse1.getError(),
						c24ftResponse1.getError_desc());

				//// update table
				ipsDao.updateTranCBS(sysTraceAuditNumber, mcCreditTransferRequest.getFrAccount().getAcctNumber(),
						mcCreditTransferRequest.getTrAmt(), "DEBIT", mcCreditTransferRequest.getCurrencyCode(),
						"FAILURE", c24ftResponse1.getError_desc().get(0).toString(), seqUniqueID,"SYSTEM"
						,settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));
				
				ipsDao.updateINOUT(seqUniqueID,"CBS_OUT");

				return new ResponseEntity<>(c24ftResponse, ex.getStatusCode());
			}

		} catch (HttpServerErrorException ex) {
			logger.info("HttpServerErrorException --------->");
			logger.info(ex.getLocalizedMessage());
			logger.error(ex.getLocalizedMessage());
			


			C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", SERVER_ERROR_CODE,
					Collections.singletonList(TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString()));
			ipsDao.updateTranCBS(sysTraceAuditNumber, mcCreditTransferRequest.getFrAccount().getAcctNumber(),
					mcCreditTransferRequest.getTrAmt(), "DEBIT", mcCreditTransferRequest.getCurrencyCode(), "FAILURE",
					TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(), seqUniqueID,"SYSTEM"
					,settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));
			
			ipsDao.updateINOUT(seqUniqueID,"CBS_OUT");

			return new ResponseEntity<>(c24ftResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	////Credit Fund Transfer
	public ResponseEntity<C24FTResponse> cdtFundRequest(String acctNumber, String trAmt, String currency,
			String sysTraceAuditNumber, String SeqUniqueID,String trRmks) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.set("Partipant_BIC",env.getProperty("bob.bankcode"));
		httpHeaders.set("Partipant_SOL", acctNumber.substring(0, 4));
		httpHeaders.set("SYS_TRACE_AUDIT_NUMBER", sysTraceAuditNumber);

		C24FTRequest c24ftRequest = new C24FTRequest();
		C24RequestAcount c24RequestAcount = new C24RequestAcount();
		c24RequestAcount.setAcctNumber(acctNumber);
		c24RequestAcount.setSchmType("");
		c24RequestAcount.setSettlAcctNumber(settlAcctRep.findById(env.getProperty("settl.receivable")).get().getAccount_number());

		c24ftRequest.setAccount(c24RequestAcount);
		c24ftRequest.setCurrency_Code(currency);
		c24ftRequest.setPAN("0000000000000000");
		c24ftRequest.setTrAmt(trAmt);
		c24ftRequest.setTrRmks(trRmks);
		HttpEntity<C24FTRequest> entity = new HttpEntity<>(c24ftRequest, httpHeaders);
		
		SettlementAccount settlAcct=settlAcctRep.findById(env.getProperty("settl.receivable")).get();
		
		ResponseEntity<C24FTResponse> response = null;
		try {
			logger.info("Sending message to connect24 credit using restTemplate");
			response = restTemplate.postForEntity(env.getProperty("connect24.url") + "/api/ws/cdtActfndTransfer?",
					entity, C24FTResponse.class);

			C24FTResponse c24ftResponse = new C24FTResponse("SUCCESS", response.getBody().getBalance(),
					response.getBody().getTranCurrency());

			//// update table
			ipsDao.updateTranCBS(sysTraceAuditNumber, acctNumber, trAmt, "CREDIT", currency, "SUCCESS", "",
					SeqUniqueID,"SYSTEM",settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));

			return new ResponseEntity<>(c24ftResponse, HttpStatus.OK);

		} catch (HttpClientErrorException ex) {
			logger.info("HttpClientErrorException --------->");
			logger.info(ex.getLocalizedMessage());
			logger.error(ex.getLocalizedMessage());

			if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", SERVER_ERROR_CODE,
						Collections.singletonList(TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString()));

				//// update table
				ipsDao.updateTranCBS(sysTraceAuditNumber, acctNumber, trAmt, "CREDIT", currency, "FAILURE",
						TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(), SeqUniqueID,"SYSTEM",settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));
				return new ResponseEntity<>(c24ftResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				C24FTResponse c24ftResponse1 = new Gson().fromJson(ex.getResponseBodyAsString(), C24FTResponse.class);
				C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", c24ftResponse1.getError(),
						c24ftResponse1.getError_desc());

				//// update table
				ipsDao.updateTranCBS(sysTraceAuditNumber, acctNumber, trAmt, "CREDIT", currency, "FAILURE",
						c24ftResponse1.getError_desc().get(0), SeqUniqueID,"SYSTEM",settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));

				return new ResponseEntity<>(c24ftResponse, ex.getStatusCode());
			}

		} catch (HttpServerErrorException ex) {
			logger.info("HttpServerErrorException --------->");
			logger.info(ex.getLocalizedMessage());
			logger.error(ex.getLocalizedMessage());

			C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", SERVER_ERROR_CODE,
					Collections.singletonList(TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString()));

			//// update table
			ipsDao.updateTranCBS(sysTraceAuditNumber, acctNumber, trAmt, "CREDIT", currency, "FAILURE",
					TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(), SeqUniqueID,"SYSTEM",settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));

			return new ResponseEntity<>(c24ftResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	public void dbtReverseFundRequest(String acctNumber, String sysTraceAuditNumber, String currencyCode, String trAmt,
			String seqUniqueID,String trRmks) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.set("Partipant_BIC", env.getProperty("bob.bankcode"));
		httpHeaders.set("Partipant_SOL", acctNumber.substring(0, 4));
		httpHeaders.set("SYS_TRACE_AUDIT_NUMBER", sysTraceAuditNumber);

		C24FTRequest c24ftRequest = new C24FTRequest();
		C24RequestAcount c24RequestAcount = new C24RequestAcount();
		c24RequestAcount.setAcctNumber(acctNumber);
		c24RequestAcount.setSchmType("");
		c24RequestAcount.setSettlAcctNumber(settlAcctRep.findById(env.getProperty("settl.payable")).get().getAccount_number());
		c24ftRequest.setAccount(c24RequestAcount);
		c24ftRequest.setCurrency_Code(currencyCode);
		c24ftRequest.setPAN("0000000000000000");
		c24ftRequest.setTrRmks(trRmks);
		c24ftRequest.setTrAmt(trAmt);

		HttpEntity<C24FTRequest> entity = new HttpEntity<>(c24ftRequest, httpHeaders);
		
		SettlementAccount settlAcct=settlAcctRep.findById(env.getProperty("settl.payable")).get();

		
		ResponseEntity<C24FTResponse> response = null;
		
		ipsDao.updateINOUT(seqUniqueID,"CBS_REVERSE_IN");
		try {
			logger.info("Sending message to connect24ReversaL using restTemplate");
			response = restTemplate.postForEntity(
					env.getProperty("connect24.url") + "/api/ws/dbtActReversefndTransfer?", entity,
					C24FTResponse.class);

			//// update table
			ipsDao.updateTranCBS(sysTraceAuditNumber, acctNumber, trAmt, "DEBIT_REVERSE", currencyCode, "SUCCESS", "",
					seqUniqueID,"SYSTEM",settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));
			
			ipsDao.updateINOUT(seqUniqueID,"CBS_REVERSE_OUT");

			if (response.getStatusCode() == HttpStatus.OK) {
				///// Register Table
				ipsDao.updateCBSStatus(seqUniqueID, TranMonitorStatus.CBS_DEBIT_REVERSE_OK.toString(),
						TranMonitorStatus.FAILURE.toString());
			} else {
				ipsDao.updateCBSStatusError(seqUniqueID, TranMonitorStatus.CBS_DEBIT_REVERSE_ERROR.toString(),
						response.getBody().getError_desc().get(0).toString(),
						TranMonitorStatus.REVERSE_FAILURE.toString());
			}

		} catch (HttpClientErrorException ex) {
			logger.info("HttpClientErrorException --------->");
			logger.info(ex.getLocalizedMessage());
			logger.error(ex.getLocalizedMessage());
			if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				ipsDao.updateCBSStatusError(seqUniqueID, TranMonitorStatus.CBS_DEBIT_REVERSE_ERROR.toString(),
						TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(),
						TranMonitorStatus.REVERSE_FAILURE.toString());

				ipsDao.updateTranCBS(sysTraceAuditNumber, acctNumber, trAmt, "DEBIT_REVERSE", currencyCode, "FAILURE",
						TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(), seqUniqueID,"SYSTEM",settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));
				
				ipsDao.updateINOUT(seqUniqueID,"CBS_REVERSE_OUT");

			} else {
				C24FTResponse c24ftResponse1 = new Gson().fromJson(ex.getResponseBodyAsString(), C24FTResponse.class);
				C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", c24ftResponse1.getError(),
						c24ftResponse1.getError_desc());
				ipsDao.updateCBSStatusError(seqUniqueID, TranMonitorStatus.CBS_DEBIT_REVERSE_ERROR.toString(),
						c24ftResponse.getError_desc().get(0).toString(), TranMonitorStatus.REVERSE_FAILURE.toString());
				ipsDao.updateTranCBS(sysTraceAuditNumber, acctNumber, trAmt, "DEBIT_REVERSE", currencyCode, "FAILURE",
						c24ftResponse.getError_desc().get(0).toString(), seqUniqueID,"SYSTEM",settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));
				
				ipsDao.updateINOUT(seqUniqueID,"CBS_REVERSE_OUT");
			}

		} catch (HttpServerErrorException ex) {

			logger.info("HttpServerErrorException --------->");
			logger.info(ex.getLocalizedMessage());
			logger.error(ex.getLocalizedMessage());

			ipsDao.updateCBSStatusError(seqUniqueID, TranMonitorStatus.CBS_DEBIT_REVERSE_ERROR.toString(),
					TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(),
					TranMonitorStatus.REVERSE_FAILURE.toString());

			ipsDao.updateTranCBS(sysTraceAuditNumber, acctNumber, trAmt, "DEBIT_REVERSE", currencyCode, "FAILURE",
					TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(), seqUniqueID,"SYSTEM",settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));
			
			ipsDao.updateINOUT(seqUniqueID,"CBS_REVERSE_OUT");
		}
	}

	public void cdtReverseFundRequest(String acctNumber, String trAmt, String currency, String sysTraceAuditNumber,
			String seqUniqueID,String trRmks) {

		ipsDao.updateCBSStatus(seqUniqueID, TranMonitorStatus.CBS_CREDIT_REVERSE_INITIATED.toString(),
				TranMonitorStatus.IN_PROGRESS.toString());

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.set("Partipant_BIC", env.getProperty("bob.bankcode"));
		httpHeaders.set("Partipant_SOL", acctNumber.substring(0, 4));
		httpHeaders.set("SYS_TRACE_AUDIT_NUMBER", sysTraceAuditNumber);

		C24FTRequest c24ftRequest = new C24FTRequest();
		C24RequestAcount c24RequestAcount = new C24RequestAcount();
		c24RequestAcount.setAcctNumber(acctNumber);
		c24RequestAcount.setSchmType("");
		c24RequestAcount.setSettlAcctNumber(settlAcctRep.findById(env.getProperty("settl.receivable")).get().getAccount_number());
		c24ftRequest.setAccount(c24RequestAcount);
		c24ftRequest.setCurrency_Code(currency);
		c24ftRequest.setPAN("0000000000000000");
		c24ftRequest.setTrAmt(trAmt);
		c24ftRequest.setTrRmks(trRmks);
		HttpEntity<C24FTRequest> entity = new HttpEntity<>(c24ftRequest, httpHeaders);
		
		SettlementAccount settlAcct=settlAcctRep.findById(env.getProperty("settl.receivable")).get();

		
		ResponseEntity<C24FTResponse> response = null;
		try {
			logger.info("Sending message to connect24ReversaL");
			response = restTemplate.postForEntity(
					env.getProperty("connect24.url") + "/api/ws/cdtActReversefndTransfer?", entity,
					C24FTResponse.class);

			ipsDao.updateTranCBS(sysTraceAuditNumber, acctNumber, trAmt, "CREDIT_REVERSE", currency, "SUCCESS", "",
					seqUniqueID,"SYSTEM",settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));

			if (response.getStatusCode() == HttpStatus.OK) {

				ipsDao.updateCBSStatus(seqUniqueID, TranMonitorStatus.CBS_CREDIT_REVERSE_OK.toString(),
						TranMonitorStatus.FAILURE.toString());

			} else {
				ipsDao.updateCBSStatusError(seqUniqueID, TranMonitorStatus.CBS_CREDIT_REVERSE_ERROR.toString(),
						response.getBody().getError_desc().get(0).toString(),
						TranMonitorStatus.REVERSE_FAILURE.toString());

			}

		} catch (HttpClientErrorException ex) {

			logger.info("HttpClientErrorException --------->");
			logger.info(ex.getLocalizedMessage());
			logger.error(ex.getLocalizedMessage());

			if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				ipsDao.updateCBSStatusError(seqUniqueID, TranMonitorStatus.CBS_CREDIT_REVERSE_ERROR.toString(),
						TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(),
						TranMonitorStatus.REVERSE_FAILURE.toString());

				ipsDao.updateTranCBS(sysTraceAuditNumber, acctNumber, trAmt, "CREDIT_REVERSE", currency, "FAILURE",
						TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(), seqUniqueID,"SYSTEM",settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));
			} else {
				C24FTResponse c24ftResponse1 = new Gson().fromJson(ex.getResponseBodyAsString(), C24FTResponse.class);
				C24FTResponse c24ftResponse = new C24FTResponse("Failure", c24ftResponse1.getError(),
						c24ftResponse1.getError_desc());
				ipsDao.updateCBSStatusError(seqUniqueID, TranMonitorStatus.CBS_CREDIT_REVERSE_ERROR.toString(),
						c24ftResponse.getError_desc().get(0).toString(), TranMonitorStatus.REVERSE_FAILURE.toString());

				ipsDao.updateTranCBS(sysTraceAuditNumber, acctNumber, trAmt, "CREDIT_REVERSE", currency, "FAILURE",
						c24ftResponse.getError_desc().get(0).toString(), seqUniqueID,"SYSTEM",settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));
			}

		} catch (HttpServerErrorException ex) {

			logger.info("HttpServerErrorException --------->");
			logger.info(ex.getLocalizedMessage());
			logger.error(ex.getLocalizedMessage());

			ipsDao.updateCBSStatusError(seqUniqueID, TranMonitorStatus.CBS_CREDIT_REVERSE_ERROR.toString(),
					TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(),
					TranMonitorStatus.REVERSE_FAILURE.toString());

			ipsDao.updateTranCBS(sysTraceAuditNumber, acctNumber, trAmt, "CREDIT_REVERSE", currency, "FAILURE",
					TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(), seqUniqueID,"SYSTEM",settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));
		}

	}

	public ResponseEntity<C24FTResponse> checkAccountExist(String psuDeviceID, String ipAddress, String psuID,
			String acctNumber) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.set("Partipant_BIC", env.getProperty("bob.bankcode"));
		httpHeaders.set("Partipant_SOL", acctNumber.substring(0, 4));

		C24FTRequest c24ftRequest = new C24FTRequest();
		C24RequestAcount c24RequestAcount = new C24RequestAcount();
		c24RequestAcount.setAcctNumber(acctNumber);
		c24RequestAcount.setSchmType("");
		c24ftRequest.setAccount(c24RequestAcount);
		c24ftRequest.setCurrency_Code(env.getProperty("bob.crncycode"));
		c24ftRequest.setPAN("0000000000000000");

		HttpEntity<C24FTRequest> entity = new HttpEntity<>(c24ftRequest, httpHeaders);
		

		
		ResponseEntity<C24FTResponse> response = null;
		try {
			logger.info("Sending message to connect24 ");
			response = restTemplate.postForEntity(env.getProperty("connect24.url") + "/api/ws/balances?", entity,
					C24FTResponse.class);
		} catch (HttpClientErrorException ex) {

			logger.info("HttpClientErrorException --------->");
			logger.info(ex.getLocalizedMessage());
			logger.error(ex.getLocalizedMessage());

			if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", SERVER_ERROR_CODE,
						Collections.singletonList(TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString()));
				return new ResponseEntity<>(c24ftResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				C24FTResponse c24ftResponse1 = new Gson().fromJson(ex.getResponseBodyAsString(), C24FTResponse.class);
				C24FTResponse c24ftResponse = new C24FTResponse("Failure", c24ftResponse1.getError(),
						c24ftResponse1.getError_desc());
				return new ResponseEntity<>(c24ftResponse, ex.getStatusCode());
			}

		} catch (HttpServerErrorException ex) {
			logger.info("HttpServerErrorException --------->");
			logger.info(ex.getLocalizedMessage());
			logger.error(ex.getLocalizedMessage());

			C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", SERVER_ERROR_CODE,
					Collections.singletonList(TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString()));
			return new ResponseEntity<>(c24ftResponse, HttpStatus.INTERNAL_SERVER_ERROR);

		}

		return response;
	}
	
	public ResponseEntity<C24FTResponse> getBalance(
			String acctNumber) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.set("Partipant_BIC", env.getProperty("bob.bankcode"));
		httpHeaders.set("Partipant_SOL", acctNumber.substring(0, 4));

		C24FTRequest c24ftRequest = new C24FTRequest();
		C24RequestAcount c24RequestAcount = new C24RequestAcount();
		c24RequestAcount.setAcctNumber(acctNumber);
		c24RequestAcount.setSchmType("");
		c24ftRequest.setAccount(c24RequestAcount);
		c24ftRequest.setCurrency_Code(env.getProperty("bob.crncycode"));
		c24ftRequest.setPAN("0000000000000000");

		HttpEntity<C24FTRequest> entity = new HttpEntity<>(c24ftRequest, httpHeaders);
		ResponseEntity<C24FTResponse> response = null;
		try {
			logger.info("Sending message to connect24 ");
			response = restTemplate.postForEntity(env.getProperty("connect24.url") + "/api/ws/balances?", entity,
					C24FTResponse.class);
		} catch (HttpClientErrorException ex) {

			logger.info("HttpClientErrorException --------->");
			logger.info(ex.getLocalizedMessage());
			logger.error(ex.getLocalizedMessage());

			if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", SERVER_ERROR_CODE,
						Collections.singletonList(TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString()));
				return new ResponseEntity<>(c24ftResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				C24FTResponse c24ftResponse1 = new Gson().fromJson(ex.getResponseBodyAsString(), C24FTResponse.class);
				C24FTResponse c24ftResponse = new C24FTResponse("Failure", c24ftResponse1.getError(),
						c24ftResponse1.getError_desc());
				return new ResponseEntity<>(c24ftResponse, ex.getStatusCode());
			}

		} catch (HttpServerErrorException ex) {
			logger.info("HttpServerErrorException --------->");
			logger.info(ex.getLocalizedMessage());
			logger.error(ex.getLocalizedMessage());

			C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", SERVER_ERROR_CODE,
					Collections.singletonList(TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString()));
			return new ResponseEntity<>(c24ftResponse, HttpStatus.INTERNAL_SERVER_ERROR);

		}

		return response;
	}

	public ResponseEntity<C24FTResponse> rtpFundRequest(String sysTraceAuditNumber, String acctNumber, String ccy,
			String trAmt, String seqUniqueID,String trRmks) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.set("Partipant_BIC", env.getProperty("bob.bankcode"));
		httpHeaders.set("Partipant_SOL", acctNumber.substring(0, 4));
		httpHeaders.set("SYS_TRACE_AUDIT_NUMBER", sysTraceAuditNumber);

		C24FTRequest c24ftRequest = new C24FTRequest();
		C24RequestAcount c24RequestAcount = new C24RequestAcount();
		c24RequestAcount.setAcctNumber(acctNumber);
		c24RequestAcount.setSettlAcctNumber(settlAcctRep.findById(env.getProperty("settl.payable")).get().getAccount_number());
		c24ftRequest.setAccount(c24RequestAcount);
		c24ftRequest.setCurrency_Code(ccy);
		c24ftRequest.setPAN("0000000000000000");
		c24ftRequest.setTrAmt(trAmt);
		c24ftRequest.setTrRmks(trRmks);

		HttpEntity<C24FTRequest> entity = new HttpEntity<>(c24ftRequest, httpHeaders);
		
		SettlementAccount settlAcct=settlAcctRep.findById(env.getProperty("settl.payable")).get();

		
		ResponseEntity<C24FTResponse> response = null;
		try {
			logger.info("Sending message to connect24 ");
			response = restTemplate.postForEntity(env.getProperty("connect24.url") + "/api/ws/dbtActfndTransfer?",
					entity, C24FTResponse.class);

			//// update table
			ipsDao.updateTranCBS(sysTraceAuditNumber, acctNumber, trAmt, "DEBIT", ccy, "SUCCESS", "", seqUniqueID,"SYSTEM",
					settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));

		} catch (HttpClientErrorException ex) {
			logger.info("HttpClientErrorException --------->");
			logger.info(ex.getLocalizedMessage());
			logger.error(ex.getLocalizedMessage());

			if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", SERVER_ERROR_CODE,
						Collections.singletonList(TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString()));

				//// update table
				ipsDao.updateTranCBS(sysTraceAuditNumber, acctNumber, trAmt, "DEBIT", ccy, "FAILURE",
						TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(), seqUniqueID,"SYSTEM",settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));

				return new ResponseEntity<>(c24ftResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				C24FTResponse c24ftResponse1 = new Gson().fromJson(ex.getResponseBodyAsString(), C24FTResponse.class);
				C24FTResponse c24ftResponse = new C24FTResponse("Failure", c24ftResponse1.getError(),
						c24ftResponse1.getError_desc());

				//// update table
				ipsDao.updateTranCBS(sysTraceAuditNumber, acctNumber, trAmt, "DEBIT", ccy, "FAILURE",
						c24ftResponse1.getError_desc().get(0), seqUniqueID,"SYSTEM",settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));
				return new ResponseEntity<>(c24ftResponse, ex.getStatusCode());
			}

		} catch (HttpServerErrorException ex) {
			logger.info("HttpServerErrorException --------->");
			logger.info(ex.getLocalizedMessage());
			logger.error(ex.getLocalizedMessage());

			C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", SERVER_ERROR_CODE,
					Collections.singletonList(TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString()));
			//// update table
			ipsDao.updateTranCBS(sysTraceAuditNumber, acctNumber, trAmt, "DEBIT", ccy, "FAILURE",
					TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(), seqUniqueID,"SYSTEM",settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));

			return new ResponseEntity<>(c24ftResponse, HttpStatus.INTERNAL_SERVER_ERROR);

		}

		return response;
	}

	public ResponseEntity<C24FTResponse> rtpReverseFundRequest(String acctNumber, String sysTraceAuditNumber,
			String ccy, String trAmt, String seqUniqueID,String trRmks) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.set("Partipant_BIC", env.getProperty("bob.bankcode"));
		httpHeaders.set("Partipant_SOL", acctNumber.substring(0, 4));
		httpHeaders.set("SYS_TRACE_AUDIT_NUMBER", sysTraceAuditNumber);

		C24FTRequest c24ftRequest = new C24FTRequest();
		C24RequestAcount c24RequestAcount = new C24RequestAcount();
		c24RequestAcount.setAcctNumber(acctNumber);
		c24RequestAcount.setSettlAcctNumber(settlAcctRep.findById(env.getProperty("settl.payable")).get().getAccount_number());
		c24ftRequest.setAccount(c24RequestAcount);
		c24ftRequest.setCurrency_Code(ccy);
		c24ftRequest.setPAN("0000000000000000");
		c24ftRequest.setTrRmks(trRmks);
		c24ftRequest.setTrAmt(trAmt);

		HttpEntity<C24FTRequest> entity = new HttpEntity<>(c24ftRequest, httpHeaders);
		
		SettlementAccount settlAcct=settlAcctRep.findById(env.getProperty("settl.payable")).get();

		
		ResponseEntity<C24FTResponse> response = null;
		try {
			logger.info("Sending message to connect24 ");
			response = restTemplate.postForEntity(
					env.getProperty("connect24.url") + "/api/ws/dbtActReversefndTransfer?", entity,
					C24FTResponse.class);

			ipsDao.updateTranCBS(sysTraceAuditNumber, acctNumber, trAmt, "DEBIT_REVERSE", ccy, "SUCCESS", "",
					seqUniqueID,"SYSTEM",settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));

			if (response.getStatusCode() == HttpStatus.OK) {
				///// Register Table
				ipsDao.updateCBSStatus(seqUniqueID, TranMonitorStatus.CBS_DEBIT_REVERSE_OK.toString(),
						TranMonitorStatus.FAILURE.toString());
			} else {
				ipsDao.updateCBSStatusError(seqUniqueID, TranMonitorStatus.CBS_DEBIT_REVERSE_ERROR.toString(),
						response.getBody().getError_desc().get(0).toString(),
						TranMonitorStatus.REVERSE_FAILURE.toString());
			}

		} catch (HttpClientErrorException ex) {

			logger.info("HttpClientErrorException --------->");
			logger.info(ex.getLocalizedMessage());
			logger.error(ex.getLocalizedMessage());

			if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", SERVER_ERROR_CODE,
						Collections.singletonList(TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString()));

				ipsDao.updateTranCBS(sysTraceAuditNumber, acctNumber, trAmt, "DEBIT_REVERSE", ccy, "FAILURE",
						TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(), seqUniqueID,"SYSTEM",settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));

				return new ResponseEntity<>(c24ftResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				C24FTResponse c24ftResponse1 = new Gson().fromJson(ex.getResponseBodyAsString(), C24FTResponse.class);
				C24FTResponse c24ftResponse = new C24FTResponse("Failure", c24ftResponse1.getError(),
						c24ftResponse1.getError_desc());
				ipsDao.updateCBSStatusError(seqUniqueID, TranMonitorStatus.CBS_DEBIT_REVERSE_ERROR.toString(),
						c24ftResponse.getError_desc().get(0).toString(), TranMonitorStatus.REVERSE_FAILURE.toString());

				ipsDao.updateTranCBS(sysTraceAuditNumber, acctNumber, trAmt, "DEBIT_REVERSE", ccy, "FAILURE",
						c24ftResponse.getError_desc().get(0).toString(), seqUniqueID,"SYSTEM",settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));

				return new ResponseEntity<>(c24ftResponse, ex.getStatusCode());
			}

		} catch (HttpServerErrorException ex) {
			logger.info("HttpServerErrorException --------->");
			logger.info(ex.getLocalizedMessage());
			logger.error(ex.getLocalizedMessage());

			C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", SERVER_ERROR_CODE,
					Collections.singletonList(TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString()));

			ipsDao.updateTranCBS(sysTraceAuditNumber, acctNumber, trAmt, "DEBIT_REVERSE", ccy, "FAILURE",
					TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(), seqUniqueID,"SYSTEM",settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));

			return new ResponseEntity<>(c24ftResponse, HttpStatus.INTERNAL_SERVER_ERROR);

		}

		return response;
	}

	public ResponseEntity<AccountContactResponse> getAccountContact(String psuDeviceID, String ipAddress, String psuID,
			String acctNumber) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.set("Partipant_BIC", env.getProperty("bob.bankcode"));
		httpHeaders.set("Partipant_SOL", acctNumber.substring(0, 4));

		C24FTRequest c24ftRequest = new C24FTRequest();
		C24RequestAcount c24RequestAcount = new C24RequestAcount();
		c24RequestAcount.setAcctNumber(acctNumber);
		c24RequestAcount.setSchmType("");
		c24ftRequest.setAccount(c24RequestAcount);
		c24ftRequest.setCurrency_Code(env.getProperty("bob.crncycode"));
		c24ftRequest.setPAN("0000000000000000");

		HttpEntity<C24FTRequest> entity = new HttpEntity<>(c24ftRequest, httpHeaders);
		ResponseEntity<AccountContactResponse> response = null;
		try {
			logger.info("Sending message to connect24 ");
			response = restTemplate.postForEntity(env.getProperty("connect24.url") + "/api/ws/getContact?", entity,
					AccountContactResponse.class);
		} catch (HttpClientErrorException ex) {

			logger.info("HttpClientErrorException --------->");
			logger.info(ex.getLocalizedMessage());
			logger.error(ex.getLocalizedMessage());

			if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				AccountContactResponse contactResponse = new AccountContactResponse("FAILURE", SERVER_ERROR_CODE,
						Collections.singletonList(TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString()));
				return new ResponseEntity<>(contactResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				AccountContactResponse contactResponse1 = new Gson().fromJson(ex.getResponseBodyAsString(),
						AccountContactResponse.class);
				AccountContactResponse contactResponse = new AccountContactResponse("Failure",
						contactResponse1.getError(), contactResponse1.getError_desc());
				return new ResponseEntity<>(contactResponse, ex.getStatusCode());
			}

		} catch (HttpServerErrorException ex) {
			logger.info("HttpServerErrorException --------->");
			logger.info(ex.getLocalizedMessage());
			logger.error(ex.getLocalizedMessage());

			AccountContactResponse contactResponse = new AccountContactResponse("FAILURE", SERVER_ERROR_CODE,
					Collections.singletonList(TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString()));
			return new ResponseEntity<>(contactResponse, HttpStatus.INTERNAL_SERVER_ERROR);

		}

		return response;

	}

	public ResponseEntity<C24FTResponse> charge(String acctNumber, String sysTraceAuditNumber)
			throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException,
			IOException, KeyManagementException, UnrecoverableKeyException {

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.set("Partipant_BIC", env.getProperty("bob.bankcode"));
		httpHeaders.set("Partipant_SOL", acctNumber.substring(0, 4));
		httpHeaders.set("SYS_TRACE_AUDIT_NUMBER", sysTraceAuditNumber);

		C24FTRequest c24ftRequest = new C24FTRequest();
		C24RequestAcount c24RequestAcount = new C24RequestAcount();
		c24RequestAcount.setAcctNumber(acctNumber);
		c24RequestAcount.setSchmType("");
		c24ftRequest.setAccount(c24RequestAcount);
		c24ftRequest.setCurrency_Code(env.getProperty("bob.crncycode"));
		c24ftRequest.setPAN("0000000000000000");
		c24ftRequest.setTrAmt("2.00");

		HttpEntity<C24FTRequest> entity = new HttpEntity<>(c24ftRequest, httpHeaders);
		ResponseEntity<C24FTResponse> response = null;

		try {
			logger.info("Sending message to connect24 ");
			response = restTemplate.postForEntity(env.getProperty("connect24.url") + "/api/ws/chargeDbtFund?", entity,
					C24FTResponse.class);
			C24FTResponse c24ftResponse = new C24FTResponse("SUCCESS", response.getBody().getBalance(),
					response.getBody().getTranCurrency());
			return new ResponseEntity<>(c24ftResponse, HttpStatus.OK);
		} catch (HttpClientErrorException ex) {

			logger.info("HttpClientErrorException --------->" + ex.getStatusCode());
			logger.info(ex.getLocalizedMessage());
			logger.error(ex.getLocalizedMessage());

			if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", SERVER_ERROR_CODE,
						Collections.singletonList(TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString()));
				return new ResponseEntity<>(c24ftResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				C24FTResponse c24ftResponse1 = new Gson().fromJson(ex.getResponseBodyAsString(), C24FTResponse.class);
				C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", c24ftResponse1.getError(),
						c24ftResponse1.getError_desc());
				return new ResponseEntity<>(c24ftResponse, ex.getStatusCode());
			}

		} catch (HttpServerErrorException ex) {
			logger.info("HttpServerErrorException --------->");
			logger.info(ex.getLocalizedMessage());
			logger.error(ex.getLocalizedMessage());

			C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", SERVER_ERROR_CODE,
					Collections.singletonList(TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString()));
			return new ResponseEntity<>(c24ftResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	public ResponseEntity<C24FTResponse> BulkCreditRequest(String senderParticipantBIC, String participantSOL,
			BukCreditTransferRequest mcCreditTransferRequest, String sysTraceAuditNumber, String seqUniqueID,String trRmks)
			 {

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.set("Partipant_BIC", env.getProperty("bob.bankcode"));
		httpHeaders.set("Partipant_SOL", mcCreditTransferRequest.getFrAccount().get(0).getAcctNumber().substring(0,4));
		httpHeaders.set("SYS_TRACE_AUDIT_NUMBER", sysTraceAuditNumber);

		C24FTRequest c24ftRequest = new C24FTRequest();
		C24RequestAcount c24RequestAcount = new C24RequestAcount();
		c24RequestAcount.setAcctNumber(mcCreditTransferRequest.getFrAccount().get(0).getAcctNumber());
		c24RequestAcount.setSchmType("");
		c24RequestAcount.setSettlAcctNumber(settlAcctRep.findById(env.getProperty("settl.payable")).get().getAccount_number());
		c24ftRequest.setAccount(c24RequestAcount);
		c24ftRequest.setCurrency_Code(mcCreditTransferRequest.getCurrencyCode());
		c24ftRequest.setPAN("");
		c24ftRequest.setTrAmt(mcCreditTransferRequest.getTotAmt());
		c24ftRequest.setTrRmks(trRmks);

		HttpEntity<C24FTRequest> entity = new HttpEntity<>(c24ftRequest, httpHeaders);
		
		SettlementAccount settlAcct=settlAcctRep.findById(env.getProperty("settl.payable")).get();

		
		ResponseEntity<C24FTResponse> response = null;

		try {
			logger.info("Sending message to connect24 ");
			
			response = restTemplate.postForEntity(env.getProperty("connect24.url") + "/api/ws/dbtActfndTransfer?",
					entity, C24FTResponse.class);
			C24FTResponse c24ftResponse = new C24FTResponse("SUCCESS", response.getBody().getBalance(),
					response.getBody().getTranCurrency());

			//// update table
			ipsDao.updateTranCBS(sysTraceAuditNumber, mcCreditTransferRequest.getFrAccount().get(0).getAcctNumber(),
					mcCreditTransferRequest.getTotAmt(), "BULK_CREDIT", mcCreditTransferRequest.getCurrencyCode(), "SUCCESS",
					"", seqUniqueID,"SYSTEM",settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));

			return new ResponseEntity<>(c24ftResponse, HttpStatus.OK);
		} catch (HttpClientErrorException ex) {

			logger.info("HttpClientErrorException --------->" + ex.getStatusCode());
			logger.info(ex.getLocalizedMessage());
			logger.error(ex.getLocalizedMessage());

			if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				C24FTResponse c24ftResponse = new C24FTResponse("BULK_CREDIT", SERVER_ERROR_CODE,
						Collections.singletonList(TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString()));

				///// Update Table
				ipsDao.updateTranCBS(sysTraceAuditNumber, mcCreditTransferRequest.getFrAccount().get(0).getAcctNumber(),
						mcCreditTransferRequest.getTotAmt(), "BULK_CREDIT", mcCreditTransferRequest.getCurrencyCode(),
						"FAILURE", TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(), seqUniqueID,"SYSTEM",settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));

				return new ResponseEntity<>(c24ftResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				C24FTResponse c24ftResponse1 = new Gson().fromJson(ex.getResponseBodyAsString(), C24FTResponse.class);
				C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", c24ftResponse1.getError(),
						c24ftResponse1.getError_desc());

				//// update table
				ipsDao.updateTranCBS(sysTraceAuditNumber, mcCreditTransferRequest.getFrAccount().get(0).getAcctNumber(),
						mcCreditTransferRequest.getTotAmt(), "BULK_CREDIT", mcCreditTransferRequest.getCurrencyCode(),
						"FAILURE", c24ftResponse1.getError_desc().get(0).toString(), seqUniqueID,"SYSTEM",settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));

				return new ResponseEntity<>(c24ftResponse, ex.getStatusCode());
			}

		} catch (HttpServerErrorException ex) {
			logger.info("HttpServerErrorException --------->");
			logger.info(ex.getLocalizedMessage());
			logger.error(ex.getLocalizedMessage());

			C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", SERVER_ERROR_CODE,
					Collections.singletonList(TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString()));
			ipsDao.updateTranCBS(sysTraceAuditNumber, mcCreditTransferRequest.getFrAccount().get(0).getAcctNumber(),
					mcCreditTransferRequest.getTotAmt(), "BULK_CREDIT", mcCreditTransferRequest.getCurrencyCode(), "FAILURE",
					TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(), seqUniqueID,"SYSTEM",settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));

			return new ResponseEntity<>(c24ftResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	public void dbtReverseBulkCreditFundRequest(String acctNumber, String sysTraceAuditNumber, String currencyCode, String trAmt,
			String seqUniqueID,String trRmks) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.set("Partipant_BIC", env.getProperty("bob.bankcode"));
		httpHeaders.set("Partipant_SOL", acctNumber.substring(0, 4));
		httpHeaders.set("SYS_TRACE_AUDIT_NUMBER", sysTraceAuditNumber);

		C24FTRequest c24ftRequest = new C24FTRequest();
		C24RequestAcount c24RequestAcount = new C24RequestAcount();
		c24RequestAcount.setAcctNumber(acctNumber);
		c24RequestAcount.setSchmType("");
		c24RequestAcount.setSettlAcctNumber(settlAcctRep.findById(env.getProperty("settl.payable")).get().getAccount_number());
		c24ftRequest.setAccount(c24RequestAcount);
		c24ftRequest.setCurrency_Code(currencyCode);
		c24ftRequest.setPAN("0000000000000000");
		c24ftRequest.setTrRmks(trRmks);
		c24ftRequest.setTrAmt(trAmt);

		HttpEntity<C24FTRequest> entity = new HttpEntity<>(c24ftRequest, httpHeaders);
		
		SettlementAccount settlAcct=settlAcctRep.findById(env.getProperty("settl.payable")).get();

		ResponseEntity<C24FTResponse> response = null;
		try {
			logger.info("Sending message to connect24ReversaL using restTemplate");
			response = restTemplate.postForEntity(
					env.getProperty("connect24.url") + "/api/ws/dbtActReversefndTransfer?", entity,
					C24FTResponse.class);

			//// update table
			ipsDao.updateTranCBS(sysTraceAuditNumber, acctNumber, trAmt, "BULK_CREDIT_REVERSE", currencyCode, "SUCCESS", "",
					seqUniqueID.split("/")[0],"SYSTEM",settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));

			if (response.getStatusCode() == HttpStatus.OK) {
				///// Register Table
				ipsDao.updateCBSStatus(seqUniqueID, TranMonitorStatus.CBS_DEBIT_REVERSE_OK.toString(),
						TranMonitorStatus.FAILURE.toString());
			} else {
				ipsDao.updateCBSStatusError(seqUniqueID, TranMonitorStatus.CBS_DEBIT_REVERSE_ERROR.toString(),
						response.getBody().getError_desc().get(0).toString(),
						TranMonitorStatus.REVERSE_FAILURE.toString());
			}

		} catch (HttpClientErrorException ex) {
			logger.info("HttpClientErrorException --------->");
			logger.info(ex.getLocalizedMessage());
			logger.error(ex.getLocalizedMessage());
			if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				ipsDao.updateCBSStatusError(seqUniqueID, TranMonitorStatus.CBS_DEBIT_REVERSE_ERROR.toString(),
						TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(),
						TranMonitorStatus.REVERSE_FAILURE.toString());

				ipsDao.updateTranCBS(sysTraceAuditNumber, acctNumber, trAmt, "BULK_CREDIT_REVERSE", currencyCode, "FAILURE",
						TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(), seqUniqueID.split("/")[0],"SYSTEM",settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));

			} else {
				C24FTResponse c24ftResponse1 = new Gson().fromJson(ex.getResponseBodyAsString(), C24FTResponse.class);
				C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", c24ftResponse1.getError(),
						c24ftResponse1.getError_desc());
				ipsDao.updateCBSStatusError(seqUniqueID, TranMonitorStatus.CBS_DEBIT_REVERSE_ERROR.toString(),
						c24ftResponse.getError_desc().get(0).toString(), TranMonitorStatus.REVERSE_FAILURE.toString());
				ipsDao.updateTranCBS(sysTraceAuditNumber, acctNumber, trAmt, "BULK_CREDIT_REVERSE", currencyCode, "FAILURE",
						c24ftResponse.getError_desc().get(0).toString(), seqUniqueID.split("/")[0],"SYSTEM",settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));
			}

		} catch (HttpServerErrorException ex) {

			logger.info("HttpServerErrorException --------->");
			logger.info(ex.getLocalizedMessage());
			logger.error(ex.getLocalizedMessage());

			ipsDao.updateCBSStatusError(seqUniqueID, TranMonitorStatus.CBS_DEBIT_REVERSE_ERROR.toString(),
					TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(),
					TranMonitorStatus.REVERSE_FAILURE.toString());

			ipsDao.updateTranCBS(sysTraceAuditNumber, acctNumber, trAmt, "BULK_CREDIT_REVERSE", currencyCode, "FAILURE",
					TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(), seqUniqueID.split("/")[0],"SYSTEM",settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));
		}
	}


	public ResponseEntity<C24FTResponse> ManualdbtReverseBulkCreditFundRequest(String acctNumber, String sysTraceAuditNumber, String currencyCode, String trAmt,
			String seqUniqueID,String userID,String trRmks) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.set("Partipant_BIC", env.getProperty("bob.bankcode"));
		httpHeaders.set("Partipant_SOL", acctNumber.substring(0, 4));
		httpHeaders.set("SYS_TRACE_AUDIT_NUMBER", sysTraceAuditNumber);

		C24FTRequest c24ftRequest = new C24FTRequest();
		C24RequestAcount c24RequestAcount = new C24RequestAcount();
		c24RequestAcount.setAcctNumber(acctNumber);
		c24RequestAcount.setSchmType("");
		c24RequestAcount.setSettlAcctNumber(settlAcctRep.findById(env.getProperty("settl.payable")).get().getAccount_number());
		c24ftRequest.setAccount(c24RequestAcount);
		c24ftRequest.setCurrency_Code(currencyCode);
		c24ftRequest.setPAN("0000000000000000");
		c24ftRequest.setTrAmt(trAmt);
		c24ftRequest.setTrRmks(trRmks);

		HttpEntity<C24FTRequest> entity = new HttpEntity<>(c24ftRequest, httpHeaders);
		SettlementAccount settlAcct=settlAcctRep.findById(env.getProperty("settl.payable")).get();

		ResponseEntity<C24FTResponse> response = null;
		try {
			logger.info("Sending message to connect24ReversaL using restTemplate");
			response = restTemplate.postForEntity(
					env.getProperty("connect24.url") + "/api/ws/dbtActReversefndTransfer?", entity,
					C24FTResponse.class);

			//// update table
			ipsDao.updateTranCBS(sysTraceAuditNumber, acctNumber, trAmt, "BULK_CREDIT_REVERSE", currencyCode, "SUCCESS", "",
					seqUniqueID.split("/")[0],userID,settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));

			if (response.getStatusCode() == HttpStatus.OK) {
				
				C24FTResponse c24ftResponse = new C24FTResponse("SUCCESS", response.getBody().getBalance(),
						response.getBody().getTranCurrency());
				
				///// Register Table
				ipsDao.updateCBSStatusManual(seqUniqueID, TranMonitorStatus.CBS_DEBIT_REVERSE_OK.toString(),
						TranMonitorStatus.FAILURE.toString());
				return new ResponseEntity<>(c24ftResponse, HttpStatus.OK);
			} else {
				ipsDao.updateCBSStatusErrorManual(seqUniqueID, TranMonitorStatus.CBS_DEBIT_REVERSE_ERROR.toString(),
						response.getBody().getError_desc().get(0).toString(),
						TranMonitorStatus.REVERSE_FAILURE.toString());
				
				C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", SERVER_ERROR_CODE,
						Collections.singletonList(TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString()));
				
				return new ResponseEntity<>(c24ftResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			}

		} catch (HttpClientErrorException ex) {
			logger.info("HttpClientErrorException --------->");
			logger.info(ex.getLocalizedMessage());
			logger.error(ex.getLocalizedMessage());
			if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				ipsDao.updateCBSStatusErrorManual(seqUniqueID, TranMonitorStatus.CBS_DEBIT_REVERSE_ERROR.toString(),
						TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(),
						TranMonitorStatus.REVERSE_FAILURE.toString());

				ipsDao.updateTranCBS(sysTraceAuditNumber, acctNumber, trAmt, "BULK_CREDIT_REVERSE", currencyCode, "FAILURE",
						TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(), seqUniqueID.split("/")[0],userID,settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));
				
				C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", SERVER_ERROR_CODE,
						Collections.singletonList(TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString()));
				
				ipsDao.updateINOUT(seqUniqueID,"CBS_REVERSE_OUT");
				
				return new ResponseEntity<>(c24ftResponse, HttpStatus.INTERNAL_SERVER_ERROR);

			} else {
				C24FTResponse c24ftResponse1 = new Gson().fromJson(ex.getResponseBodyAsString(), C24FTResponse.class);
				C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", c24ftResponse1.getError(),
						c24ftResponse1.getError_desc());
				ipsDao.updateCBSStatusErrorManual(seqUniqueID, TranMonitorStatus.CBS_DEBIT_REVERSE_ERROR.toString(),
						c24ftResponse.getError_desc().get(0).toString(), TranMonitorStatus.REVERSE_FAILURE.toString());
				ipsDao.updateTranCBS(sysTraceAuditNumber, acctNumber, trAmt, "BULK_CREDIT_REVERSE", currencyCode, "FAILURE",
						c24ftResponse.getError_desc().get(0).toString(), seqUniqueID.split("/")[0],userID,settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));
				
				ipsDao.updateINOUT(seqUniqueID,"CBS_REVERSE_OUT");
				
				return new ResponseEntity<>(c24ftResponse, ex.getStatusCode());
			}

		} catch (HttpServerErrorException ex) {

			logger.info("HttpServerErrorException --------->");
			logger.info(ex.getLocalizedMessage());
			logger.error(ex.getLocalizedMessage());

			ipsDao.updateCBSStatusErrorManual(seqUniqueID, TranMonitorStatus.CBS_DEBIT_REVERSE_ERROR.toString(),
					TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(),
					TranMonitorStatus.REVERSE_FAILURE.toString());

			ipsDao.updateTranCBS(sysTraceAuditNumber, acctNumber, trAmt, "BULK_CREDIT_REVERSE", currencyCode, "FAILURE",
					TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(), seqUniqueID.split("/")[0],userID,settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));
			
			C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", SERVER_ERROR_CODE,
					Collections.singletonList(TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString()));
			
			
			ipsDao.updateINOUT(seqUniqueID,"CBS_REVERSE_OUT");
			
			return new ResponseEntity<>(c24ftResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	public ResponseEntity<C24FTResponse> ManualdbtReverseFundRequest(String acctNumber, String sysTraceAuditNumber, String currencyCode, String trAmt,
			String seqUniqueID,String userID,String trRmks) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.set("Partipant_BIC", env.getProperty("bob.bankcode"));
		httpHeaders.set("Partipant_SOL", acctNumber.substring(0, 4));
		httpHeaders.set("SYS_TRACE_AUDIT_NUMBER", sysTraceAuditNumber);

		C24FTRequest c24ftRequest = new C24FTRequest();
		C24RequestAcount c24RequestAcount = new C24RequestAcount();
		c24RequestAcount.setAcctNumber(acctNumber);
		c24RequestAcount.setSchmType("");
		c24RequestAcount.setSettlAcctNumber(settlAcctRep.findById(env.getProperty("settl.payable")).get().getAccount_number());
		c24ftRequest.setAccount(c24RequestAcount);
		c24ftRequest.setCurrency_Code(currencyCode);
		c24ftRequest.setPAN("0000000000000000");
		c24ftRequest.setTrAmt(trAmt);
		c24ftRequest.setTrRmks(trRmks);

		HttpEntity<C24FTRequest> entity = new HttpEntity<>(c24ftRequest, httpHeaders);
		SettlementAccount settlAcct=settlAcctRep.findById(env.getProperty("settl.payable")).get();
		ResponseEntity<C24FTResponse> response = null;
		
		ipsDao.updateINOUT(seqUniqueID,"CBS_REVERSE_IN");
		try {
			logger.info("Sending message to connect24ReversaL using restTemplate");
			response = restTemplate.postForEntity(
					env.getProperty("connect24.url") + "/api/ws/dbtActReversefndTransfer?", entity,
					C24FTResponse.class);

		
			
			ipsDao.updateINOUT(seqUniqueID,"CBS_REVERSE_OUT");

			if (response.getStatusCode() == HttpStatus.OK) {
				C24FTResponse c24ftResponse = new C24FTResponse("SUCCESS", response.getBody().getBalance(),
						response.getBody().getTranCurrency());
				//// update table
				ipsDao.updateTranCBS(sysTraceAuditNumber, acctNumber, trAmt, "DEBIT_REVERSE", currencyCode, "SUCCESS", "",
						seqUniqueID,userID,settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));
				///// Register Table
				ipsDao.updateCBSStatusManual(seqUniqueID, TranMonitorStatus.CBS_DEBIT_REVERSE_OK.toString(),
						TranMonitorStatus.FAILURE.toString());
				
				return new ResponseEntity<>(c24ftResponse, HttpStatus.OK);

			} else {
				
				//// update table
				ipsDao.updateTranCBS(sysTraceAuditNumber, acctNumber, trAmt, "DEBIT_REVERSE", currencyCode, "FAILURE", "",
						seqUniqueID,userID,settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));
				
				ipsDao.updateCBSStatusErrorManual(seqUniqueID, TranMonitorStatus.CBS_DEBIT_REVERSE_ERROR.toString(),
						response.getBody().getError_desc().get(0).toString(),
						TranMonitorStatus.REVERSE_FAILURE.toString());
				
				C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", SERVER_ERROR_CODE,
						Collections.singletonList(TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString()));
				
				return new ResponseEntity<>(c24ftResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			}

		} catch (HttpClientErrorException ex) {
			logger.info("HttpClientErrorException --------->");
			logger.info(ex.getLocalizedMessage());
			logger.error(ex.getLocalizedMessage());
			if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				ipsDao.updateCBSStatusErrorManual(seqUniqueID, TranMonitorStatus.CBS_DEBIT_REVERSE_ERROR.toString(),
						TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(),
						TranMonitorStatus.REVERSE_FAILURE.toString());

				ipsDao.updateTranCBS(sysTraceAuditNumber, acctNumber, trAmt, "DEBIT_REVERSE", currencyCode, "FAILURE",
						TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(), seqUniqueID,userID,settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));
				
				C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", SERVER_ERROR_CODE,
						Collections.singletonList(TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString()));
				
				ipsDao.updateINOUT(seqUniqueID,"CBS_REVERSE_OUT");
				
				return new ResponseEntity<>(c24ftResponse, HttpStatus.INTERNAL_SERVER_ERROR);


			} else {
				C24FTResponse c24ftResponse1 = new Gson().fromJson(ex.getResponseBodyAsString(), C24FTResponse.class);
				C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", c24ftResponse1.getError(),
						c24ftResponse1.getError_desc());
				ipsDao.updateCBSStatusErrorManual(seqUniqueID, TranMonitorStatus.CBS_DEBIT_REVERSE_ERROR.toString(),
						c24ftResponse.getError_desc().get(0).toString(), TranMonitorStatus.REVERSE_FAILURE.toString());
				ipsDao.updateTranCBS(sysTraceAuditNumber, acctNumber, trAmt, "DEBIT_REVERSE", currencyCode, "FAILURE",
						c24ftResponse.getError_desc().get(0).toString(), seqUniqueID,userID,settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));
				
				ipsDao.updateINOUT(seqUniqueID,"CBS_REVERSE_OUT");
				
				return new ResponseEntity<>(c24ftResponse, ex.getStatusCode());

			}

		} catch (HttpServerErrorException ex) {

			logger.info("HttpServerErrorException --------->");
			logger.info(ex.getLocalizedMessage());
			logger.error(ex.getLocalizedMessage());

			C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", SERVER_ERROR_CODE,
					Collections.singletonList(TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString()));
			
			ipsDao.updateCBSStatusErrorManual(seqUniqueID, TranMonitorStatus.CBS_DEBIT_REVERSE_ERROR.toString(),
					TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(),
					TranMonitorStatus.REVERSE_FAILURE.toString());

			ipsDao.updateTranCBS(sysTraceAuditNumber, acctNumber, trAmt, "DEBIT_REVERSE", currencyCode, "FAILURE",
					TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(), seqUniqueID,userID,settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));
			
			ipsDao.updateINOUT(seqUniqueID,"CBS_REVERSE_OUT");
			
			return new ResponseEntity<>(c24ftResponse, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}



	public ResponseEntity<C24FTResponse> ManualBulkdbtReverseFundRequest(String acctNumber, String sysTraceAuditNumber, String currencyCode, String trAmt,
			String seqUniqueID,String userID,String trRmks) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.set("Partipant_BIC", env.getProperty("bob.bankcode"));
		httpHeaders.set("Partipant_SOL", acctNumber.substring(0, 4));
		httpHeaders.set("SYS_TRACE_AUDIT_NUMBER", sysTraceAuditNumber);

		C24FTRequest c24ftRequest = new C24FTRequest();
		C24RequestAcount c24RequestAcount = new C24RequestAcount();
		c24RequestAcount.setAcctNumber(acctNumber);
		c24RequestAcount.setSchmType("");
		c24RequestAcount.setSettlAcctNumber(settlAcctRep.findById(env.getProperty("settl.payable")).get().getAccount_number());
		c24ftRequest.setAccount(c24RequestAcount);
		c24ftRequest.setCurrency_Code(currencyCode);
		c24ftRequest.setPAN("0000000000000000");
		c24ftRequest.setTrAmt(trAmt);
		c24ftRequest.setTrRmks(trRmks);

		HttpEntity<C24FTRequest> entity = new HttpEntity<>(c24ftRequest, httpHeaders);
		SettlementAccount settlAcct=settlAcctRep.findById(env.getProperty("settl.payable")).get();
		ResponseEntity<C24FTResponse> response = null;
		
		try {
			logger.info("Sending message to connect24ReversaL using restTemplate");
			response = restTemplate.postForEntity(
					env.getProperty("connect24.url") + "/api/ws/dbtActReversefndTransfer?", entity,
					C24FTResponse.class);

		
			

			if (response.getStatusCode() == HttpStatus.OK) {
				C24FTResponse c24ftResponse = new C24FTResponse("SUCCESS", response.getBody().getBalance(),
						response.getBody().getTranCurrency());
				//// update table
				ipsDao.updateTranCBS(sysTraceAuditNumber, acctNumber, trAmt, "BULK_DEBIT_REVERSE", currencyCode, "SUCCESS", "",
						seqUniqueID,userID,settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));
				///// Register Table
				ipsDao.updateCBSStatusManual(seqUniqueID, TranMonitorStatus.CBS_DEBIT_REVERSE_OK.toString(),
						TranMonitorStatus.FAILURE.toString());
				
				return new ResponseEntity<>(c24ftResponse, HttpStatus.OK);

			} else {
				
				//// update table
				ipsDao.updateTranCBS(sysTraceAuditNumber, acctNumber, trAmt, "BULK_DEBIT_REVERSE", currencyCode, "FAILURE", "",
						seqUniqueID,userID,settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));
				
				ipsDao.updateCBSStatusErrorManual(seqUniqueID, TranMonitorStatus.CBS_DEBIT_REVERSE_ERROR.toString(),
						response.getBody().getError_desc().get(0).toString(),
						TranMonitorStatus.REVERSE_FAILURE.toString());
				
				C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", SERVER_ERROR_CODE,
						Collections.singletonList(TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString()));
				
				return new ResponseEntity<>(c24ftResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			}

		} catch (HttpClientErrorException ex) {
			logger.info("HttpClientErrorException --------->");
			logger.info(ex.getLocalizedMessage());
			logger.error(ex.getLocalizedMessage());
			if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				ipsDao.updateCBSStatusErrorManual(seqUniqueID, TranMonitorStatus.CBS_DEBIT_REVERSE_ERROR.toString(),
						TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(),
						TranMonitorStatus.REVERSE_FAILURE.toString());

				ipsDao.updateTranCBS(sysTraceAuditNumber, acctNumber, trAmt, "BULK_DEBIT_REVERSE", currencyCode, "FAILURE",
						TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(), seqUniqueID,userID,settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));
				
				C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", SERVER_ERROR_CODE,
						Collections.singletonList(TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString()));
				
				
				return new ResponseEntity<>(c24ftResponse, HttpStatus.INTERNAL_SERVER_ERROR);


			} else {
				C24FTResponse c24ftResponse1 = new Gson().fromJson(ex.getResponseBodyAsString(), C24FTResponse.class);
				C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", c24ftResponse1.getError(),
						c24ftResponse1.getError_desc());
				ipsDao.updateCBSStatusErrorManual(seqUniqueID, TranMonitorStatus.CBS_DEBIT_REVERSE_ERROR.toString(),
						c24ftResponse.getError_desc().get(0).toString(), TranMonitorStatus.REVERSE_FAILURE.toString());
				ipsDao.updateTranCBS(sysTraceAuditNumber, acctNumber, trAmt, "BULK_DEBIT_REVERSE", currencyCode, "FAILURE",
						c24ftResponse.getError_desc().get(0).toString(), seqUniqueID,userID,settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));
				
				
				return new ResponseEntity<>(c24ftResponse, ex.getStatusCode());

			}

		} catch (HttpServerErrorException ex) {

			logger.info("HttpServerErrorException --------->");
			logger.info(ex.getLocalizedMessage());
			logger.error(ex.getLocalizedMessage());

			C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", SERVER_ERROR_CODE,
					Collections.singletonList(TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString()));
			
			ipsDao.updateCBSStatusErrorManual(seqUniqueID, TranMonitorStatus.CBS_DEBIT_REVERSE_ERROR.toString(),
					TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(),
					TranMonitorStatus.REVERSE_FAILURE.toString());

			ipsDao.updateTranCBS(sysTraceAuditNumber, acctNumber, trAmt, "BULK_DEBIT_REVERSE", currencyCode, "FAILURE",
					TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(), seqUniqueID,userID,settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));
			
			ipsDao.updateINOUT(seqUniqueID,"CBS_REVERSE_OUT");
			
			return new ResponseEntity<>(c24ftResponse, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	public ResponseEntity<C24FTResponse> ManualCdtReverseFundRequest(String acctNumber, String sysTraceAuditNumber,
			String currencyCode, String trAmt, String seqUniqueID, String userID,String trRmks) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.set("Partipant_BIC", env.getProperty("bob.bankcode"));
		httpHeaders.set("Partipant_SOL", acctNumber.substring(0, 4));
		httpHeaders.set("SYS_TRACE_AUDIT_NUMBER", sysTraceAuditNumber);

		C24FTRequest c24ftRequest = new C24FTRequest();
		C24RequestAcount c24RequestAcount = new C24RequestAcount();
		c24RequestAcount.setAcctNumber(acctNumber);
		c24RequestAcount.setSchmType("");
		c24RequestAcount.setSettlAcctNumber(settlAcctRep.findById(env.getProperty("settl.receivable")).get().getAccount_number());
		c24ftRequest.setAccount(c24RequestAcount);
		c24ftRequest.setCurrency_Code(currencyCode);
		c24ftRequest.setPAN("0000000000000000");
		c24ftRequest.setTrAmt(trAmt);
		c24ftRequest.setTrRmks(trRmks);

		HttpEntity<C24FTRequest> entity = new HttpEntity<>(c24ftRequest, httpHeaders);
		SettlementAccount settlAcct=settlAcctRep.findById(env.getProperty("settl.receivable")).get();

		ResponseEntity<C24FTResponse> response = null;
		
		ipsDao.updateINOUT(seqUniqueID,"CBS_REVERSE_IN");
		try {
			logger.info("Sending message to connect24ReversaL using restTemplate");
			response = restTemplate.postForEntity(
					env.getProperty("connect24.url") + "/api/ws/cdtActReversefndTransfer?", entity,
					C24FTResponse.class);

			//// update table
			ipsDao.updateTranCBS(sysTraceAuditNumber, acctNumber, trAmt, "CREDIT_REVERSE", currencyCode, "SUCCESS", "",
					seqUniqueID,userID,settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));
			
			ipsDao.updateINOUT(seqUniqueID,"CBS_REVERSE_OUT");

			if (response.getStatusCode() == HttpStatus.OK) {
				C24FTResponse c24ftResponse = new C24FTResponse("SUCCESS", response.getBody().getBalance(),
						response.getBody().getTranCurrency());
				
				///// Register Table
				ipsDao.updateCBSStatusManual(seqUniqueID, TranMonitorStatus.CBS_CREDIT_REVERSE_OK.toString(),
						TranMonitorStatus.FAILURE.toString());
				
				return new ResponseEntity<>(c24ftResponse, HttpStatus.OK);

			} else {
				ipsDao.updateCBSStatusErrorManual(seqUniqueID, TranMonitorStatus.CBS_CREDIT_REVERSE_ERROR.toString(),
						response.getBody().getError_desc().get(0).toString(),
						TranMonitorStatus.REVERSE_FAILURE.toString());
				
				C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", SERVER_ERROR_CODE,
						Collections.singletonList(TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString()));
				
				return new ResponseEntity<>(c24ftResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			}

		} catch (HttpClientErrorException ex) {
			logger.info("HttpClientErrorException --------->");
			logger.info(ex.getLocalizedMessage());
			logger.error(ex.getLocalizedMessage());
			if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				ipsDao.updateCBSStatusErrorManual(seqUniqueID, TranMonitorStatus.CBS_CREDIT_REVERSE_ERROR.toString(),
						TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(),
						TranMonitorStatus.REVERSE_FAILURE.toString());

				ipsDao.updateTranCBS(sysTraceAuditNumber, acctNumber, trAmt, "CREDIT_REVERSE", currencyCode, "FAILURE",
						TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(), seqUniqueID,userID,settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));
				
				C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", SERVER_ERROR_CODE,
						Collections.singletonList(TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString()));
				
				ipsDao.updateINOUT(seqUniqueID,"CBS_REVERSE_OUT");
				
				return new ResponseEntity<>(c24ftResponse, HttpStatus.INTERNAL_SERVER_ERROR);


			} else {
				C24FTResponse c24ftResponse1 = new Gson().fromJson(ex.getResponseBodyAsString(), C24FTResponse.class);
				C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", c24ftResponse1.getError(),
						c24ftResponse1.getError_desc());
				ipsDao.updateCBSStatusErrorManual(seqUniqueID, TranMonitorStatus.CBS_CREDIT_REVERSE_ERROR.toString(),
						c24ftResponse.getError_desc().get(0).toString(), TranMonitorStatus.REVERSE_FAILURE.toString());
				ipsDao.updateTranCBS(sysTraceAuditNumber, acctNumber, trAmt, "CREDIT_REVERSE", currencyCode, "FAILURE",
						c24ftResponse.getError_desc().get(0).toString(), seqUniqueID,userID,settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));
				
				ipsDao.updateINOUT(seqUniqueID,"CBS_REVERSE_OUT");
				
				return new ResponseEntity<>(c24ftResponse, ex.getStatusCode());

			}

		} catch (HttpServerErrorException ex) {

			logger.info("HttpServerErrorException --------->");
			logger.info(ex.getLocalizedMessage());
			logger.error(ex.getLocalizedMessage());

			C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", SERVER_ERROR_CODE,
					Collections.singletonList(TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString()));
			
			ipsDao.updateCBSStatusErrorManual(seqUniqueID, TranMonitorStatus.CBS_CREDIT_REVERSE_ERROR.toString(),
					TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(),
					TranMonitorStatus.REVERSE_FAILURE.toString());

			ipsDao.updateTranCBS(sysTraceAuditNumber, acctNumber, trAmt, "CREDIT_REVERSE", currencyCode, "FAILURE",
					TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(), seqUniqueID,userID,settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));
			
			ipsDao.updateINOUT(seqUniqueID,"CBS_REVERSE_OUT");
			
			return new ResponseEntity<>(c24ftResponse, HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	public ResponseEntity<C24FTResponse> ManualDbtFundRequest(ManualFndTransferRequest manualFndTransferRequest,
			String sysTraceAuditNumber, String seqUniqueID,String trRmks) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.set("Partipant_BIC", env.getProperty("bob.bankcode"));
		httpHeaders.set("Partipant_SOL", manualFndTransferRequest.getRemitterAcctNumber().substring(0, 4));
		httpHeaders.set("SYS_TRACE_AUDIT_NUMBER", sysTraceAuditNumber);

		C24FTRequest c24ftRequest = new C24FTRequest();
		C24RequestAcount c24RequestAcount = new C24RequestAcount();
		c24RequestAcount.setAcctNumber(manualFndTransferRequest.getRemitterAcctNumber());
		c24RequestAcount.setSchmType("");
		c24RequestAcount.setSettlAcctNumber(settlAcctRep.findById(env.getProperty("settl.payable")).get().getAccount_number());
		c24ftRequest.setAccount(c24RequestAcount);
		c24ftRequest.setCurrency_Code(manualFndTransferRequest.getCurrencyCode());
		c24ftRequest.setPAN("0000000000000000");
		c24ftRequest.setTrAmt(manualFndTransferRequest.getTrAmt());
		c24ftRequest.setTrRmks(trRmks);

		HttpEntity<C24FTRequest> entity = new HttpEntity<>(c24ftRequest, httpHeaders);
		SettlementAccount settlAcct=settlAcctRep.findById(env.getProperty("settl.payable")).get();

		ResponseEntity<C24FTResponse> response = null;
		
		ipsDao.updateINOUT(seqUniqueID,"CBS_IN");


		try {
			logger.info("Sending message to connect24 ");
			
			response = restTemplate.postForEntity(env.getProperty("connect24.url") + "/api/ws/dbtActfndTransfer?",
					entity, C24FTResponse.class);
			C24FTResponse c24ftResponse = new C24FTResponse("SUCCESS", response.getBody().getBalance(),
					response.getBody().getTranCurrency());

			//// update table
			ipsDao.updateTranCBS(sysTraceAuditNumber, manualFndTransferRequest.getRemitterAcctNumber(),
					manualFndTransferRequest.getTrAmt(), "DEBIT", manualFndTransferRequest.getCurrencyCode(), "SUCCESS",
					"", seqUniqueID,"SYSTEM",settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));
			
			ipsDao.updateINOUT(seqUniqueID,"CBS_OUT");

			return new ResponseEntity<>(c24ftResponse, HttpStatus.OK);
		} catch (HttpClientErrorException ex) {

			logger.info("HttpClientErrorException --------->" + ex.getStatusCode());
			logger.info(ex.getLocalizedMessage());
			logger.error(ex.getLocalizedMessage());
			
	
			
			if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", SERVER_ERROR_CODE,
						Collections.singletonList(TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString()));

				///// Update Table
				ipsDao.updateTranCBS(sysTraceAuditNumber, manualFndTransferRequest.getRemitterAcctNumber(),
						manualFndTransferRequest.getTrAmt(), "DEBIT", manualFndTransferRequest.getCurrencyCode(),
						"FAILURE", TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(), seqUniqueID,"SYSTEM",settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));
				
				ipsDao.updateINOUT(seqUniqueID,"CBS_OUT");

				return new ResponseEntity<>(c24ftResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				C24FTResponse c24ftResponse1 = new Gson().fromJson(ex.getResponseBodyAsString(), C24FTResponse.class);
				C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", c24ftResponse1.getError(),
						c24ftResponse1.getError_desc());

				//// update table
				ipsDao.updateTranCBS(sysTraceAuditNumber, manualFndTransferRequest.getRemitterAcctNumber(),
						manualFndTransferRequest.getTrAmt(), "DEBIT", manualFndTransferRequest.getCurrencyCode(),
						"FAILURE", c24ftResponse1.getError_desc().get(0).toString(), seqUniqueID,"SYSTEM",settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));
				
				ipsDao.updateINOUT(seqUniqueID,"CBS_OUT");

				return new ResponseEntity<>(c24ftResponse, ex.getStatusCode());
			}

		} catch (HttpServerErrorException ex) {
			logger.info("HttpServerErrorException --------->");
			logger.info(ex.getLocalizedMessage());
			logger.error(ex.getLocalizedMessage());
			


			C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", SERVER_ERROR_CODE,
					Collections.singletonList(TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString()));
			ipsDao.updateTranCBS(sysTraceAuditNumber, manualFndTransferRequest.getRemitterAcctNumber(),
					manualFndTransferRequest.getTrAmt(), "DEBIT", manualFndTransferRequest.getCurrencyCode(), "FAILURE",
					TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(), seqUniqueID,"SYSTEM",settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));
			
			ipsDao.updateINOUT(seqUniqueID,"CBS_OUT");

			return new ResponseEntity<>(c24ftResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	public ResponseEntity<C24FTResponse> BulkDebitFundRequest(BulkDebitRemitterAccount bulkDebitRemitterAccount,
			String sysTraceAuditNumber, String seqUniqueID,String trRmks) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.set("Partipant_BIC", env.getProperty("bob.bankcode"));
		httpHeaders.set("Partipant_SOL", bulkDebitRemitterAccount.getRemitterAcctNumber().substring(0, 4));
		httpHeaders.set("SYS_TRACE_AUDIT_NUMBER", sysTraceAuditNumber);

		C24FTRequest c24ftRequest = new C24FTRequest();
		C24RequestAcount c24RequestAcount = new C24RequestAcount();
		c24RequestAcount.setAcctNumber(bulkDebitRemitterAccount.getRemitterAcctNumber());
		c24RequestAcount.setSchmType("");
		c24RequestAcount.setSettlAcctNumber(settlAcctRep.findById(env.getProperty("settl.payable")).get().getAccount_number());
		c24ftRequest.setAccount(c24RequestAcount);
		c24ftRequest.setCurrency_Code(bulkDebitRemitterAccount.getCurrencyCode());
		c24ftRequest.setPAN("0000000000000000");
		c24ftRequest.setTrAmt(bulkDebitRemitterAccount.getTrAmt());
		c24ftRequest.setTrRmks(trRmks);

		HttpEntity<C24FTRequest> entity = new HttpEntity<>(c24ftRequest, httpHeaders);
		SettlementAccount settlAcct=settlAcctRep.findById(env.getProperty("settl.payable")).get();

		ResponseEntity<C24FTResponse> response = null;
		
		ipsDao.updateINOUT(seqUniqueID,"CBS_IN");
		
		logger.info("gfhkjgkh");


		try {
			logger.info("Sending message to connect24 ");
			
			response = restTemplate.postForEntity(env.getProperty("connect24.url") + "/api/ws/dbtActfndTransfer?",
					entity, C24FTResponse.class);
			C24FTResponse c24ftResponse = new C24FTResponse("SUCCESS", response.getBody().getBalance(),
					response.getBody().getTranCurrency());

			//// update table
			ipsDao.updateTranCBS(sysTraceAuditNumber, bulkDebitRemitterAccount.getRemitterAcctNumber(),
					bulkDebitRemitterAccount.getTrAmt(), "BULK_DEBIT", bulkDebitRemitterAccount.getCurrencyCode(), "SUCCESS",
					"", seqUniqueID,"SYSTEM",settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));
			
			ipsDao.updateINOUT(seqUniqueID,"CBS_OUT");

			return new ResponseEntity<>(c24ftResponse, HttpStatus.OK);
		} catch (HttpClientErrorException ex) {

			logger.info("HttpClientErrorException --------->" + ex.getStatusCode());
			logger.info(ex.getLocalizedMessage());
			logger.error(ex.getLocalizedMessage());
			
	
			
			if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", SERVER_ERROR_CODE,
						Collections.singletonList(TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString()));

				///// Update Table
				ipsDao.updateTranCBS(sysTraceAuditNumber, bulkDebitRemitterAccount.getRemitterAcctNumber(),
						bulkDebitRemitterAccount.getTrAmt(), "BULK_DEBIT", bulkDebitRemitterAccount.getCurrencyCode(),
						"FAILURE", TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(), seqUniqueID,"SYSTEM",settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));
				
				ipsDao.updateINOUT(seqUniqueID,"CBS_OUT");

				return new ResponseEntity<>(c24ftResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				C24FTResponse c24ftResponse1 = new Gson().fromJson(ex.getResponseBodyAsString(), C24FTResponse.class);
				C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", c24ftResponse1.getError(),
						c24ftResponse1.getError_desc());

				//// update table
				ipsDao.updateTranCBS(sysTraceAuditNumber, bulkDebitRemitterAccount.getRemitterAcctNumber(),
						bulkDebitRemitterAccount.getTrAmt(), "BULK_DEBIT", bulkDebitRemitterAccount.getCurrencyCode(),
						"FAILURE", c24ftResponse1.getError_desc().get(0).toString(), seqUniqueID,"SYSTEM",settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));
				
				ipsDao.updateINOUT(seqUniqueID,"CBS_OUT");

				return new ResponseEntity<>(c24ftResponse, ex.getStatusCode());
			}

		} catch (HttpServerErrorException ex) {
			logger.info("HttpServerErrorException --------->");
			logger.info(ex.getLocalizedMessage());
			logger.error(ex.getLocalizedMessage());
			


			C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", SERVER_ERROR_CODE,
					Collections.singletonList(TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString()));
			ipsDao.updateTranCBS(sysTraceAuditNumber, bulkDebitRemitterAccount.getRemitterAcctNumber(),
					bulkDebitRemitterAccount.getTrAmt(), "BULK_DEBIT", bulkDebitRemitterAccount.getCurrencyCode(), "FAILURE",
					TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(), seqUniqueID,"SYSTEM",settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));
			
			ipsDao.updateINOUT(seqUniqueID,"CBS_OUT");

			return new ResponseEntity<>(c24ftResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	public void BulkdbtReverseFundRequest(String acctNumber, String sysTraceAuditNumber, String currencyCode, String trAmt,
			String seqUniqueID,String trRmks) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.set("Partipant_BIC", env.getProperty("bob.bankcode"));
		httpHeaders.set("Partipant_SOL", acctNumber.substring(0, 4));
		httpHeaders.set("SYS_TRACE_AUDIT_NUMBER", sysTraceAuditNumber);

		C24FTRequest c24ftRequest = new C24FTRequest();
		C24RequestAcount c24RequestAcount = new C24RequestAcount();
		c24RequestAcount.setAcctNumber(acctNumber);
		c24RequestAcount.setSchmType("");
		c24RequestAcount.setSettlAcctNumber(settlAcctRep.findById(env.getProperty("settl.payable")).get().getAccount_number());
		c24ftRequest.setAccount(c24RequestAcount);
		c24ftRequest.setCurrency_Code(currencyCode);
		c24ftRequest.setPAN("0000000000000000");
		c24ftRequest.setTrAmt(trAmt);
		c24ftRequest.setTrRmks(trRmks);
		
		HttpEntity<C24FTRequest> entity = new HttpEntity<>(c24ftRequest, httpHeaders);
		
		SettlementAccount settlAcct=settlAcctRep.findById(env.getProperty("settl.payable")).get();

		
		ResponseEntity<C24FTResponse> response = null;
		
		ipsDao.updateINOUT(seqUniqueID,"CBS_REVERSE_IN");
		try {
			logger.info("Sending message to connect24ReversaL using restTemplate");
			response = restTemplate.postForEntity(
					env.getProperty("connect24.url") + "/api/ws/dbtActReversefndTransfer?", entity,
					C24FTResponse.class);

			//// update table
			ipsDao.updateTranCBS(sysTraceAuditNumber, acctNumber, trAmt, "BULK_DEBIT_REVERSE", currencyCode, "SUCCESS", "",
					seqUniqueID,"SYSTEM",settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));
			
			ipsDao.updateINOUT(seqUniqueID,"CBS_REVERSE_OUT");

			if (response.getStatusCode() == HttpStatus.OK) {
				///// Register Table
				ipsDao.updateCBSStatus(seqUniqueID, TranMonitorStatus.CBS_DEBIT_REVERSE_OK.toString(),
						TranMonitorStatus.FAILURE.toString());
			} else {
				ipsDao.updateCBSStatusError(seqUniqueID, TranMonitorStatus.CBS_DEBIT_REVERSE_ERROR.toString(),
						response.getBody().getError_desc().get(0).toString(),
						TranMonitorStatus.REVERSE_FAILURE.toString());
			}

		} catch (HttpClientErrorException ex) {
			logger.info("HttpClientErrorException --------->");
			logger.info(ex.getLocalizedMessage());
			logger.error(ex.getLocalizedMessage());
			if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				ipsDao.updateCBSStatusError(seqUniqueID, TranMonitorStatus.CBS_DEBIT_REVERSE_ERROR.toString(),
						TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(),
						TranMonitorStatus.REVERSE_FAILURE.toString());

				ipsDao.updateTranCBS(sysTraceAuditNumber, acctNumber, trAmt, "BULK_DEBIT_REVERSE", currencyCode, "FAILURE",
						TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(), seqUniqueID,"SYSTEM",settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));
				
				ipsDao.updateINOUT(seqUniqueID,"CBS_REVERSE_OUT");

			} else {
				C24FTResponse c24ftResponse1 = new Gson().fromJson(ex.getResponseBodyAsString(), C24FTResponse.class);
				C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", c24ftResponse1.getError(),
						c24ftResponse1.getError_desc());
				ipsDao.updateCBSStatusError(seqUniqueID, TranMonitorStatus.CBS_DEBIT_REVERSE_ERROR.toString(),
						c24ftResponse.getError_desc().get(0).toString(), TranMonitorStatus.REVERSE_FAILURE.toString());
				ipsDao.updateTranCBS(sysTraceAuditNumber, acctNumber, trAmt, "BULK_DEBIT_REVERSE", currencyCode, "FAILURE",
						c24ftResponse.getError_desc().get(0).toString(), seqUniqueID,"SYSTEM",settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));
				
				ipsDao.updateINOUT(seqUniqueID,"CBS_REVERSE_OUT");
			}

		} catch (HttpServerErrorException ex) {

			logger.info("HttpServerErrorException --------->");
			logger.info(ex.getLocalizedMessage());
			logger.error(ex.getLocalizedMessage());

			ipsDao.updateCBSStatusError(seqUniqueID, TranMonitorStatus.CBS_DEBIT_REVERSE_ERROR.toString(),
					TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(),
					TranMonitorStatus.REVERSE_FAILURE.toString());

			ipsDao.updateTranCBS(sysTraceAuditNumber, acctNumber, trAmt, "BULK_DEBIT_REVERSE", currencyCode, "FAILURE",
					TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(), seqUniqueID,"SYSTEM",settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));
			
			ipsDao.updateINOUT(seqUniqueID,"CBS_REVERSE_OUT");
		}
	}

	public ResponseEntity<C24FTResponse> ipsChargeAndFeeMyt(RegPublicKey regPublicKey, String remarks, String tranAmt,String sysTraceAuditNumber,
			String remark) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.set("Partipant_BIC", env.getProperty("bob.bankcode"));
		httpHeaders.set("Partipant_SOL", regPublicKey.getAcct_number().substring(0, 4));
		httpHeaders.set("SYS_TRACE_AUDIT_NUMBER", sysTraceAuditNumber);
		httpHeaders.set("Remark", remark);

		C24FTRequest c24ftRequest = new C24FTRequest();
		C24RequestAcount c24RequestAcount = new C24RequestAcount();
		c24RequestAcount.setAcctNumber(regPublicKey.getAcct_number());
		c24RequestAcount.setSchmType("");
		c24RequestAcount.setSettlAcctNumber(settlAcctRep.findById(env.getProperty("settl.income")).get().getAccount_number());
		
		c24ftRequest.setAccount(c24RequestAcount);
		c24ftRequest.setCurrency_Code(env.getProperty("bob.crncycode"));
		c24ftRequest.setPAN("");
		c24ftRequest.setTrAmt(tranAmt);

		HttpEntity<C24FTRequest> entity = new HttpEntity<>(c24ftRequest, httpHeaders);
		ResponseEntity<C24FTResponse> response = null;
	

		SettlementAccount settlAcct=settlAcctRep.findById(env.getProperty("settl.income")).get();
		try {
			logger.info("Sending message to connect24 ");
			
			response = restTemplate.postForEntity(env.getProperty("connect24.url") + "/api/ws/ipsChargeAndFee?",
					entity, C24FTResponse.class);
			C24FTResponse c24ftResponse = new C24FTResponse("SUCCESS", response.getBody().getBalance(),
					response.getBody().getTranCurrency());

			//// update table
			ipsDao.updateTranCBS(sysTraceAuditNumber, regPublicKey.getAcct_number(),
					tranAmt, "CHARGE",env.getProperty("bob.crncycode"), "SUCCESS",
					"", regPublicKey.getRecord_id(),"SYSTEM",settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.charge"));
			
			return new ResponseEntity<>(c24ftResponse, HttpStatus.OK);
		} catch (HttpClientErrorException ex) {
			if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				
				C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", SERVER_ERROR_CODE,
						Collections.singletonList(TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString()));

				///// Update Table
				ipsDao.updateTranCBS(sysTraceAuditNumber,regPublicKey.getAcct_number(),
						tranAmt, "CHARGE",env.getProperty("bob.crncycode"),
						"FAILURE", TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(),regPublicKey.getRecord_id(),"SYSTEM"
						,settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.charge"));
				
				return new ResponseEntity<>(c24ftResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				C24FTResponse c24ftResponse1 = new Gson().fromJson(ex.getResponseBodyAsString(), C24FTResponse.class);
				C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", c24ftResponse1.getError(),
						c24ftResponse1.getError_desc());

				//// update table
				ipsDao.updateTranCBS(sysTraceAuditNumber, regPublicKey.getAcct_number(),
						tranAmt, "CHARGE", env.getProperty("bob.crncycode"),
						"FAILURE", c24ftResponse1.getError_desc().get(0).toString(), regPublicKey.getRecord_id(),"SYSTEM"
						,settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.charge"));
				

				return new ResponseEntity<>(c24ftResponse, ex.getStatusCode());
			}

		} catch (HttpServerErrorException ex) {
		
			C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", SERVER_ERROR_CODE,
					Collections.singletonList(TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString()));
			
			ipsDao.updateTranCBS(sysTraceAuditNumber,regPublicKey.getAcct_number(),
					tranAmt, "CHARGE",env.getProperty("bob.crncycode"),
					"FAILURE", TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(),regPublicKey.getRecord_id(),"SYSTEM"
					,settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.charge"));
			
			return new ResponseEntity<>(c24ftResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	public ResponseEntity<C24FTResponse> ipsChargeAndFeeTran(TransactionMonitor tranMonitor, String remarks, String tranAmt,String sysTraceAuditNumber,
			String remark) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.set("Partipant_BIC", env.getProperty("bob.bankcode"));
		httpHeaders.set("Partipant_SOL", tranMonitor.getBob_account().substring(0, 4));
		httpHeaders.set("SYS_TRACE_AUDIT_NUMBER", sysTraceAuditNumber);
		httpHeaders.set("Remark", remark);

		C24FTRequest c24ftRequest = new C24FTRequest();
		C24RequestAcount c24RequestAcount = new C24RequestAcount();
		c24RequestAcount.setAcctNumber(tranMonitor.getBob_account());
		c24RequestAcount.setSchmType("");
		c24RequestAcount.setSettlAcctNumber(settlAcctRep.findById(env.getProperty("settl.income")).get().getAccount_number());
		
		c24ftRequest.setAccount(c24RequestAcount);
		c24ftRequest.setCurrency_Code(env.getProperty("bob.crncycode"));
		c24ftRequest.setPAN("");
		c24ftRequest.setTrAmt(tranAmt);

		HttpEntity<C24FTRequest> entity = new HttpEntity<>(c24ftRequest, httpHeaders);
		ResponseEntity<C24FTResponse> response = null;
	

		SettlementAccount settlAcct=settlAcctRep.findById(env.getProperty("settl.income")).get();
		try {
			logger.info("Sending message to connect24 ");
			
			response = restTemplate.postForEntity(env.getProperty("connect24.url") + "/api/ws/ipsChargeAndFee?",
					entity, C24FTResponse.class);
			C24FTResponse c24ftResponse = new C24FTResponse("SUCCESS", response.getBody().getBalance(),
					response.getBody().getTranCurrency());

			//// update table
			ipsDao.updateTranCBS(sysTraceAuditNumber, tranMonitor.getBob_account(),
					tranAmt, "CHARGE",env.getProperty("bob.crncycode"), "SUCCESS",
					"",tranMonitor.getSequence_unique_id(),"SYSTEM",settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.tran"));
			
			return new ResponseEntity<>(c24ftResponse, HttpStatus.OK);
		} catch (HttpClientErrorException ex) {
			if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				
				C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", SERVER_ERROR_CODE,
						Collections.singletonList(TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString()));

				///// Update Table
				ipsDao.updateTranCBS(sysTraceAuditNumber,tranMonitor.getBob_account(),
						tranAmt, "CHARGE",env.getProperty("bob.crncycode"),
						"FAILURE", TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(),tranMonitor.getSequence_unique_id(),"SYSTEM"
						,settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.charge"));
				
				return new ResponseEntity<>(c24ftResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				C24FTResponse c24ftResponse1 = new Gson().fromJson(ex.getResponseBodyAsString(), C24FTResponse.class);
				C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", c24ftResponse1.getError(),
						c24ftResponse1.getError_desc());

				//// update table
				ipsDao.updateTranCBS(sysTraceAuditNumber,tranMonitor.getBob_account(),
						tranAmt, "CHARGE", env.getProperty("bob.crncycode"),
						"FAILURE", c24ftResponse1.getError_desc().get(0).toString(), tranMonitor.getSequence_unique_id(),"SYSTEM"
						,settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.charge"));
				

				return new ResponseEntity<>(c24ftResponse, ex.getStatusCode());
			}

		} catch (HttpServerErrorException ex) {
		
			C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", SERVER_ERROR_CODE,
					Collections.singletonList(TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString()));
			
			ipsDao.updateTranCBS(sysTraceAuditNumber,tranMonitor.getBob_account(),
					tranAmt, "CHARGE",env.getProperty("bob.crncycode"),
					"FAILURE", TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(),tranMonitor.getSequence_unique_id(),"SYSTEM"
					,settlAcct.getAccount_number(),settlAcct.getAcct_type(),env.getProperty("trchtype.charge"));
			
			return new ResponseEntity<>(c24ftResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	public ResponseEntity<AccountContactResponse> test(){
		
	
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.set("Partipant_BIC", env.getProperty("bob.bankcode"));
		httpHeaders.set("Partipant_SOL", "9031");

		C24FTRequest c24ftRequest = new C24FTRequest();
		C24RequestAcount c24RequestAcount = new C24RequestAcount();
		c24RequestAcount.setAcctNumber("90310200004212");
		c24RequestAcount.setSchmType("SBA");
		
		c24ftRequest.setAccount(c24RequestAcount);
		c24ftRequest.setCurrency_Code("MUR");
		c24ftRequest.setPAN("0000000000000000");

		HttpEntity<C24FTRequest> entity = new HttpEntity<>(c24ftRequest, httpHeaders);
		ResponseEntity<AccountContactResponse> response = null;
	

		try {
			logger.info("Sending message to connect24 ");
			
			response = restTemplate.postForEntity(env.getProperty("connect24.url") + "/api/ws/getContact?",
					entity, AccountContactResponse.class);

			
			return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
		} catch (HttpClientErrorException ex) {

			logger.info("HttpClientErrorException --------->" + ex.getStatusCode());
			logger.info(ex.getLocalizedMessage());
			logger.error(ex.getLocalizedMessage());
			
	
			
			if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				AccountContactResponse c24ftResponse = new AccountContactResponse("FAILURE", SERVER_ERROR_CODE,
						Collections.singletonList(TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString()));
			

				return new ResponseEntity<>(c24ftResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				AccountContactResponse c24ftResponse1 = new Gson().fromJson(ex.getResponseBodyAsString(), AccountContactResponse.class);
				AccountContactResponse c24ftResponse = new AccountContactResponse("FAILURE", c24ftResponse1.getError(),
						c24ftResponse1.getError_desc());

				return new ResponseEntity<>(c24ftResponse, ex.getStatusCode());
			}

		} catch (HttpServerErrorException ex) {
			logger.info("HttpServerErrorException --------->");
			logger.info(ex.getLocalizedMessage());
			logger.error(ex.getLocalizedMessage());

			AccountContactResponse c24ftResponse = new AccountContactResponse("FAILURE", SERVER_ERROR_CODE,
					Collections.singletonList(TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString()));

			return new ResponseEntity<>(c24ftResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
    ////Payable Account to Settlement Fund Transfer
	public ResponseEntity<C24FTResponse> payableSettl(String sysTraceAuditNumber,String trRmks,
			MCCreditTransferRequest mcCreditTransferRequest,String userID){
		
		//SettlementAccount payableAcct=settlAcctRep.findById(env.getProperty("settl.payable")).get();
		//SettlementAccount settlAcct=settlAcctRep.findById(env.getProperty("settl.settlment")).get();

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.set("Partipant_BIC", env.getProperty("bob.bankcode"));
		httpHeaders.set("Partipant_SOL", mcCreditTransferRequest.getFrAccount().getAcctNumber().substring(0,4));
		httpHeaders.set("SYS_TRACE_AUDIT_NUMBER", sysTraceAuditNumber);

		C24FTRequest c24ftRequest = new C24FTRequest();
		C24RequestAcount c24RequestAcount = new C24RequestAcount();
		c24RequestAcount.setAcctNumber(mcCreditTransferRequest.getFrAccount().getAcctNumber());
		c24RequestAcount.setSchmType("");
		c24RequestAcount.setSettlAcctNumber(mcCreditTransferRequest.getToAccount().getAcctNumber());
		
		c24ftRequest.setAccount(c24RequestAcount);
		c24ftRequest.setCurrency_Code(env.getProperty("bob.crncycode"));
		c24ftRequest.setPAN("0000000000000000");
		c24ftRequest.setTrAmt(mcCreditTransferRequest.getTrAmt());
		c24ftRequest.setTrRmks(trRmks);

		HttpEntity<C24FTRequest> entity = new HttpEntity<>(c24ftRequest, httpHeaders);
		ResponseEntity<C24FTResponse> response = null;

		try {
			logger.info("Sending message to connect24 ");
			
			response = restTemplate.postForEntity(env.getProperty("connect24.url") + "/api/ws/dbtActfndTransfer?",
					entity, C24FTResponse.class);
			
			C24FTResponse c24ftResponse = new C24FTResponse("SUCCESS", response.getBody().getBalance(),
					response.getBody().getTranCurrency());

			//// update table
			ipsDao.updateTranCBS(sysTraceAuditNumber,mcCreditTransferRequest.getFrAccount().getAcctNumber(),
					mcCreditTransferRequest.getTrAmt(), "PAYABLE",env.getProperty("bob.crncycode"), "SUCCESS",
					"", sysTraceAuditNumber,userID,mcCreditTransferRequest.getToAccount().getAcctNumber(),"SETTLEMENT",env.getProperty("trchtype.settl"));
			

			return new ResponseEntity<>(c24ftResponse, HttpStatus.OK);
		} catch (HttpClientErrorException ex) {	
			if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", SERVER_ERROR_CODE,
						Collections.singletonList(TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString()));

				///// Update Table
				ipsDao.updateTranCBS(sysTraceAuditNumber,mcCreditTransferRequest.getFrAccount().getAcctNumber(),
						mcCreditTransferRequest.getTrAmt(), "PAYABLE",env.getProperty("bob.crncycode"),
						"FAILURE", TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(),sysTraceAuditNumber,userID
						,mcCreditTransferRequest.getToAccount().getAcctNumber(),"SETTLEMENT",env.getProperty("trchtype.settl"));
				
				return new ResponseEntity<>(c24ftResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				C24FTResponse c24ftResponse1 = new Gson().fromJson(ex.getResponseBodyAsString(), C24FTResponse.class);
				C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", c24ftResponse1.getError(),
						c24ftResponse1.getError_desc());

				//// update table
				ipsDao.updateTranCBS(sysTraceAuditNumber,mcCreditTransferRequest.getFrAccount().getAcctNumber(),
						mcCreditTransferRequest.getTrAmt(), "PAYABLE",env.getProperty("bob.crncycode"),
						"FAILURE", c24ftResponse1.getError_desc().get(0).toString(), sysTraceAuditNumber,userID
						,mcCreditTransferRequest.getToAccount().getAcctNumber(),"SETTLEMENT",env.getProperty("trchtype.settl"));
				

				return new ResponseEntity<>(c24ftResponse, ex.getStatusCode());
			}

		} catch (HttpServerErrorException ex) {

			C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", SERVER_ERROR_CODE,
					Collections.singletonList(TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString()));
			ipsDao.updateTranCBS(sysTraceAuditNumber, mcCreditTransferRequest.getFrAccount().getAcctNumber(),
					mcCreditTransferRequest.getTrAmt(), "PAYABLE", env.getProperty("bob.crncycode"), "FAILURE",
					TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(), sysTraceAuditNumber,userID
					,mcCreditTransferRequest.getToAccount().getAcctNumber(),"SETTLEMENT",env.getProperty("trchtype.settl"));
			

			return new ResponseEntity<>(c24ftResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<C24FTResponse> receivableSettl(String sysTraceAuditNumber, String trAmt,
			String trRmks,MCCreditTransferRequest mcCreditTransferRequest,String userID) {
		//SettlementAccount receivableAcct=settlAcctRep.findById(env.getProperty("settl.receivable")).get();
		//SettlementAccount settlAcct=settlAcctRep.findById(env.getProperty("settl.settlment")).get();
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.set("Partipant_BIC", env.getProperty("bob.bankcode"));
		httpHeaders.set("Partipant_SOL",mcCreditTransferRequest.getFrAccount().getAcctNumber().substring(0, 4));
		httpHeaders.set("SYS_TRACE_AUDIT_NUMBER", sysTraceAuditNumber);

		C24FTRequest c24ftRequest = new C24FTRequest();
		C24RequestAcount c24RequestAcount = new C24RequestAcount();
		c24RequestAcount.setAcctNumber(mcCreditTransferRequest.getFrAccount().getAcctNumber());
		c24RequestAcount.setSchmType("");
		c24RequestAcount.setSettlAcctNumber(mcCreditTransferRequest.getToAccount().getAcctNumber());

		c24ftRequest.setAccount(c24RequestAcount);
		c24ftRequest.setCurrency_Code(env.getProperty("bob.crncycode"));
		c24ftRequest.setPAN("0000000000000000");
		c24ftRequest.setTrAmt(trAmt);
		c24ftRequest.setTrRmks(trRmks);
		HttpEntity<C24FTRequest> entity = new HttpEntity<>(c24ftRequest, httpHeaders);
		
		
		ResponseEntity<C24FTResponse> response = null;
		try {
			logger.info("Sending message to connect24 credit using restTemplate");
			response = restTemplate.postForEntity(env.getProperty("connect24.url") + "/api/ws/cdtActfndTransfer?",
					entity, C24FTResponse.class);

			C24FTResponse c24ftResponse = new C24FTResponse("SUCCESS", response.getBody().getBalance(),
					response.getBody().getTranCurrency());

			//// update table
			ipsDao.updateTranCBS(sysTraceAuditNumber,mcCreditTransferRequest.getFrAccount().getAcctNumber(), trAmt, "RECEIVABLE", env.getProperty("bob.crncycode"), "SUCCESS", "",
					sysTraceAuditNumber,userID,mcCreditTransferRequest.getToAccount().getAcctNumber(),"SETTLEMENT",env.getProperty("trchtype.settl"));

			return new ResponseEntity<>(c24ftResponse, HttpStatus.OK);

		} catch (HttpClientErrorException ex) {

			if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", SERVER_ERROR_CODE,
						Collections.singletonList(TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString()));

				//// update table
				ipsDao.updateTranCBS(sysTraceAuditNumber, mcCreditTransferRequest.getFrAccount().getAcctNumber(), trAmt, "RECEIVABLE", env.getProperty("bob.crncycode"), "FAILURE",
						TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(), sysTraceAuditNumber,userID,
						mcCreditTransferRequest.getToAccount().getAcctNumber(),"SETTLEMENT",env.getProperty("trchtype.settl"));
				return new ResponseEntity<>(c24ftResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				C24FTResponse c24ftResponse1 = new Gson().fromJson(ex.getResponseBodyAsString(), C24FTResponse.class);
				C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", c24ftResponse1.getError(),
						c24ftResponse1.getError_desc());

				//// update table
				ipsDao.updateTranCBS(sysTraceAuditNumber, mcCreditTransferRequest.getFrAccount().getAcctNumber(), trAmt, "RECEIVABLE", env.getProperty("bob.crncycode"), "FAILURE",
						c24ftResponse1.getError_desc().get(0), sysTraceAuditNumber,userID,
						mcCreditTransferRequest.getToAccount().getAcctNumber(),"SETTLEMENT",env.getProperty("trchtype.settl"));

				return new ResponseEntity<>(c24ftResponse, ex.getStatusCode());
			}

		} catch (HttpServerErrorException ex) {
			

			C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", SERVER_ERROR_CODE,
					Collections.singletonList(TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString()));

			//// update table
			ipsDao.updateTranCBS(sysTraceAuditNumber, mcCreditTransferRequest.getFrAccount().getAcctNumber(), trAmt, "RECEIVABLE", env.getProperty("bob.crncycode"), "FAILURE",
					TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(), sysTraceAuditNumber,userID,
					mcCreditTransferRequest.getToAccount().getAcctNumber(),"SETTLEMENT",env.getProperty("trchtype.settl"));

			return new ResponseEntity<>(c24ftResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	/*public ResponseEntity<C24FTResponse> expenseSettl(String sysTraceAuditNumber, String trRmks,
			MCCreditTransferRequest mcCreditTransferRequest, String userID) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.set("Partipant_BIC", env.getProperty("bob.bankcode"));
		httpHeaders.set("Partipant_SOL", mcCreditTransferRequest.getFrAccount().getAcctNumber().substring(0,4));
		httpHeaders.set("SYS_TRACE_AUDIT_NUMBER", sysTraceAuditNumber);

		C24FTRequest c24ftRequest = new C24FTRequest();
		C24RequestAcount c24RequestAcount = new C24RequestAcount();
		c24RequestAcount.setAcctNumber(mcCreditTransferRequest.getFrAccount().getAcctNumber());
		c24RequestAcount.setSchmType("");
		c24RequestAcount.setSettlAcctNumber(mcCreditTransferRequest.getToAccount().getAcctNumber());
		
		c24ftRequest.setAccount(c24RequestAcount);
		c24ftRequest.setCurrency_Code(env.getProperty("bob.crncycode"));
		c24ftRequest.setPAN("0000000000000000");
		c24ftRequest.setTrAmt(mcCreditTransferRequest.getTrAmt());
		c24ftRequest.setTrRmks(trRmks);

		HttpEntity<C24FTRequest> entity = new HttpEntity<>(c24ftRequest, httpHeaders);
		ResponseEntity<C24FTResponse> response = null;

		try {
			logger.info("Sending message to connect24 ");
			
			response = restTemplate.postForEntity(env.getProperty("connect24.url") + "/api/ws/dbtActfndTransfer?",
					entity, C24FTResponse.class);
			
			C24FTResponse c24ftResponse = new C24FTResponse("SUCCESS", response.getBody().getBalance(),
					response.getBody().getTranCurrency());

			//// update table
			ipsDao.updateTranCBS(sysTraceAuditNumber,mcCreditTransferRequest.getFrAccount().getAcctNumber(),
					mcCreditTransferRequest.getTrAmt(), "EXPENSE",env.getProperty("bob.crncycode"), "SUCCESS",
					"", sysTraceAuditNumber,userID,mcCreditTransferRequest.getToAccount().getAcctNumber(),"SETTLEMENT",env.getProperty("trchtype.settl"));
			

			return new ResponseEntity<>(c24ftResponse, HttpStatus.OK);
		} catch (HttpClientErrorException ex) {	
			if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", SERVER_ERROR_CODE,
						Collections.singletonList(TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString()));

				///// Update Table
				ipsDao.updateTranCBS(sysTraceAuditNumber,mcCreditTransferRequest.getFrAccount().getAcctNumber(),
						mcCreditTransferRequest.getTrAmt(), "EXPENSE",env.getProperty("bob.crncycode"),
						"FAILURE", TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(),sysTraceAuditNumber,userID
						,mcCreditTransferRequest.getToAccount().getAcctNumber(),"SETTLEMENT",env.getProperty("trchtype.settl"));
				
				return new ResponseEntity<>(c24ftResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				C24FTResponse c24ftResponse1 = new Gson().fromJson(ex.getResponseBodyAsString(), C24FTResponse.class);
				C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", c24ftResponse1.getError(),
						c24ftResponse1.getError_desc());

				//// update table
				ipsDao.updateTranCBS(sysTraceAuditNumber,mcCreditTransferRequest.getFrAccount().getAcctNumber(),
						mcCreditTransferRequest.getTrAmt(), "EXPENSE",env.getProperty("bob.crncycode"),
						"FAILURE", c24ftResponse1.getError_desc().get(0).toString(), sysTraceAuditNumber,userID
						,mcCreditTransferRequest.getToAccount().getAcctNumber(),"SETTLEMENT",env.getProperty("trchtype.settl"));
				

				return new ResponseEntity<>(c24ftResponse, ex.getStatusCode());
			}

		} catch (HttpServerErrorException ex) {

			C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", SERVER_ERROR_CODE,
					Collections.singletonList(TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString()));
			ipsDao.updateTranCBS(sysTraceAuditNumber, mcCreditTransferRequest.getFrAccount().getAcctNumber(),
					mcCreditTransferRequest.getTrAmt(), "EXPENSE", env.getProperty("bob.crncycode"), "FAILURE",
					TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(), sysTraceAuditNumber,userID
					,mcCreditTransferRequest.getToAccount().getAcctNumber(),"SETTLEMENT",env.getProperty("trchtype.settl"));
			

			return new ResponseEntity<>(c24ftResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<C24FTResponse> incomeSettl(String sysTraceAuditNumber, String trAmt, String trRmks,
			MCCreditTransferRequest mcCreditTransferRequest, String userID) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.set("Partipant_BIC", env.getProperty("bob.bankcode"));
		httpHeaders.set("Partipant_SOL",mcCreditTransferRequest.getFrAccount().getAcctNumber().substring(0, 4));
		httpHeaders.set("SYS_TRACE_AUDIT_NUMBER", sysTraceAuditNumber);

		C24FTRequest c24ftRequest = new C24FTRequest();
		C24RequestAcount c24RequestAcount = new C24RequestAcount();
		c24RequestAcount.setAcctNumber(mcCreditTransferRequest.getFrAccount().getAcctNumber());
		c24RequestAcount.setSchmType("");
		c24RequestAcount.setSettlAcctNumber(mcCreditTransferRequest.getToAccount().getAcctNumber());

		c24ftRequest.setAccount(c24RequestAcount);
		c24ftRequest.setCurrency_Code(env.getProperty("bob.crncycode"));
		c24ftRequest.setPAN("0000000000000000");
		c24ftRequest.setTrAmt(trAmt);
		c24ftRequest.setTrRmks(trRmks);
		HttpEntity<C24FTRequest> entity = new HttpEntity<>(c24ftRequest, httpHeaders);
		
		
		ResponseEntity<C24FTResponse> response = null;
		try {
			logger.info("Sending message to connect24 credit using restTemplate");
			response = restTemplate.postForEntity(env.getProperty("connect24.url") + "/api/ws/cdtActfndTransfer?",
					entity, C24FTResponse.class);

			C24FTResponse c24ftResponse = new C24FTResponse("SUCCESS", response.getBody().getBalance(),
					response.getBody().getTranCurrency());

			//// update table
			ipsDao.updateTranCBS(sysTraceAuditNumber,mcCreditTransferRequest.getFrAccount().getAcctNumber(), trAmt, "INCOME", env.getProperty("bob.crncycode"), "SUCCESS", "",
					sysTraceAuditNumber,userID,mcCreditTransferRequest.getToAccount().getAcctNumber(),"SETTLEMENT",env.getProperty("trchtype.settl"));

			return new ResponseEntity<>(c24ftResponse, HttpStatus.OK);

		} catch (HttpClientErrorException ex) {

			if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", SERVER_ERROR_CODE,
						Collections.singletonList(TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString()));

				//// update table
				ipsDao.updateTranCBS(sysTraceAuditNumber, mcCreditTransferRequest.getFrAccount().getAcctNumber(), trAmt, "INCOME", env.getProperty("bob.crncycode"), "FAILURE",
						TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(), sysTraceAuditNumber,userID,
						mcCreditTransferRequest.getToAccount().getAcctNumber(),"SETTLEMENT",env.getProperty("trchtype.settl"));
				return new ResponseEntity<>(c24ftResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				C24FTResponse c24ftResponse1 = new Gson().fromJson(ex.getResponseBodyAsString(), C24FTResponse.class);
				C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", c24ftResponse1.getError(),
						c24ftResponse1.getError_desc());

				//// update table
				ipsDao.updateTranCBS(sysTraceAuditNumber, mcCreditTransferRequest.getFrAccount().getAcctNumber(), trAmt, "INCOME", env.getProperty("bob.crncycode"), "FAILURE",
						c24ftResponse1.getError_desc().get(0), sysTraceAuditNumber,userID,
						mcCreditTransferRequest.getToAccount().getAcctNumber(),"SETTLEMENT",env.getProperty("trchtype.settl"));

				return new ResponseEntity<>(c24ftResponse, ex.getStatusCode());
			}

		} catch (HttpServerErrorException ex) {
			

			C24FTResponse c24ftResponse = new C24FTResponse("FAILURE", SERVER_ERROR_CODE,
					Collections.singletonList(TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString()));

			//// update table
			ipsDao.updateTranCBS(sysTraceAuditNumber, mcCreditTransferRequest.getFrAccount().getAcctNumber(), trAmt, "INCOME", env.getProperty("bob.crncycode"), "FAILURE",
					TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(), sysTraceAuditNumber,userID,
					mcCreditTransferRequest.getToAccount().getAcctNumber(),"SETTLEMENT",env.getProperty("trchtype.settl"));

			return new ResponseEntity<>(c24ftResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}*/



}
