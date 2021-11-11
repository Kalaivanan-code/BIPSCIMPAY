package com.bornfire.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "message" })
public class CimGLresponseData {

	@JsonProperty("message")
	private String message;

	@JsonProperty("message")
	public String getMessage() {
		return message;
	}

	@JsonProperty("message")
	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "CimGLresponseData [message=" + message + "]";
	}
	
	
}
