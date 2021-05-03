package com.bornfire.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Amount {

	private String Amount;
	private String Currency;
	
	@JsonProperty("Amount")
	public String getAmount() {
		return Amount;
	}
	public void setAmount(String amount) {
		Amount = amount;
	}
	
	@JsonProperty("Currency")
	public String getCurrency() {
		return Currency;
	}
	public void setCurrency(String currency) {
		Currency = currency;
	}
	
	
}
