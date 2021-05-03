package com.bornfire.entity;

import java.util.List;

public class BukCreditTransferRequest {
	private List<FrAccount> FrAccount;
	private List<ToAccount> ToAccountList;
	private String CurrencyCode;
	private String Pan;
	private String TotAmt;
	private String TrRmks;
	
	
	public BukCreditTransferRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	

	
	public BukCreditTransferRequest(List<com.bornfire.entity.FrAccount> frAccount, List<ToAccount> toAccountList,
			String currencyCode, String pan, String totAmt, String trRmks) {
		super();
		FrAccount = frAccount;
		ToAccountList = toAccountList;
		CurrencyCode = currencyCode;
		Pan = pan;
		TotAmt = totAmt;
		TrRmks = trRmks;
	}




	public List<FrAccount> getFrAccount() {
		return FrAccount;
	}

	public void setFrAccount(List<FrAccount> frAccount) {
		FrAccount = frAccount;
	}


	public List<ToAccount> getToAccountList() {
		return ToAccountList;
	}
	public void setToAccountList(List<ToAccount> toAccountList) {
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
