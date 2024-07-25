package com.bornfire.controller;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

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

import com.bornfire.config.ErrorResponseCode;
import com.bornfire.entity.OutwardTransactionMonitoringTableRep;
import com.bornfire.entity.OutwardTransactionNotifiactionRep;
import com.bornfire.entity.RTPTranStatusResponseAPI;
import com.bornfire.entity.RTPTransferRequestStatus;
import com.bornfire.entity.RTPTransferRequestStatusNew;
import com.bornfire.entity.RTPTransferStatusResponse;
import com.bornfire.entity.RtpResponse;
import com.bornfire.exception.IPSXException;


@RestController
@Validated
public class IPSQRNotification {

	private static final Logger logger = LoggerFactory.getLogger(IPSRestController.class);
	
	
	@Autowired
	ErrorResponseCode errorCode;
	
	@Autowired
	OutwardTransactionNotifiactionRep outwardTranRep;
	
	@PostMapping(path = "/api/ws/RTPbulkTransferStatus", produces = "application/json", consumes = "application/json")
	public ResponseEntity<RTPTransferRequestStatusNew> bulkRTPTransfer(
			@RequestHeader(value = "X-Request-ID", required = true) @NotEmpty(message = "Required") String p_id,
			@RequestHeader(value = "PSU-Device-ID", required = true) @NotEmpty(message = "Required") String psuDeviceID,
			@RequestHeader(value = "PSU-IP-Address", required = true) String psuIpAddress,
			@Valid @RequestBody RTPTransferRequestStatus rtpTransferRequeststatus)
			throws DatatypeConfigurationException, JAXBException, KeyManagementException, UnrecoverableKeyException,
			KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {

		logger.info("Service Status RTP Starts :" + p_id);

		RTPTransferRequestStatusNew response = null;

		logger.info("RTP Status Request->" + rtpTransferRequeststatus);
		
		 response = invalidTran_ID(rtpTransferRequeststatus);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	
	public RTPTransferRequestStatusNew invalidTran_ID(RTPTransferRequestStatus tran_ID) {
		boolean valid = false;
		
		try {
			
			List<RTPTranStatusResponseAPI> otm = null;
			if(!tran_ID.getTranID().equals("")) {
				System.out.println("TRANID"+tran_ID.getTranID());
				otm = outwardTranRep.existsByTranIDNew(tran_ID.getTranID());
			}else if(!tran_ID.getReqId().equals("")) {
				System.out.println("REFID");
				 otm = outwardTranRep.existsByRefIDNew(tran_ID.getReqId());
			}
			System.out.println("REFID12 "+otm.size());
			if (otm.size()>0) {
				System.out.println("REFID1");
				RTPTransferRequestStatusNew rtp = new RTPTransferRequestStatusNew();
				
				RTPTranStatusResponseAPI otmotm=otm.get(0);

				if (!String.valueOf(otmotm.getTransactionNo()).equals("null")
						&& !String.valueOf(otmotm.getTransactionNo()).equals("")) {
					rtp.setTransactionNo(otmotm.getTransactionNo());
				}
				if (!String.valueOf(otmotm.getTranId()).equals("null")
						&& !String.valueOf(otmotm.getTranId()).equals("")) {
					rtp.setTranId(otmotm.getTranId().toString());
				}

				if (!String.valueOf(otmotm.getReferenceId()).equals("null")
						&& !String.valueOf(otmotm.getReferenceId()).equals("")) {

					rtp.setReferenceId(otmotm.getReferenceId().toString());
				}
				if (!String.valueOf(otmotm.getTransactionDate()).equals("null")
						&& !String.valueOf(otmotm.getTransactionDate()).equals("")) {
					rtp.setTransactionDate(otmotm.getTransactionDate());
				}
				if (!String.valueOf(otmotm.getToAccountNumber()).equals("null")
						&& !String.valueOf(otmotm.getToAccountNumber()).equals("")) {
					rtp.setToAccountNumber(otmotm.getToAccountNumber());
				}
				if (!String.valueOf(otmotm.getTransactionAmount()).equals("null")
						&& !String.valueOf(otmotm.getTransactionAmount()).equals("")) {
					rtp.setTransactionAmount(otmotm.getTransactionAmount());
				}
				if (!String.valueOf(otmotm.getStatusCode()).equals("null")
						&& !String.valueOf(otmotm.getStatusCode()).equals("")) {
					rtp.setStatusCode(otmotm.getStatusCode());
				}
				if (!String.valueOf(otmotm.getMessage()).equals("null")
						&& !String.valueOf(otmotm.getMessage()).equals("")) {
					rtp.setMessage(otmotm.getMessage());
				}
				if (!String.valueOf(otmotm.getProductType()).equals("null")
						&& !String.valueOf(otmotm.getProductType()).equals("")) {
					rtp.setProductType(otmotm.getProductType());
				}
//				if (!String.valueOf().equals("null") && !String.valueOf().equals("")) {
//					rtp.setReceiptNumber("");
//				}
	
				if(otmotm.getIsSuccess().toString().equals("SUCCESS")){
					System.out.println("SUCCESS");
					
					rtp.setIsSuccess("true");
				}else if(otmotm.getIsSuccess().toString().equals("FAILURE")){
					System.out.println("FAILURE");
					RtpResponse rtpResponse = new RtpResponse();
					rtp.setIsSuccess("false");
					if(!otmotm.getMessage().toString().equals("")) {
						rtp.setMessage(otmotm.getMessage().toString());
					}else {
						rtpResponse.setErrorStatus("");
					}
					System.out.println("FAILURE");
				}else {
					RtpResponse rtpResponse = new RtpResponse();
					rtp.setMessage("Inprogress");
				}
				
				return rtp;
				
				
			} else {
				String responseStatus = errorCode.validationError("BIPS34");
				throw new IPSXException(responseStatus);
			}
		} catch (Exception e) {
			String responseStatus = errorCode.validationError("BIPS501");
			throw new IPSXException(responseStatus);
		}

	}
}
