package com.bornfire.controller;

 
/*CREATED BY	: KALAIVANAN RAJENDRAN.R
CREATED ON	: 30-DEC-2019
PURPOSE		: IPS Connection(Connection between BIPS,Connect 24 and IPSx Webservice)
*/

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.KeyFactory;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.stereotype.Component;
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
import com.bornfire.entity.ConsentAccessRequest;
import com.bornfire.entity.ConsentAccessResponse;
import com.bornfire.entity.ConsentAccessTable;
import com.bornfire.entity.ConsentAccessTableRep;
import com.bornfire.entity.ConsentAccountBalance;
import com.bornfire.entity.ConsentRequest;
import com.bornfire.entity.ConsentResponse;
import com.bornfire.entity.CreditTransferTransaction;
import com.bornfire.entity.CryptogramResponse;
import com.bornfire.entity.Data;
import com.bornfire.entity.IPSChargesAndFeesRep;
import com.bornfire.entity.Links;
import com.bornfire.entity.MCCreditTransferRequest;
import com.bornfire.entity.MCCreditTransferResponse;
import com.bornfire.entity.ManualFndTransferRequest;
import com.bornfire.entity.OtherBankDetResponse;
import com.bornfire.entity.ReadConsentBalance;
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
import com.bornfire.exception.Connect24Exception;
import com.bornfire.exception.IPSXException;
import com.bornfire.exception.IPSXRestException;
import com.bornfire.exception.ServerErrorException;
import com.bornfire.jaxb.wsdl.SendT;
import com.bornfire.messagebuilder.SignDocument;
import com.google.gson.Gson;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

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
	
	


	
	/////Fund Transfer Connection
	////Debit Customer Account Credit Settl Account(Connect 24)
	////Send Packages to IPSX,Credit to IPSX Account
	public MCCreditTransferResponse createFTConnection(String psuDeviceID, String psuIpAddress, String psuID,
			String senderParticipantBIC, String participantSOL, MCCreditTransferRequest mcCreditTransferRequest)
			throws DatatypeConfigurationException, JAXBException, KeyManagementException, UnrecoverableKeyException,
			KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {

		MCCreditTransferResponse mcCreditTransferResponse = null;

		///// Generate Sequence Unique ID
		String seqUniqueID = sequence.generateSeqUniqueID();
		///// Generate Bob Msg ID
		String bobMsgID = seqUniqueID;
		///// Generate SystemTraceAuditNumber
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
		logger.info("System Sequence ID" + bobMsgID);
		
		logger.info("Register Initial outgoing Fund Transfer Record");
		
		///Purpose Code for Peer to Peer Connection
		mcCreditTransferRequest.setPurpose("100");
		
		///// Get Other Bank Agent and Agent Account number
		BankAgentTable othBankAgent = ipsDao.getOtherBankAgent(
				mcCreditTransferRequest.getToAccount().getBankCode());
		logger.info(othBankAgent.getBank_agent(),
				"" + othBankAgent.getBank_agent_account());

		///// Register OutGoing Message
		ipsDao.RegisterOutMsgRecord(psuDeviceID, psuIpAddress, psuID,
				senderParticipantBIC, participantSOL, sysTraceNumber,
				mcCreditTransferRequest, bobMsgID, seqUniqueID, endTOEndID,mcCreditTransferRequest.getPurpose(),msgNetMir,
				env.getProperty("ipsx.bicfi"),othBankAgent.getBank_agent(),env.getProperty("ipsx.dbtragt"),env.getProperty("ipsx.dbtragtacct"),
				othBankAgent.getBank_agent(),othBankAgent.getBank_agent_account(),
				seqUniqueID,"0100","CSDC",mcCreditTransferRequest.getPurpose());

		///// Update TranMonitor Table
		ipsDao.updateINOUT(seqUniqueID, "MC_IN");


		ResponseEntity<C24FTResponse> connect24Response = null;
		try {

			if (!mcCreditTransferRequest.getFrAccount().getAcctName().equals("")) {
				if (!mcCreditTransferRequest.getFrAccount().getAcctNumber().equals("")) {
					if (!mcCreditTransferRequest.getToAccount().getAcctName().equals("")) {
						if (!mcCreditTransferRequest.getToAccount().getAcctNumber().equals("")) {
							if (!mcCreditTransferRequest.getTrAmt().equals("")) {
								if (!mcCreditTransferRequest.getTrAmt().equals("0")
										&& !mcCreditTransferRequest.getTrAmt().equals("0.00")) {
									if (Double.parseDouble(mcCreditTransferRequest.getTrAmt())<=Integer.parseInt(env.getProperty("bob.maxamount"))) {
										if (!mcCreditTransferRequest.getCurrencyCode().equals("")) {
											if (mcCreditTransferRequest.getCurrencyCode().equals(env.getProperty("bob.crncycode"))) {
												if(ipsDao.invalidBankCode(mcCreditTransferRequest.getToAccount().getBankCode())) {
													String responseStatus = errorCode.validationError("BIPS10");
													ipsDao.updateCBSStatusError(seqUniqueID,
															TranMonitorStatus.VALIDATION_ERROR.toString(),
															responseStatus.split(":")[1],
															TranMonitorStatus.FAILURE.toString());
													throw new IPSXException(responseStatus);
												}else {
												
													if (mcCreditTransferRequest.getFrAccount().getAcctNumber().length() == 14) {

													
														///// Calling Connect 24 for DEBIT
														logger.info("Send message to Connect24");
														connect24Response = connect24Service.DbtFundRequest(
																senderParticipantBIC, participantSOL, mcCreditTransferRequest,
																sysTraceNumber, seqUniqueID,"MC/"+mcCreditTransferRequest.getToAccount().getBankCode()+
																"/"+mcCreditTransferRequest.getToAccount().getAcctNumber()+"/"+
																		mcCreditTransferRequest.getToAccount().getAcctName());

														///// Return Status Code 200 from Connect 24
														if (connect24Response.getStatusCode() == HttpStatus.OK) {

															logger.info("Connect24 Processed Successfully");
															logger.info("Update CBS Debit OK Status to Table");

															///// Update CBS Status
															ipsDao.updateCBSStatus(seqUniqueID,
																	TranMonitorStatus.CBS_DEBIT_OK.toString(),
																	TranMonitorStatus.IN_PROGRESS.toString());


															///// Call IPSX
															logger.info("Calling IPSX");
															mcCreditTransferResponse = ipsxClient.sendftRequst(
																	senderParticipantBIC, participantSOL,
																	mcCreditTransferRequest, sysTraceNumber, bobMsgID,
																	seqUniqueID, othBankAgent, msgSeq, endTOEndID,msgNetMir);
															return mcCreditTransferResponse;
															/*************************/

														}
														///// Return Status Code 500 from Connect 24
														else if (connect24Response
																.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {

															///// update CBS Status Error
															ipsDao.updateCBSStatusError(seqUniqueID,
																	TranMonitorStatus.CBS_DEBIT_ERROR.toString(),
																	TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(),
																	TranMonitorStatus.FAILURE.toString());

															ipsDao.updateINOUT(seqUniqueID, "MC_OUT");

															throw new ServerErrorException(SERVER_ERROR);
														} else {
															///// update CBS Status Error
															ipsDao.updateCBSStatusError(seqUniqueID,
																	TranMonitorStatus.CBS_DEBIT_ERROR.toString(),
																	connect24Response.getBody().getError_desc().get(0)
																			.toString(),
																	TranMonitorStatus.FAILURE.toString());

															ipsDao.updateINOUT(seqUniqueID, "MC_OUT");

															throw new Connect24Exception(errorCode.ErrorCode(connect24Response.getBody().getError()));
														}
													} else {
														String responseStatus = errorCode.validationError("BIPS9");
														ipsDao.updateCBSStatusError(seqUniqueID,
																TranMonitorStatus.VALIDATION_ERROR.toString(),
																responseStatus.split(":")[1],
																TranMonitorStatus.FAILURE.toString());
														throw new IPSXException(responseStatus);
													}
												}
												
											} else {
												String responseStatus = errorCode.validationError("BIPS8");
												ipsDao.updateCBSStatusError(seqUniqueID,
														TranMonitorStatus.VALIDATION_ERROR.toString(),
														responseStatus.split(":")[1], TranMonitorStatus.FAILURE.toString());
												throw new IPSXException(responseStatus);
											}
										} else {
											String responseStatus = errorCode.validationError("BIPS7");
											ipsDao.updateCBSStatusError(seqUniqueID,
													TranMonitorStatus.VALIDATION_ERROR.toString(),
													responseStatus.split(":")[1], TranMonitorStatus.FAILURE.toString());
											throw new IPSXException(responseStatus);
										}
									}else {
										String responseStatus = errorCode.validationError("BIPS12");
										ipsDao.updateCBSStatusError(seqUniqueID,
												TranMonitorStatus.VALIDATION_ERROR.toString(), responseStatus.split(":")[1],
												TranMonitorStatus.FAILURE.toString());
										throw new IPSXException(responseStatus);
									}
									
								} else {
									String responseStatus = errorCode.validationError("BIPS6");
									ipsDao.updateCBSStatusError(seqUniqueID,
											TranMonitorStatus.VALIDATION_ERROR.toString(), responseStatus.split(":")[1],
											TranMonitorStatus.FAILURE.toString());
									throw new IPSXException(responseStatus);
								}

							} else {
								String responseStatus = errorCode.validationError("BIPS5");
								ipsDao.updateCBSStatusError(seqUniqueID, TranMonitorStatus.VALIDATION_ERROR.toString(),
										responseStatus.split(":")[1], TranMonitorStatus.FAILURE.toString());
								throw new IPSXException(responseStatus);
							}

						} else {
							String responseStatus = errorCode.validationError("BIPS4");
							ipsDao.updateCBSStatusError(seqUniqueID, TranMonitorStatus.VALIDATION_ERROR.toString(),
									responseStatus.split(":")[1], TranMonitorStatus.FAILURE.toString());
							throw new IPSXException(responseStatus);
						}

					} else {
						String responseStatus = errorCode.validationError("BIPS3");
						ipsDao.updateCBSStatusError(seqUniqueID, TranMonitorStatus.VALIDATION_ERROR.toString(),
								responseStatus.split(":")[1], TranMonitorStatus.FAILURE.toString());
						throw new IPSXException(responseStatus);
					}

				} else {
					String responseStatus = errorCode.validationError("BIPS2");
					ipsDao.updateCBSStatusError(seqUniqueID, TranMonitorStatus.VALIDATION_ERROR.toString(),
							responseStatus.split(":")[1], TranMonitorStatus.FAILURE.toString());
					throw new IPSXException(responseStatus);
				}
			} else {
				String responseStatus = errorCode.validationError("BIPS1");
				ipsDao.updateCBSStatusError(seqUniqueID, TranMonitorStatus.VALIDATION_ERROR.toString(),
						responseStatus.split(":")[1], TranMonitorStatus.FAILURE.toString());
				throw new IPSXException(responseStatus);
			}

		} catch (RemoteAccessException e) {
			logger.error(e.getMessage());
		}
		return mcCreditTransferResponse;

	}

	////Bulk Credit Transaction
	public MCCreditTransferResponse createBulkCreditConnection(String psuDeviceID, String psuIpAddress, String psuID,
			String senderParticipantBIC, String participantSOL, BukCreditTransferRequest mcCreditTransferRequest,String userID) {

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
									if (connect24ResponseAccContactExist.getBody().getCurrencyCode().equals(env.getProperty("bob.crncycode"))) {
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

		return mcCreditTransferResponse;
	}

	/////Bulk Debit Transaction
	
	public MCCreditTransferResponse createBulkDebitConnection(String psuDeviceID, String psuIpAddress,
			BulkDebitFndTransferRequest bulkDebitFndTransferRequest,String userID) {

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
										
										if (connect24ResponseAccContactExist.getBody().getCurrencyCode().equals(env.getProperty("bob.crncycode"))) {
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
												/*************************/

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
							env.getProperty("bob.crncycode"), sysTraceNumber, bobMsgID + "/1", seqUniqueID + "/1", othBankAgent, msgSeq,
							endTOEndID,msgNetMir);
				}

			}
		});

		///// Return Master Ref ID:
		mcCreditTransferResponse = new MCCreditTransferResponse(seqUniqueID,
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()));

		return mcCreditTransferResponse;
	}

	public MCCreditTransferResponse createMAnualTransaction(String psuDeviceID, String psuIpAddress,
			List<ManualFndTransferRequest> manualFundTransferRequest,String userID)
			{

		///// Generate Master Ref Id
		String master_ref_id = "BARB" + sequence.generateSystemTraceAuditNumber();

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
					String bobMsgID = seqUniqueID;
					///// Generate Msg Sequence
					String msgSeq = sequence.generateMsgSequence();
					///// Generate End To End ID
					String endTOEndID = env.getProperty("ipsx.bicfi")
							+ new SimpleDateFormat("yyyyMMdd").format(new Date()) + msgSeq;
					
				    /////Net Mir
					String msgNetMir=new SimpleDateFormat("yyMMdd").format(new Date())+env.getProperty("ipsx.user")+"0001"+msgSeq;

					///// Register Manual Record
					logger.info("Register Initial outgoing Fund Transfer Record");
					
					
				    ////// Get Bank Agent Account and Agent Account
					BankAgentTable othBankAgent = ipsDao
												.getOtherBankAgent(manualFundTransferRequest.get(i).getBeneficiaryBankCode());
					
					ipsDao.RegisterManualTranRecord(psuDeviceID, psuIpAddress, sysTraceNumber,
							manualFundTransferRequest.get(i), bobMsgID, seqUniqueID, endTOEndID, master_ref_id,msgNetMir,
							env.getProperty("ipsx.bicfi"),othBankAgent.getBank_agent(),env.getProperty("ipsx.dbtragt"),env.getProperty("ipsx.dbtragtacct"),
							othBankAgent.getBank_agent(),othBankAgent.getBank_agent_account(),seqUniqueID,"0100","CSDC","100","100",userID);

					ResponseEntity<C24FTResponse> connect24Response = null;

					ResponseEntity<AccountContactResponse> connect24ResponseAccContactExist = connect24Service
							.getAccountContact("", "", "",
									manualFundTransferRequest.get(i).getRemitterAcctNumber());
					
					if (connect24ResponseAccContactExist.getStatusCode() == HttpStatus.OK) {
						if (connect24ResponseAccContactExist.getBody().getStatus().equals("0")) {
							if (connect24ResponseAccContactExist.getBody().getSchmType().equals("SBA")
									|| connect24ResponseAccContactExist.getBody().getSchmType().equals("CAA")
									|| connect24ResponseAccContactExist.getBody().getSchmType().equals("ODA")) {
								if (connect24ResponseAccContactExist.getBody().getAccountStatus().equals("A")) {
									if (connect24ResponseAccContactExist.getBody().getFrezCode().equals("T")
											|| connect24ResponseAccContactExist.getBody().getFrezCode().equals("D")
											|| connect24ResponseAccContactExist.getBody().getFrezCode().equals("C")) {

										ipsDao.updateCBSStatusError(seqUniqueID,
												TranMonitorStatus.VALIDATION_ERROR.toString(),
												"Transaction forbidden on this type of account/" + "Freeze Account",
												TranMonitorStatus.FAILURE.toString());
									}else {
										if (connect24ResponseAccContactExist.getBody().getCurrencyCode().equals(env.getProperty("bob.crncycode"))) {
										////// Calling Connect 24 for Manual Transaction
											logger.info("Send message to Connect24");
											connect24Response = connect24Service.ManualDbtFundRequest(manualFundTransferRequest.get(i),
													sysTraceNumber, seqUniqueID,manualFundTransferRequest.get(i).getBeneficiaryBankCode()+"/"+
															manualFundTransferRequest.get(i).getBeneficiaryAcctNumber()+"/"+
															manualFundTransferRequest.get(i).getBeneficiaryName());

											///// Return Status Code 200 from Connect 24
											if (connect24Response.getStatusCode() == HttpStatus.OK) {

												logger.info("Connect24 Processed Successfully");
												logger.info("Update CBS Debit OK Status to Table");
												ipsDao.updateCBSStatus(seqUniqueID, TranMonitorStatus.CBS_DEBIT_OK.toString(),
														TranMonitorStatus.IN_PROGRESS.toString());

												
												logger.info(othBankAgent.getBank_agent(), "" + othBankAgent.getBank_agent_account());
												

												////// Calling IPSX
												logger.info("Calling IPSX");
												MCCreditTransferResponse response = ipsxClient.sendManualftRequst(
														manualFundTransferRequest.get(i).getRemitterName(),
														manualFundTransferRequest.get(i).getRemitterAcctNumber(),
														manualFundTransferRequest.get(i).getBeneficiaryName(),
														manualFundTransferRequest.get(i).getBeneficiaryAcctNumber(),
														manualFundTransferRequest.get(i).getTrAmt(),
														manualFundTransferRequest.get(i).getCurrencyCode(), sysTraceNumber, bobMsgID,
														seqUniqueID, othBankAgent, msgSeq, endTOEndID,msgNetMir);

											} else if (connect24Response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
												ipsDao.updateCBSStatusError(seqUniqueID, TranMonitorStatus.CBS_DEBIT_ERROR.toString(),
														TranMonitorStatus.CBS_SERVER_NOT_CONNECTED.toString(),
														TranMonitorStatus.FAILURE.toString());
												ipsDao.updateINOUT(seqUniqueID, "MC_OUT");

											} else {
												ipsDao.updateCBSStatusError(seqUniqueID, TranMonitorStatus.CBS_DEBIT_ERROR.toString(),
														connect24Response.getBody().getError_desc().get(0).toString(),
														TranMonitorStatus.FAILURE.toString());

												ipsDao.updateINOUT(seqUniqueID, "MC_OUT");

											}
										}else {
											ipsDao.updateCBSStatusError(seqUniqueID,
													TranMonitorStatus.VALIDATION_ERROR.toString(),
													"Invalid Currency Code",
													TranMonitorStatus.FAILURE.toString());
										}
									} 
									
								}else {
									if (connect24ResponseAccContactExist.getBody().getAccountStatus().equals("C")) {

										ipsDao.updateCBSStatusError(seqUniqueID,
												TranMonitorStatus.VALIDATION_ERROR.toString(), "Closed Account Number",
												TranMonitorStatus.FAILURE.toString());

									} else {
										ipsDao.updateCBSStatusError(seqUniqueID,
												TranMonitorStatus.VALIDATION_ERROR.toString(), "Blocked Account Number",
												TranMonitorStatus.FAILURE.toString());

									}
								}
								
							}else {
								ipsDao.updateCBSStatusError(seqUniqueID,
										TranMonitorStatus.VALIDATION_ERROR.toString(),
										"Transaction forbidden on this type of account",
										TranMonitorStatus.FAILURE.toString());
							}
						}else {
							if (connect24ResponseAccContactExist.getBody().getStatus().equals("1")) {

								ipsDao.updateCBSStatusError(seqUniqueID,
										TranMonitorStatus.VALIDATION_ERROR.toString(),
										"Incorrect Account Debtor Number", TranMonitorStatus.FAILURE.toString());

							} else {
								ipsDao.updateCBSStatusError(seqUniqueID,
										TranMonitorStatus.VALIDATION_ERROR.toString(),
										"Transaction forbidden on this type of account/" + "Invalid Account Number",
										TranMonitorStatus.FAILURE.toString());

							}
						}
					}else {
						ipsDao.updateCBSStatusError(seqUniqueID,
								TranMonitorStatus.VALIDATION_ERROR.toString(), "Technical Problem",
								TranMonitorStatus.FAILURE.toString());
					}
					
					

				}

			}
		});

		///// Return Master Ref ID
		mcCreditTransferResponse = new MCCreditTransferResponse(master_ref_id,
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()));
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

								if (connect24ResponseAccContactExist.getBody().getCurrencyCode().equals(env.getProperty("bob.crncycode"))) {

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

								if (connect24ResponseAccContactExist.getBody().getCurrencyCode().equals(env.getProperty("bob.crncycode"))) {

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
						.getAccountContact(psuDeviceID, psuIPAddress, psuID, consentRequest.getAccount().get(0).getIdentification());

				if (connect24ResponseAccContactExist.getStatusCode() == HttpStatus.OK) {

					if (connect24ResponseAccContactExist.getBody().getStatus().equals("0")) {

						if (connect24ResponseAccContactExist.getBody().getSchmType().equals("SBA")
								|| connect24ResponseAccContactExist.getBody().getSchmType().equals("CAA")
								|| connect24ResponseAccContactExist.getBody().getSchmType().equals("ODA")) {

							if (connect24ResponseAccContactExist.getBody().getAccountStatus().equals("A")) {

								if (connect24ResponseAccContactExist.getBody().getCurrencyCode().equals(env.getProperty("bob.crncycode"))) {

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
		
		ConsentAccountBalance consentAcctBalance=null;
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
				
				readAcctBalance.setCreditDebitIndicator(TranMonitorStatus.Credit);
				readAcctBalance.setType(TranMonitorStatus.InterimAvailable);
				
				readAcctBalance.setDateTime(listener.getxmlGregorianCalender("0"));
				
				consentAcctBalance.setBalance(readAcctBalance);
				
				Links links = new Links();
				links.setSelf("/accounts/"+consentID+"/balances");
				consentAcctBalance.setLinks(links);
				
				////Update Tran Non CBS Table
				ipsDao.updateNonCBSData(consentID,x_request_id,accountID,sequence.generateSystemTraceAuditNumber(),
						TranMonitorStatus.ReadBalances.toString(),"Success","");
				
				return consentAcctBalance;
			} else {
				ipsDao.updateNonCBSData(consentID,x_request_id,accountID,sequence.generateSystemTraceAuditNumber(),
						TranMonitorStatus.ReadBalances.toString(),"Failure",balannce.getBody().getError_desc().get(0));
				throw new IPSXRestException(errorCode.ErrorCodeRegistration("0"));
			}
		}else {
			throw new IPSXRestException(errorCode.ErrorCodeRegistration("0"));
		}
	}
	
	public AccountListResponse accountList(String x_request_id, String sender_participant_bic,
			String sender_participant_member_id, String receiver_participant_bic, String receiver_participant_member_id,
			String psuDeviceID, String psuIPAddress, String psuID, String psuIDCountry, String psuIDType,
			String consentID, String cryptogram) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public AccountsListAccounts accountListInd(String x_request_id, String sender_participant_bic,
			String sender_participant_member_id, String receiver_participant_bic, String receiver_participant_member_id,
			String psuDeviceID, String psuIPAddress, String psuID, String psuIDCountry, String psuIDType,
			String consentID, String cryptogram,String accountID) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public TransactionListResponse tranList(String x_request_id, String sender_participant_bic,
			String sender_participant_member_id, String receiver_participant_bic, String receiver_participant_member_id,
			String psuDeviceID, String psuIPAddress, String psuID, String psuIDCountry, String psuIDType,
			String consentID, String cryptogram, String accountID, String fromBookingDateTime,
			String toBookingDateTime) {
		// TODO Auto-generated method stub
		return null;
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

									if (connect24ResponseAccContactExist.getBody().getCurrencyCode().equals(env.getProperty("bob.crncycode"))) {

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

	/////Inward Transaction 
	////Calling Connect 24 Debit Settle account and credit customer account
	public String incomingFundTransferConnection(String acctNumber, String trAmt, String currency,
			String sysTraceAuditNumber, String SeqUniqueID,String trRmks,SendT request) {

		String tranResponse = "";
		logger.info(SeqUniqueID + ": Calling Connect 24 for Account Status");

		logger.info(String.valueOf(signDocument.verifySignParseDoc(request.getMessage().getBlock4())));
		
		if(signDocument.verifySignParseDoc(request.getMessage().getBlock4())) {
			
			if (!acctNumber.equals("")) {
				if (!trAmt.equals("0") && !trAmt.equals("0.00")&& !trAmt.equals("")) {
					
					/*ResponseEntity<C24FTResponse> connect24Response = connect24Service
							.cdtFundRequest(acctNumber, trAmt, currency, sysTraceAuditNumber,
									SeqUniqueID);

					if (connect24Response.getStatusCode() == HttpStatus.OK) {
						logger.info(SeqUniqueID + ": success");

						tranResponse = errorCode.ErrorCode("BOB0");
					} else {

						logger.info(SeqUniqueID + ": failed");

						ipsDao.updateCBSStatusError(SeqUniqueID,
								TranMonitorStatus.CBS_CREDIT_ERROR.toString(),
								connect24Response.getBody().getError_desc().get(0),
								TranMonitorStatus.FAILURE.toString());

						tranResponse = errorCode
								.ErrorCode(connect24Response.getBody().getError().toString());
					}*/

					ResponseEntity<AccountContactResponse> connect24ResponseAccContactExist = connect24Service
							.getAccountContact("", "", "", acctNumber);

					if (connect24ResponseAccContactExist.getStatusCode() == HttpStatus.OK) {

						logger.info(SeqUniqueID + ": Got Account Status Sucessfully");

						if (connect24ResponseAccContactExist.getBody().getStatus().equals("0")) {

							if (connect24ResponseAccContactExist.getBody().getSchmType().equals("SBA")
									|| connect24ResponseAccContactExist.getBody().getSchmType().equals("CAA")
									|| connect24ResponseAccContactExist.getBody().getSchmType().equals("ODA")) {

								if (connect24ResponseAccContactExist.getBody().getAccountStatus().equals("A")) {

									logger.info(SeqUniqueID + " :Account is Active");

									if (connect24ResponseAccContactExist.getBody().getFrezCode().equals("T")
											|| connect24ResponseAccContactExist.getBody().getFrezCode().equals("C")
											|| connect24ResponseAccContactExist.getBody().getFrezCode().equals("D")) {

										logger.info(SeqUniqueID + ": Total Freeze or Debit Freeze");

										ipsDao.updateCBSStatusError(SeqUniqueID,
												TranMonitorStatus.VALIDATION_ERROR.toString(),
												"Transaction forbidden on this type of account/"
														+ "TotalFreeze or Debit Freeze",
												TranMonitorStatus.FAILURE.toString());
										tranResponse = errorCode.ErrorCode("BOB119");
									} else {
										if (connect24ResponseAccContactExist.getBody().getCurrencyCode().equals(env.getProperty("bob.crncycode"))) {
											if (connect24ResponseAccContactExist.getBody().getCurrencyCode()
													.equals(currency)) {

												logger.info(SeqUniqueID + ": Calling Connect24 for Credit FundTransfer");

												//tranResponse = errorCode.ErrorCode("BOB0");
												ResponseEntity<C24FTResponse> connect24Response = connect24Service
														.cdtFundRequest(acctNumber, trAmt, currency, sysTraceAuditNumber,
																SeqUniqueID,trRmks);

												if (connect24Response.getStatusCode() == HttpStatus.OK) {
													logger.info(SeqUniqueID + ": success");

													tranResponse = errorCode.ErrorCode("BOB0");
												} else {

													logger.info(SeqUniqueID + ": failed");

													ipsDao.updateCBSStatusError(SeqUniqueID,
															TranMonitorStatus.CBS_CREDIT_ERROR.toString(),
															connect24Response.getBody().getError_desc().get(0),
															TranMonitorStatus.FAILURE.toString());

													tranResponse = errorCode
															.ErrorCode(connect24Response.getBody().getError().toString());
												}
											} else {
												logger.info(SeqUniqueID + ":Invalid Currency Code ");

												ipsDao.updateCBSStatusError(SeqUniqueID,
														TranMonitorStatus.VALIDATION_ERROR.toString(),
														"Validation Failed/Invalid Currency Code",
														TranMonitorStatus.FAILURE.toString());
												tranResponse = errorCode.ErrorCode("AM11");
											}
										} else {
											logger.info(SeqUniqueID + ":Invalid Currency Code ");

											ipsDao.updateCBSStatusError(SeqUniqueID,
													TranMonitorStatus.VALIDATION_ERROR.toString(),
													"Transaction forbidden/Invalid Currency Code",
													TranMonitorStatus.FAILURE.toString());
											tranResponse = errorCode.ErrorCode("BOB119");
										}

									}

								} else {
									if (connect24ResponseAccContactExist.getBody().getAccountStatus().equals("C")) {
										logger.info(SeqUniqueID + ": Account is Closed");

										ipsDao.updateCBSStatusError(SeqUniqueID,
												TranMonitorStatus.VALIDATION_ERROR.toString(), "Closed Account Number",
												TranMonitorStatus.FAILURE.toString());

										tranResponse = errorCode.ErrorCode("AC04");
									} else {
										logger.info(SeqUniqueID + ": Account is Blocked/Dormant");

										ipsDao.updateCBSStatusError(SeqUniqueID,
												TranMonitorStatus.VALIDATION_ERROR.toString(), "Blocked Account Number",
												TranMonitorStatus.FAILURE.toString());

										tranResponse = errorCode.ErrorCode("AC06");
									}
								}
							} else {

								logger.info(SeqUniqueID + ": Invalid Account Type");

								ipsDao.updateCBSStatusError(SeqUniqueID, TranMonitorStatus.VALIDATION_ERROR.toString(),
										"Transaction forbidden on this type of account",
										TranMonitorStatus.FAILURE.toString());

								tranResponse = errorCode.ErrorCode("BOB119");
							}

						} else {

							if (connect24ResponseAccContactExist.getBody().getStatus().equals("1")) {
								logger.info(SeqUniqueID + ": Invalid Account Number ");

								ipsDao.updateCBSStatusError(SeqUniqueID, TranMonitorStatus.VALIDATION_ERROR.toString(),
										"Incorrect Account Debtor Number", TranMonitorStatus.FAILURE.toString());
								tranResponse = errorCode.ErrorCode("AC03");
							} else {
								logger.info(SeqUniqueID + ": Invalid Account Type ");

								ipsDao.updateCBSStatusError(SeqUniqueID, TranMonitorStatus.VALIDATION_ERROR.toString(),
										"Transaction forbidden on this type of account",
										TranMonitorStatus.FAILURE.toString());
								tranResponse = errorCode.ErrorCode("BOB119");
							}

						}

					} else {
						
						logger.info(SeqUniqueID + ": failed");

						ipsDao.updateCBSStatusError(SeqUniqueID,
								TranMonitorStatus.CBS_CREDIT_ERROR.toString(),
								connect24ResponseAccContactExist.getBody().getError_desc().get(0),
								TranMonitorStatus.FAILURE.toString());

						tranResponse = errorCode
								.ErrorCode(connect24ResponseAccContactExist.getBody().getError().toString());
				
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
		}else {
			logger.info(SeqUniqueID + ": Signature Failed");

			ipsDao.updateCBSStatusError(SeqUniqueID, TranMonitorStatus.VALIDATION_ERROR.toString(),
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


	
}
