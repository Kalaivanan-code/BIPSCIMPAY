package com.bornfire.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QrNotificationRequest {
	
	@JsonProperty("header")
	public QrNotificationRequestHeader header;
	
	@JsonProperty("data")
	public QrNotificationRequestData data;



	public QrNotificationRequestHeader getHeader() {
		return header;
	}

	public void setHeader(QrNotificationRequestHeader header) {
		this.header = header;
	}

	public QrNotificationRequestData getData() {
		return data;
	}

	public void setData(QrNotificationRequestData data) {
		this.data = data;
	}

	public QrNotificationRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "QrNotificationRequest [header=" + header + ", data=" + data + "]";
	}
	
	
	

}
