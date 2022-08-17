package com.bornfire.upiqrcodeentity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Merchant {

	
	@JsonProperty("Identifier")
	private MerchantIdentifier Identifier;
	
	@JsonProperty("Name")
	private MerchantName name;
	
	@JsonProperty("Ownership")
	private MerchantOwnership ownership;

	public MerchantIdentifier getIdentifier() {
		return Identifier;
	}

	public void setIdentifier(MerchantIdentifier identifier) {
		Identifier = identifier;
	}

	public MerchantName getName() {
		return name;
	}

	public void setName(MerchantName name) {
		this.name = name;
	}

	public MerchantOwnership getOwnership() {
		return ownership;
	}

	public void setOwnership(MerchantOwnership ownership) {
		this.ownership = ownership;
	}
	
	
	
}
