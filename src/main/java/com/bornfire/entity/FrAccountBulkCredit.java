package com.bornfire.entity;

public class FrAccountBulkCredit {

	private String SchmType;
	private String AcctName;
	private String AcctNumber;
	public String getSchmType() {
		return SchmType;
	}
	public void setSchmType(String schmType) {
		SchmType = schmType;
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
	public FrAccountBulkCredit(String schmType, String acctName, String acctNumber) {
		super();
		SchmType = schmType;
		AcctName = acctName;
		AcctNumber = acctNumber;
	}
	public FrAccountBulkCredit() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

	
}
