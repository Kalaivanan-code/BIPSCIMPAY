package com.bornfire.controller;

import static com.bornfire.exception.ErrorResponseCode.SERVER_ERROR_CODE;

import java.util.Arrays;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.bornfire.config.ErrorResponseCode;
import com.bornfire.entity.Account;
import com.bornfire.entity.AccountContactResponse;
import com.bornfire.entity.AccountListResponse;
import com.bornfire.entity.AccountsListAccounts;
import com.bornfire.entity.C24FTRequest;
import com.bornfire.entity.C24FTResponse;
import com.bornfire.entity.C24RequestAcount;
import com.bornfire.entity.ConsentAccessRequest;
import com.bornfire.entity.ConsentAccessResponse;
import com.bornfire.entity.ConsentAccountBalance;
import com.bornfire.entity.ConsentOutwardAccessAuthRequest;
import com.bornfire.entity.ConsentOutwardAccessRequest;
import com.bornfire.entity.SCAAthenticationResponse;
import com.bornfire.entity.SCAAuthenticatedData;
import com.bornfire.entity.SettlementAccount;
import com.bornfire.entity.TranMonitorStatus;
import com.bornfire.entity.TransactionListResponse;
import com.bornfire.exception.ErrorRestResponse;
import com.google.gson.Gson;

@Component
public class ConsentIPSXservice {
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	ErrorResponseCode errorCode;

	private static final Logger logger = LoggerFactory.getLogger(ConsentIPSXservice.class);

	public ResponseEntity<Object> accountConsent(String x_request_id, String psuDeviceID,
			String psuIPAddress, String psuID, String psuIDCountry, String psuIDType,
			ConsentOutwardAccessRequest consentOutwardAccessRequest, String sender_participant_bic,
			String sender_participant_member_id, String receiver_participant_bic,
			String receiver_participant_member_id,String publicKey) {
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
		httpHeaders.set("X-Request-ID",x_request_id );
		httpHeaders.set("SenderParticipant-BIC", sender_participant_bic);
		httpHeaders.set("SenderParticipant-MemberID", sender_participant_member_id);
		httpHeaders.set("ReceiverParticipant-BIC", receiver_participant_bic);
		httpHeaders.set("ReceiverParticipant-MemberID", receiver_participant_member_id);
		httpHeaders.set("PSU-Device-ID", psuDeviceID);
		httpHeaders.set("PSU-IP-Address", psuIPAddress);
		httpHeaders.set("PSU-ID", psuID);
		httpHeaders.set("PSU-ID-Country", psuIDCountry);
		httpHeaders.set("PSU-ID-Type", psuIDType);


		ConsentAccessRequest consentAccessRequest = new ConsentAccessRequest();
		
		Account account = new Account();
		account.setIdentification(consentOutwardAccessRequest.getAccounts().getAccountNumber());
		account.setSchemeName(consentOutwardAccessRequest.getAccounts().getShemeName());
		
		consentAccessRequest.setPhoneNumber(consentOutwardAccessRequest.getPhoneNumber());
		consentAccessRequest.setPublicKey(publicKey);
		consentAccessRequest.setAccount(Arrays.asList(account));
		consentAccessRequest.setPermissions(Arrays.asList(TranMonitorStatus.ReadBalances.toString(),TranMonitorStatus.ReadTransactionsDetails.toString(),
				TranMonitorStatus.ReadAccountsDetails.toString(),TranMonitorStatus.DebitAccount.toString()));

		HttpEntity<ConsentAccessRequest> entity = new HttpEntity<>(consentAccessRequest, httpHeaders);
		ResponseEntity<ConsentAccessResponse> response = null;

		try {
			logger.info("Sending consent Access message to ipsx Rest WebService");
			
			response = restTemplate.postForEntity("/api/ws/dbtActfndTransfer?",
					entity, ConsentAccessResponse.class);
			
			if(response.getStatusCode().equals(HttpStatus.OK)){
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			}else{
				ErrorRestResponse c24ftResponse = new ErrorRestResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("23").split(":")[0]),
						errorCode.ErrorCodeRegistration("23").split(":")[1]);
				return new ResponseEntity<>(c24ftResponse, HttpStatus.NO_CONTENT);
			}
			
		} catch (HttpClientErrorException ex) {
			
			logger.info("HttpClientErrorException --------->");
			logger.info("HttpClientStatusCode --------->"+ex.getStatusCode());
			
			if (ex.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
				logger.info("HttpClientErrorException --------->Bad Request");
				ErrorRestResponse errorRestResponse = new Gson().fromJson(ex.getResponseBodyAsString().toString(), ErrorRestResponse.class);
				return new ResponseEntity<>(errorRestResponse, HttpStatus.BAD_REQUEST);
			} else if(ex.getStatusCode().equals(HttpStatus.NO_CONTENT)){
				logger.info("HttpClientErrorException --------->No Content");
				ErrorRestResponse c24ftResponse = new ErrorRestResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("23").split(":")[0]),
						errorCode.ErrorCodeRegistration("23").split(":")[1]);
				return new ResponseEntity<>(c24ftResponse, HttpStatus.NO_CONTENT);
			}else if(ex.getStatusCode().equals(HttpStatus.UNAUTHORIZED)){
				logger.info("HttpClientErrorException --------->UnAuthorised");
				ErrorRestResponse c24ftResponse = new ErrorRestResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("24").split(":")[0]),
						errorCode.ErrorCodeRegistration("24").split(":")[1]);
				return new ResponseEntity<>(c24ftResponse, HttpStatus.UNAUTHORIZED);
			}else {
				logger.info("HttpClientErrorException --------->Other");
				ErrorRestResponse c24ftResponse = new ErrorRestResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("25").split(":")[0]),
						errorCode.ErrorCodeRegistration("25").split(":")[1]);
				return new ResponseEntity<>(c24ftResponse, ex.getStatusCode());
			}

		} catch (HttpServerErrorException ex) {
			logger.info("HttpServerErrorException --------->");
			
			ErrorRestResponse c24ftResponse = new ErrorRestResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("23").split(":")[0]),
					errorCode.ErrorCodeRegistration("23").split(":")[1]);
			return new ResponseEntity<>(c24ftResponse, ex.getStatusCode());	
			
		}catch (Exception ex) {
			logger.info("HttpException --------->");
			
			ErrorRestResponse c24ftResponse = new ErrorRestResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("25").split(":")[0]),
					errorCode.ErrorCodeRegistration("25").split(":")[1]);
			return new ResponseEntity<>(c24ftResponse,HttpStatus.INTERNAL_SERVER_ERROR);
			
		}

	}

	public ResponseEntity<Object> accountConsentAuthorisation(String w_request_id, String psuDeviceID,
			String psuIPAddress, String psuID, String psuIDCountry, String psuIDType,
			ConsentOutwardAccessAuthRequest consentOutwardAccessAuthRequest, String sender_participant_bic,
			String sender_participant_member_id, String receiver_participant_bic, String receiver_participant_member_id,
			String cryptogram, String consentID, String authID) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
		httpHeaders.set("X-Request-ID",w_request_id );
		httpHeaders.set("SenderParticipant-BIC", sender_participant_bic);
		httpHeaders.set("SenderParticipant-MemberID", sender_participant_member_id);
		httpHeaders.set("ReceiverParticipant-BIC", receiver_participant_bic);
		httpHeaders.set("ReceiverParticipant-MemberID", receiver_participant_member_id);
		httpHeaders.set("PSU-Device-ID", psuDeviceID);
		httpHeaders.set("PSU-IP-Address", psuIPAddress);
		httpHeaders.set("PSU-ID", psuID);
		httpHeaders.set("PSU-ID-Country", psuIDCountry);
		httpHeaders.set("PSU-ID-Type", psuIDType);
		httpHeaders.set("Cryptogram", cryptogram);


		SCAAuthenticatedData sCAAuthenticatedData = new SCAAuthenticatedData();
		sCAAuthenticatedData.setScaAuthenticationData(consentOutwardAccessAuthRequest.getSCAData());
		HttpEntity<SCAAuthenticatedData> entity = new HttpEntity<>(sCAAuthenticatedData, httpHeaders);
		ResponseEntity<SCAAthenticationResponse> response = null;
		

		try {
			logger.info("Sending message to ipsx Rest WebService Authorisation");
			
			   response = restTemplate.exchange("/api/ws/dbtActfndTransfer?", HttpMethod.PUT, entity, SCAAthenticationResponse.class);

			/*response = restTemplate.postForEntity("/api/ws/dbtActfndTransfer?",
					entity, SCAAthenticationResponse.class);*/
			
			if(response.getStatusCode().equals(HttpStatus.OK)) {
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			}else {
				ErrorRestResponse c24ftResponse = new ErrorRestResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("23").split(":")[0]),
						errorCode.ErrorCodeRegistration("23").split(":")[1]);
				return new ResponseEntity<>(c24ftResponse, HttpStatus.NO_CONTENT);
			}
			
		} catch (HttpClientErrorException ex) {
			
			logger.info("HttpClientErrorException --------->");
			logger.info("HttpClientStatusCode --------->"+ex.getStatusCode());
			
			if (ex.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
				logger.info("HttpClientErrorException --------->Bad Request");
				ErrorRestResponse errorRestResponse = new Gson().fromJson(ex.getResponseBodyAsString().toString(), ErrorRestResponse.class);
				return new ResponseEntity<>(errorRestResponse, HttpStatus.BAD_REQUEST);
			} else if(ex.getStatusCode().equals(HttpStatus.NO_CONTENT)){
				logger.info("HttpClientErrorException --------->No Content");
				ErrorRestResponse c24ftResponse = new ErrorRestResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("23").split(":")[0]),
						errorCode.ErrorCodeRegistration("23").split(":")[1]);
				return new ResponseEntity<>(c24ftResponse, ex.getStatusCode());
			}else if(ex.getStatusCode().equals(HttpStatus.UNAUTHORIZED)){
				logger.info("HttpClientErrorException --------->UnAuthorised");
				ErrorRestResponse c24ftResponse = new ErrorRestResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("24").split(":")[0]),
						errorCode.ErrorCodeRegistration("24").split(":")[1]);
				return new ResponseEntity<>(c24ftResponse, ex.getStatusCode());
			}else {
				logger.info("HttpClientErrorException --------->Other");
				ErrorRestResponse c24ftResponse = new ErrorRestResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("25").split(":")[0]),
						errorCode.ErrorCodeRegistration("25").split(":")[1]);
				return new ResponseEntity<>(c24ftResponse, ex.getStatusCode());
			}

		} catch (HttpServerErrorException ex) {
			logger.info("HttpServerErrorException --------->");
			
			ErrorRestResponse c24ftResponse = new ErrorRestResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("23").split(":")[0]),
					errorCode.ErrorCodeRegistration("23").split(":")[1]);
			return new ResponseEntity<>(c24ftResponse, ex.getStatusCode());	
			
		}catch (Exception ex) {
			logger.info("HttpException --------->");
			
			ErrorRestResponse c24ftResponse = new ErrorRestResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("25").split(":")[0]),
					errorCode.ErrorCodeRegistration("25").split(":")[1]);
			return new ResponseEntity<>(c24ftResponse,HttpStatus.INTERNAL_SERVER_ERROR);
			
		}

	}

	public ResponseEntity<Object> deleteAccountConsent(String x_request_id, String psuDeviceID, String psuIPAddress,
			String psuID, String psuIDCountry, String psuIDType, String sender_participant_bic,
			String sender_participant_member_id, String receiver_participant_bic, String receiver_participant_member_id,
			String consentID) {
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
		httpHeaders.set("X-Request-ID",x_request_id );
		httpHeaders.set("SenderParticipant-BIC", sender_participant_bic);
		httpHeaders.set("SenderParticipant-MemberID", sender_participant_member_id);
		httpHeaders.set("ReceiverParticipant-BIC", receiver_participant_bic);
		httpHeaders.set("ReceiverParticipant-MemberID", receiver_participant_member_id);
		httpHeaders.set("PSU-Device-ID", psuDeviceID);
		httpHeaders.set("PSU-IP-Address", psuIPAddress);
		httpHeaders.set("PSU-ID", psuID);
		httpHeaders.set("PSU-ID-Country", psuIDCountry);
		httpHeaders.set("PSU-ID-Type", psuIDType);


		HttpEntity<String> entity = new HttpEntity<>("", httpHeaders);
		ResponseEntity<String> response = null;
		

		try {
			logger.info("Sending message to ipsx Rest WebService Delete Account Consent");
			
		   response = restTemplate.exchange("/account- consents/"+consentID, HttpMethod.DELETE, entity, String.class);

			/*response = restTemplate.post("/api/ws/dbtActfndTransfer?",
					entity, String.class);*/
			
			if(response.getStatusCode().equals(HttpStatus.OK)) {
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			}else {
				ErrorRestResponse c24ftResponse = new ErrorRestResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("23").split(":")[0]),
						errorCode.ErrorCodeRegistration("23").split(":")[1]);
				return new ResponseEntity<>(c24ftResponse, HttpStatus.NO_CONTENT);
			}
			
		} catch (HttpClientErrorException ex) {
			
			logger.info("HttpClientErrorException --------->");
			logger.info("HttpClientStatusCode --------->"+ex.getStatusCode());
			
			if (ex.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
				logger.info("HttpClientErrorException --------->Bad Request");
				ErrorRestResponse errorRestResponse = new Gson().fromJson(ex.getResponseBodyAsString().toString(), ErrorRestResponse.class);
				return new ResponseEntity<>(errorRestResponse, HttpStatus.BAD_REQUEST);
			} else if(ex.getStatusCode().equals(HttpStatus.NO_CONTENT)){
				logger.info("HttpClientErrorException --------->No Content");
				ErrorRestResponse c24ftResponse = new ErrorRestResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("23").split(":")[0]),
						errorCode.ErrorCodeRegistration("23").split(":")[1]);
				return new ResponseEntity<>(c24ftResponse, ex.getStatusCode());
			}else if(ex.getStatusCode().equals(HttpStatus.UNAUTHORIZED)){
				logger.info("HttpClientErrorException --------->UnAuthorised");
				ErrorRestResponse c24ftResponse = new ErrorRestResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("24").split(":")[0]),
						errorCode.ErrorCodeRegistration("24").split(":")[1]);
				return new ResponseEntity<>(c24ftResponse, ex.getStatusCode());
			}else {
				logger.info("HttpClientErrorException --------->Other");
				ErrorRestResponse c24ftResponse = new ErrorRestResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("25").split(":")[0]),
						errorCode.ErrorCodeRegistration("25").split(":")[1]);
				return new ResponseEntity<>(c24ftResponse, ex.getStatusCode());
			}

		} catch (HttpServerErrorException ex) {
			logger.info("HttpServerErrorException --------->");
			
			ErrorRestResponse c24ftResponse = new ErrorRestResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("23").split(":")[0]),
					errorCode.ErrorCodeRegistration("23").split(":")[1]);
			return new ResponseEntity<>(c24ftResponse, ex.getStatusCode());	
			
		}catch (Exception ex) {
			logger.info("HttpException --------->");
			
			ErrorRestResponse c24ftResponse = new ErrorRestResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("25").split(":")[0]),
					errorCode.ErrorCodeRegistration("25").split(":")[1]);
			return new ResponseEntity<>(c24ftResponse,HttpStatus.INTERNAL_SERVER_ERROR);
			
		}

	}

	public ResponseEntity<Object> accountConsentBalances(String x_request_id, String psuDeviceID, String psuIPAddress,
			String psuID, String psuIDCountry, String psuIDType, String sender_participant_bic,
			String sender_participant_member_id, String receiver_participant_bic, String receiver_participant_member_id,
			String consentID, String accountID, String cryptogram) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
		httpHeaders.set("X-Request-ID",x_request_id );
		httpHeaders.set("SenderParticipant-BIC", sender_participant_bic);
		httpHeaders.set("SenderParticipant-MemberID", sender_participant_member_id);
		httpHeaders.set("ReceiverParticipant-BIC", receiver_participant_bic);
		httpHeaders.set("ReceiverParticipant-MemberID", receiver_participant_member_id);
		httpHeaders.set("PSU-Device-ID", psuDeviceID);
		httpHeaders.set("PSU-IP-Address", psuIPAddress);
		httpHeaders.set("PSU-ID", psuID);
		httpHeaders.set("PSU-ID-Country", psuIDCountry);
		httpHeaders.set("PSU-ID-Type", psuIDType);
		httpHeaders.set("Consent-ID", consentID);
		httpHeaders.set("Cryptogram", cryptogram);


		HttpEntity<String> entity = new HttpEntity<>("", httpHeaders);
		ResponseEntity<ConsentAccountBalance> response = null;
		

		try {
			logger.info("Sending message to ipsx Rest WebService Balance Account ");
			
		   response = restTemplate.exchange("/accounts/"+accountID+"/balances", HttpMethod.GET, entity, ConsentAccountBalance.class);

			/*response = restTemplate.post("/api/ws/dbtActfndTransfer?",
					entity, String.class);*/
			
			if(response.getStatusCode().equals(HttpStatus.OK)) {
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			}else {
				ErrorRestResponse c24ftResponse = new ErrorRestResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("23").split(":")[0]),
						errorCode.ErrorCodeRegistration("23").split(":")[1]);
				return new ResponseEntity<>(c24ftResponse, HttpStatus.NO_CONTENT);
			}
			
		} catch (HttpClientErrorException ex) {
			
			logger.info("HttpClientErrorException --------->");
			logger.info("HttpClientStatusCode --------->"+ex.getStatusCode());
			
			if (ex.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
				logger.info("HttpClientErrorException --------->Bad Request");
				ErrorRestResponse errorRestResponse = new Gson().fromJson(ex.getResponseBodyAsString().toString(), ErrorRestResponse.class);
				return new ResponseEntity<>(errorRestResponse, HttpStatus.BAD_REQUEST);
			} else if(ex.getStatusCode().equals(HttpStatus.NO_CONTENT)){
				logger.info("HttpClientErrorException --------->No Content");
				ErrorRestResponse c24ftResponse = new ErrorRestResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("23").split(":")[0]),
						errorCode.ErrorCodeRegistration("23").split(":")[1]);
				return new ResponseEntity<>(c24ftResponse, ex.getStatusCode());
			}else if(ex.getStatusCode().equals(HttpStatus.UNAUTHORIZED)){
				logger.info("HttpClientErrorException --------->UnAuthorised");
				ErrorRestResponse c24ftResponse = new ErrorRestResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("24").split(":")[0]),
						errorCode.ErrorCodeRegistration("24").split(":")[1]);
				return new ResponseEntity<>(c24ftResponse, ex.getStatusCode());
			}else {
				logger.info("HttpClientErrorException --------->Other");
				ErrorRestResponse c24ftResponse = new ErrorRestResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("25").split(":")[0]),
						errorCode.ErrorCodeRegistration("25").split(":")[1]);
				return new ResponseEntity<>(c24ftResponse, ex.getStatusCode());
			}

		} catch (HttpServerErrorException ex) {
			logger.info("HttpServerErrorException --------->");
			
			ErrorRestResponse c24ftResponse = new ErrorRestResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("23").split(":")[0]),
					errorCode.ErrorCodeRegistration("23").split(":")[1]);
			return new ResponseEntity<>(c24ftResponse, ex.getStatusCode());	
			
		}catch (Exception ex) {
			logger.info("HttpException --------->");
			
			ErrorRestResponse c24ftResponse = new ErrorRestResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("25").split(":")[0]),
					errorCode.ErrorCodeRegistration("25").split(":")[1]);
			return new ResponseEntity<>(c24ftResponse,HttpStatus.INTERNAL_SERVER_ERROR);
			
		}

	}

	public ResponseEntity<Object> accountConsentTransaction(String x_request_id, String psuDeviceID,
			String psuIPAddress, String psuID, String psuIDCountry, String psuIDType, String sender_participant_bic,
			String sender_participant_member_id, String receiver_participant_bic, String receiver_participant_member_id,
			String consentID, String accountID, String cryptogram,String fromBookingDateTime,String toBookingDateTime) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
		httpHeaders.set("X-Request-ID",x_request_id );
		httpHeaders.set("SenderParticipant-BIC", sender_participant_bic);
		httpHeaders.set("SenderParticipant-MemberID", sender_participant_member_id);
		httpHeaders.set("ReceiverParticipant-BIC", receiver_participant_bic);
		httpHeaders.set("ReceiverParticipant-MemberID", receiver_participant_member_id);
		httpHeaders.set("PSU-Device-ID", psuDeviceID);
		httpHeaders.set("PSU-IP-Address", psuIPAddress);
		httpHeaders.set("PSU-ID", psuID);
		httpHeaders.set("PSU-ID-Country", psuIDCountry);
		httpHeaders.set("PSU-ID-Type", psuIDType);
		httpHeaders.set("Consent-ID", consentID);
		httpHeaders.set("Cryptogram", cryptogram);


		HttpEntity<String> entity = new HttpEntity<>("", httpHeaders);
		ResponseEntity<TransactionListResponse> response = null;
		

		try {
			logger.info("Sending message to ipsx Rest WebService Transaction Inquiry ");
			
			String url="";
			
			if(fromBookingDateTime!=null && toBookingDateTime!=null) {
				url="/accounts/"+accountID+"/transactions?fromBookingDateTime="+fromBookingDateTime+"&toBookingDateTime="+toBookingDateTime ;
			}else {
				url="/accounts/"+accountID+"/transactions" ;
			}
		   response = restTemplate.exchange(url, HttpMethod.GET, entity, TransactionListResponse.class);

			/*response = restTemplate.post("/api/ws/dbtActfndTransfer?",
					entity, String.class);*/
			
			if(response.getStatusCode().equals(HttpStatus.OK)) {
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			}else {
				ErrorRestResponse c24ftResponse = new ErrorRestResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("23").split(":")[0]),
						errorCode.ErrorCodeRegistration("23").split(":")[1]);
				return new ResponseEntity<>(c24ftResponse, HttpStatus.NO_CONTENT);
			}
			
		} catch (HttpClientErrorException ex) {
			
			logger.info("HttpClientErrorException --------->");
			logger.info("HttpClientStatusCode --------->"+ex.getStatusCode());
			
			if (ex.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
				logger.info("HttpClientErrorException --------->Bad Request");
				ErrorRestResponse errorRestResponse = new Gson().fromJson(ex.getResponseBodyAsString().toString(), ErrorRestResponse.class);
				return new ResponseEntity<>(errorRestResponse, HttpStatus.BAD_REQUEST);
			} else if(ex.getStatusCode().equals(HttpStatus.NO_CONTENT)){
				logger.info("HttpClientErrorException --------->No Content");
				ErrorRestResponse c24ftResponse = new ErrorRestResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("23").split(":")[0]),
						errorCode.ErrorCodeRegistration("23").split(":")[1]);
				return new ResponseEntity<>(c24ftResponse, ex.getStatusCode());
			}else if(ex.getStatusCode().equals(HttpStatus.UNAUTHORIZED)){
				logger.info("HttpClientErrorException --------->UnAuthorised");
				ErrorRestResponse c24ftResponse = new ErrorRestResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("24").split(":")[0]),
						errorCode.ErrorCodeRegistration("24").split(":")[1]);
				return new ResponseEntity<>(c24ftResponse, ex.getStatusCode());
			}else {
				logger.info("HttpClientErrorException --------->Other");
				ErrorRestResponse c24ftResponse = new ErrorRestResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("25").split(":")[0]),
						errorCode.ErrorCodeRegistration("25").split(":")[1]);
				return new ResponseEntity<>(c24ftResponse, ex.getStatusCode());
			}

		} catch (HttpServerErrorException ex) {
			logger.info("HttpServerErrorException --------->");
			
			ErrorRestResponse c24ftResponse = new ErrorRestResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("23").split(":")[0]),
					errorCode.ErrorCodeRegistration("23").split(":")[1]);
			return new ResponseEntity<>(c24ftResponse, ex.getStatusCode());	
			
		}catch (Exception ex) {
			logger.info("HttpException --------->");
			
			ErrorRestResponse c24ftResponse = new ErrorRestResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("25").split(":")[0]),
					errorCode.ErrorCodeRegistration("25").split(":")[1]);
			return new ResponseEntity<>(c24ftResponse,HttpStatus.INTERNAL_SERVER_ERROR);
			
		}

	}

	public ResponseEntity<Object> accountConsentAccountList(String x_request_id, String psuDeviceID,
			String psuIPAddress, String psuID, String psuIDCountry, String psuIDType, String sender_participant_bic,
			String sender_participant_member_id, String receiver_participant_bic, String receiver_participant_member_id,
			String consentID, String cryptogram) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
		httpHeaders.set("X-Request-ID",x_request_id );
		httpHeaders.set("SenderParticipant-BIC", sender_participant_bic);
		httpHeaders.set("SenderParticipant-MemberID", sender_participant_member_id);
		httpHeaders.set("ReceiverParticipant-BIC", receiver_participant_bic);
		httpHeaders.set("ReceiverParticipant-MemberID", receiver_participant_member_id);
		httpHeaders.set("PSU-Device-ID", psuDeviceID);
		httpHeaders.set("PSU-IP-Address", psuIPAddress);
		httpHeaders.set("PSU-ID", psuID);
		httpHeaders.set("PSU-ID-Country", psuIDCountry);
		httpHeaders.set("PSU-ID-Type", psuIDType);
		httpHeaders.set("Consent-ID", consentID);
		httpHeaders.set("Cryptogram", cryptogram);


		HttpEntity<String> entity = new HttpEntity<>("", httpHeaders);
		ResponseEntity<AccountListResponse> response = null;
		

		try {
			logger.info("Sending message to ipsx Rest WebService Account List Inquiry ");
			
		   response = restTemplate.exchange("/accounts", HttpMethod.GET, entity, AccountListResponse.class);

			/*response = restTemplate.post("/api/ws/dbtActfndTransfer?",
					entity, String.class);*/
			
			if(response.getStatusCode().equals(HttpStatus.OK)) {
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			}else {
				ErrorRestResponse c24ftResponse = new ErrorRestResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("23").split(":")[0]),
						errorCode.ErrorCodeRegistration("23").split(":")[1]);
				return new ResponseEntity<>(c24ftResponse, HttpStatus.NO_CONTENT);
			}
			
		} catch (HttpClientErrorException ex) {
			
			logger.info("HttpClientErrorException --------->");
			logger.info("HttpClientStatusCode --------->"+ex.getStatusCode());
			
			if (ex.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
				logger.info("HttpClientErrorException --------->Bad Request");
				ErrorRestResponse errorRestResponse = new Gson().fromJson(ex.getResponseBodyAsString().toString(), ErrorRestResponse.class);
				return new ResponseEntity<>(errorRestResponse, HttpStatus.BAD_REQUEST);
			} else if(ex.getStatusCode().equals(HttpStatus.NO_CONTENT)){
				logger.info("HttpClientErrorException --------->No Content");
				ErrorRestResponse c24ftResponse = new ErrorRestResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("23").split(":")[0]),
						errorCode.ErrorCodeRegistration("23").split(":")[1]);
				return new ResponseEntity<>(c24ftResponse, ex.getStatusCode());
			}else if(ex.getStatusCode().equals(HttpStatus.UNAUTHORIZED)){
				logger.info("HttpClientErrorException --------->UnAuthorised");
				ErrorRestResponse c24ftResponse = new ErrorRestResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("24").split(":")[0]),
						errorCode.ErrorCodeRegistration("24").split(":")[1]);
				return new ResponseEntity<>(c24ftResponse, ex.getStatusCode());
			}else {
				logger.info("HttpClientErrorException --------->Other");
				ErrorRestResponse c24ftResponse = new ErrorRestResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("25").split(":")[0]),
						errorCode.ErrorCodeRegistration("25").split(":")[1]);
				return new ResponseEntity<>(c24ftResponse, ex.getStatusCode());
			}

		} catch (HttpServerErrorException ex) {
			logger.info("HttpServerErrorException --------->");
			
			ErrorRestResponse c24ftResponse = new ErrorRestResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("23").split(":")[0]),
					errorCode.ErrorCodeRegistration("23").split(":")[1]);
			return new ResponseEntity<>(c24ftResponse, ex.getStatusCode());	
			
		}catch (Exception ex) {
			logger.info("HttpException --------->");
			
			ErrorRestResponse c24ftResponse = new ErrorRestResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("25").split(":")[0]),
					errorCode.ErrorCodeRegistration("25").split(":")[1]);
			return new ResponseEntity<>(c24ftResponse,HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
	}

	public ResponseEntity<Object> accountConsentAccountInd(String x_request_id, String psuDeviceID, String psuIPAddress,
			String psuID, String psuIDCountry, String psuIDType, String sender_participant_bic,
			String sender_participant_member_id, String receiver_participant_bic, String receiver_participant_member_id,
			String consentID, String cryptogram, String accountID) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
		httpHeaders.set("X-Request-ID",x_request_id );
		httpHeaders.set("SenderParticipant-BIC", sender_participant_bic);
		httpHeaders.set("SenderParticipant-MemberID", sender_participant_member_id);
		httpHeaders.set("ReceiverParticipant-BIC", receiver_participant_bic);
		httpHeaders.set("ReceiverParticipant-MemberID", receiver_participant_member_id);
		httpHeaders.set("PSU-Device-ID", psuDeviceID);
		httpHeaders.set("PSU-IP-Address", psuIPAddress);
		httpHeaders.set("PSU-ID", psuID);
		httpHeaders.set("PSU-ID-Country", psuIDCountry);
		httpHeaders.set("PSU-ID-Type", psuIDType);
		httpHeaders.set("Consent-ID", consentID);
		httpHeaders.set("Cryptogram", cryptogram);


		HttpEntity<String> entity = new HttpEntity<>("", httpHeaders);
		ResponseEntity<AccountsListAccounts> response = null;
		

		try {
			logger.info("Sending message to ipsx Rest WebService Account Inquiry ");
			
		   response = restTemplate.exchange("/accounts/"+accountID, HttpMethod.GET, entity, AccountsListAccounts.class);

			/*response = restTemplate.post("/api/ws/dbtActfndTransfer?",
					entity, String.class);*/
			
			if(response.getStatusCode().equals(HttpStatus.OK)) {
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			}else {
				ErrorRestResponse c24ftResponse = new ErrorRestResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("23").split(":")[0]),
						errorCode.ErrorCodeRegistration("23").split(":")[1]);
				return new ResponseEntity<>(c24ftResponse, HttpStatus.NO_CONTENT);
			}
			
		} catch (HttpClientErrorException ex) {
			
			logger.info("HttpClientErrorException --------->");
			logger.info("HttpClientStatusCode --------->"+ex.getStatusCode());
			
			if (ex.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
				logger.info("HttpClientErrorException --------->Bad Request");
				ErrorRestResponse errorRestResponse = new Gson().fromJson(ex.getResponseBodyAsString().toString(), ErrorRestResponse.class);
				return new ResponseEntity<>(errorRestResponse, HttpStatus.BAD_REQUEST);
			} else if(ex.getStatusCode().equals(HttpStatus.NO_CONTENT)){
				logger.info("HttpClientErrorException --------->No Content");
				ErrorRestResponse c24ftResponse = new ErrorRestResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("23").split(":")[0]),
						errorCode.ErrorCodeRegistration("23").split(":")[1]);
				return new ResponseEntity<>(c24ftResponse, ex.getStatusCode());
			}else if(ex.getStatusCode().equals(HttpStatus.UNAUTHORIZED)){
				logger.info("HttpClientErrorException --------->UnAuthorised");
				ErrorRestResponse c24ftResponse = new ErrorRestResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("24").split(":")[0]),
						errorCode.ErrorCodeRegistration("24").split(":")[1]);
				return new ResponseEntity<>(c24ftResponse, ex.getStatusCode());
			}else {
				logger.info("HttpClientErrorException --------->Other");
				ErrorRestResponse c24ftResponse = new ErrorRestResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("25").split(":")[0]),
						errorCode.ErrorCodeRegistration("25").split(":")[1]);
				return new ResponseEntity<>(c24ftResponse, ex.getStatusCode());
			}

		} catch (HttpServerErrorException ex) {
			logger.info("HttpServerErrorException --------->");
			
			ErrorRestResponse c24ftResponse = new ErrorRestResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("23").split(":")[0]),
					errorCode.ErrorCodeRegistration("23").split(":")[1]);
			return new ResponseEntity<>(c24ftResponse, ex.getStatusCode());	
			
		}catch (Exception ex) {
			logger.info("HttpException --------->");
			
			ErrorRestResponse c24ftResponse = new ErrorRestResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("25").split(":")[0]),
					errorCode.ErrorCodeRegistration("25").split(":")[1]);
			return new ResponseEntity<>(c24ftResponse,HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
	}

}
