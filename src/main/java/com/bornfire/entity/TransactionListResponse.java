package com.bornfire.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransactionListResponse {

	private List<TransactionList> transactionList;
	private Links links;
	
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
	
	
}
