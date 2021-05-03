package com.bornfire.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.bornfire.clientService.IPSXClient;

@Configuration
public class IPSClientConfig {
	 @Bean
	  public Jaxb2Marshaller marshaller() {
	    Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
	    marshaller.setContextPath("com.bornfire.jaxb.wsdl");
	    return marshaller;
	  }

	  @Bean
	  public IPSXClient ipsClient(Jaxb2Marshaller marshaller) {
		IPSXClient client = new IPSXClient();
	    client.setDefaultUri("http://localhost:8000/GWClientSAService/GWClientSA");
	    client.setMarshaller(marshaller);
	    client.setUnmarshaller(marshaller);
	    return client;
	  }
}
