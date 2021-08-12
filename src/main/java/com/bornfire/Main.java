package com.bornfire;
import java.security.*;
import java.security.KeyStore.*;
import java.io.*;
import java.security.cert.CertificateException;
import java.util.Base64;
import java.util.Enumeration;

import javax.crypto.Cipher;
import javax.xml.bind.DatatypeConverter;

import java.nio.charset.*;
import sun.security.provider.*;
import  javax.crypto.*;

import java.security.*;
import java.security.KeyStore.*;
import java.io.*;

import javax.crypto.Cipher;
import java.nio.charset.*;
import sun.security.provider.*;
import javax.crypto.*;

public class Main {
	static String message = "hello welcome to India";

	
	//keypair form jks
		public static KeyPair KeyPairGeneratorFromJkS() throws KeyStoreException, NoSuchAlgorithmException,
				CertificateException, IOException, UnrecoverableEntryException {
	       
			
			File file = new File("C:\\softlib\\Certificates\\cim_wildcard.jks");

			InputStream ins = new FileInputStream("C:\\softlib\\Certificates\\cim_wildcard.jks");
			String alias = "";
			String pass = "Passw0rd$";
			
			
		       
		        
			KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			keyStore.load(ins, pass.toCharArray());
			 Enumeration<String> enumeration = keyStore.aliases();
		        while(enumeration.hasMoreElements()) {
		             alias = enumeration.nextElement();
		            System.out.println("alias name: " + alias);
		            
		            

		        }
			KeyStore.PasswordProtection keyPassword = new KeyStore.PasswordProtection(pass.toCharArray());

			KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, keyPassword);

			java.security.cert.Certificate cert = keyStore.getCertificate(alias);
			PublicKey publickey = cert.getPublicKey();
			PrivateKey privatekey = privateKeyEntry.getPrivateKey();
			return new KeyPair(publickey, privatekey);

		}

	//encrypt
		public static String encrypt(String plainText, PublicKey publicKey) throws Exception {
			Cipher c = Cipher.getInstance("RSA");
			c.init(Cipher.ENCRYPT_MODE, publicKey);

			byte[] encrypyedText = c.doFinal(plainText.getBytes());

			return Base64.getEncoder().encodeToString(encrypyedText);
		}

	//decrypt

		public static String decrypt(String encrypyedText, PrivateKey privateKey) throws Exception {
			byte[] encdata = Base64.getDecoder().decode(encrypyedText);

			Cipher c = Cipher.getInstance("RSA");
			c.init(Cipher.DECRYPT_MODE, privateKey);

			return new String(c.doFinal(encdata));
		}
	//Signature

		public static String sign(String plainText, PrivateKey privateKey) throws Exception {
			Signature privateSignature = Signature.getInstance("SHA256withRSA");
			privateSignature.initSign(privateKey);
			privateSignature.update(plainText.getBytes());

			byte[] signature = privateSignature.sign();

			return Base64.getEncoder().encodeToString(signature);
		}
	//SignatureVerify

		public static boolean verify(String plainText, String signature, PublicKey publicKey) throws Exception {
			Signature publicSignature = Signature.getInstance("SHA256withRSA");
			publicSignature.initVerify(publicKey);
			publicSignature.update(plainText.getBytes());

			byte[] signatureBytes = Base64.getDecoder().decode(signature);

			return publicSignature.verify(signatureBytes);
		}

		public static void main(String[] args) throws Exception {
			
			 
		        
		        
			System.out.println("Original data : " + message);
			KeyPair pair = KeyPairGeneratorFromJkS();
			System.out.println("PUBLIC KEY : " + DatatypeConverter.printHexBinary(pair.getPublic().getEncoded()));
			String encodedPublicKey = Base64.getEncoder().encodeToString(pair.getPublic().getEncoded());
			System.out.println("PUBLIC KEY : " + (encodedPublicKey));


			System.out.println("PRIVATE KEY : " + DatatypeConverter.printHexBinary(pair.getPrivate().getEncoded()));
			String encodedPrivateKey = Base64.getEncoder().encodeToString(pair.getPrivate().getEncoded());

			System.out.println("PRIVATE KEY : " + (encodedPrivateKey));

			String EncryptText = encrypt(encodedPrivateKey, pair.getPublic());
			System.out.println("Encrypt data : " + EncryptText);

			String decryptText = decrypt(EncryptText, pair.getPrivate());

			System.out.println("Decrypt data :  " + decryptText);

			String signature = sign(message, pair.getPrivate());
			
			System.out.println(signature);

			boolean isCorrect = verify(message, signature, pair.getPublic());
			System.out.println("Signature correct: " + isCorrect);

		}

}
