package com.bornfire.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RTPTransferStatusResponse {

	private String TranId;
	private String ReqId;
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
		return "RTPTransferStatusResponse [TranId=" + TranId + "ReqId=" + ReqId +", RtpResponse=" + RtpResponse + "]";
	}
	
	
}
