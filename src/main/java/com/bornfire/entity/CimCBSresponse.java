package com.bornfire.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "data", "status" })

public class CimCBSresponse {

	@JsonProperty("data")
	private CimCBSresponseData data;
	
	@JsonProperty("status")
	private CimCBSresponseStatus status;

	@JsonProperty("data")
	public CimCBSresponseData getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(CimCBSresponseData data) {
		this.data = data;
	}

	@JsonProperty("status")
	public CimCBSresponseStatus getStatus() {
		return status;
	}

	@JsonProperty("status")
	public void setStatus(CimCBSresponseStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "CimCBSresponse [data=" + data + ", status=" + status + "]";
	}
	
	

	
}
