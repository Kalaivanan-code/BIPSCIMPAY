package com.bornfire.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConsentOutwardAccessRequest {
	
	@JsonProperty("Accounts")
	private ConsentOutwardAccessRequestAccounts accounts;
	
	@JsonProperty("PhoneNumber")
	private String phoneNumber;
	
	
	

	public ConsentOutwardAccessRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ConsentOutwardAccessRequest(ConsentOutwardAccessRequestAccounts accounts, String phoneNumber) {
		super();
		this.accounts = accounts;
		this.phoneNumber = phoneNumber;
	}

	public ConsentOutwardAccessRequestAccounts getAccounts() {
		return accounts;
	}

	public void setAccounts(ConsentOutwardAccessRequestAccounts accounts) {
		this.accounts = accounts;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	

}
