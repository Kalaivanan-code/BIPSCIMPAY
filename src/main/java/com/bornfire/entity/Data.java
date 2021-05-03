package com.bornfire.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)

public class Data {
	
	
	@JsonProperty("ConsentId")
	private String consentId;
	
	@JsonProperty("Status")
	private String status;
	
	@JsonProperty("StatusUpdateDateTime")
	private Date statusUpdateDateTime;
	
	@JsonProperty("CreationDateTime")
	private Date creationDateTime;
	
	@JsonProperty("Accounts")
	private List<Account> account;
	
	@JsonProperty("Permissions")
	private List<String> permissions;
	
	@JsonProperty("ExpirationDateTime")
	private Date expirationDateTime;
	
	@JsonProperty("TransactionFromDateTime")
	private Date transactionFromDateTime;
	
	@JsonProperty("TransactionToDateTime")
	private Date transactionToDateTime;
	
	@JsonProperty("Links")
	private Links links;
	
	

	

	public List<Account> getAccount() {
		return account;
	}

	public void setAccount(List<Account> account) {
		this.account = account;
	}

	public List<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}

	public Date getExpirationDateTime() {
		return expirationDateTime;
	}

	public void setExpirationDateTime(Date expirationDateTime) {
		this.expirationDateTime = expirationDateTime;
	}

	public Date getTransactionFromDateTime() {
		return transactionFromDateTime;
	}

	public void setTransactionFromDateTime(Date transactionFromDateTime) {
		this.transactionFromDateTime = transactionFromDateTime;
	}

	public Date getTransactionToDateTime() {
		return transactionToDateTime;
	}

	public void setTransactionToDateTime(Date transactionToDateTime) {
		this.transactionToDateTime = transactionToDateTime;
	}
	
	public String getConsentId() {
		return consentId;
	}

	public void setConsentId(String consentId) {
		this.consentId = consentId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	

	public Date getStatusUpdateDateTime() {
		return statusUpdateDateTime;
	}

	public void setStatusUpdateDateTime(Date statusUpdateDateTime) {
		this.statusUpdateDateTime = statusUpdateDateTime;
	}

	public Date getCreationDateTime() {
		return creationDateTime;
	}

	public void setCreationDateTime(Date creationDateTime) {
		this.creationDateTime = creationDateTime;
	}



	public Links getLinks() {
		return links;
	}

	public void setLinks(Links links) {
		this.links = links;
	}

	@Override
	public String toString() {
		return "Data [data=" + account + ", permissions=" + permissions + ", expirationDateTime=" + expirationDateTime
				+ ", transactionFromDateTime=" + transactionFromDateTime + ", transactionToDateTime="
				+ transactionToDateTime + "]";
	}
	
	
	
	
}
