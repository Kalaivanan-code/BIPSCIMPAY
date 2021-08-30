package com.bornfire.exception;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorResponse {
	
	@JsonProperty("Error")
	private String error;
	
	@JsonProperty("Error_Desc")
	private List<String> error_desc;
	
	
	public ErrorResponse(String error, List<String> error_desc) {
		super();
		this.error = error;
		this.error_desc = error_desc;
	}
	@JsonProperty("Error")
	public String getError() {
		return error;
	}
	@JsonProperty("Error")
	public void setError(String error) {
		this.error = error;
	}
	@JsonProperty("Error_Desc")
	public List<String> getError_desc() {
		return error_desc;
	}
	@JsonProperty("Error_Desc")
	public void setError_desc(List<String> error_desc) {
		this.error_desc = error_desc;
	}
	
}
