package com.bornfire.entity;

import javax.xml.datatype.XMLGregorianCalendar;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransactionList {

	private String AccountId;
	private String TransactionReference;
	private Amount Amount;
	private String CreditDebitIndicator;
	private String Status;
	private XMLGregorianCalendar BookingDataTime;
	private XMLGregorianCalendar ValueDateTime;
	private String TransactionInformation;
	private ProprietaryBankTransactionCode tranCode;
	private ReadConsentBalance balance;
	
	@JsonProperty("AccountId")
	public String getAccountId() {
		return AccountId;
	}
	public void setAccountId(String accountId) {
		AccountId = accountId;
	}
	
	@JsonProperty("TransactionReference")
	public String getTransactionReference() {
		return TransactionReference;
	}
	public void setTransactionReference(String transactionReference) {
		TransactionReference = transactionReference;
	}
	
	@JsonProperty("Amount")
	public Amount getAmount() {
		return Amount;
	}
	public void setAmount(Amount amount) {
		Amount = amount;
	}
	
	@JsonProperty("CreditDebitIndicator")
	public String getCreditDebitIndicator() {
		return CreditDebitIndicator;
	}
	public void setCreditDebitIndicator(String creditDebitIndicator) {
		CreditDebitIndicator = creditDebitIndicator;
	}
	
	@JsonProperty("Status")
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	
	@JsonProperty("BookingDateTime")
	public XMLGregorianCalendar getBookingDataTime() {
		return BookingDataTime;
	}
	public void setBookingDataTime(XMLGregorianCalendar bookingDataTime) {
		BookingDataTime = bookingDataTime;
	}
	
	@JsonProperty("ValueDateTime")
	public XMLGregorianCalendar getValueDateTime() {
		return ValueDateTime;
	}
	public void setValueDateTime(XMLGregorianCalendar valueDateTime) {
		ValueDateTime = valueDateTime;
	}
	
	@JsonProperty("TransactionInformation")
	public String getTransactionInformation() {
		return TransactionInformation;
	}
	public void setTransactionInformation(String transactionInformation) {
		TransactionInformation = transactionInformation;
	}
	
	@JsonProperty("ProprietaryBankTransactionCode")
	public ProprietaryBankTransactionCode getTranCode() {
		return tranCode;
	}
	public void setTranCode(ProprietaryBankTransactionCode tranCode) {
		this.tranCode = tranCode;
	}
	
	@JsonProperty("Balance")
	public ReadConsentBalance getBalance() {
		return balance;
	}
	public void setBalance(ReadConsentBalance balance) {
		this.balance = balance;
	}
	
	
}
