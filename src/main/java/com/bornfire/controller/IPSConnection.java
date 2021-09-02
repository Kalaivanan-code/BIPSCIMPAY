package com.bornfire.controller;

 
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

/*CREATED BY	: KALAIVANAN RAJENDRAN.R
CREATED ON	: 30-DEC-2019
PURPOSE		: IPS Connection(Connection between BIPS,Connect 24 and IPSx Webservice)
*/

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.security.KeyFactory;
import java.security.KeyManagementException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import javax.imageio.ImageIO;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.bornfire.clientService.IPSXClient;
import com.bornfire.config.ErrorResponseCode;
import com.bornfire.config.Listener;
import com.bornfire.config.SequenceGenerator;
import com.bornfire.entity.AccountContactResponse;
import com.bornfire.entity.AccountListResponse;
import com.bornfire.entity.AccountsListAccounts;
import com.bornfire.entity.Amount;
import com.bornfire.entity.BankAgentTable;
import com.bornfire.entity.BukCreditTransferRequest;
import com.bornfire.entity.BulkDebitFndTransferRequest;
import com.bornfire.entity.C24FTResponse;
import com.bornfire.entity.C24FTResponseBalance;
import com.bornfire.entity.CIMCreditTransferRequest;
import com.bornfire.entity.CIMMerchantDecodeQRFormatResponse;
import com.bornfire.entity.CIMMerchantDecodeQRMerchantAcctInfo;
import com.bornfire.entity.CIMMerchantDecodeQRMerchantAddlInfo;
import com.bornfire.entity.CIMMerchantDirectFndRequest;
import com.bornfire.entity.CIMMerchantQRRequestFormat;
import com.bornfire.entity.CIMMerchantQRcodeRequest;
import com.bornfire.entity.CimCBSrequestData;
import com.bornfire.entity.CimCBSrequestHeader;
import com.bornfire.entity.CimCBSresponse;
import com.bornfire.entity.CimMerchantResponse;
import com.bornfire.entity.ConsentAccessRequest;
import com.bornfire.entity.ConsentAccessResponse;
import com.bornfire.entity.ConsentAccessTable;
import com.bornfire.entity.ConsentAccessTableRep;
import com.bornfire.entity.ConsentAccountBalance;
import com.bornfire.entity.ConsentOutwardAccessAuthRequest;
import com.bornfire.entity.ConsentOutwardAccessAuthResponse;
import com.bornfire.entity.ConsentOutwardAccessRequest;
import com.bornfire.entity.ConsentOutwardAccessRequestAccounts;
import com.bornfire.entity.ConsentOutwardAccessTable;
import com.bornfire.entity.ConsentOutwardAccessTableRep;
import com.bornfire.entity.ConsentOutwardAccessTmpTable;
import com.bornfire.entity.ConsentRequest;
import com.bornfire.entity.ConsentResponse;
import com.bornfire.entity.CreditTransferTransaction;
import com.bornfire.entity.CryptogramResponse;
import com.bornfire.entity.Data;
import com.bornfire.entity.EncodeQRFormatResponse;
import com.bornfire.entity.IPSChargesAndFeesRep;
import com.bornfire.entity.Links;
import com.bornfire.entity.MCCreditTransferRequest;
import com.bornfire.entity.MCCreditTransferResponse;
import com.bornfire.entity.ManualFndTransferRequest;
import com.bornfire.entity.McConsentOutwardAccessResponse;
import com.bornfire.entity.OtherBankDetResponse;
import com.bornfire.entity.RTPbulkTransferRequest;
import com.bornfire.entity.RTPbulkTransferResponse;
import com.bornfire.entity.ReadConsentBalance;
import com.bornfire.entity.RegConsentAccountList;
import com.bornfire.entity.RegPublicKey;
import com.bornfire.entity.RegPublicKeyRep;
import com.bornfire.entity.SCAAthenticationResponse;
import com.bornfire.entity.SCAAuthenticatedData;
import com.bornfire.entity.SettlementAccount;
import com.bornfire.entity.SettlementAccountRep;
import com.bornfire.entity.SettlementLimitResponse;
import com.bornfire.entity.TranCBSTable;
import com.bornfire.entity.TranMonitorStatus;
import com.bornfire.entity.TransactionListResponse;
import com.bornfire.entity.TransactionMonitor;
import com.bornfire.entity.TransactionMonitorRep;
import com.bornfire.entity.UserRegistrationRequest;
import com.bornfire.entity.UserRegistrationResponse;
import com.bornfire.entity.WalletAccessRequest;
import com.bornfire.entity.WalletAccessResponse;
import com.bornfire.entity.WalletAccessTable;
import com.bornfire.entity.WalletBalanceResponse;
import com.bornfire.entity.WalletFndTransferRequest;
import com.bornfire.entity.WalletFndTransferResponse;
import com.bornfire.entity.WalletRequestRegisterTable;
import com.bornfire.entity.WalletRequestRegisterTableRep;
import com.bornfire.entity.WalletStatement;
import com.bornfire.entity.WalletStatementResponse;
import com.bornfire.exception.Connect24Exception;
import com.bornfire.exception.ErrorRestResponse;
import com.bornfire.exception.IPSXException;
import com.bornfire.exception.IPSXRestException;
import com.bornfire.exception.ServerErrorException;
import com.bornfire.jaxb.pacs_008_001_08.ChargeBearerType1Code;
import com.bornfire.jaxb.pain_001_001_09.ChargeBearerType1Code1;
import com.bornfire.jaxb.wsdl.SendT;
import com.bornfire.messagebuilder.SignDocument;
import com.bornfire.qrcode.core.isos.Currency;
import com.bornfire.qrcode.core.model.mpm.TagLengthString;
import com.bornfire.qrcode.decoder.mpm.DecoderMpm;
import com.bornfire.qrcode.model.mpm.AdditionalDataField;
import com.bornfire.qrcode.model.mpm.AdditionalDataFieldTemplate;
import com.bornfire.qrcode.model.mpm.MerchantAccountInformation;
import com.bornfire.qrcode.model.mpm.MerchantAccountInformationReserved;
import com.bornfire.qrcode.model.mpm.MerchantAccountInformationReservedAdditional;
import com.bornfire.qrcode.model.mpm.MerchantAccountInformationTemplate;
import com.bornfire.qrcode.model.mpm.MerchantInformationLanguage;
import com.bornfire.qrcode.model.mpm.MerchantInformationLanguageTemplate;
import com.bornfire.qrcode.model.mpm.MerchantPresentedMode;
import com.bornfire.qrcode.model.mpm.Unreserved;
import com.bornfire.qrcode.model.mpm.UnreservedTemplate;
import com.bornfire.qrcode.validators.Crc16Validate;
import com.bornfire.qrcode.validators.MerchantPresentedModeValidate;
import com.bornfire.valid.context.ValidationResult;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import static com.bornfire.exception.ErrorResponseCode.*;

@Component
public class IPSConnection {

	private static final Logger logger = LoggerFactory.getLogger(IPSConnection.class);

	@Autowired
	Connect24Service connect24Service;

	@Autowired
	IPSXClient ipsxClient;

	@Autowired
	SequenceGenerator sequence;

	@Autowired
	TransactionMonitorRep tranRep;

	@Autowired
	IPSDao ipsDao;

	@Autowired
	ErrorResponseCode errorCode;

	@Autowired
	TaskExecutor taskExecutor;

	@Autowired
	Environment env;

	@Autowired
	RegPublicKeyRep regPublicKeyRep;
	
	@Autowired
	IPSChargesAndFeesRep ipsChargesAndFeeRep;
	
	@Autowired
	SettlementAccountRep settlAccountRep;
	
	@Autowired
	SignDocument signDocument;
	
	@Autowired
	Listener listener;
	
	@Autowired
	ConsentAccessTableRep consentAccessTableRep;
	
	@Autowired
	WalletRequestRegisterTableRep walletRequestRegisterTableRep;
	
	@Autowired
	ConsentIPSXservice consentIPSXservice;
	
	@Autowired
	CimCBSservice cimCBSservice;

	@Autowired
	ConsentOutwardAccessTableRep consentOutwardAccessTableRep;
	
	/////Fund Transfer Connection
	////Debit Customer Account Credit Settl Account(Connect 24)
	////Send Packages to IPSX,Credit to IPSX Account
	public MCCreditTransferResponse createFTConnection(String psuDeviceID, String psuIpAddress, String psuID,
			 CIMCreditTransferRequest mcCreditTransferRequest,String p_id,
			String channelID,String resvField1,String resvField2)
			throws DatatypeConfigurationException, JAXBException, KeyManagementException, UnrecoverableKeyException,
			KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {

		MCCreditTransferResponse mcCreditTransferResponse = null;

		///// Generate Sequence Unique ID
		String seqUniqueID = sequence.generateSeqUniqueID();
		///// Generate Bob Msg ID
		String cimMsgID = seqUniqueID;
		///// Generate SystemTraceAuditNumber or CBS Tran Number
		String sysTraceNumber = sequence.generateSystemTraceAuditNumber();
		//// Generate Msg Sequence
		String msgSeq = sequence.generateMsgSequence();
		///// Generate EndToEnd ID
		String endTOEndID = env.getProperty("ipsx.bicfi") + new SimpleDateFormat("yyyyMMdd").format(new Date())
				+ msgSeq;
		/////Net Mir
		String msgNetMir=new SimpleDateFormat("yyMMdd").format(new Date())+env.getProperty("ipsx.user")+"0001"+msgSeq;

		logger.info("Transaction cycle starts");
		logger.info("System Trace Audit Number" + sysTraceNumber);
		logger.info("System Sequence ID" + cimMsgID);
		
		logger.info("Register Initial outgoing Fund Transfer Record");
		
		///Purpose Code for Peer to Peer Connection
		//mcCreditTransferRequest.setPurpose("100");
		
		///// Get Other Bank Agent and Agent Account number
		BankAgentTable othBankAgent = ipsDao.getOtherBankAgent(
				mcCreditTransferRequest.getToAccount().getBankCode());
		
		////Get Remitter Bank Code
		String remitterBankCode=ipsDao.getOtherBankCode(env.getProperty("ipsx.dbtragt"));
		
		logger.info(othBankAgent.getBank_agent(),
				"" + othBankAgent.getBank_agent_account());

			///// Starting Background Service
			taskExecutor.execute(new Runnable() {
				@Override
				public void run() {
				
					
				//////Register Data to Master Table
				ipsDao.RegisterOutgoingMasterRecord(psuDeviceID, psuIpAddress, sysTraceNumber, cimMsgID,
						seqUniqueID, endTOEndID, seqUniqueID, msgNetMir, env.getProperty("ipsx.bicfi"),
						othBankAgent.getBank_agent(), env.getProperty("ipsx.dbtragt"),
						env.getProperty("ipsx.dbtragtacct"), othBankAgent.getBank_agent(),
						othBankAgent.getBank_agent_account(), seqUniqueID, "0100", "CSDC", "100", "100",
						mcCreditTransferRequest.getFrAccount().getAcctName(),
						mcCreditTransferRequest.getFrAccount().getAcctNumber(),
						mcCreditTransferRequest.getToAccount().getBankCode(),
						remitterBankCode,
						mcCreditTransferRequest.getCurrencyCode(),
						mcCreditTransferRequest.getToAccount().getAcctName(),
						mcCreditTransferRequest.getToAccount().getAcctNumber(),
						p_id,mcCreditTransferRequest.getTrAmt(),mcCreditTransferRequest.getTrRmks(), p_id,
						p_id, channelID, resvField1,resvField2);
				
				 
				 
				/*///// Register OutGoing Message to Tran Table
				ipsDao.RegisterOutMsgRecord(psuDeviceID, psuIpAddress, psuID,
							senderParticipantBIC, participantSOL, sysTraceNumber,
							mcCreditTransferRequest, cimMsgID, seqUniqueID, endTOEndID,mcCreditTransferRequest.getPurpose(),msgNetMir,
							env.getProperty("ipsx.bicfi"),othBankAgent.getBank_agent(),env.getProperty("ipsx.dbtragt"),env.getProperty("ipsx.dbtragtacct"),
							othBankAgent.getBank_agent(),othBankAgent.getBank_agent_account(),
							seqUniqueID,"0100","CSDC",mcCreditTransferRequest.getPurpose(),remitterBankCode);
					*/
					
				////Generate RequestUUID
				String requestUUID=sequence.generateRequestUUId();
					
				/**********ESB*********/
				////Store ESB Registration Data
				////Register ESB Data
			    ipsDao.registerCIMcbsIncomingData(requestUUID, env.getProperty("cimCBS.channelID"),
						env.getProperty("cimCBS.servicereqversion"), env.getProperty("cimCBS.servicereqID"), new Date(),
						sysTraceNumber, env.getProperty("cimCBS.outDBChannel"), "", "True", "DR", "N", "", mcCreditTransferRequest.getToAccount().getAcctNumber(), mcCreditTransferRequest.getTrAmt(), mcCreditTransferRequest.getCurrencyCode(),
						seqUniqueID, mcCreditTransferRequest.getFrAccount().getAcctNumber(), mcCreditTransferRequest.getFrAccount().getAcctName(), "NRT/RTP",mcCreditTransferRequest.getTrRmks(), "", "", "");
				
			    /////Call ESB Connection
					ResponseEntity<CimCBSresponse> connect24Response = cimCBSservice.dbtFundRequest(requestUUID);

					logger.debug("CBS Data:"+connect24Response.toString());
					if (connect24Response.getStatusCode() == HttpStatus.OK) {
						
						logger.info(seqUniqueID + ": success"+String.valueOf(connect24Response.getBody().getStatus()));

						logger.info(seqUniqueID + ": success"+connect24Response.getBody().getStatus().getIsSuccess()+":"+connect24Response.getBody().getStatus().getMessage());

						if (connect24Response.getBody().getStatus().getIsSuccess()) {
							
							////Update ESB Data
							ipsDao.updateCIMcbsData(requestUUID, TranMonitorStatus.SUCCESS.toString(),
									connect24Response.getBody().getStatus().getStatusCode(),
									connect24Response.getBody().getStatus().getMessage(),
									connect24Response.getBody().getData().getTransactionNoFromCBS());
							
							/*////Update CBS Status to Tran Table
							ipsDao.updateCBSStatus(seqUniqueID,
									TranMonitorStatus.CBS_DEBIT_OK.toString(),
									TranMonitorStatus.IN_PROGRESS.toString());*/
							
						    ////Update CBS Status to Tran Table
							ipsDao.updateOutwardCBSStatus(seqUniqueID,
									TranMonitorStatus.CBS_DEBIT_OK.toString(),
									TranMonitorStatus.IN_PROGRESS.toString());
							
							/////Calling IPSX
							ipsxClient.sendftRequst( mcCreditTransferRequest,
									sysTraceNumber, cimMsgID, seqUniqueID, othBankAgent, msgSeq, endTOEndID, msgNetMir);


						} else {
							
							//// Update ESB Data
							ipsDao.updateCIMcbsData(requestUUID, TranMonitorStatus.FAILURE.toString(),
									connect24Response.getBody().getStatus().getStatusCode(),
									connect24Response.getBody().getStatus().getMessage(),
									connect24Response.getBody().getData().getTransactionNoFromCBS());
	
							/*//// Update CBS Status to Tran table
							ipsDao.updateCBSStatusError(seqUniqueID, TranMonitorStatus.CBS_DEBIT_ERROR.toString(),
									connect24Response.getBody().getStatus().getMessage(),
									TranMonitorStatus.FAILURE.toString());*/
	
					      	//// Update ESB Data Error
							ipsDao.updateOutwardCBSStatusError(seqUniqueID, TranMonitorStatus.CBS_DEBIT_ERROR.toString(),
									connect24Response.getBody().getStatus().getMessage(), TranMonitorStatus.FAILURE.toString());
		
							//// Send Failure Message to CIM
							/*ipsDao.updateIPSXStatusResponseRJCTBulkRTP(seqUniqueID, connect24Response.getBody().getStatus().getMessage(), seqUniqueID,
									TranMonitorStatus.FAILURE.toString(), "",
									"",  connect24Response.getBody().getStatus().getStatusCode());*/
							
						}
					} else {
						//// Update ESB Data to CIM Table
						ipsDao.updateCIMcbsData(requestUUID, TranMonitorStatus.FAILURE.toString(), String.valueOf(connect24Response.getStatusCodeValue()),
								"Internal Server Error","");
	 
						/*//// Update ESB Data Error to Transaction Table
						ipsDao.updateCBSStatusError(seqUniqueID, TranMonitorStatus.CBS_DEBIT_ERROR.toString(),
								String.valueOf(connect24Response.getStatusCodeValue())+":Internal Server Error", TranMonitorStatus.FAILURE.toString());
	*/
					    //// Update ESB Data to Outward Table
						ipsDao.updateOutwardCBSStatusError(seqUniqueID, TranMonitorStatus.CBS_DEBIT_ERROR.toString(),
								String.valueOf(connect24Response.getStatusCodeValue())+":Internal Server Error", TranMonitorStatus.FAILURE.toString());
	
					    //// Send Failure Message to CIM
						/*ipsDao.updateIPSXStatusResponseRJCTBulkRTP(seqUniqueID, connect24Response.getBody().getStatus().getMessage(), seqUniqueID,
								TranMonitorStatus.FAILURE.toString(), "",
								"",  connect24Response.getBody().getStatus().getStatusCode());
						*/
						
					}

				}
			});
			
		    ///// Return Sequence ID to CIM
			mcCreditTransferResponse = new MCCreditTransferResponse(seqUniqueID,
					new SimpleDateFormat("YYYY-MM-dd HH:mm:ss ").format(new Date()));
			return mcCreditTransferResponse;
		


		
	}

	

	public MCCreditTransferResponse createMAnualTransaction(String psuDeviceID, String psuIpAddress,
			List<ManualFndTransferRequest> manualFundTransferRequest,String userID,
			String p_id,String channelID,String resvField1,String resvField2 ){

		///// Generate Master Ref Id
		String master_ref_id = "CFSL" + sequence.generateSystemTraceAuditNumber();

		MCCreditTransferResponse mcCreditTransferResponse = null;

		///// Starting Background Service
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {

				for (int i = 0; i < manualFundTransferRequest.size(); i++) {

					////// Generate System Trace Audit Number
					String sysTraceNumber = sequence.generateSystemTraceAuditNumber();
					////// Generate Sequence Unique ID
					String seqUniqueID = sequence.generateSeqUniqueID();
					///// Generate Bob Message ID
					String cimMsgID = seqUniqueID;
					///// Generate Msg Sequence
					String msgSeq = sequence.generateMsgSequence();
					///// Generate End To End ID
					String endTOEndID = env.getProperty("ipsx.bicfi")
							+ new SimpleDateFormat("yyyyMMdd").format(new Date()) + msgSeq;
					
				    /////Net Mir
					String msgNetMir=new SimpleDateFormat("yyMMdd").format(new Date())+env.getProperty("ipsx.user")+"0001"+msgSeq;

					///// Register Manual Record
					logger.info("Register Initial outgoing Fund Transfer Record");
					
					////Get Remitter Bank Code
					String remitterBankCode=ipsDao.getOtherBankCode(env.getProperty("ipsx.dbtragt"));
					
					
				    ////// Get Bank Agent Account and Agent Account
					BankAgentTable othBankAgent = ipsDao
												.getOtherBankAgent(manualFundTransferRequest.get(i).getBeneficiaryBankCode());
					
					
					//////Register Data to Master Table
					ipsDao.RegisterManualMasterRecord(psuDeviceID, psuIpAddress, sysTraceNumber, cimMsgID,
							seqUniqueID, endTOEndID, master_ref_id, msgNetMir, env.getProperty("ipsx.bicfi"),
							othBankAgent.getBank_agent(), env.getProperty("ipsx.dbtragt"),
							env.getProperty("ipsx.dbtragtacct"), othBankAgent.getBank_agent(),
							othBankAgent.getBank_agent_account(), seqUniqueID, "0100", "CSDC", "100", "100",
							manualFundTransferRequest.get(i).getRemitterName(),
							manualFundTransferRequest.get(i).getRemitterAcctNumber(),
							manualFundTransferRequest.get(i).getBeneficiaryBankCode(),
							remitterBankCode,
							manualFundTransferRequest.get(i).getCurrencyCode(),
							manualFundTransferRequest.get(i).getBeneficiaryName(),
							manualFundTransferRequest.get(i).getBeneficiaryAcctNumber(),
							p_id,manualFundTransferRequest.get(i).getTrAmt(),manualFundTransferRequest.get(i).getTrRmks(), p_id,
							manualFundTransferRequest.get(i).getReqUniqueID(), channelID, resvField1,resvField2);
					
					
					/*ipsDao.RegisterManualTranRecord(psuDeviceID, psuIpAddress, sysTraceNumber,
							manualFundTransferRequest.get(i), bobMsgID, seqUniqueID, endTOEndID, master_ref_id,msgNetMir,
							env.getProperty("ipsx.bicfi"),othBankAgent.getBank_agent(),env.getProperty("ipsx.dbtragt"),env.getProperty("ipsx.dbtragtacct"),
							othBankAgent.getBank_agent(),othBankAgent.getBank_agent_account(),seqUniqueID,"0100","CSDC","100","100",userID);
*/
					
					
					
					
				////Generate RequestUUID
				String requestUUID=sequence.generateRequestUUId();
					
				/**********ESB*********/
				////Store ESB Registration Data
				////Register ESB Data
			    ipsDao.registerCIMcbsIncomingData(requestUUID, env.getProperty("cimCBS.channelID"),
						env.getProperty("cimCBS.servicereqversion"), env.getProperty("cimCBS.servicereqID"), new Date(),
						sysTraceNumber, env.getProperty("cimCBS.outDBChannel"), "", "True", "DR", "N", "", manualFundTransferRequest.get(i).getBeneficiaryAcctNumber(), manualFundTransferRequest.get(i).getTrAmt(), manualFundTransferRequest.get(i).getCurrencyCode(),
						seqUniqueID, manualFundTransferRequest.get(i).getRemitterAcctNumber(), manualFundTransferRequest.get(i).getRemitterName(), "NRT/RTP",manualFundTransferRequest.get(i).getTrRmks(), "", "", "");
				
			    /////Call ESB Connection
					ResponseEntity<CimCBSresponse> connect24Response = cimCBSservice.dbtFundRequest(requestUUID);

					logger.debug("CBS Data:"+connect24Response.toString());
					if (connect24Response.getStatusCode() == HttpStatus.OK) {
						logger.info(seqUniqueID + ": success"+connect24Response.getBody().getStatus().getIsSuccess()+":"+connect24Response.getBody().getStatus().getMessage());

						if (connect24Response.getBody().getStatus().getIsSuccess()) {
							
							////Update ESB Data
							ipsDao.updateCIMcbsData(requestUUID, TranMonitorStatus.SUCCESS.toString(),
									connect24Response.getBody().getStatus().getStatusCode(),
									connect24Response.getBody().getStatus().getMessage(),
									connect24Response.getBody().getData().getTransactionNoFromCBS());
							
							
						    ////Update CBS Status to Tran Table
							ipsDao.updateOutwardCBSStatus(seqUniqueID,
									TranMonitorStatus.CBS_DEBIT_OK.toString(),
									TranMonitorStatus.IN_PROGRESS.toString());
							
							/////Calling IPSX
							/*ipsxClient.sendftRequst( mcCreditTransferRequest,
									sysTraceNumber, cimMsgID, seqUniqueID, othBankAgent, msgSeq, endTOEndID, msgNetMir);*/
							
							logger.info("Calling IPSX");
							ipsxClient.sendManualftRequst(
									manualFundTransferRequest.get(i).getRemitterName(),
									manualFundTransferRequest.get(i).getRemitterAcctNumber(),
									manualFundTransferRequest.get(i).getBeneficiaryName(),
									manualFundTransferRequest.get(i).getBeneficiaryAcctNumber(),
									manualFundTransferRequest.get(i).getTrAmt(),
									manualFundTransferRequest.get(i).getCurrencyCode(), sysTraceNumber, cimMsgID,
									seqUniqueID, othBankAgent, msgSeq, endTOEndID,msgNetMir);



						} else {
							
							//// Update ESB Data
							ipsDao.updateCIMcbsData(requestUUID, TranMonitorStatus.FAILURE.toString(),
									connect24Response.getBody().getStatus().getStatusCode(),
									connect24Response.getBody().getStatus().getMessage(),
									connect24Response.getBody().getData().getTransactionNoFromCBS());
	
							
					      	//// Update ESB Data Error
							ipsDao.updateOutwardCBSStatusError(seqUniqueID, TranMonitorStatus.CBS_DEBIT_ERROR.toString(),
									connect24Response.getBody().getStatus().getMessage(), TranMonitorStatus.FAILURE.toString());
		
							
						}
					} else {
						//// Update ESB Data to CIM Table
						ipsDao.updateCIMcbsData(requestUUID, TranMonitorStatus.FAILURE.toString(), String.valueOf(connect24Response.getStatusCodeValue()),
								"Internal Server Error","");
	 
						
					    //// Update ESB Data to Outward Table
						ipsDao.updateOutwardCBSStatusError(seqUniqueID, TranMonitorStatus.CBS_DEBIT_ERROR.toString(),
								String.valueOf(connect24Response.getStatusCodeValue())+":Internal Server Error", TranMonitorStatus.FAILURE.toString());
	
						
					}
					

				}

			}
		});

		///// Return Master Ref ID
		mcCreditTransferResponse = new MCCreditTransferResponse(master_ref_id,
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()));
		return mcCreditTransferResponse;
		

	}

	

	////Bulk Credit Transaction
	public MCCreditTransferResponse createBulkCreditConnection(String psuDeviceID, String psuIpAddress, String psuID,
			String senderParticipantBIC, String participantSOL, BukCreditTransferRequest mcCreditTransferRequest,String userID) {

		/*MCCreditTransferResponse mcCreditTransferResponse = null;

		///// Generate Sequence Unique ID
		String seqUniqueID = sequence.generateSeqUniqueID();
		///// Generate Bob Msg ID
		String bobMsgID = seqUniqueID;
		///// Generate System Trace Audit Number
		String sysTraceNumber = sequence.generateSystemTraceAuditNumber();
		

		///// Starting Background service
		logger.info("Starting background service");
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				ResponseEntity<C24FTResponse> connect24Response = null;

				List<String> endToEndIdList=new ArrayList<>();
				List<String> msgIdList=new ArrayList<>();
				List<String> msgNetMirList=new ArrayList<>();
				
				///// Register Bulk Credit Record
				logger.info("Register Initial bulk credit Record"+mcCreditTransferRequest.getToAccountList().size());
				for (int i = 0; i < mcCreditTransferRequest.getToAccountList().size(); i++) {
					int srlNo = i + 1;
					
					//// Generate Msg Sequence
					String msgSeq = sequence.generateMsgSequence();
					///// Generate EndToEnd ID
					String endTOEndID = env.getProperty("ipsx.bicfi") + new SimpleDateFormat("yyyyMMdd").format(new Date())
							+ msgSeq;
					/////Net Mir
					String msgNetMir=new SimpleDateFormat("yyMMdd").format(new Date())+env.getProperty("ipsx.user")+"0001"+msgSeq;
					
				
					///// Get Other Bank Agent and Agent Account
					BankAgentTable othBankAgent = ipsDao.getOtherBankAgent(
							mcCreditTransferRequest.getToAccountList().get(i).getBankCode());
					
					ipsDao.RegisterBulkCreditRecord(psuDeviceID, psuIpAddress, psuID, senderParticipantBIC,
							participantSOL, sysTraceNumber,mcCreditTransferRequest.getFrAccount().get(i).getAcctNumber(),
							mcCreditTransferRequest.getFrAccount().get(i).getAcctName(),mcCreditTransferRequest.getToAccountList().get(i).getAcctNumber(),
							mcCreditTransferRequest.getToAccountList().get(i).getAcctName(),mcCreditTransferRequest.getToAccountList().get(i).getBankCode(),
							mcCreditTransferRequest.getToAccountList().get(i).getTrAmt(),mcCreditTransferRequest.getCurrencyCode(),
							 bobMsgID+"/"+srlNo, seqUniqueID+"/"+srlNo, "100",seqUniqueID,endTOEndID,msgNetMir, 
							 env.getProperty("ipsx.bicfi"),othBankAgent.getBank_agent(),env.getProperty("ipsx.dbtragt"),env.getProperty("ipsx.dbtragtacct"),
							othBankAgent.getBank_agent(),othBankAgent.getBank_agent_account(),
							seqUniqueID,"0100","CSDC","100",userID);
					
					endToEndIdList.add(endTOEndID);
					msgIdList.add(msgSeq);
					msgNetMirList.add(msgNetMir);
					
				}
				
				logger.info("Register Initial bulk" +mcCreditTransferRequest.getToAccountList().size());

				ResponseEntity<AccountContactResponse> connect24ResponseAccContactExist = connect24Service
						.getAccountContact("", "", "", mcCreditTransferRequest.getFrAccount().get(0).getAcctNumber());

				if (connect24ResponseAccContactExist.getStatusCode() == HttpStatus.OK) {
					if (connect24ResponseAccContactExist.getBody().getStatus().equals("0")) {

						if (connect24ResponseAccContactExist.getBody().getSchmType().equals("SBA")
								|| connect24ResponseAccContactExist.getBody().getSchmType().equals("CAA")
								|| connect24ResponseAccContactExist.getBody().getSchmType().equals("ODA")) {

							if (connect24ResponseAccContactExist.getBody().getAccountStatus().equals("A")) {

								if (connect24ResponseAccContactExist.getBody().getFrezCode().equals("T")
										|| connect24ResponseAccContactExist.getBody().getFrezCode().equals("D")
										|| connect24ResponseAccContactExist.getBody().getFrezCode().equals("C")) {

									ipsDao.updateCBSStatusBulkCreditError(seqUniqueID,
											TranMonitorStatus.VALIDATION_ERROR.toString(),
											"Transaction forbidden on this type of account/" + "Freeze Account",
											TranMonitorStatus.FAILURE.toString());

								} else {
									if (connect24ResponseAccContactExist.getBody().getCurrencyCode().equals(env.getProperty("cim.crncycode"))) {
									///// Calling Connect 24 for Bulk Credit
										logger.info("Send message to Connect24");
										connect24Response = connect24Service.BulkCreditRequest(senderParticipantBIC,
												participantSOL, mcCreditTransferRequest, sysTraceNumber, seqUniqueID,mcCreditTransferRequest.getTrRmks());

										///// Return Status COde 200 from Connect 24
										if (connect24Response.getStatusCode() == HttpStatus.OK) {

											///// Update Bulk Credit CBS Status
											logger.info("Connect24 Processed Successfully");
											logger.info("Update CBS Debit OK Status to Table");
											ipsDao.updateCBSStatusBulkCredit(seqUniqueID,
													TranMonitorStatus.CBS_DEBIT_OK.toString(),
													TranMonitorStatus.IN_PROGRESS.toString());

											for (int i = 0; i < mcCreditTransferRequest.getToAccountList().size(); i++) {

												///// Get SRL Number
												int srlNo = i + 1;

												///// Get Other Bank Agent and Agent Account
												BankAgentTable othBankAgent = ipsDao.getOtherBankAgent(
														mcCreditTransferRequest.getToAccountList().get(i).getBankCode());
												logger.info("Size" + mcCreditTransferRequest.getToAccountList().size());
												logger.info(othBankAgent.getBank_agent(),
														"" + othBankAgent.getBank_agent_account());

												///// Calling IPSX
												logger.info("Calling IPSX");
												MCCreditTransferResponse response = ipsxClient.sendBulkCreditRequest(
														senderParticipantBIC, participantSOL,
														mcCreditTransferRequest.getFrAccount().get(i).getAcctName(),
														mcCreditTransferRequest.getFrAccount().get(i).getAcctNumber(),
														mcCreditTransferRequest.getToAccountList().get(i).getAcctName(),
														mcCreditTransferRequest.getToAccountList().get(i).getAcctNumber(),
														mcCreditTransferRequest.getToAccountList().get(i).getTrAmt(),
														mcCreditTransferRequest.getCurrencyCode(), sysTraceNumber,
														bobMsgID + "/" + srlNo, seqUniqueID + "/" + srlNo, othBankAgent,
														msgIdList.get(i),endToEndIdList.get(i),msgNetMirList.get(i));

												logger.info("okk");
											}
										} else if (connect24Response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {

											ipsDao.updateCBSStatusBulkCreditError(seqUniqueID,
													TranMonitorStatus.CBS_DEBIT_ERROR.toString(),
													TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(),
													TranMonitorStatus.FAILURE.toString());
										} else {
											ipsDao.updateCBSStatusBulkCreditError(seqUniqueID,
													TranMonitorStatus.CBS_DEBIT_ERROR.toString(),
													connect24Response.getBody().getError_desc().get(0).toString(),
													TranMonitorStatus.FAILURE.toString());

										}
									}else {
										ipsDao.updateCBSStatusBulkCreditError(seqUniqueID,
												TranMonitorStatus.VALIDATION_ERROR.toString(),
												"Invalid Currency Code",
												TranMonitorStatus.FAILURE.toString());
									}
									

								}

							} else {
								if (connect24ResponseAccContactExist.getBody().getAccountStatus().equals("C")) {

									ipsDao.updateCBSStatusBulkCreditError(seqUniqueID,
											TranMonitorStatus.VALIDATION_ERROR.toString(), "Closed Account Number",
											TranMonitorStatus.FAILURE.toString());

								} else {

									ipsDao.updateCBSStatusBulkCreditError(seqUniqueID,
											TranMonitorStatus.VALIDATION_ERROR.toString(), "Blocked Account Number",
											TranMonitorStatus.FAILURE.toString());

								}
							}

						} else {
							ipsDao.updateCBSStatusBulkCreditError(seqUniqueID,
									TranMonitorStatus.VALIDATION_ERROR.toString(),
									"Transaction forbidden on this type of account",
									TranMonitorStatus.FAILURE.toString());
						}

					} else {
						if (connect24ResponseAccContactExist.getBody().getStatus().equals("1")) {

							ipsDao.updateCBSStatusBulkCreditError(seqUniqueID,
									TranMonitorStatus.VALIDATION_ERROR.toString(), "Incorrect Account Debtor Number",
									TranMonitorStatus.FAILURE.toString());

						} else {
							ipsDao.updateCBSStatusBulkCreditError(seqUniqueID,
									TranMonitorStatus.VALIDATION_ERROR.toString(),
									"Transaction forbidden on this type of account/" + "Invalid Account Number",
									TranMonitorStatus.FAILURE.toString());
						}
					}
				} else {
					ipsDao.updateCBSStatusBulkCreditError(seqUniqueID, TranMonitorStatus.CBS_DEBIT_ERROR.toString(),
							"Technical Problem", TranMonitorStatus.FAILURE.toString());
				}
			}
		});

		mcCreditTransferResponse = new MCCreditTransferResponse(seqUniqueID,
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()));

		return mcCreditTransferResponse;*/
		
		MCCreditTransferResponse mcCreditTransferResponse = null;

		///// Generate Sequence Unique ID
		String seqUniqueID = sequence.generateSeqUniqueID();
		///// Generate Bob Msg ID
		String bobMsgID = seqUniqueID;
		///// Generate System Trace Audit Number
		String sysTraceNumber = sequence.generateSystemTraceAuditNumber();
		

		///// Starting Background service
		logger.info("Starting background service");
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				ResponseEntity<C24FTResponse> connect24Response = null;

				List<String> endToEndIdList=new ArrayList<>();
				List<String> msgIdList=new ArrayList<>();
				List<String> msgNetMirList=new ArrayList<>();
				
				///// Register Bulk Credit Record
				logger.info("Register Initial bulk credit Record"+mcCreditTransferRequest.getToAccountList().size());
				for (int i = 0; i < mcCreditTransferRequest.getToAccountList().size(); i++) {
					int srlNo = i + 1;
					
					//// Generate Msg Sequence
					String msgSeq = sequence.generateMsgSequence();
					///// Generate EndToEnd ID
					String endTOEndID = env.getProperty("ipsx.bicfi") + new SimpleDateFormat("YYYYMMdd").format(new Date())
							+ msgSeq;
					/////Net Mir
					String msgNetMir=new SimpleDateFormat("YYMMdd").format(new Date())+env.getProperty("ipsx.user")+"0001"+msgSeq;
					
				
					///// Get Other Bank Agent and Agent Account
					BankAgentTable othBankAgent = ipsDao.getOtherBankAgent(
							mcCreditTransferRequest.getToAccountList().get(i).getBankCode());
					
					ipsDao.RegisterBulkCreditRecord(psuDeviceID, psuIpAddress, psuID, senderParticipantBIC,
							participantSOL, sysTraceNumber,mcCreditTransferRequest.getFrAccount().get(i).getAcctNumber(),
							mcCreditTransferRequest.getFrAccount().get(i).getAcctName(),mcCreditTransferRequest.getToAccountList().get(i).getAcctNumber(),
							mcCreditTransferRequest.getToAccountList().get(i).getAcctName(),mcCreditTransferRequest.getToAccountList().get(i).getBankCode(),
							mcCreditTransferRequest.getToAccountList().get(i).getTrAmt(),mcCreditTransferRequest.getCurrencyCode(),
							 bobMsgID+"/"+srlNo, seqUniqueID+"/"+srlNo, "100",seqUniqueID,endTOEndID,msgNetMir, 
							 env.getProperty("ipsx.bicfi"),othBankAgent.getBank_agent(),env.getProperty("ipsx.dbtragt"),env.getProperty("ipsx.dbtragtacct"),
							othBankAgent.getBank_agent(),othBankAgent.getBank_agent_account(),
							seqUniqueID,"0100","CSDC","100",userID);
					
					endToEndIdList.add(endTOEndID);
					msgIdList.add(msgSeq);
					msgNetMirList.add(msgNetMir);
					
				}
				
				logger.info("Register Initial bulk" +mcCreditTransferRequest.getToAccountList().size());
			}
		});

		mcCreditTransferResponse = new MCCreditTransferResponse(seqUniqueID,
				new SimpleDateFormat("YYYY-MM-dd HH:mm:ss ").format(new Date()));

		return mcCreditTransferResponse;
	}

	/////Bulk Debit Transaction
	
	public MCCreditTransferResponse createBulkDebitConnection(String psuDeviceID, String psuIpAddress,
			BulkDebitFndTransferRequest bulkDebitFndTransferRequest,String userID) {

		/*MCCreditTransferResponse mcCreditTransferResponse = null;

		///// Generate Sequence Unique ID
		String seqUniqueID = sequence.generateSeqUniqueID();
		///// Generate Bob Msg ID
		String bobMsgID = seqUniqueID;
		///// Generate System Trace Audit Number
		String sysTraceNumber = sequence.generateSystemTraceAuditNumber();
		///// Generate Msg Sequence
		String msgSeq = sequence.generateMsgSequence();
		///// Generate End To End ID
		String endTOEndID = env.getProperty("ipsx.bicfi") + new SimpleDateFormat("yyyyMMdd").format(new Date())
				+ msgSeq;
	    /////Net Mir
		String msgNetMir=new SimpleDateFormat("yyMMdd").format(new Date())+env.getProperty("ipsx.user")+"0001"+msgSeq;
		
		///// Starting Background service
		logger.info("Starting background service");
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {

				logger.info("Send message");
				logger.info("Send message to Connect24"+bulkDebitFndTransferRequest.getRemitterAccount().size());

				ResponseEntity<C24FTResponse> connect24Response = null;

				double totAmt = 0;
				NumberFormat formatter = new DecimalFormat("#0.00");
				

			    ///// Get Bank Agent Name and Agent Account Number
				BankAgentTable othBankAgent = ipsDao
						.getOtherBankAgent(bulkDebitFndTransferRequest.getBenAccount().getBankCode());
				

				for (int i = 0; i < bulkDebitFndTransferRequest.getRemitterAccount().size(); i++) {

					//// Generate SRL NO
					int srlNo = i + 1;
					String sysTraceNumber1 = sequence.generateSystemTraceAuditNumber();

					
					///// Register Bulk Debit Record
					ipsDao.RegisterBulkDebitRecord(psuDeviceID, psuIpAddress, sysTraceNumber1,
							bulkDebitFndTransferRequest.getRemitterAccount().get(i).getRemitterName(),
							bulkDebitFndTransferRequest.getRemitterAccount().get(i).getRemitterAcctNumber(),
							bulkDebitFndTransferRequest.getRemitterAccount().get(i).getTrAmt(),
							bulkDebitFndTransferRequest.getRemitterAccount().get(i).getCurrencyCode(),
							bulkDebitFndTransferRequest.getBenAccount().getAcctName(),
							bulkDebitFndTransferRequest.getBenAccount().getAcctNumber(),
							bulkDebitFndTransferRequest.getBenAccount().getBankCode(), seqUniqueID + "/" + srlNo,
							bobMsgID + "/" + srlNo, endTOEndID, seqUniqueID,"100",msgNetMir,
							env.getProperty("ipsx.bicfi"),othBankAgent.getBank_agent(),env.getProperty("ipsx.dbtragt"),env.getProperty("ipsx.dbtragtacct"),
							othBankAgent.getBank_agent(),othBankAgent.getBank_agent_account(),
							seqUniqueID,"0100","CSDC","100",userID);

					ResponseEntity<AccountContactResponse> connect24ResponseAccContactExist = connect24Service
							.getAccountContact("", "", "",
									bulkDebitFndTransferRequest.getRemitterAccount().get(i).getRemitterAcctNumber());

					if (connect24ResponseAccContactExist.getStatusCode() == HttpStatus.OK) {
						if (connect24ResponseAccContactExist.getBody().getStatus().equals("0")) {

							if (connect24ResponseAccContactExist.getBody().getSchmType().equals("SBA")
									|| connect24ResponseAccContactExist.getBody().getSchmType().equals("CAA")
									|| connect24ResponseAccContactExist.getBody().getSchmType().equals("ODA")) {

								if (connect24ResponseAccContactExist.getBody().getAccountStatus().equals("A")) {

									if (connect24ResponseAccContactExist.getBody().getFrezCode().equals("T")
											|| connect24ResponseAccContactExist.getBody().getFrezCode().equals("D")
											|| connect24ResponseAccContactExist.getBody().getFrezCode().equals("C")) {

										ipsDao.updateCBSStatusError(seqUniqueID + "/" + srlNo,
												TranMonitorStatus.VALIDATION_ERROR.toString(),
												"Transaction forbidden on this type of account/" + "Freeze Account",
												TranMonitorStatus.FAILURE.toString());
									} else {
										
										if (connect24ResponseAccContactExist.getBody().getCurrencyCode().equals(env.getProperty("cim.crncycode"))) {
										///// Calling Connect 24 for Bulk Debit
											logger.info("Send message to Connect24");
											logger.info("Send message to Connect24SDGFF");
											logger.info("Send message to Connect24"+bulkDebitFndTransferRequest.getRemitterAccount().size());
											
											connect24Response = connect24Service.BulkDebitFundRequest(
													bulkDebitFndTransferRequest.getRemitterAccount().get(i),
													sysTraceNumber1, seqUniqueID + "/" + srlNo,bulkDebitFndTransferRequest.getRemitterAccount().get(i).getTrRmks());

											///// Return Status Code 200 from Connect 24
											if (connect24Response.getStatusCode() == HttpStatus.OK) {
												logger.info("Connect24 Processed Successfully");

												logger.info("Update CBS Debit OK Status to Table");
												ipsDao.updateCBSStatus(seqUniqueID + "/" + srlNo,
														TranMonitorStatus.CBS_DEBIT_OK.toString(),
														TranMonitorStatus.IN_PROGRESS.toString());

												// totAmt=totAmt+;
												*//*************************//*

												totAmt = totAmt + Double.parseDouble(
														bulkDebitFndTransferRequest.getRemitterAccount().get(i).getTrAmt());

											} else if (connect24Response
													.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {

												ipsDao.updateCBSStatusError(seqUniqueID + "/" + srlNo,
														TranMonitorStatus.CBS_DEBIT_ERROR.toString(),
														TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(),
														TranMonitorStatus.FAILURE.toString());

											} else {
												ipsDao.updateCBSStatusError(seqUniqueID + "/" + srlNo,
														TranMonitorStatus.CBS_DEBIT_ERROR.toString(),
														connect24Response.getBody().getError_desc().get(0).toString(),
														TranMonitorStatus.FAILURE.toString());

											}
										}else {
											ipsDao.updateCBSStatusError(seqUniqueID + "/" + srlNo,
													TranMonitorStatus.VALIDATION_ERROR.toString(),
													"Invalid Currency Code",
													TranMonitorStatus.FAILURE.toString());
										}
										
									}

								} else {
									if (connect24ResponseAccContactExist.getBody().getAccountStatus().equals("C")) {

										ipsDao.updateCBSStatusError(seqUniqueID + "/" + srlNo,
												TranMonitorStatus.VALIDATION_ERROR.toString(), "Closed Account Number",
												TranMonitorStatus.FAILURE.toString());

									} else {
										ipsDao.updateCBSStatusError(seqUniqueID + "/" + srlNo,
												TranMonitorStatus.VALIDATION_ERROR.toString(), "Blocked Account Number",
												TranMonitorStatus.FAILURE.toString());

									}
								}

							} else {
								ipsDao.updateCBSStatusError(seqUniqueID + "/" + srlNo,
										TranMonitorStatus.VALIDATION_ERROR.toString(),
										"Transaction forbidden on this type of account",
										TranMonitorStatus.FAILURE.toString());
							}

						} else {
							if (connect24ResponseAccContactExist.getBody().getStatus().equals("1")) {

								ipsDao.updateCBSStatusError(seqUniqueID + "/" + srlNo,
										TranMonitorStatus.VALIDATION_ERROR.toString(),
										"Incorrect Account Debtor Number", TranMonitorStatus.FAILURE.toString());

							} else {
								ipsDao.updateCBSStatusError(seqUniqueID + "/" + srlNo,
										TranMonitorStatus.VALIDATION_ERROR.toString(),
										"Transaction forbidden on this type of account/" + "Invalid Account Number",
										TranMonitorStatus.FAILURE.toString());

							}
						}
					} else {
						ipsDao.updateCBSStatusError(seqUniqueID + "/" + srlNo,
								TranMonitorStatus.VALIDATION_ERROR.toString(), "Technical Problem",
								TranMonitorStatus.FAILURE.toString());
					}

				}

				///// Calling IPSX
				logger.info("Send message to Connect24");
				if (totAmt > 0) {

					MCCreditTransferResponse response = ipsxClient.sendBulkDebitFndTransefer("BOB", "",
							bulkDebitFndTransferRequest.getBenAccount().getAcctName(),
							bulkDebitFndTransferRequest.getBenAccount().getAcctNumber(), formatter.format(totAmt),
							env.getProperty("cim.crncycode"), sysTraceNumber, bobMsgID + "/1", seqUniqueID + "/1", othBankAgent, msgSeq,
							endTOEndID,msgNetMir);
				}

			}
		});

		///// Return Master Ref ID:
		mcCreditTransferResponse = new MCCreditTransferResponse(seqUniqueID,
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()));

		return mcCreditTransferResponse;*/
		
		MCCreditTransferResponse mcCreditTransferResponse = null;

		///// Generate Sequence Unique ID
		String seqUniqueID = sequence.generateSeqUniqueID();
		///// Generate Bob Msg ID
		String bobMsgID = seqUniqueID;
		///// Generate System Trace Audit Number
		String sysTraceNumber = sequence.generateSystemTraceAuditNumber();
		///// Generate Msg Sequence
		String msgSeq = sequence.generateMsgSequence();
		///// Generate End To End ID
		String endTOEndID = env.getProperty("ipsx.bicfi") + new SimpleDateFormat("YYYYMMdd").format(new Date())
				+ msgSeq;
	    /////Net Mir
		String msgNetMir=new SimpleDateFormat("YYMMdd").format(new Date())+env.getProperty("ipsx.user")+"0001"+msgSeq;
		
		///// Starting Background service
		logger.info("Starting background service");
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {

				logger.info("Send message");
				logger.info("Send message to Connect24"+bulkDebitFndTransferRequest.getRemitterAccount().size());

				ResponseEntity<C24FTResponse> connect24Response = null;

				double totAmt = 0;
				NumberFormat formatter = new DecimalFormat("#0.00");
				

			    ///// Get Bank Agent Name and Agent Account Number
				BankAgentTable othBankAgent = ipsDao
						.getOtherBankAgent(bulkDebitFndTransferRequest.getBenAccount().getBankCode());
				

				for (int i = 0; i < bulkDebitFndTransferRequest.getRemitterAccount().size(); i++) {

					//// Generate SRL NO
					int srlNo = i + 1;
					String sysTraceNumber1 = sequence.generateSystemTraceAuditNumber();

					
					///// Register Bulk Debit Record
					ipsDao.RegisterBulkDebitRecord(psuDeviceID, psuIpAddress, sysTraceNumber1,
							bulkDebitFndTransferRequest.getRemitterAccount().get(i).getRemitterName(),
							bulkDebitFndTransferRequest.getRemitterAccount().get(i).getRemitterAcctNumber(),
							bulkDebitFndTransferRequest.getRemitterAccount().get(i).getTrAmt(),
							bulkDebitFndTransferRequest.getRemitterAccount().get(i).getCurrencyCode(),
							bulkDebitFndTransferRequest.getBenAccount().getAcctName(),
							bulkDebitFndTransferRequest.getBenAccount().getAcctNumber(),
							bulkDebitFndTransferRequest.getBenAccount().getBankCode(), seqUniqueID + "/" + srlNo,
							bobMsgID + "/" + srlNo, endTOEndID, seqUniqueID,"100",msgNetMir,
							env.getProperty("ipsx.bicfi"),othBankAgent.getBank_agent(),env.getProperty("ipsx.dbtragt"),env.getProperty("ipsx.dbtragtacct"),
							othBankAgent.getBank_agent(),othBankAgent.getBank_agent_account(),
							seqUniqueID,"0100","CSDC","100",userID);
				}
			}
			});

			///// Return Master Ref ID:
			mcCreditTransferResponse = new MCCreditTransferResponse(seqUniqueID,
					new SimpleDateFormat("YYYY-MM-dd HH:mm:ss ").format(new Date()));

			return mcCreditTransferResponse;
	}

	public String setSL() {
		logger.info("Setting Settlement Limit");
		String response = ipsxClient.setSL();

		return response;
	}

	
	////Generate Settlement Report
	public SettlementLimitResponse generateSLReport() {
		logger.info("Setting Settlement Limit Report");
		String bobMsgID = sequence.generateSeqUniqueID();

		logger.info("Register Initial Incoming Fund Transfer Record to table");
		ipsDao.SettlementRecordEntry(bobMsgID);

		SettlementLimitResponse response = ipsxClient.generateSLReport(bobMsgID);

		return response;
	}

	////MYT Create Public Key
	////Check Account Number,Mobile Number,NIC registered.If Registered BOB allow registration
	public UserRegistrationResponse createUserPublicKey(String senderParticipantBIC, String senderParticipantMemberID,
			String receiverParticipantBIC, String receiverPartcipantMemberID, String psuDeviceID, String ipAddress,
			String psuID, UserRegistrationRequest userRequest) throws KeyManagementException, UnrecoverableKeyException,
			KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException {

		UserRegistrationResponse response = null;

		try {

			//// generate OTP
			String otp = sequence.generateOTP();
			//// generate Record ID
			String recordID = sequence.generateRecordId();
			//// generate Auth ID
			String authID = sequence.generateRecordId();

			logger.info("Connect24 Processed Successfully");
			logger.info("Register User Publickey data to table");

			//// update Table

			String status = ipsDao.createPublicKey(senderParticipantBIC, senderParticipantMemberID,
					receiverParticipantBIC, receiverPartcipantMemberID, psuDeviceID, ipAddress, psuID, userRequest,
					recordID, authID, otp);

			//// Send Response
			if (status.equals("0")) {
				ResponseEntity<AccountContactResponse> connect24ResponseAccContactExist = connect24Service
						.getAccountContact(psuDeviceID, ipAddress, psuID, userRequest.getAccount().getIdentification());

				if (connect24ResponseAccContactExist.getStatusCode() == HttpStatus.OK) {

					if (connect24ResponseAccContactExist.getBody().getStatus().equals("0")) {

						if (connect24ResponseAccContactExist.getBody().getSchmType().equals("SBA")
								|| connect24ResponseAccContactExist.getBody().getSchmType().equals("CAA")
								|| connect24ResponseAccContactExist.getBody().getSchmType().equals("ODA")) {

							if (connect24ResponseAccContactExist.getBody().getAccountStatus().equals("A")) {

								if (connect24ResponseAccContactExist.getBody().getCurrencyCode().equals(env.getProperty("cim.crncycode"))) {

									if (connect24ResponseAccContactExist.getBody().getFrezCode().equals("T")
											|| connect24ResponseAccContactExist.getBody().getFrezCode().equals("C")
											|| connect24ResponseAccContactExist.getBody().getFrezCode().equals("D")) {
										ipsDao.updatePublicKeyAccountStatus(authID,
												errorCode.ErrorCodeRegistration("10"));
										throw new IPSXRestException(errorCode.ErrorCodeRegistration("10"));
									} else {
										if (connect24ResponseAccContactExist.getBody().getDocNumber().equals(psuID)
												&& connect24ResponseAccContactExist.getBody().getPhoneNumber()
														.equals(userRequest.getPhoneNumber())) {

											//// Send Response

											response = new UserRegistrationResponse();
											Links links = new Links();
											links.setSelf("/accounts/" + recordID + "/");
											links.setAuthoriseTransaction(
													"/accounts/public-keys/" + recordID + "/authorisations/" + authID);
											response.setRecordId(recordID);
											response.setLinks(links);
											
											/////Calling OTP Server
											logger.info("Calling OTP server ");
											String msgResponse=sequence.sms(otp, userRequest.getPhoneNumber());
											logger.info("OTP Response:"+msgResponse);
											return response;

										} else {
											ipsDao.updatePublicKeyAccountStatus(authID,
													errorCode.ErrorCodeRegistration("8"));
											throw new IPSXRestException(errorCode.ErrorCodeRegistration("8"));
										}
									}

									//// Check Customer Data exist

								} else {
									ipsDao.updatePublicKeyAccountStatus(authID, errorCode.ErrorCodeRegistration("7"));
									throw new IPSXRestException(errorCode.ErrorCodeRegistration("7"));
								}

							} else {
								if (connect24ResponseAccContactExist.getBody().getAccountStatus().equals("C")) {
									ipsDao.updatePublicKeyAccountStatus(authID, errorCode.ErrorCodeRegistration("9"));
									throw new IPSXRestException(errorCode.ErrorCodeRegistration("9"));
								} else {
									ipsDao.updatePublicKeyAccountStatus(authID, errorCode.ErrorCodeRegistration("10"));
									throw new IPSXRestException(errorCode.ErrorCodeRegistration("10"));
								}

							}
						} else {
							ipsDao.updatePublicKeyAccountStatus(authID, errorCode.ErrorCodeRegistration("7"));
							throw new IPSXRestException(errorCode.ErrorCodeRegistration("7"));
						}

					} else {
						ipsDao.updatePublicKeyAccountStatus(authID, errorCode.ErrorCodeRegistration("7"));
						throw new IPSXRestException(errorCode.ErrorCodeRegistration("7"));
					}

				} else {
					ipsDao.updatePublicKeyAccountStatus(authID, errorCode.ErrorCodeRegistration("0"));
					throw new IPSXRestException(errorCode.ErrorCodeRegistration("0"));

				}

			} else {
				throw new IPSXRestException(errorCode.ErrorCodeRegistration("0"));
			}

		} catch (HttpClientErrorException e) {
			logger.error(e.getMessage());
			throw new IPSXRestException(errorCode.ErrorCodeRegistration("0"));
		}
	}

	
	////MYT Update Public Key
	////Check BOB Customer Exist.If customet Exist,update public key
	public UserRegistrationResponse updateUserPublicKey(String senderParticipantBIC, String senderParticipantMemberID,
			String receiverParticipantBIC, String receiverPartcipantMemberID, String psuDeviceID, String ipAddress,
			String psuID, UserRegistrationRequest userRequest, String recordID)
			throws KeyManagementException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException,
			CertificateException, FileNotFoundException, IOException {
		UserRegistrationResponse response = null;

		try {
			/**** Connect 24 Connection *****/

			Optional<RegPublicKey> regPublicKeyotm = regPublicKeyRep.findById(recordID);

			if (regPublicKeyotm.isPresent()) {

				//// generate auth ID
				String authID = sequence.generateRecordId();
				//// generate OTP
				String otp = sequence.generateOTP();

				ipsDao.updatePublicKey(senderParticipantBIC, senderParticipantMemberID, receiverParticipantBIC,
						receiverPartcipantMemberID, psuDeviceID, ipAddress, psuID, userRequest, recordID, authID, otp);

				ResponseEntity<AccountContactResponse> connect24ResponseAccContactExist = connect24Service
						.getAccountContact(psuDeviceID, ipAddress, psuID, userRequest.getAccount().getIdentification());

				if (connect24ResponseAccContactExist.getStatusCode() == HttpStatus.OK) {

					if (connect24ResponseAccContactExist.getBody().getStatus().equals("0")) {

						if (connect24ResponseAccContactExist.getBody().getSchmType().equals("SBA")
								|| connect24ResponseAccContactExist.getBody().getSchmType().equals("CAA")
								|| connect24ResponseAccContactExist.getBody().getSchmType().equals("ODA")) {

							if (connect24ResponseAccContactExist.getBody().getAccountStatus().equals("A")) {

								if (connect24ResponseAccContactExist.getBody().getCurrencyCode().equals(env.getProperty("cim.crncycode"))) {

									if (connect24ResponseAccContactExist.getBody().getFrezCode().equals("T")
											|| connect24ResponseAccContactExist.getBody().getFrezCode().equals("C")
											|| connect24ResponseAccContactExist.getBody().getFrezCode().equals("D")) {
										ipsDao.updatePublicKeyAccountStatus(authID,
												errorCode.ErrorCodeRegistration("10"));
										throw new IPSXRestException(errorCode.ErrorCodeRegistration("10"));
									} else {
										if (connect24ResponseAccContactExist.getBody().getDocNumber().equals(psuID)
												&& connect24ResponseAccContactExist.getBody().getPhoneNumber()
														.equals(userRequest.getPhoneNumber())) {

											response = new UserRegistrationResponse();
											Links links = new Links();
											links.setSelf("/accounts/" + recordID + "/");
											links.setAuthoriseTransaction(
													"/accounts/public-keys/" + recordID + "/authorisations/" + authID);
											response.setRecordId(recordID);
											response.setLinks(links);
											
										    /////Calling OTP Server
											logger.info("Calling OTP server");
											String msgResponse=sequence.sms(otp, userRequest.getPhoneNumber());
											logger.info("OTP Response:"+msgResponse);

											return response;

										} else {
											ipsDao.updatePublicKeyAccountStatus(authID,
													errorCode.ErrorCodeRegistration("8"));
											throw new IPSXRestException(errorCode.ErrorCodeRegistration("8"));
										}
									}

								} else {
									ipsDao.updatePublicKeyAccountStatus(authID, errorCode.ErrorCodeRegistration("7"));
									throw new IPSXRestException(errorCode.ErrorCodeRegistration("7"));
								}
							} else {
								if (connect24ResponseAccContactExist.getBody().getAccountStatus().equals("C")) {
									ipsDao.updatePublicKeyAccountStatus(authID, errorCode.ErrorCodeRegistration("9"));
									throw new IPSXRestException(errorCode.ErrorCodeRegistration("9"));
								} else {
									ipsDao.updatePublicKeyAccountStatus(authID, errorCode.ErrorCodeRegistration("10"));
									throw new IPSXRestException(errorCode.ErrorCodeRegistration("10"));
								}
							}

						} else {
							ipsDao.updatePublicKeyAccountStatus(authID, errorCode.ErrorCodeRegistration("7"));
							throw new IPSXRestException(errorCode.ErrorCodeRegistration("7"));
						}
					} else {
						ipsDao.updatePublicKeyAccountStatus(authID, errorCode.ErrorCodeRegistration("7"));
						throw new IPSXRestException(errorCode.ErrorCodeRegistration("7"));
					}
				} else {
					ipsDao.updatePublicKeyAccountStatus(authID, errorCode.ErrorCodeRegistration("0"));
					throw new IPSXRestException(errorCode.ErrorCodeRegistration("0"));
				}
			} else {
				throw new IPSXRestException(errorCode.ErrorCodeRegistration("1"));
			}

		} catch (HttpClientErrorException e) {
			logger.error(e.getMessage());
			throw new IPSXRestException(errorCode.ErrorCodeRegistration("0"));
		}
	}

	////MYT Delete Public Key
	public String deletePublicKey(String senderParticipantBIC, String senderParticipantMemberID,
			String receiverParticipantBIC, String receiverPartcipantMemberID, String psuDeviceID, String ipAddress,
			String psuID, String recordID) {
		
		try {
			Optional<RegPublicKey> regPublicKeyotm = regPublicKeyRep.findById(recordID);

			if (regPublicKeyotm.isPresent()) {

				RegPublicKey regPublicKey=regPublicKeyotm.get();
				regPublicKey.setDel_flg("Y");
				regPublicKey.setDel_user("SYSTEM");
				regPublicKey.setDel_time(new Date());
				
				return "Success";
			} else {
				throw new IPSXRestException(errorCode.ErrorCodeRegistration("1"));
			}
			
		} catch (HttpClientErrorException e) {
			logger.error(e.getMessage());
			throw new IPSXRestException(errorCode.ErrorCodeRegistration("0"));
		}
	}

	////MYT OTP Authentication (public Key)
	////Validate OTP
	public SCAAthenticationResponse authTransaction(String senderParticipantBIC, String senderParticipantMemberID,
			String receiverParticipantBIC, String receiverPartcipantMemberID, String psuDeviceID, String ipAddress,
			String psuID, SCAAuthenticatedData scaAuthenticatedData, String recordID, String authID) {

		SCAAthenticationResponse response = ipsDao.updateSCA(scaAuthenticatedData, recordID, authID);
		return response;
	}

	////Myt Consent ID Generator
	////Create Consent ID
	public ConsentResponse createConsentID(String psuDeviceID, String ipAddress, String psuID,
			ConsentRequest consentRequest, String consentID, String authID) {
		ConsentResponse response = null;

		try {
			/**** Connect 24 Connection *****/
			ResponseEntity<C24FTResponse> connect24Response = connect24Service.checkAccountExist(psuDeviceID, ipAddress,
					psuID, consentRequest.getData().get(0).getAccount().get(0).getIdentification());
			if (connect24Response.getStatusCode() == HttpStatus.OK) {
				String otp = sequence.generateOTP();
				logger.info("Connect24 Processed Successfully");
				logger.info("Register User Publickey data to table");
				ipsDao.createConsent(psuDeviceID, ipAddress, psuID, consentRequest, consentID, authID, otp);

				Data data = new Data();
				data.setConsentId(consentID);
				data.setStatus(TranMonitorStatus.AwaitingAuthorisation.toString());
				data.setStatusUpdateDateTime(new Date());
				data.setCreationDateTime(new Date());

				List<String> list = new ArrayList<String>();
				list.add(TranMonitorStatus.ReadBalances.toString());
				list.add(TranMonitorStatus.ReadAccountsDetails.toString());
				list.add(TranMonitorStatus.ReadTransactionsDetails.toString());
				data.setPermissions(list);
				data.setExpirationDateTime(new Date());
				data.setTransactionFromDateTime(new Date());
				data.setTransactionToDateTime(new Date());

				Links links = new Links();
				links.setSelf("/account-access-consents/" + consentID + "/");
				links.setAuthoriseTransaction("/account-access-consents/" + consentID + "/authorisations/" + authID);
				data.setLinks(links);

				response.setData(data);

				return response;
			} else {
				throw new IPSXRestException("104:Internal Error");
			}
		} catch (HttpClientErrorException e) {
			logger.error(e.getMessage());
		}
		return response;
	}

	////MYt OTP Authentication Consent
	///Validate OTP 
	public SCAAthenticationResponse authTransactionConsent(String psuDeviceID, String ipAddress, String psuID,
			SCAAuthenticatedData scaAuthenticatedData, String consentID, String authID) {
		SCAAthenticationResponse response = ipsDao.updateSCAConsent(scaAuthenticatedData, consentID, authID);
		return response;
	}
	
	public ConsentAccessResponse createConsentAccessID(String x_request_id, String sender_participant_bic,
			String sender_participant_member_id, String receiver_participant_bic, String receiver_participant_member_id,
			String psuDeviceID, String psuIPAddress, String psuID, String psuIDCountry, String psuIDType,
			ConsentAccessRequest consentRequest) {
		
		ConsentAccessResponse response=null;
		try {

			//// generate OTP
			String otp = sequence.generateOTP();
			//// generate Record ID
			String consentID = sequence.generateConsentId();
			//// generate Auth ID
			String authID = sequence.generateRecordId();

			logger.info("Register User Publickey data to table");

			//// update Table

			String status = ipsDao.createConsentIDAccess(x_request_id, sender_participant_bic,sender_participant_member_id,
					receiver_participant_bic, receiver_participant_member_id, psuDeviceID, psuIPAddress, psuID,psuIDCountry,
					psuIDType,consentRequest,consentID, authID, otp);

			//// Send Response
			if (status.equals("0")) {
				ResponseEntity<AccountContactResponse> connect24ResponseAccContactExist = connect24Service
						.getAccountContact(psuDeviceID, psuIPAddress, psuID, consentRequest.getAccounts().get(0).getIdentification());

				if (connect24ResponseAccContactExist.getStatusCode() == HttpStatus.OK) {

					if (connect24ResponseAccContactExist.getBody().getStatus().equals("0")) {

						if (connect24ResponseAccContactExist.getBody().getSchmType().equals("SBA")
								|| connect24ResponseAccContactExist.getBody().getSchmType().equals("CAA")
								|| connect24ResponseAccContactExist.getBody().getSchmType().equals("ODA")) {

							if (connect24ResponseAccContactExist.getBody().getAccountStatus().equals("A")) {

								if (connect24ResponseAccContactExist.getBody().getCurrencyCode().equals(env.getProperty("cim.crncycode"))) {

									if (connect24ResponseAccContactExist.getBody().getFrezCode().equals("T")
											|| connect24ResponseAccContactExist.getBody().getFrezCode().equals("C")
											|| connect24ResponseAccContactExist.getBody().getFrezCode().equals("D")) {
										ipsDao.updatePublicKeyAccountStatus(authID,
												errorCode.ErrorCodeRegistration("10"));
										throw new IPSXRestException(errorCode.ErrorCodeRegistration("10"));
									} else {
										if (connect24ResponseAccContactExist.getBody().getDocNumber().equals(psuID)
												&& connect24ResponseAccContactExist.getBody().getPhoneNumber()
														.equals(consentRequest.getPhoneNumber())) {

											//// Send Response

											response = new ConsentAccessResponse();
											response.setConsentID(consentID);
											response.setStatus(TranMonitorStatus.AwaitingAuthorisation.toString());
											response.setStatusUpdateDateTime(listener.getxmlGregorianCalender("0"));
											response.setCreationDateTime(listener.getxmlGregorianCalender("0"));
							
											response.setPermissions(consentRequest.getPermissions());
											//response.setExpirationDateTime(consentRequest.getExpirationDateTime());
											//response.setTransactionFromDateTime(consentRequest.getTransactionFromDateTime());
											//response.setTransactionToDateTime(consentRequest.getTransactionToDateTime());
											
											Links links = new Links();
											links.setSelf("/accounts-consents/" + consentID);
											links.setAuthoriseTransaction(
													"/accounts-consents/" + consentID + "/authorisations/" + authID);
											response.setLinks(links);
											
											/////Calling OTP Server
											logger.info("Calling OTP server ");
											String msgResponse=sequence.sms(otp, consentRequest.getPhoneNumber());
											logger.info("OTP Response:"+msgResponse);
											return response;

										} else {
											ipsDao.updatePublicKeyAccountStatus(authID,
													errorCode.ErrorCodeRegistration("8"));
											throw new IPSXRestException(errorCode.ErrorCodeRegistration("8"));
										}
									}

									//// Check Customer Data exist

								} else {
									ipsDao.updatePublicKeyAccountStatus(authID, errorCode.ErrorCodeRegistration("7"));
									throw new IPSXRestException(errorCode.ErrorCodeRegistration("7"));
								}

							} else {
								if (connect24ResponseAccContactExist.getBody().getAccountStatus().equals("C")) {
									ipsDao.updatePublicKeyAccountStatus(authID, errorCode.ErrorCodeRegistration("9"));
									throw new IPSXRestException(errorCode.ErrorCodeRegistration("9"));
								} else {
									ipsDao.updatePublicKeyAccountStatus(authID, errorCode.ErrorCodeRegistration("10"));
									throw new IPSXRestException(errorCode.ErrorCodeRegistration("10"));
								}

							}
						} else {
							ipsDao.updatePublicKeyAccountStatus(authID, errorCode.ErrorCodeRegistration("7"));
							throw new IPSXRestException(errorCode.ErrorCodeRegistration("7"));
						}

					} else {
						ipsDao.updatePublicKeyAccountStatus(authID, errorCode.ErrorCodeRegistration("7"));
						throw new IPSXRestException(errorCode.ErrorCodeRegistration("7"));
					}

				} else {
					ipsDao.updatePublicKeyAccountStatus(authID, errorCode.ErrorCodeRegistration("0"));
					throw new IPSXRestException(errorCode.ErrorCodeRegistration("0"));

				}

			} else {
				throw new IPSXRestException(errorCode.ErrorCodeRegistration("0"));
			}

		} catch (HttpClientErrorException e) {
			logger.error(e.getMessage());
			throw new IPSXRestException(errorCode.ErrorCodeRegistration("0"));
		}
	}
	
	public SCAAthenticationResponse authConsentAccessID(String x_request_id, String sender_participant_bic,
			String sender_participant_member_id, String receiver_participant_bic, String receiver_participant_member_id,
			String psuDeviceID, String psuIPAddress, String psuID, String psuIDCountry, String psuIDType,
			SCAAuthenticatedData scaAuthenticatedData, String consentID, String authID, String cryptogram) {
		SCAAthenticationResponse response = ipsDao.updateSCAConsentAccess(scaAuthenticatedData, consentID, authID,cryptogram);
		return response;
	}
	
	public String deleteConsentAccessID(String x_request_id, String sender_participant_bic,
			String sender_participant_member_id, String receiver_participant_bic, String receiver_participant_member_id,
			String psuDeviceID, String psuIPAddress, String psuID, String psuIDCountry, String psuIDType,
			SCAAuthenticatedData scaAuthenticatedData, String consentID) {
		try {
			Optional<ConsentAccessTable> regPublicKeyotm = consentAccessTableRep.findById(consentID);

			if (regPublicKeyotm.isPresent()) {

				ConsentAccessTable regPublicKey=regPublicKeyotm.get();
				regPublicKey.setDel_flg("Y");
				regPublicKey.setDel_user("SYSTEM");
				regPublicKey.setDel_time(new Date());
				
				return "Success";
			} else {
				throw new IPSXRestException(errorCode.ErrorCodeRegistration("1"));
			}
			
		} catch (HttpClientErrorException e) {
			logger.error(e.getMessage());
			throw new IPSXRestException(errorCode.ErrorCodeRegistration("0"));
		}
	}
	
	public ConsentAccountBalance consentBalance(String x_request_id, String sender_participant_bic,
			String sender_participant_member_id, String receiver_participant_bic, String receiver_participant_member_id,
			String psuDeviceID, String psuIPAddress, String psuID, String psuIDCountry, String psuIDType,
			String consentID, String accountID,String cryptogram) {
		
		
		String status = ipsDao.consentDataRegisterBalances(x_request_id, psuDeviceID,
				psuIPAddress, psuID, psuIDCountry, psuIDType, sender_participant_bic,sender_participant_member_id,
				receiver_participant_bic, receiver_participant_member_id,consentID,accountID);
		
		ConsentAccountBalance consentAcctBalance=null;

		if(status.equals("0")) {
			String response=ipsDao.checkConsentAccountExist(x_request_id, sender_participant_bic,
					sender_participant_member_id, receiver_participant_bic, receiver_participant_member_id, psuDeviceID,
					psuIPAddress, psuID, psuIDCountry, psuIDType, consentID,accountID,cryptogram);
			
			if(!response.equals("0")) {
				
				
				ResponseEntity<C24FTResponse> balannce = connect24Service
						.getBalance(accountID);

				if (balannce.getStatusCode() == HttpStatus.OK) {
					consentAcctBalance=new ConsentAccountBalance();
					
					ReadConsentBalance readAcctBalance=new ReadConsentBalance();
					readAcctBalance.setAccountID(accountID);
					
					Amount amount=new Amount();
					amount.setAmount(balannce.getBody().getBalance().getAvailableBalance());
					amount.setCurrency(balannce.getBody().getTranCurrency());
					readAcctBalance.setAmount(amount);
					
					readAcctBalance.setCreditDebitIndicator(TranMonitorStatus.Credit.toString());
					readAcctBalance.setType(TranMonitorStatus.InterimAvailable.toString());
					
					readAcctBalance.setDateTime(listener.getxmlGregorianCalender("0"));
					
					consentAcctBalance.setBalance(readAcctBalance);
					
					Links links = new Links();
					links.setSelf("/accounts/"+consentID+"/balances");
					consentAcctBalance.setLinks(links);
					
					////Update Tran Non CBS Table
					ipsDao.updateNonCBSData(consentID,x_request_id,accountID,sequence.generateSystemTraceAuditNumber(),
							TranMonitorStatus.ReadBalances.toString(),"Success","");
					
				    ////Update consent Inquiry Table
					ipsDao.updateConsentData(x_request_id,"Success","");
					
					return consentAcctBalance;
				} else {
					ipsDao.updateNonCBSData(consentID,x_request_id,accountID,sequence.generateSystemTraceAuditNumber(),
							TranMonitorStatus.ReadBalances.toString(),"Failure",balannce.getBody().getError_desc().get(0));
					
					////Update consent Inquiry Table
					ipsDao.updateConsentData(x_request_id,"Failure",balannce.getBody().getError_desc().get(0));
					
					throw new IPSXRestException(errorCode.ErrorCodeRegistration("0"));
				}
			}else {
			    ////Update consent Inquiry Table
				ipsDao.updateConsentData(x_request_id,"Failure",errorCode.ErrorCodeRegistration("0"));
				throw new IPSXRestException(errorCode.ErrorCodeRegistration("0"));
			}
		}else {
			////Update consent Inquiry Table
			ipsDao.updateConsentData(x_request_id,"Failure",errorCode.ErrorCodeRegistration("0"));
			throw new IPSXRestException(errorCode.ErrorCodeRegistration("0"));
		}
		
	}
	
	public AccountListResponse accountList(String x_request_id, String sender_participant_bic,
			String sender_participant_member_id, String receiver_participant_bic, String receiver_participant_member_id,
			String psuDeviceID, String psuIPAddress, String psuID, String psuIDCountry, String psuIDType,
			String consentID, String cryptogram) {
		
		String status = ipsDao.consentDataRegisterAccountListInc(x_request_id, psuDeviceID,
				psuIPAddress, psuID, psuIDCountry, psuIDType, sender_participant_bic,sender_participant_member_id,
				receiver_participant_bic, receiver_participant_member_id,consentID);
		
		if(status.equals("0")) {
			
			String response=ipsDao.checkConsentAccountExist(x_request_id, sender_participant_bic,
					sender_participant_member_id, receiver_participant_bic, receiver_participant_member_id, psuDeviceID,
					psuIPAddress, psuID, psuIDCountry, psuIDType, consentID,"",cryptogram);
			
			if(!response.equals("0")) {
				 ////Update consent Inquiry Table
				ipsDao.updateConsentData(x_request_id,"Failure",errorCode.ErrorCodeRegistration("0"));
				throw new IPSXRestException(errorCode.ErrorCodeRegistration("0"));
			}else {
				 ////Update consent Inquiry Table
				ipsDao.updateConsentData(x_request_id,"Failure",errorCode.ErrorCodeRegistration("0"));
				throw new IPSXRestException(errorCode.ErrorCodeRegistration("0"));
			}
			
		}else {
		    ////Update consent Inquiry Table
			ipsDao.updateConsentData(x_request_id,"Failure",errorCode.ErrorCodeRegistration("0"));
			throw new IPSXRestException(errorCode.ErrorCodeRegistration("0"));
		}
		
	}
	
	public AccountsListAccounts accountListInd(String x_request_id, String sender_participant_bic,
			String sender_participant_member_id, String receiver_participant_bic, String receiver_participant_member_id,
			String psuDeviceID, String psuIPAddress, String psuID, String psuIDCountry, String psuIDType,
			String consentID, String cryptogram,String accountID) {
		String status = ipsDao.consentDataRegisterAccountInc(x_request_id, psuDeviceID,
				psuIPAddress, psuID, psuIDCountry, psuIDType, sender_participant_bic,sender_participant_member_id,
				receiver_participant_bic, receiver_participant_member_id,consentID,accountID);
		
		if(status.equals("0")) {
			
			String response=ipsDao.checkConsentAccountExist(x_request_id, sender_participant_bic,
					sender_participant_member_id, receiver_participant_bic, receiver_participant_member_id, psuDeviceID,
					psuIPAddress, psuID, psuIDCountry, psuIDType, consentID,accountID,cryptogram);
			
			if(!response.equals("0")) {
				 ////Update consent Inquiry Table
				ipsDao.updateConsentData(x_request_id,"Failure",errorCode.ErrorCodeRegistration("0"));
				throw new IPSXRestException(errorCode.ErrorCodeRegistration("0"));
			}else {
				 ////Update consent Inquiry Table
				ipsDao.updateConsentData(x_request_id,"Failure",errorCode.ErrorCodeRegistration("0"));
				throw new IPSXRestException(errorCode.ErrorCodeRegistration("0"));
			}
			
		}else {
		    ////Update consent Inquiry Table
			ipsDao.updateConsentData(x_request_id,"Failure",errorCode.ErrorCodeRegistration("0"));
			throw new IPSXRestException(errorCode.ErrorCodeRegistration("0"));
		}
	}
	
	public TransactionListResponse tranList(String x_request_id, String sender_participant_bic,
			String sender_participant_member_id, String receiver_participant_bic, String receiver_participant_member_id,
			String psuDeviceID, String psuIPAddress, String psuID, String psuIDCountry, String psuIDType,
			String consentID, String cryptogram, String accountID, String fromBookingDateTime,
			String toBookingDateTime) {
		String status = ipsDao.consentDataRegisterTransactionInc(x_request_id, psuDeviceID,
				psuIPAddress, psuID, psuIDCountry, psuIDType, sender_participant_bic,sender_participant_member_id,
				receiver_participant_bic, receiver_participant_member_id,consentID,accountID);
		
		if(status.equals("0")) {
			
			String response=ipsDao.checkConsentAccountExist(x_request_id, sender_participant_bic,
					sender_participant_member_id, receiver_participant_bic, receiver_participant_member_id, psuDeviceID,
					psuIPAddress, psuID, psuIDCountry, psuIDType, consentID,accountID,cryptogram);
			
			if(!response.equals("0")) {
				 ////Update consent Inquiry Table
				ipsDao.updateConsentData(x_request_id,"Failure",errorCode.ErrorCodeRegistration("0"));
				throw new IPSXRestException(errorCode.ErrorCodeRegistration("0"));
			}else {
				 ////Update consent Inquiry Table
				ipsDao.updateConsentData(x_request_id,"Failure",errorCode.ErrorCodeRegistration("0"));
				throw new IPSXRestException(errorCode.ErrorCodeRegistration("0"));
			}
			
		}else {
		    ////Update consent Inquiry Table
			ipsDao.updateConsentData(x_request_id,"Failure",errorCode.ErrorCodeRegistration("0"));
			throw new IPSXRestException(errorCode.ErrorCodeRegistration("0"));
		}
	}
	
	////RTP Transaction
	////MYt Initiate the RTP request,debit Customer Account and credit SettleAccount
	public String rtpFundTransferConnection(String sysTraceAuditNumber, String acctNumber, String ccy, String trAmt,
			String seqUniqueID, String jwtToken, String creditorAccount, String endToEndID, String debtorAccountName,String trRmks,SendT request) {

		String tranResponse = "";
		
	    logger.info(String.valueOf(signDocument.verifySignParseDoc(request.getMessage().getBlock4())));
		
		if(signDocument.verifySignParseDoc(request.getMessage().getBlock4())) {
		
		if (!acctNumber.equals("")) {
			if (!trAmt.equals("0") && !trAmt.equals("0.00")&&!trAmt.equals("")) {
				logger.info(seqUniqueID + ": Get Contact Details ");
				logger.info(seqUniqueID + ": -------------------- ");
				ResponseEntity<AccountContactResponse> connect24ResponseAccContactExist = connect24Service
						.getAccountContact("", "", "", acctNumber);

				if (connect24ResponseAccContactExist.getStatusCode() == HttpStatus.OK) {

					logger.info(seqUniqueID + ": Get Account Status Successfully  Account No: " + acctNumber);

					if (connect24ResponseAccContactExist.getBody().getStatus().equals("0")) {

						if (connect24ResponseAccContactExist.getBody().getSchmType().equals("SBA")
								|| connect24ResponseAccContactExist.getBody().getSchmType().equals("CAA")
								|| connect24ResponseAccContactExist.getBody().getSchmType().equals("ODA")) {

							if (connect24ResponseAccContactExist.getBody().getAccountStatus().equals("A")) {
								logger.info(seqUniqueID + ": Account is Active");
								if (connect24ResponseAccContactExist.getBody().getFrezCode().equals("T")
										|| connect24ResponseAccContactExist.getBody().getFrezCode().equals("D")
										|| connect24ResponseAccContactExist.getBody().getFrezCode().equals("C")) {

									logger.info(seqUniqueID + ": Total Freeze or Debit Freeze");

									ipsDao.updateCBSStatusError(seqUniqueID,
											TranMonitorStatus.VALIDATION_ERROR.toString(),
											"Transaction forbidden on this type of account/"
													+ "TotalFreeze or Debit Freeze",
											TranMonitorStatus.FAILURE.toString());
									tranResponse = errorCode.ErrorCode("BOB119");
								} else {

									if (connect24ResponseAccContactExist.getBody().getCurrencyCode().equals(env.getProperty("cim.crncycode"))) {

										if (connect24ResponseAccContactExist.getBody().getCurrencyCode().equals(ccy)) {
											logger.info(seqUniqueID + ": Token/Cryptogram Token Validated");

											if (validateJWTToken(jwtToken, acctNumber, creditorAccount, ccy, trAmt,
													endToEndID)) {
												logger.info(seqUniqueID + ": Token/Cryptogram Token Validated");

												logger.info(
														seqUniqueID + ": Calling Connect 24 For Fund Transfer Debit");
												
												//tranResponse = errorCode.ErrorCode("BOB0");
												ResponseEntity<C24FTResponse> connect24Response = connect24Service
														.rtpFundRequest(sysTraceAuditNumber, acctNumber, ccy, trAmt,
																seqUniqueID,trRmks);

												if (connect24Response.getStatusCode() == HttpStatus.OK) {
													logger.info(seqUniqueID
															+ ": Connect 24 Fund Transfer Debited successfully");

													tranResponse = errorCode.ErrorCode("BOB0");

												} else {
													logger.info(seqUniqueID
															+ ": Connect 24 Fund Transfer Debited not successfully Error:"
															+ connect24Response.getBody().getError_desc().get(0));

													ipsDao.updateCBSStatusError(seqUniqueID,
															TranMonitorStatus.CBS_DEBIT_ERROR.toString(),
															connect24Response.getBody().getError_desc().get(0),
															TranMonitorStatus.FAILURE.toString());

													tranResponse = errorCode.ErrorCode(
															connect24Response.getBody().getError().toString());
												}

											} else {
												logger.info(seqUniqueID + ": Token/Cryptogram Token not Validated");
												ipsDao.updateCBSStatusError(seqUniqueID,
														TranMonitorStatus.VALIDATION_ERROR.toString(),
														"Technical Problem/Cryptogram Failed",
														TranMonitorStatus.FAILURE.toString());
												tranResponse = errorCode.ErrorCode("BOB500");
											}
										} else {
											logger.info(seqUniqueID + ":Invalid Currency Code ");

											ipsDao.updateCBSStatusError(seqUniqueID,
													TranMonitorStatus.VALIDATION_ERROR.toString(),
													"Validation Failed/Invalid Currency Code",
													TranMonitorStatus.FAILURE.toString());
											tranResponse = errorCode.ErrorCode("AM11");
										}

									} else {
										logger.info(seqUniqueID + ":Invalid Currency Code ");

										ipsDao.updateCBSStatusError(seqUniqueID,
												TranMonitorStatus.VALIDATION_ERROR.toString(),
												"Transaction forbidden/Invalid Account/FRCNCY",
												TranMonitorStatus.FAILURE.toString());
										tranResponse = errorCode.ErrorCode("BOB119");
									}
								}

							} else {
								if (connect24ResponseAccContactExist.getBody().getAccountStatus().equals("C")) {

									logger.info(seqUniqueID + ": Account is Closed");

									ipsDao.updateCBSStatusError(seqUniqueID,
											TranMonitorStatus.VALIDATION_ERROR.toString(), "Closed Account Number",
											TranMonitorStatus.FAILURE.toString());
									tranResponse = errorCode.ErrorCode("AC04");
								} else {
									logger.info(seqUniqueID + ": Account is blocked/dormant");

									ipsDao.updateCBSStatusError(seqUniqueID,
											TranMonitorStatus.VALIDATION_ERROR.toString(), "Blocked Account Number",
											TranMonitorStatus.FAILURE.toString());
									tranResponse = errorCode.ErrorCode("AC06");
								}
							}
						} else {

							logger.info(seqUniqueID + ":Invalid Account Type ");

							ipsDao.updateCBSStatusError(seqUniqueID, TranMonitorStatus.VALIDATION_ERROR.toString(),
									"Transaction forbidden on this type of account",
									TranMonitorStatus.FAILURE.toString());
							tranResponse = errorCode.ErrorCode("BOB119");
						}

					} else {

						if (connect24ResponseAccContactExist.getBody().getStatus().equals("1")) {
							logger.info(seqUniqueID + ": Invalid Account Number ");

							ipsDao.updateCBSStatusError(seqUniqueID, TranMonitorStatus.VALIDATION_ERROR.toString(),
									"Incorrect Account Debtor Number", TranMonitorStatus.FAILURE.toString());
							tranResponse = errorCode.ErrorCode("AC01");
						} else {
							logger.info(seqUniqueID + ": Invalid Account Type ");

							ipsDao.updateCBSStatusError(seqUniqueID, TranMonitorStatus.VALIDATION_ERROR.toString(),
									"Transaction forbidden on this type of account/" + "Invalid Account Number",
									TranMonitorStatus.FAILURE.toString());
							tranResponse = errorCode.ErrorCode("BOB119");
						}

					}

				} else {
					
					ipsDao.updateCBSStatusError(seqUniqueID,
							TranMonitorStatus.CBS_DEBIT_ERROR.toString(),
							connect24ResponseAccContactExist.getBody().getError_desc().get(0),
							TranMonitorStatus.FAILURE.toString());

					tranResponse = errorCode
							.ErrorCode(connect24ResponseAccContactExist.getBody().getError().toString());
					
					//ipsDao.updateCBSStatusError(seqUniqueID, TranMonitorStatus.VALIDATION_ERROR.toString(),
							//"Technical Problem/From Contact", TranMonitorStatus.FAILURE.toString());
					//tranResponse = errorCode.ErrorCode("BOB500");
				}

			} else {
				logger.info(seqUniqueID + ": Zero Amount");
				ipsDao.updateCBSStatusError(seqUniqueID, TranMonitorStatus.VALIDATION_ERROR.toString(),
						"Validation Failed/Zero Amount", TranMonitorStatus.FAILURE.toString());
				tranResponse = errorCode.ErrorCode("AM01");
			}
		} else {
			logger.info(seqUniqueID + ": Missing Debtor Account or Identification");
			ipsDao.updateCBSStatusError(seqUniqueID, TranMonitorStatus.VALIDATION_ERROR.toString(),
					"Validation Failed/Missing Debtor Account", TranMonitorStatus.FAILURE.toString());
			tranResponse = errorCode.ErrorCode("RR01");
		}
		}else {
			logger.info(seqUniqueID + ": Signature Failed");

			ipsDao.updateCBSStatusError(seqUniqueID, TranMonitorStatus.VALIDATION_ERROR.toString(),
					"Signature Failed",
					TranMonitorStatus.FAILURE.toString());

			tranResponse = errorCode.ErrorCode("BOB500");
		}

		return tranResponse;
	}

	
	////Validate JWT Token
	////In Every RTP Tran Message having Authentication , We validate the Token already registered.
	///If Token validated,allow transaction
	private boolean validateJWTToken(String jwtToken, String debtorAccount, String creditorAccount, String ccy,
			String trAmt, String endToEndID) {

		boolean response = false;

		try {
			String[] split_string = jwtToken.split("\\.");
			String base64EncodedHeader = split_string[0];
			String base64EncodedBody = split_string[1];

			//Base64 base64Url = new Base64(true);
			//String body = new String(base64Url.decode(base64EncodedBody));
			//System.out.println("JWT Body : " + body);

			String publicKeyfromMYT=ipsDao.getPublicKey(debtorAccount);
			
			logger.info("publicKeyfromMYT"+publicKeyfromMYT);
			
			if(!publicKeyfromMYT.equals("0")) {

			        PublicKey publicKey = decodePublicKey(pemToDer(publicKeyfromMYT));

			        Jws<Claims> claimsJws = Jwts.parser() //
			                .setSigningKey(publicKey) //
			                .parseClaimsJws(jwtToken) //
			                ;			        
			        CryptogramResponse cryptogramResponse = new Gson().fromJson(claimsJws.getBody().toString(), CryptogramResponse.class);

			        logger.info(cryptogramResponse.toString());

					if (debtorAccount.equals(cryptogramResponse.getDebtorAccount())
							&& creditorAccount.equals(cryptogramResponse.getCreditorAccount())
							&& ccy.equals(cryptogramResponse.getCurrency()) 
							&& String.valueOf(Double.parseDouble(trAmt)).equals(String.valueOf(Double.parseDouble(cryptogramResponse.getAmount())))
							&& endToEndID.equals(cryptogramResponse.getEndToEndId())) {
						response = ipsDao.checkRegPublicKey(cryptogramResponse);
						logger.info("Crypto Success");
						return response;
					} else {
						response = false;
						logger.info("Crypto failure");
						return response;
					}
			}else {
				response = false;
				logger.info("Public key not got");
				return response;
			}

			

		} catch (Exception e) {
			response = false;
			return response;
		}
	}

	public byte[] pemToDer(String pem) throws IOException {
		return Base64.getDecoder().decode(stripBeginEnd(pem));
	}

	public String stripBeginEnd(String pem) {

		String stripped = pem.replaceAll("-----BEGIN (.*)-----", "");
		stripped = stripped.replaceAll("-----END (.*)----", "");
		stripped = stripped.replaceAll("\r\n", "");
		stripped = stripped.replaceAll("\n", "");

		return stripped.trim();
	}

	public static PublicKey decodePublicKey(byte[] der) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {

        X509EncodedKeySpec spec = new X509EncodedKeySpec(der);

        KeyFactory kf = KeyFactory.getInstance("RSA"
                //        , "BC" //use provider BouncyCastle if available.
        );

        return kf.generatePublic(spec);
    }

	//////Reverse Transaction Debit Account(MConnect,RTP and Bulk Debit etc)
	/////Incase transaction Failed during reversal, That time we initiate reversal transaction from IPS Admin using Connect 24
	public String reverseDebitTransaction(TranCBSTable tranCBSTable, String userID) {

		try {
			/// Generate Audit Number
			String AuditNumber = sequence.generateSystemTraceAuditNumber();

			ResponseEntity<C24FTResponse> connect24Response = connect24Service.ManualdbtReverseFundRequest(
					tranCBSTable.getBob_account(), AuditNumber, tranCBSTable.getTran_currency(),
					tranCBSTable.getTran_amount().toString(), tranCBSTable.getSequence_unique_id(), userID,"TR/"+ipsDao.getIPSXStatusError(tranCBSTable.getSequence_unique_id()));
			if (connect24Response.getStatusCode() == HttpStatus.OK) {
				
				return "Success";
			} else if (connect24Response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
				throw new ServerErrorException(SERVER_ERROR);
			} else {
				throw new Connect24Exception(connect24Response.getBody().getError() + ":"
						+ connect24Response.getBody().getError_desc().get(0));
			}
		} catch (RemoteAccessException e) {
			throw new ServerErrorException(SERVER_ERROR);
		}

	}
	
	//////Reverse Transaction Bulk Credit
	/////Incase transaction Failed during reversal, That time we initiate reversal transaction from IPS Admin using Connect 24

	public String reversereverseBulkCreditTransaction(TranCBSTable tranCBSTable, String userID) {

		try {
			//// Generate Audit Number
			String AuditNumber = sequence.generateSystemTraceAuditNumber();

			////calling Connect 24 for reversal Transaction
			ResponseEntity<C24FTResponse> connect24Response = connect24Service.ManualdbtReverseBulkCreditFundRequest(
					tranCBSTable.getBob_account(), AuditNumber, tranCBSTable.getTran_currency(),
					tranCBSTable.getTran_amount().toString(), tranCBSTable.getSequence_unique_id(), userID,"TR/"+ipsDao.getIPSXStatusError(tranCBSTable.getSequence_unique_id()));
			if (connect24Response.getStatusCode() == HttpStatus.OK) {
				return "Success";
			} else if (connect24Response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
				throw new ServerErrorException(SERVER_ERROR);
			} else {
				throw new Connect24Exception(connect24Response.getBody().getError() + ":"
						+ connect24Response.getBody().getError_desc().get(0));
			}
		} catch (RemoteAccessException e) {
			throw new ServerErrorException(SERVER_ERROR);
		}

	}
	
    //////Reverse Transaction Debit Account(MConnect,RTP and Bulk Debit etc)
	/////Incase transaction Failed during reversal, That time we initiate reversal transaction from IPS Admin using Connect 24
	public String reverseBulkDebitTransaction(TranCBSTable tranCBSTable, String userID) {

		try {
			/// Generate Audit Number
			String AuditNumber = sequence.generateSystemTraceAuditNumber();

			ResponseEntity<C24FTResponse> connect24Response = connect24Service.ManualBulkdbtReverseFundRequest(
					tranCBSTable.getBob_account(), AuditNumber, tranCBSTable.getTran_currency(),
					tranCBSTable.getTran_amount().toString(), tranCBSTable.getSequence_unique_id(), userID,"TR/"+ipsDao.getIPSXStatusError(tranCBSTable.getSequence_unique_id()));
			if (connect24Response.getStatusCode() == HttpStatus.OK) {
				
				return "Success";
			} else if (connect24Response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
				throw new ServerErrorException(SERVER_ERROR);
			} else {
				throw new Connect24Exception(connect24Response.getBody().getError() + ":"
						+ connect24Response.getBody().getError_desc().get(0));
			}
		} catch (RemoteAccessException e) {
			throw new ServerErrorException(SERVER_ERROR);
		}

	}
	

	/////Reverse Transaction Credit Account(Inward Transaction)
	/////Incase transaction Failed during reversal, That time we initiate reversal transaction from IPS Admin using Connect 24

	public String reverseCreditTransaction(TranCBSTable tranCBSTable, String userID) {
		try {
			//// Generate Audit Number
			String AuditNumber = sequence.generateSystemTraceAuditNumber();

			////Calling Connect 24 for Reverce CreditTransaction
			ResponseEntity<C24FTResponse> connect24Response = connect24Service.ManualCdtReverseFundRequest(
					tranCBSTable.getBob_account(), AuditNumber, tranCBSTable.getTran_currency(),
					tranCBSTable.getTran_amount().toString(), tranCBSTable.getSequence_unique_id(), userID,"TR/"+ipsDao.getIPSXStatusError(tranCBSTable.getSequence_unique_id()));
			if (connect24Response.getStatusCode() == HttpStatus.OK) {
				return "Success";
			} else if (connect24Response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
				throw new ServerErrorException(SERVER_ERROR);
			} else {
				throw new Connect24Exception(connect24Response.getBody().getError() + ":"
						+ connect24Response.getBody().getError_desc().get(0));
			}
		} catch (RemoteAccessException e) {
			throw new ServerErrorException(SERVER_ERROR);
		}

	}

	///// Charges And Fee Connection MYT Registration
	///// Calling Connect 24
	///// If charges Applicable for MYT Registartion , Debit Customer Account to
	///// Credit Income Charge account
	public void ipsChargesAndFeeMyt(String recordID, String remarks) {

		Optional<RegPublicKey> regPublicKeyRef = regPublicKeyRep.findById(recordID);

		logger.info("Charges and fee: MYT Registration/" + recordID);
		if (regPublicKeyRef.isPresent()) {
			ResponseEntity<C24FTResponse> connect24Response = null;

			try {
				//// get Fees Amount from service and Fees Table
				BigDecimal tranAmt = ipsChargesAndFeeRep.findById(env.getProperty("charge.Reg")).get().getFees();

				if (tranAmt != null && Double.parseDouble(tranAmt.toString()) > 0) {
					logger.info("Charges and fee : Charges Applicable");
					connect24Response = connect24Service.ipsChargeAndFeeMyt(regPublicKeyRef.get(), remarks,
							tranAmt.toString(), sequence.generateSystemTraceAuditNumber(), remarks);

					if (connect24Response.getStatusCode() == HttpStatus.OK) {
						logger.info("Charges and fee : Charges Debited Successfully");
						RegPublicKey regPublic = regPublicKeyRef.get();
						regPublic.setCharge_app_flg("Y");
						regPublicKeyRep.save(regPublic);
					} else {
						logger.info("Charges and fee : Charges Not Debited/"
								+ connect24Response.getBody().getError_desc().get(0).toString());
						RegPublicKey regPublic = regPublicKeyRef.get();
						regPublic.setCharge_app_flg("N");
						regPublicKeyRep.save(regPublic);
					}
				} else {
					logger.info("Charges and fee : Charges Not Applicable");
				}

			} catch (Exception e) {
				logger.info("Charges and Fee  : " + e.getLocalizedMessage());
			}
		}
	}
	
	
	///// Charges And Fee Connection Other Bank Fund Transfer
	///// Calling Connect 24
	///// If charges Applicable for Other Bank Fund Transfer , Debit Customer
	///// Account to Credit Income Charge account
	public void ipsChargesAndFeeTran(String seqUniqueID, String remarks) {

		Optional<TransactionMonitor> TranMonitorRef = tranRep.findById(seqUniqueID);

		logger.info("Charges and fee");
		if (TranMonitorRef.isPresent()) {
			ResponseEntity<C24FTResponse> connect24Response = null;
			try {
				//// get Fees Amount from service and Fees Table
				BigDecimal tranAmt = ipsChargesAndFeeRep.findById(env.getProperty("charge.Tran")).get().getFees();

				if (tranAmt != null && Double.parseDouble(tranAmt.toString()) > 0) {
					logger.info("Charges and fee : Charges Applicable");
					connect24Response = connect24Service.ipsChargeAndFeeTran(TranMonitorRef.get(), remarks,
							tranAmt.toString(), sequence.generateSystemTraceAuditNumber(), remarks);

					///// Return Status Code 200 from Connect 24
					if (connect24Response.getStatusCode() == HttpStatus.OK) {
						logger.info("Charges and fee : Charges Debited Successfully");
						TransactionMonitor tranMonitor = TranMonitorRef.get();
						tranMonitor.setCharge_app_flg("Y");
						tranRep.save(tranMonitor);
					} else {
						logger.info("Charges and fee : Charges Not Debited /"
								+ connect24Response.getBody().getError_desc().get(0).toString());
						TransactionMonitor tranMonitor = TranMonitorRef.get();
						tranMonitor.setCharge_app_flg("N");
						tranRep.save(tranMonitor);
					}
				} else {
					logger.info("Charges and fee : Charges Not Applicable");
				}

			} catch (Exception e) {
				logger.info("Charges and Fee  : " + e.getLocalizedMessage());
			}
		}
	}

	public AccountContactResponse test() {
		// TODO Auto-generated method stub
		return connect24Service.test().getBody();
	}

	public String payableAccount(MCCreditTransferRequest mcCreditTransferRequest,String userID) {
		
		logger.info("Bal:"+mcCreditTransferRequest.getTrAmt());
		if(Double.parseDouble(mcCreditTransferRequest.getTrAmt())>0 && mcCreditTransferRequest.getTrAmt()!=null){
			logger.info("Bal:OK");
			ResponseEntity<C24FTResponse> connect24Response = connect24Service.payableSettl(
					sequence.generateSystemTraceAuditNumber(),"IPSTransaction BY Settlement Account",
					mcCreditTransferRequest,userID);
			if (connect24Response.getStatusCode() == HttpStatus.OK) {
				ipsDao.updateSettlAmtTablePayableFlg();
				return "Success";
			}else if(connect24Response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR){
				throw new ServerErrorException(SERVER_ERROR);
			}else {
				throw new Connect24Exception(errorCode.ErrorCode(connect24Response.getBody().getError()));
			}
		}else {
			logger.info("Bal:not");
			throw new Connect24Exception(errorCode.validationError("BIPS6"));

		}
		
	}

	public String receivableAccount(MCCreditTransferRequest mcCreditTransferRequest,String userID) {
		logger.info("Send message to Connect24");


		logger.info("Bal:" + mcCreditTransferRequest.getTrAmt());
		if (Double.parseDouble(mcCreditTransferRequest.getTrAmt()) > 0 && mcCreditTransferRequest.getTrAmt() != null) {
			logger.info("Bal:OK");
			ResponseEntity<C24FTResponse> connect24Response = connect24Service.receivableSettl(
					sequence.generateSystemTraceAuditNumber(), mcCreditTransferRequest.getTrAmt(), "IPSTransaction BY Settlement Account",
					mcCreditTransferRequest,userID);
			if (connect24Response.getStatusCode() == HttpStatus.OK) {
				ipsDao.updateSettlAmtTableReceivableFlg();
				return "Success";
			}else if(connect24Response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR){
				throw new ServerErrorException(SERVER_ERROR);
			}else {
				throw new Connect24Exception(errorCode.ErrorCode(connect24Response.getBody().getError()));
			}
		} else {
			logger.info("Bal:not");
			throw new Connect24Exception(errorCode.validationError("BIPS6"));
		}

	}
	
	/*public String expenseAccount(MCCreditTransferRequest mcCreditTransferRequest, String userID) {
		logger.info("Bal:"+mcCreditTransferRequest.getTrAmt());
		if(Double.parseDouble(mcCreditTransferRequest.getTrAmt())>0 && mcCreditTransferRequest.getTrAmt()!=null){
			logger.info("Bal:OK");
			ResponseEntity<C24FTResponse> connect24Response = connect24Service.expenseSettl(
					sequence.generateSystemTraceAuditNumber(),"IPSTransaction BY Settlement Account",
					mcCreditTransferRequest,userID);
			if (connect24Response.getStatusCode() == HttpStatus.OK) {
				ipsDao.updateSettlAmtTableExpenseFlg();
				return "Success";
			}else if(connect24Response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR){
				throw new ServerErrorException(SERVER_ERROR);
			}else {
				throw new Connect24Exception(errorCode.ErrorCode(connect24Response.getBody().getError()));
			}
		}else {
			logger.info("Bal:not");
			throw new Connect24Exception(errorCode.validationError("BIPS6"));

		}
	}

	public String incomeAccount(MCCreditTransferRequest mcCreditTransferRequest, String userID) {
		logger.info("Send message to Connect24");


		logger.info("Bal:" + mcCreditTransferRequest.getTrAmt());
		if (Double.parseDouble(mcCreditTransferRequest.getTrAmt()) > 0 && mcCreditTransferRequest.getTrAmt() != null) {
			logger.info("Bal:OK");
			ResponseEntity<C24FTResponse> connect24Response = connect24Service.incomeSettl(
					sequence.generateSystemTraceAuditNumber(), mcCreditTransferRequest.getTrAmt(), "IPSTransaction BY Settlement Account",
					mcCreditTransferRequest,userID);
			if (connect24Response.getStatusCode() == HttpStatus.OK) {
				ipsDao.updateSettlAmtTableIncomeFlg();
				return "Success";
			}else if(connect24Response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR){
				throw new ServerErrorException(SERVER_ERROR);
			}else {
				throw new Connect24Exception(errorCode.ErrorCode(connect24Response.getBody().getError()));
			}
		} else {
			logger.info("Bal:not");
			throw new Connect24Exception(errorCode.validationError("BIPS6"));
		}
	}*/
	
	public String payableAmt() {
		logger.info("Payable Amount");
		
		String trAmt="0";
		
		ResponseEntity<C24FTResponse> balannce = connect24Service
				.getBalance(settlAccountRep.findById(env.getProperty("settl.payable")).get().getAccount_number());

		if (balannce.getStatusCode() == HttpStatus.OK) {
			trAmt = balannce.getBody().getBalance().getAvailableBalance();
			logger.info("Payable Amount OK");
			return trAmt;
		} else {
			trAmt="0";
			logger.info("Payable Amount ERROR"+balannce.getStatusCodeValue());
			return trAmt;
		}

	}
	
	public String receivalbleAmt() {
		logger.info("receivable Amount");
		
		String trAmt="0";
		
		ResponseEntity<C24FTResponse> balannce = connect24Service
				.getBalance(settlAccountRep.findById(env.getProperty("settl.receivable")).get().getAccount_number());

		if (balannce.getStatusCode() == HttpStatus.OK) {
			trAmt = balannce.getBody().getBalance().getAvailableBalance();
			logger.info("receivable Amount OK");
			return trAmt;
		} else {
			logger.info("receivable Amount Error"+balannce.getStatusCodeValue());
			trAmt="0";
			return trAmt;
		}

	}
	
	public String expenseAmt() {
		logger.info("Expense Amount");
		
		String trAmt="0";
		
		ResponseEntity<C24FTResponse> balannce = connect24Service
				.getBalance(settlAccountRep.findById(env.getProperty("settl.expense")).get().getAccount_number());

		if (balannce.getStatusCode() == HttpStatus.OK) {
			trAmt = balannce.getBody().getBalance().getAvailableBalance();
			logger.info("Expense Amount OK");
			return trAmt;
		} else {
			trAmt="0";
			logger.info("Expense Amount ERROR"+balannce.getStatusCodeValue());
			return trAmt;
		}

	}
	
	public String incomeAmt() {
		logger.info("Income Amount");
		
		String trAmt="0";
		
		ResponseEntity<C24FTResponse> balannce = connect24Service
				.getBalance(settlAccountRep.findById(env.getProperty("settl.income")).get().getAccount_number());

		if (balannce.getStatusCode() == HttpStatus.OK) {
			trAmt = balannce.getBody().getBalance().getAvailableBalance();
			logger.info("Income Amount OK");
			return trAmt;
		} else {
			logger.info("Income Amount Error"+balannce.getStatusCodeValue());
			trAmt="0";
			return trAmt;
		}

	}

	
	///Wallet
	public WalletAccessResponse createWalletAccessID(String w_request_id, String psuDeviceID, String psuIPAddress,
			String psuID, String psuIDCountry, String psuIDType, WalletAccessRequest walletRequest) {
		WalletAccessResponse response=null;
		try {

			//// generate OTP
			String otp = sequence.generateOTP();
			//// generate Wallet ID
			String walletID =null;
			
			String walletAcctNo = null;

			WalletAccessTable AccountsExist1=ipsDao.checkReqAccountsExist1(psuID,walletRequest.getPhoneNumber());
			if(AccountsExist1.getWallet_acct_no()!=null) {
				walletAcctNo = AccountsExist1.getWallet_acct_no();
				walletID= AccountsExist1.getWallet_id();
			}else {
				walletAcctNo = sequence.generateWalletAccountNumber(walletRequest.getPhoneNumber().replaceAll("\\+", ""));
				walletID= sequence.generateWalletId();
			}
		    //// generate Wallet ID
			//// generate Auth ID
			String authID = sequence.generateAuthId();

			logger.info("Register Wallet Publickey data to table");

			//// update Table

			String status = ipsDao.registerWalletData(w_request_id,psuDeviceID, psuIPAddress, psuID,psuIDCountry,
					psuIDType,walletRequest,walletID, authID, otp,walletAcctNo);

			//// Send Response
			if (status.equals("0")) {
				
				/////check Validation with CBS
				/*if(checkValidation with CBS) {
					Move data to Original Table
				}else{
				    Update Error Status
				}*/
				
				////Check Existing Record
				
				WalletAccessResponse walletAccessResponse=ipsDao.checkReqAccountsExist(w_request_id,psuDeviceID, psuIPAddress, psuID,psuIDCountry,
						psuIDType,walletRequest,walletID, authID, otp,walletAcctNo);
				
				return walletAccessResponse;
								
				/*ResponseEntity<AccountContactResponse> connect24ResponseAccContactExist = connect24Service
						.getAccountContact(psuDeviceID, psuIPAddress, psuID, consentRequest.getAccount().get(0).getIdentification());

				if (connect24ResponseAccContactExist.getStatusCode() == HttpStatus.OK) {

					if (connect24ResponseAccContactExist.getBody().getStatus().equals("0")) {

						if (connect24ResponseAccContactExist.getBody().getSchmType().equals("SBA")
								|| connect24ResponseAccContactExist.getBody().getSchmType().equals("CAA")
								|| connect24ResponseAccContactExist.getBody().getSchmType().equals("ODA")) {

							if (connect24ResponseAccContactExist.getBody().getAccountStatus().equals("A")) {

								if (connect24ResponseAccContactExist.getBody().getCurrencyCode().equals(env.getProperty("cim.crncycode"))) {

									if (connect24ResponseAccContactExist.getBody().getFrezCode().equals("T")
											|| connect24ResponseAccContactExist.getBody().getFrezCode().equals("C")
											|| connect24ResponseAccContactExist.getBody().getFrezCode().equals("D")) {
										ipsDao.updatePublicKeyAccountStatus(authID,
												errorCode.ErrorCodeRegistration("10"));
										throw new IPSXRestException(errorCode.ErrorCodeRegistration("10"));
									} else {
										if (connect24ResponseAccContactExist.getBody().getDocNumber().equals(psuID)
												&& connect24ResponseAccContactExist.getBody().getPhoneNumber()
														.equals(consentRequest.getPhoneNumber())) {

											//// Send Response

											response = new ConsentAccessResponse();
											response.setConsentID(consentID);
											response.setStatus(TranMonitorStatus.AwaitingAuthorisation.toString());
											response.setStatusUpdateDateTime(listener.getxmlGregorianCalender("0"));
											response.setCreationDateTime(listener.getxmlGregorianCalender("0"));
							
											response.setPermissions(consentRequest.getPermissions());
											response.setExpirationDateTime(consentRequest.getExpirationDateTime());
											response.setTransactionFromDateTime(consentRequest.getTransactionFromDateTime());
											response.setTransactionToDateTime(consentRequest.getTransactionToDateTime());
											
											Links links = new Links();
											links.setSelf("/accounts-consents/" + consentID);
											links.setAuthoriseTransaction(
													"/accounts-consents/" + consentID + "/authorisations/" + authID);
											response.setLinks(links);
											
											/////Calling OTP Server
											logger.info("Calling OTP server ");
											String msgResponse=sequence.sms(otp, consentRequest.getPhoneNumber());
											logger.info("OTP Response:"+msgResponse);
											return response;

										} else {
											ipsDao.updatePublicKeyAccountStatus(authID,
													errorCode.ErrorCodeRegistration("8"));
											throw new IPSXRestException(errorCode.ErrorCodeRegistration("8"));
										}
									}

									//// Check Customer Data exist

								} else {
									ipsDao.updatePublicKeyAccountStatus(authID, errorCode.ErrorCodeRegistration("7"));
									throw new IPSXRestException(errorCode.ErrorCodeRegistration("7"));
								}

							} else {
								if (connect24ResponseAccContactExist.getBody().getAccountStatus().equals("C")) {
									ipsDao.updatePublicKeyAccountStatus(authID, errorCode.ErrorCodeRegistration("9"));
									throw new IPSXRestException(errorCode.ErrorCodeRegistration("9"));
								} else {
									ipsDao.updatePublicKeyAccountStatus(authID, errorCode.ErrorCodeRegistration("10"));
									throw new IPSXRestException(errorCode.ErrorCodeRegistration("10"));
								}

							}
						} else {
							ipsDao.updatePublicKeyAccountStatus(authID, errorCode.ErrorCodeRegistration("7"));
							throw new IPSXRestException(errorCode.ErrorCodeRegistration("7"));
						}

					} else {
						ipsDao.updatePublicKeyAccountStatus(authID, errorCode.ErrorCodeRegistration("7"));
						throw new IPSXRestException(errorCode.ErrorCodeRegistration("7"));
					}

				} else {
					ipsDao.updatePublicKeyAccountStatus(authID, errorCode.ErrorCodeRegistration("0"));
					throw new IPSXRestException(errorCode.ErrorCodeRegistration("0"));

				}*/

			} else {
				throw new IPSXException(errorCode.ErrorCodeRegistration("0"));
			}

		} catch (HttpClientErrorException e) {
			logger.error(e.getMessage());
			throw new IPSXException(errorCode.ErrorCodeRegistration("0"));
		}

	}

	public SCAAthenticationResponse authWalletAccessID(String w_request_id, String psuDeviceID, String psuIPAddress,
			String psuID, String psuIDCountry, String psuIDType, SCAAuthenticatedData scaAuthenticatedData,
			String walletID, String authID) {
		SCAAthenticationResponse response = ipsDao.updateSCAwalletAccess(scaAuthenticatedData, walletID, authID);
		return response;
	}

	public WalletFndTransferResponse walletFundTransferTopupConnection(String w_request_id, String psuDeviceID,
			String psuIPAddress, String psuID, String psuIDCountry, String psuIDType, String walletID,
			WalletFndTransferRequest walletFndTransferRequest) {
		
		
		String response = ipsDao.RegisterWalletTopupData(w_request_id, psuDeviceID,
				psuIPAddress, psuID, psuIDCountry, psuIDType, walletID,walletFndTransferRequest);
		
		
		String res=ipsDao.RegisterFundTRansferTopUpData(w_request_id,psuDeviceID,psuID,psuIPAddress,walletFndTransferRequest);
		

		if(!response.equals("0")) {
			
			boolean walletIDExist=ipsDao.walletIDExist(walletID);
			
			
			if(walletIDExist) {

				if (Double.parseDouble(walletFndTransferRequest.getTrAmt()) <= 10000) {
					if(Double.parseDouble(walletFndTransferRequest.getTrAmt())<2000) {
						ipsDao.updateWalletRegisterTranResponseSuccess(w_request_id,"SUCCESS");
						ipsDao.updateWalletBalance(walletID,"DEP",walletFndTransferRequest.getTrAmt());
						ipsDao.updateFundTransferDataResponseSuccess(w_request_id,"SUCCESS","");

						WalletFndTransferResponse walletResponse=new WalletFndTransferResponse();
						walletResponse = new WalletFndTransferResponse(w_request_id,
								new SimpleDateFormat("YYYY-MM-dd HH:mm:ss ").format(new Date()));

						return walletResponse;
					}else {
						ipsDao.updateWalletRegisterTranResponseFailure(w_request_id,"FAILURE","Insufficient Fund");
						throw new IPSXException(errorCode.ErrorCodeRegistration("21"));
					}
				}else {
					ipsDao.updateWalletRegisterTranResponseFailure(w_request_id,"FAILURE","Maximum Amount Not Permitted(must be less than or equal 10000 Only) ");
					throw new IPSXException(errorCode.ErrorCodeRegistration("22"));
				}
				
				
			}else {
				throw new IPSXException(errorCode.ErrorCodeRegistration("0"));
			}
			
		}else {
			throw new IPSXException(errorCode.ErrorCodeRegistration("0"));
		}
		

	}

	public WalletFndTransferResponse walletFundTransferConnection(String w_request_id, String psuDeviceID,
			String psuIPAddress, String psuID, String psuIDCountry, String psuIDType, String walletID,
			WalletFndTransferRequest walletFndTransferRequest,String tranTypeCode) {
		
		String response = ipsDao.RegisterWalletTranData(w_request_id, psuDeviceID,
				psuIPAddress, psuID, psuIDCountry, psuIDType, walletID,walletFndTransferRequest,tranTypeCode);

		String res=ipsDao.RegisterFundTRansferData(w_request_id,psuDeviceID,psuID,psuIPAddress,walletFndTransferRequest,tranTypeCode);

		if (!response.equals("0")) {

			boolean walletIDExist = ipsDao.walletIDExist(walletID);

			
			if (walletIDExist) {
				
				if (Double.parseDouble(walletFndTransferRequest.getTrAmt()) <= 10000) {
						ipsDao.updateWalletRegisterTranResponseSuccess(w_request_id,"SUCCESS");
						ipsDao.updateWalletBalance(walletID,"WDL",walletFndTransferRequest.getTrAmt());
						ipsDao.updateFundTransferDataResponseSuccess(w_request_id,"SUCCESS","");

						WalletFndTransferResponse walletResponse=new WalletFndTransferResponse();
						walletResponse = new WalletFndTransferResponse(w_request_id,
								new SimpleDateFormat("YYYY-MM-dd HH:mm:ss ").format(new Date()));

						return walletResponse;
					
				}else {
					ipsDao.updateWalletRegisterTranResponseFailure(w_request_id,"FAILURE","Maximum Amount Not Permitted(must be less than or equal 10000 Only) ");
					throw new IPSXException(errorCode.ErrorCodeRegistration("22"));
				}
							

			} else {
				throw new IPSXException(errorCode.ErrorCodeRegistration("0"));
			}
			
		}else {
			throw new IPSXException(errorCode.ErrorCodeRegistration("0"));
		}
	}

	public WalletBalanceResponse walletBalnceResponse(String w_request_id, String psuDeviceID, String psuIPAddress,
			String psuID, String psuIDCountry, String psuIDType, String walletID) {
		WalletBalanceResponse response=new WalletBalanceResponse();
		
		boolean walletIDExist = ipsDao.walletIDExist(walletID);

		if (walletIDExist) {
			
			WalletAccessTable walletAccountData=ipsDao.getWalletData(walletID);
			
			if(walletAccountData.getWallet_acct_no()!=null) {
				C24FTResponseBalance Balance=new C24FTResponseBalance();
				DecimalFormat df = new DecimalFormat("#0.00");
				Balance.setAvailableBalance( df.format(walletAccountData.getWallet_balance()).toString());
				Balance.setFFTBalance("0.00");
				Balance.setFloatBalance("0.00");
				Balance.setUserDefBalance("0.00");
				Balance.setLedgeBalance("0.00");
				response.setBalance(Balance);
				response.setWalletAcctNumber(walletAccountData.getWallet_acct_no());
				return response;
			}else {
				throw new IPSXException(errorCode.ErrorCodeRegistration("0"));
			}
			
		} else {
			throw new IPSXException(errorCode.ErrorCodeRegistration("0"));
		} 
		
	}

	public WalletStatementResponse walletStatement(String w_request_id, String psuDeviceID, String psuIPAddress,
			String psuID, String psuIDCountry, String psuIDType, String walletID) {
		WalletStatementResponse response=new WalletStatementResponse();
		
		boolean walletIDExist = ipsDao.walletIDExist(walletID);

		if (walletIDExist) {
			
			WalletAccessTable walletAccountData=ipsDao.getWalletData(walletID);
			
			if(walletAccountData.getWallet_acct_no()!=null) {
				
				List<WalletRequestRegisterTable> list=ipsDao.getWalletStatement(walletID);
				
				List<WalletStatement> walletStatementList=new ArrayList<>();
				if(list.size()>0) {
					for(WalletRequestRegisterTable wallet:list) {
						WalletStatement walletStatement=new WalletStatement();
						walletStatement.setTranDate( new SimpleDateFormat("dd/MM/yyyy").format(wallet.getTran_date()));
						if(wallet.getTran_type().equals("DEP")) {
							walletStatement.setCreditDebitIndicator("C");
						}else {
							walletStatement.setCreditDebitIndicator("D");
						}
						walletStatement.setTrAmt(wallet.getTran_amount().toString());
						walletStatement.setTranParticulars(wallet.getTran_remarks());
						walletStatement.setTranStatus(wallet.getTran_status());
						walletStatementList.add(walletStatement);
						
					}
					response.setWalletStatement(walletStatementList);
				}
				
				DecimalFormat df = new DecimalFormat("#0.00");
				response.setWalletBalance(df.format(walletAccountData.getWallet_balance()).toString());
				response.setWalletAccountNumber(walletAccountData.getWallet_acct_no());
				return response;
			}else {
				throw new IPSXException(errorCode.ErrorCodeRegistration("0"));
			}
			
		} else {
			throw new IPSXException(errorCode.ErrorCodeRegistration("0"));
		}

	}

	public McConsentOutwardAccessResponse outwardConsentAccess(String x_request_id, String psuDeviceID, String psuIPAddress,
			String psuID, String psuIDCountry, String psuIDType,
			ConsentOutwardAccessRequest consentOutwardAccessRequest) throws Exception {
		

		McConsentOutwardAccessResponse response=null;
		try {

			////Sender Participant BIC
			String sender_participant_bic=env.getProperty("ipsx.bicfi");
			////Sender Participant Member ID
			String sender_participant_member_id="";
			////Receiver Participant BIC
			String receiver_participant_bic=ipsDao.getOtherBankAgent(consentOutwardAccessRequest.getBankCode()).getBank_agent();
			////Receiver Participant Member ID
			String receiver_participant_member_id="";
			
			logger.debug("Calling Outward Consent Access Connection");
			
			
			////Generate Custom Device ID
			String customDeviceID=sequence.generateCustomDeviceID( psuDeviceID.concat(psuID).concat(consentOutwardAccessRequest.getAccounts().getAccountNumber()));


			////Generate KeyPair 
			KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");
			keyGenerator.initialize(1024);
			KeyPair kp = keyGenerator.genKeyPair();
			/////Generate Public Key
			String publicKey=generatePublicKey(kp);
			/////Generate Private Key
			String privateKey=generatePrivateKey(kp);

			///Store KeyPair
			//listener.storePubKey(kp, customDeviceID);
			//String encryptPublicKey=listener.encrypt(publicKey,customDeviceID);
			//String encryptPrivateKey=listener.encrypt(privateKey,customDeviceID);
			//String encryptCustomDevieID=listener.encryptDid(customDeviceID);
			
			String res = ipsDao.consentOutwardDataRegister(x_request_id, psuDeviceID, psuIPAddress, psuID, psuIDCountry,
					psuIDType, sender_participant_bic, sender_participant_member_id, receiver_participant_bic,
					receiver_participant_member_id, "", "");
			
			if (res.equals("0")) {
				String status = ipsDao.outwardConsentDataRegister(x_request_id, psuDeviceID,
						psuIPAddress, psuID, psuIDCountry, psuIDType, consentOutwardAccessRequest, sender_participant_bic,sender_participant_member_id,
						receiver_participant_bic, receiver_participant_member_id,publicKey,privateKey,customDeviceID);

				logger.debug("Calling Outward Consent Access Connection Register Status"+status);

				//// Send Response
				if (status.equals("0")) {
					
					logger.debug("Calling Outward Consent Access IPSXService");

					ResponseEntity<ConsentAccessResponse> accountConsentResponse = consentIPSXservice
							.accountConsent(x_request_id, psuDeviceID,
									psuIPAddress, psuID, psuIDCountry, psuIDType, consentOutwardAccessRequest, sender_participant_bic,sender_participant_member_id,
									receiver_participant_bic, receiver_participant_member_id,publicKey, customDeviceID);

					if (accountConsentResponse.getStatusCode() == HttpStatus.OK) {
						logger.debug("Response:"+accountConsentResponse.getBody().toString());

						ConsentAccessResponse consentAccessResponse =accountConsentResponse.getBody(); 
						ipsDao.outwardConsentDataRegisterUpdateResponse(x_request_id,consentAccessResponse);
						
						
						////Generate Consent Access Reponse
						response=new McConsentOutwardAccessResponse();
						response.setConsentID(consentAccessResponse.getConsentID());
						response.setStatus(consentAccessResponse.getStatus());
						response.setPermissions(consentAccessResponse.getPermissions());
						
						Links links=new Links();
						links.setSelf("/api/ws/accounts-consents");
						links.setAuthoriseTransaction("/api/ws/accounts-consents/"+consentAccessResponse.getConsentID()+"/authorisations/"+
								consentAccessResponse.getLinks().getAuthoriseTransaction().substring
								(consentAccessResponse.getLinks().getAuthoriseTransaction().lastIndexOf("/")+1));
						
						response.setLinks(links);
						
						return response;
						
						
					} else if(accountConsentResponse.getStatusCode() == HttpStatus.BAD_REQUEST){
						logger.debug("Response:"+accountConsentResponse.getBody().toString());

						ErrorRestResponse errorRestResponse = new ErrorRestResponse(accountConsentResponse.getBody().getErrorCode(),
								accountConsentResponse.getBody().getDescription());
						ipsDao.outwardConsentDataRegisterUpdateErrorResponse(x_request_id,errorRestResponse);

						throw new IPSXException(errorRestResponse.getErrorCode().toString()
								+":"+errorRestResponse.getDescription());
					}else if(accountConsentResponse.getStatusCode() == HttpStatus.UNAUTHORIZED){
						logger.debug("Response:"+accountConsentResponse.getBody().toString());

						ErrorRestResponse errorRestResponse = new ErrorRestResponse(accountConsentResponse.getBody().getErrorCode(),
								accountConsentResponse.getBody().getDescription());
						ipsDao.outwardConsentDataRegisterUpdateErrorResponse(x_request_id,errorRestResponse);

						throw new IPSXException(accountConsentResponse.getBody().getErrorCode().toString()+":"+accountConsentResponse.getBody().getDescription());
					}else {
						logger.debug("Response:"+accountConsentResponse.getBody().toString());

						ErrorRestResponse errorRestResponse = new ErrorRestResponse(accountConsentResponse.getBody().getErrorCode(),
								accountConsentResponse.getBody().getDescription());
						ipsDao.outwardConsentDataRegisterUpdateErrorResponse(x_request_id,errorRestResponse);

						throw new IPSXException(accountConsentResponse.getBody().getErrorCode().toString()+":"+accountConsentResponse.getBody().getDescription());
					}

				} else {
					throw new IPSXException(errorCode.ErrorCodeRegistration("0"));
				}
			}else {
				throw new IPSXException(errorCode.ErrorCodeRegistration("0"));
			}


		} catch (HttpClientErrorException e) {
			logger.error(e.getMessage());
			throw new IPSXException(errorCode.ErrorCodeRegistration("0"));
		}
		
	}
	
	
	public ConsentOutwardAccessAuthResponse outwardConsentAccessSCAAuth(String x_request_id, String psuDeviceID,
			String psuIPAddress, String psuID, String psuIDCountry, String psuIDType,
			ConsentOutwardAccessAuthRequest consentOutwardAccessAuthRequest, String consentID, String authID) throws Exception {
		
		
		
		List<ConsentOutwardAccessTmpTable>  consentDataList=ipsDao.getConsentTmpData(consentID);
		
		logger.debug("Calling Outward Consent Access Authorisation");

		
		if(consentDataList.size()>0) {
			String sender_participant_bic=consentDataList.get(0).getSenderparticipant_bic();
			String sender_participant_member_id=consentDataList.get(0).getSenderparticipant_memberid();
			String receiver_participant_bic=consentDataList.get(0).getReceiverparticipant_bic();
			String receiver_participant_member_id=consentDataList.get(0).getReceiverparticipant_memberid();
			
			String res = ipsDao.consentOutwardDataAuthorisation(x_request_id, psuDeviceID, psuIPAddress, psuID, psuIDCountry,
					psuIDType, sender_participant_bic, sender_participant_member_id, receiver_participant_bic,
					receiver_participant_member_id, consentID, "");
			if (res.equals("0")) {
				String status = ipsDao.outwardConsentDataRegisterAuth(x_request_id, psuDeviceID,
						psuIPAddress, psuID, psuIDCountry, psuIDType, consentOutwardAccessAuthRequest, sender_participant_bic,sender_participant_member_id,
						receiver_participant_bic, receiver_participant_member_id,consentID,authID);
				
				logger.debug("Calling Outward Consent Access Authorisation Register status"+status);

				if(status.equals("0")) {

					////Create Cryptogram Data
					Map<String, Object> claimsData = new HashMap<String, Object>();
					//claimsData.put("deviceId",listener.decryptDid(consentDataList.get(0).getCustom_device_id()));
					claimsData.put("deviceId",consentDataList.get(0).getCustom_device_id());

					claimsData.put("endToEndId",x_request_id);
					claimsData.put("consentId", consentID);
					claimsData.put("href","/accounts-consents/"+consentID+"/authorisations/"+authID );
			        
					//String cryptogram=generateJwtToken(claimsData,listener.decrypt(consentDataList.get(0).getPrivate_key(),listener.decryptDid(consentDataList.get(0).getCustom_device_id())));
					//String cryptogram=generateJwtToken(claimsData,listener.retrievePriKey(consentDataList.get(0).getCustom_device_id()));
					String cryptogram=generateJwtToken(claimsData,consentDataList.get(0).getPrivate_key());

					logger.debug("Calling Outward Consent Access Authorisation IPSXService");

					ResponseEntity<SCAAthenticationResponse> accountConsentResponse = consentIPSXservice
							.accountConsentAuthorisation(x_request_id, psuDeviceID,
									psuIPAddress, psuID, psuIDCountry, psuIDType, consentOutwardAccessAuthRequest, sender_participant_bic,sender_participant_member_id,
									receiver_participant_bic, receiver_participant_member_id,cryptogram,consentID,authID,consentDataList.get(0).getCustom_device_id());
					
					if (accountConsentResponse.getStatusCode() == HttpStatus.OK) {
						SCAAthenticationResponse consentAccessResponse =accountConsentResponse.getBody();
						ipsDao.outwardConsentDataRegisterAuthUpdateResponse(x_request_id,consentAccessResponse.getSCAStatus());
						
						logger.debug("Response:"+consentAccessResponse.toString());
						
						if(consentAccessResponse.getSCAStatus().equals("finalised")){

							////Move Data to org Table
							ConsentOutwardAccessAuthResponse response=ipsDao.updateOutwardSCAAuth(x_request_id, psuDeviceID,
									psuIPAddress, psuID, psuIDCountry, psuIDType, consentOutwardAccessAuthRequest, sender_participant_bic,sender_participant_member_id,
									receiver_participant_bic, receiver_participant_member_id,consentID,authID,consentAccessResponse);
							
							return response;
							
						} else {
							logger.debug("Response:"+consentAccessResponse.toString());

							ConsentOutwardAccessAuthResponse response=new ConsentOutwardAccessAuthResponse();
							
							response.setSCAStatus(consentAccessResponse.getSCAStatus());

							Links links = new Links();
							links.setSCAStatus("/api/ws/account-consents/" + consentID + "/authorisations/"
									+ consentAccessResponse.getLinks().getAuthoriseTransaction().substring(
											consentAccessResponse.getLinks().getAuthoriseTransaction().lastIndexOf("/")+ 1));

							response.setLinks(links);
							
							return response;
						}
						
						
					} else if(accountConsentResponse.getStatusCode() == HttpStatus.BAD_REQUEST){
						logger.debug("Response:"+accountConsentResponse.getBody().toString());

						ErrorRestResponse errorRestResponse = new ErrorRestResponse(accountConsentResponse.getBody().getErrorCode(),
								accountConsentResponse.getBody().getDescription());
						ipsDao.outwardConsentDataRegisterAuthUpdateErrorResponse(x_request_id,accountConsentResponse.getBody().getErrorCode()+":"+
								accountConsentResponse.getBody().getDescription());

						ipsDao.outwardConsentDataRegisterUpdateErrorResponse(x_request_id,errorRestResponse);
						throw new IPSXException(errorRestResponse.getErrorCode()+":"+errorRestResponse.getDescription());
					}else if(accountConsentResponse.getStatusCode() == HttpStatus.UNAUTHORIZED){
						logger.debug("Response:"+accountConsentResponse.getBody().toString());

						ErrorRestResponse errorRestResponse = new ErrorRestResponse(accountConsentResponse.getBody().getErrorCode(),
								accountConsentResponse.getBody().getDescription());
						ipsDao.outwardConsentDataRegisterUpdateErrorResponse(x_request_id,errorRestResponse);
						ipsDao.outwardConsentDataRegisterAuthUpdateErrorResponse(x_request_id,accountConsentResponse.getBody().getErrorCode()+":"+
								accountConsentResponse.getBody().getDescription());

						throw new IPSXException(accountConsentResponse.getBody().getErrorCode().toString()+":"+accountConsentResponse.getBody().getDescription());
					}else {
						logger.debug("Response:"+accountConsentResponse.getBody().toString());

						ErrorRestResponse errorRestResponse = new ErrorRestResponse(accountConsentResponse.getBody().getErrorCode(),
								accountConsentResponse.getBody().getDescription());
						ipsDao.outwardConsentDataRegisterUpdateErrorResponse(x_request_id,errorRestResponse);
						ipsDao.outwardConsentDataRegisterAuthUpdateErrorResponse(x_request_id,accountConsentResponse.getBody().getErrorCode()+":"+
								accountConsentResponse.getBody().getDescription());

						throw new IPSXException(accountConsentResponse.getBody().getErrorCode().toString()+":"+accountConsentResponse.getBody().getDescription());
					}
				}else {
					throw new IPSXException(errorCode.ErrorCodeRegistration("0"));
				}
			}else {
				throw new IPSXException(errorCode.ErrorCodeRegistration("0"));
			}

		}else {
			throw new IPSXException(errorCode.ErrorCodeRegistration("13"));
		}
		
	}
	
	public ErrorRestResponse outwardDeleteConsentAccess(String x_request_id, String psuDeviceID, String psuIPAddress, String psuID,
			String psuIDCountry, String psuIDType, String consentID) {
		List<ConsentOutwardAccessTable> consentDataList = ipsDao.getConsentData(consentID);

		logger.debug("Calling Outward Delete Consent Access ");

		if (consentDataList.size() > 0) {
			
			String sender_participant_bic=consentDataList.get(0).getSenderparticipant_bic();
			String sender_participant_member_id=consentDataList.get(0).getSenderparticipant_memberid();
			String receiver_participant_bic=consentDataList.get(0).getReceiverparticipant_bic();
			String receiver_participant_member_id=consentDataList.get(0).getReceiverparticipant_memberid();
			
			String res = ipsDao.consentOutwardDataDelete(x_request_id, psuDeviceID, psuIPAddress, psuID, psuIDCountry,
					psuIDType, sender_participant_bic, sender_participant_member_id, receiver_participant_bic,
					receiver_participant_member_id, consentID, "");
			if (res.equals("0")) {
				ResponseEntity<ErrorRestResponse> accountConsentResponse = consentIPSXservice
						.deleteAccountConsent(x_request_id, psuDeviceID,
								psuIPAddress, psuID, psuIDCountry, psuIDType, sender_participant_bic,sender_participant_member_id,
								receiver_participant_bic, receiver_participant_member_id,consentID,consentDataList.get(0).getCustom_device_id());
				
				if (accountConsentResponse.getStatusCode() == HttpStatus.OK) {
					ipsDao.outwardConsentDataRegisterDeleteResponse(consentID);
					
					ErrorRestResponse eer=new ErrorRestResponse(000,"Consent Deleted Successfully");
					return eer;
				} else if(accountConsentResponse.getStatusCode() == HttpStatus.BAD_REQUEST){
					ErrorRestResponse errorRestResponse = accountConsentResponse.getBody();
					throw new IPSXException(errorRestResponse.getErrorCode().toString()+":"+errorRestResponse.getDescription());
				}else{
					ErrorRestResponse errorRestResponse = accountConsentResponse.getBody();
					throw new IPSXException(errorRestResponse.getErrorCode().toString()+":"+errorRestResponse.getDescription());
				}
			}else {
				throw new IPSXException(errorCode.ErrorCodeRegistration("13"));

			}
			
		}else {
			throw new IPSXException(errorCode.ErrorCodeRegistration("13"));
		}
	}
	
	
	public ConsentAccountBalance outwardConsentAccessBalances(String x_request_id, String psuDeviceID, String psuIPAddress,
			String psuID, String psuIDCountry, String psuIDType, String consentID, String accountID) throws Exception {
		List<ConsentOutwardAccessTable> consentDataList = ipsDao.getConsentData(consentID);

		logger.debug("Calling Outward Balance Consent Access "+consentDataList.size());

		logger.debug("Calling Outward Balance Consent Access ");
		if(consentDataList.size()>0) {
			///Sender Participant BIC
			String sender_participant_bic=consentDataList.get(0).getSenderparticipant_bic();
			String sender_participant_member_id=consentDataList.get(0).getSenderparticipant_memberid();
			String receiver_participant_bic=consentDataList.get(0).getReceiverparticipant_bic();
			String receiver_participant_member_id=consentDataList.get(0).getReceiverparticipant_memberid();
			
		////Create Cryptogram Data
			Map<String, Object> claimsData = new HashMap<String, Object>();
			claimsData.put("deviceId",consentDataList.get(0).getCustom_device_id() );
			claimsData.put("endToEndId",x_request_id);
			claimsData.put("consentId", consentID);
			claimsData.put("href","/accounts/"+accountID+"/balances" );
			
			String cryptogram=generateJwtToken(claimsData,consentDataList.get(0).getPrivate_key());
			//String cryptogram=generateJwtToken(claimsData,listener.retrievePriKey(consentDataList.get(0).getCustom_device_id()));

			
			
			String status = ipsDao.outwardConsentDataRegisterBalances(x_request_id, psuDeviceID,
					psuIPAddress, psuID, psuIDCountry, psuIDType, sender_participant_bic,sender_participant_member_id,
					receiver_participant_bic, receiver_participant_member_id,consentID,accountID);
			
			logger.debug("Calling Outward Consent Access Balance Register status"+status);

			if(status.equals("0")) {
				ResponseEntity<ConsentAccountBalance> accountConsentResponse = consentIPSXservice
						.accountConsentBalances(x_request_id, consentDataList.get(0).getCustom_device_id(),
								psuIPAddress, psuID, psuIDCountry, psuIDType, sender_participant_bic,sender_participant_member_id,
								receiver_participant_bic, receiver_participant_member_id,consentID,accountID,cryptogram);
				
				if (accountConsentResponse.getStatusCode() == HttpStatus.OK) {
					ConsentAccountBalance consentAccessResponse =accountConsentResponse.getBody();
					ipsDao.accountConsentBalancesResponse(x_request_id,"SUCCESS","");
					
					return consentAccessResponse;
				}  else if(accountConsentResponse.getStatusCode() == HttpStatus.BAD_REQUEST){
					logger.debug("Response:"+accountConsentResponse.getBody().toString());

					ErrorRestResponse errorRestResponse = new ErrorRestResponse(accountConsentResponse.getBody().getErrorCode(),
							accountConsentResponse.getBody().getDescription());
					ipsDao.accountConsentBalancesResponse(x_request_id,"FAILURE",errorRestResponse.getErrorCode()+":"+errorRestResponse.getDescription());

					throw new IPSXException(errorRestResponse.getErrorCode().toString()+":"+errorRestResponse.getDescription());
				}else if(accountConsentResponse.getStatusCode() == HttpStatus.UNAUTHORIZED){
					logger.debug("Response:"+accountConsentResponse.getBody().toString());

					ErrorRestResponse errorRestResponse = new ErrorRestResponse(accountConsentResponse.getBody().getErrorCode(),
							accountConsentResponse.getBody().getDescription());
					ipsDao.accountConsentBalancesResponse(x_request_id,"FAILURE",errorRestResponse.getErrorCode()+":"+errorRestResponse.getDescription());

					throw new IPSXException(accountConsentResponse.getBody().getErrorCode().toString()+":"+accountConsentResponse.getBody().getDescription());
				}else {
					logger.debug("Response:"+accountConsentResponse.getBody().toString());

					ErrorRestResponse errorRestResponse = new ErrorRestResponse(accountConsentResponse.getBody().getErrorCode(),
							accountConsentResponse.getBody().getDescription());
					ipsDao.accountConsentBalancesResponse(x_request_id,"FAILURE",errorRestResponse.getErrorCode()+":"+errorRestResponse.getDescription());

					throw new IPSXException(accountConsentResponse.getBody().getErrorCode().toString()+":"+accountConsentResponse.getBody().getDescription());
				}

			}else {
				throw new IPSXException(errorCode.ErrorCodeRegistration("0"));
			}
	
		}else {
			throw new IPSXException(errorCode.ErrorCodeRegistration("13"));
		}
	}
	
	public TransactionListResponse outwardConsentAccessTransactionInc(String x_request_id, String psuDeviceID,
			String psuIPAddress, String psuID, String psuIDCountry, String psuIDType, String consentID,
			String accountID, String fromBookingDateTime, String toBookingDateTime) throws Exception {
		List<ConsentOutwardAccessTable> consentDataList = ipsDao.getConsentData(consentID);

		logger.debug("Calling Outward Consent Access Trancation Inquiry");
		if(consentDataList.size()>0) {
			String sender_participant_bic=consentDataList.get(0).getSenderparticipant_bic();
			String sender_participant_member_id=consentDataList.get(0).getSenderparticipant_memberid();
			String receiver_participant_bic=consentDataList.get(0).getReceiverparticipant_bic();
			String receiver_participant_member_id=consentDataList.get(0).getReceiverparticipant_memberid();
			
	
		////Create Cryptogram Data
			Map<String, Object> claimsData = new HashMap<String, Object>();
			claimsData.put("deviceId",consentDataList.get(0).getCustom_device_id() );
			claimsData.put("endToEndId",x_request_id);
			claimsData.put("consentId", consentID);
			
			logger.debug(""+"/accounts/"+accountID+"/transactions?fromBookingDateTime="+fromBookingDateTime+"&toBookingDateTime="+toBookingDateTime );
			if(fromBookingDateTime!=null && toBookingDateTime!=null) {
				claimsData.put("href","/accounts/"+accountID+"/transactions" );
			}else {
				claimsData.put("href","/accounts/"+accountID+"/transactions" );
			}
			
			String cryptogram=generateJwtToken(claimsData,consentDataList.get(0).getPrivate_key());
			//String cryptogram=generateJwtToken(claimsData,listener.retrievePriKey(consentDataList.get(0).getCustom_device_id()));

			String status = ipsDao.outwardConsentDataRegisterTransactionInc(x_request_id, psuDeviceID,
					psuIPAddress, psuID, psuIDCountry, psuIDType, sender_participant_bic,sender_participant_member_id,
					receiver_participant_bic, receiver_participant_member_id,consentID,accountID);
			
			logger.debug("Calling Outward Consent Access Balance Register status"+status);

			if(status.equals("0")) {
				ResponseEntity<TransactionListResponse> accountConsentResponse = consentIPSXservice
						.accountConsentTransaction(x_request_id, consentDataList.get(0).getCustom_device_id(),
								psuIPAddress, psuID, psuIDCountry, psuIDType, sender_participant_bic,sender_participant_member_id,
								receiver_participant_bic, receiver_participant_member_id,consentID,accountID,cryptogram,fromBookingDateTime,toBookingDateTime);
				
				if (accountConsentResponse.getStatusCode() == HttpStatus.OK) {
					TransactionListResponse consentAccessResponse =accountConsentResponse.getBody();
					ipsDao.accountConsentBalancesResponse(x_request_id,"SUCCESS","");
					
					return consentAccessResponse;
				} else if(accountConsentResponse.getStatusCode() == HttpStatus.BAD_REQUEST){
					ErrorRestResponse errorRestResponse = new ErrorRestResponse(accountConsentResponse.getBody().getErrorCode(),
							accountConsentResponse.getBody().getDescription());
					
					ipsDao.accountConsentTransactionIncResponse(x_request_id,"FAILURE",errorRestResponse.getErrorCode()+":"+errorRestResponse.getDescription());

					throw new IPSXException(errorRestResponse.getErrorCode()+":"+errorRestResponse.getDescription());
				}else{
					ErrorRestResponse errorRestResponse = new ErrorRestResponse(accountConsentResponse.getBody().getErrorCode(),
							accountConsentResponse.getBody().getDescription());
					
					ipsDao.accountConsentTransactionIncResponse(x_request_id,"FAILURE",errorRestResponse.getErrorCode()+":"+errorRestResponse.getDescription());
					throw new IPSXException(errorRestResponse.getErrorCode()+":"+errorRestResponse.getDescription());
				}
			}else {
				throw new IPSXException(errorCode.ErrorCodeRegistration("0"));
			}
	
		}else {
			throw new IPSXException(errorCode.ErrorCodeRegistration("13"));
		}

	}
	
	
	public AccountListResponse outwardConsentAccessAccountListInc(String x_request_id, String psuDeviceID,
			String psuIPAddress, String psuID, String psuIDCountry, String psuIDType, String consentID) throws Exception {
		List<ConsentOutwardAccessTable> consentDataList = ipsDao.getConsentData(consentID);

		logger.debug("Calling Outward Consent Access Account List Inquiry");
		if(consentDataList.size()>0) {
			String sender_participant_bic=consentDataList.get(0).getSenderparticipant_bic();
			String sender_participant_member_id=consentDataList.get(0).getSenderparticipant_memberid();
			String receiver_participant_bic=consentDataList.get(0).getReceiverparticipant_bic();
			String receiver_participant_member_id=consentDataList.get(0).getReceiverparticipant_memberid();
			
		////Create Cryptogram Data
		
			
			Map<String, Object> claimsData = new HashMap<String, Object>();
			claimsData.put("deviceId",consentDataList.get(0).getCustom_device_id() );
			claimsData.put("endToEndId",x_request_id);
			claimsData.put("consentId", consentID);
			claimsData.put("href","/accounts/");
			

			String cryptogram=generateJwtToken(claimsData,consentDataList.get(0).getPrivate_key());
			//String cryptogram=generateJwtToken(claimsData,listener.retrievePriKey(consentDataList.get(0).getCustom_device_id()));

			String status = ipsDao.outwardConsentDataRegisterAccountListInc(x_request_id, psuDeviceID,
					psuIPAddress, psuID, psuIDCountry, psuIDType, sender_participant_bic,sender_participant_member_id,
					receiver_participant_bic, receiver_participant_member_id,consentID);
			
			logger.debug("Calling Outward Consent Access Account List Register status"+status);

			if(status.equals("0")) {
				ResponseEntity<AccountListResponse> accountConsentResponse = consentIPSXservice
						.accountConsentAccountList(x_request_id, consentDataList.get(0).getCustom_device_id(),
								psuIPAddress, psuID, psuIDCountry, psuIDType, sender_participant_bic,sender_participant_member_id,
								receiver_participant_bic, receiver_participant_member_id,consentID,cryptogram);
				
				if (accountConsentResponse.getStatusCode() == HttpStatus.OK) {
					AccountListResponse consentAccessResponse = accountConsentResponse.getBody();
					ipsDao.accountConsentTransactionIncResponse(x_request_id,"SUCCESS","");
					
					return consentAccessResponse;
				} else if(accountConsentResponse.getStatusCode() == HttpStatus.BAD_REQUEST){
					
					ErrorRestResponse errorRestResponse = new ErrorRestResponse(accountConsentResponse.getBody().getErrorCode(),
							accountConsentResponse.getBody().getDescription());
					ipsDao.accountConsentTransactionIncResponse(x_request_id,"FAILURE",errorRestResponse.getErrorCode()+":"+errorRestResponse.getDescription());

					logger.info(errorRestResponse.getErrorCode()+":"+errorRestResponse.getDescription());
					throw new IPSXException(errorRestResponse.getErrorCode()+":"+errorRestResponse.getDescription());
				}else{
					ErrorRestResponse errorRestResponse = new ErrorRestResponse(accountConsentResponse.getBody().getErrorCode(),
							accountConsentResponse.getBody().getDescription());	
					ipsDao.accountConsentTransactionIncResponse(x_request_id,"FAILURE",errorRestResponse.getErrorCode()+":"+errorRestResponse.getDescription());
					throw new IPSXException(errorRestResponse.getErrorCode()+":"+errorRestResponse.getDescription());
				}


			}else {
				throw new IPSXException(errorCode.ErrorCodeRegistration("0"));
			}
	
		}else {
			throw new IPSXException(errorCode.ErrorCodeRegistration("13"));
		}

	}
	
	
	public AccountsListAccounts outwardConsentAccessAccountInc(String x_request_id, String psuDeviceID,
			String psuIPAddress, String psuID, String psuIDCountry, String psuIDType, String consentID,
			String accountID) throws Exception {
		List<ConsentOutwardAccessTable> consentDataList = ipsDao.getConsentData(consentID);

		logger.debug("Calling Outward Consent Access Account  Inquiry");
		if(consentDataList.size()>0) {
			String sender_participant_bic=consentDataList.get(0).getSenderparticipant_bic();
			String sender_participant_member_id=consentDataList.get(0).getSenderparticipant_memberid();
			String receiver_participant_bic=consentDataList.get(0).getReceiverparticipant_bic();
			String receiver_participant_member_id=consentDataList.get(0).getReceiverparticipant_memberid();
			
			logger.info(accountID);
			
		////Create Cryptogram Data
			Map<String, Object> claimsData = new HashMap<String, Object>();
			claimsData.put("deviceId",consentDataList.get(0).getCustom_device_id() );
			claimsData.put("endToEndId",x_request_id);
			claimsData.put("consentId", consentID);
			claimsData.put("href","/accounts/"+accountID);

			String cryptogram=generateJwtToken(claimsData,consentDataList.get(0).getPrivate_key());
			//String cryptogram=generateJwtToken(claimsData,listener.retrievePriKey(consentDataList.get(0).getCustom_device_id()));

			
			String status = ipsDao.outwardConsentDataRegisterAccountInc(x_request_id, psuDeviceID,
					psuIPAddress, psuID, psuIDCountry, psuIDType, sender_participant_bic,sender_participant_member_id,
					receiver_participant_bic, receiver_participant_member_id,consentID,accountID);
			
			logger.debug("Calling Outward Consent Access Account  Register status"+status);

			if(status.equals("0")) {
				ResponseEntity<AccountsListAccounts> accountConsentResponse = consentIPSXservice
						.accountConsentAccountInd(x_request_id, consentDataList.get(0).getCustom_device_id(),
								psuIPAddress, psuID, psuIDCountry, psuIDType, sender_participant_bic,sender_participant_member_id,
								receiver_participant_bic, receiver_participant_member_id,consentID,cryptogram,accountID);
				
				if (accountConsentResponse.getStatusCode() == HttpStatus.OK) {
					AccountsListAccounts consentAccessResponse = accountConsentResponse.getBody();
					ipsDao.accountConsentBalancesResponse(x_request_id,"SUCCESS","");
					
					return consentAccessResponse;
				} else if(accountConsentResponse.getStatusCode() == HttpStatus.BAD_REQUEST){
					ErrorRestResponse errorRestResponse = new ErrorRestResponse(accountConsentResponse.getBody().getErrorCode(),
							accountConsentResponse.getBody().getDescription());

					
					ipsDao.accountConsentTransactionIncResponse(x_request_id,"FAILURE",errorRestResponse.getErrorCode()+":"+errorRestResponse.getDescription());

					throw new IPSXException(errorRestResponse.getErrorCode()+":"+errorRestResponse.getDescription());
				}else{
					ErrorRestResponse errorRestResponse = new ErrorRestResponse(accountConsentResponse.getBody().getErrorCode(),
							accountConsentResponse.getBody().getDescription());
					ipsDao.accountConsentTransactionIncResponse(x_request_id,"FAILURE",errorRestResponse.getErrorCode()+":"+errorRestResponse.getDescription());
					throw new IPSXException(errorRestResponse.getErrorCode()+":"+errorRestResponse.getDescription());
				}
			}else {
				throw new IPSXException(errorCode.ErrorCodeRegistration("0"));
			}
	
		}else {
			throw new IPSXException(errorCode.ErrorCodeRegistration("13"));
		}
	}

	
	public List<RegConsentAccountList> outwardConsentRegisterAccountList(  String psuDeviceID,
			String psuIPAddress, String documentID) {
		
		
			List<Object[]> data=ipsDao.getConsentAccounts(documentID);
			
			if(data.size()>0) {
				List<RegConsentAccountList>  listData=new ArrayList<>();
				for(Object[] obj:data) {
					RegConsentAccountList regConsentData=new RegConsentAccountList();
					regConsentData.setConsentID(obj[0].toString());
					
					ConsentOutwardAccessRequestAccounts acctInfo=new ConsentOutwardAccessRequestAccounts();
					acctInfo.setAccountNumber(obj[1].toString());
					acctInfo.setShemeName(String.valueOf(obj[8]).equals("null")?"":String.valueOf(obj[8]));
					acctInfo.setAccountName(String.valueOf(obj[2]).equals("null")?"":String.valueOf(obj[2]));
					regConsentData.setAccounts(acctInfo);
					
					regConsentData.setBankName(obj[5].toString()+"-"+obj[3].toString()+"-"+obj[4].toString());
					
					regConsentData.setPhoneNumber(obj[7].toString());
					
					List<String> listPermi=new ArrayList<>();
					
					if(String.valueOf(obj[9]).equals("Y")) {
						listPermi.add(TranMonitorStatus.ReadBalances.toString());
					}
					if(String.valueOf(obj[10]).equals("Y")) {
						listPermi.add(TranMonitorStatus.ReadTransactionsDetails.toString());
					}
					if(String.valueOf(obj[11]).equals("Y")) {
						listPermi.add(TranMonitorStatus.ReadAccountsDetails.toString());
					}
					if(String.valueOf(obj[12]).equals("Y")) {
						listPermi.add(TranMonitorStatus.DebitAccount.toString());
					}
					
					regConsentData.setPermission(listPermi);
					
					listData.add(regConsentData);
				}
				return listData;
			}else {
				throw new IPSXException(errorCode.validationError("BIPS15"));
			}
		
		
	}


	public String generatePublicKey(KeyPair kp) throws NoSuchAlgorithmException {
		
		PublicKey publicKey1 = (PublicKey) kp.getPublic();

		String encodedPublicKey = Base64.getEncoder().encodeToString(publicKey1.getEncoded());

		return encodedPublicKey;
	
	}
	
  public String generatePrivateKey(KeyPair kp) throws NoSuchAlgorithmException {
		
	    PrivateKey privateKey1 = (PrivateKey) kp.getPrivate();

		String encodedPrivateKey = Base64.getEncoder().encodeToString(privateKey1.getEncoded());

		return encodedPrivateKey;
	
	}

	public String generateJwtToken( Map<String, Object> claimsData,String privateKey) throws NoSuchAlgorithmException {
		/*KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");
		keyGenerator.initialize(1024);

		KeyPair kp = keyGenerator.genKeyPair();
		PrivateKey privateKey = (PrivateKey) kp.getPrivate();

		String token = generateJwtToken(privateKey,claimsData);

		return token;*/
		 String token="";
         try {
         	byte[] data = Base64.getDecoder().decode((privateKey.getBytes()));
	            X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
	            KeyFactory fact = KeyFactory.getInstance("RSA");
	            
	          String  rsaPrivateKey = privateKey.replace("-----BEGIN PRIVATE KEY-----", "");
	          rsaPrivateKey = privateKey.replace("-----END PRIVATE KEY-----", "");

	          PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(rsaPrivateKey));
	          KeyFactory kf = KeyFactory.getInstance("RSA");
	          PrivateKey privKey = kf.generatePrivate(keySpec);
				
	           
	            
	             token = Jwts.builder().setClaims(claimsData).signWith(SignatureAlgorithm.RS512,  privKey).compact();
				return token;
         }catch(Exception e) {
         	System.out.println("Exception:"+e.getMessage());
         }
         return token;

	}
	
	public static String generateJwtToken(PrivateKey privateKey, Map<String, Object> claimsData) {

		String token = Jwts.builder().setClaims(claimsData).signWith(SignatureAlgorithm.RS512, privateKey).compact();
		return token;

	}

	public RTPbulkTransferResponse createBulkRTPconnection(String psuDeviceID, String psuIpAddress, String psuID,
			RTPbulkTransferRequest rtpBulkTransferRequest,String p_id,String channelID,String resvfield1,String resvfield2) {
		
		String master_ref_id = env.getProperty("ipsx.userS") + sequence.generateSystemTraceAuditNumber();

		
		RTPbulkTransferResponse rtpbulkTransferResponse = null;
		
		
			///// Starting Background Service
			taskExecutor.execute(new Runnable() {
				@Override
				public void run() {

					for (int i = 0; i < rtpBulkTransferRequest.getBenAccount().size(); i++) {

						int data=i+1;
						////// Generate System Trace Audit Number
						String sysTraceNumber = "";
						////// Generate Sequence Unique ID
						String seqUniqueID = sequence.generateSeqUniqueID()+"/"+data;
						///// Generate CIM Message ID
						String cimMsgID = seqUniqueID;
						///// Generate Msg Sequence
						String msgSeq = sequence.generateMsgSequence();
						///// Generate End To End ID
						String endTOEndID = env.getProperty("ipsx.bicfi")
								+ new SimpleDateFormat("YYYYMMdd").format(new Date()) + msgSeq;
						
					    /////Net Mir
						String msgNetMir=new SimpleDateFormat("YYMMdd").format(new Date())+env.getProperty("ipsx.user")+"0001"+msgSeq;

						///// Register Manual Record
						logger.info("Register Initial outgoing Fund Transfer Record");
						
						
					    ////// Get Bank Agent Account and Agent Account
						BankAgentTable creditorAgent = ipsDao
													.getOtherBankAgent(rtpBulkTransferRequest.getBenAccount().get(i).getBankCode());
						
						logger.info("Register Initial outgoing Fund Transfer Record"+creditorAgent.getBank_agent());

						////Get Consent Data
						List<ConsentOutwardAccessTable> regAccList = consentOutwardAccessTableRep
								.getAccountNumber(rtpBulkTransferRequest.getRemitterAccount().getAcctNumber());
						
						BankAgentTable bankAgentTable=ipsDao.findByBank(regAccList.get(0).getReceiverparticipant_bic());
						
						String instgAgent=env.getProperty("ipsx.bicfi");
						String instdAgent=bankAgentTable.getBank_agent();
						String debtorAgent=bankAgentTable.getBank_agent();
						String debtorAgentAcct=bankAgentTable.getBank_agent_account();
						String CreditorAgent=creditorAgent.getBank_agent();
						String CreditorAgentAcct=creditorAgent.getBank_agent_account();
						
						////Check Creditor Account Number already registerd in BIPS
						boolean isRegisteredPISP=ipsDao.checkExistConsent(rtpBulkTransferRequest.getBenAccount().get(i).getBenAcctNumber());
						
						////Category Purpose
						String ctgyPurp=listener.getCtgyPurp(instgAgent,debtorAgent,CreditorAgent,rtpBulkTransferRequest.getRemitterAccount().getAcctName(),rtpBulkTransferRequest.getBenAccount().get(i).getBenName(),isRegisteredPISP);
						
						///Local Instrumentation
						String lclInstr=TranMonitorStatus.CSDC.toString();
						
						///ChargeBearer
						String chargeBearer=ChargeBearerType1Code1.SLEV.toString();
						
						ipsDao.RegisterBulkRTPRecord(psuDeviceID, psuIpAddress, sysTraceNumber,
								cimMsgID, seqUniqueID, endTOEndID, master_ref_id,msgNetMir,
								instgAgent,instdAgent,debtorAgent,
								debtorAgentAcct,CreditorAgent,CreditorAgentAcct,
								seqUniqueID,"0100",lclInstr,ctgyPurp,ctgyPurp,rtpBulkTransferRequest.getRemitterAccount().getAcctName(),
								rtpBulkTransferRequest.getRemitterAccount().getAcctNumber(),rtpBulkTransferRequest.getBenAccount().get(i).getBankCode(),
								rtpBulkTransferRequest.getRemitterAccount().getCurrencyCode(),rtpBulkTransferRequest.getBenAccount().get(i).getBenName(),
								rtpBulkTransferRequest.getBenAccount().get(i).getBenAcctNumber(),rtpBulkTransferRequest.getBenAccount().get(i).getReqUniqueId(),
								rtpBulkTransferRequest.getBenAccount().get(i).getTrAmt(),rtpBulkTransferRequest.getBenAccount().get(i).getTrRmks(),
								p_id,rtpBulkTransferRequest.getBenAccount().get(i).getReqUniqueId(),channelID,resvfield1,resvfield2,
								bankAgentTable.getBank_code(),chargeBearer);
					

						
					
						////Generate JWT Token
					    ////Create Cryptogram Data
						Map<String, Object> claimsData = new HashMap<String, Object>();
						claimsData.put("deviceId",regAccList.get(0).getCustom_device_id());
						claimsData.put("amount",rtpBulkTransferRequest.getBenAccount().get(i).getTrAmt());
						claimsData.put("currency",rtpBulkTransferRequest.getBenAccount().get(i).getCurrencyCode());
						claimsData.put("debtorAccount",rtpBulkTransferRequest.getRemitterAccount().getAcctNumber());
						claimsData.put("creditorAccount",rtpBulkTransferRequest.getBenAccount().get(i).getBenAcctNumber());
						claimsData.put("endToEndId",endTOEndID);
						claimsData.put("consentId", regAccList.get(0).getConsent_id());
				        
						//String cryptogram="eyJhbGciOiJSUzUxMiJ9.eyJjb25zZW50SWQiOiI4LWh0ZEkyQVR3V1czazRFYzAxQldRIiwiaHJlZiI6Ii9hY2NvdW50cy1jb25zZW50cy84LWh0ZEkyQVR3V1czazRFYzAxQldRL2F1dGhvcmlzYXRpb25zLy1YUmRVb3EwUkxxMDNkdGpWMUhNdUEiLCJlbmRUb0VuZElkIjoiQ0lNMTAwUkVHMDAwMzciLCJkZXZpY2VJZCI6ImE3M2JhN2IyLWMxYTItNTA1Ny1iM2ViLTA5ZGFlOWZlNjBhYyJ9.f6zfIvv70rq1T2HxWq0b5UGS1rZdRp0LZZwHpDAKZGCvuDfhzHRF3xaEL3T3OJ0X1nCg43xPYaJh79OfkMVLsCLn2EtdgSC_j1jBpY7yOckPI9qo8b8hhGID-NLEYoRkBUoVbH0AnX2zhWIkTJyQCwD6RXHctObSt-vozFzKRBM";
						
						String cryptogram="";
						try {
							cryptogram=generateJwtToken(claimsData,regAccList.get(0).getPrivate_key());
							// cryptogram=generateJwtToken(claimsData,listener.retrievePriKey(regAccList.get(0).getCustom_device_id()));

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
												
						
						////// Calling IPSX
						logger.info("Calling IPSX");
						ipsxClient.sendBulkRTPRequst(
								rtpBulkTransferRequest.getRemitterAccount().getAcctName(),
								rtpBulkTransferRequest.getRemitterAccount().getAcctNumber(),
								rtpBulkTransferRequest.getRemitterAccount().getCurrencyCode(),
								rtpBulkTransferRequest.getBenAccount().get(i).getBenName(),
								rtpBulkTransferRequest.getBenAccount().get(i).getBenAcctNumber(),
								rtpBulkTransferRequest.getBenAccount().get(i).getTrAmt(),
								rtpBulkTransferRequest.getBenAccount().get(i).getTrRmks(),
								seqUniqueID,cimMsgID,msgSeq, endTOEndID,msgNetMir,cryptogram,
								instgAgent,instdAgent,debtorAgent,
								debtorAgentAcct,CreditorAgent,CreditorAgentAcct,lclInstr,ctgyPurp,chargeBearer);
						
					
					}

				}
			});

			///// Return Master Ref ID
			rtpbulkTransferResponse = new RTPbulkTransferResponse(master_ref_id,
					new SimpleDateFormat("YYYY-MM-dd HH:mm:ss ").format(new Date()));
			return rtpbulkTransferResponse;
	
		
		
	}

/////Fund Transfer Connection
	////Debit Customer Account Credit Settl Account(Connect 24)
	////Send Packages to IPSX,Credit to IPSX Account
	public MCCreditTransferResponse createMerchantFTConnection(String psuDeviceID, String psuIpAddress, String psuID,
			CIMMerchantDirectFndRequest mcCreditTransferRequest,String p_id,
			String channelID,String resvField1,String resvField2)
			throws DatatypeConfigurationException, JAXBException, KeyManagementException, UnrecoverableKeyException,
			KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {

		MCCreditTransferResponse mcCreditTransferResponse = null;

		///// Generate Sequence Unique ID
		String seqUniqueID = sequence.generateSeqUniqueID();
		///// Generate Bob Msg ID
		String cimMsgID = seqUniqueID;
		///// Generate SystemTraceAuditNumber or CBS Tran Number
		String sysTraceNumber = sequence.generateSystemTraceAuditNumber();
		//// Generate Msg Sequence
		String msgSeq = sequence.generateMsgSequence();
		///// Generate EndToEnd ID
		String endTOEndID = env.getProperty("ipsx.bicfi") + new SimpleDateFormat("yyyyMMdd").format(new Date())
				+ msgSeq;
		/////Net Mir
		String msgNetMir=new SimpleDateFormat("yyMMdd").format(new Date())+env.getProperty("ipsx.user")+"0001"+msgSeq;

		logger.info("Transaction cycle starts");
		logger.info("System Trace Audit Number" + sysTraceNumber);
		logger.info("System Sequence ID" + cimMsgID);
		
		logger.info("Register Initial outgoing Fund Transfer Record");
		
		///Purpose Code for Peer to Peer Connection
		//mcCreditTransferRequest.setPurpose("100");
		
		///// Get Other Bank Agent and Agent Account number
		BankAgentTable othBankAgent = ipsDao.findByBank(
				mcCreditTransferRequest.getMerchantAccount().getPayeeParticipantCode().replace("XXXX", ""));
		
		////Get Remitter Bank Code
		String remitterBankCode=ipsDao.getOtherBankCode(env.getProperty("ipsx.dbtragt"));
		
		
		logger.info(othBankAgent.getBank_agent(),
				"" + othBankAgent.getBank_agent_account());

			///// Starting Background Service
			taskExecutor.execute(new Runnable() {
				@Override
				public void run() {
				
					
				String lclInstrm=TranMonitorStatus.CSDC.toString();
				String ctgyPurp="300";
				String chrBeearer=ChargeBearerType1Code.SLEV.value();
				
				String tot_tran_amount=mcCreditTransferRequest.getMerchantAccount().getTrAmt();
				
				
				////Remarks
				String remarks="";
				////Retrieve Remittence  Information
				StringBuilder remInfo=new StringBuilder();
				remInfo.append("/QR/"+mcCreditTransferRequest.getMerchantAccount().getGlobalID()+"//");
				if(!String.valueOf(mcCreditTransferRequest.getMerchantAccount().getPointOfInitiationFormat()).equals("null") && 
						!String.valueOf(mcCreditTransferRequest.getMerchantAccount().getPointOfInitiationFormat()).equals("")) {
					remInfo.append("01/"+mcCreditTransferRequest.getMerchantAccount().getPointOfInitiationFormat()+"/");
				}
				if(mcCreditTransferRequest.getMerchantAccount().isConvenienceIndicator()) {
					
					if(String.valueOf(mcCreditTransferRequest.getMerchantAccount().getConvenienceIndicatorFeeType()).equals("Fixed")) {
						remInfo.append("55/02/56/"+mcCreditTransferRequest.getMerchantAccount().getConvenienceIndicatorFee()+"/");
						
						Double sumData=Double.parseDouble(mcCreditTransferRequest.getMerchantAccount().getConvenienceIndicatorFee())+(Double.parseDouble(mcCreditTransferRequest.getMerchantAccount().getTrAmt()));
						tot_tran_amount=sumData.toString();
					}else if(String.valueOf(mcCreditTransferRequest.getMerchantAccount().getConvenienceIndicatorFeeType()).equals("Percentage")) {
						remInfo.append("55/03/57/"+mcCreditTransferRequest.getMerchantAccount().getConvenienceIndicatorFee()+"/");
					
						Double convFee=(((Double.parseDouble(mcCreditTransferRequest.getMerchantAccount().getTrAmt()))*(Double.parseDouble(mcCreditTransferRequest.getMerchantAccount().getConvenienceIndicatorFee())))/100);
						Double sumData=convFee+(Double.parseDouble(mcCreditTransferRequest.getMerchantAccount().getTrAmt()));
						tot_tran_amount=sumData.toString();
					
					}
				}
				
				
				if(mcCreditTransferRequest.getAdditionalDataInformation()!=null) {
					if((!String.valueOf(mcCreditTransferRequest.getAdditionalDataInformation().getBillNumber()).equals("null")&&!String.valueOf(mcCreditTransferRequest.getAdditionalDataInformation().getBillNumber()).equals(""))||
							(!String.valueOf(mcCreditTransferRequest.getAdditionalDataInformation().getMobileNumber()).equals("null")&&!String.valueOf(mcCreditTransferRequest.getAdditionalDataInformation().getMobileNumber()).equals(""))||
							(!String.valueOf(mcCreditTransferRequest.getAdditionalDataInformation().getReferenceLabel()).equals("null")&&!String.valueOf(mcCreditTransferRequest.getAdditionalDataInformation().getReferenceLabel()).equals(""))||
							(!String.valueOf(mcCreditTransferRequest.getAdditionalDataInformation().getStoreLabel()).equals("null")&&!String.valueOf(mcCreditTransferRequest.getAdditionalDataInformation().getStoreLabel()).equals(""))||
							(!String.valueOf(mcCreditTransferRequest.getAdditionalDataInformation().getLoyaltyNumber()).equals("null")&&!String.valueOf(mcCreditTransferRequest.getAdditionalDataInformation().getLoyaltyNumber()).equals(""))||
							(!String.valueOf(mcCreditTransferRequest.getAdditionalDataInformation().getCustomerLabel()).equals("null")&&!String.valueOf(mcCreditTransferRequest.getAdditionalDataInformation().getCustomerLabel()).equals(""))||
							(!String.valueOf(mcCreditTransferRequest.getAdditionalDataInformation().getTerminalLabel()).equals("null")&&!String.valueOf(mcCreditTransferRequest.getAdditionalDataInformation().getTerminalLabel()).equals(""))||
							(!String.valueOf(mcCreditTransferRequest.getAdditionalDataInformation().getAddlDataRequest()).equals("null")&&!String.valueOf(mcCreditTransferRequest.getAdditionalDataInformation().getAddlDataRequest()).equals(""))||
							(!String.valueOf(mcCreditTransferRequest.getAdditionalDataInformation().getPurposeOfTransaction()).equals("null")&&!String.valueOf(mcCreditTransferRequest.getAdditionalDataInformation().getPurposeOfTransaction()).equals(""))) {
						
						
						remInfo.append("62//");
						
						if(!String.valueOf(mcCreditTransferRequest.getAdditionalDataInformation().getBillNumber()).equals("null")&&
								!String.valueOf(mcCreditTransferRequest.getAdditionalDataInformation().getBillNumber()).equals("")	) {
							remInfo.append("01/"+mcCreditTransferRequest.getAdditionalDataInformation().getBillNumber()+"/");
						}
						if(!String.valueOf(mcCreditTransferRequest.getAdditionalDataInformation().getMobileNumber()).equals("null")&&
								!String.valueOf(mcCreditTransferRequest.getAdditionalDataInformation().getMobileNumber()).equals("")	) {
							remInfo.append("02/"+mcCreditTransferRequest.getAdditionalDataInformation().getMobileNumber()+"/");
						}
						if(!String.valueOf(mcCreditTransferRequest.getAdditionalDataInformation().getStoreLabel()).equals("null")&&
								!String.valueOf(mcCreditTransferRequest.getAdditionalDataInformation().getStoreLabel()).equals("")
								) {
							remInfo.append("03/"+mcCreditTransferRequest.getAdditionalDataInformation().getStoreLabel()+"/");
						}
						if(!String.valueOf(mcCreditTransferRequest.getAdditionalDataInformation().getLoyaltyNumber()).equals("null")&&
								!String.valueOf(mcCreditTransferRequest.getAdditionalDataInformation().getLoyaltyNumber()).equals("")
								) {
							remInfo.append("04/"+mcCreditTransferRequest.getAdditionalDataInformation().getLoyaltyNumber()+"/");
						}
						if(!String.valueOf(mcCreditTransferRequest.getAdditionalDataInformation().getReferenceLabel()).equals("null")&&
								!String.valueOf(mcCreditTransferRequest.getAdditionalDataInformation().getReferenceLabel()).equals("")
								) {
							remInfo.append("05/"+mcCreditTransferRequest.getAdditionalDataInformation().getReferenceLabel()+"/");
						}
						if(!String.valueOf(mcCreditTransferRequest.getAdditionalDataInformation().getCustomerLabel()).equals("null")&&
								!String.valueOf(mcCreditTransferRequest.getAdditionalDataInformation().getCustomerLabel()).equals("")
								) {
							remInfo.append("06/"+mcCreditTransferRequest.getAdditionalDataInformation().getCustomerLabel()+"/");
						}
						if(!String.valueOf(mcCreditTransferRequest.getAdditionalDataInformation().getTerminalLabel()).equals("null")&&
								!String.valueOf(mcCreditTransferRequest.getAdditionalDataInformation().getTerminalLabel()).equals("")
								) {
							remInfo.append("07/"+mcCreditTransferRequest.getAdditionalDataInformation().getTerminalLabel()+"/");
						}
						if(!String.valueOf(mcCreditTransferRequest.getAdditionalDataInformation().getPurposeOfTransaction()).equals("null")&&
								!String.valueOf(mcCreditTransferRequest.getAdditionalDataInformation().getPurposeOfTransaction()).equals("null")) {
							remInfo.append("08/"+mcCreditTransferRequest.getAdditionalDataInformation().getPurposeOfTransaction()+"/");
							remarks=mcCreditTransferRequest.getAdditionalDataInformation().getPurposeOfTransaction();
						}
						if(!String.valueOf(mcCreditTransferRequest.getAdditionalDataInformation().getAddlDataRequest()).equals("null")&&
								!String.valueOf(mcCreditTransferRequest.getAdditionalDataInformation().getAddlDataRequest()).equals("null")) {
							remInfo.append("09/"+mcCreditTransferRequest.getAdditionalDataInformation().getAddlDataRequest()+"/");
						}
					}
					
					remInfo.append("//RQ/");
					
				}
				
				
				logger.debug("RemitterInfo->"+remInfo.toString());
				//////Register Data to Master Table
				ipsDao.RegisterMerchantOutgoingMasterRecord(psuDeviceID, psuIpAddress, sysTraceNumber, cimMsgID,
						seqUniqueID, endTOEndID, seqUniqueID, msgNetMir, env.getProperty("ipsx.bicfi"),
						othBankAgent.getBank_agent(), env.getProperty("ipsx.dbtragt"),
						env.getProperty("ipsx.dbtragtacct"), othBankAgent.getBank_agent(),
						othBankAgent.getBank_agent_account(), seqUniqueID, "0100", lclInstrm, ctgyPurp, ctgyPurp,
						mcCreditTransferRequest.getRemitterAccount().getAcctName(),
						mcCreditTransferRequest.getRemitterAccount().getAcctNumber(),
						mcCreditTransferRequest.getMerchantAccount().getPayeeParticipantCode(),
						remitterBankCode,
						mcCreditTransferRequest.getMerchantAccount().getCurrency(),
						mcCreditTransferRequest.getMerchantAccount().getMerchantName(),
						mcCreditTransferRequest.getMerchantAccount().getMerchantAcctNumber(),
						p_id,tot_tran_amount,remarks, p_id,
						p_id, channelID, resvField1,resvField2,chrBeearer,remInfo.toString(),
						mcCreditTransferRequest);
				
				 
				/* 
				///// Register OutGoing Message to Tran Table
				ipsDao.RegisterMerchantOutMsgRecord(psuDeviceID, psuIpAddress, psuID,
							sysTraceNumber,
							mcCreditTransferRequest, cimMsgID, seqUniqueID, endTOEndID,"300",msgNetMir,
							env.getProperty("ipsx.bicfi"),othBankAgent.getBank_agent(),env.getProperty("ipsx.dbtragt"),env.getProperty("ipsx.dbtragtacct"),
							othBankAgent.getBank_agent(),othBankAgent.getBank_agent_account(),
							seqUniqueID,"0100",lclInstrm,ctgyPurp,remitterBankCode,chrBeearer,remInfo.toString(),tot_tran_amount);
					*/
					
				////Generate RequestUUID
				String requestUUID=sequence.generateRequestUUId();
					
				/**********ESB*********/
				////Store ESB Registration Data
				////Register ESB Data
			    ipsDao.registerCIMcbsIncomingData(requestUUID, env.getProperty("cimCBS.channelID"),
						env.getProperty("cimCBS.servicereqversion"), env.getProperty("cimCBS.servicereqID"), new Date(),
						sysTraceNumber, env.getProperty("cimCBS.outDBChannel"), "", "True", "DR", "N", "", mcCreditTransferRequest.getMerchantAccount().getMerchantAcctNumber(), tot_tran_amount, mcCreditTransferRequest.getMerchantAccount().getCurrency(),
						seqUniqueID, mcCreditTransferRequest.getRemitterAccount().getAcctNumber(), mcCreditTransferRequest.getRemitterAccount().getAcctName(), "NRT/RTP",remarks, "", "", "");
				
			    /////Call ESB Connection
					ResponseEntity<CimCBSresponse> connect24Response = cimCBSservice.dbtFundRequest(requestUUID);

					logger.debug("CBS Data:"+connect24Response.toString());
					if (connect24Response.getStatusCode() == HttpStatus.OK) {
						logger.info(seqUniqueID + ": success"+connect24Response.getBody().getStatus().getIsSuccess()+":"+connect24Response.getBody().getStatus().getMessage());

						if (connect24Response.getBody().getStatus().getIsSuccess()) {
							
							////Update ESB Data
							ipsDao.updateCIMcbsData(requestUUID, TranMonitorStatus.SUCCESS.toString(),
									connect24Response.getBody().getStatus().getStatusCode(),
									connect24Response.getBody().getStatus().getMessage(),
									connect24Response.getBody().getData().getTransactionNoFromCBS());
							
							/*////Update CBS Status to Tran Table
							ipsDao.updateCBSStatus(seqUniqueID,
									TranMonitorStatus.CBS_DEBIT_OK.toString(),
									TranMonitorStatus.IN_PROGRESS.toString());*/
							
						    ////Update CBS Status to Tran Table
							ipsDao.updateOutwardCBSStatus(seqUniqueID,
									TranMonitorStatus.CBS_DEBIT_OK.toString(),
									TranMonitorStatus.IN_PROGRESS.toString());
							
							/////Calling IPSX
							ipsxClient.sendMerchantftRequst( mcCreditTransferRequest,
									sysTraceNumber, cimMsgID, seqUniqueID, othBankAgent, msgSeq, endTOEndID, msgNetMir,
									lclInstrm,ctgyPurp,chrBeearer,remInfo.toString(),tot_tran_amount);


						} else {
							
							//// Update ESB Data
							ipsDao.updateCIMcbsData(requestUUID, TranMonitorStatus.FAILURE.toString(),
									connect24Response.getBody().getStatus().getStatusCode(),
									connect24Response.getBody().getStatus().getMessage(),
									connect24Response.getBody().getData().getTransactionNoFromCBS());
	/*
							//// Update CBS Status to Tran table
							ipsDao.updateCBSStatusError(seqUniqueID, TranMonitorStatus.CBS_DEBIT_ERROR.toString(),
									connect24Response.getBody().getStatus().getMessage(),
									TranMonitorStatus.FAILURE.toString());*/
	
					      	//// Update ESB Data Error
							ipsDao.updateOutwardCBSStatusError(seqUniqueID, TranMonitorStatus.CBS_DEBIT_ERROR.toString(),
									connect24Response.getBody().getStatus().getMessage(), TranMonitorStatus.FAILURE.toString());
		
							//// Send Failure Message to CIM
							/*ipsDao.updateIPSXStatusResponseRJCTBulkRTP(seqUniqueID, connect24Response.getBody().getStatus().getMessage(), seqUniqueID,
									TranMonitorStatus.FAILURE.toString(), "",
									"",  connect24Response.getBody().getStatus().getStatusCode());*/
							
						}
					} else {
						//// Update ESB Data to CIM Table
						ipsDao.updateCIMcbsData(requestUUID, TranMonitorStatus.FAILURE.toString(), String.valueOf(connect24Response.getStatusCodeValue()),
								"Internal Server Error","");
	 
						/*//// Update ESB Data Error to Transaction Table
						ipsDao.updateCBSStatusError(seqUniqueID, TranMonitorStatus.CBS_DEBIT_ERROR.toString(),
								String.valueOf(connect24Response.getStatusCodeValue())+":Internal Server Error", TranMonitorStatus.FAILURE.toString());
	*/
					    //// Update ESB Data to Outward Table
						ipsDao.updateOutwardCBSStatusError(seqUniqueID, TranMonitorStatus.CBS_DEBIT_ERROR.toString(),
								String.valueOf(connect24Response.getStatusCodeValue())+":Internal Server Error", TranMonitorStatus.FAILURE.toString());
	
					    //// Send Failure Message to CIM
						/*ipsDao.updateIPSXStatusResponseRJCTBulkRTP(seqUniqueID, connect24Response.getBody().getStatus().getMessage(), seqUniqueID,
								TranMonitorStatus.FAILURE.toString(), "",
								"",  connect24Response.getBody().getStatus().getStatusCode());
						*/
						
					}

				}
			});
			
		    ///// Return Sequence ID to CIM
			mcCreditTransferResponse = new MCCreditTransferResponse(seqUniqueID,
					new SimpleDateFormat("YYYY-MM-dd HH:mm:ss ").format(new Date()));
			return mcCreditTransferResponse;
		


		
	}


	public RTPbulkTransferResponse createMerchantRTPconnection(String psuDeviceID, String psuIpAddress, String psuID,
			CIMMerchantDirectFndRequest cimMerchantRequest,String p_id,String channelID,String resvfield1,String resvfield2) {
		
		String master_ref_id = env.getProperty("ipsx.userS") + sequence.generateSystemTraceAuditNumber();

		
		RTPbulkTransferResponse rtpbulkTransferResponse = null;
		
		
			///// Starting Background Service
			taskExecutor.execute(new Runnable() {
				@Override
				public void run() {

						////// Generate System Trace Audit Number
						String sysTraceNumber = "";
						////// Generate Sequence Unique ID
						String seqUniqueID = sequence.generateSeqUniqueID();
						///// Generate CIM Message ID
						String cimMsgID = seqUniqueID;
						///// Generate Msg Sequence
						String msgSeq = sequence.generateMsgSequence();
						///// Generate End To End ID
						String endTOEndID = env.getProperty("ipsx.bicfi")
								+ new SimpleDateFormat("YYYYMMdd").format(new Date()) + msgSeq;
						
					    /////Net Mir
						String msgNetMir=new SimpleDateFormat("YYMMdd").format(new Date())+env.getProperty("ipsx.user")+"0001"+msgSeq;

						///// Register Manual Record
						logger.info("Register Initial outgoing Fund Transfer Record");
						
						
					    ////// Get Bank Agent Account and Agent Account
						BankAgentTable creditorAgent = ipsDao
													.findByBank(cimMerchantRequest.getMerchantAccount().getPayeeParticipantCode().replace("XXXX", ""));
						
						logger.info("Register Initial outgoing Fund Transfer Record"+creditorAgent.getBank_agent());

						////Get Consent Data
						List<ConsentOutwardAccessTable> regAccList = consentOutwardAccessTableRep
								.getAccountNumber(cimMerchantRequest.getRemitterAccount().getAcctNumber());
						
						BankAgentTable bankAgentTable=ipsDao.findByBank(regAccList.get(0).getReceiverparticipant_bic());
						
						String instgAgent=env.getProperty("ipsx.bicfi");
						String instdAgent=bankAgentTable.getBank_agent();
						String debtorAgent=bankAgentTable.getBank_agent();
						String debtorAgentAcct=bankAgentTable.getBank_agent_account();
						String CreditorAgent=creditorAgent.getBank_agent();
						String CreditorAgentAcct=creditorAgent.getBank_agent_account();
						
						////Category Purpose
						//String ctgyPurp=listener.getCtgyPurp(instgAgent,debtorAgent,CreditorAgent);
						String ctgyPurp="300";
						///Local Instrumentation
						String lclInstr=TranMonitorStatus.CSDC.toString();
						
						///Remarks
						String remarks="";
						///ChargeBearer
						String chargeBearer=ChargeBearerType1Code1.SLEV.toString();
						
						String tot_tran_amount=cimMerchantRequest.getMerchantAccount().getTrAmt();
					////Retrieve Remittence  Information
						StringBuilder remInfo=new StringBuilder();
						remInfo.append("/QR/"+cimMerchantRequest.getMerchantAccount().getGlobalID()+"//");
						if(!String.valueOf(cimMerchantRequest.getMerchantAccount().getPointOfInitiationFormat()).equals("null") && 
								!String.valueOf(cimMerchantRequest.getMerchantAccount().getPointOfInitiationFormat()).equals("")) {
							remInfo.append("01/"+cimMerchantRequest.getMerchantAccount().getPointOfInitiationFormat()+"/");
						}
						if(cimMerchantRequest.getMerchantAccount().isConvenienceIndicator()) {
							
							if(String.valueOf(cimMerchantRequest.getMerchantAccount().getConvenienceIndicatorFee()).equals("Fixed")) {
								remInfo.append("55/02/56/"+cimMerchantRequest.getMerchantAccount().getConvenienceIndicatorFee()+"/");
								
								Double sumData=Double.parseDouble(cimMerchantRequest.getMerchantAccount().getConvenienceIndicatorFee())+(Double.parseDouble(cimMerchantRequest.getMerchantAccount().getTrAmt()));
								tot_tran_amount=sumData.toString();
							}else if(String.valueOf(cimMerchantRequest.getMerchantAccount().getConvenienceIndicatorFee()).equals("Percentage")) {
								remInfo.append("55/03/57/"+cimMerchantRequest.getMerchantAccount().getConvenienceIndicatorFee()+"/");
							
								Double convFee=(((Double.parseDouble(cimMerchantRequest.getMerchantAccount().getTrAmt()))*(Double.parseDouble(cimMerchantRequest.getMerchantAccount().getConvenienceIndicatorFee())))/100);
								Double sumData=convFee+(Double.parseDouble(cimMerchantRequest.getMerchantAccount().getTrAmt()));
								tot_tran_amount=sumData.toString();
							
							}
						}
						
						if(cimMerchantRequest.getAdditionalDataInformation()!=null) {
							if((!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getBillNumber()).equals("null")&&!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getBillNumber()).equals(""))||
									(!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getMobileNumber()).equals("null")&&!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getMobileNumber()).equals(""))||
									(!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getReferenceLabel()).equals("null")&&!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getReferenceLabel()).equals(""))||
									(!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getStoreLabel()).equals("null")&&!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getStoreLabel()).equals(""))||
									(!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getLoyaltyNumber()).equals("null")&&!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getLoyaltyNumber()).equals(""))||
									(!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getCustomerLabel()).equals("null")&&!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getCustomerLabel()).equals(""))||
									(!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getTerminalLabel()).equals("null")&&!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getTerminalLabel()).equals(""))||
									(!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getAddlDataRequest()).equals("null")&&!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getAddlDataRequest()).equals(""))||
									(!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getPurposeOfTransaction()).equals("null")&&!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getPurposeOfTransaction()).equals(""))) {
								
								
								remInfo.append("62//");
								
								if(!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getBillNumber()).equals("null")&&
										!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getBillNumber()).equals("")	) {
									remInfo.append("01/"+cimMerchantRequest.getAdditionalDataInformation().getBillNumber()+"/");
								}
								if(!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getMobileNumber()).equals("null")&&
										!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getMobileNumber()).equals("")	) {
									remInfo.append("02/"+cimMerchantRequest.getAdditionalDataInformation().getMobileNumber()+"/");
								}
								if(!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getStoreLabel()).equals("null")&&
										!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getStoreLabel()).equals("")
										) {
									remInfo.append("03/"+cimMerchantRequest.getAdditionalDataInformation().getStoreLabel()+"/");
								}
								if(!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getLoyaltyNumber()).equals("null")&&
										!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getLoyaltyNumber()).equals("")
										) {
									remInfo.append("04/"+cimMerchantRequest.getAdditionalDataInformation().getLoyaltyNumber()+"/");
								}
								if(!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getReferenceLabel()).equals("null")&&
										!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getReferenceLabel()).equals("")
										) {
									remInfo.append("05/"+cimMerchantRequest.getAdditionalDataInformation().getReferenceLabel()+"/");
								}
								if(!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getCustomerLabel()).equals("null")&&
										!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getCustomerLabel()).equals("")
										) {
									remInfo.append("06/"+cimMerchantRequest.getAdditionalDataInformation().getCustomerLabel()+"/");
								}
								if(!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getTerminalLabel()).equals("null")&&
										!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getTerminalLabel()).equals("")
										) {
									remInfo.append("07/"+cimMerchantRequest.getAdditionalDataInformation().getTerminalLabel()+"/");
								}
								if(!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getPurposeOfTransaction()).equals("null")&&
										!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getPurposeOfTransaction()).equals("null")) {
									remInfo.append("08/"+cimMerchantRequest.getAdditionalDataInformation().getPurposeOfTransaction()+"/");
									remarks=cimMerchantRequest.getAdditionalDataInformation().getPurposeOfTransaction();
								}
								if(!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getAddlDataRequest()).equals("null")&&
										!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getAddlDataRequest()).equals("null")) {
									remInfo.append("09/"+cimMerchantRequest.getAdditionalDataInformation().getAddlDataRequest()+"/");
								}
							}
							
							remInfo.append("//RQ/");
							
							logger.debug("RemitterInfo->"+remInfo.toString());
						
						}
							
						
						ipsDao.RegisterMerchantRTPRecord(psuDeviceID, psuIpAddress, sysTraceNumber,
								cimMsgID, seqUniqueID, endTOEndID, master_ref_id,msgNetMir,
								instgAgent,instdAgent,debtorAgent,
								debtorAgentAcct,CreditorAgent,CreditorAgentAcct,
								seqUniqueID,"0100",lclInstr,ctgyPurp,ctgyPurp,cimMerchantRequest.getRemitterAccount().getAcctName(),
								cimMerchantRequest.getRemitterAccount().getAcctNumber(),cimMerchantRequest.getMerchantAccount().getPayeeParticipantCode(),
								cimMerchantRequest.getMerchantAccount().getCurrency(),cimMerchantRequest.getMerchantAccount().getMerchantName(),
								cimMerchantRequest.getMerchantAccount().getMerchantAcctNumber(),p_id,
								tot_tran_amount,remarks,
								p_id,p_id,channelID,resvfield1,resvfield2,
								bankAgentTable.getBank_code(),chargeBearer,
								cimMerchantRequest,remInfo.toString());
					

						
					
						////Generate JWT Token
					    ////Create Cryptogram Data
						Map<String, Object> claimsData = new HashMap<String, Object>();
						claimsData.put("deviceId",regAccList.get(0).getCustom_device_id());
						claimsData.put("amount",cimMerchantRequest.getMerchantAccount().getTrAmt());
						claimsData.put("currency",cimMerchantRequest.getMerchantAccount().getCurrency());
						claimsData.put("debtorAccount",cimMerchantRequest.getRemitterAccount().getAcctNumber());
						claimsData.put("creditorAccount",cimMerchantRequest.getMerchantAccount().getMerchantAcctNumber());
						claimsData.put("endToEndId",endTOEndID);
						claimsData.put("consentId", regAccList.get(0).getConsent_id());
				        
						
						String cryptogram="";
						try {
							cryptogram=generateJwtToken(claimsData,regAccList.get(0).getPrivate_key());
							// cryptogram=generateJwtToken(claimsData,listener.retrievePriKey(regAccList.get(0).getCustom_device_id()));

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
												
						
						////// Calling IPSX
						logger.info("Calling IPSX");
						ipsxClient.sendMerchantRTPRequst(
								cimMerchantRequest.getRemitterAccount().getAcctName(),
								cimMerchantRequest.getRemitterAccount().getAcctNumber(),
								cimMerchantRequest.getMerchantAccount().getCurrency(),
								cimMerchantRequest.getMerchantAccount().getMerchantName(),
								cimMerchantRequest.getMerchantAccount().getMerchantAcctNumber(),
								tot_tran_amount,
								remarks,
								seqUniqueID,cimMsgID,msgSeq, endTOEndID,msgNetMir,cryptogram,
								instgAgent,instdAgent,debtorAgent,
								debtorAgentAcct,CreditorAgent,CreditorAgentAcct,lclInstr,ctgyPurp,chargeBearer,
								cimMerchantRequest,remInfo.toString());

				}
			});

			///// Return Master Ref ID
			rtpbulkTransferResponse = new RTPbulkTransferResponse(master_ref_id,
					new SimpleDateFormat("YYYY-MM-dd HH:mm:ss ").format(new Date()));
			return rtpbulkTransferResponse;
	
		
		
	}


	
	public String incomingFundTransferConnection1(String acctNumber, String trAmt, String currency,
			String sysTraceAuditNumber, String SeqUniqueID,String trRmks,SendT request,
			String debrAcctNumber,String debtAcctName,String instgAcct,String ctgyPurp,String rmtInfo) {

		String tranResponse = "";
		logger.info(SeqUniqueID + ": Calling Connect 24 for Account Status");

		//logger.info(String.valueOf(signDocument.verifySignParseDoc(request.getMessage().getBlock4())));
		
		//if(signDocument.verifySignParseDoc(request.getMessage().getBlock4())) {
			
			if (!acctNumber.equals("")) {
				if (!trAmt.equals("0") && !trAmt.equals("0.00")&& !trAmt.equals("")) {
					
		
					////Generate RequestUUID
					String requestUUID=sequence.generateRequestUUId();
					
					String response="";
					
					if(ctgyPurp.equals("100")) {
						response=ipsDao.registerCIMcbsIncomingData(requestUUID,env.getProperty("cimCBS.channelID"),
								env.getProperty("cimCBS.servicereqversion"),env.getProperty("cimCBS.servicereqID"),new Date(),
								sysTraceAuditNumber,env.getProperty("cimCBS.incCRChannel"),"","True","CR","N","",
								acctNumber, trAmt, currency,
								SeqUniqueID,debrAcctNumber,debtAcctName,"NRT/RTP","",rmtInfo,"","");
					}else {
						response=ipsDao.registerCIMcbsIncomingData(requestUUID,env.getProperty("cimCBS.channelID"),
								env.getProperty("cimCBS.servicereqversion"),env.getProperty("cimCBS.servicereqID"),new Date(),
								sysTraceAuditNumber,env.getProperty("cimCBS.rtpCRChannel"),"","True","CR","N","",
								acctNumber, trAmt, currency,
								SeqUniqueID,debrAcctNumber,debtAcctName,"NRT/RTP","",rmtInfo,"","");
					}
					
					
					if(response.equals("1")) {
						ResponseEntity<CimCBSresponse> connect24Response = cimCBSservice
								.cdtFundRequest(requestUUID,acctNumber, trAmt, currency, sysTraceAuditNumber,
										SeqUniqueID,trRmks);

						logger.info("Response->"+connect24Response.toString());
						if (connect24Response.getStatusCode() == HttpStatus.OK) {
							logger.info(SeqUniqueID + ": success");

							if(connect24Response.getBody().getStatus()!=null) {
								if(connect24Response.getBody().getStatus().getIsSuccess()) {
									logger.info(SeqUniqueID + ": success"+connect24Response.getBody().getStatus().getIsSuccess());

									ipsDao.updateCIMcbsData(requestUUID,"SUCCESS",connect24Response.getBody().getStatus().getStatusCode(),connect24Response.getBody().getStatus().getMessage(),connect24Response.getBody().getData().getTransactionNoFromCBS());
									tranResponse = errorCode.ErrorCode("CIM0");
									
								}else {
									logger.info(SeqUniqueID + ": Fail"+connect24Response.getBody().getStatus().getIsSuccess());
									logger.info(SeqUniqueID + ": Fail"+connect24Response.getBody().getStatus().getMessage());

									ipsDao.updateCIMcbsData(requestUUID,"FAILURE",connect24Response.getBody().getStatus().getStatusCode(),connect24Response.getBody().getStatus().getMessage(),connect24Response.getBody().getData().getTransactionNoFromCBS());
									tranResponse = errorCode.ErrorCode(connect24Response.getBody().getStatus().getStatusCode());
								}
							}else {
								ipsDao.updateCIMcbsData(requestUUID,"FAILURE","201","No Response return from CBS","");
								tranResponse = errorCode.ErrorCode("CIM500");
							}
							
						} else {
							ipsDao.updateCIMcbsData(requestUUID,"FAILURE","500","Internal Server Error","");
							tranResponse = errorCode.ErrorCode("CIM500");
						}
					}else {
						tranResponse = errorCode.ErrorCode("CIM500");
					}

				} else {
					logger.info(SeqUniqueID + ": Zero amount");

					ipsDao.updateCBSStatusError(SeqUniqueID, TranMonitorStatus.VALIDATION_ERROR.toString(),
							"Validation Failed/Zero Amount", TranMonitorStatus.FAILURE.toString());
					tranResponse = errorCode.ErrorCode("AM01");
				}
			} else {
				logger.info(SeqUniqueID + ": Creditor Account Number Missing");

				ipsDao.updateCBSStatusError(SeqUniqueID, TranMonitorStatus.VALIDATION_ERROR.toString(),
						"Validation Failed/Zero Amount", TranMonitorStatus.FAILURE.toString());
				tranResponse = errorCode.ErrorCode("AC03");
			}
		/*}else {
			logger.info(SeqUniqueID + ": Signature Failed");

			ipsDao.updateCBSStatusError(SeqUniqueID, TranMonitorStatus.VALIDATION_ERROR.toString(),
					"Signature Failed",
					TranMonitorStatus.FAILURE.toString());

			tranResponse = errorCode.ErrorCode("BOB500");
		}*/
		
			

		return tranResponse;
	}

	

	
	
	public CimMerchantResponse createMerchantQRConnection(String psuDeviceID, String psuIpAddress, String psuID,
			CIMMerchantQRcodeRequest qrrequest,String p_id,
			String channelID,String resvField1,String resvField2)
			throws DatatypeConfigurationException, JAXBException, KeyManagementException, UnrecoverableKeyException,
			KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {

		CimMerchantResponse response=new CimMerchantResponse();

		EncodeQRFormatResponse encodeQRresponse=encodeQRCodeFormat(qrrequest);
							
		if(encodeQRresponse.isSuccess()) {
			String[] displayText= {qrrequest.getMerchantName(),""};
			String[] titletextDesc= {"Scan here to pay"};
			String qrImageCode=generateQRCode(encodeQRresponse.getQrMsg(),displayText,titletextDesc,350,350);
			response.setBase64QR(qrImageCode);
			return response;
		}else {
			//String responseStatus = errorCode.validationError("BIPS17");
			
			System.out.println("QR Code Error:"+encodeQRresponse.getError_desc().get(0).toString());
			throw new IPSXException("BIPS17:"+encodeQRresponse.getError_desc().get(0));
		}
		
	}

	private String generateQRCode(String qrMsg,String[] displayTextQR,String[] titleText,Integer width,Integer height) {
		
		String encodedQRImage="0";
		try {
			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			BitMatrix bitMatrix = qrCodeWriter.encode(qrMsg, BarcodeFormat.QR_CODE, width, height);

			ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
			MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
			byte[] pngData = pngOutputStream.toByteArray();
			int totalTextLineToadd = displayTextQR.length;
			InputStream in = new ByteArrayInputStream(pngData);
			BufferedImage image = ImageIO.read(in);

			BufferedImage outputImage = new BufferedImage(image.getWidth(), image.getHeight() + 15 * totalTextLineToadd,
					BufferedImage.TYPE_INT_ARGB);
			// If text is needed to display
			if (displayTextQR.length > 0) {
				Graphics g = outputImage.getGraphics();
				g.setColor(Color.WHITE);
				g.fillRect(0, 0, outputImage.getWidth(), outputImage.getHeight());
				g.drawImage(image, 0, 0, null);
				g.setFont(new Font("Arial Black", Font.ITALIC, 14));
				Color textColor = Color.BLACK;
				g.setColor(textColor);
				FontMetrics fm = g.getFontMetrics();
				int startingYposition = height + 5;
				for (String displayText : displayTextQR) {
					g.drawString(displayText, (outputImage.getWidth() / 2) - (fm.stringWidth(displayText) / 2),
							startingYposition);
					startingYposition += 20;
				}

				ByteArrayOutputStream outputbytestream1 = new ByteArrayOutputStream();
				ImageIO.write(outputImage, "PNG", outputbytestream1);

				int totalTextLineToadd2 = titleText.length;

				BufferedImage outputImage2 = new BufferedImage(outputImage.getWidth(),
						outputImage.getHeight() + 15 * totalTextLineToadd2, BufferedImage.TYPE_INT_ARGB);
				Graphics gq = outputImage2.getGraphics();
				gq.setColor(Color.WHITE);
				gq.drawImage(outputImage, 0, 0, null);
				gq.setFont(new Font("Arial Black", Font.ITALIC, 16));
				Color textColor2 = Color.BLACK;
				gq.setColor(textColor2);
				FontMetrics fm2 = gq.getFontMetrics();
				int startingYposition2 = height - 330;
				for (String displayText : titleText) {
					gq.drawString(displayText, (outputImage2.getWidth() / 2) - (fm2.stringWidth(displayText) / 2),
							startingYposition2);
					startingYposition2 += 20;
				}

				ByteArrayOutputStream outputbytestream2 = new ByteArrayOutputStream();

				//File outputnew = new File("E:\\log3.png");
				ImageIO.write(outputImage2, "PNG", outputbytestream2);

				BufferedImage outputImage3 = new BufferedImage(800, 800, BufferedImage.TYPE_INT_ARGB);
				Graphics g2 = outputImage3.getGraphics();
				g2.setColor(Color.BLACK);
				g2.drawRect(0, 0, 800, 800);

				g2.dispose();
				
				
				
				Image picture = ImageIO.read(this.getClass().getResourceAsStream(
						"/static/Image/MauCAS_logo.png"));
				//BufferedImage logo = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
				Graphics gn = outputImage3.getGraphics();
				gn.setColor(Color.BLACK);

				//gn.drawImage(picture, 300, 5, 180, 180, null);
				gn.drawImage(picture, 200, 3, 400, 180, null);
				
				gn.dispose();

				Graphics g3 = outputImage3.getGraphics();

				g3.setColor(Color.black);

				g3.drawRoundRect(130, 170, 550, 550, 20, 20);


				g3.drawImage(outputImage2, 230, 250, outputImage2.getWidth(), outputImage2.getHeight(), null);
				g3.dispose();

				ByteArrayOutputStream outputByteStreamDataImage = new ByteArrayOutputStream();

				//File output = new File("E:\\logofinal.png");
				ImageIO.write(outputImage3, "PNG", outputByteStreamDataImage);
				encodedQRImage = Base64.getEncoder().encodeToString(outputByteStreamDataImage.toByteArray());
				
				return encodedQRImage;

			}
		} catch (Exception ex) {
			logger.info(ex.getMessage());
			return encodedQRImage;
		}
		return encodedQRImage;

	}

	private EncodeQRFormatResponse encodeQRCodeFormat(CIMMerchantQRcodeRequest qrReuest) {

		EncodeQRFormatResponse response=new EncodeQRFormatResponse();

		final MerchantPresentedMode merchantPresentMode = new MerchantPresentedMode();
		
		///Payload Format Indicator(00)
		merchantPresentMode.setPayloadFormatIndicator(qrReuest.getPayloadFormatIndiator());
		//Point Of Initiation Method(01)
		if(!String.valueOf(qrReuest.getPointOfInitiationFormat()).equals("null")&&
				!String.valueOf(qrReuest.getPointOfInitiationFormat()).equals("")) {
			merchantPresentMode.setPointOfInitiationMethod(qrReuest.getPointOfInitiationFormat());
		}
		///02-25///
		///	final MerchantAccountInformationTemplate merchanAccountInformationReserved = getMerchanAccountInformationReserved();
		///merchantPresentMode.addMerchantAccountInformation(merchanAccountInformationReserved);
		
		///Maucas Merchant Account Information(26)
		final MerchantAccountInformationReservedAdditional merchantAccountInformationValue = new MerchantAccountInformationReservedAdditional();
		merchantAccountInformationValue.setGloballyUniqueIdentifier(qrReuest.getMerchantAcctInformation().getGlobalID());
		merchantAccountInformationValue.setPayeeParticipantCode(qrReuest.getMerchantAcctInformation().getPayeeParticipantCode());
		merchantAccountInformationValue.setMerchantAccountNumber(qrReuest.getMerchantAcctInformation().getMerchantAcctNumber());
		merchantAccountInformationValue.setMerchantID(qrReuest.getMerchantAcctInformation().getMerchantID());
		final MerchantAccountInformationTemplate merchanAccountInformationReservedAdditional =new MerchantAccountInformationTemplate("26", merchantAccountInformationValue);
		// merchantAccountInformationValue.addPaymentNetworkSpecific(paymentNetworkSpecific);
		merchantPresentMode.addMerchantAccountInformation(merchanAccountInformationReservedAdditional);

		  
		////Merchant Category Code(52)
		merchantPresentMode.setMerchantCategoryCode(qrReuest.getMCC());
		
		
		merchantPresentMode.setTransactionCurrency(Currency.entryOf(qrReuest.getCurrency()).getNumber());

		////Transaction Amount(optional)(54)
		if(!String.valueOf(qrReuest.getTrAmt()).equals("null")&&
				!String.valueOf(qrReuest.getTrAmt()).equals("")	) {
			merchantPresentMode.setTransactionAmount(qrReuest.getTrAmt());
		}

		
		if(qrReuest.isConvenienceIndicator()) {
			if(String.valueOf(qrReuest.getConvenienceIndicatorFeeType()).equals("Fixed")) {

				////Tip or Convenience Indicator(55)
				merchantPresentMode.setTipOrConvenienceIndicator("02");

				///Convenience Indicator Fee Fixed(56)
				merchantPresentMode.setValueOfConvenienceFeeFixed(qrReuest.getConvenienceIndicatorFee());

			}else if(String.valueOf(qrReuest.getConvenienceIndicatorFeeType()).equals("Percentage")) {			

				//// Tip or Convenience Indicator(55)
				merchantPresentMode.setTipOrConvenienceIndicator("03");

				//// Convenience Indicator Fee Percentage(57)
				 merchantPresentMode.setValueOfConvenienceFeePercentage(qrReuest.getConvenienceIndicatorFee());
			}
		}
		
		////Country Code (58)
		merchantPresentMode.setCountryCode(qrReuest.getCountryCode());
		
		////Merchant Name(59)
		merchantPresentMode.setMerchantName(qrReuest.getMerchantName());

		////Merchant City(60)
		merchantPresentMode.setMerchantCity(qrReuest.getCity());

		////Postal Code (61)
		if(!String.valueOf(qrReuest.getPostalCode()).equals("null")&&
				!String.valueOf(qrReuest.getPostalCode()).equals("")	) {
			merchantPresentMode.setPostalCode(qrReuest.getPostalCode());
		}
		

		if(qrReuest.getAdditionalDataInformation()!=null) {

			if((!String.valueOf(qrReuest.getAdditionalDataInformation().getBillNumber()).equals("null")&&!String.valueOf(qrReuest.getAdditionalDataInformation().getBillNumber()).equals(""))||
					(!String.valueOf(qrReuest.getAdditionalDataInformation().getMobileNumber()).equals("null")&&!String.valueOf(qrReuest.getAdditionalDataInformation().getMobileNumber()).equals(""))||
					(!String.valueOf(qrReuest.getAdditionalDataInformation().getStoreLabel()).equals("null")&&!String.valueOf(qrReuest.getAdditionalDataInformation().getStoreLabel()).equals(""))||
					(!String.valueOf(qrReuest.getAdditionalDataInformation().getLoyaltyNumber()).equals("null")&&!String.valueOf(qrReuest.getAdditionalDataInformation().getLoyaltyNumber()).equals(""))||
					(!String.valueOf(qrReuest.getAdditionalDataInformation().getCustomerLabel()).equals("null")&&!String.valueOf(qrReuest.getAdditionalDataInformation().getCustomerLabel()).equals(""))||
					(!String.valueOf(qrReuest.getAdditionalDataInformation().getTerminalLabel()).equals("null")&&!String.valueOf(qrReuest.getAdditionalDataInformation().getTerminalLabel()).equals(""))||
					(!String.valueOf(qrReuest.getAdditionalDataInformation().getAddlDataRequest()).equals("null")&&!String.valueOf(qrReuest.getAdditionalDataInformation().getAddlDataRequest()).equals(""))||
					(!String.valueOf(qrReuest.getAdditionalDataInformation().getReferenceLabel()).equals("null")&&!String.valueOf(qrReuest.getAdditionalDataInformation().getReferenceLabel()).equals(""))||
					(!String.valueOf(qrReuest.getAdditionalDataInformation().getPurposeOfTransaction()).equals("null")&&!String.valueOf(qrReuest.getAdditionalDataInformation().getPurposeOfTransaction()).equals(""))) {
				
			////Additional Data Information(62)
				final AdditionalDataField additionalDataFieldValue = new AdditionalDataField();
				
				if(!String.valueOf(qrReuest.getAdditionalDataInformation().getBillNumber()).equals("null")&&
						!String.valueOf(qrReuest.getAdditionalDataInformation().getBillNumber()).equals("")	) {
					additionalDataFieldValue.setBillNumber(qrReuest.getAdditionalDataInformation().getBillNumber());
				}
				if(!String.valueOf(qrReuest.getAdditionalDataInformation().getMobileNumber()).equals("null")&&
						!String.valueOf(qrReuest.getAdditionalDataInformation().getMobileNumber()).equals("")	) {
					additionalDataFieldValue.setMobileNumber(qrReuest.getAdditionalDataInformation().getMobileNumber());
				}
				if (!String.valueOf(qrReuest.getAdditionalDataInformation().getReferenceLabel()).equals("null")
						&& !String.valueOf(qrReuest.getAdditionalDataInformation().getReferenceLabel()).equals("")) {
					additionalDataFieldValue
							.setReferenceLabel(qrReuest.getAdditionalDataInformation().getReferenceLabel());

				}

				if (!String.valueOf(qrReuest.getAdditionalDataInformation().getPurposeOfTransaction()).equals("null")
						&& !String.valueOf(qrReuest.getAdditionalDataInformation().getPurposeOfTransaction())
								.equals("null")) {
					additionalDataFieldValue
							.setPurposeTransaction(qrReuest.getAdditionalDataInformation().getPurposeOfTransaction());

				}
				
				if(!String.valueOf(qrReuest.getAdditionalDataInformation().getStoreLabel()).equals("null")&&
						!String.valueOf(qrReuest.getAdditionalDataInformation().getStoreLabel()).equals("")
						) {
					additionalDataFieldValue.setReferenceLabel(qrReuest.getAdditionalDataInformation().getStoreLabel());

				}
				
				if(!String.valueOf(qrReuest.getAdditionalDataInformation().getLoyaltyNumber()).equals("null")&&
						!String.valueOf(qrReuest.getAdditionalDataInformation().getLoyaltyNumber()).equals("")
						) {
					additionalDataFieldValue.setReferenceLabel(qrReuest.getAdditionalDataInformation().getLoyaltyNumber());

				}
				
				if(!String.valueOf(qrReuest.getAdditionalDataInformation().getCustomerLabel()).equals("null")&&
						!String.valueOf(qrReuest.getAdditionalDataInformation().getCustomerLabel()).equals("")
						) {
					additionalDataFieldValue.setReferenceLabel(qrReuest.getAdditionalDataInformation().getCustomerLabel());

				}
				
				if(!String.valueOf(qrReuest.getAdditionalDataInformation().getTerminalLabel()).equals("null")&&
						!String.valueOf(qrReuest.getAdditionalDataInformation().getTerminalLabel()).equals("")
						) {
					additionalDataFieldValue.setReferenceLabel(qrReuest.getAdditionalDataInformation().getTerminalLabel());

				}
				
				if(!String.valueOf(qrReuest.getAdditionalDataInformation().getAddlDataRequest()).equals("null")&&
						!String.valueOf(qrReuest.getAdditionalDataInformation().getAddlDataRequest()).equals("")
						) {
					additionalDataFieldValue.setReferenceLabel(qrReuest.getAdditionalDataInformation().getAddlDataRequest());

				}

				
				final AdditionalDataFieldTemplate additionalDataField = new AdditionalDataFieldTemplate();
				additionalDataField.setValue(additionalDataFieldValue);
				
				merchantPresentMode.setAdditionalDataField(additionalDataField);

			}

		}

		
		

		//final MerchantInformationLanguageTemplate merchantInformationLanguage = getMerchantInformationLanguage();
		//final UnreservedTemplate unreserved = getUnreserved();
		//final TagLengthString rFUforEMVCo = new TagLengthString("65", "00");

		//merchantPresentMode.setMerchantInformationLanguage(merchantInformationLanguage);
		
		
		//merchantPresentMode.addRFUforEMVCo(rFUforEMVCo);
		//merchantPresentMode.addUnreserved(unreserved);

		final ValidationResult validationResult = MerchantPresentedModeValidate.validate(merchantPresentMode);
		System.out.println(validationResult.getErrors().toString());

		if (validationResult.isValid()) {
			response.setSuccess(true);
			response.setQrMsg(merchantPresentMode.toString());
		} else {
			response.setSuccess(false);
			List<String> desc=new ArrayList<>();
			Collection<com.bornfire.valid.context.Error> error=validationResult.getErrors();
					for (com.bornfire.valid.context.Error elem : error) {
						desc.add(elem.getMessage());
						break;
					}
			response.setError_desc(desc);
		}
		System.out.println(merchantPresentMode.toString());


		return response;

	}
	

	private static MerchantAccountInformationTemplate getMerchanAccountInformationReserved() {
	  final MerchantAccountInformationReserved merchantAccountInformationValue = new MerchantAccountInformationReserved("0004");

	  return new MerchantAccountInformationTemplate("02", merchantAccountInformationValue);
	}

	// Merchant Account Information Template (IDs "26" to "51")
	private static MerchantAccountInformationTemplate getMerchanAccountInformationReservedAdditional() {
	  /*final TagLengthString paymentNetworkSpecific = new TagLengthString();
	  paymentNetworkSpecific.setTag("01");
	  paymentNetworkSpecific.setValue("abcd");*/

	  final MerchantAccountInformationReservedAdditional merchantAccountInformationValue = new MerchantAccountInformationReservedAdditional();
	  merchantAccountInformationValue.setGloballyUniqueIdentifier("mu.maucas");
	  merchantAccountInformationValue.setPayeeParticipantCode("TSTBMUMUXXXX");
	  merchantAccountInformationValue.setMerchantAccountNumber("01234123412");
	  merchantAccountInformationValue.setMerchantID("000000000000020");
	  
	 // merchantAccountInformationValue.addPaymentNetworkSpecific(paymentNetworkSpecific);

	  return new MerchantAccountInformationTemplate("26", merchantAccountInformationValue);
	}

	private static UnreservedTemplate getUnreserved() {
	  final TagLengthString contextSpecificData = new TagLengthString();
	  contextSpecificData.setTag("07");
	  contextSpecificData.setValue("12345678");

	  final Unreserved value = new Unreserved();
	  value.setGloballyUniqueIdentifier("A011223344998877");
	  value.addContextSpecificData(contextSpecificData);

	  final UnreservedTemplate unreserved = new UnreservedTemplate();
	  unreserved.setValue(value);
	  unreserved.setTag("80");

	  return unreserved;
	}

	private static MerchantInformationLanguageTemplate getMerchantInformationLanguage() {

	  final TagLengthString rFUforEMVCo = new TagLengthString();
	  rFUforEMVCo.setTag("03");
	  rFUforEMVCo.setValue("abcd");

	  final MerchantInformationLanguage merchantInformationLanguageValue = new MerchantInformationLanguage();
	  merchantInformationLanguageValue.setLanguagePreference("");
	  merchantInformationLanguageValue.setMerchantName("");
	  merchantInformationLanguageValue.setMerchantCity("");
	  merchantInformationLanguageValue.addRFUforEMVCo(rFUforEMVCo);

	  final MerchantInformationLanguageTemplate merchantInformationLanguage = new MerchantInformationLanguageTemplate();
	  merchantInformationLanguage.setValue(merchantInformationLanguageValue);

	  return merchantInformationLanguage;
	}

	private static AdditionalDataFieldTemplate getAddtionalDataField() {
	  /*final PaymentSystemSpecific paymentSystemSpecific = new PaymentSystemSpecific();
	  paymentSystemSpecific.setGloballyUniqueIdentifier("1");
	  paymentSystemSpecific.addPaymentSystemSpecific(new TagLengthString("01", "i"));

	  final PaymentSystemSpecificTemplate paymentSystemSpecificTemplate = new PaymentSystemSpecificTemplate();
	  paymentSystemSpecificTemplate.setTag("50");
	  paymentSystemSpecificTemplate.setValue(paymentSystemSpecific);*/

	  final AdditionalDataField additionalDataFieldValue = new AdditionalDataField();
	  //Bill Number (01)
	  additionalDataFieldValue.setBillNumber("BCQ456789");
	  
	  //MobileNumber(02)
	  additionalDataFieldValue.setMobileNumber("52516314");
	  
	  //StoreLabel(03)
	  //additionalDataFieldValue.setStoreLabel("09876");
	  
	  //Loyalty Number(04)
	  //additionalDataFieldValue.setLoyaltyNumber("54321");
	  
	  ///Reference Label(05)
	  additionalDataFieldValue.setReferenceLabel("BOM123");
	  
	  //Custom Label (06)
	  //additionalDataFieldValue.setCustomerLabel("52516314");
	  
	  //Terminal Label(07)
	  //additionalDataFieldValue.setTerminalLabel("klmno");
	 
	  //Purpose Transaction(08)
	  additionalDataFieldValue.setPurposeTransaction("Payment of network fees");
	 
	 //Additional data request
	 // additionalDataFieldValue.setAdditionalConsumerDataRequest("tuvxy");

	 //additionalDataFieldValue.addPaymentSystemSpecific(paymentSystemSpecificTemplate);
	  final AdditionalDataFieldTemplate additionalDataField = new AdditionalDataFieldTemplate();
	  additionalDataField.setValue(additionalDataFieldValue);

	  return additionalDataField;
	}

	public CIMMerchantDecodeQRFormatResponse getMerchantQRdata(String psuDeviceID, String psuIpAddress,
			String psuID,CIMMerchantQRRequestFormat cimQRFormatData,
			String p_id, String channelID, String resvfield1, String resvfield2) {

		  final ValidationResult validationResult = Crc16Validate.validate(cimQRFormatData.getQrCode());

		if (validationResult.isValid()) {
			final MerchantPresentedMode merchantPresentedMode = DecoderMpm.decode(cimQRFormatData.getQrCode(), MerchantPresentedMode.class);

			CIMMerchantDecodeQRFormatResponse response=new CIMMerchantDecodeQRFormatResponse();
			
			response.setPayloadFormatIndiator(merchantPresentedMode.getPayloadFormatIndicator().getValue());
			
			/*if(!String.valueOf(merchantPresentedMode.getPointOfInitiationMethod()).equals("null")) {
				response.setPointOfInitiationFormat(merchantPresentedMode.getPointOfInitiationMethod().getValue());
			}
			*/

			if(!String.valueOf(merchantPresentedMode.getPointOfInitiationMethod()).equals("null")&&
					!String.valueOf(merchantPresentedMode.getPointOfInitiationMethod()).equals("")){
						response.setPointOfInitiationFormat(merchantPresentedMode.getPointOfInitiationMethod().getValue());
					}
			
			Map<String, MerchantAccountInformationTemplate> merTemplate = merchantPresentedMode.getMerchantAccountInformation();

			CIMMerchantDecodeQRMerchantAcctInfo acctInfo=new CIMMerchantDecodeQRMerchantAcctInfo();

			 for (final Entry<String, MerchantAccountInformationTemplate> entry : merTemplate.entrySet()) {
				  System.out.println(entry.getValue().getValue());
				  MerchantAccountInformationReservedAdditional ss=(MerchantAccountInformationReservedAdditional) entry.getValue().getValue();
				  
				  if(!String.valueOf(ss.getGloballyUniqueIdentifier()).equals("null")&&
							!String.valueOf(ss.getGloballyUniqueIdentifier()).equals("")){
					  acctInfo.setGlobalID(ss.getGloballyUniqueIdentifier().getValue());
				  }
				  
				  if(!String.valueOf(ss.getPayeeParticipantCode()).equals("null")&&
							!String.valueOf(ss.getPayeeParticipantCode()).equals("")){
					  acctInfo.setPayeeParticipantCode(ss.getPayeeParticipantCode().getValue());
				  }
				  
				  if(!String.valueOf(ss.getMerchantAccountNumber()).equals("null")&&
							!String.valueOf(ss.getMerchantAccountNumber()).equals("")){
					  acctInfo.setMerchantAcctNumber(ss.getMerchantAccountNumber().getValue());
				  }

				  if(!String.valueOf(ss.getMerchantID()).equals("null")&&
							!String.valueOf(ss.getMerchantID()).equals("")){
					  acctInfo.setMerchantID(ss.getMerchantID().getValue());
				  }
				  
				  response.setMerchantAcctInformation(acctInfo);

			  }
			
			if(!String.valueOf(merchantPresentedMode.getMerchantCategoryCode()).equals("null")&&
					!String.valueOf(merchantPresentedMode.getMerchantCategoryCode()).equals("")){
						response.setMCC(merchantPresentedMode.getMerchantCategoryCode().getValue());
					}
			
			if(!String.valueOf(merchantPresentedMode.getTransactionCurrency()).equals("null")&&
					!String.valueOf(merchantPresentedMode.getTransactionCurrency()).equals("")){

						response.setCurrency(Currency.entryOf1(merchantPresentedMode.getTransactionCurrency().getValue()).getCode());
					}
			
			if(!String.valueOf(merchantPresentedMode.getTransactionAmount()).equals("null")&&
			!String.valueOf(merchantPresentedMode.getTransactionAmount()).equals("")){
				response.setTrAmt(merchantPresentedMode.getTransactionAmount().getValue());
			}
			if(!String.valueOf(merchantPresentedMode.getTipOrConvenienceIndicator()).equals("null")&&
			!String.valueOf(merchantPresentedMode.getTipOrConvenienceIndicator()).equals(""))
			{
				if(merchantPresentedMode.getTipOrConvenienceIndicator().getValue().equals("02")) {
					response.setConvenienceIndicator(true);
					response.setConvenienceIndicatorFeeType("Fixed");
					response.setConvenienceIndicatorFee(merchantPresentedMode.getValueOfConvenienceFeeFixed().getValue());
				}else if(merchantPresentedMode.getTipOrConvenienceIndicator().getValue().equals("03")) {
					response.setConvenienceIndicator(true);
					response.setConvenienceIndicatorFeeType("Percentage");
					response.setConvenienceIndicatorFee(merchantPresentedMode.getValueOfConvenienceFeePercentage().getValue());

				}else {
					response.setConvenienceIndicator(false);
				}
			}
			
			
			response.setCountryCode(merchantPresentedMode.getCountryCode().getValue());
			response.setMerchantName(merchantPresentedMode.getMerchantName().getValue());
			response.setCity(merchantPresentedMode.getMerchantCity().getValue());
			if(!String.valueOf(merchantPresentedMode.getPostalCode()).equals("null")&&
					!String.valueOf(merchantPresentedMode.getPostalCode()).equals("")){
						response.setPostalCode(merchantPresentedMode.getPostalCode().getValue());
					}
			
			if(merchantPresentedMode.getAdditionalDataField()!=null) {
				CIMMerchantDecodeQRMerchantAddlInfo addInfo=new CIMMerchantDecodeQRMerchantAddlInfo();
				AdditionalDataFieldTemplate merAddTemplate = merchantPresentedMode.getAdditionalDataField();

				if(!String.valueOf(merAddTemplate.getValue().getBillNumber()).equals("null")&&
						!String.valueOf(merAddTemplate.getValue().getBillNumber()).equals("")) {
					addInfo.setBillNumber(merAddTemplate.getValue().getBillNumber().getValue());	
				}
				if(!String.valueOf(merAddTemplate.getValue().getMobileNumber()).equals("null")&&
						!String.valueOf(merAddTemplate.getValue().getMobileNumber()).equals("")) {
					addInfo.setMobileNumber(merAddTemplate.getValue().getMobileNumber().getValue());	
				}
				if(!String.valueOf(merAddTemplate.getValue().getStoreLabel()).equals("null")&&
						!String.valueOf(merAddTemplate.getValue().getStoreLabel()).equals("")) {
					addInfo.setStoreLabel(merAddTemplate.getValue().getStoreLabel().getValue());	
				}
				if(!String.valueOf(merAddTemplate.getValue().getLoyaltyNumber()).equals("null")&&
						!String.valueOf(merAddTemplate.getValue().getLoyaltyNumber()).equals("")) {
					addInfo.setLoyaltyNumber(merAddTemplate.getValue().getLoyaltyNumber().getValue());	
				}
				if(!String.valueOf(merAddTemplate.getValue().getReferenceLabel()).equals("null")&&
						!String.valueOf(merAddTemplate.getValue().getReferenceLabel()).equals("")) {
					addInfo.setReferenceLabel(merAddTemplate.getValue().getReferenceLabel().getValue());	
				}
				if(!String.valueOf(merAddTemplate.getValue().getCustomerLabel()).equals("null")&&
						!String.valueOf(merAddTemplate.getValue().getCustomerLabel()).equals("")) {
					addInfo.setCustomerLabel(merAddTemplate.getValue().getCustomerLabel().getValue());	
				}
				if(!String.valueOf(merAddTemplate.getValue().getTerminalLabel()).equals("null")&&
						!String.valueOf(merAddTemplate.getValue().getTerminalLabel()).equals("")) {
					addInfo.setTerminalLabel(merAddTemplate.getValue().getTerminalLabel().getValue());	
				}
				if(!String.valueOf(merAddTemplate.getValue().getPurposeTransaction()).equals("null")&&
						!String.valueOf(merAddTemplate.getValue().getPurposeTransaction()).equals("")) {
					addInfo.setPurposeOfTransaction(merAddTemplate.getValue().getPurposeTransaction().getValue());	
				}
				if(!String.valueOf(merAddTemplate.getValue().getAdditionalConsumerDataRequest()).equals("null")&&
						!String.valueOf(merAddTemplate.getValue().getAdditionalConsumerDataRequest()).equals("")) {
					addInfo.setAddlDataRequest(merAddTemplate.getValue().getAdditionalConsumerDataRequest().getValue());	
				}
				
				response.setAdditionalDataInformation(addInfo);
			}
			
			return response;
					 
		} else {
			System.out.println(validationResult.getErrors().toString());
			throw new IPSXException(errorCode.validationError("BIPS18"));
		}
		
	}

	


	
}
