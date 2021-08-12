package com.bornfire.entity;

import java.util.Date;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConsentAccessRequest {
	@JsonProperty("PhoneNumber")
	private String PhoneNumber;
	
	@JsonProperty("PublicKey")
	private String PublicKey;
	
	
	
	@JsonProperty("Accounts")
	private List<Account> Accounts;
	
	@JsonProperty("Permissions")
	private List<String> Permissions;

	@JsonProperty("PhoneNumber")
	public String getPhoneNumber() {
		return PhoneNumber;
	}

	@JsonProperty("PhoneNumber")
	public void setPhoneNumber(String PhoneNumber) {
		this.PhoneNumber = PhoneNumber;
	}

	@JsonProperty("PublicKey")
	public String getPublicKey() {
		return PublicKey;
	}

	@JsonProperty("PublicKey")
	public void setPublicKey(String PublicKey) {
		this.PublicKey = PublicKey;
	}



	@JsonProperty("Accounts")
	public List<Account> getAccounts() {
		return Accounts;
	}
	@JsonProperty("Accounts")
	public void setAccount(List<Account> Accounts) {
		this.Accounts = Accounts;
	}

	@JsonProperty("Permissions")
	public List<String> getPermissions() {
		return Permissions;
	}
	@JsonProperty("Permissions")
	public void setPermissions(List<String> Permissions) {
		this.Permissions = Permissions;
	}

	@Override
	public String toString() {
		return "ConsentAccessRequest [PhoneNumber=" + PhoneNumber + ", PublicKey=" + PublicKey + ", Accounts="
				+ Accounts + ", Permissions=" + Permissions + "]";
	}


	


	
}
