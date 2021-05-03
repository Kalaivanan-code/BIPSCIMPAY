package com.bornfire.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountListResponse {

	private List<AccountsListAccounts> Accounts;

	@JsonProperty("Accounts")
	public List<AccountsListAccounts> getAccounts() {
		return Accounts;
	}

	public void setAccounts(List<AccountsListAccounts> accounts) {
		Accounts = accounts;
	}
	
	
}
