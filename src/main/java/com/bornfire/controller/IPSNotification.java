package com.bornfire.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.bornfire.config.ErrorResponseCode;
import com.bornfire.config.InformixConnectionManager;
import com.bornfire.config.Listener;
import com.bornfire.config.SequenceGenerator;
import com.bornfire.entity.BankAgentTable;
import com.bornfire.entity.BankAgentTableRep;
import com.bornfire.entity.CimCBSrequest;
import com.bornfire.entity.CimCBSrequestData;
import com.bornfire.entity.CimCBSrequestDatanew;
import com.bornfire.entity.CimCBSrequestHeader;
import com.bornfire.entity.CimCBSrequestNew;
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
import com.bornfire.entity.TranQRNotificationRep;
import com.bornfire.entity.TranQRNotificationTable;
import com.bornfire.entity.TransactionMonitor;
import com.bornfire.entity.TransactionMonitorRep;
import com.bornfire.entity.WalletAccessTableRep;
import com.bornfire.entity.WalletAccessTmpTableRep;
import com.bornfire.entity.WalletRequestRegisterTableRep;
import com.bornfire.exception.Connect24Exception;
import com.bornfire.exception.ServerErrorException;


@Service
@Transactional
public class IPSNotification {

	

	@Autowired
	RestTemplate restTemplate;
	
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
	TranQRNotificationRep tranQRNotificationRep;
	
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
	TransactionMonitorRep tranrep;
	
	private static final Logger logger = LoggerFactory.getLogger(IPSNotification.class);
	public static final String SERVER_ERROR = "CIM500:SOMETHING WENT WRONG AT SERVER END";


	
	public void sendNotification(String seqUniqueID) {
		try {

			logger.info(seqUniqueID + " :IPSX Rejected Sttatus");

			List<TranIPSTable> otmTranIPS = tranIPStableRep.findByIdCustom(seqUniqueID);
			String requestUUID = sequence.generateRequestUUId();
			String settlReceivableAccount=settlAccountRep.findByAccountNuber(env.getProperty("settl.receivable"));

			for (TranIPSTable item : otmTranIPS) {
				Optional<OutwardTransactionMonitoringTable> otm = outwardTranRep.findById(item.getSequence_unique_id());
				Optional<TransactionMonitor> otm1 = tranrep.findById(item.getSequence_unique_id());

				if (otm.isPresent()) {
					
					Query<Object[]> qr;
					Session hs = sessionFactory.getCurrentSession();
					qr = hs.createNativeQuery("select * from table(GetMerchantTranStatusbySequenceID(?1))");
					qr.setParameter(1, item.getSequence_unique_id());
					List<Object[]> result = qr.getResultList();
					logger.info("Inside checking valid transaction for sending notification"+result.get(0).toString());
					
					logger.info("outwardRJCT");
					OutwardTransactionMonitoringTable tm = otm.get();
					
					BankAgentTable remitterBank= findByBank(tm.getDbtr_agt());
					BankAgentTable benificiaryBank = findByBank(tm.getCdtr_agt());
					
					TranQRNotificationTable qrn = new TranQRNotificationTable();
					qrn.setSequence_unique_id(seqUniqueID);
					qrn.setRequest_uuid(requestUUID);
					logger.info(seqUniqueID + " :IPSX Rejected Sttatus1");
					qrn.setChannel_id( env.getProperty("cimCBS.channelID"));
					qrn.setService_request_version(env.getProperty("cimCBS.servicereqversion"));
					qrn.setService_request_id(env.getProperty("cimCBS.servicereqIDNotification"));
					qrn.setMessage_date_time(new Date());
					String sysTraceNumber = sequence.generateSystemTraceAuditNumber();
					logger.info(seqUniqueID + " :IPSX Rejected Sttatus2");
					qrn.setTran_no(sysTraceNumber);
					qrn.setInit_tran_no(tm.getResv_field1());
					qrn.setPost_to_cbs("False");
					logger.info(seqUniqueID + " :IPSX Rejected Sttatus3");
					//qrn.setTran_type(tran_type);
					//qrn.setIsreversal(isReversal);
					//qrn.setTran_no_from_cbs(tran_numberFromCbs);
					qrn.setCustomer_name(tm.getCim_account_name());
					qrn.setFrom_account_no(settlReceivableAccount);
					qrn.setTo_account_no(tm.getCim_account());
					qrn.setTran_amt(tm.getTran_amount());
					qrn.setTran_date(new Date());
					logger.info(seqUniqueID + " :IPSX Rejected Sttatus4");
					qrn.setTran_currency(tm.getTran_currency());
					qrn.setTran_particular_code("RTP");
				//	qrn.setDebit_remarks(debit_remarks);
					//qrn.setCredit_remarks(credit_remarks);
					qrn.setResv_field_1(tm.getResv_field1());
					qrn.setResv_field_2(tm.getResv_field2());
					qrn.setValue_date(new Date());
					qrn.setStatus(tm.getTran_status());
					//qrn.setSettl_acct_type(settlType);
					qrn.setInit_sub_tran_no(tm.getReq_unique_id());
					qrn.setError_code(tm.getIpsx_status_code());
					qrn.setError_msg(tm.getIpsx_status_error());
					logger.info(seqUniqueID + " :IPSX Rejected Sttatus5");
					qrn.setIps_master_ref_id(tm.getMaster_ref_id());
					qrn.setRemitterbank(remitterBank.getBank_name());
					qrn.setRemitterbankcode(remitterBank.getBank_code());
					qrn.setRemitterswiftcode(remitterBank.getBank_agent());
					qrn.setBeneficiarybank(benificiaryBank.getBank_name());
					qrn.setBeneficiarybankcode(benificiaryBank.getBank_code());
					qrn.setBeneficiaryswiftcode(benificiaryBank.getBank_agent());
					qrn.setTran_status(tm.getTran_status());
					
					
					if (result.size() > 0) {
						for (Object[] a : result) {
							logger.info(seqUniqueID + " :INSIDE SETTING NOTIFICATION VALUE FROM QR GEN TABLE");	
							qrn.setPid(a[0].toString());
							qrn.setDevice_id(a[1].toString());
							qrn.setInit_channel(a[2].toString());

						}
						}
					tranQRNotificationRep.saveAndFlush(qrn);


							
							taskExecutor.execute(new Runnable() {
								@Override
								public void run() {
									logger.info(seqUniqueID + " :INSIDE SENDING NOTIFICATION");
									
									ResponseEntity<CimCBSresponse> connect24Response = cbsResponseSuccess(requestUUID);
									QRNotificationRequest(connect24Response,tm.getSequence_unique_id());
								}
							});
														
							
						
						break;
				
				}
				else {
if (otm1.isPresent()) {
					
					Query<Object[]> qr;
					Session hs = sessionFactory.getCurrentSession();
					qr = hs.createNativeQuery("select * from table(GetMerchantTranStatusbySequenceID(?1))");
					qr.setParameter(1, item.getSequence_unique_id());
					List<Object[]> result = qr.getResultList();
					logger.info("Inside checking valid transaction for sending notification"+result.get(0).toString());
					
					logger.info("outwardRJCT");
					TransactionMonitor tm = otm1.get();
					
					BankAgentTable remitterBank= findByBank(tm.getDbtr_agt());
					BankAgentTable benificiaryBank = findByBank(tm.getCdtr_agt());
					
					TranQRNotificationTable qrn = new TranQRNotificationTable();
					qrn.setSequence_unique_id(seqUniqueID);
					qrn.setRequest_uuid(requestUUID);
					logger.info(seqUniqueID + " :IPSX Rejected Sttatus1");
					qrn.setChannel_id( env.getProperty("cimCBS.channelID"));
					qrn.setService_request_version(env.getProperty("cimCBS.servicereqversion"));
					qrn.setService_request_id(env.getProperty("cimCBS.servicereqIDNotification"));
					qrn.setMessage_date_time(new Date());
					String sysTraceNumber = sequence.generateSystemTraceAuditNumber();
					logger.info(seqUniqueID + " :IPSX Rejected Sttatus2");
					qrn.setTran_no(sysTraceNumber);
					//qrn.setInit_tran_no(tm.getResv_field1());
					qrn.setPost_to_cbs("False");
					logger.info(seqUniqueID + " :IPSX Rejected Sttatus3");
					//qrn.setTran_type(tran_type);
					//qrn.setIsreversal(isReversal);
					//qrn.setTran_no_from_cbs(tran_numberFromCbs);
					qrn.setCustomer_name(tm.getBob_account_name());
					qrn.setFrom_account_no(settlReceivableAccount);
					qrn.setTo_account_no(tm.getBob_account());
					qrn.setTran_amt(tm.getTran_amount());
					qrn.setTran_date(new Date());
					logger.info(seqUniqueID + " :IPSX Rejected Sttatus4");
					qrn.setTran_currency(tm.getTran_currency());
					qrn.setTran_particular_code("RTP");
				//	qrn.setDebit_remarks(debit_remarks);
					//qrn.setCredit_remarks(credit_remarks);
					//qrn.setResv_field_1(tm.getResv_field1());
					//qrn.setResv_field_2(tm.getResv_field2());
					qrn.setValue_date(new Date());
					qrn.setStatus(tm.getTran_status());
					//qrn.setSettl_acct_type(settlType);
					//qrn.setInit_sub_tran_no(tm.getReq_unique_id());
					qrn.setError_code(tm.getIpsx_status_code());
					qrn.setError_msg(tm.getIpsx_status_error());
					logger.info(seqUniqueID + " :IPSX Rejected Sttatus5");
					qrn.setIps_master_ref_id(tm.getBob_message_id());
					qrn.setRemitterbank(remitterBank.getBank_name());
					qrn.setRemitterbankcode(remitterBank.getBank_code());
					qrn.setRemitterswiftcode(remitterBank.getBank_agent());
					qrn.setBeneficiarybank(benificiaryBank.getBank_name());
					qrn.setBeneficiarybankcode(benificiaryBank.getBank_code());
					qrn.setBeneficiaryswiftcode(benificiaryBank.getBank_agent());
					qrn.setTran_status(tm.getTran_status());
					
					
					if (result.size() > 0) {
						for (Object[] a : result) {
							logger.info(seqUniqueID + " :INSIDE SETTING NOTIFICATION VALUE FROM QR GEN TABLE");	
							qrn.setPid(a[0].toString());
							qrn.setDevice_id(a[1].toString());
							qrn.setInit_channel(a[2].toString());

						}
						}
					tranQRNotificationRep.saveAndFlush(qrn);


							
							taskExecutor.execute(new Runnable() {
								@Override
								public void run() {
									logger.info(seqUniqueID + " :INSIDE SENDING NOTIFICATION");
									
									ResponseEntity<CimCBSresponse> connect24Response = cbsResponseSuccess(requestUUID);
									QRNotificationRequest(connect24Response,tm.getSequence_unique_id());
								}
							});
														
							
						
						break;
				
				}
				}
					}
			
			

				
		} catch (Exception e) {
			System.err.println(e.getMessage());
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

	
	public  String  QRNotificationRequest(ResponseEntity<CimCBSresponse> requestUUID1, String requestUUID) {
		try {

			logger.info(requestUUID + " :INSIDE UPDATING RESPONSE");
						///// Call ESB Connection
			ResponseEntity<CimCBSresponse> connect24Response = requestUUID1;

						logger.debug("CBS Data:" + connect24Response.toString());
						if (connect24Response.getStatusCode() == HttpStatus.OK) {

							if (!connect24Response.toString().equals("<200 OK OK,[]>")) {
								if (connect24Response.getBody().getStatus().getIsSuccess()) {

									//// Update ESB Data
									updateCIMcbsData(requestUUID, TranMonitorStatus.SUCCESS.toString(),
											connect24Response.getBody().getStatus().getStatusCode(),
											connect24Response.getBody().getStatus().getMessage(),
											connect24Response.getBody().getData().getTransactionNoFromCBS());

									

									return "Success";
								} else {

									//// Update ESB Data
									updateCIMcbsData(requestUUID, TranMonitorStatus.FAILURE.toString(),
											connect24Response.getBody().getStatus().getStatusCode(),
											connect24Response.getBody().getStatus().getMessage(),
											connect24Response.getBody().getData().getTransactionNoFromCBS());

									throw new Connect24Exception(connect24Response.getBody().getStatus().getStatusCode() + ":"
											+ connect24Response.getBody().getStatus().getMessage());

								}
							} else {
								//// Update ESB Data to CIM Table
								updateCIMcbsData(requestUUID, TranMonitorStatus.FAILURE.toString(),
										String.valueOf(connect24Response.getStatusCodeValue()), "No Response return from CBS",
										"");
								throw new ServerErrorException(SERVER_ERROR);

							}

						} else {
							//// Update ESB Data to CIM Table
							updateCIMcbsData(requestUUID, TranMonitorStatus.FAILURE.toString(),
									String.valueOf(connect24Response.getStatusCodeValue()), "Internal Server Error", "");
							throw new ServerErrorException(SERVER_ERROR);

						}

				

				} catch (RemoteAccessException e) {
					throw new ServerErrorException(SERVER_ERROR);
				}
		}
	
	public ResponseEntity<CimCBSresponse> cbsResponseSuccess(String requestUUID) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
		TranQRNotificationTable data = tranQRNotificationRep.findById(requestUUID).get();

		
		logger.info(requestUUID + " :INSIDE CALLING NOTIFICATION API");
		CimCBSrequestNew cimCBSrequest=new CimCBSrequestNew();
		
		CimCBSrequestHeader cimCBSrequestHeader=new CimCBSrequestHeader();
		cimCBSrequestHeader.setRequestUUId(data.getRequest_uuid());
		cimCBSrequestHeader.setChannelId(data.getChannel_id());
		cimCBSrequestHeader.setServiceRequestVersion(data.getService_request_version());
		cimCBSrequestHeader.setServiceRequestId(data.getService_request_id());
		cimCBSrequestHeader.setMessageDateTime(listener.convertDateToGreDate(data.getMessage_date_time(), "2").toString());
		cimCBSrequestHeader.setCountryCode(env.getProperty("cimCBS.countryCode"));
		cimCBSrequest.setHeader(cimCBSrequestHeader);
		
		CimCBSrequestDatanew cimCBSrequestData=new CimCBSrequestDatanew();
		cimCBSrequestData.setTransactionNo(data.getTran_no());
		cimCBSrequestData.setInitiatingChannel(data.getInit_channel());
		cimCBSrequestData.setInitatorTransactionNo(data.getInit_tran_no());
		cimCBSrequestData.setPostToCBS(Boolean.FALSE);
		cimCBSrequestData.setTransactionType("");
		cimCBSrequestData.setIsReversal("");
		cimCBSrequestData.setTransactionNoFromCBS("");
		cimCBSrequestData.setCustomerName(data.getCustomer_name());
		cimCBSrequestData.setFromAccountNo(data.getFrom_account_no());
		cimCBSrequestData.setToAccountNo(data.getTo_account_no());
		cimCBSrequestData.setTransactionAmount(new BigDecimal(data.getTran_amt().toString()));
		cimCBSrequestData.setTransactionDate(new SimpleDateFormat("yyyy-MM-dd").format(data.getTran_date()));
		cimCBSrequestData.setTransactionCurrency(data.getTran_currency());
		cimCBSrequestData.setTransactionParticularCode(data.getTran_particular_code());
		cimCBSrequestData.setCreditRemarks("");
		cimCBSrequestData.setDebitRemarks("");
		cimCBSrequestData.setReservedField1(data.getResv_field_1());
		cimCBSrequestData.setReservedField2(data.getResv_field_2());
		
		cimCBSrequestData.setInitatorSubTransactionNo((data.getInit_sub_tran_no()==null)?"":data.getInit_sub_tran_no());
		cimCBSrequestData.setErrorCode((data.getError_code()==null)?"":data.getError_code());
		cimCBSrequestData.setErrorMessage((data.getError_msg()==null)?"":data.getError_msg());
		cimCBSrequestData.setIpsMasterRefId((data.getIps_master_ref_id()==null)?"":data.getIps_master_ref_id());
		cimCBSrequestData.setRemitterBank(data.getRemitterbank());
		cimCBSrequestData.setRemitterBankCode(data.getRemitterbankcode());
		cimCBSrequestData.setRemitterSwiftCode(data.getRemitterswiftcode());
		cimCBSrequestData.setBeneficiaryBank(data.getBeneficiarybank());
		cimCBSrequestData.setBeneficiaryBankCode(data.getBeneficiarybankcode());
		cimCBSrequestData.setBeneficiarySwiftCode(data.getBeneficiaryswiftcode());
		cimCBSrequestData.setNotificationChannel("IVERI");
		if(data.getTran_status().equals("SUCCESS")) {
			cimCBSrequestData.setIpsxTranStatus("TRUE");
		}else {
			cimCBSrequestData.setIpsxTranStatus("FALSE");
		}
		cimCBSrequestData.setpId(data.getPid());
		cimCBSrequestData.setPsuDeviceId(data.getDevice_id());
		cimCBSrequest.setData(cimCBSrequestData);
		

		logger.debug(cimCBSrequest.toString());
		HttpEntity<CimCBSrequestNew> entity = new HttpEntity<>(cimCBSrequest, httpHeaders);
				
		ResponseEntity<CimCBSresponse> response = null;
		try {
			logger.info("Sending message to CBS Notification using restTemplate Success");
			response = restTemplate.postForEntity(env.getProperty("cimESB.url")+"appname="+env.getProperty("cimESB.appname")+"&prgname="+env.getProperty("cimESB.prgname")+"&arguments="+env.getProperty("cimESB.arguments"),
					entity, CimCBSresponse.class);

			
			return new ResponseEntity<>(response.getBody(), HttpStatus.OK);

		} catch (HttpClientErrorException ex) {
			logger.debug("HttpClient"+ex.getStatusCode());
			logger.debug("Exception"+ex.getLocalizedMessage());
			CimCBSresponse cbsResponse=new CimCBSresponse();
			return new ResponseEntity<>(cbsResponse, HttpStatus.BAD_REQUEST);
		} catch (HttpServerErrorException ex) {
			CimCBSresponse cbsResponse=new CimCBSresponse();
			return new ResponseEntity<>(cbsResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}catch (Exception ex) {
			CimCBSresponse cbsResponse=new CimCBSresponse();
			return new ResponseEntity<>(cbsResponse, HttpStatus.BAD_REQUEST);
		}

	}
	
	
public void updateCIMcbsData(String requestUUID, String status, String statusCode, String message,String tranNoFromCBS) {
		
	tranQRNotificationRep.updateCIMcbsData(requestUUID,status,statusCode,message,tranNoFromCBS,
				new SimpleDateFormat("dd-MMM-yyyy").format(new Date()));
			}

public boolean invalidTran_ID(String pid) {
	boolean valid = false;
	logger.info("Inside checking valid transaction for sending notification");
	try {
		Optional<OutwardTransactionMonitoringTable> otm = outwardTranRep.findById(pid);
	
		if (otm.isPresent()) {
			OutwardTransactionMonitoringTable tm = otm.get();
			if(tm.getTran_type_code().equals("300") || (tm.getTran_type_code().equals("101") && (!tm.getCdtr_agt().equals(env.getProperty("ipsx.bicfi"))))) {
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


public boolean valid_PACS002(String pid) {
	boolean valid = false;
	logger.info("Inside checking valid Pacs.002");
	try {
		Optional<OutwardTransactionMonitoringTable> otm = outwardTranRep.findById(pid);
	
		Optional<TransactionMonitor> otm1 = tranRep.findById(pid);
		
		if (otm.isPresent()) {
			OutwardTransactionMonitoringTable tm = otm.get();
			logger.info("Inside checking valid Pacs.002 inside OTM");
			if(!String.valueOf(tm.getResponse_status()).equals("null")	&& !String.valueOf(tm.getResponse_status()).equals("")) {
				valid=false;
				return valid;
			}else {
				valid = true;
				return valid;	
			}
			
		}else if(otm1.isPresent()) {
			TransactionMonitor tm1 = otm1.get();
			logger.info("Inside checking valid Pacs.002 inside TM");
			if(!String.valueOf(tm1.getResponse_status()).equals("null")	&& !String.valueOf(tm1.getResponse_status()).equals("")) {
				valid=false;
				return valid;
			}else {
				valid = true;
				return valid;	
			}
		}
		
		
		else {
			valid = true;
			return valid;
		}
	} catch (Exception e) {
		System.err.println(e.getMessage());
	}

	return valid;
}


}

