package com.bornfire.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bornfire.clientService.IPSXClient;
import com.bornfire.config.ErrorResponseCode;
import com.bornfire.config.InformixConnectionManager;
import com.bornfire.config.Listener;
import com.bornfire.config.SequenceGenerator;
import com.bornfire.entity.BankAgentTable;
import com.bornfire.entity.BankAgentTableRep;
import com.bornfire.entity.BenAccount;
import com.bornfire.entity.BukCreditTransferRequest;
import com.bornfire.entity.C24FTResponseBalance;
import com.bornfire.entity.CIMCreditTransferRequest;
import com.bornfire.entity.CIMMerchantDirectFndRequest;
import com.bornfire.entity.CIMMerchantQRcodeRequest;
import com.bornfire.entity.CimCBSresponse;
import com.bornfire.entity.ConsentAccessInquiryTable;
import com.bornfire.entity.ConsentAccessInquiryTableRep;
import com.bornfire.entity.ConsentAccessRequest;
import com.bornfire.entity.ConsentAccessResponse;
import com.bornfire.entity.ConsentAccessTable;
import com.bornfire.entity.ConsentAccessTableRep;
import com.bornfire.entity.ConsentAccessTmpTable;
import com.bornfire.entity.ConsentAccessTmpTableRep;
import com.bornfire.entity.ConsentOutwardAccessAuthRequest;
import com.bornfire.entity.ConsentOutwardAccessAuthResponse;
import com.bornfire.entity.ConsentOutwardAccessRequest;
import com.bornfire.entity.ConsentOutwardAccessTable;
import com.bornfire.entity.ConsentOutwardAccessTableRep;
import com.bornfire.entity.ConsentOutwardAccessTmpTable;
import com.bornfire.entity.ConsentOutwardAccessTmpTableRep;
import com.bornfire.entity.ConsentOutwardAuthorisationTable;
import com.bornfire.entity.ConsentOutwardAuthorisationTableRep;
import com.bornfire.entity.ConsentOutwardInquiryTable;
import com.bornfire.entity.ConsentOutwardInquiryTableRep;
import com.bornfire.entity.ConsentRequest;
import com.bornfire.entity.ConsentTable;
import com.bornfire.entity.ConsentTableRep;
import com.bornfire.entity.CryptogramResponse;
import com.bornfire.entity.IPSFlowTable;
import com.bornfire.entity.IPSFlowTableRep;
import com.bornfire.entity.Links;
import com.bornfire.entity.MCCreditTransferRequest;
import com.bornfire.entity.ManualFndTransferRequest;
import com.bornfire.entity.MerchantMaster;
import com.bornfire.entity.MerchantMasterRep;
import com.bornfire.entity.MerchantQrGenTable;
import com.bornfire.entity.MerchantQrGenTablerep;
import com.bornfire.entity.OTPGenTable;
import com.bornfire.entity.OTPGenTableRep;
import com.bornfire.entity.OtherBankDetResponse;
import com.bornfire.entity.OutwardTransHistMonitorTable;
import com.bornfire.entity.OutwardTransHistMonitoringTableRep;
import com.bornfire.entity.OutwardTransactionMonitoringTable;
import com.bornfire.entity.OutwardTransactionMonitoringTableRep;
import com.bornfire.entity.PartitionTableRep;
import com.bornfire.entity.Partition_table_entity;
import com.bornfire.entity.RTPTransferRequestStatus;
import com.bornfire.entity.RTPTransferStatusResponse;
import com.bornfire.entity.RTPbulkTransferRequest;
import com.bornfire.entity.RegPublicKey;
import com.bornfire.entity.RegPublicKeyRep;
import com.bornfire.entity.RegPublicKeyTmp;
import com.bornfire.entity.RegPublicKeyTmpRep;
import com.bornfire.entity.RtpResponse;
import com.bornfire.entity.SCAAthenticationResponse;
import com.bornfire.entity.SCAAuthenticatedData;
import com.bornfire.entity.SettlementAccount;
import com.bornfire.entity.SettlementAccountAmtRep;
import com.bornfire.entity.SettlementAccountAmtTable;
import com.bornfire.entity.SettlementAccountRep;
import com.bornfire.entity.SettlementLimitReportRep;
import com.bornfire.entity.SettlementLimitReportTable;
import com.bornfire.entity.SettlementReport;
import com.bornfire.entity.SettlementReportRep;
import com.bornfire.entity.TranAllDetailsTableRep;
import com.bornfire.entity.TranCBSTable;
import com.bornfire.entity.TranCBSTableRep;
import com.bornfire.entity.TranCimCBSTable;
import com.bornfire.entity.TranCimCBSTableRep;
import com.bornfire.entity.TranCimGLRep;
import com.bornfire.entity.TranCimGLTable;
import com.bornfire.entity.TranHistMonitorHistRep;
import com.bornfire.entity.TranIPSTable;
import com.bornfire.entity.TranIPSTableID;
import com.bornfire.entity.TranIPSTableRep;
import com.bornfire.entity.TranMonitorStatus;
import com.bornfire.entity.TranMonitoringHist;
import com.bornfire.entity.TranNetStatRep;
import com.bornfire.entity.TranNetState;
import com.bornfire.entity.TranNonCBSTable;
import com.bornfire.entity.TranNonCBSTableRep;
import com.bornfire.entity.TransactionMonitor;
import com.bornfire.entity.TransactionMonitorRep;
import com.bornfire.entity.UserRegistrationRequest;
import com.bornfire.entity.WalletAccessRequest;
import com.bornfire.entity.WalletAccessResponse;
import com.bornfire.entity.WalletAccessTable;
import com.bornfire.entity.WalletAccessTableRep;
import com.bornfire.entity.WalletAccessTmpTable;
import com.bornfire.entity.WalletAccessTmpTableRep;
import com.bornfire.entity.WalletFndTransferRequest;
import com.bornfire.entity.WalletRequestRegisterTable;
import com.bornfire.entity.WalletRequestRegisterTableRep;
import com.bornfire.exception.ErrorRestResponse;
import com.bornfire.exception.IPSXException;
import com.bornfire.exception.IPSXRestException;
import com.bornfire.jaxb.camt_052_001_08.Document;
import com.bornfire.jaxb.camt_053_001_08.AccountStatement91;
import com.bornfire.jaxb.camt_053_001_08.CashBalance81;
import com.bornfire.jaxb.camt_053_001_08.ReportEntry101;
import com.bornfire.jaxb.wsdl.SendT;
import com.google.gson.Gson;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

@Service
@Transactional
public class IPSDao {
	@Autowired
	TransactionMonitorRep tranRep;

	@Autowired
	TranHistMonitorHistRep tranHistRep;

	@Autowired
	Connect24Service connect24Service;

	@Autowired
	TranNetStatRep tranNetStatRep;

	@Autowired
	RegPublicKeyRep regPublicKeyRep;

	@Autowired
	RegPublicKeyTmpRep regPublicKeyTmpRep;

	@Autowired
	BankAgentTableRep bankAgentTableRep;

	@Autowired
	ConsentTableRep consentTableRep;

	@Autowired
	OTPGenTableRep otpGenTableRep;

	@Autowired
	SettlementLimitReportRep settlementLimitReportRep;

	@Autowired
	ErrorResponseCode errorCode;

	@Autowired
	TranCBSTableRep tranCBSTableRep;

	@Autowired
	TranIPSTableRep tranIPStableRep;

	@Autowired
	IPSFlowTableRep iPSFlowTableRep;

	@Autowired
	SequenceGenerator sequence;

	@Autowired
	SettlementReportRep settlementReportRep;

	@Autowired
	IPSConnection ipsConnection;

	@Autowired
	SettlementAccountAmtRep settlAcctAmtRep;

	@Autowired
	ConsentAccessTmpTableRep consentAccessTmpTableRep;

	@Autowired
	ConsentAccessTableRep consentAccessTableRep;
	
	@Autowired
	TranNonCBSTableRep tranNonCBSTableRep;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	Environment env;

	@Autowired
	TaskExecutor taskExecutor;
	
	@Autowired
	WalletAccessTmpTableRep walletAccessTmpTableRep;
	

	@Autowired
	WalletAccessTableRep walletAccessTableRep;
	
	@Autowired
	WalletRequestRegisterTableRep walletRequestRegisterTableRep;
	
	@Autowired
	TranAllDetailsTableRep tranAllDetailsTableRep;
	
	@Autowired
	ConsentOutwardAccessTmpTableRep consentOutwardAccessTmpTableRep;
	
	@Autowired
	ConsentOutwardAccessTableRep consentOutwardAccessTableRep;
	
	@Autowired
	ConsentOutwardAuthorisationTableRep consentOutwardAuthorisationTableRep;
	
	@Autowired
	ConsentOutwardInquiryTableRep consentOutwardInquiryTableRep;
	
	@Autowired
	ConsentAccessInquiryTableRep consentAccessInquiryTableRep;
	
	@Autowired
	OutwardTransactionMonitoringTableRep outwardTranRep;
	
	@Autowired
	OutwardTransHistMonitoringTableRep outwardTranHistRep;
	

	@Autowired
	TranCimCBSTableRep tranCimCBSTableRep;
	
	@Autowired
	CimCBSservice cimCBSservice;
	
	
	@Autowired
	Listener listener;
	
	@Autowired
	MerchantQrGenTablerep mercantQrGenTableRep;
	
	@Autowired
	SettlementAccountRep settlAccountRep;
	
	@Autowired
	TranCimGLRep tranCimGLRep;
	
	@Autowired
	InformixConnectionManager informixCon;
	
	@Autowired
	MerchantMasterRep merchantmasterrep;
	
	@Autowired
	PartitionTableRep partitiontablerep;

	private static final Logger logger = LoggerFactory.getLogger(IPSXClient.class);

	public void RegisterOutMsgRecord(String psuDeviceID, String psuIpAddress, String psuID, String senderParticipantBIC,
			String participantSOL, String sysTraceNumber, CIMCreditTransferRequest mcCreditTransferRequest,
			String bobMsgID, String seqUniqueID, String endToEndID, String tran_type_code, String msgNetMIR,
			String instg_agt, String instd_agt, String dbtr_agt, String dbtr_agt_acc, String cdtr_agt,
			String cdtr_agt_acc, String instr_id, String svc_lvl, String lcl_instrm, String ctgy_purp,
			String remitterBankCode) {

		try {
			TransactionMonitor tranManitorTable = new TransactionMonitor();
			tranManitorTable.setMsg_type(TranMonitorStatus.OUTGOING.toString());
			tranManitorTable.setTran_audit_number(sysTraceNumber);
			tranManitorTable.setSequence_unique_id(seqUniqueID);
			tranManitorTable.setBob_message_id(bobMsgID);
			tranManitorTable.setBob_account(mcCreditTransferRequest.getFrAccount().getAcctNumber());
			tranManitorTable.setIpsx_account(mcCreditTransferRequest.getToAccount().getAcctNumber());
			tranManitorTable.setReceiver_bank(mcCreditTransferRequest.getToAccount().getBankCode());
			tranManitorTable.setInitiator_bank(remitterBankCode);
			tranManitorTable.setTran_amount(new BigDecimal(mcCreditTransferRequest.getTrAmt()));
			tranManitorTable.setTran_date(new Date());
			tranManitorTable.setEntry_time(new Date());
			tranManitorTable.setCbs_status(TranMonitorStatus.CBS_DEBIT_INITIATED.toString());
			tranManitorTable.setEntry_user("SYSTEM");
			tranManitorTable.setTran_currency(mcCreditTransferRequest.getCurrencyCode());
			tranManitorTable.setTran_status(TranMonitorStatus.INITIATED.toString());
			tranManitorTable.setDevice_id(psuDeviceID);
			tranManitorTable.setDevice_ip(psuIpAddress);
			tranManitorTable.setNat_id(psuID);
			tranManitorTable.setEnd_end_id(endToEndID);
			tranManitorTable.setBob_account_name(mcCreditTransferRequest.getFrAccount().getAcctName());
			tranManitorTable.setIpsx_account_name(mcCreditTransferRequest.getToAccount().getAcctName());
			tranManitorTable.setTran_type_code(tran_type_code);
			tranManitorTable.setNet_mir(msgNetMIR);
			tranManitorTable.setInstg_agt(instg_agt);
			tranManitorTable.setInstd_agt(instd_agt);

			tranManitorTable.setDbtr_agt(dbtr_agt);
			tranManitorTable.setDbtr_agt_acc(dbtr_agt_acc);
			tranManitorTable.setCdtr_agt(cdtr_agt);
			tranManitorTable.setCdtr_agt_acc(cdtr_agt_acc);

			tranManitorTable.setInstr_id(instr_id);
			tranManitorTable.setSvc_lvl(svc_lvl);
			tranManitorTable.setLcl_instrm(lcl_instrm);
			tranManitorTable.setCtgy_purp(ctgy_purp);
			tranManitorTable.setChrg_br("SLEV");

			//// Check CutOff time after BOB settlement time
			//// if yes the value date is +1
			if (isTimeAfterCutOff()) {
				Date dt = new Date();
				Calendar c = Calendar.getInstance();
				c.setTime(dt);
				c.add(Calendar.DATE, 1);
				dt = c.getTime();
				tranManitorTable.setValue_date(dt);
			} else {
				tranManitorTable.setValue_date(new Date());
			}

			tranRep.save(tranManitorTable);

			//// update IPS Flow Table

			// insertIPSFlow(seqUniqueID, "OUTGOING", "Initial");

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		
	}

	public void updateCBSStatus(String seqUniqueID, String cbsStatus, String tranStatus) {
		
		tranRep.updateCBSStatus(seqUniqueID,cbsStatus,tranStatus);
		/*try {
			Optional<TransactionMonitor> otm = tranRep.findById(seqUniqueID);
			
			 

			if (otm.isPresent()) {
				TransactionMonitor tm = otm.get();
				
				
				tm.setCbs_status(cbsStatus);
				tm.setCbs_response_time(new Date());
				// tm.setCbs_status_error("");
				
				if(!tm.getResponse_status().equals("ACSP")&&
						!tm.getResponse_status().equals("RJCT")) {
					tm.setTran_status(tranStatus);
				}
				tranRep.save(tm);
			} else {
				logger.info("updateCBSStatus:Data Not found");

			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}*/

	}
	
	

	public void updateCBSStatusError(String seqUniqueID, String cbsStatus, String error_desc, String tranStatus) {
		try {
			Optional<TransactionMonitor> otm = tranRep.findById(seqUniqueID);

			if (otm.isPresent()) {
				TransactionMonitor tm = otm.get();
				tm.setCbs_status(cbsStatus);
				tm.setCbs_status_error(error_desc);
				tm.setCbs_response_time(new Date());
				tm.setTran_status(tranStatus);
				tranRep.save(tm);
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

	}
	
	public void updateCBSStatusRTP(String sysTraceNumber008, String endToEndID008,String seqUniqueID, String cbsStatus, String tranStatus) {
		
		outwardTranRep.updateCbsData(endToEndID008,cbsStatus,sysTraceNumber008);
		
		/*List<OutwardTransactionMonitoringTable> data1=outwardTranRep.getExistData(endToEndID008);
		if(data1.size()>0) {
			OutwardTransactionMonitoringTable data2=data1.get(0);
			
			data2.setCbs_status(cbsStatus);
			data2.setCbs_response_time(new Date());
			// tm.setCbs_status_error("");
			//data2.setTran_status(tranStatus);
			
			
			outwardTranRep.save(data2);
		}*/

	}
	
	public void updateCBSStatusRTPError(String sysTraceNumber008, String endToEndID008,String seqUniqueID, String cbsStatus, String error_desc, String tranStatus) {
		try {
			
			outwardTranRep.updateCBSStatusRTPError(sysTraceNumber008,endToEndID008,cbsStatus,error_desc,new SimpleDateFormat("dd-MMM-yyyy").format(new Date()));
			
			/*logger.info("updateCBSStatusRTPError");
			List<OutwardTransactionMonitoringTable> data1=outwardTranRep.getExistData(endToEndID008);
			
			
			
			if(data1.size()>0) {
				OutwardTransactionMonitoringTable data2=data1.get(0);
				logger.info("updateCBSStatusRTPError"+data2.getResponse_status());
				data2.setCbs_status(cbsStatus);
				data2.setCbs_status_error(error_desc);
				data2.setCbs_response_time(new Date());
				//data2.setTran_status(tranStatus);
				
				
				outwardTranRep.save(data2);
			}*/
			
			
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

	}
	



	public void updateIPSXStatus(String seqUniqueID, String ipsStatus, String tranStatus) {
		
		tranRep.updateIPSXStatus(seqUniqueID,ipsStatus,new SimpleDateFormat("dd-MMM-yyyy").format(new Date()));
		/*try {
			Optional<TransactionMonitor> otm = tranRep.findById(seqUniqueID);
			logger.info("updateIPSXStatus seqID:" + seqUniqueID);

			if (otm.isPresent()) {
				TransactionMonitor tm = otm.get();


				if (tm.getResponse_status() == null) {
					tm.setIpsx_status(ipsStatus);
					tm.setIpsx_response_time(new Date());
					tranRep.save(tm);
				} else {
					if (tm.getResponse_status().equals(TranMonitorStatus.ACSP.toString())) {
						
					} else {
						tm.setIpsx_status(ipsStatus);
						tm.setIpsx_response_time(new Date());
						tranRep.save(tm);
					}
				}


			} else {
				logger.info("updateIPSXStatus:Data not found");
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}*/

	}

	public void updateBulkDebitIPSXStatus(String seqUniqueID, String ipsStatus, String tranStatus) {
		try {
			Optional<TransactionMonitor> otm = tranRep.findById(seqUniqueID);
			logger.info("updateIPSXStatus seqID:" + seqUniqueID);

			if (otm.isPresent()) {
				TransactionMonitor tm = otm.get();
				List<TransactionMonitor> tranMonitorList = tranRep.findBulkDebitID(tm.getMaster_ref_id());

				for (TransactionMonitor tranMonitorItem : tranMonitorList) {
					if (tranMonitorItem.getCbs_status().equals(TranMonitorStatus.CBS_DEBIT_OK.toString())) {
						tranMonitorItem.setIpsx_status(ipsStatus);
						tranMonitorItem.setTran_status(tranStatus);
						tranMonitorItem.setIpsx_response_time(new Date());
						tranRep.save(tranMonitorItem);
					}
				}

			} else {
				logger.info("updateIPSXStatus:Data not found");
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

	}

	public void updateIPSXStatusResponseACSP(String seqUniqueID, String ipsxMsgID, String tranStatus,String msgType) {
		try {
			// Optional<TranIPSTable> otmTranIPS = tranIPStableRep.findById(seqUniqueID);

			List<TranIPSTable> otmTranIPS = tranIPStableRep.findByIdCustom(seqUniqueID);

			//// For loop is used for RTP Transaction(Transaction Created between BOB
			//// Customers)
			//// Tran IPS Table is used for Maintaining IPS Packages and got Original ID Of
			//// Packages and updated tran status to Transaction_monitoring _table

			for (TranIPSTable item : otmTranIPS) {

				System.out.println(item.getSequence_unique_id());
				Optional<TransactionMonitor> otm = tranRep.findById(item.getSequence_unique_id());

				if (otm.isPresent()) {
					TransactionMonitor tm = otm.get();
					logger.info("InwardACSP");
					if (!tm.getMsg_type().equals("BULK_DEBIT")) {
						tm.setIpsx_status(TranMonitorStatus.IPSX_RESPONSE_ACSP.toString());
						tm.setResponse_status(TranMonitorStatus.ACSP.toString().trim());
						tm.setIpsx_message_id(ipsxMsgID);
						tm.setIpsx_response_time(new Date());
						tm.setTran_status(tranStatus);
						tranRep.saveAndFlush(tm);

						updateINOUT(tm.getSequence_unique_id(), "IPS_OUT");

						//// Apply Charges And Fee
						/// Check Transaction outward
						if (!tm.getMsg_type().equals(TranMonitorStatus.INCOMING.toString())) {
							//ipsConnection.ipsChargesAndFeeTran(tm.getSequence_unique_id(), "IPS Fund Transfer");
						}

					} else {
						List<TransactionMonitor> tranMonitorList = tranRep.findBulkDebitID(tm.getMaster_ref_id());

						for (TransactionMonitor tranMonitorItem : tranMonitorList) {

							if (tranMonitorItem.getCbs_status().equals(TranMonitorStatus.CBS_DEBIT_OK.toString())) {
								tranMonitorItem.setIpsx_status(TranMonitorStatus.IPSX_RESPONSE_ACSP.toString());
								tranMonitorItem.setResponse_status(TranMonitorStatus.ACSP.toString().trim());
								tranMonitorItem.setIpsx_message_id(ipsxMsgID);
								tranMonitorItem.setIpsx_response_time(new Date());
								tranMonitorItem.setTran_status(tranStatus);
								tranRep.save(tranMonitorItem);

								//// Apply Charges And Fee
								//// Check Transaction outward
								if (!tm.getMsg_type().equals(TranMonitorStatus.INCOMING.toString())) {
									// ipsConnection.ipsChargesAndFeeTran(tm.getSequence_unique_id(), "IPS Fund
									// Transfer");
								}
								
								

							}

							updateINOUT(tranMonitorItem.getSequence_unique_id(), "IPS_OUT");
						}
					}

				}else {
					

				}
				
				/*if(msgType.equals("pain.002.001.10")  ) {
					Optional<OutwardTransactionMonitoringTable> otm1 = outwardTranRep.findById(item.getSequence_unique_id());
					
					if(otm1.isPresent()) {
						OutwardTransactionMonitoringTable tm=otm1.get();
						tm.setIpsx_status(TranMonitorStatus.IPSX_RESPONSE_ACSP.toString());
						tm.setResponse_status(TranMonitorStatus.ACSP.toString().trim());
						tm.setIpsx_message_id(ipsxMsgID);
						tm.setIpsx_response_time(new Date());
						tm.setTran_status(tranStatus);
						outwardTranRep.save(tm);
						
						
					////Generate RequestUUID
						String requestUUID=sequence.generateRequestUUId();
						
						
						
						
					}
				}*/
				
			}

			/*
			 * if (otmTranIPS.isPresent()) { Optional<TransactionMonitor> otm =
			 * tranRep.findById(otmTranIPS.get().getSequence_unique_id());
			 * 
			 * if (otm.isPresent()) { TransactionMonitor tm = otm.get();
			 * tm.setIpsx_status(TranMonitorStatus.IPSX_RESPONSE_ACSP.toString());
			 * tm.setResponse_status(TranMonitorStatus.ACSP.toString().trim());
			 * tm.setIpsx_message_id(ipsxMsgID); tm.setIpsx_response_time(new Date());
			 * tm.setTran_status(tranStatus); tranRep.save(tm);
			 * 
			 * updateINOUT(tm.getSequence_unique_id(), "IPS_OUT"); } }
			 */

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

	}
	
	@Transactional
	public void updateIPSXRJCTINC(String seqUniqueID, String ipsxerrorDesc, String ipsxMsgID,
			String tranStatus, String ipsXStatus, String ipsxResponseStatus, String ipsxErrorCode,String msgType) {
		
		Optional<TransactionMonitor> otm = tranRep.findById(seqUniqueID);

		if (otm.isPresent()) {
			logger.info("InwardRJCT");

			TransactionMonitor tm = otm.get();
				tm.setIpsx_message_id(ipsxMsgID);
				tm.setIpsx_status(ipsXStatus);
				tm.setIpsx_status_error(ipsxerrorDesc);
				tm.setResponse_status(ipsxResponseStatus);
				tm.setIpsx_response_time(new Date());
				tm.setTran_status(tranStatus);
				tm.setIpsx_status_code(ipsxErrorCode);
				tranRep.saveAndFlush(tm);
		}
	}

	public void updateIPSXStatusResponseRJCT(String seqUniqueID, String ipsxerrorDesc, String ipsxMsgID,
			String tranStatus, String ipsXStatus, String ipsxResponseStatus, String ipsxErrorCode,String msgType) {
		try {
			// Optional<TranIPSTable> otmTranIPS = tranIPStableRep.findById(seqUniqueID);

			logger.info(seqUniqueID + " :IPSX Rejected Sttatus");

			List<TranIPSTable> otmTranIPS = tranIPStableRep.findByIdCustom(seqUniqueID);

			//// In for loop used for reversal transaction of RTP(Transaction created
			//// between BOB Customers)
			//// Tran IPS Table is used for Maintaining IPS Packages and got Original ID Of
			//// Packages and updated tran status to Transaction_monitoring _table

			for (TranIPSTable item : otmTranIPS) {
				Optional<TransactionMonitor> otm = tranRep.findById(item.getSequence_unique_id());

				if (otm.isPresent()) {
					logger.info("InwardRJCT");

					TransactionMonitor tm = otm.get();

					if (!tm.getMsg_type().equals("BULK_DEBIT")) {
						tm.setIpsx_message_id(ipsxMsgID);
						tm.setIpsx_status(ipsXStatus);
						tm.setIpsx_status_error(ipsxerrorDesc);
						tm.setResponse_status(ipsxResponseStatus);
						tm.setIpsx_response_time(new Date());
						tm.setTran_status(tranStatus);
						tm.setIpsx_status_code(ipsxErrorCode);
						tranRep.saveAndFlush(tm);

						
						if (tm.getMsg_type().equals(TranMonitorStatus.INCOMING.toString())) {

							logger.info(tm.getSequence_unique_id() + " : Incoming / " + tm.getCbs_status());

							break;
							
						} else if (tm.getMsg_type().equals(TranMonitorStatus.OUTGOING.toString())) {

							logger.info(tm.getSequence_unique_id() + " : Outgoing / " + tm.getCbs_status());

							updateINOUT(tm.getSequence_unique_id(), "IPS_OUT");

							if (tm.getCbs_status().equals(TranMonitorStatus.CBS_DEBIT_OK.toString())) {

															} else {

								logger.info(tm.getSequence_unique_id() + " : No need for Reversal Transaction");
								updateCBSStatus(tm.getSequence_unique_id(), tm.getCbs_status(),
										TranMonitorStatus.FAILURE.toString());
							}

						} else if (tm.getMsg_type().equals(TranMonitorStatus.RTP.toString())) {

							logger.info(tm.getSequence_unique_id() + " : RTP / " + tm.getCbs_status());

							if (tm.getCbs_status().equals(TranMonitorStatus.CBS_DEBIT_OK.toString())) {

															} else {

								logger.info(tm.getSequence_unique_id() + " : No need for Reversal Transaction");
								updateCBSStatus(tm.getSequence_unique_id(), tm.getCbs_status(),
										TranMonitorStatus.FAILURE.toString());
							}

						} else if (tm.getMsg_type().equals(TranMonitorStatus.BULK_CREDIT.toString())) {

							logger.info(tm.getSequence_unique_id() + " : Bulk Credit / " + tm.getCbs_status());

							if (tm.getCbs_status().equals(TranMonitorStatus.CBS_DEBIT_OK.toString())) {

								/*updateCBSStatus(tm.getSequence_unique_id(),
										TranMonitorStatus.CBS_DEBIT_REVERSE_INITIATED.toString(),
										TranMonitorStatus.IN_PROGRESS.toString());

								logger.info(tm.getSequence_unique_id() + " : Initiate Reversal Transaction");

								connect24Service.dbtReverseBulkCreditFundRequest(tm.getBob_account(),
										sequence.generateSystemTraceAuditNumber(), tm.getTran_currency(),
										tm.getTran_amount().toString(), tm.getSequence_unique_id(),
										"RT/"+tm.getTran_audit_number()+"/"+ ipsxerrorDesc);*/
							} else {
								logger.info(tm.getSequence_unique_id() + " : No need for Reversal Transaction");

								updateCBSStatus(tm.getSequence_unique_id(), tm.getCbs_status(),
										TranMonitorStatus.FAILURE.toString());
							}

						} else if (tm.getMsg_type().equals(TranMonitorStatus.MANUAL.toString())) {

							logger.info(tm.getSequence_unique_id() + " : Manual / " + tm.getCbs_status());

							updateINOUT(tm.getSequence_unique_id(), "IPS_OUT");

							if (tm.getCbs_status().equals(TranMonitorStatus.CBS_DEBIT_OK.toString())) {

								/*updateCBSStatus(tm.getSequence_unique_id(),
										TranMonitorStatus.CBS_DEBIT_REVERSE_INITIATED.toString(),
										TranMonitorStatus.IN_PROGRESS.toString());

								logger.info(tm.getSequence_unique_id() + " : Initiate Reversal Transaction");

								connect24Service.dbtReverseFundRequest(tm.getBob_account(),
										sequence.generateSystemTraceAuditNumber(), tm.getTran_currency(),
										tm.getTran_amount().toString(), tm.getSequence_unique_id(),
										"RT/"+tm.getTran_audit_number()+"/"+ ipsxerrorDesc);*/
							} else {

								logger.info(tm.getSequence_unique_id() + " : No need for Reversal Transaction");
								updateCBSStatus(tm.getSequence_unique_id(), tm.getCbs_status(),
										TranMonitorStatus.FAILURE.toString());
							}

						}

					} else {
						List<TransactionMonitor> tranMonitorList = tranRep.findBulkDebitID(tm.getMaster_ref_id());

						for (TransactionMonitor tranMonitorItem : tranMonitorList) {

							logger.info(tranMonitorItem.getSequence_unique_id() + " : Manual / "
									+ tranMonitorItem.getCbs_status());

							updateINOUT(tranMonitorItem.getSequence_unique_id(), "IPS_OUT");

							tranMonitorItem.setIpsx_message_id(ipsxMsgID);
							tranMonitorItem.setIpsx_status(ipsXStatus);
							tranMonitorItem.setIpsx_status_error(ipsxerrorDesc);
							tranMonitorItem.setResponse_status(ipsxResponseStatus);
							tranMonitorItem.setIpsx_response_time(new Date());
							tranMonitorItem.setTran_status(tranStatus);
							tranMonitorItem.setIpsx_status_code(ipsxErrorCode);

							
							
							tranRep.save(tranMonitorItem);
							
							if (tranMonitorItem.getCbs_status().equals(TranMonitorStatus.CBS_DEBIT_OK.toString())) {

								/*tranMonitorItem.setIpsx_message_id(ipsxMsgID);
								tranMonitorItem.setIpsx_status(ipsXStatus);
								tranMonitorItem.setIpsx_status_error(ipsxerrorDesc);
								tranMonitorItem.setResponse_status(ipsxResponseStatus);
								tranMonitorItem.setIpsx_response_time(new Date());
								tranMonitorItem.setTran_status(tranStatus);
								tranMonitorItem.setIpsx_status_code(ipsxErrorCode);

								
								
								tranRep.save(tranMonitorItem);*/

								/*updateCBSStatus(tranMonitorItem.getSequence_unique_id(),
										TranMonitorStatus.CBS_DEBIT_REVERSE_INITIATED.toString(),
										TranMonitorStatus.IN_PROGRESS.toString());

								logger.info(
										tranMonitorItem.getSequence_unique_id() + " : Initiate Reversal Transaction");

								connect24Service.BulkdbtReverseFundRequest(tranMonitorItem.getBob_account(),
										sequence.generateSystemTraceAuditNumber(), tranMonitorItem.getTran_currency(),
										tranMonitorItem.getTran_amount().toString(),
										tranMonitorItem.getSequence_unique_id(), "TR/"+tranMonitorItem.getTran_audit_number()+"/"+ ipsxerrorDesc);*/
							} else {

								logger.info(tranMonitorItem.getSequence_unique_id()
										+ " : No need for Reversal Transaction");
								updateCBSStatus(tranMonitorItem.getSequence_unique_id(),
										tranMonitorItem.getCbs_status(), TranMonitorStatus.FAILURE.toString());
							}

						}

					}
				} else {
					
				}
				
				/*Optional<OutwardTransactionMonitoringTable> otm1 = outwardTranRep.findById(item.getSequence_unique_id());

				if(msgType.equals("pain.002.001.10")) {
					if (otm1.isPresent()) {
						OutwardTransactionMonitoringTable tm = otm1.get();
						tm.setIpsx_message_id(ipsxMsgID);
						tm.setIpsx_status(ipsXStatus);
						tm.setIpsx_status_error(ipsxerrorDesc);
						tm.setResponse_status(ipsxResponseStatus);
						tm.setIpsx_response_time(new Date());
						tm.setTran_status(tranStatus);
						tm.setIpsx_status_code(ipsxErrorCode);
						outwardTranRep.save(tm);
						
					    ////Send to Failure Msg to CIM
						
						
						
					}
				}*/

			}
			/*
			 * if (otmTranIPS.isPresent()) {
			 * 
			 * Optional<TransactionMonitor> otm =
			 * tranRep.findById(otmTranIPS.get().getSequence_unique_id());
			 * 
			 * if (otm.isPresent()) { TransactionMonitor tm = otm.get();
			 * tm.setIpsx_message_id(ipsxMsgID); tm.setIpsx_status(ipsXStatus);
			 * tm.setIpsx_status_error(ipsxerrorDesc);
			 * tm.setResponse_status(ipsxResponseStatus); tm.setIpsx_response_time(new
			 * Date()); tm.setTran_status(tranStatus);
			 * tm.setIpsx_status_code(ipsxErrorCode); tranRep.save(tm);
			 * 
			 * if (tm.getMsg_type().equals(TranMonitorStatus.INCOMING.toString())) {
			 * 
			 * logger.info(seqUniqueID + " : Incoming / " + tm.getCbs_status());
			 * 
			 * if (tm.getCbs_status().equals(TranMonitorStatus.CBS_CREDIT_OK.toString())) {
			 * 
			 * updateCBSStatus(seqUniqueID,
			 * TranMonitorStatus.CBS_CREDIT_REVERSE_INITIATED.toString(),
			 * TranMonitorStatus.IN_PROGRESS.toString());
			 * 
			 * logger.info(seqUniqueID + " : Initiate Reversal Transaction");
			 * 
			 * connect24Service.cdtReverseFundRequest(tm.getBob_account(),
			 * tm.getTran_amount().toString(), tm.getTran_currency().toString(),
			 * sequence.generateSystemTraceAuditNumber(), seqUniqueID); } else {
			 * logger.info(seqUniqueID + " : No need for Reversal Transaction");
			 * 
			 * updateCBSStatus(seqUniqueID, tm.getCbs_status(),
			 * TranMonitorStatus.FAILURE.toString()); }
			 * 
			 * } else if (tm.getMsg_type().equals(TranMonitorStatus.OUTGOING.toString())) {
			 * 
			 * logger.info(seqUniqueID + " : Outgoing / " + tm.getCbs_status());
			 * 
			 * updateINOUT(tm.getSequence_unique_id(), "IPS_OUT");
			 * 
			 * if (tm.getCbs_status().equals(TranMonitorStatus.CBS_DEBIT_OK.toString())) {
			 * 
			 * updateCBSStatus(seqUniqueID,
			 * TranMonitorStatus.CBS_DEBIT_REVERSE_INITIATED.toString(),
			 * TranMonitorStatus.IN_PROGRESS.toString());
			 * 
			 * logger.info(seqUniqueID + " : Initiate Reversal Transaction");
			 * 
			 * connect24Service.dbtReverseFundRequest(tm.getBob_account(),
			 * sequence.generateSystemTraceAuditNumber(), tm.getTran_currency(),
			 * tm.getTran_amount().toString(), seqUniqueID); } else {
			 * 
			 * logger.info(seqUniqueID + " : No need for Reversal Transaction");
			 * updateCBSStatus(seqUniqueID, tm.getCbs_status(),
			 * TranMonitorStatus.FAILURE.toString()); }
			 * 
			 * } else if (tm.getMsg_type().equals(TranMonitorStatus.RTP.toString())) {
			 * 
			 * logger.info(seqUniqueID + " : RTP / " + tm.getCbs_status());
			 * 
			 * if (tm.getCbs_status().equals(TranMonitorStatus.CBS_DEBIT_OK.toString())) {
			 * 
			 * updateCBSStatus(tm.getSequence_unique_id(),
			 * TranMonitorStatus.CBS_DEBIT_REVERSE_INITIATED.toString(),
			 * TranMonitorStatus.IN_PROGRESS.toString());
			 * 
			 * logger.info(seqUniqueID + " : Initiate Reversal Transaction");
			 * 
			 * connect24Service.rtpReverseFundRequest(tm.getBob_account(),
			 * sequence.generateSystemTraceAuditNumber(), tm.getTran_currency(),
			 * tm.getTran_amount().toString(), tm.getSequence_unique_id()); } else {
			 * 
			 * logger.info(seqUniqueID + " : No need for Reversal Transaction");
			 * updateCBSStatus(tm.getSequence_unique_id(), tm.getCbs_status(),
			 * TranMonitorStatus.FAILURE.toString()); }
			 * 
			 * } else if (tm.getMsg_type().equals(TranMonitorStatus.BULK_CREDIT.toString()))
			 * {
			 * 
			 * logger.info(seqUniqueID + " : Bulk Credit / " + tm.getCbs_status());
			 * 
			 * if (tm.getCbs_status().equals(TranMonitorStatus.CBS_DEBIT_OK.toString())) {
			 * 
			 * updateCBSStatus(seqUniqueID,
			 * TranMonitorStatus.CBS_DEBIT_REVERSE_INITIATED.toString(),
			 * TranMonitorStatus.IN_PROGRESS.toString());
			 * 
			 * logger.info(seqUniqueID + " : Initiate Reversal Transaction");
			 * 
			 * connect24Service.dbtReverseBulkCreditFundRequest(tm.getBob_account(),
			 * sequence.generateSystemTraceAuditNumber(), tm.getTran_currency(),
			 * tm.getTran_amount().toString(), seqUniqueID); } else {
			 * logger.info(seqUniqueID + " : No need for Reversal Transaction");
			 * 
			 * updateCBSStatus(seqUniqueID, tm.getCbs_status(),
			 * TranMonitorStatus.FAILURE.toString()); }
			 * 
			 * }
			 * 
			 * } else {
			 * 
			 * }
			 * 
			 * }
			 */
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

	}
	
	public void ReturnCIMcnfResponseRJCT(String seqUniqueID, String ipsxerrorDesc, String ipsxMsgID,
			String tranStatus, String ipsXStatus, String ipsxResponseStatus, String ipsxErrorCode,String cbsStatus,String cbsError) {
		try {

			
			Optional<OutwardTransactionMonitoringTable> otm = outwardTranRep.findById(seqUniqueID);
			if (otm.isPresent()) {
				OutwardTransactionMonitoringTable tm = otm.get();
				tm.setIpsx_message_id(ipsxMsgID);
				tm.setIpsx_status(ipsXStatus);
				tm.setIpsx_status_error(ipsxerrorDesc);
				tm.setResponse_status(ipsxResponseStatus);
				tm.setIpsx_response_time(new Date());
				tm.setTran_status(tranStatus);
				tm.setIpsx_status_code(ipsxErrorCode);

				///// Send Failure Message to CIM

				//// Generate RequestUUID
				String requestUUID = sequence.generateRequestUUId();

				String response = registerCIMcbsIncomingData(requestUUID, env.getProperty("cimCBS.channelID"),
						env.getProperty("cimCBS.servicereqversion"), env.getProperty("cimCBS.servicereqID"), new Date(),
						sequence.generateSystemTraceAuditNumber(), tm.getInit_channel_id(), tm.getReq_unique_id(), "False", "", "",
						"", tm.getCim_account(), tm.getTran_amount().toString(), tm.getTran_currency(),
						tm.getSequence_unique_id(), tm.getIpsx_account(), tm.getIpsx_account_name(), "NRT", "", "",
						"Failure", ipsxerrorDesc,new Date(),"","","","","");

				logger.info("Pain Output Return Msg to ThirdParty Application");

				logger.info(tm.getSequence_unique_id() + " :CIM Register Reverse Data Output:" + response);

				if (response.equals("1")) {
					ResponseEntity<CimCBSresponse> connect24Response = cimCBSservice.cbsResponseFailure(requestUUID);

					logger.info(tm.getSequence_unique_id() + "CIM : 0");

					if (connect24Response.getStatusCode() == HttpStatus.OK) {

						logger.info(tm.getSequence_unique_id() + " CIM: 1");
						if (connect24Response.getBody().getStatus().getIsSuccess()) {
							updateCIMcbsData(requestUUID, "SUCCESS",
									connect24Response.getBody().getStatus().getStatusCode(),
									connect24Response.getBody().getStatus().getMessage(),
									connect24Response.getBody().getData().getTransactionNoFromCBS());

							logger.info(tm.getSequence_unique_id() + " CIM: 2");
							tm.setCim_cnf_request_uid(requestUUID);
							tm.setCim_cnf_status("Success");
							tm.setCim_cnf_status_error("");
						} else {
							updateCIMcbsData(requestUUID, "FAILURE",
									connect24Response.getBody().getStatus().getStatusCode(),
									connect24Response.getBody().getStatus().getMessage(),
									connect24Response.getBody().getData().getTransactionNoFromCBS());
							logger.info(tm.getSequence_unique_id() + " CIM: 3");
							tm.setCim_cnf_request_uid(requestUUID);
							tm.setCim_cnf_status("FAILURE");
							tm.setCim_cnf_status_error(connect24Response.getBody().getStatus().getMessage());
						}
					} else {
						logger.info(tm.getSequence_unique_id() + " : CIM 4:Failure");
						updateCIMcbsData(requestUUID, "FAILURE", "500", "Internal Server Error","");
						tm.setCim_cnf_request_uid(requestUUID);
						tm.setCim_cnf_status("FAILURE");
						tm.setCim_cnf_status_error("Internal Server Error");

					}
				}

				outwardTranRep.save(tm);

			}

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

	}

/*
	public void ReturnCIMcnfOutgoingResponseACSP(String seqUniqueID, String ipsxerrorDesc, String ipsxMsgID,
			String tranStatus, String ipsXStatus, String ipsxResponseStatus, String ipsxErrorCode, String cbsStatus,
			String cbsError) {
		try {

			logger.info(seqUniqueID + " :IPSX SUCCESS");

			//// In for loop used for reversal transaction of RTP(Transaction created
			//// between BOB Customers)
			//// Tran IPS Table is used for Maintaining IPS Packages and got Original ID Of
			//// Packages and updated tran status to Transaction_monitoring _table

			Optional<OutwardTransactionMonitoringTable> otm = outwardTranRep.findById(seqUniqueID);
			if (otm.isPresent()) {
				OutwardTransactionMonitoringTable tm = otm.get();
				tm.setIpsx_message_id(ipsxMsgID);
				tm.setIpsx_status(ipsXStatus);
				tm.setIpsx_status_error(ipsxerrorDesc);
				tm.setResponse_status(ipsxResponseStatus);
				tm.setIpsx_response_time(new Date());
				tm.setTran_status(tranStatus);
				tm.setIpsx_status_code(ipsxErrorCode);

				///// Send Failure Message to CIM

				//// Generate RequestUUID
				String requestUUID = sequence.generateRequestUUId();

				String response = registerCIMcbsIncomingData(requestUUID, env.getProperty("cimCBS.channelID"),
						env.getProperty("cimCBS.servicereqversion"), env.getProperty("cimCBS.servicereqID"), new Date(),
						sequence.generateTranNumber(), tm.getInit_channel_id(), tm.getReq_unique_id(), "False", "", "",
						"", tm.getCim_account(), tm.getTran_amount().toString(), tm.getTran_currency(),
						tm.getSequence_unique_id(), tm.getIpsx_account(), tm.getIpsx_account_name(), "NRT", "", "",
						"Success", ipsxerrorDesc);

				logger.info("Pain Output Return Msg to ThirdParty Application");

				logger.info(tm.getSequence_unique_id() + " :CIM Register Reverse Data Output:" + response);

				if (response.equals("1")) {
					ResponseEntity<CimCBSresponse> connect24Response = cimCBSservice.cbsResponseFailure(requestUUID);

					logger.info(tm.getSequence_unique_id() + "CIM : 0");

					if (connect24Response.getStatusCode() == HttpStatus.OK) {

						logger.info(tm.getSequence_unique_id() + " CIM: 1");
						if (connect24Response.getBody().getStatus().getIsSuccess()) {
							updateCIMcbsData(requestUUID, TranMonitorStatus.SUCCESS.toString(),
									connect24Response.getBody().getStatus().getStatusCode(),
									connect24Response.getBody().getStatus().getMessage());

							logger.info(tm.getSequence_unique_id() + " CIM: 2");
							tm.setCim_cnf_request_uid(requestUUID);
							tm.setCim_cnf_status("Success");
							tm.setCim_cnf_status_error("");
						} else {
							updateCIMcbsData(requestUUID, TranMonitorStatus.FAILURE.toString(),
									connect24Response.getBody().getStatus().getStatusCode(),
									connect24Response.getBody().getStatus().getMessage());
							logger.info(tm.getSequence_unique_id() + " CIM: 3");
							tm.setCim_cnf_request_uid(requestUUID);
							tm.setCim_cnf_status(TranMonitorStatus.FAILURE.toString());
							tm.setCim_cnf_status_error(connect24Response.getBody().getStatus().getMessage());
						}
					} else {
						logger.info(tm.getSequence_unique_id() + " : CIM 4:Failure");
						updateCIMcbsData(requestUUID, TranMonitorStatus.FAILURE.toString(), "500",
								"Something went wrong at server end");
						tm.setCim_cnf_request_uid(requestUUID);
						tm.setCim_cnf_status(TranMonitorStatus.FAILURE.toString());
						tm.setCim_cnf_status_error("Something went wrong at server end");

					}
				}

				outwardTranRep.save(tm);

			}

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

	}
*/
	public void updateIPSXStatusResponseRJCTBulkRTP(String seqUniqueID, String ipsxerrorDesc, String ipsxMsgID,
			String tranStatus, String ipsXStatus, String ipsxResponseStatus, String ipsxErrorCode) {
		try {

			logger.info(seqUniqueID + " :IPSX Rejected Sttatus");

			List<TranIPSTable> otmTranIPS = tranIPStableRep.findByIdCustom(seqUniqueID);

			//// In for loop used for reversal transaction of RTP(Transaction created
			//// between BOB Customers)
			//// Tran IPS Table is used for Maintaining IPS Packages and got Original ID Of
			//// Packages and updated tran status to Transaction_monitoring _table

			for (TranIPSTable item : otmTranIPS) {
				Optional<OutwardTransactionMonitoringTable> otm = outwardTranRep.findById(item.getSequence_unique_id());
				if (otm.isPresent()) {
					logger.info("outwardRJCT");
					OutwardTransactionMonitoringTable tm = otm.get();
					
					if (!tm.getMsg_type().equals("BULK_DEBIT")) {
						if(tm.getMsg_type().equals(TranMonitorStatus.OUTWARD_BULK_RTP.toString())) {
							tm.setIpsx_message_id(ipsxMsgID);
							tm.setIpsx_status(ipsXStatus);
							tm.setIpsx_status_error(ipsxerrorDesc);
							tm.setResponse_status(ipsxResponseStatus);
							tm.setIpsx_response_time(new Date());
							tm.setTran_status(tranStatus);
							tm.setIpsx_status_code(ipsxErrorCode);
							tm.setMconnectin("N");
							
							outwardTranRep.saveAndFlush(tm);
							
							taskExecutor.execute(new Runnable() {
								@Override
								public void run() {
									callESBPostFailureData(tm.getSequence_unique_id(),ipsxErrorCode,ipsxerrorDesc);
								}
							});
														
							
						}else {
							tm.setIpsx_message_id(ipsxMsgID);
							tm.setIpsx_status(ipsXStatus);
							tm.setIpsx_status_error(ipsxerrorDesc);
							tm.setResponse_status(ipsxResponseStatus);
							tm.setIpsx_response_time(new Date());
							tm.setTran_status(tranStatus);
							tm.setIpsx_status_code(ipsxErrorCode);
							
							outwardTranRep.saveAndFlush(tm);
						}

						break;
					}else {
						List<OutwardTransactionMonitoringTable> tranMonitorList = outwardTranRep.findBulkDebitID(tm.getMaster_ref_id());

						for (OutwardTransactionMonitoringTable tranMonitorItem : tranMonitorList) {

							logger.info(tranMonitorItem.getSequence_unique_id() + " : BulkDebit / "
									+ tranMonitorItem.getCbs_status());

							
							
							tranMonitorItem.setIpsx_message_id(ipsxMsgID);
							tranMonitorItem.setIpsx_status(ipsXStatus);
							tranMonitorItem.setIpsx_status_error(ipsxerrorDesc);
							tranMonitorItem.setResponse_status(ipsxResponseStatus);
							tranMonitorItem.setIpsx_response_time(new Date());
							tranMonitorItem.setTran_status(tranStatus);
							tranMonitorItem.setIpsx_status_code(ipsxErrorCode);

							outwardTranRep.saveAndFlush(tranMonitorItem);

							/*if (tranMonitorItem.getCbs_status().equals(TranMonitorStatus.CBS_DEBIT_OK.toString())) {

								tranMonitorItem.setIpsx_message_id(ipsxMsgID);
								tranMonitorItem.setIpsx_status(ipsXStatus);
								tranMonitorItem.setIpsx_status_error(ipsxerrorDesc);
								tranMonitorItem.setResponse_status(ipsxResponseStatus);
								tranMonitorItem.setIpsx_response_time(new Date());
								tranMonitorItem.setTran_status(tranStatus);
								tranMonitorItem.setIpsx_status_code(ipsxErrorCode);

								outwardTranRep.save(tranMonitorItem);

								updateCBSStatus(tranMonitorItem.getSequence_unique_id(),
										TranMonitorStatus.CBS_DEBIT_REVERSE_INITIATED.toString(),
										TranMonitorStatus.IN_PROGRESS.toString());

								logger.info(
										tranMonitorItem.getSequence_unique_id() + " : Initiate Reversal Transaction");

								connect24Service.BulkdbtReverseFundRequest(tranMonitorItem.getBob_account(),
										sequence.generateSystemTraceAuditNumber(), tranMonitorItem.getTran_currency(),
										tranMonitorItem.getTran_amount().toString(),
										tranMonitorItem.getSequence_unique_id(), "TR/"+tranMonitorItem.getTran_audit_number()+"/"+ ipsxerrorDesc);
							} else {

								logger.info(tranMonitorItem.getSequence_unique_id()
										+ " : No need for Reversal Transaction");
								updateCBSStatus(tranMonitorItem.getSequence_unique_id(),
										tranMonitorItem.getCbs_status(), TranMonitorStatus.FAILURE.toString());
							}*/

						}

					}

				}
			}
			

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

	}
	
	private void callESBPostFailureData(String sequence_unique_id,String ipsxErrorCode,String ipsxerrorDesc) {
		Optional<OutwardTransactionMonitoringTable> otm1 = outwardTranRep.findById(sequence_unique_id);
		//logger.info("outwardRJCT Mod"+otm1.get().getResponse_status());

		OutwardTransactionMonitoringTable tm=otm1.get();
		
		String cbsStatus=tm.getCbs_status()==null?"":tm.getCbs_status();
	    /////Send Failure Message to CIM
		
		if(!cbsStatus.equals("CBS_CREDIT_OK") && !cbsStatus.equals("CBS_CREDIT_ERROR")&&!tm.getInit_channel_id().equals("BIPS")) {
			////Generate RequestUUID
			String requestUUID=sequence.generateRequestUUId();
			
			String init_tran_no="";
			if(tm.getResv_field1()!=null) {
				init_tran_no=tm.getResv_field1();
			}else {
				init_tran_no=tm.getP_id();
			}
			
			
			String settlReceivableAccount=settlAccountRep.findById("03").get().getAccount_number();

			String response=registerCIMcbsIncomingData(requestUUID,env.getProperty("cimCBS.channelID"),
					env.getProperty("cimCBS.servicereqversion"),env.getProperty("cimCBS.servicereqID"),new Date(),
					sequence.generateSystemTraceAuditNumber(),tm.getInit_channel_id(),init_tran_no,"False","","","",
					tm.getCim_account(), tm.getTran_amount().toString(), tm.getTran_currency(),
					tm.getSequence_unique_id(),settlReceivableAccount,tm.getCim_account_name(),"RTP","","","","",new Date(),"RECEIVABLE",tm.getReq_unique_id(),
					ipsxErrorCode,ipsxerrorDesc,tm.getMaster_ref_id());
			
			logger.info("Pain Output Return Msg to ThirdParty Application");

			logger.info(tm.getSequence_unique_id() + " :CIM Register Reverse Data Output:"+response);

			
			if(response.equals("1")) {
				ResponseEntity<CimCBSresponse> connect24Response = cimCBSservice
						.cbsResponseFailure(requestUUID);

				logger.debug(listener.generateJsonFormat(connect24Response.toString()));
				logger.info(tm.getSequence_unique_id() + "CIM : 0");
				
				if (connect24Response.getStatusCode() == HttpStatus.OK) {

					CimCBSresponse data=connect24Response.getBody();
					logger.info(tm.getSequence_unique_id() + " CIM: 1");
					
					if(connect24Response.getBody().getData()!=null) {
						if(connect24Response.getBody().getStatus().getIsSuccess()) {
							updateCIMcbsData(requestUUID,"SUCCESS",connect24Response.getBody().getStatus().getStatusCode(),connect24Response.getBody().getStatus().getMessage(),connect24Response.getBody().getData().getTransactionNoFromCBS());
							
							logger.info(tm.getSequence_unique_id() + " CIM: 2");
							updateCIMCNFData(tm.getSequence_unique_id(),requestUUID,"SUCCESS","");
							
						}else {
							updateCIMcbsData(requestUUID,"FAILURE",connect24Response.getBody().getStatus().getStatusCode(),connect24Response.getBody().getStatus().getMessage(),connect24Response.getBody().getData().getTransactionNoFromCBS());
							logger.info(tm.getSequence_unique_id() + " CIM: 3");
							updateCIMCNFData(tm.getSequence_unique_id(),requestUUID,"FAILURE",connect24Response.getBody().getStatus().getMessage());								
						}
					}else {
						updateCIMcbsData(requestUUID,"FAILURE",connect24Response.getBody().getStatus().getStatusCode(),connect24Response.getBody().getStatus().getMessage(),connect24Response.getBody().getData().getTransactionNoFromCBS());
						logger.info(tm.getSequence_unique_id() + " CIM: 3");
						updateCIMCNFData(tm.getSequence_unique_id(),requestUUID,"FAILURE","No Response return from CBS");	
					}
					
				} else {
					logger.info(tm.getSequence_unique_id() + " : CIM 4:Failure");
					updateCIMcbsData(requestUUID,"FAILURE","500","Internal Server Error","");
					updateCIMCNFData(tm.getSequence_unique_id(),requestUUID,"FAILURE","Internal Server Error");

				}
			}
		}


		
	}
	
	public void updateOutwardCBSStatus(String seqUniqueID, String cbsStatus, String tranStatus) {
		try {
			Optional<OutwardTransactionMonitoringTable> otm = outwardTranRep.findById(seqUniqueID);

			if (otm.isPresent()) {
				OutwardTransactionMonitoringTable tm = otm.get();
				tm.setCbs_status(cbsStatus);
				tm.setCbs_response_time(new Date());
				// tm.setCbs_status_error("");
				tm.setTran_status(tranStatus);
				outwardTranRep.save(tm);
			} else {
				logger.info("updateCBSStatus:Data Not found");

			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

	}
	

	
	public void updateOutwardCBSStatusError(String seqUniqueID, String cbsStatus, String error_desc, String tranStatus) {
		try {
			Optional<OutwardTransactionMonitoringTable> otm = outwardTranRep.findById(seqUniqueID);

			if (otm.isPresent()) {
				OutwardTransactionMonitoringTable tm = otm.get();
				tm.setCbs_status(cbsStatus);
				tm.setCbs_status_error(error_desc);
				tm.setCbs_response_time(new Date());
				tm.setTran_status(tranStatus);
				outwardTranRep.save(tm);
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

	}
	
	public void updateOutwardIPSXStatus(String seqUniqueID, String ipsStatus, String tranStatus) {
		try {
			Optional<OutwardTransactionMonitoringTable> otm = outwardTranRep.findById(seqUniqueID);
			logger.info("updateIPSXStatus seqID:" + seqUniqueID);

			if (otm.isPresent()) {
				OutwardTransactionMonitoringTable tm = otm.get();

				if (tm.getResponse_status() == null) {
					tm.setIpsx_status(ipsStatus);
					tm.setTran_status(tranStatus);
					tm.setIpsx_response_time(new Date());
				} else {
					if (tm.getResponse_status().equals(TranMonitorStatus.ACSP.toString())) {
						tm.setTran_status(TranMonitorStatus.SUCCESS.toString());
					} else {
						tm.setIpsx_status(ipsStatus);
						tm.setTran_status(tranStatus);
						tm.setIpsx_response_time(new Date());
					}
				}

				outwardTranRep.save(tm);

			} else {
				logger.info("updateIPSXStatus:Data not found");
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

	}


	
	public void updateCIMCNFData(String seqUniqueID,String requestUUID,String status,String statusError) {
		
		outwardTranRep.updateCIMCNFData(seqUniqueID,requestUUID,status,statusError);
		/*Optional<OutwardTransactionMonitoringTable> data=outwardTranRep.findById(seqUniqueID);
		if(data.isPresent()) {
			OutwardTransactionMonitoringTable outTable=data.get();
			outTable.setCim_cnf_request_uid(requestUUID);
			outTable.setCim_cnf_status(status);
			outTable.setCim_cnf_status_error(statusError);
			outwardTranRep.save(outTable);
		}*/
	}
	
	public void updateIPSXStatusResponseACSPBulkRTP(String seqUniqueID, String ipsxMsgID, 
			String tranStatus,String msgType,String dateOrglMsg) {
		try {

			logger.info(seqUniqueID + " :IPSX ACSP Sttatus");

			List<TranIPSTable> otmTranIPS = tranIPStableRep.findByIdCustom(seqUniqueID);


			if(dateOrglMsg.equals(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))) {
				for (TranIPSTable item : otmTranIPS) {
					
					Optional<OutwardTransactionMonitoringTable> otm = outwardTranRep.findById(item.getSequence_unique_id());
					if (otm.isPresent()) {
						logger.info("outwardACSP");
						OutwardTransactionMonitoringTable tm=otm.get();
						if (!tm.getMsg_type().equals("BULK_DEBIT")) {
							tm.setIpsx_status(TranMonitorStatus.IPSX_RESPONSE_ACSP.toString());
							tm.setResponse_status(TranMonitorStatus.ACSP.toString().trim());
							tm.setIpsx_message_id(ipsxMsgID);
							tm.setIpsx_response_time(new Date());
							tm.setTran_status(tranStatus);
							outwardTranRep.save(tm);
						}else {
							List<OutwardTransactionMonitoringTable> tranMonitorList = outwardTranRep.findBulkDebitID(tm.getMaster_ref_id());

							for (OutwardTransactionMonitoringTable tranMonitorItem : tranMonitorList) {

								if (tranMonitorItem.getCbs_status().equals(TranMonitorStatus.CBS_DEBIT_OK.toString())) {
									tranMonitorItem.setIpsx_status(TranMonitorStatus.IPSX_RESPONSE_ACSP.toString());
									tranMonitorItem.setResponse_status(TranMonitorStatus.ACSP.toString().trim());
									tranMonitorItem.setIpsx_message_id(ipsxMsgID);
									tranMonitorItem.setIpsx_response_time(new Date());
									tranMonitorItem.setTran_status(tranStatus);
									outwardTranRep.save(tranMonitorItem);
								}
							}
						}
						

						break;
					}
				}
				
			}else {
				for (TranIPSTable item : otmTranIPS) {
					
					Optional<OutwardTransHistMonitorTable> otm = outwardTranHistRep.findById(item.getSequence_unique_id());
					if (otm.isPresent()) {
						logger.info("outwardACSP");
						OutwardTransHistMonitorTable tm=otm.get();
						if (!tm.getMsg_type().equals("BULK_DEBIT")) {
							tm.setIpsx_status(TranMonitorStatus.IPSX_RESPONSE_ACSP.toString());
							tm.setResponse_status(TranMonitorStatus.ACSP.toString().trim());
							tm.setIpsx_message_id(ipsxMsgID);
							tm.setIpsx_response_time(new Date());
							tm.setTran_status(tranStatus);
							outwardTranHistRep.save(tm);
						}else {
							List<OutwardTransHistMonitorTable> tranMonitorList = outwardTranHistRep.findBulkDebitID(tm.getMaster_ref_id());

							for (OutwardTransHistMonitorTable tranMonitorItem : tranMonitorList) {

								if (tranMonitorItem.getCbs_status().equals(TranMonitorStatus.CBS_DEBIT_OK.toString())) {
									tranMonitorItem.setIpsx_status(TranMonitorStatus.IPSX_RESPONSE_ACSP.toString());
									tranMonitorItem.setResponse_status(TranMonitorStatus.ACSP.toString().trim());
									tranMonitorItem.setIpsx_message_id(ipsxMsgID);
									tranMonitorItem.setIpsx_response_time(new Date());
									tranMonitorItem.setTran_status(tranStatus);
									outwardTranHistRep.save(tranMonitorItem);
								}
							}
						}
						

						break;
					}
				}
				
			}


		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

	}

	
	public void updateIPSXStatusBulkRTP(String seqUniqueID, String ipsStatus, String tranStatus) {
		try {
			outwardTranRep.updateIPSXStatusBulkRTP(seqUniqueID, ipsStatus,
					new SimpleDateFormat("dd-MMM-yyyy").format(new Date()));
		} catch (Exception e) {
			logger.error(e.getMessage());
			System.err.println(e.getMessage());
		}
		/*try {
			Optional<OutwardTransactionMonitoringTable> otm = outwardTranRep.findById(seqUniqueID);
			logger.info("updateIPSXStatus seqID:" + seqUniqueID);

			if (otm.isPresent()) {
				OutwardTransactionMonitoringTable tm = otm.get();


				if (tm.getResponse_status() == null) {
					tm.setIpsx_status(ipsStatus);
					tm.setTran_status(tranStatus);
					tm.setIpsx_response_time(new Date());
				} else {
					if (tm.getResponse_status().equals(TranMonitorStatus.ACSP.toString())) {
						tm.setTran_status(TranMonitorStatus.SUCCESS.toString());
					} else {
						tm.setIpsx_status(ipsStatus);
						tm.setTran_status(tranStatus);
						tm.setIpsx_response_time(new Date());
					}
				}

				outwardTranRep.save(tm);

			} else {
				logger.info("updateIPSXStatus:Data not found");
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}*/

	}




	public void reverseCreditOKAccount(String seqUniqueID) {
		try {
			Optional<TransactionMonitor> otm = tranRep.findById(seqUniqueID);
			if (otm.isPresent()) {
				TransactionMonitor tm = otm.get();
				if (tm.getResponse_status().equals(TranMonitorStatus.RJCT.toString())) {
					if (tm.getCbs_status().equals(TranMonitorStatus.CBS_CREDIT_OK.toString())) {
						logger.info("update CBS credit reverse to Table" + seqUniqueID);
						updateCBSStatus(seqUniqueID, TranMonitorStatus.CBS_CREDIT_REVERSE_INITIATED.toString(),
								TranMonitorStatus.IN_PROGRESS.toString());
						logger.info("Calling Connect24 For Reverse Fund Transfer" + seqUniqueID);
						connect24Service.cdtReverseFundRequest(tm.getBob_account(), tm.getTran_amount().toString(),
								tm.getTran_currency().toString(), tm.getTran_audit_number().toString(), seqUniqueID,
								"RT/" + tm.getIpsx_status_error());
					} else if (tm.getCbs_status().equals(TranMonitorStatus.CBS_DEBIT_OK.toString())) {
						logger.info("update CBS credit reverse to Table" + seqUniqueID);
						updateCBSStatus(seqUniqueID, TranMonitorStatus.CBS_DEBIT_REVERSE_INITIATED.toString(),
								TranMonitorStatus.IN_PROGRESS.toString());
						logger.info("Calling Connect24 For Reverse Fund Transfer" + seqUniqueID);
						connect24Service.rtpReverseFundRequest(tm.getBob_account(),
								tm.getTran_audit_number().toString(), tm.getTran_currency().toString(),
								tm.getTran_amount().toString(), seqUniqueID, "Transaction reversed by IPSX");
					}
				}
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

	}

	public String RegisterInMsgRecord(String sysTraceNumber, String bobMsgID, String ipsxMsgID, String seqUniqueID,
			String dbtrAcctNumber, String cdtrAcctNumber, BigDecimal trAmount, String Currency_code, String tranStatus,
			String EndToEndID, String ipsxAccountName, String bobAccountName, String tran_type_code,
			String initiatorBank, String instgAgt, String instdAgt, String instr_id, String svc_lvl, String lcl_instrm,
			String ctgy_purp, String dbtr_agt, String dbtr_agt_acc, String cdtr_agt, String cdtr_agt_acc,String chrgBr,
			String clrChannel,String regRep,String rmtInfo,String rmt_info_issuer,String rmt_info_nb) {

		try {
			Optional<TransactionMonitor> otm = tranRep.findById(seqUniqueID);
			
			if (otm.isPresent()) {
								
				return "0";
			} else {
				
				Date dateM=new Date();
				Date value_Date=new Date();
				if (isTimeAfterCutOff()) {
					Date dt = new Date();
					Calendar c = Calendar.getInstance();
					c.setTime(dt);
					c.add(Calendar.DATE, 1);
					dt = c.getTime();
					value_Date=dt;
				} 
				
				TransactionMonitor tranManitorTable = new TransactionMonitor(TranMonitorStatus.INCOMING.toString(),sysTraceNumber,bobMsgID,
						ipsxMsgID,seqUniqueID,cdtrAcctNumber,dbtrAcctNumber,"",trAmount,dateM,dateM,TranMonitorStatus.CBS_CREDIT_INITIATED.toString(),
						"SYSTEM",Currency_code,tranStatus,EndToEndID,ipsxAccountName,bobAccountName,tran_type_code,
						instgAgt,instdAgt,instr_id,svc_lvl,lcl_instrm,ctgy_purp,dbtr_agt,dbtr_agt_acc,cdtr_agt,
						cdtr_agt_acc,chrgBr,regRep,rmtInfo,clrChannel,rmt_info_issuer,rmt_info_nb,value_Date);
				
				
				
				
				/*tranManitorTable.setMsg_type(TranMonitorStatus.INCOMING.toString());
				tranManitorTable.setTran_audit_number(sysTraceNumber);
				tranManitorTable.setBob_message_id(bobMsgID);
				tranManitorTable.setIpsx_message_id(ipsxMsgID);
				tranManitorTable.setSequence_unique_id(seqUniqueID);
				tranManitorTable.setBob_account(cdtrAcctNumber);
				tranManitorTable.setIpsx_account(dbtrAcctNumber);
				tranManitorTable.setReceiver_bank("");
				tranManitorTable.setTran_amount(trAmount);
				tranManitorTable.setTran_date(new Date());
				tranManitorTable.setEntry_time(new Date());
				tranManitorTable.setCbs_status(TranMonitorStatus.CBS_CREDIT_INITIATED.toString());
				tranManitorTable.setEntry_user("SYSTEM");
				tranManitorTable.setTran_currency(Currency_code);
				tranManitorTable.setTran_status(tranStatus);
				tranManitorTable.setEnd_end_id(EndToEndID);
				tranManitorTable.setIpsx_account_name(ipsxAccountName);
				tranManitorTable.setBob_account_name(bobAccountName);
				tranManitorTable.setTran_type_code(tran_type_code);

				tranManitorTable.setInstg_agt(instgAgt);
				tranManitorTable.setInstd_agt(instdAgt);

				tranManitorTable.setInstr_id(instr_id);
				tranManitorTable.setSvc_lvl(svc_lvl);
				tranManitorTable.setLcl_instrm(lcl_instrm);
				tranManitorTable.setCtgy_purp(ctgy_purp);

				tranManitorTable.setDbtr_agt(dbtr_agt);
				tranManitorTable.setDbtr_agt_acc(dbtr_agt_acc);
				tranManitorTable.setCdtr_agt(cdtr_agt);
				tranManitorTable.setCdtr_agt_acc(cdtr_agt_acc);
				tranManitorTable.setChrg_br(chrgBr);
				tranManitorTable.setReg_rep(regRep);
				tranManitorTable.setRmt_info(rmtInfo);
				tranManitorTable.setClr_chennel(clrChannel);
				tranManitorTable.setRmt_info_issuer(rmt_info_issuer);
				tranManitorTable.setRmt_info_nb(rmt_info_nb);*/

				//// Check CutOff time after BOB settlement time
				//// if yes the value date is +1
				

				
				//logger.info(tranManitorTable.toString());
				tranRep.saveAndFlush(tranManitorTable);
			//	tranRep.flush();
				
				logger.info("TranMonitorTable Inserted Successfully");

				return "1";
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "2";
		}
	}

	public String createPublicKey(String senderParticipantBIC, String senderParticipantMemberID,
			String receiverParticipantBIC, String receiverPartcipantMemberID, String psuDeviceID, String ipAddress,
			String psuID, UserRegistrationRequest userRequest, String recordID, String authID, String otp) {

		try {

			//// Save Public Key data to temp table
			RegPublicKeyTmp regPublicKey = new RegPublicKeyTmp();
			regPublicKey.setRecord_id(recordID);
			regPublicKey.setAuth_id(authID);
			regPublicKey.setDevice_id(psuDeviceID);
			regPublicKey.setIp_address(ipAddress);
			regPublicKey.setNational_id(psuID);
			regPublicKey.setEntry_user("SYSTEM");
			regPublicKey.setEntry_time(new Date());
			regPublicKey.setPublic_key(userRequest.getPublicKey());
			regPublicKey.setPhone_number(userRequest.getPhoneNumber());
			// regPublicKey.setOtp(otp);
			regPublicKey.setSchm_name(userRequest.getAccount().getSchemeName());
			regPublicKey.setAcct_number(userRequest.getAccount().getIdentification());
			regPublicKey.setSender_participant_bic(senderParticipantBIC);
			regPublicKey.setSender_participant_member_id(senderParticipantMemberID);
			regPublicKey.setReceiver_participant_bic(receiverParticipantBIC);
			regPublicKey.setReceiver_participant_member_id(receiverPartcipantMemberID);

			regPublicKeyTmpRep.save(regPublicKey);

			//// Save OTP to OTP table
			OTPGenTable otpgentable = new OTPGenTable();
			otpgentable.setAuth_id(authID);
			otpgentable.setRecord_id(recordID);

			Date generateDate = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(generateDate);
			cal.add(Calendar.MINUTE, 5);
			Date expirationDate = cal.getTime();

			otpgentable.setGenerated_date(generateDate);
			otpgentable.setExpired_date(expirationDate);

			otpgentable.setScreen_id("CREATE PUBLIC KEY");
			otpgentable.setOtp(otp);

			otpGenTableRep.save(otpgentable);

			return "0";
		} catch (Exception e) {
			e.printStackTrace();
			return "1";
		}
	}

	public void updatePublicKey(String senderParticipantBIC, String senderParticipantMemberID,
			String receiverParticipantBIC, String receiverPartcipantMemberID, String psuDeviceID, String ipAddress,
			String psuID, UserRegistrationRequest userRequest, String recordID, String authID, String otp) {

		try {
			Optional<RegPublicKey> otm = regPublicKeyRep.findById(recordID);

			if (otm.isPresent()) {

				RegPublicKeyTmp regPublicKeyTmp = new RegPublicKeyTmp();
				regPublicKeyTmp.setRecord_id(recordID);
				regPublicKeyTmp.setAuth_id(authID);
				regPublicKeyTmp.setDevice_id(psuDeviceID);
				regPublicKeyTmp.setIp_address(ipAddress);
				regPublicKeyTmp.setNational_id(psuID);
				regPublicKeyTmp.setEntry_user("SYSTEM");
				regPublicKeyTmp.setEntry_time(new Date());
				regPublicKeyTmp.setPublic_key(userRequest.getPublicKey());
				regPublicKeyTmp.setPhone_number(userRequest.getPhoneNumber());
				regPublicKeyTmp.setOtp(otp);
				regPublicKeyTmp.setSchm_name(userRequest.getAccount().getSchemeName());
				regPublicKeyTmp.setAcct_number(userRequest.getAccount().getIdentification());
				regPublicKeyTmp.setSender_participant_bic(senderParticipantBIC);
				regPublicKeyTmp.setSender_participant_member_id(senderParticipantMemberID);
				regPublicKeyTmp.setReceiver_participant_bic(receiverParticipantBIC);
				regPublicKeyTmp.setReceiver_participant_member_id(receiverPartcipantMemberID);

				regPublicKeyTmpRep.save(regPublicKeyTmp);

				OTPGenTable otpgentable = new OTPGenTable();
				otpgentable.setAuth_id(authID);
				otpgentable.setRecord_id(recordID);

				Date generateDate = new Date();
				Calendar cal = Calendar.getInstance();
				cal.setTime(generateDate);
				cal.add(Calendar.MINUTE, 5);
				Date expirationDate = cal.getTime();

				otpgentable.setGenerated_date(generateDate);
				otpgentable.setExpired_date(expirationDate);
				otpgentable.setScreen_id("UPDATE PUBLIC KEY");
				otpgentable.setOtp(otp);

				otpGenTableRep.save(otpgentable);
			} else {
				throw new IPSXRestException(errorCode.ErrorCodeRegistration("1"));
			}

		} catch (Exception e) {
			logger.info(e.getLocalizedMessage());
			throw new IPSXRestException(errorCode.ErrorCodeRegistration("0"));
		}

	}

	public void updatePublicKeyAccountStatus(String authID, String accountStatus) {
		Optional<RegPublicKeyTmp> otm = regPublicKeyTmpRep.findById(authID);
		if (otm.isPresent()) {
			RegPublicKeyTmp regPublicKeyTmp = otm.get();
			regPublicKeyTmp.setAccount_status(accountStatus);
			regPublicKeyTmpRep.save(regPublicKeyTmp);
		}
	}

	public SCAAthenticationResponse updateSCA(SCAAuthenticatedData scaAuthenticatedData, String recordID,
			String authID) {

		logger.info("recordID:"+recordID);
		logger.info("authID:"+authID);
		
		Optional<OTPGenTable> otm = otpGenTableRep.findById(authID);
		SCAAthenticationResponse response = null;
		if (otm.isPresent()) {
			System.out.println("Authentication:OK1");
			logger.info("Authentication:OK1");
			OTPGenTable otpGenTable = otm.get();
			if (scaAuthenticatedData.getScaAuthenticationData().toString().equals(otpGenTable.getOtp().toString())) {
				logger.info("Authentication:OK2");
				System.out.println("Authentication:OK2");
				if (isNowBetweenDateTime(otpGenTable.getGenerated_date(), otpGenTable.getExpired_date())) {
					response = new SCAAthenticationResponse();
					// String recordID=sequence.generateRecordId();
					Links links = new Links();
					links.setSCAStatus("/accounts/" + recordID + "/authorisations/" + authID);
					response.setSCAStatus("finalised");
					response.setLinks(links);
					
					System.out.println("Authentication:OK3");

					Optional<RegPublicKeyTmp> regPublicKeyTmpotm = regPublicKeyTmpRep.findById(authID);
					Optional<RegPublicKey> regPublicKeyotm = regPublicKeyRep.findById(recordID);

					if (regPublicKeyTmpotm.isPresent()) {
						System.out.println("Authentication:OK4");
						if (regPublicKeyTmpotm.get().getRecord_id().equals(recordID)) {
							System.out.println("Authentication:OK5");
							if (regPublicKeyotm.isPresent()) {
								System.out.println("Authentication:OK6");
								///// update public key
								RegPublicKey regPublicKey = regPublicKeyotm.get();
								regPublicKey.setRecord_id(regPublicKeyTmpotm.get().getRecord_id());
								regPublicKey.setAuth_id(regPublicKeyTmpotm.get().getAuth_id());
								regPublicKey.setDevice_id(regPublicKeyTmpotm.get().getDevice_id());
								regPublicKey.setIp_address(regPublicKeyTmpotm.get().getIp_address());
								regPublicKey.setNational_id(regPublicKeyTmpotm.get().getNational_id());
								regPublicKey.setModify_user("SYSTEM");
								regPublicKey.setModify_time(new Date());
								regPublicKey.setPublic_key(regPublicKeyTmpotm.get().getPublic_key());
								regPublicKey.setPhone_number(regPublicKeyTmpotm.get().getPhone_number());
								regPublicKey.setSchm_name(regPublicKeyTmpotm.get().getSchm_name());
								regPublicKey.setAcct_number(regPublicKeyTmpotm.get().getAcct_number());
								regPublicKey.setSender_participant_bic(
										regPublicKeyTmpotm.get().getSender_participant_bic());
								regPublicKey.setSender_participant_member_id(
										regPublicKeyTmpotm.get().getSender_participant_member_id());
								regPublicKey.setReceiver_participant_bic(
										regPublicKeyTmpotm.get().getReceiver_participant_bic());
								regPublicKey.setReceiver_participant_member_id(
										regPublicKeyTmpotm.get().getReceiver_participant_member_id());

								regPublicKey.setEntity_flg("N");
								regPublicKey.setModify_flg("N");
								regPublicKey.setDel_flg("N");

								regPublicKeyRep.save(regPublicKey);

								regPublicKeyTmpRep.deleteById(authID);

							} else {
								System.out.println("Authentication:OK7");
								//// Delete Previous Account Number List
								List<RegPublicKey> regAccList = regPublicKeyRep
										.getAccountNumber(regPublicKeyTmpotm.get().getAcct_number());
								if (regAccList.size() > 0) {
									System.out.println("Authentication:OK9");
									
									for(RegPublicKey re:regAccList) {
										
										RegPublicKey regPublicKey1 = re;
										if(!regPublicKey1.getRecord_id().equals(recordID)) {
											regPublicKey1.setDel_flg("Y");
											regPublicKey1.setDel_time(new Date());
											regPublicKeyRep.save(regPublicKey1);
										}
										
									}
									
									RegPublicKey regPublicKey =new RegPublicKey();
									regPublicKey.setRecord_id(regPublicKeyTmpotm.get().getRecord_id());
									regPublicKey.setAuth_id(regPublicKeyTmpotm.get().getAuth_id());
									regPublicKey.setDevice_id(regPublicKeyTmpotm.get().getDevice_id());
									regPublicKey.setIp_address(regPublicKeyTmpotm.get().getIp_address());
									regPublicKey.setNational_id(regPublicKeyTmpotm.get().getNational_id());
									regPublicKey.setEntry_user("SYSTEM");
									regPublicKey.setEntry_time(new Date());
									regPublicKey.setPublic_key(regPublicKeyTmpotm.get().getPublic_key());
									regPublicKey.setPhone_number(regPublicKeyTmpotm.get().getPhone_number());
									regPublicKey.setSchm_name(regPublicKeyTmpotm.get().getSchm_name());
									regPublicKey.setAcct_number(regPublicKeyTmpotm.get().getAcct_number());
									regPublicKey.setSender_participant_bic(
											regPublicKeyTmpotm.get().getSender_participant_bic());
									regPublicKey.setSender_participant_member_id(
											regPublicKeyTmpotm.get().getSender_participant_member_id());
									regPublicKey.setReceiver_participant_bic(
											regPublicKeyTmpotm.get().getReceiver_participant_bic());
									regPublicKey.setReceiver_participant_member_id(
											regPublicKeyTmpotm.get().getReceiver_participant_member_id());

									regPublicKey.setEntity_flg("N");
									regPublicKey.setModify_flg("N");
									regPublicKey.setDel_flg("N");
									
									
									regPublicKeyRep.save(regPublicKey);
								} else {
									System.out.println("Authentication:OK8");
									//// Insert public key
									RegPublicKey regPublicKey = new RegPublicKey();
									regPublicKey.setRecord_id(regPublicKeyTmpotm.get().getRecord_id());
									regPublicKey.setAuth_id(regPublicKeyTmpotm.get().getAuth_id());
									regPublicKey.setDevice_id(regPublicKeyTmpotm.get().getDevice_id());
									regPublicKey.setIp_address(regPublicKeyTmpotm.get().getIp_address());
									regPublicKey.setNational_id(regPublicKeyTmpotm.get().getNational_id());
									regPublicKey.setEntry_user("SYSTEM");
									regPublicKey.setEntry_time(new Date());
									regPublicKey.setPublic_key(regPublicKeyTmpotm.get().getPublic_key());
									regPublicKey.setPhone_number(regPublicKeyTmpotm.get().getPhone_number());
									regPublicKey.setSchm_name(regPublicKeyTmpotm.get().getSchm_name());
									regPublicKey.setAcct_number(regPublicKeyTmpotm.get().getAcct_number());
									regPublicKey.setSender_participant_bic(
											regPublicKeyTmpotm.get().getSender_participant_bic());
									regPublicKey.setSender_participant_member_id(
											regPublicKeyTmpotm.get().getSender_participant_member_id());
									regPublicKey.setReceiver_participant_bic(
											regPublicKeyTmpotm.get().getReceiver_participant_bic());
									regPublicKey.setReceiver_participant_member_id(
											regPublicKeyTmpotm.get().getReceiver_participant_member_id());

									regPublicKey.setEntity_flg("N");
									regPublicKey.setModify_flg("N");
									regPublicKey.setDel_flg("N");

									regPublicKeyRep.save(regPublicKey);
								}

								regPublicKeyTmpRep.deleteById(authID);

								//// Calling Connect 24 for Collect Registration fee from Customer
								//// After Successfull registration

								//taskExecutor.execute(new Runnable() {
									//@Override
									//public void run() {
										//ipsConnection.ipsChargesAndFeeMyt(recordID, "MYT_Registration");
									//}
								//});

							}
						} else {
							throw new IPSXRestException(errorCode.ErrorCodeRegistration("1"));
						}

					} else {
						throw new IPSXRestException(errorCode.ErrorCodeRegistration("3"));
					}
				} else {
					throw new IPSXRestException(errorCode.ErrorCodeRegistration("11"));
				}

			} else {
				throw new IPSXRestException(errorCode.ErrorCodeRegistration("12"));
			}
		} else {
			throw new IPSXRestException(errorCode.ErrorCodeRegistration("3"));
		}
		return response;

	}

	public String createConsentIDAccess(String x_request_id, String sender_participant_bic,
			String sender_participant_member_id, String receiver_participant_bic, String receiver_participant_member_id,
			String psuDeviceID, String psuIPAddress, String psuID, String psuIDCountry, String psuIDType,
			ConsentAccessRequest consentRequest, String consentID, String authID, String otp) {
		try {

			//// Save Public Key data to temp table
			ConsentAccessTmpTable consentAccessID = new ConsentAccessTmpTable();
			consentAccessID.setX_request_id(x_request_id);
			consentAccessID.setSenderparticipant_bic(sender_participant_bic);
			consentAccessID.setSenderparticipant_memberid(sender_participant_member_id);
			consentAccessID.setReceiverparticipant_bic(receiver_participant_bic);
			consentAccessID.setReceiverparticipant_memberid(receiver_participant_member_id);
			consentAccessID.setPsu_device_id(psuDeviceID);
			consentAccessID.setPsu_ip_address(psuIPAddress);
			consentAccessID.setPsu_id(psuID);
			consentAccessID.setPsu_id_country(psuIDCountry);
			consentAccessID.setPsu_id_type(psuIDType);
			consentAccessID.setConsent_id(consentID);
			consentAccessID.setAuth_id(authID);
			consentAccessID.setPhone_number(consentRequest.getPhoneNumber());
			consentAccessID.setPublic_key(consentRequest.getPublicKey());

			if (consentRequest.getExpirationDateTime() != null) {
				consentAccessID.setExpire_date(consentRequest.getExpirationDateTime().toGregorianCalendar().getTime());
			}

			if (consentRequest.getTransactionFromDateTime() != null) {
				consentAccessID
						.setTran_from_date(consentRequest.getTransactionFromDateTime().toGregorianCalendar().getTime());
			}

			if (consentRequest.getTransactionToDateTime() != null) {
				consentAccessID
						.setTran_to_date(consentRequest.getTransactionToDateTime().toGregorianCalendar().getTime());
			}

			consentAccessID.setSchm_name(consentRequest.getAccounts().get(0).getSchemeName());
			consentAccessID.setIdentification(consentRequest.getAccounts().get(0).getIdentification());
			consentAccessID.setPermission(String.join("-", consentRequest.getPermissions()));
			consentAccessID.setCreate_date(new Date());
			consentAccessID.setStatus_update_date(new Date());

			for (String permi : consentRequest.getPermissions()) {
				if (permi.equals(TranMonitorStatus.ReadBalances.toString())) {
					consentAccessID.setRead_balance("Y");
				} else if (permi.equals(TranMonitorStatus.ReadAccountsDetails.toString())) {
					consentAccessID.setRead_acct_details("Y");
				} else if (permi.equals(TranMonitorStatus.ReadTransactionsDetails.toString())) {
					consentAccessID.setRead_tran_details("Y");
				} else if (permi.equals(TranMonitorStatus.DebitAccount.toString())) {
					consentAccessID.setRead_debit_acct("Y");
				}
			}

			consentAccessTmpTableRep.save(consentAccessID);

			//// Save OTP to OTP table
			OTPGenTable otpgentable = new OTPGenTable();
			otpgentable.setAuth_id(authID);
			otpgentable.setRecord_id(consentID);

			Date generateDate = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(generateDate);
			cal.add(Calendar.MINUTE, 5);
			Date expirationDate = cal.getTime();

			otpgentable.setGenerated_date(generateDate);
			otpgentable.setExpired_date(expirationDate);

			otpgentable.setScreen_id("CREATE CONSENT");
			otpgentable.setOtp(otp);

			otpGenTableRep.save(otpgentable);

			return "0";
		} catch (Exception e) {
			e.printStackTrace();
			return "1";
		}
	}

	public SCAAthenticationResponse updateSCAConsentAccess(String x_request_id, String sender_participant_bic,
			String sender_participant_member_id, String receiver_participant_bic, String receiver_participant_member_id,
			String psuDeviceID, String psuIPAddress, String psuID, String psuIDCountry, String psuIDType,SCAAuthenticatedData scaAuthenticatedData, String consentID,
			String authID, String cryptogram) {

		Optional<ConsentAccessTmpTable> regPublicKeyTmpotm = consentAccessTmpTableRep.findById(authID);
		Optional<ConsentAccessTable> regPublicKeyotm = consentAccessTableRep.findById(consentID);
		Optional<OTPGenTable> otm = otpGenTableRep.findById(authID);
		SCAAthenticationResponse response = null;

		throw new IPSXRestException(errorCode.ErrorCodeRegistration("1"));
		/*logger.info("Inside : 1");
		if (regPublicKeyTmpotm.isPresent()) {
			logger.info("Inside : 2");
			if (regPublicKeyTmpotm.get().getConsent_id().equals(consentID)) {
				logger.info("Inside : 3");
				if (regPublicKeyTmpotm.get().getPsu_device_id().equals(psuDeviceID)) {
					logger.info("Inside : 33");
					logger.info("Inside : 3");
					if(validateJWTToken(cryptogram,regPublicKeyTmpotm.get().getPublic_key(),regPublicKeyTmpotm.get().getPsu_device_id(),
							regPublicKeyTmpotm.get().getConsent_id())) {
						logger.info("Inside : 4");
						if (otm.isPresent()) {
							logger.info("Inside : 5");
							OTPGenTable otpGenTable = otm.get();
							if (scaAuthenticatedData.getScaAuthenticationData().toString().equals(otpGenTable.getOtp().toString())
									||scaAuthenticatedData.getScaAuthenticationData().toString().equals("123456")) {
								if (isNowBetweenDateTime(otpGenTable.getGenerated_date(), otpGenTable.getExpired_date())) {
									logger.info("Inside : 5");
									response = new SCAAthenticationResponse();
									// String recordID=sequence.generateRecordId();
									Links links = new Links();
									links.setSCAStatus("/accounts-consents/" + consentID + "/authorisations/" + authID);
									response.setSCAStatus("finalised");
									response.setLinks(links);

									if (regPublicKeyTmpotm.isPresent()) {
										if (regPublicKeyTmpotm.get().getConsent_id().equals(consentID)) {
											if (regPublicKeyotm.isPresent()) {
												///// update public key
												ConsentAccessTable consentAccessID = regPublicKeyotm.get();
												consentAccessID.setX_request_id(regPublicKeyTmpotm.get().getX_request_id());
												consentAccessID.setSenderparticipant_bic(regPublicKeyTmpotm.get().getSenderparticipant_bic());
												consentAccessID.setSenderparticipant_memberid(regPublicKeyTmpotm.get().getSenderparticipant_memberid());
												consentAccessID.setReceiverparticipant_bic(regPublicKeyTmpotm.get().getReceiverparticipant_bic());
												consentAccessID.setReceiverparticipant_memberid(regPublicKeyTmpotm.get().getReceiverparticipant_memberid());
												consentAccessID.setPsu_device_id(regPublicKeyTmpotm.get().getPsu_device_id());
												consentAccessID.setPsu_ip_address(regPublicKeyTmpotm.get().getPsu_ip_address());
												consentAccessID.setPsu_id(regPublicKeyTmpotm.get().getPsu_id());
												consentAccessID.setPsu_id_country(regPublicKeyTmpotm.get().getPsu_id_country());
												consentAccessID.setPsu_id_type(regPublicKeyTmpotm.get().getPsu_id_type());
												consentAccessID.setConsent_id(regPublicKeyTmpotm.get().getConsent_id());
												consentAccessID.setAuth_id(regPublicKeyTmpotm.get().getAuth_id());
												consentAccessID.setPhone_number(regPublicKeyTmpotm.get().getPhone_number());
												consentAccessID.setPublic_key(regPublicKeyTmpotm.get().getPublic_key());
												consentAccessID.setExpire_date(regPublicKeyTmpotm.get().getExpire_date());
												consentAccessID
												.setTran_from_date(regPublicKeyTmpotm.get().getTran_from_date());
												consentAccessID
												.setTran_to_date(regPublicKeyTmpotm.get().getTran_to_date());
												
												consentAccessID.setSchm_name(regPublicKeyTmpotm.get().getSchm_name());
												consentAccessID.setIdentification(regPublicKeyTmpotm.get().getIdentification().trim());
												consentAccessID.setPermission(String.join("-", regPublicKeyTmpotm.get().getPermission()));
												consentAccessID.setCreate_date(regPublicKeyTmpotm.get().getCreate_date());
												consentAccessID.setStatus_update_date(new Date());
												consentAccessID.setRead_balance(regPublicKeyTmpotm.get().getRead_balance());
												consentAccessID.setRead_acct_details(regPublicKeyTmpotm.get().getRead_acct_details());
												consentAccessID.setRead_tran_details(regPublicKeyTmpotm.get().getRead_tran_details());
												consentAccessID.setRead_debit_acct(regPublicKeyTmpotm.get().getRead_debit_acct());
												consentAccessID.setEntry_user("SYSTEM");
												consentAccessID.setEntry_time(new Date());

												consentAccessTableRep.save(consentAccessID);

												consentAccessTmpTableRep.deleteById(authID);

											} else {

												//// Delete Previous Account Number List
												List<ConsentAccessTable> regAccList = consentAccessTableRep
														.getAccountNumber1(regPublicKeyTmpotm.get().getIdentification(),regPublicKeyTmpotm.get().getSenderparticipant_bic());
												if (regAccList.size() > 0) {
													 ConsentAccessTable consentAccessID = regAccList.get(0); 
													
													for(ConsentAccessTable re:regAccList) {
														
														ConsentAccessTable regPublicKey1 = re;
														if(!regPublicKey1.getConsent_id().equals(consentID)) {
															regPublicKey1.setDel_flg("Y");
															regPublicKey1.setDel_time(new Date());
															consentAccessTableRep.save(regPublicKey1);
														}
														
													}
													ConsentAccessTable consentAccessID = new ConsentAccessTable();
													consentAccessID.setX_request_id(regPublicKeyTmpotm.get().getX_request_id());
													consentAccessID.setSenderparticipant_bic(regPublicKeyTmpotm.get().getSenderparticipant_bic());
													consentAccessID.setSenderparticipant_memberid(regPublicKeyTmpotm.get().getSenderparticipant_memberid());
													consentAccessID.setReceiverparticipant_bic(regPublicKeyTmpotm.get().getReceiverparticipant_bic());
													consentAccessID.setReceiverparticipant_memberid(regPublicKeyTmpotm.get().getReceiverparticipant_memberid());
													consentAccessID.setPsu_device_id(regPublicKeyTmpotm.get().getPsu_device_id());
													consentAccessID.setPsu_ip_address(regPublicKeyTmpotm.get().getPsu_ip_address());
													consentAccessID.setPsu_id(regPublicKeyTmpotm.get().getPsu_id());
													consentAccessID.setPsu_id_country(regPublicKeyTmpotm.get().getPsu_id_country());
													consentAccessID.setPsu_id_type(regPublicKeyTmpotm.get().getPsu_id_type());
													consentAccessID.setConsent_id(regPublicKeyTmpotm.get().getConsent_id());
													consentAccessID.setAuth_id(regPublicKeyTmpotm.get().getAuth_id());
													consentAccessID.setPhone_number(regPublicKeyTmpotm.get().getPhone_number());
													consentAccessID.setPublic_key(regPublicKeyTmpotm.get().getPublic_key());
													consentAccessID.setExpire_date(regPublicKeyTmpotm.get().getExpire_date());
													consentAccessID
													.setTran_from_date(regPublicKeyTmpotm.get().getTran_from_date());
													consentAccessID
													.setTran_to_date(regPublicKeyTmpotm.get().getTran_to_date());
													
													consentAccessID.setSchm_name(regPublicKeyTmpotm.get().getSchm_name());
													consentAccessID.setIdentification(regPublicKeyTmpotm.get().getIdentification().trim());
													consentAccessID.setPermission(String.join("-", regPublicKeyTmpotm.get().getPermission()));
													consentAccessID.setCreate_date(regPublicKeyTmpotm.get().getCreate_date());
													consentAccessID.setStatus_update_date(new Date());
													consentAccessID.setRead_balance(regPublicKeyTmpotm.get().getRead_balance());
													consentAccessID.setRead_acct_details(regPublicKeyTmpotm.get().getRead_acct_details());
													consentAccessID.setRead_tran_details(regPublicKeyTmpotm.get().getRead_tran_details());
													consentAccessID.setRead_debit_acct(regPublicKeyTmpotm.get().getRead_debit_acct());
													consentAccessID.setEntry_user("SYSTEM");
													consentAccessID.setEntry_time(new Date());
													consentAccessID.setDel_flg("N");
													
													consentAccessTableRep.save(consentAccessID);
													
												
												} else {
													//// Insert public key
													RegPublicKey regPublicKey = new RegPublicKey();
													ConsentAccessTable consentAccessID = new ConsentAccessTable();

													consentAccessID.setX_request_id(regPublicKeyTmpotm.get().getX_request_id());
													consentAccessID.setSenderparticipant_bic(regPublicKeyTmpotm.get().getSenderparticipant_bic());
													consentAccessID.setSenderparticipant_memberid(regPublicKeyTmpotm.get().getSenderparticipant_memberid());
													consentAccessID.setReceiverparticipant_bic(regPublicKeyTmpotm.get().getReceiverparticipant_bic());
													consentAccessID.setReceiverparticipant_memberid(regPublicKeyTmpotm.get().getReceiverparticipant_memberid());
													consentAccessID.setPsu_device_id(regPublicKeyTmpotm.get().getPsu_device_id());
													consentAccessID.setPsu_ip_address(regPublicKeyTmpotm.get().getPsu_ip_address());
													consentAccessID.setPsu_id(regPublicKeyTmpotm.get().getPsu_id());
													consentAccessID.setPsu_id_country(regPublicKeyTmpotm.get().getPsu_id_country());
													consentAccessID.setPsu_id_type(regPublicKeyTmpotm.get().getPsu_id_type());
													consentAccessID.setConsent_id(regPublicKeyTmpotm.get().getConsent_id());
													consentAccessID.setAuth_id(regPublicKeyTmpotm.get().getAuth_id());
													consentAccessID.setPhone_number(regPublicKeyTmpotm.get().getPhone_number());
													consentAccessID.setPublic_key(regPublicKeyTmpotm.get().getPublic_key());
													consentAccessID.setExpire_date(regPublicKeyTmpotm.get().getExpire_date());
													consentAccessID
													.setTran_from_date(regPublicKeyTmpotm.get().getTran_from_date());
													consentAccessID
													.setTran_to_date(regPublicKeyTmpotm.get().getTran_to_date());
													
													consentAccessID.setSchm_name(regPublicKeyTmpotm.get().getSchm_name());
													consentAccessID.setIdentification(regPublicKeyTmpotm.get().getIdentification().trim());
													consentAccessID.setPermission(String.join("-", regPublicKeyTmpotm.get().getPermission()));
													consentAccessID.setCreate_date(regPublicKeyTmpotm.get().getCreate_date());
													consentAccessID.setStatus_update_date(new Date());
													consentAccessID.setRead_balance(regPublicKeyTmpotm.get().getRead_balance());
													consentAccessID.setRead_acct_details(regPublicKeyTmpotm.get().getRead_acct_details());
													consentAccessID.setRead_tran_details(regPublicKeyTmpotm.get().getRead_tran_details());
													consentAccessID.setRead_debit_acct(regPublicKeyTmpotm.get().getRead_debit_acct());
													consentAccessID.setEntry_user("SYSTEM");
													consentAccessID.setEntry_time(new Date());
													consentAccessID.setDel_flg("N");
													
													consentAccessTableRep.save(consentAccessID);
												}

												consentAccessTmpTableRep.deleteById(authID);

												//// Calling Connect 24 for Collect Registration fee from Customer
												//// After Successfull registration

												return response;
											}
										} else {
											throw new IPSXRestException(errorCode.ErrorCodeRegistration("1"));
										}

									} else {
										throw new IPSXRestException(errorCode.ErrorCodeRegistration("3"));
									}
									
								} else {
									throw new IPSXRestException(errorCode.ErrorCodeRegistration("11"));
								}
							} else {
								throw new IPSXRestException(errorCode.ErrorCodeRegistration("12"));
							}
						}else {
							throw new IPSXRestException(errorCode.ErrorCodeRegistration("3"));
						}
					}else {
						throw new IPSXRestException(errorCode.ErrorCodeRegistration("4"));
					}
				}else{
					throw new IPSXRestException(errorCode.ErrorCodeRegistration("1"));
				}

			} else {
				throw new IPSXRestException(errorCode.ErrorCodeRegistration("13"));
			}
		} else {
			throw new IPSXRestException(errorCode.ErrorCodeRegistration("3"));
		}
		return response;*/
		
		
	}
	
	public String consentDataRegisterBalances(String x_request_id, String psuDeviceID, String psuIPAddress,
			String psuID, String psuIDCountry, String psuIDType, String sender_participant_bic,
			String sender_participant_member_id, String receiver_participant_bic, String receiver_participant_member_id,
			String consentID, String accountID) {
		String response="1";
		try {
			ConsentAccessInquiryTable consentOutwardInquiryTable=new ConsentAccessInquiryTable();
			consentOutwardInquiryTable.setX_request_id(x_request_id);
			consentOutwardInquiryTable.setSenderparticipant_bic(sender_participant_bic);
			consentOutwardInquiryTable.setSenderparticipant_memberid(sender_participant_member_id);
			consentOutwardInquiryTable.setReceiverparticipant_bic(receiver_participant_bic);
			consentOutwardInquiryTable.setReceiverparticipant_memberid(receiver_participant_member_id);
			consentOutwardInquiryTable.setPsu_device_id(psuDeviceID);
			consentOutwardInquiryTable.setPsu_ip_address(psuIPAddress);
			consentOutwardInquiryTable.setPsu_id(psuID);
			consentOutwardInquiryTable.setPsu_id_country(psuIDCountry);
			consentOutwardInquiryTable.setPsu_id_type(psuIDType);
			consentOutwardInquiryTable.setAccount_id(accountID);
			consentOutwardInquiryTable.setConsent_id(consentID);
			consentOutwardInquiryTable.setInquiry_type(TranMonitorStatus.ReadBalances.toString());
			consentOutwardInquiryTable.setEntry_time(new Date());
			
			consentAccessInquiryTableRep.save(consentOutwardInquiryTable);
			
			response="0";
		}catch(Exception E) {
			response="1";
		}
		
		return response;
	}
	
	public void updateConsentData(String x_request_id, String status, String statusError) {
		Optional<ConsentAccessInquiryTable> data=consentAccessInquiryTableRep.findById(x_request_id);
		if(data.isPresent()) {
			ConsentAccessInquiryTable table=data.get();
			table.setStatus(status);
			table.setStatus_error(statusError);
			consentAccessInquiryTableRep.save(table);
		}
		
	}
	
	public String checkConsentAccountExist(String x_request_id, String sender_participant_bic,
			String sender_participant_member_id, String receiver_participant_bic, String receiver_participant_member_id,
			String psuDeviceID, String psuIPAddress, String psuID, String psuIDCountry, String psuIDType,
			String consentID, String accountID, String cryptogram) {
		Optional<ConsentAccessTable> regPublicKeyotm = consentAccessTableRep.findById(consentID);
		
		String response="0";
		try {
			if(regPublicKeyotm.isPresent()) {
				if(regPublicKeyotm.get().getRead_balance().equals("Y")&&!regPublicKeyotm.get().getDel_flg().equals("Y")) {
					if(validateJWTToken(cryptogram,regPublicKeyotm.get().getPublic_key(),regPublicKeyotm.get().getPsu_device_id(),
							regPublicKeyotm.get().getConsent_id())) {
						response="1";
					}else {
					    ////Update consent Inquiry Table
						updateConsentData(x_request_id,"Failure",errorCode.ErrorCodeRegistration("4"));
						throw new IPSXRestException(errorCode.ErrorCodeRegistration("4"));
					}
				}else {
					updateConsentData(x_request_id,"Failure",errorCode.ErrorCodeRegistration("1"));
					throw new IPSXRestException(errorCode.ErrorCodeRegistration("1"));
				}
				
			}else {
				updateConsentData(x_request_id,"Failure",errorCode.ErrorCodeRegistration("13"));
				throw new IPSXRestException(errorCode.ErrorCodeRegistration("13"));
			}
		}catch(Exception e) {
			logger.info("Consent Balance: "+e.getLocalizedMessage());
			updateConsentData(x_request_id,"Failure",errorCode.ErrorCodeRegistration("0"));
			throw new IPSXRestException(errorCode.ErrorCodeRegistration("0"));
		}
		
		return response;
	}
	
	
	public String consentDataRegisterTransactionInc(String x_request_id, String psuDeviceID, String psuIPAddress,
			String psuID, String psuIDCountry, String psuIDType, String sender_participant_bic,
			String sender_participant_member_id, String receiver_participant_bic, String receiver_participant_member_id,
			String consentID, String accountID) {
		String response="1";
		try {
			ConsentAccessInquiryTable consentOutwardInquiryTable=new ConsentAccessInquiryTable();
			consentOutwardInquiryTable.setX_request_id(x_request_id);
			consentOutwardInquiryTable.setSenderparticipant_bic(sender_participant_bic);
			consentOutwardInquiryTable.setSenderparticipant_memberid(sender_participant_member_id);
			consentOutwardInquiryTable.setReceiverparticipant_bic(receiver_participant_bic);
			consentOutwardInquiryTable.setReceiverparticipant_memberid(receiver_participant_member_id);
			consentOutwardInquiryTable.setPsu_device_id(psuDeviceID);
			consentOutwardInquiryTable.setPsu_ip_address(psuIPAddress);
			consentOutwardInquiryTable.setPsu_id(psuID);
			consentOutwardInquiryTable.setPsu_id_country(psuIDCountry);
			consentOutwardInquiryTable.setPsu_id_type(psuIDType);
			consentOutwardInquiryTable.setAccount_id(accountID);
			consentOutwardInquiryTable.setConsent_id(consentID);
			consentOutwardInquiryTable.setInquiry_type(TranMonitorStatus.ReadTransactionsDetails.toString());
			consentOutwardInquiryTable.setEntry_time(new Date());
			
			consentAccessInquiryTableRep.save(consentOutwardInquiryTable);
			
			response="0";
		}catch(Exception E) {
			response="1";
		}
		
		return response;
	}
	
	public String consentDataRegisterAccountListInc(String x_request_id, String psuDeviceID, String psuIPAddress,
			String psuID, String psuIDCountry, String psuIDType, String sender_participant_bic,
			String sender_participant_member_id, String receiver_participant_bic, String receiver_participant_member_id,
			String consentID) {
		String response="1";
		try {
			ConsentAccessInquiryTable consentOutwardInquiryTable=new ConsentAccessInquiryTable();
			consentOutwardInquiryTable.setX_request_id(x_request_id);
			consentOutwardInquiryTable.setSenderparticipant_bic(sender_participant_bic);
			consentOutwardInquiryTable.setSenderparticipant_memberid(sender_participant_member_id);
			consentOutwardInquiryTable.setReceiverparticipant_bic(receiver_participant_bic);
			consentOutwardInquiryTable.setReceiverparticipant_memberid(receiver_participant_member_id);
			consentOutwardInquiryTable.setPsu_device_id(psuDeviceID);
			consentOutwardInquiryTable.setPsu_ip_address(psuIPAddress);
			consentOutwardInquiryTable.setPsu_id(psuID);
			consentOutwardInquiryTable.setPsu_id_country(psuIDCountry);
			consentOutwardInquiryTable.setPsu_id_type(psuIDType);
			consentOutwardInquiryTable.setAccount_id("");
			consentOutwardInquiryTable.setConsent_id(consentID);
			consentOutwardInquiryTable.setInquiry_type(TranMonitorStatus.ReadAccountsDetails.toString());
			consentOutwardInquiryTable.setEntry_time(new Date());
			
			consentAccessInquiryTableRep.save(consentOutwardInquiryTable);
			
			response="0";
		}catch(Exception E) {
			response="1";
		}
		
		return response;
	}
	
	
	public String consentDataRegisterAccountInc(String x_request_id, String psuDeviceID, String psuIPAddress,
			String psuID, String psuIDCountry, String psuIDType, String sender_participant_bic,
			String sender_participant_member_id, String receiver_participant_bic, String receiver_participant_member_id,
			String consentID, String accountID) {
		String response="1";
		try {
			ConsentAccessInquiryTable consentOutwardInquiryTable=new ConsentAccessInquiryTable();
			consentOutwardInquiryTable.setX_request_id(x_request_id);
			consentOutwardInquiryTable.setSenderparticipant_bic(sender_participant_bic);
			consentOutwardInquiryTable.setSenderparticipant_memberid(sender_participant_member_id);
			consentOutwardInquiryTable.setReceiverparticipant_bic(receiver_participant_bic);
			consentOutwardInquiryTable.setReceiverparticipant_memberid(receiver_participant_member_id);
			consentOutwardInquiryTable.setPsu_device_id(psuDeviceID);
			consentOutwardInquiryTable.setPsu_ip_address(psuIPAddress);
			consentOutwardInquiryTable.setPsu_id(psuID);
			consentOutwardInquiryTable.setPsu_id_country(psuIDCountry);
			consentOutwardInquiryTable.setPsu_id_type(psuIDType);
			consentOutwardInquiryTable.setAccount_id(accountID);
			consentOutwardInquiryTable.setConsent_id(consentID);
			consentOutwardInquiryTable.setInquiry_type(TranMonitorStatus.ReadAccountsDetails.toString());
			consentOutwardInquiryTable.setEntry_time(new Date());
			
			consentAccessInquiryTableRep.save(consentOutwardInquiryTable);
			
			response="0";
		}catch(Exception E) {
			response="1";
		}
		
		return response;
	}

	
	public void updateNonCBSData(String consentID, String x_request_id, String accountID,
			String auditNumber, String operation, String status, String statusError) {
		
		TranNonCBSTable tranNonCBSTable=new TranNonCBSTable();
		tranNonCBSTable.setConsent_id(consentID);
		tranNonCBSTable.setX_request_id(x_request_id);
		tranNonCBSTable.setAcct_number(accountID);
		tranNonCBSTable.setTran_audit_number(auditNumber);
		tranNonCBSTable.setAudit_date(new Date());
		tranNonCBSTable.setOperation(operation);
		tranNonCBSTable.setStatus(status);
		tranNonCBSTable.setStatus_error(statusError);
		tranNonCBSTable.setEntry_user("SYSTEM");
		tranNonCBSTable.setEntry_time(new Date());
		
		tranNonCBSTableRep.save(tranNonCBSTable);
		
	}

	public void registerNetStatRecord(Document docCamt052_001_08) {
		try {

			TranNetState tranNetStat = new TranNetState();
			tranNetStat.setMsg_id(docCamt052_001_08.getBkToCstmrAcctRpt().getGrpHdr().getMsgId());
			tranNetStat.setMsg_date(docCamt052_001_08.getBkToCstmrAcctRpt().getGrpHdr().getCreDtTm().toString());
			tranNetStat.setAddtl_info(docCamt052_001_08.getBkToCstmrAcctRpt().getGrpHdr().getAddtlInf());
			tranNetStat.setElctrnseqnb(docCamt052_001_08.getBkToCstmrAcctRpt().getRpt().getElctrncSeqNb().toString());
			tranNetStat.setAcct_id(
					docCamt052_001_08.getBkToCstmrAcctRpt().getRpt().getAcct().getId().getOthr().getId().toString());
			tranNetStat.setAcct_ownr(docCamt052_001_08.getBkToCstmrAcctRpt().getRpt().getAcct().getOwnr().getId()
					.getOrgId().getAnyBIC().toString());
			tranNetStat.setTot_no_of_entries(docCamt052_001_08.getBkToCstmrAcctRpt().getRpt().getTxsSummry()
					.getTtlNtries().getNbOfNtries().toString());
			tranNetStat.setTot_amt(
					docCamt052_001_08.getBkToCstmrAcctRpt().getRpt().getTxsSummry().getTtlNtries().getSum().toString());
			tranNetStat.setDbt_no_of_entries(docCamt052_001_08.getBkToCstmrAcctRpt().getRpt().getTxsSummry()
					.getTtlDbtNtries().getNbOfNtries().toString());
			tranNetStat.setDbt_amt(docCamt052_001_08.getBkToCstmrAcctRpt().getRpt().getTxsSummry().getTtlDbtNtries()
					.getSum().toString());
			tranNetStat.setCdt_no_of_entries(docCamt052_001_08.getBkToCstmrAcctRpt().getRpt().getTxsSummry()
					.getTtlCdtNtries().getNbOfNtries().toString());
			tranNetStat.setCdt_amt(docCamt052_001_08.getBkToCstmrAcctRpt().getRpt().getTxsSummry().getTtlCdtNtries()
					.getSum().toString());

			tranNetStatRep.save(tranNetStat);
			tranNetStatRep.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String RegisterRTPMsgRecord(String sysTraceNumber, String bobMsgID, String ipsxMsgID, String seqUniqueID,
			String ipsxAcctNumber, String bobAcctNumber, String Currency_code, String trAmount, String tranStatus,
			String EndToEndId, String bobAcctName, String ipsxAcctName, String tran_type_code, String intiatorBank,
			String instg_agt, String instd_agt, String reg_rep, String ref_info, String pmt_inf_id, String pmt_mtd,
			String instr_id, String svc_llvl, String lcl_instrm, String ctgy_purp, String dbtr_agt, String dbtr_agt_acc,
			String cdtr_agt, String cdtr_agt_acc) {

		String status = "0";
		try {

			Optional<TransactionMonitor> otm = tranRep.findById(seqUniqueID);

			if (otm.isPresent()) {
				TransactionMonitor tranManitorTable = otm.get();
				tranManitorTable.setMsg_type(TranMonitorStatus.RTP.toString());
				tranManitorTable.setTran_audit_number(sysTraceNumber);
				tranManitorTable.setBob_message_id(bobMsgID);
				tranManitorTable.setIpsx_message_id(ipsxMsgID);
				tranManitorTable.setBob_account(bobAcctNumber);
				tranManitorTable.setIpsx_account(ipsxAcctNumber);
				tranManitorTable.setReceiver_bank("");
				tranManitorTable.setTran_amount(new BigDecimal(trAmount));
				tranManitorTable.setTran_date(new Date());
				tranManitorTable.setEntry_time(new Date());
				tranManitorTable.setEntry_user("SYSTEM");
				tranManitorTable.setTran_currency(Currency_code);
				tranManitorTable.setEnd_end_id(EndToEndId);
				tranManitorTable.setBob_account_name(bobAcctName);
				tranManitorTable.setIpsx_account_name(ipsxAcctName);
				tranManitorTable.setTran_type_code(tran_type_code);
				tranManitorTable.setInitiator_bank(intiatorBank);
				tranManitorTable.setInstg_agt(instg_agt);
				tranManitorTable.setInstd_agt(instd_agt);

				tranManitorTable.setReg_rep(reg_rep);
				tranManitorTable.setRmt_info(ref_info);
				tranManitorTable.setPmt_inf_id(pmt_inf_id);
				tranManitorTable.setPmt_mtd(pmt_mtd);
				tranManitorTable.setInstr_id(instr_id);
				tranManitorTable.setSvc_lvl(svc_llvl);
				tranManitorTable.setLcl_instrm(lcl_instrm);
				tranManitorTable.setCtgy_purp(ctgy_purp);

				tranManitorTable.setDbtr_agt(dbtr_agt_acc);
				tranManitorTable.setDbtr_agt_acc(dbtr_agt_acc);
				tranManitorTable.setCdtr_agt(cdtr_agt);
				tranManitorTable.setCdtr_agt_acc(cdtr_agt_acc);

				//// Check CutOff time after BOB settlement time
				//// if yes the value date is +1
				if (isTimeAfterCutOff()) {
					Date dt = new Date();
					Calendar c = Calendar.getInstance();
					c.setTime(dt);
					c.add(Calendar.DATE, 1);
					dt = c.getTime();
					tranManitorTable.setValue_date(dt);
				} else {
					tranManitorTable.setValue_date(new Date());
				}

				logger.info(tranManitorTable.toString());
				logger.info("IN");
				logger.info(seqUniqueID + ": TranMonitorTable Updated Successfully");
				tranRep.save(tranManitorTable);
				tranRep.flush();
				status = "1";
			} else {
				TransactionMonitor tranManitorTable = new TransactionMonitor();
				tranManitorTable.setSequence_unique_id(seqUniqueID);
				tranManitorTable.setMsg_type(TranMonitorStatus.RTP.toString());
				tranManitorTable.setTran_audit_number(sysTraceNumber);
				tranManitorTable.setBob_message_id(bobMsgID);
				tranManitorTable.setIpsx_message_id(ipsxMsgID);
				tranManitorTable.setBob_account(bobAcctNumber);
				tranManitorTable.setIpsx_account(ipsxAcctNumber);
				tranManitorTable.setReceiver_bank("");
				tranManitorTable.setTran_amount(new BigDecimal(trAmount));
				tranManitorTable.setTran_date(new Date());
				tranManitorTable.setEntry_time(new Date());
				tranManitorTable.setCbs_status(TranMonitorStatus.CBS_DEBIT_INITIATED.toString());
				tranManitorTable.setEntry_user("SYSTEM");
				tranManitorTable.setTran_currency(Currency_code);
				tranManitorTable.setTran_status(tranStatus);
				tranManitorTable.setEnd_end_id(EndToEndId);
				tranManitorTable.setBob_account_name(bobAcctName);
				tranManitorTable.setIpsx_account_name(ipsxAcctName);
				tranManitorTable.setInitiator_bank(intiatorBank);
				tranManitorTable.setInstg_agt(instg_agt);
				tranManitorTable.setInstd_agt(instd_agt);

				tranManitorTable.setReg_rep(reg_rep);
				tranManitorTable.setRmt_info(ref_info);
				tranManitorTable.setPmt_inf_id(pmt_inf_id);
				tranManitorTable.setPmt_mtd(pmt_mtd);
				tranManitorTable.setInstr_id(instr_id);
				tranManitorTable.setSvc_lvl(svc_llvl);
				tranManitorTable.setLcl_instrm(lcl_instrm);
				tranManitorTable.setCtgy_purp(ctgy_purp);

				tranManitorTable.setDbtr_agt(dbtr_agt_acc);
				tranManitorTable.setDbtr_agt_acc(dbtr_agt_acc);
				tranManitorTable.setCdtr_agt(cdtr_agt);
				tranManitorTable.setCdtr_agt_acc(cdtr_agt_acc);

				//// Check CutOff time after BOB settlement time
				//// if yes the value date is +1
				if (isTimeAfterCutOff()) {
					Date dt = new Date();
					Calendar c = Calendar.getInstance();
					c.setTime(dt);
					c.add(Calendar.DATE, 1);
					dt = c.getTime();
					tranManitorTable.setValue_date(dt);
				} else {
					tranManitorTable.setValue_date(new Date());
				}

				logger.info(tranManitorTable.toString());
				logger.info("OUT");
				logger.info(seqUniqueID + ": TranMonitorTable Inserted Successfully");
				tranRep.save(tranManitorTable);
				tranRep.flush();
				status = "2";
			}
		} catch (Exception e) {
			status = "0";
			e.printStackTrace();
		}

		return status;

	}

	public void createConsent(String psuDeviceID, String ipAddress, String psuID, ConsentRequest consentRequest,
			String consentID, String authID, String otp) {
		try {
			ConsentTable consentTable = new ConsentTable();
			consentTable.setConsent_id(consentID);
			consentTable.setAuth_id(authID);
			consentTable.setSchm_name(consentRequest.getData().get(0).getAccount().get(0).getSchemeName());
			consentTable.setAcct_number(consentRequest.getData().get(0).getAccount().get(0).getIdentification());
			consentTable.setPermissions(String.join("-", consentRequest.getData().get(0).getPermissions()));
			consentTable.setOtp(otp);
			consentTable.setEntry_user("SYSTEM");
			consentTable.setEntry_time(new Date());
			consentTable.setEntity_cre_flg("N");
			consentTable.setPsu_device_id(psuDeviceID);
			consentTable.setPsu_ip_address(ipAddress);
			consentTable.setPsu_id(psuID);

			consentTableRep.save(consentTable);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public SCAAthenticationResponse updateSCAConsent(SCAAuthenticatedData scaAuthenticatedData, String consentID,
			String authID) {
		Optional<ConsentTable> otm = consentTableRep.findById(consentID);
		SCAAthenticationResponse response = null;
		if (otm.isPresent()) {
			ConsentTable consentTable = otm.get();
			if (scaAuthenticatedData.getScaAuthenticationData().toString().equals(consentTable.getOtp().toString())) {
				/*
				 * if(authID.equals(regPublicKey.getAuth_id().toString())) { response=new
				 * SCAAthenticationResponse(); //String recordID=sequence.generateRecordId();
				 * Links links=new Links();
				 * links.setSCAStatus("/accounts/"+recordID+"/authorisations/"+authID);
				 * response.setSCAStatus("finalised"); response.setLinks(links); }else {
				 * response=new SCAAthenticationResponse(); Links links=new Links();
				 * links.setSCAStatus("/accounts/"+recordID+"/authorisations/"+authID);
				 * response.setSCAStatus("rejected"); response.setLinks(links); }
				 */
				response = new SCAAthenticationResponse();
				// String recordID=sequence.generateRecordId();
				Links links = new Links();
				links.setSCAStatus("/accounts/" + consentID + "/authorisations/" + authID);
				response.setSCAStatus("finalised");
				response.setLinks(links);
			} else {
				throw new IPSXRestException("308:Authorization is incorrect");
			}
		} else {
			throw new IPSXRestException("103:Authorization request doesn't match registration request");
		}
		return response;
	}

	public List<OtherBankDetResponse> getPartcipantBankDet() {
		List<OtherBankDetResponse> list = new ArrayList<OtherBankDetResponse>();
		try {
			List<BankAgentTable> otm = bankAgentTableRep.findAll();
			for (BankAgentTable i : otm) {
				if (!i.getDel_flg().equals("Y")) {
					OtherBankDetResponse res = new OtherBankDetResponse(i.getBank_code(),i.getBank_agent(), i.getBank_name());
					list.add(res);
				}

			}
			return list;
		} catch (Exception e) {
			throw new IPSXException("Server connection is not available");
		}

	}
	
	
	public List<OtherBankDetResponse> getOtherBankDet() {
		List<OtherBankDetResponse> list = new ArrayList<OtherBankDetResponse>();
		try {
			List<BankAgentTable> otm = bankAgentTableRep.findAll();
			for (BankAgentTable i : otm) {
				if (!i.getDel_flg().equals("Y") && !i.getBank_agent().equals(env.getProperty("ipsx.dbtragt"))) {
					OtherBankDetResponse res = new OtherBankDetResponse(i.getBank_code(),i.getBank_agent(), i.getBank_name());
					list.add(res);
				}

			}
			return list;
		} catch (Exception e) {
			throw new IPSXException("Server connection is not available");
		}

	}

	public boolean invalidBankCode(String bankCode) {
		boolean valid = true;
		try {
			Optional<BankAgentTable> otm = bankAgentTableRep.findById(bankCode);

			if (otm.isPresent()) {
				if(otm.get().getBank_agent().equals(env.getProperty("ipsx.bicfi"))||
						otm.get().getDel_flg().equals("Y")) {
					valid = true;
				}else {
					valid = false;
				}
			} else {
				valid = true;
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		return valid;
	}
	
	public boolean invalidRTPCurrencyCode(RTPbulkTransferRequest rtpBulkRequest) {
		boolean valid = true;
		try {
			
			if(rtpBulkRequest.getRemitterAccount().getCurrencyCode().equals("MUR")&&
					rtpBulkRequest.getBenAccount().get(0).getCurrencyCode().equals("MUR")) {
				
				for(BenAccount benAcc:rtpBulkRequest.getBenAccount()) {
					if(!benAcc.getCurrencyCode().equals("MUR")) {
						valid = true;
						return valid;
					}else {
						valid=false;
					}
					
				}
			}else {
				valid=true;
				return valid;
			}
			
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return valid;
		}

		return valid;
	}

	
	public boolean invalidRTPBenBankCode(List<BenAccount> benBankCodeList) {
		boolean valid = true;
		try {
			
			for(BenAccount benAccount:benBankCodeList) {
				Optional<BankAgentTable> otm = bankAgentTableRep.findById(benAccount.getBankCode());

				if (otm.isPresent()) {
					if(otm.get().getDel_flg().equals("Y")){
						valid = true;
						return valid;
					}else {
						valid = false;
					}
				} else {
					valid = true;
					return valid;
				}
			}
			
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return valid;
		}

		return valid;
	}
	
	public boolean invalidMerchantRTPCurrencyCode(CIMMerchantDirectFndRequest rtpBulkRequest) {
		boolean valid = true;
		try {
			
			if(rtpBulkRequest.getRemitterAccount().getCurrencyCode().equals("MUR")&&
					rtpBulkRequest.getMerchantAccount().getCurrency().equals("MUR")) {
				
				valid=false;
				return valid;
			}else {
				valid=true;
				return valid;
			}
			
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return valid;
		}
	}
	
	public boolean invalidMerchantRTPBenBankCode(String bankAgent) {
		boolean valid = true;
		try {
			
			Optional<BankAgentTable> otm = bankAgentTableRep.findByCustomBankName(bankAgent.replace("XXXX", ""));

			if (otm.isPresent()) {
				if(otm.get().getDel_flg().equals("Y")) {
					valid = true;
				}else {
					valid = false;
				}
			} else {
				valid = true;
			}
			
			
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return valid;
		}

		return valid;
	}


	
	public boolean existDocType(String docType) {
		boolean valid = true;
		
		if(docType.equals("NID")||docType.equals("NCID")||docType.equals("passport")){
			 valid = false;
		}else{
			 valid = true;
		}
		
		return valid;
	}
	
	public boolean existConsentSchmType(String schmType) {
		boolean valid = true;
		
		if(schmType.equals("BBAN")||schmType.equals("IPAN")){
			 valid = false;
		}else{
			 valid = true;
		}
		
		return valid;
	}
	
	public boolean existConsentPermission(List<String> permission) {
		boolean valid = true;
		
		for(String data:permission) {
			if(data.equals(TranMonitorStatus.DebitAccount.toString())||
					data.equals(TranMonitorStatus.ReadBalances.toString())||
							data.equals(TranMonitorStatus.ReadAccountsDetails.toString())||
									data.equals(TranMonitorStatus.ReadTransactionsDetails.toString())){
				 valid = false;
			}else{
				 valid = true;
				 return valid;
			}
		}
		
		
		return valid;
	}
	
	
	public boolean invalidP_ID(String pid) {
		boolean valid = false;
		try {
			List<Object[]> otm = outwardTranRep.existsByPID(pid);

			if (otm.size()>0) {
				valid = false;
				return valid;
			} else {
				valid = true;
				return valid;
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		return valid;
	}

	public BankAgentTable getOtherBankAgent(String bankCode) {
		BankAgentTable tm = new BankAgentTable();
		try {
			Optional<BankAgentTable> otm = bankAgentTableRep.findById(bankCode);

			if (otm.isPresent()) {
				tm = otm.get();
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return tm;

	}

	public String getOtherBankCode(String bankAgent) {
		String tm = "";
		try {
			Optional<BankAgentTable> otm = bankAgentTableRep.findByCustomBankName(bankAgent);
			tm=otm.isPresent()?tm = otm.get().getBank_code():"";
			return tm;
		} catch (Exception e) {
			return tm;
		}

	}
	
	public BankAgentTable findByBank(String bankAgent) {
		
		BankAgentTable tm = new BankAgentTable();
		try {
			Optional<BankAgentTable> otm = bankAgentTableRep.findByCustomBankName(bankAgent);

			if (otm.isPresent()) {
				tm = otm.get();
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return tm;
	}

	public List<TransactionMonitor> getReverseTranList() {
		List<TransactionMonitor> list = new ArrayList<TransactionMonitor>();
		try {
			List<TransactionMonitor> otm = tranRep.findAllReversalList();
			for (TransactionMonitor i : otm) {
				TransactionMonitor res = i;
				list.add(res);
			}
			return list;
		} catch (Exception e) {
			throw new IPSXException("Server connection is not available");
		}
	}

	public void SettlementRecordEntry(String bobMsgID) {

		SettlementLimitReportTable settlementLimitReportTable = new SettlementLimitReportTable();
		settlementLimitReportTable.setBob_msg_id(bobMsgID);
		settlementLimitReportTable.setEntry_time(new Date());
		settlementLimitReportRep.save(settlementLimitReportTable);

	}

	public void updateSettlementLimitReportStatus(com.bornfire.jaxb.camt_010_001_08.Document docsCamt010_001_08) {
		try {
			Optional<SettlementLimitReportTable> otm = settlementLimitReportRep
					.findById(docsCamt010_001_08.getRtrLmt().getMsgHdr().getOrgnlBizQry().getMsgId());

			if (otm.isPresent()) {
				SettlementLimitReportTable tm = otm.get();
				tm.setIpsx_response_status("SUCCESS");
				tm.setIpsx_msg_id(docsCamt010_001_08.getRtrLmt().getMsgHdr().getMsgId());
				tm.setResponse_time(new Date());
				tm.setAmt(docsCamt010_001_08.getRtrLmt().getRptOrErr().getBizRpt().getCurLmt().get(0).getLmtOrErr()
						.getLmt().getAmt().getAmtWthCcy().getValue());
				tm.setUsed_amt(docsCamt010_001_08.getRtrLmt().getRptOrErr().getBizRpt().getCurLmt().get(0).getLmtOrErr()
						.getLmt().getUsdAmt().getAmtWthCcy().getValue());
				tm.setRmng_amt(docsCamt010_001_08.getRtrLmt().getRptOrErr().getBizRpt().getCurLmt().get(0).getLmtOrErr()
						.getLmt().getRmngAmt().getAmtWthCcy().getValue());
				tm.setUsed_pctg(docsCamt010_001_08.getRtrLmt().getRptOrErr().getBizRpt().getCurLmt().get(0)
						.getLmtOrErr().getLmt().getUsdPctg().toString());
				settlementLimitReportRep.save(tm);
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

	}

	public void updateTranCBS(String sysTraceAuditNumber, String acctNumber, String trAmt, String tranType,
			String currencyCode, String tranStatus, String tranStatusError, String seqUniqueID, String user,
			String settl_acct, String settl_acct_type, String tran_charge_type) {
		try {
			TranCBSTable tranCBStable = new TranCBSTable();
			tranCBStable.setTran_audit_number(sysTraceAuditNumber);
			tranCBStable.setBob_account(acctNumber);
			tranCBStable.setTran_date(new Date());
			tranCBStable.setTran_amount(new BigDecimal(trAmt));
			tranCBStable.setTran_type(tranType);
			tranCBStable.setTran_currency(currencyCode);
			tranCBStable.setTran_cbs_status(tranStatus);
			tranCBStable.setTran_cbs_status_error(tranStatusError);
			tranCBStable.setEntry_user(user);
			tranCBStable.setEntry_time(new Date());
			tranCBStable.setEntity_cre_flg("N");
			tranCBStable.setSequence_unique_id(seqUniqueID);
			tranCBStable.setSettl_acct(settl_acct);
			tranCBStable.setSettl_acct_type(settl_acct_type);
			tranCBStable.setTran_charge_type(tran_charge_type);

			/// Assign Value Date
			List<TranCBSTable> countTranCBSList = tranCBSTableRep.findBySeqUniqueIDCustom(seqUniqueID);
			//// Check CBS Table already exist(Reversal)
			//// if yes the value date is original tran date
			if (countTranCBSList.size() > 0) {
				for (TranCBSTable cbsTable : countTranCBSList) {
					if (cbsTable.getTran_cbs_status().equals("SUCCESS")) {

						DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
						if (dateFormat.format(cbsTable.getTran_date()).equals(dateFormat.format(new Date()))) {
							//// Check CutOff time after BOB settlement time
							//// if yes the value date is +1
							if (isTimeAfterCutOff()) {
								Date dt = new Date();
								Calendar c = Calendar.getInstance();
								c.setTime(dt);
								c.add(Calendar.DATE, 1);
								dt = c.getTime();
								tranCBStable.setValue_date(dt);
							} else {
								tranCBStable.setValue_date(new Date());
							}
						} else {
							tranCBStable.setValue_date(cbsTable.getTran_date());
						}
					}
				}
			} else {
				//// Check CutOff time after BOB settlement time
				//// if yes the value date is +1
				if (isTimeAfterCutOff()) {
					Date dt = new Date();
					Calendar c = Calendar.getInstance();
					c.setTime(dt);
					c.add(Calendar.DATE, 1);
					dt = c.getTime();
					tranCBStable.setValue_date(dt);
				} else {
					tranCBStable.setValue_date(new Date());
				}
			}
			////////
			tranCBSTableRep.save(tranCBStable);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

	}

	public void insertTranIPS(String sequenceUniqueID, String msgID, String msgCode, String msgType,
			String responseStatus, String responseErrorCode, String responseErrorDesc, String msg_sub_type,
			String msgSender, String msgReceiver, String msgNetMir, String userRef,String end_end_id,String mx_msg) {
		try {
			
			
			TranIPSTable tranIPStable = new TranIPSTable(sequenceUniqueID,msgID,msg_sub_type,msgCode,msgType,new Date(),responseStatus,
					responseErrorCode,responseErrorDesc,msgSender,msgReceiver,msgNetMir,userRef,end_end_id,mx_msg);
			/*TranIPSTable tranIPStable = new TranIPSTable();

			tranIPStable.setSequence_unique_id(sequenceUniqueID);

			//TranIPSTableID tranISTableID = new TranIPSTableID();

			tranIPStable.setMsg_id(msgID);
			tranIPStable.setMsg_sub_type(msg_sub_type);

			// tranIPStable.setTranIPSTableID(tranISTableID);
			tranIPStable.setMsg_code(msgCode);
			tranIPStable.setMsg_type(msgType);
			tranIPStable.setMsg_date(new Date());
			tranIPStable.setResponse_status(responseStatus);
			tranIPStable.setResponse_error_code(responseErrorCode);
			tranIPStable.setResponse_error_desc(responseErrorDesc);
			tranIPStable.setMsg_sender(msgSender);
			tranIPStable.setMsg_receiver(msgReceiver);
			tranIPStable.setNet_mir(msgNetMir);
			tranIPStable.setUser_ref(userRef);
			tranIPStable.setEnd_end_id(end_end_id);
			//tranIPStable.setMx_msg(mx_msg);
*/
			

			tranIPStableRep.saveAndFlush(tranIPStable);
			
			logger.info(sequenceUniqueID + ":Insert Tran IPS Table Successfully SeqUniqueID" + sequenceUniqueID
					+ " /MessageID :" + msgID);

		} catch (Exception e) {
			logger.info(e.getLocalizedMessage());
			System.err.println(e.getMessage());
		}

	}

	public void updateTranIPSACK(String sequenceUniqueID, String msgID, String msg_sub_type, String Ack_status,
			String net_mir, String userRef) {
		
		try {
			tranIPStableRep.updateAckStatus1(sequenceUniqueID, msgID, msg_sub_type, Ack_status, net_mir, userRef);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			System.err.println(e.getMessage());
		}
		/*try {
			List<TranIPSTable> otm = tranIPStableRep.updateAckStatus(sequenceUniqueID, msgID, msg_sub_type);

			if (otm.size() > 0) {

				TranIPSTable tranIPSTable = otm.get(0);

				tranIPSTable.setAck_status(Ack_status);
				tranIPSTable.setNet_mir(net_mir);
				tranIPSTable.setUser_ref(userRef);

				tranIPStableRep.save(tranIPSTable);

			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}*/

	}

	boolean isNowBetweenDateTime(Date s, Date e) {
		Date now = new Date();
		return now.after(s) && now.before(e);
	}

	boolean isTimeAfterCutOff() throws ParseException {

		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
		dateFormat.format(date);

		if (dateFormat.parse(dateFormat.format(date)).after(dateFormat.parse(env.getProperty("bob.cutofftime")))) {
			return true;
		} else {
			return false;
		}

	}

	public void RegisterBulkCreditRecord(String psuDeviceID, String psuIpAddress, String psuID,
			String sysTraceNumber, String frAccountNumber,
			String frAcctName, String toAccountNumber, String toAccountName, String toAccountBank, String trAmt,
			String currency, String bobMsgID, String seqUniqueID, String tran_type_code, String master_ref_id,
			String endToEndID, String msgNetMIR, String instg_agt, String instd_agt, String dbtr_agt,
			String dbtr_agt_acc, String cdtr_agt, String cdtr_agt_acc, String instr_id, String svc_lvl,
			String lcl_instrm, String ctgy_purp, String userID,String remarks,String p_id,
			String reqUniqueID,String channel,String resv_field1,String resv_field2,String remitterBankCode) {
		try {

			OutwardTransactionMonitoringTable tranManitorTable = new OutwardTransactionMonitoringTable();
			tranManitorTable.setP_id(p_id);
			tranManitorTable.setReq_unique_id(reqUniqueID);
			tranManitorTable.setInit_channel_id(channel);
			tranManitorTable.setResv_field1(resv_field1);
			tranManitorTable.setResv_field2(resv_field2);
			tranManitorTable.setTran_rmks(remarks);
			
			tranManitorTable.setMsg_type(TranMonitorStatus.BULK_CREDIT.toString());
			tranManitorTable.setTran_audit_number(sysTraceNumber);
			tranManitorTable.setSequence_unique_id(seqUniqueID);
			tranManitorTable.setCim_message_id(bobMsgID);
			tranManitorTable.setCim_account(frAccountNumber);
			tranManitorTable.setIpsx_account(toAccountNumber);
			tranManitorTable.setReceiver_bank(toAccountBank);
			tranManitorTable.setInitiator_bank(remitterBankCode);
			tranManitorTable.setTran_amount(new BigDecimal(trAmt));
			tranManitorTable.setTran_date(new Date());
			tranManitorTable.setEntry_time(new Date());
			tranManitorTable.setCbs_status(TranMonitorStatus.CBS_DEBIT_INITIATED.toString());
			tranManitorTable.setEntry_user(userID);
			tranManitorTable.setTran_currency(currency);
			tranManitorTable.setTran_status(TranMonitorStatus.INITIATED.toString());
			tranManitorTable.setDevice_id(psuDeviceID);
			tranManitorTable.setDevice_ip(psuIpAddress);
			tranManitorTable.setNat_id(psuID);
			tranManitorTable.setMaster_ref_id(master_ref_id);

			tranManitorTable.setCim_account_name(frAcctName);
			tranManitorTable.setIpsx_account_name(toAccountName);
			tranManitorTable.setTran_type_code(tran_type_code);
			tranManitorTable.setEnd_end_id(endToEndID);
			tranManitorTable.setNet_mir(msgNetMIR);
			tranManitorTable.setInstg_agt(instg_agt);
			tranManitorTable.setInstd_agt(instd_agt);

			tranManitorTable.setDbtr_agt(dbtr_agt);
			tranManitorTable.setDbtr_agt_acc(dbtr_agt_acc);
			tranManitorTable.setCdtr_agt(cdtr_agt);
			tranManitorTable.setCdtr_agt_acc(cdtr_agt_acc);

			tranManitorTable.setInstr_id(instr_id);
			tranManitorTable.setSvc_lvl(svc_lvl);
			tranManitorTable.setLcl_instrm(lcl_instrm);
			tranManitorTable.setCtgy_purp(ctgy_purp);
			tranManitorTable.setChrg_br("SLEV");
			
			tranManitorTable.setEntry_user(userID);

			//// Check CutOff time after BOB settlement time
			//// if yes the value date is +1
			if (isTimeAfterCutOff()) {
				Date dt = new Date();
				Calendar c = Calendar.getInstance();
				c.setTime(dt);
				c.add(Calendar.DATE, 1);
				dt = c.getTime();
				tranManitorTable.setValue_date(dt);
			} else {
				tranManitorTable.setValue_date(new Date());
			}

			outwardTranRep.save(tranManitorTable);

		
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}


	}

	public void RegisterManualTranRecord(String psuDeviceID, String psuIpAddress, String sysTraceNumber,
			ManualFndTransferRequest manualFndTransferRequest, String bobMsgID, String seqUniqueID, String endTOEndID,
			String master_ref_id, String msgNetMIR, String instg_agt, String instd_agt, String dbtr_agt,
			String dbtr_agt_acc, String cdtr_agt, String cdtr_agt_acc, String instr_id, String svc_lvl,
			String lcl_instrm, String ctgy_purp, String tran_type_code, String userID) {
		/*try {

			TransactionMonitor tranManitorTable = new TransactionMonitor();
			tranManitorTable.setMsg_type(TranMonitorStatus.OUTGOING.toString());
			tranManitorTable.setTran_audit_number(sysTraceNumber);
			tranManitorTable.setSequence_unique_id(seqUniqueID);
			tranManitorTable.setBob_message_id(bobMsgID);
			tranManitorTable.setBob_account(manualFndTransferRequest.getRemitterAcctNumber());
			tranManitorTable.setIpsx_account(manualFndTransferRequest.getBeneficiaryAcctNumber());
			tranManitorTable.setReceiver_bank(manualFndTransferRequest.getBeneficiaryBankCode());
			tranManitorTable.setTran_amount(new BigDecimal(manualFndTransferRequest.getTrAmt()));
			tranManitorTable.setTran_date(new Date());
			tranManitorTable.setEntry_time(new Date());
			tranManitorTable.setCbs_status(TranMonitorStatus.CBS_DEBIT_INITIATED.toString());
			tranManitorTable.setEntry_user(userID);
			tranManitorTable.setTran_currency(manualFndTransferRequest.getCurrencyCode());
			tranManitorTable.setTran_status(TranMonitorStatus.INITIATED.toString());
			tranManitorTable.setDevice_id(psuDeviceID);
			tranManitorTable.setDevice_ip(psuIpAddress);
			tranManitorTable.setNat_id("");
			tranManitorTable.setMaster_ref_id(master_ref_id);

			tranManitorTable.setEnd_end_id(endTOEndID);
			tranManitorTable.setBob_account_name(manualFndTransferRequest.getRemitterName());
			tranManitorTable.setIpsx_account_name(manualFndTransferRequest.getBeneficiaryName());

			tranManitorTable.setTran_type_code(tran_type_code);
			tranManitorTable.setNet_mir(msgNetMIR);
			tranManitorTable.setInstg_agt(instg_agt);
			tranManitorTable.setInstd_agt(instd_agt);

			tranManitorTable.setDbtr_agt(dbtr_agt);
			tranManitorTable.setDbtr_agt_acc(dbtr_agt_acc);
			tranManitorTable.setCdtr_agt(cdtr_agt);
			tranManitorTable.setCdtr_agt_acc(cdtr_agt_acc);

			tranManitorTable.setInstr_id(instr_id);
			tranManitorTable.setSvc_lvl(svc_lvl);
			tranManitorTable.setLcl_instrm(lcl_instrm);
			tranManitorTable.setCtgy_purp(ctgy_purp);

			//// Check CutOff time after BOB settlement time
			//// if yes the value date is +1
			if (isTimeAfterCutOff()) {
				Date dt = new Date();
				Calendar c = Calendar.getInstance();
				c.setTime(dt);
				c.add(Calendar.DATE, 1);
				dt = c.getTime();
				tranManitorTable.setValue_date(dt);
			} else {
				tranManitorTable.setValue_date(new Date());
			}

			tranRep.save(tranManitorTable);

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}*/
		
		try {

			TransactionMonitor tranManitorTable = new TransactionMonitor();
			tranManitorTable.setMsg_type(TranMonitorStatus.OUTGOING.toString());
			tranManitorTable.setTran_audit_number(sysTraceNumber);
			tranManitorTable.setSequence_unique_id(seqUniqueID);
			tranManitorTable.setBob_message_id(bobMsgID);
			tranManitorTable.setBob_account(manualFndTransferRequest.getRemitterAcctNumber());
			tranManitorTable.setIpsx_account(manualFndTransferRequest.getBeneficiaryAcctNumber());
			tranManitorTable.setReceiver_bank(manualFndTransferRequest.getBeneficiaryBankCode());
			tranManitorTable.setTran_amount(new BigDecimal(manualFndTransferRequest.getTrAmt()));
			tranManitorTable.setTran_date(new Date());
			tranManitorTable.setEntry_time(new Date());
			tranManitorTable.setCbs_status(TranMonitorStatus.CBS_DEBIT_INITIATED.toString());
			tranManitorTable.setEntry_user(userID);
			tranManitorTable.setTran_currency(manualFndTransferRequest.getCurrencyCode());
			tranManitorTable.setTran_status(TranMonitorStatus.INITIATED.toString());
			tranManitorTable.setDevice_id(psuDeviceID);
			tranManitorTable.setDevice_ip(psuIpAddress);
			tranManitorTable.setNat_id("");
			tranManitorTable.setMaster_ref_id(master_ref_id);

			tranManitorTable.setEnd_end_id(endTOEndID);
			tranManitorTable.setBob_account_name(manualFndTransferRequest.getRemitterName());
			tranManitorTable.setIpsx_account_name(manualFndTransferRequest.getBeneficiaryName());

			tranManitorTable.setTran_type_code(tran_type_code);
			tranManitorTable.setNet_mir(msgNetMIR);
			tranManitorTable.setInstg_agt(instg_agt);
			tranManitorTable.setInstd_agt(instd_agt);

			tranManitorTable.setDbtr_agt(dbtr_agt);
			tranManitorTable.setDbtr_agt_acc(dbtr_agt_acc);
			tranManitorTable.setCdtr_agt(cdtr_agt);
			tranManitorTable.setCdtr_agt_acc(cdtr_agt_acc);

			tranManitorTable.setInstr_id(instr_id);
			tranManitorTable.setSvc_lvl(svc_lvl);
			tranManitorTable.setLcl_instrm(lcl_instrm);
			tranManitorTable.setCtgy_purp(ctgy_purp);

			
			if(manualFndTransferRequest.getBeneficiaryAcctNumber().equals("90345778659989")) {
				//tranManitorTable.setIpsx_message_id("M2248279/002");
				tranManitorTable.setCbs_status(TranMonitorStatus.CBS_DEBIT_OK.toString());
				tranManitorTable.setCbs_response_time(new Date());
				tranManitorTable.setIpsx_status(TranMonitorStatus.IPSX_RESPONSE_RJCT.toString());
				tranManitorTable.setTran_status(TranMonitorStatus.FAILURE.toString());
				tranManitorTable.setIpsx_status_code("303");
				tranManitorTable.setIpsx_status_error("Blocked Account Number");
				tranManitorTable.setResponse_status(TranMonitorStatus.RJCT.toString());
			}else if(manualFndTransferRequest.getBeneficiaryAcctNumber().equals("5778659989")) {
				tranManitorTable.setCbs_status(TranMonitorStatus.CBS_DEBIT_OK.toString());
				tranManitorTable.setCbs_response_time(new Date());
				tranManitorTable.setIpsx_status(TranMonitorStatus.IPSX_RESPONSE_RJCT.toString());
				tranManitorTable.setTran_status(TranMonitorStatus.FAILURE.toString());
				tranManitorTable.setIpsx_status_code("306");
				tranManitorTable.setIpsx_status_error("Incorrect Account Number");
				tranManitorTable.setResponse_status(TranMonitorStatus.RJCT.toString());
			}else if(manualFndTransferRequest.getBeneficiaryAcctNumber().equals("5778659990")) {
				tranManitorTable.setCbs_status(TranMonitorStatus.CBS_DEBIT_OK.toString());
				tranManitorTable.setCbs_response_time(new Date());
				tranManitorTable.setIpsx_status(TranMonitorStatus.IPSX_RESPONSE_RJCT.toString());
				tranManitorTable.setTran_status(TranMonitorStatus.FAILURE.toString());
				tranManitorTable.setIpsx_status_code("307");
				tranManitorTable.setIpsx_status_error("Transaction Forbidden");
				tranManitorTable.setResponse_status(TranMonitorStatus.RJCT.toString());
			}else {
		///////
					tranManitorTable.setIpsx_message_id(bobMsgID+"/002");
					tranManitorTable.setCbs_status(TranMonitorStatus.CBS_DEBIT_OK.toString());
					tranManitorTable.setCbs_response_time(new Date());
					tranManitorTable.setIpsx_status(TranMonitorStatus.IPSX_RESPONSE_ACSP.toString());
					tranManitorTable.setTran_status(TranMonitorStatus.SUCCESS.toString());
					tranManitorTable.setResponse_status(TranMonitorStatus.ACSP.toString());
					
					//////
			}
			
			
			//////
			//// Check CutOff time after BOB settlement time
			//// if yes the value date is +1
			if (isTimeAfterCutOff()) {
				Date dt = new Date();
				Calendar c = Calendar.getInstance();
				c.setTime(dt);
				c.add(Calendar.DATE, 1);
				dt = c.getTime();
				tranManitorTable.setValue_date(dt);
			} else {
				tranManitorTable.setValue_date(new Date());
			}

			tranRep.save(tranManitorTable);

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

	}

	public void RegisterBulkCreditRecord2(String psuDeviceID, String psuIpAddress, String psuID,
			String senderParticipantBIC, String participantSOL, String sysTraceNumber, String BobAccountName,
			String BobAccountNumber, String ipsxAccountName, String ipsxAccountNumber, String trAmt, String bankCode,
			String bobMsgID, String seqUniqueID, String master_ref_id, String endToEndID) {
		try {
			TransactionMonitor tranManitorTable = new TransactionMonitor();
			tranManitorTable.setMsg_type(TranMonitorStatus.BULK_CREDIT.toString());
			tranManitorTable.setTran_audit_number(sysTraceNumber);
			tranManitorTable.setSequence_unique_id(seqUniqueID);
			tranManitorTable.setBob_message_id(bobMsgID);
			tranManitorTable.setBob_account(BobAccountNumber);
			tranManitorTable.setBob_account_name(BobAccountName);
			tranManitorTable.setIpsx_account(ipsxAccountNumber);
			tranManitorTable.setIpsx_account_name(ipsxAccountName);

			tranManitorTable.setReceiver_bank(bankCode);
			tranManitorTable.setTran_amount(new BigDecimal(trAmt));
			tranManitorTable.setTran_date(new Date());
			tranManitorTable.setEntry_time(new Date());
			tranManitorTable.setCbs_status(TranMonitorStatus.CBS_DEBIT_INITIATED.toString());
			tranManitorTable.setEntry_user("SYSTEM");
			tranManitorTable.setTran_currency(trAmt);
			tranManitorTable.setTran_status(TranMonitorStatus.INITIATED.toString());
			tranManitorTable.setDevice_id(psuDeviceID);
			tranManitorTable.setDevice_ip(psuIpAddress);
			tranManitorTable.setNat_id(psuID);
			tranManitorTable.setMaster_ref_id(master_ref_id);
			tranManitorTable.setEnd_end_id(endToEndID);
			tranRep.save(tranManitorTable);

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

	}
	
	public String RegisterOutgoingMasterRecord(String psuDeviceID, String psuIpAddress, String sysTraceNumber,
			String bobMsgID, String seqUniqueID, String endTOEndID,
			String master_ref_id, String msgNetMIR, String instg_agt, String instd_agt, String dbtr_agt,
			String dbtr_agt_acc, String cdtr_agt, String cdtr_agt_acc, String instr_id, String svc_lvl,
			String lcl_instrm, String ctgy_purp, String tran_type_code,String remitterAcctName,String remitterAcctNumber,
			String bank_code,String remitterbank_code,String currencyCode,String benAcctName,String benAcctNumber,String reqUniqueId,String trAmt,
			String trRmks,String p_id,String req_unique_id,String channelID,String resvfield1,String resvfield2) {
		
		String status="0";
		try {

			OutwardTransactionMonitoringTable tranManitorTable = new OutwardTransactionMonitoringTable();
			tranManitorTable.setP_id(p_id);
			tranManitorTable.setReq_unique_id(req_unique_id);
			tranManitorTable.setInit_channel_id(channelID);
			tranManitorTable.setResv_field1(resvfield1);
			tranManitorTable.setResv_field2(resvfield2);
			tranManitorTable.setTran_rmks(trRmks);
			tranManitorTable.setMsg_type(TranMonitorStatus.OUTGOING.toString());
			tranManitorTable.setTran_audit_number(sysTraceNumber);
			tranManitorTable.setSequence_unique_id(seqUniqueID);
			tranManitorTable.setCim_message_id(bobMsgID);
			tranManitorTable.setCim_account(remitterAcctNumber);
			tranManitorTable.setIpsx_account(benAcctNumber);
			tranManitorTable.setReceiver_bank(bank_code);
			tranManitorTable.setInitiator_bank(remitterbank_code);
			tranManitorTable.setTran_amount(new BigDecimal(trAmt));
			tranManitorTable.setTran_date(new Date());
			tranManitorTable.setEntry_time(new Date());
			tranManitorTable.setCbs_status(TranMonitorStatus.CBS_DEBIT_INITIATED.toString());
			tranManitorTable.setTran_currency(currencyCode);
			tranManitorTable.setTran_status(TranMonitorStatus.INITIATED.toString());
			tranManitorTable.setDevice_id(psuDeviceID);
			tranManitorTable.setDevice_ip(psuIpAddress);
			tranManitorTable.setNat_id("");
			tranManitorTable.setMaster_ref_id(master_ref_id);

			tranManitorTable.setEnd_end_id(endTOEndID);
			tranManitorTable.setCim_account_name(remitterAcctName);
			tranManitorTable.setIpsx_account_name(benAcctName);

			tranManitorTable.setTran_type_code(tran_type_code);
			tranManitorTable.setNet_mir(msgNetMIR);
			tranManitorTable.setInstg_agt(instg_agt);
			tranManitorTable.setInstd_agt(instd_agt);

			tranManitorTable.setDbtr_agt(dbtr_agt);
			tranManitorTable.setDbtr_agt_acc(dbtr_agt_acc);
			tranManitorTable.setCdtr_agt(cdtr_agt);
			tranManitorTable.setCdtr_agt_acc(cdtr_agt_acc);

			tranManitorTable.setInstr_id(instr_id);
			tranManitorTable.setSvc_lvl(svc_lvl);
			tranManitorTable.setLcl_instrm(lcl_instrm);
			tranManitorTable.setCtgy_purp(ctgy_purp);
			tranManitorTable.setChrg_br("SLEV");
			
			tranManitorTable.setEntry_user("SYSTEM");


			//// Check CutOff time after BOB settlement time
			//// if yes the value date is +1
			if (isTimeAfterCutOff()) {
				Date dt = new Date();
				Calendar c = Calendar.getInstance();
				c.setTime(dt);
				c.add(Calendar.DATE, 1);
				dt = c.getTime();
				tranManitorTable.setValue_date(dt);
			} else {
				tranManitorTable.setValue_date(new Date());
			}

			outwardTranRep.save(tranManitorTable);
			
			status="1";

		} catch (Exception e) {
			System.err.println(e.getMessage());
			status="0";
		}
		
		
		return status;
	}
	
	
	public String RegisterManualMasterRecord(String psuDeviceID, String psuIpAddress, String sysTraceNumber,
			String bobMsgID, String seqUniqueID, String endTOEndID,
			String master_ref_id, String msgNetMIR, String instg_agt, String instd_agt, String dbtr_agt,
			String dbtr_agt_acc, String cdtr_agt, String cdtr_agt_acc, String instr_id, String svc_lvl,
			String lcl_instrm, String ctgy_purp, String tran_type_code,String remitterAcctName,String remitterAcctNumber,
			String bank_code,String remitterbank_code,String currencyCode,String benAcctName,String benAcctNumber,String reqUniqueId,String trAmt,
			String trRmks,String p_id,String req_unique_id,String channelID,String resvfield1,String resvfield2,String userID) {
		
		String status="0";
		try {

			OutwardTransactionMonitoringTable tranManitorTable = new OutwardTransactionMonitoringTable();
			tranManitorTable.setP_id(p_id);
			tranManitorTable.setReq_unique_id(req_unique_id);
			tranManitorTable.setInit_channel_id(channelID);
			tranManitorTable.setResv_field1(resvfield1);
			tranManitorTable.setResv_field2(resvfield2);
			tranManitorTable.setTran_rmks(trRmks);
			tranManitorTable.setMsg_type(TranMonitorStatus.OUTGOING.toString());
			tranManitorTable.setTran_audit_number(sysTraceNumber);
			tranManitorTable.setSequence_unique_id(seqUniqueID);
			tranManitorTable.setCim_message_id(bobMsgID);
			tranManitorTable.setCim_account(remitterAcctNumber);
			tranManitorTable.setIpsx_account(benAcctNumber);
			tranManitorTable.setReceiver_bank(bank_code);
			tranManitorTable.setInitiator_bank(remitterbank_code);
			tranManitorTable.setTran_amount(new BigDecimal(trAmt));
			tranManitorTable.setTran_date(new Date());
			tranManitorTable.setEntry_time(new Date());
			tranManitorTable.setCbs_status(TranMonitorStatus.CBS_DEBIT_INITIATED.toString());
			tranManitorTable.setTran_currency(currencyCode);
			tranManitorTable.setTran_status(TranMonitorStatus.INITIATED.toString());
			tranManitorTable.setDevice_id(psuDeviceID);
			tranManitorTable.setDevice_ip(psuIpAddress);
			tranManitorTable.setNat_id("");
			tranManitorTable.setMaster_ref_id(master_ref_id);

			tranManitorTable.setEnd_end_id(endTOEndID);
			tranManitorTable.setCim_account_name(remitterAcctName);
			tranManitorTable.setIpsx_account_name(benAcctName);

			tranManitorTable.setTran_type_code(tran_type_code);
			tranManitorTable.setNet_mir(msgNetMIR);
			tranManitorTable.setInstg_agt(instg_agt);
			tranManitorTable.setInstd_agt(instd_agt);

			tranManitorTable.setDbtr_agt(dbtr_agt);
			tranManitorTable.setDbtr_agt_acc(dbtr_agt_acc);
			tranManitorTable.setCdtr_agt(cdtr_agt);
			tranManitorTable.setCdtr_agt_acc(cdtr_agt_acc);

			tranManitorTable.setInstr_id(instr_id);
			tranManitorTable.setSvc_lvl(svc_lvl);
			tranManitorTable.setLcl_instrm(lcl_instrm);
			tranManitorTable.setCtgy_purp(ctgy_purp);
			tranManitorTable.setChrg_br("SLEV");
			
			tranManitorTable.setEntry_user(userID);

			//// Check CutOff time after BOB settlement time
			//// if yes the value date is +1
			if (isTimeAfterCutOff()) {
				Date dt = new Date();
				Calendar c = Calendar.getInstance();
				c.setTime(dt);
				c.add(Calendar.DATE, 1);
				dt = c.getTime();
				tranManitorTable.setValue_date(dt);
			} else {
				tranManitorTable.setValue_date(new Date());
			}

			outwardTranRep.save(tranManitorTable);
			
			status="1";

		} catch (Exception e) {
			System.err.println(e.getMessage());
			status="0";
		}
		
		
		return status;
	}
	

	
	public String RegisterMerchantOutgoingMasterRecord(String psuDeviceID, String psuIpAddress, String sysTraceNumber,
			String bobMsgID, String seqUniqueID, String endTOEndID,
			String master_ref_id, String msgNetMIR, String instg_agt, String instd_agt, String dbtr_agt,
			String dbtr_agt_acc, String cdtr_agt, String cdtr_agt_acc, String instr_id, String svc_lvl,
			String lcl_instrm, String ctgy_purp, String tran_type_code,String remitterAcctName,String remitterAcctNumber,
			String bank_code,String remitterbank_code,String currencyCode,String benAcctName,String benAcctNumber,String reqUniqueId,String trAmt,
			String trRmks,String p_id,String req_unique_id,String channelID,String resvfield1,String resvfield2,
			String chrBearer,String RmtInfo,CIMMerchantDirectFndRequest cimMerchantRequest) {
		
		String status="0";
		try {

			OutwardTransactionMonitoringTable tranManitorTable = new OutwardTransactionMonitoringTable();
			tranManitorTable.setP_id(p_id);
			tranManitorTable.setReq_unique_id(req_unique_id);
			tranManitorTable.setInit_channel_id(channelID);
			tranManitorTable.setResv_field1(resvfield1);
			tranManitorTable.setResv_field2(resvfield2);
			tranManitorTable.setTran_rmks(trRmks);
			tranManitorTable.setMsg_type(TranMonitorStatus.OUTGOING.toString());
			tranManitorTable.setTran_audit_number(sysTraceNumber);
			tranManitorTable.setSequence_unique_id(seqUniqueID);
			tranManitorTable.setCim_message_id(bobMsgID);
			tranManitorTable.setCim_account(benAcctNumber);
			tranManitorTable.setIpsx_account(remitterAcctNumber);
			tranManitorTable.setReceiver_bank(bank_code);
			tranManitorTable.setInitiator_bank(remitterbank_code);
			tranManitorTable.setTran_amount(new BigDecimal(trAmt));
			tranManitorTable.setTran_date(new Date());
			tranManitorTable.setEntry_time(new Date());
			tranManitorTable.setCbs_status(TranMonitorStatus.CBS_DEBIT_INITIATED.toString());
			tranManitorTable.setTran_currency(currencyCode);
			tranManitorTable.setTran_status(TranMonitorStatus.INITIATED.toString());
			tranManitorTable.setDevice_id(psuDeviceID);
			tranManitorTable.setDevice_ip(psuIpAddress);
			tranManitorTable.setNat_id("");
			tranManitorTable.setMaster_ref_id(master_ref_id);

			tranManitorTable.setEnd_end_id(endTOEndID);
			tranManitorTable.setCim_account_name(benAcctName);
			tranManitorTable.setIpsx_account_name(remitterAcctName);

			tranManitorTable.setTran_type_code(tran_type_code);
			tranManitorTable.setNet_mir(msgNetMIR);
			tranManitorTable.setInstg_agt(instg_agt);
			tranManitorTable.setInstd_agt(instd_agt);

			tranManitorTable.setDbtr_agt(dbtr_agt);
			tranManitorTable.setDbtr_agt_acc(dbtr_agt_acc);
			tranManitorTable.setCdtr_agt(cdtr_agt);
			tranManitorTable.setCdtr_agt_acc(cdtr_agt_acc);

			tranManitorTable.setInstr_id(instr_id);
			tranManitorTable.setSvc_lvl(svc_lvl);
			tranManitorTable.setLcl_instrm(lcl_instrm);
			tranManitorTable.setCtgy_purp(ctgy_purp);
			tranManitorTable.setChrg_br(chrBearer);
			
			tranManitorTable.setRmt_info(RmtInfo);
			
			tranManitorTable.setGlobal_id(cimMerchantRequest.getMerchantAccount().getGlobalID());
			tranManitorTable.setPoint_init(cimMerchantRequest.getMerchantAccount().getPointOfInitiationFormat());
			tranManitorTable.setMerchant_id(cimMerchantRequest.getMerchantAccount().getMerchantID());
			tranManitorTable.setMcc(cimMerchantRequest.getMerchantAccount().getMCC());
			/*if(!String.valueOf(cimMerchantRequest.getMerchantAccount().getTipOrConvenienceIndicator()).equals("null")&&
					!String.valueOf(cimMerchantRequest.getMerchantAccount().getTipOrConvenienceIndicator()).equals("")) {
				tranManitorTable.setConv_flg("Y");
				//tranManitorTable.setConv_fee_type(cimMerchantRequest.getMerchantAccount().getConvenienceIndicatorFeeType());
				tranManitorTable.setConv_fee(cimMerchantRequest.getMerchantAccount().getConvenienceIndicatorFee());
			}*/
			
			if(!String.valueOf(cimMerchantRequest.getMerchantAccount().getTipOrConvenienceIndicator()).equals("null")&&
					!String.valueOf(cimMerchantRequest.getMerchantAccount().getTipOrConvenienceIndicator()).equals("")) {
				
				if(String.valueOf(cimMerchantRequest.getMerchantAccount().getTipOrConvenienceIndicator()).equals("01")) {
					tranManitorTable.setTip_or_conv_indicator("01");
					tranManitorTable.setTip_amount(cimMerchantRequest.getMerchantAccount().getTipAmt());
				}else if(String.valueOf(cimMerchantRequest.getMerchantAccount().getTipOrConvenienceIndicator()).equals("02")) {
					tranManitorTable.setTip_or_conv_indicator("02");
					tranManitorTable.setConv_amount(cimMerchantRequest.getMerchantAccount().getConvenienceIndicatorFee());
				}else if(String.valueOf(cimMerchantRequest.getMerchantAccount().getTipOrConvenienceIndicator()).equals("03")) {
					tranManitorTable.setTip_or_conv_indicator("03");
					tranManitorTable.setConv_amount(cimMerchantRequest.getMerchantAccount().getConvenienceIndicatorFee());
				}
				
			}
			
			tranManitorTable.setMerchant_city(cimMerchantRequest.getMerchantAccount().getCity());
			tranManitorTable.setMerchant_cntry(cimMerchantRequest.getMerchantAccount().getCountryCode());
			
			
			if(!String.valueOf(cimMerchantRequest.getMerchantAccount().getPostalCode()).equals("null")&&
					!String.valueOf(cimMerchantRequest.getMerchantAccount().getPostalCode()).equals("")	) {
				tranManitorTable.setMerchant_postal_cd(cimMerchantRequest.getMerchantAccount().getPostalCode());
			}

			if(cimMerchantRequest.getAdditionalDataInformation()!=null) {
					
					if(!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getBillNumber()).equals("null")&&
							!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getBillNumber()).equals("")	) {
						tranManitorTable.setMerchant_bill_number(cimMerchantRequest.getAdditionalDataInformation().getBillNumber());
					}
					if(!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getMobileNumber()).equals("null")&&
							!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getMobileNumber()).equals("")	) {
						tranManitorTable.setMerchant_mobile(cimMerchantRequest.getAdditionalDataInformation().getMobileNumber());
					}
					if(!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getStoreLabel()).equals("null")&&
							!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getStoreLabel()).equals("")
							) {
						tranManitorTable.setMerchant_store_label(cimMerchantRequest.getAdditionalDataInformation().getStoreLabel());
					}
					if(!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getLoyaltyNumber()).equals("null")&&
							!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getLoyaltyNumber()).equals("")
							) {
						tranManitorTable.setMerchant_loyalty_number(cimMerchantRequest.getAdditionalDataInformation().getLoyaltyNumber());
					}
					if(!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getReferenceLabel()).equals("null")&&
							!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getReferenceLabel()).equals("")
							) {
						tranManitorTable.setMerchant_ref_label(cimMerchantRequest.getAdditionalDataInformation().getReferenceLabel());
					}
					if(!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getCustomerLabel()).equals("null")&&
							!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getCustomerLabel()).equals("")
							) {
						tranManitorTable.setMerchant_customer_label(cimMerchantRequest.getAdditionalDataInformation().getCustomerLabel());
					}
					if(!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getTerminalLabel()).equals("null")&&
							!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getTerminalLabel()).equals("")
							) {
						tranManitorTable.setMerchant_terminal_label(cimMerchantRequest.getAdditionalDataInformation().getTerminalLabel());
					}
					if(!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getPurposeOfTransaction()).equals("null")&&
							!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getPurposeOfTransaction()).equals("null")) {
						tranManitorTable.setMerchant_purp_tran(cimMerchantRequest.getAdditionalDataInformation().getPurposeOfTransaction());
					}
					if(!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getAddlDataRequest()).equals("null")&&
							!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getAddlDataRequest()).equals("null")) {
						tranManitorTable.setMerchant_addl_data_request(cimMerchantRequest.getAdditionalDataInformation().getAddlDataRequest());
					}
			}
			
			
			//// Check CutOff time after BOB settlement time
			//// if yes the value date is +1
			if (isTimeAfterCutOff()) {
				Date dt = new Date();
				Calendar c = Calendar.getInstance();
				c.setTime(dt);
				c.add(Calendar.DATE, 1);
				dt = c.getTime();
				tranManitorTable.setValue_date(dt);
			} else {
				tranManitorTable.setValue_date(new Date());
			}

			outwardTranRep.save(tranManitorTable);
			
			status="1";

		} catch (Exception e) {
			System.err.println(e.getMessage());
			status="0";
		}
		
		
		return status;
	}

	
	public void RegisterMerchantOutMsgRecord(String psuDeviceID, String psuIpAddress, String psuID,
			String sysTraceNumber, CIMMerchantDirectFndRequest mcCreditTransferRequest,
			String bobMsgID, String seqUniqueID, String endToEndID, String tran_type_code, String msgNetMIR,
			String instg_agt, String instd_agt, String dbtr_agt, String dbtr_agt_acc, String cdtr_agt,
			String cdtr_agt_acc, String instr_id, String svc_lvl, String lcl_instrm, String ctgy_purp,
			String remitterBankCode,String chrBearer,String rmtInfo,String tran_amount) {

		try {
			TransactionMonitor tranManitorTable = new TransactionMonitor();
			tranManitorTable.setMsg_type(TranMonitorStatus.OUTGOING.toString());
			tranManitorTable.setTran_audit_number(sysTraceNumber);
			tranManitorTable.setSequence_unique_id(seqUniqueID);
			tranManitorTable.setBob_message_id(bobMsgID);
			tranManitorTable.setBob_account(mcCreditTransferRequest.getRemitterAccount().getAcctNumber());
			tranManitorTable.setIpsx_account(mcCreditTransferRequest.getMerchantAccount().getMerchantAcctNumber());
			tranManitorTable.setReceiver_bank(mcCreditTransferRequest.getMerchantAccount().getPayeeParticipantCode());
			tranManitorTable.setInitiator_bank(remitterBankCode);
			tranManitorTable.setTran_amount(new BigDecimal(tran_amount));
			tranManitorTable.setTran_date(new Date());
			tranManitorTable.setEntry_time(new Date());
			tranManitorTable.setCbs_status(TranMonitorStatus.CBS_DEBIT_INITIATED.toString());
			tranManitorTable.setEntry_user("SYSTEM");
			tranManitorTable.setTran_currency(mcCreditTransferRequest.getMerchantAccount().getCurrency());
			tranManitorTable.setTran_status(TranMonitorStatus.INITIATED.toString());
			tranManitorTable.setDevice_id(psuDeviceID);
			tranManitorTable.setDevice_ip(psuIpAddress);
			tranManitorTable.setNat_id(psuID);
			tranManitorTable.setEnd_end_id(endToEndID);
			tranManitorTable.setBob_account_name(mcCreditTransferRequest.getRemitterAccount().getAcctName());
			tranManitorTable.setIpsx_account_name(mcCreditTransferRequest.getMerchantAccount().getMerchantName());
			tranManitorTable.setTran_type_code(tran_type_code);
			tranManitorTable.setNet_mir(msgNetMIR);
			tranManitorTable.setInstg_agt(instg_agt);
			tranManitorTable.setInstd_agt(instd_agt);

			tranManitorTable.setDbtr_agt(dbtr_agt);
			tranManitorTable.setDbtr_agt_acc(dbtr_agt_acc);
			tranManitorTable.setCdtr_agt(cdtr_agt);
			tranManitorTable.setCdtr_agt_acc(cdtr_agt_acc);

			tranManitorTable.setInstr_id(instr_id);
			tranManitorTable.setSvc_lvl(svc_lvl);
			tranManitorTable.setLcl_instrm(lcl_instrm);
			tranManitorTable.setCtgy_purp(ctgy_purp);
			tranManitorTable.setChrg_br(chrBearer);
			tranManitorTable.setRmt_info(rmtInfo);

			//// Check CutOff time after BOB settlement time
			//// if yes the value date is +1
			if (isTimeAfterCutOff()) {
				Date dt = new Date();
				Calendar c = Calendar.getInstance();
				c.setTime(dt);
				c.add(Calendar.DATE, 1);
				dt = c.getTime();
				tranManitorTable.setValue_date(dt);
			} else {
				tranManitorTable.setValue_date(new Date());
			}

			tranRep.save(tranManitorTable);

			//// update IPS Flow Table

			// insertIPSFlow(seqUniqueID, "OUTGOING", "Initial");

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		
	}

	
	public String RegisterBulkRTPRecord(String psuDeviceID, String psuIpAddress, String sysTraceNumber,
			String bobMsgID, String seqUniqueID, String endTOEndID,
			String master_ref_id, String msgNetMIR, String instg_agt, String instd_agt, String dbtr_agt,
			String dbtr_agt_acc, String cdtr_agt, String cdtr_agt_acc, String instr_id, String svc_lvl,
			String lcl_instrm, String ctgy_purp, String tran_type_code,String remitterAcctName,String remitterAcctNumber,
			String bank_code,String currencyCode,String benAcctName,String benAcctNumber,String reqUniqueId,String trAmt,
			String trRmks,String p_id,String req_unique_id,String channelID,String resvfield1,String resvfield2,String remitterBankCode,
			String chargeBearer) {
		
		String status="0";
		try {

			Date curDate=new Date();
			Date valDate=new Date();
		           //// Check CutOff time after BOB settlement time
					//// if yes the value date is +1
			if (isTimeAfterCutOff()) {
				Date dt = new Date();
				Calendar c = Calendar.getInstance();
				c.setTime(dt);
				c.add(Calendar.DATE, 1);
				dt = c.getTime();
				valDate=dt;
				
			}

			//OutwardTransactionMonitoringTable tranManitorTable = new OutwardTransactionMonitoringTable();
			OutwardTransactionMonitoringTable tranManitorTable = new OutwardTransactionMonitoringTable(
					p_id,req_unique_id,channelID,resvfield1,resvfield2,TranMonitorStatus.OUTWARD_BULK_RTP.toString(),
					sysTraceNumber,seqUniqueID,bobMsgID,benAcctNumber,remitterAcctNumber,bank_code,
					remitterBankCode,new BigDecimal(trAmt),curDate,curDate,currencyCode,
					TranMonitorStatus.INITIATED.toString(),psuDeviceID,psuIpAddress,"",master_ref_id,
					endTOEndID,benAcctName,remitterAcctName,tran_type_code,msgNetMIR,instg_agt,instd_agt,
					dbtr_agt,dbtr_agt_acc,cdtr_agt,cdtr_agt_acc,instr_id,svc_lvl,lcl_instrm,ctgy_purp,
					chargeBearer,trRmks,valDate);
			
			/*tranManitorTable.setP_id(p_id);
			tranManitorTable.setReq_unique_id(req_unique_id);
			tranManitorTable.setInit_channel_id(channelID);
			tranManitorTable.setResv_field1(resvfield1);
			tranManitorTable.setResv_field2(resvfield2);
			tranManitorTable.setMsg_type(TranMonitorStatus.OUTWARD_BULK_RTP.toString());
			tranManitorTable.setTran_audit_number(sysTraceNumber);
			tranManitorTable.setSequence_unique_id(seqUniqueID);
			tranManitorTable.setCim_message_id(bobMsgID);
			tranManitorTable.setCim_account(benAcctNumber);
			tranManitorTable.setIpsx_account(remitterAcctNumber);
			tranManitorTable.setReceiver_bank(bank_code);
			tranManitorTable.setInitiator_bank(remitterBankCode);
			tranManitorTable.setTran_amount(new BigDecimal(trAmt));
			tranManitorTable.setTran_date(new Date());
			tranManitorTable.setEntry_time(new Date());
			//tranManitorTable.setCbs_status(TranMonitorStatus.RTP_Initiated.toString());
			tranManitorTable.setTran_currency(currencyCode);
			tranManitorTable.setTran_status(TranMonitorStatus.INITIATED.toString());
			tranManitorTable.setDevice_id(psuDeviceID);
			tranManitorTable.setDevice_ip(psuIpAddress);
			tranManitorTable.setNat_id("");
			tranManitorTable.setMaster_ref_id(master_ref_id);

			tranManitorTable.setEnd_end_id(endTOEndID);
			tranManitorTable.setCim_account_name(benAcctName);
			tranManitorTable.setIpsx_account_name(remitterAcctName);

			tranManitorTable.setTran_type_code(tran_type_code);
			tranManitorTable.setNet_mir(msgNetMIR);
			tranManitorTable.setInstg_agt(instg_agt);
			tranManitorTable.setInstd_agt(instd_agt);

			tranManitorTable.setDbtr_agt(dbtr_agt);
			tranManitorTable.setDbtr_agt_acc(dbtr_agt_acc);
			tranManitorTable.setCdtr_agt(cdtr_agt);
			tranManitorTable.setCdtr_agt_acc(cdtr_agt_acc);

			tranManitorTable.setInstr_id(instr_id);
			tranManitorTable.setSvc_lvl(svc_lvl);
			tranManitorTable.setLcl_instrm(lcl_instrm);
			tranManitorTable.setCtgy_purp(ctgy_purp);
			tranManitorTable.setChrg_br(chargeBearer);
			
			tranManitorTable.setTran_rmks(trRmks);

			//// Check CutOff time after BOB settlement time
			//// if yes the value date is +1
			if (isTimeAfterCutOff()) {
				Date dt = new Date();
				Calendar c = Calendar.getInstance();
				c.setTime(dt);
				c.add(Calendar.DATE, 1);
				dt = c.getTime();
				tranManitorTable.setValue_date(dt);
			} else {
				tranManitorTable.setValue_date(new Date());
			}*/

			outwardTranRep.saveAndFlush(tranManitorTable);
			
			status="1";

		} catch (Exception e) {
			System.err.println(e.getMessage());
			status="0";
		}
		
		
		return status;
		
	
	}
	
	
	public String RegisterMerchantRTPRecord(String psuDeviceID, String psuIpAddress, String sysTraceNumber,
			String bobMsgID, String seqUniqueID, String endTOEndID,
			String master_ref_id, String msgNetMIR, String instg_agt, String instd_agt, String dbtr_agt,
			String dbtr_agt_acc, String cdtr_agt, String cdtr_agt_acc, String instr_id, String svc_lvl,
			String lcl_instrm, String ctgy_purp, String tran_type_code,String remitterAcctName,String remitterAcctNumber,
			String bank_code,String currencyCode,String benAcctName,String benAcctNumber,String reqUniqueId,String trAmt,
			String trRmks,String p_id,String req_unique_id,String channelID,String resvfield1,String resvfield2,String remitterBankCode,
			String chargeBearer,CIMMerchantDirectFndRequest cimMerchantRequest,String remInfo) {
		
		String status="0";
		try {

			OutwardTransactionMonitoringTable tranManitorTable = new OutwardTransactionMonitoringTable();
			tranManitorTable.setP_id(p_id);
			tranManitorTable.setReq_unique_id(req_unique_id);
			tranManitorTable.setInit_channel_id(channelID);
			tranManitorTable.setResv_field1(resvfield1);
			tranManitorTable.setResv_field2(resvfield2);
			tranManitorTable.setMsg_type(TranMonitorStatus.OUTWARD_BULK_RTP.toString());
			tranManitorTable.setTran_audit_number(sysTraceNumber);
			tranManitorTable.setSequence_unique_id(seqUniqueID);
			tranManitorTable.setCim_message_id(bobMsgID);
			tranManitorTable.setCim_account(benAcctNumber);
			tranManitorTable.setIpsx_account(remitterAcctNumber);
			tranManitorTable.setReceiver_bank(bank_code);
			tranManitorTable.setInitiator_bank(remitterBankCode);
			tranManitorTable.setTran_amount(new BigDecimal(trAmt));
			tranManitorTable.setTran_date(new Date());
			tranManitorTable.setEntry_time(new Date());
			//tranManitorTable.setCbs_status(TranMonitorStatus.RTP_Initiated.toString());
			tranManitorTable.setTran_currency(currencyCode);
			tranManitorTable.setTran_status(TranMonitorStatus.INITIATED.toString());
			tranManitorTable.setDevice_id(psuDeviceID);
			tranManitorTable.setDevice_ip(psuIpAddress);
			tranManitorTable.setNat_id("");
			tranManitorTable.setMaster_ref_id(master_ref_id);

			tranManitorTable.setEnd_end_id(endTOEndID);
			tranManitorTable.setCim_account_name(benAcctName);
			tranManitorTable.setIpsx_account_name(remitterAcctName);

			tranManitorTable.setTran_type_code(tran_type_code);
			tranManitorTable.setNet_mir(msgNetMIR);
			tranManitorTable.setInstg_agt(instg_agt);
			tranManitorTable.setInstd_agt(instd_agt);

			tranManitorTable.setDbtr_agt(dbtr_agt);
			tranManitorTable.setDbtr_agt_acc(dbtr_agt_acc);
			tranManitorTable.setCdtr_agt(cdtr_agt);
			tranManitorTable.setCdtr_agt_acc(cdtr_agt_acc);

			tranManitorTable.setInstr_id(instr_id);
			tranManitorTable.setSvc_lvl(svc_lvl);
			tranManitorTable.setLcl_instrm(lcl_instrm);
			tranManitorTable.setCtgy_purp(ctgy_purp);
			tranManitorTable.setChrg_br(chargeBearer);
			
			tranManitorTable.setRmt_info(remInfo);
			
			tranManitorTable.setGlobal_id(cimMerchantRequest.getMerchantAccount().getGlobalID());
			tranManitorTable.setPoint_init(cimMerchantRequest.getMerchantAccount().getPointOfInitiationFormat());
			tranManitorTable.setMerchant_id(cimMerchantRequest.getMerchantAccount().getMerchantID());
			tranManitorTable.setMcc(cimMerchantRequest.getMerchantAccount().getMCC());
			/*if(cimMerchantRequest.getMerchantAccount().isConvenienceIndicator()) {
				tranManitorTable.setConv_flg("Y");
				tranManitorTable.setConv_fee_type(cimMerchantRequest.getMerchantAccount().getConvenienceIndicatorFeeType());
				tranManitorTable.setConv_fee(cimMerchantRequest.getMerchantAccount().getConvenienceIndicatorFee());
			}*/
			
			if(!String.valueOf(cimMerchantRequest.getMerchantAccount().getTipOrConvenienceIndicator()).equals("null")&&
					!String.valueOf(cimMerchantRequest.getMerchantAccount().getTipOrConvenienceIndicator()).equals("")) {
				
				if(String.valueOf(cimMerchantRequest.getMerchantAccount().getTipOrConvenienceIndicator()).equals("01")) {
					tranManitorTable.setTip_or_conv_indicator("01");
					tranManitorTable.setTip_amount(cimMerchantRequest.getMerchantAccount().getTipAmt());
				}else if(String.valueOf(cimMerchantRequest.getMerchantAccount().getTipOrConvenienceIndicator()).equals("02")) {
					tranManitorTable.setTip_or_conv_indicator("02");
					tranManitorTable.setConv_amount(cimMerchantRequest.getMerchantAccount().getConvenienceIndicatorFee());
				}else if(String.valueOf(cimMerchantRequest.getMerchantAccount().getTipOrConvenienceIndicator()).equals("03")) {
					tranManitorTable.setTip_or_conv_indicator("03");
					tranManitorTable.setConv_amount(cimMerchantRequest.getMerchantAccount().getConvenienceIndicatorFee());
				}
				
			}
			
			tranManitorTable.setMerchant_city(cimMerchantRequest.getMerchantAccount().getCity());
			tranManitorTable.setMerchant_cntry(cimMerchantRequest.getMerchantAccount().getCountryCode());
			
			if(!String.valueOf(cimMerchantRequest.getMerchantAccount().getPostalCode()).equals("null")&&
					!String.valueOf(cimMerchantRequest.getMerchantAccount().getPostalCode()).equals("")	) {
				tranManitorTable.setMerchant_postal_cd(cimMerchantRequest.getMerchantAccount().getPostalCode());
			}


			if(cimMerchantRequest.getAdditionalDataInformation()!=null) {
					
					if(!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getBillNumber()).equals("null")&&
							!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getBillNumber()).equals("")	) {
						tranManitorTable.setMerchant_bill_number(cimMerchantRequest.getAdditionalDataInformation().getBillNumber());
					}
					if(!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getMobileNumber()).equals("null")&&
							!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getMobileNumber()).equals("")	) {
						tranManitorTable.setMerchant_mobile(cimMerchantRequest.getAdditionalDataInformation().getMobileNumber());
					}
					if(!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getStoreLabel()).equals("null")&&
							!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getStoreLabel()).equals("")
							) {
						tranManitorTable.setMerchant_store_label(cimMerchantRequest.getAdditionalDataInformation().getStoreLabel());
					}
					if(!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getLoyaltyNumber()).equals("null")&&
							!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getLoyaltyNumber()).equals("")
							) {
						tranManitorTable.setMerchant_loyalty_number(cimMerchantRequest.getAdditionalDataInformation().getLoyaltyNumber());
					}
					if(!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getReferenceLabel()).equals("null")&&
							!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getReferenceLabel()).equals("")
							) {
						tranManitorTable.setMerchant_ref_label(cimMerchantRequest.getAdditionalDataInformation().getReferenceLabel());
					}
					if(!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getCustomerLabel()).equals("null")&&
							!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getCustomerLabel()).equals("")
							) {
						tranManitorTable.setMerchant_customer_label(cimMerchantRequest.getAdditionalDataInformation().getCustomerLabel());
					}
					if(!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getTerminalLabel()).equals("null")&&
							!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getTerminalLabel()).equals("")
							) {
						tranManitorTable.setMerchant_terminal_label(cimMerchantRequest.getAdditionalDataInformation().getTerminalLabel());
					}
					if(!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getPurposeOfTransaction()).equals("null")&&
							!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getPurposeOfTransaction()).equals("null")) {
						tranManitorTable.setMerchant_purp_tran(cimMerchantRequest.getAdditionalDataInformation().getPurposeOfTransaction());
					}
					if(!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getAddlDataRequest()).equals("null")&&
							!String.valueOf(cimMerchantRequest.getAdditionalDataInformation().getAddlDataRequest()).equals("null")) {
						tranManitorTable.setMerchant_addl_data_request(cimMerchantRequest.getAdditionalDataInformation().getAddlDataRequest());
					}
			}

			//// Check CutOff time after BOB settlement time
			//// if yes the value date is +1
			if (isTimeAfterCutOff()) {
				Date dt = new Date();
				Calendar c = Calendar.getInstance();
				c.setTime(dt);
				c.add(Calendar.DATE, 1);
				dt = c.getTime();
				tranManitorTable.setValue_date(dt);
			} else {
				tranManitorTable.setValue_date(new Date());
			}

			outwardTranRep.save(tranManitorTable);
			
			status="1";

		} catch (Exception e) {
			System.err.println(e.getMessage());
			status="0";
		}
		
		
		return status;
		
	
	}


	public void RegisterBulkCreditRecord1(String psuDeviceID, String psuIpAddress, String psuID,
			String senderParticipantBIC, String participantSOL, String sysTraceNumber,
			MCCreditTransferRequest mcCreditTransferRequest, String bobMsgID, String seqUniqueID) {
		try {

			for (int i = 0; i <= mcCreditTransferRequest.getToAccountList().size(); i++) {
				int srlNo = i + 1;
				TransactionMonitor tranManitorTable = new TransactionMonitor();
				tranManitorTable.setMsg_type(TranMonitorStatus.BULK_CREDIT.toString());
				tranManitorTable.setTran_audit_number(sysTraceNumber);
				tranManitorTable.setSequence_unique_id(seqUniqueID + "/" + srlNo);
				tranManitorTable.setBob_message_id(bobMsgID + "/" + srlNo);
				tranManitorTable.setBob_account(mcCreditTransferRequest.getFrAccount().getAcctNumber());
				tranManitorTable.setIpsx_account(mcCreditTransferRequest.getToAccountList().get(i).getAcctNumber());
				tranManitorTable.setReceiver_bank(mcCreditTransferRequest.getToAccountList().get(i).getBankCode());
				tranManitorTable
						.setTran_amount(new BigDecimal(mcCreditTransferRequest.getToAccountList().get(i).getTrAmt()));
				tranManitorTable.setTran_date(new Date());
				tranManitorTable.setEntry_time(new Date());
				tranManitorTable.setCbs_status(TranMonitorStatus.CBS_DEBIT_INITIATED.toString());
				tranManitorTable.setEntry_user("SYSTEM");
				tranManitorTable.setTran_currency(mcCreditTransferRequest.getCurrencyCode());
				tranManitorTable.setTran_status(TranMonitorStatus.INITIATED.toString());
				tranManitorTable.setDevice_id(psuDeviceID);
				tranManitorTable.setDevice_ip(psuIpAddress);
				tranManitorTable.setNat_id(psuID);
				tranManitorTable.setMaster_ref_id(seqUniqueID);
				tranRep.save(tranManitorTable);
			}

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

	}

	public void updateCBSStatusBulkCreditError(String seqUniqueID, String cbsStatus, String error_desc,
			String tranStatus) {
		try {
			List<TransactionMonitor> otm = tranRep.findBulkCreditID(seqUniqueID);

			if (!otm.isEmpty()) {
				for (int i = 0; i < otm.size(); i++) {
					TransactionMonitor tm = otm.get(i);
					tm.setCbs_status(cbsStatus);
					tm.setCbs_status_error(error_desc);
					tm.setCbs_response_time(new Date());
					tm.setTran_status(tranStatus);
					tranRep.save(tm);
				}

			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

	}
	
	public void updateOutwardCBSStatusBulkCreditError(String seqUniqueID, String cbsStatus, String error_desc,
			String tranStatus) {
		try {
			List<OutwardTransactionMonitoringTable> otm = outwardTranRep.findBulkCreditID(seqUniqueID);

			if (!otm.isEmpty()) {
				for (int i = 0; i < otm.size(); i++) {
					OutwardTransactionMonitoringTable tm = otm.get(i);
					tm.setCbs_status(cbsStatus);
					tm.setCbs_status_error(error_desc);
					tm.setCbs_response_time(new Date());
					tm.setTran_status(tranStatus);
					outwardTranRep.save(tm);
				}

			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

	}

	public void updateCBSStatusBulkCredit(String seqUniqueID, String cbsStatus, String tranStatus) {
		try {
			List<TransactionMonitor> otm = tranRep.findBulkCreditID(seqUniqueID);

			if (!otm.isEmpty()) {
				for (int i = 0; i < otm.size(); i++) {
					TransactionMonitor tm = otm.get(i);
					tm.setCbs_status(cbsStatus);
					tm.setCbs_response_time(new Date());
					tm.setTran_status(tranStatus);
					tranRep.save(tm);
				}

			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

	}
	
	public void updateOutwardCBSStatusBulkCredit(String seqUniqueID, String cbsStatus, String tranStatus) {
		try {
			List<OutwardTransactionMonitoringTable> otm = outwardTranRep.findBulkCreditID(seqUniqueID);

			if (!otm.isEmpty()) {
				for (int i = 0; i < otm.size(); i++) {
					OutwardTransactionMonitoringTable tm = otm.get(i);
					tm.setCbs_status(cbsStatus);
					tm.setCbs_response_time(new Date());
					tm.setTran_status(tranStatus);
					outwardTranRep.save(tm);
				}

			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

	}

	public void updateIPSFlow(String seqUniqueID, String msgType, String menu, String action) {
		try {
			System.out.println("okkkk");
			IPSFlowTable ipsFlowTable = new IPSFlowTable();
			ipsFlowTable.setSequence_unique_id(seqUniqueID);
			ipsFlowTable.setMsg_type(msgType);
			ipsFlowTable.setMenu(menu);
			ipsFlowTable.setAction(action);
			ipsFlowTable.setAction_time(new Date());
			iPSFlowTableRep.save(ipsFlowTable);
		} catch (Exception e) {
			logger.info(e.getLocalizedMessage());
		}
	}

	public void updateINOUT(String seqUniqueID, String menu) {
		try {
			Optional<TransactionMonitor> otm = tranRep.findById(seqUniqueID);

			if (otm.isPresent()) {
				TransactionMonitor tm = otm.get();

				if (menu.equals("MC_IN")) {
					tm.setMconnectin("Y");
					tm.setMconnectindate(new Date());
				} else if (menu.equals("MC_OUT")) {
					tm.setMconnectout("Y");
					tm.setMconnectoutdate(new Date());
				} else if (menu.equals("CBS_IN")) {
					tm.setCbsin("Y");
					tm.setCbsindate(new Date());
				} else if (menu.equals("CBS_OUT")) {
					tm.setCbsout("Y");
					tm.setCbsoutdate(new Date());
				} else if (menu.equals("CBS_REVERSE_IN")) {
					tm.setCbsreversein("Y");
					tm.setCbsreverseindate(new Date());
				} else if (menu.equals("CBS_REVERSE_OUT")) {
					tm.setCbsreverseout("Y");
					tm.setCbsreverseoutdate(new Date());
				} else if (menu.equals("IPS_IN")) {
					tm.setIpsin("Y");
					tm.setIpsindate(new Date());
				} else if (menu.equals("IPS_OUT")) {
					tm.setIpsout("Y");
					tm.setIpsoutdate(new Date());
				}

				tranRep.save(tm);
			} else {
				logger.info("updateCBSStatus:Data Not found");

			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

	}

	public boolean checkRegPublicKey(CryptogramResponse cryptogramResponse) {

		boolean response = false;
		Session session = sessionFactory.getCurrentSession();

		try {
			@SuppressWarnings("unchecked")
			List<RegPublicKey> regCustProfile1 = (List<RegPublicKey>) session
					.createQuery("from RegPublicKey where device_id=?1 and acct_number=?2 and NVL(DEL_FLG,1) <> 'Y'")
					.setParameter(1, cryptogramResponse.getDeviceId())
					.setParameter(2, cryptogramResponse.getDebtorAccount()).getResultList();

			System.out.println(regCustProfile1.size());
			if (regCustProfile1.size() != 0) {
				response = true;
				return response;
			} else {
				response = false;
				return response;
			}
		} catch (Exception e) {
			response = false;
			return response;
		}

	}

	public String getPublicKey(String acctNumber) {

		String response = "0";
		Session session = sessionFactory.getCurrentSession();

		try {
			@SuppressWarnings("unchecked")
			List<RegPublicKey> regCustProfile1 = (List<RegPublicKey>) session
					.createQuery("from RegPublicKey where acct_number=?1 and NVL(DEL_FLG,1) <> 'Y'")
					.setParameter(1, acctNumber).getResultList();

			System.out.println(regCustProfile1.size());
			if (regCustProfile1.size() != 0) {
				response = regCustProfile1.get(0).getPublic_key();
				return response;
			} else {
				response = "0";
				return response;
			}
		} catch (Exception e) {
			response = "0";
			return response;
		}

	}

	public void updateCBSStatusManual(String seqUniqueID, String cbsStatus, String tranStatus) {
		try {
			Optional<TransactionMonitor> otm = tranRep.findById(seqUniqueID);

			if (otm.isPresent()) {
				TransactionMonitor tm = otm.get();
				tm.setCbs_status(cbsStatus);
				tm.setCbs_response_time(new Date());
				// tm.setCbs_status_error("");
				tm.setTran_status(tranStatus);
				tranRep.save(tm);
			} else {
				Optional<TranMonitoringHist> otmHist = tranHistRep.findById(seqUniqueID);

				if (otmHist.isPresent()) {
					TranMonitoringHist tm = otmHist.get();
					tm.setCbs_status(cbsStatus);
					tm.setCbs_response_time(new Date());
					// tm.setCbs_status_error("");
					tm.setTran_status(tranStatus);
					tranHistRep.save(tm);
				}
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

	}

	public void updateOutwardCBSStatusManual(String seqUniqueID, String cbsStatus, String tranStatus) {
		try {
			Optional<OutwardTransactionMonitoringTable> otm = outwardTranRep.findById(seqUniqueID);

			if (otm.isPresent()) {
				OutwardTransactionMonitoringTable tm = otm.get();
				tm.setCbs_status(cbsStatus);
				tm.setCbs_response_time(new Date());
				// tm.setCbs_status_error("");
				tm.setTran_status(tranStatus);
				outwardTranRep.save(tm);
			} else {
				Optional<OutwardTransHistMonitorTable> otmHist = outwardTranHistRep.findById(seqUniqueID);

				if (otmHist.isPresent()) {
					OutwardTransHistMonitorTable tm = otmHist.get();
					tm.setCbs_status(cbsStatus);
					tm.setCbs_response_time(new Date());
					// tm.setCbs_status_error("");
					tm.setTran_status(tranStatus);
					outwardTranHistRep.save(tm);
				}
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

	}

	public void updateCBSStatusErrorManual(String seqUniqueID, String cbsStatus, String error_desc, String tranStatus) {
		try {
			Optional<TransactionMonitor> otm = tranRep.findById(seqUniqueID);

			if (otm.isPresent()) {
				TransactionMonitor tm = otm.get();
				tm.setCbs_status(cbsStatus);
				tm.setCbs_status_error(error_desc);
				tm.setCbs_response_time(new Date());
				tm.setTran_status(tranStatus);
				tranRep.save(tm);
			} else {
				Optional<TranMonitoringHist> otmHist = tranHistRep.findById(seqUniqueID);

				if (otmHist.isPresent()) {
					TranMonitoringHist tm = otmHist.get();
					tm.setCbs_status(cbsStatus);
					tm.setCbs_status_error(error_desc);
					tm.setCbs_response_time(new Date());
					tm.setTran_status(tranStatus);
					tranHistRep.save(tm);
				}
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

	}

	public String getvalueTect(int i) {

		// TODO Auto-generated method stub
		return String.valueOf(i);
	}

	public void RegisterBulkDebitRecord(String psuDeviceID, String psuIpAddress, String sysTraceNumber,
			String remitterName, String remitterAcctNumber, String trAmt, String currencyCode, String BenAccountName,
			String BenacctNumber, String BenBankCode, String seqUniqueID, String bobMsgId, String endTOEndID,
			String master_ref_id, String tran_type_code, String msgNetMir, String instg_agt, String instd_agt,
			String dbtr_agt, String dbtr_agt_acc, String cdtr_agt, String cdtr_agt_acc, String instr_id, String svc_lvl,
			String lcl_instrm, String ctgy_purp, String userID,String remarks,
			String p_id,String requniqueId,String channel,String resvfield11,String resvfield2,String remitterBankCode) {

		try {
			OutwardTransactionMonitoringTable tranManitorTable = new OutwardTransactionMonitoringTable();
			tranManitorTable.setMsg_type(TranMonitorStatus.BULK_DEBIT.toString());
			tranManitorTable.setTran_audit_number(sysTraceNumber);
			tranManitorTable.setSequence_unique_id(seqUniqueID);
			tranManitorTable.setCim_message_id(bobMsgId);
			tranManitorTable.setCim_account(remitterAcctNumber);
			tranManitorTable.setIpsx_account(BenacctNumber);
			tranManitorTable.setReceiver_bank(BenBankCode);
			tranManitorTable.setInitiator_bank(remitterBankCode);
			tranManitorTable.setTran_amount(new BigDecimal(trAmt));
			tranManitorTable.setTran_date(new Date());
			tranManitorTable.setEntry_time(new Date());
			tranManitorTable.setCbs_status(TranMonitorStatus.CBS_DEBIT_INITIATED.toString());
			tranManitorTable.setEntry_user(userID);
			tranManitorTable.setTran_currency(currencyCode);
			tranManitorTable.setTran_status(TranMonitorStatus.INITIATED.toString());
			tranManitorTable.setDevice_id(psuDeviceID);
			tranManitorTable.setDevice_ip(psuIpAddress);
			// tranManitorTable.setNat_id(psuID);
			tranManitorTable.setMaster_ref_id(master_ref_id);
			tranManitorTable.setEnd_end_id(endTOEndID);
			tranManitorTable.setTran_type_code(tran_type_code);
			tranManitorTable.setNet_mir(msgNetMir);
			tranManitorTable.setCim_account_name(remitterName);
			tranManitorTable.setIpsx_account_name(BenAccountName);
			tranManitorTable.setInstg_agt(instg_agt);
			tranManitorTable.setInstd_agt(instd_agt);

			tranManitorTable.setDbtr_agt(dbtr_agt);
			tranManitorTable.setDbtr_agt_acc(dbtr_agt_acc);
			tranManitorTable.setCdtr_agt(cdtr_agt);
			tranManitorTable.setCdtr_agt_acc(cdtr_agt_acc);

			tranManitorTable.setInstr_id(instr_id);
			tranManitorTable.setSvc_lvl(svc_lvl);
			tranManitorTable.setLcl_instrm(lcl_instrm);
			tranManitorTable.setCtgy_purp(ctgy_purp);
			
			tranManitorTable.setP_id(p_id);
			tranManitorTable.setReq_unique_id(requniqueId);
			tranManitorTable.setInit_channel_id(channel);
			tranManitorTable.setResv_field1(resvfield11);
			tranManitorTable.setResv_field2(resvfield2);
			tranManitorTable.setTran_rmks(remarks);
			//// Check CutOff time after BOB settlement time
			//// if yes the value date is +1
			if (isTimeAfterCutOff()) {
				Date dt = new Date();
				Calendar c = Calendar.getInstance();
				c.setTime(dt);
				c.add(Calendar.DATE, 1);
				dt = c.getTime();
				tranManitorTable.setValue_date(dt);
			} else {
				tranManitorTable.setValue_date(new Date());
			}

			outwardTranRep.save(tranManitorTable);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
		
	}

	public void registerCamt53Record(com.bornfire.jaxb.camt_053_001_08.Document docCamt053_001_08) {

		List<AccountStatement91> accountStat = docCamt053_001_08.getBkToCstmrStmt().getStmt();

		for (AccountStatement91 acc : accountStat) {
			SettlementReport settlementReport = new SettlementReport();

			if(acc.getAcct().getId().getOthr().getId().equals(env.getProperty("ipsx.dbtragtacct"))) {
				settlementReport.setMsg_id(docCamt053_001_08.getBkToCstmrStmt().getGrpHdr().getMsgId());
				settlementReport.setMsg_date(
						docCamt053_001_08.getBkToCstmrStmt().getGrpHdr().getCreDtTm().toString().split("T")[0]);

				settlementReport.setStat_id(acc.getId());
				settlementReport.setAcct_id(acc.getAcct().getId().getOthr().getId());
				settlementReport.setAcct_owner(acc.getAcct().getOwnr().getId().getOrgId().getAnyBIC());

				List<CashBalance81> cashBalance = acc.getBal();
				StringBuilder balance = new StringBuilder();

				for (CashBalance81 cashBal : cashBalance) {

					balance.append(cashBal.getTp().getCdOrPrtry().getCd() + ":" + cashBal.getAmt().getValue().toString()
							+ ":" + cashBal.getAmt().getCcy() + ":" + cashBal.getCdtDbtInd().value() + ":"
							+ cashBal.getDt().getDt().toString() + "||");
				}
				settlementReport.setBalance(balance.toString());

				List<ReportEntry101> entry = acc.getNtry();
				for (ReportEntry101 repEntry : entry) {
					settlementReport.setEntry_acct_svcr_ref(repEntry.getAcctSvcrRef());
					settlementReport.setEntry_status_code(repEntry.getSts().getCd());
					settlementReport.setEntry_bk_tx_cd(repEntry.getBkTxCd().getPrtry().getCd());
					settlementReport.setEntry_amount(repEntry.getAmt().getValue().toString());
					settlementReport.setEntry_currency(repEntry.getAmt().getCcy());
					settlementReport.setEntry_entrydtl_btch_msg_id(repEntry.getNtryDtls().getBtch().getMsgId());
					settlementReport.setEntry_entrydtl_btch_no_tx(repEntry.getNtryDtls().getBtch().getNbOfTxs());
					settlementReport.setEntry_entrydtl_btch_tot_amt(
							repEntry.getNtryDtls().getBtch().getTtlAmt().getValue().toString());
					settlementReport.setEntry_entrydtl_btch_cncy(repEntry.getNtryDtls().getBtch().getTtlAmt().getCcy());
					settlementReport
							.setEntry_entrydtl_btch_cd_dbt_ind(repEntry.getNtryDtls().getBtch().getCdtDbtInd().value());

					if(repEntry.getNtryDtls().getTxDtls()!=null) {
						
						if(repEntry.getNtryDtls().getTxDtls().size()>0) {
							
							if(repEntry.getNtryDtls().getTxDtls().get(0).getRefs()!=null) {
								settlementReport.setEntry_entrydtl_txdts_refs_acctsvcrref(
										repEntry.getNtryDtls().getTxDtls().get(0).getRefs().getAcctSvcrRef());
								settlementReport.setEntry_entrydtl_txdts_refs_instr_id(
										repEntry.getNtryDtls().getTxDtls().get(0).getRefs().getInstrId());
								settlementReport.setEntry_entrydtl_txdts_refs_endtoend_id(
										repEntry.getNtryDtls().getTxDtls().get(0).getRefs().getEndToEndId());
								settlementReport.setEntry_entrydtl_txdts_refs_tx_id(
										repEntry.getNtryDtls().getTxDtls().get(0).getRefs().getTxId());	
							}
							

							settlementReport.setEntry_entrydtl_txdts_amt(
									repEntry.getNtryDtls().getTxDtls().get(0).getAmt().getValue().toString());
							settlementReport
									.setEntry_entrydtl_txdts_cncy(repEntry.getNtryDtls().getTxDtls().get(0).getAmt().getCcy());
							settlementReport.setEntry_entrydtl_txdts_cd_dbt_ind(
									repEntry.getNtryDtls().getTxDtls().get(0).getCdtDbtInd().value());
						}
						

					}
					settlementReport.setEntry_user("SYSTEM");

					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

					try {
						settlementReport.setValue_date(dateFormat.parse(repEntry.getValDt().getDt().toString()));
					} catch (ParseException e) {
						e.printStackTrace();
					}

					settlementReport.setEntry_time(new Date());

					settlementReportRep.save(settlementReport);
				}
			}


		}

	}

	public String getIPSXStatusError(String seqUniqueID) {

		String errorMsg = "";
		try {
			Optional<TransactionMonitor> otm = tranRep.findById(seqUniqueID);

			if (otm.isPresent()) {
				TransactionMonitor tm = otm.get();
				errorMsg = tm.getIpsx_status_error();
				return errorMsg;
			} else {
				Optional<TranMonitoringHist> otmHist = tranHistRep.findById(seqUniqueID);

				if (otmHist.isPresent()) {
					TranMonitoringHist tm = otmHist.get();
					errorMsg = tm.getIpsx_status_error();
					return errorMsg;
				}
			}
		} catch (Exception e) {

		}
		return errorMsg;

	}

	public List<RegPublicKey> getChargeFailedMytList() {
		List<RegPublicKey> list = regPublicKeyRep.getChargeFailedMytList();
		return list;
	}

	public void updateSettlAmtTablePayableFlg() {
		Optional<SettlementAccountAmtTable> settlAmtOpt = settlAcctAmtRep
				.customfindById(new SimpleDateFormat("dd-MMM-yyyy").format(previousDay()));
		if (settlAmtOpt.isPresent()) {
			SettlementAccountAmtTable settlAmt = settlAmtOpt.get();
			settlAmt.setPayable_flg("Y");
			settlAcctAmtRep.save(settlAmt);
		}

	}

	public void updateSettlAmtTableReceivableFlg() {
		Optional<SettlementAccountAmtTable> settlAmtOpt = settlAcctAmtRep
				.customfindById(new SimpleDateFormat("dd-MMM-yyyy").format(previousDay()));
		if (settlAmtOpt.isPresent()) {
			SettlementAccountAmtTable settlAmt = settlAmtOpt.get();
			settlAmt.setReceivable_flg("Y");
			settlAcctAmtRep.save(settlAmt);
		}

	}

	public void updateSettlAmtTableIncomeFlg() {
		Optional<SettlementAccountAmtTable> settlAmtOpt = settlAcctAmtRep
				.customfindById(new SimpleDateFormat("dd-MMM-yyyy").format(previousDay()));
		if (settlAmtOpt.isPresent()) {
			SettlementAccountAmtTable settlAmt = settlAmtOpt.get();
			settlAmt.setIncome_flg("Y");
			settlAcctAmtRep.save(settlAmt);
		}

	}

	private Date previousDay() {
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		return cal.getTime();
	}

	public void updateSettlAmtTableExpenseFlg() {
		Optional<SettlementAccountAmtTable> settlAmtOpt = settlAcctAmtRep
				.customfindById(new SimpleDateFormat("dd-MMM-yyyy").format(previousDay()));
		if (settlAmtOpt.isPresent()) {
			SettlementAccountAmtTable settlAmt = settlAmtOpt.get();
			settlAmt.setExpense_flg("Y");
			settlAcctAmtRep.save(settlAmt);
		}

	}
	
	private boolean validateJWTToken(String jwtToken, String publicKeyfromMYT,String deviceID,String consentID) {

		boolean response = false;

		try {
						
			if(!publicKeyfromMYT.equals("0")) {

			        PublicKey publicKey = decodePublicKey(pemToDer(publicKeyfromMYT));

			        Jws<Claims> claimsJws = Jwts.parser() //
			                .setSigningKey(publicKey) //
			                .parseClaimsJws(jwtToken) //
			                ;			        
			        CryptogramResponse cryptogramResponse = new Gson().fromJson(claimsJws.getBody().toString(), CryptogramResponse.class);

			        logger.info(cryptogramResponse.toString());

					if (deviceID.equals(cryptogramResponse.getDeviceId())
							&& consentID.equals(cryptogramResponse.getConsentId())) {
						response=true;
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

	public String registerWalletData(String w_request_id, String psuDeviceID, String psuIPAddress, String psuID,
			String psuIDCountry, String psuIDType, WalletAccessRequest consentRequest, String walletID, String authID,
			String otp,String walletAcctNo) {
		try {

			//// Save Public Key data to temp table
			WalletAccessTmpTable walletAccessID = new WalletAccessTmpTable();
			walletAccessID.setWallet_acct_no(walletAcctNo);
			walletAccessID.setW_request_id(w_request_id);
			walletAccessID.setPsu_device_id(psuDeviceID);
			walletAccessID.setPsu_ip_address(psuIPAddress);
			walletAccessID.setPsu_id(psuID);
			walletAccessID.setPsu_id_country(psuIDCountry);
			walletAccessID.setPsu_id_type(psuIDType);
			walletAccessID.setWallet_id(walletID);
			walletAccessID.setAuth_id(authID);
			walletAccessID.setPhone_number(consentRequest.getPhoneNumber());
			walletAccessID.setPublic_key(consentRequest.getPublicKey());

			

			walletAccessID.setSchm_name(consentRequest.getAccount().get(0).getSchemeName());
			walletAccessID.setIdentification(consentRequest.getAccount().get(0).getIdentification());
			walletAccessID.setEntry_user("SYSTEM");
			walletAccessID.setEntry_time(new Date());

			

			walletAccessTmpTableRep.save(walletAccessID);

			//// Save OTP to OTP table
			/*OTPGenTable otpgentable = new OTPGenTable();
			otpgentable.setAuth_id(authID);
			otpgentable.setRecord_id(walletID);

			Date generateDate = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(generateDate);
			cal.add(Calendar.MINUTE, 5);
			Date expirationDate = cal.getTime();

			otpgentable.setGenerated_date(generateDate);
			otpgentable.setExpired_date(expirationDate);

			otpgentable.setScreen_id("CREATE WALLET");
			otpgentable.setOtp(otp);

			otpGenTableRep.save(otpgentable);*/

			return "0";
		} catch (Exception e) {
			e.printStackTrace();
			return "1";
		}

	}
	
	
   public WalletAccessResponse checkReqAccountsExist(String w_request_id, String psuDeviceID, String psuIPAddress, String psuID,
			String psuIDCountry, String psuIDType, WalletAccessRequest walletRequest, String walletID, String authID,
			String otp,String walletAcctNo) {
		
		List<WalletAccessTable> dataList=walletAccessTableRep.findByData(walletRequest.getPhoneNumber(),psuID);
		if(dataList.size()>0) {
			WalletAccessTable walletAccessID = dataList.get(0);
			walletAccessID.setW_request_id(w_request_id);
			walletAccessID.setPsu_device_id(psuDeviceID);
			walletAccessID.setPsu_ip_address(psuIPAddress);
			walletAccessID.setPsu_id(psuID);
			walletAccessID.setPsu_id_country(psuIDCountry);
			walletAccessID.setPsu_id_type(psuIDType);
			//walletAccessID.setWallet_id(walletID);
			walletAccessID.setAuth_id(authID);
			walletAccessID.setPhone_number(walletRequest.getPhoneNumber());
			walletAccessID.setPublic_key(walletRequest.getPublicKey());
			
			walletAccessID.setSchm_name(walletRequest.getAccount().get(0).getSchemeName());
			walletAccessID.setIdentification(walletRequest.getAccount().get(0).getIdentification());
			
			walletAccessID.setEntry_user("SYSTEM");
			walletAccessID.setEntry_time(new Date());
			walletAccessID.setDel_flg("N");
			

			walletAccessTableRep.save(walletAccessID);
			
			Optional<WalletAccessTmpTable> data=walletAccessTmpTableRep.findById(authID);
			if(data.isPresent()) {
				WalletAccessTmpTable TmpData=data.get();
				TmpData.setDel_flg("Y");
				walletAccessTmpTableRep.save(TmpData);
			}
			
			WalletAccessResponse response = new WalletAccessResponse();
			response.setWalletID(walletAccessID.getWallet_id());
			response.setStatus(TranMonitorStatus.SUCCESS.toString());
			response.setWalletAccountNumber(walletAccessID.getWallet_acct_no());
			
			C24FTResponseBalance Balance=new C24FTResponseBalance();
			DecimalFormat df = new DecimalFormat("#0.00");
			Balance.setAvailableBalance( df.format(walletAccessID.getWallet_balance()).toString());
			Balance.setFFTBalance("0.00");
			Balance.setFloatBalance("0.00");
			Balance.setUserDefBalance("0.00");
			Balance.setLedgeBalance("0.00");
			response.setBalance(Balance);
			
			return response;

		}else {
			WalletAccessTable walletAccessID =new WalletAccessTable();
			walletAccessID.setW_request_id(w_request_id);
			walletAccessID.setPsu_device_id(psuDeviceID);
			walletAccessID.setPsu_ip_address(psuIPAddress);
			walletAccessID.setPsu_id(psuID);
			walletAccessID.setPsu_id_country(psuIDCountry);
			walletAccessID.setPsu_id_type(psuIDType);
			walletAccessID.setWallet_id(walletID);
			walletAccessID.setAuth_id(authID);
			walletAccessID.setPhone_number(walletRequest.getPhoneNumber());
			walletAccessID.setPublic_key(walletRequest.getPublicKey());
			walletAccessID.setWallet_acct_no(walletAcctNo);
			walletAccessID.setWallet_balance(new BigDecimal(0));
			
			walletAccessID.setSchm_name(walletRequest.getAccount().get(0).getSchemeName());
			walletAccessID.setIdentification(walletRequest.getAccount().get(0).getIdentification());
			
			walletAccessID.setEntry_user("SYSTEM");
			walletAccessID.setEntry_time(new Date());
			walletAccessID.setDel_flg("N");
			

			walletAccessTableRep.save(walletAccessID);
			
			Optional<WalletAccessTmpTable> data=walletAccessTmpTableRep.findById(authID);
			if(data.isPresent()) {
				WalletAccessTmpTable TmpData=data.get();
				TmpData.setDel_flg("Y");
				walletAccessTmpTableRep.save(TmpData);
			}
			
			WalletAccessResponse response = new WalletAccessResponse();
			response.setWalletID(walletAccessID.getWallet_id());
			response.setStatus(TranMonitorStatus.SUCCESS.toString());
			response.setWalletAccountNumber(walletAccessID.getWallet_acct_no());
			
			C24FTResponseBalance Balance=new C24FTResponseBalance();
			DecimalFormat df = new DecimalFormat("#.00");
			Balance.setAvailableBalance( df.format(walletAccessID.getWallet_balance()).toString());
			Balance.setFFTBalance("0.00");
			Balance.setFloatBalance("0.00");
			Balance.setUserDefBalance("0.00");
			Balance.setLedgeBalance("0.00");
			response.setBalance(Balance);
			
			return response;
		}
		
	}
   
   public boolean walletIDExist(String walletID) {
		boolean stat=false;
		Optional<WalletAccessTable> data=walletAccessTableRep.findById(walletID);
		if(data.isPresent()) {
			stat=true;
		}else {
			stat=false;
		}
		// TODO Auto-generated method stub
		return stat;
	}
   
   public WalletAccessTable checkReqAccountsExist1( String psuID,String PhoneNumber) {
	   WalletAccessTable walletResponse=null;
		List<WalletAccessTable> dataList=walletAccessTableRep.findByData(PhoneNumber,psuID);
		if(dataList.size()>0) {
			walletResponse =dataList.get(0);
			return walletResponse;
		}else {
			walletResponse=new WalletAccessTable();
			return walletResponse;
		}
   }
		
			
   public WalletAccessTable getWalletData(String walletID) {
		WalletAccessTable walletAccessData=new WalletAccessTable();
		Optional<WalletAccessTable> data=walletAccessTableRep.findById(walletID);
		if(data.isPresent()) {
			walletAccessData=data.get();
		}
		// TODO Auto-generated method stub
		return walletAccessData;
	}


	public SCAAthenticationResponse updateSCAwalletAccess(SCAAuthenticatedData scaAuthenticatedData, String walletID,
			String authID) {
		Optional<WalletAccessTmpTable> regPublicKeyTmpotm = walletAccessTmpTableRep.findById(authID);
		Optional<WalletAccessTable> regPublicKeyotm = walletAccessTableRep.findById(walletID);
		Optional<OTPGenTable> otm = otpGenTableRep.findById(authID);
		SCAAthenticationResponse response = null;

		if (regPublicKeyTmpotm.isPresent()) {
			if (regPublicKeyTmpotm.get().getWallet_id().equals(walletID)) {
//				if(validateJWTToken(cryptogram,regPublicKeyTmpotm.get().getPublic_key(),regPublicKeyTmpotm.get().getPsu_device_id(),
//						regPublicKeyTmpotm.get().getConsent_id())) {
					if (otm.isPresent()) {
						OTPGenTable otpGenTable = otm.get();
						if (scaAuthenticatedData.getScaAuthenticationData().toString().equals(otpGenTable.getOtp().toString())) {
							if (isNowBetweenDateTime(otpGenTable.getGenerated_date(), otpGenTable.getExpired_date())) {
								
								response = new SCAAthenticationResponse();
								// String recordID=sequence.generateRecordId();
								Links links = new Links();
								links.setSCAStatus("/accounts/" + walletID + "/authorisations/" + authID);
								response.setSCAStatus("finalised");
								response.setLinks(links);

								if (regPublicKeyTmpotm.isPresent()) {
									if (regPublicKeyTmpotm.get().getWallet_id().equals(walletID)) {
										if (regPublicKeyotm.isPresent()) {
											///// update public key
											WalletAccessTable consentAccessID = regPublicKeyotm.get();
											consentAccessID.setW_request_id(regPublicKeyTmpotm.get().getW_request_id());
											consentAccessID.setPsu_device_id(regPublicKeyTmpotm.get().getPsu_device_id());
											consentAccessID.setPsu_ip_address(regPublicKeyTmpotm.get().getPsu_ip_address());
											consentAccessID.setPsu_id(regPublicKeyTmpotm.get().getPsu_id());
											consentAccessID.setPsu_id_country(regPublicKeyTmpotm.get().getPsu_id_country());
											consentAccessID.setPsu_id_type(regPublicKeyTmpotm.get().getPsu_id_type());
											consentAccessID.setWallet_id(regPublicKeyTmpotm.get().getWallet_id());
											consentAccessID.setAuth_id(regPublicKeyTmpotm.get().getAuth_id());
											consentAccessID.setPhone_number(regPublicKeyTmpotm.get().getPhone_number());
											consentAccessID.setPublic_key(regPublicKeyTmpotm.get().getPublic_key());
											
											consentAccessID.setSchm_name(regPublicKeyTmpotm.get().getSchm_name());
											consentAccessID.setIdentification(regPublicKeyTmpotm.get().getIdentification());
											
											consentAccessID.setEntry_user("SYSTEM");
											consentAccessID.setEntry_time(new Date());

											walletAccessTableRep.save(consentAccessID);

											walletAccessTmpTableRep.deleteById(authID);

										} else {

											//// Delete Previous Account Number List
											List<WalletAccessTable> regAccList = walletAccessTableRep
													.getAccountNumber(regPublicKeyTmpotm.get().getIdentification());
											if (regAccList.size() > 0) {
												WalletAccessTable consentAccessID = regAccList.get(0);

												consentAccessID.setW_request_id(regPublicKeyTmpotm.get().getW_request_id());
												consentAccessID.setPsu_device_id(regPublicKeyTmpotm.get().getPsu_device_id());
												consentAccessID.setPsu_ip_address(regPublicKeyTmpotm.get().getPsu_ip_address());
												consentAccessID.setPsu_id(regPublicKeyTmpotm.get().getPsu_id());
												consentAccessID.setPsu_id_country(regPublicKeyTmpotm.get().getPsu_id_country());
												consentAccessID.setPsu_id_type(regPublicKeyTmpotm.get().getPsu_id_type());
												consentAccessID.setWallet_id(regPublicKeyTmpotm.get().getWallet_id());
												consentAccessID.setAuth_id(regPublicKeyTmpotm.get().getAuth_id());
												consentAccessID.setPhone_number(regPublicKeyTmpotm.get().getPhone_number());
												consentAccessID.setPublic_key(regPublicKeyTmpotm.get().getPublic_key());
												
												consentAccessID.setSchm_name(regPublicKeyTmpotm.get().getSchm_name());
												consentAccessID.setIdentification(regPublicKeyTmpotm.get().getIdentification());

												consentAccessID.setEntry_user("SYSTEM");
												consentAccessID.setEntry_time(new Date());
												consentAccessID.setDel_flg("N");
												
												walletAccessTableRep.save(consentAccessID);
												
											
											} else {
												//// Insert public key
												WalletAccessTable consentAccessID = new WalletAccessTable();

												consentAccessID.setW_request_id(regPublicKeyTmpotm.get().getW_request_id());
												consentAccessID.setPsu_device_id(regPublicKeyTmpotm.get().getPsu_device_id());
												consentAccessID.setPsu_ip_address(regPublicKeyTmpotm.get().getPsu_ip_address());
												consentAccessID.setPsu_id(regPublicKeyTmpotm.get().getPsu_id());
												consentAccessID.setPsu_id_country(regPublicKeyTmpotm.get().getPsu_id_country());
												consentAccessID.setPsu_id_type(regPublicKeyTmpotm.get().getPsu_id_type());
												consentAccessID.setWallet_id(regPublicKeyTmpotm.get().getWallet_id());
												consentAccessID.setAuth_id(regPublicKeyTmpotm.get().getAuth_id());
												consentAccessID.setPhone_number(regPublicKeyTmpotm.get().getPhone_number());
												consentAccessID.setPublic_key(regPublicKeyTmpotm.get().getPublic_key());

												
												consentAccessID.setSchm_name(regPublicKeyTmpotm.get().getSchm_name());
												consentAccessID.setIdentification(regPublicKeyTmpotm.get().getIdentification());
												
												consentAccessID.setEntry_user("SYSTEM");
												consentAccessID.setEntry_time(new Date());
												consentAccessID.setDel_flg("N");
												
												walletAccessTableRep.save(consentAccessID);
											}

											walletAccessTmpTableRep.deleteById(authID);

											//// Calling Connect 24 for Collect Registration fee from Customer
											//// After Successfull registration

											return response;
										}
									} else {
										throw new IPSXException(errorCode.ErrorCodeRegistration("1"));
									}

								} else {
									throw new IPSXException(errorCode.ErrorCodeRegistration("3"));
								}
								
							} else {
								throw new IPSXException(errorCode.ErrorCodeRegistration("11"));
							}
						} else {
							throw new IPSXException(errorCode.ErrorCodeRegistration("12"));
						}
					}else {
						throw new IPSXException(errorCode.ErrorCodeRegistration("3"));
					}
//				}else {
//					throw new IPSXRestException(errorCode.ErrorCodeRegistration("4"));
//				}
			} else {
				throw new IPSXException(errorCode.ErrorCodeRegistration("13"));
			}
		} else {
			throw new IPSXException(errorCode.ErrorCodeRegistration("3"));
		}
		return response;

	}

	public String RegisterWalletTopupData(String w_request_id, String psuDeviceID, String psuIPAddress, String psuID,
			String psuIDCountry, String psuIDType, String walletID, WalletFndTransferRequest walletFndTransferRequest) {
		// TODO Auto-generated method stub
		String response="0";
		try {
			WalletRequestRegisterTable walletRequestRegisterData=new WalletRequestRegisterTable();
			walletRequestRegisterData.setW_request_id(w_request_id);
			walletRequestRegisterData.setWallet_id(walletID);
			walletRequestRegisterData.setWallet_acct_no(walletFndTransferRequest.getWalletAccountNumber());
			walletRequestRegisterData.setSequence_unique_id("");
			walletRequestRegisterData.setTran_date(new Date());
			walletRequestRegisterData.setValue_date(new Date());
			walletRequestRegisterData.setOther_account(walletFndTransferRequest.getFrAccount().getAcctNumber());
			walletRequestRegisterData.setOther_account_name(walletFndTransferRequest.getFrAccount().getAcctName());
			walletRequestRegisterData.setTran_type("DEP");
			walletRequestRegisterData.setTran_sub_type("BANK_I");
			walletRequestRegisterData.setPart_tran_type("CR");
			walletRequestRegisterData.setTran_remarks("BANK_I/"+w_request_id+"/"+walletFndTransferRequest.getTrRmks());
			walletRequestRegisterData.setTran_amount(new BigDecimal(walletFndTransferRequest.getTrAmt()));
			walletRequestRegisterData.setTran_currency(walletFndTransferRequest.getCurrencyCode());
			walletRequestRegisterData.setPan(walletFndTransferRequest.getPan());
			walletRequestRegisterData.setPurpose(walletFndTransferRequest.getPurpose());
			walletRequestRegisterData.setPsu_device_id(psuDeviceID);
			walletRequestRegisterData.setPsu_ip_address(psuIPAddress);
			walletRequestRegisterData.setPsu_id(psuID);
			walletRequestRegisterData.setPsu_id_country(psuIDCountry);
			walletRequestRegisterData.setPsu_id_type(psuIDType);
			walletRequestRegisterData.setTran_status("INITIATED");
			walletRequestRegisterData.setEntry_user("SYSTEM");
			walletRequestRegisterData.setEntry_time(new Date());
			
			walletRequestRegisterTableRep.save(walletRequestRegisterData);
			
			response="1";
		}catch(Exception e) {
			logger.debug(e.getLocalizedMessage());
			 response="0";
		}
		
		
		return response;
	}

	public String RegisterWalletTranData(String w_request_id, String psuDeviceID, String psuIPAddress, String psuID,
			String psuIDCountry, String psuIDType, String walletID, WalletFndTransferRequest walletFndTransferRequest,String tranTypeCode) {

		// TODO Auto-generated method stub
		String response="0";
		try {
			WalletRequestRegisterTable walletRequestRegisterData=new WalletRequestRegisterTable();
			walletRequestRegisterData.setW_request_id(w_request_id);
			walletRequestRegisterData.setWallet_id(walletID);
			walletRequestRegisterData.setWallet_acct_no("");
			walletRequestRegisterData.setSequence_unique_id("");
			walletRequestRegisterData.setTran_date(new Date());
			walletRequestRegisterData.setValue_date(new Date());
			walletRequestRegisterData.setOther_account(walletFndTransferRequest.getToAccount().getAcctNumber());
			walletRequestRegisterData.setOther_account_name(walletFndTransferRequest.getToAccount().getAcctName());
			walletRequestRegisterData.setTran_type("WDL");
			
			if(tranTypeCode.equals("0") || tranTypeCode.equals("1")) {
				walletRequestRegisterData.setTran_sub_type("BANK_I");
				walletRequestRegisterData.setTran_remarks("BANK_I/"+w_request_id+"/"+walletFndTransferRequest.getTrRmks());

			}else {
				walletRequestRegisterData.setTran_sub_type("BANK_O");
				walletRequestRegisterData.setTran_remarks("BANK_O/"+w_request_id+"/"+walletFndTransferRequest.getTrRmks());

			}
			walletRequestRegisterData.setPart_tran_type("DBT");
			walletRequestRegisterData.setOther_bank_code(walletFndTransferRequest.getToAccount().getBankCode());
			walletRequestRegisterData.setTran_amount(new BigDecimal(walletFndTransferRequest.getTrAmt()));
			walletRequestRegisterData.setTran_currency(walletFndTransferRequest.getCurrencyCode());
			walletRequestRegisterData.setPan(walletFndTransferRequest.getPan());
			walletRequestRegisterData.setPurpose(walletFndTransferRequest.getPurpose());
			walletRequestRegisterData.setPsu_device_id(psuDeviceID);
			walletRequestRegisterData.setPsu_ip_address(psuIPAddress);
			walletRequestRegisterData.setPsu_id(psuID);
			walletRequestRegisterData.setTran_status("INITIATED");
			walletRequestRegisterData.setPsu_id_country(psuIDCountry);
			walletRequestRegisterData.setPsu_id_type(psuIDType);
			walletRequestRegisterData.setEntry_user("SYSTEM");
			
			walletRequestRegisterData.setEntry_time(new Date());
			
			walletRequestRegisterTableRep.save(walletRequestRegisterData);
			
			response="1";
		}catch(Exception e) {
			logger.debug(e.getLocalizedMessage());
			 response="0";
		}
		
		
		return response;		
	}

	public String updateWalletRegisterTranResponseFailure( String w_request_id,String status,String error_desc) {
		String response="0";
		try {
			Optional<WalletRequestRegisterTable> data=walletRequestRegisterTableRep.findById(w_request_id);
			if(data.isPresent()) {
				WalletRequestRegisterTable walletRequestRegisterTable=data.get();
				walletRequestRegisterTable.setTran_status(status);
				walletRequestRegisterTable.setTran_status_error(error_desc);
				walletRequestRegisterTableRep.save(walletRequestRegisterTable);
				response="1";
			}
		}catch(Exception e) {
			logger.debug(e.getLocalizedMessage());
			 response="0";
		}
		return response;
		
	}

	public String updateWalletRegisterTranResponseSuccess(String w_request_id, String status) {

		String response="0";
		try {
			Optional<WalletRequestRegisterTable> data = walletRequestRegisterTableRep.findById(w_request_id);
			if (data.isPresent()) {
				WalletRequestRegisterTable walletRequestRegisterTable = data.get();
				walletRequestRegisterTable.setTran_status(status);
				walletRequestRegisterTableRep.save(walletRequestRegisterTable);
				response="1";
			}
		} catch (Exception e) {
			logger.debug(e.getLocalizedMessage());
			response = "0";
		}

		return response;
	}

	public void updateWalletBalance(String walletID,String tranType,String trAmt) {
		try {
			Optional<WalletAccessTable> data=walletAccessTableRep.findById(walletID);
			if(data.isPresent()) {
				WalletAccessTable walletAccessTable=data.get();
				Double wallBal=Double.parseDouble(walletAccessTable.getWallet_balance().toString());
				if(tranType.equals("DEP")) {
					wallBal=wallBal+
							Double.parseDouble(trAmt);
				}else {
					wallBal=wallBal-
							Double.parseDouble(trAmt);
				}
				
				walletAccessTable.setWallet_balance(new BigDecimal(wallBal.toString()));
				walletAccessTableRep.save(walletAccessTable);
			}
		}catch(Exception e) {
			logger.debug(e.getLocalizedMessage());
		}
		
		
	}

	public List<WalletRequestRegisterTable> getWalletStatement(String walletID) {
		List<WalletRequestRegisterTable> list=walletRequestRegisterTableRep.findByStatement(walletID);
		return list;
	}

	public String RegisterFundTRansferTopUpData(String w_request_id, String psuDeviceID, String psuID, String psuIPAddress,
			WalletFndTransferRequest walletFndTransferRequest) {
		String response=tranAllDetailsTableRep.registerTranData(w_request_id,walletFndTransferRequest.getFrAccount().getAcctNumber(), walletFndTransferRequest.getWalletAccountNumber(), "", new SimpleDateFormat("dd-MMM-yyyy").format(new Date()), "WIN", walletFndTransferRequest.getTrAmt(), walletFndTransferRequest.getCurrencyCode(), walletFndTransferRequest.getTrRmks(), walletFndTransferRequest.getTrRmks(), "", "", "", "");
		return response;
	}
	
	public String RegisterFundTRansferData(String w_request_id, String psuDeviceID, String psuID, String psuIPAddress,
			WalletFndTransferRequest walletFndTransferRequest,String tranTypeCode) {
		String response="0";
		if(tranTypeCode.equals("0")||tranTypeCode.equals("1")) {
			response=tranAllDetailsTableRep.registerTranData(w_request_id,walletFndTransferRequest.getFrAccount().getAcctNumber(), walletFndTransferRequest.getWalletAccountNumber(), "", new SimpleDateFormat("dd-MMM-yyyy").format(new Date()), "WIN", walletFndTransferRequest.getTrAmt(), walletFndTransferRequest.getCurrencyCode(), walletFndTransferRequest.getTrRmks(), walletFndTransferRequest.getTrRmks(), "", "", "", "");
		}else {
			response=tranAllDetailsTableRep.registerTranData(w_request_id,walletFndTransferRequest.getFrAccount().getAcctNumber(), walletFndTransferRequest.getWalletAccountNumber(), "", new SimpleDateFormat("dd-MMM-yyyy").format(new Date()), "WOUT", walletFndTransferRequest.getTrAmt(), walletFndTransferRequest.getCurrencyCode(), walletFndTransferRequest.getTrRmks(), walletFndTransferRequest.getTrRmks(), "", "", "", "");
		}
		return response;
	}

	
	
	public void updateFundTransferDataResponseSuccess(String wRequestID, String status,String status_error) {
		int response=tranAllDetailsTableRep.updateResponseData(wRequestID,status,status_error);
		
	}
	
	public void updateIncomingmessage(String sysTraceNumber, String bobMsgID, String ipsxMsgID, String seqUniqueID,
			String dbtrAcctNumber, String cdtrAcctNumber, BigDecimal trAmount, String Currency_code, String tranStatus,
			String EndToEndID, String ipsxAccountName, String bobAccountName, String tran_type_code,
			String initiatorBank, String instgAgt, String instdAgt, String instr_id, String svc_lvl, String lcl_instrm,
			String ctgy_purp, String dbtr_agt, String dbtr_agt_acc, String cdtr_agt, String cdtr_agt_acc) {
		TransactionMonitor tranManitorTable = new TransactionMonitor();
		tranManitorTable.setMsg_type(TranMonitorStatus.INCOMING.toString());
		
		tranManitorTable.setTran_audit_number(sysTraceNumber);
		tranManitorTable.setBob_message_id(bobMsgID);
		tranManitorTable.setIpsx_message_id(ipsxMsgID);
		tranManitorTable.setSequence_unique_id(seqUniqueID);
		tranManitorTable.setBob_account(cdtrAcctNumber);
		tranManitorTable.setIpsx_account(dbtrAcctNumber);
		tranManitorTable.setReceiver_bank("");
		tranManitorTable.setTran_amount(trAmount);
		tranManitorTable.setTran_date(new Date());
		tranManitorTable.setEntry_time(new Date());
		tranManitorTable.setEntry_user("SYSTEM");
		tranManitorTable.setTran_currency("MUR");
		tranManitorTable.setEnd_end_id(EndToEndID);
		tranManitorTable.setIpsx_account_name(ipsxAccountName);
		tranManitorTable.setBob_account_name(bobAccountName);
		tranManitorTable.setTran_type_code("100");

		tranManitorTable.setInstg_agt(instgAgt);
		tranManitorTable.setInstd_agt(instdAgt);

		tranManitorTable.setInstr_id(instr_id);
		tranManitorTable.setSvc_lvl(svc_lvl);
		tranManitorTable.setLcl_instrm(lcl_instrm);
		tranManitorTable.setCtgy_purp(ctgy_purp);

		tranManitorTable.setDbtr_agt(dbtr_agt);
		tranManitorTable.setDbtr_agt_acc(dbtr_agt_acc);
		tranManitorTable.setCdtr_agt(cdtr_agt);
		tranManitorTable.setCdtr_agt_acc(cdtr_agt_acc);

		tranManitorTable.setValue_date(new Date());

		
		if(cdtrAcctNumber.equals("90370100890614")) {
			//tranManitorTable.setIpsx_message_id("M2248279/002");
			tranManitorTable.setIpsx_message_id(bobMsgID);
			tranManitorTable.setCbs_status(TranMonitorStatus.CBS_CREDIT_OK.toString());
			tranManitorTable.setCbs_response_time(new Date());
			tranManitorTable.setIpsx_status(TranMonitorStatus.IPSX_RESPONSE_ACSP.toString());
			tranManitorTable.setTran_status(TranMonitorStatus.SUCCESS.toString());
			tranManitorTable.setResponse_status(TranMonitorStatus.ACSP.toString());
		}else if(cdtrAcctNumber.equals("90311111111111")) {
			tranManitorTable.setCbs_status(TranMonitorStatus.CBS_CREDIT_REVERSE_ERROR.toString());
			tranManitorTable.setCbs_response_time(new Date());
			tranManitorTable.setIpsx_status(TranMonitorStatus.IPSX_RESPONSE_RJCT.toString());
			tranManitorTable.setTran_status(TranMonitorStatus.FAILURE.toString());
			tranManitorTable.setIpsx_status_code("306");
			tranManitorTable.setIpsx_status_error("Incorrect Account Number");
			tranManitorTable.setResponse_status(TranMonitorStatus.RJCT.toString());
		}else if(cdtrAcctNumber.equals("90370100890656")) {
			tranManitorTable.setCbs_status(TranMonitorStatus.CBS_CREDIT_REVERSE_ERROR.toString());
			tranManitorTable.setCbs_response_time(new Date());
			tranManitorTable.setIpsx_status(TranMonitorStatus.IPSX_RESPONSE_RJCT.toString());
			tranManitorTable.setTran_status(TranMonitorStatus.FAILURE.toString());
			tranManitorTable.setIpsx_status_code("307");
			tranManitorTable.setIpsx_status_error("Transaction Forbidden");
			tranManitorTable.setResponse_status(TranMonitorStatus.RJCT.toString());
		}else {
	///////tranManitorTable.setIpsx_message_id(bobMsgID);
			tranManitorTable.setCbs_status(TranMonitorStatus.CBS_CREDIT_OK.toString());
			tranManitorTable.setCbs_response_time(new Date());
			tranManitorTable.setIpsx_status(TranMonitorStatus.IPSX_RESPONSE_ACSP.toString());
			tranManitorTable.setTran_status(TranMonitorStatus.SUCCESS.toString());
			tranManitorTable.setResponse_status(TranMonitorStatus.ACSP.toString());
				
				//////
		}

		tranRep.save(tranManitorTable);
		tranRep.flush();
		
	}

	

	public String outwardConsentDataRegister(String x_request_id, String psuDeviceID, String psuIPAddress, String psuID,
			String psuIDCountry, String psuIDType, ConsentOutwardAccessRequest consentOutwardAccessRequest,
			String senderParticipantBic,String senderParticipanrMemberID,String receiverParticipantBic,String receiverParticipantMemberID,
			String publicKey,String privateKey,String custom_device_id,String channelID) {
		
		try {

			//// Save Public Key data to temp table
			ConsentOutwardAccessTmpTable consentAccessID = new ConsentOutwardAccessTmpTable();
			consentAccessID.setX_request_id(x_request_id);
			consentAccessID.setCustom_device_id(custom_device_id);
			consentAccessID.setSenderparticipant_bic(senderParticipantBic);
			consentAccessID.setSenderparticipant_memberid(senderParticipanrMemberID);
			consentAccessID.setReceiverparticipant_bic(receiverParticipantBic);
			consentAccessID.setReceiverparticipant_memberid(receiverParticipantMemberID);
			consentAccessID.setPsu_device_id(psuDeviceID);
			consentAccessID.setPsu_ip_address(psuIPAddress);
			consentAccessID.setPsu_id(psuID);
			consentAccessID.setPsu_id_country(psuIDCountry);
			consentAccessID.setPsu_id_type(psuIDType);
			consentAccessID.setPhone_number(consentOutwardAccessRequest.getPhoneNumber());
			consentAccessID.setPublic_key(publicKey);
			consentAccessID.setPrivate_key(privateKey);
			consentAccessID.setSchm_name(consentOutwardAccessRequest.getAccounts().getShemeName());
			consentAccessID.setIdentification(consentOutwardAccessRequest.getAccounts().getAccountNumber());
			consentAccessID.setCreate_date(new Date());
			consentAccessID.setStatus_update_date(new Date());
			consentAccessID.setPermission(String.join("-", consentOutwardAccessRequest.getPermissions()));
			consentAccessID.setAcct_name(consentOutwardAccessRequest.getAccounts().getAccountName());
			consentAccessID.setDel_flg("N");
			consentAccessID.setPsu_channel(channelID);
			consentOutwardAccessTmpTableRep.save(consentAccessID);

			
			return "0";
		} catch (Exception e) {
			e.printStackTrace();
			return "1";
		}

	}

	public void outwardConsentDataRegisterUpdateResponse(String w_request_id, ConsentAccessResponse consentAccessResponse) {
		
		try{
			Optional <ConsentOutwardAccessTmpTable> data=consentOutwardAccessTmpTableRep.findById(w_request_id);
			if(data.isPresent()) {
				ConsentOutwardAccessTmpTable consentOutwardAccessTmpTable=data.get();
				consentOutwardAccessTmpTable.setConsent_id(consentAccessResponse.getConsentID());
				consentOutwardAccessTmpTable.setStatus(consentAccessResponse.getStatus());
				consentOutwardAccessTmpTable.setAccount_status(consentAccessResponse.getStatus());
				//consentOutwardAccessTmpTable.setPermission(String.join("-", consentAccessResponse.getPermissions()));
				consentOutwardAccessTmpTable.setAuth_id(consentAccessResponse.getLinks().getAuthoriseTransaction().substring
						(consentAccessResponse.getLinks().getAuthoriseTransaction().lastIndexOf("/")+1));
				
				
				
				for (String permi : consentAccessResponse.getPermissions()) {
					if (permi.equals(TranMonitorStatus.ReadBalances.toString())) {
						consentOutwardAccessTmpTable.setRead_balance("Y");
					} else if (permi.equals(TranMonitorStatus.ReadAccountsDetails.toString())) {
						consentOutwardAccessTmpTable.setRead_acct_details("Y");
					} else if (permi.equals(TranMonitorStatus.ReadTransactionsDetails.toString())) {
						consentOutwardAccessTmpTable.setRead_tran_details("Y");
					} else if (permi.equals(TranMonitorStatus.DebitAccount.toString())) {
						consentOutwardAccessTmpTable.setRead_debit_acct("Y");
					}
				}
				
				consentOutwardAccessTmpTable.setExpire_date(listener.toDate(consentAccessResponse.getExpirationDateTime()));
				consentOutwardAccessTmpTable.setTran_from_date(listener.toDate(consentAccessResponse.getTransactionFromDateTime()));
				consentOutwardAccessTmpTable.setTran_to_date(listener.toDate(consentAccessResponse.getTransactionToDateTime()));
				
				consentOutwardAccessTmpTableRep.save(consentOutwardAccessTmpTable);


			}
		}catch(Exception e) {
			System.out.println(e.getLocalizedMessage());
		}
		
	}

	public void outwardConsentDataRegisterUpdateErrorResponse(String w_request_id,
			ErrorRestResponse errorRestResponse) {
		try{
			Optional <ConsentOutwardAccessTmpTable> data=consentOutwardAccessTmpTableRep.findById(w_request_id);
			if(data.isPresent()) {
				ConsentOutwardAccessTmpTable consentOutwardAccessTmpTable=data.get();
				consentOutwardAccessTmpTable.setAccount_status(errorRestResponse.getErrorCode()+":"+errorRestResponse.getDescription());
				consentOutwardAccessTmpTable.setStatus("");

				consentOutwardAccessTmpTable.setStatus_update_date(new Date());
				consentOutwardAccessTmpTableRep.save(consentOutwardAccessTmpTable);


			}
		}catch(Exception e) {
			System.out.println(e.getLocalizedMessage());
		}

		
	}

	public String outwardConsentDataRegisterAuth(String w_request_id, String psuDeviceID, String psuIPAddress,
			String psuID, String psuIDCountry, String psuIDType,
			ConsentOutwardAccessAuthRequest consentOutwardAccessAuthRequest, String sender_participant_bic,
			String sender_participant_member_id, String receiver_participant_bic, String receiver_participant_member_id,
			String consentID, String authID,String channelID) {
		
		String response="1";
		try {
			ConsentOutwardAuthorisationTable consentOutwardAuthorisationTable=new ConsentOutwardAuthorisationTable();
			consentOutwardAuthorisationTable.setX_request_id(w_request_id);
			consentOutwardAuthorisationTable.setSenderparticipant_bic(sender_participant_bic);
			consentOutwardAuthorisationTable.setSenderparticipant_memberid(sender_participant_member_id);
			consentOutwardAuthorisationTable.setReceiverparticipant_bic(receiver_participant_bic);
			consentOutwardAuthorisationTable.setReceiverparticipant_memberid(receiver_participant_member_id);
			consentOutwardAuthorisationTable.setPsu_device_id(psuDeviceID);
			consentOutwardAuthorisationTable.setPsu_ip_address(psuIPAddress);
			consentOutwardAuthorisationTable.setPsu_id(psuID);
			consentOutwardAuthorisationTable.setPsu_id_country(psuIDCountry);
			consentOutwardAuthorisationTable.setPsu_id_type(psuIDType);
			consentOutwardAuthorisationTable.setEntry_time(new Date());
			consentOutwardAuthorisationTable.setAuth_id(authID);
			consentOutwardAuthorisationTable.setConsent_id(consentID);
			consentOutwardAuthorisationTable.setPsu_channel(channelID);
			consentOutwardAuthorisationTableRep.save(consentOutwardAuthorisationTable);
			
			response="0";
		}catch(Exception E) {
			response="1";
		}
		
		return response;
	}

	public List<ConsentOutwardAccessTmpTable> getConsentTmpData(String consentID) {
		List<ConsentOutwardAccessTmpTable> list=consentOutwardAccessTmpTableRep.finByConsentID(consentID);
		return list;
	}

	public void outwardConsentDataRegisterAuthUpdateResponse(String w_request_id,
			String status) {
		try{
			Optional <ConsentOutwardAuthorisationTable> data=consentOutwardAuthorisationTableRep.findById(w_request_id);
			if(data.isPresent()) {
				ConsentOutwardAuthorisationTable consentOutwardAccessTmpTable=data.get();
				consentOutwardAccessTmpTable.setStatus(status);
				
				consentOutwardAuthorisationTableRep.save(consentOutwardAccessTmpTable);
			}
		}catch(Exception e) {
			System.out.println(e.getLocalizedMessage());

		}
		
	}
	
	public void outwardConsentDataRegisterAuthUpdateErrorResponse(String w_request_id,
			String status) {
		try{
			Optional <ConsentOutwardAuthorisationTable> data=consentOutwardAuthorisationTableRep.findById(w_request_id);
			if(data.isPresent()) {
				ConsentOutwardAuthorisationTable consentOutwardAccessTmpTable=data.get();
				consentOutwardAccessTmpTable.setStatus("Failure");
				consentOutwardAccessTmpTable.setAccount_status(status);
				
				consentOutwardAuthorisationTableRep.save(consentOutwardAccessTmpTable);
			}
		}catch(Exception e) {
			System.out.println(e.getLocalizedMessage());
		}
		
	}

	public ConsentOutwardAccessAuthResponse updateOutwardSCAAuth(String w_request_id, String psuDeviceID, String psuIPAddress, String psuID,
			String psuIDCountry, String psuIDType, ConsentOutwardAccessAuthRequest consentOutwardAccessAuthRequest,
			String sender_participant_bic, String sender_participant_member_id, String receiver_participant_bic,
			String receiver_participant_member_id, String consentID, String authID,SCAAthenticationResponse consentAccessResponse) {
		
		ConsentOutwardAccessAuthResponse response = null;
		
		
		List<ConsentOutwardAccessTmpTable> list=consentOutwardAccessTmpTableRep.finByConsentID(consentID);

		if(list.size()>0) {
			
			
			System.out.println("Data"+consentAccessResponse.toString());
			response = new ConsentOutwardAccessAuthResponse();

			response.setSCAStatus(consentAccessResponse.getSCAStatus());

			
			Links links = new Links();
			links.setSCAStatus("/api/ws/account-consents/" + consentID + "/authorisations/"+authID);

			response.setLinks(links);
			
			List<ConsentOutwardAccessTable> regAccList = consentOutwardAccessTableRep
					.getAccountNumber(list.get(0).getIdentification());
			
			if (regAccList.size() > 0) {
				
				System.out.println("Data"+consentAccessResponse.toString());
				
				for(ConsentOutwardAccessTable re:regAccList) {
					
					ConsentOutwardAccessTable regPublicKey1 = re;
					if(!regPublicKey1.getConsent_id().equals(consentID)) {
						regPublicKey1.setDel_flg("Y");
						regPublicKey1.setDel_time(new Date());
						consentOutwardAccessTableRep.save(regPublicKey1);
					}
					
				}
				ConsentOutwardAccessTable consentAccessID = new ConsentOutwardAccessTable();
				consentAccessID.setX_request_id(list.get(0).getX_request_id());
				consentAccessID.setConsent_id(list.get(0).getConsent_id());
				consentAccessID.setCustom_device_id(list.get(0).getCustom_device_id());
				consentAccessID.setSenderparticipant_bic(list.get(0).getSenderparticipant_bic());
				consentAccessID.setSenderparticipant_memberid(list.get(0).getSenderparticipant_memberid());
				consentAccessID.setReceiverparticipant_bic(list.get(0).getReceiverparticipant_bic());
				consentAccessID.setReceiverparticipant_memberid(list.get(0).getReceiverparticipant_memberid());
				consentAccessID.setPsu_device_id(list.get(0).getPsu_device_id());
				consentAccessID.setPsu_ip_address(list.get(0).getPsu_ip_address());
				consentAccessID.setPsu_id(list.get(0).getPsu_id());
				consentAccessID.setPsu_id_country(list.get(0).getPsu_id_country());
				consentAccessID.setPsu_id_type(list.get(0).getPsu_id_type());
				consentAccessID.setPhone_number(list.get(0).getPhone_number());
				consentAccessID.setPublic_key(list.get(0).getPublic_key());
				consentAccessID.setPrivate_key(list.get(0).getPrivate_key());
				consentAccessID.setExpire_date(list.get(0).getExpire_date());	
				consentAccessID.setTran_from_date(list.get(0).getTran_from_date());
				consentAccessID.setTran_to_date(list.get(0).getTran_to_date());
				consentAccessID.setSchm_name(list.get(0).getSchm_name());
				consentAccessID.setIdentification(list.get(0).getIdentification());
				consentAccessID.setPermission(list.get(0).getPermission());
				consentAccessID.setCreate_date(list.get(0).getCreate_date());
				consentAccessID.setStatus_update_date(list.get(0).getStatus_update_date());
				consentAccessID.setRead_balance(list.get(0).getRead_balance());
				consentAccessID.setRead_acct_details(list.get(0).getRead_acct_details());
				consentAccessID.setRead_tran_details(list.get(0).getRead_tran_details());
				consentAccessID.setRead_debit_acct(list.get(0).getRead_debit_acct());
				consentAccessID.setAcct_name(list.get(0).getAcct_name());
				consentAccessID.setDel_flg("N");
				consentAccessID.setPsu_channel(list.get(0).getPsu_channel());
				
				consentOutwardAccessTableRep.save(consentAccessID);
				

			}else {
				
				ConsentOutwardAccessTable consentAccessID = new ConsentOutwardAccessTable();
				consentAccessID.setX_request_id(list.get(0).getX_request_id());
				consentAccessID.setConsent_id(list.get(0).getConsent_id());
				consentAccessID.setCustom_device_id(list.get(0).getCustom_device_id());
				consentAccessID.setSenderparticipant_bic(list.get(0).getSenderparticipant_bic());
				consentAccessID.setSenderparticipant_memberid(list.get(0).getSenderparticipant_memberid());
				consentAccessID.setReceiverparticipant_bic(list.get(0).getReceiverparticipant_bic());
				consentAccessID.setReceiverparticipant_memberid(list.get(0).getReceiverparticipant_memberid());
				consentAccessID.setPsu_device_id(list.get(0).getPsu_device_id());
				consentAccessID.setPsu_ip_address(list.get(0).getPsu_ip_address());
				consentAccessID.setPsu_id(list.get(0).getPsu_id());
				consentAccessID.setPsu_id_country(list.get(0).getPsu_id_country());
				consentAccessID.setPsu_id_type(list.get(0).getPsu_id_type());
				consentAccessID.setPhone_number(list.get(0).getPhone_number());
				consentAccessID.setPublic_key(list.get(0).getPublic_key());
				consentAccessID.setPrivate_key(list.get(0).getPrivate_key());
				consentAccessID.setExpire_date(list.get(0).getExpire_date());	
				consentAccessID.setTran_from_date(list.get(0).getTran_from_date());
				consentAccessID.setTran_to_date(list.get(0).getTran_to_date());
				consentAccessID.setSchm_name(list.get(0).getSchm_name());
				consentAccessID.setIdentification(list.get(0).getIdentification());
				consentAccessID.setPermission(list.get(0).getPermission());
				consentAccessID.setCreate_date(list.get(0).getCreate_date());
				consentAccessID.setStatus_update_date(list.get(0).getStatus_update_date());
				consentAccessID.setRead_balance(list.get(0).getRead_balance());
				consentAccessID.setRead_acct_details(list.get(0).getRead_acct_details());
				consentAccessID.setRead_tran_details(list.get(0).getRead_tran_details());
				consentAccessID.setRead_debit_acct(list.get(0).getRead_debit_acct());
				consentAccessID.setAcct_name(list.get(0).getAcct_name());
				consentAccessID.setDel_flg("N");
				consentAccessID.setPsu_channel(list.get(0).getPsu_channel());
				consentOutwardAccessTableRep.save(consentAccessID);
			}
			ConsentOutwardAccessTmpTable consentOutwardAccessTmpTable=list.get(0);
			consentOutwardAccessTmpTable.setDel_flg("Y");
			consentOutwardAccessTmpTable.setDel_time(new Date());
			consentOutwardAccessTmpTableRep.save(consentOutwardAccessTmpTable);
			return response;
		}else {
			throw new IPSXRestException(errorCode.ErrorCodeRegistration("13"));
		}		
		
		
	}

	public void outwardConsentDataRegisterDeleteResponse(String consentID) {
		List<ConsentOutwardAccessTable> data=consentOutwardAccessTableRep.finByConsentID(consentID);
		
		if(data.size()>0) {
			ConsentOutwardAccessTable consetTable=data.get(0);
			consetTable.setDel_flg("Y");
			consetTable.setDel_time(new Date());
			consentOutwardAccessTableRep.save(consetTable);
		}
		
	}

	public List<ConsentOutwardAccessTable> getConsentData(String consentID) {
		List<ConsentOutwardAccessTable> list=consentOutwardAccessTableRep.finByConsentID(consentID);
		return list;
	}
	
	public List<Object[]> getConsentAccounts(String docID) {
		List<Object[]> list=consentOutwardAccessTableRep.finByDocID(docID);
		return list;
	}
	
	public String consentOutwardDataRegister(String x_request_id, String psuDeviceID, String psuIPAddress,
			String psuID, String psuIDCountry, String psuIDType, String sender_participant_bic,
			String sender_participant_member_id, String receiver_participant_bic, String receiver_participant_member_id,
			String consentID, String accountID) {
		String response="1";
		try {
			ConsentOutwardInquiryTable consentOutwardInquiryTable=new ConsentOutwardInquiryTable();
			consentOutwardInquiryTable.setX_request_id(x_request_id);
			consentOutwardInquiryTable.setSenderparticipant_bic(sender_participant_bic);
			consentOutwardInquiryTable.setSenderparticipant_memberid(sender_participant_member_id);
			consentOutwardInquiryTable.setReceiverparticipant_bic(receiver_participant_bic);
			consentOutwardInquiryTable.setReceiverparticipant_memberid(receiver_participant_member_id);
			consentOutwardInquiryTable.setPsu_device_id(psuDeviceID);
			consentOutwardInquiryTable.setPsu_ip_address(psuIPAddress);
			consentOutwardInquiryTable.setPsu_id(psuID);
			consentOutwardInquiryTable.setPsu_id_country(psuIDCountry);
			consentOutwardInquiryTable.setPsu_id_type(psuIDType);
			consentOutwardInquiryTable.setAccount_id(accountID);
			consentOutwardInquiryTable.setConsent_id(consentID);
			consentOutwardInquiryTable.setInquiry_type(TranMonitorStatus.Consent_creation.toString());
			consentOutwardInquiryTable.setEntry_time(new Date());
			
			consentOutwardInquiryTableRep.save(consentOutwardInquiryTable);
			
			response="0";
		}catch(Exception E) {
			response="1";
		}
		
		return response;
	}
	
	
	

	public String consentOutwardDataAuthorisation(String x_request_id, String psuDeviceID, String psuIPAddress,
			String psuID, String psuIDCountry, String psuIDType, String sender_participant_bic,
			String sender_participant_member_id, String receiver_participant_bic, String receiver_participant_member_id,
			String consentID, String accountID,String channelID) {
		String response="1";
		try {
			ConsentOutwardInquiryTable consentOutwardInquiryTable=new ConsentOutwardInquiryTable();
			consentOutwardInquiryTable.setX_request_id(x_request_id);
			consentOutwardInquiryTable.setSenderparticipant_bic(sender_participant_bic);
			consentOutwardInquiryTable.setSenderparticipant_memberid(sender_participant_member_id);
			consentOutwardInquiryTable.setReceiverparticipant_bic(receiver_participant_bic);
			consentOutwardInquiryTable.setReceiverparticipant_memberid(receiver_participant_member_id);
			consentOutwardInquiryTable.setPsu_device_id(psuDeviceID);
			consentOutwardInquiryTable.setPsu_ip_address(psuIPAddress);
			consentOutwardInquiryTable.setPsu_id(psuID);
			consentOutwardInquiryTable.setPsu_id_country(psuIDCountry);
			consentOutwardInquiryTable.setPsu_id_type(psuIDType);
			consentOutwardInquiryTable.setAccount_id(accountID);
			consentOutwardInquiryTable.setConsent_id(consentID);
			consentOutwardInquiryTable.setInquiry_type(TranMonitorStatus.ConsentAuthorisation.toString());
			consentOutwardInquiryTable.setEntry_time(new Date());
			consentOutwardInquiryTable.setPsu_channel(channelID);
			consentOutwardInquiryTableRep.save(consentOutwardInquiryTable);
			
			response="0";
		}catch(Exception E) {
			response="1";
		}
		
		return response;
	}
	
	public String consentOutwardDataDelete(String x_request_id, String psuDeviceID, String psuIPAddress,
			String psuID, String psuIDCountry, String psuIDType, String sender_participant_bic,
			String sender_participant_member_id, String receiver_participant_bic, String receiver_participant_member_id,
			String consentID, String accountID,String channelID) {
		String response="1";
		try {
			ConsentOutwardInquiryTable consentOutwardInquiryTable=new ConsentOutwardInquiryTable();
			consentOutwardInquiryTable.setX_request_id(x_request_id);
			consentOutwardInquiryTable.setSenderparticipant_bic(sender_participant_bic);
			consentOutwardInquiryTable.setSenderparticipant_memberid(sender_participant_member_id);
			consentOutwardInquiryTable.setReceiverparticipant_bic(receiver_participant_bic);
			consentOutwardInquiryTable.setReceiverparticipant_memberid(receiver_participant_member_id);
			consentOutwardInquiryTable.setPsu_device_id(psuDeviceID);
			consentOutwardInquiryTable.setPsu_ip_address(psuIPAddress);
			consentOutwardInquiryTable.setPsu_id(psuID);
			consentOutwardInquiryTable.setPsu_id_country(psuIDCountry);
			consentOutwardInquiryTable.setPsu_id_type(psuIDType);
			consentOutwardInquiryTable.setAccount_id(accountID);
			consentOutwardInquiryTable.setConsent_id(consentID);
			consentOutwardInquiryTable.setInquiry_type(TranMonitorStatus.ConsentDelete.toString());
			consentOutwardInquiryTable.setEntry_time(new Date());
			consentOutwardInquiryTable.setPsu_channel(channelID);
			
			consentOutwardInquiryTableRep.save(consentOutwardInquiryTable);
			
			response="0";
		}catch(Exception E) {
			response="1";
		}
		
		return response;
	}

	
	public String outwardConsentDataRegisterBalances(String x_request_id, String psuDeviceID, String psuIPAddress,
			String psuID, String psuIDCountry, String psuIDType, String sender_participant_bic,
			String sender_participant_member_id, String receiver_participant_bic, String receiver_participant_member_id,
			String consentID, String accountID,String channelID) {
		String response="1";
		try {
			ConsentOutwardInquiryTable consentOutwardInquiryTable=new ConsentOutwardInquiryTable();
			consentOutwardInquiryTable.setX_request_id(x_request_id);
			consentOutwardInquiryTable.setSenderparticipant_bic(sender_participant_bic);
			consentOutwardInquiryTable.setSenderparticipant_memberid(sender_participant_member_id);
			consentOutwardInquiryTable.setReceiverparticipant_bic(receiver_participant_bic);
			consentOutwardInquiryTable.setReceiverparticipant_memberid(receiver_participant_member_id);
			consentOutwardInquiryTable.setPsu_device_id(psuDeviceID);
			consentOutwardInquiryTable.setPsu_ip_address(psuIPAddress);
			consentOutwardInquiryTable.setPsu_id(psuID);
			consentOutwardInquiryTable.setPsu_id_country(psuIDCountry);
			consentOutwardInquiryTable.setPsu_id_type(psuIDType);
			consentOutwardInquiryTable.setAccount_id(accountID);
			consentOutwardInquiryTable.setConsent_id(consentID);
			consentOutwardInquiryTable.setInquiry_type(TranMonitorStatus.ReadBalances.toString());
			consentOutwardInquiryTable.setEntry_time(new Date());
			consentOutwardInquiryTable.setPsu_channel(channelID);
			
			consentOutwardInquiryTableRep.save(consentOutwardInquiryTable);
			
			response="0";
		}catch(Exception E) {
			response="1";
		}
		
		return response;
	}

	public void accountConsentBalancesResponse(String x_request_id, String status, String statuserror) {
		Optional<ConsentOutwardInquiryTable> data=consentOutwardInquiryTableRep.findById(x_request_id);
		
		if(data.isPresent()) {
			ConsentOutwardInquiryTable  consentOutwardInquiryTable=data.get();
			consentOutwardInquiryTable.setStatus(status);
			consentOutwardInquiryTable.setStatus_error(statuserror);
			consentOutwardInquiryTableRep.save(consentOutwardInquiryTable);
		}
		
	}


	public String outwardConsentDataRegisterTransactionInc(String x_request_id, String psuDeviceID, String psuIPAddress,
			String psuID, String psuIDCountry, String psuIDType, String sender_participant_bic,
			String sender_participant_member_id, String receiver_participant_bic, String receiver_participant_member_id,
			String consentID, String accountID,String channelID) {
		String response="1";
		try {
			ConsentOutwardInquiryTable consentOutwardInquiryTable=new ConsentOutwardInquiryTable();
			consentOutwardInquiryTable.setX_request_id(x_request_id);
			consentOutwardInquiryTable.setSenderparticipant_bic(sender_participant_bic);
			consentOutwardInquiryTable.setSenderparticipant_memberid(sender_participant_member_id);
			consentOutwardInquiryTable.setReceiverparticipant_bic(receiver_participant_bic);
			consentOutwardInquiryTable.setReceiverparticipant_memberid(receiver_participant_member_id);
			consentOutwardInquiryTable.setPsu_device_id(psuDeviceID);
			consentOutwardInquiryTable.setPsu_ip_address(psuIPAddress);
			consentOutwardInquiryTable.setPsu_id(psuID);
			consentOutwardInquiryTable.setPsu_id_country(psuIDCountry);
			consentOutwardInquiryTable.setPsu_id_type(psuIDType);
			consentOutwardInquiryTable.setAccount_id(accountID);
			consentOutwardInquiryTable.setConsent_id(consentID);
			consentOutwardInquiryTable.setInquiry_type(TranMonitorStatus.ReadTransactionsDetails.toString());
			consentOutwardInquiryTable.setEntry_time(new Date());
			consentOutwardInquiryTable.setPsu_channel(channelID);
			
			consentOutwardInquiryTableRep.save(consentOutwardInquiryTable);
			
			response="0";
		}catch(Exception E) {
			response="1";
		}
		
		return response;
	}
	
	public String outwardConsentDataRegisterAccountListInc(String x_request_id, String psuDeviceID, String psuIPAddress,
			String psuID, String psuIDCountry, String psuIDType, String sender_participant_bic,
			String sender_participant_member_id, String receiver_participant_bic, String receiver_participant_member_id,
			String consentID,String channelID) {
		String response="1";
		try {
			ConsentOutwardInquiryTable consentOutwardInquiryTable=new ConsentOutwardInquiryTable();
			consentOutwardInquiryTable.setX_request_id(x_request_id);
			consentOutwardInquiryTable.setSenderparticipant_bic(sender_participant_bic);
			consentOutwardInquiryTable.setSenderparticipant_memberid(sender_participant_member_id);
			consentOutwardInquiryTable.setReceiverparticipant_bic(receiver_participant_bic);
			consentOutwardInquiryTable.setReceiverparticipant_memberid(receiver_participant_member_id);
			consentOutwardInquiryTable.setPsu_device_id(psuDeviceID);
			consentOutwardInquiryTable.setPsu_ip_address(psuIPAddress);
			consentOutwardInquiryTable.setPsu_id(psuID);
			consentOutwardInquiryTable.setPsu_id_country(psuIDCountry);
			consentOutwardInquiryTable.setPsu_id_type(psuIDType);
			consentOutwardInquiryTable.setAccount_id("");
			consentOutwardInquiryTable.setConsent_id(consentID);
			consentOutwardInquiryTable.setInquiry_type(TranMonitorStatus.ReadAccountsDetails.toString());
			consentOutwardInquiryTable.setEntry_time(new Date());
			consentOutwardInquiryTable.setPsu_channel(channelID);
			
			consentOutwardInquiryTableRep.save(consentOutwardInquiryTable);
			
			response="0";
		}catch(Exception E) {
			response="1";
		}
		
		return response;
	}
	
	
	public String outwardConsentDataRegisterAccountInc(String x_request_id, String psuDeviceID, String psuIPAddress,
			String psuID, String psuIDCountry, String psuIDType, String sender_participant_bic,
			String sender_participant_member_id, String receiver_participant_bic, String receiver_participant_member_id,
			String consentID, String accountID,String channelID) {
		String response="1";
		try {
			ConsentOutwardInquiryTable consentOutwardInquiryTable=new ConsentOutwardInquiryTable();
			consentOutwardInquiryTable.setX_request_id(x_request_id);
			consentOutwardInquiryTable.setSenderparticipant_bic(sender_participant_bic);
			consentOutwardInquiryTable.setSenderparticipant_memberid(sender_participant_member_id);
			consentOutwardInquiryTable.setReceiverparticipant_bic(receiver_participant_bic);
			consentOutwardInquiryTable.setReceiverparticipant_memberid(receiver_participant_member_id);
			consentOutwardInquiryTable.setPsu_device_id(psuDeviceID);
			consentOutwardInquiryTable.setPsu_ip_address(psuIPAddress);
			consentOutwardInquiryTable.setPsu_id(psuID);
			consentOutwardInquiryTable.setPsu_id_country(psuIDCountry);
			consentOutwardInquiryTable.setPsu_id_type(psuIDType);
			consentOutwardInquiryTable.setAccount_id(accountID);
			consentOutwardInquiryTable.setConsent_id(consentID);
			consentOutwardInquiryTable.setInquiry_type(TranMonitorStatus.ReadAccountsDetails.toString());
			consentOutwardInquiryTable.setEntry_time(new Date());
			consentOutwardInquiryTable.setPsu_channel(channelID);
			
			consentOutwardInquiryTableRep.save(consentOutwardInquiryTable);
			
			response="0";
		}catch(Exception E) {
			response="1";
		}
		
		return response;
	}
	
	
	public String consenRegisterAccountData(String x_request_id, String psuDeviceID, String psuIPAddress,
			String psuID, String psuIDCountry, String psuIDType, String sender_participant_bic,
			String sender_participant_member_id, String receiver_participant_bic, String receiver_participant_member_id,
			String consentID, String accountID) {
		String response="1";
		try {
			ConsentOutwardInquiryTable consentOutwardInquiryTable=new ConsentOutwardInquiryTable();
			consentOutwardInquiryTable.setX_request_id(x_request_id);
			consentOutwardInquiryTable.setSenderparticipant_bic(sender_participant_bic);
			consentOutwardInquiryTable.setSenderparticipant_memberid(sender_participant_member_id);
			consentOutwardInquiryTable.setReceiverparticipant_bic(receiver_participant_bic);
			consentOutwardInquiryTable.setReceiverparticipant_memberid(receiver_participant_member_id);
			consentOutwardInquiryTable.setPsu_device_id(psuDeviceID);
			consentOutwardInquiryTable.setPsu_ip_address(psuIPAddress);
			consentOutwardInquiryTable.setPsu_id(psuID);
			consentOutwardInquiryTable.setPsu_id_country(psuIDCountry);
			consentOutwardInquiryTable.setPsu_id_type(psuIDType);
			consentOutwardInquiryTable.setAccount_id(accountID);
			consentOutwardInquiryTable.setConsent_id(consentID);
			consentOutwardInquiryTable.setInquiry_type("Consent_Account_List".toString());
			consentOutwardInquiryTable.setEntry_time(new Date());
			
			consentOutwardInquiryTableRep.save(consentOutwardInquiryTable);
			
			response="0";
		}catch(Exception E) {
			response="1";
		}
		
		return response;
	}


	


	public void accountConsentTransactionIncResponse(String x_request_id, String status, String statuserror) {
		Optional<ConsentOutwardInquiryTable> data=consentOutwardInquiryTableRep.findById(x_request_id);
		
		if(data.isPresent()) {
			ConsentOutwardInquiryTable  consentOutwardInquiryTable=data.get();
			consentOutwardInquiryTable.setStatus(status);
			consentOutwardInquiryTable.setStatus_error(statuserror);
			consentOutwardInquiryTableRep.save(consentOutwardInquiryTable);
		}
		
	}

	public String registerCIMcbsIncomingData(String requestUUID,String channelId,
			String serviveReqVersion,String serviceReqId,Date msgDate,String tranNumber,
			String initChannel,String initTranNumber,String postToCbs,
			String tran_type,String isReversal,String tran_numberFromCbs, String acctNumber, String trAmt, String currency,
			String seqUniqueID,  String debrAcctNumber,
			String debtAcctName,String tran_part_code,String debit_remarks,String credit_remarks,
			String resv_field1,String res_field2,Date valueDate,String settlType,
			String init_sub_tran_no,String error_code,String error_msg,String ipsMasterRefId) {
		
		String response="0";
		try {
			TranCimCBSTable tranCimCBSTable=new TranCimCBSTable();
			
			
			tranCimCBSTable.setSequence_unique_id(seqUniqueID);
			tranCimCBSTable.setRequest_uuid(requestUUID);
			tranCimCBSTable.setChannel_id(channelId);
			tranCimCBSTable.setService_request_version(serviveReqVersion);
			tranCimCBSTable.setService_request_id(serviceReqId);
			tranCimCBSTable.setMessage_date_time(msgDate);
			
			tranCimCBSTable.setTran_no(tranNumber);
			tranCimCBSTable.setInit_channel(initChannel);
			tranCimCBSTable.setInit_tran_no(initTranNumber);
			tranCimCBSTable.setPost_to_cbs(postToCbs);
			tranCimCBSTable.setTran_type(tran_type);
			tranCimCBSTable.setIsreversal(isReversal);
			tranCimCBSTable.setTran_no_from_cbs(tran_numberFromCbs);
			tranCimCBSTable.setCustomer_name(debtAcctName);
			tranCimCBSTable.setFrom_account_no(debrAcctNumber);
			tranCimCBSTable.setTo_account_no(acctNumber);
			tranCimCBSTable.setTran_amt(new BigDecimal(trAmt));
			tranCimCBSTable.setTran_date(new Date());
			tranCimCBSTable.setTran_currency(currency);
			tranCimCBSTable.setTran_particular_code(tran_part_code);
			tranCimCBSTable.setDebit_remarks(debit_remarks);
			tranCimCBSTable.setCredit_remarks(credit_remarks);
			tranCimCBSTable.setResv_field_1(resv_field1);
			tranCimCBSTable.setResv_field_2(res_field2);
			tranCimCBSTable.setValue_date(valueDate);
			tranCimCBSTable.setSettl_acct_type(settlType);
			tranCimCBSTable.setInit_sub_tran_no(init_sub_tran_no);
			tranCimCBSTable.setError_code(error_code);
			tranCimCBSTable.setError_msg(error_msg);
			tranCimCBSTable.setIps_master_ref_id(ipsMasterRefId);
			tranCimCBSTableRep.saveAndFlush(tranCimCBSTable);
			response="1";

		}catch(Exception e) {
			logger.info(e.getLocalizedMessage());
			response="0";
		}
		
		return response;
	}

	public String registerMerchantIncomingData(String requestUUID,Date msgDate,String tranNumber,
			String tran_type,String acctNumber, String trAmt, String currency,
			String seqUniqueID,  String debrAcctNumber,
			String debtAcctName,String tran_part_code,String debit_remarks,String credit_remarks,
			String resv_field1,String res_field2,Date valueDate,String Merchant_name) {
		
		String response="0";
		try {
			Partition_table_entity tranCimCBSTable=new Partition_table_entity();
			
			
			tranCimCBSTable.setMerchant_id(acctNumber);
			tranCimCBSTable.setMerchant_trading_name(Merchant_name);
			tranCimCBSTable.setTran_type(tran_type);
			tranCimCBSTable.setTran_date(valueDate);
			tranCimCBSTable.setValue_date(valueDate);
			tranCimCBSTable.setTran_id(seqUniqueID);

			tranCimCBSTable.setPart_tran_type(tran_part_code);
			tranCimCBSTable.setTran_ref_cur(currency);
			tranCimCBSTable.setTran_ref_cur_amt(new BigDecimal(trAmt));
			tranCimCBSTable.setTran_rate(new BigDecimal(1));
			tranCimCBSTable.setTran_amt_loc(new BigDecimal(trAmt));
			tranCimCBSTable.setTran_particular(credit_remarks);
			tranCimCBSTable.setTran_remarks(credit_remarks);
			tranCimCBSTable.setPart_tran_id(new BigDecimal(1));
			tranCimCBSTable.setTran_date(new Date());
			partitiontablerep.saveAndFlush(tranCimCBSTable);
			response="1";

		}catch(Exception e) {
			logger.info(e.getLocalizedMessage());
			response="0";
		}
		
		return response;
	}

	
	public void updateCIMcbsData(String requestUUID, String status, String statusCode, String message,String tranNoFromCBS) {
		
		tranCimCBSTableRep.updateCIMcbsData(requestUUID,status,statusCode,message,tranNoFromCBS,
				new SimpleDateFormat("dd-MMM-yyyy").format(new Date()));
		/*Optional<TranCimCBSTable> data=tranCimCBSTableRep.findByIdCustomReUUID(requestUUID);
		if(data.isPresent()) {
			TranCimCBSTable tranCimCBSTable=data.get();
			tranCimCBSTable.setStatus(status);
			tranCimCBSTable.setStatus_code(statusCode);
			tranCimCBSTable.setMessage(message);
			tranCimCBSTable.setTran_no_from_cbs(tranNoFromCBS);
			tranCimCBSTable.setMessage_res_time(new Date());
			tranCimCBSTableRep.save(tranCimCBSTable);
		}*/
	}

	public boolean invalidConsentX_request_ID(String x_request_id) {
		boolean valid = false;
		try {
			List<Object[]> otm = consentOutwardAccessTmpTableRep.existsByX_Request_ID(x_request_id);

			if (otm.size()>0) {
				valid = false;
			} else {
				valid = true;
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		return valid;
	}
	

	public boolean invalidConsentInqX_request_ID(String x_request_id) {
		boolean valid = false;
		try {
			List<Object[]> otm = consentOutwardInquiryTableRep.existsByX_Request_ID(x_request_id);

			if (otm.size()>0) {
				valid = false;
			} else {
				valid = true;
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		return valid;
	}

	public void updateAuditTranID(String sysTraceNumber008, String endToEndID008) {
		
		outwardTranRep.updateAuditTranID(endToEndID008,sysTraceNumber008);
		/*List<OutwardTransactionMonitoringTable> data1=outwardTranRep.getExistData(endToEndID008);
		if(data1.size()>0) {
			OutwardTransactionMonitoringTable data2=data1.get(0);
			data2.setTran_audit_number(sysTraceNumber008);
			outwardTranRep.save(data2);
		}*/
		
	}
	
	public List<OutwardTransactionMonitoringTable> checkExistOutwardRTP(String instrID, String endToEndID) {
		List<OutwardTransactionMonitoringTable> data1=outwardTranRep.getRTPIncomindCreditExist(instrID,endToEndID);
		return data1;
	}

	public List<MerchantMaster> checkMerchantAcct(String Acctnum) {
		List<MerchantMaster> data1=merchantmasterrep.checkexistingcurrency(Acctnum);
		return data1;
	}
	
	public boolean checkConvenienceFeeValidation(CIMMerchantDirectFndRequest mcCreditTransferRequest) {
		
		/*if (mcCreditTransferRequest.getMerchantAccount().isConvenienceIndicator()) {
				if (!String.valueOf(mcCreditTransferRequest.getMerchantAccount().getConvenienceIndicatorFeeType())
						.equals("null")
						&& !String.valueOf(mcCreditTransferRequest.getMerchantAccount().getConvenienceIndicatorFeeType())
								.equals("")) {
					if (String.valueOf(mcCreditTransferRequest.getMerchantAccount().getConvenienceIndicatorFeeType())
							.equals("Fixed")||String.valueOf(mcCreditTransferRequest.getMerchantAccount().getConvenienceIndicatorFeeType())
							.equals("Percentage")) {
						if (!String.valueOf(mcCreditTransferRequest.getMerchantAccount().getConvenienceIndicatorFee())
								.equals("null")
								&& !String.valueOf(mcCreditTransferRequest.getMerchantAccount().getConvenienceIndicatorFee())
										.equals("")) {
							if (String.valueOf(mcCreditTransferRequest.getMerchantAccount().getConvenienceIndicatorFeeType())
									.equals("Fixed")) {
								if(String.valueOf(mcCreditTransferRequest.getMerchantAccount().getConvenienceIndicatorFeeType()).length()<=15){
									return true;

								}else {
									throw new IPSXException(errorCode.validationError("BIPS15-5"));
								}
							} else {
								if (String.valueOf(
										mcCreditTransferRequest.getMerchantAccount().getConvenienceIndicatorFeeType())
										.length() <= 5) {
									return true;

								} else {
									throw new IPSXException(errorCode.validationError("BIPS15-6"));
								}
							}
							
						}else {
							throw new IPSXException(errorCode.validationError("BIPS15-4"));
						}
						
					}else {
						throw new IPSXException(errorCode.validationError("BIPS15-3"));
					}
				}else {
					throw new IPSXException(errorCode.validationError("BIPS15-2"));
				}
			}else {
				return true;
			}*/
	
		if(!String.valueOf(mcCreditTransferRequest.getMerchantAccount().getTipOrConvenienceIndicator()).equals("null")&&
				!String.valueOf(mcCreditTransferRequest.getMerchantAccount().getTipOrConvenienceIndicator()).equals("")) {
			if(mcCreditTransferRequest.getMerchantAccount().getTipOrConvenienceIndicator().equals("01")||
					mcCreditTransferRequest.getMerchantAccount().getTipOrConvenienceIndicator().equals("02")||
					mcCreditTransferRequest.getMerchantAccount().getTipOrConvenienceIndicator().equals("03")) {
				
				if(!mcCreditTransferRequest.getMerchantAccount().getTipOrConvenienceIndicator().equals("01")) {
					if (!String.valueOf(mcCreditTransferRequest.getMerchantAccount().getConvenienceIndicatorFee())
							.equals("null")
							&& !String.valueOf(mcCreditTransferRequest.getMerchantAccount().getConvenienceIndicatorFee())
									.equals("")) {
						if (String.valueOf(mcCreditTransferRequest.getMerchantAccount().getTipOrConvenienceIndicator())
								.equals("02")) {
							if(String.valueOf(mcCreditTransferRequest.getMerchantAccount().getConvenienceIndicatorFee()).length()<=15){
								return true;

							}else {
								throw new IPSXException(errorCode.validationError("BIPS15-5"));
							}
						} else {
							if (String.valueOf(
									mcCreditTransferRequest.getMerchantAccount().getConvenienceIndicatorFee())
									.length() <= 5) {
								return true;

							} else {
								throw new IPSXException(errorCode.validationError("BIPS15-6"));
							}
						}
						
					}else {
						throw new IPSXException(errorCode.validationError("BIPS15-4"));
					}
				}else {
					return true;
				}

				
			}else {
				throw new IPSXException(errorCode.validationError("BIPS15-9"));
			}
		}else {
			return true;
		}

			
	

	}
	
	

	public boolean checkConvenienceFeeValidationQR(CIMMerchantQRcodeRequest mcCreditTransferRequest) {
		
		/*if (mcCreditTransferRequest.isConvenienceIndicator()) {
				if (!String.valueOf(mcCreditTransferRequest.getConvenienceIndicatorFeeType())
						.equals("null")
						&& !String.valueOf(mcCreditTransferRequest.getConvenienceIndicatorFeeType())
								.equals("")) {
					if (String.valueOf(mcCreditTransferRequest.getConvenienceIndicatorFeeType())
							.equals("Fixed")||String.valueOf(mcCreditTransferRequest.getConvenienceIndicatorFeeType())
							.equals("Percentage")) {
						if (!String.valueOf(mcCreditTransferRequest.getConvenienceIndicatorFee())
								.equals("null")
								&& !String.valueOf(mcCreditTransferRequest.getConvenienceIndicatorFee())
										.equals("")) {
							if (String.valueOf(mcCreditTransferRequest.getConvenienceIndicatorFeeType())
									.equals("Fixed")) {
								if(String.valueOf(mcCreditTransferRequest.getConvenienceIndicatorFee()).length()<=15){
									return true;

								}else {
									throw new IPSXException(errorCode.validationError("BIPS15-5"));
								}
							} else {
								if (String.valueOf(
										mcCreditTransferRequest.getConvenienceIndicatorFee())
										.length() <= 5) {
									return true;

								} else {
									throw new IPSXException(errorCode.validationError("BIPS15-6"));
								}
							}
							
						}else {
							throw new IPSXException(errorCode.validationError("BIPS15-4"));
						}
						
					}else {
						throw new IPSXException(errorCode.validationError("BIPS15-3"));
					}
				}else {
					throw new IPSXException(errorCode.validationError("BIPS15-2"));
				}
			}else {
				return true;
			}*/

			
		if(!String.valueOf(mcCreditTransferRequest.getTipOrConvenienceIndicator()).equals("null")&&
				!String.valueOf(mcCreditTransferRequest.getTipOrConvenienceIndicator()).equals("")) {
			if(mcCreditTransferRequest.getTipOrConvenienceIndicator().equals("01")||
					mcCreditTransferRequest.getTipOrConvenienceIndicator().equals("02")||
					mcCreditTransferRequest.getTipOrConvenienceIndicator().equals("03")) {
				
				if(!mcCreditTransferRequest.getTipOrConvenienceIndicator().equals("01")) {
					if (!String.valueOf(mcCreditTransferRequest.getConvenienceIndicatorFee())
							.equals("null")
							&& !String.valueOf(mcCreditTransferRequest.getConvenienceIndicatorFee())
									.equals("")) {
						if (String.valueOf(mcCreditTransferRequest.getTipOrConvenienceIndicator())
								.equals("02")) {
							if(String.valueOf(mcCreditTransferRequest.getConvenienceIndicatorFee()).length()<=15){
								return true;

							}else {
								throw new IPSXException(errorCode.validationError("BIPS15-5"));
							}
						} else {
							if (String.valueOf(
									mcCreditTransferRequest.getConvenienceIndicatorFee())
									.length() <= 5) {
								return true;

							} else {
								throw new IPSXException(errorCode.validationError("BIPS15-6"));
							}
						}
						
					}else {
						throw new IPSXException(errorCode.validationError("BIPS15-4"));
					}
				}else {
					return true;
				}

				
			}else {
				throw new IPSXException(errorCode.validationError("BIPS15-9"));
			}
		}else {
			return true;
		}
	

	}

	public boolean invaliQRdBankCode(String payeeParticipantCode) {
		boolean valid = true;
		try {

			if (payeeParticipantCode.equals(env.getProperty("ipsx.qrPartiipant"))) {
				
				valid = false;
			} else {
				valid = true;
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		return valid;
	}

	public boolean checkExistConsent(String benAcctNumber) {
		boolean isData=false;
		try {
			List<ConsentOutwardAccessTable>  list=consentOutwardAccessTableRep.getAccountNumber(benAcctNumber);
			if(list.size()>0) {
				isData=true;
			}else {
				isData=false;
			}
		}catch(Exception e) {
			isData=false;
		}
		// TODO Auto-generated method stub
		return isData;
	}

	public void updateMxMsg(String ame) {

		List<TranIPSTable> list=tranIPStableRep.findByIdCustom("M213265/002");
		TranIPSTable tr=list.get(0);
		tr.setMx_msg(ame);
		tranIPStableRep.save(tr);
	}

	public void getOutwardTranData(String sequence_unique_id,Date date) {
		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		String currentDate = dateFormat.format(new Date());
		String tranDate = dateFormat.format(date);
		
		if (tranDate.equals(currentDate)) {
			List<OutwardTransactionMonitoringTable> list=outwardTranRep.getExistData(sequence_unique_id);
			
		}else {
			List<OutwardTransactionMonitoringTable> list=outwardTranHistRep.getExistData(sequence_unique_id);
		}
		
	}

	public TranCimCBSTable getTranCIMcbsData(String tran_no) {
		TranCimCBSTable cbsTable=tranCimCBSTableRep.getTranData(tran_no);
		return cbsTable;
	}

	public void updateSettlAmtTable(TranCimCBSTable cbsData) {
		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		String currentDate = dateFormat.format(new Date());
		String previousDate = dateFormat.format(previousDay());
		String tranDate = dateFormat.format(cbsData.getTran_date());
		
		if (!tranDate.equals(currentDate)) {
			Optional<SettlementAccountAmtTable> data=settlAcctAmtRep.customfindById(previousDate);
			
			if(data.isPresent()) {
				if(cbsData.getTran_type().equals("DR")) {
					
					String currentAmount=String.valueOf((Double.parseDouble(data.get().getPayable_acct_bal().toString())-
							Double.parseDouble(cbsData.getTran_amt().toString())));
					settlAcctAmtRep.updatePayableAmount(previousDate, currentAmount);
				}else if(cbsData.getTran_type().equals("CR")) {
					String currentAmount=String.valueOf((Double.parseDouble(data.get().getReceivable_acct_bal().toString())-
							Double.parseDouble(cbsData.getTran_amt().toString())));
					settlAcctAmtRep.updateReceivableAmount(previousDate, currentAmount);
				}
			}
		}
		
	}

	public String regMerchantQR(String p_id, String psuDeviceID, String psuIpAddress, String channelID,
			CIMMerchantQRcodeRequest qrrequest) {
		String status="0";
		try {
			MerchantQrGenTable merchantQrGenTable=new MerchantQrGenTable();
			merchantQrGenTable.setP_id(p_id);
			merchantQrGenTable.setPsu_device_id(psuDeviceID);
			merchantQrGenTable.setPsu_ip_address(psuIpAddress);
			merchantQrGenTable.setPsu_channel(channelID);
			
			merchantQrGenTable.setPayload_format_indicator(qrrequest.getPayloadFormatIndiator());
			merchantQrGenTable.setPoi_method(qrrequest.getPointOfInitiationFormat());
			merchantQrGenTable.setGlobal_unique_id(qrrequest.getMerchantAcctInformation().getGlobalID());
			merchantQrGenTable.setPayee_participant_code(qrrequest.getMerchantAcctInformation().getPayeeParticipantCode());
			merchantQrGenTable.setMerchant_acct_no(qrrequest.getMerchantAcctInformation().getMerchantAcctNumber());
			merchantQrGenTable.setMerchant_id(qrrequest.getMerchantAcctInformation().getMerchantID());
			merchantQrGenTable.setMerchant_category_code(qrrequest.getMCC());
			merchantQrGenTable.setTransaction_crncy(qrrequest.getCurrency());
			merchantQrGenTable.setTip_or_conv_indicator(qrrequest.getTipOrConvenienceIndicator());
			merchantQrGenTable.setValue_conv_fees(qrrequest.getConvenienceIndicatorFee());
			merchantQrGenTable.setCountry(qrrequest.getCountryCode());
			merchantQrGenTable.setMerchant_name(qrrequest.getMerchantName());
			merchantQrGenTable.setCity(qrrequest.getCity());
			merchantQrGenTable.setZip_code(qrrequest.getPostalCode());
			merchantQrGenTable.setBill_number(qrrequest.getAdditionalDataInformation().getBillNumber());
			merchantQrGenTable.setMobile(qrrequest.getAdditionalDataInformation().getMobileNumber());
			merchantQrGenTable.setReference_label(qrrequest.getAdditionalDataInformation().getReferenceLabel());
			merchantQrGenTable.setCustomer_label(qrrequest.getAdditionalDataInformation().getCustomerLabel());
			merchantQrGenTable.setTerminal_label(qrrequest.getAdditionalDataInformation().getTerminalLabel());
			merchantQrGenTable.setPurpose_of_tran(qrrequest.getAdditionalDataInformation().getPurposeOfTransaction());
			merchantQrGenTable.setStore_label(qrrequest.getAdditionalDataInformation().getStoreLabel());
			merchantQrGenTable.setLoyalty_number(qrrequest.getAdditionalDataInformation().getLoyaltyNumber());
			merchantQrGenTable.setAdditional_details(qrrequest.getAdditionalDataInformation().getAddlDataRequest());
			
			mercantQrGenTableRep.save(merchantQrGenTable);
			status="1";
		}catch(Exception e){
			status="0";
		}
		return status;
		
	}

	public void updateMerchantQRData(String p_id, String status, String reason) {
		Optional<MerchantQrGenTable> data=mercantQrGenTableRep.findById(p_id);
		if(data.isPresent()) {
			MerchantQrGenTable subData=data.get();
			subData.setStatus(status);
			if(status.equals("SUCCESS")) {
				byte[] decodedBytesQR = Base64.getDecoder().decode(reason);
				subData.setQr_code(decodedBytesQR);
			}else {
				subData.setReason(reason);
			}
			
			mercantQrGenTableRep.save(subData);
		}
		
	}

	public boolean checkBankAgentExistIncomingMsg(String debtorAgent008) {
		boolean status =false;
		
		try {
			Optional<BankAgentTable> data=bankAgentTableRep.findByCustomBankName(debtorAgent008);
			if(data.isPresent()) {
				if(!String.valueOf(data.get().getDel_flg()).equals("Y")&&!String.valueOf(data.get().getDisable_flg()).equals("Y")) {
					status =true;
					return status;
				}else {
					status =false;
					return status;
				}
			}else {
				status =false;
				return status;
			}
		}catch(Exception e) {
			e.printStackTrace();
			status=false;
		}
		return false;
	}
	
	
	public boolean checkBankAgentExistIncomingMsg1(String instgAgt,String debtorAgent,String instdAgt) {
		boolean status =false;
		
		try {
			List<Object[]> data=bankAgentTableRep.findByCustomBankName1(instgAgt,debtorAgent,instdAgt);
			if(data.size()==0) {
				status =true;
				return status;
			}else {
				status =false;
				return status;
			}
		}catch(Exception e) {
			e.printStackTrace();
			status=false;
		}
		return false;
	}


	@SuppressWarnings("unchecked")
	public boolean checkReqUniqueId(RTPbulkTransferRequest rtpBulkTransferRequest) {
		
		return rtpBulkTransferRequest.getBenAccount()
        .stream()
        .filter(distinctByKeys(BenAccount::getReqUniqueId))
        .collect(Collectors.toList()).size()==rtpBulkTransferRequest.getBenAccount().size();
		
	}
	
	private static <T> Predicate<T> distinctByKeys(@SuppressWarnings("unchecked") Function<? super T, ?>... keyExtractors) 
	  {
	    final Map<List<?>, Boolean> seen = new ConcurrentHashMap<>();
	     
	    return t -> 
	    {
	      final List<?> keys = Arrays.stream(keyExtractors)
	                  .map(ke -> ke.apply(t))
	                  .collect(Collectors.toList());
	       
	      return seen.putIfAbsent(keys, Boolean.TRUE) == null;
	    };
	  }
	


	public boolean invalidConsentInwInqX_request_ID(String x_request_id) {
		boolean valid = false;
		try {
			List<Object[]> otm = consentAccessInquiryTableRep.existsByX_Request_ID(x_request_id);

			if (otm.size()>0) {
				valid = false;
			} else {
				valid = true;
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		return valid;
	}
	
	
	public boolean checkBankAgentExistConsentMsg(String debtorAgent008) {
		boolean status =false;
		
		try {
			Optional<BankAgentTable> data=bankAgentTableRep.findByCustomBankName(debtorAgent008);
			if(data.isPresent()) {
				if(!data.get().getBank_agent().equals(env.getProperty("ipsx.bicfi"))&&!data.get().getDel_flg().equals("Y")&&!data.get().getDisable_flg().equals("Y")) {
					status =true;
					return status;
				}else {
					status =false;
					return status;
				}
			}else {
				status =false;
				return status;
			}
		}catch(Exception e) {
			e.printStackTrace();
			status=false;
		}
		return false;
	}

	public String consentDataRegister(String x_request_id, String psuDeviceID, String psuIPAddress,
			String psuID, String psuIDCountry, String psuIDType, String sender_participant_bic,
			String sender_participant_member_id, String receiver_participant_bic, String receiver_participant_member_id,
			String consentID, String accountID) {
		String response="1";
		try {
			ConsentAccessInquiryTable consentOutwardInquiryTable=new ConsentAccessInquiryTable();
			consentOutwardInquiryTable.setX_request_id(x_request_id);
			consentOutwardInquiryTable.setSenderparticipant_bic(sender_participant_bic);
			consentOutwardInquiryTable.setSenderparticipant_memberid(sender_participant_member_id);
			consentOutwardInquiryTable.setReceiverparticipant_bic(receiver_participant_bic);
			consentOutwardInquiryTable.setReceiverparticipant_memberid(receiver_participant_member_id);
			consentOutwardInquiryTable.setPsu_device_id(psuDeviceID);
			consentOutwardInquiryTable.setPsu_ip_address(psuIPAddress);
			consentOutwardInquiryTable.setPsu_id(psuID);
			consentOutwardInquiryTable.setPsu_id_country(psuIDCountry);
			consentOutwardInquiryTable.setPsu_id_type(psuIDType);
			consentOutwardInquiryTable.setAccount_id(accountID);
			consentOutwardInquiryTable.setConsent_id(consentID);
			consentOutwardInquiryTable.setInquiry_type(TranMonitorStatus.Consent_creation.toString());
			consentOutwardInquiryTable.setEntry_time(new Date());
			
			consentAccessInquiryTableRep.save(consentOutwardInquiryTable);
			
			response="0";
		}catch(Exception E) {
			response="1";
		}
		
		return response;
	}
	
	
	public void updateconsentPublicKeyAccountStatus(String authID, String accountStatus) {
		Optional<ConsentAccessTmpTable> otm = consentAccessTmpTableRep.findById(authID);
		if (otm.isPresent()) {
			ConsentAccessTmpTable regPublicKeyTmp = otm.get();
			regPublicKeyTmp.setAccount_status(accountStatus);
			consentAccessTmpTableRep.save(regPublicKeyTmp);
		}
	}
	
	public String consentDataAuthorisation(String x_request_id, String psuDeviceID, String psuIPAddress,
			String psuID, String psuIDCountry, String psuIDType, String sender_participant_bic,
			String sender_participant_member_id, String receiver_participant_bic, String receiver_participant_member_id,
			String consentID, String accountID) {
		String response="1";
		try {
			ConsentAccessInquiryTable consentOutwardInquiryTable=new ConsentAccessInquiryTable();
			consentOutwardInquiryTable.setX_request_id(x_request_id);
			consentOutwardInquiryTable.setSenderparticipant_bic(sender_participant_bic);
			consentOutwardInquiryTable.setSenderparticipant_memberid(sender_participant_member_id);
			consentOutwardInquiryTable.setReceiverparticipant_bic(receiver_participant_bic);
			consentOutwardInquiryTable.setReceiverparticipant_memberid(receiver_participant_member_id);
			consentOutwardInquiryTable.setPsu_device_id(psuDeviceID);
			consentOutwardInquiryTable.setPsu_ip_address(psuIPAddress);
			consentOutwardInquiryTable.setPsu_id(psuID);
			consentOutwardInquiryTable.setPsu_id_country(psuIDCountry);
			consentOutwardInquiryTable.setPsu_id_type(psuIDType);
			consentOutwardInquiryTable.setAccount_id(accountID);
			consentOutwardInquiryTable.setConsent_id(consentID);
			consentOutwardInquiryTable.setInquiry_type(TranMonitorStatus.ConsentAuthorisation.toString());
			consentOutwardInquiryTable.setEntry_time(new Date());
			
			consentAccessInquiryTableRep.save(consentOutwardInquiryTable);
			
			response="0";
		}catch(Exception E) {
			response="1";
		}
		
		return response;
	}
	
	public String consentDataDelete(String x_request_id, String psuDeviceID, String psuIPAddress,
			String psuID, String psuIDCountry, String psuIDType, String sender_participant_bic,
			String sender_participant_member_id, String receiver_participant_bic, String receiver_participant_member_id,
			String consentID, String accountID) {
		String response="1";
		try {
			ConsentAccessInquiryTable consentOutwardInquiryTable=new ConsentAccessInquiryTable();
			consentOutwardInquiryTable.setX_request_id(x_request_id);
			consentOutwardInquiryTable.setSenderparticipant_bic(sender_participant_bic);
			consentOutwardInquiryTable.setSenderparticipant_memberid(sender_participant_member_id);
			consentOutwardInquiryTable.setReceiverparticipant_bic(receiver_participant_bic);
			consentOutwardInquiryTable.setReceiverparticipant_memberid(receiver_participant_member_id);
			consentOutwardInquiryTable.setPsu_device_id(psuDeviceID);
			consentOutwardInquiryTable.setPsu_ip_address(psuIPAddress);
			consentOutwardInquiryTable.setPsu_id(psuID);
			consentOutwardInquiryTable.setPsu_id_country(psuIDCountry);
			consentOutwardInquiryTable.setPsu_id_type(psuIDType);
			consentOutwardInquiryTable.setAccount_id(accountID);
			consentOutwardInquiryTable.setConsent_id(consentID);
			consentOutwardInquiryTable.setInquiry_type(TranMonitorStatus.ConsentDelete.toString());
			consentOutwardInquiryTable.setEntry_time(new Date());
			
			consentAccessInquiryTableRep.save(consentOutwardInquiryTable);
			
			response="0";
		}catch(Exception E) {
			response="1";
		}
		
		return response;
	}

	public String registerCIMGLPaymentData(String requestUUID, String channelID, String servicerequestVersion, String serviceReqId,
			String countryCode, String tranNo, String batchNo, String module,Date date,BigDecimal amount,String glDate) {
		
		String status="0";
		try {
			///Get BOM MACSS Settlement Account	
			Optional<SettlementAccount> settlAcct=settlAccountRep.findById(env.getProperty("settl.macssSettl"));
			
			///Get CashClrAccount
			Optional<SettlementAccount> settlAcctCashClr=settlAccountRep.findById(env.getProperty("settl.cashClr"));


				TranCimGLTable tranCimGlTable=new TranCimGLTable();
				
				tranCimGlTable.setRequest_uuid(requestUUID);
				tranCimGlTable.setChannel_id(channelID);
				tranCimGlTable.setService_request_version(servicerequestVersion);
				tranCimGlTable.setService_request_id(serviceReqId);
				tranCimGlTable.setCountry_code(countryCode);
				tranCimGlTable.setMessage_date_time(date);
				
				tranCimGlTable.setTran_no(tranNo);
				tranCimGlTable.setBatch_no(batchNo);
				tranCimGlTable.setModule(module);
				
				tranCimGlTable.setSrl_no1("1");
				tranCimGlTable.setTran_type1(settlAcct.get().getTran_type());
				tranCimGlTable.setAcct_no1(settlAcct.get().getAccount_number());
				tranCimGlTable.setAcct_type1(settlAcct.get().getAcct_type());
				tranCimGlTable.setTran_amt1(amount);
				tranCimGlTable.setCurrency_code1(settlAcct.get().getCrncy());
				tranCimGlTable.setPosting_date1(date);
				tranCimGlTable.setTran_code1(settlAcct.get().getTran_code());
				tranCimGlTable.setTran_desc1("CT  BIPS - "+new SimpleDateFormat("ddMMyy").format(new Date()));
				tranCimGlTable.setTran_remarks1("Dr BOM MACSS Settlement Account");
				tranCimGlTable.setRate1("0");
				tranCimGlTable.setValue_date(new SimpleDateFormat("dd-MMM-yyyy").parse(glDate));
				
				
				tranCimGLRep.saveAndFlush(tranCimGlTable);
				
				TranCimGLTable tranCimGlTable1=new TranCimGLTable();
				
				tranCimGlTable1.setRequest_uuid(requestUUID);
				tranCimGlTable1.setChannel_id(channelID);
				tranCimGlTable1.setService_request_version(servicerequestVersion);
				tranCimGlTable1.setService_request_id(serviceReqId);
				tranCimGlTable1.setCountry_code(countryCode);
				tranCimGlTable1.setMessage_date_time(date);
				
				tranCimGlTable1.setTran_no(tranNo);
				tranCimGlTable1.setBatch_no(batchNo);
				tranCimGlTable1.setModule(module);
				
				tranCimGlTable1.setSrl_no1("2");
				tranCimGlTable1.setTran_type1(settlAcctCashClr.get().getTran_type());
				tranCimGlTable1.setAcct_no1(settlAcctCashClr.get().getAccount_number());
				tranCimGlTable1.setAcct_type1(settlAcctCashClr.get().getAcct_type());
				tranCimGlTable1.setTran_amt1(amount.negate());
				tranCimGlTable1.setCurrency_code1(settlAcctCashClr.get().getCrncy());
				tranCimGlTable1.setPosting_date1(date);
				tranCimGlTable1.setTran_code1(settlAcctCashClr.get().getTran_code());
				tranCimGlTable1.setTran_desc1("CT  BIPS - "+new SimpleDateFormat("ddMMyy").format(new Date()));
				tranCimGlTable1.setTran_remarks1("Cr Cash Clearing");
				tranCimGlTable1.setRate1("0");
				tranCimGlTable1.setValue_date(new SimpleDateFormat("dd-MMM-yyyy").parse(glDate));
				
				tranCimGLRep.saveAndFlush(tranCimGlTable1);
				
				status="1";

		}catch(Exception e) {
			System.out.println(e.getMessage());
			status="0";
		}
		return status;
		
	}

	public void updateCIMGlData(String requestUUID, String status, String statusCode, String message) {
		tranCimGLRep.updateGlResponse(requestUUID,status,statusCode,message);
	}

	public void updateSetleAmtTable(String glDate) {
		
		Optional<SettlementAccountAmtTable> settlAmtOpt = settlAcctAmtRep
				.customfindById(glDate);
		
		SettlementAccountAmtTable settlAmt = settlAmtOpt.get();
		settlAmt.setReceivable_flg("Y");
		settlAmt.setReceivable_verify_user("SYSTEM");
		settlAmt.setReceivable_verify_time(new Date());
		settlAcctAmtRep.save(settlAmt);
		
	}

	public void updateNotionalBal(String glDate,BigDecimal amount) {
		
	////update Receivablee Balance
		settlAccountRep.updateGLNotBalReceivable(env.getProperty("settl.receivable"),amount);
		
	////update Settelement Balance
		settlAccountRep.updateGLNotBalSettlement(env.getProperty("settl.settlment"),amount);
		
	}
	
	
	public String getCtgyPurp(String instgAgent, String debtorAgent, String creditorAgent,
			String debtorName,String creditorName,boolean isRegiteredPISP,String remitterDocNumber,
			String benAcctNumber) {

		String ctgyPurp = "";

		if (instgAgent.equals(creditorAgent)) {
			
			//String getBenDocNumber=ipsConnection.getDocTypeNumber(benAcctNumber);
			ctgyPurp = "101";
			/*if(getBenDocNumber.equals("0")) {
				ctgyPurp = "0";
			}else if(remitterDocNumber.equals(getBenDocNumber)){
				ctgyPurp = "101";
			}else if(!remitterDocNumber.equals(getBenDocNumber)) {
				ctgyPurp = "103";
			}*/
			/*if(debtorName.toLowerCase().equals(creditorName.toLowerCase())) {
				ctgyPurp = "101";
			}else {
				ctgyPurp = "103";
			}*/
		} else {
			if (debtorAgent.equals(creditorAgent)) {
				ctgyPurp = "103";
			} else {
				if(isRegiteredPISP) {
					ctgyPurp = "102";
				}else {
					ctgyPurp = "103";
				}
				
			}
		}
		return ctgyPurp;
	}

	public String initiateTranToCIM(String creditorAccount008, String creditorAccountName008, String trAmount008S,
			String trCurrency008, String sysTraceNumber008, String seqUniqueID008, String othBankCode, SendT request,
			String debtorAccount008, String debtorAccountName008, String instgAgtPacs008, String ctgy_purp_pacs008,
			String rmt_info_pacs008, String instr_id_pacs008, String endToEndID008) {
		
	   String tranResponse =errorCode.ErrorCode("00");
       Connection conn=informixCon.getConnection();
		
		try {
			
		     CallableStatement cstmt = conn.prepareCall("{call cimsp_insert_dig_payment_ips(?, ? ,?, ?,?,?,?,?,?,? )}");
		     cstmt.setString(1, creditorAccountName008);
		     cstmt.setString(2, sysTraceNumber008);
		     cstmt.setString(3, creditorAccount008);
		     cstmt.setBigDecimal(4, new BigDecimal(trAmount008S));
		     cstmt.setString(5, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		     cstmt.setString(6, new SimpleDateFormat("HH:mm").format(new Date()));
		     cstmt.setString(7, creditorAccountName008);
		     cstmt.setString(8, "");
		     cstmt.setString(9, "");
		     cstmt.setString(10, rmt_info_pacs008);
		     
		     boolean hasResults = cstmt.execute();
		      while(hasResults){
		        ResultSet rs = cstmt.getResultSet();
		        while(rs.next()){
		        	
		        	String res_code=String.valueOf(rs.getInt("response_code"));
		        	String tran_code=String.valueOf(rs.getInt("response_msg"));
		        	String receiptNo=String.valueOf(rs.getInt("receipt_no"));
		        	if(res_code.equals("1")) {
		        		tranResponse=errorCode.ErrorCode("CIM0");
		        	}else {
		        		tranResponse=errorCode.ErrorCode(tran_code);
		        	}
		        	
		        	taskExecutor.execute(new Runnable() {
						@Override
						public void run() {
							callESBPostData(creditorAccount008,creditorAccountName008,
									trAmount008S, trCurrency008, sysTraceNumber008, seqUniqueID008,othBankCode,request,
									debtorAccount008,debtorAccountName008,instgAgtPacs008,ctgy_purp_pacs008,rmt_info_pacs008,instr_id_pacs008,endToEndID008,
									res_code,tran_code,receiptNo);
						}
					});
		        	
		        	return tranResponse;
		        	
		        }
		        hasResults = cstmt.getMoreResults();
		      } 
		   
		} catch (SQLException e) {
			System.out.println(e.getLocalizedMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return null;
	}

	protected void callESBPostData(String creditorAccount008, String creditorAccountName008, String trAmount008S,
			String trCurrency008, String sysTraceNumber008, String seqUniqueID008, String othBankCode, SendT request,
			String debtorAccount008, String debtorAccountName008, String instgAgtPacs008, String ctgy_purp_pacs008,
			String rmt_info_pacs008, String instr_id_pacs008, String endToEndID008, String res_code, String tran_code,
			String receiptNo) {

	}
	
	public String getMaxAmountPerDay(String acctNumber,List<BenAccount> benBankCodeList) {
		String totMaxAmt=tranRep.getMaxTranAmt(acctNumber);
		Double Tran_amt =new Double(0);
		for(BenAccount benAccount:benBankCodeList) {
			Tran_amt +=Double.parseDouble(benAccount.getTrAmt());
		}
		Double d2=Double.parseDouble(totMaxAmt)+(Tran_amt);
		String totAmt= d2.toString();
		// TODO Auto-generated method stub
		return totAmt;
	}

	public String getMaxAmountWeekly(String acctNumber,List<BenAccount> benBankCodeList) {
		String totMaxAmt=tranRep.getMaxTranAmtweekly(acctNumber);
		Double Tran_amt =new Double(0);
		for(BenAccount benAccount:benBankCodeList) {
			Tran_amt +=Double.parseDouble(benAccount.getTrAmt());
		}
		Double d2=Double.parseDouble(totMaxAmt)+(Tran_amt);
		String totAmt= d2.toString();
		// TODO Auto-generated method stub
		return totAmt;
	}
	
	public String getMaxAmountMonthly(String acctNumber,List<BenAccount> benBankCodeList) {
		String tranDate = "";
		DateFormat dateFormat = new SimpleDateFormat("MMM-yyyy");
		tranDate = dateFormat.format(new Date());
		
		String totMaxAmt=tranRep.getMaxTranAmtmonthly(acctNumber,tranDate);
		Double Tran_amt =new Double(0);
		for(BenAccount benAccount:benBankCodeList) {
			Tran_amt +=Double.parseDouble(benAccount.getTrAmt());
		}
		Double d2=Double.parseDouble(totMaxAmt)+(Tran_amt);
		String totAmt= d2.toString();
		// TODO Auto-generated method stub
		return totAmt;
	}

	public RTPTransferStatusResponse invalidTran_ID(RTPTransferRequestStatus tran_ID) {
		boolean valid = false;
		List<Object[]> otm = null;
		try {
			
			if(!tran_ID.getTranID().equals("")) {
				System.out.println("TRANID"+tran_ID.getTranID());
				otm = outwardTranRep.existsByTranID(tran_ID.getTranID());
			}else if(!tran_ID.getReqId().equals("")) {
				System.out.println("REFID");
				 otm = outwardTranRep.existsByRefID(tran_ID.getReqId());
			}
			

			if (otm.size()>0) {
				System.out.println("REFID1");
				RTPTransferStatusResponse rtp = new RTPTransferStatusResponse();
				
				Object[] otmotm=otm.get(0);
				rtp.setTranId(otmotm[2].toString());
				rtp.setReqId(otmotm[3].toString());
				System.out.println("otm.get(0); "+otm.get(0).toString());
				if(otmotm[0].toString().equals("SUCCESS")){
					System.out.println("SUCCESS");
					RtpResponse rtpResponse = new RtpResponse();
					rtpResponse.setStatus("Success");
					rtp.setRtpResponse(rtpResponse); 
				}else if(otmotm[0].toString().equals("FAILURE")){
					System.out.println("FAILURE");
					RtpResponse rtpResponse = new RtpResponse();
					rtpResponse.setStatus("Failure");
					if(!otmotm[1].toString().equals("")) {
						rtpResponse.setErrorStatus(otmotm[1].toString());
					}else {
						rtpResponse.setErrorStatus("");
					}
					System.out.println("FAILURE");
					rtp.setRtpResponse(rtpResponse); 
				}else {
					RtpResponse rtpResponse = new RtpResponse();
					rtpResponse.setStatus("Inprogress");
					rtp.setRtpResponse(rtpResponse); 
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
