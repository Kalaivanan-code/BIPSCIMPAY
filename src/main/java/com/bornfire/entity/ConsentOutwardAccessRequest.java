package com.bornfire.entity;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.bornfire.exception.TranAmount;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ConsentOutwardAccessRequest {
	
	@JsonProperty("AccountInfo")
	@NotNull(message="Account Info Required")
	@Valid
	private ConsentOutwardAccessRequestAccounts accounts;
	
	@NotBlank(message="PhoneNumber Required")
	@JsonProperty("PhoneNumber")
	private String phoneNumber;
	
	@NotBlank(message="Bank Code Required")
	@JsonProperty("BankCode")
	private String bankCode;
	
	@JsonProperty("Permissions")
	@NotEmpty(message="Permission Required")
	private List<String> permissions;
	

	public ConsentOutwardAccessRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ConsentOutwardAccessRequest(ConsentOutwardAccessRequestAccounts accounts, String phoneNumber) {
		super();
		this.accounts = accounts;
		this.phoneNumber = phoneNumber;
	}

	@JsonProperty("AccountInfo")
	public ConsentOutwardAccessRequestAccounts getAccounts() {
		return accounts;
	}

	@JsonProperty("AccountInfo")
	public void setAccounts(ConsentOutwardAccessRequestAccounts accounts) {
		this.accounts = accounts;
	}

	@JsonProperty("PhoneNumber")
	public String getPhoneNumber() {
		return phoneNumber;
	}

	@JsonProperty("PhoneNumber")
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@JsonProperty("BankCode")
	public String getBankCode() {
		return bankCode;
	}

	@JsonProperty("BankCode")
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	@JsonProperty("Permissions")
	public List<String> getPermissions() {
		return permissions;
	}
	@JsonProperty("Permissions")
	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}
	
	

}
