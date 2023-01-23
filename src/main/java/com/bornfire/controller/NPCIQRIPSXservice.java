package com.bornfire.controller;

import java.text.ParseException;
import java.util.Base64;

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

import com.bornfire.config.ErrorResponseCode;
import com.bornfire.config.Listener;
import com.bornfire.exception.ErrorRestResponse;
import com.bornfire.upiqrcodeentity.NpciupiReqcls;
import com.bornfire.upiqrcodeentity.UPIRespEntity;
import com.google.gson.Gson;

@Component
public class NPCIQRIPSXservice {

	
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
	
	private static final Logger logger = LoggerFactory.getLogger(NPCIQRIPSXservice.class);

	
public ResponseEntity<ErrorRestResponse> respvalQr(NpciupiReqcls npcireq,String x_request_id) throws ParseException {
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
		httpHeaders.set("X-Request-ID",x_request_id );
		httpHeaders.set("Receiver-Participant-Code",env.getProperty("NPCIParticipant.user") );
		
		UPIRespEntity qr =  npsqrcode.getreqdet(npcireq,x_request_id);
		logger.debug("---Header Parameter---");
		logger.debug("X-Request-ID:"+x_request_id);
		
		String authStr = env.getProperty("ipsxconsent.user")+":"+env.getProperty("ipsxconsent.passwd");
		String base64Creds = Base64.getEncoder().encodeToString(authStr.getBytes());
		httpHeaders.set("Authorization", "Basic " + base64Creds);
		//httpHeaders.set("Authorization", "Basic QkFSQk1VTTBBTlJUOjE=");
		
		logger.debug("Authorization:Basic "+base64Creds);

		UPIRespEntity sCAAuthenticatedData = new UPIRespEntity();
		sCAAuthenticatedData.setInvoice(qr.getInvoice());
		sCAAuthenticatedData.setPayee(qr.getPayee());
		sCAAuthenticatedData.setQrPayLoad(qr.getQrPayLoad());
		sCAAuthenticatedData.setResp(qr.getResp());
		sCAAuthenticatedData.setTxn(qr.getTxn());
		
		logger.debug("Json Body:"+listener.generateJsonFormat(sCAAuthenticatedData.toString()));

		logger.debug("Json Body:"+ (qr.getQrPayLoad().toString()+ "QRMSG :"+qr.getResp().getReqMsgId().toString()));
		
		HttpEntity<UPIRespEntity> entity = new HttpEntity<>(sCAAuthenticatedData, httpHeaders);
		ResponseEntity<String> response = null;
		logger.info("RESPONSE BODY :"+sCAAuthenticatedData.toString());
		logger.info("REQUEST URL :"+env.getProperty("ipsxconsent.url")+"/respvalqr");
		try {
			logger.info("Sending RESP VAL QR message to ipsx Rest WebService");
			
			response = restTemplate.postForEntity(env.getProperty("ipsxconsent.url")+"/respvalqr",
					entity, String.class);
			
			if(response.getStatusCode().equals(HttpStatus.OK)) {
				logger.info(response.getStatusCode().toString());
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
}
