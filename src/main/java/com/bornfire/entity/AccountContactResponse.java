package com.bornfire.entity;

import java.util.List;

public class AccountContactResponse {

	private String status;
	private String error;
	private List<String> error_desc;

	private String docName;
	private String docNumber;
	private String countryCode;
	private String phoneNumber;
	private String schmType;
	private String accountStatus;
	private String currencyCode;
	private String frezCode;
	
	
	public AccountContactResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	



	public AccountContactResponse(String status, String error, List<String> error_desc) {
		super();
		this.status = status;
		this.error = error;
		this.error_desc = error_desc;
	}
	
	



	
	
	public String getAccountStatus() {
		return accountStatus;
	}




	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}




	public String getCurrencyCode() {
		return currencyCode;
	}




	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}




	public String getFrezCode() {
		return frezCode;
	}




	public void setFrezCode(String frezCode) {
		this.frezCode = frezCode;
	}




	public String getStatus() {
		return status;
	}




	public void setStatus(String status) {
		this.status = status;
	}




	public String getError() {
		return error;
	}




	public void setError(String error) {
		this.error = error;
	}




	public List<String> getError_desc() {
		return error_desc;
	}




	public void setError_desc(List<String> error_desc) {
		this.error_desc = error_desc;
	}




	public AccountContactResponse(String docName, String docNumber, String countryCode, String phoneNumber) {
		super();
		this.docName = docName;
		this.docNumber = docNumber;
		this.countryCode = countryCode;
		this.phoneNumber = phoneNumber;
	}


	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	public String getDocNumber() {
		return docNumber;
	}
	public void setDocNumber(String docNumber) {
		this.docNumber = docNumber;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}




	public String getSchmType() {
		return schmType;
	}




	public void setSchmType(String schmType) {
		this.schmType = schmType;
	}
	
	
}
