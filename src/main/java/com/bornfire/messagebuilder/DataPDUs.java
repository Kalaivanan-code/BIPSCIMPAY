package com.bornfire.messagebuilder;

import java.io.FileNotFoundException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.bornfire.entity.BankAgentTable;
import com.bornfire.entity.CreditTransferTransaction;
import com.bornfire.entity.MCCreditTransferRequest;
import com.bornfire.jaxb.wsdl.SendT;

public class DataPDUs {
	@Autowired
	AppHeaders appHeaders;

	@Autowired
	DocumentPacks documentPacks;

	public String getDataPDUPacs008(String msgType, SendT request, String msgId,MCCreditTransferRequest mcCreditTransferRequest,
			BankAgentTable othBankAgent,String msgSeq,String endToEndID) {

		StringBuilder sb = new StringBuilder();
		sb.append("<DataPDU xmlns=\"urn:cma:stp:xsd:stp.1.0\">\r\n" + "<Body>\r\n");
		sb.append(appHeaders.getAppHeader(msgType,msgSeq));
		sb.append(documentPacks.getPacs_008_001_01Doc(msgId,mcCreditTransferRequest,othBankAgent,msgSeq,endToEndID));
		sb.append("</Body>\r\n" + "</DataPDU>");

		return sb.toString();
	}

	public String getDataPDUPacs002(String msgType, SendT request, String msgId,String creditStatusType,String creditStatusCode,String creditStatusDesc,String msgSeq) {

		StringBuilder sb = new StringBuilder();
		sb.append("<DataPDU xmlns=\"urn:cma:stp:xsd:stp.1.0\">\r\n" + "<Body>\r\n");
		sb.append(appHeaders.getAppHeader002(request,msgType,msgSeq));
		sb.append(documentPacks.getPacs_002_001_10Doc(request,msgId,creditStatusType,creditStatusCode,creditStatusDesc));

		sb.append("</Body>\r\n" + "</DataPDU>");

		return sb.toString();
	}

	public String getRTPDataPDUPacs008(String msgType, SendT request, String bobMsgID,String msgSeq) {
		StringBuilder sb = new StringBuilder();
		sb.append("<DataPDU xmlns=\"urn:cma:stp:xsd:stp.1.0\">\r\n" + "<Body>\r\n");
		sb.append(appHeaders.getRTPAppHeader008(msgType,request,msgSeq));
		sb.append(documentPacks.getRTPPacs_008_001_01Doc(msgType,request,bobMsgID,msgSeq));
		sb.append("</Body>\r\n" + "</DataPDU>");

		return sb.toString();
	}

	public String getDataPDUPain002(String msgType, SendT request, String bobMsgID,String CreditStatusCode,String CreditStatusDesc,String msgSeq) {
		StringBuilder sb = new StringBuilder();
		sb.append("<DataPDU xmlns=\"urn:cma:stp:xsd:stp.1.0\">\r\n" + "<Body>\r\n");
		sb.append(appHeaders.getAppHeaderPain002(request,msgType,bobMsgID,msgSeq));
		sb.append(documentPacks.getPain_002_001_10Doc(msgType,request,bobMsgID,CreditStatusCode,CreditStatusDesc));

		sb.append("</Body>\r\n" + "</DataPDU>");

		return sb.toString();
	}

	public String getDataPDUCamt011(String msgType, String bobMsgID,String msgSeq) {
		StringBuilder sb = new StringBuilder();
		sb.append("<DataPDU xmlns=\"urn:cma:stp:xsd:stp.1.0\">\r\n" + "<Body>\r\n");
		sb.append(appHeaders.getAppHeaderCamt011(msgType,bobMsgID,msgSeq));
		sb.append(documentPacks.getCamt_011_001_07Doc(msgType,bobMsgID));

		sb.append("</Body>\r\n" + "</DataPDU>");

		return sb.toString();
	}

	public String getDataPDUCamt009(String msgType, String bobMsgID,String msgSeq) {
		StringBuilder sb = new StringBuilder();
		sb.append("<DataPDU xmlns=\"urn:cma:stp:xsd:stp.1.0\">\r\n" + "<Body>\r\n");
		sb.append(appHeaders.getAppHeaderCamt009(msgType,bobMsgID,msgSeq));
		sb.append(documentPacks.getCamt_009_001_07Doc(msgType,bobMsgID));

		sb.append("</Body>\r\n" + "</DataPDU>");

		return sb.toString();
	}

/*	public String getDataPDUBulkCreditPacs008(String msgType, SendT request, String msgId,List<CreditTransferTransaction> creditTranList,String totAmt,String msgSeq) {
		StringBuilder sb = new StringBuilder();
		sb.append("<DataPDU xmlns=\"urn:cma:stp:xsd:stp.1.0\">\r\n" + "<Body>\r\n");
		sb.append(appHeaders.getAppHeader(msgType,msgSeq));
		sb.append(documentPacks.getPacsBulkCredit_008_001_01Doc(msgId,creditTranList,totAmt,msgSeq));
		sb.append("</Body>\r\n" + "</DataPDU>");

		return sb.toString();
	}*/
	
	public String getDataPDUBulkCreditPacs008(String msgType, SendT request, String msgId, String frAccountName,
			String frAccountNumber, String toAccountName, String toAccountNumber, String trAmt, String trCurrency,
			BankAgentTable othBankAgent,String msgSeq,String endToEndID) {
		StringBuilder sb = new StringBuilder();
		sb.append("<DataPDU xmlns=\"urn:cma:stp:xsd:stp.1.0\">\r\n" + "<Body>\r\n");
		sb.append(appHeaders.getAppHeader(msgType,msgSeq));
		sb.append(documentPacks.getPacsBulkCredit_008_001_01Doc(msgId,frAccountName,frAccountNumber,toAccountName,toAccountNumber,trAmt,trCurrency,othBankAgent,msgSeq,endToEndID));
		sb.append("</Body>\r\n" + "</DataPDU>");

		return sb.toString();
	}
	
	public String getDataPDUBulkDebitPacs008(String msgType, SendT request, String msgId, String frAccountName,
			String frAccountNumber, String toAccountName, String toAccountNumber, String trAmt, String trCurrency,
			BankAgentTable othBankAgent,String msgSeq,String endToEndID) {
		StringBuilder sb = new StringBuilder();
		sb.append("<DataPDU xmlns=\"urn:cma:stp:xsd:stp.1.0\">\r\n" + "<Body>\r\n");
		sb.append(appHeaders.getAppHeader(msgType,msgSeq));
		sb.append(documentPacks.getPacsBulkDebit_008_001_01Doc(msgId,frAccountName,frAccountNumber,toAccountName,toAccountNumber,trAmt,trCurrency,othBankAgent,msgSeq,endToEndID));
		sb.append("</Body>\r\n" + "</DataPDU>");

		return sb.toString();
	}


}
