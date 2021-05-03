package com.bornfire.entity;

import javax.xml.datatype.XMLGregorianCalendar;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConsentAccountBalance {

	private ReadConsentBalance Balance;
	private Links links;
	
	@JsonProperty("Balances")
	public ReadConsentBalance getBalance() {
		return Balance;
	}
	public void setBalance(ReadConsentBalance balance) {
		Balance = balance;
	}
	
	@JsonProperty("Links")
	public Links getLinks() {
		return links;
	}
	public void setLinks(Links links) {
		this.links = links;
	}
	
	
}
