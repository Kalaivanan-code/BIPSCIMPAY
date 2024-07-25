package com.bornfire.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QrNotificationResponse {

	@JsonProperty("data")
	public QrNotificationResponseData data;
	
	@JsonProperty("status")
	public QrNotificationResponseStatus status;

	public QrNotificationResponseData getData() {
		return data;
	}

	public void setData(QrNotificationResponseData data) {
		this.data = data;
	}

	public QrNotificationResponseStatus getStatus() {
		return status;
	}

	public void setStatus(QrNotificationResponseStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "QrNotificationResponse [data=" + data + ", status=" + status + "]";
	}

	public QrNotificationResponse(QrNotificationResponseData data, QrNotificationResponseStatus status) {
		super();
		this.data = data;
		this.status = status;
	}

	public QrNotificationResponse() {
		// TODO Auto-generated constructor stub
	}
	
	
}
