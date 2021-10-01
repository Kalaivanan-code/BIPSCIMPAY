package com.bornfire.controller;

import static com.bornfire.exception.ErrorResponseCode.SERVER_ERROR_CODE;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Optional;

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

import com.bornfire.config.Listener;
import com.bornfire.entity.C24FTRequest;
import com.bornfire.entity.C24FTResponse;
import com.bornfire.entity.C24RequestAcount;
import com.bornfire.entity.CimCBSrequest;
import com.bornfire.entity.CimCBSrequestData;
import com.bornfire.entity.CimCBSrequestHeader;
import com.bornfire.entity.CimCBSresponse;
import com.bornfire.entity.SettlementAccount;
import com.bornfire.entity.TranCimCBSTable;
import com.bornfire.entity.TranCimCBSTableRep;
import com.bornfire.entity.TranMonitorStatus;
import com.google.gson.Gson;

@Component
public class CimCBSservice {
	private static final Logger logger = LoggerFactory.getLogger(CimCBSservice.class);
	
	@Autowired
	Listener listener;
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	Environment env;

	@Autowired
	TranCimCBSTableRep tranCimCBSTableRep;
	
	@Autowired
	IPSDao ipsDao;

	public ResponseEntity<CimCBSresponse> cdtFundRequest(String requestUUID) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
		
		TranCimCBSTable data=tranCimCBSTableRep.findById(requestUUID).get();
		
		CimCBSrequest cimCBSrequest=new CimCBSrequest();
		
		CimCBSrequestHeader cimCBSrequestHeader=new CimCBSrequestHeader();
		cimCBSrequestHeader.setRequestUUId(data.getRequest_uuid());
		cimCBSrequestHeader.setChannelId(data.getChannel_id());
		cimCBSrequestHeader.setServiceRequestVersion(data.getService_request_version());
		cimCBSrequestHeader.setServiceRequestId(data.getService_request_id());
		cimCBSrequestHeader.setMessageDateTime(listener.convertDateToGreDate(data.getMessage_date_time(), "2"));
		cimCBSrequestHeader.setCountryCode(env.getProperty("cimCBS.countryCode"));
		cimCBSrequest.setHeader(cimCBSrequestHeader);
		
		CimCBSrequestData cimCBSrequestData=new CimCBSrequestData();
		cimCBSrequestData.setTransactionNo(data.getTran_no());
		cimCBSrequestData.setInitiatingChannel(data.getInit_channel());
		cimCBSrequestData.setInitatorTransactionNo((data.getInit_tran_no()==null)?"":data.getInit_tran_no());
		if(data.getPost_to_cbs().equals("True")) {
			cimCBSrequestData.setPostToCBS(Boolean.TRUE);
		}else {
			cimCBSrequestData.setPostToCBS(Boolean.FALSE);
		}
		cimCBSrequestData.setTransactionType(data.getTran_type());
		cimCBSrequestData.setIsReversal(data.getIsreversal());
		cimCBSrequestData.setTransactionNoFromCBS((data.getTran_no_from_cbs()==null)?"":data.getTran_no_from_cbs());
		cimCBSrequestData.setCustomerName(data.getCustomer_name());
		cimCBSrequestData.setFromAccountNo(data.getFrom_account_no());
		cimCBSrequestData.setToAccountNo(data.getTo_account_no());
	      NumberFormat formatter = new DecimalFormat("0.##");

		cimCBSrequestData.setTransactionAmount(new BigDecimal(data.getTran_amt().toString()));
			cimCBSrequestData.setTransactionDate(new SimpleDateFormat("dd-MM-yyyy").format(data.getTran_date()));
		
		cimCBSrequestData.setTransactionCurrency(data.getTran_currency());
		cimCBSrequestData.setTransactionParticularCode(data.getTran_particular_code());
		cimCBSrequestData.setCreditRemarks((data.getCredit_remarks()==null)?"":data.getCredit_remarks());
		cimCBSrequestData.setDebitRemarks((data.getDebit_remarks()==null)?"":data.getDebit_remarks());
		cimCBSrequestData.setReservedField1((data.getResv_field_1()==null)?"":data.getResv_field_1());
		cimCBSrequestData.setReservedField2((data.getResv_field_2()==null)?"":data.getResv_field_2());
		
		cimCBSrequestData.setInitatorSubTransactionNo((data.getInit_sub_tran_no()==null)?"":data.getInit_sub_tran_no());
		cimCBSrequestData.setErrorCode((data.getError_code()==null)?"":data.getError_code());
		cimCBSrequestData.setErrorMessage((data.getError_msg()==null)?"":data.getError_msg());

		cimCBSrequest.setData(cimCBSrequestData);
		
	
		logger.debug(cimCBSrequest.toString());
		
		HttpEntity<CimCBSrequest> entity = new HttpEntity<>(cimCBSrequest, httpHeaders);
				
		ResponseEntity<CimCBSresponse> response = null;
		try {
			logger.info("Sending message to connect24 credit using restTemplate");
			response = restTemplate.postForEntity(env.getProperty("cimESB.url")+"appname="+env.getProperty("cimESB.appname")+"&prgname="+env.getProperty("cimESB.prgname")+"&arguments="+env.getProperty("cimESB.arguments"),
					entity, CimCBSresponse.class);

			
			return new ResponseEntity<>(response.getBody(), HttpStatus.OK);

		} catch (HttpClientErrorException ex) {
			CimCBSresponse cbsResponse=new CimCBSresponse();
			return new ResponseEntity<>(cbsResponse, HttpStatus.BAD_REQUEST);
		} catch (HttpServerErrorException ex) {
			CimCBSresponse cbsResponse=new CimCBSresponse();
			return new ResponseEntity<>(cbsResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}catch (Exception ex) {
			CimCBSresponse cbsResponse=new CimCBSresponse();
			return new ResponseEntity<>(cbsResponse, HttpStatus.BAD_REQUEST);
		}

	}
	
	
	public ResponseEntity<CimCBSresponse> dbtFundRequest(String requestUUID) {
		
		////Request Headers
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
		
		////Get Data from Table
		TranCimCBSTable data=tranCimCBSTableRep.findById(requestUUID).get();
		
		/////////////////////Request Body Creation/////////////////////////////
		CimCBSrequest cimCBSrequest=new CimCBSrequest();
		
		CimCBSrequestHeader cimCBSrequestHeader=new CimCBSrequestHeader();
		cimCBSrequestHeader.setRequestUUId(data.getRequest_uuid());
		cimCBSrequestHeader.setChannelId(data.getChannel_id());
		cimCBSrequestHeader.setServiceRequestVersion(data.getService_request_version());
		cimCBSrequestHeader.setServiceRequestId(data.getService_request_id());
		cimCBSrequestHeader.setMessageDateTime(listener.convertDateToGreDate(data.getMessage_date_time(), "2"));
		cimCBSrequestHeader.setCountryCode(env.getProperty("cimCBS.countryCode"));
		cimCBSrequest.setHeader(cimCBSrequestHeader);
		
		CimCBSrequestData cimCBSrequestData=new CimCBSrequestData();
		cimCBSrequestData.setTransactionNo(data.getTran_no());
		cimCBSrequestData.setInitiatingChannel(data.getInit_channel());
		cimCBSrequestData.setInitatorTransactionNo((data.getInit_tran_no()==null)?"":data.getInit_tran_no());
		if(data.getPost_to_cbs().equals("True")) {
			cimCBSrequestData.setPostToCBS(Boolean.TRUE);
		}else {
			cimCBSrequestData.setPostToCBS(Boolean.FALSE);
		}
		cimCBSrequestData.setTransactionType(data.getTran_type());
		cimCBSrequestData.setIsReversal(data.getIsreversal());
		cimCBSrequestData.setTransactionNoFromCBS((data.getTran_no_from_cbs()==null)?"":data.getTran_no_from_cbs());
		cimCBSrequestData.setCustomerName(data.getCustomer_name());
		cimCBSrequestData.setFromAccountNo(data.getFrom_account_no());
		cimCBSrequestData.setToAccountNo(data.getTo_account_no());
	      NumberFormat formatter = new DecimalFormat("0.##");

		cimCBSrequestData.setTransactionAmount(new BigDecimal(data.getTran_amt().toString()));
			cimCBSrequestData.setTransactionDate(new SimpleDateFormat("dd-MM-yyyy").format(data.getTran_date()));
		
		cimCBSrequestData.setTransactionCurrency(data.getTran_currency());
		cimCBSrequestData.setTransactionParticularCode(data.getTran_particular_code());
		cimCBSrequestData.setCreditRemarks((data.getCredit_remarks()==null)?"":data.getCredit_remarks());
		cimCBSrequestData.setDebitRemarks((data.getDebit_remarks()==null)?"":data.getDebit_remarks());
		cimCBSrequestData.setReservedField1((data.getResv_field_1()==null)?"":data.getResv_field_1());
		cimCBSrequestData.setReservedField2((data.getResv_field_2()==null)?"":data.getResv_field_2());
		
		cimCBSrequestData.setInitatorSubTransactionNo((data.getInit_sub_tran_no()==null)?"":data.getInit_sub_tran_no());
		cimCBSrequestData.setErrorCode((data.getError_code()==null)?"":data.getError_code());
		cimCBSrequestData.setErrorMessage((data.getError_msg()==null)?"":data.getError_msg());

		cimCBSrequest.setData(cimCBSrequestData);
		
		logger.debug(cimCBSrequest.toString());
	///////////////////////////////////////////////////
	
		HttpEntity<CimCBSrequest> entity = new HttpEntity<>(cimCBSrequest, httpHeaders);
		
		/////Call REST API
		ResponseEntity<CimCBSresponse> response = null;
		try {
			logger.info("Sending message to ESB Cable for Debit the Customer Amount");
			response = restTemplate.postForEntity(env.getProperty("cimESB.url")+"appname="+env.getProperty("cimESB.appname")+"&prgname="+env.getProperty("cimESB.prgname")+"&arguments="+env.getProperty("cimESB.arguments"),
					entity, CimCBSresponse.class);
			return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
		} catch (HttpClientErrorException ex) {
			CimCBSresponse cbsResponse=new CimCBSresponse();
			return new ResponseEntity<>(cbsResponse, HttpStatus.BAD_REQUEST);
		} catch (HttpServerErrorException ex) {
			CimCBSresponse cbsResponse=new CimCBSresponse();
			return new ResponseEntity<>(cbsResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}catch (Exception ex) {
			CimCBSresponse cbsResponse=new CimCBSresponse();
			return new ResponseEntity<>(cbsResponse, HttpStatus.BAD_REQUEST);
		}

	}

	
	
	public ResponseEntity<CimCBSresponse> cbsResponseSuccess(String requestUUID) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
		TranCimCBSTable data=tranCimCBSTableRep.findById(requestUUID).get();
		
		CimCBSrequest cimCBSrequest=new CimCBSrequest();
		
		CimCBSrequestHeader cimCBSrequestHeader=new CimCBSrequestHeader();
		cimCBSrequestHeader.setRequestUUId(data.getRequest_uuid());
		cimCBSrequestHeader.setChannelId(data.getChannel_id());
		cimCBSrequestHeader.setServiceRequestVersion(data.getService_request_version());
		cimCBSrequestHeader.setServiceRequestId(data.getService_request_id());
		cimCBSrequestHeader.setMessageDateTime(listener.convertDateToGreDate(data.getMessage_date_time(), "2"));
		cimCBSrequestHeader.setCountryCode(env.getProperty("cimCBS.countryCode"));
		cimCBSrequest.setHeader(cimCBSrequestHeader);
		
		CimCBSrequestData cimCBSrequestData=new CimCBSrequestData();
		cimCBSrequestData.setTransactionNo(data.getTran_no());
		cimCBSrequestData.setInitiatingChannel(data.getInit_channel());
		cimCBSrequestData.setInitatorTransactionNo(data.getInit_tran_no());
		cimCBSrequestData.setPostToCBS(Boolean.FALSE);
		cimCBSrequestData.setTransactionType("");
		cimCBSrequestData.setIsReversal("");
		cimCBSrequestData.setTransactionNoFromCBS("");
		cimCBSrequestData.setCustomerName(data.getCustomer_name());
		cimCBSrequestData.setFromAccountNo(data.getFrom_account_no());
		cimCBSrequestData.setToAccountNo(data.getTo_account_no());
		cimCBSrequestData.setTransactionAmount(new BigDecimal(data.getTran_amt().toString()));
		cimCBSrequestData.setTransactionDate(new SimpleDateFormat("yyyy-MM-dd").format(data.getTran_date()));
		cimCBSrequestData.setTransactionCurrency(data.getTran_currency());
		cimCBSrequestData.setTransactionParticularCode(data.getTran_particular_code());
		cimCBSrequestData.setCreditRemarks("");
		cimCBSrequestData.setDebitRemarks("");
		cimCBSrequestData.setReservedField1(data.getResv_field_1());
		cimCBSrequestData.setReservedField2(data.getResv_field_2());
		
		cimCBSrequestData.setInitatorSubTransactionNo((data.getInit_sub_tran_no()==null)?"":data.getInit_sub_tran_no());
		cimCBSrequestData.setErrorCode((data.getError_code()==null)?"":data.getError_code());
		cimCBSrequestData.setErrorMessage((data.getError_msg()==null)?"":data.getError_msg());

		cimCBSrequest.setData(cimCBSrequestData);
		

		logger.debug(cimCBSrequest.toString());
		HttpEntity<CimCBSrequest> entity = new HttpEntity<>(cimCBSrequest, httpHeaders);
				
		ResponseEntity<CimCBSresponse> response = null;
		try {
			logger.info("Sending message to CBS credit using restTemplate Success");
			response = restTemplate.postForEntity(env.getProperty("cimESB.url")+"appname="+env.getProperty("cimESB.appname")+"&prgname="+env.getProperty("cimESB.prgname")+"&arguments="+env.getProperty("cimESB.arguments"),
					entity, CimCBSresponse.class);

			
			return new ResponseEntity<>(response.getBody(), HttpStatus.OK);

		} catch (HttpClientErrorException ex) {
			CimCBSresponse cbsResponse=new CimCBSresponse();
			return new ResponseEntity<>(cbsResponse, HttpStatus.BAD_REQUEST);
		} catch (HttpServerErrorException ex) {
			CimCBSresponse cbsResponse=new CimCBSresponse();
			return new ResponseEntity<>(cbsResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}catch (Exception ex) {
			CimCBSresponse cbsResponse=new CimCBSresponse();
			return new ResponseEntity<>(cbsResponse, HttpStatus.BAD_REQUEST);
		}

	}
	
	public ResponseEntity<CimCBSresponse> cbsResponseFailure(String requestUUID) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
		////Get Data from Table
		TranCimCBSTable data=tranCimCBSTableRep.findById(requestUUID).get();
		
		/////////////////////Request Body Creation/////////////////////////////
		CimCBSrequest cimCBSrequest=new CimCBSrequest();
		
		CimCBSrequestHeader cimCBSrequestHeader=new CimCBSrequestHeader();
		cimCBSrequestHeader.setRequestUUId(data.getRequest_uuid());
		cimCBSrequestHeader.setChannelId(data.getChannel_id());
		cimCBSrequestHeader.setServiceRequestVersion(data.getService_request_version());
		cimCBSrequestHeader.setServiceRequestId(data.getService_request_id());
		cimCBSrequestHeader.setMessageDateTime(listener.convertDateToGreDate(data.getMessage_date_time(), "2"));
		cimCBSrequestHeader.setCountryCode(env.getProperty("cimCBS.countryCode"));
		cimCBSrequest.setHeader(cimCBSrequestHeader);
		
		CimCBSrequestData cimCBSrequestData=new CimCBSrequestData();
		cimCBSrequestData.setTransactionNo(data.getTran_no());
		cimCBSrequestData.setInitiatingChannel(data.getInit_channel());
		cimCBSrequestData.setInitatorTransactionNo((data.getInit_tran_no()==null)?"":data.getInit_tran_no());
		if(data.getPost_to_cbs().equals("True")) {
			cimCBSrequestData.setPostToCBS(Boolean.TRUE);
		}else {
			cimCBSrequestData.setPostToCBS(Boolean.FALSE);
		}
		cimCBSrequestData.setTransactionType(data.getTran_type());
		cimCBSrequestData.setIsReversal(data.getIsreversal());
		cimCBSrequestData.setTransactionNoFromCBS((data.getTran_no_from_cbs()==null)?"":data.getTran_no_from_cbs());
		cimCBSrequestData.setCustomerName(data.getCustomer_name());
		cimCBSrequestData.setFromAccountNo(data.getFrom_account_no());
		cimCBSrequestData.setToAccountNo(data.getTo_account_no());
	      NumberFormat formatter = new DecimalFormat("0.##");

		cimCBSrequestData.setTransactionAmount(new BigDecimal(data.getTran_amt().toString()));
			cimCBSrequestData.setTransactionDate(new SimpleDateFormat("dd-MM-yyyy").format(data.getTran_date()));
		
		cimCBSrequestData.setTransactionCurrency(data.getTran_currency());
		cimCBSrequestData.setTransactionParticularCode(data.getTran_particular_code());
		cimCBSrequestData.setCreditRemarks((data.getCredit_remarks()==null)?"":data.getCredit_remarks());
		cimCBSrequestData.setDebitRemarks((data.getDebit_remarks()==null)?"":data.getDebit_remarks());
		cimCBSrequestData.setReservedField1((data.getResv_field_1()==null)?"":data.getResv_field_1());
		cimCBSrequestData.setReservedField2((data.getResv_field_2()==null)?"":data.getResv_field_2());
		cimCBSrequestData.setInitatorSubTransactionNo((data.getInit_sub_tran_no()==null)?"":data.getInit_sub_tran_no());
		cimCBSrequestData.setErrorCode((data.getError_code()==null)?"":data.getError_code());
		cimCBSrequestData.setErrorMessage((data.getError_msg()==null)?"":data.getError_msg());

		
		cimCBSrequest.setData(cimCBSrequestData);
		
		logger.debug(cimCBSrequest.toString());
		logger.debug(listener.generateJsonFormat(cimCBSrequest.toString()));
	///////////////////////////////////////////////////
	
		HttpEntity<CimCBSrequest> entity = new HttpEntity<>(cimCBSrequest, httpHeaders);
		
		/////Call REST API
		ResponseEntity<CimCBSresponse> response = null;
		try {
			logger.info("Sending Failure message to ESB Cable ");
			response = restTemplate.postForEntity(env.getProperty("cimESB.url")+"appname="+env.getProperty("cimESB.appname")+"&prgname="+env.getProperty("cimESB.prgname")+"&arguments="+env.getProperty("cimESB.arguments"),
					entity, CimCBSresponse.class);
			return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
		} catch (HttpClientErrorException ex) {
			logger.debug("HttpClient"+ex.getStatusCode());
			logger.debug("Exception"+ex.getLocalizedMessage());
			CimCBSresponse cbsResponse=new CimCBSresponse();
			return new ResponseEntity<>(cbsResponse, HttpStatus.BAD_REQUEST);
		} catch (HttpServerErrorException ex) {
			logger.debug("HttpServerErrorException"+ex.getStatusCode());
			logger.debug("Exception"+ex.getLocalizedMessage());
			CimCBSresponse cbsResponse=new CimCBSresponse();
			return new ResponseEntity<>(cbsResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}catch (Exception ex) {
			logger.debug("Exception"+ex.getLocalizedMessage());
			CimCBSresponse cbsResponse=new CimCBSresponse();
			return new ResponseEntity<>(cbsResponse, HttpStatus.BAD_REQUEST);
		}

	}



}
