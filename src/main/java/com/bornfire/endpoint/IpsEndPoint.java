package com.bornfire.endpoint;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.wsdl.Fault;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.ws.BindingType;
import javax.xml.ws.handler.MessageContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.soap.SoapFaultException;
import org.springframework.ws.soap.SoapMessageException;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;

import com.bornfire.clientService.IPSXClient;
import com.bornfire.controller.IPSConnection;
import com.bornfire.jaxb.wsdl.AckNakType;
import com.bornfire.jaxb.wsdl.EmptyT;
import com.bornfire.jaxb.wsdl.ObjectFactory;
import com.bornfire.jaxb.wsdl.ResultT;
import com.bornfire.jaxb.wsdl.SendResponse;
import com.bornfire.jaxb.wsdl.SendT;


@Endpoint
public class IpsEndPoint {

	private static final Logger logger = LoggerFactory.getLogger(IpsEndPoint.class);
	@Autowired
	TaskExecutor taskExecutor;
	
	@Autowired
	IPSXClient ipsxClient;
	
	private static final String NAMESPACE_URI = "http://integration.gwclient.smallsystems.cma.se/";


	
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "send")
	@ResponsePayload
	public JAXBElement<SendResponse> getRequest(@RequestPayload SendT request) {
		
		logger.info("Getting Request from IPSX: ");
		logger.info("Sender :"+request.getMessage().getMsgSender()+", Message Type: "+request.getMessage().getMsgType()+"/"+request.getMessage().getMsgNetMir());

		SendResponse response = new SendResponse();
		
		ResultT result = new ResultT();

		result.setType(AckNakType.ACK);
		result.setMir(request.getMessage().getMsgNetMir());
		result.setRef(request.getMessage().getMsgUserReference());
		result.setDatetime(new SimpleDateFormat("yyMMddHHmm").format(new Date()));
//		 response.addHeader("Connection", "Keep-Alive");
//		  response.addHeader("Keep-Alive", "timeout=60000");
		response.setData(result);

		ObjectFactory obj = new ObjectFactory();
		JAXBElement<SendResponse> jaxbElement = obj.createSendResponse(response);

		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				
					ipsxClient.doProcess(request);
				
			}
		});
		logger.info("RETURN");

		return jaxbElement;

	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "getUpdates")
	@ResponsePayload
	public SendResponse getUpdates(@RequestPayload EmptyT request) {
		System.out.println("Got Request successfull");
		logger.info("Getting Request from IPSX: getUpdate");
		SendResponse response = new SendResponse();
		ResultT result = new ResultT();
		result.setDescription("Response---->");
		response.setData(result);
		return response;

	}
	
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "fault")
	@ResponsePayload
	public SendResponse getFault(@RequestPayload Fault request) {
		logger.info("Getting Request from IPSX: fault");

		request.getMessage();
		request.getName();
		System.out.println("Got Request successfull");
		SendResponse response = new SendResponse();
		ResultT result = new ResultT();
		result.setDescription("Response---->");
		response.setData(result);
		return response;

	}
}
