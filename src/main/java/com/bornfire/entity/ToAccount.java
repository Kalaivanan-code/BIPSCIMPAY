package com.bornfire.entity;

import javax.validation.constraints.NotBlank;

public class ToAccount {
	
	private String AcctName;
	private String AcctNumber;
	private String BankCode;
	private String trAmt;
	
	
	
	public ToAccount() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ToAccount(String acctName, String acctNumber, String bankCode) {
		super();
		AcctName = acctName;
		AcctNumber = acctNumber;
		BankCode = bankCode;
	}

	
	@Override
	public String toString() {
		return "ToAccount [AcctName=" + AcctName + ", AcctNumber=" + AcctNumber + ", BankCode=" + BankCode + "]";
	}

	public String getAcctName() {
		return AcctName;
	}

	public void setAcctName(String acctName) {
		AcctName = acctName;
	}

	public String getAcctNumber() {
		return AcctNumber;
	}

	public void setAcctNumber(String acctNumber) {
		AcctNumber = acctNumber;
	}

	public String getBankCode() {
		return BankCode;
	}

	public void setBankCode(String bankCode) {
		BankCode = bankCode;
	}

	public String getTrAmt() {
		return trAmt;
	}

	public void setTrAmt(String trAmt) {
		this.trAmt = trAmt;
	}
	
	
	
	
}
