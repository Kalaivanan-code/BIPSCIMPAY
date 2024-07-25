package com.bornfire.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QrNotificationResponseData {
	
	@JsonProperty("message")
	public String message;

	public QrNotificationResponseData() {

	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "QrNotificationResponseData [message=" + message + "]";
	}

	public QrNotificationResponseData(String message) {
		super();
		this.message = message;
	}
	
	

}
