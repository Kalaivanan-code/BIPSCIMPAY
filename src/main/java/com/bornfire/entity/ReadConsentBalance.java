package com.bornfire.entity;

import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("rawtypes")
public class ReadConsentBalance {
	private String AccountID;
	private Amount amount;
	
	private Enum CreditDebitIndicator;
	private Enum Type;
	private XMLGregorianCalendar DateTime;
	private List<CreditLine> creditLine;
	
	
	@JsonProperty("AccountId")
	public String getAccountID() {
		return AccountID;
	}
	public void setAccountID(String accountID) {
		AccountID = accountID;
	}
	
	@JsonProperty("Amount")
	public Amount getAmount() {
		return amount;
	}
	public void setAmount(Amount amount) {
		this.amount = amount;
	}
	@SuppressWarnings("rawtypes")
	@JsonProperty("CreditDebitIndicator")
	public Enum getCreditDebitIndicator() {
		return CreditDebitIndicator;
	}
	public void setCreditDebitIndicator(@SuppressWarnings("rawtypes") Enum creditDebitIndicator) {
		CreditDebitIndicator = creditDebitIndicator;
	}
	
	@JsonProperty("Type")
	public Enum getType() {
		return Type;
	}
	public void setType(Enum type) {
		Type = type;
	}
	
	@JsonProperty("DateTime")
	public XMLGregorianCalendar getDateTime() {
		return DateTime;
	}
	public void setDateTime(XMLGregorianCalendar dateTime) {
		DateTime = dateTime;
	}
	
	@JsonProperty("CreditLine")
	public List<CreditLine> getCreditLine() {
		return creditLine;
	}
	public void setCreditLine(List<CreditLine> creditLine) {
		this.creditLine = creditLine;
	}
	
	
}
