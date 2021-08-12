package com.bornfire.entity;

import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("rawtypes")
@JsonInclude(JsonInclude.Include.NON_NULL)

public class ReadConsentBalance {
	@JsonProperty("AccountId")
	private String AccountID;
	@JsonProperty("Amount")
	private Amount amount;
	@JsonProperty("CreditDebitIndicator")
	private String CreditDebitIndicator;
	@JsonProperty("Type")
	private String Type;
	@JsonProperty("DateTime")
	private XMLGregorianCalendar DateTime;
	@JsonProperty("CreditLine")
	private List<CreditLine> creditLine;
	
	
	@JsonProperty("AccountId")
	public String getAccountID() {
		return AccountID;
	}
	@JsonProperty("AccountId")
	public void setAccountID(String accountID) {
		AccountID = accountID;
	}
	
	@JsonProperty("Amount")
	public Amount getAmount() {
		return amount;
	}
	@JsonProperty("Amount")
	public void setAmount(Amount amount) {
		this.amount = amount;
	}
	@SuppressWarnings("rawtypes")
	@JsonProperty("CreditDebitIndicator")
	public String getCreditDebitIndicator() {
		return CreditDebitIndicator;
	}
	@JsonProperty("CreditDebitIndicator")

	public void setCreditDebitIndicator(@SuppressWarnings("rawtypes") String creditDebitIndicator) {
		CreditDebitIndicator = creditDebitIndicator;
	}
	
	@JsonProperty("Type")
	public String getType() {
		return Type;
	}
	@JsonProperty("Type")

	public void setType(String type) {
		Type = type;
	}
	
	@JsonProperty("DateTime")
	public XMLGregorianCalendar getDateTime() {
		return DateTime;
	}
	@JsonProperty("DateTime")
	public void setDateTime(XMLGregorianCalendar dateTime) {
		DateTime = dateTime;
	}
	
	@JsonProperty("CreditLine")
	public List<CreditLine> getCreditLine() {
		return creditLine;
	}
	@JsonProperty("CreditLine")
	public void setCreditLine(List<CreditLine> creditLine) {
		this.creditLine = creditLine;
	}
	
	
}
