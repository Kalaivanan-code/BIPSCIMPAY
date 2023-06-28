package com.bornfire.entity;

public class QrPaymentStatusRequest {

	
	private String pID;
	private String merchantID;
	private String billNum;
	private String refNum;
	public String getpID() {
		return pID;
	}
	public void setpID(String pID) {
		this.pID = pID;
	}
	public String getMerchantID() {
		return merchantID;
	}
	public void setMerchantID(String merchantID) {
		this.merchantID = merchantID;
	}
	public String getBillNum() {
		return billNum;
	}
	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}
	public String getRefNum() {
		return refNum;
	}
	public void setRefNum(String refNum) {
		this.refNum = refNum;
	}
	@Override
	public String toString() {
		return "QrPaymentStatusRequest [pID=" + pID + ", merchantID=" + merchantID + ", billNum=" + billNum
				+ ", refNum=" + refNum + "]";
	}
	public QrPaymentStatusRequest(String pID, String merchantID, String billNum, String refNum) {
		super();
		this.pID = pID;
		this.merchantID = merchantID;
		this.billNum = billNum;
		this.refNum = refNum;
	}

	
}

