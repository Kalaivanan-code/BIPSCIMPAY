package com.bornfire.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConsentOutwardAccessAuthRequest {
	
	@JsonProperty("SCAData")
	private String SCAData;

	public String getSCAData() {
		return SCAData;
	}

	public void setSCAData(String sCAData) {
		SCAData = sCAData;
	}
	
	

}
