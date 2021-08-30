package com.bornfire.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionListResponse {

	private List<TransactionList> transactionList;
	private Links links;
	
	@JsonProperty("ErrorCode")
	private Integer ErrorCode;
	
	@JsonProperty("Description")
	private String Description;
	
	@JsonProperty("Transactions")
	public List<TransactionList> getTransactionList() {
		return transactionList;
	}
	public void setTransactionList(List<TransactionList> transactionList) {
		this.transactionList = transactionList;
	}
	
	@JsonProperty("Links")
	public Links getLinks() {
		return links;
	}
	public void setLinks(Links links) {
		this.links = links;
	}
	public Integer getErrorCode() {
		return ErrorCode;
	}
	public void setErrorCode(Integer errorCode) {
		ErrorCode = errorCode;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public TransactionListResponse(Integer errorCode, String description) {
		super();
		ErrorCode = errorCode;
		Description = description;
	}
	public TransactionListResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
