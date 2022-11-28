package com.bornfire.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CimMerchantQRcodeResponse {
	private String base64QR;
	
	@JsonProperty("Error")
	private String Error;
	@JsonProperty("Error_Desc")
	private List<String> Error_Desc;

	public String getBase64QR() {
		return base64QR;
	}

	public void setBase64QR(String base64qr) {
		base64QR = base64qr;
	}

	@JsonProperty("Error")
	public String getError() {
		return Error;
	}

	@JsonProperty("Error")
	public void setError(String error) {
		Error = error;
	}

	@JsonProperty("Error_Desc")
	public List<String> getError_Desc() {
		return Error_Desc;
	}
	@JsonProperty("Error_Desc")
	public void setError_Desc(List<String> error_Desc) {
		Error_Desc = error_Desc;
	}

	public CimMerchantQRcodeResponse(String error, List<String> error_Desc) {
		super();
		Error = error;
		Error_Desc = error_Desc;
	}

	public CimMerchantQRcodeResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
