package com.bornfire.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RTPTransferStatusResponse {

	private String TranId;
	private String ReqId;
	private String Resv_field1;
	private String Resv_field2;
	@JsonProperty("RTPResponse")
	private RtpResponse RtpResponse;
	public String getTranId() {
		return TranId;
	}
	public void setTranId(String tranID) {
		TranId = tranID;
	}
	
	
	public String getReqId() {
		return ReqId;
	}
	public void setReqId(String reqId) {
		ReqId = reqId;
	}
	
	
	public String getResv_field1() {
		return Resv_field1;
	}
	public void setResv_field1(String resv_field1) {
		Resv_field1 = resv_field1;
	}
	public String getResv_field2() {
		return Resv_field2;
	}
	public void setResv_field2(String resv_field2) {
		Resv_field2 = resv_field2;
	}
	@JsonProperty("RTPResponse")
	public RtpResponse getRtpResponse() {
		return RtpResponse;
	}
	@JsonProperty("RTPResponse")
	public void setRtpResponse(RtpResponse rtpResponse) {
		RtpResponse = rtpResponse;
	}
	@Override
	public String toString() {
		return "RTPTransferStatusResponse [TranId=" + TranId + ", ReqId=" + ReqId + ", Resv_field1=" + Resv_field1
				+ ", Resv_field2=" + Resv_field2 + ", RtpResponse=" + RtpResponse + "]";
	}
	

	
	
}
