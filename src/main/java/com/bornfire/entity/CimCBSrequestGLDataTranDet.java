package com.bornfire.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CimCBSrequestGLDataTranDet {

	@JsonProperty("serialNumber")
	private String serialNumber;
	
	@JsonProperty("transactionType")
	private String transactionType;
	
	@JsonProperty("accountNo")
	private String accountNo;
	
	@JsonProperty("accountType")
	private String accountType;
	
	@JsonProperty("transactionAmount")
	private String transactionAmount;
	
	@JsonProperty("currencyCode")
	private String currencyCode;
	
	@JsonProperty("postingDate")
	private String postingDate;
	
	@JsonProperty("transactionCode")
	private String transactionCode;
	
	@JsonProperty("transactionDescription")
	private String transactionDescription;
	
	@JsonProperty("transactionRemarks")
	private String transactionRemarks;
	
	@JsonProperty("rate")
	private String rate;

	@JsonProperty("serialNumber")
	public String getSerialNumber() {
		return serialNumber;
	}

	@JsonProperty("serialNumber")
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	@JsonProperty("transactionType")
	public String getTransactionType() {
		return transactionType;
	}

	@JsonProperty("transactionType")
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	@JsonProperty("accountNo")
	public String getAccountNo() {
		return accountNo;
	}

	@JsonProperty("accountNo")
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	@JsonProperty("accountType")
	public String getAccountType() {
		return accountType;
	}
	
	@JsonProperty("accountType")
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	@JsonProperty("transactionAmount")
	public String getTransactionAmount() {
		return transactionAmount;
	}

	@JsonProperty("transactionAmount")
	public void setTransactionAmount(String transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	@JsonProperty("currencyCode")
	public String getCurrencyCode() {
		return currencyCode;
	}

	@JsonProperty("currencyCode")
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	@JsonProperty("postingDate")
	public String getPostingDate() {
		return postingDate;
	}

	@JsonProperty("postingDate")
	public void setPostingDate(String postingDate) {
		this.postingDate = postingDate;
	}

	@JsonProperty("transactionCode")
	public String getTransactionCode() {
		return transactionCode;
	}

	@JsonProperty("transactionCode")
	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	@JsonProperty("transactionDescription")
	public String getTransactionDescription() {
		return transactionDescription;
	}

	@JsonProperty("transactionDescription")
	public void setTransactionDescription(String transactionDescription) {
		this.transactionDescription = transactionDescription;
	}

	@JsonProperty("transactionRemarks")
	public String getTransactionRemarks() {
		return transactionRemarks;
	}

	@JsonProperty("transactionRemarks")
	public void setTransactionRemarks(String transactionRemarks) {
		this.transactionRemarks = transactionRemarks;
	}

	@JsonProperty("rate")
	public String getRate() {
		return rate;
	}

	@JsonProperty("rate")
	public void setRate(String rate) {
		this.rate = rate;
	}
	
	
	
}
