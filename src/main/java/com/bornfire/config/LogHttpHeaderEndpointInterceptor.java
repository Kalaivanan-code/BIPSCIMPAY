package com.bornfire.config;

import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.EndpointInterceptor;

public class LogHttpHeaderEndpointInterceptor implements EndpointInterceptor {

	  @Override
	  public void afterCompletion(MessageContext arg0, Object arg1, Exception arg2) throws Exception {
	    // No-op
	  }

	  @Override
	  public boolean handleFault(MessageContext messageContext, Object arg1) throws Exception {
		  
		  System.out.println("dhdghfghfg");
	    // No-op
	    return true;
	  }

	  @Override
	  public boolean handleRequest(MessageContext messageContext, Object arg1) throws Exception {
	    //HttpLoggingUtils.logMessage("Server Request Message", messageContext.getRequest());
		  System.out.println(messageContext.getRequest());
		  System.out.println("dhdghfghfg");

	    return true;
	  }

	  @Override
	  public boolean handleResponse(MessageContext messageContext, Object arg1) throws Exception {
	   // HttpLoggingUtils.logMessage("Server Response Message", messageContext.getResponse());
		  System.out.println("dhdghfghfg");
		  System.out.println(messageContext.getResponse());

	    return true;
	  }
	}