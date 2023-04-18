package com.bornfire.controller;

import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.bornfire.clientService.IPSXClient;
import com.bornfire.upiqrcodeentity.ErrorResponseforUPI;
import com.bornfire.upiqrcodeentity.NpciupiReqcls;

@RestController
@Validated
public class IPSTESTController {

	@Autowired
	IPSXClient ipsxClient;
	
	private static final Logger logger = LoggerFactory.getLogger(IPSTESTController.class);

	
////NPCI Request UPI Validation
	@PostMapping(path = "/mvc/0/public-service/reqvalqrtest", produces = "application/json", consumes = "application/json")
	public ResponseEntity<ErrorResponseforUPI> ReqValQr(
			@RequestHeader(value = "X-Request-ID", required = true)   String p_id,
			@RequestBody NpciupiReqcls npcireq) throws ParseException {

		logger.info("reqvalqr Request->" + npcireq.toString());
		//UPIRespEntity response = new UPIRespEntity();
		ErrorResponseforUPI resp=null;
		
		ErrorResponseforUPI errRes = new ErrorResponseforUPI();

		return new ResponseEntity<>(resp, HttpStatus.OK);
	}
}
