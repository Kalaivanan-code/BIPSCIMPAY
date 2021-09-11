package com.bornfire.entity;

public class ToAccountBulkCredit {
	private String ReqUniqueID;
	private String AcctName;
	private String AcctNumber;
	private String BankCode;
	private String TrAmt;
	private String TrRmks;
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
		return TrAmt;
	}
	public void setTrAmt(String trAmt) {
		TrAmt = trAmt;
	}
	public String getTrRmks() {
		return TrRmks;
	}
	public void setTrRmks(String trRmks) {
		TrRmks = trRmks;
	}
	
	public String getReqUniqueID() {
		return ReqUniqueID;
	}
	public void setReqUniqueID(String reqUniqueID) {
		ReqUniqueID = reqUniqueID;
	}
	public ToAccountBulkCredit() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
}
