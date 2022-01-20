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
	 private static final String PORT_TYPE_NAME ="GWClientSA";
	 private static final String LOC_URI ="/GWClientSAService/GWClientSA";
	 private static final String NAMESPACE ="http://integration.gwclient.smallsystems.cma.se/";
  
	 private static final String XSD_PATH="wsdl/IPSX.xsd";
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
        wsdl11Definition.setPortTypeName(PORT_TYPE_NAME);
        wsdl11Definition.setLocationUri(LOC_URI);
        wsdl11Definition.setTargetNamespace(NAMESPACE);
        wsdl11Definition.setSchema(ipsSchema);
        return wsdl11Definition;
    }
        
    @Bean
    public XsdSchema ipsSchema() 
    {
        return new SimpleXsdSchema(new ClassPathResource(XSD_PATH));
    }
    
     
  
}


