package com.bornfire.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CimCBSCustDocRequest {
	
	@JsonProperty("header")
	private CimCBSCustDocHeader header;
	@JsonProperty("data")
	private CimCBSCustDocData data;
	
	@JsonProperty("header")
	public CimCBSCustDocHeader getHeader() {
		return header;
	}
	@JsonProperty("header")
	public void setHeader(CimCBSCustDocHeader header) {
		this.header = header;
	}
	@JsonProperty("data")
	public CimCBSCustDocData getData() {
		return data;
	}
	@JsonProperty("data")
	public void setData(CimCBSCustDocData data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "CimCBSCustDocRequest [header=" + header + ", data=" + data + "]";
	}

	
	
	
}
