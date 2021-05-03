package com.bornfire.entity;

public class BulkDebitRemitterAccount {

	private String RemitterName;
	private String RemitterAcctNumber;
	private String TrAmt;
	private String TrRmks;
	private String CurrencyCode;
	
	public BulkDebitRemitterAccount() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BulkDebitRemitterAccount(String remitterName, String remitterAcctNumber, String trAmt, String currencyCode) {
		super();
		RemitterName = remitterName;
		RemitterAcctNumber = remitterAcctNumber;
		TrAmt = trAmt;
		CurrencyCode = currencyCode;
	}

	public String getRemitterName() {
		return RemitterName;
	}

	public void setRemitterName(String remitterName) {
		RemitterName = remitterName;
	}

	public String getRemitterAcctNumber() {
		return RemitterAcctNumber;
	}

	public void setRemitterAcctNumber(String remitterAcctNumber) {
		RemitterAcctNumber = remitterAcctNumber;
	}

	public String getTrAmt() {
		return TrAmt;
	}

	public void setTrAmt(String trAmt) {
		TrAmt = trAmt;
	}

	public String getCurrencyCode() {
		return CurrencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		CurrencyCode = currencyCode;
	}

	public String getTrRmks() {
		return TrRmks;
	}

	public void setTrRmks(String trRmks) {
		TrRmks = trRmks;
	}
	
	
}
