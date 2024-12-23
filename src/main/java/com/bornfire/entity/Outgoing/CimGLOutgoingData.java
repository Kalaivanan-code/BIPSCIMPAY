package com.bornfire.entity.Outgoing;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "transactionNo","message" })
public class CimGLOutgoingData {

	@JsonProperty("message")
	private String message;

	@JsonProperty("transactionNo")
	private String transactionNo;

	@JsonProperty("transactionNoFromCBS")
	private String transactionNoFromCBS;
	
	@JsonProperty("transactionDate")
	private String transactionDate;
	
	
	@JsonProperty("message")
	public String getMessage() {
		return message;
	}

	@JsonProperty("message")
	public void setMessage(String message) {
		this.message = message;
	}

	@JsonProperty("transactionNo")
	public String getTransactionNo() {
		return transactionNo;
	}

	@JsonProperty("transactionNo")
	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}
	@JsonProperty("transactionNoFromCBS")
	public String getTransactionNoFromCBS() {
		return transactionNoFromCBS;
	}

	@JsonProperty("transactionNoFromCBS")
	public void setTransactionNoFromCBS(String transactionNoFromCBS) {
		this.transactionNoFromCBS = transactionNoFromCBS;
	}

	@JsonProperty("transactionDate")
	public String getTransactionDate() {
		return transactionDate;
	}

	@JsonProperty("transactionDate")
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

	@Override
	public String toString() {
		return "CimGLOutgoingData [message=" + message + ", transactionNo=" + transactionNo + ", transactionNoFromCBS="
				+ transactionNoFromCBS + ", transactionDate=" + transactionDate + "]";
	}



	
}
