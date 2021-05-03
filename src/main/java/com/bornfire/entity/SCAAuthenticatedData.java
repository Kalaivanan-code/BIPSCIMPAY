package com.bornfire.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SCAAuthenticatedData {
	@JsonProperty("SCAAuthenticationData")
	private String ScaAuthenticationData;

	public String getScaAuthenticationData() {
		return ScaAuthenticationData;
	}

	public void setScaAuthenticationData(String scaAuthenticationData) {
		ScaAuthenticationData = scaAuthenticationData;
	}

	@Override
	public String toString() {
		return "SCAAuthenticatedData [ScaAuthenticationData=" + ScaAuthenticationData + "]";
	}
	
	
}
