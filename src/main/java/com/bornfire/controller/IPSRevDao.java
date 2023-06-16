package com.bornfire.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bornfire.clientService.IPSXClient;
import com.bornfire.config.ErrorResponseCode;
import com.bornfire.config.InformixConnectionManager;
import com.bornfire.config.Listener;
import com.bornfire.config.SequenceGenerator;
import com.bornfire.entity.BankAgentTableRep;
import com.bornfire.entity.CimCBSresponse;
import com.bornfire.entity.ConsentAccessInquiryTableRep;
import com.bornfire.entity.ConsentAccessTableRep;
import com.bornfire.entity.ConsentAccessTmpTableRep;
import com.bornfire.entity.ConsentOutwardAccessTableRep;
import com.bornfire.entity.ConsentOutwardAccessTmpTableRep;
import com.bornfire.entity.ConsentOutwardAuthorisationTableRep;
import com.bornfire.entity.ConsentOutwardInquiryTableRep;
import com.bornfire.entity.ConsentTableRep;
import com.bornfire.entity.IPSFlowTableRep;
import com.bornfire.entity.MerchantQrGenTablerep;
import com.bornfire.entity.OTPGenTableRep;
import com.bornfire.entity.OutwardTransHistMonitoringTableRep;
import com.bornfire.entity.OutwardTransactionMonitoringTable;
import com.bornfire.entity.OutwardTransactionMonitoringTableRep;
import com.bornfire.entity.RegPublicKeyRep;
import com.bornfire.entity.RegPublicKeyTmpRep;
import com.bornfire.entity.SettlementAccountAmtRep;
import com.bornfire.entity.SettlementAccountRep;
import com.bornfire.entity.SettlementLimitReportRep;
import com.bornfire.entity.SettlementReportRep;
import com.bornfire.entity.TranAllDetailsTableRep;
import com.bornfire.entity.TranCBSTableRep;
import com.bornfire.entity.TranCimCBSTable;
import com.bornfire.entity.TranCimCBSTableRep;
import com.bornfire.entity.TranCimGLRep;
import com.bornfire.entity.TranHistMonitorHistRep;
import com.bornfire.entity.TranIPSTable;
import com.bornfire.entity.TranIPSTableRep;
import com.bornfire.entity.TranMonitorStatus;
import com.bornfire.entity.TranNetStatRep;
import com.bornfire.entity.TranNonCBSTableRep;
import com.bornfire.entity.TransactionMonitor;
import com.bornfire.entity.TransactionMonitorRep;
import com.bornfire.entity.WalletAccessTableRep;
import com.bornfire.entity.WalletAccessTmpTableRep;
import com.bornfire.entity.WalletRequestRegisterTableRep;

@Service
@Transactional
public class IPSRevDao {

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

	private static final Logger logger = LoggerFactory.getLogger(IPSXClient.class);

	public void updateIPSXStatusResponseRJCT(String seqUniqueID, String ipsxerrorDesc, String ipsxMsgID,
			String tranStatus, String ipsXStatus, String ipsxResponseStatus, String ipsxErrorCode, String msgType) {
		try {
			// Optional<TranIPSTable> otmTranIPS = tranIPStableRep.findById(seqUniqueID);

			logger.info(seqUniqueID + " :IPSX Rejected Sttatus");

			List<TranIPSTable> otmTranIPS = tranIPStableRep.findByIdCustom(seqUniqueID);

			//// In for loop used for reversal transaction of RTP(Transaction created
			//// between BOB Customers)
			//// Tran IPS Table is used for Maintaining IPS Packages and got Original ID Of
			//// Packages and updated tran status to Transaction_monitoring _table
//INWARD
			for (TranIPSTable item : otmTranIPS) {
				Optional<TransactionMonitor> otm1 = tranRep.findById(item.getSequence_unique_id());

				if (otm1.isPresent()) {
					logger.info("InwardRJCT");

					TransactionMonitor tm1 = otm1.get();
					taskExecutor.execute(new Runnable() {
						@Override
						public void run() {
							callESBINWARDPostFailureData(tm1.getSequence_unique_id(), ipsxErrorCode, ipsxerrorDesc);
						}
					});
				}
			}

//OUTWARD
			for (TranIPSTable item : otmTranIPS) {
				Optional<OutwardTransactionMonitoringTable> otm = outwardTranRep.findById(item.getSequence_unique_id());
				if (otm.isPresent()) {
					logger.info("outwardRJCT");
					OutwardTransactionMonitoringTable tm = otm.get();

					if (!tm.getMsg_type().equals("BULK_DEBIT")) {
						if (tm.getMsg_type().equals(TranMonitorStatus.OUTWARD_BULK_RTP.toString())) {

							taskExecutor.execute(new Runnable() {
								@Override
								public void run() {
									callESBOUTWARDPostFailureData(tm.getSequence_unique_id(), ipsxErrorCode, ipsxerrorDesc);
								}
							});

						}

					}

				}
			}

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

	}

	private void callESBOUTWARDPostFailureData(String sequence_unique_id, String ipsxErrorCode, String ipsxerrorDesc) {
		Optional<OutwardTransactionMonitoringTable> otm1 = outwardTranRep.findById(sequence_unique_id);
		// logger.info("outwardRJCT Mod"+otm1.get().getResponse_status());

		OutwardTransactionMonitoringTable tm = otm1.get();

		///// Send Failure Message to CIM

			//// Generate RequestUUID
			String requestUUID = sequence.generateRequestUUId();

			String init_tran_no = "";
			if (tm.getResv_field1() != null) {
				init_tran_no = tm.getResv_field1();
			} else {
				init_tran_no = tm.getP_id();
			}

			String settlReceivableAccount = settlAccountRep.findById("03").get().getAccount_number();

			String response = registerCIMcbsIncomingData(requestUUID, env.getProperty("cimCBS.channelID"),
					env.getProperty("cimCBS.servicereqversion"), env.getProperty("cimCBS.servicereqID"), new Date(),
					sequence.generateSystemTraceAuditNumber(), tm.getInit_channel_id(), init_tran_no, "TRUE", "DR", "Y",
					"", tm.getCim_account(), tm.getTran_amount().toString(), tm.getTran_currency(),
					tm.getSequence_unique_id(), settlReceivableAccount, tm.getCim_account_name(), "RTP", "", "", "", "",
					new Date(), "RECEIVABLE", tm.getReq_unique_id(), ipsxErrorCode, ipsxerrorDesc,
					tm.getMaster_ref_id());

			logger.info("Pain Output Return Msg to ThirdParty Application");

			logger.info(tm.getSequence_unique_id() + " :CIM Register Reverse Data Output:" + response);

			if (response.equals("1")) {
				ResponseEntity<CimCBSresponse> connect24Response = cimCBSservice.cbsResponseFailure(requestUUID);

				logger.debug(listener.generateJsonFormat(connect24Response.toString()));
				logger.info(tm.getSequence_unique_id() + "CIM : 0");

				if (connect24Response.getStatusCode() == HttpStatus.OK) {

					/*
					 * CimCBSresponse data=connect24Response.getBody();
					 * logger.info(tm.getSequence_unique_id() + " CIM: 1");
					 * 
					 * if(connect24Response.getBody().getData()!=null) {
					 * if(connect24Response.getBody().getStatus().getIsSuccess()) {
					 * updateCIMcbsData(requestUUID,"SUCCESS",connect24Response.getBody().getStatus(
					 * ).getStatusCode(),connect24Response.getBody().getStatus().getMessage(),
					 * connect24Response.getBody().getData().getTransactionNoFromCBS());
					 * 
					 * logger.info(tm.getSequence_unique_id() + " CIM: 2");
					 * updateCIMCNFData(tm.getSequence_unique_id(),requestUUID,"SUCCESS","");
					 * 
					 * }else {
					 * updateCIMcbsData(requestUUID,"FAILURE",connect24Response.getBody().getStatus(
					 * ).getStatusCode(),connect24Response.getBody().getStatus().getMessage(),
					 * connect24Response.getBody().getData().getTransactionNoFromCBS());
					 * logger.info(tm.getSequence_unique_id() + " CIM: 3");
					 * updateCIMCNFData(tm.getSequence_unique_id(),requestUUID,"FAILURE",
					 * connect24Response.getBody().getStatus().getMessage()); } }else {
					 * updateCIMcbsData(requestUUID,"FAILURE",connect24Response.getBody().getStatus(
					 * ).getStatusCode(),connect24Response.getBody().getStatus().getMessage(),
					 * connect24Response.getBody().getData().getTransactionNoFromCBS());
					 * logger.info(tm.getSequence_unique_id() + " CIM: 3");
					 * updateCIMCNFData(tm.getSequence_unique_id(),requestUUID,
					 * "FAILURE","No Response return from CBS"); }
					 */
				} /*
					 * else { logger.info(tm.getSequence_unique_id() + " : CIM 4:Failure");
					 * updateCIMcbsData(requestUUID,"FAILURE","500","Internal Server Error","");
					 * updateCIMCNFData(tm.getSequence_unique_id(),requestUUID,
					 * "FAILURE","Internal Server Error");
					 * 
					 * }
					 */
			}
		

	}
	private void callESBINWARDPostFailureData(String sequence_unique_id, String ipsxErrorCode, String ipsxerrorDesc) {
		Optional<TransactionMonitor> otm1 = tranRep.findById(sequence_unique_id);
		// logger.info("outwardRJCT Mod"+otm1.get().getResponse_status());

		TransactionMonitor tm = otm1.get();

		///// Send Failure Message to CIM

			//// Generate RequestUUID
			String requestUUID = sequence.generateRequestUUId();

		

			String settlReceivableAccount = settlAccountRep.findById("03").get().getAccount_number();

			String response = registerCIMcbsIncomingData(requestUUID, env.getProperty("cimCBS.channelID"),
					env.getProperty("cimCBS.servicereqversion"), env.getProperty("cimCBS.servicereqID"), new Date(),
					sequence.generateSystemTraceAuditNumber(), env.getProperty("cimCBS.incCRChannel"), tm.getEnd_end_id(), "TRUE", "DR", "Y",
					"", tm.getBob_account(), tm.getTran_amount().toString(), tm.getTran_currency(),
					tm.getSequence_unique_id(), settlReceivableAccount, tm.getBob_account_name(), "NRT", "", tm.getRmt_info(), "", "",
					new Date(), "RECEIVABLE", tm.getRmt_info(), ipsxErrorCode, ipsxerrorDesc,
					tm.getMaster_ref_id());

			logger.info("Pain Output Return Msg to ThirdParty Application");

			logger.info(tm.getSequence_unique_id() + " :CIM Register Reverse Data Output:" + response);

			if (response.equals("1")) {
				ResponseEntity<CimCBSresponse> connect24Response = cimCBSservice.cbsResponseFailure(requestUUID);

				logger.debug(listener.generateJsonFormat(connect24Response.toString()));
				logger.info(tm.getSequence_unique_id() + "CIM : 0");

				if (connect24Response.getStatusCode() == HttpStatus.OK) {

					/*
					 * CimCBSresponse data=connect24Response.getBody();
					 * logger.info(tm.getSequence_unique_id() + " CIM: 1");
					 * 
					 * if(connect24Response.getBody().getData()!=null) {
					 * if(connect24Response.getBody().getStatus().getIsSuccess()) {
					 * updateCIMcbsData(requestUUID,"SUCCESS",connect24Response.getBody().getStatus(
					 * ).getStatusCode(),connect24Response.getBody().getStatus().getMessage(),
					 * connect24Response.getBody().getData().getTransactionNoFromCBS());
					 * 
					 * logger.info(tm.getSequence_unique_id() + " CIM: 2");
					 * updateCIMCNFData(tm.getSequence_unique_id(),requestUUID,"SUCCESS","");
					 * 
					 * }else {
					 * updateCIMcbsData(requestUUID,"FAILURE",connect24Response.getBody().getStatus(
					 * ).getStatusCode(),connect24Response.getBody().getStatus().getMessage(),
					 * connect24Response.getBody().getData().getTransactionNoFromCBS());
					 * logger.info(tm.getSequence_unique_id() + " CIM: 3");
					 * updateCIMCNFData(tm.getSequence_unique_id(),requestUUID,"FAILURE",
					 * connect24Response.getBody().getStatus().getMessage()); } }else {
					 * updateCIMcbsData(requestUUID,"FAILURE",connect24Response.getBody().getStatus(
					 * ).getStatusCode(),connect24Response.getBody().getStatus().getMessage(),
					 * connect24Response.getBody().getData().getTransactionNoFromCBS());
					 * logger.info(tm.getSequence_unique_id() + " CIM: 3");
					 * updateCIMCNFData(tm.getSequence_unique_id(),requestUUID,
					 * "FAILURE","No Response return from CBS"); }
					 */
				} /*
					 * else { logger.info(tm.getSequence_unique_id() + " : CIM 4:Failure");
					 * updateCIMcbsData(requestUUID,"FAILURE","500","Internal Server Error","");
					 * updateCIMCNFData(tm.getSequence_unique_id(),requestUUID,
					 * "FAILURE","Internal Server Error");
					 * 
					 * }
					 */
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
}
