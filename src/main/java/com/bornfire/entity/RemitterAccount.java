package com.bornfire.entity;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.bornfire.exception.BankCodeVal;

public class RemitterAccount {
	
	@NotBlank(message="Remitter Account Name Required")
	private String AcctName;
	
	@NotBlank(message="Remitter Account Number Required")
	private String AcctNumber;
	
	@NotBlank(message="Remitter Bank Code Required")
	private String BankCode;
	
	@NotBlank(message="Remitter Currency Code Required")
	private String CurrencyCode;
	
	public String getAcctName() {
		return AcctName;
	}
	public void setAcctName(String acctName) {
		AcctName = acctName;
	}
	public String getAcctNumber() {
		return AcctNumber;
	}
	public void setAcctNumber(String acctNumber) {
		AcctNumber = acctNumber;
	}
	
	public String getCurrencyCode() {
		return CurrencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		CurrencyCode = currencyCode;
	}
	
	
	public String getBankCode() {
		return BankCode;
	}
	public void setBankCode(String bankCode) {
		BankCode = bankCode;
	}
	@Override
	public String toString() {
		return "RemitterAccount [AcctName=" + AcctName + ", AcctNumber=" + AcctNumber + ", CurrencyCode=" + CurrencyCode
				+ "]";
	}
	
	
	
}
