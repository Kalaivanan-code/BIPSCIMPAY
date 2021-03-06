package com.bornfire.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CimCBSrequestGL {

	@JsonProperty("header")
	private CimCBSrequestGLHeader header;
	
	@JsonProperty("data")
	private CimCBSrequestGLData data;

	@JsonProperty("header")
	public CimCBSrequestGLHeader getHeader() {
		return header;
	}

	@JsonProperty("header")
	public void setHeader(CimCBSrequestGLHeader header) {
		this.header = header;
	}

	@JsonProperty("data")
	public CimCBSrequestGLData getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(CimCBSrequestGLData data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "CimCBSrequestGL [header=" + header + ", data=" + data + "]";
	}
	
	

}
