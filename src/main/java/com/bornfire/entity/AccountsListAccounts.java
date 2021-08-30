package com.bornfire.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "AccountId", "Currency","AccountType","AccountSubType","Description","Nickname" ,"Account","Links"})
public class AccountsListAccounts {

	@JsonProperty("AccountId")
	private String AccountId;
	
	@JsonProperty("Currency")
	private String Currency;
	
	@JsonProperty("AccountType")
	private String AccountType;
	
	@JsonProperty("AccountSubType")
	private String AccountSubType;
	
	@JsonProperty("Nickname")
	private String NickName;
	
	@JsonProperty("Account")
	private Account Account;
	
	@JsonProperty("Links")
	private Links links;
	
	@JsonProperty("ErrorCode")
	private Integer ErrorCode;
	@JsonProperty("Description")
	private String Description;
	
	
	@JsonProperty("AccountId")
	public String getAccountId() {
		return AccountId;
	}
	@JsonProperty("AccountId")
	public void setAccountId(String accountId) {
		AccountId = accountId;
	}
	@JsonProperty("Currency")
	public String getCurrency() {
		return Currency;
	}
	@JsonProperty("Currency")
	public void setCurrency(String currency) {
		Currency = currency;
	}
	@JsonProperty("AccountType")
	public String getAccountType() {
		return AccountType;
	}
	@JsonProperty("AccountType")
	public void setAccountType(String accountType) {
		AccountType = accountType;
	}
	@JsonProperty("AccountSubType")
	public String getAccountSubType() {
		return AccountSubType;
	}
	@JsonProperty("AccountSubType")
	public void setAccountSubType(String accountSubType) {
		AccountSubType = accountSubType;
	}
	@JsonProperty("Nickname")
	public String getNickName() {
		return NickName;
	}
	@JsonProperty("Nickname")
	public void setNickName(String nickName) {
		NickName = nickName;
	}
	@JsonProperty("Account")
	public Account getAccount() {
		return Account;
	}
	@JsonProperty("Account")
	public void setAccount(Account account) {
		Account = account;
	}
	@JsonProperty("Links")
	public Links getLinks() {
		return links;
	}
	@JsonProperty("Links")
	public void setLinks(Links links) {
		this.links = links;
	}
	public Integer getErrorCode() {
		return ErrorCode;
	}
	public void setErrorCode(Integer errorCode) {
		ErrorCode = errorCode;
	}
	@JsonProperty("Description")
	public String getDescription() {
		return Description;
	}
	@JsonProperty("Description")
	public void setDescription(String description) {
		Description = description;
	}
	public AccountsListAccounts(Integer errorCode, String description) {
		super();
		ErrorCode = errorCode;
		Description = description;
	}
	public AccountsListAccounts() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	
}
