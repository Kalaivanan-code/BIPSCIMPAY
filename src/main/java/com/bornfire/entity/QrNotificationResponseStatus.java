package com.bornfire.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QrNotificationResponseStatus {
	
	@JsonProperty("isSuccess")
	public Boolean isSuccess;
	
	@JsonProperty("message")
	public String message;
	
	@JsonProperty("statusCode")
	public String statusCode;

	public QrNotificationResponseStatus() {

	}

	public Boolean getIsSuccess() {
		return isSuccess;
	}
	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	@Override
	public String toString() {
		return "QrNotificationResponseStatus [isSuccess=" + isSuccess + ", message=" + message + ", statusCode="
				+ statusCode + "]";
	}
	public QrNotificationResponseStatus(Boolean isSuccess, String message, String statusCode) {
		super();
		this.isSuccess = isSuccess;
		this.message = message;
		this.statusCode = statusCode;
	}
	
	
	
	
}
