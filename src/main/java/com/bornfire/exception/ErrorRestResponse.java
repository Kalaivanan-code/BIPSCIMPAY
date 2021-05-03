package com.bornfire.exception;


import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorRestResponse {
	private String ErrorCode;
	private String Description;
	@JsonProperty("ErrorCode")
	public String getErrorCode() {
		return ErrorCode;
	}
	@JsonProperty("ErrorCode")
	public void setErrorCode(String errorCode) {
		ErrorCode = errorCode;
	}
	@JsonProperty("Description")
	public String getDescription() {
		return Description;
	}
	@JsonProperty("Description")
	public void setDescription(String description) {
		Description = description;
	}
	
	public ErrorRestResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ErrorRestResponse(String errorCode, String description) {
		super();
		ErrorCode = errorCode;
		Description = description;
	}
	
	
}
