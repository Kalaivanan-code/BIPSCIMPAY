package com.bornfire.config;

import org.springframework.stereotype.Component;

@Component
public class ErrorResponseCode {
	public String ErrorCode(String code) {

		String responseDesc = null;

		/*if (code.equals("BOB0")) {
			responseDesc = "BOB0:Success";
		} else if (code.equals("BOB114")) {
			responseDesc = "AC01:Incorrect Account Number";
		} else if (code.equals("BOB116")) {
			responseDesc = "AM04:Insufficient Funds";
		} else if (code.equals("BOB119")) {
			responseDesc = "AG01:Transaction Forbidden";
		} */
		if (code.equals("CIM0")) {
			responseDesc = "CIM0:Success";
		} else if (code.equals("AC01")) {
			responseDesc = "AC01:Incorrect Account Number";
		} else if (code.equals("AM04")) {
			responseDesc = "AM04:Insufficient Funds";
		} else if (code.equals("AG01")) {
			responseDesc = "AG01:Transaction Forbidden";
		} else if (code.equals("AC04")) {
			responseDesc = "AC04:Closed Account Number";
		} else if (code.equals("AC06")) {
			responseDesc = "AC06:Blocked Account";
		} else if (code.equals("AM01")) {
			responseDesc = "AM01:Zero Amount";
		} else if (code.equals("RR01")) {
			responseDesc = "RR01:Missing Debtor Account or Identification";
		}else if (code.equals("AM11")) {
			responseDesc = "AM11:Invalid Transaction Currency";
		}else if (code.equals("AC03")) {
			//responseDesc = "AC03:Invalid Creditor Account Number";
			responseDesc = "AC03:Invalid Account Number";
		}else if (code.equals("AC01")) {
			responseDesc = "AC01:Incorrect Account Number";
		}else if (code.equals("AC13")) {
			responseDesc = "AC13:Invalid Account Type";
		} else {
			responseDesc = "TECH:Technical Problem";
		}
		return responseDesc;
	}

	public String validationError(String code) {
		String responseDesc = null;
		
		if(code.equals("BIPS1")) {
			responseDesc = "BIPS1:Missing Debtor Name";
		}else if(code.equals("BIPS2")) {
			responseDesc = "BIPS2:Missing Debtor Account Number";
		}else if(code.equals("BIPS3")) {
			responseDesc = "BIPS3:Missing Creditor Name";
		}else if(code.equals("BIPS4")) {
			responseDesc = "BIPS4:Missing Creditor Account Number";
		}else if(code.equals("BIPS5")) {
			responseDesc = "BIPS5:Missing Transaction Amount";
		}else if(code.equals("BIPS6")) {
			responseDesc = "BIPS6:Zero Amount";
		}else if(code.equals("BIPS7")) {
			responseDesc = "BIPS7:Missing Currency Code";
		}else if(code.equals("BIPS8")) {
			responseDesc = "BIPS8:Invalid Currency Code";
		}else if(code.equals("BIPS9")) {
			responseDesc = "BIPS9:Invalid Debtor Account Number";
		}else if(code.equals("BIPS10")) {
			responseDesc = "BIPS10:Invalid Bank Code";
		}else if(code.equals("BIPS11")) {
			responseDesc = "BIPS11:Invalid Purpose Code";
		}else if(code.equals("BIPS12")) {
			responseDesc = "BIPS12:Maximum Amount Not Permitted";
		}else if(code.equals("BIPS13")) {
			responseDesc = "BIPS13:Unable to Process Request.Dublicate Reference ID:PID";
		}else if(code.equals("BIPS14")) {
			responseDesc = "BIPS14:Invalid Response";
		}else if(code.equals("BIPS15")) {
			responseDesc = "BIPS15:Invalid Request";
		}else if(code.equals("BIPS16")) {
			responseDesc = "BIPS15:Invalid Document Type";
		}
		
		return responseDesc;
	}
	
	public String ErrorCodeRegistration(String code) {

		String responseDesc = null;

		if (code.equals("1")) {
			responseDesc = "101:Invalid request";
		} else if (code.equals("2")) {
			responseDesc = "102:No registration request for received authorization";
		} else if (code.equals("3")) {
			responseDesc = "103:Authorization request doesn’t match registration request";
		} else if(code.equals("4")){
			responseDesc = "104:Internal error";
		}else if(code.equals("5")){
			responseDesc = "202:Receiver's URL is not found";
		}else if(code.equals("6")){
			responseDesc = "302:Internal error (response from receiver)";
		}else if(code.equals("7")){
			responseDesc = "303:Invalid account";
		}else if(code.equals("8")){
			responseDesc = "304:Incorrect customer data";
		}else if(code.equals("9")){
			responseDesc = "305:Account is closed";
		}else if(code.equals("10")){
			responseDesc = "306:Account is blocked/dormant";
		}else if(code.equals("11")){
			responseDesc = "307:Authorization is expired";
		}else if(code.equals("12")){
			responseDesc = "308:Authorization is incorrect";
		}else if(code.equals("13")){
			responseDesc = "315:Invalid consent";
		}
		
		else if(code.equals("21")) {
			responseDesc="351:Insufficient Fund";
		}else if(code.equals("22")) {
			responseDesc="352:Maximum Amount Not Permitted(must be less than or equal 10000 Only)";
		}else if(code.equals("23")) {
			responseDesc="101:No account found with specified CPR/CR";
		}else if(code.equals("24")) {
			responseDesc="102:wrong credentials";
		}else if(code.equals("25")) {
			responseDesc="500:Internal Server Error";
		}
		
		else {
			responseDesc = "104:Internal error";
		}
		return responseDesc;
	}

}
