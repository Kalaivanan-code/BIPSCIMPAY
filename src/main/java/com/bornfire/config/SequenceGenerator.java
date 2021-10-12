package com.bornfire.config;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.bornfire.entity.TranCimCBSTableRep;

import antlr.StringUtils;
import ch.qos.logback.classic.Logger;

public class SequenceGenerator {

	@Autowired
	Environment env;
	
	@Autowired
	TranCimCBSTableRep tranCimCBSTableRep;
	
	private static final String CHAR_LIST = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	private static final int RECORD_ID = 11;
	private static final int WALLET_ACCT_NO = 6;
	private static final int CONSENT_ID = 11;
	private static final int WALLET_ID = 11;
	private static final int SEQ_MSG_ID = 5;
	private static final int SYSTEM_TRACE_AUDIT_NUMBER = 6;
	private static final int SEQ_UNIQUE_ID = 6;
	private static final int OTP = 6;
	private static final int MSG_SEQ = 6;
	private static final String NUM_LIST = "0123456789";
	private static final int TRAN_NUMBER = 6;
	private static final int REQUEST_UUID = 6;
	
	static Long NUM_REQUEST_UUID = 1L;
	
	
	/////////////////
	////Sha HAsh Elements
    public static final UUID NAMESPACE_DNS = new UUID(0x6ba7b8109dad11d1L, 0x80b400c04fd430c8L);
    // Uniform Resource Locator
    public static final UUID NAMESPACE_URL = new UUID(0x6ba7b8119dad11d1L, 0x80b400c04fd430c8L);
    // ISO Object ID
    public static final UUID NAMESPACE_ISO_OID = new UUID(0x6ba7b8129dad11d1L, 0x80b400c04fd430c8L);
    // X.500 Distinguished Name
    public static final UUID NAMESPACE_X500_DN = new UUID(0x6ba7b8149dad11d1L, 0x80b400c04fd430c8L);

    private static final int VERSION_3 = 3; // UUIDv3 MD5
    private static final int VERSION_5 = 5; // UUIDv5 SHA1

    private static final String MESSAGE_DIGEST_MD5 = "MD5"; // UUIDv3
    private static final String MESSAGE_DIGEST_SHA1 = "SHA-1"; // UUIDv5
    private static final String MESSAGE_DIGEST_SHA256 = "SHA-256"; // UUIDv5
    //////////////

	

	public String generateRecordId() {

		StringBuffer randStr = new StringBuffer();
		for (int i = 0; i < RECORD_ID; i++) {
			int number = getRandomNumber();
			char ch = CHAR_LIST.charAt(number);
			randStr.append(ch);
		}
		return randStr.toString();
	}
	
	public String generateConsentId() {

		StringBuffer randStr = new StringBuffer();
		for (int i = 0; i < CONSENT_ID; i++) {
			int number = getRandomNumber();
			char ch = CHAR_LIST.charAt(number);
			randStr.append(ch);
		}
		return randStr.toString();
	}
	
	public String generateAuthId() {

		StringBuffer randStr = new StringBuffer();
		for (int i = 0; i < CONSENT_ID; i++) {
			int number = getRandomNumber();
			char ch = CHAR_LIST.charAt(number);
			randStr.append(ch);
		}
		return randStr.toString();
	}
	
	public String generateWalletId() {

		StringBuffer randStr = new StringBuffer();
		for (int i = 0; i < WALLET_ID; i++) {
			int number = getRandomNumber();
			char ch = CHAR_LIST.charAt(number);
			randStr.append(ch);
		}
		return randStr.toString();
	}

	public String generateOTP() {

		StringBuffer randStr = new StringBuffer();
		for (int i = 0; i < OTP; i++) {
			int number = getRandomMsgNumber();
			char ch = NUM_LIST.charAt(number);
			randStr.append(ch);
		}
		return randStr.toString();
	}

	public String generateMsgSequence() {

		StringBuffer randStr = new StringBuffer();
		for (int i = 0; i < MSG_SEQ; i++) {
			int number = getRandomMsgNumber();
			char ch = NUM_LIST.charAt(number);
			randStr.append(ch);
		}
		return randStr.toString();
	}

	private int getRandomNumber() {
		int randomInt = 0;
		Random randomGenerator = new Random();
		randomInt = randomGenerator.nextInt(CHAR_LIST.length());

		if (randomInt - 1 == -1) {
			return randomInt;
		} else {
			return randomInt - 1;
		}
	}

	public String generateSeqMsgId() {

		StringBuffer randStr = new StringBuffer();
		randStr.append("CFSL");
		randStr.append(new SimpleDateFormat("yyMMdd").format(new Date()));

		for (int i = 0; i < SEQ_MSG_ID; i++) {
			int number = getRandomMsgNumber();
			char ch = NUM_LIST.charAt(number);
			randStr.append(ch);
		}
		randStr.append(new SimpleDateFormat("HHmmss").format(new Date()));
		return randStr.toString();
	}

	public String generateSystemTraceAuditNumber() {

		StringBuffer randStr = new StringBuffer();
		randStr.append(new SimpleDateFormat("yyyyMMdd").format(new Date()));

		for (int i = 0; i < SYSTEM_TRACE_AUDIT_NUMBER; i++) {
			int number = getRandomMsgNumber();
			char ch = NUM_LIST.charAt(number);
			randStr.append(ch);
		}
		return randStr.toString();
	}
	
	public String generateWalletAccountNumber(String phoneNumber) {
		StringBuffer randStr = new StringBuffer();
		randStr.append(phoneNumber);
		randStr.append(new SimpleDateFormat("MMdd").format(new Date()));

		for (int i = 0; i < WALLET_ACCT_NO; i++) {
			int number = getRandomMsgNumber();
			char ch = NUM_LIST.charAt(number);
			randStr.append(ch);
		}
		return randStr.toString();
	}

	private int getRandomMsgNumber() {
		int randomInt = 0;
		Random randomGenerator = new Random();
		randomInt = randomGenerator.nextInt(NUM_LIST.length());

		if (randomInt - 1 == -1) {
			return randomInt;
		} else {
			return randomInt - 1;
		}
	}

	public String generateSeqUniqueID() {

		StringBuffer randStr = new StringBuffer();
		randStr.append(env.getProperty("ipsx.userS"));
		randStr.append(new SimpleDateFormat("yyMMddHHmmss").format(new Date()));

		for (int i = 0; i < SEQ_UNIQUE_ID; i++) {
			int number = getRandomMsgNumber();
			char ch = NUM_LIST.charAt(number);
			randStr.append(ch);
		}
		return randStr.toString();
	}
	
	
	
	public String generateRequestUUId() {

		StringBuffer randStr = new StringBuffer();
		randStr.append(new SimpleDateFormat("yyyyMMdd").format(new Date()));
		randStr.append("_");
		
		Long request_UUID=tranCimCBSTableRep.getRequestUUID();
		
		randStr.append(String.format("%04d", request_UUID));
		
		return randStr.toString();
	}
	
	/*public String generateTranNumber() {

		StringBuffer randStr = new StringBuffer();
		randStr.append(new SimpleDateFormat("yyMMddHHmmss").format(new Date()));

		for (int i = 0; i < TRAN_NUMBER; i++) {
			int number = getRandomMsgNumber();
			char ch = NUM_LIST.charAt(number);
			randStr.append(ch);
		}
		return randStr.toString();
	}*/
	
	
	
	 private static UUID getHashUuid(UUID namespace, String name, String algorithm, int version) {

	        final byte[] hash;
	        final MessageDigest hasher;

	        try {
	            // Instantiate a message digest for the chosen algorithm
	            hasher = MessageDigest.getInstance(algorithm);

	            // Insert name space if NOT NULL
	            if (namespace != null) {
	                hasher.update(toBytes(namespace.getMostSignificantBits()));
	                hasher.update(toBytes(namespace.getLeastSignificantBits()));
	            }

	            // Generate the hash
	            hash = hasher.digest(name.getBytes(StandardCharsets.UTF_8));

	            // Split the hash into two parts: MSB and LSB
	            long msb = toNumber(hash, 0, 8); // first 8 bytes for MSB
	            long lsb = toNumber(hash, 8, 16); // last 8 bytes for LSB

	            // Apply version and variant bits (required for RFC-4122 compliance)
	            msb = (msb & 0xffffffffffff0fffL) | (version & 0x0f) << 12; // apply version bits
	            lsb = (lsb & 0x3fffffffffffffffL) | 0x8000000000000000L; // apply variant bits

	            // Return the UUID
	            return new UUID(msb, lsb);

	        } catch (NoSuchAlgorithmException e) {
	            throw new RuntimeException("Message digest algorithm not supported.");
	        }
	    }

	    public static UUID getMd5Uuid(String string) {
	        return getHashUuid(null, string, MESSAGE_DIGEST_MD5, VERSION_3);
	    }

	    public static UUID getSha1Uuid(String string) {
	        return getHashUuid(null, string, MESSAGE_DIGEST_SHA1, VERSION_5);
	    }
	    
	    public static UUID getSha256Uuid(String string) {
	        return getHashUuid(null, string, MESSAGE_DIGEST_SHA256, VERSION_5);
	    }

	    public static UUID getMd5Uuid(UUID namespace, String string) {
	        return getHashUuid(namespace, string, MESSAGE_DIGEST_MD5, VERSION_3);
	    }

	    public static UUID getSha1Uuid(UUID namespace, String string) {
	        return getHashUuid(namespace, string, MESSAGE_DIGEST_SHA1, VERSION_5);
	    }
	    
	    public static UUID getSha256Uuid(UUID namespace, String string) {
	        return getHashUuid(namespace, string, MESSAGE_DIGEST_SHA256, VERSION_5);
	    }

	    private static byte[] toBytes(final long number) {
	        return new byte[] { (byte) (number >>> 56), (byte) (number >>> 48), (byte) (number >>> 40),
	                (byte) (number >>> 32), (byte) (number >>> 24), (byte) (number >>> 16), (byte) (number >>> 8),
	                (byte) (number) };
	    }

	    private static long toNumber(final byte[] bytes, final int start, final int length) {
	        long result = 0;
	        for (int i = start; i < length; i++) {
	            result = (result << 8) | (bytes[i] & 0xff);
	        }
	        return result;
	    }

	   
	    public String generateCustomDeviceID(String input) {
	    	 /*String string = "";
	         UUID namespace = UUID.randomUUID(); // A custom name space

	         System.out.println("Java's generator");
	         System.out.println("String:"+string);
	         System.out.println("-------------");
	         System.out.println("UUID.nameUUIDFromBytes():      '" + UUID.nameUUIDFromBytes(string.getBytes()) + "'");
	         System.out.println();
	         System.out.println("This generator");
	         System.out.println("HashUuidCreator.getMd5Uuid():  '" + SequenceGenerator.getMd5Uuid(string) + "'");
	         System.out.println("HashUuidCreator.getSha1Uuid(): '" + SequenceGenerator.getSha1Uuid(string) + "'");
	         System.out.println("HashUuidCreator.getSha256Uuid(): '" + SequenceGenerator.getSha256Uuid(string) + "'");
	         System.out.println();
	         System.out.println("This generator WITH name space");
	         System.out.println("HashUuidCreator.getMd5Uuid():  '" + SequenceGenerator.getMd5Uuid(namespace, string) + "'");
	         System.out.println("HashUuidCreator.getSha1Uuid(): '" + SequenceGenerator.getSha1Uuid(namespace, string) + "'");
	         System.out.println("HashUuidCreator.getSha256Uuid(): '" + SequenceGenerator.getSha256Uuid(namespace, string) + "'");*/
	    	UUID namespace = UUID.randomUUID();
	    	UUID customDeviceID=SequenceGenerator.getSha256Uuid(namespace, input);
	    	return customDeviceID.toString();
	    }
	    
	    
	public boolean endToEndValidation(String endToEndID,String DbtrAgent,String cdtrAgent) {
		
		
			if(endToEndID.length()==22) {
				
				String bankAgent=endToEndID.substring(0,8);
				String currentData=endToEndID.substring(8,16);
				String seq=endToEndID.substring(16,22);
				
				
				if((bankAgent.equals(DbtrAgent)||bankAgent.equals(cdtrAgent))&&
						currentData.equals(new SimpleDateFormat("yyyyMMdd").format(new Date()))) {
					System.out.println("TEST INWARD TRANSACTION STEP 1");
					if(isNumeric(seq)) {
						System.out.println("TEST INWARD TRANSACTION STEP 2");
						return true;
					}else {
						return false;
					}
					
				}else {
					return false;
				}
				
			}else {
				return false;
			}
	}
	
	public  boolean isNumeric(String strNum) {
	    if (strNum == null) {
	        return false;
	    }
	    try {
	    	if(strNum.contains(".")) {
	    		return false;
	    	}else {
	    		double d = Double.parseDouble(strNum);
		        if(d<0) {
		        	return false;
		        }else {
		        	return true;
		        }
	    	}
	        
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	}
	/// Production SMS Server
	public String sms(String otp, String MobNum) {

		String retval = "";

		try {

			String data = URLEncoder.encode("dcode", "UTF-8") + "="
					+ URLEncoder.encode(env.getProperty("sms.dcode"), "UTF-8");
			data += "&" + URLEncoder.encode("subuid", "UTF-8") + "="
					+ URLEncoder.encode(env.getProperty("sms.subuid"), "UTF-8");
			data += "&" + URLEncoder.encode("pwd", "UTF-8") + "="
					+ URLEncoder.encode(env.getProperty("sms.pwd"), "UTF-8");
			data += "&" + URLEncoder.encode("sender", "UTF-8") + "="
					+ URLEncoder.encode(env.getProperty("sms.sender"), "UTF-8");
			data += "&" + URLEncoder.encode("pno", "UTF-8") + "=" + URLEncoder.encode(MobNum.trim(), "UTF-8");
			data += "&" + URLEncoder.encode("msgtxt", "UTF-8") + "="
					+ URLEncoder.encode("Dear Customer, OTP for MYT registration is " + otp
							+ " and is valid for 5 minutes only - Bank of Baroda".trim(), "UTF-8");
			if (MobNum.trim().length() > 3) {
				if (MobNum.trim().substring(0, 3).equals(env.getProperty("bob.phonecode"))) {
					data += "&" + URLEncoder.encode("intflag", "UTF-8") + "="
							+ URLEncoder.encode(env.getProperty("sms.intflag1"), "UTF-8");
				} else {
					data += "&" + URLEncoder.encode("intflag", "UTF-8") + "="
							+ URLEncoder.encode(env.getProperty("sms.intflag2"), "UTF-8");
				}

			}

			data += "&" + URLEncoder.encode("msgtype", "UTF-8") + "="
					+ URLEncoder.encode(env.getProperty("sms.msgtype"), "UTF-8");
			data += "&" + URLEncoder.encode("alert", "UTF-8") + "="
					+ URLEncoder.encode(env.getProperty("sms.alert"), "UTF-8");
			data += "&" + URLEncoder.encode("langid", "UTF-8") + "="
					+ URLEncoder.encode(env.getProperty("sms.langid"), "UTF-8");

			URL url = new URL(env.getProperty("sms.url"));
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(data);
			wr.flush();

			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				retval += line;
			}
			wr.close();
			rd.close();
			String rsp = retval;

			System.out.println("Message Sent to " + MobNum);
			System.out.println("Message Sent to " + rsp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retval;
	}
	
	
	

	/// UAT SMS Server
	/*public String sms(String otp, String MobNum) {

		String retval = "";

		try {

			String data = URLEncoder.encode("dcode", "UTF-8") + "="
					+ URLEncoder.encode(env.getProperty("sms.dcode"), "UTF-8");
			data += "&" + URLEncoder.encode("subuid", "UTF-8") + "="
					+ URLEncoder.encode(env.getProperty("sms.subuid"), "UTF-8");
			data += "&" + URLEncoder.encode("pwd", "UTF-8") + "="
					+ URLEncoder.encode(env.getProperty("sms.pwd"), "UTF-8");
			data += "&" + URLEncoder.encode("sender", "UTF-8") + "="
					+ URLEncoder.encode(env.getProperty("sms.sender"), "UTF-8");
			data += "&" + URLEncoder.encode("pno", "UTF-8") + "=" + URLEncoder.encode(MobNum.trim(), "UTF-8");
			data += "&" + URLEncoder.encode("msgtxt", "UTF-8") + "="
					+ URLEncoder.encode("Dear Customer, OTP for MYT registration is " + otp
							+ " and is valid for 5 minutes only-Bank of Baroda".trim(), "UTF-8");
			data += "&" + URLEncoder.encode("intflag", "UTF-8") + "="
					+ URLEncoder.encode(env.getProperty("sms.intflag"), "UTF-8");
			data += "&" + URLEncoder.encode("msgtype", "UTF-8") + "="
					+ URLEncoder.encode(env.getProperty("sms.msgtype"), "UTF-8");
			data += "&" + URLEncoder.encode("alert", "UTF-8") + "="
					+ URLEncoder.encode(env.getProperty("sms.alert"), "UTF-8");
		

			URL url = new URL(env.getProperty("sms.url"));
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(data);
			wr.flush();

			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				retval += line;
			}
			wr.close();
			rd.close();
			String rsp = retval;

			System.out.println("Message Sent to " + MobNum);
			System.out.println("Message Sent to " + rsp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retval;
	}*/

	
	public String sms1(String otp, String MobNum) {

		String retval = "";

		try {

			String data = URLEncoder.encode("dcode", "UTF-8") + "=" + URLEncoder.encode("2967", "UTF-8");
			data += "&" + URLEncoder.encode("subuid", "UTF-8") + "=" + URLEncoder.encode("MAUMYTOTP", "UTF-8");
			data += "&" + URLEncoder.encode("pwd", "UTF-8") + "=" + URLEncoder.encode("L698R5n", "UTF-8");
			data += "&" + URLEncoder.encode("sender", "UTF-8") + "=" + URLEncoder.encode("BOBOTP", "UTF-8");
			data += "&" + URLEncoder.encode("pno", "UTF-8") + "=" + URLEncoder.encode(MobNum.trim(), "UTF-8");
			data += "&" + URLEncoder.encode("intflag", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
			data += "&" + URLEncoder.encode("msgtype", "UTF-8") + "=" + URLEncoder.encode("S", "UTF-8");
			data += "&" + URLEncoder.encode("alert", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
			data += "&" + URLEncoder.encode("msgtxt", "UTF-8") + "="
					+ URLEncoder.encode("Dear Customer, OTP for MYT registration is " + otp
							+ " and is valid for 5 minutes only-Bank of Baroda".trim(), "UTF-8");
			data += "&" + URLEncoder.encode("langid", "UTF-8") + "=" + URLEncoder.encode("en", "UTF-8");

			URL url = new URL("http://SMSAPP.bankofbaroda.co.in:7003/axiomdbrec/pushlistener?");
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(data);
			wr.flush();

			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				retval += line;
			}
			wr.close();
			rd.close();
			String rsp = retval;

			System.out.println("Message Sent to " + MobNum);
			System.out.println("Message Sent to " + rsp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retval;
	}

	

}
