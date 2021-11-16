package com.bornfire.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CimCBSCustDocResponseData {

	@JsonProperty("firstName")
	private String firstName;
	@JsonProperty("lastName")
	private String lastName;
	@JsonProperty("identityNumber")
	private String identityNumber;
	@JsonProperty("identityProofType")
	private String identityProofType;
	@JsonProperty("emailAddress")
	private String emailAddress;
	@JsonProperty("mobile")
	private String mobile;
	@JsonProperty("customerSerialNumber")
	private Integer customerSerialNumber;
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getIdentityNumber() {
		return identityNumber;
	}
	public void setIdentityNumber(String identityNumber) {
		this.identityNumber = identityNumber;
	}
	public String getIdentityProofType() {
		return identityProofType;
	}
	public void setIdentityProofType(String identityProofType) {
		this.identityProofType = identityProofType;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Integer getCustomerSerialNumber() {
		return customerSerialNumber;
	}
	public void setCustomerSerialNumber(Integer customerSerialNumber) {
		this.customerSerialNumber = customerSerialNumber;
	}
	@Override
	public String toString() {
		return "CimCBSCustDocResponseData [firstName=" + firstName + ", lastName=" + lastName + ", identityNumber="
				+ identityNumber + ", identityProofType=" + identityProofType + ", emailAddress=" + emailAddress
				+ ", mobile=" + mobile + ", customerSerialNumber=" + customerSerialNumber + "]";
	}
	
	
	
}
