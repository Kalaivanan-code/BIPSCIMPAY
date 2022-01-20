package com.bornfire.config;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

import com.bornfire.clientService.IPSXClient;

@Configuration
public class IPSClientConfig {
	private static final String URI = "http://localhost:8000/GWClientSAService/GWClientSA";
	private static final int CONNECTION_TIMEOUT = (120 * 1000);
	private static final int READ_TIMEOUT = (120 * 1000);
	private static final int MAX_CONNECT = 100000;
	
	private static final int DEFAULT_CONNECTION_TIMEOUT_MILLISECONDS = 60000;
	private static final int DEFAULT_READ_TIMEOUT_MILLISECONDS = 60000;
	private static final int DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 10;

	public static final int CONNECTION_REQUEST_TIMEOUT = 30000;
	private static final int maxConnections = 10000;

	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("com.bornfire.jaxb.wsdl");
		return marshaller;
	}

	@Bean
	public IPSXClient ipsClient(Jaxb2Marshaller marshaller) throws Exception {
		IPSXClient client = new IPSXClient();
		client.setDefaultUri(URI);
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);

		/*
		 * HttpComponentsMessageSender httpComponentsMessageSender = new
		 * HttpComponentsMessageSender();
		 * httpComponentsMessageSender.setReadTimeout(READ_TIMEOUT);
		 * httpComponentsMessageSender.setConnectionTimeout(CONNECTION_TIMEOUT);
		 * httpComponentsMessageSender.setMaxTotalConnections(MAX_CONNECT);
		 * client.setMessageSender(httpComponentsMessageSender);
		 */
		 client.setMessageSender(createMessageSender());
		 client.afterPropertiesSet();
		return client;
	}

	@Bean
	public HttpComponentsMessageSender createMessageSender() {
		return new HttpComponentsMessageSender(createHttpClient());
	}

	private HttpClient createHttpClient() {
		RequestConfig.Builder configBuilder = RequestConfig.custom()
				.setConnectTimeout(DEFAULT_CONNECTION_TIMEOUT_MILLISECONDS)
				.setSocketTimeout(DEFAULT_READ_TIMEOUT_MILLISECONDS)
				.setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT);
		// addProxySettings(configBuilder);

		HttpClientBuilder clientBuilder = HttpClients.custom().setDefaultRequestConfig(configBuilder.build());
		addInterceptor(clientBuilder);
		addConnectionManager(clientBuilder);

		return clientBuilder.build();
	}

	private void addInterceptor(HttpClientBuilder clientBuilder) {
		clientBuilder.addInterceptorFirst(new HttpComponentsMessageSender.RemoveSoapHeadersInterceptor());
	}

	private void addConnectionManager(HttpClientBuilder clientBuilder) {
		if (maxConnections > DEFAULT_MAX_CONNECTIONS_PER_ROUTE) {
			PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
			cm.setMaxTotal(maxConnections);
			cm.setDefaultMaxPerRoute(maxConnections);
			clientBuilder.setConnectionManager(cm);
		}
	}

}
