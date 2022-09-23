package com.bornfire.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RtpResponse {

	private String Status;
	private String ErrorStatus;
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public String getErrorStatus() {
		return ErrorStatus;
	}
	public void setErrorStatus(String errorStatus) {
		ErrorStatus = errorStatus;
	}
	

}
