package com.bornfire.entity;

public class OtherBankDetResponse {
	private String bankCode;
	private String bankAgent;
	private String bankName;
	
	
	public OtherBankDetResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	

	public OtherBankDetResponse(String bankCode, String bankAgent, String bankName) {
		super();
		this.bankCode = bankCode;
		this.bankAgent = bankAgent;
		this.bankName = bankName;
	}




	public String getBankAgent() {
		return bankAgent;
	}

	public void setBankAgent(String bankAgent) {
		this.bankAgent = bankAgent;
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
