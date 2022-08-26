package com.bornfire.upiqrcodeentity;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)

public class BaseCurr {

	@JsonProperty("baseCurr")
	private String baseCurr;

	public String getBaseCurr() {
		return baseCurr;
	}

	public void setBaseCurr(String baseCurr) {
		this.baseCurr = baseCurr;
	}

	@Override
	public String toString() {
		return "{baseCurr=" + baseCurr + "}";
	}
	
	
}
