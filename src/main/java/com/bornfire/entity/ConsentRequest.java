package com.bornfire.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConsentRequest {
	@JsonProperty("Data")
	private List<Data> data;

	public List<Data> getData() {
		return data;
	}

	public void setData(List<Data> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "ConsentRequest [data=" + data + "]";
	}
	
	

}
