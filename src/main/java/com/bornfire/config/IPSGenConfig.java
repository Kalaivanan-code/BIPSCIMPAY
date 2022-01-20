package com.bornfire.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.concurrent.Executor;

import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import com.bornfire.messagebuilder.AppHeaders;
import com.bornfire.messagebuilder.DataPDUs;
import com.bornfire.messagebuilder.DocumentPacks;
import com.bornfire.messagebuilder.ParamMTmessage;

import javassist.NotFoundException;

@Configuration
@EnableAsync

public class IPSGenConfig {
	
	@Autowired
	Environment env;
	
	private static final int CONNECT_REQ_TIMEOUT=(60*1000);
	private static final int CONNECT_TIMEOUT=(60*1000);
	private static final int READ_TIMEOUT=(60*1000);
	@Bean
	public SequenceGenerator sequence() {
		return new SequenceGenerator();
	}

	@Bean
	public ParamMTmessage paramMTmsg() {
		return new ParamMTmessage();
	}

	@Bean
	public DataPDUs dataPDUs() {
		return new DataPDUs();
	}

	@Bean
	public AppHeaders appHeaders() {
		return new AppHeaders();
	}

	@Bean
	public DocumentPacks documentPacks() {
		return new DocumentPacks();
	}

	
	@Bean
	public MethodValidationPostProcessor methodValidationPostProcessor() {
	     return new MethodValidationPostProcessor();
	}
	
    @Bean
	public TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(8000);
		executor.setMaxPoolSize(15000);
		executor.setKeepAliveSeconds(60);
		executor.setQueueCapacity(75000);
		executor.setThreadNamePrefix("Request>>");
		executor.initialize();
		return executor;

	}
    
    @Bean(name = "asyncExecutor")
    public TaskExecutor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(8000);
        executor.setMaxPoolSize(15000);
        executor.setKeepAliveSeconds(80);
        executor.setQueueCapacity(50000);
        executor.setThreadNamePrefix("OutwardRequest");
        executor.initialize();
        return executor;
    }
    
    
    /*@Bean(name = "asyncExecutor1")
    public TaskExecutor asyncExecutor1() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5000);
        executor.setMaxPoolSize(12000);
        executor.setKeepAliveSeconds(120);
        executor.setQueueCapacity(50000);
        executor.setThreadNamePrefix("AsynchThread1-");
        executor.initialize();
        return executor;
    }*/
   
    
	@Bean
	public RestTemplate restTemplate() throws NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, KeyStoreException, KeyManagementException, UnrecoverableKeyException {
		KeyStore ks = KeyStore.getInstance("JKS");
		char[] pwdArray = env.getProperty("cimESB.jks.password").toCharArray();

		ks.load(new FileInputStream(env.getProperty("cimESB.jks.file")), pwdArray);
		SSLContext sslContext=org.apache.http.ssl.SSLContextBuilder.create()
				.loadKeyMaterial(ks, pwdArray)
				.loadTrustMaterial(new File(env.getProperty("cimESB.jks.file")), pwdArray, new TrustSelfSignedStrategy() {
                    @Override
                    public boolean isTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
                        return true;
                    }
                })
				//.loadTrustMaterial(null, new TrustSelfSignedStrategy())
				.build();
		
		SSLConnectionSocketFactory socketFactory=new SSLConnectionSocketFactory(sslContext,NoopHostnameVerifier.INSTANCE);
		
		//HttpClient httpClient=HttpClients.custom().setSSLContext(sslContext).build();
		HttpClient httpClient=HttpClients.custom().setSSLSocketFactory(socketFactory).build();
		
		HttpComponentsClientHttpRequestFactory requestFactory =getHttpComponentsClientHttpRequestFactory(httpClient);

		RestTemplate restTemplate = new RestTemplate(requestFactory);
		
		restTemplate.getMessageConverters()
        .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
		return restTemplate;
		//return builder.errorHandler(getRestErrorHandler()).build();
		
		
		/*TrustStrategy acceptingTrustStrategy = (x509Certificates, s) -> true;
	    SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
	    SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
	    CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
	    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
	    requestFactory.setHttpClient(httpClient);
	    RestTemplate restTemplate = new RestTemplate(requestFactory);
	    return restTemplate;*/
	}
	
	private HttpComponentsClientHttpRequestFactory getHttpComponentsClientHttpRequestFactory(HttpClient httpClient) {
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(
				httpClient);
		
		requestFactory.setConnectionRequestTimeout(CONNECT_REQ_TIMEOUT);
		requestFactory.setConnectTimeout(CONNECT_TIMEOUT);
		requestFactory.setReadTimeout(READ_TIMEOUT);
		
		return requestFactory;
	}

	@Bean
	public HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory() {
		HttpComponentsClientHttpRequestFactory t=new HttpComponentsClientHttpRequestFactory();
		return t;
	}
	

	@Bean
	public ResponseErrorHandler getRestErrorHandler() {
		
	
	return new ResponseErrorHandler() {
	 
	    @Override
	    public boolean hasError(ClientHttpResponse httpResponse) 
	      throws IOException {
	 
	        return (httpResponse.getStatusCode().series()==HttpStatus.Series.SERVER_ERROR||httpResponse.getStatusCode().series()== HttpStatus.Series.CLIENT_ERROR);
	    }
	 
	    @Override
	    public void handleError(ClientHttpResponse httpResponse) 
	      throws IOException {
	 
	        if (httpResponse.getStatusCode()
	          .series() == HttpStatus.Series.SERVER_ERROR) {
	            System.out.println("httpResponseServer"+httpResponse.getStatusCode());

	            // handle SERVER_ERROR
	        } else if (httpResponse.getStatusCode()
	          .series() == HttpStatus.Series.CLIENT_ERROR) {
	            // handle CLIENT_ERROR
	            System.out.println("httpResponse"+httpResponse.getStatusCode());
	        }
	    }
	};
	}
	


	
	
}
