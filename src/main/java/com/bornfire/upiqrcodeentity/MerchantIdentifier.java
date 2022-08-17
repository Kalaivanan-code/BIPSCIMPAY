package com.bornfire.upiqrcodeentity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MerchantIdentifier {

	@JsonProperty("subCode")
	private String subCode;
	
	@JsonProperty("mid")
	private String mid;

	@JsonProperty("sid")
	private String sid;
	
	@JsonProperty("tid")
	private String tid;
	
	@JsonProperty("merchantType")
	private String merchantType;
	
	@JsonProperty("merchantGenre")
	private String merchantGenre;
	
	@JsonProperty("onBoardingType")
	private String onBoardingType;
	
	@JsonProperty("regId")
	private String regId;
	
	@JsonProperty("pinCode")
	private BigDecimal pinCode;
	
	@JsonProperty("tier")
	private String tier;
	
	@JsonProperty("merchantLoc")
	private String merchantLoc;
	
	@JsonProperty("merchantInstCode")
	private String merchantInstCode;

	public String getSubCode() {
		return subCode;
	}

	public void setSubCode(String subCode) {
		this.subCode = subCode;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getMerchantType() {
		return merchantType;
	}

	public void setMerchantType(String merchantType) {
		this.merchantType = merchantType;
	}

	public String getMerchantGenre() {
		return merchantGenre;
	}

	public void setMerchantGenre(String merchantGenre) {
		this.merchantGenre = merchantGenre;
	}

	public String getOnBoardingType() {
		return onBoardingType;
	}

	public void setOnBoardingType(String onBoardingType) {
		this.onBoardingType = onBoardingType;
	}

	public String getRegId() {
		return regId;
	}

	public void setRegId(String regId) {
		this.regId = regId;
	}

	public BigDecimal getPinCode() {
		return pinCode;
	}

	public void setPinCode(BigDecimal pinCode) {
		this.pinCode = pinCode;
	}

	public String getTier() {
		return tier;
	}

	public void setTier(String tier) {
		this.tier = tier;
	}

	public String getMerchantLoc() {
		return merchantLoc;
	}

	public void setMerchantLoc(String merchantLoc) {
		this.merchantLoc = merchantLoc;
	}

	public String getMerchantInstCode() {
		return merchantInstCode;
	}

	public void setMerchantInstCode(String merchantInstCode) {
		this.merchantInstCode = merchantInstCode;
	}
	
	
	
}
