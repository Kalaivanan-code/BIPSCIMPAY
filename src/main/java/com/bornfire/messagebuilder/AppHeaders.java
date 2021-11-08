package com.bornfire.messagebuilder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import com.bornfire.exception.JaxbCharacterEscapeHandler;
import com.bornfire.jaxb.head_001_001_01.BranchAndFinancialInstitutionIdentification51;
import com.bornfire.jaxb.head_001_001_01.BusinessApplicationHeaderV01;
import com.bornfire.jaxb.head_001_001_01.ClearingSystemMemberIdentification21;
import com.bornfire.jaxb.head_001_001_01.FinancialInstitutionIdentification81;
import com.bornfire.jaxb.head_001_001_01.ObjectFactory;
import com.bornfire.jaxb.head_001_001_01.Party9Choice1;
import com.bornfire.jaxb.pacs_002_001_010.FIToFIPaymentStatusReportV10;
import com.bornfire.jaxb.pacs_008_001_08.Document;
import com.bornfire.jaxb.wsdl.SendT;

public class AppHeaders {

	@Autowired
	Environment env;

	@Autowired
	Listener listener;

	public String getAppHeader(String msgType,String msgSeq) {

		BusinessApplicationHeaderV01 appHeader = new BusinessApplicationHeaderV01();

		FinancialInstitutionIdentification81 finInstnId = new FinancialInstitutionIdentification81();
		finInstnId.setBICFI(env.getProperty("ipsx.bicfi"));
		//ClearingSystemMemberIdentification21 clrSysMmbId = new ClearingSystemMemberIdentification21();
		//clrSysMmbId.setMmbId(env.getProperty("ipsx.user"));
		//finInstnId.setClrSysMmbId(clrSysMmbId);
		BranchAndFinancialInstitutionIdentification51 fIId = new BranchAndFinancialInstitutionIdentification51();
		fIId.setFinInstnId(finInstnId);
		Party9Choice1 fr = new Party9Choice1();
		fr.setFIId(fIId);

		FinancialInstitutionIdentification81 finInstnIdTo = new FinancialInstitutionIdentification81();
		finInstnIdTo.setBICFI(env.getProperty("ipsx.msgReceiverbicfi"));
		BranchAndFinancialInstitutionIdentification51 fIIdTo = new BranchAndFinancialInstitutionIdentification51();
		fIIdTo.setFinInstnId(finInstnIdTo);
		Party9Choice1 to = new Party9Choice1();
		to.setFIId(fIIdTo);

		appHeader.setTo(to);
		appHeader.setFr(fr);
		appHeader.setBizMsgIdr(
				new SimpleDateFormat("yyMMdd").format(new Date()) + env.getProperty("ipsx.user") + "0001"+msgSeq);
		appHeader.setMsgDefIdr(msgType);
		appHeader.setBizSvc("ACH");

		appHeader.setCreDt(listener.getxmlGregorianCalender("0"));

		JAXBContext jaxbContext;
		Marshaller jaxbMarshaller;
		StringWriter sw = null;
		try {
			jaxbContext = JAXBContext.newInstance(BusinessApplicationHeaderV01.class);
			jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			jaxbMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
			jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			JaxbCharacterEscapeHandler jaxbCharHandler = new JaxbCharacterEscapeHandler();
			jaxbMarshaller.setProperty("com.sun.xml.bind.characterEscapeHandler", jaxbCharHandler);

			ObjectFactory obj = new ObjectFactory();
			JAXBElement<BusinessApplicationHeaderV01> jaxbElement = obj.createAppHdr(appHeader);
			sw = new StringWriter();
			jaxbMarshaller.marshal(jaxbElement, sw);
			//OutputStreamWriter os = new OutputStreamWriter(new ByteArrayOutputStream());
			//jaxbMarshaller.marshal(jaxbElement, os);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sw.toString();
	}
	
	public String getAppHeader002(SendT request,String msgType,String msgSeq) {
		
		BusinessApplicationHeaderV01 appHeaderPacs008 = getAppHeaderUnMarshalDoc(request);
		
		BusinessApplicationHeaderV01 appHeader = new BusinessApplicationHeaderV01();

		FinancialInstitutionIdentification81 finInstnId = new FinancialInstitutionIdentification81();
		finInstnId.setBICFI(env.getProperty("ipsx.bicfi"));
		//ClearingSystemMemberIdentification21 clrSysMmbId = new ClearingSystemMemberIdentification21();
		//clrSysMmbId.setMmbId(env.getProperty("ipsx.user"));
		//finInstnId.setClrSysMmbId(clrSysMmbId);
		BranchAndFinancialInstitutionIdentification51 fIId = new BranchAndFinancialInstitutionIdentification51();
		fIId.setFinInstnId(finInstnId);
		Party9Choice1 fr = new Party9Choice1();
		fr.setFIId(fIId);

		FinancialInstitutionIdentification81 finInstnIdTo = new FinancialInstitutionIdentification81();
		finInstnIdTo.setBICFI(appHeaderPacs008.getFr().getFIId().getFinInstnId().getClrSysMmbId().getMmbId());
		BranchAndFinancialInstitutionIdentification51 fIIdTo = new BranchAndFinancialInstitutionIdentification51();
		fIIdTo.setFinInstnId(finInstnIdTo);
		Party9Choice1 to = new Party9Choice1();
		to.setFIId(fIIdTo);

		appHeader.setTo(to);
		appHeader.setFr(fr);
		appHeader.setBizMsgIdr(
				new SimpleDateFormat("yyMMdd").format(new Date()) + env.getProperty("ipsx.user") + "0001"+msgSeq);
		appHeader.setMsgDefIdr(msgType);
		appHeader.setBizSvc("ACH");

		appHeader.setCreDt(listener.getxmlGregorianCalender("0"));

		JAXBContext jaxbContext;
		Marshaller jaxbMarshaller;
		StringWriter sw = null;
		try {
			jaxbContext = JAXBContext.newInstance(BusinessApplicationHeaderV01.class);
			jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			jaxbMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
			jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			JaxbCharacterEscapeHandler jaxbCharHandler = new JaxbCharacterEscapeHandler();
			jaxbMarshaller.setProperty("com.sun.xml.bind.characterEscapeHandler", jaxbCharHandler);

			ObjectFactory obj = new ObjectFactory();
			JAXBElement<BusinessApplicationHeaderV01> jaxbElement = obj.createAppHdr(appHeader);
			sw = new StringWriter();
			jaxbMarshaller.marshal(jaxbElement, sw);
			//OutputStreamWriter os = new OutputStreamWriter(new ByteArrayOutputStream());
			//jaxbMarshaller.marshal(jaxbElement, os);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sw.toString();
	}
	
	

	public Object getRTPAppHeader008(String msgType, SendT request,String msgSeq) {
		BusinessApplicationHeaderV01 appHeaderPain001 = getRTPAppHeaderUnMarshalDoc(request);
		BusinessApplicationHeaderV01 appHeader = new BusinessApplicationHeaderV01();

		FinancialInstitutionIdentification81 finInstnId = new FinancialInstitutionIdentification81();
		//finInstnId.setBICFI(env.getProperty("ipsx.bicfi"));
		ClearingSystemMemberIdentification21 clrSysMmbId = new ClearingSystemMemberIdentification21();
		clrSysMmbId.setMmbId(env.getProperty("ipsx.user"));
		//finInstnId.setClrSysMmbId(clrSysMmbId);
		finInstnId.setBICFI(env.getProperty("ipsx.bicfi"));
		BranchAndFinancialInstitutionIdentification51 fIId = new BranchAndFinancialInstitutionIdentification51();
		fIId.setFinInstnId(finInstnId);
		Party9Choice1 fr = new Party9Choice1();
		fr.setFIId(fIId);

		FinancialInstitutionIdentification81 finInstnIdTo = new FinancialInstitutionIdentification81();
		finInstnIdTo.setBICFI(appHeaderPain001.getFr().getFIId().getFinInstnId().getClrSysMmbId().getMmbId());
		BranchAndFinancialInstitutionIdentification51 fIIdTo = new BranchAndFinancialInstitutionIdentification51();
		fIIdTo.setFinInstnId(finInstnIdTo);
		Party9Choice1 to = new Party9Choice1();
		to.setFIId(fIIdTo);

		appHeader.setTo(to);
		appHeader.setFr(fr);
		appHeader.setBizMsgIdr(
				new SimpleDateFormat("yyMMdd").format(new Date()) + env.getProperty("ipsx.user") + "0001"+msgSeq);
		appHeader.setMsgDefIdr(msgType);
		appHeader.setBizSvc("ACH");

		appHeader.setCreDt(listener.getxmlGregorianCalender("0"));

		JAXBContext jaxbContext;
		Marshaller jaxbMarshaller;
		StringWriter sw = null;
		try {
			jaxbContext = JAXBContext.newInstance(BusinessApplicationHeaderV01.class);
			jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			jaxbMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
			jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			JaxbCharacterEscapeHandler jaxbCharHandler = new JaxbCharacterEscapeHandler();
			jaxbMarshaller.setProperty("com.sun.xml.bind.characterEscapeHandler", jaxbCharHandler);

			ObjectFactory obj = new ObjectFactory();
			JAXBElement<BusinessApplicationHeaderV01> jaxbElement = obj.createAppHdr(appHeader);
			sw = new StringWriter();
			jaxbMarshaller.marshal(jaxbElement, sw);
			//OutputStreamWriter os = new OutputStreamWriter(new ByteArrayOutputStream());
			//jaxbMarshaller.marshal(jaxbElement, os);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sw.toString();
	}
	
	public String getAppHeaderPain002(SendT request, String msgType, String bobMsgID,String msgSeq) {
		BusinessApplicationHeaderV01 appHeaderPain001 = getRTPAppHeaderUnMarshalDoc(request);
		BusinessApplicationHeaderV01 appHeader = new BusinessApplicationHeaderV01();

		FinancialInstitutionIdentification81 finInstnId = new FinancialInstitutionIdentification81();
		finInstnId.setBICFI(env.getProperty("ipsx.bicfi"));
		//ClearingSystemMemberIdentification21 clrSysMmbId = new ClearingSystemMemberIdentification21();
		//clrSysMmbId.setMmbId(env.getProperty("ipsx.user"));
		//finInstnId.setClrSysMmbId(clrSysMmbId);
		BranchAndFinancialInstitutionIdentification51 fIId = new BranchAndFinancialInstitutionIdentification51();
		fIId.setFinInstnId(finInstnId);
		Party9Choice1 fr = new Party9Choice1();
		fr.setFIId(fIId);

		FinancialInstitutionIdentification81 finInstnIdTo = new FinancialInstitutionIdentification81();
		finInstnIdTo.setBICFI(appHeaderPain001.getFr().getFIId().getFinInstnId().getClrSysMmbId().getMmbId());
		BranchAndFinancialInstitutionIdentification51 fIIdTo = new BranchAndFinancialInstitutionIdentification51();
		fIIdTo.setFinInstnId(finInstnIdTo);
		Party9Choice1 to = new Party9Choice1();
		to.setFIId(fIIdTo);

		appHeader.setTo(to);
		appHeader.setFr(fr);
		appHeader.setBizMsgIdr(
				new SimpleDateFormat("yyMMdd").format(new Date()) + env.getProperty("ipsx.user") + "0001"+msgSeq);
		appHeader.setMsgDefIdr(msgType);
		appHeader.setBizSvc("ACH");

		appHeader.setCreDt(listener.getxmlGregorianCalender("0"));

		JAXBContext jaxbContext;
		Marshaller jaxbMarshaller;
		StringWriter sw = null;
		try {
			jaxbContext = JAXBContext.newInstance(BusinessApplicationHeaderV01.class);
			jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			jaxbMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
			jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			JaxbCharacterEscapeHandler jaxbCharHandler = new JaxbCharacterEscapeHandler();
			jaxbMarshaller.setProperty("com.sun.xml.bind.characterEscapeHandler", jaxbCharHandler);

			ObjectFactory obj = new ObjectFactory();
			JAXBElement<BusinessApplicationHeaderV01> jaxbElement = obj.createAppHdr(appHeader);
			sw = new StringWriter();
			jaxbMarshaller.marshal(jaxbElement, sw);
			//OutputStreamWriter os = new OutputStreamWriter(new ByteArrayOutputStream());
			//jaxbMarshaller.marshal(jaxbElement, os);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sw.toString();
	}
	
	public BusinessApplicationHeaderV01 getAppHeaderUnMarshalDoc(SendT request) {
		String block4=request.getMessage().getBlock4();
		final int start = block4.indexOf("<AppHdr");
		final int end = block4.indexOf("</AppHdr>");

		InputStream stream=null;
		JAXBContext jaxBContext;
		JAXBElement<BusinessApplicationHeaderV01> jaxbElement = null;
		try {
			stream=new ByteArrayInputStream(block4.substring(start, end + 9).getBytes("UTF-8"));
			jaxBContext = JAXBContext.newInstance(BusinessApplicationHeaderV01.class);
			Unmarshaller unMarshaller = jaxBContext.createUnmarshaller();
			XMLInputFactory factory=XMLInputFactory.newInstance();
			XMLEventReader xmlEventReader=factory.createXMLEventReader(stream);
			jaxbElement = unMarshaller.unmarshal(xmlEventReader,BusinessApplicationHeaderV01.class);
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
				e.printStackTrace();
		}
		BusinessApplicationHeaderV01 appHeader = jaxbElement.getValue();

		return appHeader;
		
	}
	public BusinessApplicationHeaderV01 getRTPAppHeaderUnMarshalDoc(SendT request) {
		String block4=request.getMessage().getBlock4();
		final int start = block4.indexOf("<AppHdr");
		final int end = block4.indexOf("</AppHdr>");

		InputStream stream=null;
		JAXBContext jaxBContext;
		JAXBElement<BusinessApplicationHeaderV01> jaxbElement = null;
		try {
			stream=new ByteArrayInputStream(block4.substring(start, end + 9).getBytes("UTF-8"));
			jaxBContext = JAXBContext.newInstance(BusinessApplicationHeaderV01.class);
			Unmarshaller unMarshaller = jaxBContext.createUnmarshaller();
			XMLInputFactory factory=XMLInputFactory.newInstance();
			XMLEventReader xmlEventReader=factory.createXMLEventReader(stream);
			jaxbElement = unMarshaller.unmarshal(xmlEventReader,BusinessApplicationHeaderV01.class);
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
				e.printStackTrace();
		}
		BusinessApplicationHeaderV01 appHeader = jaxbElement.getValue();

		return appHeader;
		
	}

	public Object getAppHeaderCamt011(String msgType, String bobMsgID,String msgSeq) {
		BusinessApplicationHeaderV01 appHeader = new BusinessApplicationHeaderV01();

		FinancialInstitutionIdentification81 finInstnId = new FinancialInstitutionIdentification81();
		finInstnId.setBICFI(env.getProperty("ipsx.bicfi"));
		//ClearingSystemMemberIdentification21 clrSysMmbId = new ClearingSystemMemberIdentification21();
		//clrSysMmbId.setMmbId(env.getProperty("ipsx.user"));
		//finInstnId.setClrSysMmbId(clrSysMmbId);
		BranchAndFinancialInstitutionIdentification51 fIId = new BranchAndFinancialInstitutionIdentification51();
		fIId.setFinInstnId(finInstnId);
		Party9Choice1 fr = new Party9Choice1();
		fr.setFIId(fIId);

		FinancialInstitutionIdentification81 finInstnIdTo = new FinancialInstitutionIdentification81();
		finInstnIdTo.setBICFI(env.getProperty("ipsx.msgReceiverbicfi"));
		BranchAndFinancialInstitutionIdentification51 fIIdTo = new BranchAndFinancialInstitutionIdentification51();
		fIIdTo.setFinInstnId(finInstnIdTo);
		Party9Choice1 to = new Party9Choice1();
		to.setFIId(fIIdTo);

		appHeader.setTo(to);
		appHeader.setFr(fr);
		appHeader.setBizMsgIdr(
				new SimpleDateFormat("yyMMdd").format(new Date()) + env.getProperty("ipsx.user") + "0001"+msgSeq);
		appHeader.setMsgDefIdr(msgType);
		appHeader.setBizSvc("ACH");

		appHeader.setCreDt(listener.getxmlGregorianCalender("0"));

		JAXBContext jaxbContext;
		Marshaller jaxbMarshaller;
		StringWriter sw = null;
		try {
			jaxbContext = JAXBContext.newInstance(BusinessApplicationHeaderV01.class);
			jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			jaxbMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
			jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			JaxbCharacterEscapeHandler jaxbCharHandler = new JaxbCharacterEscapeHandler();
			jaxbMarshaller.setProperty("com.sun.xml.bind.characterEscapeHandler", jaxbCharHandler);

			ObjectFactory obj = new ObjectFactory();
			JAXBElement<BusinessApplicationHeaderV01> jaxbElement = obj.createAppHdr(appHeader);
			sw = new StringWriter();
			jaxbMarshaller.marshal(jaxbElement, sw);
			//OutputStreamWriter os = new OutputStreamWriter(new ByteArrayOutputStream());
			//jaxbMarshaller.marshal(jaxbElement, os);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sw.toString();
	}

	public Object getAppHeaderCamt009(String msgType, String bobMsgID,String msgSeq) {
		BusinessApplicationHeaderV01 appHeader = new BusinessApplicationHeaderV01();

		FinancialInstitutionIdentification81 finInstnId = new FinancialInstitutionIdentification81();
		finInstnId.setBICFI(env.getProperty("ipsx.bicfi"));
//		ClearingSystemMemberIdentification21 clrSysMmbId = new ClearingSystemMemberIdentification21();
//		clrSysMmbId.setMmbId(env.getProperty("ipsx.user"));
//		finInstnId.setClrSysMmbId(clrSysMmbId);
		BranchAndFinancialInstitutionIdentification51 fIId = new BranchAndFinancialInstitutionIdentification51();
		fIId.setFinInstnId(finInstnId);
		Party9Choice1 fr = new Party9Choice1();
		fr.setFIId(fIId);

		FinancialInstitutionIdentification81 finInstnIdTo = new FinancialInstitutionIdentification81();
		finInstnIdTo.setBICFI(env.getProperty("ipsx.msgReceiverbicfi"));
		BranchAndFinancialInstitutionIdentification51 fIIdTo = new BranchAndFinancialInstitutionIdentification51();
		fIIdTo.setFinInstnId(finInstnIdTo);
		Party9Choice1 to = new Party9Choice1();
		to.setFIId(fIIdTo);

		appHeader.setTo(to);
		appHeader.setFr(fr);
		appHeader.setBizMsgIdr(
				new SimpleDateFormat("yyMMdd").format(new Date()) + env.getProperty("ipsx.user") + "0001"+msgSeq);
		appHeader.setMsgDefIdr(msgType);
		appHeader.setBizSvc("ACH");

		appHeader.setCreDt(listener.getxmlGregorianCalender("0"));

		JAXBContext jaxbContext;
		Marshaller jaxbMarshaller;
		StringWriter sw = null;
		try {
			jaxbContext = JAXBContext.newInstance(BusinessApplicationHeaderV01.class);
			jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			jaxbMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
			jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			JaxbCharacterEscapeHandler jaxbCharHandler = new JaxbCharacterEscapeHandler();
			jaxbMarshaller.setProperty("com.sun.xml.bind.characterEscapeHandler", jaxbCharHandler);

			ObjectFactory obj = new ObjectFactory();
			JAXBElement<BusinessApplicationHeaderV01> jaxbElement = obj.createAppHdr(appHeader);
			sw = new StringWriter();
			jaxbMarshaller.marshal(jaxbElement, sw);
			//OutputStreamWriter os = new OutputStreamWriter(new ByteArrayOutputStream());
			//jaxbMarshaller.marshal(jaxbElement, os);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sw.toString();
	}

	
}
