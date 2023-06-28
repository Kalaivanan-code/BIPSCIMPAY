package com.bornfire.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QrPaymentStatus {

	@JsonProperty("isSuccess")
	private Boolean IsSuccess;
	
	@JsonProperty("message")
	private String Message;
	
	@JsonProperty("statusCode")
	private String StatusCode;

	public Boolean getIssuccess() {
		return IsSuccess;
	}

	public void setIssuccess(Boolean issuccess) {
		IsSuccess = issuccess;
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

	public QrPaymentStatus(Boolean issuccess, String message, String statusCode) {
		super();
		IsSuccess = issuccess;
		Message = message;
		StatusCode = statusCode;
	}

	@Override
	public String toString() {
		return "QrPaymentStatus [Issuccess=" + IsSuccess + ", Message=" + Message + ", StatusCode=" + StatusCode + "]";
	}

	public QrPaymentStatus() {
		super();
		// TODO Auto-generated constructor stub
	}

	
}
