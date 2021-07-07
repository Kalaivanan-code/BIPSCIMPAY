package com.bornfire.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConsentOutwardAccessRequestAccounts {
	
	@JsonProperty("SchemeName")
	private String shemeName;
	
	@JsonProperty("AccountName")
	private String accountName;
	
	@JsonProperty("AccountNumber")
	private String accountNumber;

	public ConsentOutwardAccessRequestAccounts() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ConsentOutwardAccessRequestAccounts(String accountName, String accountNumber) {
		super();
		this.accountName = accountName;
		this.accountNumber = accountNumber;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getShemeName() {
		return shemeName;
	}

	public void setShemeName(String shemeName) {
		this.shemeName = shemeName;
	}
	
	
	
	
	
}
