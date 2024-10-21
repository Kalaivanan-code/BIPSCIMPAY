package com.bornfire.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.bornfire.clientService.IPSXClient;
import com.bornfire.config.ErrorResponseCode;
import com.bornfire.config.SequenceGenerator;
import com.bornfire.entity.BankAgentTable;
import com.bornfire.entity.BukCreditTransferRequest;
import com.bornfire.entity.BukOutgoingCreditTransferRequest;
import com.bornfire.entity.CimCBSresponse;
import com.bornfire.entity.MCCreditTransferResponse;
import com.bornfire.entity.SettlementAccountRep;
import com.bornfire.entity.TranMonitorStatus;

@Component
public class IPSOutgoingConnection {

	

	private static final Logger logger = LoggerFactory.getLogger(IPSOutgoingConnection.class);

	@Autowired
	Connect24Service connect24Service;

	@Autowired
	IPSXClient ipsxClient;

	@Autowired
	SequenceGenerator sequence;
	
	@Autowired
	IPSDao ipsDao;

	@Autowired
	ErrorResponseCode errorCode;

	@Autowired
	@Qualifier("asyncExecutor")
	TaskExecutor taskExecutor;

	@Autowired
	Environment env;

	@Autowired
	SettlementAccountRep settlAccountRep;
	
////Bulk Credit Transaction
	public MCCreditTransferResponse createBulkCreditConnection(String psuDeviceID, String psuIpAddress, String psuID,
			BukOutgoingCreditTransferRequest mcCreditTransferRequest, String userID, String p_id, String channel_ID,
			String resv_field1, String resv_field2,CimCBSresponse connect24Response) {

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

				List<String> endToEndIdList = new ArrayList<>();
				List<String> msgIdList = new ArrayList<>();
				List<String> msgNetMirList = new ArrayList<>();

				//// Get Remitter Bank Code
				String remitterBankCode = ipsDao.getOtherBankCode(env.getProperty("ipsx.dbtragt"));

				///// Get Bank Agent Name and Agent Account Number
				BankAgentTable RemitterBankAgent = ipsDao.getOtherBankAgent(remitterBankCode);

				///// Register Bulk Credit Record
				logger.info("Register Initial bulk credit Record" + mcCreditTransferRequest.getToAccountList().size());
				for (int i = 0; i < mcCreditTransferRequest.getToAccountList().size(); i++) {
					int srlNo = i + 1;

					//// Generate Msg Sequence
					String msgSeq = sequence.generateMsgSequence();
					///// Generate EndToEnd ID
					String endTOEndID = env.getProperty("ipsx.bicfi")
							+ new SimpleDateFormat("yyyyMMdd").format(new Date()) + msgSeq;
					///// Net Mir
					String msgNetMir = new SimpleDateFormat("yyMMdd").format(new Date()) + env.getProperty("ipsx.user")
							+ "0001" + msgSeq;

					///// Get Other Bank Agent and Agent Account
					BankAgentTable othBankAgent = ipsDao
							.getOtherBankAgent(mcCreditTransferRequest.getToAccountList().get(i).getBankCode());

					ipsDao.RegisterBulkCreditRecord(psuDeviceID, psuIpAddress, psuID, sysTraceNumber,
							mcCreditTransferRequest.getFrAccount().getAcctNumber(),
							mcCreditTransferRequest.getFrAccount().getAcctName(),
							mcCreditTransferRequest.getToAccountList().get(i).getAcctNumber(),
							mcCreditTransferRequest.getToAccountList().get(i).getAcctName(),
							mcCreditTransferRequest.getToAccountList().get(i).getBankCode(),
							mcCreditTransferRequest.getToAccountList().get(i).getTrAmt(),
							mcCreditTransferRequest.getCurrencyCode(), bobMsgID + "/" + srlNo,
							seqUniqueID + "/" + srlNo, "100", seqUniqueID, endTOEndID, msgNetMir,
							env.getProperty("ipsx.bicfi"), othBankAgent.getBank_agent(),
							env.getProperty("ipsx.dbtragt"), env.getProperty("ipsx.dbtragtacct"),
							othBankAgent.getBank_agent(), othBankAgent.getBank_agent_account(),
							seqUniqueID + "/" + srlNo, "0100", "CSDC", "100", userID,
							mcCreditTransferRequest.getToAccountList().get(i).getTrRmks(), p_id,
							mcCreditTransferRequest.getToAccountList().get(i).getReqUniqueID(), channel_ID, resv_field1,
							resv_field2, remitterBankCode);

					endToEndIdList.add(endTOEndID);
					msgIdList.add(msgSeq);
					msgNetMirList.add(msgNetMir);

				}

				logger.info("Register Initial bulk" + mcCreditTransferRequest.getToAccountList().size());

				//// Generate RequestUUID
				String requestUUID = sequence.generateRequestUUId();

				/********** ESB *********/
				//// Store ESB Registration Data
				//// Register ESB Data
				String settlPayableAccount = settlAccountRep.findById("04").get().getAccount_number();
				ipsDao.registerCIMcbsIncomingData(requestUUID, env.getProperty("cimCBS.channelID"),
						env.getProperty("cimCBS.servicereqversion"), env.getProperty("cimCBS.servicereqID"), new Date(),
						sysTraceNumber, env.getProperty("cimCBS.outDBChannel"), p_id, "True", "DR", "N", "",
						settlPayableAccount, mcCreditTransferRequest.getTotAmt(),
						mcCreditTransferRequest.getCurrencyCode(), seqUniqueID,
						mcCreditTransferRequest.getFrAccount().getAcctNumber(),
						mcCreditTransferRequest.getFrAccount().getAcctName(), "NRT",
						mcCreditTransferRequest.getToAccountList().get(0).getTrRmks(), "", "", "", new Date(),
						"PAYABLE", "", "", "", "", RemitterBankAgent.getBank_agent(),
						RemitterBankAgent.getBank_agent());

				///// Call ESB Connection
			//	ResponseEntity<CimCBSresponse> connect24Response = cimCBSservice.dbtFundRequest(requestUUID);

				logger.debug("CBS Data:" + connect24Response.toString());
			

						logger.info(seqUniqueID + ": success" + connect24Response.getStatus().getIsSuccess()
								+ ":" + connect24Response.getStatus().getMessage());

					

							//// Update ESB Data
							ipsDao.updateCIMcbsData(requestUUID, TranMonitorStatus.SUCCESS.toString(),
									connect24Response.getStatus().getStatusCode(),
									connect24Response.getStatus().getMessage(),
									connect24Response.getData().getTransactionNoFromCBS());

							//// Update CBS Status to Tran Table
							ipsDao.updateOutwardCBSStatusBulkCredit(seqUniqueID,
									TranMonitorStatus.CBS_DEBIT_OK.toString(),
									TranMonitorStatus.IN_PROGRESS.toString());

							///// Calling IPSX
							/*
							 * ipsxClient.sendftRequst( mcCreditTransferRequest, sysTraceNumber, cimMsgID,
							 * seqUniqueID, othBankAgent, msgSeq, endTOEndID, msgNetMir);
							 */

							logger.info("Calling IPSX");
							for (int i = 0; i < mcCreditTransferRequest.getToAccountList().size(); i++) {

								///// Get SRL Number
								int srlNo = i + 1;

								///// Get Other Bank Agent and Agent Account
								BankAgentTable othBankAgent = ipsDao.getOtherBankAgent(
										mcCreditTransferRequest.getToAccountList().get(i).getBankCode());
								logger.info("Size" + mcCreditTransferRequest.getToAccountList().size());
								logger.info(othBankAgent.getBank_agent(), "" + othBankAgent.getBank_agent_account());

								///// Calling IPSX
								logger.info("Calling IPSX");
								MCCreditTransferResponse response = ipsxClient.sendBulkCreditRequest(

										mcCreditTransferRequest.getFrAccount().getAcctName(),
										mcCreditTransferRequest.getFrAccount().getAcctNumber(),
										mcCreditTransferRequest.getToAccountList().get(i).getAcctName(),
										mcCreditTransferRequest.getToAccountList().get(i).getAcctNumber(),
										mcCreditTransferRequest.getToAccountList().get(i).getTrAmt(),
										mcCreditTransferRequest.getCurrencyCode(), sysTraceNumber,
										bobMsgID + "/" + srlNo, seqUniqueID + "/" + srlNo, othBankAgent,
										msgIdList.get(i), endToEndIdList.get(i), msgNetMirList.get(i));

								logger.info("okk");

							}

						
				

			

			}
		});

		mcCreditTransferResponse = new MCCreditTransferResponse(seqUniqueID,
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()));

		return mcCreditTransferResponse;

	}

}
