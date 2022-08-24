package com.bornfire.upiqrcodeentity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Payee {

	@JsonProperty("addr")
	private String addr;
	
	@JsonProperty("name")
	private String name;
	
	
	@JsonProperty("seqNum")
	private String seqNum;
	
	
	@JsonProperty("type")
	private String type;
	
	@JsonProperty("MCC")
	private String mcc;
	
	@JsonProperty("Merchant")
	private Merchant merchant;

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSeqNum() {
		return seqNum;
	}

	public void setSeqNum(String seqNum) {
		this.seqNum = seqNum;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getmcc() {
		return mcc;
	}

	public void setmcc(String mcc) {
		this.mcc = mcc;
	}

	public Merchant getMerchant() {
		return merchant;
	}

	public void setMerchant(Merchant merchant) {
		this.merchant = merchant;
	}

	@Override
	public String toString() {
		return "Payee [addr=" + addr + ", name=" + name + ", seqNum=" + seqNum + ", type=" + type + ", MCC=" + mcc
				+ ", merchant=" + merchant + "]";
	}
	


}
