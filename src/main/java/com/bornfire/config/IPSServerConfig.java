package com.bornfire.config;

import java.util.List;
import java.util.Properties;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.soap.server.endpoint.SoapFaultDefinition;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;
import org.springframework.ws.soap.server.endpoint.interceptor.PayloadRootSmartSoapEndpointInterceptor;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

import com.bornfire.exception.DetailSoapFaultDefinitionExceptionResolver;
import com.bornfire.exception.ServiceFaultException;

@EnableWs
@Configuration
public class IPSServerConfig extends WsConfigurerAdapter 
{
    @Bean
    public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) 
    {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean(servlet, "/GWClientSAService/*");
    }
 
    @Bean(name = "GWClientSA")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema ipsSchema) 
    {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("GWClientSA");
        wsdl11Definition.setLocationUri("/GWClientSAService/GWClientSA");
        wsdl11Definition.setTargetNamespace("http://integration.gwclient.smallsystems.cma.se/");
        wsdl11Definition.setSchema(ipsSchema);
        return wsdl11Definition;
    }
    
    /*@Bean(name = "soapFaultAnnotationExceptionResolver")
    public DetailSoapFaultDefinitionExceptionResolver exceptionResolver( ApplicationContext applicationContext ){
        DetailSoapFaultDefinitionExceptionResolver exceptionResolver = new DetailSoapFaultDefinitionExceptionResolver();

        System.out.println("mjhznhj");
        SoapFaultDefinition soapFaultDefinition = new SoapFaultDefinition();
        soapFaultDefinition.setFaultCode( SoapFaultDefinition.SERVER );
        exceptionResolver.setDefaultFault( soapFaultDefinition );

        return exceptionResolver;
    }*/
    
    /*@Bean
    public SoapFaultMappingExceptionResolver exceptionResolver() {
		SoapFaultMappingExceptionResolver exceptionResolver = new DetailSoapFaultDefinitionExceptionResolver();

		SoapFaultDefinition faultDefinition = new SoapFaultDefinition();
		faultDefinition.setFaultCode(SoapFaultDefinition.SERVER);
		exceptionResolver.setDefaultFault(faultDefinition);

		Properties errorMappings = new Properties();
		errorMappings.setProperty(Exception.class.getName(), SoapFaultDefinition.SERVER.toString());
		errorMappings.setProperty(ServiceFaultException.class.getName(), SoapFaultDefinition.SERVER.toString());
		exceptionResolver.setExceptionMappings(errorMappings);
		exceptionResolver.setOrder(1);
		return exceptionResolver;
	}    
    
   /* @Override
    public void addInterceptors(List<EndpointInterceptor> interceptors) {

    	System.out.println("Klaiaalla");
        // register global interceptor
        interceptors.add(new CustomEndpointInterceptor());

        // register endpoint specific interceptor
        interceptors.add(new PayloadRootSmartSoapEndpointInterceptor(
                new CustomEndpointInterceptor(),
                "http://integration.gwclient.smallsystems.cma.se/",
                "send"));
    }
    /*@Override
    public void addInterceptors(List<EndpointInterceptor> interceptors) {
        // validate requests and responses
        // cannot use PayloadValidatingInterceptor because that one would generate an unwanted/unavoidable SoapFault
        CustomValidatingInterceptor validatingInterceptor = new CustomValidatingInterceptor();
        validatingInterceptor.setValidateRequest(true);
        validatingInterceptor.setValidateResponse(true);
        validatingInterceptor.setXsdSchema(ipsSchema());
        interceptors.add(validatingInterceptor);
    }*/
 
   
    
    @Bean
    public XsdSchema ipsSchema() 
    {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/IPSX.xsd"));
    }
    
     
  
}


