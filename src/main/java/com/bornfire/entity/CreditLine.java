package com.bornfire.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreditLine {
	private String Included;
	private Amount amount;
	private String type;
	
	@JsonProperty("Included")
	public String getIncluded() {
		return Included;
	}
	public void setIncluded(String included) {
		Included = included;
	}
	
	@JsonProperty("Amount")
	public Amount getAmount() {
		return amount;
	}
	public void setAmount(Amount amount) {
		this.amount = amount;
	}
	
	@JsonProperty("Type")
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
}
