package com.bornfire.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRegistrationRequest {
	@JsonProperty("Account")
	private Account Account;

	@JsonProperty("PublicKey")
	private String PublicKey;

	@JsonProperty("PhoneNumber")
	private String PhoneNumber;

	public Account getAccount() {
		return Account;
	}

	public void setAccount(Account Account) {
		this.Account = Account;
	}

	public String getPublicKey() {
		return PublicKey;
	}

	public void setPublicKey(String PublicKey) {
		this.PublicKey = PublicKey;
	}

	public String getPhoneNumber() {
		return PhoneNumber;
	}

	@JsonProperty("PhoneNumber")
	public void setPhoneNumber(String PhoneNumber) {
		this.PhoneNumber = PhoneNumber;
	}

	@Override
	public String toString() {
		return "ClassPojo [Account = " + Account + ", PublicKey = " + PublicKey + ", PhoneNumber = " + PhoneNumber
				+ "]";
	}
}
