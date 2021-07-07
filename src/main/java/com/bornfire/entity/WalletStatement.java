package com.bornfire.entity;

public class WalletStatement {

	private String TranDate;
	private String TranParticulars;
	private String TrAmt;
	private String CreditDebitIndicator;
	private String TranStatus;
	public String getTranDate() {
		return TranDate;
	}
	public void setTranDate(String tranDate) {
		TranDate = tranDate;
	}
	public String getTranParticulars() {
		return TranParticulars;
	}
	public void setTranParticulars(String tranParticulars) {
		TranParticulars = tranParticulars;
	}
	public String getTrAmt() {
		return TrAmt;
	}
	public void setTrAmt(String trAmt) {
		TrAmt = trAmt;
	}
	public String getCreditDebitIndicator() {
		return CreditDebitIndicator;
	}
	public void setCreditDebitIndicator(String creditDebitIndicator) {
		CreditDebitIndicator = creditDebitIndicator;
	}
	public String getTranStatus() {
		return TranStatus;
	}
	public void setTranStatus(String tranStatus) {
		TranStatus = tranStatus;
	}
	
	
}
