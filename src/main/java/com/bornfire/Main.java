package com.bornfire;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Date;

import javax.crypto.Cipher;
import javax.xml.bind.DatatypeConverter;

import sun.security.x509.AlgorithmId;
import sun.security.x509.CertificateAlgorithmId;
import sun.security.x509.CertificateSerialNumber;
import sun.security.x509.CertificateValidity;
import sun.security.x509.CertificateVersion;
import sun.security.x509.CertificateX509Key;
import sun.security.x509.X500Name;
import sun.security.x509.X509CertImpl;
import sun.security.x509.X509CertInfo;

public class Main {
	
	
	public static void main(String[] args) throws Exception {
		try {
			char[] pwdArray = "Passw0rd$".toCharArray();// ->JKS password
			InputStream ins = new FileInputStream(
					"C:\\softlib\\Certificates\\cim_wildcard.jks");
			KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
			ks.load(ins, pwdArray);
			KeyStore.PasswordProtection keyPassword = new KeyStore.PasswordProtection(pwdArray);

			/*KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(4096);
			KeyPair keyPair = keyPairGenerator.generateKeyPair();
*/
		//	Certificate[] chain = { generateCertificate("cn=Unknown", keyPair, 365, "SHA256withRSA") };

/*			ks.setKeyEntry("9C20282A-30BB-46E6-AC0F-CF1E61F19233", keyPair.getPrivate(), pwdArray, chain);
			 FileOutputStream outputStream = new FileOutputStream("C:\\Users\\User\\Desktop\\Certificates\\test1.jks");
			  ks.store(outputStream, "Test@123".toCharArray());
			
			System.out.println( "PublicKey:"+Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
			System.out.println("Private Key:"+ Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));
*/
			KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) ks.getEntry("32303a73-3696-5b6c-a3bd-c8b0b172eb2c", keyPassword);
			Certificate origCert = privateKeyEntry.getCertificate();
			PublicKey publickey = origCert.getPublicKey();

			PrivateKey privateKey = privateKeyEntry.getPrivateKey();
			
			System.out.println( "PublicKey:"+Base64.getEncoder().encodeToString(publickey.getEncoded()));
			System.out.println("Private Key:"+ Base64.getEncoder().encodeToString(privateKey.getEncoded()));
			
			/*System.out.println(DatatypeConverter.printHexBinary(publickey.getEncoded()));
			System.out.println(DatatypeConverter.printHexBinary(privateKey.getEncoded()));
			String message = "hello";
			String EncryptText = encrypt(message, publickey);
			System.out.println("Encrypt data : " + EncryptText);

			String decryptText = decrypt(EncryptText, privateKey);

			System.out.println("Decrypt data :  " + decryptText);*/

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

// Certificate Details
	@SuppressWarnings("restriction")
	private static X509Certificate generateCertificate(String dn, KeyPair keyPair, int validity, String sigAlgName)
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

	public static String encrypt(String plainText, PublicKey publicKey) throws Exception {
		Cipher c = Cipher.getInstance("RSA");
		c.init(Cipher.ENCRYPT_MODE, publicKey);

		byte[] encrypyedText = c.doFinal(plainText.getBytes());

		return Base64.getEncoder().encodeToString(encrypyedText);
	}

	public static String decrypt(String encrypyedText, PrivateKey privateKey) throws Exception {
		byte[] encdata = Base64.getDecoder().decode(encrypyedText);

		Cipher c = Cipher.getInstance("RSA");
		c.init(Cipher.DECRYPT_MODE, privateKey);

		return new String(c.doFinal(encdata));
	}

}
