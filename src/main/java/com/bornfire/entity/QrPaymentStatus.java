package com.bornfire.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QrPaymentStatus {

	@JsonProperty("issuccess")
	private Boolean isSuccess;
	
	@JsonProperty("message")
	private String Message;
	
	@JsonProperty("statusCode")
	private String StatusCode;

	public Boolean getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

	public String getStatusCode() {
		return StatusCode;
	}

	public void setStatusCode(String statusCode) {
		StatusCode = statusCode;
	}

	@Override
	public String toString() {
		return "QrPaymentStatus [isSuccess=" + isSuccess + ", Message=" + Message + ", StatusCode=" + StatusCode + "]";
	}



	
}
