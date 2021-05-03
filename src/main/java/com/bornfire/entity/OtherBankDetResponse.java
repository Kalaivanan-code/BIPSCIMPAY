package com.bornfire.entity;

public class OtherBankDetResponse {
	private String bankCode;
	private String bankName;
	
	
	public OtherBankDetResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public OtherBankDetResponse(String bankCode, String bankName) {
		super();
		this.bankCode = bankCode;
		this.bankName = bankName;
	}


	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	
	
}
