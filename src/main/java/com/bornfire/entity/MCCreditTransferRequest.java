package com.bornfire.entity;

import java.util.List;

public class MCCreditTransferRequest {
	private FrAccount FrAccount;
	private ToAccount ToAccount;
	private String CurrencyCode;
	private String Pan;
	private String TrAmt;
	private String TrRmks;
	private String Purpose;
	private List<ToAccount> ToAccountList;
	
	public MCCreditTransferRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	public MCCreditTransferRequest(com.bornfire.entity.FrAccount frAccount, com.bornfire.entity.ToAccount toAccount,
			String currencyCode, String pan, String trAmt) {
		super();
		FrAccount = frAccount;
		ToAccount = toAccount;
		CurrencyCode = currencyCode;
		Pan = pan;
		TrAmt = trAmt;
	}
	
	
	
	
	@Override
	public String toString() {
		return "MCCreditTransferRequest [FrAccount=" + FrAccount.toString() + ", ToAccount=" + ToAccount.toString() + ", CurrencyCode="
				+ CurrencyCode + ", Pan=" + Pan + ", TrAmt=" + TrAmt + "]";
	}
	public FrAccount getFrAccount() {
		return FrAccount;
	}
	public void setFrAccount(FrAccount frAccount) {
		FrAccount = frAccount;
	}
	public ToAccount getToAccount() {
		return ToAccount;
	}
	public void setToAccount(ToAccount toAccount) {
		ToAccount = toAccount;
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
	public String getPurpose() {
		return Purpose;
	}
	public void setPurpose(String purpose) {
		Purpose = purpose;
	}
	public List<ToAccount> getToAccountList() {
		return ToAccountList;
	}
	public void setToAccountList(List<ToAccount> toAccountList) {
		ToAccountList = toAccountList;
	}

	
}
