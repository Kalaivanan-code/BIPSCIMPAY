package com.bornfire.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Enumeration;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class Listener {
	
	
	@Autowired
	Environment env;

	public XMLGregorianCalendar getxmlGregorianCalender(String type) {
		String dataFormat = null;
		switch(type) {
		case "0":
			dataFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date());
			break;
		case "1":
			dataFormat=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			break;
		case "2":
			dataFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000").format(new Date());
			break;
		}
	
	

	XMLGregorianCalendar xgc = null;
	try
	{
		xgc = DatatypeFactory.newInstance().newXMLGregorianCalendar(dataFormat);
	}catch(
	DatatypeConfigurationException e)
	{
		e.printStackTrace();
	}return xgc;
}
	
	
	public XMLGregorianCalendar convertDateToGreDate(Date date,String type) {
		String dataFormat = null;
		switch(type) {
		case "0":
			dataFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(date);
			break;
		case "1":
			dataFormat=new SimpleDateFormat("yyyy-MM-dd").format(date);
			break;
		case "2":
			dataFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000").format(date);
			break;
		}
	
	

	XMLGregorianCalendar xgc = null;
	try
	{
		xgc = DatatypeFactory.newInstance().newXMLGregorianCalendar(dataFormat);
	}catch(
	DatatypeConfigurationException e)
	{
		e.printStackTrace();
	}return xgc;
}
	
	
	public Date toDate(XMLGregorianCalendar calendar)
    {
        if (calendar == null) {
            return null;
        }
        return calendar.toGregorianCalendar().getTime();
    }
	
	
	public String generateJsonFormat(String msg) {
		//Creating the ObjectMapper object
	      ObjectMapper mapper = new ObjectMapper();
	    //Converting the Object to JSONString
	      String jsonString = "";
		try {
			jsonString = mapper.writeValueAsString(msg);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return jsonString;
	}


	public String getCtgyPurp(String instgAgent,String debtorAgent, String creditorAgent) {
		
		String ctgyPurp="";
		
		if(instgAgent.equals(creditorAgent)) {
			ctgyPurp="101";
		}else {
			if(debtorAgent.equals(creditorAgent)) {
				ctgyPurp="102";
			}else{
				ctgyPurp="103";
			}
		}
		return ctgyPurp;
	}
	
	public  KeyPair KeyPairGeneratorFromJkS() throws KeyStoreException, NoSuchAlgorithmException,
			CertificateException, IOException, UnrecoverableEntryException {


		InputStream ins = new FileInputStream(env.getProperty("cimESB.jks.file"));
		String alias = "";
		String pass = env.getProperty("cimESB.jks.password");

		KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		keyStore.load(ins, pass.toCharArray());
		Enumeration<String> enumeration = keyStore.aliases();
		while (enumeration.hasMoreElements()) {
			alias = enumeration.nextElement();

		}
		KeyStore.PasswordProtection keyPassword = new KeyStore.PasswordProtection(pass.toCharArray());

		KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, keyPassword);

		java.security.cert.Certificate cert = keyStore.getCertificate(alias);
		PublicKey publickey = cert.getPublicKey();
		PrivateKey privatekey = privateKeyEntry.getPrivateKey();
		return new KeyPair(publickey, privatekey);

	}
	
	public  String encrypt(String plainText) throws Exception {
		KeyPair pair = KeyPairGeneratorFromJkS();
		
		Cipher c = Cipher.getInstance("RSA");
		c.init(Cipher.ENCRYPT_MODE, pair.getPublic());

		byte[] encrypyedText = c.doFinal(plainText.getBytes());

		return Base64.getEncoder().encodeToString(encrypyedText);
	}

//decrypt

	public  String decrypt(String encrypyedText) throws Exception {
		KeyPair pair = KeyPairGeneratorFromJkS();

		byte[] encdata = Base64.getDecoder().decode(encrypyedText);

		Cipher c = Cipher.getInstance("RSA");
		c.init(Cipher.DECRYPT_MODE, pair.getPrivate());

		return new String(c.doFinal(encdata));
	}
	
	
}
