package com.bornfire.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QrPaymentResponse {

	
	@JsonProperty("data")
	private QrPaymentStatusData data;
	@JsonProperty("status")
	private QrPaymentStatus status;
	public QrPaymentStatusData getData() {
		return data;
	}
	public void setData(QrPaymentStatusData data) {
		this.data = data;
	}
	public QrPaymentStatus getStatus() {
		return status;
	}
	public void setStatus(QrPaymentStatus status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "QrPaymentResponse [data=" + data + ", status=" + status + "]";
	}
	public QrPaymentResponse(QrPaymentStatusData data, QrPaymentStatus status) {
		super();
		this.data = data;
		this.status = status;
	}
	public QrPaymentResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	

}
