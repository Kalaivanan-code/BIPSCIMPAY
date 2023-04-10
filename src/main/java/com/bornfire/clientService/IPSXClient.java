package com.bornfire.clientService;

/*CREATED BY	: KALAIVANAN RAJENDRAN.R
CREATED ON	: 16-JAN-2020
PURPOSE		: IPSX Client(Calling IPSX Web service and processing Inward Transaction)
*/

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import com.bornfire.config.ErrorResponseCode;
import com.bornfire.config.SequenceGenerator;
import com.bornfire.controller.CimCBSservice;
import com.bornfire.controller.Connect24Service;
import com.bornfire.controller.IPSConnection;
import com.bornfire.controller.IPSDao;
import com.bornfire.entity.BankAgentTable;
import com.bornfire.entity.BankAgentTableRep;
import com.bornfire.entity.C24FTResponse;
import com.bornfire.entity.CIMCreditTransferRequest;
import com.bornfire.entity.CIMMerchantDirectFndRequest;
import com.bornfire.entity.CimCBSresponse;
import com.bornfire.entity.CreditTransferTransaction;
import com.bornfire.entity.DocType;
import com.bornfire.entity.MCCreditTransferRequest;
import com.bornfire.entity.MCCreditTransferResponse;
import com.bornfire.entity.MerchantMaster;
import com.bornfire.entity.MerchantMasterRep;
import com.bornfire.entity.SettlementLimitReportRep;
import com.bornfire.entity.SettlementLimitReportTable;
import com.bornfire.entity.SettlementLimitResponse;
import com.bornfire.entity.TranIPSTable;
import com.bornfire.entity.TranIPSTableRep;
import com.bornfire.entity.TranMonitorStatus;
import com.bornfire.entity.TransactionMonitor;
import com.bornfire.entity.TransactionMonitorRep;
import com.bornfire.entity.UpdateCBSIncProceRep;
import com.bornfire.exception.CustomWebServiceIOException;
import com.bornfire.exception.IPSXException;
import com.bornfire.exception.ServerErrorException;
import com.bornfire.jaxb.pacs_002_001_010.FIToFIPaymentStatusReportV10;
import com.bornfire.jaxb.pacs_002_001_010.PaymentTransaction1101;
import com.bornfire.jaxb.pacs_002_001_010.StatusReason6Choice1;
import com.bornfire.jaxb.pacs_002_001_010.StatusReasonInformation121;
import com.bornfire.jaxb.pacs_008_001_08.AccountIdentification4Choice1;
import com.bornfire.jaxb.pacs_008_001_08.ActiveCurrencyAndAmount;
import com.bornfire.jaxb.pacs_008_001_08.BranchAndFinancialInstitutionIdentification61;
import com.bornfire.jaxb.pacs_008_001_08.CashAccount381;
import com.bornfire.jaxb.pacs_008_001_08.ChargeBearerType1Code;
import com.bornfire.jaxb.pacs_008_001_08.ClearingChannel2Code1;
import com.bornfire.jaxb.pacs_008_001_08.ClearingSystemMemberIdentification21;
import com.bornfire.jaxb.pacs_008_001_08.CreditTransferTransaction391;
import com.bornfire.jaxb.pacs_008_001_08.Document;
import com.bornfire.jaxb.pacs_008_001_08.FIToFICustomerCreditTransferV08;
import com.bornfire.jaxb.pacs_008_001_08.FinancialInstitutionIdentification181;
import com.bornfire.jaxb.pacs_008_001_08.GenericAccountIdentification11;
import com.bornfire.jaxb.pacs_008_001_08.GroupHeader931;
import com.bornfire.jaxb.pacs_008_001_08.PartyIdentification1351;
import com.bornfire.jaxb.pacs_008_001_08.PaymentIdentification71;
import com.bornfire.jaxb.pacs_008_001_08.PaymentTypeInformation281;
import com.bornfire.jaxb.pacs_008_001_08.ReferredDocumentInformation71;
import com.bornfire.jaxb.pacs_008_001_08.ReferredDocumentType41;
import com.bornfire.jaxb.pacs_008_001_08.RegulatoryReporting31;
import com.bornfire.jaxb.pacs_008_001_08.RemittanceInformation161;
import com.bornfire.jaxb.pacs_008_001_08.StructuredRemittanceInformation161;
import com.bornfire.jaxb.pain_001_001_09.CustomerCreditTransferInitiationV09;
import com.bornfire.jaxb.pain_001_001_09.GenericOrganisationIdentification11;
import com.bornfire.jaxb.pain_001_001_09.PaymentInstruction301;
import com.bornfire.jaxb.pain_001_001_09.StructuredRegulatoryReporting31;
import com.bornfire.jaxb.pain_002_001_10.CustomerPaymentStatusReportV10;
import com.bornfire.jaxb.wsdl.ParamsMtMsg;
import com.bornfire.jaxb.wsdl.SendResponse;
import com.bornfire.jaxb.wsdl.SendT;
import com.bornfire.messagebuilder.DocumentPacks;
import com.bornfire.messagebuilder.ParamMTmessage;
import com.bornfire.messagebuilder.SignDocument;

import static com.bornfire.exception.ErrorResponseCode.*;

public class IPSXClient extends WebServiceGatewaySupport {

	private static final Logger logger = LoggerFactory.getLogger(IPSXClient.class);

	@Autowired
	Environment env;

	@Autowired
	TransactionMonitorRep tranRep;

	@Autowired
	ParamMTmessage paramMTMsgs;

	@Autowired
	DocumentPacks docPacs;

	@Autowired
	SequenceGenerator sequence;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	Connect24Service connect24Service;

	@Autowired
	TaskExecutor taskExecutor;

	@Autowired
	SettlementLimitReportRep settlementLimitReportRep;

	@Autowired
	IPSDao ipsDao;

	@Autowired
	SignDocument signDocument;

	@Autowired
	IPSConnection ipsConnection;

	@Autowired
	TranIPSTableRep tranIPSTableRep;
	
	@Autowired
	ErrorResponseCode errorCode;
	
	
	@Autowired
	BankAgentTableRep bankAgentTableRep;
	
	@Autowired
	UpdateCBSIncProceRep updateProcedure;
	
	@Autowired
	MerchantMasterRep merchantmasterRep;
	
	@Autowired
	CimCBSservice cimCBSservice;
	

	@SuppressWarnings("unchecked")
	public void sendftRequst(
			CIMCreditTransferRequest mcCreditTransferRequest, String sysTraceNumber, String bobMsgID, String seqUniqueID,
			BankAgentTable othBankAgent, String msgSeq, String endToEndIDt, String msgNetMir) {


		
		/*logger.info("Update IPSX OutMessage Initiated Status ");
		ipsDao.updateIPSXStatus(seqUniqueID, TranMonitorStatus.IPSX_OUTMSG_INITIATED.toString(),
				TranMonitorStatus.IN_PROGRESS.toString());*/
		/*ipsDao.updateOutwardIPSXStatus(seqUniqueID, TranMonitorStatus.IPSX_OUTMSG_INITIATED.toString(),
				TranMonitorStatus.IN_PROGRESS.toString());*/

		///// Create Pacs.008
		logger.info("Creating pacs.008.001.01 message");
		SendT sendRequest = new SendT();
		sendRequest.setMessage(paramMTMsgs.getParamMTmsg(DocType.pacs_008_001_08.getDocs(), sendRequest, bobMsgID,
				mcCreditTransferRequest, othBankAgent, msgSeq, endToEndIDt));

	    ///// update IPS TranIPS Table
		ipsDao.insertTranIPS(seqUniqueID, seqUniqueID, "pacs.008.001.08", "OUTGOING", "", "", "", "O",
					env.getProperty("ipsx.sender"), env.getProperty("ipsx.msgReceiver"), msgNetMir, "",endToEndIDt,docPacs.getPacs_008_001_01UnMarshalDocXML(sendRequest));

			
		com.bornfire.jaxb.wsdl.ObjectFactory obj = new com.bornfire.jaxb.wsdl.ObjectFactory();
		JAXBElement<SendT> jaxbElement = obj.createSend(sendRequest);

		///// Send Pacs.008 package to IPSX
		logger.info("Sending pacs.008.001.01 to ipsx");
		JAXBElement<SendResponse> response = null;

		try {
			response = (JAXBElement<SendResponse>) getWebServiceTemplate().marshalSendAndReceive(jaxbElement);
			logger.info("Get ACK/NAK from IPSX :" + response.getValue().getData().getType().toString());
		} catch (Exception e) {
			logger.info(e.getLocalizedMessage());
			e.printStackTrace();
			taskExecutor.execute(new Runnable() {
				@Override
				public void run() {

					/////Update IPSX Status to Tran Table
					/*ipsDao.updateIPSXStatusResponseRJCT(seqUniqueID, TranMonitorStatus.IPSX_NOT_CONNECTED.toString(),
							"", TranMonitorStatus.FAILURE.toString(), TranMonitorStatus.IPSX_ERROR.toString(), "",
							SERVER_ERROR_CODE,"pacs.008.001.08");*/
					
					ipsDao.updateIPSXStatusResponseRJCTBulkRTP(seqUniqueID, TranMonitorStatus.IPSX_NOT_CONNECTED.toString(),
							"", TranMonitorStatus.FAILURE.toString(), TranMonitorStatus.IPSX_ERROR.toString(), "",
							SERVER_ERROR_CODE);

				    ////Send Failure Message to CIM
					//ipsDao.ReturnCIMcnfResponseRJCT(seqUniqueID,"","",TranMonitorStatus.FAILURE.toString(),"","","",SERVER_ERROR_CODE,
							//TranMonitorStatus.IPSX_NOT_CONNECTED.toString());
				}
			});

		}

		///// Get ACK/NAK Message from IPSX
		if (response.getValue().getData().getType().toString().equals("ACK")) {

			String NetMIR = response.getValue().getData().getMir();
			String UserRef = response.getValue().getData().getRef();

			/*////// Update ACK Message
			logger.info("update Out Message ACK to Table");
			ipsDao.updateIPSXStatus(seqUniqueID, TranMonitorStatus.IPSX_OUTMSG_ACK_RECEIVED.toString(),
					TranMonitorStatus.IN_PROGRESS.toString());*/
			
			ipsDao.updateOutwardIPSXStatus(seqUniqueID, TranMonitorStatus.IPSX_OUTMSG_ACK_RECEIVED.toString(),
					TranMonitorStatus.IN_PROGRESS.toString());

			ipsDao.updateTranIPSACK(seqUniqueID, seqUniqueID, "O", "SUCCESS", NetMIR, UserRef);

		} else {
			///// Update NAK Message
			logger.info("update Out Message NAK to Table");
			/*ipsDao.updateIPSXStatus(seqUniqueID, TranMonitorStatus.IPSX_OUTMSG_NAK_RECEICED.toString(),
					TranMonitorStatus.FAILURE.toString());*/
			
			ipsDao.updateOutwardIPSXStatus(seqUniqueID, TranMonitorStatus.IPSX_OUTMSG_NAK_RECEICED.toString(),
					TranMonitorStatus.FAILURE.toString());

			String NetMIR = response.getValue().getData().getMir();
			String UserRef = response.getValue().getData().getRef();
			ipsDao.updateTranIPSACK(seqUniqueID, seqUniqueID, "O", "FAILURE", NetMIR, UserRef);

			
		}
		
	}

	
	/////Processing Incoming packages from IPSX
	////Register Message
	/////Calling Connect 24
	/////Calling IPSX web service
	
	@SuppressWarnings({ "unchecked", "unlikely-arg-type" })
	public void doProcess(SendT request) {

		
		//// Get Message Sender from IPSX
		String msgSender = request.getMessage().getMsgSender();
		//// Get Message Receiver from IPSX
		String msgReceiver = request.getMessage().getMsgReceiver();

		String msgNetMIR = request.getMessage().getMsgNetMir();
		String userReference = request.getMessage().getMsgUserReference();
		//logger.info("SignVerify"+signDocument.verifySignParseDoc(request.getMessage().getBlock4()).toString());

		switch (request.getMessage().getMsgType()) {

		case "pacs.008.001.08":

			logger.info("Start processing Incoming messages from IPSX");
			logger.info(request.getMessage().getMsgType());
			//logger.info("SignVerify"+signDocument.verifySignParseDoc(request.getMessage().getBlock4()).toString());
			///// Pacs.008 document from IPSX
			
		    ///// Generate System Trace Audit Number
			String sysTraceNumber008 = sequence.generateSystemTraceAuditNumber();
			///// Generate Bob Msg ID
			String bobMsgID008 = sequence.generateSeqUniqueID();
					
			/////Document UnMarshalling
			Document doc008 = docPacs.getPacs_008_001_01UnMarshalDoc(request);
			CreditTransferTransaction391 creditTran391=doc008.getFIToFICstmrCdtTrf().getCdtTrfTxInf().get(0);
			GroupHeader931 grpHdr931=doc008.getFIToFICstmrCdtTrf().getGrpHdr();
			
			logger.info("Start from document");

			///// Get Orgl Msg ID from IPSX
			String ipsxMsgID008 = grpHdr931.getMsgId();
			String seqUniqueID008 = ipsxMsgID008;
					
			
			String totInttBkSettlCcyPacs008 =grpHdr931.getTtlIntrBkSttlmAmt().getCcy();
			BigDecimal totInttBkSettlAmtPacs008 = grpHdr931.getTtlIntrBkSttlmAmt().getValue();

			///DebtorInformation
			//String debtorAccount008=creditTran391.getDbtrAcct().getId().getOthr().getId();
			String debtorAccount008 = Optional.ofNullable(creditTran391)
					.map(CreditTransferTransaction391::getDbtrAcct).map(CashAccount381::getId)
					.map(AccountIdentification4Choice1::getOthr).map(GenericAccountIdentification11::getId).orElse("");
			
			
			String debtorAccountName008 = Optional.ofNullable(creditTran391)
					.map(CreditTransferTransaction391::getDbtr).map(PartyIdentification1351::getNm).orElse("");
			
			//String debtorAgent008 =creditTran391.getDbtrAgt().getFinInstnId().getBICFI();
			String debtorAgent008 =Optional.ofNullable(creditTran391)
					.map(CreditTransferTransaction391::getDbtrAgt)
					.map(BranchAndFinancialInstitutionIdentification61::getFinInstnId)
					.map(FinancialInstitutionIdentification181::getBICFI)
					
					.orElse(Optional.ofNullable(creditTran391)
							.map(CreditTransferTransaction391::getDbtrAgt)
							.map(BranchAndFinancialInstitutionIdentification61::getFinInstnId)
							.map(FinancialInstitutionIdentification181::getClrSysMmbId)
							.map(ClearingSystemMemberIdentification21::getMmbId).orElse(""));
			
			String debtorAgentAcc008 ="";
			/*String debtorAgentAcc008 = Optional.ofNullable(creditTran391)
					.map(CreditTransferTransaction391::getDbtrAgtAcct)
					.map(CashAccount381::getId)
					.map(AccountIdentification4Choice1::getOthr)
					.map(GenericAccountIdentification11::getId).orElse("");*/
			
			
			///Creditor Information
			//String creditorAccount008 =creditTran391.getCdtrAcct().getId().getOthr().getId();
			String creditorAccount008 = Optional.ofNullable(creditTran391)
					.map(CreditTransferTransaction391::getCdtrAcct).map(CashAccount381::getId)
					.map(AccountIdentification4Choice1::getOthr).map(GenericAccountIdentification11::getId).orElse("");
			
			
			String creditorAccountName008 = Optional.ofNullable(creditTran391)
					.map(CreditTransferTransaction391::getCdtr).map(PartyIdentification1351::getNm).orElse("");
			
			//String creditorAgent008 =creditTran391.getCdtrAgt().getFinInstnId().getBICFI();
			String creditorAgent008 = Optional.ofNullable(creditTran391)
					.map(CreditTransferTransaction391::getCdtrAgt)
					.map(BranchAndFinancialInstitutionIdentification61::getFinInstnId)
					.map(FinancialInstitutionIdentification181::getBICFI)
					
					.orElse(Optional.ofNullable(creditTran391)
							.map(CreditTransferTransaction391::getCdtrAgt)
							.map(BranchAndFinancialInstitutionIdentification61::getFinInstnId)
							.map(FinancialInstitutionIdentification181::getClrSysMmbId)
							.map(ClearingSystemMemberIdentification21::getMmbId).orElse(""));
			
			String creditorAgentAcc008 ="";
			
			/*String creditorAgentAcc008 = Optional.ofNullable(creditTran391)
					.map(CreditTransferTransaction391::getCdtrAgtAcct)
					.map(CashAccount381::getId)
					.map(AccountIdentification4Choice1::getOthr)
					.map(GenericAccountIdentification11::getId).orElse("");*/


			/////Tran Amount and Currency
			BigDecimal trAmount008 = creditTran391.getIntrBkSttlmAmt()
					.getValue();
			String trCurrency008 = creditTran391.getIntrBkSttlmAmt().getCcy();
			
			
			///Payment Identification
			PaymentIdentification71 paymentID=creditTran391.getPmtId();
			String instr_id_pacs008=paymentID.getInstrId();
			String endToEndID008 = paymentID.getEndToEndId();
			
			
			////Instructing Party Details
			//String instgAgtPacs008=creditTran391.getInstgAgt().getFinInstnId().getBICFI();
			String instgAgtPacs008=Optional.ofNullable(creditTran391)
					.map(CreditTransferTransaction391::getInstgAgt)
					.map(BranchAndFinancialInstitutionIdentification61::getFinInstnId)
					.map(FinancialInstitutionIdentification181::getBICFI)
					
					.orElse(Optional.ofNullable(creditTran391)
							.map(CreditTransferTransaction391::getInstgAgt)
							.map(BranchAndFinancialInstitutionIdentification61::getFinInstnId)
							.map(FinancialInstitutionIdentification181::getClrSysMmbId)
							.map(ClearingSystemMemberIdentification21::getMmbId).orElse(""));
				
			
			//String instdAgtPacs008 =creditTran391.getInstdAgt().getFinInstnId().getBICFI();
			String instdAgtPacs008 =Optional.ofNullable(creditTran391)
					.map(CreditTransferTransaction391::getInstdAgt)
					.map(BranchAndFinancialInstitutionIdentification61::getFinInstnId)
					.map(FinancialInstitutionIdentification181::getBICFI)
					
					.orElse(Optional.ofNullable(creditTran391)
							.map(CreditTransferTransaction391::getInstdAgt)
							.map(BranchAndFinancialInstitutionIdentification61::getFinInstnId)
							.map(FinancialInstitutionIdentification181::getClrSysMmbId)
							.map(ClearingSystemMemberIdentification21::getMmbId)
							.orElse(""));
					

			String chrgBr008 =creditTran391.getChrgBr().value();
			/*String chrgBr008 = Optional.ofNullable(doc008).map(Document::getFIToFICstmrCdtTrf)
					.map(FIToFICustomerCreditTransferV08::getCdtTrfTxInf)
					.filter(indAliasList -> !indAliasList.isEmpty()).map(indAliasList -> indAliasList.get(0))
					.map(CreditTransferTransaction391::getChrgBr)
					.map(ChargeBearerType1Code::value).orElse("");*/
			
			
			PaymentTypeInformation281 paymentType281=creditTran391.getPmtTpInf();
			
			String clr_chnnl_pacs008= paymentType281.getClrChanl().value();
			
			String svc_lcl_pacs008=paymentType281.getSvcLvl().get(0).getPrtry();
			
			String lcl_instrm_pacs008=paymentType281.getLclInstrm().getPrtry();
			
			String ctgy_purp_pacs008=paymentType281.getCtgyPurp().getPrtry();
			
			String tran_type_code008 =paymentType281.getCtgyPurp()
					.getPrtry();
			
			String reg_rep_pacs008="";
			String rmt_info_pacs008 ="";
			String rmt_info_issuer_pacs008="";
			String rmt_info_nb_pacs008 ="";
			 rmt_info_pacs008 = Optional.ofNullable(doc008).map(Document::getFIToFICstmrCdtTrf)
					.map(FIToFICustomerCreditTransferV08::getCdtTrfTxInf)
					.filter(indAliasList -> !indAliasList.isEmpty()).map(indAliasList -> indAliasList.get(0))
					.map(CreditTransferTransaction391::getRmtInf)
					.map(RemittanceInformation161::getUstrd)
					.filter(indAliasList -> !indAliasList.isEmpty()).map(indAliasList -> indAliasList.get(0))
					.orElse("");
			
			/*String reg_rep_pacs008 = Optional.ofNullable(creditTran391)
					.map(CreditTransferTransaction391::getRgltryRptg)
					.map(RegulatoryReporting31::getDtls)
					.map(com.bornfire.jaxb.pacs_008_001_08.StructuredRegulatoryReporting31::getInf)
					.filter(indAliasList -> !indAliasList.isEmpty()).map(indAliasList -> indAliasList.get(0))
					.orElse("");*/
			
			
			/*String rmt_info_pacs008 = Optional.ofNullable(creditTran391)
					.map(CreditTransferTransaction391::getRmtInf)
					.map(RemittanceInformation161::getUstrd)
					.filter(indAliasList -> !indAliasList.isEmpty()).map(indAliasList -> indAliasList.get(0))
					.orElse("");*/
			
			/*String rmt_info_issuer_pacs008 = Optional.ofNullable(creditTran391)
					.map(CreditTransferTransaction391::getRmtInf)
					.map(RemittanceInformation161::getStrd)
					.filter(indAliasList -> !indAliasList.isEmpty()).map(indAliasList -> indAliasList.get(0))
					.map(StructuredRemittanceInformation161::getRfrdDocInf)
					.filter(indAliasList -> !indAliasList.isEmpty()).map(indAliasList -> indAliasList.get(0))
					.map(ReferredDocumentInformation71::getTp)
					.map(ReferredDocumentType41::getIssr)
					.orElse("");*/
			
			
			/*String rmt_info_nb_pacs008 = Optional.ofNullable(creditTran391)
					.map(CreditTransferTransaction391::getRmtInf)
					.map(RemittanceInformation161::getStrd)
					.filter(indAliasList -> !indAliasList.isEmpty()).map(indAliasList -> indAliasList.get(0))
					.map(StructuredRemittanceInformation161::getRfrdDocInf)
					.filter(indAliasList -> !indAliasList.isEmpty()).map(indAliasList -> indAliasList.get(0))
					.map(ReferredDocumentInformation71::getNb)
					.orElse("");*/
			
			///// Get Bank Code
			String othBankCode = ipsDao.getOtherBankCode(instgAgtPacs008);

			logger.info("Get Data from Pacs.008");
			
			logger.debug("System Trace Audit Number:" + sysTraceNumber008+
					"CIM Message ID:" + bobMsgID008+
					"IPS Message ID:" + ipsxMsgID008);

			///// Register InComing Message
			String status = ipsDao.RegisterInMsgRecord(sysTraceNumber008, bobMsgID008, ipsxMsgID008, seqUniqueID008,
					debtorAccount008, creditorAccount008, trAmount008, trCurrency008,
					TranMonitorStatus.INITIATED.toString(), endToEndID008, debtorAccountName008, creditorAccountName008,
					tran_type_code008, othBankCode,instgAgtPacs008,instdAgtPacs008,instr_id_pacs008,svc_lcl_pacs008,lcl_instrm_pacs008,
					ctgy_purp_pacs008,debtorAgent008,debtorAgentAcc008,creditorAgent008,creditorAgentAcc008,chrgBr008,
					clr_chnnl_pacs008,reg_rep_pacs008,rmt_info_pacs008,rmt_info_issuer_pacs008,rmt_info_nb_pacs008);

			logger.info("Table Update Status" + status);
			
			ipsDao.insertTranIPS(seqUniqueID008, seqUniqueID008, "pacs.008.001.08", "INCOMING", "", "", "", "I",
					msgSender, msgReceiver, msgNetMIR, userReference,endToEndID008,docPacs.getPacs_008_001_01UnMarshalDocXML(request));
			
			
			
			if(status.equals("1")) {
				
				//List<TranIPSTable> listTranTR = tranIPSTableRep.getCustomResult1(seqUniqueID008,instr_id_pacs008);

				//int sizePacs=listTranTR.size();
				
				/*if (sizePacs > 0) {
					////if (listTranTR.get(0).getResponse_status().equals(TranMonitorStatus.RJCT.toString())) {
						ipsDao.updateIPSXRJCTINC(seqUniqueID008,
								listTranTR.get(0).getResponse_error_desc(), listTranTR.get(0).getMsg_id(),
								TranMonitorStatus.FAILURE.toString(),
								TranMonitorStatus.IPSX_RESPONSE_RJCT.toString(),
								listTranTR.get(0).getResponse_status(), listTranTR.get(0).getResponse_error_code(),"pacs.008.001.08");
					//}

				}else {*/
					
					//int bankAgentExistSize=bankAgentTableRep.findByCustomBankName1
					//(instgAgtPacs008,debtorAgent008,instdAgtPacs008).size();
					
				logger.info(ctgy_purp_pacs008+"trAmount"+trAmount008.toString());
					String trAmount008S=trAmount008.toString();
					String trAmount008D= String.valueOf(Double.parseDouble(trAmount008S));
					String totInttBkSettlAmtPacs008D=String.valueOf(Double.parseDouble(totInttBkSettlAmtPacs008.toString()));
					
					String CreditStatusType = null;
					String CreditStatusCode = null;
					String CreditStatusDesc = null;

					String responseIncomeMsg = "";
					String responseTestMsg = "";
					
					
					if(seqUniqueID008.substring(0, 3).equals("UPI")||seqUniqueID008.substring(0, 3).equals("XYD")) {
						//	responseIncomeMsg=errorCode.ErrorCode("IV");

							int firstindex = rmt_info_pacs008.indexOf("/07/");
							int lastindex = rmt_info_pacs008.indexOf("/08/");
							
							logger.info("firstindex"+firstindex);
							logger.info("lastindex "+lastindex);
							logger.info("rmt_info_pacs008"+rmt_info_pacs008.substring(firstindex+4, lastindex));
							
							if(rmt_info_pacs008.substring(firstindex+4, lastindex).equals("123123")) {
								responseTestMsg="Success";
								logger.info("responseTestMsg"+responseTestMsg);
							}else {
							//	responseIncomeMsg=errorCode.ErrorCode("IV");
								
								responseTestMsg="Failure";
								logger.info("responseTestMsg"+responseTestMsg);
							}
						}
					
					
					
					if(ctgy_purp_pacs008.equals("102") || 
							//ctgy_purp_pacs008.equals("300")||
							!trCurrency008.equals("MUR")||
							!totInttBkSettlCcyPacs008.equals("MUR")||
							!trAmount008D.equals(totInttBkSettlAmtPacs008D)||
							creditorAccount008.equals("")||
							trAmount008S.equals("0") || trAmount008S.equals("0.00")||
							trAmount008S.equals("")||
							!ipsDao.checkBankAgentExistIncomingMsg(debtorAgent008)||
							!ipsDao.checkBankAgentExistIncomingMsg(instgAgtPacs008)||
							!ipsDao.checkBankAgentExistIncomingMsg(instdAgtPacs008)|| responseTestMsg.equals("Failure")) {
						logger.info("debtorAgent008"+debtorAgent008);
						logger.info("instgAgtPacs008"+instgAgtPacs008);
						logger.info("instdAgtPacs008"+instdAgtPacs008);
						logger.info("trAmount008S"+trAmount008S);
						logger.info("responseTestMsg"+responseTestMsg);
						logger.info("creditorAccount008"+creditorAccount008);
						logger.info("trCurrency008"+trCurrency008);
						logger.info("ctgy_purp_pacs008"+ctgy_purp_pacs008);
						logger.info("Validation Problem");
						
						
						if(creditorAccount008.equals("")) {
							responseIncomeMsg=errorCode.ErrorCode("AC03");
						}else if(trAmount008S.equals("0") || trAmount008S.equals("0.00")||
								trAmount008S.equals("")) {
							responseIncomeMsg=errorCode.ErrorCode("AM01");
						}else if(responseTestMsg.equals("Failure")){
							responseIncomeMsg=errorCode.ErrorCode("IV");
						}else {
							responseIncomeMsg=errorCode.ErrorCode("AG01");
						}
						
						CreditStatusType = TranMonitorStatus.RJCT.toString();
						CreditStatusDesc = responseIncomeMsg.split(":")[1];
						CreditStatusCode = responseIncomeMsg.split(":")[0];
						
						ipsDao.updateCBSStatusError(seqUniqueID008,
								TranMonitorStatus.VALIDATION_ERROR.toString(),
								CreditStatusDesc,
								TranMonitorStatus.FAILURE.toString());
						
						ipsDao.updateCBSStatusRTPError(sysTraceNumber008, instr_id_pacs008,seqUniqueID008,
								TranMonitorStatus.VALIDATION_ERROR.toString(),
								CreditStatusDesc,
								TranMonitorStatus.FAILURE.toString());
						
						
					}/*else if(!sequence.endToEndValidation(endToEndID008,debtorAgent008,creditorAgent008)){
						logger.info(ctgy_purp_pacs008+"STEP 5");
						responseIncomeMsg=errorCode.ErrorCode("AG01");
						errorType=TranMonitorStatus.VALIDATION_ERROR.toString();
					}*/else {
//						MerchantMaster ms = merchantmasterRep.findByIdCustom(creditorAccount008);
//						
						if(!ctgy_purp_pacs008.equals("300")) {
							logger.info("Calling ESB Connection "+ ctgy_purp_pacs008);
							responseIncomeMsg = ipsConnection.incomingFundTransferConnection1(creditorAccount008,creditorAccountName008,
									trAmount008S, trCurrency008, sysTraceNumber008, seqUniqueID008,"CUSTIN/"+othBankCode+"/"+debtorAccount008+"/"+debtorAccountName008,request,
									debtorAccount008,debtorAccountName008,instgAgtPacs008,ctgy_purp_pacs008,rmt_info_pacs008,instr_id_pacs008,endToEndID008);
						
						}else {
							responseIncomeMsg="CIM0:SUCCESS";
							responseIncomeMsg = ipsConnection.incomingFundTransferConnectionMerchant(creditorAccount008,creditorAccountName008,
									trAmount008S, trCurrency008, sysTraceNumber008, seqUniqueID008,"CUSTIN/"+othBankCode+"/"+debtorAccount008+"/"+debtorAccountName008,request,
									debtorAccount008,debtorAccountName008,instgAgtPacs008,ctgy_purp_pacs008,rmt_info_pacs008,instr_id_pacs008,endToEndID008,instdAgtPacs008);
							
						}
					/*	logger.info("Calling ESB Connection");
						responseIncomeMsg = ipsConnection.incomingFundTransferConnection1(creditorAccount008,creditorAccountName008,
								trAmount008S, trCurrency008, sysTraceNumber008, seqUniqueID008,"CUSTIN/"+othBankCode+"/"+debtorAccount008+"/"+debtorAccountName008,request,
								debtorAccount008,debtorAccountName008,instgAgtPacs008,ctgy_purp_pacs008,rmt_info_pacs008,instr_id_pacs008,endToEndID008);
					*/
//						responseIncomeMsg=ipsDao.initiateTranToCIM(creditorAccount008,creditorAccountName008,
//								trAmount008S, trCurrency008, sysTraceNumber008, seqUniqueID008,"CUSTIN/"+othBankCode+"/"+debtorAccount008+"/"+debtorAccountName008,request,
//								debtorAccount008,debtorAccountName008,instgAgtPacs008,ctgy_purp_pacs008,rmt_info_pacs008,instr_id_pacs008,endToEndID008);
						
						logger.info("Calling ESB ConnectionEnd");
						String msgStatus=responseIncomeMsg.split(":")[0];
						
						switch(msgStatus) {
							case "CIM0":
								CreditStatusType = TranMonitorStatus.ACSP.toString();
							break;
							
							default:
								CreditStatusType = TranMonitorStatus.RJCT.toString();
								CreditStatusDesc = responseIncomeMsg.split(":")[1];
								CreditStatusCode = responseIncomeMsg.split(":")[0];
								
							break;
								
						}
						
					}
					
					logger.info("Calling ESB ConnectionEnd11");

										
					try {
						
						/*List<TranIPSTable> list = tranIPSTableRep.getCustomResult(seqUniqueID008);

						if (list.size() > 0) {
							if (list.get(0).getResponse_status().equals(TranMonitorStatus.RJCT.toString())) {
								ipsDao.updateIPSXStatusResponseRJCT(list.get(0).getSequence_unique_id(),
										list.get(0).getResponse_error_desc(), list.get(0).getMsg_id(),
										TranMonitorStatus.FAILURE.toString(),
										TranMonitorStatus.IPSX_RESPONSE_RJCT.toString(),
										list.get(0).getResponse_status(), list.get(0).getResponse_error_code(),"pacs.008.001.08");
							}

						}else {*/
							
						    //// Out Message Sequence
							String msgSeqPacs002 = sequence.generateMsgSequence();
							//// Out MsgNetMir
							
							StringBuilder msgOutPacs002NetSB=new StringBuilder();
							String msgOutPacs002NteMIR = msgOutPacs002NetSB.append(new SimpleDateFormat("yyMMdd").format(new Date()))
									.append(env.getProperty("ipsx.user")).append("0001").append(msgSeqPacs002).toString();

							///// Create Pacs.002 Package
							SendT sendRequest008 = new SendT();
							logger.info(seqUniqueID008 + " :Creating pacs.002.001.10 msg from response pacs.008.001.08");
							
							sendRequest008.setMessage(paramMTMsgs.getParamMTmsgPacs002(DocType.pacs_002_001_10.getDocs(), request,
									bobMsgID008, CreditStatusType, CreditStatusCode, CreditStatusDesc, msgSeqPacs002));

							ipsDao.insertTranIPS(seqUniqueID008, bobMsgID008, "pacs.002.001.10", "", "", "", "", "O",
									env.getProperty("ipsx.sender"), env.getProperty("ipsx.msgReceiver"), msgOutPacs002NteMIR, "",endToEndID008,docPacs.getPacs_002_001_10UnMarshalDocXML(sendRequest008));

							
							///// Send Pacs.002 Pacs to IPSX
							com.bornfire.jaxb.wsdl.ObjectFactory obj008 = new com.bornfire.jaxb.wsdl.ObjectFactory();
							
						JAXBElement<SendT> jaxbElement008 = obj008.createSend(sendRequest008);
							

							JAXBElement<SendResponse> response008 = null;
							
							
								response008 = (JAXBElement<SendResponse>) getWebServiceTemplate().marshalSendAndReceive(jaxbElement008);
								

								logger.info(
										seqUniqueID008 + " :Getting ACK/NAK" + response008.getValue().getData().getType().toString());
								///// Get ACK,NAK Msg from IPSX
								if (response008.getValue().getData().getType().toString().equals("ACK")) {

									ipsDao.updateIPSXStatus(seqUniqueID008, TranMonitorStatus.IPSX_INMSG_ACK_RECEIVED.toString(),
											TranMonitorStatus.IN_PROGRESS.toString());
									
									String NetMIR = response008.getValue().getData().getMir();
									String UserRef = response008.getValue().getData().getRef();

									ipsDao.updateTranIPSACK(seqUniqueID008, bobMsgID008, "O", "SUCCESS", NetMIR, UserRef);

									logger.info(seqUniqueID008 + " :update IN Message ACK to Table");

								} else {
									String NetMIR = response008.getValue().getData().getMir();
									String UserRef = response008.getValue().getData().getRef();
									String error=response008.getValue().getData().getDescription();

									ipsDao.updateTranIPSACK(seqUniqueID008, bobMsgID008, "O", "FAILURE", NetMIR, UserRef);

									logger.info(seqUniqueID008 + " :update IN Message NAK to Table");

									ipsDao.updateIPSXStatus(seqUniqueID008, TranMonitorStatus.IPSX_INMSG_NAK_RECEICED.toString(),
											TranMonitorStatus.FAILURE.toString());

								}
								
								

								
							//}
						
						//}
						
					} catch (Exception e) {

						logger.info(e.getLocalizedMessage());
					}
				//}
			}

			
			break;

		case "pacs.002.001.10":

			logger.info("Processing  pacs.002.001.10 received from IPSX");

			///// Get Pacs.002 document from IPSX
			com.bornfire.jaxb.pacs_002_001_010.Document doc = docPacs.getPacs_002_001_10UnMarshalDoc(request);

			///// Verify the Signature
			signDocument.verifySignParseDoc(request.getMessage().getBlock4());

			logger.info("Transaction Status from pacs.002 ,ID"
					+ doc.getFIToFIPmtStsRpt().getOrgnlGrpInfAndSts().getOrgnlMsgId() + " ,Status :"
					+ doc.getFIToFIPmtStsRpt().getTxInfAndSts().get(0).getTxSts());
			
			//doc.getFIToFIPmtStsRpt().getGrpHdr().getInstgAgt().getFinInstnId().getBICFI();
			

			///// Transaction Status
			String tranStatus002 = doc.getFIToFIPmtStsRpt().getTxInfAndSts().get(0).getTxSts();
			//// Orgl Msg ID
			String orglMsgID002 = doc.getFIToFIPmtStsRpt().getOrgnlGrpInfAndSts().getOrgnlMsgId();
			//// Mesage ID
			String msgID002 = doc.getFIToFIPmtStsRpt().getGrpHdr().getMsgId();
			
			
			
			String endToendID002 =Optional.ofNullable(doc)
					.map(com.bornfire.jaxb.pacs_002_001_010.Document::getFIToFIPmtStsRpt)
					.map(FIToFIPaymentStatusReportV10::getTxInfAndSts).filter(indAliasList -> !indAliasList.isEmpty())
					.map(indAliasList -> indAliasList.get(0)).map(PaymentTransaction1101::getOrgnlEndToEndId).orElse("");
			///// Error Code
			
			logger.info("endToEnd"+endToendID002);
			String errorCode002 =  Optional.ofNullable(doc)
					.map(com.bornfire.jaxb.pacs_002_001_010.Document::getFIToFIPmtStsRpt)
					.map(FIToFIPaymentStatusReportV10::getTxInfAndSts).filter(indAliasList -> !indAliasList.isEmpty())
					.map(indAliasList -> indAliasList.get(0)).map(PaymentTransaction1101::getStsRsnInf)
					.filter(indAliasList -> !indAliasList.isEmpty()).map(indAliasList -> indAliasList.get(0))
					.map(StatusReasonInformation121::getRsn).map(StatusReason6Choice1::getPrtry).orElse("");
			///// Error Desc
			
			
			
			String errorDesc002 = Optional.ofNullable(doc)
					.map(com.bornfire.jaxb.pacs_002_001_010.Document::getFIToFIPmtStsRpt)
					.map(FIToFIPaymentStatusReportV10::getTxInfAndSts).filter(indAliasList -> !indAliasList.isEmpty())
					.map(indAliasList -> indAliasList.get(0)).map(PaymentTransaction1101::getStsRsnInf)
					.filter(indAliasList -> !indAliasList.isEmpty()).map(indAliasList -> indAliasList.get(0))
					.map(StatusReasonInformation121::getAddtlInf).filter(indAliasList -> !indAliasList.isEmpty())
					.map(indAliasList -> indAliasList.get(0)).orElse("");

			logger.info(errorCode002 + "  " + errorDesc002);

			if (tranStatus002.equals(TranMonitorStatus.ACSP.toString())) {
				logger.info(orglMsgID002 + " :Update IPSX response ACSP to Table");

				

				logger.info(orglMsgID002 + " :Update IPSX response ACSP to Table");

				if (!userReference.equals("")) {
					ipsDao.updateIPSXStatusResponseACSP(orglMsgID002, msgID002, TranMonitorStatus.SUCCESS.toString(),"pacs.002.001.10");
				}

				ipsDao.insertTranIPS(orglMsgID002, msgID002, "pacs.002.001.10", "", "ACSP", "", "", "I", msgSender,
						msgReceiver, msgNetMIR, userReference,endToendID002,docPacs.getPacs_002_001_10UnMarshalDocXML(request));
				
				ipsDao.updateIPSXStatusResponseACSPBulkRTP(orglMsgID002, msgID002,
						TranMonitorStatus.SUCCESS.toString(), "pacs.002.001.10",
						new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
				
				
			} else if(tranStatus002.equals(TranMonitorStatus.RJCT.toString())){

				logger.info(orglMsgID002 + " :Update IPSX RJCT msg response in table");
				
				if(errorCode002.equals("EL201")) {
					List<StatusReasonInformation121> listerrorDesc=doc.getFIToFIPmtStsRpt().getTxInfAndSts().get(0).getStsRsnInf();
					
					if(listerrorDesc.size()>0) {
						
						int size=listerrorDesc.size()-1;
						
						errorCode002 = Optional.ofNullable(doc)
								.map(com.bornfire.jaxb.pacs_002_001_010.Document::getFIToFIPmtStsRpt)
								.map(FIToFIPaymentStatusReportV10::getTxInfAndSts).filter(indAliasList -> !indAliasList.isEmpty())
								.map(indAliasList -> indAliasList.get(0)).map(PaymentTransaction1101::getStsRsnInf)
								.filter(indAliasList -> !indAliasList.isEmpty()).map(indAliasList -> indAliasList.get(size))
								.map(StatusReasonInformation121::getRsn).map(StatusReason6Choice1::getPrtry).orElse("");
						
						errorDesc002 = Optional.ofNullable(doc)
								.map(com.bornfire.jaxb.pacs_002_001_010.Document::getFIToFIPmtStsRpt)
								.map(FIToFIPaymentStatusReportV10::getTxInfAndSts).filter(indAliasList -> !indAliasList.isEmpty())
								.map(indAliasList -> indAliasList.get(0)).map(PaymentTransaction1101::getStsRsnInf)
								.filter(indAliasList -> !indAliasList.isEmpty()).map(indAliasList -> indAliasList.get(size))
								.map(StatusReasonInformation121::getAddtlInf).filter(indAliasList -> !indAliasList.isEmpty())
								.map(indAliasList -> indAliasList.get(0)).orElse("");
					}
				}
				//List<StatusReasonInformation121> listerrorDesc=doc.getFIToFIPmtStsRpt().getTxInfAndSts().get(0).getStsRsnInf();


				if (!userReference.equals("")) {
					ipsDao.updateIPSXStatusResponseRJCT(orglMsgID002, errorDesc002, msgID002,
							TranMonitorStatus.FAILURE.toString(), TranMonitorStatus.IPSX_RESPONSE_RJCT.toString(),
							TranMonitorStatus.RJCT.toString(), errorCode002,"pacs.002.001.10");
				}
				
				ipsDao.insertTranIPS(orglMsgID002, msgID002, "pacs.002.001.10", "",tranStatus002, errorCode002, errorDesc002,
						"I", msgSender, msgReceiver, msgNetMIR, userReference,endToendID002,docPacs.getPacs_002_001_10UnMarshalDocXML(request));
				
				if (!userReference.equals("")) {
				ipsDao.updateIPSXStatusResponseRJCTBulkRTP(orglMsgID002, errorDesc002, msgID002,
							TranMonitorStatus.FAILURE.toString(), TranMonitorStatus.IPSX_RESPONSE_RJCT.toString(),
							TranMonitorStatus.RJCT.toString(), errorCode002);
				}
				

			}else {
				logger.info(orglMsgID002 + " :Update IPSX other msg response in table");
				ipsDao.insertTranIPS(orglMsgID002, msgID002, "pacs.002.001.10", "",tranStatus002, errorCode002, errorDesc002,
						"I", msgSender, msgReceiver, msgNetMIR, userReference,endToendID002,docPacs.getPacs_002_001_10UnMarshalDocXML(request));
			}

			break;

		case "camt.025.001.05":

			logger.info("Processing  camt.025.001.05 received from IPSX");

			///// Get Camt025 document from IPSX
			com.bornfire.jaxb.camt_025_001_05.Document docCamt_025_001_05 = docPacs
					.getCamt025_001_05UnMarshalDoc(request);

			///// Message ID
			String msgIDCamt025 = docCamt_025_001_05.getRct().getMsgHdr().getMsgId();
			///// Orgl Message ID
			String orglMsgIDCamt025 = docCamt_025_001_05.getRct().getRctDtls().get(0).getOrgnlMsgId().getMsgId();
			//// Status Code
			String stsCodeCamt025 = docCamt_025_001_05.getRct().getRctDtls().get(0).getReqHdlg().get(0).getStsCd();
			///// Status Desc
			String stsDescCamt025 = docCamt_025_001_05.getRct().getRctDtls().get(0).getReqHdlg().get(0).getDesc();

			logger.info("camt.025.001.05 MsgID:" + docCamt_025_001_05.getRct().getMsgHdr().getMsgId() + " OrglMsgName :"
					+ docCamt_025_001_05.getRct().getRctDtls().get(0).getOrgnlMsgId().getMsgNmId().split("/")[0]);

			logger.info(orglMsgIDCamt025 + " :Insert Tran IPS Camt025");

			logger.info(orglMsgIDCamt025 + " :Insert Tran IPS Camt025" + stsCodeCamt025);

			ipsDao.insertTranIPS(orglMsgIDCamt025, msgIDCamt025, "camt.025.001.05", "", "", "", "", "I", msgSender,
					msgReceiver, msgNetMIR, userReference,"",docPacs.getCamt025_001_05UnMarshalDocXML(request));

			if (docCamt_025_001_05.getRct().getRctDtls().get(0).getOrgnlMsgId().getMsgNmId().split("/")[0]
					.equals(DocType.pacs_008_001_08.toString())) {

				if (!userReference.equals("")) {
					if(stsCodeCamt025.equals("ERRC")) {
						String errdata=stsDescCamt025.replace("\n", "-").replace("\r", "-");

					ipsDao.updateIPSXStatusResponseRJCT(orglMsgIDCamt025,errdata.split("-")[1], msgIDCamt025,
							TranMonitorStatus.IN_PROGRESS.toString(), TranMonitorStatus.IPSX_RESPONSE_RJCT.toString(),
							TranMonitorStatus.RJCT.toString(), errdata.split("-")[0],"camt.025.001.05");
					
					ipsDao.updateIPSXStatusResponseRJCTBulkRTP(orglMsgIDCamt025, errdata.split("-")[1], msgIDCamt025,
							TranMonitorStatus.FAILURE.toString(), TranMonitorStatus.IPSX_RESPONSE_RJCT.toString(),
							TranMonitorStatus.RJCT.toString(), errdata.split("-")[0]);
					}
					
						
				}

			}else{
				
					if(stsCodeCamt025.equals("ERRC")) {

						String errdata=stsDescCamt025.replace("\n", "-").replace("\r", "-");
						ipsDao.updateIPSXStatusResponseRJCT(orglMsgIDCamt025,errdata.split("-")[1], msgIDCamt025,
								TranMonitorStatus.FAILURE.toString(), TranMonitorStatus.IPSX_RESPONSE_RJCT.toString(),
								TranMonitorStatus.RJCT.toString(), errdata.split("-")[0], "camt.025.001.05");


						// if(instgId.equals(env.getProperty("ipsx.bicfi"))) {
						ipsDao.updateIPSXStatusResponseRJCTBulkRTP(orglMsgIDCamt025, errdata.split("-")[1], msgIDCamt025,
								TranMonitorStatus.FAILURE.toString(), TranMonitorStatus.IPSX_RESPONSE_RJCT.toString(),
								TranMonitorStatus.RJCT.toString(), errdata.split("-")[0]);	
					}
				
				
			} /*
				 * else if
				 * (docCamt_025_001_05.getRct().getRctDtls().get(0).getOrgnlMsgId().getMsgNmId()
				 * .split("/")[0] .equals(DocType.pacs_002_001_10.toString())) {
				 * 
				 * if (!userReference.equals("")) {
				 * ipsDao.updateIPSXStatusResponseRJCT(orglMsgIDCamt025, stsDescCamt025,
				 * msgIDCamt025, TranMonitorStatus.IN_PROGRESS.toString(),
				 * TranMonitorStatus.IPSX_RESPONSE_RJCT.toString(),
				 * TranMonitorStatus.RJCT.toString(), stsCodeCamt025); } }
				 */
			break;
		case "admi.002.001.01":
			// docPacs.getAdmi_002_001_01UnMarshalDoc(request);
			logger.info("Register Transaction Net Statement Report to table:");

			com.bornfire.jaxb.admi_002_001_01.Document docAdmi002_001_01 = docPacs
					.getAdmi_002_001_01UnMarshalDoc(request);

			String orglMsgIdAdmi002 = docAdmi002_001_01.getAdmi00200101().getRltdRef().getRef();
			String codeAdmi002=docAdmi002_001_01.getAdmi00200101().getRsn().getRjctgPtyRsn();
			String descAdmi002=docAdmi002_001_01.getAdmi00200101().getRsn().getRsnDesc();

			ipsDao.insertTranIPS(orglMsgIdAdmi002, orglMsgIdAdmi002, "admi.002.001.01", "", "", codeAdmi002,
					descAdmi002, "I", msgSender, msgReceiver, msgNetMIR, userReference,"",docPacs.getAdmi_002_001_01UnMarshalDocXML(request));

			ipsDao.updateIPSXStatusResponseRJCT(orglMsgIdAdmi002, descAdmi002, orglMsgIdAdmi002,
					TranMonitorStatus.FAILURE.toString(), TranMonitorStatus.IPSX_RESPONSE_RJCT.toString(),
					TranMonitorStatus.RJCT.toString(), codeAdmi002,"admi.002.001.01");

			ipsDao.updateIPSXStatusResponseRJCTBulkRTP(orglMsgIdAdmi002, descAdmi002, orglMsgIdAdmi002,
					TranMonitorStatus.FAILURE.toString(), TranMonitorStatus.IPSX_RESPONSE_RJCT.toString(),
					TranMonitorStatus.RJCT.toString(), codeAdmi002);
			break;
			
		case "camt.019.001.07":
			logger.info("---Camt.019.001.07---");

			com.bornfire.jaxb.camt_019_001_07.Document docCamt019_001_07 = docPacs
					.getCamt019_001_07UnMarshalDoc(request);

			String orglMsgIdCamt019 = docCamt019_001_07.getRtrBizDayInf().getMsgHdr().getMsgId();

			ipsDao.insertTranIPS(orglMsgIdCamt019, orglMsgIdCamt019, "camt.019.001.07", "", "", "",
					"", "I", msgSender, msgReceiver, msgNetMIR, userReference,"",docPacs.getCamt019_001_07UnMarshalDocXML(request));


			break;
			
		case "pacs.004.001.09":
			logger.info("---pacs.004.001.09---");

			com.bornfire.jaxb.pacs_004_001_09.Document docPacs_004_001_09 = docPacs
					.getPacs004_001_09UnMarshalDoc(request);

			String orglMsgIdPacs004 = docPacs_004_001_09.getPmtRtr().getGrpHdr().getMsgId();

			ipsDao.insertTranIPS(orglMsgIdPacs004, orglMsgIdPacs004, "pacs.004.001.09", "", "", "",
					"", "I", msgSender, msgReceiver, msgNetMIR, userReference,"",docPacs.getPacs004_001_09UnMarshalDocXML(request));


			break;

		case "camt.052.001.08":

			logger.info("Register Transaction Net Statement Report to table:");

			////// Get Camt.052 Document
			com.bornfire.jaxb.camt_052_001_08.Document docCamt052_001_08 = docPacs
					.getCamt052_001_08UnMarshalDoc(request);

			String orglMsgIDCamt052 = docCamt052_001_08.getBkToCstmrAcctRpt().getGrpHdr().getMsgId();
			String msgIDCamt052 = docCamt052_001_08.getBkToCstmrAcctRpt().getGrpHdr().getMsgId();

			ipsDao.insertTranIPS(orglMsgIDCamt052, msgIDCamt052, "camt.052.001.08", "", "", "", "", "I", msgSender,
					msgReceiver, msgNetMIR, userReference,"",docPacs.getCamt052_001_08UnMarshalDocXML(request));

			ipsDao.registerNetStatRecord(docCamt052_001_08);

			break;
		case "camt.053.001.08":

			logger.info("Register Settlement Report to table:");

			////// Get Camt.053 Document
			com.bornfire.jaxb.camt_053_001_08.Document docCamt053_001_08 = docPacs
					.getCamt053_001_08UnMarshalDoc(request);

			String orglMsgIDCamt053 = docCamt053_001_08.getBkToCstmrStmt().getGrpHdr().getMsgId();
			String msgIDCamt053 = docCamt053_001_08.getBkToCstmrStmt().getGrpHdr().getMsgId();

			ipsDao.insertTranIPS(orglMsgIDCamt053, msgIDCamt053, "camt.053.001.08", "", "", "", "", "I", msgSender,
					msgReceiver, msgNetMIR, userReference,"",docPacs.getCamt053_001_08UnMarshalDocXML(request));

			ipsDao.registerCamt53Record(docCamt053_001_08);
			// ipsDao.registerNetStatRecord(docCamt052_001_08);

			break;

		case "camt.010.001.08":

			com.bornfire.jaxb.camt_010_001_08.Document docsCamt010_001_08 = docPacs
					.getCamt010_001_07UnMarshalDoc(request);
			String ipsxMsgID11 = docsCamt010_001_08.getRtrLmt().getMsgHdr().getMsgId();

			ipsDao.insertTranIPS(ipsxMsgID11, ipsxMsgID11, "camt.010.001.08", "", "", "", "", "I", msgSender,
					msgReceiver, msgNetMIR, userReference,"",docPacs.getCamt010_001_07UnMarshalDocXML(request));

			if (docsCamt010_001_08.getRtrLmt().getMsgHdr().getOrgnlBizQry().getMsgNmId().equals("camt.009.001.07")) {
				ipsDao.updateSettlementLimitReportStatus(docsCamt010_001_08);
			}
			break;

		case "pain.001.001.09":

			logger.info("Processing  pain.001.001.09 received from IPSX");
			///// Get pain.001 document from IPSX
			com.bornfire.jaxb.pain_001_001_09.Document docPain001_001_09 = docPacs
					.getPain001_001_09UnMarshalDoc(request);

			///// Generate System Trace Audit Number
			String sysTraceNumber = sequence.generateSystemTraceAuditNumber();
			///// Generate Bob Msg ID
			String bobMsgID = sequence.generateSeqUniqueID();
			///// Msg ID
			String ipsxMsgID = docPain001_001_09.getCstmrCdtTrfInitn().getGrpHdr().getMsgId();
			String seqUniqueID = docPain001_001_09.getCstmrCdtTrfInitn().getGrpHdr().getMsgId();

			
			//// Debtor Account Name
			String debtorAccount = Optional.ofNullable(docPain001_001_09)
					.map(com.bornfire.jaxb.pain_001_001_09.Document::getCstmrCdtTrfInitn)
					.map(com.bornfire.jaxb.pain_001_001_09.CustomerCreditTransferInitiationV09::getPmtInf)
					.map(com.bornfire.jaxb.pain_001_001_09.PaymentInstruction301::getDbtrAcct)
					.map(com.bornfire.jaxb.pain_001_001_09.CashAccount381::getId)
					.map(com.bornfire.jaxb.pain_001_001_09.AccountIdentification4Choice1::getOthr)
					.map(com.bornfire.jaxb.pain_001_001_09.GenericAccountIdentification11::getId).orElse("");
			//// Debtor Account Number
			String debtorAccountName = Optional.ofNullable(docPain001_001_09)
					.map(com.bornfire.jaxb.pain_001_001_09.Document::getCstmrCdtTrfInitn)
					.map(com.bornfire.jaxb.pain_001_001_09.CustomerCreditTransferInitiationV09::getPmtInf)
					.map(com.bornfire.jaxb.pain_001_001_09.PaymentInstruction301::getDbtr)
					.map(com.bornfire.jaxb.pain_001_001_09.PartyIdentification1351::getNm).orElse("");

			String debtorAgentPain001 = Optional.ofNullable(docPain001_001_09)
					.map(com.bornfire.jaxb.pain_001_001_09.Document::getCstmrCdtTrfInitn)
					.map(com.bornfire.jaxb.pain_001_001_09.CustomerCreditTransferInitiationV09::getPmtInf)
					.map(com.bornfire.jaxb.pain_001_001_09.PaymentInstruction301::getDbtrAgt)
					.map(com.bornfire.jaxb.pain_001_001_09.BranchAndFinancialInstitutionIdentification61::getFinInstnId)
					.map(com.bornfire.jaxb.pain_001_001_09.FinancialInstitutionIdentification181::getBICFI).orElse("");
			
			if(debtorAgentPain001.equals("")) {
				debtorAgentPain001 = Optional.ofNullable(docPain001_001_09)
						.map(com.bornfire.jaxb.pain_001_001_09.Document::getCstmrCdtTrfInitn)
						.map(com.bornfire.jaxb.pain_001_001_09.CustomerCreditTransferInitiationV09::getPmtInf)
						.map(com.bornfire.jaxb.pain_001_001_09.PaymentInstruction301::getDbtrAgt)
						.map(com.bornfire.jaxb.pain_001_001_09.BranchAndFinancialInstitutionIdentification61::getFinInstnId)
						.map(com.bornfire.jaxb.pain_001_001_09.FinancialInstitutionIdentification181::getClrSysMmbId)
						.map(com.bornfire.jaxb.pain_001_001_09.ClearingSystemMemberIdentification21::getMmbId).orElse("");
				
			}
			String debtorAgentAccPain001 = Optional.ofNullable(docPain001_001_09)
					.map(com.bornfire.jaxb.pain_001_001_09.Document::getCstmrCdtTrfInitn)
					.map(com.bornfire.jaxb.pain_001_001_09.CustomerCreditTransferInitiationV09::getPmtInf)
					.map(com.bornfire.jaxb.pain_001_001_09.PaymentInstruction301::getDbtrAgtAcct)
					.map(com.bornfire.jaxb.pain_001_001_09.CashAccount381::getId)
					.map(com.bornfire.jaxb.pain_001_001_09.AccountIdentification4Choice1::getOthr)
					.map(com.bornfire.jaxb.pain_001_001_09.GenericAccountIdentification11::getId).orElse("");
			
			String creditorAgentPain001 = Optional.ofNullable(docPain001_001_09)
					.map(com.bornfire.jaxb.pain_001_001_09.Document::getCstmrCdtTrfInitn)
					.map(com.bornfire.jaxb.pain_001_001_09.CustomerCreditTransferInitiationV09::getPmtInf)
					.map(com.bornfire.jaxb.pain_001_001_09.PaymentInstruction301::getCdtTrfTxInf)
					.filter(indAliasList -> !indAliasList.isEmpty()).map(indAliasList -> indAliasList.get(0))
					.map(com.bornfire.jaxb.pain_001_001_09.CreditTransferTransaction341::getCdtrAgt)
					.map(com.bornfire.jaxb.pain_001_001_09.BranchAndFinancialInstitutionIdentification61::getFinInstnId)
					.map(com.bornfire.jaxb.pain_001_001_09.FinancialInstitutionIdentification181::getBICFI).orElse("");
			
			if(creditorAgentPain001.equals("")) {
				creditorAgentPain001 = Optional.ofNullable(docPain001_001_09)
						.map(com.bornfire.jaxb.pain_001_001_09.Document::getCstmrCdtTrfInitn)
						.map(com.bornfire.jaxb.pain_001_001_09.CustomerCreditTransferInitiationV09::getPmtInf)
						.map(com.bornfire.jaxb.pain_001_001_09.PaymentInstruction301::getCdtTrfTxInf)
						.filter(indAliasList -> !indAliasList.isEmpty()).map(indAliasList -> indAliasList.get(0))
						.map(com.bornfire.jaxb.pain_001_001_09.CreditTransferTransaction341::getCdtrAgt)
						.map(com.bornfire.jaxb.pain_001_001_09.BranchAndFinancialInstitutionIdentification61::getFinInstnId)
						.map(com.bornfire.jaxb.pain_001_001_09.FinancialInstitutionIdentification181::getClrSysMmbId)
						.map(com.bornfire.jaxb.pain_001_001_09.ClearingSystemMemberIdentification21::getMmbId).orElse("");
			}
			
			String creditorAgentAccPain001 = Optional.ofNullable(docPain001_001_09)
					.map(com.bornfire.jaxb.pain_001_001_09.Document::getCstmrCdtTrfInitn)
					.map(com.bornfire.jaxb.pain_001_001_09.CustomerCreditTransferInitiationV09::getPmtInf)
					.map(com.bornfire.jaxb.pain_001_001_09.PaymentInstruction301::getCdtTrfTxInf)
					.filter(indAliasList -> !indAliasList.isEmpty()).map(indAliasList -> indAliasList.get(0))
					.map(com.bornfire.jaxb.pain_001_001_09.CreditTransferTransaction341::getCdtrAgtAcct)
					.map(com.bornfire.jaxb.pain_001_001_09.CashAccount381::getId)
					.map(com.bornfire.jaxb.pain_001_001_09.AccountIdentification4Choice1::getOthr)
					.map(com.bornfire.jaxb.pain_001_001_09.GenericAccountIdentification11::getId).orElse("");
			
			
			String instgAgtPain001 = Optional.ofNullable(docPain001_001_09)
					.map(com.bornfire.jaxb.pain_001_001_09.Document::getCstmrCdtTrfInitn)
					.map(com.bornfire.jaxb.pain_001_001_09.CustomerCreditTransferInitiationV09::getGrpHdr)
					.map(com.bornfire.jaxb.pain_001_001_09.GroupHeader851::getInitgPty)
					.map(com.bornfire.jaxb.pain_001_001_09.PartyIdentification1351::getId)
					.map(com.bornfire.jaxb.pain_001_001_09.Party38Choice1::getOrgId)
					.map(com.bornfire.jaxb.pain_001_001_09.OrganisationIdentification291::getAnyBIC).orElse("");
			
//			if (instgAgtPain001.equals("")) {
//				instgAgtPain001 = Optional.ofNullable(docPain001_001_09)
//						.map(com.bornfire.jaxb.pain_001_001_09.Document::getCstmrCdtTrfInitn)
//						.map(com.bornfire.jaxb.pain_001_001_09.CustomerCreditTransferInitiationV09::getGrpHdr)
//						.map(com.bornfire.jaxb.pain_001_001_09.GroupHeader851::getInitgPty)
//						.map(com.bornfire.jaxb.pain_001_001_09.PartyIdentification1351::getId)
//						.map(com.bornfire.jaxb.pain_001_001_09.Party38Choice1::getOrgId)
//						.map(com.bornfire.jaxb.pain_001_001_09.OrganisationIdentification291::getOthr)
//						.filter(indAliasList -> !indAliasList.isEmpty()).map(indAliasList -> indAliasList.get(0))
//						.map(com.bornfire.jaxb.pain_001_001_09.GenericOrganisationIdentification11::getId).orElse("");
//
//			}
		
			String instdAgtPain001 = Optional.ofNullable(docPain001_001_09)
					.map(com.bornfire.jaxb.pain_001_001_09.Document::getCstmrCdtTrfInitn)
					.map(com.bornfire.jaxb.pain_001_001_09.CustomerCreditTransferInitiationV09::getGrpHdr)
					.map(com.bornfire.jaxb.pain_001_001_09.GroupHeader851::getFwdgAgt)
					.map(com.bornfire.jaxb.pain_001_001_09.BranchAndFinancialInstitutionIdentification61::getFinInstnId)
					.map(com.bornfire.jaxb.pain_001_001_09.FinancialInstitutionIdentification181::getBICFI).orElse("");
			
			if(instdAgtPain001.equals("")) {
				instdAgtPain001 = Optional.ofNullable(docPain001_001_09)
						.map(com.bornfire.jaxb.pain_001_001_09.Document::getCstmrCdtTrfInitn)
						.map(com.bornfire.jaxb.pain_001_001_09.CustomerCreditTransferInitiationV09::getGrpHdr)
						.map(com.bornfire.jaxb.pain_001_001_09.GroupHeader851::getFwdgAgt)
						.map(com.bornfire.jaxb.pain_001_001_09.BranchAndFinancialInstitutionIdentification61::getFinInstnId)
						.map(com.bornfire.jaxb.pain_001_001_09.FinancialInstitutionIdentification181::getClrSysMmbId)
						.map(com.bornfire.jaxb.pain_001_001_09.ClearingSystemMemberIdentification21::getMmbId).orElse("");
				
			}

			String currency = docPain001_001_09.getCstmrCdtTrfInitn().getPmtInf().getCdtTrfTxInf().get(0).getAmt()
					.getInstdAmt().getCcy();

			String trAmount = docPain001_001_09.getCstmrCdtTrfInitn().getPmtInf().getCdtTrfTxInf().get(0).getAmt()
					.getInstdAmt().getValue().toString();
			String endToEndID = docPain001_001_09.getCstmrCdtTrfInitn().getPmtInf().getCdtTrfTxInf().get(0).getPmtId()
					.getEndToEndId();
			
			
			String creditorAccount = docPain001_001_09.getCstmrCdtTrfInitn().getPmtInf().getCdtTrfTxInf().get(0)
					.getCdtrAcct().getId().getOthr().getId();
			String creditorAccountName = docPain001_001_09.getCstmrCdtTrfInitn().getPmtInf().getCdtTrfTxInf().get(0)
					.getCdtr().getNm();

			String tran_type_codePain001 = docPain001_001_09.getCstmrCdtTrfInitn().getPmtInf().getCdtTrfTxInf().get(0)
					.getPmtTpInf().getCtgyPurp().getPrtry();
			
			String reg_rep_pain001 = Optional.ofNullable(docPain001_001_09)
					.map(com.bornfire.jaxb.pain_001_001_09.Document::getCstmrCdtTrfInitn)
					.map(com.bornfire.jaxb.pain_001_001_09.CustomerCreditTransferInitiationV09::getPmtInf)
					.map(com.bornfire.jaxb.pain_001_001_09.PaymentInstruction301::getCdtTrfTxInf)
					.filter(indAliasList -> !indAliasList.isEmpty()).map(indAliasList -> indAliasList.get(0))
					.map(com.bornfire.jaxb.pain_001_001_09.CreditTransferTransaction341::getRgltryRptg)
					.filter(indAliasList -> !indAliasList.isEmpty()).map(indAliasList -> indAliasList.get(0))
					.map(com.bornfire.jaxb.pain_001_001_09.RegulatoryReporting31::getDtls)
					.map(com.bornfire.jaxb.pain_001_001_09.StructuredRegulatoryReporting31::getInf)
					.filter(indAliasList -> !indAliasList.isEmpty()).map(indAliasList -> indAliasList.get(0))
					.orElse("");
			
			String rmt_info_pain001 = Optional.ofNullable(docPain001_001_09)
					.map(com.bornfire.jaxb.pain_001_001_09.Document::getCstmrCdtTrfInitn)
					.map(com.bornfire.jaxb.pain_001_001_09.CustomerCreditTransferInitiationV09::getPmtInf)
					.map(com.bornfire.jaxb.pain_001_001_09.PaymentInstruction301::getCdtTrfTxInf)
					.filter(indAliasList -> !indAliasList.isEmpty()).map(indAliasList -> indAliasList.get(0))
					.map(com.bornfire.jaxb.pain_001_001_09.CreditTransferTransaction341::getRmtInf)
					.map(com.bornfire.jaxb.pain_001_001_09.RemittanceInformation161::getUstrd)
					.filter(indAliasList -> !indAliasList.isEmpty()).map(indAliasList -> indAliasList.get(0))
					.orElse("");
		

			String pmt_inf_id_pain001=Optional.ofNullable(docPain001_001_09)
					.map(com.bornfire.jaxb.pain_001_001_09.Document::getCstmrCdtTrfInitn)
					.map(com.bornfire.jaxb.pain_001_001_09.CustomerCreditTransferInitiationV09::getPmtInf)
					.map(com.bornfire.jaxb.pain_001_001_09.PaymentInstruction301::getPmtInfId)
					.orElse("");
			
			String pmt_mtd_pain001=Optional.ofNullable(docPain001_001_09)
					.map(com.bornfire.jaxb.pain_001_001_09.Document::getCstmrCdtTrfInitn)
					.map(com.bornfire.jaxb.pain_001_001_09.CustomerCreditTransferInitiationV09::getPmtInf)
					.map(com.bornfire.jaxb.pain_001_001_09.PaymentInstruction301::getPmtMtd)
					.map(com.bornfire.jaxb.pain_001_001_09.PaymentMethod3Code1::value)
					.orElse("");
			
			String instr_id_pain001 = docPain001_001_09.getCstmrCdtTrfInitn().getPmtInf().getCdtTrfTxInf().get(0).getPmtId()
					.getInstrId();
			
			String svc_lvl_pain001=docPain001_001_09.getCstmrCdtTrfInitn().getPmtInf().getCdtTrfTxInf().get(0).getPmtTpInf().getSvcLvl()
					.get(0).getPrtry();
			
			String lcl_instrm_pain001=docPain001_001_09.getCstmrCdtTrfInitn().getPmtInf().getCdtTrfTxInf().get(0).getPmtTpInf()
					.getLclInstrm().getPrtry();
			
			String ctgy_purp_pain001=docPain001_001_09.getCstmrCdtTrfInitn().getPmtInf().getCdtTrfTxInf().get(0).getPmtTpInf()
					.getCtgyPurp().getPrtry();
			
			logger.info("System Trace Audit Number :" + sysTraceNumber);
			logger.info("BOB Message ID :" + bobMsgID);
			logger.info("IPSX Message ID :" + ipsxMsgID);
			logger.info("Sequence Unique ID :" + seqUniqueID);

			///// Get Bank Code
			String othBankCode1 = ipsDao.getOtherBankCode(creditorAgentPain001);

			////// Register Pain001 RTP Message
			ipsDao.insertTranIPS(seqUniqueID, seqUniqueID, "pain.001.001.09", "RTP", "", "", "", "I", msgSender,
					msgReceiver, msgNetMIR, userReference,endToEndID,docPacs.getPain001_001_09UnMarshalDocXML(request));
			logger.info(seqUniqueID + ": Register RTP Message to table");

			String stat = ipsDao.RegisterRTPMsgRecord(sysTraceNumber, bobMsgID, ipsxMsgID, seqUniqueID, creditorAccount,
					debtorAccount, currency, trAmount, TranMonitorStatus.INITIATED.toString(), endToEndID,
					debtorAccountName, creditorAccountName, tran_type_codePain001, othBankCode1,instgAgtPain001,instdAgtPain001,
					reg_rep_pain001,rmt_info_pain001,pmt_inf_id_pain001,pmt_mtd_pain001,instr_id_pain001,svc_lvl_pain001,
					lcl_instrm_pain001,ctgy_purp_pain001,debtorAgentPain001,debtorAgentAccPain001,creditorAgentPain001,creditorAgentAccPain001);
			logger.info(seqUniqueID + " :Status Of RTP" + stat);

			
			String CreditStatusType = null;
			String CreditStatusCode = null;
			String CreditStatusDesc = null;
			
			List<TranIPSTable> listPainVal = tranIPSTableRep.getCustomResultPain002(seqUniqueID);

			if (listPainVal.size() > 0) {
				if (listPainVal.get(0).getResponse_status().equals(TranMonitorStatus.RJCT.toString())) {
					ipsDao.updateIPSXStatusResponseRJCT(listPainVal.get(0).getSequence_unique_id(),
							listPainVal.get(0).getResponse_error_desc(), listPainVal.get(0).getMsg_id(),
							TranMonitorStatus.IN_PROGRESS.toString(),
							TranMonitorStatus.IPSX_RESPONSE_RJCT.toString(),
							listPainVal.get(0).getResponse_status(), listPainVal.get(0).getResponse_error_code(),"pain.001.001.09");
				}

			}else {
			///// Calling Connect 24 for RTP
				/*String rtpFundResponse = ipsConnection.rtpFundTransferConnection(sysTraceNumber, debtorAccount, currency,
						trAmount, seqUniqueID,
						docPain001_001_09.getCstmrCdtTrfInitn().getGrpHdr().getAuthstn().get(0).getPrtry(), creditorAccount,
						endToEndID, debtorAccountName,"RTP/"+othBankCode1+"/"+creditorAccount+"/"+creditorAccountName,request);
				logger.info(seqUniqueID + ": Status Of rtp Whole " + rtpFundResponse);
*/
				
				String rtpFundResponse= errorCode.ErrorCode("AG01");
				if (rtpFundResponse.split(":")[0].equals("CIM0")) {

					ipsDao.updateCBSStatus(seqUniqueID, TranMonitorStatus.CBS_DEBIT_OK.toString(),
							TranMonitorStatus.IN_PROGRESS.toString());
					logger.info(seqUniqueID + ": Insert Tran IPS Table pacs.008.001.08");

					
					List<TranIPSTable> listPain = tranIPSTableRep.getCustomResultPain002(seqUniqueID);

					if (listPain.size() > 0) {
						if (listPain.get(0).getResponse_status().equals(TranMonitorStatus.RJCT.toString())) {
							ipsDao.updateIPSXStatusResponseRJCT(listPain.get(0).getSequence_unique_id(),
									listPain.get(0).getResponse_error_desc(), listPain.get(0).getMsg_id(),
									TranMonitorStatus.IN_PROGRESS.toString(),
									TranMonitorStatus.IPSX_RESPONSE_RJCT.toString(),
									listPain.get(0).getResponse_status(), listPain.get(0).getResponse_error_code(),"pain.001.001.09");
						}

					}else {
						//// OutPainMsgId
						String msgSeqPain001Pain008 = sequence.generateMsgSequence();
						String msgNetMirPain001Pain008 = new SimpleDateFormat("yyMMdd").format(new Date())
								+ env.getProperty("ipsx.user") + "0001" + msgSeqPain001Pain008;
						

						///// Create Pacs008 Package
						logger.info(seqUniqueID + ": Create Pacs008 package");
						SendT sendRequest = new SendT();
						sendRequest.setMessage(paramMTMsgs.getRTPParamMTmsgPacs008(DocType.pacs_008_001_08.getDocs(), // msgType
								request, bobMsgID, msgSeqPain001Pain008));

						ipsDao.insertTranIPS(seqUniqueID, bobMsgID, "pacs.008.001.08", "", "", "", "", "O",
								env.getProperty("ipsx.sender"), env.getProperty("ipsx.msgReceiver"), msgNetMirPain001Pain008,
								"",endToEndID,docPacs.getPacs_008_001_01UnMarshalDocXML(sendRequest));
						///// Send Pacs008 to IPSX
						com.bornfire.jaxb.wsdl.ObjectFactory obj = new com.bornfire.jaxb.wsdl.ObjectFactory();
						JAXBElement<SendT> jaxbElement = obj.createSend(sendRequest);
						logger.info(seqUniqueID + ": Sending pacs.008.001.01 to ipsx");
						JAXBElement<SendResponse> response = null;
						try {
							response = (JAXBElement<SendResponse>) getWebServiceTemplate().marshalSendAndReceive(jaxbElement);
						} catch (Exception e) {
							taskExecutor.execute(new Runnable() {
								@Override
								public void run() {

									logger.info(seqUniqueID + ": IPSX Not Connected");

									ipsDao.updateIPSXStatusResponseRJCT(seqUniqueID,
											TranMonitorStatus.IPSX_NOT_CONNECTED.toString(), ipsxMsgID,
											TranMonitorStatus.IN_PROGRESS.toString(), TranMonitorStatus.IPSX_ERROR.toString(),
											"", SERVER_ERROR_CODE,"pain.001.001.09");

								}
							});

							logger.error(e.getLocalizedMessage());
						}

						///// Get ACk,NAK Message from IPSX
						logger.info(
								seqUniqueID + " :Get ACK/NAK from IPSX :" + response.getValue().getData().getType().toString());
						if (response.getValue().getData().getType().toString().equals("ACK")) {

							ipsDao.updateIPSXStatus(seqUniqueID, TranMonitorStatus.IPSX_RTP_ACK_RECEIVED.toString(),
									TranMonitorStatus.IN_PROGRESS.toString());
							
							String NetMIR = response.getValue().getData().getMir();
							String UserRef = response.getValue().getData().getRef();

							ipsDao.updateTranIPSACK(seqUniqueID, bobMsgID, "O", "SUCCESS", NetMIR, UserRef);
							logger.info(seqUniqueID + " :ACK Received");
							
							
							List<TranIPSTable> listPain1 = tranIPSTableRep.getCustomResultPain002(seqUniqueID);

							
							if (listPain1.size() > 0) {
								if (listPain1.get(0).getResponse_status().equals(TranMonitorStatus.RJCT.toString())) {
									ipsDao.updateIPSXStatusResponseRJCT(listPain1.get(0).getSequence_unique_id(),
											listPain1.get(0).getResponse_error_desc(), listPain1.get(0).getMsg_id(),
											TranMonitorStatus.IN_PROGRESS.toString(),
											TranMonitorStatus.IPSX_RESPONSE_RJCT.toString(),
											listPain1.get(0).getResponse_status(), listPain1.get(0).getResponse_error_code(),"pain.001.001.09");
								}

							}

						} else {

							String NetMIR = response.getValue().getData().getMir();
							String UserRef = response.getValue().getData().getRef();
							String error = response.getValue().getData().getDescription();

							logger.info(seqUniqueID + " :NAK Received");
							ipsDao.updateTranIPSACK(seqUniqueID, bobMsgID, "O", "FAILURE", NetMIR, UserRef);
							ipsDao.updateIPSXStatus(seqUniqueID, TranMonitorStatus.IPSX_RTP_NAK_RECEICED.toString(),
									TranMonitorStatus.IN_PROGRESS.toString());
							logger.info(seqUniqueID + " :update CBS debit reverse to Table");
							ipsDao.updateCBSStatus(seqUniqueID, TranMonitorStatus.CBS_DEBIT_REVERSE_INITIATED.toString(),
									TranMonitorStatus.IN_PROGRESS.toString());
							logger.info(seqUniqueID + " :Calling Connect24 For Reverse Fund Transfer");
							connect24Service.rtpReverseFundRequest(debtorAccount, sequence.generateSystemTraceAuditNumber(),
									currency, trAmount, seqUniqueID,"RT/"+error);

						}
					}
					


				} else {
					CreditStatusDesc = rtpFundResponse.split(":")[1];
					CreditStatusCode = rtpFundResponse.split(":")[0];

					
					List<TranIPSTable> listPain = tranIPSTableRep.getCustomResultPain002(seqUniqueID);

					if (listPain.size() > 0) {
						if (listPain.get(0).getResponse_status().equals(TranMonitorStatus.RJCT.toString())) {
							ipsDao.updateIPSXStatusResponseRJCT(listPain.get(0).getSequence_unique_id(),
									listPain.get(0).getResponse_error_desc(), listPain.get(0).getMsg_id(),
									TranMonitorStatus.IN_PROGRESS.toString(),
									TranMonitorStatus.IPSX_RESPONSE_RJCT.toString(),
									listPain.get(0).getResponse_status(), listPain.get(0).getResponse_error_code(),"pain.001.001.09");
						}

					}else {
						
					//// OutPainMsgId
						
						String msgSeqPain001Pain002 = sequence.generateMsgSequence();
						String msgNetMirPain001Pain002 = new SimpleDateFormat("yyMMdd").format(new Date())
								+ env.getProperty("ipsx.user") + "0001" + msgSeqPain001Pain002;

						logger.info(seqUniqueID + ": Insert Tran IPS Table pain.002.001.10");
						
						logger.info(seqUniqueID + ": Create  pain.002.001.10 Package");

						///// Create Pain 002 Pacs
						SendT sendRequest = new SendT();
						sendRequest.setMessage(paramMTMsgs.getRTPParamMTmsgPain002(DocType.pain_002_001_10.getDocs(), // msgType
								request, bobMsgID, CreditStatusCode, CreditStatusDesc, msgSeqPain001Pain002));

						ipsDao.insertTranIPS(seqUniqueID, bobMsgID, "pain.002.001.10", "", "", CreditStatusCode,
								CreditStatusDesc, "O", env.getProperty("ipsx.sender"), env.getProperty("ipsx.msgReceiver"),
								msgNetMirPain001Pain002, "",endToEndID,docPacs.getPain_002_001_10UnMarshalDocXML(sendRequest));
						
						///// Send Pain002 to IPSX
						
						com.bornfire.jaxb.wsdl.ObjectFactory obj = new com.bornfire.jaxb.wsdl.ObjectFactory();
						JAXBElement<SendT> jaxbElement = obj.createSend(sendRequest);
						logger.info(seqUniqueID + ": Send  pain.002.001.10 Package");
						JAXBElement<SendResponse> response = null;
						try {
							response = (JAXBElement<SendResponse>) getWebServiceTemplate().marshalSendAndReceive(jaxbElement);
						} catch (Exception e) {
							taskExecutor.execute(new Runnable() {
								@Override
								public void run() {
									logger.info(seqUniqueID + ": IPSX Not Connected");

									ipsDao.updateIPSXStatusResponseRJCT(seqUniqueID,
											TranMonitorStatus.IPSX_NOT_CONNECTED.toString(), "NONE",
											TranMonitorStatus.FAILURE.toString(), TranMonitorStatus.IPSX_ERROR.toString(), "",
											SERVER_ERROR_CODE,"pain.001.001.09");

								}
							});
							logger.error(e.getLocalizedMessage());
						}

						///// Get ACK,NAK from IPSX
						logger.info(
								seqUniqueID + " :Get ACK/NAK from IPSX :" + response.getValue().getData().getType().toString());
						if (response.getValue().getData().getType().toString().equals("ACK")) {
							logger.info(seqUniqueID + " :ACK Received");
							String NetMIR = response.getValue().getData().getMir();
							String UserRef = response.getValue().getData().getRef();

							ipsDao.updateTranIPSACK(seqUniqueID, bobMsgID, "O", "SUCCESS", NetMIR, UserRef);

							ipsDao.updateIPSXStatus(seqUniqueID, TranMonitorStatus.IPSX_RTP_ACK_RECEIVED.toString(),
									TranMonitorStatus.FAILURE.toString());
						} else {
							String NetMIR = response.getValue().getData().getMir();
							String UserRef = response.getValue().getData().getRef();

							ipsDao.updateTranIPSACK(seqUniqueID, bobMsgID, "O", "FAILURE", NetMIR, UserRef);
							logger.info(seqUniqueID + " :NACK Received");

							ipsDao.updateIPSXStatus(seqUniqueID, TranMonitorStatus.IPSX_RTP_NAK_RECEICED.toString(),
									TranMonitorStatus.FAILURE.toString());
						}

					}
					
				}
			}
			

			break;

		case "pain.002.001.10":

			logger.info("Received Pain002 from IPSX");

			///// Get Pain002 Pacs from IPSX
			com.bornfire.jaxb.pain_002_001_10.Document docPain002 = docPacs.getPain_002_001_10UnMarshalDoc(request);

			//// Orgl Msg ID
			String orglMsgID = docPain002.getCstmrPmtStsRpt().getOrgnlGrpInfAndSts().getOrgnlMsgId();
			//// Msg ID
			String msgID = docPain002.getCstmrPmtStsRpt().getGrpHdr().getMsgId();
			//// Transaction Status
			String tranStatus = docPain002.getCstmrPmtStsRpt().getOrgnlPmtInfAndSts().get(0).getTxInfAndSts().get(0)
					.getTxSts();
			
			String endToEndIDPain002 = docPain002.getCstmrPmtStsRpt().getOrgnlPmtInfAndSts().get(0).getTxInfAndSts().get(0)
					.getOrgnlEndToEndId();
			
			
			
			/*String instgId=Optional.ofNullable(docPain002)
			.map(com.bornfire.jaxb.pain_002_001_10.Document::getCstmrPmtStsRpt)
			.map(com.bornfire.jaxb.pain_002_001_10.CustomerPaymentStatusReportV10::getGrpHdr)
			.map(com.bornfire.jaxb.pain_002_001_10.GroupHeader861::getInitgPty)
			.map(com.bornfire.jaxb.pain_002_001_10.PartyIdentification1351::getId)
			.map(com.bornfire.jaxb.pain_002_001_10.Party38Choice1::getOrgId)
			.map(com.bornfire.jaxb.pain_002_001_10.OrganisationIdentification291::getAnyBIC).orElse("");
*/
			logger.info(orglMsgID + " : Get Pain002 from IPSX");
			logger.info("Transaction Status from Pain002 ,ID"
					+ docPain002.getCstmrPmtStsRpt().getOrgnlPmtInfAndSts().get(0).getTxInfAndSts().get(0).getTxSts());

			
			String orglTxRefDate =docPain002.getCstmrPmtStsRpt().getOrgnlPmtInfAndSts().get(0).getTxInfAndSts().get(0).getOrgnlTxRef().getReqdExctnDt().getDt().toString();

			logger.info("datae-"+orglTxRefDate);
			
			if (tranStatus.equals(TranMonitorStatus.ACSP.toString())) {

				

				logger.info(orglMsgID + " : Update IPSX response ACSP to Table");

				
				//if (!userReference.equals("")) {
					ipsDao.updateIPSXStatusResponseACSP(orglMsgID, msgID, TranMonitorStatus.SUCCESS.toString(),"pain.002.001.10");
				//}
				
				logger.info(orglMsgID + " : Insert Tran IPS Table Pain002.001.10");
				ipsDao.insertTranIPS(orglMsgID, msgID, "pain.002.001.10", "", "ACSP", "", "", "I", msgSender,
						msgReceiver, msgNetMIR, userReference,endToEndIDPain002,docPacs.getPain_002_001_10UnMarshalDocXML(request));
				
				//if(instgId.equals(env.getProperty("ipsx.bicfi"))) {
					ipsDao.updateIPSXStatusResponseACSPBulkRTP(orglMsgID, msgID,
							TranMonitorStatus.SUCCESS.toString(), "pain.002.001.10",orglTxRefDate);
				//}
					


			} else {
				
				logger.info("Transaction Status from Pain002 ,ID"
						+ docPain002.getCstmrPmtStsRpt().getOrgnlPmtInfAndSts().get(0).getTxInfAndSts().get(0).getTxSts()
						+ " ,Status :"
						+ docPain002.getCstmrPmtStsRpt().getOrgnlPmtInfAndSts().get(0).getTxInfAndSts().get(0)
								.getStsRsnInf().get(0).getAddtlInf().get(0)
						+ " ," + docPain002.getCstmrPmtStsRpt().getGrpHdr().getMsgId());

				

			//// Error Code
				
				List<com.bornfire.jaxb.pain_002_001_10.StatusReasonInformation121> listPainRsn = docPain002.getCstmrPmtStsRpt().getOrgnlPmtInfAndSts().get(0).getTxInfAndSts().get(0)
						.getStsRsnInf();
				
				List<String> rsnList=new ArrayList<String>();
				List<String> rsnListDesc=new ArrayList<String>();
				for(com.bornfire.jaxb.pain_002_001_10.StatusReasonInformation121 listData:listPainRsn) {
					rsnList.add(listData.getRsn().getPrtry());
					rsnListDesc.add(listData.getAddtlInf().get(0));
				}
				/*String tranErrorCode = docPain002.getCstmrPmtStsRpt().getOrgnlPmtInfAndSts().get(0).getTxInfAndSts().get(0)
						.getStsRsnInf().get(0).getRsn().getPrtry();*/
				String tranErrorCode=String.join("/", rsnList);
				String tranErrorDesc=String.join("/", rsnListDesc);
				
				//// Error Desc
				/*String tranErrorDesc = docPain002.getCstmrPmtStsRpt().getOrgnlPmtInfAndSts().get(0).getTxInfAndSts().get(0)
						.getStsRsnInf().get(0).getAddtlInf().get(0);*/
				
				logger.info(orglMsgID + " : Update IPSX response RJCT to Table");
				//if (!userReference.equals("")) {
					ipsDao.updateIPSXStatusResponseRJCT(orglMsgID, tranErrorDesc, msgID,
							TranMonitorStatus.FAILURE.toString(), TranMonitorStatus.IPSX_RESPONSE_RJCT.toString(),
							TranMonitorStatus.RJCT.toString(), tranErrorCode,"pain.002.001.10");
				//}
				
				logger.info(orglMsgID + " : Insert Tran IPS Table Pain002.001.10");
				ipsDao.insertTranIPS(orglMsgID, msgID, "pain.002.001.10", "", "RJCT", tranErrorCode, tranErrorDesc, "I",
						msgSender, msgReceiver, msgNetMIR, userReference,endToEndIDPain002,docPacs.getPain_002_001_10UnMarshalDocXML(request));
				
				//if(instgId.equals(env.getProperty("ipsx.bicfi"))) {
					ipsDao.updateIPSXStatusResponseRJCTBulkRTP(orglMsgID, tranErrorDesc, msgID,
							TranMonitorStatus.FAILURE.toString(), TranMonitorStatus.IPSX_RESPONSE_RJCT.toString(),
							TranMonitorStatus.RJCT.toString(), tranErrorCode);
				//}


			}
			break;

		}

	}

	@SuppressWarnings("unchecked")
	public String setSL() {

		logger.info("Creating camt011 message");
		SendT sendRequest = new SendT();
		String bobMsgID = "BOB" + sequence.generateSystemTraceAuditNumber();
		sendRequest.setMessage(paramMTMsgs.getParamMTmsgCamt011(DocType.camt_011_001_07.getDocs(), bobMsgID));

		com.bornfire.jaxb.wsdl.ObjectFactory obj = new com.bornfire.jaxb.wsdl.ObjectFactory();
		JAXBElement<SendT> jaxbElement = obj.createSend(sendRequest);
		/************************/

		/**** Send Pacs008 Request to IPSX *****/
		logger.info("Sending pacs.008.001.01 to ipsx");
		JAXBElement<SendResponse> response;
		try {
			response = (JAXBElement<SendResponse>) getWebServiceTemplate().marshalSendAndReceive(jaxbElement);
			logger.info("Get ACK/NAK from IPSX :" + response.getValue().getData().getType().toString());
			System.out.println("okkk");
			return "suceess";
		} catch (Exception e) {
			logger.info(e.getLocalizedMessage());
			e.printStackTrace();
			throw new ServerErrorException(SERVER_ERROR);
		}

	}

	@SuppressWarnings("unchecked")
	public SettlementLimitResponse generateSLReport(String bobMsgID) {
		logger.info("Creating camt011 message");
		SendT sendRequest = new SendT();

		sendRequest.setMessage(paramMTMsgs.getParamMTmsgCamt009(DocType.camt_009_001_07.getDocs(), bobMsgID));

		com.bornfire.jaxb.wsdl.ObjectFactory obj = new com.bornfire.jaxb.wsdl.ObjectFactory();
		JAXBElement<SendT> jaxbElement = obj.createSend(sendRequest);
		/************************/

		/**** Send Pacs008 Request to IPSX *****/
		logger.info("Sending camt009 to ipsx");
		JAXBElement<SendResponse> response;
		try {
			response = (JAXBElement<SendResponse>) getWebServiceTemplate().marshalSendAndReceive(jaxbElement);
			logger.info("Get ACK/NAK from IPSX :" + response.getValue().getData().getType().toString());
			if (response.getValue().getData().getType().toString().equals("ACK")) {
				String responseStatus = null;
				long start = System.currentTimeMillis();
				long end = start + 30000;
				do {
					if (System.currentTimeMillis() > end) {
						logger.info("Waiting Expired");
						throw new IPSXException(REQUEST_TIME_OUT);
					}
					Session hs = sessionFactory.getCurrentSession();
					Optional<SettlementLimitReportTable> tr = settlementLimitReportRep.findById(bobMsgID);
					if (tr.isPresent()) {
						SettlementLimitReportTable tm2 = tr.get();
						hs.refresh(tm2);
						responseStatus = tm2.getIpsx_response_status();
						if (responseStatus != null) {
							if (!responseStatus.equals("SUCCESS")) {
								logger.info("");
								throw new IPSXException(tm2.getIpsx_status_code() + ":" + tm2.getIpsx_status_error());
							} else {
								DecimalFormat decimalFormat = new DecimalFormat("####,####.##");
								SettlementLimitResponse settlementLimitResponse = new SettlementLimitResponse(
										decimalFormat.format(Double.valueOf(tm2.getAmt().toString())),
										decimalFormat.format(Double.valueOf(tm2.getUsed_amt().toString())),
										decimalFormat.format(Double.valueOf(tm2.getRmng_amt().toString())),
										decimalFormat.format(Double.valueOf(tm2.getUsed_pctg())));
								return settlementLimitResponse;
							}

						}
					}
				} while (responseStatus == null);
			} else {
				throw new IPSXException(
						response.getValue().getData().getCode() + ":" + response.getValue().getData().getDescription());
			}
		} catch (RemoteAccessException e) {
			logger.info(e.getLocalizedMessage());
			e.printStackTrace();
			throw new ServerErrorException(SERVER_ERROR);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public MCCreditTransferResponse sendBulkCreditRequest(
			String frAccountName, String frAccountNumber, String toAccountName, String toAccountNumber, String trAmt,
			String trCurrency, String sysTraceNumber, String bobMsgID, String seqUniqueID, BankAgentTable othBankAgent,
			String msgSeq, String endToEndID, String netMsgMIR) {
		MCCreditTransferResponse mcCreditTransferResponse = null;

		logger.info("Update IPSX OutMessage Initiated Status ");

		
		ipsDao.updateIPSXStatus(seqUniqueID, TranMonitorStatus.IPSX_OUTMSG_INITIATED.toString(),
				TranMonitorStatus.IN_PROGRESS.toString());

		////// Create Pacs008
		logger.info("Creating pacs.008.001.01 message");
		SendT sendRequest = new SendT();
		sendRequest.setMessage(paramMTMsgs.getParamMTmsgBulkCredit(DocType.pacs_008_001_08.getDocs(), sendRequest,
				bobMsgID, frAccountName, frAccountNumber, toAccountName, toAccountNumber, trAmt, trCurrency,
				othBankAgent, msgSeq, endToEndID));

		ipsDao.insertTranIPS(seqUniqueID, seqUniqueID, "pacs.008.001.08", "BULK_CREDIT", "", "", "", "O",
				env.getProperty("ipsx.sender"), env.getProperty("ipsx.msgReceiver"), netMsgMIR, "",endToEndID,docPacs.getPacs_008_001_01UnMarshalDocXML(sendRequest));
		
		///// Send Pacs008 to IPSX
		logger.info("Sending pacs.008.001.01 to ipsx");
		com.bornfire.jaxb.wsdl.ObjectFactory obj = new com.bornfire.jaxb.wsdl.ObjectFactory();
		JAXBElement<SendT> jaxbElement = obj.createSend(sendRequest);

		JAXBElement<SendResponse> response = null;
		try {
			response = (JAXBElement<SendResponse>) getWebServiceTemplate().marshalSendAndReceive(jaxbElement);
			logger.info("Get ACK/NAK from IPSX :" + response.getValue().getData().getType().toString());
		} catch (Exception e) {
			logger.info(e.getLocalizedMessage());
			e.printStackTrace();
			taskExecutor.execute(new Runnable() {
				@Override
				public void run() {

					ipsDao.updateIPSXStatusResponseRJCT(seqUniqueID, TranMonitorStatus.IPSX_NOT_CONNECTED.toString(),
							"", TranMonitorStatus.IN_PROGRESS.toString(), TranMonitorStatus.IPSX_ERROR.toString(), "",
							SERVER_ERROR_CODE,"");

				}
			});
			return mcCreditTransferResponse;
		}

		///// Get ACK,NAK Msg
		if (response.getValue().getData().getType().toString().equals("ACK")) {
			String NetMIR = response.getValue().getData().getMir();
			String UserRef = response.getValue().getData().getRef();

			logger.info("update Out Message ACK to Table");
			ipsDao.updateTranIPSACK(seqUniqueID, seqUniqueID, "O", "SUCCESS", NetMIR, UserRef);
			ipsDao.updateIPSXStatus(seqUniqueID, TranMonitorStatus.IPSX_OUTMSG_ACK_RECEIVED.toString(),
					TranMonitorStatus.IN_PROGRESS.toString());

		} else {
			String NetMIR = response.getValue().getData().getMir();
			String UserRef = response.getValue().getData().getRef();
			String error=response.getValue().getData().getDescription();

			logger.info("update Out Message NAK to Table");
			ipsDao.updateTranIPSACK(seqUniqueID, seqUniqueID, "O", "FAILURE", NetMIR, UserRef);
			ipsDao.updateIPSXStatus(seqUniqueID, TranMonitorStatus.IPSX_OUTMSG_NAK_RECEICED.toString(),
					TranMonitorStatus.IN_PROGRESS.toString());

			taskExecutor.execute(new Runnable() {
				@Override
				public void run() {
					logger.info("update CBS debit reverse to Table");
					ipsDao.updateCBSStatus(seqUniqueID, TranMonitorStatus.CBS_DEBIT_REVERSE_INITIATED.toString(),
							TranMonitorStatus.IN_PROGRESS.toString());

					logger.info("Calling Connect24 For Reverse Fund Transfer");
					
					connect24Service.dbtReverseFundRequest(frAccountNumber, sequence.generateSystemTraceAuditNumber(),
							trCurrency, trAmt, seqUniqueID,"RT/"+error);
				}
			});

		}

		return mcCreditTransferResponse;

	}

	
	public MCCreditTransferResponse sendManualftRequst(String frAccountName, String frAccountNumber,
			String toAccountName, String toAccountNumber, String trAmt, String trCurrency, String sysTraceNumber,
			String bobMsgID, String seqUniqueID, BankAgentTable othBankAgent, String msgSeq, String endToEndIDt, String msgNetMir) {
		MCCreditTransferResponse mcCreditTransferResponse = null;

		logger.info("Update IPSX OutMessage Initiated Status ");


		ipsDao.updateOutwardIPSXStatus(seqUniqueID, TranMonitorStatus.IPSX_OUTMSG_INITIATED.toString(),
				TranMonitorStatus.IN_PROGRESS.toString());

		/**** Create Pacs008 *****/
		logger.info("Creating pacs.008.001.01 message");

		SendT sendRequest = new SendT();
		
		
		sendRequest.setMessage(paramMTMsgs.getParamMTmsgBulkCredit(DocType.pacs_008_001_08.getDocs(), sendRequest,
				bobMsgID, frAccountName, frAccountNumber, toAccountName, toAccountNumber, trAmt, trCurrency,
				othBankAgent,msgSeq,endToEndIDt));


		
		ipsDao.insertTranIPS(seqUniqueID, seqUniqueID, "pacs.008.001.08", "MANUAL", "", "", "", "O",
				env.getProperty("ipsx.sender"), env.getProperty("ipsx.msgReceiver"), msgNetMir, "",endToEndIDt,docPacs.getPacs_008_001_01UnMarshalDocXML(sendRequest));
		
		com.bornfire.jaxb.wsdl.ObjectFactory obj = new com.bornfire.jaxb.wsdl.ObjectFactory();
		JAXBElement<SendT> jaxbElement = obj.createSend(sendRequest);
		/************************/

		/**** Send Pacs008 Request to IPSX *****/
		logger.info("Sending pacs.008.001.01 to ipsx");
		JAXBElement<SendResponse> response = null;
		try {
			response = (JAXBElement<SendResponse>) getWebServiceTemplate().marshalSendAndReceive(jaxbElement);
			logger.info("Get ACK/NAK from IPSX :" + response.getValue().getData().getType().toString());
		} catch (Exception e) {
			logger.info(e.getLocalizedMessage());
			e.printStackTrace();
			taskExecutor.execute(new Runnable() {
				@Override
				public void run() {

					ipsDao.updateIPSXStatusResponseRJCTBulkRTP(seqUniqueID, TranMonitorStatus.IPSX_NOT_CONNECTED.toString(),
							"", TranMonitorStatus.IN_PROGRESS.toString(), TranMonitorStatus.IPSX_ERROR.toString(), "",
							SERVER_ERROR_CODE);

				}
			});
			return mcCreditTransferResponse;

			// throw new ServerErrorException(SERVER_ERROR);
		}
		/*************************/

		/**** Get ACK/NAK response from IPSX *****/
		if (response.getValue().getData().getType().toString().equals("ACK")) {

			logger.info("update Out Message ACK to Table");
			String NetMIR = response.getValue().getData().getMir();
			String UserRef = response.getValue().getData().getRef();

			ipsDao.updateTranIPSACK(seqUniqueID, seqUniqueID, "O", "SUCCESS", NetMIR, UserRef);

			ipsDao.updateOutwardIPSXStatus(seqUniqueID, TranMonitorStatus.IPSX_OUTMSG_ACK_RECEIVED.toString(),
					TranMonitorStatus.IN_PROGRESS.toString());

		} else {
			logger.info("update Out Message NAK to Table");
			String NetMIR = response.getValue().getData().getMir();
			String UserRef = response.getValue().getData().getRef();

			ipsDao.updateTranIPSACK(seqUniqueID, seqUniqueID, "O", "FAILURE", NetMIR, UserRef);

			ipsDao.updateIPSXStatus(seqUniqueID, TranMonitorStatus.IPSX_OUTMSG_NAK_RECEICED.toString(),
					TranMonitorStatus.IN_PROGRESS.toString());

			logger.info("update CBS debit reverse to Table");
			ipsDao.updateOutwardIPSXStatus(seqUniqueID, TranMonitorStatus.CBS_DEBIT_REVERSE_INITIATED.toString(),
					TranMonitorStatus.IN_PROGRESS.toString());

			/*logger.info("Calling Connect24 For Reverse Fund Transfer");

			connect24Service.dbtReverseFundRequest(frAccountNumber, sequence.generateSystemTraceAuditNumber(),
					trCurrency, trAmt, seqUniqueID,"RT/"+response.getValue().getData().getDescription());
*/
		}

		return mcCreditTransferResponse;
	}

	@SuppressWarnings("unchecked")
	public MCCreditTransferResponse sendBulkDebitFndTransefer(String remitterAcctName, String remitterAcctNumber,
			String benAcctName, String benAcctNumber, String trAmt, String trCurrency,
			String bobMsgID, String seqUniqueID, BankAgentTable othBankAgent, String msgSeq, String endToEndID,
			String msgNetMir) {
		MCCreditTransferResponse mcCreditTransferResponse = null;

		logger.info("Update IPSX OutMessage Initiated Status ");

		
		ipsDao.updateBulkDebitIPSXStatus(seqUniqueID, TranMonitorStatus.IPSX_OUTMSG_INITIATED.toString(),
				TranMonitorStatus.IN_PROGRESS.toString());

		///// Create Pacs 008 Pacs
		logger.info("Creating pacs.008.001.01 message");
		SendT sendRequest = new SendT();
		sendRequest.setMessage(paramMTMsgs.getParamMTmsgBulkDebit(DocType.pacs_008_001_08.getDocs(), sendRequest,
				bobMsgID, remitterAcctName, remitterAcctNumber, benAcctName, benAcctNumber, trAmt, trCurrency,
				othBankAgent, msgSeq, endToEndID));

		ipsDao.insertTranIPS(seqUniqueID, seqUniqueID, "pacs.008.001.08", "BULK_DEBIT", "", "", "", "O",
				env.getProperty("ipsx.sender"), env.getProperty("ipsx.msgReceiver"), msgNetMir, "",endToEndID,docPacs.getPacs_008_001_01UnMarshalDocXML(sendRequest));
		///// Send Pacs008 to IPSX
		logger.info("Sending pacs.008.001.01 to ipsx");
		com.bornfire.jaxb.wsdl.ObjectFactory obj = new com.bornfire.jaxb.wsdl.ObjectFactory();
		JAXBElement<SendT> jaxbElement = obj.createSend(sendRequest);
		logger.info("Sending pacs.008.001.01 to ipsx");
		JAXBElement<SendResponse> response = null;
		try {
			response = (JAXBElement<SendResponse>) getWebServiceTemplate().marshalSendAndReceive(jaxbElement);
			logger.info("Get ACK/NAK from IPSX :" + response.getValue().getData().getType().toString());
		} catch (Exception e) {
			logger.info(e.getLocalizedMessage());
			e.printStackTrace();
			taskExecutor.execute(new Runnable() {
				@Override
				public void run() {

					ipsDao.updateIPSXStatusResponseRJCT(seqUniqueID, TranMonitorStatus.IPSX_NOT_CONNECTED.toString(),
							"", TranMonitorStatus.IN_PROGRESS.toString(), TranMonitorStatus.IPSX_ERROR.toString(), "",
							SERVER_ERROR_CODE,"");

				}
			});
			return mcCreditTransferResponse;
		}

		///// Get ACK,NAK Msg
		if (response.getValue().getData().getType().toString().equals("ACK")) {

			logger.info("update Out Message ACK to Table");
			String NetMIR = response.getValue().getData().getMir();
			String UserRef = response.getValue().getData().getRef();

			ipsDao.updateTranIPSACK(seqUniqueID, seqUniqueID, "O", "SUCCESS", NetMIR, UserRef);

			ipsDao.updateBulkDebitIPSXStatus(seqUniqueID, TranMonitorStatus.IPSX_OUTMSG_ACK_RECEIVED.toString(),
					TranMonitorStatus.IN_PROGRESS.toString());

		} else {
			logger.info("update Out Message NAK to Table");

			String NetMIR = response.getValue().getData().getMir();
			String UserRef = response.getValue().getData().getRef();

			ipsDao.updateTranIPSACK(seqUniqueID, seqUniqueID, "O", "FAILURE", NetMIR, UserRef);

			ipsDao.updateIPSXStatusResponseRJCT(seqUniqueID, TranMonitorStatus.IPSX_OUTMSG_NAK_RECEICED.toString(), "",
					TranMonitorStatus.IN_PROGRESS.toString(), TranMonitorStatus.IPSX_ERROR.toString(), "",
					response.getValue().getData().getCode(),"");

		}

		return mcCreditTransferResponse;
	}


	public void sendBulkRTPRequst(String acctName, String acctNumber, String currencyCode,
			String benName, String benAcctNumber, String trAmt,
			String trRmks, String seqUniqueID, String cimMsgID, String msgSeq, String endTOEndID, String msgNetMir,String cryptogram,
			String instgAgent,String instdAgent,String debtorAgent,String debtorAgentAcct,String CreditorAgent,String CreditorAgentAcct,String lclInstr,String ctgyPurp,String chargeBearer) {
		

			///// Create Pacs.008
			logger.info("Creating pain.001.001.09 message");
			SendT sendRequest = new SendT();
			
			sendRequest.setMessage(paramMTMsgs.getRTPParamMTmsgPain001(DocType.pain_001_001_09.getDocs(),sendRequest, acctName, acctNumber,  currencyCode,
					 benName,  benAcctNumber,  trAmt,
					 trRmks,  seqUniqueID,  cimMsgID,  msgSeq,  endTOEndID,  msgNetMir,cryptogram,
					 instgAgent,instdAgent,debtorAgent,
						debtorAgentAcct,CreditorAgent,CreditorAgentAcct,lclInstr,ctgyPurp,chargeBearer));

			ipsDao.insertTranIPS(seqUniqueID, seqUniqueID, "pain.001.001.09", "BULK_RTP", "", "", "", "O",
					env.getProperty("ipsx.sender"), env.getProperty("ipsx.msgReceiver"), msgNetMir, "",endTOEndID,
					docPacs.getPain001_001_09UnMarshalDocXML(sendRequest));
			
			com.bornfire.jaxb.wsdl.ObjectFactory obj = new com.bornfire.jaxb.wsdl.ObjectFactory();
			JAXBElement<SendT> jaxbElement = obj.createSend(sendRequest);

			///// Send Pacs.008 package to IPSX
			logger.info("Sending pacs.001.001.09 to ipsx");
			JAXBElement<SendResponse> response = null;
			
			try {
				response = (JAXBElement<SendResponse>) getWebServiceTemplate().marshalSendAndReceive(jaxbElement);
				logger.info("Get ACK/NAK from IPSX :" + response.getValue().getData().getType().toString());
			} catch (Exception e) {
				logger.info(e.getLocalizedMessage());
				e.printStackTrace();
				taskExecutor.execute(new Runnable() {
					@Override
					public void run() {

						ipsDao.updateIPSXStatusResponseRJCTBulkRTP(seqUniqueID, TranMonitorStatus.IPSX_NOT_CONNECTED.toString(),
								"", TranMonitorStatus.FAILURE.toString(), TranMonitorStatus.IPSX_ERROR.toString(), "",
								SERVER_ERROR_CODE);

					}
				});

				//throw new ServerErrorException(SERVER_ERROR);
			}

			///// Get ACK/NAK Message from IPSX
			if (response.getValue().getData().getType().toString().equals("ACK")) {

				String NetMIR = response.getValue().getData().getMir();
				String UserRef = response.getValue().getData().getRef();

				////// Update ACK Message
				logger.info("update Out Message ACK to Table");
				ipsDao.updateIPSXStatusBulkRTP(seqUniqueID, TranMonitorStatus.IPSX_OUTMSG_ACK_RECEIVED.toString(),
						TranMonitorStatus.IN_PROGRESS.toString());

				ipsDao.updateTranIPSACK(seqUniqueID, seqUniqueID, "O", "SUCCESS", NetMIR, UserRef);

				///// Waiting Pacs.002 Message from IPSX
				logger.info("Waits for pain.002.001.01 msg from IPSX");
				
				

			} else {
				///// Update NAK Message
				logger.info("update Out Message NAK to Table");
				ipsDao.updateIPSXStatusBulkRTP(seqUniqueID, TranMonitorStatus.IPSX_OUTMSG_NAK_RECEICED.toString(),
						TranMonitorStatus.IN_PROGRESS.toString());

				String NetMIR = response.getValue().getData().getMir();
				String UserRef = response.getValue().getData().getRef();
				ipsDao.updateTranIPSACK(seqUniqueID, seqUniqueID, "O", "FAILURE", NetMIR, UserRef);

				
			}

	}
	
	public MCCreditTransferResponse sendMerchantRTPRequst(String acctName, String acctNumber, String currencyCode,
			String benName, String benAcctNumber, String trAmt,
			String trRmks, String seqUniqueID, String cimMsgID, String msgSeq, String endTOEndID, String msgNetMir,String cryptogram,
			String instgAgent,String instdAgent,String debtorAgent,String debtorAgentAcct,String CreditorAgent,String CreditorAgentAcct,String lclInstr,String ctgyPurp,String chargeBearer,
			CIMMerchantDirectFndRequest cimMerchantRequest,String rmtInfo) {
		
	       
			logger.info("Update IPSX OutMessage Initiated Status ");
			ipsDao.updateIPSXStatus(seqUniqueID, TranMonitorStatus.IPSX_OUTMSG_INITIATED.toString(),
					TranMonitorStatus.IN_PROGRESS.toString());


			///// Create Pacs.008
			logger.info("Creating pain.001.001.09 message");
			SendT sendRequest = new SendT();
			sendRequest.setMessage(paramMTMsgs.getMerchantRTPParamMTmsgPain001(DocType.pain_001_001_09.getDocs(),sendRequest, acctName, acctNumber,  currencyCode,
					 benName,  benAcctNumber,  trAmt,
					 trRmks,  seqUniqueID,  cimMsgID,  msgSeq,  endTOEndID,  msgNetMir,cryptogram,
					 instgAgent,instdAgent,debtorAgent,
						debtorAgentAcct,CreditorAgent,CreditorAgentAcct,lclInstr,ctgyPurp,chargeBearer,
						cimMerchantRequest,rmtInfo));

			 ///// update IPS TranIPS Table
			ipsDao.insertTranIPS(seqUniqueID, seqUniqueID, "pain.001.001.09", "BULK_RTP", "", "", "", "O",
					env.getProperty("ipsx.sender"), env.getProperty("ipsx.msgReceiver"), msgNetMir, "",endTOEndID,docPacs.getPain001_001_09UnMarshalDocXML(sendRequest));

			com.bornfire.jaxb.wsdl.ObjectFactory obj = new com.bornfire.jaxb.wsdl.ObjectFactory();
			JAXBElement<SendT> jaxbElement = obj.createSend(sendRequest);

			///// Send Pacs.008 package to IPSX
			logger.info("Sending pacs.001.001.09 to ipsx");
			JAXBElement<SendResponse> response;
			String responseStatus = null;
			try {
				response = (JAXBElement<SendResponse>) getWebServiceTemplate().marshalSendAndReceive(jaxbElement);
				logger.info("Get ACK/NAK from IPSX :" + response.getValue().getData().getType().toString());
			} catch (Exception e) {
				logger.info(e.getLocalizedMessage());
				e.printStackTrace();
				taskExecutor.execute(new Runnable() {
					@Override
					public void run() {

						ipsDao.updateIPSXStatusResponseRJCTBulkRTP(seqUniqueID, TranMonitorStatus.IPSX_NOT_CONNECTED.toString(),
								"", TranMonitorStatus.FAILURE.toString(), TranMonitorStatus.IPSX_ERROR.toString(), "",
								SERVER_ERROR_CODE);

					}
				});

				throw new ServerErrorException(SERVER_ERROR);
			}

			///// Get ACK/NAK Message from IPSX
			if (response.getValue().getData().getType().toString().equals("ACK")) {

				String NetMIR = response.getValue().getData().getMir();
				String UserRef = response.getValue().getData().getRef();

				////// Update ACK Message
				logger.info("update Out Message ACK to Table");
				ipsDao.updateIPSXStatusBulkRTP(seqUniqueID, TranMonitorStatus.IPSX_OUTMSG_ACK_RECEIVED.toString(),
						TranMonitorStatus.IN_PROGRESS.toString());

				ipsDao.updateTranIPSACK(seqUniqueID, seqUniqueID, "O", "SUCCESS", NetMIR, UserRef);

				///// Waiting Pacs.002 Message from IPSX
				logger.info("Waits for pain.002.001.01 msg from IPSX");
				
				

			} else {
				///// Update NAK Message
				logger.info("update Out Message NAK to Table");
				ipsDao.updateIPSXStatusBulkRTP(seqUniqueID, TranMonitorStatus.IPSX_OUTMSG_NAK_RECEICED.toString(),
						TranMonitorStatus.IN_PROGRESS.toString());

				String NetMIR = response.getValue().getData().getMir();
				String UserRef = response.getValue().getData().getRef();
				ipsDao.updateTranIPSACK(seqUniqueID, seqUniqueID, "O", "FAILURE", NetMIR, UserRef);

				
			}

		return null;
	}

	
	@SuppressWarnings("unchecked")
	public void sendMerchantftRequst(
			CIMMerchantDirectFndRequest mcCreditTransferRequest, String sysTraceNumber, String bobMsgID, String seqUniqueID,
			BankAgentTable othBankAgent, String msgSeq, String endToEndIDt, String msgNetMir,
			String lclInstr,String ctgyPurp,String chrBearer,String rmtInfo,String tot_tran_amount) {


		
		logger.info("Update IPSX OutMessage Initiated Status ");
		ipsDao.updateOutwardIPSXStatus(seqUniqueID, TranMonitorStatus.IPSX_OUTMSG_INITIATED.toString(),
				TranMonitorStatus.IN_PROGRESS.toString());

		///// Create Pacs.008
		logger.info("Creating pacs.008.001.01 message");
		SendT sendRequest = new SendT();
		sendRequest.setMessage(paramMTMsgs.getParamMerchantMTmsg(DocType.pacs_008_001_08.getDocs(), sendRequest, bobMsgID,
				mcCreditTransferRequest, othBankAgent, msgSeq, endToEndIDt, lclInstr, ctgyPurp, chrBearer, rmtInfo,tot_tran_amount));

	///// update IPS TranIPS Table
		ipsDao.insertTranIPS(seqUniqueID, seqUniqueID, "pacs.008.001.08", "OUTGOING", "", "", "", "O",
					env.getProperty("ipsx.sender"), env.getProperty("ipsx.msgReceiver"), msgNetMir, "",endToEndIDt,docPacs.getPacs_008_001_01UnMarshalDocXML(sendRequest));

			
		com.bornfire.jaxb.wsdl.ObjectFactory obj = new com.bornfire.jaxb.wsdl.ObjectFactory();
		JAXBElement<SendT> jaxbElement = obj.createSend(sendRequest);

		///// Send Pacs.008 package to IPSX
		logger.info("Sending pacs.008.001.01 to ipsx");
		JAXBElement<SendResponse> response = null;

		try {
			response = (JAXBElement<SendResponse>) getWebServiceTemplate().marshalSendAndReceive(jaxbElement);
			logger.info("Get ACK/NAK from IPSX :" + response.getValue().getData().getType().toString());
		} catch (Exception e) {
			logger.info(e.getLocalizedMessage());
			e.printStackTrace();
			taskExecutor.execute(new Runnable() {
				@Override
				public void run() {

					/*/////Update IPSX Status to Tran Table
					ipsDao.updateIPSXStatusResponseRJCT(seqUniqueID, TranMonitorStatus.IPSX_NOT_CONNECTED.toString(),
							"", TranMonitorStatus.FAILURE.toString(), TranMonitorStatus.IPSX_ERROR.toString(), "",
							SERVER_ERROR_CODE,"pacs.008.001.08");*/
					
					ipsDao.updateIPSXStatusResponseRJCTBulkRTP(seqUniqueID, TranMonitorStatus.IPSX_NOT_CONNECTED.toString(),
							"", TranMonitorStatus.FAILURE.toString(), TranMonitorStatus.IPSX_ERROR.toString(), "",
							SERVER_ERROR_CODE);

				    ////Send Failure Message to CIM
					//ipsDao.ReturnCIMcnfResponseRJCT(seqUniqueID,"","",TranMonitorStatus.FAILURE.toString(),"","","",SERVER_ERROR_CODE,
							//TranMonitorStatus.IPSX_NOT_CONNECTED.toString());
				}
			});

		}

		///// Get ACK/NAK Message from IPSX
		if (response.getValue().getData().getType().toString().equals("ACK")) {

			String NetMIR = response.getValue().getData().getMir();
			String UserRef = response.getValue().getData().getRef();

			////// Update ACK Message
			logger.info("update Out Message ACK to Table");
			/*ipsDao.updateIPSXStatus(seqUniqueID, TranMonitorStatus.IPSX_OUTMSG_ACK_RECEIVED.toString(),
					TranMonitorStatus.IN_PROGRESS.toString());*/
			
			ipsDao.updateOutwardIPSXStatus(seqUniqueID, TranMonitorStatus.IPSX_OUTMSG_ACK_RECEIVED.toString(),
					TranMonitorStatus.IN_PROGRESS.toString());

			ipsDao.updateTranIPSACK(seqUniqueID, seqUniqueID, "O", "SUCCESS", NetMIR, UserRef);

		} else {
			///// Update NAK Message
			logger.info("update Out Message NAK to Table");
			/*ipsDao.updateIPSXStatus(seqUniqueID, TranMonitorStatus.IPSX_OUTMSG_NAK_RECEICED.toString(),
					TranMonitorStatus.FAILURE.toString());*/
			
			ipsDao.updateOutwardIPSXStatus(seqUniqueID, TranMonitorStatus.IPSX_OUTMSG_NAK_RECEICED.toString(),
					TranMonitorStatus.FAILURE.toString());

			String NetMIR = response.getValue().getData().getMir();
			String UserRef = response.getValue().getData().getRef();
			ipsDao.updateTranIPSACK(seqUniqueID, seqUniqueID, "O", "FAILURE", NetMIR, UserRef);

			
		}

		
	}


	
	
	/*public MCCreditTransferResponse sendBulkRTPRequst(String acctName, String acctNumber, String currencyCode,
			String bank_agent, String bank_agent_account, String benName, String benAcctNumber, String trAmt,
			String trRmks, String seqUniqueID, String cimMsgID, String msgSeq, String endTOEndID, String msgNetMir) {
		
	        ///// update IPS TranIPS Table
			ipsDao.insertTranIPS(seqUniqueID, seqUniqueID, "pain.001.001.09", "BULK_RTP", "", "", "", "O",
					env.getProperty("ipsx.sender"), env.getProperty("ipsx.msgReceiver"), msgNetMir, "");

			logger.info("Update IPSX OutMessage Initiated Status ");
			ipsDao.updateIPSXStatus(seqUniqueID, TranMonitorStatus.IPSX_OUTMSG_INITIATED.toString(),
					TranMonitorStatus.IN_PROGRESS.toString());


			///// Create Pain.001
			logger.info("Creating pain.001.001.09 message");
			SendT sendRequest = new SendT();
			sendRequest.setMessage(paramMTMsgs.getRTPParamMTmsgPain001(DocType.pain_001_001_09.getDocs(),sendRequest, acctName, acctNumber,  currencyCode,
					 bank_agent,  bank_agent_account,  benName,  benAcctNumber,  trAmt,
					 trRmks,  seqUniqueID,  cimMsgID,  msgSeq,  endTOEndID,  msgNetMir));

			//com.bornfire.jaxb.wsdl.ObjectFactory obj = new com.bornfire.jaxb.wsdl.ObjectFactory();
			//JAXBElement<SendT> jaxbElement = obj.createSend(sendRequest);

						
			String NetMIR = endTOEndID;
			String UserRef = endTOEndID;

			////// Update ACK Message
			logger.info("update Out Message ACK to Table");
			ipsDao.updateIPSXStatusBulkRTP(seqUniqueID, TranMonitorStatus.IPSX_OUTMSG_ACK_RECEIVED.toString(),
					TranMonitorStatus.IN_PROGRESS.toString());

			ipsDao.updateTranIPSACK(seqUniqueID, seqUniqueID, "O", "SUCCESS", NetMIR, UserRef);
			
			
			///Pacs008
			String bobMsgID=sequence.generateSeqUniqueID();
			String msgSeqPain001Pain008=sequence.generateSeqUniqueID();
			SendT sendRequest1 = new SendT();
			sendRequest1.setMessage(paramMTMsgs.getRTPParamMTmsgPacs008(DocType.pacs_008_001_08.getDocs(), // msgType
					sendRequest, bobMsgID, msgSeqPain001Pain008));
			
			
			
			Sample(sendRequest1,seqUniqueID);
			///// Waiting Pacs.002 Message from IPSX
			logger.info("Waits for pain.002.001.01 msg from IPSX");

		return null;
	}
	
*/	

}
