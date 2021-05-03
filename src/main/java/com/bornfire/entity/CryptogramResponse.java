package com.bornfire.entity;

public class CryptogramResponse {

	private String deviceId;
	private String amount;
	private String currency;
	private String debtorAccount;
	private String creditorAccount;
	private String endToEndId;
	private String consentId;
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getDebtorAccount() {
		return debtorAccount;
	}
	public void setDebtorAccount(String debtorAccount) {
		this.debtorAccount = debtorAccount;
	}
	public String getCreditorAccount() {
		return creditorAccount;
	}
	public void setCreditorAccount(String creditorAccount) {
		this.creditorAccount = creditorAccount;
	}
	public String getEndToEndId() {
		return endToEndId;
	}
	public void setEndToEndId(String endToEndId) {
		this.endToEndId = endToEndId;
	}
	
	
	public String getConsentId() {
		return consentId;
	}
	public void setConsentId(String consentId) {
		this.consentId = consentId;
	}
	@Override
	public String toString() {
		return "CryptogramResponse [deviceId=" + deviceId + ", amount=" + amount + ", currency=" + currency
				+ ", debtorAccount=" + debtorAccount + ", creditorAccount=" + creditorAccount + ", endToEndId="
				+ endToEndId + "]";
	}
	
	
}
