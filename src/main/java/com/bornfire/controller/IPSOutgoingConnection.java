package com.bornfire.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.bornfire.clientService.IPSXClient;
import com.bornfire.config.ErrorResponseCode;
import com.bornfire.config.SequenceGenerator;
import com.bornfire.entity.BankAgentTable;
import com.bornfire.entity.BukOutgoingCreditTransferRequest;
import com.bornfire.entity.CimCBSrequestGL;
import com.bornfire.entity.CimCBSrequestGLData;
import com.bornfire.entity.CimCBSrequestGLDataTranDet;
import com.bornfire.entity.CimCBSrequestGLHeader;
import com.bornfire.entity.CimCBSresponse;
import com.bornfire.entity.MCCreditTransferResponse;
import com.bornfire.entity.OutwardTransactionMonitoringTable;
import com.bornfire.entity.OutwardTransactionMonitoringTableRep;
import com.bornfire.entity.SettlementAccountRep;
import com.bornfire.entity.TranMonitorStatus;
import com.bornfire.entity.Outgoing.BipsBulkOutgoingTransaction;
import com.bornfire.entity.Outgoing.BipsBulkOutgoingTransactionRepository;
import com.bornfire.entity.Outgoing.BipsOutgoingGLEntryEntity;
import com.bornfire.entity.Outgoing.BipsOutgoingGLEntryRep;
import com.bornfire.entity.Outgoing.Business_central_bank_detail_entity;
import com.bornfire.entity.Outgoing.CimGLOutgoingResponse;
import com.bornfire.entity.Outgoing.CreditBankAgentTableRep;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
	
	@Autowired
	OutwardTransactionMonitoringTableRep outwardTranRep;
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	CreditBankAgentTableRep creditbankAgentTablerep;
	
	@Autowired
	BipsBulkOutgoingTransactionRepository bipsbulkoutgoingTransactionRepository;
	
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
						RemitterBankAgent.getBank_agent(),"");

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


public boolean invalidOutgoingTran_ID(String pid) {
	boolean valid = false;
	logger.info("Inside checking reversal transaction for Outgoing Transaction");
	try {
		Optional<OutwardTransactionMonitoringTable> otm = outwardTranRep.getIncomindCreditExist(pid);
	
		if (otm.isPresent()) {
			OutwardTransactionMonitoringTable tm = otm.get();
			List<BipsBulkOutgoingTransaction>  botm = bipsbulkoutgoingTransactionRepository.findByIdDocRefID(tm.getP_id(),tm.getReq_unique_id());
			if(tm.getTran_type_code().equals("100") && (tm.getDbtr_agt().equals(env.getProperty("ipsx.bicfi"))) && (tm.getInstg_agt().equals(env.getProperty("ipsx.bicfi"))) && !botm.isEmpty()) {
				valid=true;
				return valid;
			}else {
				valid = false;
				return valid;	
			}
			
		} else {
			valid = true;
			return valid;
		}
	} catch (Exception e) {
		System.err.println(e.getMessage());
	}

	return valid;
}

@Autowired
BipsOutgoingGLEntryRep bipsGlentry;

public ResponseEntity<CimGLOutgoingResponse> sendreversal(String pid, String ipsxerrorDesc, String ipsxMsgID,
		String tranStatus, String ipsXStatus, String ipsxResponseStatus, String ipsxErrorCode) {
	
	ResponseEntity<CimGLOutgoingResponse> response1 = null;
	logger.info("Inside checking reversal transaction for Outgoing Transaction");
	try {
		Optional<OutwardTransactionMonitoringTable> otm = outwardTranRep.getIncomindCreditExist(pid);
	
		if (otm.isPresent()) {
			OutwardTransactionMonitoringTable tm = otm.get();
			List<BipsOutgoingGLEntryEntity> bulkTranList = bipsGlentry.getOutgoingCreditList(tm.getP_id(),tm.getReq_unique_id());
			 response1 = postPaymentforoutgoingGLInc(bulkTranList,tm.getEntry_user());
			 tm.setIpsx_message_id(ipsxMsgID);
				tm.setIpsx_status(ipsXStatus);
				tm.setIpsx_status_error(ipsxerrorDesc);
				tm.setResponse_status(ipsxResponseStatus);
				tm.setIpsx_response_time(new Date());
				tm.setTran_status(tranStatus);
				tm.setIpsx_status_code(ipsxErrorCode);
				tm.setMconnectin("N");
				tm.setCbs_status("CBS_DEBIT_REVERSE_OK");

				outwardTranRep.saveAndFlush(tm);

		} 
	} catch (Exception e) {
		System.err.println(e.getMessage());
	}

	return response1;
}
	
	
public ResponseEntity<CimGLOutgoingResponse> postPaymentforoutgoingGLInc(List<BipsOutgoingGLEntryEntity> tranList,String userID) {
	HttpHeaders httpHeaders = new HttpHeaders();
	httpHeaders.setContentType(MediaType.APPLICATION_JSON);
	
	logger.info("Sending REVERSAL MSG to ESB for OUTGOING TRANSACTION");
	////Get Data from Table
	List<BipsOutgoingGLEntryEntity> data=tranList;
	
	/// Generate RequestUUID
			String requestUUID = sequence.generateRequesttranUUId();
			
			
	/////////////////////Request Body Creation/////////////////////////////
	CimCBSrequestGL cimCBSrequest=new CimCBSrequestGL();
	
	CimCBSrequestGLHeader cimCBSrequestHeader=new CimCBSrequestGLHeader();
	cimCBSrequestHeader.setRequestUUId(requestUUID);
	cimCBSrequestHeader.setChannelId(env.getProperty("cimOutGL.channelID"));
	cimCBSrequestHeader.setServiceRequestVersion(env.getProperty("cimOutGL.servicereqversion"));
	cimCBSrequestHeader.setServiceRequestId(env.getProperty("cimOutgoingGL.servicereqID"));
	cimCBSrequestHeader.setMessageDateTime(convertDateToGreDate(new Date(), "2").toString());
	cimCBSrequestHeader.setCountryCode(env.getProperty("cimOutGL.countryCode"));
	cimCBSrequest.setHeader(cimCBSrequestHeader);
	
	String tranNo = sequence.generateSystemTraceAuditNumber();

	/// Generate Batch No
	String batchNo = sequence.generateGLBatchNumber();
	
	CimCBSrequestGLData cimCBSrequestData=new CimCBSrequestGLData();
	cimCBSrequestData.setTransactionNo(tranNo);
	cimCBSrequestData.setBatchNo(batchNo);
	cimCBSrequestData.setModule(env.getProperty("cimOutGL.module"));
			
	List<CimCBSrequestGLDataTranDet> tranDetails=new ArrayList<CimCBSrequestGLDataTranDet>();
	 
	for(BipsOutgoingGLEntryEntity data1 :data) {
		Business_central_bank_detail_entity gl = creditbankAgentTablerep.Findaccountindividual(data1.getAccount_no());
		
		CimCBSrequestGLDataTranDet tranData=new CimCBSrequestGLDataTranDet();
		tranData.setSerialNumber(Integer.parseInt(data1.getDoc_sub_id()));
		tranData.setTransactionType(data1.getTran_type());
		tranData.setAccountNo(gl.getBc_gl_id());
		tranData.setAccountType(data1.getAcct_type());
		tranData.setTransactionAmount(data1.getTran_amt().toString());
		tranData.setCurrencyCode(data1.getTran_crncy_code());
		//tranData.setPostingDate(new SimpleDateFormat("yyyy-MM-dd").format(data1.getPosting_date1()));
		tranData.setPostingDate(convertDateToGreDate(data1.getTran_date(),"3").toString());
		
		tranData.setTransactionCode(gl.getBc_dimension_id());
		tranData.setTransactionDescription(data1.getTran_particular());
		tranData.setTransactionRemarks(data1.getTran_particular());
		tranData.setRate("0");
		tranDetails.add(tranData);
	}
	cimCBSrequestData.setTransactionDetails(tranDetails);
	
	cimCBSrequest.setData(cimCBSrequestData);
	
	logger.debug(cimCBSrequest.toString());
	logger.debug(generateJsonFormat1(cimCBSrequest));
///////////////////////////////////////////////////

	HttpEntity<CimCBSrequestGL> entity = new HttpEntity<>(cimCBSrequest, httpHeaders);
	
	/////Call REST API
	ResponseEntity<CimGLOutgoingResponse> response = null;
	try {
	
		response = restTemplate.postForEntity(env.getProperty("cimESBGL.url")+"appname="+env.getProperty("cimESBGL.appname")+"&prgname="+env.getProperty("cimESBGL.prgname")+"&arguments="+env.getProperty("cimESBGL.arguments"),
				entity, CimGLOutgoingResponse.class);
		logger.debug("Done");
		return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
	} catch (HttpClientErrorException ex) {
		logger.debug("HttpClient"+ex.getStatusCode());
		logger.debug("Exception"+ex.getLocalizedMessage());
		CimGLOutgoingResponse cbsResponse=new CimGLOutgoingResponse();
		return new ResponseEntity<>(cbsResponse, HttpStatus.BAD_REQUEST);
	} catch (HttpServerErrorException ex) {
		logger.debug("HttpServerErrorException"+ex.getStatusCode());
		logger.debug("Exception"+ex.getLocalizedMessage());
		CimGLOutgoingResponse cbsResponse=new CimGLOutgoingResponse();
		return new ResponseEntity<>(cbsResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}catch (Exception ex) {
		logger.debug("Exception"+ex.getLocalizedMessage());
		CimGLOutgoingResponse cbsResponse=new CimGLOutgoingResponse();
		return new ResponseEntity<>(cbsResponse, HttpStatus.BAD_REQUEST);
	}

}

public XMLGregorianCalendar convertDateToGreDate(Date date, String type) {
	String dataFormat = null;
	switch (type) {
	case "0":
		dataFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(date);
		break;
	case "1":
		dataFormat = new SimpleDateFormat("yyyy-MM-dd").format(date);
		break;
	case "2":
		dataFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000").format(date);
		break;
	case "3":
		dataFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(date);
		break;
	}

	XMLGregorianCalendar xgc = null;
	try {
		xgc = DatatypeFactory.newInstance().newXMLGregorianCalendar(dataFormat);
	} catch (DatatypeConfigurationException e) {
		e.printStackTrace();
	}
	return xgc;
}


public String generateJsonFormat1(CimCBSrequestGL msg) {
	// Creating the ObjectMapper object
	ObjectMapper mapper = new ObjectMapper();
	// Converting the Object to JSONString
	String jsonString = "";
	try {
		jsonString = mapper.writeValueAsString(msg);
	} catch (JsonProcessingException e) {
		e.printStackTrace();
	}

	return jsonString;
}
}
