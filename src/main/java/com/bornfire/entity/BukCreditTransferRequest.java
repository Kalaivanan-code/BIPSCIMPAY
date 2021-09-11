package com.bornfire.entity;

import java.util.List;

public class BukCreditTransferRequest {
	private FrAccountBulkCredit FrAccount;
	private List<ToAccountBulkCredit> ToAccountList;
	private String CurrencyCode;
	private String Pan;
	private String TotAmt;
	private String TrRmks;
	
	
	public BukCreditTransferRequest() {
		super();
		// TODO Auto-generated constructor stub
	}


	public FrAccountBulkCredit getFrAccount() {
		return FrAccount;
	}


	public void setFrAccount(FrAccountBulkCredit frAccount) {
		FrAccount = frAccount;
	}


	public List<ToAccountBulkCredit> getToAccountList() {
		return ToAccountList;
	}


	public void setToAccountList(List<ToAccountBulkCredit> toAccountList) {
		ToAccountList = toAccountList;
	}


	public String getCurrencyCode() {
		return CurrencyCode;
	}


	public void setCurrencyCode(String currencyCode) {
		CurrencyCode = currencyCode;
	}


	public String getPan() {
		return Pan;
	}


	public void setPan(String pan) {
		Pan = pan;
	}


	public String getTotAmt() {
		return TotAmt;
	}


	public void setTotAmt(String totAmt) {
		TotAmt = totAmt;
	}


	public String getTrRmks() {
		return TrRmks;
	}


	public void setTrRmks(String trRmks) {
		TrRmks = trRmks;
	}
	

	

}
