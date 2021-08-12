package com.bornfire.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountListResponse {

	private List<AccountsListAccounts> Accounts;

	@JsonProperty("Accounts")
	public List<AccountsListAccounts> getAccounts() {
		return Accounts;
	}

	@JsonProperty("ErrorCode")
	private Integer ErrorCode;
	
	@JsonProperty("Description")
	private String Description;

	public void setAccounts(List<AccountsListAccounts> accounts) {
		Accounts = accounts;
	}

	public Integer getErrorCode() {
		return ErrorCode;
	}

	public void setErrorCode(Integer errorCode) {
		ErrorCode = errorCode;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public AccountListResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AccountListResponse(Integer errorCode, String description) {
		super();
		ErrorCode = errorCode;
		Description = description;
	}
	
	
	
	
}
