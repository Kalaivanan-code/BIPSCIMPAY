package com.bornfire.config;

import java.util.Arrays;
import java.util.stream.Collectors;

import javax.xml.transform.TransformerException;

import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapFaultException;
import org.springframework.ws.soap.server.endpoint.interceptor.PayloadValidatingInterceptor;
import org.xml.sax.SAXParseException;

public class CustomValidatingInterceptor extends PayloadValidatingInterceptor {

    @Override
    protected boolean handleRequestValidationErrors(MessageContext messageContext, SAXParseException[] errors)
            throws TransformerException {

        // if any validation errors, convert them to a string and throw on as Exception to be handled by CustomSoapErrorMessageDispatcherServlet
        if (errors.length > 0) {
            String validationErrorsString = Arrays.stream(errors)
                    .map(error -> "[" + error.getLineNumber() + "," + error.getColumnNumber() + "]: " + error.getMessage())
                    .collect(Collectors.joining(" -- "));
            throw new SoapFaultException("Validation Errors: " + validationErrorsString);
        }
        return true;
    }
}