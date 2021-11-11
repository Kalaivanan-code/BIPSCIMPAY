package com.bornfire.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "data", "status" })
public class CimGLresponse {
	@JsonProperty("data")
	private CimGLresponseData data;
	
	@JsonProperty("status")
	private CimGLresponseStatus status;

	@JsonProperty("data")
	public CimGLresponseData getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(CimGLresponseData data) {
		this.data = data;
	}

	@JsonProperty("status")
	public CimGLresponseStatus getStatus() {
		return status;
	}

	@JsonProperty("status")
	public void setStatus(CimGLresponseStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "CimGLresponse [data=" + data + ", status=" + status + "]";
	}

	public CimGLresponse(CimGLresponseData data, CimGLresponseStatus status) {
		super();
		this.data = data;
		this.status = status;
	}

	public CimGLresponse() {
		super();
		// TODO Auto-generated constructor stub
	}

}
