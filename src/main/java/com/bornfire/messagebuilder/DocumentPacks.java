package com.bornfire.messagebuilder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.bornfire.config.Listener;
import com.bornfire.config.SequenceGenerator;
import com.bornfire.entity.BankAgentTable;
import com.bornfire.entity.CIMCreditTransferRequest;
import com.bornfire.entity.CIMMerchantDirectFndRequest;
import com.bornfire.entity.CreditTransferTransaction;
import com.bornfire.entity.MCCreditTransferRequest;
import com.bornfire.entity.TranMonitorStatus;
import com.bornfire.exception.JaxbCharacterEscapeHandler;
import com.bornfire.jaxb.camt_009_001_07.GetLimitV07;
import com.bornfire.jaxb.camt_009_001_07.LimitCriteria61;
import com.bornfire.jaxb.camt_009_001_07.LimitCriteria6Choice1;
import com.bornfire.jaxb.camt_009_001_07.LimitQuery41;
import com.bornfire.jaxb.camt_009_001_07.LimitSearchCriteria61;
import com.bornfire.jaxb.camt_009_001_07.MessageHeader91;
import com.bornfire.jaxb.camt_011_001_07.Amount2Choice1;
import com.bornfire.jaxb.camt_011_001_07.CreditDebitCode;
import com.bornfire.jaxb.camt_011_001_07.Limit81;
import com.bornfire.jaxb.camt_011_001_07.LimitIdentification2Choice1;
import com.bornfire.jaxb.camt_011_001_07.LimitIdentification51;
import com.bornfire.jaxb.camt_011_001_07.LimitStructure31;
import com.bornfire.jaxb.camt_011_001_07.LimitType1Choice1;
import com.bornfire.jaxb.camt_011_001_07.LimitType3Code1;
import com.bornfire.jaxb.camt_011_001_07.MarketInfrastructureIdentification1Choice1;
import com.bornfire.jaxb.camt_011_001_07.MessageHeader11;
import com.bornfire.jaxb.camt_011_001_07.ModifyLimitV07;
import com.bornfire.jaxb.camt_011_001_07.SystemIdentification2Choice1;
import com.bornfire.jaxb.camt_052_001_08.TotalTransactions61;
import com.bornfire.jaxb.pacs_002_001_010.ActiveOrHistoricCurrencyAndAmount;
import com.bornfire.jaxb.pacs_002_001_010.FIToFIPaymentStatusReportV10;
import com.bornfire.jaxb.pacs_002_001_010.GenericFinancialIdentification11;
import com.bornfire.jaxb.pacs_002_001_010.GroupHeader911;
import com.bornfire.jaxb.pacs_002_001_010.ObjectFactory;
import com.bornfire.jaxb.pacs_002_001_010.OriginalGroupHeader171;
import com.bornfire.jaxb.pacs_002_001_010.OriginalTransactionReference281;
import com.bornfire.jaxb.pacs_002_001_010.PaymentTransaction1101;
import com.bornfire.jaxb.pacs_002_001_010.PaymentTypeInformation271;
import com.bornfire.jaxb.pacs_002_001_010.StatusReason6Choice1;
import com.bornfire.jaxb.pacs_002_001_010.StatusReasonInformation121;
import com.bornfire.jaxb.pacs_008_001_08.AccountIdentification4Choice1;
import com.bornfire.jaxb.pacs_008_001_08.ActiveCurrencyAndAmount;
import com.bornfire.jaxb.pacs_008_001_08.BranchAndFinancialInstitutionIdentification61;
import com.bornfire.jaxb.pacs_008_001_08.CashAccount381;
import com.bornfire.jaxb.pacs_008_001_08.CategoryPurpose1Choice1;
import com.bornfire.jaxb.pacs_008_001_08.ChargeBearerType1Code;
import com.bornfire.jaxb.pacs_008_001_08.ClearingChannel2Code1;
import com.bornfire.jaxb.pacs_008_001_08.ClearingSystemMemberIdentification21;
import com.bornfire.jaxb.pacs_008_001_08.CreditTransferTransaction391;
import com.bornfire.jaxb.pacs_008_001_08.Document;
import com.bornfire.jaxb.pacs_008_001_08.FIToFICustomerCreditTransferV08;
import com.bornfire.jaxb.pacs_008_001_08.FinancialIdentificationSchemeName1Choice;
import com.bornfire.jaxb.pacs_008_001_08.FinancialInstitutionIdentification181;
import com.bornfire.jaxb.pacs_008_001_08.GenericAccountIdentification11;
import com.bornfire.jaxb.pacs_008_001_08.GroupHeader931;
import com.bornfire.jaxb.pacs_008_001_08.LocalInstrument2Choice1;
import com.bornfire.jaxb.pacs_008_001_08.PartyIdentification1351;
import com.bornfire.jaxb.pacs_008_001_08.PaymentIdentification71;
import com.bornfire.jaxb.pacs_008_001_08.PaymentTypeInformation281;
import com.bornfire.jaxb.pacs_008_001_08.PostalAddress241;
import com.bornfire.jaxb.pacs_008_001_08.ReferredDocumentInformation71;
import com.bornfire.jaxb.pacs_008_001_08.ReferredDocumentType3Choice1;
import com.bornfire.jaxb.pacs_008_001_08.ReferredDocumentType41;
import com.bornfire.jaxb.pacs_008_001_08.RegulatoryReporting31;
import com.bornfire.jaxb.pacs_008_001_08.RemittanceInformation161;
import com.bornfire.jaxb.pacs_008_001_08.ServiceLevel8Choice1;
import com.bornfire.jaxb.pacs_008_001_08.SettlementInstruction71;
import com.bornfire.jaxb.pacs_008_001_08.SettlementMethod1Code1;
import com.bornfire.jaxb.pacs_008_001_08.StructuredRegulatoryReporting31;
import com.bornfire.jaxb.pacs_008_001_08.StructuredRemittanceInformation161;
import com.bornfire.jaxb.pain_001_001_09.AmountType4Choice1;
import com.bornfire.jaxb.pain_001_001_09.Authorisation1Choice1;
import com.bornfire.jaxb.pain_001_001_09.ChargeBearerType1Code1;
import com.bornfire.jaxb.pain_001_001_09.CreditTransferTransaction341;
import com.bornfire.jaxb.pain_001_001_09.CustomerCreditTransferInitiationV09;
import com.bornfire.jaxb.pain_001_001_09.GroupHeader851;
import com.bornfire.jaxb.pain_001_001_09.PaymentIdentification61;
import com.bornfire.jaxb.pain_001_001_09.PaymentInstruction301;
import com.bornfire.jaxb.pain_001_001_09.PaymentTypeInformation261;
import com.bornfire.jaxb.pain_002_001_10.CustomerPaymentStatusReportV10;
import com.bornfire.jaxb.pain_002_001_10.DateAndDateTime2Choice1;
import com.bornfire.jaxb.pain_002_001_10.GenericOrganisationIdentification11;
import com.bornfire.jaxb.pain_002_001_10.GroupHeader861;
import com.bornfire.jaxb.pain_002_001_10.OrganisationIdentification291;
import com.bornfire.jaxb.pain_002_001_10.OrganisationIdentificationSchemeName1Choice;
import com.bornfire.jaxb.pain_002_001_10.OriginalPaymentInstruction321;
import com.bornfire.jaxb.pain_002_001_10.Party38Choice1;
import com.bornfire.jaxb.pain_002_001_10.PaymentMethod4Code1;
import com.bornfire.jaxb.pain_002_001_10.PaymentTransaction1051;
import com.bornfire.jaxb.wsdl.SendT;

import ch.qos.logback.classic.Logger;

public class DocumentPacks implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7528939653987212494L;

	@Autowired
	SequenceGenerator sequence;

	@Autowired
	Listener listener;

	@Autowired
	Environment env;

	/**** Create Document of Pacs.008.001.08 ****/
	public String getPacs_008_001_01Doc(String msgId, CIMCreditTransferRequest mcCreditTransferRequest,
			BankAgentTable othBankAgent,String msgSeq,String endToEndID) {

///Group Header
		GroupHeader931 grpHdr = new GroupHeader931();
		String msg_seq_id = msgId;
		/// Message Identification ID
		grpHdr.setMsgId(msg_seq_id);
		/// Credit Date Time
		grpHdr.setCreDtTm(listener.getxmlGregorianCalender("0"));
		/// Number Of Transaction
		grpHdr.setNbOfTxs("1");
		/// Total Inter Bank Settlement Amount
		ActiveCurrencyAndAmount activeCurrencyAndAmount = new ActiveCurrencyAndAmount();
		activeCurrencyAndAmount.setCcy(mcCreditTransferRequest.getCurrencyCode());/// Currency
		activeCurrencyAndAmount.setValue(new BigDecimal(mcCreditTransferRequest.getTrAmt()));/// Amount
		grpHdr.setTtlIntrBkSttlmAmt(activeCurrencyAndAmount);
		/// Inter Bank settlement Date
		grpHdr.setIntrBkSttlmDt(listener.getxmlGregorianCalender("1"));
		/// Settlement Instruction
		SettlementInstruction71 sttlmInf = new SettlementInstruction71();
		sttlmInf.setSttlmMtd(SettlementMethod1Code1.CLRG);/// Settlement method (CLRG)
		grpHdr.setSttlmInf(sttlmInf);

///// creditTransaction Information
		List<CreditTransferTransaction391> cdtTrfTxInf = new ArrayList<CreditTransferTransaction391>();
		CreditTransferTransaction391 creditTransferTransaction391 = new CreditTransferTransaction391();
		/// Payment Identification
		PaymentIdentification71 pmtId = new PaymentIdentification71();
		pmtId.setInstrId(msg_seq_id);/// Instruction ID
		
		//String EntToEndID=env.getProperty("ipsx.bicfi")+new SimpleDateFormat("YYYYMMdd").format(new Date())+msgSeq;
		pmtId.setEndToEndId(endToEndID);/// End to End ID
		pmtId.setTxId(msg_seq_id);/// Transaction ID
		creditTransferTransaction391.setPmtId(pmtId);
		/// Payment Type Information
		PaymentTypeInformation281 pmtTpInf = new PaymentTypeInformation281();
		pmtTpInf.setClrChanl(ClearingChannel2Code1.RTGS);/// Clearing Channel like(RTGS,RTNS,MPNS)
		CategoryPurpose1Choice1 ctgyPurp = new CategoryPurpose1Choice1();
		ctgyPurp.setPrtry(mcCreditTransferRequest.getPurpose());
		pmtTpInf.setCtgyPurp(ctgyPurp);/// Category Purpose
		///CSDC-Customer direct Credit Payment
		///BPDC-Bill Payments(Direct Credit)
		LocalInstrument2Choice1 lclInstrm = new LocalInstrument2Choice1();
		lclInstrm.setPrtry("CSDC");
		pmtTpInf.setLclInstrm(lclInstrm);/// Local Instrument
		List<ServiceLevel8Choice1> svcLvl = new ArrayList<ServiceLevel8Choice1>();
		ServiceLevel8Choice1 serlc = new ServiceLevel8Choice1();
		serlc.setPrtry("0100");
		svcLvl.add(serlc);
		pmtTpInf.setSvcLvl(svcLvl);/// Service Level
		creditTransferTransaction391.setPmtTpInf(pmtTpInf);
		/// Inter Bank Settlement Currency And Amount
		ActiveCurrencyAndAmount intrBkSttlmAmt = new ActiveCurrencyAndAmount();
		intrBkSttlmAmt.setCcy(mcCreditTransferRequest.getCurrencyCode());/// Currency
		intrBkSttlmAmt.setValue(new BigDecimal(mcCreditTransferRequest.getTrAmt()));/// Amount
		creditTransferTransaction391.setIntrBkSttlmAmt(intrBkSttlmAmt);
		/// Charge Bearer
		creditTransferTransaction391.setChrgBr(ChargeBearerType1Code.SLEV);
		/// Financial Institution Identification(Instructing Agent)
		FinancialInstitutionIdentification181 fin = new FinancialInstitutionIdentification181();
		fin.setBICFI(env.getProperty("ipsx.bicfi"));
		BranchAndFinancialInstitutionIdentification61 instgAgt = new BranchAndFinancialInstitutionIdentification61();
		instgAgt.setFinInstnId(fin);
		creditTransferTransaction391.setInstgAgt(instgAgt);
		/// Financial Institution Identification(Instructed Agent)
		FinancialInstitutionIdentification181 fin1 = new FinancialInstitutionIdentification181();
		// fin1.setBICFI("TSTAMUMU");
		fin1.setBICFI(othBankAgent.getBank_agent());
		BranchAndFinancialInstitutionIdentification61 instdAgt = new BranchAndFinancialInstitutionIdentification61();
		instdAgt.setFinInstnId(fin1);
		creditTransferTransaction391.setInstdAgt(instdAgt);
		/// Debtor name
		PartyIdentification1351 dbtr = new PartyIdentification1351();
		dbtr.setNm(mcCreditTransferRequest.getFrAccount().getAcctName());
		creditTransferTransaction391.setDbtr(dbtr);
		
	     CashAccount381 dbtrAcct = new CashAccount381();
		 AccountIdentification4Choice1 acc1 = new AccountIdentification4Choice1();
		 GenericAccountIdentification11 id = new GenericAccountIdentification11();
		 id.setId((mcCreditTransferRequest.getFrAccount().getAcctNumber()));
		/* id.setSchmeNm("OBAN");*/
		 
		/* FinancialIdentificationSchemeName1Choice finId=new FinancialIdentificationSchemeName1Choice();
		 finId.setCd("OAB");
		 finId.setPrtry("Office");
		 id.setSchmeNm(finId);*/
		 
		 acc1.setOthr(id); 
		 dbtrAcct.setId(acc1);
		 creditTransferTransaction391.setDbtrAcct(dbtrAcct);
		 
		/// Debtor Agent
		BranchAndFinancialInstitutionIdentification61 dbtrAgt = new BranchAndFinancialInstitutionIdentification61();
		FinancialInstitutionIdentification181 fin2 = new FinancialInstitutionIdentification181();
		fin2.setBICFI(env.getProperty("ipsx.dbtragt"));
		dbtrAgt.setFinInstnId(fin2);
		creditTransferTransaction391.setDbtrAgt(dbtrAgt);
		/// Debtor Agent Account
		CashAccount381 dbtrAgtAcct = new CashAccount381();
		AccountIdentification4Choice1 id2 = new AccountIdentification4Choice1();
		GenericAccountIdentification11 gen1 = new GenericAccountIdentification11();
		gen1.setId(env.getProperty("ipsx.dbtragtacct"));
		id2.setOthr(gen1);
		dbtrAgtAcct.setId(id2);
		creditTransferTransaction391.setDbtrAgtAcct(dbtrAgtAcct);
		/// Creditor Agent
		BranchAndFinancialInstitutionIdentification61 cdtrAgt = new BranchAndFinancialInstitutionIdentification61();
		FinancialInstitutionIdentification181 fin3 = new FinancialInstitutionIdentification181();
		// fin3.setBICFI("TSTAMUMU");
		fin3.setBICFI(othBankAgent.getBank_agent());
		cdtrAgt.setFinInstnId(fin3);
		creditTransferTransaction391.setCdtrAgt(cdtrAgt);
		/// Creditor Agent Account
		CashAccount381 cdtrAgtAcct = new CashAccount381();
		AccountIdentification4Choice1 acc3 = new AccountIdentification4Choice1();
		GenericAccountIdentification11 gen3 = new GenericAccountIdentification11();
		// gen3.setId("TSTBNRT");
		gen3.setId(othBankAgent.getBank_agent_account());
		acc3.setOthr(gen3);
		cdtrAgtAcct.setId(acc3);
		creditTransferTransaction391.setCdtrAgtAcct(cdtrAgtAcct);
		/// Creditor Name
		PartyIdentification1351 cdtr = new PartyIdentification1351();
		cdtr.setNm(mcCreditTransferRequest.getToAccount().getAcctName());
		creditTransferTransaction391.setCdtr(cdtr);
		/// Creditor Account Number
		CashAccount381 cdtrAcct = new CashAccount381();
		AccountIdentification4Choice1 id4 = new AccountIdentification4Choice1();
		GenericAccountIdentification11 gen4 = new GenericAccountIdentification11();
		gen4.setId(mcCreditTransferRequest.getToAccount().getAcctNumber());
		id4.setOthr(gen4);
		cdtrAcct.setId(id4);
		creditTransferTransaction391.setCdtrAcct(cdtrAcct);
		
		////Remitter Information
		RemittanceInformation161 rmtInf=new RemittanceInformation161();
		
		if(String.valueOf(mcCreditTransferRequest.getTrRmks()).equals("null")&&
				String.valueOf(mcCreditTransferRequest.getTrRmks()).equals("")) {
			rmtInf.setUstrd(Arrays.asList("Credit Transfer"));
		}else {
			rmtInf.setUstrd(Arrays.asList(mcCreditTransferRequest.getTrRmks()));
		}
		creditTransferTransaction391.setRmtInf(rmtInf);
		
		cdtTrfTxInf.add(creditTransferTransaction391);
		
		

///Financial Customer Credit Transfer		
		FIToFICustomerCreditTransferV08 fiToFICstmrCdtTrf = new FIToFICustomerCreditTransferV08();
		fiToFICstmrCdtTrf.setGrpHdr(grpHdr);
		fiToFICstmrCdtTrf.setCdtTrfTxInf(cdtTrfTxInf);

///Document
		Document document = new Document();
		document.setFIToFICstmrCdtTrf(fiToFICstmrCdtTrf);

///Convert Document XMl element to String
		JAXBContext jaxbContext;
		Marshaller jaxbMarshaller;
		StringWriter sw = null;
		try {
			jaxbContext = JAXBContext.newInstance(Document.class);
			jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			jaxbMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
			jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			JaxbCharacterEscapeHandler jaxbCharHandler = new JaxbCharacterEscapeHandler();
			jaxbMarshaller.setProperty("com.sun.xml.bind.characterEscapeHandler", jaxbCharHandler);

			com.bornfire.jaxb.pacs_008_001_08.ObjectFactory obj = new com.bornfire.jaxb.pacs_008_001_08.ObjectFactory();
			JAXBElement<Document> jaxbElement = obj.createDocument(document);
			sw = new StringWriter();
			jaxbMarshaller.marshal(jaxbElement, sw);
		} catch (Exception e) {
			e.printStackTrace();
		}

///return document 
		return sw.toString();

	}

	/**** Create Document of Pacs.008.001.08 ****/
	public String getMerchantPacs_008_001_01Doc(String msgId, CIMMerchantDirectFndRequest mcCreditTransferRequest,
			BankAgentTable othBankAgent,String msgSeq,String endToEndID,String lclInstr,String ctgyPurpData,String chrBearer,String rmtInfo,String tot_tran_amount) {

///Group Header
		GroupHeader931 grpHdr = new GroupHeader931();
		String msg_seq_id = msgId;
		/// Message Identification ID
		grpHdr.setMsgId(msg_seq_id);
		/// Credit Date Time
		grpHdr.setCreDtTm(listener.getxmlGregorianCalender("0"));
		/// Number Of Transaction
		grpHdr.setNbOfTxs("1");
		/// Total Inter Bank Settlement Amount
		ActiveCurrencyAndAmount activeCurrencyAndAmount = new ActiveCurrencyAndAmount();
		activeCurrencyAndAmount.setCcy(mcCreditTransferRequest.getMerchantAccount().getCurrency());/// Currency
		activeCurrencyAndAmount.setValue(new BigDecimal(tot_tran_amount));/// Amount
		grpHdr.setTtlIntrBkSttlmAmt(activeCurrencyAndAmount);
		/// Inter Bank settlement Date
		grpHdr.setIntrBkSttlmDt(listener.getxmlGregorianCalender("1"));
		/// Settlement Instruction
		SettlementInstruction71 sttlmInf = new SettlementInstruction71();
		sttlmInf.setSttlmMtd(SettlementMethod1Code1.CLRG);/// Settlement method (CLRG)
		grpHdr.setSttlmInf(sttlmInf);

///// creditTransaction Information
		List<CreditTransferTransaction391> cdtTrfTxInf = new ArrayList<CreditTransferTransaction391>();
		CreditTransferTransaction391 creditTransferTransaction391 = new CreditTransferTransaction391();
		/// Payment Identification
		PaymentIdentification71 pmtId = new PaymentIdentification71();
		pmtId.setInstrId(msg_seq_id);/// Instruction ID
		
		//String EntToEndID=env.getProperty("ipsx.bicfi")+new SimpleDateFormat("YYYYMMdd").format(new Date())+msgSeq;
		pmtId.setEndToEndId(endToEndID);/// End to End ID
		pmtId.setTxId(msg_seq_id);/// Transaction ID
		creditTransferTransaction391.setPmtId(pmtId);
		/// Payment Type Information
		PaymentTypeInformation281 pmtTpInf = new PaymentTypeInformation281();
		pmtTpInf.setClrChanl(ClearingChannel2Code1.RTGS);/// Clearing Channel like(RTGS,RTNS,MPNS)
		CategoryPurpose1Choice1 ctgyPurp = new CategoryPurpose1Choice1();
		ctgyPurp.setPrtry(ctgyPurpData);
		pmtTpInf.setCtgyPurp(ctgyPurp);/// Category Purpose
		///CSDC-Customer direct Credit Payment
		///BPDC-Bill Payments(Direct Credit)
		LocalInstrument2Choice1 lclInstrm = new LocalInstrument2Choice1();
		lclInstrm.setPrtry(lclInstr);
		pmtTpInf.setLclInstrm(lclInstrm);/// Local Instrument
		List<ServiceLevel8Choice1> svcLvl = new ArrayList<ServiceLevel8Choice1>();
		ServiceLevel8Choice1 serlc = new ServiceLevel8Choice1();
		serlc.setPrtry("0100");
		svcLvl.add(serlc);
		pmtTpInf.setSvcLvl(svcLvl);/// Service Level
		creditTransferTransaction391.setPmtTpInf(pmtTpInf);
		/// Inter Bank Settlement Currency And Amount
		ActiveCurrencyAndAmount intrBkSttlmAmt = new ActiveCurrencyAndAmount();
		intrBkSttlmAmt.setCcy(mcCreditTransferRequest.getMerchantAccount().getCurrency());/// Currency
		intrBkSttlmAmt.setValue(new BigDecimal(tot_tran_amount));/// Amount
		creditTransferTransaction391.setIntrBkSttlmAmt(intrBkSttlmAmt);
		/// Charge Bearer
		if(chrBearer.equals(ChargeBearerType1Code.SLEV.value())) {
			creditTransferTransaction391.setChrgBr(ChargeBearerType1Code.SLEV);
		}else if(chrBearer.equals(ChargeBearerType1Code.SHAR.value())) {
			creditTransferTransaction391.setChrgBr(ChargeBearerType1Code.SHAR);
		}else if(chrBearer.equals(ChargeBearerType1Code.CRED.value())) {
			creditTransferTransaction391.setChrgBr(ChargeBearerType1Code.CRED);
		}else if(chrBearer.equals(ChargeBearerType1Code.DEBT.value())) {
			creditTransferTransaction391.setChrgBr(ChargeBearerType1Code.DEBT);
		}
		/// Financial Institution Identification(Instructing Agent)
		FinancialInstitutionIdentification181 fin = new FinancialInstitutionIdentification181();
		fin.setBICFI(env.getProperty("ipsx.bicfi"));
		BranchAndFinancialInstitutionIdentification61 instgAgt = new BranchAndFinancialInstitutionIdentification61();
		instgAgt.setFinInstnId(fin);
		creditTransferTransaction391.setInstgAgt(instgAgt);
		/// Financial Institution Identification(Instructed Agent)
		FinancialInstitutionIdentification181 fin1 = new FinancialInstitutionIdentification181();
		// fin1.setBICFI("TSTAMUMU");
		fin1.setBICFI(othBankAgent.getBank_agent());
		BranchAndFinancialInstitutionIdentification61 instdAgt = new BranchAndFinancialInstitutionIdentification61();
		instdAgt.setFinInstnId(fin1);
		creditTransferTransaction391.setInstdAgt(instdAgt);
		/// Debtor name
		PartyIdentification1351 dbtr = new PartyIdentification1351();
		dbtr.setNm(mcCreditTransferRequest.getRemitterAccount().getAcctName());
		creditTransferTransaction391.setDbtr(dbtr);
		
	     CashAccount381 dbtrAcct = new CashAccount381();
		 AccountIdentification4Choice1 acc1 = new AccountIdentification4Choice1();
		 GenericAccountIdentification11 id = new GenericAccountIdentification11();
		 id.setId((mcCreditTransferRequest.getRemitterAccount().getAcctNumber()));
		/* id.setSchmeNm("OBAN");*/
		 
		/* FinancialIdentificationSchemeName1Choice finId=new FinancialIdentificationSchemeName1Choice();
		 finId.setCd("OAB");
		 finId.setPrtry("Office");
		 id.setSchmeNm(finId);*/
		 
		 acc1.setOthr(id); 
		 dbtrAcct.setId(acc1);
		 creditTransferTransaction391.setDbtrAcct(dbtrAcct);
		 
		/// Debtor Agent
		BranchAndFinancialInstitutionIdentification61 dbtrAgt = new BranchAndFinancialInstitutionIdentification61();
		FinancialInstitutionIdentification181 fin2 = new FinancialInstitutionIdentification181();
		fin2.setBICFI(env.getProperty("ipsx.dbtragt"));
		dbtrAgt.setFinInstnId(fin2);
		creditTransferTransaction391.setDbtrAgt(dbtrAgt);
		/// Debtor Agent Account
		CashAccount381 dbtrAgtAcct = new CashAccount381();
		AccountIdentification4Choice1 id2 = new AccountIdentification4Choice1();
		GenericAccountIdentification11 gen1 = new GenericAccountIdentification11();
		gen1.setId(env.getProperty("ipsx.dbtragtacct"));
		id2.setOthr(gen1);
		dbtrAgtAcct.setId(id2);
		creditTransferTransaction391.setDbtrAgtAcct(dbtrAgtAcct);
		/// Creditor Agent
		BranchAndFinancialInstitutionIdentification61 cdtrAgt = new BranchAndFinancialInstitutionIdentification61();
		FinancialInstitutionIdentification181 fin3 = new FinancialInstitutionIdentification181();
		// fin3.setBICFI("TSTAMUMU");
		fin3.setBICFI(othBankAgent.getBank_agent());
		cdtrAgt.setFinInstnId(fin3);
		creditTransferTransaction391.setCdtrAgt(cdtrAgt);
		/// Creditor Agent Account
		CashAccount381 cdtrAgtAcct = new CashAccount381();
		AccountIdentification4Choice1 acc3 = new AccountIdentification4Choice1();
		GenericAccountIdentification11 gen3 = new GenericAccountIdentification11();
		// gen3.setId("TSTBNRT");
		gen3.setId(othBankAgent.getBank_agent_account());
		acc3.setOthr(gen3);
		cdtrAgtAcct.setId(acc3);
		creditTransferTransaction391.setCdtrAgtAcct(cdtrAgtAcct);
		/// Creditor Name
		PartyIdentification1351 cdtr = new PartyIdentification1351();
		cdtr.setNm(mcCreditTransferRequest.getMerchantAccount().getMerchantName());
		
		///Merchant ID and MCC
		com.bornfire.jaxb.pacs_008_001_08.Party38Choice1 idMer=new com.bornfire.jaxb.pacs_008_001_08.Party38Choice1();
		com.bornfire.jaxb.pacs_008_001_08.OrganisationIdentification291 orgIdMerID=new com.bornfire.jaxb.pacs_008_001_08.OrganisationIdentification291();
		List<com.bornfire.jaxb.pacs_008_001_08.GenericOrganisationIdentification11> listOthrMerID=new ArrayList<>();
		com.bornfire.jaxb.pacs_008_001_08.GenericOrganisationIdentification11 othrMerID=new com.bornfire.jaxb.pacs_008_001_08.GenericOrganisationIdentification11();
		othrMerID.setId(mcCreditTransferRequest.getMerchantAccount().getMerchantID());
		com.bornfire.jaxb.pacs_008_001_08.OrganisationIdentificationSchemeName1Choice schmeNmMerID=new com.bornfire.jaxb.pacs_008_001_08.OrganisationIdentificationSchemeName1Choice();
		schmeNmMerID.setPrtry("MerchantID");
		othrMerID.setSchmeNm(schmeNmMerID);
		listOthrMerID.add(othrMerID);
		
		///MCC
		com.bornfire.jaxb.pacs_008_001_08.GenericOrganisationIdentification11 othrMCCID=new com.bornfire.jaxb.pacs_008_001_08.GenericOrganisationIdentification11();
		othrMCCID.setId(mcCreditTransferRequest.getMerchantAccount().getMCC());
		com.bornfire.jaxb.pacs_008_001_08.OrganisationIdentificationSchemeName1Choice schmeNmMCCID=new com.bornfire.jaxb.pacs_008_001_08.OrganisationIdentificationSchemeName1Choice();
		schmeNmMCCID.setPrtry("MCC");
		othrMCCID.setSchmeNm(schmeNmMCCID);
		listOthrMerID.add(othrMCCID);
		
		orgIdMerID.setOthr(listOthrMerID);
		idMer.setOrgId(orgIdMerID);
		cdtr.setId(idMer);
		
		///Postal Address
		//Country
		PostalAddress241 pstlAdrMer=new PostalAddress241();
		pstlAdrMer.setCtry(mcCreditTransferRequest.getMerchantAccount().getCountryCode());
		pstlAdrMer.setTwnNm(mcCreditTransferRequest.getMerchantAccount().getCity());
		if(!String.valueOf(mcCreditTransferRequest.getMerchantAccount().getPostalCode()).equals("null")) {
			if(!String.valueOf(mcCreditTransferRequest.getMerchantAccount().getPostalCode()).equals("")) {
				pstlAdrMer.setPstCd(mcCreditTransferRequest.getMerchantAccount().getPostalCode());
			}
		}
		cdtr.setPstlAdr(pstlAdrMer);
		
		creditTransferTransaction391.setCdtr(cdtr);
		/// Creditor Account Number
		CashAccount381 cdtrAcct = new CashAccount381();
		AccountIdentification4Choice1 id4 = new AccountIdentification4Choice1();
		GenericAccountIdentification11 gen4 = new GenericAccountIdentification11();
		gen4.setId(mcCreditTransferRequest.getMerchantAccount().getMerchantAcctNumber());
		id4.setOthr(gen4);
		cdtrAcct.setId(id4);
		creditTransferTransaction391.setCdtrAcct(cdtrAcct);
		
		////Remitter Information
		RemittanceInformation161 rmtInf=new RemittanceInformation161();
		rmtInf.setUstrd(Arrays.asList(rmtInfo));
		creditTransferTransaction391.setRmtInf(rmtInf);
		
		cdtTrfTxInf.add(creditTransferTransaction391);
		
		

///Financial Customer Credit Transfer		
		FIToFICustomerCreditTransferV08 fiToFICstmrCdtTrf = new FIToFICustomerCreditTransferV08();
		fiToFICstmrCdtTrf.setGrpHdr(grpHdr);
		fiToFICstmrCdtTrf.setCdtTrfTxInf(cdtTrfTxInf);

///Document
		Document document = new Document();
		document.setFIToFICstmrCdtTrf(fiToFICstmrCdtTrf);

///Convert Document XMl element to String
		JAXBContext jaxbContext;
		Marshaller jaxbMarshaller;
		StringWriter sw = null;
		try {
			jaxbContext = JAXBContext.newInstance(Document.class);
			jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			jaxbMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
			jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			JaxbCharacterEscapeHandler jaxbCharHandler = new JaxbCharacterEscapeHandler();
			jaxbMarshaller.setProperty("com.sun.xml.bind.characterEscapeHandler", jaxbCharHandler);

			com.bornfire.jaxb.pacs_008_001_08.ObjectFactory obj = new com.bornfire.jaxb.pacs_008_001_08.ObjectFactory();
			JAXBElement<Document> jaxbElement = obj.createDocument(document);
			sw = new StringWriter();
			jaxbMarshaller.marshal(jaxbElement, sw);
		} catch (Exception e) {
			e.printStackTrace();
		}

///return document 
		return sw.toString();

	}

	/**** Create Document of Pacs.002.001.10 ****/
	public String getPacs_002_001_10Doc(SendT request, String msgId, String creditStatusType, String creditStatusCode,
			String creditStatusDesc) {

		com.bornfire.jaxb.pacs_002_001_010.Document docPacs002_001_10 = new com.bornfire.jaxb.pacs_002_001_010.Document();
		FIToFIPaymentStatusReportV10 fiToFIPmtStsRpt = new FIToFIPaymentStatusReportV10();
		Document docPacs008_001_01 = getPacs_008_001_01UnMarshalDoc(request);

////Group Header
		GroupHeader911 grpHdr = new GroupHeader911();
		// String msg_seq_id=sequence.generateSeqMsgId();
		grpHdr.setMsgId(msgId);
		grpHdr.setCreDtTm(listener.getxmlGregorianCalender("0"));

		/*
		 * com.bornfire.jaxb.pacs_002_001_010.
		 * BranchAndFinancialInstitutionIdentification61 instgAgt1=new
		 * com.bornfire.jaxb.pacs_002_001_010.
		 * BranchAndFinancialInstitutionIdentification61();
		 * com.bornfire.jaxb.pacs_002_001_010.FinancialInstitutionIdentification181
		 * finInstnId1=new
		 * com.bornfire.jaxb.pacs_002_001_010.FinancialInstitutionIdentification181();
		 * com.bornfire.jaxb.pacs_002_001_010.ClearingSystemMemberIdentification21
		 * clrSysMmbId=new
		 * com.bornfire.jaxb.pacs_002_001_010.ClearingSystemMemberIdentification21();
		 * clrSysMmbId.setMmbId(env.getProperty("ipsx.bicfi"));
		 * finInstnId1.setClrSysMmbId(clrSysMmbId);
		 * instgAgt1.setFinInstnId(finInstnId1); grpHdr.setInstgAgt(instgAgt1);
		 * 
		 * com.bornfire.jaxb.pacs_002_001_010.
		 * BranchAndFinancialInstitutionIdentification61 instdAgt1=new
		 * com.bornfire.jaxb.pacs_002_001_010.
		 * BranchAndFinancialInstitutionIdentification61();
		 * com.bornfire.jaxb.pacs_002_001_010.FinancialInstitutionIdentification181
		 * finInstgId1=new
		 * com.bornfire.jaxb.pacs_002_001_010.FinancialInstitutionIdentification181();
		 * com.bornfire.jaxb.pacs_002_001_010.ClearingSystemMemberIdentification21
		 * clrSysMmbId1=new
		 * com.bornfire.jaxb.pacs_002_001_010.ClearingSystemMemberIdentification21();
		 * clrSysMmbId1.setMmbId("BOMMMUPLIPS");
		 * finInstgId1.setClrSysMmbId(clrSysMmbId1);
		 * instdAgt1.setFinInstnId(finInstgId1); grpHdr.setInstdAgt(instdAgt1);
		 */
///Original Group Header
		OriginalGroupHeader171 orgnlGrpInfAndSts = new OriginalGroupHeader171();
		orgnlGrpInfAndSts.setOrgnlMsgId(docPacs008_001_01.getFIToFICstmrCdtTrf().getGrpHdr().getMsgId());
		orgnlGrpInfAndSts.setOrgnlMsgNmId(request.getMessage().getMsgType());
		orgnlGrpInfAndSts.setOrgnlCreDtTm(docPacs008_001_01.getFIToFICstmrCdtTrf().getGrpHdr().getCreDtTm());
		if (creditStatusType.equals(TranMonitorStatus.ACSP.toString())) {
			orgnlGrpInfAndSts.setGrpSts(TranMonitorStatus.ACSP.toString());
			List<StatusReasonInformation121> stsRsnInf = new ArrayList<StatusReasonInformation121>();
			StatusReasonInformation121 statusRsn = new StatusReasonInformation121();
			StatusReason6Choice1 statusReason6Choice1 = new StatusReason6Choice1();
			statusReason6Choice1.setPrtry("AUTH");
			statusRsn.setRsn(statusReason6Choice1);
			// statusRsn.setAddtlInf(Collections.singletonList());
			stsRsnInf.add(statusRsn);
			orgnlGrpInfAndSts.setStsRsnInf(stsRsnInf);

		} else {
			orgnlGrpInfAndSts.setGrpSts(TranMonitorStatus.RJCT.toString());
			List<StatusReasonInformation121> stsRsnInf = new ArrayList<StatusReasonInformation121>();
			StatusReasonInformation121 statusRsn = new StatusReasonInformation121();
			StatusReason6Choice1 statusReason6Choice1 = new StatusReason6Choice1();
			statusReason6Choice1.setPrtry("NAUT");
			statusRsn.setRsn(statusReason6Choice1);
			// statusRsn.setAddtlInf(Collections.singletonList("Transaction Not
			// Supported"));
			stsRsnInf.add(statusRsn);
			orgnlGrpInfAndSts.setStsRsnInf(stsRsnInf);
		}

///Transation Info and Status
		List<PaymentTransaction1101> txInfAndSts = new ArrayList<PaymentTransaction1101>();
		PaymentTransaction1101 paymentTransaction1101 = new PaymentTransaction1101();
		if (creditStatusType.equals(TranMonitorStatus.ACSP.toString())) {
			// paymentTransaction1101.setStsId("AUTH");///Status ID
		} else {
			// paymentTransaction1101.setStsId("NOAN");///Status ID
		}
		// paymentTransaction1101.setOrgnlInstrId(docPacs008_001_01.getFIToFICstmrCdtTrf().getCdtTrfTxInf().get(0).getPmtId().getTxId());

		paymentTransaction1101.setOrgnlInstrId(
				docPacs008_001_01.getFIToFICstmrCdtTrf().getCdtTrfTxInf().get(0).getPmtId().getInstrId());
		paymentTransaction1101.setOrgnlEndToEndId(
				docPacs008_001_01.getFIToFICstmrCdtTrf().getCdtTrfTxInf().get(0).getPmtId().getEndToEndId());
		paymentTransaction1101
				.setOrgnlTxId(docPacs008_001_01.getFIToFICstmrCdtTrf().getCdtTrfTxInf().get(0).getPmtId().getTxId());
		if (creditStatusType.equals(TranMonitorStatus.ACSP.toString())) {
			paymentTransaction1101.setTxSts(TranMonitorStatus.ACSP.toString());
		} else {
			paymentTransaction1101.setTxSts(TranMonitorStatus.RJCT.toString());
		}

		if (creditStatusType.equals(TranMonitorStatus.RJCT.toString())) {
			List<StatusReasonInformation121> stsRsnIn = new ArrayList<StatusReasonInformation121>();
			StatusReasonInformation121 statusReasonInfo = new StatusReasonInformation121();
			StatusReason6Choice1 statusReason6Choice1 = new StatusReason6Choice1();
			statusReason6Choice1.setPrtry(creditStatusCode);
			statusReasonInfo.setRsn(statusReason6Choice1);
			List<String> addtlInf = new ArrayList<String>();
			addtlInf.add(creditStatusDesc);
			statusReasonInfo.setAddtlInf(addtlInf);
			stsRsnIn.add(statusReasonInfo);
			paymentTransaction1101.setStsRsnInf(stsRsnIn);
		} else {
			List<StatusReasonInformation121> stsRsnIn = new ArrayList<StatusReasonInformation121>();
			StatusReasonInformation121 statusReasonInfo = new StatusReasonInformation121();
			StatusReason6Choice1 statusReason6Choice2 = new StatusReason6Choice1();
			statusReason6Choice2.setPrtry("AUTH");
			statusReasonInfo.setRsn(statusReason6Choice2);
			// List<String> addtlInf=new ArrayList<String>();
			// addtlInf.add(creditStatusDesc);
			// statusReasonInfo.setAddtlInf(addtlInf);
			stsRsnIn.add(statusReasonInfo);
			paymentTransaction1101.setStsRsnInf(stsRsnIn);
		}

		// paymentTransaction1101.setAcctSvcrRef("2385");

		com.bornfire.jaxb.pacs_002_001_010.BranchAndFinancialInstitutionIdentification61 instgAgt = new com.bornfire.jaxb.pacs_002_001_010.BranchAndFinancialInstitutionIdentification61();
		com.bornfire.jaxb.pacs_002_001_010.FinancialInstitutionIdentification181 finInstnId = new com.bornfire.jaxb.pacs_002_001_010.FinancialInstitutionIdentification181();
		// finInstnId.setBICFI(docPacs008_001_01.getFIToFICstmrCdtTrf().getCdtTrfTxInf().get(0).getInstgAgt().getFinInstnId().getBICFI());
		com.bornfire.jaxb.pacs_002_001_010.ClearingSystemMemberIdentification21 clrSysMmbId2 = new com.bornfire.jaxb.pacs_002_001_010.ClearingSystemMemberIdentification21();
		clrSysMmbId2.setMmbId(docPacs008_001_01.getFIToFICstmrCdtTrf().getCdtTrfTxInf().get(0).getInstgAgt()
				.getFinInstnId().getBICFI());
		finInstnId.setClrSysMmbId(clrSysMmbId2);
		instgAgt.setFinInstnId(finInstnId);
		paymentTransaction1101.setInstgAgt(instgAgt);

		/*
		 * com.bornfire.jaxb.pacs_002_001_010.
		 * BranchAndFinancialInstitutionIdentification61 instdAgt=new
		 * com.bornfire.jaxb.pacs_002_001_010.
		 * BranchAndFinancialInstitutionIdentification61();
		 * com.bornfire.jaxb.pacs_002_001_010.FinancialInstitutionIdentification181
		 * finInstdId1=new
		 * com.bornfire.jaxb.pacs_002_001_010.FinancialInstitutionIdentification181();
		 * GenericFinancialIdentification11 genericFinancialIdentification11=new
		 * GenericFinancialIdentification11();
		 * genericFinancialIdentification11.setId("ONLINE");
		 * finInstdId1.setBICFI(env.);
		 * finInstdId1.setOthr(genericFinancialIdentification11);
		 * instdAgt.setFinInstnId(finInstdId1);
		 * paymentTransaction1101.setInstdAgt(instdAgt);
		 */

		OriginalTransactionReference281 orgnlTxRef = new OriginalTransactionReference281();
		ActiveOrHistoricCurrencyAndAmount activeOrHistoricCurrencyAndAmount = new ActiveOrHistoricCurrencyAndAmount();
		activeOrHistoricCurrencyAndAmount
				.setCcy(docPacs008_001_01.getFIToFICstmrCdtTrf().getCdtTrfTxInf().get(0).getIntrBkSttlmAmt().getCcy());
		activeOrHistoricCurrencyAndAmount.setValue(
				docPacs008_001_01.getFIToFICstmrCdtTrf().getCdtTrfTxInf().get(0).getIntrBkSttlmAmt().getValue());
		// orgnlTxRef.setIntrBkSttlmAmt(activeOrHistoricCurrencyAndAmount);
		orgnlTxRef.setIntrBkSttlmDt(docPacs008_001_01.getFIToFICstmrCdtTrf().getGrpHdr().getIntrBkSttlmDt());
		PaymentTypeInformation271 pmtTpInf = new PaymentTypeInformation271();
		pmtTpInf.setClrChanl(com.bornfire.jaxb.pacs_002_001_010.ClearingChannel2Code1.RTGS);
		List<com.bornfire.jaxb.pacs_002_001_010.ServiceLevel8Choice1> svcLvl = new ArrayList<com.bornfire.jaxb.pacs_002_001_010.ServiceLevel8Choice1>();
		com.bornfire.jaxb.pacs_002_001_010.ServiceLevel8Choice1 serviceLevel8Choice1 = new com.bornfire.jaxb.pacs_002_001_010.ServiceLevel8Choice1();
		serviceLevel8Choice1.setPrtry(docPacs008_001_01.getFIToFICstmrCdtTrf().getCdtTrfTxInf().get(0).getPmtTpInf()
				.getSvcLvl().get(0).getPrtry());
		svcLvl.add(serviceLevel8Choice1);
		pmtTpInf.setSvcLvl(svcLvl);
		com.bornfire.jaxb.pacs_002_001_010.LocalInstrument2Choice1 lclInstrm = new com.bornfire.jaxb.pacs_002_001_010.LocalInstrument2Choice1();
		lclInstrm.setPrtry(docPacs008_001_01.getFIToFICstmrCdtTrf().getCdtTrfTxInf().get(0).getPmtTpInf().getLclInstrm()
				.getPrtry());
		pmtTpInf.setLclInstrm(lclInstrm);
		com.bornfire.jaxb.pacs_002_001_010.CategoryPurpose1Choice1 ctgyPurp = new com.bornfire.jaxb.pacs_002_001_010.CategoryPurpose1Choice1();
		ctgyPurp.setPrtry(docPacs008_001_01.getFIToFICstmrCdtTrf().getCdtTrfTxInf().get(0).getPmtTpInf().getCtgyPurp()
				.getPrtry());
		pmtTpInf.setCtgyPurp(ctgyPurp);
		// orgnlTxRef.setPmtTpInf(pmtTpInf);
		paymentTransaction1101.setOrgnlTxRef(orgnlTxRef);

		txInfAndSts.add(paymentTransaction1101);

		fiToFIPmtStsRpt.setGrpHdr(grpHdr);
		fiToFIPmtStsRpt.setOrgnlGrpInfAndSts(orgnlGrpInfAndSts);
		fiToFIPmtStsRpt.setTxInfAndSts(txInfAndSts);

		docPacs002_001_10.setFIToFIPmtStsRpt(fiToFIPmtStsRpt);

		JAXBContext jaxbContext;
		Marshaller jaxbMarshaller;
		StringWriter sw = null;
		try {
			jaxbContext = JAXBContext.newInstance(com.bornfire.jaxb.pacs_002_001_010.Document.class);
			jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			jaxbMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
			jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			JaxbCharacterEscapeHandler jaxbCharHandler = new JaxbCharacterEscapeHandler();
			jaxbMarshaller.setProperty("com.sun.xml.bind.characterEscapeHandler", jaxbCharHandler);

			ObjectFactory obj = new ObjectFactory();
			JAXBElement<com.bornfire.jaxb.pacs_002_001_010.Document> jaxbElement = obj
					.createDocument(docPacs002_001_10);
			sw = new StringWriter();
			jaxbMarshaller.marshal(jaxbElement, sw);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sw.toString();
	}

	public Document getPacs_008_001_01UnMarshalDoc(SendT request) {
		String block4 = request.getMessage().getBlock4();
		final int start = block4.indexOf("<Document");
		final int end = block4.indexOf("</Document>");

		InputStream stream = null;
		JAXBContext jaxBContext;
		JAXBElement<Document> jaxbElement = null;
		try {
			stream = new ByteArrayInputStream(block4.substring(start, end + 11).getBytes("UTF-8"));
			jaxBContext = JAXBContext.newInstance(Document.class);
			Unmarshaller unMarshaller = jaxBContext.createUnmarshaller();
			XMLInputFactory factory = XMLInputFactory.newInstance();
			XMLEventReader xmlEventReader = factory.createXMLEventReader(stream);
			jaxbElement = unMarshaller.unmarshal(xmlEventReader, Document.class);
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		Document document = jaxbElement.getValue();

		return document;
	}

	public com.bornfire.jaxb.pacs_002_001_010.Document getPacs_002_001_10UnMarshalDoc(SendT request){
		String block4 = request.getMessage().getBlock4();
		final int start = block4.indexOf("<Document");
		final int end = block4.indexOf("</Document>");

		InputStream stream = null;
		JAXBContext jaxBContext;
		JAXBElement<com.bornfire.jaxb.pacs_002_001_010.Document> jaxbElement = null;

		try {
			stream = new ByteArrayInputStream(block4.substring(start, end + 11).getBytes("UTF-8"));
			jaxBContext = JAXBContext.newInstance(com.bornfire.jaxb.pacs_002_001_010.Document.class);
			Unmarshaller unMarshaller = jaxBContext.createUnmarshaller();
			XMLInputFactory factory = XMLInputFactory.newInstance();
			XMLEventReader xmlEventReader = factory.createXMLEventReader(stream);
			jaxbElement = unMarshaller.unmarshal(xmlEventReader, com.bornfire.jaxb.pacs_002_001_010.Document.class);

		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		com.bornfire.jaxb.pacs_002_001_010.Document document = jaxbElement.getValue();

		return document;
	}

	public com.bornfire.jaxb.admi_002_001_01.Document getAdmi_002_001_01UnMarshalDoc(SendT request) {
		String block4 = request.getMessage().getBlock4();
		final int start = block4.indexOf("<Document");
		final int end = block4.indexOf("</Document>");

		InputStream stream = null;
		JAXBContext jaxBContext;
		JAXBElement<com.bornfire.jaxb.admi_002_001_01.Document> jaxbElement = null;
		try {
			stream = new ByteArrayInputStream(block4.substring(start, end + 11).getBytes("UTF-8"));
			jaxBContext = JAXBContext.newInstance(com.bornfire.jaxb.admi_002_001_01.Document.class);
			Unmarshaller unMarshaller = jaxBContext.createUnmarshaller();
			XMLInputFactory factory = XMLInputFactory.newInstance();
			XMLEventReader xmlEventReader = factory.createXMLEventReader(stream);
			jaxbElement = unMarshaller.unmarshal(xmlEventReader, com.bornfire.jaxb.admi_002_001_01.Document.class);

		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		com.bornfire.jaxb.admi_002_001_01.Document document = jaxbElement.getValue();

		return document;

	}

	public com.bornfire.jaxb.camt_052_001_08.Document getCamt052_001_08UnMarshalDoc(SendT request) {
		String block4 = request.getMessage().getBlock4();
		final int start = block4.indexOf("<Document");
		final int end = block4.indexOf("</Document>");

		InputStream stream = null;
		JAXBContext jaxBContext;
		JAXBElement<com.bornfire.jaxb.camt_052_001_08.Document> jaxbElement = null;

		try {
			stream = new ByteArrayInputStream(block4.substring(start, end + 11).getBytes("UTF-8"));
			jaxBContext = JAXBContext.newInstance(com.bornfire.jaxb.camt_052_001_08.Document.class);
			Unmarshaller unMarshaller = jaxBContext.createUnmarshaller();
			XMLInputFactory factory = XMLInputFactory.newInstance();
			XMLEventReader xmlEventReader = factory.createXMLEventReader(stream);
			jaxbElement = unMarshaller.unmarshal(xmlEventReader, com.bornfire.jaxb.camt_052_001_08.Document.class);

		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		com.bornfire.jaxb.camt_052_001_08.Document document = jaxbElement.getValue();

		return document;
	}

	public com.bornfire.jaxb.pain_001_001_09.Document getPain001_001_09UnMarshalDoc(SendT request) {
		String block4 = request.getMessage().getBlock4();
		final int start = block4.indexOf("<Document");
		final int end = block4.indexOf("</Document>");

		InputStream stream = null;
		JAXBContext jaxBContext;
		JAXBElement<com.bornfire.jaxb.pain_001_001_09.Document> jaxbElement = null;

		try {
			stream = new ByteArrayInputStream(block4.substring(start, end + 11).getBytes("UTF-8"));
			jaxBContext = JAXBContext.newInstance(com.bornfire.jaxb.pain_001_001_09.Document.class);
			Unmarshaller unMarshaller = jaxBContext.createUnmarshaller();
			XMLInputFactory factory = XMLInputFactory.newInstance();
			XMLEventReader xmlEventReader = factory.createXMLEventReader(stream);
			jaxbElement = unMarshaller.unmarshal(xmlEventReader, com.bornfire.jaxb.pain_001_001_09.Document.class);

		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		com.bornfire.jaxb.pain_001_001_09.Document document = jaxbElement.getValue();

		return document;
	}

	public String getRTPPacs_008_001_01Doc(String msgType, SendT request, String bobMsgId,String msgSeq) {

		com.bornfire.jaxb.pain_001_001_09.Document docPacsPain001 = getPain001_001_09UnMarshalDoc(request);
		/// Group Header
		GroupHeader931 grpHdr = new GroupHeader931();
		String msg_seq_id = bobMsgId;
		/// Message Identification ID
		grpHdr.setMsgId(msg_seq_id);
		/// Credit Date Time
		grpHdr.setCreDtTm(listener.getxmlGregorianCalender("0"));
		/// Number Of Transaction
		grpHdr.setNbOfTxs("1");
		/// Total Inter Bank Settlement Amount
		ActiveCurrencyAndAmount activeCurrencyAndAmount = new ActiveCurrencyAndAmount();
		activeCurrencyAndAmount.setCcy(docPacsPain001.getCstmrCdtTrfInitn().getPmtInf().getCdtTrfTxInf().get(0).getAmt()
				.getInstdAmt().getCcy());/// Currency
		activeCurrencyAndAmount.setValue(docPacsPain001.getCstmrCdtTrfInitn().getPmtInf().getCdtTrfTxInf().get(0)
				.getAmt().getInstdAmt().getValue());/// Amount
		grpHdr.setTtlIntrBkSttlmAmt(activeCurrencyAndAmount);
		/// Inter Bank settlement Date
		grpHdr.setIntrBkSttlmDt(listener.getxmlGregorianCalender("1"));
		/// Settlement Instruction
		SettlementInstruction71 sttlmInf = new SettlementInstruction71();
		sttlmInf.setSttlmMtd(SettlementMethod1Code1.CLRG);/// Settlement method (CLRG)
		grpHdr.setSttlmInf(sttlmInf);

		///// creditTransaction Information
		List<CreditTransferTransaction391> cdtTrfTxInf = new ArrayList<CreditTransferTransaction391>();
		CreditTransferTransaction391 creditTransferTransaction391 = new CreditTransferTransaction391();
		/// Payment Identification
		PaymentIdentification71 pmtId = new PaymentIdentification71();
		pmtId.setInstrId(
				docPacsPain001.getCstmrCdtTrfInitn().getPmtInf().getCdtTrfTxInf().get(0).getPmtId().getInstrId());/// Instruction
		String EntToEndID=env.getProperty("ipsx.bicfi")+new SimpleDateFormat("yyyyMMdd").format(new Date())+msgSeq;
		//pmtId.setEndToEndId(EntToEndID);																			/// ID
		pmtId.setEndToEndId(
				docPacsPain001.getCstmrCdtTrfInitn().getPmtInf().getCdtTrfTxInf().get(0).getPmtId().getEndToEndId());/// End
																														/// to
																														/// End
																														/// ID
		pmtId.setTxId(msg_seq_id);/// Transaction ID
		creditTransferTransaction391.setPmtId(pmtId);
		/// Payment Type Information
		PaymentTypeInformation281 pmtTpInf = new PaymentTypeInformation281();
		pmtTpInf.setClrChanl(ClearingChannel2Code1.RTNS);/// Clearing Channel like(RTGS,RTNS,MPNS)
		CategoryPurpose1Choice1 ctgyPurp = new CategoryPurpose1Choice1();
		ctgyPurp.setPrtry(docPacsPain001.getCstmrCdtTrfInitn().getPmtInf().getCdtTrfTxInf().get(0).getPmtTpInf()
				.getCtgyPurp().getPrtry());
		pmtTpInf.setCtgyPurp(ctgyPurp);/// Category Purpose
		LocalInstrument2Choice1 lclInstrm = new LocalInstrument2Choice1();
		lclInstrm.setPrtry(docPacsPain001.getCstmrCdtTrfInitn().getPmtInf().getCdtTrfTxInf().get(0).getPmtTpInf()
				.getLclInstrm().getPrtry());
		pmtTpInf.setLclInstrm(lclInstrm);/// Local Instrument
		List<ServiceLevel8Choice1> svcLvl = new ArrayList<ServiceLevel8Choice1>();
		ServiceLevel8Choice1 serlc = new ServiceLevel8Choice1();
		serlc.setPrtry(docPacsPain001.getCstmrCdtTrfInitn().getPmtInf().getCdtTrfTxInf().get(0).getPmtTpInf()
				.getSvcLvl().get(0).getPrtry());
		svcLvl.add(serlc);
		pmtTpInf.setSvcLvl(svcLvl);/// Service Level
		creditTransferTransaction391.setPmtTpInf(pmtTpInf);
		/// Inter Bank Settlement Currency And Amount
		ActiveCurrencyAndAmount intrBkSttlmAmt = new ActiveCurrencyAndAmount();
		intrBkSttlmAmt.setCcy(docPacsPain001.getCstmrCdtTrfInitn().getPmtInf().getCdtTrfTxInf().get(0).getAmt()
				.getInstdAmt().getCcy());/// Currency
		intrBkSttlmAmt.setValue(docPacsPain001.getCstmrCdtTrfInitn().getPmtInf().getCdtTrfTxInf().get(0).getAmt()
				.getInstdAmt().getValue());/// Amount
		creditTransferTransaction391.setIntrBkSttlmAmt(intrBkSttlmAmt);
		/// Charge Bearer
		creditTransferTransaction391.setChrgBr(ChargeBearerType1Code.SLEV);
		/// Financial Institution Identification(Instructing Agent)
		FinancialInstitutionIdentification181 fin = new FinancialInstitutionIdentification181();
		fin.setBICFI(env.getProperty("ipsx.bicfi"));
		BranchAndFinancialInstitutionIdentification61 instgAgt = new BranchAndFinancialInstitutionIdentification61();
		instgAgt.setFinInstnId(fin);
		creditTransferTransaction391.setInstgAgt(instgAgt);
		/// Financial Institution Identification(Instructed Agent)
		FinancialInstitutionIdentification181 fin1 = new FinancialInstitutionIdentification181();
		fin1.setBICFI(docPacsPain001.getCstmrCdtTrfInitn().getGrpHdr().getInitgPty().getId().getOrgId().getAnyBIC());
		BranchAndFinancialInstitutionIdentification61 instdAgt = new BranchAndFinancialInstitutionIdentification61();
		instdAgt.setFinInstnId(fin1);
		creditTransferTransaction391.setInstdAgt(instdAgt);
		/// Debtor name
		PartyIdentification1351 dbtr = new PartyIdentification1351();
		dbtr.setNm(docPacsPain001.getCstmrCdtTrfInitn().getPmtInf().getDbtr().getNm());
		PostalAddress241 pstlAdr = new PostalAddress241();
		pstlAdr.setAdrLine(docPacsPain001.getCstmrCdtTrfInitn().getPmtInf().getDbtr().getPstlAdr().getAdrLine());
		dbtr.setPstlAdr(pstlAdr);
		creditTransferTransaction391.setDbtr(dbtr);
		/// Debtor Account Number
		CashAccount381 dbtrAcct = new CashAccount381();
		AccountIdentification4Choice1 acc1 = new AccountIdentification4Choice1();
		GenericAccountIdentification11 id = new GenericAccountIdentification11();
		id.setId(docPacsPain001.getCstmrCdtTrfInitn().getPmtInf().getDbtrAcct().getId().getOthr().getId());
		acc1.setOthr(id);
		dbtrAcct.setId(acc1);
		creditTransferTransaction391.setDbtrAcct(dbtrAcct);
		/// Debtor Agent
		BranchAndFinancialInstitutionIdentification61 dbtrAgt = new BranchAndFinancialInstitutionIdentification61();
		FinancialInstitutionIdentification181 fin2 = new FinancialInstitutionIdentification181();
		//fin2.setBICFI(docPacsPain001.getCstmrCdtTrfInitn().getPmtInf().getDbtrAgt().getFinInstnId().getBICFI());
		
		/*if(docPacsPain001.getCstmrCdtTrfInitn().getPmtInf().getDbtrAgt()!=null) {
			if(docPacsPain001.getCstmrCdtTrfInitn().getPmtInf().getDbtrAgt().getFinInstnId()!=null) {
				if(docPacsPain001.getCstmrCdtTrfInitn().getPmtInf().getDbtrAgt().getFinInstnId().getBICFI()!=null) {
					fin2.setBICFI(docPacsPain001.getCstmrCdtTrfInitn().getPmtInf().getDbtrAgt().getFinInstnId().getBICFI());
				}
				
				if(docPacsPain001.getCstmrCdtTrfInitn().getPmtInf().getDbtrAgt().getFinInstnId().getClrSysMmbId()!=null) {
					if(docPacsPain001.getCstmrCdtTrfInitn().getPmtInf().getDbtrAgt().getFinInstnId().getClrSysMmbId().getMmbId()!=null) {
						ClearingSystemMemberIdentification21 cls=new ClearingSystemMemberIdentification21();
						cls.setMmbId(docPacsPain001.getCstmrCdtTrfInitn().getPmtInf().getDbtrAgt().getFinInstnId().getClrSysMmbId().getMmbId());
						fin2.setClrSysMmbId(cls);
					}
					
				}
			}
		}*/
		
		fin2.setBICFI(env.getProperty("ipsx.bicfi"));
		dbtrAgt.setFinInstnId(fin2);
		creditTransferTransaction391.setDbtrAgt(dbtrAgt);
		/// Debtor Agent Account
		/*CashAccount381 dbtrAgtAcct = new CashAccount381();
		AccountIdentification4Choice1 id2 = new AccountIdentification4Choice1();
		GenericAccountIdentification11 gen1 = new GenericAccountIdentification11();
		gen1.setId(docPacsPain001.getCstmrCdtTrfInitn().getPmtInf().getDbtrAgtAcct().getId().getOthr().getId());
		id2.setOthr(gen1);
		dbtrAgtAcct.setId(id2);
		creditTransferTransaction391.setDbtrAgtAcct(dbtrAgtAcct);*/
		/// Creditor Agent
		BranchAndFinancialInstitutionIdentification61 cdtrAgt = new BranchAndFinancialInstitutionIdentification61();
		FinancialInstitutionIdentification181 fin3 = new FinancialInstitutionIdentification181();
		fin3.setBICFI(docPacsPain001.getCstmrCdtTrfInitn().getPmtInf().getCdtTrfTxInf().get(0).getCdtrAgt()
				.getFinInstnId().getBICFI());
		cdtrAgt.setFinInstnId(fin3);
		creditTransferTransaction391.setCdtrAgt(cdtrAgt);
		/// Creditor Agent Account
		/*CashAccount381 cdtrAgtAcct = new CashAccount381();
		AccountIdentification4Choice1 acc3 = new AccountIdentification4Choice1();
		GenericAccountIdentification11 gen3 = new GenericAccountIdentification11();
		gen3.setId(docPacsPain001.getCstmrCdtTrfInitn().getPmtInf().getCdtTrfTxInf().get(0).getCdtrAgtAcct().getId()
				.getOthr().getId());
		acc3.setOthr(gen3);
		cdtrAgtAcct.setId(acc3);
		creditTransferTransaction391.setCdtrAgtAcct(cdtrAgtAcct);*/
		/// Creditor Name
		PartyIdentification1351 cdtr = new PartyIdentification1351();
		cdtr.setNm(docPacsPain001.getCstmrCdtTrfInitn().getPmtInf().getCdtTrfTxInf().get(0).getCdtr().getNm());
		PostalAddress241 pstlAdr1 = new PostalAddress241();
		
		if(docPacsPain001.getCstmrCdtTrfInitn().getPmtInf().getCdtTrfTxInf().get(0).getCdtr()
				.getPstlAdr()!=null) {
			if(docPacsPain001.getCstmrCdtTrfInitn().getPmtInf().getCdtTrfTxInf().get(0).getCdtr()
					.getPstlAdr().getAdrLine()!=null) {
				pstlAdr1.setAdrLine(docPacsPain001.getCstmrCdtTrfInitn().getPmtInf().getCdtTrfTxInf().get(0).getCdtr()
						.getPstlAdr().getAdrLine());
				cdtr.setPstlAdr(pstlAdr1);
			}
			
		}
		
		creditTransferTransaction391.setCdtr(cdtr);
		/// Creditor Account Number
		CashAccount381 cdtrAcct = new CashAccount381();
		AccountIdentification4Choice1 id4 = new AccountIdentification4Choice1();
		GenericAccountIdentification11 gen4 = new GenericAccountIdentification11();
		gen4.setId(docPacsPain001.getCstmrCdtTrfInitn().getPmtInf().getCdtTrfTxInf().get(0).getCdtrAcct().getId()
				.getOthr().getId());
		id4.setOthr(gen4);
		cdtrAcct.setId(id4);
		creditTransferTransaction391.setCdtrAcct(cdtrAcct);

		if(docPacsPain001.getCstmrCdtTrfInitn().getPmtInf().getCdtTrfTxInf().get(0)
				.getRgltryRptg()!=null) {
			
			if(docPacsPain001.getCstmrCdtTrfInitn().getPmtInf().getCdtTrfTxInf().get(0)
				.getRgltryRptg().size()>0) {
				RegulatoryReporting31 rgltryRptg = new RegulatoryReporting31();
				StructuredRegulatoryReporting31 dtls = new StructuredRegulatoryReporting31();
				dtls.setInf(Collections.singletonList(docPacsPain001.getCstmrCdtTrfInitn().getPmtInf().getCdtTrfTxInf().get(0)
						.getRgltryRptg().get(0).getDtls().getInf().get(0)));
				rgltryRptg.setDtls(dtls);
				creditTransferTransaction391.setRgltryRptg(rgltryRptg);
			}
			
		}
		

		RemittanceInformation161 rmtInf = new RemittanceInformation161();
		rmtInf.setUstrd(Collections.singletonList(docPacsPain001.getCstmrCdtTrfInitn().getPmtInf().getCdtTrfTxInf()
				.get(0).getRmtInf().getUstrd().get(0)));

		List<StructuredRemittanceInformation161> strd = new ArrayList<StructuredRemittanceInformation161>();
		StructuredRemittanceInformation161 structuredRemittanceInformation161 = new StructuredRemittanceInformation161();
		List<ReferredDocumentInformation71> rfrdDocInf = new ArrayList<ReferredDocumentInformation71>();
		ReferredDocumentInformation71 referredDocumentInformation71 = new ReferredDocumentInformation71();
		referredDocumentInformation71.setNb(docPacsPain001.getCstmrCdtTrfInitn().getGrpHdr().getMsgId());
		ReferredDocumentType41 tp = new ReferredDocumentType41();
		ReferredDocumentType3Choice1 cdOrPrtry = new ReferredDocumentType3Choice1();
		cdOrPrtry.setPrtry("PAIN");
		tp.setCdOrPrtry(cdOrPrtry);
		tp.setIssr(docPacsPain001.getCstmrCdtTrfInitn().getGrpHdr().getInitgPty().getId().getOrgId().getAnyBIC());
		referredDocumentInformation71.setTp(tp);
		rfrdDocInf.add(referredDocumentInformation71);
		structuredRemittanceInformation161.setRfrdDocInf(rfrdDocInf);
		strd.add(structuredRemittanceInformation161);
		rmtInf.setStrd(strd);

		creditTransferTransaction391.setRmtInf(rmtInf);

		cdtTrfTxInf.add(creditTransferTransaction391);

		/// Financial Customer Credit Transfer
		FIToFICustomerCreditTransferV08 fiToFICstmrCdtTrf = new FIToFICustomerCreditTransferV08();
		fiToFICstmrCdtTrf.setGrpHdr(grpHdr);
		fiToFICstmrCdtTrf.setCdtTrfTxInf(cdtTrfTxInf);

		/// Document
		Document document = new Document();
		document.setFIToFICstmrCdtTrf(fiToFICstmrCdtTrf);

		/// Convert Document XMl element to String
		JAXBContext jaxbContext;
		Marshaller jaxbMarshaller;
		StringWriter sw = null;
		try {
			jaxbContext = JAXBContext.newInstance(Document.class);
			jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			jaxbMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
			jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			JaxbCharacterEscapeHandler jaxbCharHandler = new JaxbCharacterEscapeHandler();
			jaxbMarshaller.setProperty("com.sun.xml.bind.characterEscapeHandler", jaxbCharHandler);

			com.bornfire.jaxb.pacs_008_001_08.ObjectFactory obj = new com.bornfire.jaxb.pacs_008_001_08.ObjectFactory();
			JAXBElement<Document> jaxbElement = obj.createDocument(document);
			sw = new StringWriter();
			jaxbMarshaller.marshal(jaxbElement, sw);
		} catch (Exception e) {
			e.printStackTrace();
		}

		/// return document
		return sw.toString();
	}

	public String getPain_002_001_10Doc(String msgType, SendT request, String bobMsgID,String CreditStatusCode,String CreditStatusDesc) {
		com.bornfire.jaxb.pain_001_001_09.Document docPacsPain001 = getPain001_001_09UnMarshalDoc(request);

		com.bornfire.jaxb.pain_002_001_10.Document docPain002 = new com.bornfire.jaxb.pain_002_001_10.Document();
		com.bornfire.jaxb.pain_002_001_10.CustomerPaymentStatusReportV10 cstmrPmtStsRpt = new com.bornfire.jaxb.pain_002_001_10.CustomerPaymentStatusReportV10();

//Group Header
		com.bornfire.jaxb.pain_002_001_10.GroupHeader861 grpHdr = new com.bornfire.jaxb.pain_002_001_10.GroupHeader861();

		grpHdr.setMsgId(bobMsgID);

		grpHdr.setCreDtTm(listener.getxmlGregorianCalender("0"));

		com.bornfire.jaxb.pain_002_001_10.PartyIdentification1351 initgPty = new com.bornfire.jaxb.pain_002_001_10.PartyIdentification1351();
		com.bornfire.jaxb.pain_002_001_10.Party38Choice1 id = new com.bornfire.jaxb.pain_002_001_10.Party38Choice1();
		com.bornfire.jaxb.pain_002_001_10.OrganisationIdentification291 orgId = new com.bornfire.jaxb.pain_002_001_10.OrganisationIdentification291();
		orgId.setAnyBIC(docPacsPain001.getCstmrCdtTrfInitn().getGrpHdr().getFwdgAgt().getFinInstnId().getBICFI());
		id.setOrgId(orgId);
		initgPty.setId(id);
		grpHdr.setInitgPty(initgPty);

		com.bornfire.jaxb.pain_002_001_10.BranchAndFinancialInstitutionIdentification61 fwdgAgt = new com.bornfire.jaxb.pain_002_001_10.BranchAndFinancialInstitutionIdentification61();
		com.bornfire.jaxb.pain_002_001_10.FinancialInstitutionIdentification181 finInstnId = new com.bornfire.jaxb.pain_002_001_10.FinancialInstitutionIdentification181();
		finInstnId.setBICFI(
				docPacsPain001.getCstmrCdtTrfInitn().getGrpHdr().getInitgPty().getId().getOrgId().getAnyBIC());
		fwdgAgt.setFinInstnId(finInstnId);
		grpHdr.setFwdgAgt(fwdgAgt);

//Original group info and status
		com.bornfire.jaxb.pain_002_001_10.OriginalGroupHeader171 orgnlGrpInfAndSts = new com.bornfire.jaxb.pain_002_001_10.OriginalGroupHeader171();
		orgnlGrpInfAndSts.setOrgnlMsgId(docPacsPain001.getCstmrCdtTrfInitn().getGrpHdr().getMsgId());
		orgnlGrpInfAndSts.setOrgnlCreDtTm(docPacsPain001.getCstmrCdtTrfInitn().getGrpHdr().getCreDtTm());
		orgnlGrpInfAndSts.setOrgnlMsgNmId(request.getMessage().getMsgType());
		// orgnlGrpInfAndSts.setGrpSts(TranMonitorStatus.RJCT.toString());

		List<com.bornfire.jaxb.pain_002_001_10.StatusReasonInformation121> stsRsnInf = new ArrayList<com.bornfire.jaxb.pain_002_001_10.StatusReasonInformation121>();
		com.bornfire.jaxb.pain_002_001_10.StatusReasonInformation121 statusRsn = new com.bornfire.jaxb.pain_002_001_10.StatusReasonInformation121();
		com.bornfire.jaxb.pain_002_001_10.StatusReason6Choice1 statusReason6Choice1 = new com.bornfire.jaxb.pain_002_001_10.StatusReason6Choice1();
		statusReason6Choice1.setPrtry("NAUT");
		statusRsn.setRsn(statusReason6Choice1);
		// statusRsn.setAddtlInf(Collections.singletonList("Transaction Not
		// Supported"));
		stsRsnInf.add(statusRsn);
		orgnlGrpInfAndSts.setStsRsnInf(stsRsnInf);

//Original payment info and status	
		List<com.bornfire.jaxb.pain_002_001_10.OriginalPaymentInstruction321> orgnlPmtInfAndSts = new ArrayList<com.bornfire.jaxb.pain_002_001_10.OriginalPaymentInstruction321>();
		com.bornfire.jaxb.pain_002_001_10.OriginalPaymentInstruction321 originalPaymentInstruction321 = new com.bornfire.jaxb.pain_002_001_10.OriginalPaymentInstruction321();

		originalPaymentInstruction321.setOrgnlPmtInfId(docPacsPain001.getCstmrCdtTrfInitn().getPmtInf().getPmtInfId());

		List<com.bornfire.jaxb.pain_002_001_10.PaymentTransaction1051> txInfAndSts = new ArrayList<com.bornfire.jaxb.pain_002_001_10.PaymentTransaction1051>();
		com.bornfire.jaxb.pain_002_001_10.PaymentTransaction1051 paymentTransaction1051 = new com.bornfire.jaxb.pain_002_001_10.PaymentTransaction1051();
		com.bornfire.jaxb.pain_002_001_10.OriginalTransactionReference281 orgnlTxRef = new com.bornfire.jaxb.pain_002_001_10.OriginalTransactionReference281();
		com.bornfire.jaxb.pain_002_001_10.ActiveOrHistoricCurrencyAndAmount intrBkSttlmAmt = new com.bornfire.jaxb.pain_002_001_10.ActiveOrHistoricCurrencyAndAmount();
		intrBkSttlmAmt.setCcy(docPacsPain001.getCstmrCdtTrfInitn().getPmtInf().getCdtTrfTxInf().get(0).getAmt()
				.getInstdAmt().getCcy());
		intrBkSttlmAmt.setValue(docPacsPain001.getCstmrCdtTrfInitn().getPmtInf().getCdtTrfTxInf().get(0).getAmt()
				.getInstdAmt().getValue());
		orgnlTxRef.setIntrBkSttlmAmt(intrBkSttlmAmt);
		orgnlTxRef.setPmtMtd(com.bornfire.jaxb.pain_002_001_10.PaymentMethod4Code1.TRF);
		com.bornfire.jaxb.pain_002_001_10.DateAndDateTime2Choice1 reqdExctnDt = new com.bornfire.jaxb.pain_002_001_10.DateAndDateTime2Choice1();
		reqdExctnDt.setDt(docPacsPain001.getCstmrCdtTrfInitn().getPmtInf().getReqdExctnDt().getDt());
		orgnlTxRef.setReqdExctnDt(reqdExctnDt);

		com.bornfire.jaxb.pain_002_001_10.PaymentTypeInformation271 pmtTpInf = new com.bornfire.jaxb.pain_002_001_10.PaymentTypeInformation271();
		com.bornfire.jaxb.pain_002_001_10.CategoryPurpose1Choice1 ctgyPurp = new com.bornfire.jaxb.pain_002_001_10.CategoryPurpose1Choice1();
		ctgyPurp.setPrtry(docPacsPain001.getCstmrCdtTrfInitn().getPmtInf().getCdtTrfTxInf().get(0).getPmtTpInf()
				.getCtgyPurp().getPrtry());
		pmtTpInf.setCtgyPurp(ctgyPurp);/// Category Purpose
		com.bornfire.jaxb.pain_002_001_10.LocalInstrument2Choice1 lclInstrm = new com.bornfire.jaxb.pain_002_001_10.LocalInstrument2Choice1();
		lclInstrm.setPrtry(docPacsPain001.getCstmrCdtTrfInitn().getPmtInf().getCdtTrfTxInf().get(0).getPmtTpInf()
				.getLclInstrm().getPrtry());
		pmtTpInf.setLclInstrm(lclInstrm);/// Local Instrument
		List<com.bornfire.jaxb.pain_002_001_10.ServiceLevel8Choice1> svcLvl = new ArrayList<com.bornfire.jaxb.pain_002_001_10.ServiceLevel8Choice1>();
		com.bornfire.jaxb.pain_002_001_10.ServiceLevel8Choice1 serlc = new com.bornfire.jaxb.pain_002_001_10.ServiceLevel8Choice1();
		serlc.setPrtry(docPacsPain001.getCstmrCdtTrfInitn().getPmtInf().getCdtTrfTxInf().get(0).getPmtTpInf()
				.getSvcLvl().get(0).getPrtry());
		svcLvl.add(serlc);
		pmtTpInf.setSvcLvl(svcLvl);/// Service Level
		orgnlTxRef.setPmtTpInf(pmtTpInf);

		paymentTransaction1051.setOrgnlTxRef(orgnlTxRef);
		// paymentTransaction1051.setOrgnlInstrId(docPacsPain001.getCstmrCdtTrfInitn().getPmtInf().getCdtTrfTxInf().get(0).getPmtId().getInstrId());
		paymentTransaction1051.setOrgnlEndToEndId(
				docPacsPain001.getCstmrCdtTrfInitn().getPmtInf().getCdtTrfTxInf().get(0).getPmtId().getEndToEndId());
		// paymentTransaction1051.setTxSts(TranMonitorStatus.RJCT.toString());
		paymentTransaction1051.setAccptncDtTm(listener.getxmlGregorianCalender("0"));
		paymentTransaction1051.setAcctSvcrRef(docPacsPain001.getCstmrCdtTrfInitn().getGrpHdr().getMsgId());
		paymentTransaction1051.setTxSts(TranMonitorStatus.RJCT.toString());

		List<com.bornfire.jaxb.pain_002_001_10.StatusReasonInformation121> stsRsnIn = new ArrayList<com.bornfire.jaxb.pain_002_001_10.StatusReasonInformation121>();
		com.bornfire.jaxb.pain_002_001_10.StatusReasonInformation121 statusReasonInfo = new com.bornfire.jaxb.pain_002_001_10.StatusReasonInformation121();
		com.bornfire.jaxb.pain_002_001_10.StatusReason6Choice1 statusReason6Choice2 = new com.bornfire.jaxb.pain_002_001_10.StatusReason6Choice1();
		statusReason6Choice2.setPrtry(CreditStatusCode);
		statusReasonInfo.setRsn(statusReason6Choice2);
		List<String> addtlInf = new ArrayList<String>();
		addtlInf.add(CreditStatusDesc);
		statusReasonInfo.setAddtlInf(addtlInf);
		stsRsnIn.add(statusReasonInfo);
		paymentTransaction1051.setStsRsnInf(stsRsnIn);

		txInfAndSts.add(paymentTransaction1051);
		originalPaymentInstruction321.setTxInfAndSts(txInfAndSts);
		orgnlPmtInfAndSts.add(originalPaymentInstruction321);

		cstmrPmtStsRpt.setGrpHdr(grpHdr);
		cstmrPmtStsRpt.setOrgnlGrpInfAndSts(orgnlGrpInfAndSts);
		cstmrPmtStsRpt.setOrgnlPmtInfAndSts(orgnlPmtInfAndSts);

		docPain002.setCstmrPmtStsRpt(cstmrPmtStsRpt);

		JAXBContext jaxbContext;
		Marshaller jaxbMarshaller;
		StringWriter sw = null;
		try {
			jaxbContext = JAXBContext.newInstance(com.bornfire.jaxb.pain_002_001_10.Document.class);
			jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			jaxbMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
			jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			JaxbCharacterEscapeHandler jaxbCharHandler = new JaxbCharacterEscapeHandler();
			jaxbMarshaller.setProperty("com.sun.xml.bind.characterEscapeHandler", jaxbCharHandler);

			com.bornfire.jaxb.pain_002_001_10.ObjectFactory obj = new com.bornfire.jaxb.pain_002_001_10.ObjectFactory();
			JAXBElement<com.bornfire.jaxb.pain_002_001_10.Document> jaxbElement = obj.createDocument(docPain002);
			sw = new StringWriter();
			jaxbMarshaller.marshal(jaxbElement, sw);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sw.toString();
	}

	public com.bornfire.jaxb.pain_002_001_10.Document getPain_002_001_10UnMarshalDoc(SendT request) {
		String block4 = request.getMessage().getBlock4();
		final int start = block4.indexOf("<Document");
		final int end = block4.indexOf("</Document>");

		InputStream stream = null;
		JAXBContext jaxBContext;
		JAXBElement<com.bornfire.jaxb.pain_002_001_10.Document> jaxbElement = null;

		try {
			stream = new ByteArrayInputStream(block4.substring(start, end + 11).getBytes("UTF-8"));
			jaxBContext = JAXBContext.newInstance(com.bornfire.jaxb.pain_002_001_10.Document.class);
			Unmarshaller unMarshaller = jaxBContext.createUnmarshaller();
			XMLInputFactory factory = XMLInputFactory.newInstance();
			XMLEventReader xmlEventReader = factory.createXMLEventReader(stream);
			jaxbElement = unMarshaller.unmarshal(xmlEventReader, com.bornfire.jaxb.pain_002_001_10.Document.class);

		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		com.bornfire.jaxb.pain_002_001_10.Document document = jaxbElement.getValue();

		return document;
	}

	public com.bornfire.jaxb.camt_025_001_05.Document getCamt025_001_05UnMarshalDoc(SendT request) {
		String block4 = request.getMessage().getBlock4();
		final int start = block4.indexOf("<Document");
		final int end = block4.indexOf("</Document>");

		InputStream stream = null;
		JAXBContext jaxBContext;
		JAXBElement<com.bornfire.jaxb.camt_025_001_05.Document> jaxbElement = null;

		try {
			stream = new ByteArrayInputStream(block4.substring(start, end + 11).getBytes("UTF-8"));
			jaxBContext = JAXBContext.newInstance(com.bornfire.jaxb.camt_025_001_05.Document.class);
			Unmarshaller unMarshaller = jaxBContext.createUnmarshaller();
			XMLInputFactory factory = XMLInputFactory.newInstance();
			XMLEventReader xmlEventReader = factory.createXMLEventReader(stream);
			jaxbElement = unMarshaller.unmarshal(xmlEventReader, com.bornfire.jaxb.camt_025_001_05.Document.class);

		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		com.bornfire.jaxb.camt_025_001_05.Document document = jaxbElement.getValue();

		return document;
	}
	
	public com.bornfire.jaxb.camt_010_001_08.Document getCamt010_001_07UnMarshalDoc(SendT request) {
		String block4 = request.getMessage().getBlock4();
		final int start = block4.indexOf("<Document");
		final int end = block4.indexOf("</Document>");
		
		InputStream stream = null;
		JAXBContext jaxBContext;
		JAXBElement<com.bornfire.jaxb.camt_010_001_08.Document> jaxbElement = null;

		try {
			stream = new ByteArrayInputStream(block4.substring(start, end + 11).getBytes("UTF-8"));
			jaxBContext = JAXBContext.newInstance(com.bornfire.jaxb.camt_010_001_08.Document.class);
			Unmarshaller unMarshaller = jaxBContext.createUnmarshaller();
			XMLInputFactory factory = XMLInputFactory.newInstance();
			XMLEventReader xmlEventReader = factory.createXMLEventReader(stream);
			jaxbElement = unMarshaller.unmarshal(xmlEventReader, com.bornfire.jaxb.camt_010_001_08.Document.class);

		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		com.bornfire.jaxb.camt_010_001_08.Document document = jaxbElement.getValue();
		//System.out.println(document.getRtrLmt().getRptOrErr().getBizRpt().getCurLmt().get(0).getLmtOrErr().getBizErr().getDesc());
		return document;
	}
	
	

	public String getCamt_011_001_07Doc(String msgType, String bobMsgID) {

		com.bornfire.jaxb.camt_011_001_07.Document documentCamt011 = new com.bornfire.jaxb.camt_011_001_07.Document();
		com.bornfire.jaxb.camt_011_001_07.ModifyLimitV07 modfyLmt = new com.bornfire.jaxb.camt_011_001_07.ModifyLimitV07();

///Message Header
		com.bornfire.jaxb.camt_011_001_07.MessageHeader11 msgHdr = new com.bornfire.jaxb.camt_011_001_07.MessageHeader11();
		msgHdr.setMsgId(bobMsgID);
		msgHdr.setCreDtTm(listener.getxmlGregorianCalender("0"));

///Limit Details
		com.bornfire.jaxb.camt_011_001_07.LimitStructure31 lmtDtls = new com.bornfire.jaxb.camt_011_001_07.LimitStructure31();
		com.bornfire.jaxb.camt_011_001_07.LimitIdentification2Choice1 lmtId = new com.bornfire.jaxb.camt_011_001_07.LimitIdentification2Choice1();

		com.bornfire.jaxb.camt_011_001_07.LimitIdentification51 cur = new com.bornfire.jaxb.camt_011_001_07.LimitIdentification51();
		com.bornfire.jaxb.camt_011_001_07.SystemIdentification2Choice1 sysId = new com.bornfire.jaxb.camt_011_001_07.SystemIdentification2Choice1();
		com.bornfire.jaxb.camt_011_001_07.MarketInfrastructureIdentification1Choice1 mktInfrstrctrId = new com.bornfire.jaxb.camt_011_001_07.MarketInfrastructureIdentification1Choice1();
		mktInfrstrctrId.setPrtry("ACH");
		sysId.setMktInfrstrctrId(mktInfrstrctrId);
		cur.setSysId(sysId);

		com.bornfire.jaxb.camt_011_001_07.BranchAndFinancialInstitutionIdentification61 bilLmtCtrPtyId = new com.bornfire.jaxb.camt_011_001_07.BranchAndFinancialInstitutionIdentification61();
		com.bornfire.jaxb.camt_011_001_07.FinancialInstitutionIdentification181 finInstnId = new com.bornfire.jaxb.camt_011_001_07.FinancialInstitutionIdentification181();
		finInstnId.setBICFI(env.getProperty("ipsx.bicfi"));
		bilLmtCtrPtyId.setFinInstnId(finInstnId);
		cur.setBilLmtCtrPtyId(bilLmtCtrPtyId);

		com.bornfire.jaxb.camt_011_001_07.LimitType1Choice1 tp = new com.bornfire.jaxb.camt_011_001_07.LimitType1Choice1();
		tp.setCd(LimitType3Code1.MULT);
		//tp.setPrtry("ACH");
		cur.setTp(tp);
		

		com.bornfire.jaxb.camt_011_001_07.BranchAndFinancialInstitutionIdentification61 acctOwnr = new com.bornfire.jaxb.camt_011_001_07.BranchAndFinancialInstitutionIdentification61();
		com.bornfire.jaxb.camt_011_001_07.FinancialInstitutionIdentification181 finInstnId1 = new com.bornfire.jaxb.camt_011_001_07.FinancialInstitutionIdentification181();
		finInstnId1.setBICFI(env.getProperty("ipsx.bicfi"));
		acctOwnr.setFinInstnId(finInstnId1);
		cur.setAcctOwnr(acctOwnr);

		com.bornfire.jaxb.camt_011_001_07.AccountIdentification4Choice1 acctId = new com.bornfire.jaxb.camt_011_001_07.AccountIdentification4Choice1();
		com.bornfire.jaxb.camt_011_001_07.GenericAccountIdentification11 othr = new com.bornfire.jaxb.camt_011_001_07.GenericAccountIdentification11();
		othr.setId(env.getProperty("ipsx.dbtragtacct"));
		acctId.setOthr(othr);
		cur.setAcctId(acctId);

		lmtId.setCur(cur);

		com.bornfire.jaxb.camt_011_001_07.Limit81 newLmtValSet = new com.bornfire.jaxb.camt_011_001_07.Limit81();
		com.bornfire.jaxb.camt_011_001_07.Amount2Choice1 amt = new com.bornfire.jaxb.camt_011_001_07.Amount2Choice1();
		com.bornfire.jaxb.camt_011_001_07.ActiveCurrencyAndAmount amtWthCcy = new com.bornfire.jaxb.camt_011_001_07.ActiveCurrencyAndAmount();
		amtWthCcy.setCcy("MUR");
		amtWthCcy.setValue(new BigDecimal("100.00"));
		amt.setAmtWthCcy(amtWthCcy);
		newLmtValSet.setAmt(amt);
		newLmtValSet.setCdtDbtInd(CreditDebitCode.DBIT);

		lmtDtls.setLmtId(lmtId);
		lmtDtls.setNewLmtValSet(newLmtValSet);

		modfyLmt.setLmtDtls(lmtDtls);
		modfyLmt.setMsgHdr(msgHdr);

		documentCamt011.setModfyLmt(modfyLmt);

		/// Convert Document XMl element to String
		JAXBContext jaxbContext;
		Marshaller jaxbMarshaller;
		StringWriter sw = null;
		try {
			jaxbContext = JAXBContext.newInstance(com.bornfire.jaxb.camt_011_001_07.Document.class);
			jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			jaxbMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
			jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			JaxbCharacterEscapeHandler jaxbCharHandler = new JaxbCharacterEscapeHandler();
			jaxbMarshaller.setProperty("com.sun.xml.bind.characterEscapeHandler", jaxbCharHandler);

			com.bornfire.jaxb.camt_011_001_07.ObjectFactory obj = new com.bornfire.jaxb.camt_011_001_07.ObjectFactory();
			JAXBElement<com.bornfire.jaxb.camt_011_001_07.Document> jaxbElement = obj.createDocument(documentCamt011);
			sw = new StringWriter();
			jaxbMarshaller.marshal(jaxbElement, sw);
		} catch (Exception e) {
			e.printStackTrace();
		}

		/// return document
		return sw.toString();
	}

	public String getCamt_009_001_07Doc(String msgType, String bobMsgID) {
		com.bornfire.jaxb.camt_009_001_07.Document docCamt009 = new com.bornfire.jaxb.camt_009_001_07.Document();

		com.bornfire.jaxb.camt_009_001_07.GetLimitV07 getLmt = new com.bornfire.jaxb.camt_009_001_07.GetLimitV07();

		com.bornfire.jaxb.camt_009_001_07.MessageHeader91 msgHdr = new com.bornfire.jaxb.camt_009_001_07.MessageHeader91();
		msgHdr.setMsgId(bobMsgID);
		msgHdr.setCreDtTm(listener.getxmlGregorianCalender("0"));

		getLmt.setMsgHdr(msgHdr);

		com.bornfire.jaxb.camt_009_001_07.LimitQuery41 lmtQryDef = new com.bornfire.jaxb.camt_009_001_07.LimitQuery41();
		com.bornfire.jaxb.camt_009_001_07.LimitCriteria6Choice1 lmtCrit = new com.bornfire.jaxb.camt_009_001_07.LimitCriteria6Choice1();
		com.bornfire.jaxb.camt_009_001_07.LimitCriteria61 newCrit = new com.bornfire.jaxb.camt_009_001_07.LimitCriteria61();
		com.bornfire.jaxb.camt_009_001_07.LimitSearchCriteria61 schCrit = new com.bornfire.jaxb.camt_009_001_07.LimitSearchCriteria61();

		com.bornfire.jaxb.camt_009_001_07.AccountIdentification4Choice1 acctId = new com.bornfire.jaxb.camt_009_001_07.AccountIdentification4Choice1();
		com.bornfire.jaxb.camt_009_001_07.GenericAccountIdentification11 othr = new com.bornfire.jaxb.camt_009_001_07.GenericAccountIdentification11();
		othr.setId(env.getProperty("ipsx.dbtragtacct"));
		acctId.setOthr(othr);

		schCrit.setAcctId(acctId);
		newCrit.setSchCrit(schCrit);
		lmtCrit.setNewCrit(newCrit);
		lmtQryDef.setLmtCrit(lmtCrit);

		getLmt.setLmtQryDef(lmtQryDef);
		docCamt009.setGetLmt(getLmt);

		/// Convert Document XMl element to String
		JAXBContext jaxbContext;
		Marshaller jaxbMarshaller;
		StringWriter sw = null;
		try {
			jaxbContext = JAXBContext.newInstance(com.bornfire.jaxb.camt_009_001_07.Document.class);
			jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			jaxbMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
			jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			JaxbCharacterEscapeHandler jaxbCharHandler = new JaxbCharacterEscapeHandler();
			jaxbMarshaller.setProperty("com.sun.xml.bind.characterEscapeHandler", jaxbCharHandler);

			com.bornfire.jaxb.camt_009_001_07.ObjectFactory obj = new com.bornfire.jaxb.camt_009_001_07.ObjectFactory();
			JAXBElement<com.bornfire.jaxb.camt_009_001_07.Document> jaxbElement = obj.createDocument(docCamt009);
			sw = new StringWriter();
			jaxbMarshaller.marshal(jaxbElement, sw);
		} catch (Exception e) {
			e.printStackTrace();
		}

		/// return document
		return sw.toString();
	}

	public Object getPacsBulkCredit_008_001_01Doc(String msgId, String frAccountName, String frAccountNumber,
			String toAccountName, String toAccountNumber, String trAmt, String trCurrency,
			BankAgentTable othBankAgent,String msgSeq,String EntToEndID) {
		///Group Header
				GroupHeader931 grpHdr = new GroupHeader931();
				String msg_seq_id = msgId;
				/// Message Identification ID
				grpHdr.setMsgId(msg_seq_id);
				/// Credit Date Time
				grpHdr.setCreDtTm(listener.getxmlGregorianCalender("0"));
				/// Number Of Transaction
				grpHdr.setNbOfTxs("1");
				/// Total Inter Bank Settlement Amount
				ActiveCurrencyAndAmount activeCurrencyAndAmount = new ActiveCurrencyAndAmount();
				activeCurrencyAndAmount.setCcy(trCurrency);/// Currency
				activeCurrencyAndAmount.setValue(new BigDecimal(trAmt));/// Amount
				grpHdr.setTtlIntrBkSttlmAmt(activeCurrencyAndAmount);
				/// Inter Bank settlement Date
				grpHdr.setIntrBkSttlmDt(listener.getxmlGregorianCalender("1"));
				/// Settlement Instruction
				SettlementInstruction71 sttlmInf = new SettlementInstruction71();
				sttlmInf.setSttlmMtd(SettlementMethod1Code1.CLRG);/// Settlement method (CLRG)
				grpHdr.setSttlmInf(sttlmInf);

		///// creditTransaction Information
				List<CreditTransferTransaction391> cdtTrfTxInf = new ArrayList<CreditTransferTransaction391>();
				CreditTransferTransaction391 creditTransferTransaction391 = new CreditTransferTransaction391();
				/// Payment Identification
				PaymentIdentification71 pmtId = new PaymentIdentification71();
				pmtId.setInstrId(msg_seq_id);/// Instruction ID
				//String EntToEndID=env.getProperty("ipsx.bicfi")+new SimpleDateFormat("YYYYMMdd").format(new Date())+msgSeq;
				pmtId.setEndToEndId(EntToEndID);/// End to End ID
				pmtId.setTxId(msg_seq_id);/// Transaction ID
				creditTransferTransaction391.setPmtId(pmtId);
				/// Payment Type Information
				PaymentTypeInformation281 pmtTpInf = new PaymentTypeInformation281();
				pmtTpInf.setClrChanl(ClearingChannel2Code1.RTGS);/// Clearing Channel like(RTGS,RTNS,MPNS)
				CategoryPurpose1Choice1 ctgyPurp = new CategoryPurpose1Choice1();
				ctgyPurp.setPrtry("100");
				pmtTpInf.setCtgyPurp(ctgyPurp);/// Category Purpose
				LocalInstrument2Choice1 lclInstrm = new LocalInstrument2Choice1();
				lclInstrm.setPrtry("CSDC");
				pmtTpInf.setLclInstrm(lclInstrm);/// Local Instrument
				List<ServiceLevel8Choice1> svcLvl = new ArrayList<ServiceLevel8Choice1>();
				ServiceLevel8Choice1 serlc = new ServiceLevel8Choice1();
				serlc.setPrtry("0100");
				svcLvl.add(serlc);
				pmtTpInf.setSvcLvl(svcLvl);/// Service Level
				creditTransferTransaction391.setPmtTpInf(pmtTpInf);
				/// Inter Bank Settlement Currency And Amount
				ActiveCurrencyAndAmount intrBkSttlmAmt = new ActiveCurrencyAndAmount();
				intrBkSttlmAmt.setCcy(trCurrency);/// Currency
				intrBkSttlmAmt.setValue(new BigDecimal(trAmt));/// Amount
				creditTransferTransaction391.setIntrBkSttlmAmt(intrBkSttlmAmt);
				/// Charge Bearer
				creditTransferTransaction391.setChrgBr(ChargeBearerType1Code.SLEV);
				/// Financial Institution Identification(Instructing Agent)
				FinancialInstitutionIdentification181 fin = new FinancialInstitutionIdentification181();
				fin.setBICFI(env.getProperty("ipsx.bicfi"));
				BranchAndFinancialInstitutionIdentification61 instgAgt = new BranchAndFinancialInstitutionIdentification61();
				instgAgt.setFinInstnId(fin);
				creditTransferTransaction391.setInstgAgt(instgAgt);
				/// Financial Institution Identification(Instructed Agent)
				FinancialInstitutionIdentification181 fin1 = new FinancialInstitutionIdentification181();
				// fin1.setBICFI("TSTAMUMU");
				fin1.setBICFI(othBankAgent.getBank_agent());
				BranchAndFinancialInstitutionIdentification61 instdAgt = new BranchAndFinancialInstitutionIdentification61();
				instdAgt.setFinInstnId(fin1);
				creditTransferTransaction391.setInstdAgt(instdAgt);
				/// Debtor name
				PartyIdentification1351 dbtr = new PartyIdentification1351();
				dbtr.setNm(frAccountName);
				creditTransferTransaction391.setDbtr(dbtr);

				/// Debtor Account Number
				CashAccount381 dbtrAcct = new CashAccount381();
				AccountIdentification4Choice1 acc1 = new AccountIdentification4Choice1();
				GenericAccountIdentification11 id = new GenericAccountIdentification11();
				id.setId((frAccountNumber));
				acc1.setOthr(id);
				dbtrAcct.setId(acc1);
				creditTransferTransaction391.setDbtrAcct(dbtrAcct);

				/// Debtor Agent
				BranchAndFinancialInstitutionIdentification61 dbtrAgt = new BranchAndFinancialInstitutionIdentification61();
				FinancialInstitutionIdentification181 fin2 = new FinancialInstitutionIdentification181();
				fin2.setBICFI(env.getProperty("ipsx.dbtragt"));
				dbtrAgt.setFinInstnId(fin2);
				creditTransferTransaction391.setDbtrAgt(dbtrAgt);
				/// Debtor Agent Account
				CashAccount381 dbtrAgtAcct = new CashAccount381();
				AccountIdentification4Choice1 id2 = new AccountIdentification4Choice1();
				GenericAccountIdentification11 gen1 = new GenericAccountIdentification11();
				gen1.setId(env.getProperty("ipsx.dbtragtacct"));
				id2.setOthr(gen1);
				dbtrAgtAcct.setId(id2);
				creditTransferTransaction391.setDbtrAgtAcct(dbtrAgtAcct);
				/// Creditor Agent
				BranchAndFinancialInstitutionIdentification61 cdtrAgt = new BranchAndFinancialInstitutionIdentification61();
				FinancialInstitutionIdentification181 fin3 = new FinancialInstitutionIdentification181();
				// fin3.setBICFI("TSTAMUMU");
				fin3.setBICFI(othBankAgent.getBank_agent());
				cdtrAgt.setFinInstnId(fin3);
				creditTransferTransaction391.setCdtrAgt(cdtrAgt);
				/// Creditor Agent Account
				CashAccount381 cdtrAgtAcct = new CashAccount381();
				AccountIdentification4Choice1 acc3 = new AccountIdentification4Choice1();
				GenericAccountIdentification11 gen3 = new GenericAccountIdentification11();
				// gen3.setId("TSTBNRT");
				gen3.setId(othBankAgent.getBank_agent_account());
				acc3.setOthr(gen3);
				cdtrAgtAcct.setId(acc3);
				creditTransferTransaction391.setCdtrAgtAcct(cdtrAgtAcct);
				/// Creditor Name
				PartyIdentification1351 cdtr = new PartyIdentification1351();
				cdtr.setNm(toAccountName);
				creditTransferTransaction391.setCdtr(cdtr);
				/// Creditor Account Number
				CashAccount381 cdtrAcct = new CashAccount381();
				AccountIdentification4Choice1 id4 = new AccountIdentification4Choice1();
				GenericAccountIdentification11 gen4 = new GenericAccountIdentification11();
				gen4.setId(toAccountNumber);
				id4.setOthr(gen4);
				cdtrAcct.setId(id4);
				creditTransferTransaction391.setCdtrAcct(cdtrAcct);
				cdtTrfTxInf.add(creditTransferTransaction391);

		///Financial Customer Credit Transfer		
				FIToFICustomerCreditTransferV08 fiToFICstmrCdtTrf = new FIToFICustomerCreditTransferV08();
				fiToFICstmrCdtTrf.setGrpHdr(grpHdr);
				fiToFICstmrCdtTrf.setCdtTrfTxInf(cdtTrfTxInf);

		///Document
				Document document = new Document();
				document.setFIToFICstmrCdtTrf(fiToFICstmrCdtTrf);

		///Convert Document XMl element to String
				JAXBContext jaxbContext;
				Marshaller jaxbMarshaller;
				StringWriter sw = null;
				try {
					jaxbContext = JAXBContext.newInstance(Document.class);
					jaxbMarshaller = jaxbContext.createMarshaller();
					jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
					jaxbMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
					jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
					JaxbCharacterEscapeHandler jaxbCharHandler = new JaxbCharacterEscapeHandler();
					jaxbMarshaller.setProperty("com.sun.xml.bind.characterEscapeHandler", jaxbCharHandler);

					com.bornfire.jaxb.pacs_008_001_08.ObjectFactory obj = new com.bornfire.jaxb.pacs_008_001_08.ObjectFactory();
					JAXBElement<Document> jaxbElement = obj.createDocument(document);
					sw = new StringWriter();
					jaxbMarshaller.marshal(jaxbElement, sw);
				} catch (Exception e) {
					e.printStackTrace();
				}

		///return document 
				return sw.toString();


	}

	public Object getPacsBulkDebit_008_001_01Doc(String msgId, String frAccountName, String frAccountNumber,
			String toAccountName, String toAccountNumber, String trAmt, String trCurrency,
			BankAgentTable othBankAgent,String msgSeq,String EntToEndID) {
		///Group Header
				GroupHeader931 grpHdr = new GroupHeader931();
				String msg_seq_id = msgId;
				/// Message Identification ID
				grpHdr.setMsgId(msg_seq_id);
				/// Credit Date Time
				grpHdr.setCreDtTm(listener.getxmlGregorianCalender("0"));
				/// Number Of Transaction
				grpHdr.setNbOfTxs("1");
				/// Total Inter Bank Settlement Amount
				ActiveCurrencyAndAmount activeCurrencyAndAmount = new ActiveCurrencyAndAmount();
				activeCurrencyAndAmount.setCcy(trCurrency);/// Currency
				activeCurrencyAndAmount.setValue(new BigDecimal(trAmt));/// Amount
				grpHdr.setTtlIntrBkSttlmAmt(activeCurrencyAndAmount);
				/// Inter Bank settlement Date
				grpHdr.setIntrBkSttlmDt(listener.getxmlGregorianCalender("1"));
				/// Settlement Instruction
				SettlementInstruction71 sttlmInf = new SettlementInstruction71();
				sttlmInf.setSttlmMtd(SettlementMethod1Code1.CLRG);/// Settlement method (CLRG)
				grpHdr.setSttlmInf(sttlmInf);

		///// creditTransaction Information
				List<CreditTransferTransaction391> cdtTrfTxInf = new ArrayList<CreditTransferTransaction391>();
				CreditTransferTransaction391 creditTransferTransaction391 = new CreditTransferTransaction391();
				/// Payment Identification
				PaymentIdentification71 pmtId = new PaymentIdentification71();
				pmtId.setInstrId(msg_seq_id);/// Instruction ID
				//String EntToEndID=env.getProperty("ipsx.bicfi")+new SimpleDateFormat("YYYYMMdd").format(new Date())+msgSeq;
				pmtId.setEndToEndId(EntToEndID);/// End to End ID
				pmtId.setTxId(msg_seq_id);/// Transaction ID
				creditTransferTransaction391.setPmtId(pmtId);
				/// Payment Type Information
				PaymentTypeInformation281 pmtTpInf = new PaymentTypeInformation281();
				pmtTpInf.setClrChanl(ClearingChannel2Code1.RTGS);/// Clearing Channel like(RTGS,RTNS,MPNS)
				CategoryPurpose1Choice1 ctgyPurp = new CategoryPurpose1Choice1();
				ctgyPurp.setPrtry("100");
				pmtTpInf.setCtgyPurp(ctgyPurp);/// Category Purpose
				LocalInstrument2Choice1 lclInstrm = new LocalInstrument2Choice1();
				lclInstrm.setPrtry("CSDC");
				pmtTpInf.setLclInstrm(lclInstrm);/// Local Instrument
				List<ServiceLevel8Choice1> svcLvl = new ArrayList<ServiceLevel8Choice1>();
				ServiceLevel8Choice1 serlc = new ServiceLevel8Choice1();
				serlc.setPrtry("0100");
				svcLvl.add(serlc);
				pmtTpInf.setSvcLvl(svcLvl);/// Service Level
				creditTransferTransaction391.setPmtTpInf(pmtTpInf);
				/// Inter Bank Settlement Currency And Amount
				ActiveCurrencyAndAmount intrBkSttlmAmt = new ActiveCurrencyAndAmount();
				intrBkSttlmAmt.setCcy(trCurrency);/// Currency
				intrBkSttlmAmt.setValue(new BigDecimal(trAmt));/// Amount
				creditTransferTransaction391.setIntrBkSttlmAmt(intrBkSttlmAmt);
				/// Charge Bearer
				creditTransferTransaction391.setChrgBr(ChargeBearerType1Code.SLEV);
				/// Financial Institution Identification(Instructing Agent)
				FinancialInstitutionIdentification181 fin = new FinancialInstitutionIdentification181();
				fin.setBICFI(env.getProperty("ipsx.bicfi"));
				BranchAndFinancialInstitutionIdentification61 instgAgt = new BranchAndFinancialInstitutionIdentification61();
				instgAgt.setFinInstnId(fin);
				creditTransferTransaction391.setInstgAgt(instgAgt);
				/// Financial Institution Identification(Instructed Agent)
				FinancialInstitutionIdentification181 fin1 = new FinancialInstitutionIdentification181();
				// fin1.setBICFI("TSTAMUMU");
				fin1.setBICFI(othBankAgent.getBank_agent());
				BranchAndFinancialInstitutionIdentification61 instdAgt = new BranchAndFinancialInstitutionIdentification61();
				instdAgt.setFinInstnId(fin1);
				creditTransferTransaction391.setInstdAgt(instdAgt);
				/// Debtor name
				PartyIdentification1351 dbtr = new PartyIdentification1351();
				dbtr.setNm(frAccountName);
				creditTransferTransaction391.setDbtr(dbtr);
				
				  ///Debtor Account Number 
				CashAccount381 dbtrAcct = new CashAccount381();
				AccountIdentification4Choice1 acc1 = new AccountIdentification4Choice1();
				GenericAccountIdentification11 id = new GenericAccountIdentification11();
				id.setId((frAccountNumber));
				acc1.setOthr(id);
				dbtrAcct.setId(acc1);
				creditTransferTransaction391.setDbtrAcct(dbtrAcct);

				/// Debtor Agent
				BranchAndFinancialInstitutionIdentification61 dbtrAgt = new BranchAndFinancialInstitutionIdentification61();
				FinancialInstitutionIdentification181 fin2 = new FinancialInstitutionIdentification181();
				fin2.setBICFI(env.getProperty("ipsx.dbtragt"));
				dbtrAgt.setFinInstnId(fin2);
				creditTransferTransaction391.setDbtrAgt(dbtrAgt);
				/// Debtor Agent Account
				CashAccount381 dbtrAgtAcct = new CashAccount381();
				AccountIdentification4Choice1 id2 = new AccountIdentification4Choice1();
				GenericAccountIdentification11 gen1 = new GenericAccountIdentification11();
				gen1.setId(env.getProperty("ipsx.dbtragtacct"));
				id2.setOthr(gen1);
				dbtrAgtAcct.setId(id2);
				creditTransferTransaction391.setDbtrAgtAcct(dbtrAgtAcct);
				/// Creditor Agent
				BranchAndFinancialInstitutionIdentification61 cdtrAgt = new BranchAndFinancialInstitutionIdentification61();
				FinancialInstitutionIdentification181 fin3 = new FinancialInstitutionIdentification181();
				// fin3.setBICFI("TSTAMUMU");
				fin3.setBICFI(othBankAgent.getBank_agent());
				cdtrAgt.setFinInstnId(fin3);
				creditTransferTransaction391.setCdtrAgt(cdtrAgt);
				/// Creditor Agent Account
				CashAccount381 cdtrAgtAcct = new CashAccount381();
				AccountIdentification4Choice1 acc3 = new AccountIdentification4Choice1();
				GenericAccountIdentification11 gen3 = new GenericAccountIdentification11();
				// gen3.setId("TSTBNRT");
				gen3.setId(othBankAgent.getBank_agent_account());
				acc3.setOthr(gen3);
				cdtrAgtAcct.setId(acc3);
				creditTransferTransaction391.setCdtrAgtAcct(cdtrAgtAcct);
				/// Creditor Name
				PartyIdentification1351 cdtr = new PartyIdentification1351();
				cdtr.setNm(toAccountName);
				creditTransferTransaction391.setCdtr(cdtr);
				/// Creditor Account Number
				CashAccount381 cdtrAcct = new CashAccount381();
				AccountIdentification4Choice1 id4 = new AccountIdentification4Choice1();
				GenericAccountIdentification11 gen4 = new GenericAccountIdentification11();
				gen4.setId(toAccountNumber);
				id4.setOthr(gen4);
				cdtrAcct.setId(id4);
				creditTransferTransaction391.setCdtrAcct(cdtrAcct);
				cdtTrfTxInf.add(creditTransferTransaction391);

		///Financial Customer Credit Transfer		
				FIToFICustomerCreditTransferV08 fiToFICstmrCdtTrf = new FIToFICustomerCreditTransferV08();
				fiToFICstmrCdtTrf.setGrpHdr(grpHdr);
				fiToFICstmrCdtTrf.setCdtTrfTxInf(cdtTrfTxInf);

		///Document
				Document document = new Document();
				document.setFIToFICstmrCdtTrf(fiToFICstmrCdtTrf);

		///Convert Document XMl element to String
				JAXBContext jaxbContext;
				Marshaller jaxbMarshaller;
				StringWriter sw = null;
				try {
					jaxbContext = JAXBContext.newInstance(Document.class);
					jaxbMarshaller = jaxbContext.createMarshaller();
					jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
					jaxbMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
					jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
					JaxbCharacterEscapeHandler jaxbCharHandler = new JaxbCharacterEscapeHandler();
					jaxbMarshaller.setProperty("com.sun.xml.bind.characterEscapeHandler", jaxbCharHandler);

					com.bornfire.jaxb.pacs_008_001_08.ObjectFactory obj = new com.bornfire.jaxb.pacs_008_001_08.ObjectFactory();
					JAXBElement<Document> jaxbElement = obj.createDocument(document);
					sw = new StringWriter();
					jaxbMarshaller.marshal(jaxbElement, sw);
				} catch (Exception e) {
					e.printStackTrace();
				}

		///return document 
				return sw.toString();


	}
	
	
	public Object getPain_001_001_09Doc(String msgType, SendT request, String acctName, String acctNumber,
			String currencyCode ,String benName, String benAcctNumber,
			String trAmt, String trRmks, String seqUniqueID, String cimMsgID, String msgSeq, String endTOEndID,
			String msgNetMir,String cryptogram,String instgAgent,String instdAgent,String debtorAgent,String debtorAgentAcct,String CreditorAgent,String CreditorAgentAcct,
			String lclInstrData,String ctgyPurpData,String chargeBearer) {
		///Group Header
		GroupHeader851 grpHdr = new GroupHeader851();
		String msg_seq_id = seqUniqueID;
		/// Message Identification ID
		grpHdr.setMsgId(msg_seq_id);
		/// Credit Date Time
		grpHdr.setCreDtTm(listener.getxmlGregorianCalender("0"));
		/// Number Of Transaction
		grpHdr.setNbOfTxs("1");
		
		///Authorisation Cryptogram
		Authorisation1Choice1 Authstn=new Authorisation1Choice1();
		Authstn.setPrtry(cryptogram);
		List<Authorisation1Choice1> authstn=Arrays.asList(Authstn);
		grpHdr.setAuthstn(authstn);
		
		////Initiating Party(InitgPty)
		com.bornfire.jaxb.pain_001_001_09.PartyIdentification1351 initgPty=new com.bornfire.jaxb.pain_001_001_09.PartyIdentification1351();
		
		com.bornfire.jaxb.pain_001_001_09.Party38Choice1 id=new com.bornfire.jaxb.pain_001_001_09.Party38Choice1();
		com.bornfire.jaxb.pain_001_001_09.OrganisationIdentification291 orgId=new com.bornfire.jaxb.pain_001_001_09.OrganisationIdentification291();
		orgId.setAnyBIC(instgAgent);
		id.setOrgId(orgId);
		initgPty.setId(id);
		grpHdr.setInitgPty(initgPty);
		
		////Forwarding Agent
		com.bornfire.jaxb.pain_001_001_09.BranchAndFinancialInstitutionIdentification61 fwdgAgt=new com.bornfire.jaxb.pain_001_001_09.BranchAndFinancialInstitutionIdentification61();
		com.bornfire.jaxb.pain_001_001_09.FinancialInstitutionIdentification181 finInstnId=new com.bornfire.jaxb.pain_001_001_09.FinancialInstitutionIdentification181();
		finInstnId.setBICFI(instdAgent);
		fwdgAgt.setFinInstnId(finInstnId);
		grpHdr.setFwdgAgt(fwdgAgt);
		

		/////Payment Information
		com.bornfire.jaxb.pain_001_001_09.PaymentInstruction301 pmtInf=new com.bornfire.jaxb.pain_001_001_09.PaymentInstruction301();
		pmtInf.setPmtInfId(seqUniqueID);
		pmtInf.setPmtMtd(com.bornfire.jaxb.pain_001_001_09.PaymentMethod3Code1.TRF);
		com.bornfire.jaxb.pain_001_001_09.DateAndDateTime2Choice1 reqdExctnDt=new com.bornfire.jaxb.pain_001_001_09.DateAndDateTime2Choice1();
		reqdExctnDt.setDt(listener.getxmlGregorianCalender("1"));
		pmtInf.setReqdExctnDt(reqdExctnDt);
		
		com.bornfire.jaxb.pain_001_001_09.PartyIdentification1351 dbtr=new com.bornfire.jaxb.pain_001_001_09.PartyIdentification1351();
		dbtr.setNm(acctName);
		/*com.bornfire.jaxb.pain_001_001_09.PostalAddress241 pstlAdr=new com.bornfire.jaxb.pain_001_001_09.PostalAddress241();
		pstlAdr.setAdrLine(Arrays.asList(""));
		dbtr.setPstlAdr(pstlAdr);*/
		pmtInf.setDbtr(dbtr);
		
		com.bornfire.jaxb.pain_001_001_09.CashAccount381 dbtrAcct=new com.bornfire.jaxb.pain_001_001_09.CashAccount381();
		com.bornfire.jaxb.pain_001_001_09.AccountIdentification4Choice1 id1=new com.bornfire.jaxb.pain_001_001_09.AccountIdentification4Choice1();
		com.bornfire.jaxb.pain_001_001_09.GenericAccountIdentification11 othr=new com.bornfire.jaxb.pain_001_001_09.GenericAccountIdentification11();
		othr.setId(acctNumber);
		id1.setOthr(othr);
		dbtrAcct.setId(id1);
		pmtInf.setDbtrAcct(dbtrAcct);
		
		com.bornfire.jaxb.pain_001_001_09.BranchAndFinancialInstitutionIdentification61 dbtrAgt=new com.bornfire.jaxb.pain_001_001_09.BranchAndFinancialInstitutionIdentification61();
		com.bornfire.jaxb.pain_001_001_09.FinancialInstitutionIdentification181 finInstnId1=new com.bornfire.jaxb.pain_001_001_09.FinancialInstitutionIdentification181();
		finInstnId1.setBICFI(debtorAgent);
		dbtrAgt.setFinInstnId(finInstnId1);
		pmtInf.setDbtrAgt(dbtrAgt);
		
		com.bornfire.jaxb.pain_001_001_09.CashAccount381 dbtrAgtAcct=new com.bornfire.jaxb.pain_001_001_09.CashAccount381();
		com.bornfire.jaxb.pain_001_001_09.AccountIdentification4Choice1 finInstnId3=new com.bornfire.jaxb.pain_001_001_09.AccountIdentification4Choice1();
		com.bornfire.jaxb.pain_001_001_09.GenericAccountIdentification11 other1=new com.bornfire.jaxb.pain_001_001_09.GenericAccountIdentification11();
		other1.setId(debtorAgentAcct);
		finInstnId3.setOthr(other1);
		dbtrAgtAcct.setId(finInstnId3);
		pmtInf.setDbtrAgtAcct(dbtrAgtAcct);
		
		pmtInf.setChrgBr(com.bornfire.jaxb.pain_001_001_09.ChargeBearerType1Code1.SLEV);

		/*if (chargeBearer.equals(com.bornfire.jaxb.pain_001_001_09.ChargeBearerType1Code1.SHAR.value())) {
			pmtInf.setChrgBr(com.bornfire.jaxb.pain_001_001_09.ChargeBearerType1Code1.SHAR);
		} else if (chargeBearer.equals(com.bornfire.jaxb.pain_001_001_09.ChargeBearerType1Code1.DEBT.value())) {
			pmtInf.setChrgBr(com.bornfire.jaxb.pain_001_001_09.ChargeBearerType1Code1.DEBT);
		} else if (chargeBearer.equals(com.bornfire.jaxb.pain_001_001_09.ChargeBearerType1Code1.CRED.value())) {
			pmtInf.setChrgBr(com.bornfire.jaxb.pain_001_001_09.ChargeBearerType1Code1.CRED);
		} else {
			pmtInf.setChrgBr(com.bornfire.jaxb.pain_001_001_09.ChargeBearerType1Code1.SLEV);
		}*/
		
		/////Credit TransferInfo
		List<com.bornfire.jaxb.pain_001_001_09.CreditTransferTransaction341> cdtTrfTxInfList=new ArrayList<>();
		com.bornfire.jaxb.pain_001_001_09.CreditTransferTransaction341 cdtTrfTxInf=new com.bornfire.jaxb.pain_001_001_09.CreditTransferTransaction341();
		
		com.bornfire.jaxb.pain_001_001_09.PaymentIdentification61 pmtId=new com.bornfire.jaxb.pain_001_001_09.PaymentIdentification61();
		pmtId.setInstrId(seqUniqueID);
		pmtId.setEndToEndId(endTOEndID);
		cdtTrfTxInf.setPmtId(pmtId);
		
		com.bornfire.jaxb.pain_001_001_09.PaymentTypeInformation261 pmtTpInf=new com.bornfire.jaxb.pain_001_001_09.PaymentTypeInformation261();
		com.bornfire.jaxb.pain_001_001_09.ServiceLevel8Choice1 svclvl=new com.bornfire.jaxb.pain_001_001_09.ServiceLevel8Choice1();
		svclvl.setPrtry("0100");
		pmtTpInf.setSvcLvl(Arrays.asList(svclvl));
		com.bornfire.jaxb.pain_001_001_09.LocalInstrument2Choice1 lclInstrm=new com.bornfire.jaxb.pain_001_001_09.LocalInstrument2Choice1();
		lclInstrm.setPrtry(lclInstrData);
		pmtTpInf.setLclInstrm(lclInstrm);
		com.bornfire.jaxb.pain_001_001_09.CategoryPurpose1Choice1 ctgyPurp=new com.bornfire.jaxb.pain_001_001_09.CategoryPurpose1Choice1();
		ctgyPurp.setPrtry(ctgyPurpData);
		pmtTpInf.setCtgyPurp(ctgyPurp);
		cdtTrfTxInf.setPmtTpInf(pmtTpInf);
		
		com.bornfire.jaxb.pain_001_001_09.AmountType4Choice1 amt=new com.bornfire.jaxb.pain_001_001_09.AmountType4Choice1();
		com.bornfire.jaxb.pain_001_001_09.ActiveOrHistoricCurrencyAndAmount instdAmt=new com.bornfire.jaxb.pain_001_001_09.ActiveOrHistoricCurrencyAndAmount();
		instdAmt.setCcy(currencyCode);
		instdAmt.setValue(new BigDecimal(trAmt));
		amt.setInstdAmt(instdAmt);
		cdtTrfTxInf.setAmt(amt);
		
		com.bornfire.jaxb.pain_001_001_09.BranchAndFinancialInstitutionIdentification61 cdtrAgt=new com.bornfire.jaxb.pain_001_001_09.BranchAndFinancialInstitutionIdentification61();
		com.bornfire.jaxb.pain_001_001_09.FinancialInstitutionIdentification181 finInstnId2=new com.bornfire.jaxb.pain_001_001_09.FinancialInstitutionIdentification181();
		finInstnId2.setBICFI(CreditorAgent);
		cdtrAgt.setFinInstnId(finInstnId2);
		cdtTrfTxInf.setCdtrAgt(cdtrAgt);
		
		com.bornfire.jaxb.pain_001_001_09.CashAccount381 cdtrAgtAcct=new com.bornfire.jaxb.pain_001_001_09.CashAccount381();
		com.bornfire.jaxb.pain_001_001_09.AccountIdentification4Choice1 finInstnId4=new com.bornfire.jaxb.pain_001_001_09.AccountIdentification4Choice1();
		com.bornfire.jaxb.pain_001_001_09.GenericAccountIdentification11 other2=new com.bornfire.jaxb.pain_001_001_09.GenericAccountIdentification11();
		other2.setId(CreditorAgentAcct);
		finInstnId4.setOthr(other2);
		cdtrAgtAcct.setId(finInstnId4);
		cdtTrfTxInf.setCdtrAgtAcct(cdtrAgtAcct);
		
		com.bornfire.jaxb.pain_001_001_09.PartyIdentification1351 cdtr=new com.bornfire.jaxb.pain_001_001_09.PartyIdentification1351();
		cdtr.setNm(benName);
		//com.bornfire.jaxb.pain_001_001_09.PostalAddress241 pstlAdr1=new com.bornfire.jaxb.pain_001_001_09.PostalAddress241();
		//pstlAdr1.setAdrLine(Arrays.asList(""));
		//cdtr.setPstlAdr(pstlAdr1);
		cdtTrfTxInf.setCdtr(cdtr);
		
		com.bornfire.jaxb.pain_001_001_09.CashAccount381 cdtrAcct=new com.bornfire.jaxb.pain_001_001_09.CashAccount381();
		com.bornfire.jaxb.pain_001_001_09.AccountIdentification4Choice1 id2=new com.bornfire.jaxb.pain_001_001_09.AccountIdentification4Choice1();
		com.bornfire.jaxb.pain_001_001_09.GenericAccountIdentification11 acctId=new 	com.bornfire.jaxb.pain_001_001_09.GenericAccountIdentification11();
		acctId.setId(benAcctNumber);
		id2.setOthr(acctId);
		cdtrAcct.setId(id2);
		cdtTrfTxInf.setCdtrAcct(cdtrAcct);
		
		com.bornfire.jaxb.pain_001_001_09.RegulatoryReporting31 rgltryRptgList=new com.bornfire.jaxb.pain_001_001_09.RegulatoryReporting31();
		com.bornfire.jaxb.pain_001_001_09.StructuredRegulatoryReporting31 dtls=new com.bornfire.jaxb.pain_001_001_09.StructuredRegulatoryReporting31();
		dtls.setInf(Arrays.asList("Transfer"));
		rgltryRptgList.setDtls(dtls);
		cdtTrfTxInf.setRgltryRptg(Arrays.asList(rgltryRptgList));
		
		com.bornfire.jaxb.pain_001_001_09.RemittanceInformation161 rmtInf=new com.bornfire.jaxb.pain_001_001_09.RemittanceInformation161();
		if(trRmks.equals("null")&&trRmks.equals("")) {
			rmtInf.setUstrd(Arrays.asList("Transfer"));
		}else {
			rmtInf.setUstrd(Arrays.asList(trRmks));
		}
		
		
		cdtTrfTxInf.setRmtInf(rmtInf);
		pmtInf.setCdtTrfTxInf(Arrays.asList(cdtTrfTxInf));
		
		
		/////Customer Credit Transfer Initiation
		
		com.bornfire.jaxb.pain_001_001_09.CustomerCreditTransferInitiationV09  CstmrCdtTrfInitn=new com.bornfire.jaxb.pain_001_001_09.CustomerCreditTransferInitiationV09();
		CstmrCdtTrfInitn.setGrpHdr(grpHdr);
		CstmrCdtTrfInitn.setPmtInf(pmtInf);
		
///Document
		com.bornfire.jaxb.pain_001_001_09.Document document = new com.bornfire.jaxb.pain_001_001_09.Document();
		document.setCstmrCdtTrfInitn(CstmrCdtTrfInitn);

		JAXBContext jaxbContext;
		Marshaller jaxbMarshaller;
		StringWriter sw = null;
		try {
			jaxbContext = JAXBContext.newInstance(com.bornfire.jaxb.pain_001_001_09.Document.class);
			jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			jaxbMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
			jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			JaxbCharacterEscapeHandler jaxbCharHandler = new JaxbCharacterEscapeHandler();
			jaxbMarshaller.setProperty("com.sun.xml.bind.characterEscapeHandler", jaxbCharHandler);

			com.bornfire.jaxb.pain_001_001_09.ObjectFactory obj = new com.bornfire.jaxb.pain_001_001_09.ObjectFactory();
			JAXBElement<com.bornfire.jaxb.pain_001_001_09.Document> jaxbElement = obj.createDocument(document);
			sw = new StringWriter();
			System.out.println("okkkkkk");
			try {
				jaxbMarshaller.marshal(jaxbElement, sw);

				//OutputStreamWriter os = new OutputStreamWriter(new ByteArrayOutputStream());
				//jaxbMarshaller.marshal(jaxbElement, os);
			}catch(Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


///return document 
		return sw.toString();
	}
	
	public Object getMerchantPain_001_001_09Doc(String msgType, SendT request, String acctName, String acctNumber,
			String currencyCode ,String benName, String benAcctNumber,
			String trAmt, String trRmks, String seqUniqueID, String cimMsgID, String msgSeq, String endTOEndID,
			String msgNetMir,String cryptogram,String instgAgent,String instdAgent,String debtorAgent,String debtorAgentAcct,String CreditorAgent,String CreditorAgentAcct,
			String lclInstrData,String ctgyPurpData,String chargeBearer,CIMMerchantDirectFndRequest cimMerchantRequest,String remitInfo) {
		///Group Header
		GroupHeader851 grpHdr = new GroupHeader851();
		String msg_seq_id = seqUniqueID;
		/// Message Identification ID
		grpHdr.setMsgId(msg_seq_id);
		/// Credit Date Time
		grpHdr.setCreDtTm(listener.getxmlGregorianCalender("0"));
		/// Number Of Transaction
		grpHdr.setNbOfTxs("1");
		
		///Authorisation Cryptogram
		Authorisation1Choice1 Authstn=new Authorisation1Choice1();
		Authstn.setPrtry(cryptogram);
		List<Authorisation1Choice1> authstn=Arrays.asList(Authstn);
		grpHdr.setAuthstn(authstn);
		
		////Initiating Party(InitgPty)
		com.bornfire.jaxb.pain_001_001_09.PartyIdentification1351 initgPty=new com.bornfire.jaxb.pain_001_001_09.PartyIdentification1351();
		
		com.bornfire.jaxb.pain_001_001_09.Party38Choice1 id=new com.bornfire.jaxb.pain_001_001_09.Party38Choice1();
		com.bornfire.jaxb.pain_001_001_09.OrganisationIdentification291 orgId=new com.bornfire.jaxb.pain_001_001_09.OrganisationIdentification291();
		orgId.setAnyBIC(instgAgent);
		id.setOrgId(orgId);
		initgPty.setId(id);
		grpHdr.setInitgPty(initgPty);
		
		////Forwarding Agent
		com.bornfire.jaxb.pain_001_001_09.BranchAndFinancialInstitutionIdentification61 fwdgAgt=new com.bornfire.jaxb.pain_001_001_09.BranchAndFinancialInstitutionIdentification61();
		com.bornfire.jaxb.pain_001_001_09.FinancialInstitutionIdentification181 finInstnId=new com.bornfire.jaxb.pain_001_001_09.FinancialInstitutionIdentification181();
		finInstnId.setBICFI(instdAgent);
		fwdgAgt.setFinInstnId(finInstnId);
		grpHdr.setFwdgAgt(fwdgAgt);
		

		/////Payment Information
		com.bornfire.jaxb.pain_001_001_09.PaymentInstruction301 pmtInf=new com.bornfire.jaxb.pain_001_001_09.PaymentInstruction301();
		pmtInf.setPmtInfId(seqUniqueID);
		pmtInf.setPmtMtd(com.bornfire.jaxb.pain_001_001_09.PaymentMethod3Code1.TRF);
		com.bornfire.jaxb.pain_001_001_09.DateAndDateTime2Choice1 reqdExctnDt=new com.bornfire.jaxb.pain_001_001_09.DateAndDateTime2Choice1();
		reqdExctnDt.setDt(listener.getxmlGregorianCalender("1"));
		pmtInf.setReqdExctnDt(reqdExctnDt);
		
		com.bornfire.jaxb.pain_001_001_09.PartyIdentification1351 dbtr=new com.bornfire.jaxb.pain_001_001_09.PartyIdentification1351();
		dbtr.setNm(acctName);
		/*com.bornfire.jaxb.pain_001_001_09.PostalAddress241 pstlAdr=new com.bornfire.jaxb.pain_001_001_09.PostalAddress241();
		pstlAdr.setAdrLine(Arrays.asList(""));
		dbtr.setPstlAdr(pstlAdr);*/
		pmtInf.setDbtr(dbtr);
		
		com.bornfire.jaxb.pain_001_001_09.CashAccount381 dbtrAcct=new com.bornfire.jaxb.pain_001_001_09.CashAccount381();
		com.bornfire.jaxb.pain_001_001_09.AccountIdentification4Choice1 id1=new com.bornfire.jaxb.pain_001_001_09.AccountIdentification4Choice1();
		com.bornfire.jaxb.pain_001_001_09.GenericAccountIdentification11 othr=new com.bornfire.jaxb.pain_001_001_09.GenericAccountIdentification11();
		othr.setId(acctNumber);
		id1.setOthr(othr);
		dbtrAcct.setId(id1);
		pmtInf.setDbtrAcct(dbtrAcct);
		
		com.bornfire.jaxb.pain_001_001_09.BranchAndFinancialInstitutionIdentification61 dbtrAgt=new com.bornfire.jaxb.pain_001_001_09.BranchAndFinancialInstitutionIdentification61();
		com.bornfire.jaxb.pain_001_001_09.FinancialInstitutionIdentification181 finInstnId1=new com.bornfire.jaxb.pain_001_001_09.FinancialInstitutionIdentification181();
		finInstnId1.setBICFI(debtorAgent);
		dbtrAgt.setFinInstnId(finInstnId1);
		pmtInf.setDbtrAgt(dbtrAgt);
		
		com.bornfire.jaxb.pain_001_001_09.CashAccount381 dbtrAgtAcct=new com.bornfire.jaxb.pain_001_001_09.CashAccount381();
		com.bornfire.jaxb.pain_001_001_09.AccountIdentification4Choice1 finInstnId3=new com.bornfire.jaxb.pain_001_001_09.AccountIdentification4Choice1();
		com.bornfire.jaxb.pain_001_001_09.GenericAccountIdentification11 other1=new com.bornfire.jaxb.pain_001_001_09.GenericAccountIdentification11();
		other1.setId(debtorAgentAcct);
		finInstnId3.setOthr(other1);
		dbtrAgtAcct.setId(finInstnId3);
		pmtInf.setDbtrAgtAcct(dbtrAgtAcct);
		
		pmtInf.setChrgBr(com.bornfire.jaxb.pain_001_001_09.ChargeBearerType1Code1.SLEV);

		
		/////Credit TransferInfo
		List<com.bornfire.jaxb.pain_001_001_09.CreditTransferTransaction341> cdtTrfTxInfList=new ArrayList<>();
		com.bornfire.jaxb.pain_001_001_09.CreditTransferTransaction341 cdtTrfTxInf=new com.bornfire.jaxb.pain_001_001_09.CreditTransferTransaction341();
		
		com.bornfire.jaxb.pain_001_001_09.PaymentIdentification61 pmtId=new com.bornfire.jaxb.pain_001_001_09.PaymentIdentification61();
		pmtId.setInstrId(seqUniqueID);
		pmtId.setEndToEndId(endTOEndID);
		cdtTrfTxInf.setPmtId(pmtId);
		
		com.bornfire.jaxb.pain_001_001_09.PaymentTypeInformation261 pmtTpInf=new com.bornfire.jaxb.pain_001_001_09.PaymentTypeInformation261();
		com.bornfire.jaxb.pain_001_001_09.ServiceLevel8Choice1 svclvl=new com.bornfire.jaxb.pain_001_001_09.ServiceLevel8Choice1();
		svclvl.setPrtry("0100");
		pmtTpInf.setSvcLvl(Arrays.asList(svclvl));
		com.bornfire.jaxb.pain_001_001_09.LocalInstrument2Choice1 lclInstrm=new com.bornfire.jaxb.pain_001_001_09.LocalInstrument2Choice1();
		lclInstrm.setPrtry(lclInstrData);
		pmtTpInf.setLclInstrm(lclInstrm);
		com.bornfire.jaxb.pain_001_001_09.CategoryPurpose1Choice1 ctgyPurp=new com.bornfire.jaxb.pain_001_001_09.CategoryPurpose1Choice1();
		ctgyPurp.setPrtry(ctgyPurpData);
		pmtTpInf.setCtgyPurp(ctgyPurp);
		cdtTrfTxInf.setPmtTpInf(pmtTpInf);
		
		com.bornfire.jaxb.pain_001_001_09.AmountType4Choice1 amt=new com.bornfire.jaxb.pain_001_001_09.AmountType4Choice1();
		com.bornfire.jaxb.pain_001_001_09.ActiveOrHistoricCurrencyAndAmount instdAmt=new com.bornfire.jaxb.pain_001_001_09.ActiveOrHistoricCurrencyAndAmount();
		instdAmt.setCcy(currencyCode);
		instdAmt.setValue(new BigDecimal(trAmt));
		amt.setInstdAmt(instdAmt);
		cdtTrfTxInf.setAmt(amt);
		
		com.bornfire.jaxb.pain_001_001_09.BranchAndFinancialInstitutionIdentification61 cdtrAgt=new com.bornfire.jaxb.pain_001_001_09.BranchAndFinancialInstitutionIdentification61();
		com.bornfire.jaxb.pain_001_001_09.FinancialInstitutionIdentification181 finInstnId2=new com.bornfire.jaxb.pain_001_001_09.FinancialInstitutionIdentification181();
		finInstnId2.setBICFI(CreditorAgent);
		cdtrAgt.setFinInstnId(finInstnId2);
		cdtTrfTxInf.setCdtrAgt(cdtrAgt);
		
		com.bornfire.jaxb.pain_001_001_09.CashAccount381 cdtrAgtAcct=new com.bornfire.jaxb.pain_001_001_09.CashAccount381();
		com.bornfire.jaxb.pain_001_001_09.AccountIdentification4Choice1 finInstnId4=new com.bornfire.jaxb.pain_001_001_09.AccountIdentification4Choice1();
		com.bornfire.jaxb.pain_001_001_09.GenericAccountIdentification11 other2=new com.bornfire.jaxb.pain_001_001_09.GenericAccountIdentification11();
		other2.setId(CreditorAgentAcct);
		finInstnId4.setOthr(other2);
		cdtrAgtAcct.setId(finInstnId4);
		cdtTrfTxInf.setCdtrAgtAcct(cdtrAgtAcct);
		
		com.bornfire.jaxb.pain_001_001_09.PartyIdentification1351 cdtr=new com.bornfire.jaxb.pain_001_001_09.PartyIdentification1351();
		cdtr.setNm(benName);
		
		/// Merchant ID and MCC
		com.bornfire.jaxb.pain_001_001_09.Party38Choice1 idMer = new com.bornfire.jaxb.pain_001_001_09.Party38Choice1();
		com.bornfire.jaxb.pain_001_001_09.OrganisationIdentification291 orgIdMerID = new com.bornfire.jaxb.pain_001_001_09.OrganisationIdentification291();
		List<com.bornfire.jaxb.pain_001_001_09.GenericOrganisationIdentification11> listOthrMerID = new ArrayList<>();
		com.bornfire.jaxb.pain_001_001_09.GenericOrganisationIdentification11 othrMerID = new com.bornfire.jaxb.pain_001_001_09.GenericOrganisationIdentification11();
		othrMerID.setId(cimMerchantRequest.getMerchantAccount().getMerchantID());
		com.bornfire.jaxb.pain_001_001_09.OrganisationIdentificationSchemeName1Choice schmeNmMerID = new com.bornfire.jaxb.pain_001_001_09.OrganisationIdentificationSchemeName1Choice();
		schmeNmMerID.setPrtry("MerchantID");
		othrMerID.setSchmeNm(schmeNmMerID);
		listOthrMerID.add(othrMerID);

		/// MCC
		com.bornfire.jaxb.pain_001_001_09.GenericOrganisationIdentification11 othrMCCID = new com.bornfire.jaxb.pain_001_001_09.GenericOrganisationIdentification11();
		othrMCCID.setId(cimMerchantRequest.getMerchantAccount().getMCC());
		com.bornfire.jaxb.pain_001_001_09.OrganisationIdentificationSchemeName1Choice schmeNmMCCID = new com.bornfire.jaxb.pain_001_001_09.OrganisationIdentificationSchemeName1Choice();
		schmeNmMCCID.setPrtry("MCC");
		othrMCCID.setSchmeNm(schmeNmMCCID);
		listOthrMerID.add(othrMCCID);

		orgIdMerID.setOthr(listOthrMerID);
		idMer.setOrgId(orgIdMerID);
		cdtr.setId(idMer);
		
		/// Postal Address
		// Country
		com.bornfire.jaxb.pain_001_001_09.PostalAddress241 pstlAdrMer = new com.bornfire.jaxb.pain_001_001_09.PostalAddress241();
		pstlAdrMer.setCtry(cimMerchantRequest.getMerchantAccount().getCountryCode());
		pstlAdrMer.setTwnNm(cimMerchantRequest.getMerchantAccount().getCity());
		pstlAdrMer.setAdrLine(Arrays.asList(cimMerchantRequest.getMerchantAccount().getCity()));

		if (!String.valueOf(cimMerchantRequest.getMerchantAccount().getPostalCode()).equals("null")) {
			if (!String.valueOf(cimMerchantRequest.getMerchantAccount().getPostalCode()).equals("")) {
				pstlAdrMer.setPstCd(cimMerchantRequest.getMerchantAccount().getPostalCode());
			}
		}
		cdtr.setPstlAdr(pstlAdrMer);

		//com.bornfire.jaxb.pain_001_001_09.PostalAddress241 pstlAdr1=new com.bornfire.jaxb.pain_001_001_09.PostalAddress241();
		//pstlAdr1.setAdrLine(Arrays.asList(""));
		//cdtr.setPstlAdr(pstlAdr1);
		cdtTrfTxInf.setCdtr(cdtr);
		
		com.bornfire.jaxb.pain_001_001_09.CashAccount381 cdtrAcct=new com.bornfire.jaxb.pain_001_001_09.CashAccount381();
		com.bornfire.jaxb.pain_001_001_09.AccountIdentification4Choice1 id2=new com.bornfire.jaxb.pain_001_001_09.AccountIdentification4Choice1();
		com.bornfire.jaxb.pain_001_001_09.GenericAccountIdentification11 acctId=new 	com.bornfire.jaxb.pain_001_001_09.GenericAccountIdentification11();
		acctId.setId(benAcctNumber);
		id2.setOthr(acctId);
		cdtrAcct.setId(id2);
		cdtTrfTxInf.setCdtrAcct(cdtrAcct);
		
		com.bornfire.jaxb.pain_001_001_09.RegulatoryReporting31 rgltryRptgList=new com.bornfire.jaxb.pain_001_001_09.RegulatoryReporting31();
		com.bornfire.jaxb.pain_001_001_09.StructuredRegulatoryReporting31 dtls=new com.bornfire.jaxb.pain_001_001_09.StructuredRegulatoryReporting31();
		dtls.setInf(Arrays.asList("Transfer"));
		rgltryRptgList.setDtls(dtls);
		//cdtTrfTxInf.setRgltryRptg(Arrays.asList(rgltryRptgList));
		
		com.bornfire.jaxb.pain_001_001_09.RemittanceInformation161 rmtInf=new com.bornfire.jaxb.pain_001_001_09.RemittanceInformation161();
		rmtInf.setUstrd(Arrays.asList(remitInfo));
		cdtTrfTxInf.setRmtInf(rmtInf);
		pmtInf.setCdtTrfTxInf(Arrays.asList(cdtTrfTxInf));
		
		
		/////Customer Credit Transfer Initiation
		
		com.bornfire.jaxb.pain_001_001_09.CustomerCreditTransferInitiationV09  CstmrCdtTrfInitn=new com.bornfire.jaxb.pain_001_001_09.CustomerCreditTransferInitiationV09();
		CstmrCdtTrfInitn.setGrpHdr(grpHdr);
		CstmrCdtTrfInitn.setPmtInf(pmtInf);
		
///Document
		com.bornfire.jaxb.pain_001_001_09.Document document = new com.bornfire.jaxb.pain_001_001_09.Document();
		document.setCstmrCdtTrfInitn(CstmrCdtTrfInitn);

		JAXBContext jaxbContext;
		Marshaller jaxbMarshaller;
		StringWriter sw = null;
		try {
			jaxbContext = JAXBContext.newInstance(com.bornfire.jaxb.pain_001_001_09.Document.class);
			jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			jaxbMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
			jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			JaxbCharacterEscapeHandler jaxbCharHandler = new JaxbCharacterEscapeHandler();
			jaxbMarshaller.setProperty("com.sun.xml.bind.characterEscapeHandler", jaxbCharHandler);

			com.bornfire.jaxb.pain_001_001_09.ObjectFactory obj = new com.bornfire.jaxb.pain_001_001_09.ObjectFactory();
			JAXBElement<com.bornfire.jaxb.pain_001_001_09.Document> jaxbElement = obj.createDocument(document);
			sw = new StringWriter();
			System.out.println("okkkkkk");
			try {
				jaxbMarshaller.marshal(jaxbElement, sw);

				//OutputStreamWriter os = new OutputStreamWriter(new ByteArrayOutputStream());
				//jaxbMarshaller.marshal(jaxbElement, os);
			}catch(Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


///return document 
		return sw.toString();
	}


	public com.bornfire.jaxb.camt_053_001_08.Document getCamt053_001_08UnMarshalDoc(SendT request) {
		String block4 = request.getMessage().getBlock4();
		final int start = block4.indexOf("<Document");
		final int end = block4.indexOf("</Document>");
		
		InputStream stream = null;
		JAXBContext jaxBContext;
		JAXBElement<com.bornfire.jaxb.camt_053_001_08.Document> jaxbElement = null;

		try {
			stream = new ByteArrayInputStream(block4.substring(start, end + 11).getBytes("UTF-8"));
			jaxBContext = JAXBContext.newInstance(com.bornfire.jaxb.camt_053_001_08.Document.class);
			Unmarshaller unMarshaller = jaxBContext.createUnmarshaller();
			XMLInputFactory factory = XMLInputFactory.newInstance();
			XMLEventReader xmlEventReader = factory.createXMLEventReader(stream);
			jaxbElement = unMarshaller.unmarshal(xmlEventReader, com.bornfire.jaxb.camt_053_001_08.Document.class);

		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		com.bornfire.jaxb.camt_053_001_08.Document document = jaxbElement.getValue();
		//System.out.println(document.getRtrLmt().getRptOrErr().getBizRpt().getCurLmt().get(0).getLmtOrErr().getBizErr().getDesc());
		return document;

	}

	public com.bornfire.jaxb.camt_019_001_07.Document getCamt019_001_07UnMarshalDoc(SendT request) {
		String block4 = request.getMessage().getBlock4();
		final int start = block4.indexOf("<Document");
		final int end = block4.indexOf("</Document>");
		
		InputStream stream = null;
		JAXBContext jaxBContext;
		JAXBElement<com.bornfire.jaxb.camt_019_001_07.Document> jaxbElement = null;

		try {
			stream = new ByteArrayInputStream(block4.substring(start, end + 11).getBytes("UTF-8"));
			jaxBContext = JAXBContext.newInstance(com.bornfire.jaxb.camt_019_001_07.Document.class);
			Unmarshaller unMarshaller = jaxBContext.createUnmarshaller();
			XMLInputFactory factory = XMLInputFactory.newInstance();
			XMLEventReader xmlEventReader = factory.createXMLEventReader(stream);
			jaxbElement = unMarshaller.unmarshal(xmlEventReader, com.bornfire.jaxb.camt_019_001_07.Document.class);

		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		com.bornfire.jaxb.camt_019_001_07.Document document = jaxbElement.getValue();
		//System.out.println(document.getRtrLmt().getRptOrErr().getBizRpt().getCurLmt().get(0).getLmtOrErr().getBizErr().getDesc());
		return document;
	}
	

	

	/*public Object getPacsBulkCredit_008_001_01Doc(String msgId,List<CreditTransferTransaction> creditTranList,String totAmt,String msgSeq) {
		///Group Header
				GroupHeader931 grpHdr = new GroupHeader931();
				String msg_seq_id = msgId;
				/// Message Identification ID
				grpHdr.setMsgId(msg_seq_id);
				/// Credit Date Time
				grpHdr.setCreDtTm(listener.getxmlGregorianCalender("0"));
				/// Number Of Transaction
				grpHdr.setNbOfTxs("1");
				/// Total Inter Bank Settlement Amount
				ActiveCurrencyAndAmount activeCurrencyAndAmount = new ActiveCurrencyAndAmount();
				activeCurrencyAndAmount.setCcy("MUR");/// Currency
				activeCurrencyAndAmount.setValue(new BigDecimal(totAmt));/// Amount
				grpHdr.setTtlIntrBkSttlmAmt(activeCurrencyAndAmount);
				/// Inter Bank settlement Date
				grpHdr.setIntrBkSttlmDt(listener.getxmlGregorianCalender("1"));
				/// Settlement Instruction
				SettlementInstruction71 sttlmInf = new SettlementInstruction71();
				sttlmInf.setSttlmMtd(SettlementMethod1Code1.CLRG);/// Settlement method (CLRG)
				grpHdr.setSttlmInf(sttlmInf);

		///// creditTransaction Information
				List<CreditTransferTransaction391> cdtTrfTxInf = new ArrayList<CreditTransferTransaction391>();
				for(int i=0;i<creditTranList.size();i++) {
					CreditTransferTransaction391 creditTransferTransaction391 = new CreditTransferTransaction391();
					/// Payment Identification
					PaymentIdentification71 pmtId = new PaymentIdentification71();
					pmtId.setInstrId(creditTranList.get(i).getInstrID());/// Instruction ID
					//String EntToEndID=env.getProperty("ipsx.bicfi")+new SimpleDateFormat("YYYYMMdd").format(new Date())+msgSeq;
					pmtId.setEndToEndId(creditTranList.get(i).getEndToEndID());/// End to End ID
					pmtId.setTxId(creditTranList.get(i).getTxID());/// Transaction ID
					creditTransferTransaction391.setPmtId(pmtId);
					/// Payment Type Information
					PaymentTypeInformation281 pmtTpInf = new PaymentTypeInformation281();
					pmtTpInf.setClrChanl(ClearingChannel2Code1.RTGS);/// Clearing Channel like(RTGS,RTNS,MPNS)
					CategoryPurpose1Choice1 ctgyPurp = new CategoryPurpose1Choice1();
					ctgyPurp.setPrtry("100");
					pmtTpInf.setCtgyPurp(ctgyPurp);/// Category Purpose
					LocalInstrument2Choice1 lclInstrm = new LocalInstrument2Choice1();
					lclInstrm.setPrtry("CSDC");
					pmtTpInf.setLclInstrm(lclInstrm);/// Local Instrument
					List<ServiceLevel8Choice1> svcLvl = new ArrayList<ServiceLevel8Choice1>();
					ServiceLevel8Choice1 serlc = new ServiceLevel8Choice1();
					serlc.setPrtry("0100");
					svcLvl.add(serlc);
					pmtTpInf.setSvcLvl(svcLvl);/// Service Level
					creditTransferTransaction391.setPmtTpInf(pmtTpInf);
					/// Inter Bank Settlement Currency And Amount
					ActiveCurrencyAndAmount intrBkSttlmAmt = new ActiveCurrencyAndAmount();
					intrBkSttlmAmt.setCcy(creditTranList.get(i).getTrCurrency());/// Currency
					intrBkSttlmAmt.setValue(new BigDecimal(creditTranList.get(i).getTrAmt()));/// Amount
					creditTransferTransaction391.setIntrBkSttlmAmt(intrBkSttlmAmt);
					/// Charge Bearer
					creditTransferTransaction391.setChrgBr(ChargeBearerType1Code.SLEV);
					/// Financial Institution Identification(Instructing Agent)
					FinancialInstitutionIdentification181 fin = new FinancialInstitutionIdentification181();
					fin.setBICFI(env.getProperty("ipsx.bicfi"));
					BranchAndFinancialInstitutionIdentification61 instgAgt = new BranchAndFinancialInstitutionIdentification61();
					instgAgt.setFinInstnId(fin);
					creditTransferTransaction391.setInstgAgt(instgAgt);
					/// Financial Institution Identification(Instructed Agent)
					FinancialInstitutionIdentification181 fin1 = new FinancialInstitutionIdentification181();
					// fin1.setBICFI("TSTAMUMU");
					fin1.setBICFI(creditTranList.get(i).getCredrAgent());
					BranchAndFinancialInstitutionIdentification61 instdAgt = new BranchAndFinancialInstitutionIdentification61();
					instdAgt.setFinInstnId(fin1);
					creditTransferTransaction391.setInstdAgt(instdAgt);
					/// Debtor name
					PartyIdentification1351 dbtr = new PartyIdentification1351();
					dbtr.setNm(creditTranList.get(i).getDebrName());
					creditTransferTransaction391.setDbtr(dbtr);
					/*
					 * ///Debtor Account Number CashAccount381 dbtrAcct = new CashAccount381();
					 * AccountIdentification4Choice1 acc1 = new AccountIdentification4Choice1();
					 * GenericAccountIdentification11 id = new GenericAccountIdentification11();
					 * id.setId((mcCreditTransferRequest.getFrAccount().getAcctNumber()));
					 * acc1.setOthr(id); dbtrAcct.setId(acc1);
					 * creditTransferTransaction391.setDbtrAcct(dbtrAcct);
					 */
					/// Debtor Agent
					/*BranchAndFinancialInstitutionIdentification61 dbtrAgt = new BranchAndFinancialInstitutionIdentification61();
					FinancialInstitutionIdentification181 fin2 = new FinancialInstitutionIdentification181();
					fin2.setBICFI(env.getProperty("ipsx.dbtragt"));
					dbtrAgt.setFinInstnId(fin2);
					creditTransferTransaction391.setDbtrAgt(dbtrAgt);
					/// Debtor Agent Account
					CashAccount381 dbtrAgtAcct = new CashAccount381();
					AccountIdentification4Choice1 id2 = new AccountIdentification4Choice1();
					GenericAccountIdentification11 gen1 = new GenericAccountIdentification11();
					gen1.setId(env.getProperty("ipsx.dbtragtacct"));
					id2.setOthr(gen1);
					dbtrAgtAcct.setId(id2);
					creditTransferTransaction391.setDbtrAgtAcct(dbtrAgtAcct);
					/// Creditor Agent
					BranchAndFinancialInstitutionIdentification61 cdtrAgt = new BranchAndFinancialInstitutionIdentification61();
					FinancialInstitutionIdentification181 fin3 = new FinancialInstitutionIdentification181();
					// fin3.setBICFI("TSTAMUMU");
					fin3.setBICFI(creditTranList.get(i).getDebrAgent());
					cdtrAgt.setFinInstnId(fin3);
					creditTransferTransaction391.setCdtrAgt(cdtrAgt);
					/// Creditor Agent Account
					CashAccount381 cdtrAgtAcct = new CashAccount381();
					AccountIdentification4Choice1 acc3 = new AccountIdentification4Choice1();
					GenericAccountIdentification11 gen3 = new GenericAccountIdentification11();
					// gen3.setId("TSTBNRT");
					gen3.setId(creditTranList.get(i).getDebrAgentAccount());
					acc3.setOthr(gen3);
					cdtrAgtAcct.setId(acc3);
					creditTransferTransaction391.setCdtrAgtAcct(cdtrAgtAcct);
					/// Creditor Name
					PartyIdentification1351 cdtr = new PartyIdentification1351();
					cdtr.setNm(creditTranList.get(i).getCredrName());
					creditTransferTransaction391.setCdtr(cdtr);
					/// Creditor Account Number
					CashAccount381 cdtrAcct = new CashAccount381();
					AccountIdentification4Choice1 id4 = new AccountIdentification4Choice1();
					GenericAccountIdentification11 gen4 = new GenericAccountIdentification11();
					gen4.setId(creditTranList.get(i).getCredrAccountNumber());
					id4.setOthr(gen4);
					cdtrAcct.setId(id4);
					creditTransferTransaction391.setCdtrAcct(cdtrAcct);
					cdtTrfTxInf.add(creditTransferTransaction391);
				}
				

		///Financial Customer Credit Transfer		
				FIToFICustomerCreditTransferV08 fiToFICstmrCdtTrf = new FIToFICustomerCreditTransferV08();
				fiToFICstmrCdtTrf.setGrpHdr(grpHdr);
				fiToFICstmrCdtTrf.setCdtTrfTxInf(cdtTrfTxInf);

		///Document
				Document document = new Document();
				document.setFIToFICstmrCdtTrf(fiToFICstmrCdtTrf);

		///Convert Document XMl element to String
				JAXBContext jaxbContext;
				Marshaller jaxbMarshaller;
				StringWriter sw = null;
				try {
					jaxbContext = JAXBContext.newInstance(Document.class);
					jaxbMarshaller = jaxbContext.createMarshaller();
					jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
					jaxbMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
					jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
					JaxbCharacterEscapeHandler jaxbCharHandler = new JaxbCharacterEscapeHandler();
					jaxbMarshaller.setProperty("com.sun.xml.bind.characterEscapeHandler", jaxbCharHandler);

					com.bornfire.jaxb.pacs_008_001_08.ObjectFactory obj = new com.bornfire.jaxb.pacs_008_001_08.ObjectFactory();
					JAXBElement<Document> jaxbElement = obj.createDocument(document);
					sw = new StringWriter();
					jaxbMarshaller.marshal(jaxbElement, sw);
				} catch (Exception e) {
					e.printStackTrace();
				}

		///return document 
				return sw.toString();


	}*/
	

	public String getPacs_008_001_01UnMarshalDocXML(SendT request) {
		String block4 = request.getMessage().getBlock4();
		final int start = block4.indexOf("<DataPDU");
		final int end = block4.indexOf("</DataPDU>");

		String XMLData=block4.substring(start, end + 10);

		return XMLData;
	}

	public String getPacs_002_001_10UnMarshalDocXML(SendT request){
		String block4 = request.getMessage().getBlock4();
		final int start = block4.indexOf("<DataPDU");
		final int end = block4.indexOf("</DataPDU>");

		String XMLData=block4.substring(start, end + 10);

		return XMLData;	
		
	}

	public String getAdmi_002_001_01UnMarshalDocXML(SendT request) {
		String block4 = request.getMessage().getBlock4();
		final int start = block4.indexOf("<DataPDU");
		final int end = block4.indexOf("</DataPDU>");

		String XMLData=block4.substring(start, end + 10);

		return XMLData;	
	}

	public String getCamt052_001_08UnMarshalDocXML(SendT request) {
		String block4 = request.getMessage().getBlock4();
		final int start = block4.indexOf("<DataPDU");
		final int end = block4.indexOf("</DataPDU>");

		String XMLData=block4.substring(start, end + 10);

		return XMLData;	
		
	}

	public String getPain001_001_09UnMarshalDocXML(SendT request) {
		String block4 = request.getMessage().getBlock4();
		final int start = block4.indexOf("<DataPDU");
		final int end = block4.indexOf("</DataPDU>");

		String XMLData=block4.substring(start, end + 10);

		return XMLData;	
	}

	
	public String getPain_002_001_10UnMarshalDocXML(SendT request) {
		String block4 = request.getMessage().getBlock4();
		final int start = block4.indexOf("<DataPDU");
		final int end = block4.indexOf("</DataPDU>");

		String XMLData=block4.substring(start, end + 10);

		return XMLData;	
		
	}

	
	public String getCamt025_001_05UnMarshalDocXML(SendT request) {
		String block4 = request.getMessage().getBlock4();
		final int start = block4.indexOf("<DataPDU");
		final int end = block4.indexOf("</DataPDU>");

		String XMLData=block4.substring(start, end + 10);

		return XMLData;	
		
	}

	
	
	public String getCamt010_001_07UnMarshalDocXML(SendT request) {
		String block4 = request.getMessage().getBlock4();
		final int start = block4.indexOf("<DataPDU");
		final int end = block4.indexOf("</DataPDU>");

		String XMLData=block4.substring(start, end + 10);

		return XMLData;	
		
	}
	
	
public String getCamt053_001_08UnMarshalDocXML(SendT request) {
	String block4 = request.getMessage().getBlock4();
	final int start = block4.indexOf("<DataPDU");
	final int end = block4.indexOf("</DataPDU>");

	String XMLData=block4.substring(start, end + 10);

	return XMLData;	
	
	
	}

	public String getCamt019_001_07UnMarshalDocXML(SendT request) {
		String block4 = request.getMessage().getBlock4();
		final int start = block4.indexOf("<DataPDU");
		final int end = block4.indexOf("</DataPDU>");

		String XMLData=block4.substring(start, end + 10);

		return XMLData;	
		
	}


}
