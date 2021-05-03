package com.bornfire.exception;

import javax.wsdl.Fault;

import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

import com.bornfire.jaxb.wsdl.FaultT;


@SoapFault(faultCode = FaultCode.SERVER)
public class ServiceFaultException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private FaultT serviceStatus;

	public ServiceFaultException(String message, FaultT serviceStatus) {
		super(message);
		this.serviceStatus = serviceStatus;
	}

	public ServiceFaultException(String message, Throwable e, FaultT serviceStatus) {
		super(message, e);
		this.serviceStatus = serviceStatus;
	}

	public FaultT getServiceStatus() {
		return serviceStatus;
	}

	public void setServiceStatus(FaultT serviceStatus) {
		this.serviceStatus = serviceStatus;
	}

}
