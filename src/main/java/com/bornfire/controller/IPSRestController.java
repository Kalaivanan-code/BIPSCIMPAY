package com.bornfire.controller;

/*CREATED BY	: KALAIVANAN RAJENDRAN.R
CREATED ON	: 30-DEC-2019
PURPOSE		: IPS Rest Controller
*/

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bornfire.clientService.IPSXClient;
import com.bornfire.config.CronJobScheduler;
import com.bornfire.config.ErrorResponseCode;
import com.bornfire.config.IpsCertificateDAO;
import com.bornfire.config.SequenceGenerator;
import com.bornfire.entity.AccountListResponse;
import com.bornfire.entity.AccountsListAccounts;
import com.bornfire.entity.BukCreditTransferRequest;
import com.bornfire.entity.BulkDebitFndTransferRequest;
import com.bornfire.entity.CIMCreditTransferRequest;
import com.bornfire.entity.CIMMerchantDecodeQRFormatResponse;
import com.bornfire.entity.CIMMerchantDirectFndRequest;
import com.bornfire.entity.CIMMerchantQRRequestFormat;
import com.bornfire.entity.CIMMerchantQRcodeRequest;
import com.bornfire.entity.CimMerchantResponse;
import com.bornfire.entity.ConsentAccessRequest;
import com.bornfire.entity.ConsentAccessResponse;
import com.bornfire.entity.ConsentAccountBalance;
import com.bornfire.entity.ConsentOutwardAccessAuthRequest;
import com.bornfire.entity.ConsentOutwardAccessAuthResponse;
import com.bornfire.entity.ConsentOutwardAccessRequest;
import com.bornfire.entity.ConsentOutwardAccessTable;
import com.bornfire.entity.ConsentOutwardAccessTableRep;
import com.bornfire.entity.ConsentRequest;
import com.bornfire.entity.ConsentResponse;
import com.bornfire.entity.IPSChargesAndFeesRep;
import com.bornfire.entity.MCCreditTransferRequest;
import com.bornfire.entity.MCCreditTransferResponse;
import com.bornfire.entity.ManualFndTransferRequest;
import com.bornfire.entity.McConsentOutwardAccessResponse;
import com.bornfire.entity.OtherBankDetResponse;
import com.bornfire.entity.RTPTransferRequestStatus;
import com.bornfire.entity.RTPTransferStatusResponse;
import com.bornfire.entity.RTPbulkTransferRequest;
import com.bornfire.entity.RTPbulkTransferResponse;
import com.bornfire.entity.RegConsentAccountList;
import com.bornfire.entity.SCAAthenticationResponse;
import com.bornfire.entity.SCAAuthenticatedData;
import com.bornfire.entity.SettlementAccountRep;
import com.bornfire.entity.SettlementLimitResponse;
import com.bornfire.entity.TranCimCBSTable;
import com.bornfire.entity.TranIPSTableRep;
import com.bornfire.entity.TransactionListResponse;
import com.bornfire.entity.UPIQREntityRep;
import com.bornfire.entity.UserRegistrationRequest;
import com.bornfire.entity.UserRegistrationResponse;
import com.bornfire.entity.WalletAccessRequest;
import com.bornfire.entity.WalletAccessResponse;
import com.bornfire.entity.WalletBalanceResponse;
import com.bornfire.entity.WalletFndTransferRequest;
import com.bornfire.entity.WalletFndTransferResponse;
import com.bornfire.entity.WalletStatementResponse;
import com.bornfire.exception.ErrorRestResponse;
import com.bornfire.exception.FieldValidation;
import com.bornfire.exception.IPSXException;
import com.bornfire.exception.IPSXRestException;
import com.bornfire.messagebuilder.DocumentPacks;
import com.bornfire.messagebuilder.SignDocument;
import com.bornfire.qrcode.core.isos.Country;
import com.bornfire.qrcode.core.isos.Currency;
import com.bornfire.upiqrcodeentity.ErrorResponseforUPI;
import com.bornfire.upiqrcodeentity.NpciupiReqcls;
import com.bornfire.upiqrcodeentity.UPIRespEntity;

@RestController
@Validated
public class IPSRestController {

	private static final Logger logger = LoggerFactory.getLogger(IPSRestController.class);

	@Autowired
	CronJobScheduler cronJobSchedular;

	@Autowired
	ErrorResponseCode errorCode;

	@Autowired
	IpsCertificateDAO cert;

	@Autowired
	IPSXClient ipsxClient;

	@Autowired
	SequenceGenerator sequence;

	@Autowired
	IPSConnection ipsConnection;

	@Autowired
	Connect24Service connect24Service;

	@Autowired
	IpsCertificateDAO ipsCertificateDAO;

	@Autowired
	IPSDao ipsDao;

	@Autowired
	FieldValidation fieldValidation;

	@Autowired
	TranIPSTableRep tranIPSTableRep;

	@Autowired
	SignDocument signDocument;

	@Autowired
	IPSChargesAndFeesRep ipsChargesAndFeeRep;

	@Autowired
	Environment env;

	@Autowired
	SettlementAccountRep settlAccountRep;

	@Autowired
	AsyncService async;

	@Autowired
	ConsentOutwardAccessTableRep consentOutwardAccessTableRep;
	
	
	@Autowired
	DocumentPacks docPacs;
	
	@Autowired
	NPCIQrcodeValidation npciqrcode;
	
	

	/* Credit Fund Transfer Initiated from MConnect Application */
	/* MConnect Initiate the request */
	@PostMapping(path = "/api/ws/creditFndTransfer", produces = "application/json", consumes = "application/json")
	public ResponseEntity<MCCreditTransferResponse> sendCreditTransferMessage(
			@RequestHeader(value = "P-ID", required = true) @NotEmpty(message = "Required") String p_id,
			@RequestHeader(value = "PSU-Device-ID", required = true) @NotEmpty(message = "Required") String psuDeviceID,
			@RequestHeader(value = "PSU-IP-Address", required = true) String psuIpAddress,
			@RequestHeader(value = "PSU-ID", required = false) String psuID,
			@RequestHeader(value = "PSU-Channel", required = true) String channelID,
			@RequestHeader(value = "PSU-Resv-Field1", required = false) String resvfield1,
			@RequestHeader(value = "PSU-Resv-Field2", required = false) String resvfield2,
			@Valid @RequestBody CIMCreditTransferRequest mcCreditTransferRequest)
			throws DatatypeConfigurationException, JAXBException, KeyManagementException, UnrecoverableKeyException,
			KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {

		logger.info("Service Starts" + p_id);

		MCCreditTransferResponse response = null;

		logger.info("Calling Credit Transfer Connection flow Starts");
		if (ipsDao.invalidP_ID(p_id)) {
			if (!ipsDao.invalidBankCode(mcCreditTransferRequest.getToAccount().getBankCode())) {
				if(mcCreditTransferRequest.getCurrencyCode().equals("MUR")) {
					response = ipsConnection.createFTConnection(psuDeviceID, psuIpAddress, psuID, 
							 mcCreditTransferRequest, p_id, channelID, resvfield1, resvfield2);
				}else {
					String responseStatus = errorCode.validationError("BIPS8");
					throw new IPSXException(responseStatus);
				}
			} else {
				String responseStatus = errorCode.validationError("BIPS23");
				throw new IPSXException(responseStatus);
			}
		} else {
			String responseStatus = errorCode.validationError("BIPS13");
			throw new IPSXException(responseStatus);
		}

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping(path = "/api/ws/manualCreditFndTransfer", produces = "application/json", consumes = "application/json")
	public ResponseEntity<MCCreditTransferResponse> manualCreditFndTransfer(
			@RequestHeader(value = "P-ID", required = true)   String p_id,
			@RequestHeader(value = "PSU-Device-ID", required = true) String psuDeviceID,
			@RequestHeader(value = "PSU-IP-Address", required = false) String psuIpAddress,
			@RequestHeader(value = "USER-ID", required = true) String userID,
			@RequestHeader(value = "PSU-Channel", required = true) String channelID,
			@RequestHeader(value = "PSU-Resv-Field1", required = false) String resvfield1,
			@RequestHeader(value = "PSU-Resv-Field2", required = false) String resvfield2,
			@RequestBody List<ManualFndTransferRequest> manualFundTransferRequest) {

		MCCreditTransferResponse response = null;

		response = ipsConnection.createMAnualTransaction(psuDeviceID, psuIpAddress, manualFundTransferRequest, userID,
				p_id,channelID,resvfield1,resvfield2);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	//// Bulk Credit Fund Transfer
	@PostMapping(path = "/api/ws/bulkCreditFndTransfer", produces = "application/json", consumes = "application/json")
	public ResponseEntity<MCCreditTransferResponse> bulkCreditTransfer(
			@RequestHeader(value = "P-ID", required = true)   String p_id,
			@RequestHeader(value = "PSU-Device-ID", required = true) String psuDeviceID,
			@RequestHeader(value = "PSU-IP-Address", required = false) String psuIpAddress,
			@RequestHeader(value = "PSU-ID", required = false) String psuID,
			@RequestHeader(value = "USER-ID", required = true) String userID,
			@RequestHeader(value = "PSU-Channel", required = true) String channelID,
			@RequestHeader(value = "PSU-Resv-Field1", required = false) String resvfield1,
			@RequestHeader(value = "PSU-Resv-Field2", required = false) String resvfield2,
			@RequestBody BukCreditTransferRequest mcCreditTransferRequest) {

		MCCreditTransferResponse response = null;

		response = ipsConnection.createBulkCreditConnection(psuDeviceID, psuIpAddress, psuID, 
				 mcCreditTransferRequest, userID,p_id,channelID,resvfield1,resvfield2);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	//// Bulk Debit Fund Transfer
	@PostMapping(path = "/api/ws/bulkDebitFndTransfer", produces = "application/json", consumes = "application/json")
	public ResponseEntity<MCCreditTransferResponse> bulkDebitFndTransfer(
			@RequestHeader(value = "P-ID", required = true)   String p_id,
			@RequestHeader(value = "PSU-Device-ID", required = true) String psuDeviceID,
			@RequestHeader(value = "PSU-IP-Address", required = false) String psuIpAddress,
			@RequestHeader(value = "USER-ID", required = true) String userID,
			@RequestHeader(value = "PSU-Channel", required = true) String channelID,
			@RequestHeader(value = "PSU-Resv-Field1", required = false) String resvfield1,
			@RequestHeader(value = "PSU-Resv-Field2", required = false) String resvfield2,
			@RequestBody BulkDebitFndTransferRequest bulkDebitFndTransferRequest) {

		MCCreditTransferResponse response = null;

		response = ipsConnection.createBulkDebitConnection(psuDeviceID, psuIpAddress, bulkDebitFndTransferRequest,
				userID,p_id,channelID,resvfield1,resvfield2);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}



	//// Reverse Transaction (Debit) from IPS Admin Manually
	@PostMapping(path = "api/ws/{userID}/reverseDebit", produces = "application/json", consumes = "application/json")
	public ResponseEntity<String> ManualdebitReverseTransaction(
			@PathVariable(value = "userID", required = true) String UserID,
			@RequestHeader(value = "PSU-Device-ID", required = true) String psuDeviceID,
			@RequestHeader(value = "PSU-IP-Address", required = true) String psuIpAddress,
			@RequestBody TranCimCBSTable tranCBSTable) {

		logger.info("ManualDebitReverse" + UserID);
		String response = ipsConnection.reverseDebitTransaction(tranCBSTable, UserID);
		return new ResponseEntity<String>(response, HttpStatus.OK);
	}

	//// Reverse Transaction (Credit) from IPS Admin Manually
	@PostMapping(path = "api/ws/{userID}/reverseCredit", produces = "application/json", consumes = "application/json")
	public ResponseEntity<String> ManualCreditReverseTransaction(
			@PathVariable(value = "userID", required = true) String UserID,
			@RequestHeader(value = "PSU-Device-ID", required = true) String psuDeviceID,
			@RequestHeader(value = "PSU-IP-Address", required = true) String psuIpAddress,
			@RequestBody TranCimCBSTable tranCBSTable) {

		logger.info("ManualCreditReverse" + UserID);
		String response = ipsConnection.reverseCreditTransaction(tranCBSTable, UserID);
		return new ResponseEntity<String>(response, HttpStatus.OK);
	}

	//// Reverse Transaction (Bulk Credit) from IPS Admin Manually
	@PostMapping(path = "api/ws/{userID}/reverseBulkCredit", produces = "application/json", consumes = "application/json")
	public ResponseEntity<String> ManualreverseBulkCreditReverseTransaction(
			@PathVariable(value = "userID", required = true) String UserID,
			@RequestHeader(value = "PSU-Device-ID", required = true) String psuDeviceID,
			@RequestHeader(value = "PSU-IP-Address", required = true) String psuIpAddress,
			@RequestBody TranCimCBSTable tranCBSTable) {

		logger.info("ManualDebitReverse" + UserID);
		String response = ipsConnection.reversereverseBulkCreditTransaction(tranCBSTable, UserID);
		return new ResponseEntity<String>(response, HttpStatus.OK);
	}

	//// Reverse Transaction (Bulk Debit) from IPS Admin Manually
	@PostMapping(path = "api/ws/{userID}/reverseBulkDebit", produces = "application/json", consumes = "application/json")
	public ResponseEntity<String> ManualreverseBulkDebitReverseTransaction(
			@PathVariable(value = "userID", required = true) String UserID,
			@RequestHeader(value = "PSU-Device-ID", required = true) String psuDeviceID,
			@RequestHeader(value = "PSU-IP-Address", required = true) String psuIpAddress,
			@RequestBody TranCimCBSTable tranCBSTable) {

		logger.info("ManualDebitReverse" + UserID);
		String response = ipsConnection.reverseBulkDebitTransaction(tranCBSTable, UserID);
		return new ResponseEntity<String>(response, HttpStatus.OK);
	}

	//// Settlement Limit Cap
	@PostMapping(path = "/api/ws/setSL", produces = "application/json", consumes = "application/json")
	public ResponseEntity<String> setSL() throws FileNotFoundException, DatatypeConfigurationException, JAXBException {

		logger.info("Calling Modify Settlement Limit Connection flow Starts");
		String response = ipsConnection.setSL();

		return new ResponseEntity<String>(response, HttpStatus.OK);
	}

	//// Settlement Report
	@PostMapping(path = "/api/ws/slReport", produces = "application/json", consumes = "application/json")
	public ResponseEntity<SettlementLimitResponse> slReport()
			throws FileNotFoundException, DatatypeConfigurationException, JAXBException {

		logger.info("Calling Settlement Report Connection flow Starts");
		SettlementLimitResponse response = ipsConnection.generateSLReport();

		return new ResponseEntity<SettlementLimitResponse>(response, HttpStatus.OK);
	}

	
    ////Other Bank List(IPS Participants)
	@GetMapping(path = "/api/ws/otherParticipantBanks", produces = "application/json")
	public ResponseEntity<List<OtherBankDetResponse>> otherParticipantBanks(
			@RequestHeader(value = "PSU-Device-ID", required = true) String psuDeviceID,
			@RequestHeader(value = "PSU-IP-Address", required = true) String psuIpAddress,
			@RequestHeader(value = "PSU-Channel", required = false) String channelID,
			@RequestHeader(value = "PSU-ID", required = false) String psuID) {

		logger.info("Get Bank List flow Starts");
		List<OtherBankDetResponse> response = ipsDao.getOtherBankDet();

		return new ResponseEntity<List<OtherBankDetResponse>>(response, HttpStatus.OK);
	}
	
	//// Bank List(IPS Participants)
	@GetMapping(path = "/api/ws/participantBanks", produces = "application/json")
	public ResponseEntity<List<OtherBankDetResponse>> otherBankDet(
			@RequestHeader(value = "PSU-Device-ID", required = true) String psuDeviceID,
			@RequestHeader(value = "PSU-IP-Address", required = true) String psuIpAddress,
			@RequestHeader(value = "PSU-Channel", required = false) String channelID,
			@RequestHeader(value = "PSU-ID", required = false) String psuID) {

		logger.info("Get Bank List flow Starts");
		List<OtherBankDetResponse> response = ipsDao.getPartcipantBankDet();

		return new ResponseEntity<List<OtherBankDetResponse>>(response, HttpStatus.OK);
	}

	//// MYT Registration
	//// Create Public Key
	@PostMapping(path = "/mvc/0/public-service/accounts/public-keys", produces = "application/json;charset=utf-8", consumes = "application/json")
	public ResponseEntity<UserRegistrationResponse> createUserPublicKey(
			@RequestHeader(value = "SenderParticipant-BIC", required = false) String senderParticipantBIC,
			@RequestHeader(value = "SenderParticipant-MemberID", required = false) String senderParticipantMemberID,
			@RequestHeader(value = "ReceiverParticipant-BIC", required = false) String receiverParticipantBIC,
			@RequestHeader(value = "ReceiverParticipant-MemberID", required = false) String receiverPartcipantMemberID,
			@RequestHeader(value = "PSU-DEVICE-ID", required = true) String psuDeviceID,
			@RequestHeader(value = "PSU-IP-ADDRESS", required = true) String ipAddress,
			@RequestHeader(value = "PSU-ID", required = true) String psuID,
			@RequestBody UserRegistrationRequest userRequest) throws KeyManagementException, UnrecoverableKeyException,
			KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException {

		logger.info("Calling Create Public Key flow Starts" + userRequest.toString());

		UserRegistrationResponse response = ipsConnection.createUserPublicKey(senderParticipantBIC,
				senderParticipantMemberID, receiverParticipantBIC, receiverPartcipantMemberID, psuDeviceID, ipAddress,
				psuID, userRequest);

		return new ResponseEntity<UserRegistrationResponse>(response, HttpStatus.OK);
	}

	//// Update MYT Application Customer Details
	@PutMapping(path = "/mvc/0/public-service/accounts/public-keys/{RecordID}", produces = "application/json;charset=utf-8", consumes = "application/json")
	public ResponseEntity<UserRegistrationResponse> updateUserPublicKey(
			@RequestHeader(value = "SenderParticipant-BIC", required = false) String senderParticipantBIC,
			@RequestHeader(value = "SenderParticipant-MemberID", required = false) String senderParticipantMemberID,
			@RequestHeader(value = "ReceiverParticipant-BIC", required = false) String receiverParticipantBIC,
			@RequestHeader(value = "ReceiverParticipant-MemberID", required = false) String receiverPartcipantMemberID,
			@RequestHeader(value = "PSU-DEVICE-ID", required = true) String psuDeviceID,
			@RequestHeader(value = "PSU-IP-ADDRESS", required = true) String ipAddress,
			@RequestHeader(value = "PSU-ID", required = true) String psuID, @PathVariable("RecordID") String recordID,
			@RequestBody UserRegistrationRequest userRequest) throws KeyManagementException, UnrecoverableKeyException,
			KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException {

		logger.info("Calling Update Public Key flow Starts");

		UserRegistrationResponse response = ipsConnection.updateUserPublicKey(senderParticipantBIC,
				senderParticipantMemberID, receiverParticipantBIC, receiverPartcipantMemberID, psuDeviceID, ipAddress,
				psuID, userRequest, recordID);

		return new ResponseEntity<UserRegistrationResponse>(response, HttpStatus.OK);
	}

	//// Delete Public Key
	@DeleteMapping(path = "/mvc/0/public-service/accounts/public-keys/{RecordID}", produces = "application/json;charset=utf-8", consumes = "application/json")
	public ResponseEntity<String> deleteUserPublicKey(
			@RequestHeader(value = "SenderParticipant-BIC", required = false) String senderParticipantBIC,
			@RequestHeader(value = "SenderParticipant-MemberID", required = false) String senderParticipantMemberID,
			@RequestHeader(value = "ReceiverParticipant-BIC", required = false) String receiverParticipantBIC,
			@RequestHeader(value = "ReceiverParticipant-MemberID", required = false) String receiverPartcipantMemberID,
			@RequestHeader(value = "PSU-DEVICE-ID", required = true) String psuDeviceID,
			@RequestHeader(value = "PSU-IP-ADDRESS", required = true) String ipAddress,
			@RequestHeader(value = "PSU-ID", required = true) String psuID, @PathVariable("RecordID") String recordID) {

		String response = ipsConnection.deletePublicKey(senderParticipantBIC, senderParticipantMemberID,
				receiverParticipantBIC, receiverPartcipantMemberID, psuDeviceID, ipAddress, psuID, recordID);

		return new ResponseEntity<String>(response, HttpStatus.OK);
	}

	//// MYT Authentication
	/// Validate OTP
	@PutMapping(path = "/mvc/0/public-service/accounts/public-keys/{RecordID}/authorisations/{AuthID}", produces = "application/json;charset=utf-8", consumes = "application/json")
	public ResponseEntity<SCAAthenticationResponse> authTransaction(
			@RequestHeader(value = "SenderParticipant-BIC", required = false) String senderParticipantBIC,
			@RequestHeader(value = "SenderParticipant-MemberID", required = false) String senderParticipantMemberID,
			@RequestHeader(value = "ReceiverParticipant-BIC", required = false) String receiverParticipantBIC,
			@RequestHeader(value = "ReceiverParticipant-MemberID", required = false) String receiverPartcipantMemberID,
			@RequestHeader(value = "PSU-DEVICE-ID", required = true) String psuDeviceID,
			@RequestHeader(value = "PSU-IP-ADDRESS", required = true) String ipAddress,
			@RequestHeader(value = "PSU-ID", required = true) String psuID,
			@RequestHeader(value = "Authorization", required = false) String authorization,
			@PathVariable("RecordID") String recordID, @PathVariable("AuthID") String authID,
			@RequestBody SCAAuthenticatedData scaAuthenticatedData) {

		logger.info("authTransaction  AuthID:" + authID);

		System.out.println("authTransaction  AuthID:" + authID);
		System.out.println("authTransaction  RecordID:" + recordID);
		logger.info("authTransaction  RecordID:" + recordID);
		logger.info("authTransaction  authorization:" + authorization);

		logger.info("Calling SCA Authentication Starts");
		logger.info("authTransaction :" + scaAuthenticatedData.toString());

		SCAAthenticationResponse response = ipsConnection.authTransaction(senderParticipantBIC,
				senderParticipantMemberID, receiverParticipantBIC, receiverPartcipantMemberID, psuDeviceID, ipAddress,
				psuID, scaAuthenticatedData, recordID, authID);

		return new ResponseEntity<SCAAthenticationResponse>(response, HttpStatus.OK);
	}

	///// MYT Consent Registration
	@PostMapping(path = "/mvc/0/public-service/account-access-consents", produces = "application/json;charset=utf-8", consumes = "application/json")
	public ResponseEntity<ConsentResponse> registerConsent(
			@RequestHeader(value = "PSU-DEVICE-ID", required = false) String psuDeviceID,
			@RequestHeader(value = "PSU-IP-ADDRESS", required = false) String ipAddress,
			@RequestHeader(value = "PSU-ID", required = false) String psuID,
			@RequestBody ConsentRequest consentRequest) {
		logger.info("Calling Consent");

		logger.info("Calling Create Consent flow Starts" + consentRequest.toString());

		String consentID = sequence.generateRecordId();
		String authID = sequence.generateRecordId();
		ConsentResponse response = ipsConnection.createConsentID(psuDeviceID, ipAddress, psuID, consentRequest,
				consentID, authID);

		return new ResponseEntity<ConsentResponse>(response, HttpStatus.OK);
	}

	//// MYT Authentication
	/// Validate Consent OTP
	@PutMapping(path = "/mvc/0/public-service/account-access-consents/{ConsentID}/authorisations/{AuthID}", produces = "application/json;charset=utf-8", consumes = "application/json")
	public ResponseEntity<SCAAthenticationResponse> authTransactionConsent(
			@RequestHeader(value = "PSU-DEVICE-ID", required = false) String psuDeviceID,
			@RequestHeader(value = "PSU-IP-ADDRESS", required = false) String ipAddress,
			@RequestHeader(value = "PSU-ID", required = false) String psuID,
			@PathVariable("ConsentID") String consentID, @PathVariable("AuthID") String authID,
			@RequestBody SCAAuthenticatedData scaAuthenticatedData) {
		logger.info("Calling SCA Authentication Starts");
		logger.info("authTransaction :" + scaAuthenticatedData.toString());

		SCAAthenticationResponse response = ipsConnection.authTransactionConsent(psuDeviceID, ipAddress, psuID,
				scaAuthenticatedData, consentID, authID);

		return new ResponseEntity<SCAAthenticationResponse>(response, HttpStatus.OK);
	}

	//// Delete Consent ID
	@DeleteMapping(path = "/mvc/0/public-service/account-access-consents/{ConsentID}", produces = "application/json;charset=utf-8", consumes = "application/json")
	public ResponseEntity<String> deleteConsent(
			@RequestHeader(value = "PSU-DEVICE-ID", required = false) String psuDeviceID,
			@RequestHeader(value = "PSU-IP-ADDRESS", required = false) String ipAddress,
			@RequestHeader(value = "PSU-ID", required = false) String psuID,
			@PathVariable("ConsentID") String ConsentID) {

		return new ResponseEntity<String>("Success", HttpStatus.OK);
	}

///// MYT Consent Registration
	@PostMapping(path = "/mvc/0/public-service/accounts-consents", produces = "application/json;charset=utf-8", consumes = "application/json")
	public ResponseEntity<ConsentAccessResponse> registerConsentAccess(
			@RequestHeader(value = "X-Request-ID", required = true) String x_request_id,
			@RequestHeader(value = "SenderParticipant-BIC", required = false) String sender_participant_bic,
			@RequestHeader(value = "SenderParticipant-MemberID", required = false) String sender_participant_member_id,
			@RequestHeader(value = "ReceiverParticipant-BIC", required = false) String receiver_participant_bic,
			@RequestHeader(value = "ReceiverParticipant-MemberID", required = false) String receiver_participant_member_id,
			@RequestHeader(value = "PSU-Device-ID", required = true) String psuDeviceID,
			@RequestHeader(value = "PSU-IP-Address", required = true) String psuIPAddress,
			@RequestHeader(value = "PSU-ID", required = true) String psuID,
			@RequestHeader(value = "PSU-ID-Country", required = true) String psuIDCountry,
			@RequestHeader(value = "PSU-ID-Type", required = true) String psuIDType,
			@RequestBody ConsentAccessRequest consentRequest) {
		logger.info("Calling Consent");

		logger.info("Calling Create Consent access flow Starts" + consentRequest.toString());
		logger.info("x_request_id" + x_request_id.toString());

		if (ipsDao.invalidConsentInwInqX_request_ID(x_request_id)) {
			
			if(ipsDao.checkBankAgentExistIncomingMsg(sender_participant_bic)){
				ConsentAccessResponse response = ipsConnection.createConsentAccessID(x_request_id, sender_participant_bic,
						sender_participant_member_id, receiver_participant_bic, receiver_participant_member_id, psuDeviceID,
						psuIPAddress, psuID, psuIDCountry, psuIDType, consentRequest);

				return new ResponseEntity<ConsentAccessResponse>(response, HttpStatus.OK);
			}else {
				throw new IPSXRestException(errorCode.ErrorCodeRegistration("5"));
			}
			
		} else {
			throw new IPSXRestException(errorCode.ErrorCodeRegistration("1"));
		}
	}
	
	

	///// MYT Consent Registration Auth
	@PutMapping(path = "/mvc/0/public-service/accounts-consents/{consentID}/authorisations/{authID}", produces = "application/json;charset=utf-8", consumes = "application/json")
	public ResponseEntity<SCAAthenticationResponse> authConsentAccess(
			@PathVariable(value = "consentID", required = true) String consentID,
			@PathVariable(value = "authID", required = true) String authID,
			@RequestHeader(value = "X-Request-ID", required = true) String x_request_id,
			@RequestHeader(value = "SenderParticipant-BIC", required = false) String sender_participant_bic,
			@RequestHeader(value = "SenderParticipant-MemberID", required = false) String sender_participant_member_id,
			@RequestHeader(value = "ReceiverParticipant-BIC", required = false) String receiver_participant_bic,
			@RequestHeader(value = "ReceiverParticipant-MemberID", required = false) String receiver_participant_member_id,
			@RequestHeader(value = "PSU-Device-ID", required = true) String psuDeviceID,
			@RequestHeader(value = "PSU-IP-Address", required = true) String psuIPAddress,
			@RequestHeader(value = "PSU-ID", required = true) String psuID,
			@RequestHeader(value = "PSU-ID-Country", required = true) String psuIDCountry,
			@RequestHeader(value = "PSU-ID-Type", required = true) String psuIDType,
			@RequestHeader(value = "Cryptogram", required = true) String cryptogram,
			@RequestBody SCAAuthenticatedData scaAuthenticatedData) {
		logger.info("Calling Consent");

		logger.info("Consent Authentication Starts" + scaAuthenticatedData.toString());
		logger.info("x_request_id" + x_request_id.toString());
		logger.info("consentID" + consentID.toString());
		logger.info("authID" + authID.toString());
		logger.info("Cryptogram" + cryptogram.toString());

		if (ipsDao.invalidConsentInwInqX_request_ID(x_request_id)) {
			SCAAthenticationResponse response = ipsConnection.authConsentAccessID(x_request_id, sender_participant_bic,
					sender_participant_member_id, receiver_participant_bic, receiver_participant_member_id, psuDeviceID,
					psuIPAddress, psuID, psuIDCountry, psuIDType, scaAuthenticatedData, consentID, authID, cryptogram);

			return new ResponseEntity<SCAAthenticationResponse>(response, HttpStatus.OK);
		} else {
			throw new IPSXRestException(errorCode.ErrorCodeRegistration("1"));
		}
	}

	///// MYT Consent Registration Auth
	@DeleteMapping(path = "/mvc/0/public-service/accounts-consents/{consentID}", produces = "application/json;charset=utf-8", consumes = "application/json")
	public ResponseEntity<ErrorRestResponse> deleteConsentAccess(
			@PathVariable(value = "consentID", required = true) String consentID,
			@RequestHeader(value = "X-Request-ID", required = true) String x_request_id,
			@RequestHeader(value = "SenderParticipant-BIC", required = false) String sender_participant_bic,
			@RequestHeader(value = "SenderParticipant-MemberID", required = false) String sender_participant_member_id,
			@RequestHeader(value = "ReceiverParticipant-BIC", required = false) String receiver_participant_bic,
			@RequestHeader(value = "ReceiverParticipant-MemberID", required = false) String receiver_participant_member_id,
			@RequestHeader(value = "PSU-Device-ID", required = true) String psuDeviceID,
			@RequestHeader(value = "PSU-IP-Address", required = true) String psuIPAddress,
			@RequestHeader(value = "PSU-ID", required = true) String psuID,
			@RequestHeader(value = "PSU-ID-Country", required = true) String psuIDCountry,
			@RequestHeader(value = "PSU-ID-Type", required = true) String psuIDType) {
		logger.info("Calling Consent");

		logger.info("Consent Authentication Starts deleteConsentAccess");
		logger.info("Consent Authentication Starts deleteConsentAccess"+consentID);
		logger.info("Consent Authentication Starts deleteConsentAccess"+x_request_id);

		if (ipsDao.invalidConsentInwInqX_request_ID(x_request_id)) {
			ErrorRestResponse response = ipsConnection.deleteConsentAccessID(x_request_id, sender_participant_bic,
					sender_participant_member_id, receiver_participant_bic, receiver_participant_member_id, psuDeviceID,
					psuIPAddress, psuID, psuIDCountry, psuIDType, consentID);
			return new ResponseEntity<ErrorRestResponse>(response, HttpStatus.OK);
		} else {
			throw new IPSXRestException(errorCode.ErrorCodeRegistration("1"));
		}
	}

	///// MYT Balance
	@GetMapping(path = "/mvc/0/public-service/accounts/{accountID}/balances", produces = "application/json;charset=utf-8", consumes = "application/json")
	public ResponseEntity<ConsentAccountBalance> MytAccessBalance(
			@PathVariable(value = "accountID", required = true) String accountID,
			@RequestHeader(value = "X-Request-ID", required = true) String x_request_id,
			@RequestHeader(value = "SenderParticipant-BIC", required = false) String sender_participant_bic,
			@RequestHeader(value = "SenderParticipant-MemberID", required = false) String sender_participant_member_id,
			@RequestHeader(value = "ReceiverParticipant-BIC", required = false) String receiver_participant_bic,
			@RequestHeader(value = "ReceiverParticipant-MemberID", required = false) String receiver_participant_member_id,
			@RequestHeader(value = "PSU-Device-ID", required = true) String psuDeviceID,
			@RequestHeader(value = "PSU-IP-Address", required = true) String psuIPAddress,
			@RequestHeader(value = "PSU-ID", required = true) String psuID,
			@RequestHeader(value = "PSU-ID-Country", required = true) String psuIDCountry,
			@RequestHeader(value = "PSU-ID-Type", required = true) String psuIDType,
			@RequestHeader(value = "Consent-ID", required = true) String consentID,
			@RequestHeader(value = "Cryptogram", required = true) String cryptogram) {
		logger.info("Calling Consent");

		logger.info("Consent Balance Request Starts" + accountID);

		if (ipsDao.invalidConsentInwInqX_request_ID(x_request_id)) {
			ConsentAccountBalance response = ipsConnection.consentBalance(x_request_id, sender_participant_bic,
					sender_participant_member_id, receiver_participant_bic, receiver_participant_member_id, psuDeviceID,
					psuIPAddress, psuID, psuIDCountry, psuIDType, consentID, accountID, cryptogram);
			return new ResponseEntity<ConsentAccountBalance>(response, HttpStatus.OK);
		} else {
			throw new IPSXRestException(errorCode.ErrorCodeRegistration("1"));
		}

		
	}

	///// MYT Account List
	@GetMapping(path = "/mvc/0/public-service/accounts", produces = "application/json;charset=utf-8", consumes = "application/json")
	public ResponseEntity<AccountListResponse> MytAccountList(
			@RequestHeader(value = "X-Request-ID", required = true) String x_request_id,
			@RequestHeader(value = "SenderParticipant-BIC", required = false) String sender_participant_bic,
			@RequestHeader(value = "SenderParticipant-MemberID", required = false) String sender_participant_member_id,
			@RequestHeader(value = "ReceiverParticipant-BIC", required = false) String receiver_participant_bic,
			@RequestHeader(value = "ReceiverParticipant-MemberID", required = false) String receiver_participant_member_id,
			@RequestHeader(value = "PSU-Device-ID", required = true) String psuDeviceID,
			@RequestHeader(value = "PSU-IP-Address", required = true) String psuIPAddress,
			@RequestHeader(value = "PSU-ID", required = true) String psuID,
			@RequestHeader(value = "PSU-ID-Country", required = true) String psuIDCountry,
			@RequestHeader(value = "PSU-ID-Type", required = true) String psuIDType,
			@RequestHeader(value = "Consent-ID", required = true) String consentID,
			@RequestHeader(value = "Cryptogram", required = true) String cryptogram) {

		logger.info("Calling Consent");

		logger.info("Consent Account List Request Starts" + consentID);

		if (ipsDao.invalidConsentInwInqX_request_ID(x_request_id)) {
			AccountListResponse response = ipsConnection.accountList(x_request_id, sender_participant_bic,
					sender_participant_member_id, receiver_participant_bic, receiver_participant_member_id, psuDeviceID,
					psuIPAddress, psuID, psuIDCountry, psuIDType, consentID, cryptogram);

			return new ResponseEntity<AccountListResponse>(response, HttpStatus.OK);
		} else {
			throw new IPSXRestException(errorCode.ErrorCodeRegistration("1"));
		}
	}

	///// MYT Account List
	@GetMapping(path = "/mvc/0/public-service/accounts/{AccountID}", produces = "application/json;charset=utf-8", consumes = "application/json")
	public ResponseEntity<AccountsListAccounts> MytAccountInd(
			@PathVariable(value = "AccountID", required = true) String accountID,
			@RequestHeader(value = "X-Request-ID", required = true) String x_request_id,
			@RequestHeader(value = "SenderParticipant-BIC", required = false) String sender_participant_bic,
			@RequestHeader(value = "SenderParticipant-MemberID", required = false) String sender_participant_member_id,
			@RequestHeader(value = "ReceiverParticipant-BIC", required = false) String receiver_participant_bic,
			@RequestHeader(value = "ReceiverParticipant-MemberID", required = false) String receiver_participant_member_id,
			@RequestHeader(value = "PSU-Device-ID", required = true) String psuDeviceID,
			@RequestHeader(value = "PSU-IP-Address", required = true) String psuIPAddress,
			@RequestHeader(value = "PSU-ID", required = true) String psuID,
			@RequestHeader(value = "PSU-ID-Country", required = true) String psuIDCountry,
			@RequestHeader(value = "PSU-ID-Type", required = true) String psuIDType,
			@RequestHeader(value = "Consent-ID", required = true) String consentID,
			@RequestHeader(value = "Cryptogram", required = true) String cryptogram) {

		logger.info("Calling Consent");

		logger.info("Consent Account List Request Starts" + consentID);

		if (ipsDao.invalidConsentInwInqX_request_ID(x_request_id)) {
			AccountsListAccounts response = ipsConnection.accountListInd(x_request_id, sender_participant_bic,
					sender_participant_member_id, receiver_participant_bic, receiver_participant_member_id, psuDeviceID,
					psuIPAddress, psuID, psuIDCountry, psuIDType, consentID, cryptogram, accountID);

			return new ResponseEntity<AccountsListAccounts>(response, HttpStatus.OK);
		} else {
			throw new IPSXRestException(errorCode.ErrorCodeRegistration("1"));
		}
	}

	///// MYT Transaction List
	@GetMapping(path = "/mvc/0/public-service/accounts/{AccountID}/transactions", produces = "application/json;charset=utf-8", consumes = "application/json")
	public ResponseEntity<TransactionListResponse> MytTranList(
			@PathVariable(value = "AccountID", required = true) String accountID,
			@RequestHeader(value = "X-Request-ID", required = true) String x_request_id,
			@RequestHeader(value = "SenderParticipant-BIC", required = false) String sender_participant_bic,
			@RequestHeader(value = "SenderParticipant-MemberID", required = false) String sender_participant_member_id,
			@RequestHeader(value = "ReceiverParticipant-BIC", required = false) String receiver_participant_bic,
			@RequestHeader(value = "ReceiverParticipant-MemberID", required = false) String receiver_participant_member_id,
			@RequestHeader(value = "PSU-Device-ID", required = true) String psuDeviceID,
			@RequestHeader(value = "PSU-IP-Address", required = true) String psuIPAddress,
			@RequestHeader(value = "PSU-ID", required = true) String psuID,
			@RequestHeader(value = "PSU-ID-Country", required = true) String psuIDCountry,
			@RequestHeader(value = "PSU-ID-Type", required = true) String psuIDType,
			@RequestHeader(value = "Consent-ID", required = true) String consentID,
			@RequestHeader(value = "Cryptogram", required = true) String cryptogram,
			@RequestParam(value = "fromBookingDateTime",required = true)String fromBookingDateTime,
			@RequestParam(value = "toBookingDateTime",required = true)String toBookingDateTime) {

		logger.info("Calling Consent");

		logger.info("Consent Account List Request Starts" + consentID);

		if (ipsDao.invalidConsentInwInqX_request_ID(x_request_id)) {
			TransactionListResponse response = ipsConnection.tranList(x_request_id, sender_participant_bic,
					sender_participant_member_id, receiver_participant_bic, receiver_participant_member_id, psuDeviceID,
					psuIPAddress, psuID, psuIDCountry, psuIDType, consentID, cryptogram, accountID, fromBookingDateTime,
					toBookingDateTime);

			return new ResponseEntity<TransactionListResponse>(response, HttpStatus.OK);
		} else {
			throw new IPSXRestException(errorCode.ErrorCodeRegistration("1"));
		}
	}

	@PostMapping(path = "api/ws/payableAccount", produces = "application/json", consumes = "application/json")
	public ResponseEntity<String> payableAccount(@RequestBody MCCreditTransferRequest mcCreditTransferRequest,
			@RequestHeader(value = "PSU_Device_ID", required = true) String psuDeviceID,
			@RequestHeader(value = "USER_ID", required = true) String userID) {
		String response = ipsConnection.payableAccount(mcCreditTransferRequest, userID);
		return new ResponseEntity<String>(response, HttpStatus.OK);

	}

	@RequestMapping(value = "api/ws/receivableAccount")
	public ResponseEntity<String> receivableAccount(@RequestBody MCCreditTransferRequest mcCreditTransferRequest,
			@RequestHeader(value = "USER_ID", required = true) String userID) {
		String response = ipsConnection.receivableAccount(mcCreditTransferRequest, userID);
		return new ResponseEntity<String>(response, HttpStatus.OK);

	}

	@RequestMapping(value = "/portal/ldap")
	public String testLdap() {
		// List<X509Certificate> se= cert.getCertificates();
		// System.out.println(se.toString());

		System.out.println(cronJobSchedular.getCertificates().get(0).getPublicKey());
		System.out.println(cronJobSchedular.getLdapCert().get(0).getSignature());
		System.out.println("Public key re------>" + cronJobSchedular.getLdapCert().get(0).getPublicKey());
		System.out.println("serial key re------>" + cronJobSchedular.getLdapCert().get(0).getSerialNumber());
		return "success";
	}

	
///// Wallet Registration
	@PostMapping(path = "/api/ws/wallet_Access", produces = "application/json;charset=utf-8", consumes = "application/json")
	public ResponseEntity<WalletAccessResponse> registerWalletAccess(
			@RequestHeader(value = "W-Request-ID", required = true) String w_request_id,
			@RequestHeader(value = "PSU-Device-ID", required = true) String psuDeviceID,
			@RequestHeader(value = "PSU-IP-Address", required = true) String psuIPAddress,
			@RequestHeader(value = "PSU-ID", required = true) String psuID,
			@RequestHeader(value = "PSU-ID-Country", required = true) String psuIDCountry,
			@RequestHeader(value = "PSU-ID-Type", required = true) String psuIDType,
			@RequestBody WalletAccessRequest walletRequest) {
		logger.info("Calling Wallet");

		logger.info("Calling Create Wallet access flow Starts" + walletRequest.toString());

		WalletAccessResponse response = ipsConnection.createWalletAccessID(w_request_id, psuDeviceID, psuIPAddress,
				psuID, psuIDCountry, psuIDType, walletRequest);

		return new ResponseEntity<WalletAccessResponse>(response, HttpStatus.OK);
	}

	///// Wallet Registration Auth
	@PutMapping(path = "/api/ws/accounts-wallet/{walletID}/authorisations/{authID}", produces = "application/json;charset=utf-8", consumes = "application/json")
	public ResponseEntity<SCAAthenticationResponse> authWalletAccess(
			@PathVariable(value = "walletID", required = true) String walletID,
			@PathVariable(value = "authID", required = true) String authID,
			@RequestHeader(value = "W-Request-ID", required = true) String w_request_id,
			@RequestHeader(value = "PSU-Device-ID", required = true) String psuDeviceID,
			@RequestHeader(value = "PSU-IP-Address", required = true) String psuIPAddress,
			@RequestHeader(value = "PSU-ID", required = true) String psuID,
			@RequestHeader(value = "PSU-ID-Country", required = true) String psuIDCountry,
			@RequestHeader(value = "PSU-ID-Type", required = true) String psuIDType,
			// @RequestHeader(value = "Cryptogram", required = true) String cryptogram,
			@RequestBody SCAAuthenticatedData scaAuthenticatedData) {
		logger.info("Calling Wallet");

		logger.info("Wallet Authentication Starts" + scaAuthenticatedData.toString());

		SCAAthenticationResponse response = ipsConnection.authWalletAccessID(w_request_id, psuDeviceID, psuIPAddress,
				psuID, psuIDCountry, psuIDType, scaAuthenticatedData, walletID, authID);

		return new ResponseEntity<SCAAthenticationResponse>(response, HttpStatus.OK);
	}

	///// Topup Wallet
	@PostMapping(path = "/api/ws/accounts-Wallet-topup/{walletID}", produces = "application/json;charset=utf-8", consumes = "application/json")
	public ResponseEntity<WalletFndTransferResponse> topupWallet(
			@PathVariable(value = "walletID", required = true) String walletID,
			@RequestHeader(value = "W-Request-ID", required = true) String w_request_id,
			@RequestHeader(value = "PSU-Device-ID", required = true) String psuDeviceID,
			@RequestHeader(value = "PSU-IP-Address", required = true) String psuIPAddress,
			@RequestHeader(value = "PSU-ID", required = true) String psuID,
			@RequestHeader(value = "PSU-ID-Country", required = true) String psuIDCountry,
			@RequestHeader(value = "PSU-ID-Type", required = true) String psuIDType,
			// @RequestHeader(value = "Cryptogram", required = true) String cryptogram,
			@RequestBody WalletFndTransferRequest walletFndTransferRequest) {
		logger.info("Calling Topup Wallet");

		WalletFndTransferResponse response = ipsConnection.walletFundTransferTopupConnection(w_request_id, psuDeviceID,
				psuIPAddress, psuID, psuIDCountry, psuIDType, walletID, walletFndTransferRequest);

		return new ResponseEntity<WalletFndTransferResponse>(response, HttpStatus.OK);
	}

	///// Wallet Transaction
	@PostMapping(path = "/api/ws/accounts-Wallet-fundTran/{walletID}/{tranTypeCode}", produces = "application/json;charset=utf-8", consumes = "application/json")
	public ResponseEntity<WalletFndTransferResponse> walletTransaction(
			@PathVariable(value = "walletID", required = true) String walletID,
			@PathVariable(value = "tranTypeCode", required = true) String tranTypeCode,
			@RequestHeader(value = "W-Request-ID", required = true) String w_request_id,
			@RequestHeader(value = "PSU-Device-ID", required = true) String psuDeviceID,
			@RequestHeader(value = "PSU-IP-Address", required = true) String psuIPAddress,
			@RequestHeader(value = "PSU-ID", required = true) String psuID,
			@RequestHeader(value = "PSU-ID-Country", required = true) String psuIDCountry,
			@RequestHeader(value = "PSU-ID-Type", required = true) String psuIDType,
			// @RequestHeader(value = "Cryptogram", required = true) String cryptogram,
			@RequestBody WalletFndTransferRequest walletFndTransferRequest) {
		logger.info("Calling Topup Wallet");

		WalletFndTransferResponse response = ipsConnection.walletFundTransferConnection(w_request_id, psuDeviceID,
				psuIPAddress, psuID, psuIDCountry, psuIDType, walletID, walletFndTransferRequest, tranTypeCode);

		return new ResponseEntity<WalletFndTransferResponse>(response, HttpStatus.OK);
	}

	///// Wallet Balance
	@PostMapping(path = "/api/ws/accounts-Wallet-balance/{walletID}", produces = "application/json;charset=utf-8", consumes = "application/json")
	public ResponseEntity<WalletBalanceResponse> walletBalane(
			@PathVariable(value = "walletID", required = true) String walletID,
			@RequestHeader(value = "W-Request-ID", required = true) String w_request_id,
			@RequestHeader(value = "PSU-Device-ID", required = true) String psuDeviceID,
			@RequestHeader(value = "PSU-IP-Address", required = true) String psuIPAddress,
			@RequestHeader(value = "PSU-ID", required = true) String psuID,
			@RequestHeader(value = "PSU-ID-Country", required = true) String psuIDCountry,
			@RequestHeader(value = "PSU-ID-Type", required = true) String psuIDType) {
		logger.info("Calling Wallet Balance");

		WalletBalanceResponse response = ipsConnection.walletBalnceResponse(w_request_id, psuDeviceID, psuIPAddress,
				psuID, psuIDCountry, psuIDType, walletID);

		return new ResponseEntity<WalletBalanceResponse>(response, HttpStatus.OK);
	}

	///// Wallet Balance
	@PostMapping(path = "/api/ws/accounts-Wallet-statement/{walletID}", produces = "application/json;charset=utf-8", consumes = "application/json")
	public ResponseEntity<WalletStatementResponse> walletStatement(
			@PathVariable(value = "walletID", required = true) String walletID,
			@RequestHeader(value = "W-Request-ID", required = true) String w_request_id,
			@RequestHeader(value = "PSU-Device-ID", required = true) String psuDeviceID,
			@RequestHeader(value = "PSU-IP-Address", required = true) String psuIPAddress,
			@RequestHeader(value = "PSU-ID", required = true) String psuID,
			@RequestHeader(value = "PSU-ID-Country", required = true) String psuIDCountry,
			@RequestHeader(value = "PSU-ID-Type", required = true) String psuIDType) {
		logger.info("Calling Wallet Balance");

		WalletStatementResponse response = ipsConnection.walletStatement(w_request_id, psuDeviceID, psuIPAddress, psuID,
				psuIDCountry, psuIDType, walletID);

		return new ResponseEntity<WalletStatementResponse>(response, HttpStatus.OK);
	}

	//// Bank Management
	@PostMapping(path = "/api/ws/accounts-consents", produces = "application/json;charset=utf-8", consumes = "application/json")
	public ResponseEntity<McConsentOutwardAccessResponse> outwardAccountsConsents(
			@RequestHeader(value = "X-Request-ID", required = true) String x_request_id,
			@PathVariable(value = "consentID", required = false) String consentID,
			@RequestHeader(value = "PSU-Device-ID", required = true) String psuDeviceID,
			@RequestHeader(value = "PSU-IP-Address", required = true) String psuIPAddress,
			@RequestHeader(value = "PSU-ID", required = true) String psuID,
			@RequestHeader(value = "PSU-ID-Country", required = true) String psuIDCountry,
			@RequestHeader(value = "PSU-ID-Type", required = true) String psuIDType,
			@RequestHeader(value = "PSU-Channel", required = true) String channelID,
			@Valid @RequestBody ConsentOutwardAccessRequest consentOutwardAccessRequest) throws Exception {

		if (ipsDao.invalidConsentInqX_request_ID(x_request_id)) {
			if (!ipsDao.invalidBankCode(consentOutwardAccessRequest.getBankCode())) {
				if (!ipsDao.existDocType(psuIDType)) {
					if (!ipsDao.existConsentSchmType(consentOutwardAccessRequest.getAccounts().getShemeName())) {
						
						if (!ipsDao.existConsentPermission(consentOutwardAccessRequest.getPermissions())) {
							McConsentOutwardAccessResponse response = ipsConnection.outwardConsentAccess(x_request_id,
									psuDeviceID, psuIPAddress, psuID, psuIDCountry, psuIDType, consentOutwardAccessRequest,channelID);

							return new ResponseEntity<McConsentOutwardAccessResponse>(response, HttpStatus.OK);
						}else {
							String responseStatus = errorCode.validationError("BIPS27");
							throw new IPSXException(responseStatus);
						}
						
					}else {
						String responseStatus = errorCode.validationError("BIPS26");
						throw new IPSXException(responseStatus);
					}
					
				} else {
					String responseStatus = errorCode.validationError("BIPS16");
					throw new IPSXException(responseStatus);
				}

			} else {
				String responseStatus = errorCode.validationError("BIPS10");
				throw new IPSXException(responseStatus);
			}
		} else {
			String responseStatus = errorCode.validationError("BIPS25");
			throw new IPSXException(responseStatus);
		}

	}

	//// Bank Account Authorisation
	@PutMapping(path = "/api/ws/accounts-consents/{consentID}/authorisations/{authID}", produces = "application/json;charset=utf-8", consumes = "application/json")
	public ResponseEntity<ConsentOutwardAccessAuthResponse> outwardAccountsConsentsAuthorisation(
			@RequestHeader(value = "X-Request-ID", required = true) String x_request_id,
			@PathVariable(value = "consentID", required = true) String consentID,
			@PathVariable(value = "authID", required = true) String authID,
			@RequestHeader(value = "PSU-Device-ID", required = true) String psuDeviceID,
			@RequestHeader(value = "PSU-IP-Address", required = true) String psuIPAddress,
			@RequestHeader(value = "PSU-ID", required = true) String psuID,
			@RequestHeader(value = "PSU-ID-Country", required = true) String psuIDCountry,
			@RequestHeader(value = "PSU-ID-Type", required = true) String psuIDType,
			@RequestHeader(value = "PSU-Channel", required = true) String channelID,
			@RequestBody ConsentOutwardAccessAuthRequest consentOutwardAccessAuthRequest)
			throws NoSuchAlgorithmException, Exception {

		logger.debug("Calling Outward Consent Access Authorisation");

		if (ipsDao.invalidConsentInqX_request_ID(x_request_id)) {
			if (!ipsDao.existDocType(psuIDType)) {
				ConsentOutwardAccessAuthResponse response = ipsConnection.outwardConsentAccessSCAAuth(x_request_id,
						psuDeviceID, psuIPAddress, psuID, psuIDCountry, psuIDType, consentOutwardAccessAuthRequest,
						consentID, authID,channelID);

				return new ResponseEntity<ConsentOutwardAccessAuthResponse>(response, HttpStatus.OK);
			}else {
				String responseStatus = errorCode.validationError("BIPS16");
				throw new IPSXException(responseStatus);
			}
			
		} else {
			String responseStatus = errorCode.validationError("BIPS25");
			throw new IPSXException(responseStatus);
		}

	}

	//// Delete Bank Access
	@DeleteMapping(path = "/api/ws/accounts-consents/{consentID}", produces = "application/json;charset=utf-8", consumes = "application/json")
	public ResponseEntity<ErrorRestResponse> outwardDeleteAccountsConsents(
			@RequestHeader(value = "X-Request-ID", required = true) String x_request_id,
			@PathVariable(value = "consentID", required = true) String consentID,
			@RequestHeader(value = "PSU-Device-ID", required = true) String psuDeviceID,
			@RequestHeader(value = "PSU-IP-Address", required = true) String psuIPAddress,
			@RequestHeader(value = "PSU-ID", required = true) String psuID,
			@RequestHeader(value = "PSU-ID-Country", required = true) String psuIDCountry,
			@RequestHeader(value = "PSU-ID-Type", required = true) String psuIDType,
			@RequestHeader(value = "PSU-Channel", required = true) String channelID) throws NoSuchAlgorithmException, InvalidKeyException, KeyStoreException, CertificateException, UnrecoverableEntryException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException {

		logger.debug("Calling Outward Consent Delete");
		if (ipsDao.invalidConsentInqX_request_ID(x_request_id)) {
			ErrorRestResponse response = ipsConnection.outwardDeleteConsentAccess(x_request_id, psuDeviceID,
					psuIPAddress, psuID, psuIDCountry, psuIDType, consentID,channelID);

			return new ResponseEntity<ErrorRestResponse>(response, HttpStatus.OK);
		} else {
			String responseStatus = errorCode.validationError("BIPS25");
			throw new IPSXException(responseStatus);
		}

	}

	//// Read Balances
	@GetMapping(path = "/api/ws/accounts/{accountID}/balances", produces = "application/json;charset=utf-8")
	public ResponseEntity<ConsentAccountBalance> outwardAccountsConsentsBalances(
			@RequestHeader(value = "X-Request-ID", required = true) String x_request_id,
			@PathVariable(value = "accountID", required = true) String accountID,
			@RequestHeader(value = "PSU-Device-ID", required = true) String psuDeviceID,
			@RequestHeader(value = "PSU-IP-Address", required = true) String psuIPAddress,
			@RequestHeader(value = "PSU-ID", required = true) String psuID,
			@RequestHeader(value = "PSU-ID-Country", required = true) String psuIDCountry,
			@RequestHeader(value = "PSU-ID-Type", required = true) String psuIDType,
			@RequestHeader(value = "Consent-ID", required = true) String consentID,
			@RequestHeader(value = "PSU-Channel", required = true) String channelID) throws Exception {

		logger.debug("Calling Outward Consent Access Balance Inquiry");

		if (ipsDao.invalidConsentInqX_request_ID(x_request_id)) {
			ConsentAccountBalance response = ipsConnection.outwardConsentAccessBalances(x_request_id, psuDeviceID,
					psuIPAddress, psuID, psuIDCountry, psuIDType, consentID, accountID,channelID);

			return new ResponseEntity<ConsentAccountBalance>(response, HttpStatus.OK);
		} else {
			String responseStatus = errorCode.validationError("BIPS25");
			throw new IPSXException(responseStatus);
		}

	}

	//// Read Transaction Inquiry
	@GetMapping(path = "/api/ws/accounts/{accountID}/transactions", produces = "application/json;charset=utf-8")
	public ResponseEntity<TransactionListResponse> outwardAccountsConsentstransactions(
			@RequestHeader(value = "X-Request-ID", required = true) String x_request_id,
			@PathVariable(value = "accountID", required = true) String accountID,
			@RequestHeader(value = "PSU-Device-ID", required = true) String psuDeviceID,
			@RequestHeader(value = "PSU-IP-Address", required = true) String psuIPAddress,
			@RequestHeader(value = "PSU-ID", required = true) String psuID,
			@RequestHeader(value = "PSU-ID-Country", required = true) String psuIDCountry,
			@RequestHeader(value = "PSU-ID-Type", required = true) String psuIDType,
			@RequestHeader(value = "Consent-ID", required = true) String consentID,
			@RequestHeader(value = "PSU-Channel", required = true) String channelID,
			@RequestParam(value = "fromBookingDateTime", required = false) String fromBookingDateTime,
			@RequestParam(value = "toBookingDateTime", required = false) String toBookingDateTime) throws Exception {

		logger.debug("Calling Outward Consent Access Transaction Inquiry");

		if (ipsDao.invalidConsentInqX_request_ID(x_request_id)) {
			TransactionListResponse response = ipsConnection.outwardConsentAccessTransactionInc(x_request_id,
					psuDeviceID, psuIPAddress, psuID, psuIDCountry, psuIDType, consentID, accountID,
					fromBookingDateTime, toBookingDateTime,channelID);

			return new ResponseEntity<TransactionListResponse>(response, HttpStatus.OK);
		} else {
			String responseStatus = errorCode.validationError("BIPS25");
			throw new IPSXException(responseStatus);
		}

	}

	//// Read Accounts List Inquiry
	@GetMapping(path = "/api/ws/accounts", produces = "application/json;charset=utf-8")
	public ResponseEntity<AccountListResponse> outwardAccountsConsentsAccounts(
			@RequestHeader(value = "X-Request-ID", required = true) String x_request_id,
			@RequestHeader(value = "PSU-Device-ID", required = true) String psuDeviceID,
			@RequestHeader(value = "PSU-IP-Address", required = true) String psuIPAddress,
			@RequestHeader(value = "PSU-ID", required = true) String psuID,
			@RequestHeader(value = "PSU-ID-Country", required = true) String psuIDCountry,
			@RequestHeader(value = "PSU-ID-Type", required = true) String psuIDType,
			@RequestHeader(value = "Consent-ID", required = true) String consentID,
			@RequestHeader(value = "PSU-Channel", required = true) String channelID) throws Exception {

		logger.debug("Calling Outward Consent Access Account List Inquiry");

		if (ipsDao.invalidConsentInqX_request_ID(x_request_id)) {
			AccountListResponse response = ipsConnection.outwardConsentAccessAccountListInc(x_request_id, psuDeviceID,
					psuIPAddress, psuID, psuIDCountry, psuIDType, consentID,channelID);

			return new ResponseEntity<AccountListResponse>(response, HttpStatus.OK);
		} else {
			String responseStatus = errorCode.validationError("BIPS25");
			throw new IPSXException(responseStatus);
		}

	}

	//// Read Accounts List Inquiry
	@GetMapping(path = "/api/ws/accounts/{AccountId}", produces = "application/json;charset=utf-8")
	public ResponseEntity<AccountsListAccounts> outwardAccountsConsentsAccountsInd(
			@RequestHeader(value = "X-Request-ID", required = true) String x_request_id,
			@PathVariable(value = "AccountId", required = true) String accountID,
			@RequestHeader(value = "PSU-Device-ID", required = true) String psuDeviceID,
			@RequestHeader(value = "PSU-IP-Address", required = true) String psuIPAddress,
			@RequestHeader(value = "PSU-ID", required = true) String psuID,
			@RequestHeader(value = "PSU-ID-Country", required = true) String psuIDCountry,
			@RequestHeader(value = "PSU-ID-Type", required = true) String psuIDType,
			@RequestHeader(value = "Consent-ID", required = true) String consentID,
			@RequestHeader(value = "PSU-Channel", required = true) String channelID) throws Exception {

		logger.debug("Calling Outward Consent Access Account Inquiry");

		if (ipsDao.invalidConsentInqX_request_ID(x_request_id)) {
			AccountsListAccounts response = ipsConnection.outwardConsentAccessAccountInc(x_request_id, psuDeviceID,
					psuIPAddress, psuID, psuIDCountry, psuIDType, consentID, accountID,channelID);

			return new ResponseEntity<AccountsListAccounts>(response, HttpStatus.OK);
		} else {
			String responseStatus = errorCode.validationError("BIPS25");
			throw new IPSXException(responseStatus);
		}

	}

	//// Read Accounts List Inquiry
	@GetMapping(path = "/api/ws/consentAccounts/{documentID}", produces = "application/json;charset=utf-8")
	public ResponseEntity<List<RegConsentAccountList>> outwardconsentAccountList(
			@PathVariable(value = "documentID", required = true) String documentID,
			@RequestHeader(value = "PSU-Device-ID", required = true) String psuDeviceID,
			@RequestHeader(value = "PSU-IP-Address", required = true) String psuIPAddress,
			@RequestHeader(value = "PSU-Channel", required = false) String channelID) throws Exception {

		logger.debug("Calling Outward Consent Registered Account ");

		List<RegConsentAccountList> response = ipsConnection.outwardConsentRegisterAccountList(psuDeviceID,
				psuIPAddress, documentID);

		return new ResponseEntity<List<RegConsentAccountList>>(response, HttpStatus.OK);

	}

	/* Credit Fund Transfer Initiated from MConnect Application */
	/* MConnect Initiate the request */
	@PostMapping(path = "/api/ws/bulkRTPTransfer", produces = "application/json", consumes = "application/json")
	public ResponseEntity<RTPbulkTransferResponse> bulkRTPTransfer(
			@RequestHeader(value = "P-ID", required = true) @NotEmpty(message = "Required") String p_id,
			@RequestHeader(value = "PSU-Device-ID", required = true) @NotEmpty(message = "Required") String psuDeviceID,
			@RequestHeader(value = "PSU-IP-Address", required = true) String psuIpAddress,
			@RequestHeader(value = "PSU-ID", required = false) String psuID,
			@RequestHeader(value = "PSU-Channel", required = true) String channelID,
			@RequestHeader(value = "PSU-Resv-Field1", required = false) String resvfield1,
			@RequestHeader(value = "PSU-Resv-Field2", required = false) String resvfield2,
			@Valid @RequestBody RTPbulkTransferRequest rtpBulkTransferRequest)
			throws DatatypeConfigurationException, JAXBException, KeyManagementException, UnrecoverableKeyException,
			KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {

		logger.info("Service Bulk RTP Starts :" + p_id);

		RTPbulkTransferResponse response = null;

		logger.info("Calling Bulk RTP Credit Transfer Connection flow Starts");

		logger.info("PID:" + p_id);
		logger.info("PSU_Device_ID:" + psuDeviceID);
		logger.info("PSU_IP_Address:" + psuIpAddress);
		logger.info("PSU_ID:" + psuID);

		logger.info("RTP Bulk Request->" + rtpBulkTransferRequest);
		
		String dailyamt=	ipsDao.getMaxAmountPerDay(rtpBulkTransferRequest.getRemitterAccount().getAcctNumber(),rtpBulkTransferRequest.getBenAccount());
		String weeklyamt=	ipsDao.getMaxAmountPerDay(rtpBulkTransferRequest.getRemitterAccount().getAcctNumber(),rtpBulkTransferRequest.getBenAccount());
		String monthlyamt=	ipsDao.getMaxAmountPerDay(rtpBulkTransferRequest.getRemitterAccount().getAcctNumber(),rtpBulkTransferRequest.getBenAccount());

		System.out.println("BANKCODE :"+dailyamt);
		

		if (ipsDao.invalidP_ID(p_id)) {
			
			if(ipsDao.checkReqUniqueId(rtpBulkTransferRequest)) {
				if (!ipsDao.invalidBankCode(rtpBulkTransferRequest.getRemitterAccount().getBankCode())) {
					if (!ipsDao.invalidRTPCurrencyCode(rtpBulkTransferRequest)) {
						if(!ipsDao.invalidRTPBenBankCode(rtpBulkTransferRequest.getBenAccount())) {
							
							List<ConsentOutwardAccessTable> regAccList = consentOutwardAccessTableRep
									.getAccountNumber(rtpBulkTransferRequest.getRemitterAccount().getAcctNumber());
							if (regAccList.size() > 0) {
								
								if(regAccList.get(0).getReceiverparticipant_bic().equals
										(ipsDao.getOtherBankAgent(rtpBulkTransferRequest.getRemitterAccount().getBankCode()).getBank_agent())) {
									if (Double.parseDouble(dailyamt)<=Integer.parseInt(env.getProperty("cim.maxamountdaily"))) {
										if (Double.parseDouble(weeklyamt)<=Integer.parseInt(env.getProperty("cim.maxamountweekly"))) {
											if (Double.parseDouble(monthlyamt)<=Integer.parseInt(env.getProperty("cim.maxamountmonthly"))) {
												response = ipsConnection.createBulkRTPconnection(psuDeviceID, psuIpAddress, psuID,
														rtpBulkTransferRequest, p_id, channelID, resvfield1, resvfield2);
											}else {
												String responseStatus = errorCode.validationError("BIPS53");
												throw new IPSXException(responseStatus);
											}
										}else {
											String responseStatus = errorCode.validationError("BIPS52");
											throw new IPSXException(responseStatus);
										}
									
									
									}else {
										String responseStatus = errorCode.validationError("BIPS51");

										throw new IPSXException(responseStatus);
									}
									
								}else {
									String responseStatus = errorCode.validationError("BIPS22");
									throw new IPSXException(responseStatus);
								}
								
							} else {
								String responseStatus = errorCode.validationError("BIPS31");
								throw new IPSXException(responseStatus);
							}
						}else {
							String responseStatus = errorCode.validationError("BIPS23");
							throw new IPSXException(responseStatus);
						}
					}else {
						String responseStatus = errorCode.validationError("BIPS8");
						throw new IPSXException(responseStatus);
					}
					

				} else {
					String responseStatus = errorCode.validationError("BIPS21");
					throw new IPSXException(responseStatus);
				}
			}else {
				String responseStatus = errorCode.validationError("BIPS24");
				throw new IPSXException(responseStatus);
			}

			 
		} else {
			String responseStatus = errorCode.validationError("BIPS13");
			throw new IPSXException(responseStatus);
		}

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/* Credit Fund Transfer Initiated from MConnect Application */
	/* MConnect Initiate the request */
	@PostMapping(path = "/api/ws/directMerchantFndTransfer", produces = "application/json", consumes = "application/json")
	public ResponseEntity<MCCreditTransferResponse> directMerchantFndTransfer(
			@RequestHeader(value = "P-ID", required = true) @NotEmpty(message = "Required") String p_id,
			@RequestHeader(value = "PSU-Device-ID", required = true) @NotEmpty(message = "Required") String psuDeviceID,
			@RequestHeader(value = "PSU-IP-Address", required = true) String psuIpAddress,
			@RequestHeader(value = "PSU-ID", required = false) String psuID,
			@RequestHeader(value = "PSU-Channel", required = true) String channelID,
			@RequestHeader(value = "PSU-Resv-Field1", required = false) String resvfield1,
			@RequestHeader(value = "PSU-Resv-Field2", required = false) String resvfield2,
			@Valid @RequestBody CIMMerchantDirectFndRequest mcCreditTransferRequest)
			throws DatatypeConfigurationException, JAXBException, KeyManagementException, UnrecoverableKeyException,
			KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {

		logger.info("Service Starts" + p_id);

		MCCreditTransferResponse response = null;

		logger.info("Calling Credit Transfer Connection flow Starts");
		if (ipsDao.checkConvenienceFeeValidation(mcCreditTransferRequest)) {
			if (ipsDao.invalidP_ID(p_id)) {
				// if(!ipsDao.invalidBankCode(mcCreditTransferRequest.getMerchantAccount().getPayeeParticipantCode()))
				// {
				response = ipsConnection.createMerchantFTConnection(psuDeviceID, psuIpAddress, psuID,
						mcCreditTransferRequest, p_id, channelID, resvfield1, resvfield2);
				// }else {
				// String responseStatus = errorCode.validationError("BIPS10");
				// throw new IPSXException(responseStatus);
				// }
			} else {
				String responseStatus = errorCode.validationError("BIPS13");
				throw new IPSXException(responseStatus);
			}
		}

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/* Credit Fund Transfer Initiated from MConnect Application */
	/* MConnect Initiate the request */
	@PostMapping(path = "/api/ws/rtpMerchantFndTransfer", produces = "application/json", consumes = "application/json")
	public ResponseEntity<RTPbulkTransferResponse> rtpMerchantFndTransfer(
			@RequestHeader(value = "P-ID", required = true) @NotEmpty(message = "Required") String p_id,
			@RequestHeader(value = "PSU-Device-ID", required = true) @NotEmpty(message = "Required") String psuDeviceID,
			@RequestHeader(value = "PSU-IP-Address", required = true) String psuIpAddress,
			@RequestHeader(value = "PSU-ID", required = false) String psuID,
			@RequestHeader(value = "PSU-Channel", required = true) String channelID,
			@RequestHeader(value = "PSU-Resv-Field1", required = false) String resvfield1,
			@RequestHeader(value = "PSU-Resv-Field2", required = false) String resvfield2,
			@Valid @RequestBody CIMMerchantDirectFndRequest mcCreditTransferRequest)
			throws DatatypeConfigurationException, JAXBException, KeyManagementException, UnrecoverableKeyException,
			KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {

		logger.info("Service Starts" + p_id);

		RTPbulkTransferResponse response = null;

		if (ipsDao.checkConvenienceFeeValidation(mcCreditTransferRequest)) {
			if (ipsDao.invalidP_ID(p_id)) {
				if (!ipsDao.invalidBankCode(mcCreditTransferRequest.getRemitterAccount().getBankCode())) {
					if (!ipsDao.invalidMerchantRTPCurrencyCode(mcCreditTransferRequest)) {
						if(!ipsDao.invalidMerchantRTPBenBankCode(mcCreditTransferRequest.getMerchantAccount().getPayeeParticipantCode())) {
							List<ConsentOutwardAccessTable> regAccList = consentOutwardAccessTableRep
									.getAccountNumber(mcCreditTransferRequest.getRemitterAccount().getAcctNumber());
							if (regAccList.size() > 0) {
								response = ipsConnection.createMerchantRTPconnection(psuDeviceID, psuIpAddress, psuID,
										mcCreditTransferRequest, p_id, channelID, resvfield1, resvfield2);

							} else {
								String responseStatus = errorCode.validationError("BIPS31");
								throw new IPSXException(responseStatus);
							}
						}else {
							String responseStatus = errorCode.validationError("BIPS23");
							throw new IPSXException(responseStatus);
						}
					}else {
						String responseStatus = errorCode.validationError("BIPS8");
						throw new IPSXException(responseStatus);
					}
				}else {
					String responseStatus = errorCode.validationError("BIPS21");
					throw new IPSXException(responseStatus);
				}

			} else {
				String responseStatus = errorCode.validationError("BIPS13");
				throw new IPSXException(responseStatus);
			}
		}

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping(path = "/api/ws/generateMerchantQRcode", produces = "application/json", consumes = "application/json")
	public ResponseEntity<CimMerchantResponse> genMerchantQRcode(
			@RequestHeader(value = "P-ID", required = true) @NotEmpty(message = "Required") String p_id,
			@RequestHeader(value = "PSU-Device-ID", required = true) @NotEmpty(message = "Required") String psuDeviceID,
			@RequestHeader(value = "PSU-IP-Address", required = true) String psuIpAddress,
			@RequestHeader(value = "PSU-ID", required = false) String psuID,
			@RequestHeader(value = "PSU-Channel", required = true) String channelID,
			@RequestHeader(value = "PSU-Resv-Field1", required = false) String resvfield1,
			@RequestHeader(value = "PSU-Resv-Field2", required = false) String resvfield2,
			@Valid @RequestBody CIMMerchantQRcodeRequest mcCreditTransferRequest)
			throws DatatypeConfigurationException, JAXBException, KeyManagementException, UnrecoverableKeyException,
			KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {

		logger.info("Service Starts generate QR Code");
		
		System.out.println(mcCreditTransferRequest.toString());

		CimMerchantResponse response = null;

		if (ipsDao.checkConvenienceFeeValidationQR(mcCreditTransferRequest)) {
			if (!ipsDao.invaliQRdBankCode(
					mcCreditTransferRequest.getMerchantAcctInformation().getPayeeParticipantCode())) {
				if (Currency.exists(mcCreditTransferRequest.getCurrency())) {
					if (Country.exists(mcCreditTransferRequest.getCountryCode())) {
						response = ipsConnection.createMerchantQRConnection(psuDeviceID, psuIpAddress, psuID,
								mcCreditTransferRequest, p_id, channelID, resvfield1, resvfield2);
					} else {
						String responseStatus = errorCode.validationError("BIPS20");
						throw new IPSXException(responseStatus);
					}

				} else {
					String responseStatus = errorCode.validationError("BIPS19");
					throw new IPSXException(responseStatus);
				}

			} else {
				String responseStatus = errorCode.validationError("BIPS10");
				throw new IPSXException(responseStatus);
			}

		}

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping(path = "/api/ws/scanMerchantQRcode", produces = "application/json", consumes = "application/json")
	public ResponseEntity<CIMMerchantDecodeQRFormatResponse> getMerchantQRdata(
			@RequestHeader(value = "P-ID", required = true) @NotEmpty(message = "Required") String p_id,
			@RequestHeader(value = "PSU-Device-ID", required = true) @NotEmpty(message = "Required") String psuDeviceID,
			@RequestHeader(value = "PSU-IP-Address", required = true) String psuIpAddress,
			@RequestHeader(value = "PSU-ID", required = false) String psuID,
			@RequestHeader(value = "PSU-Channel", required = true) String channelID,
			@RequestHeader(value = "PSU-Resv-Field1", required = false) String resvfield1,
			@RequestHeader(value = "PSU-Resv-Field2", required = false) String resvfield2,
			@Valid @RequestBody CIMMerchantQRRequestFormat mcCreditTransferRequest)
			throws DatatypeConfigurationException, JAXBException, KeyManagementException, UnrecoverableKeyException,
			KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {

		logger.info("Service Starts" + p_id);

		CIMMerchantDecodeQRFormatResponse response = ipsConnection.getMerchantQRdata(psuDeviceID, psuIpAddress, psuID,
				mcCreditTransferRequest, p_id, channelID, resvfield1, resvfield2);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping(path = "/api/ws/data")
	public String getData() throws SQLException {

		//ipsConnection.initGLPayment(new SimpleDateFormat("dd-MMM-yyyy").format(previousDay()));

		ipsConnection.initInformix();
		return "ff";
	}
	
	private Date previousDay() {
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		return cal.getTime();
	}


	@PostMapping(path = "/api/ws/InwTransferStatus", produces = "application/json", consumes = "application/json")
	public ResponseEntity<RTPTransferStatusResponse> bulkRTPTransfer(
			@RequestHeader(value = "X-Request-ID", required = true) @NotEmpty(message = "Required") String p_id,
			@RequestHeader(value = "PSU-Device-ID", required = true) @NotEmpty(message = "Required") String psuDeviceID,
			@RequestHeader(value = "PSU-IP-Address", required = true) String psuIpAddress,
			@Valid @RequestBody RTPTransferRequestStatus rtpTransferRequeststatus)
			throws DatatypeConfigurationException, JAXBException, KeyManagementException, UnrecoverableKeyException,
			KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {

		logger.info("Service Status RTP Starts :" + p_id);

		RTPTransferStatusResponse response = null;

		logger.info("RTP Status Request->" + rtpTransferRequeststatus);
		
		 response = ipsDao.invalidTran_ID(rtpTransferRequeststatus);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
////NPCI Request UPI Validation
	@PostMapping(path = "/mvc/0/public-service/reqvalqr", produces = "application/json", consumes = "application/json")
	public ResponseEntity<ErrorResponseforUPI> ReqValQr(
			@RequestHeader(value = "X-Request-ID", required = true)   String p_id,
			@RequestBody NpciupiReqcls npcireq) throws ParseException {

		
		//UPIRespEntity response = new UPIRespEntity();
		
		String response = npciqrcode.ValidateQrcode(npcireq,p_id);
		ErrorResponseforUPI errRes = new ErrorResponseforUPI();
if(response.equals("SUCCESS")) {
			
		}else if(response.equals("FAILURE")) {
			errRes.setErrorCode("01");
			errRes.setDescription("QR CODE VALIDATION FAILED");
		}else if(response.equals("EXPIRED")) {
			errRes.setErrorCode("02");
			errRes.setDescription("EXPIRED QR CODE");
		}

		return new ResponseEntity<ErrorResponseforUPI>(errRes, HttpStatus.OK);
	}

	
}
