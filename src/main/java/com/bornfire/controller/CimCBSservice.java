package com.bornfire.controller;

import static com.bornfire.exception.ErrorResponseCode.SERVER_ERROR_CODE;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
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
import com.bornfire.config.SequenceGenerator;
import com.bornfire.entity.C24FTRequest;
import com.bornfire.entity.C24FTResponse;
import com.bornfire.entity.C24RequestAcount;
import com.bornfire.entity.CimCBSCustDocData;
import com.bornfire.entity.CimCBSCustDocHeader;
import com.bornfire.entity.CimCBSCustDocRequest;
import com.bornfire.entity.CimCBSCustDocResponse;
import com.bornfire.entity.CimCBSrequest;
import com.bornfire.entity.CimCBSrequestData;
import com.bornfire.entity.CimCBSrequestGL;
import com.bornfire.entity.CimCBSrequestGLData;
import com.bornfire.entity.CimCBSrequestGLDataTranDet;
import com.bornfire.entity.CimCBSrequestGLHeader;
import com.bornfire.entity.CimCBSrequestHeader;
import com.bornfire.entity.CimCBSresponse;
import com.bornfire.entity.CimGLresponse;
import com.bornfire.entity.SettlementAccount;
import com.bornfire.entity.TranCimCBSTable;
import com.bornfire.entity.TranCimCBSTableRep;
import com.bornfire.entity.TranCimGLRep;
import com.bornfire.entity.TranCimGLTable;
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
	
	@Autowired
	TranCimGLRep tranCimGlRep;
	
	@Autowired
	SequenceGenerator sequence;

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
		cimCBSrequestHeader.setMessageDateTime(listener.convertDateToGreDate(data.getMessage_date_time(), "2").toString());
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
			//cimCBSrequestData.setTransactionDate(new SimpleDateFormat("dd-MM-yyyy").format(data.getTran_date()));
			cimCBSrequestData.setTransactionDate(new SimpleDateFormat("yyyy-MM-dd").format(data.getTran_date()));

		cimCBSrequestData.setTransactionCurrency(data.getTran_currency());
		cimCBSrequestData.setTransactionParticularCode(data.getTran_particular_code());
		cimCBSrequestData.setCreditRemarks((data.getCredit_remarks()==null)?"":data.getCredit_remarks());
		cimCBSrequestData.setDebitRemarks((data.getDebit_remarks()==null)?"":data.getDebit_remarks());
		cimCBSrequestData.setReservedField1((data.getResv_field_1()==null)?"":data.getResv_field_1());
		cimCBSrequestData.setReservedField2((data.getResv_field_2()==null)?"":data.getResv_field_2());
		
		cimCBSrequestData.setInitatorSubTransactionNo((data.getInit_sub_tran_no()==null)?"":data.getInit_sub_tran_no());
		cimCBSrequestData.setErrorCode((data.getError_code()==null)?"":data.getError_code());
		cimCBSrequestData.setErrorMessage((data.getError_msg()==null)?"":data.getError_msg());
		cimCBSrequestData.setIpsMasterRefId((data.getIps_master_ref_id()==null)?"":data.getIps_master_ref_id());

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
			logger.debug("HttpClient"+ex.getStatusCode());
			logger.debug("Exception"+ex.getLocalizedMessage());
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
		cimCBSrequestHeader.setMessageDateTime(listener.convertDateToGreDate(data.getMessage_date_time(), "2").toString());
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
		cimCBSrequestData.setTransactionDate(new SimpleDateFormat("yyyy-MM-dd").format(data.getTran_date()));
		//cimCBSrequestData.setTransactionDate(listener.convertDateToGreDate(data.getTran_date(),"2"));
		
		cimCBSrequestData.setTransactionCurrency(data.getTran_currency());
		cimCBSrequestData.setTransactionParticularCode(data.getTran_particular_code());
		cimCBSrequestData.setCreditRemarks((data.getCredit_remarks()==null)?"":data.getCredit_remarks());
		cimCBSrequestData.setDebitRemarks((data.getDebit_remarks()==null)?"":data.getDebit_remarks());
		cimCBSrequestData.setReservedField1((data.getResv_field_1()==null)?"":data.getResv_field_1());
		cimCBSrequestData.setReservedField2((data.getResv_field_2()==null)?"":data.getResv_field_2());
		
		cimCBSrequestData.setInitatorSubTransactionNo((data.getInit_sub_tran_no()==null)?"":data.getInit_sub_tran_no());
		cimCBSrequestData.setErrorCode((data.getError_code()==null)?"":data.getError_code());
		cimCBSrequestData.setErrorMessage((data.getError_msg()==null)?"":data.getError_msg());
		cimCBSrequestData.setIpsMasterRefId((data.getIps_master_ref_id()==null)?"":data.getIps_master_ref_id());
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
			logger.debug("HttpClient"+ex.getStatusCode());
			logger.debug("Exception"+ex.getLocalizedMessage());
			logger.info("HTTP Client Error:"+ex.getLocalizedMessage()+"-"+ex.getStatusCode());
			CimCBSresponse cbsResponse=new CimCBSresponse();
			return new ResponseEntity<>(cbsResponse, HttpStatus.BAD_REQUEST);
		} catch (HttpServerErrorException ex) {
			CimCBSresponse cbsResponse=new CimCBSresponse();
			return new ResponseEntity<>(cbsResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}catch (Exception ex) {
			logger.info("HTTP Ex Error:"+ex.getLocalizedMessage());
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
		cimCBSrequestHeader.setMessageDateTime(listener.convertDateToGreDate(data.getMessage_date_time(), "2").toString());
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
		cimCBSrequestData.setIpsMasterRefId((data.getIps_master_ref_id()==null)?"":data.getIps_master_ref_id());
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
			logger.debug("HttpClient"+ex.getStatusCode());
			logger.debug("Exception"+ex.getLocalizedMessage());
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
		cimCBSrequestHeader.setMessageDateTime(listener.convertDateToGreDate(data.getMessage_date_time(), "2").toString());
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
			//cimCBSrequestData.setTransactionDate(new SimpleDateFormat("dd-MM-yyyy").format(data.getTran_date()));
		cimCBSrequestData.setTransactionDate(new SimpleDateFormat("yyyy-MM-dd").format(data.getTran_date()));

		cimCBSrequestData.setTransactionCurrency(data.getTran_currency());
		cimCBSrequestData.setTransactionParticularCode(data.getTran_particular_code());
		cimCBSrequestData.setCreditRemarks((data.getCredit_remarks()==null)?"":data.getCredit_remarks());
		cimCBSrequestData.setDebitRemarks((data.getDebit_remarks()==null)?"":data.getDebit_remarks());
		cimCBSrequestData.setReservedField1((data.getResv_field_1()==null)?"":data.getResv_field_1());
		cimCBSrequestData.setReservedField2((data.getResv_field_2()==null)?"":data.getResv_field_2());
		cimCBSrequestData.setInitatorSubTransactionNo((data.getInit_sub_tran_no()==null)?"":data.getInit_sub_tran_no());
		cimCBSrequestData.setErrorCode((data.getError_code()==null)?"":data.getError_code());
		cimCBSrequestData.setErrorMessage((data.getError_msg()==null)?"":data.getError_msg());
		cimCBSrequestData.setIpsMasterRefId((data.getIps_master_ref_id()==null)?"":data.getIps_master_ref_id());
		
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


	public ResponseEntity<CimGLresponse> postPaymentGLInc(String requestUUID) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
		////Get Data from Table
		List<TranCimGLTable> data=tranCimGlRep.getRequestUUIDData(requestUUID);
		
		/////////////////////Request Body Creation/////////////////////////////
		CimCBSrequestGL cimCBSrequest=new CimCBSrequestGL();
		
		CimCBSrequestGLHeader cimCBSrequestHeader=new CimCBSrequestGLHeader();
		cimCBSrequestHeader.setRequestUUId(data.get(0).getRequest_uuid());
		cimCBSrequestHeader.setChannelId(data.get(0).getChannel_id());
		cimCBSrequestHeader.setServiceRequestVersion(data.get(0).getService_request_version());
		cimCBSrequestHeader.setServiceRequestId(data.get(0).getService_request_id());
		cimCBSrequestHeader.setMessageDateTime(listener.convertDateToGreDate(data.get(0).getMessage_date_time(), "2").toString());
		cimCBSrequestHeader.setCountryCode(data.get(0).getCountry_code());
		cimCBSrequest.setHeader(cimCBSrequestHeader);
		
		CimCBSrequestGLData cimCBSrequestData=new CimCBSrequestGLData();
		cimCBSrequestData.setTransactionNo(data.get(0).getTran_no());
		cimCBSrequestData.setBatchNo(data.get(0).getBatch_no());
		cimCBSrequestData.setModule(data.get(0).getModule());
				
		List<CimCBSrequestGLDataTranDet> tranDetails=new ArrayList<CimCBSrequestGLDataTranDet>();
		 
		for(TranCimGLTable data1 :data) {
			CimCBSrequestGLDataTranDet tranData=new CimCBSrequestGLDataTranDet();
			tranData.setSerialNumber(Integer.parseInt(data1.getSrl_no1()));
			tranData.setTransactionType(data1.getTran_type1());
			tranData.setAccountNo(data1.getAcct_no1());
			tranData.setAccountType(data1.getAcct_type1());
			tranData.setTransactionAmount(data1.getTran_amt1().toString());
			tranData.setCurrencyCode(data1.getCurrency_code1());
			//tranData.setPostingDate(new SimpleDateFormat("yyyy-MM-dd").format(data1.getPosting_date1()));
			tranData.setPostingDate(listener.convertDateToGreDate(data1.getPosting_date1(),"3").toString());
			
			tranData.setTransactionCode(data1.getTran_code1());
			tranData.setTransactionDescription(data1.getTran_desc1());
			tranData.setTransactionRemarks(data1.getTran_remarks1());
			tranData.setRate(data1.getRate1());
			tranDetails.add(tranData);
		}
		cimCBSrequestData.setTransactionDetails(tranDetails);
		
		cimCBSrequest.setData(cimCBSrequestData);
		
		logger.debug(cimCBSrequest.toString());
		//logger.debug(listener.generateJsonFormat1(cimCBSrequest));
	///////////////////////////////////////////////////
	
		HttpEntity<CimCBSrequestGL> entity = new HttpEntity<>(cimCBSrequest, httpHeaders);
		
		/////Call REST API
		ResponseEntity<CimGLresponse> response = null;
		try {
		
			response = restTemplate.postForEntity(env.getProperty("cimESBGL.url")+"appname="+env.getProperty("cimESBGL.appname")+"&prgname="+env.getProperty("cimESBGL.prgname")+"&arguments="+env.getProperty("cimESBGL.arguments"),
					entity, CimGLresponse.class);
			logger.debug("Done");
			return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
		} catch (HttpClientErrorException ex) {
			logger.debug("HttpClient"+ex.getStatusCode());
			logger.debug("Exception"+ex.getLocalizedMessage());
			CimGLresponse cbsResponse=new CimGLresponse();
			return new ResponseEntity<>(cbsResponse, HttpStatus.BAD_REQUEST);
		} catch (HttpServerErrorException ex) {
			logger.debug("HttpServerErrorException"+ex.getStatusCode());
			logger.debug("Exception"+ex.getLocalizedMessage());
			CimGLresponse cbsResponse=new CimGLresponse();
			return new ResponseEntity<>(cbsResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}catch (Exception ex) {
			logger.debug("Exception"+ex.getLocalizedMessage());
			CimGLresponse cbsResponse=new CimGLresponse();
			return new ResponseEntity<>(cbsResponse, HttpStatus.BAD_REQUEST);
		}

	}

	
	public ResponseEntity<CimCBSCustDocResponse> custDocType(String agreementNumber) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
		/////////////////////Request Body Creation/////////////////////////////
		CimCBSCustDocRequest cimCBSrequest=new CimCBSCustDocRequest();
		
		///Get Request UUID
		CimCBSCustDocHeader cimCBSrequestHeader=new CimCBSCustDocHeader();
		cimCBSrequestHeader.setRequestUUId(sequence.generateRequestUUId());
		cimCBSrequestHeader.setChannelId(env.getProperty("cimESBCustType.channelID"));
		cimCBSrequestHeader.setServiceRequestVersion(env.getProperty("cimESBCustType.servicereqversion"));
		cimCBSrequestHeader.setServiceRequestId(env.getProperty("cimESBCustType.servicereqID"));
		cimCBSrequestHeader.setMessageDateTime(listener.convertDateToGreDate(new Date(), "3").toString());
		cimCBSrequestHeader.setLanguageId(env.getProperty("cimESBCustType.languageID"));
		cimCBSrequestHeader.setCountryCode("MU");
		cimCBSrequest.setHeader(cimCBSrequestHeader);
		
		CimCBSCustDocData cimCBSrequestData=new CimCBSCustDocData();
		cimCBSrequestData.setAgreementNumber(agreementNumber);
		cimCBSrequest.setData(cimCBSrequestData);
		
		logger.debug(cimCBSrequest.toString());
		logger.debug(listener.generateJsonFormat2(cimCBSrequest));
	///////////////////////////////////////////////////
	
		HttpEntity<CimCBSCustDocRequest> entity = new HttpEntity<>(cimCBSrequest, httpHeaders);
		
		/////Call REST API
		ResponseEntity<CimCBSCustDocResponse> response = null;
		try {
		
			response = restTemplate.postForEntity(env.getProperty("cimESBCustType.url")+"appname="+env.getProperty("cimESBCustType.appname")+"&prgname="+env.getProperty("cimESBCustType.prgname")+"&arguments="+env.getProperty("cimESBCustType.arguments"),
					entity, CimCBSCustDocResponse.class);
			logger.debug("Done");
			return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
		} catch (HttpClientErrorException ex) {
			logger.debug("HttpClient"+ex.getStatusCode());
			logger.debug("Exception"+ex.getLocalizedMessage());
			CimCBSCustDocResponse cbsResponse=new CimCBSCustDocResponse();
			return new ResponseEntity<>(cbsResponse, HttpStatus.BAD_REQUEST);
		} catch (HttpServerErrorException ex) {
			logger.debug("HttpServerErrorException"+ex.getStatusCode());
			logger.debug("Exception"+ex.getLocalizedMessage());
			CimCBSCustDocResponse cbsResponse=new CimCBSCustDocResponse();
			return new ResponseEntity<>(cbsResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}catch (Exception ex) {
			logger.debug("Exception"+ex.getLocalizedMessage());
			CimCBSCustDocResponse cbsResponse=new CimCBSCustDocResponse();
			return new ResponseEntity<>(cbsResponse, HttpStatus.BAD_REQUEST);
		}

	}


}
