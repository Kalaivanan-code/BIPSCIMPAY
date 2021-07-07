package com.bornfire.entity;

import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WalletAccessRequest {
	@JsonProperty("PhoneNumber")
	private String phoneNumber;
	
	@JsonProperty("PublicKey")
	private String publicKey;
	
	@JsonProperty("Accounts")
	private List<Account> account;

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public List<Account> getAccount() {
		return account;
	}

	public void setAccount(List<Account> account) {
		this.account = account;
	}

	

}
