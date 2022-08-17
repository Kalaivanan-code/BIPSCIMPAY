package com.bornfire.upiqrcodeentity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Institution {

	
	@JsonProperty("QrPayLoad")
	private String QrPayLoad;

	public String getQrPayLoad() {
		return QrPayLoad;
	}

	public void setQrPayLoad(String qrPayLoad) {
		QrPayLoad = qrPayLoad;
	}
	
	
}
