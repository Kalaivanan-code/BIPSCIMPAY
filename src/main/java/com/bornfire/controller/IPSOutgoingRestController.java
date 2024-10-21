package com.bornfire.controller;

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

import com.bornfire.entity.BukCreditTransferRequest;
import com.bornfire.entity.BukOutgoingCreditTransferRequest;
import com.bornfire.entity.CimCBSresponse;
import com.bornfire.entity.MCCreditTransferResponse;


@RestController
@Validated
public class IPSOutgoingRestController {

	private static final Logger logger = LoggerFactory.getLogger(IPSOutgoingRestController.class);
	
	@Autowired
	IPSOutgoingConnection ipsConnection;
	
	//// Bulk Credit Fund Transfer
	@PostMapping(path = "/api/ws/bulkOutgoingCreditFndTransfer", produces = "application/json", consumes = "application/json")
	public ResponseEntity<MCCreditTransferResponse> bulkCreditTransfer(
			@RequestHeader(value = "P-ID", required = true)   String p_id,
			@RequestHeader(value = "PSU-Device-ID", required = true) String psuDeviceID,
			@RequestHeader(value = "PSU-IP-Address", required = false) String psuIpAddress,
			@RequestHeader(value = "PSU-ID", required = false) String psuID,
			@RequestHeader(value = "USER-ID", required = true) String userID,
			@RequestHeader(value = "PSU-Channel", required = true) String channelID,
			@RequestHeader(value = "PSU-Resv-Field1", required = false) String resvfield1,
			@RequestHeader(value = "PSU-Resv-Field2", required = false) String resvfield2,
			@RequestBody BukOutgoingCreditTransferRequest mcCreditTransferRequest) {

		MCCreditTransferResponse response = null;

		CimCBSresponse connect24Response = new CimCBSresponse();
		
		connect24Response.setData(mcCreditTransferRequest.getData());
		connect24Response.setStatus(mcCreditTransferRequest.getStatus());
		
		
		response = ipsConnection.createBulkCreditConnection(psuDeviceID, psuIpAddress, psuID, 
				 mcCreditTransferRequest, userID,p_id,channelID,resvfield1,resvfield2,connect24Response);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
