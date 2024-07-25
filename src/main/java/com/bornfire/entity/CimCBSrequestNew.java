package com.bornfire.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

public class CimCBSrequestNew {
	
	@JsonProperty("header")
	private CimCBSrequestHeader header;
	
	@JsonProperty("data")
	private CimCBSrequestDatanew data;

	@JsonProperty("header")
	public CimCBSrequestHeader getHeader() {
		return header;
	}

	@JsonProperty("header")
	public void setHeader(CimCBSrequestHeader header) {
		this.header = header;
	}

	@JsonProperty("data")
	public CimCBSrequestDatanew getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(CimCBSrequestDatanew data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "CimCBSrequest [header=" + header + ", data=" + data + "]";
	}
	
	
	
	
}
