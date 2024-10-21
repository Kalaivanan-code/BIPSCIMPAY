package com.bornfire.entity;

import java.util.List;

public class BukOutgoingCreditTransferRequest {

	private FrAccountBulkCredit FrAccount;
	private List<ToAccountBulkCredit> ToAccountList;
	private String CurrencyCode;
	private String Pan;
	private String TotAmt;
	private String TrRmks;
	private CimCBSresponseData data;
	private CimCBSresponseStatus status;
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
	public CimCBSresponseData getData() {
		return data;
	}
	public void setData(CimCBSresponseData data) {
		this.data = data;
	}
	public CimCBSresponseStatus getStatus() {
		return status;
	}
	public void setStatus(CimCBSresponseStatus status) {
		this.status = status;
	}
	public BukOutgoingCreditTransferRequest(FrAccountBulkCredit frAccount, List<ToAccountBulkCredit> toAccountList,
			String currencyCode, String pan, String totAmt, String trRmks, CimCBSresponseData data,
			CimCBSresponseStatus status) {
		super();
		FrAccount = frAccount;
		ToAccountList = toAccountList;
		CurrencyCode = currencyCode;
		Pan = pan;
		TotAmt = totAmt;
		TrRmks = trRmks;
		this.data = data;
		this.status = status;
	}
	public BukOutgoingCreditTransferRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "BukOutgoingCreditTransferRequest [FrAccount=" + FrAccount + ", ToAccountList=" + ToAccountList
				+ ", CurrencyCode=" + CurrencyCode + ", Pan=" + Pan + ", TotAmt=" + TotAmt + ", TrRmks=" + TrRmks
				+ ", data=" + data + ", status=" + status + "]";
	}
	
	
}
