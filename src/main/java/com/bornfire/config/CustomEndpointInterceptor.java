package com.bornfire.config;

import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.EndpointInterceptor;

import com.bornfire.endpoint.IpsEndPoint;

@Component
public class CustomEndpointInterceptor implements EndpointInterceptor {

	private static final Logger LOG = LoggerFactory.getLogger(IpsEndPoint.class);

    @Override
    public boolean handleRequest(MessageContext messageContext, Object endpoint) throws Exception {
        LOG.info("Endpoint Request Handling");
        return true;
    }

    @Override
    public boolean handleResponse(MessageContext messageContext, Object endpoint) throws Exception {
        LOG.info("Endpoint Response Handling");
        return true;
    }

    @Override
    public boolean handleFault(MessageContext messageContext, Object endpoint) throws Exception {
        LOG.info("Endpoint Exception Handling");
        return true;
    }

    @Override
    public void afterCompletion(MessageContext messageContext, Object endpoint, Exception ex) throws Exception {
        LOG.info("Execute code after completion");
    }
}