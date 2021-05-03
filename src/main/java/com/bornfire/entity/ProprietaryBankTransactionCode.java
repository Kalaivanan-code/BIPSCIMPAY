package com.bornfire.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProprietaryBankTransactionCode {

	private String Code;
	private String Issuer;
	
	@JsonProperty("Code")
	public String getCode() {
		return Code;
	}
	public void setCode(String code) {
		Code = code;
	}
	
	@JsonProperty("Issuer")
	public String getIssuer() {
		return Issuer;
	}
	public void setIssuer(String issuer) {
		Issuer = issuer;
	}
	
	
}
