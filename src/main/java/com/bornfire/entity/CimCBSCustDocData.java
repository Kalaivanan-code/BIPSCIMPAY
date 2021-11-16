package com.bornfire.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CimCBSCustDocData {
	
	@JsonProperty("agreementNumber")
	private String agreementNumber;

	@JsonProperty("agreementNumber")
	public String getAgreementNumber() {
		return agreementNumber;
	}

	@JsonProperty("agreementNumber")
	public void setAgreementNumber(String agreementNumber) {
		this.agreementNumber = agreementNumber;
	}

	@Override
	public String toString() {
		return "CimCBSCustDocData [agreementNumber=" + agreementNumber + "]";
	}
	
	
	
}
