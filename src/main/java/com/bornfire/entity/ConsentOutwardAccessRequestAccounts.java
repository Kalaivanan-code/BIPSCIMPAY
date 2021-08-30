package com.bornfire.entity;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConsentOutwardAccessRequestAccounts {
	
	@JsonProperty("SchemeName")
	@NotBlank(message="Scheme Name Required")
	private String shemeName;
	
	@JsonProperty("AccountName")
	@NotBlank(message="Account Name Required")
	private String accountName;
	
	@JsonProperty("AccountNumber")
	@NotBlank(message="Account Number Required")
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
