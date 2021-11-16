package com.bornfire.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CimCBSCustDocResponse {
	@JsonProperty("data")
	private CimCBSCustDocResponseData data;
	@JsonProperty("status")
	private CimCBSCustDocResponseStatus status;
	
	@JsonProperty("data")
	public CimCBSCustDocResponseData getData() {
		return data;
	}
	@JsonProperty("data")
	public void setData(CimCBSCustDocResponseData data) {
		this.data = data;
	}
	@JsonProperty("status")
	public CimCBSCustDocResponseStatus getStatus() {
		return status;
	}
	@JsonProperty("status")
	public void setStatus(CimCBSCustDocResponseStatus status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "CimCBSCustDocResponse [data=" + data + ", status=" + status + "]";
	}
	
	
	
}
