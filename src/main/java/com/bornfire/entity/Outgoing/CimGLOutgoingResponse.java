package com.bornfire.entity.Outgoing;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "data", "status" })
public class CimGLOutgoingResponse {
	@JsonProperty("data")
	private CimGLOutgoingData data;
	
	@JsonProperty("status")
	private CimGLresponseStatus status;

	@JsonProperty("data")
	public CimGLOutgoingData getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(CimGLOutgoingData data) {
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

	public CimGLOutgoingResponse(CimGLOutgoingData data, CimGLresponseStatus status) {
		super();
		this.data = data;
		this.status = status;
	}

	public CimGLOutgoingResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

}
