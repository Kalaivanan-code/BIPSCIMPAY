package com.bornfire.messagebuilder;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.bornfire.config.SequenceGenerator;
import com.bornfire.entity.BankAgentTable;
import com.bornfire.entity.CIMCreditTransferRequest;
import com.bornfire.entity.CIMMerchantDirectFndRequest;
import com.bornfire.entity.CreditTransferTransaction;
import com.bornfire.entity.MCCreditTransferRequest;
import com.bornfire.jaxb.wsdl.ParamsMtMsg;
import com.bornfire.jaxb.wsdl.SendT;

public class ParamMTmessage {
	@Autowired
	Environment env;
	
	@Autowired
	DataPDUs dataPDUs;
	
	@Autowired
	SignDocument signDoc;
	
	@Autowired
	SequenceGenerator sequence;
	
	public ParamsMtMsg getParamMTmsg(String msgType,SendT request, String msgId,CIMCreditTransferRequest mcCreditTransferRequest,
			BankAgentTable othBankAgent,String msgSeq,String endToEndID) {
		ParamsMtMsg paramMtMsg = new ParamsMtMsg();
		paramMtMsg.setMsgFormat("S");
		paramMtMsg.setMsgSubFormat("I");
		paramMtMsg.setMsgSender(env.getProperty("ipsx.sender"));
		paramMtMsg.setMsgReceiver(env.getProperty("ipsx.msgReceiver"));
		paramMtMsg.setMsgType(msgType);
		paramMtMsg.setMsgPriority("N");
		paramMtMsg.setMsgDelNotifRq("N");
		paramMtMsg.setMsgUserPriority("0100");
		paramMtMsg.setMsgUserReference("");
		paramMtMsg.setMsgCopySrvId("");
		paramMtMsg.setMsgFinValidation("");
		paramMtMsg.setMsgPde("N");
		paramMtMsg.setMsgSession("0001");
		paramMtMsg.setMsgSequence(msgSeq);
		paramMtMsg.setMsgNetInputTime(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		paramMtMsg.setMsgNetOutputDate(new SimpleDateFormat("yyMMddHHmm").format(new Date()));
		paramMtMsg.setMsgNetMir(new SimpleDateFormat("yyMMdd").format(new Date())+env.getProperty("ipsx.user")+"0001"+msgSeq);
		paramMtMsg.setMsgCopySrvInfo("");
		paramMtMsg.setMsgPacResult("");
		paramMtMsg.setMsgPdm("N");
		paramMtMsg.setFormat("MX");
		//paramMtMsg.setBlock4(signDoc.parseDoc(dataPDUs.getDataPDUPacs008(msgType,request,msgId,mcCreditTransferRequest,othBankAgent,msgSeq,endToEndID)));
		paramMtMsg.setBlock4(dataPDUs.getDataPDUPacs008(msgType,request,msgId,mcCreditTransferRequest,othBankAgent,msgSeq,endToEndID));

		return paramMtMsg;
		
	}
	

	public ParamsMtMsg getParamMerchantMTmsg(String msgType,SendT request, String msgId,CIMMerchantDirectFndRequest mcCreditTransferRequest,
			BankAgentTable othBankAgent,String msgSeq,String endToEndID,
			String lclInstr,String ctgyPurp,String chrBearer,String rmtInfo,String tot_tran_amount) {
		ParamsMtMsg paramMtMsg = new ParamsMtMsg();
		paramMtMsg.setMsgFormat("S");
		paramMtMsg.setMsgSubFormat("I");
		paramMtMsg.setMsgSender(env.getProperty("ipsx.sender"));
		paramMtMsg.setMsgReceiver(env.getProperty("ipsx.msgReceiver"));
		paramMtMsg.setMsgType(msgType);
		paramMtMsg.setMsgPriority("N");
		paramMtMsg.setMsgDelNotifRq("N");
		paramMtMsg.setMsgUserPriority("0100");
		paramMtMsg.setMsgUserReference("");
		paramMtMsg.setMsgCopySrvId("");
		paramMtMsg.setMsgFinValidation("");
		paramMtMsg.setMsgPde("N");
		paramMtMsg.setMsgSession("0001");
		paramMtMsg.setMsgSequence(msgSeq);
		paramMtMsg.setMsgNetInputTime(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		paramMtMsg.setMsgNetOutputDate(new SimpleDateFormat("yyMMddHHmm").format(new Date()));
		paramMtMsg.setMsgNetMir(new SimpleDateFormat("yyMMdd").format(new Date())+env.getProperty("ipsx.user")+"0001"+msgSeq);
		paramMtMsg.setMsgCopySrvInfo("");
		paramMtMsg.setMsgPacResult("");
		paramMtMsg.setMsgPdm("N");
		paramMtMsg.setFormat("MX");
		//paramMtMsg.setBlock4(signDoc.parseDoc(dataPDUs.getDataPDUPacs008(msgType,request,msgId,mcCreditTransferRequest,othBankAgent,msgSeq,endToEndID)));
		paramMtMsg.setBlock4(dataPDUs.getMerchantDataPDUPacs008(msgType,request,msgId,mcCreditTransferRequest,othBankAgent,msgSeq,endToEndID, lclInstr, ctgyPurp, chrBearer, rmtInfo,tot_tran_amount));

		return paramMtMsg;
		
	}


	public ParamsMtMsg getParamMTmsgPacs002(String msgType, SendT request, String msgId, String creditStatusType,String creditStatusCode,
			String creditStatusDesc,String msgSeq) {
		ParamsMtMsg paramMtMsg = new ParamsMtMsg();
		paramMtMsg.setMsgFormat("S");
		paramMtMsg.setMsgSubFormat("I");
		paramMtMsg.setMsgSender(env.getProperty("ipsx.sender"));
		paramMtMsg.setMsgReceiver(env.getProperty("ipsx.msgReceiver"));
		paramMtMsg.setMsgType(msgType);
		paramMtMsg.setMsgPriority("N");
		paramMtMsg.setMsgDelNotifRq("N");
		paramMtMsg.setMsgUserPriority("0100");
		paramMtMsg.setMsgUserReference(request.getMessage().getMsgUserReference());
		paramMtMsg.setMsgCopySrvId("");
		paramMtMsg.setMsgFinValidation("");
		paramMtMsg.setMsgPde("N");
		paramMtMsg.setMsgSession("0001");
		paramMtMsg.setMsgSequence(msgSeq);
		paramMtMsg.setMsgNetInputTime(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		paramMtMsg.setMsgNetOutputDate(new SimpleDateFormat("yyMMddHHmm").format(new Date()));
		paramMtMsg.setMsgNetMir(new SimpleDateFormat("yyMMdd").format(new Date())+env.getProperty("ipsx.user")+"0001"+msgSeq);
		paramMtMsg.setMsgCopySrvInfo("");
		paramMtMsg.setMsgPacResult("");
		paramMtMsg.setMsgPdm("N");
		paramMtMsg.setFormat("MX");
		//paramMtMsg.setBlock4(signDoc.parseDoc(dataPDUs.getDataPDUPacs002(msgType,request,msgId,creditStatusType,creditStatusCode,creditStatusDesc,msgSeq)));
		paramMtMsg.setBlock4(dataPDUs.getDataPDUPacs002(msgType,request,msgId,creditStatusType,creditStatusCode,creditStatusDesc,msgSeq));

		return paramMtMsg;
	}

	public ParamsMtMsg getRTPParamMTmsgPacs008(String msgType, SendT request, String bobMsgID,String msgSeq) {
		
		ParamsMtMsg paramMtMsg = new ParamsMtMsg();
		paramMtMsg.setMsgFormat("S");
		paramMtMsg.setMsgSubFormat("I");
		paramMtMsg.setMsgSender(env.getProperty("ipsx.sender"));
		paramMtMsg.setMsgReceiver(env.getProperty("ipsx.msgReceiver"));
		paramMtMsg.setMsgType(msgType);
		paramMtMsg.setMsgPriority("N");
		paramMtMsg.setMsgDelNotifRq("N");
		paramMtMsg.setMsgUserPriority("0100");
		paramMtMsg.setMsgUserReference("");
		paramMtMsg.setMsgCopySrvId("");
		paramMtMsg.setMsgFinValidation("");
		paramMtMsg.setMsgPde("N");
		paramMtMsg.setMsgSession("0001");
		paramMtMsg.setMsgSequence(msgSeq);
		paramMtMsg.setMsgNetInputTime(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		paramMtMsg.setMsgNetOutputDate(new SimpleDateFormat("yyMMddHHmm").format(new Date()));
		paramMtMsg.setMsgNetMir(new SimpleDateFormat("yyMMdd").format(new Date())+env.getProperty("ipsx.user")+"0001"+msgSeq);
		paramMtMsg.setMsgCopySrvInfo("");
		paramMtMsg.setMsgPacResult("");
		paramMtMsg.setMsgPdm("N");
		paramMtMsg.setFormat("MX");
		//paramMtMsg.setBlock4(signDoc.parseDoc(dataPDUs.getRTPDataPDUPacs008(msgType,request,bobMsgID,msgSeq)));
		paramMtMsg.setBlock4(dataPDUs.getRTPDataPDUPacs008(msgType,request,bobMsgID,msgSeq));

		return paramMtMsg;
	}

	public ParamsMtMsg getRTPParamMTmsgPain002(String msgType, SendT request, String bobMsgID,String CreditStatusCode,String CreditStatusDesc,String msgSeq) {
		ParamsMtMsg paramMtMsg = new ParamsMtMsg();
		paramMtMsg.setMsgFormat("S");
		paramMtMsg.setMsgSubFormat("I");
		paramMtMsg.setMsgSender(env.getProperty("ipsx.sender"));
		paramMtMsg.setMsgReceiver(env.getProperty("ipsx.msgReceiver"));
		paramMtMsg.setMsgType(msgType);
		paramMtMsg.setMsgPriority("N");
		paramMtMsg.setMsgDelNotifRq("N");
		paramMtMsg.setMsgUserPriority("0100");
		paramMtMsg.setMsgUserReference(request.getMessage().getMsgUserReference());
		paramMtMsg.setMsgCopySrvId("");
		paramMtMsg.setMsgFinValidation("");
		paramMtMsg.setMsgPde("N");
		paramMtMsg.setMsgSession("0001");
		paramMtMsg.setMsgSequence(msgSeq);
		paramMtMsg.setMsgNetInputTime(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		paramMtMsg.setMsgNetOutputDate(new SimpleDateFormat("yyMMddHHmm").format(new Date()));
		paramMtMsg.setMsgNetMir(new SimpleDateFormat("yyMMdd").format(new Date())+env.getProperty("ipsx.user")+"0001"+msgSeq);
		paramMtMsg.setMsgCopySrvInfo("");
		paramMtMsg.setMsgPacResult("");
		paramMtMsg.setMsgPdm("N");
		paramMtMsg.setFormat("MX");
		//paramMtMsg.setBlock4(signDoc.parseDoc(dataPDUs.getDataPDUPain002(msgType,request,bobMsgID,CreditStatusCode,CreditStatusDesc,msgSeq)));
		paramMtMsg.setBlock4(dataPDUs.getDataPDUPain002(msgType,request,bobMsgID,CreditStatusCode,CreditStatusDesc,msgSeq));

		return paramMtMsg;
	}

	public ParamsMtMsg getParamMTmsgCamt011(String msgType, String bobMsgID) {
		ParamsMtMsg paramMtMsg = new ParamsMtMsg();
		paramMtMsg.setMsgFormat("S");
		paramMtMsg.setMsgSubFormat("I");
		paramMtMsg.setMsgSender(env.getProperty("ipsx.sender"));
		paramMtMsg.setMsgReceiver(env.getProperty("ipsx.msgReceiver"));
		paramMtMsg.setMsgType(msgType);
		paramMtMsg.setMsgPriority("N");
		paramMtMsg.setMsgDelNotifRq("N");
		paramMtMsg.setMsgUserPriority("0100");
		paramMtMsg.setMsgUserReference("");
		paramMtMsg.setMsgCopySrvId("");
		paramMtMsg.setMsgFinValidation("");
		paramMtMsg.setMsgPde("N");
		paramMtMsg.setMsgSession("0001");
		String msgSeq=sequence.generateMsgSequence();
		paramMtMsg.setMsgSequence(msgSeq);
		paramMtMsg.setMsgNetInputTime(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		paramMtMsg.setMsgNetOutputDate(new SimpleDateFormat("yyMMddHHmm").format(new Date()));
		paramMtMsg.setMsgNetMir(new SimpleDateFormat("yyMMdd").format(new Date())+env.getProperty("ipsx.user")+"0001"+msgSeq);
		paramMtMsg.setMsgCopySrvInfo("");
		paramMtMsg.setMsgPacResult("");
		paramMtMsg.setMsgPdm("N");
		paramMtMsg.setFormat("MX");
		//paramMtMsg.setBlock4(signDoc.parseDoc(dataPDUs.getDataPDUCamt011(msgType,bobMsgID,msgSeq)));
		paramMtMsg.setBlock4(dataPDUs.getDataPDUCamt011(msgType,bobMsgID,msgSeq));


		return paramMtMsg;
	}

	public ParamsMtMsg getParamMTmsgCamt009(String msgType, String bobMsgID) {
		ParamsMtMsg paramMtMsg = new ParamsMtMsg();
		paramMtMsg.setMsgFormat("S");
		paramMtMsg.setMsgSubFormat("I");
		paramMtMsg.setMsgSender(env.getProperty("ipsx.sender"));
		paramMtMsg.setMsgReceiver(env.getProperty("ipsx.msgReceiver"));
		paramMtMsg.setMsgType(msgType);
		paramMtMsg.setMsgPriority("N");
		paramMtMsg.setMsgDelNotifRq("N");
		paramMtMsg.setMsgUserPriority("0100");
		paramMtMsg.setMsgUserReference("");
		paramMtMsg.setMsgCopySrvId("");
		paramMtMsg.setMsgFinValidation("");
		paramMtMsg.setMsgPde("N");
		paramMtMsg.setMsgSession("0001");
		String msgSeq=sequence.generateMsgSequence();
		paramMtMsg.setMsgSequence(msgSeq);
		paramMtMsg.setMsgNetInputTime(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		paramMtMsg.setMsgNetOutputDate(new SimpleDateFormat("yyMMddHHmm").format(new Date()));
		paramMtMsg.setMsgNetMir(new SimpleDateFormat("yyMMdd").format(new Date())+env.getProperty("ipsx.user")+"0001"+msgSeq);
		paramMtMsg.setMsgCopySrvInfo("");
		paramMtMsg.setMsgPacResult("");
		paramMtMsg.setMsgPdm("N");
		paramMtMsg.setFormat("MX");
		paramMtMsg.setBlock4(dataPDUs.getDataPDUCamt009(msgType,bobMsgID,msgSeq));

		return paramMtMsg;
	}
	
	public ParamsMtMsg getParamMTmsgBulkCredit(String msgType, SendT request, String msgId, String frAccountName,
			String frAccountNumber, String toAccountName, String toAccountNumber, String trAmt, String trCurrency,
			BankAgentTable othBankAgent,String msgSeq,String endToEndID) {
		ParamsMtMsg paramMtMsg = new ParamsMtMsg();
		paramMtMsg.setMsgFormat("S");
		paramMtMsg.setMsgSubFormat("I");
		paramMtMsg.setMsgSender(env.getProperty("ipsx.sender"));
		paramMtMsg.setMsgReceiver(env.getProperty("ipsx.msgReceiver"));
		paramMtMsg.setMsgType(msgType);
		paramMtMsg.setMsgPriority("N");
		paramMtMsg.setMsgDelNotifRq("N");
		paramMtMsg.setMsgUserPriority("0100");
		paramMtMsg.setMsgUserReference("");
		paramMtMsg.setMsgCopySrvId("");
		paramMtMsg.setMsgFinValidation("");
		paramMtMsg.setMsgPde("N");
		paramMtMsg.setMsgSession("0001");
		paramMtMsg.setMsgSequence(msgSeq);
		paramMtMsg.setMsgNetInputTime(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		paramMtMsg.setMsgNetOutputDate(new SimpleDateFormat("yyMMddHHmm").format(new Date()));
		paramMtMsg.setMsgNetMir(new SimpleDateFormat("yyMMdd").format(new Date())+env.getProperty("ipsx.user")+"0001"+msgSeq);
		paramMtMsg.setMsgCopySrvInfo("");
		paramMtMsg.setMsgPacResult("");
		paramMtMsg.setMsgPdm("N");
		paramMtMsg.setFormat("MX");
		paramMtMsg.setBlock4(dataPDUs.getDataPDUBulkCreditPacs008(msgType,request,msgId,frAccountName,frAccountNumber,toAccountName,toAccountNumber,trAmt,trCurrency,othBankAgent, msgSeq,endToEndID));

		return paramMtMsg;
		

	}

	public ParamsMtMsg getParamMTmsgBulkDebit(String msgType, SendT request, String msgId, String frAccountName,
			String frAccountNumber, String toAccountName, String toAccountNumber, String trAmt, String trCurrency,
			BankAgentTable othBankAgent,String msgSeq,String endToEndID) {
		ParamsMtMsg paramMtMsg = new ParamsMtMsg();
		paramMtMsg.setMsgFormat("S");
		paramMtMsg.setMsgSubFormat("I");
		paramMtMsg.setMsgSender(env.getProperty("ipsx.sender"));
		paramMtMsg.setMsgReceiver(env.getProperty("ipsx.msgReceiver"));
		paramMtMsg.setMsgType(msgType);
		paramMtMsg.setMsgPriority("N");
		paramMtMsg.setMsgDelNotifRq("N");
		paramMtMsg.setMsgUserPriority("0100");
		paramMtMsg.setMsgUserReference("");
		paramMtMsg.setMsgCopySrvId("");
		paramMtMsg.setMsgFinValidation("");
		paramMtMsg.setMsgPde("N");
		paramMtMsg.setMsgSession("0001");
		paramMtMsg.setMsgSequence(msgSeq);
		paramMtMsg.setMsgNetInputTime(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		paramMtMsg.setMsgNetOutputDate(new SimpleDateFormat("yyMMddHHmm").format(new Date()));
		paramMtMsg.setMsgNetMir(new SimpleDateFormat("yyMMdd").format(new Date())+env.getProperty("ipsx.user")+"0001"+msgSeq);
		paramMtMsg.setMsgCopySrvInfo("");
		paramMtMsg.setMsgPacResult("");
		paramMtMsg.setMsgPdm("N");
		paramMtMsg.setFormat("MX");
		paramMtMsg.setBlock4(dataPDUs.getDataPDUBulkDebitPacs008(msgType,request,msgId,frAccountName,frAccountNumber,toAccountName,toAccountNumber,trAmt,trCurrency,othBankAgent, msgSeq,endToEndID));

		return paramMtMsg;
		

	}


	/*public ParamsMtMsg getParamMTmsgBulkCredit(String msgType, SendT request, String msgId, List<CreditTransferTransaction> creditTranList,String totAmt) {
		ParamsMtMsg paramMtMsg = new ParamsMtMsg();
		paramMtMsg.setMsgFormat("S");
		paramMtMsg.setMsgSubFormat("I");
		paramMtMsg.setMsgSender(env.getProperty("ipsx.sender"));
		paramMtMsg.setMsgReceiver("BOMMMUPLXIPS");
		paramMtMsg.setMsgType(msgType);
		paramMtMsg.setMsgPriority("N");
		paramMtMsg.setMsgDelNotifRq("N");
		paramMtMsg.setMsgUserPriority("0100");
		paramMtMsg.setMsgUserReference("");
		paramMtMsg.setMsgCopySrvId("");
		paramMtMsg.setMsgFinValidation("");
		paramMtMsg.setMsgPde("N");
		paramMtMsg.setMsgSession("0001");
		String msgSeq=sequence.generateMsgSequence();
		paramMtMsg.setMsgSequence(msgSeq);
		paramMtMsg.setMsgNetInputTime(new SimpleDateFormat("YYYYMMddHHmmss").format(new Date()));
		paramMtMsg.setMsgNetOutputDate(new SimpleDateFormat("YYMMddHHmm").format(new Date()));
		paramMtMsg.setMsgNetMir(new SimpleDateFormat("YYMMdd").format(new Date())+env.getProperty("ipsx.user")+"0001"+msgSeq);
		paramMtMsg.setMsgCopySrvInfo("");
		paramMtMsg.setMsgPacResult("");
		paramMtMsg.setMsgPdm("N");
		paramMtMsg.setFormat("MX");
		paramMtMsg.setBlock4(signDoc.parseDoc(dataPDUs.getDataPDUBulkCreditPacs008(msgType,request,msgId,creditTranList,totAmt, msgSeq)));

		return paramMtMsg;
		

	}*/
	
	
	public ParamsMtMsg getRTPParamMTmsgPain001(String msgType,SendT request, String acctName, String acctNumber, String currencyCode,
			 String benName, String benAcctNumber, String trAmt,
			String trRmks, String seqUniqueID, String cimMsgID, String msgSeq, String endTOEndID, String msgNetMir,String cryptogram,
			String instgAgent,String instdAgent,String debtorAgent,String debtorAgentAcct,String CreditorAgent,String CreditorAgentAcct,
			String lclInstr,String ctgyPurp,String chargeBearer) {
		ParamsMtMsg paramMtMsg = new ParamsMtMsg();
		paramMtMsg.setMsgFormat("S");
		paramMtMsg.setMsgSubFormat("I");
		paramMtMsg.setMsgSender(env.getProperty("ipsx.sender"));
		paramMtMsg.setMsgReceiver(env.getProperty("ipsx.msgReceiver"));
		paramMtMsg.setMsgType(msgType);
		paramMtMsg.setMsgPriority("N");
		paramMtMsg.setMsgDelNotifRq("N");
		paramMtMsg.setMsgUserPriority("0100");
		paramMtMsg.setMsgUserReference("");
		paramMtMsg.setMsgCopySrvId("");
		paramMtMsg.setMsgFinValidation("");
		paramMtMsg.setMsgPde("N");
		paramMtMsg.setMsgSession("0001");
		paramMtMsg.setMsgSequence(msgSeq);
		paramMtMsg.setMsgNetInputTime(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		paramMtMsg.setMsgNetOutputDate(new SimpleDateFormat("yyMMddHHmm").format(new Date()));
		paramMtMsg.setMsgNetMir(msgNetMir);
		paramMtMsg.setMsgCopySrvInfo("");
		paramMtMsg.setMsgPacResult("");
		paramMtMsg.setMsgPdm("N");
		paramMtMsg.setFormat("MX");
		/*paramMtMsg.setBlock4(signDoc.parseDoc(dataPDUs.getDataPDUPain001(msgType,request,acctName, acctNumber,  currencyCode,
				 bank_agent,  bank_agent_account,  benName,  benAcctNumber,  trAmt,
				 trRmks,  seqUniqueID,  cimMsgID,  msgSeq,  endTOEndID,  msgNetMir)));*/
		
		paramMtMsg.setBlock4(dataPDUs.getDataPDUPain001(msgType,request,acctName, acctNumber,  currencyCode,
				  benName,  benAcctNumber,  trAmt,
				 trRmks,  seqUniqueID,  cimMsgID,  msgSeq,  endTOEndID,  msgNetMir,cryptogram,
				 instgAgent,instdAgent,debtorAgent,
					debtorAgentAcct,CreditorAgent,CreditorAgentAcct,lclInstr,ctgyPurp, chargeBearer));


		return paramMtMsg;
	}
	
	
	public ParamsMtMsg getMerchantRTPParamMTmsgPain001(String msgType,SendT request, String acctName, String acctNumber, String currencyCode,
			 String benName, String benAcctNumber, String trAmt,
			String trRmks, String seqUniqueID, String cimMsgID, String msgSeq, String endTOEndID, String msgNetMir,String cryptogram,
			String instgAgent,String instdAgent,String debtorAgent,String debtorAgentAcct,String CreditorAgent,String CreditorAgentAcct,
			String lclInstr,String ctgyPurp,String chargeBearer,CIMMerchantDirectFndRequest cimMerchantRequest,String rmtInfo) {
		ParamsMtMsg paramMtMsg = new ParamsMtMsg();
		paramMtMsg.setMsgFormat("S");
		paramMtMsg.setMsgSubFormat("I");
		paramMtMsg.setMsgSender(env.getProperty("ipsx.sender"));
		paramMtMsg.setMsgReceiver(env.getProperty("ipsx.msgReceiver"));
		paramMtMsg.setMsgType(msgType);
		paramMtMsg.setMsgPriority("N");
		paramMtMsg.setMsgDelNotifRq("N");
		paramMtMsg.setMsgUserPriority("0100");
		paramMtMsg.setMsgUserReference("");
		paramMtMsg.setMsgCopySrvId("");
		paramMtMsg.setMsgFinValidation("");
		paramMtMsg.setMsgPde("N");
		paramMtMsg.setMsgSession("0001");
		paramMtMsg.setMsgSequence(msgSeq);
		paramMtMsg.setMsgNetInputTime(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		paramMtMsg.setMsgNetOutputDate(new SimpleDateFormat("yyMMddHHmm").format(new Date()));
		paramMtMsg.setMsgNetMir(msgNetMir);
		paramMtMsg.setMsgCopySrvInfo("");
		paramMtMsg.setMsgPacResult("");
		paramMtMsg.setMsgPdm("N");
		paramMtMsg.setFormat("MX");
		/*paramMtMsg.setBlock4(signDoc.parseDoc(dataPDUs.getDataPDUPain001(msgType,request,acctName, acctNumber,  currencyCode,
				 bank_agent,  bank_agent_account,  benName,  benAcctNumber,  trAmt,
				 trRmks,  seqUniqueID,  cimMsgID,  msgSeq,  endTOEndID,  msgNetMir)));*/
		
		paramMtMsg.setBlock4(dataPDUs.getDataPDUMerchantPain001(msgType,request,acctName, acctNumber,  currencyCode,
				  benName,  benAcctNumber,  trAmt,
				 trRmks,  seqUniqueID,  cimMsgID,  msgSeq,  endTOEndID,  msgNetMir,cryptogram,
				 instgAgent,instdAgent,debtorAgent,
					debtorAgentAcct,CreditorAgent,CreditorAgentAcct,lclInstr,ctgyPurp, chargeBearer,cimMerchantRequest,rmtInfo));


		return paramMtMsg;
	}



	
}
