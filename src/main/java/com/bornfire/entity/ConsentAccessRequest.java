package com.bornfire.entity;

import java.util.Date;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConsentAccessRequest {
	@JsonProperty("PhoneNumber")
	private String phoneNumber;
	
	@JsonProperty("PublicKey")
	private String publicKey;
	
	@JsonProperty("ExpirationDateTime")
	private XMLGregorianCalendar expirationDateTime;
	
	@JsonProperty("TransactionFromDateTime")
	private XMLGregorianCalendar transactionFromDateTime;
	
	@JsonProperty("TransactionToDateTime")
	private XMLGregorianCalendar transactionToDateTime;
	
	@JsonProperty("Accounts")
	private List<Account> account;
	
	@JsonProperty("Permissions")
	private List<String> permissions;

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public XMLGregorianCalendar getExpirationDateTime() {
		return expirationDateTime;
	}

	public void setExpirationDateTime(XMLGregorianCalendar expirationDateTime) {
		this.expirationDateTime = expirationDateTime;
	}

	public XMLGregorianCalendar getTransactionFromDateTime() {
		return transactionFromDateTime;
	}

	public void setTransactionFromDateTime(XMLGregorianCalendar transactionFromDateTime) {
		this.transactionFromDateTime = transactionFromDateTime;
	}

	public XMLGregorianCalendar getTransactionToDateTime() {
		return transactionToDateTime;
	}

	public void setTransactionToDateTime(XMLGregorianCalendar transactionToDateTime) {
		this.transactionToDateTime = transactionToDateTime;
	}

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

	@Override
	public String toString() {
		return "ConsentAccessRequest [phoneNumber=" + phoneNumber + ", publicKey=" + publicKey + ", expirationDateTime="
				+ expirationDateTime + ", transactionFromDateTime=" + transactionFromDateTime
				+ ", transactionToDateTime=" + transactionToDateTime + ", account=" + account + ", permissions="
				+ permissions + "]";
	}


	
}
