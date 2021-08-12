package com.bornfire.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)

public class AccountsListAccounts {

	private String AccountId;
	private String Currency;
	private String AccountType;
	private String AccountSubType;
	private String NickName;
	private Account Account;
	private Links links;
	
	@JsonProperty("ErrorCode")
	private Integer ErrorCode;
	@JsonProperty("Description")
	private String Description;
	
	
	@JsonProperty("AccountId")
	public String getAccountId() {
		return AccountId;
	}
	public void setAccountId(String accountId) {
		AccountId = accountId;
	}
	@JsonProperty("Currency")
	public String getCurrency() {
		return Currency;
	}
	public void setCurrency(String currency) {
		Currency = currency;
	}
	@JsonProperty("AccountType")
	public String getAccountType() {
		return AccountType;
	}
	public void setAccountType(String accountType) {
		AccountType = accountType;
	}
	@JsonProperty("AccountSubType")
	public String getAccountSubType() {
		return AccountSubType;
	}
	public void setAccountSubType(String accountSubType) {
		AccountSubType = accountSubType;
	}
	@JsonProperty("Nickname")
	public String getNickName() {
		return NickName;
	}
	public void setNickName(String nickName) {
		NickName = nickName;
	}
	@JsonProperty("Account")
	public Account getAccount() {
		return Account;
	}
	public void setAccount(Account account) {
		Account = account;
	}
	@JsonProperty("Links")
	public Links getLinks() {
		return links;
	}
	public void setLinks(Links links) {
		this.links = links;
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
	public AccountsListAccounts(Integer errorCode, String description) {
		super();
		ErrorCode = errorCode;
		Description = description;
	}
	
	
	
}
