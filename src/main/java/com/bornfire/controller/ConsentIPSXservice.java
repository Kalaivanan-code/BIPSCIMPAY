package com.bornfire.controller;

import static com.bornfire.exception.ErrorResponseCode.SERVER_ERROR_CODE;

import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
import com.bornfire.config.Listener;
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
import com.bornfire.upiqrcodeentity.NpciupiReqcls;
import com.bornfire.upiqrcodeentity.UPIRespEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

@Component
public class ConsentIPSXservice {
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	ErrorResponseCode errorCode;
	
	@Autowired
	Environment env;
	
	@Autowired
	Listener listener;

	@Autowired
	NPCIQrcodeValidation npsqrcode;
	
	private static final Logger logger = LoggerFactory.getLogger(ConsentIPSXservice.class);

	public ResponseEntity<ConsentAccessResponse> accountConsent(String x_request_id, String psuDeviceID,
			String psuIPAddress, String psuID, String psuIDCountry, String psuIDType,
			ConsentOutwardAccessRequest consentOutwardAccessRequest, String sender_participant_bic,
			String sender_participant_member_id, String receiver_participant_bic,
			String receiver_participant_member_id,String publicKey,String customDeviceID) {
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
		httpHeaders.set("X-Request-ID",x_request_id );
		httpHeaders.set("SenderParticipant-BIC", sender_participant_bic);
		httpHeaders.set("ReceiverParticipant-BIC",receiver_participant_bic);
		httpHeaders.set("PSU-Device-ID", customDeviceID);
		httpHeaders.set("PSU-IP-Address", psuIPAddress);
		httpHeaders.set("PSU-ID", psuID);
		httpHeaders.set("PSU-ID-Country", psuIDCountry);
		httpHeaders.set("PSU-ID-Type", psuIDType);
		
		logger.debug("---Header Parameter---");
		logger.debug("X-Request-ID:"+x_request_id);
		logger.debug("SenderParticipant-BIC:"+sender_participant_bic);
		logger.debug("ReceiverParticipant-BIC:"+receiver_participant_bic);
		logger.debug("PSU-Device-ID:"+customDeviceID);
		logger.debug("PSU-IP-Address:"+psuIPAddress);
		logger.debug("PSU-ID:"+psuID);
		logger.debug("PSU-ID-Country:"+psuIDCountry);
		logger.debug("PSU-ID-Type:"+psuIDType);
		
		String authStr = env.getProperty("ipsxconsent.user")+":"+env.getProperty("ipsxconsent.passwd");
		String base64Creds = Base64.getEncoder().encodeToString(authStr.getBytes());
		httpHeaders.set("Authorization", "Basic " + base64Creds);
		
		logger.debug("Authorization:Basic "+base64Creds);

		ConsentAccessRequest consentAccessRequest = new ConsentAccessRequest();
		
		Account account = new Account();
		account.setIdentification(consentOutwardAccessRequest.getAccounts().getAccountNumber());
		account.setSchemeName(consentOutwardAccessRequest.getAccounts().getShemeName());
		
		consentAccessRequest.setPhoneNumber(consentOutwardAccessRequest.getPhoneNumber());
		consentAccessRequest.setPublicKey(publicKey);
		consentAccessRequest.setAccount(Arrays.asList(account));
		consentAccessRequest.setPermissions(consentOutwardAccessRequest.getPermissions());

		logger.debug("Json Body:"+listener.generateJsonFormat(consentAccessRequest.toString()));
		
		HttpEntity<ConsentAccessRequest> entity = new HttpEntity<>(consentAccessRequest, httpHeaders);
		ResponseEntity<ConsentAccessResponse> response = null;
	
		try {
			logger.info("Sending consent Access message to ipsx Rest WebService");
			
			response = restTemplate.postForEntity(env.getProperty("ipsxconsent.url")+"/accounts-consents",
					entity, ConsentAccessResponse.class);
			
			return new ResponseEntity<ConsentAccessResponse>(response.getBody(), HttpStatus.OK);
			
		} catch (HttpClientErrorException ex) {
			
			logger.info("HttpClientErrorException --------->");
			logger.info("HttpClientStatusCode --------->"+ex.getStatusCode());
			
			if (ex.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
				logger.info("HttpClientErrorException --------->Bad Request");
				ConsentAccessResponse errorRestResponse = new Gson().fromJson(ex.getResponseBodyAsString().toString(), ConsentAccessResponse.class);
				return new ResponseEntity<ConsentAccessResponse>(errorRestResponse, HttpStatus.BAD_REQUEST);
			} else if(ex.getStatusCode().equals(HttpStatus.NO_CONTENT)){
				logger.info("HttpClientErrorException --------->No Content");
				ConsentAccessResponse c24ftResponse = new ConsentAccessResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("23").split(":")[0]),
						errorCode.ErrorCodeRegistration("23").split(":")[1]);
				return new ResponseEntity<ConsentAccessResponse>(c24ftResponse, HttpStatus.NO_CONTENT);
			}else if(ex.getStatusCode().equals(HttpStatus.UNAUTHORIZED)){
				logger.info("HttpClientErrorException --------->UnAuthorised");
				ConsentAccessResponse c24ftResponse = new ConsentAccessResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("24").split(":")[0]),
						errorCode.ErrorCodeRegistration("24").split(":")[1]);
				return new ResponseEntity<ConsentAccessResponse>(c24ftResponse, HttpStatus.UNAUTHORIZED);
			}else {
				logger.info("HttpClientErrorException --------->Other");
				ConsentAccessResponse c24ftResponse = new ConsentAccessResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("25").split(":")[0]),
						errorCode.ErrorCodeRegistration("25").split(":")[1]);
				return new ResponseEntity<ConsentAccessResponse>(c24ftResponse, ex.getStatusCode());
			}

		} catch (HttpServerErrorException ex) {
			logger.info("HttpServerErrorException --------->");
			
			ConsentAccessResponse c24ftResponse = new ConsentAccessResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("23").split(":")[0]),
					errorCode.ErrorCodeRegistration("23").split(":")[1]);
			return new ResponseEntity<ConsentAccessResponse>(c24ftResponse, ex.getStatusCode());	
			
		}

	}

	public ResponseEntity<SCAAthenticationResponse> accountConsentAuthorisation(String w_request_id, String psuDeviceID,
			String psuIPAddress, String psuID, String psuIDCountry, String psuIDType,
			ConsentOutwardAccessAuthRequest consentOutwardAccessAuthRequest, String sender_participant_bic,
			String sender_participant_member_id, String receiver_participant_bic, String receiver_participant_member_id,
			String cryptogram, String consentID, String authID,String customDeviceID) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
		httpHeaders.set("X-Request-ID",w_request_id );
		httpHeaders.set("SenderParticipant-BIC", sender_participant_bic);
		httpHeaders.set("ReceiverParticipant-BIC", receiver_participant_bic);
		httpHeaders.set("PSU-Device-ID", customDeviceID);
		httpHeaders.set("PSU-IP-Address", psuIPAddress);
		httpHeaders.set("PSU-ID", psuID);
		httpHeaders.set("PSU-ID-Country", psuIDCountry);
		httpHeaders.set("PSU-ID-Type", psuIDType);
		httpHeaders.set("Cryptogram", cryptogram);
		

		logger.debug("---Header Parameter---");
		logger.debug("X-Request-ID:"+w_request_id);
		logger.debug("SenderParticipant-BIC:"+sender_participant_bic);
		logger.debug("ReceiverParticipant-BIC:"+receiver_participant_bic);
		logger.debug("PSU-Device-ID:"+customDeviceID);
		logger.debug("PSU-IP-Address:"+psuIPAddress);
		logger.debug("PSU-ID:"+psuID);
		logger.debug("PSU-ID-Country:"+psuIDCountry);
		logger.debug("PSU-ID-Type:"+psuIDType);
		logger.debug("Cryptogram:"+cryptogram);
		
		String authStr = env.getProperty("ipsxconsent.user")+":"+env.getProperty("ipsxconsent.passwd");
		String base64Creds = Base64.getEncoder().encodeToString(authStr.getBytes());
		httpHeaders.set("Authorization", "Basic " + base64Creds);
		
		logger.debug("Authorization:Basic "+base64Creds);


		SCAAuthenticatedData sCAAuthenticatedData = new SCAAuthenticatedData();
		sCAAuthenticatedData.setScaAuthenticationData(consentOutwardAccessAuthRequest.getSCAData());
		
		logger.debug("Json Body:"+listener.generateJsonFormat(sCAAuthenticatedData.toString()));

		HttpEntity<SCAAuthenticatedData> entity = new HttpEntity<>(sCAAuthenticatedData, httpHeaders);
		ResponseEntity<SCAAthenticationResponse> response = null;
		

		try {
			logger.info("Sending message to ipsx Rest WebService Authorisation");
			
			response = restTemplate.exchange(env.getProperty("ipsxconsent.url")+"/accounts-consents/"+consentID+"/authorisations/"+authID, HttpMethod.PUT, entity, SCAAthenticationResponse.class);
			
			return new ResponseEntity<SCAAthenticationResponse>(response.getBody(), HttpStatus.OK);
			
			
		} catch (HttpClientErrorException ex) {
			
			logger.info("HttpClientErrorException --------->");
			logger.info("HttpClientStatusCode --------->"+ex.getStatusCode());
			
			if (ex.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
				logger.info("HttpClientErrorException --------->Bad Request");
				SCAAthenticationResponse errorRestResponse = new Gson().fromJson(ex.getResponseBodyAsString().toString(), SCAAthenticationResponse.class);
				return new ResponseEntity<SCAAthenticationResponse>(errorRestResponse, HttpStatus.BAD_REQUEST);
			} else if(ex.getStatusCode().equals(HttpStatus.NO_CONTENT)){
				logger.info("HttpClientErrorException --------->No Content");
				SCAAthenticationResponse c24ftResponse = new SCAAthenticationResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("23").split(":")[0]),
						errorCode.ErrorCodeRegistration("23").split(":")[1]);
				return new ResponseEntity<SCAAthenticationResponse>(c24ftResponse, HttpStatus.NO_CONTENT);
			}else if(ex.getStatusCode().equals(HttpStatus.UNAUTHORIZED)){
				logger.info("HttpClientErrorException --------->UnAuthorised");
				SCAAthenticationResponse c24ftResponse = new SCAAthenticationResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("24").split(":")[0]),
						errorCode.ErrorCodeRegistration("24").split(":")[1]);
				return new ResponseEntity<SCAAthenticationResponse>(c24ftResponse, HttpStatus.UNAUTHORIZED);
			}else {
				logger.info("HttpClientErrorException --------->Other");
				SCAAthenticationResponse c24ftResponse = new SCAAthenticationResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("25").split(":")[0]),
						errorCode.ErrorCodeRegistration("25").split(":")[1]);
				return new ResponseEntity<SCAAthenticationResponse>(c24ftResponse, ex.getStatusCode());
			}

		} catch (HttpServerErrorException ex) {
			logger.info("HttpServerErrorException --------->");
			
			SCAAthenticationResponse c24ftResponse = new SCAAthenticationResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("23").split(":")[0]),
					errorCode.ErrorCodeRegistration("23").split(":")[1]);
			return new ResponseEntity<SCAAthenticationResponse>(c24ftResponse, ex.getStatusCode());	
			
		}

	}

	public ResponseEntity <ErrorRestResponse>  deleteAccountConsent(String x_request_id, String psuDeviceID, String psuIPAddress,
			String psuID, String psuIDCountry, String psuIDType, String sender_participant_bic,
			String sender_participant_member_id, String receiver_participant_bic, String receiver_participant_member_id,
			String consentID,String customDeviceID) {
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
		httpHeaders.set("X-Request-ID",x_request_id );
		httpHeaders.set("SenderParticipant-BIC", sender_participant_bic);
		httpHeaders.set("ReceiverParticipant-BIC", receiver_participant_bic);
		httpHeaders.set("PSU-Device-ID", customDeviceID);
		httpHeaders.set("PSU-IP-Address", psuIPAddress);
		httpHeaders.set("PSU-ID", psuID);
		httpHeaders.set("PSU-ID-Country", psuIDCountry);
		httpHeaders.set("PSU-ID-Type", psuIDType);

		logger.debug("---Header Parameter---");
		logger.debug("X-Request-ID:"+x_request_id);
		logger.debug("SenderParticipant-BIC:"+sender_participant_bic);
		logger.debug("ReceiverParticipant-BIC:"+receiver_participant_bic);
		logger.debug("PSU-Device-ID:"+customDeviceID);
		logger.debug("PSU-IP-Address:"+psuIPAddress);
		logger.debug("PSU-ID:"+psuID);
		logger.debug("PSU-ID-Country:"+psuIDCountry);
		logger.debug("PSU-ID-Type:"+psuIDType);
		
		String authStr = env.getProperty("ipsxconsent.user")+":"+env.getProperty("ipsxconsent.passwd");
		String base64Creds = Base64.getEncoder().encodeToString(authStr.getBytes());
		httpHeaders.set("Authorization", "Basic " + base64Creds);
		
		logger.debug("Authorization:Basic "+base64Creds);

		HttpEntity<String> entity = new HttpEntity<>("", httpHeaders);
		ResponseEntity<String> response = null;
		
		try {
			logger.info("Sending message to ipsx Rest WebService Delete Account Consent");
			
		   response = restTemplate.exchange(env.getProperty("ipsxconsent.url")+"/accounts-consents/"+consentID, HttpMethod.DELETE, entity, String.class);
			
			if(response.getStatusCode().equals(HttpStatus.OK)) {
				//logger.info(response.getBody().toString());
				ErrorRestResponse ee=new  ErrorRestResponse();
				return new ResponseEntity<ErrorRestResponse>(ee, HttpStatus.OK);

			}else {
				ErrorRestResponse c24ftResponse = new ErrorRestResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("23").split(":")[0]),
						errorCode.ErrorCodeRegistration("23").split(":")[1]);
				return new ResponseEntity<ErrorRestResponse>(c24ftResponse, HttpStatus.NO_CONTENT);
			}
			
		} catch (HttpClientErrorException ex) {
			
			logger.info("HttpClientErrorException --------->");
			logger.info("HttpClientStatusCode --------->"+ex.getStatusCode());
			
			if (ex.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
				logger.info("HttpClientErrorException --------->Bad Request"+ex.getResponseBodyAsString().toString());
				ErrorRestResponse errorRestResponse = new Gson().fromJson(ex.getResponseBodyAsString().toString(), ErrorRestResponse.class);
				return new ResponseEntity<ErrorRestResponse>(errorRestResponse, HttpStatus.BAD_REQUEST);
			} else if(ex.getStatusCode().equals(HttpStatus.NO_CONTENT)){
				logger.info("HttpClientErrorException --------->No Content");
				ErrorRestResponse c24ftResponse = new ErrorRestResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("23").split(":")[0]),
						errorCode.ErrorCodeRegistration("23").split(":")[1]);
				return new ResponseEntity<ErrorRestResponse>(c24ftResponse, ex.getStatusCode());
			}else if(ex.getStatusCode().equals(HttpStatus.UNAUTHORIZED)){
				logger.info("HttpClientErrorException --------->UnAuthorised");
				ErrorRestResponse c24ftResponse = new ErrorRestResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("24").split(":")[0]),
						errorCode.ErrorCodeRegistration("24").split(":")[1]);
				return new ResponseEntity<ErrorRestResponse>(c24ftResponse, ex.getStatusCode());
			}else {
				logger.info("HttpClientErrorException --------->Other");
				ErrorRestResponse c24ftResponse = new ErrorRestResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("25").split(":")[0]),
						errorCode.ErrorCodeRegistration("25").split(":")[1]);
				return new ResponseEntity<ErrorRestResponse>(c24ftResponse, ex.getStatusCode());
			}

		} catch (HttpServerErrorException ex) {
			logger.info("HttpServerErrorException --------->");
			
			ErrorRestResponse c24ftResponse = new ErrorRestResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("23").split(":")[0]),
					errorCode.ErrorCodeRegistration("23").split(":")[1]);
			return new ResponseEntity<ErrorRestResponse>(c24ftResponse, ex.getStatusCode());	
			
		}
	}

	public ResponseEntity<ConsentAccountBalance> accountConsentBalances(String x_request_id, String psuDeviceID, String psuIPAddress,
			String psuID, String psuIDCountry, String psuIDType, String sender_participant_bic,
			String sender_participant_member_id, String receiver_participant_bic, String receiver_participant_member_id,
			String consentID, String accountID, String cryptogram) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
		httpHeaders.set("X-Request-ID",x_request_id );
		httpHeaders.set("SenderParticipant-BIC", sender_participant_bic);
		httpHeaders.set("ReceiverParticipant-BIC", receiver_participant_bic);
		httpHeaders.set("PSU-Device-ID", psuDeviceID);
		httpHeaders.set("PSU-IP-Address", psuIPAddress);
		httpHeaders.set("PSU-ID", psuID);
		httpHeaders.set("PSU-ID-Country", psuIDCountry);
		httpHeaders.set("PSU-ID-Type", psuIDType);
		httpHeaders.set("Consent-ID", consentID);
		httpHeaders.set("Cryptogram", cryptogram);

		logger.debug("---Header Parameter---");
		logger.debug("X-Request-ID:"+x_request_id);
		logger.debug("SenderParticipant-BIC:"+sender_participant_bic);
		logger.debug("ReceiverParticipant-BIC:"+receiver_participant_bic);
		logger.debug("PSU-Device-ID:"+psuDeviceID);
		logger.debug("PSU-IP-Address:"+psuIPAddress);
		logger.debug("PSU-ID:"+psuID);
		logger.debug("PSU-ID-Country:"+psuIDCountry);
		logger.debug("PSU-ID-Type:"+psuIDType);
		logger.debug("Consent-ID:"+consentID);
		logger.debug("Cryptogram:"+cryptogram);
		
		
		String authStr = env.getProperty("ipsxconsent.user")+":"+env.getProperty("ipsxconsent.passwd");
		String base64Creds = Base64.getEncoder().encodeToString(authStr.getBytes());
		httpHeaders.set("Authorization", "Basic " + base64Creds);
		
		logger.debug("Authorization:Basic "+base64Creds);

		HttpEntity<String> entity = new HttpEntity<>("", httpHeaders);
		ResponseEntity<ConsentAccountBalance> response = null;
		
		try {
			logger.info("Sending message to ipsx Rest WebService Balance Account ");
			
		   response = restTemplate.exchange(env.getProperty("ipsxconsent.url")+"/accounts/"+accountID+"/balances", HttpMethod.GET, entity, ConsentAccountBalance.class);

			if(response.getStatusCode().equals(HttpStatus.OK)) {
				return new ResponseEntity<ConsentAccountBalance>(response.getBody(), HttpStatus.OK);
			}else {
				ConsentAccountBalance c24ftResponse = new ConsentAccountBalance(Integer.parseInt(errorCode.ErrorCodeRegistration("23").split(":")[0]),
						errorCode.ErrorCodeRegistration("23").split(":")[1]);
				return new ResponseEntity<>(c24ftResponse, HttpStatus.NO_CONTENT);
			}
			
		} catch (HttpClientErrorException ex) {
			
			logger.info("HttpClientErrorException --------->");
			logger.info("HttpClientStatusCode --------->"+ex.getStatusCode());
			
			if (ex.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
				logger.info("HttpClientErrorException --------->Bad Request");
				ConsentAccountBalance errorRestResponse = new Gson().fromJson(ex.getResponseBodyAsString().toString(), ConsentAccountBalance.class);
				return new ResponseEntity<ConsentAccountBalance>(errorRestResponse, HttpStatus.BAD_REQUEST);
			} else if(ex.getStatusCode().equals(HttpStatus.NO_CONTENT)){
				logger.info("HttpClientErrorException --------->No Content");
				ConsentAccountBalance c24ftResponse = new ConsentAccountBalance(Integer.parseInt(errorCode.ErrorCodeRegistration("23").split(":")[0]),
						errorCode.ErrorCodeRegistration("23").split(":")[1]);
				return new ResponseEntity<ConsentAccountBalance>(c24ftResponse, HttpStatus.NO_CONTENT);
			}else if(ex.getStatusCode().equals(HttpStatus.UNAUTHORIZED)){
				logger.info("HttpClientErrorException --------->UnAuthorised");
				ConsentAccountBalance c24ftResponse = new ConsentAccountBalance(Integer.parseInt(errorCode.ErrorCodeRegistration("24").split(":")[0]),
						errorCode.ErrorCodeRegistration("24").split(":")[1]);
				return new ResponseEntity<ConsentAccountBalance>(c24ftResponse, HttpStatus.UNAUTHORIZED);
			}else {
				logger.info("HttpClientErrorException --------->Other");
				ConsentAccountBalance c24ftResponse = new ConsentAccountBalance(Integer.parseInt(errorCode.ErrorCodeRegistration("25").split(":")[0]),
						errorCode.ErrorCodeRegistration("25").split(":")[1]);
				return new ResponseEntity<ConsentAccountBalance>(c24ftResponse, ex.getStatusCode());
			}

		} catch (HttpServerErrorException ex) {
			logger.info("HttpServerErrorException --------->");
			
			ConsentAccountBalance c24ftResponse = new ConsentAccountBalance(Integer.parseInt(errorCode.ErrorCodeRegistration("23").split(":")[0]),
					errorCode.ErrorCodeRegistration("23").split(":")[1]);
			return new ResponseEntity<ConsentAccountBalance>(c24ftResponse, ex.getStatusCode());	
			
		}


	}

	public ResponseEntity<TransactionListResponse> accountConsentTransaction(String x_request_id, String psuDeviceID,
			String psuIPAddress, String psuID, String psuIDCountry, String psuIDType, String sender_participant_bic,
			String sender_participant_member_id, String receiver_participant_bic, String receiver_participant_member_id,
			String consentID, String accountID, String cryptogram,String fromBookingDateTime,String toBookingDateTime) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		


		httpHeaders.set("X-Request-ID",x_request_id );
		httpHeaders.set("SenderParticipant-BIC", sender_participant_bic);
		//httpHeaders.set("SenderParticipant-MemberID", sender_participant_member_id);
		httpHeaders.set("ReceiverParticipant-BIC", receiver_participant_bic);
		//httpHeaders.set("ReceiverParticipant-MemberID", receiver_participant_member_id);
		httpHeaders.set("PSU-Device-ID", psuDeviceID);
		httpHeaders.set("PSU-IP-Address", psuIPAddress);
		httpHeaders.set("PSU-ID", psuID);
		httpHeaders.set("PSU-ID-Country", psuIDCountry);
		httpHeaders.set("PSU-ID-Type", psuIDType);
		httpHeaders.set("Consent-ID", consentID);
		httpHeaders.set("Cryptogram", cryptogram);
		
		logger.debug("---Header Parameter---");
		logger.debug("X-Request-ID:"+x_request_id);
		logger.debug("SenderParticipant-BIC:"+sender_participant_bic);
		logger.debug("ReceiverParticipant-BIC:"+receiver_participant_bic);
		logger.debug("PSU-Device-ID:"+psuDeviceID);
		logger.debug("PSU-IP-Address:"+psuIPAddress);
		logger.debug("PSU-ID:"+psuID);
		logger.debug("PSU-ID-Country:"+psuIDCountry);
		logger.debug("PSU-ID-Type:"+psuIDType);
		logger.debug("Consent-ID:"+consentID);
		logger.debug("Cryptogram:"+cryptogram);
		
		
		String authStr = env.getProperty("ipsxconsent.user")+":"+env.getProperty("ipsxconsent.passwd");
		String base64Creds = Base64.getEncoder().encodeToString(authStr.getBytes());
		httpHeaders.set("Authorization", "Basic " + base64Creds);
		
		logger.debug("Authorization:Basic "+base64Creds);


		HttpEntity<String> entity = new HttpEntity<>("", httpHeaders);
		ResponseEntity<TransactionListResponse> response = null;
		

		try {
			logger.info("Sending message to ipsx Rest WebService Transaction Inquiry ");
			
			String url="";
			
			if(fromBookingDateTime!=null && toBookingDateTime!=null) {
				url=env.getProperty("ipsxconsent.url")+"/accounts/"+accountID+"/transactions?fromBookingDateTime="+fromBookingDateTime+"&toBookingDateTime="+toBookingDateTime ;
			}else {
				url=env.getProperty("ipsxconsent.url")+"/accounts/"+accountID+"/transactions" ;
			}
		   response = restTemplate.exchange(url, HttpMethod.GET, entity, TransactionListResponse.class);

			/*response = restTemplate.post("/api/ws/dbtActfndTransfer?",
					entity, String.class);*/
			
			if(response.getStatusCode().equals(HttpStatus.OK)) {
				return new ResponseEntity<TransactionListResponse>(response.getBody(), HttpStatus.OK);
			}else {
				TransactionListResponse c24ftResponse = new TransactionListResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("23").split(":")[0]),
						errorCode.ErrorCodeRegistration("23").split(":")[1]);
				return new ResponseEntity<TransactionListResponse>(c24ftResponse, HttpStatus.NO_CONTENT);
			}
			
		} catch (HttpClientErrorException ex) {
			
			logger.info("HttpClientErrorException --------->");
			logger.info("HttpClientStatusCode --------->"+ex.getStatusCode());
			
			if (ex.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
				logger.info("HttpClientErrorException --------->Bad Request");
				TransactionListResponse errorRestResponse = new Gson().fromJson(ex.getResponseBodyAsString().toString(), TransactionListResponse.class);
				return new ResponseEntity<TransactionListResponse>(errorRestResponse, HttpStatus.BAD_REQUEST);
			} else if(ex.getStatusCode().equals(HttpStatus.NO_CONTENT)){
				logger.info("HttpClientErrorException --------->No Content");
				TransactionListResponse c24ftResponse = new TransactionListResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("23").split(":")[0]),
						errorCode.ErrorCodeRegistration("23").split(":")[1]);
				return new ResponseEntity<TransactionListResponse>(c24ftResponse, HttpStatus.NO_CONTENT);
			}else if(ex.getStatusCode().equals(HttpStatus.UNAUTHORIZED)){
				logger.info("HttpClientErrorException --------->UnAuthorised");
				TransactionListResponse c24ftResponse = new TransactionListResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("24").split(":")[0]),
						errorCode.ErrorCodeRegistration("24").split(":")[1]);
				return new ResponseEntity<TransactionListResponse>(c24ftResponse, HttpStatus.UNAUTHORIZED);
			}else {
				logger.info("HttpClientErrorException --------->Other");
				TransactionListResponse c24ftResponse = new TransactionListResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("25").split(":")[0]),
						errorCode.ErrorCodeRegistration("25").split(":")[1]);
				return new ResponseEntity<TransactionListResponse>(c24ftResponse, ex.getStatusCode());
			}

		} catch (HttpServerErrorException ex) {
			logger.info("HttpServerErrorException --------->");
			
			TransactionListResponse c24ftResponse = new TransactionListResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("23").split(":")[0]),
					errorCode.ErrorCodeRegistration("23").split(":")[1]);
			return new ResponseEntity<TransactionListResponse>(c24ftResponse, ex.getStatusCode());	
			
		}

	}

	public ResponseEntity<AccountListResponse> accountConsentAccountList(String x_request_id, String psuDeviceID,
			String psuIPAddress, String psuID, String psuIDCountry, String psuIDType, String sender_participant_bic,
			String sender_participant_member_id, String receiver_participant_bic, String receiver_participant_member_id,
			String consentID, String cryptogram) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
		httpHeaders.set("X-Request-ID",x_request_id );
		httpHeaders.set("SenderParticipant-BIC", sender_participant_bic);
		httpHeaders.set("ReceiverParticipant-BIC", receiver_participant_bic);
		httpHeaders.set("PSU-Device-ID", psuDeviceID);
		httpHeaders.set("PSU-IP-Address", psuIPAddress);
		httpHeaders.set("PSU-ID", psuID);
		httpHeaders.set("PSU-ID-Country", psuIDCountry);
		httpHeaders.set("PSU-ID-Type", psuIDType);
		httpHeaders.set("Consent-ID", consentID);
		httpHeaders.set("Cryptogram", cryptogram);
		
		logger.debug("---Header Parameter---");
		logger.debug("X-Request-ID:"+x_request_id);
		logger.debug("SenderParticipant-BIC:"+sender_participant_bic);
		logger.debug("ReceiverParticipant-BIC:"+receiver_participant_bic);
		logger.debug("PSU-Device-ID:"+psuDeviceID);
		logger.debug("PSU-IP-Address:"+psuIPAddress);
		logger.debug("PSU-ID:"+psuID);
		logger.debug("PSU-ID-Country:"+psuIDCountry);
		logger.debug("PSU-ID-Type:"+psuIDType);
		logger.debug("Consent-ID:"+consentID);
		logger.debug("Cryptogram:"+cryptogram);
		
		
		String authStr = env.getProperty("ipsxconsent.user")+":"+env.getProperty("ipsxconsent.passwd");
		String base64Creds = Base64.getEncoder().encodeToString(authStr.getBytes());
		httpHeaders.set("Authorization", "Basic " + base64Creds);
		
		logger.debug("Authorization:Basic "+base64Creds);


		HttpEntity<String> entity = new HttpEntity<>("", httpHeaders);
		ResponseEntity<AccountListResponse> response = null;
		

		try {
			logger.info("Sending message to ipsx Rest WebService Account List Inquiry ");
			
		   response = restTemplate.exchange(env.getProperty("ipsxconsent.url")+"/accounts", HttpMethod.GET, entity, AccountListResponse.class);

			
			return new ResponseEntity<AccountListResponse>(response.getBody(), HttpStatus.OK);
			
			
		} catch (HttpClientErrorException ex) {
			
			logger.info("HttpClientErrorException --------->");
			logger.info("HttpClientStatusCode --------->"+ex.getStatusCode());
			
			if (ex.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
				logger.info("HttpClientErrorException --------->Bad Request");
				AccountListResponse errorRestResponse = new Gson().fromJson(ex.getResponseBodyAsString().toString(), AccountListResponse.class);
				return new ResponseEntity<AccountListResponse>(errorRestResponse, HttpStatus.BAD_REQUEST);
			} else if(ex.getStatusCode().equals(HttpStatus.NO_CONTENT)){
				logger.info("HttpClientErrorException --------->No Content");
				AccountListResponse c24ftResponse = new AccountListResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("23").split(":")[0]),
						errorCode.ErrorCodeRegistration("23").split(":")[1]);
				return new ResponseEntity<AccountListResponse>(c24ftResponse, HttpStatus.NO_CONTENT);
			}else if(ex.getStatusCode().equals(HttpStatus.UNAUTHORIZED)){
				logger.info("HttpClientErrorException --------->UnAuthorised");
				AccountListResponse c24ftResponse = new AccountListResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("24").split(":")[0]),
						errorCode.ErrorCodeRegistration("24").split(":")[1]);
				return new ResponseEntity<AccountListResponse>(c24ftResponse, HttpStatus.UNAUTHORIZED);
			}else {
				logger.info("HttpClientErrorException --------->Other");
				AccountListResponse c24ftResponse = new AccountListResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("25").split(":")[0]),
						errorCode.ErrorCodeRegistration("25").split(":")[1]);
				return new ResponseEntity<AccountListResponse>(c24ftResponse, ex.getStatusCode());
			}

		} catch (HttpServerErrorException ex) {
			logger.info("HttpServerErrorException --------->");
			
			AccountListResponse c24ftResponse = new AccountListResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("23").split(":")[0]),
					errorCode.ErrorCodeRegistration("23").split(":")[1]);
			return new ResponseEntity<AccountListResponse>(c24ftResponse, ex.getStatusCode());	
			
		}

	}

	public ResponseEntity<AccountsListAccounts> accountConsentAccountInd(String x_request_id, String psuDeviceID, String psuIPAddress,
			String psuID, String psuIDCountry, String psuIDType, String sender_participant_bic,
			String sender_participant_member_id, String receiver_participant_bic, String receiver_participant_member_id,
			String consentID, String cryptogram, String accountID) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
		httpHeaders.set("X-Request-ID",x_request_id );
		httpHeaders.set("SenderParticipant-BIC", sender_participant_bic);
		//httpHeaders.set("SenderParticipant-MemberID", sender_participant_member_id);
		httpHeaders.set("ReceiverParticipant-BIC", receiver_participant_bic);
		//httpHeaders.set("ReceiverParticipant-MemberID", receiver_participant_member_id);
		httpHeaders.set("PSU-Device-ID", psuDeviceID);
		httpHeaders.set("PSU-IP-Address", psuIPAddress);
		httpHeaders.set("PSU-ID", psuID);
		httpHeaders.set("PSU-ID-Country", psuIDCountry);
		httpHeaders.set("PSU-ID-Type", psuIDType);
		httpHeaders.set("Consent-ID", consentID);
		httpHeaders.set("Cryptogram", cryptogram);
		
		logger.debug("---Header Parameter---");
		logger.debug("X-Request-ID:"+x_request_id);
		logger.debug("SenderParticipant-BIC:"+sender_participant_bic);
		logger.debug("ReceiverParticipant-BIC:"+receiver_participant_bic);
		logger.debug("PSU-Device-ID:"+psuDeviceID);
		logger.debug("PSU-IP-Address:"+psuIPAddress);
		logger.debug("PSU-ID:"+psuID);
		logger.debug("PSU-ID-Country:"+psuIDCountry);
		logger.debug("PSU-ID-Type:"+psuIDType);
		logger.debug("Consent-ID:"+consentID);
		logger.debug("Cryptogram:"+cryptogram);
		
		
		String authStr = env.getProperty("ipsxconsent.user")+":"+env.getProperty("ipsxconsent.passwd");
		String base64Creds = Base64.getEncoder().encodeToString(authStr.getBytes());
		httpHeaders.set("Authorization", "Basic " + base64Creds);


		HttpEntity<String> entity = new HttpEntity<>("", httpHeaders);
		ResponseEntity<AccountsListAccounts> response = null;
		

		try {
			logger.info("Sending message to ipsx Rest WebService Account Inquiry ");
			
		   response = restTemplate.exchange(env.getProperty("ipsxconsent.url")+"/accounts/"+accountID, HttpMethod.GET, entity, AccountsListAccounts.class);

		   logger.debug(listener.generateJsonFormat(response.getBody().toString()));
			/*response = restTemplate.post("/api/ws/dbtActfndTransfer?",
					entity, String.class);*/
			
			if(response.getStatusCode().equals(HttpStatus.OK)) {
				return new ResponseEntity<AccountsListAccounts>(response.getBody(), HttpStatus.OK);
			}else {
				AccountsListAccounts c24ftResponse = new AccountsListAccounts(Integer.parseInt(errorCode.ErrorCodeRegistration("23").split(":")[0]),
						errorCode.ErrorCodeRegistration("23").split(":")[1]);
				return new ResponseEntity<AccountsListAccounts>(c24ftResponse, HttpStatus.NO_CONTENT);
			}
			
		} catch (HttpClientErrorException ex) {
			
			logger.info("HttpClientErrorException --------->");
			logger.info("HttpClientStatusCode --------->"+ex.getStatusCode());
			
			if (ex.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
				logger.info("HttpClientErrorException --------->Bad Request");
				AccountsListAccounts errorRestResponse = new Gson().fromJson(ex.getResponseBodyAsString().toString(), AccountsListAccounts.class);
				return new ResponseEntity<AccountsListAccounts>(errorRestResponse, HttpStatus.BAD_REQUEST);
			} else if(ex.getStatusCode().equals(HttpStatus.NO_CONTENT)){
				logger.info("HttpClientErrorException --------->No Content");
				AccountsListAccounts c24ftResponse = new AccountsListAccounts(Integer.parseInt(errorCode.ErrorCodeRegistration("23").split(":")[0]),
						errorCode.ErrorCodeRegistration("23").split(":")[1]);
				return new ResponseEntity<AccountsListAccounts>(c24ftResponse, HttpStatus.NO_CONTENT);
			}else if(ex.getStatusCode().equals(HttpStatus.UNAUTHORIZED)){
				logger.info("HttpClientErrorException --------->UnAuthorised");
				AccountsListAccounts c24ftResponse = new AccountsListAccounts(Integer.parseInt(errorCode.ErrorCodeRegistration("24").split(":")[0]),
						errorCode.ErrorCodeRegistration("24").split(":")[1]);
				return new ResponseEntity<AccountsListAccounts>(c24ftResponse, HttpStatus.UNAUTHORIZED);
			}else {
				logger.info("HttpClientErrorException --------->Other");
				AccountsListAccounts c24ftResponse = new AccountsListAccounts(Integer.parseInt(errorCode.ErrorCodeRegistration("25").split(":")[0]),
						errorCode.ErrorCodeRegistration("25").split(":")[1]);
				return new ResponseEntity<AccountsListAccounts>(c24ftResponse, ex.getStatusCode());
			}


		} catch (HttpServerErrorException ex) {
			logger.info("HttpServerErrorException --------->");
			
			AccountsListAccounts c24ftResponse = new AccountsListAccounts(Integer.parseInt(errorCode.ErrorCodeRegistration("23").split(":")[0]),
					errorCode.ErrorCodeRegistration("23").split(":")[1]);
			return new ResponseEntity<>(c24ftResponse, ex.getStatusCode());	
			
		}
	}
	
	
	
	public ResponseEntity<String> respvalQr(NpciupiReqcls npcireq,String x_request_id) {
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
		httpHeaders.set("X-Request-ID",x_request_id );
		
		UPIRespEntity qr =  npsqrcode.getreqdet(npcireq,x_request_id);
		logger.debug("---Header Parameter---");
		logger.debug("X-Request-ID:"+x_request_id);
		
		String authStr = env.getProperty("ipsxconsent.user")+":"+env.getProperty("ipsxconsent.passwd");
		String base64Creds = Base64.getEncoder().encodeToString(authStr.getBytes());
		httpHeaders.set("Authorization", "Basic " + base64Creds);
		
		logger.debug("Authorization:Basic "+base64Creds);


		logger.debug("Json Body:"+listener.generateJsonFormat(qr.toString()));
		
		HttpEntity<UPIRespEntity> entity = new HttpEntity<>(qr, httpHeaders);
		ResponseEntity<String> response = null;
	
		try {
			logger.info("Sending consent Access message to ipsx Rest WebService");
			
			response = restTemplate.postForEntity(env.getProperty("ipsxconsent.url")+"/respvalqr",
					entity, String.class);
			
			return new ResponseEntity<String>(response.getBody(), HttpStatus.OK);
			
		} catch (HttpClientErrorException ex) {
			
			logger.info("HttpClientErrorException --------->");
			logger.info("HttpClientStatusCode --------->"+ex.getStatusCode());
			
/*			if (ex.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
				logger.info("HttpClientErrorException --------->Bad Request");
				ConsentAccessResponse errorRestResponse = new Gson().fromJson(ex.getResponseBodyAsString().toString(), ConsentAccessResponse.class);
				return new ResponseEntity<ConsentAccessResponse>(errorRestResponse, HttpStatus.BAD_REQUEST);
			} else if(ex.getStatusCode().equals(HttpStatus.NO_CONTENT)){
				logger.info("HttpClientErrorException --------->No Content");
				ConsentAccessResponse c24ftResponse = new ConsentAccessResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("23").split(":")[0]),
						errorCode.ErrorCodeRegistration("23").split(":")[1]);
				return new ResponseEntity<ConsentAccessResponse>(c24ftResponse, HttpStatus.NO_CONTENT);
			}else if(ex.getStatusCode().equals(HttpStatus.UNAUTHORIZED)){
				logger.info("HttpClientErrorException --------->UnAuthorised");
				ConsentAccessResponse c24ftResponse = new ConsentAccessResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("24").split(":")[0]),
						errorCode.ErrorCodeRegistration("24").split(":")[1]);
				return new ResponseEntity<ConsentAccessResponse>(c24ftResponse, HttpStatus.UNAUTHORIZED);
			}else {
				logger.info("HttpClientErrorException --------->Other");
				ConsentAccessResponse c24ftResponse = new ConsentAccessResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("25").split(":")[0]),
						errorCode.ErrorCodeRegistration("25").split(":")[1]);
				return new ResponseEntity<ConsentAccessResponse>(c24ftResponse, ex.getStatusCode());
			}*/
			return new ResponseEntity<String>(response.getBody(), ex.getStatusCode());

		} catch (HttpServerErrorException ex) {
			logger.info("HttpServerErrorException --------->");
			
			ConsentAccessResponse c24ftResponse = new ConsentAccessResponse(Integer.parseInt(errorCode.ErrorCodeRegistration("23").split(":")[0]),
					errorCode.ErrorCodeRegistration("23").split(":")[1]);
			return new ResponseEntity<String>(response.getBody(), ex.getStatusCode());	
			
		}

	}

}
