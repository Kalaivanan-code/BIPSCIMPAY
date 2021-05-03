package com.bornfire.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bornfire.clientService.IPSXClient;
import com.bornfire.config.ErrorResponseCode;
import com.bornfire.config.SequenceGenerator;
import com.bornfire.entity.BankAgentTable;
import com.bornfire.entity.BankAgentTableRep;
import com.bornfire.entity.BukCreditTransferRequest;
import com.bornfire.entity.ConsentAccessRequest;
import com.bornfire.entity.ConsentAccessTable;
import com.bornfire.entity.ConsentAccessTableRep;
import com.bornfire.entity.ConsentAccessTmpTable;
import com.bornfire.entity.ConsentAccessTmpTableRep;
import com.bornfire.entity.ConsentRequest;
import com.bornfire.entity.ConsentTable;
import com.bornfire.entity.ConsentTableRep;
import com.bornfire.entity.CryptogramResponse;
import com.bornfire.entity.IPSFlowTable;
import com.bornfire.entity.IPSFlowTableRep;
import com.bornfire.entity.Links;
import com.bornfire.entity.MCCreditTransferRequest;
import com.bornfire.entity.ManualFndTransferRequest;
import com.bornfire.entity.OTPGenTable;
import com.bornfire.entity.OTPGenTableRep;
import com.bornfire.entity.OtherBankDetResponse;
import com.bornfire.entity.RegPublicKey;
import com.bornfire.entity.RegPublicKeyRep;
import com.bornfire.entity.RegPublicKeyTmp;
import com.bornfire.entity.RegPublicKeyTmpRep;
import com.bornfire.entity.SCAAthenticationResponse;
import com.bornfire.entity.SCAAuthenticatedData;
import com.bornfire.entity.SettlementAccountAmtRep;
import com.bornfire.entity.SettlementAccountAmtTable;
import com.bornfire.entity.SettlementLimitReportRep;
import com.bornfire.entity.SettlementLimitReportTable;
import com.bornfire.entity.SettlementReport;
import com.bornfire.entity.SettlementReportRep;
import com.bornfire.entity.TranCBSTable;
import com.bornfire.entity.TranCBSTableRep;
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

	private static final Logger logger = LoggerFactory.getLogger(IPSXClient.class);

	public void RegisterOutMsgRecord(String psuDeviceID, String psuIpAddress, String psuID, String senderParticipantBIC,
			String participantSOL, String sysTraceNumber, MCCreditTransferRequest mcCreditTransferRequest,
			String bobMsgID, String seqUniqueID, String endToEndID, String tran_type_code, String msgNetMIR,
			String instg_agt, String instd_agt, String dbtr_agt, String dbtr_agt_acc, String cdtr_agt,
			String cdtr_agt_acc, String instr_id, String svc_lvl, String lcl_instrm, String ctgy_purp) {

		try {
			TransactionMonitor tranManitorTable = new TransactionMonitor();
			tranManitorTable.setMsg_type(TranMonitorStatus.OUTGOING.toString());
			tranManitorTable.setTran_audit_number(sysTraceNumber);
			tranManitorTable.setSequence_unique_id(seqUniqueID);
			tranManitorTable.setBob_message_id(bobMsgID);
			tranManitorTable.setBob_account(mcCreditTransferRequest.getFrAccount().getAcctNumber());
			tranManitorTable.setIpsx_account(mcCreditTransferRequest.getToAccount().getAcctNumber());
			tranManitorTable.setReceiver_bank(mcCreditTransferRequest.getToAccount().getBankCode());
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
				logger.info("updateCBSStatus:Data Not found");

			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

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

	public void updateIPSXStatus(String seqUniqueID, String ipsStatus, String tranStatus) {
		try {
			Optional<TransactionMonitor> otm = tranRep.findById(seqUniqueID);
			logger.info("updateIPSXStatus seqID:" + seqUniqueID);

			if (otm.isPresent()) {
				TransactionMonitor tm = otm.get();

				/*
				 * if(tm.getResponse_status().equals(TranMonitorStatus.ACSP.toString())) {
				 * //tm.setIpsx_status(ipsStatus);
				 * tm.setTran_status(TranMonitorStatus.SUCCESS.toString()); tranRep.save(tm);
				 * //tm.setIpsx_response_time(new Date()); }else { tm.setIpsx_status(ipsStatus);
				 * tm.setTran_status(tranStatus); tm.setIpsx_response_time(new Date());
				 * tranRep.save(tm); }
				 */

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

				tranRep.save(tm);

			} else {
				logger.info("updateIPSXStatus:Data not found");
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

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

	public void updateIPSXStatusResponseACSP(String seqUniqueID, String ipsxMsgID, String tranStatus) {
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

					if (!tm.getMsg_type().equals("BULK_DEBIT")) {
						tm.setIpsx_status(TranMonitorStatus.IPSX_RESPONSE_ACSP.toString());
						tm.setResponse_status(TranMonitorStatus.ACSP.toString().trim());
						tm.setIpsx_message_id(ipsxMsgID);
						tm.setIpsx_response_time(new Date());
						tm.setTran_status(tranStatus);
						tranRep.save(tm);

						updateINOUT(tm.getSequence_unique_id(), "IPS_OUT");

						//// Apply Charges And Fee
						/// Check Transaction outward
						if (!tm.getMsg_type().equals(TranMonitorStatus.INCOMING.toString())) {
							ipsConnection.ipsChargesAndFeeTran(tm.getSequence_unique_id(), "IPS Fund Transfer");
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

				}
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

	public void updateIPSXStatusResponseRJCT(String seqUniqueID, String ipsxerrorDesc, String ipsxMsgID,
			String tranStatus, String ipsXStatus, String ipsxResponseStatus, String ipsxErrorCode) {
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

					TransactionMonitor tm = otm.get();

					if (!tm.getMsg_type().equals("BULK_DEBIT")) {
						tm.setIpsx_message_id(ipsxMsgID);
						tm.setIpsx_status(ipsXStatus);
						tm.setIpsx_status_error(ipsxerrorDesc);
						tm.setResponse_status(ipsxResponseStatus);
						tm.setIpsx_response_time(new Date());
						tm.setTran_status(tranStatus);
						tm.setIpsx_status_code(ipsxErrorCode);
						tranRep.save(tm);

						if (tm.getMsg_type().equals(TranMonitorStatus.INCOMING.toString())) {

							logger.info(tm.getSequence_unique_id() + " : Incoming / " + tm.getCbs_status());

							if (tm.getCbs_status().equals(TranMonitorStatus.CBS_CREDIT_OK.toString())) {

								updateCBSStatus(tm.getSequence_unique_id(),
										TranMonitorStatus.CBS_CREDIT_REVERSE_INITIATED.toString(),
										TranMonitorStatus.IN_PROGRESS.toString());

								logger.info(tm.getSequence_unique_id() + " : Initiate Reversal Transaction");

								connect24Service.cdtReverseFundRequest(tm.getBob_account(),
										tm.getTran_amount().toString(), tm.getTran_currency().toString(),
										sequence.generateSystemTraceAuditNumber(), tm.getSequence_unique_id(),
										"RT/"+tm.getTran_audit_number()+"/"+ ipsxerrorDesc);
							} else {
								logger.info(tm.getSequence_unique_id() + " : No need for Reversal Transaction");

								updateCBSStatus(tm.getSequence_unique_id(), tm.getCbs_status(),
										TranMonitorStatus.FAILURE.toString());
							}

						} else if (tm.getMsg_type().equals(TranMonitorStatus.OUTGOING.toString())) {

							logger.info(tm.getSequence_unique_id() + " : Outgoing / " + tm.getCbs_status());

							updateINOUT(tm.getSequence_unique_id(), "IPS_OUT");

							if (tm.getCbs_status().equals(TranMonitorStatus.CBS_DEBIT_OK.toString())) {

								updateCBSStatus(tm.getSequence_unique_id(),
										TranMonitorStatus.CBS_DEBIT_REVERSE_INITIATED.toString(),
										TranMonitorStatus.IN_PROGRESS.toString());

								logger.info(tm.getSequence_unique_id() + " : Initiate Reversal Transaction");

								connect24Service.dbtReverseFundRequest(tm.getBob_account(),
										sequence.generateSystemTraceAuditNumber(), tm.getTran_currency(),
										tm.getTran_amount().toString(), tm.getSequence_unique_id(),
										"RT/"+tm.getTran_audit_number()+"/"+ ipsxerrorDesc);
							} else {

								logger.info(tm.getSequence_unique_id() + " : No need for Reversal Transaction");
								updateCBSStatus(tm.getSequence_unique_id(), tm.getCbs_status(),
										TranMonitorStatus.FAILURE.toString());
							}

						} else if (tm.getMsg_type().equals(TranMonitorStatus.RTP.toString())) {

							logger.info(tm.getSequence_unique_id() + " : RTP / " + tm.getCbs_status());

							if (tm.getCbs_status().equals(TranMonitorStatus.CBS_DEBIT_OK.toString())) {

								updateCBSStatus(tm.getSequence_unique_id(),
										TranMonitorStatus.CBS_DEBIT_REVERSE_INITIATED.toString(),
										TranMonitorStatus.IN_PROGRESS.toString());

								logger.info(tm.getSequence_unique_id() + " : Initiate Reversal Transaction");

								connect24Service.rtpReverseFundRequest(tm.getBob_account(),
										sequence.generateSystemTraceAuditNumber(), tm.getTran_currency(),
										tm.getTran_amount().toString(), tm.getSequence_unique_id(),
										"RT/"+tm.getTran_audit_number()+"/"+ ipsxerrorDesc);
							} else {

								logger.info(tm.getSequence_unique_id() + " : No need for Reversal Transaction");
								updateCBSStatus(tm.getSequence_unique_id(), tm.getCbs_status(),
										TranMonitorStatus.FAILURE.toString());
							}

						} else if (tm.getMsg_type().equals(TranMonitorStatus.BULK_CREDIT.toString())) {

							logger.info(tm.getSequence_unique_id() + " : Bulk Credit / " + tm.getCbs_status());

							if (tm.getCbs_status().equals(TranMonitorStatus.CBS_DEBIT_OK.toString())) {

								updateCBSStatus(tm.getSequence_unique_id(),
										TranMonitorStatus.CBS_DEBIT_REVERSE_INITIATED.toString(),
										TranMonitorStatus.IN_PROGRESS.toString());

								logger.info(tm.getSequence_unique_id() + " : Initiate Reversal Transaction");

								connect24Service.dbtReverseBulkCreditFundRequest(tm.getBob_account(),
										sequence.generateSystemTraceAuditNumber(), tm.getTran_currency(),
										tm.getTran_amount().toString(), tm.getSequence_unique_id(),
										"RT/"+tm.getTran_audit_number()+"/"+ ipsxerrorDesc);
							} else {
								logger.info(tm.getSequence_unique_id() + " : No need for Reversal Transaction");

								updateCBSStatus(tm.getSequence_unique_id(), tm.getCbs_status(),
										TranMonitorStatus.FAILURE.toString());
							}

						} else if (tm.getMsg_type().equals(TranMonitorStatus.MANUAL.toString())) {

							logger.info(tm.getSequence_unique_id() + " : Manual / " + tm.getCbs_status());

							updateINOUT(tm.getSequence_unique_id(), "IPS_OUT");

							if (tm.getCbs_status().equals(TranMonitorStatus.CBS_DEBIT_OK.toString())) {

								updateCBSStatus(tm.getSequence_unique_id(),
										TranMonitorStatus.CBS_DEBIT_REVERSE_INITIATED.toString(),
										TranMonitorStatus.IN_PROGRESS.toString());

								logger.info(tm.getSequence_unique_id() + " : Initiate Reversal Transaction");

								connect24Service.dbtReverseFundRequest(tm.getBob_account(),
										sequence.generateSystemTraceAuditNumber(), tm.getTran_currency(),
										tm.getTran_amount().toString(), tm.getSequence_unique_id(),
										"RT/"+tm.getTran_audit_number()+"/"+ ipsxerrorDesc);
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

							if (tranMonitorItem.getCbs_status().equals(TranMonitorStatus.CBS_DEBIT_OK.toString())) {

								tranMonitorItem.setIpsx_message_id(ipsxMsgID);
								tranMonitorItem.setIpsx_status(ipsXStatus);
								tranMonitorItem.setIpsx_status_error(ipsxerrorDesc);
								tranMonitorItem.setResponse_status(ipsxResponseStatus);
								tranMonitorItem.setIpsx_response_time(new Date());
								tranMonitorItem.setTran_status(tranStatus);
								tranMonitorItem.setIpsx_status_code(ipsxErrorCode);

								tranRep.save(tranMonitorItem);

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
							}

						}

					}
				} else {

				}

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
			String ctgy_purp, String dbtr_agt, String dbtr_agt_acc, String cdtr_agt, String cdtr_agt_acc) {

		try {
			Optional<TransactionMonitor> otm = tranRep.findById(seqUniqueID);
			logger.info(seqUniqueID);
			if (otm.isPresent()) {
				////// update Registration income message sometimes RJCT Message comes first
				////// ,When disconnecting STPA adapter.

				TransactionMonitor tranManitorTable = otm.get();
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
				tranManitorTable.setTran_currency(Currency_code);
				tranManitorTable.setEnd_end_id(EndToEndID);
				tranManitorTable.setIpsx_account_name(ipsxAccountName);
				tranManitorTable.setBob_account_name(bobAccountName);
				tranManitorTable.setTran_type_code(tran_type_code);
				tranManitorTable.setInitiator_bank(initiatorBank);
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

				logger.info(seqUniqueID + ": TranMonitorTable Updated Successfully");

				logger.info(tranManitorTable.toString());
				tranRep.save(tranManitorTable);
				tranRep.flush();
				return "0";
			} else {
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

				logger.info(seqUniqueID + ": TranMonitorTable Inserted Successfully");
				logger.info(tranManitorTable.toString());
				tranRep.save(tranManitorTable);
				tranRep.flush();

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

			consentAccessID.setSchm_name(consentRequest.getAccount().get(0).getSchemeName());
			consentAccessID.setIdentification(consentRequest.getAccount().get(0).getIdentification());
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

	public SCAAthenticationResponse updateSCAConsentAccess(SCAAuthenticatedData scaAuthenticatedData, String consentID,
			String authID, String cryptogram) {

		Optional<ConsentAccessTmpTable> regPublicKeyTmpotm = consentAccessTmpTableRep.findById(authID);
		Optional<ConsentAccessTable> regPublicKeyotm = consentAccessTableRep.findById(consentID);
		Optional<OTPGenTable> otm = otpGenTableRep.findById(authID);
		SCAAthenticationResponse response = null;

		if (regPublicKeyTmpotm.isPresent()) {
			if (regPublicKeyTmpotm.get().getConsent_id().equals(consentID)) {
				if(validateJWTToken(cryptogram,regPublicKeyTmpotm.get().getPublic_key(),regPublicKeyTmpotm.get().getPsu_device_id(),
						regPublicKeyTmpotm.get().getConsent_id())) {
					if (otm.isPresent()) {
						OTPGenTable otpGenTable = otm.get();
						if (scaAuthenticatedData.getScaAuthenticationData().toString().equals(otpGenTable.getOtp().toString())) {
							if (isNowBetweenDateTime(otpGenTable.getGenerated_date(), otpGenTable.getExpired_date())) {
								
								response = new SCAAthenticationResponse();
								// String recordID=sequence.generateRecordId();
								Links links = new Links();
								links.setSCAStatus("/accounts/" + consentID + "/authorisations/" + authID);
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
											consentAccessID.setIdentification(regPublicKeyTmpotm.get().getIdentification());
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
													.getAccountNumber(regPublicKeyTmpotm.get().getIdentification());
											if (regAccList.size() > 0) {
												ConsentAccessTable consentAccessID = regAccList.get(0);

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
												consentAccessID.setIdentification(regPublicKeyTmpotm.get().getIdentification());
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
												consentAccessID.setIdentification(regPublicKeyTmpotm.get().getIdentification());
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
			} else {
				throw new IPSXRestException(errorCode.ErrorCodeRegistration("13"));
			}
		} else {
			throw new IPSXRestException(errorCode.ErrorCodeRegistration("3"));
		}
		return response;
		
	}
	
	public String checkConsentAccountExist(String x_request_id, String sender_participant_bic,
			String sender_participant_member_id, String receiver_participant_bic, String receiver_participant_member_id,
			String psuDeviceID, String psuIPAddress, String psuID, String psuIDCountry, String psuIDType,
			String consentID, String accountID, String cryptogram) {
		Optional<ConsentAccessTable> regPublicKeyotm = consentAccessTableRep.findById(consentID);
		
		String response="0";
		try {
			if(regPublicKeyotm.isPresent()) {
				if(regPublicKeyotm.get().getRead_balance().equals("Y")) {
					if(validateJWTToken(cryptogram,regPublicKeyotm.get().getPublic_key(),regPublicKeyotm.get().getPsu_device_id(),
							regPublicKeyotm.get().getConsent_id())) {
						response="1";
					}else {
						throw new IPSXRestException(errorCode.ErrorCodeRegistration("4"));
					}
				}else {
					throw new IPSXRestException(errorCode.ErrorCodeRegistration("1"));
				}
				
			}else {
				throw new IPSXRestException(errorCode.ErrorCodeRegistration("13"));
			}
		}catch(Exception e) {
			logger.info("Consent Balance: "+e.getLocalizedMessage());
			throw new IPSXRestException(errorCode.ErrorCodeRegistration("0"));
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

	public List<OtherBankDetResponse> getOtherBankDet() {
		List<OtherBankDetResponse> list = new ArrayList<OtherBankDetResponse>();
		try {
			List<BankAgentTable> otm = bankAgentTableRep.findAll();
			for (BankAgentTable i : otm) {
				if (!i.getDel_flg().equals("Y")) {
					OtherBankDetResponse res = new OtherBankDetResponse(i.getBank_code(), i.getBank_name());
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
				if(otm.get().getBank_code().equals("02")) {
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

			if (otm.isPresent()) {
				tm = otm.get().getBank_code();
				return tm;
			} else {
				return tm;
			}
		} catch (Exception e) {
			return tm;
		}

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
			String msgSender, String msgReceiver, String msgNetMir, String userRef) {
		try {
			TranIPSTable tranIPStable = new TranIPSTable();

			tranIPStable.setSequence_unique_id(sequenceUniqueID);

			TranIPSTableID tranISTableID = new TranIPSTableID();

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

			logger.info(sequenceUniqueID + ":Insert Tran IPS Table Successfully SeqUniqueID" + sequenceUniqueID
					+ " /MessageID :" + msgID);

			tranIPStableRep.saveAndFlush(tranIPStable);

		} catch (Exception e) {
			logger.info(e.getLocalizedMessage());
			System.err.println(e.getMessage());
		}

	}

	public void updateTranIPSACK(String sequenceUniqueID, String msgID, String msg_sub_type, String Ack_status,
			String net_mir, String userRef) {
		try {
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
		}

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
			String senderParticipantBIC, String participantSOL, String sysTraceNumber, String frAccountNumber,
			String frAcctName, String toAccountNumber, String toAccountName, String toAccountBank, String trAmt,
			String currency, String bobMsgID, String seqUniqueID, String tran_type_code, String master_ref_id,
			String endToEndID, String msgNetMIR, String instg_agt, String instd_agt, String dbtr_agt,
			String dbtr_agt_acc, String cdtr_agt, String cdtr_agt_acc, String instr_id, String svc_lvl,
			String lcl_instrm, String ctgy_purp, String userID) {
		try {

			TransactionMonitor tranManitorTable = new TransactionMonitor();
			tranManitorTable.setMsg_type(TranMonitorStatus.BULK_CREDIT.toString());
			tranManitorTable.setTran_audit_number(sysTraceNumber);
			tranManitorTable.setSequence_unique_id(seqUniqueID);
			tranManitorTable.setBob_message_id(bobMsgID);
			tranManitorTable.setBob_account(frAccountNumber);
			tranManitorTable.setIpsx_account(toAccountNumber);
			tranManitorTable.setReceiver_bank(toAccountBank);
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

			tranManitorTable.setBob_account_name(frAcctName);
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

	public void RegisterManualTranRecord(String psuDeviceID, String psuIpAddress, String sysTraceNumber,
			ManualFndTransferRequest manualFndTransferRequest, String bobMsgID, String seqUniqueID, String endTOEndID,
			String master_ref_id, String msgNetMIR, String instg_agt, String instd_agt, String dbtr_agt,
			String dbtr_agt_acc, String cdtr_agt, String cdtr_agt_acc, String instr_id, String svc_lvl,
			String lcl_instrm, String ctgy_purp, String tran_type_code, String userID) {
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
			String lcl_instrm, String ctgy_purp, String userID) {

		try {
			TransactionMonitor tranManitorTable = new TransactionMonitor();
			tranManitorTable.setMsg_type(TranMonitorStatus.BULK_DEBIT.toString());
			tranManitorTable.setTran_audit_number(sysTraceNumber);
			tranManitorTable.setSequence_unique_id(seqUniqueID);
			tranManitorTable.setBob_message_id(bobMsgId);
			tranManitorTable.setBob_account(remitterAcctNumber);
			tranManitorTable.setIpsx_account(BenacctNumber);
			tranManitorTable.setReceiver_bank(BenBankCode);
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
			tranManitorTable.setBob_account_name(remitterName);
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
			logger.error(e.getLocalizedMessage());
		}
	}

	public void registerCamt53Record(com.bornfire.jaxb.camt_053_001_08.Document docCamt053_001_08) {

		List<AccountStatement91> accountStat = docCamt053_001_08.getBkToCstmrStmt().getStmt();

		for (AccountStatement91 acc : accountStat) {
			SettlementReport settlementReport = new SettlementReport();

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

				settlementReport.setEntry_entrydtl_txdts_refs_acctsvcrref(
						repEntry.getNtryDtls().getTxDtls().get(0).getRefs().getAcctSvcrRef());
				settlementReport.setEntry_entrydtl_txdts_refs_instr_id(
						repEntry.getNtryDtls().getTxDtls().get(0).getRefs().getInstrId());
				settlementReport.setEntry_entrydtl_txdts_refs_endtoend_id(
						repEntry.getNtryDtls().getTxDtls().get(0).getRefs().getEndToEndId());
				settlementReport.setEntry_entrydtl_txdts_refs_tx_id(
						repEntry.getNtryDtls().getTxDtls().get(0).getRefs().getTxId());

				settlementReport.setEntry_entrydtl_txdts_amt(
						repEntry.getNtryDtls().getTxDtls().get(0).getAmt().getValue().toString());
				settlementReport
						.setEntry_entrydtl_txdts_cncy(repEntry.getNtryDtls().getTxDtls().get(0).getAmt().getCcy());
				settlementReport.setEntry_entrydtl_txdts_cd_dbt_ind(
						repEntry.getNtryDtls().getTxDtls().get(0).getCdtDbtInd().value());
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

	

	

}
