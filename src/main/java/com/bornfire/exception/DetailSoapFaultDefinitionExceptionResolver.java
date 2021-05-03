package com.bornfire.exception;

import javax.wsdl.Fault;
import javax.xml.namespace.QName;

import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.SoapFaultDetail;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;

import com.bornfire.jaxb.wsdl.FaultT;

public class DetailSoapFaultDefinitionExceptionResolver extends SoapFaultMappingExceptionResolver {

	private static final QName CODE = new QName("statusCode");
	private static final QName MESSAGE = new QName("message");

	@Override
	protected void customizeFault(Object endpoint, Exception ex, SoapFault fault) {
		logger.error("Exception processed ", ex);
		if (ex instanceof ServiceFaultException) {
			FaultT status = ((ServiceFaultException) ex).getServiceStatus();
			SoapFaultDetail detail = fault.addFaultDetail();
			detail.addFaultDetailElement(CODE).addText(status.getCode());
			detail.addFaultDetailElement(MESSAGE).addText(status.getDescription());
		}
	}

}