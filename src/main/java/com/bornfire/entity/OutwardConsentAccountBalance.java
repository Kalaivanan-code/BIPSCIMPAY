package com.bornfire.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OutwardConsentAccountBalance {
	@JsonProperty("Balances")
	private ReadConsentBalance Balance;
	@JsonProperty("Links")
	private Links links;
	
	@JsonProperty("ErrorCode")
	private Integer ErrorCode;
	@JsonProperty("Description")
	private String Description;
	
	@JsonProperty("Balances")
	public ReadConsentBalance getBalance() {
		return Balance;
	}
	@JsonProperty("Balances")
	public void setBalance(ReadConsentBalance balance) {
		Balance = balance;
	}
	
	@JsonProperty("Links")
	public Links getLinks() {
		return links;
	}
	@JsonProperty("Links")
	public void setLinks(Links links) {
		this.links = links;
	}
	
	@JsonProperty("ErrorCode")
	public Integer getErrorCode() {
		return ErrorCode;
	}
	
	@JsonProperty("ErrorCode")
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
	public OutwardConsentAccountBalance() {
		super();
		// TODO Auto-generated constructor stub
	}
	public OutwardConsentAccountBalance(Integer errorCode, String description) {
		super();
		ErrorCode = errorCode;
		Description = description;
	}
}
