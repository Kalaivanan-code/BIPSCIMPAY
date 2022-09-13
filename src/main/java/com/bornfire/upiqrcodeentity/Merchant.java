package com.bornfire.upiqrcodeentity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Merchant {

	@JsonProperty("Name")
	private MerchantName name;
	
	@JsonProperty("Identifier")
	private MerchantIdentifier identifier;
	
	
	@JsonProperty("OwnershipType")
	private String ownership;

	public MerchantIdentifier getIdentifier() {
		return identifier;
	}

	public void setIdentifier(MerchantIdentifier identifier) {
		this.identifier = identifier;
	}

	public MerchantName getName() {
		return name;
	}

	public void setName(MerchantName name) {
		this.name = name;
	}

	public String getOwnership() {
		return ownership;
	}

	public void setOwnership(String ownership) {
		this.ownership = ownership;
	}

	@Override
	public String toString() {
		return "Merchant [Name=" + name + ", Identifier=" + identifier + ", ownership=" + ownership + "]";
	}



	
	
}
