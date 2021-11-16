package com.bornfire.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CimCBSCustDocResponseStatus {

	@JsonProperty("isSuccess")
	private Boolean isSuccess;
	
	@JsonProperty("message")
	private String message;
	
	@JsonProperty("statusCode")
	private String statusCode;

	@JsonProperty("isSuccess")
	public Boolean getIsSuccess() {
		return isSuccess;
	}

	@JsonProperty("isSuccess")
	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	@JsonProperty("message")
	public String getMessage() {
		return message;
	}

	@JsonProperty("message")
	public void setMessage(String message) {
		this.message = message;
	}

	@JsonProperty("statusCode")
	public String getStatusCode() {
		return statusCode;
	}

	@JsonProperty("statusCode")
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	@Override
	public String toString() {
		return "CimCBSCustDocResponseStatus [isSuccess=" + isSuccess + ", message=" + message + ", statusCode=" + statusCode
				+ "]";
	}

}
