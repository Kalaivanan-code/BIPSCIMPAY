package com.bornfire.entity;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"ConsentID", "AccountInfo","PhoneNumber", "BankName","Permissions" })
public class RegConsentAccountList {
	
	@JsonProperty("ConsentID")
	private String ConsentID;
	
	@JsonProperty("PhoneNumber")
	private String phoneNumber;
	
	@JsonProperty("AccountInfo")
	private ConsentOutwardAccessRequestAccounts accounts;
	
	@JsonProperty("BankName")
	private String BankName;
	
	@JsonProperty("Permissions")
	private List<String> Permission;
	
	@JsonProperty("ConsentID")
	public String getConsentID() {
		return ConsentID;
	}
	@JsonProperty("ConsentID")
	public void setConsentID(String consentID) {
		ConsentID = consentID;
	}
	@JsonProperty("PhoneNumber")
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	
	@JsonProperty("PhoneNumber")
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	@JsonProperty("BankName")
	public String getBankName() {
		return BankName;
	}
	@JsonProperty("BankName")
	public void setBankName(String bankName) {
		BankName = bankName;
	}
	@JsonProperty("Permissions")
	public List<String> getPermission() {
		return Permission;
	}
	@JsonProperty("Permissions")
	public void setPermission(List<String> permission) {
		Permission = permission;
	}
	@JsonProperty("AccountInfo")
	public ConsentOutwardAccessRequestAccounts getAccounts() {
		return accounts;
	}
	@JsonProperty("AccountInfo")
	public void setAccounts(ConsentOutwardAccessRequestAccounts accounts) {
		this.accounts = accounts;
	}
	
	
}
