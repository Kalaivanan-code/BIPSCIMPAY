package com.bornfire.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.UnrecoverableEntryException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Enumeration;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.qos.logback.classic.Logger;
import sun.security.x509.AlgorithmId;
import sun.security.x509.CertificateAlgorithmId;
import sun.security.x509.CertificateSerialNumber;
import sun.security.x509.CertificateValidity;
import sun.security.x509.CertificateVersion;
import sun.security.x509.CertificateX509Key;
import sun.security.x509.X500Name;
import sun.security.x509.X509CertImpl;
import sun.security.x509.X509CertInfo;

@Component
public class Listener {

	@Autowired
	Environment env;

	public XMLGregorianCalendar getxmlGregorianCalender(String type) {
		String dataFormat = null;
		switch (type) {
		case "0":
			dataFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date());
			break;
		case "1":
			dataFormat = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			break;
		case "2":
			dataFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000").format(new Date());
			break;
		}

		XMLGregorianCalendar xgc = null;
		try {
			xgc = DatatypeFactory.newInstance().newXMLGregorianCalendar(dataFormat);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		return xgc;
	}

	public XMLGregorianCalendar convertDateToGreDate(Date date, String type) {
		String dataFormat = null;
		switch (type) {
		case "0":
			dataFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(date);
			break;
		case "1":
			dataFormat = new SimpleDateFormat("yyyy-MM-dd").format(date);
			break;
		case "2":
			dataFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000").format(date);
			break;
		case "3":
			dataFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(date);
			break;
		}

		XMLGregorianCalendar xgc = null;
		try {
			xgc = DatatypeFactory.newInstance().newXMLGregorianCalendar(dataFormat);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		return xgc;
	}

	public Date toDate(XMLGregorianCalendar calendar) {
		if (calendar == null) {
			return null;
		}
		return calendar.toGregorianCalendar().getTime();
	}

	public String generateJsonFormat(String msg) {
		// Creating the ObjectMapper object
		ObjectMapper mapper = new ObjectMapper();
		// Converting the Object to JSONString
		String jsonString = "";
		try {
			jsonString = mapper.writeValueAsString(msg);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return jsonString;
	}

	public String getCtgyPurp(String instgAgent, String debtorAgent, String creditorAgent,
			String debtorName,String creditorName,boolean isRegiteredPISP) {

		String ctgyPurp = "";

		if (instgAgent.equals(creditorAgent)) {
			ctgyPurp = "101";
			/*if(debtorName.toLowerCase().equals(creditorName.toLowerCase())) {
				ctgyPurp = "101";
			}else {
				ctgyPurp = "103";
			}*/
		} else {
			if (debtorAgent.equals(creditorAgent)) {
				ctgyPurp = "103";
			} else {
				if(isRegiteredPISP) {
					ctgyPurp = "102";
				}else {
					ctgyPurp = "103";
				}
				
			}
		}
		return ctgyPurp;
	}

	public String encrypt(String encData,String sec) throws Exception {

		byte[] skey = rekey(sec).getEncoded();
		
		byte[] data1 = Base64.getDecoder().decode(encData);
		Cipher c = Cipher.getInstance("AES");
		SecretKeySpec secretkey = new SecretKeySpec(skey, "AES");
		c.init(Cipher.ENCRYPT_MODE, secretkey);
		byte[] encryptedData = c.doFinal(data1);
		return Base64.getEncoder().encodeToString(encryptedData);
	}

	public String decrypt(String encryptedData, String data) throws Exception {
		byte[] skey = rekey(data).getEncoded();


		byte[] dv = Base64.getDecoder().decode(encryptedData);
		Cipher c = Cipher.getInstance("AES");
		SecretKeySpec secretkey = new SecretKeySpec(skey, "AES");
		c.init(Cipher.DECRYPT_MODE, secretkey);
		byte[] decryptedData = c.doFinal(dv);
		return Base64.getEncoder().encodeToString(decryptedData);
	}

	public KeyPair keypairGenerator() throws NoSuchAlgorithmException {

		SecureRandom random = new SecureRandom();
		KeyPairGenerator keypair = KeyPairGenerator.getInstance("RSA");
		keypair.initialize(2048, random);

		return keypair.generateKeyPair();

	}

	public Key rekey(String data) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException {

		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
		KeySpec spec = new PBEKeySpec(data.toCharArray(), new byte[16], 1024, 256);
		SecretKey skey = factory.generateSecret(spec);
		SecretKeySpec key = new SecretKeySpec(skey.getEncoded(), "AES");
		return key;
	}
	
	public String encryptDid(String data) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableEntryException, IOException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
		
		PublicKey publicKey= KeyPairGeneratorFromJkS().getPublic();
		Cipher c = Cipher.getInstance("RSA");
		c.init(Cipher.ENCRYPT_MODE, publicKey);

		byte[] encrypyedText = c.doFinal(data.getBytes());

		return Base64.getEncoder().encodeToString(encrypyedText);
	}
	
	public String decryptDid(String data) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableEntryException, IOException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
		byte[] encdata = Base64.getDecoder().decode(data);

		Cipher c = Cipher.getInstance("RSA");
		c.init(Cipher.DECRYPT_MODE, KeyPairGeneratorFromJkS().getPrivate());

		return new String(c.doFinal(encdata));
	}
	
	public  KeyPair KeyPairGeneratorFromJkS() throws KeyStoreException, NoSuchAlgorithmException,
			CertificateException, IOException, UnrecoverableEntryException {


		InputStream ins = new FileInputStream(env.getProperty("cimSSL.jks.file"));
		String alias = "";
		char[] pwdArray = env.getProperty("cimSSL.jks.password").toCharArray();// ->JKS password

		KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		keyStore.load(ins,pwdArray);
		Enumeration<String> enumeration = keyStore.aliases();
		while (enumeration.hasMoreElements()) {
			alias = enumeration.nextElement();

		}
		KeyStore.PasswordProtection keyPassword = new KeyStore.PasswordProtection(pwdArray);

		KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, keyPassword);

		java.security.cert.Certificate cert = keyStore.getCertificate(alias);
		PublicKey publickey = cert.getPublicKey();
		PrivateKey privatekey = privateKeyEntry.getPrivateKey();
		return new KeyPair(publickey, privatekey);

	}
	public void storePubKey(KeyPair keyPair, String key)
			throws IOException, GeneralSecurityException {

		
		char[] pwdArray = env.getProperty("cimSSL.jks.password").toCharArray();// ->JKS password
		InputStream ins = new FileInputStream(env.getProperty("cimSSL.jks.file"));
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		ks.load(ins, pwdArray);
		KeyStore.PasswordProtection keyPassword = new KeyStore.PasswordProtection(pwdArray);

		Certificate[] chain = { generateCertificate("cn=Unknown", keyPair, 365, "SHA256withRSA") };

		ks.setKeyEntry(key, keyPair.getPrivate(), pwdArray, chain);
		FileOutputStream outputStream = new FileOutputStream(env.getProperty("cimSSL.jks.file"));
		ks.store(outputStream, pwdArray);

	}
	
	public String retrievePriKey(String key)
			throws IOException, GeneralSecurityException {

		char[] pwdArray = env.getProperty("cimSSL.jks.password").toCharArray();// ->JKS password
		InputStream ins = new FileInputStream(env.getProperty("cimSSL.jks.file"));
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		ks.load(ins, pwdArray);
		KeyStore.PasswordProtection keyPassword = new KeyStore.PasswordProtection(pwdArray);

		KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) ks.getEntry(key, keyPassword);
		Certificate origCert = privateKeyEntry.getCertificate();
		
		PrivateKey privateKey = privateKeyEntry.getPrivateKey();
		
		return Base64.getEncoder().encodeToString(privateKey.getEncoded());

	}
	
	@SuppressWarnings("restriction")
	private  X509Certificate generateCertificate(String dn, KeyPair keyPair, int validity, String sigAlgName)
			throws GeneralSecurityException, IOException {
		PrivateKey privateKey = keyPair.getPrivate();

		X509CertInfo info = new X509CertInfo();

		Date from = new Date();
		Date to = new Date(from.getTime() + validity * 1000L * 24L * 60L * 60L);

		CertificateValidity interval = new CertificateValidity(from, to);
		BigInteger serialNumber = new BigInteger(64, new SecureRandom());
		X500Name owner = new X500Name(dn);
		AlgorithmId sigAlgId = new AlgorithmId(AlgorithmId.RSAEncryption_oid);

		info.set(X509CertInfo.VALIDITY, interval);
		info.set(X509CertInfo.SERIAL_NUMBER, new CertificateSerialNumber(serialNumber));
		info.set(X509CertInfo.SUBJECT, owner);
		info.set(X509CertInfo.ISSUER, owner);
		info.set(X509CertInfo.KEY, new CertificateX509Key(keyPair.getPublic()));
		info.set(X509CertInfo.VERSION, new CertificateVersion(CertificateVersion.V3));
		info.set(X509CertInfo.ALGORITHM_ID, new CertificateAlgorithmId(sigAlgId));

		X509CertImpl certificate = new X509CertImpl(info);
		certificate.sign(privateKey, sigAlgName);

		sigAlgId = (AlgorithmId) certificate.get(X509CertImpl.SIG_ALG);
		info.set(CertificateAlgorithmId.NAME + "." + CertificateAlgorithmId.ALGORITHM, sigAlgId);
		certificate = new X509CertImpl(info);
		certificate.sign(privateKey, sigAlgName);

		return certificate;

	}

	
	

}
