package com.bornfire.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;


public class CimCBSrequestData {
	
	@JsonProperty("transactionNo")
	private String transactionNo;
	
	@JsonProperty("initiatingChannel")
	private String initiatingChannel;
	
	@JsonProperty("initatorTransactionNo")
	private String initatorTransactionNo;
	
	@JsonProperty("PostToCBS")
	private Boolean PostToCBS;
	
	@JsonProperty("transactionType")
	private String transactionType;
	
	@JsonProperty("isReversal")
	private String isReversal;
	
	@JsonProperty("transactionNoFromCBS")
	private String transactionNoFromCBS;
	
	@JsonProperty("customerName")
	private String customerName;
	
	@JsonProperty("fromAccountNo")
	private String fromAccountNo;
	
	@JsonProperty("toAccountNo")
	private String toAccountNo;
	
	@JsonProperty("transactionAmount")
	private Float transactionAmount;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(pattern="yyyy-MM-dd")
	@JsonProperty("transactionDate")
	private Date transactionDate;
	
	@JsonProperty("transactionCurrency")
	private String transactionCurrency;
	
	@JsonProperty("transactionParticularCode")
	private String transactionParticularCode;
	
	@JsonProperty("creditRemarks")
	private String creditRemarks;
	
	@JsonProperty("debitRemarks")
	private String debitRemarks;
	
	@JsonProperty("reservedField1")
	private String reservedField1;
	
	@JsonProperty("reservedField2")
	private String reservedField2;

	public String getTransactionNo() {
		return transactionNo;
	}

	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}

	public String getInitiatingChannel() {
		return initiatingChannel;
	}

	public void setInitiatingChannel(String initiatingChannel) {
		this.initiatingChannel = initiatingChannel;
	}

	public String getInitatorTransactionNo() {
		return initatorTransactionNo;
	}

	public void setInitatorTransactionNo(String initatorTransactionNo) {
		this.initatorTransactionNo = initatorTransactionNo;
	}

	public Boolean getPostToCBS() {
		return PostToCBS;
	}

	public void setPostToCBS(Boolean postToCBS) {
		PostToCBS = postToCBS;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getIsReversal() {
		return isReversal;
	}

	public void setIsReversal(String isReversal) {
		this.isReversal = isReversal;
	}

	public String getTransactionNoFromCBS() {
		return transactionNoFromCBS;
	}

	public void setTransactionNoFromCBS(String transactionNoFromCBS) {
		this.transactionNoFromCBS = transactionNoFromCBS;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getFromAccountNo() {
		return fromAccountNo;
	}

	public void setFromAccountNo(String fromAccountNo) {
		this.fromAccountNo = fromAccountNo;
	}

	public String getToAccountNo() {
		return toAccountNo;
	}

	public void setToAccountNo(String toAccountNo) {
		this.toAccountNo = toAccountNo;
	}

	public Float getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(Float transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getTransactionCurrency() {
		return transactionCurrency;
	}

	public void setTransactionCurrency(String transactionCurrency) {
		this.transactionCurrency = transactionCurrency;
	}

	public String getTransactionParticularCode() {
		return transactionParticularCode;
	}

	public void setTransactionParticularCode(String transactionParticularCode) {
		this.transactionParticularCode = transactionParticularCode;
	}

	public String getCreditRemarks() {
		return creditRemarks;
	}

	public void setCreditRemarks(String creditRemarks) {
		this.creditRemarks = creditRemarks;
	}

	public String getDebitRemarks() {
		return debitRemarks;
	}

	public void setDebitRemarks(String debitRemarks) {
		this.debitRemarks = debitRemarks;
	}

	public String getReservedField1() {
		return reservedField1;
	}

	public void setReservedField1(String reservedField1) {
		this.reservedField1 = reservedField1;
	}

	public String getReservedField2() {
		return reservedField2;
	}

	public void setReservedField2(String reservedField2) {
		this.reservedField2 = reservedField2;
	}
	

	
}
