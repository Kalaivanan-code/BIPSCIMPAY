package com.bornfire.config;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.bornfire.entity.TranCimCBSTableRep;

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
		randStr.append("BOB");
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
		randStr.append(new SimpleDateFormat("yyMMdd").format(new Date()));

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
	
	public String generateTranNumber() {

		StringBuffer randStr = new StringBuffer();
		randStr.append(new SimpleDateFormat("yyMMddHHmmss").format(new Date()));

		for (int i = 0; i < TRAN_NUMBER; i++) {
			int number = getRandomMsgNumber();
			char ch = NUM_LIST.charAt(number);
			randStr.append(ch);
		}
		return randStr.toString();
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
