package com.bornfire.entity;


import com.fasterxml.jackson.annotation.JsonProperty;

public class ConsentResponse {
	@JsonProperty("Data")
	private Data data;

	@JsonProperty("Data")
	public Data getData() {
		return data;
	}
	@JsonProperty("Data")
	public void setData(Data data) {
		this.data = data;
	}
	
	
}
