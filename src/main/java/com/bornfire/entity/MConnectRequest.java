package com.bornfire.entity;

import java.math.BigDecimal;

public class MConnectRequest {
	
	
	String initiateAcc;
	String initiateBank;
	String receiveAcc;
	String receiveBank;
	BigDecimal tranAmt;
	public String getInitiateAcc() {
		return initiateAcc;
	}
	public void setInitiateAcc(String initiateAcc) {
		this.initiateAcc = initiateAcc;
	}
	public String getInitiateBank() {
		return initiateBank;
	}
	public void setInitiateBank(String initiateBank) {
		this.initiateBank = initiateBank;
	}
	public String getReceiveAcc() {
		return receiveAcc;
	}
	public void setReceiveAcc(String receiveAcc) {
		this.receiveAcc = receiveAcc;
	}
	public String getReceiveBank() {
		return receiveBank;
	}
	public void setReceiveBank(String receiveBank) {
		this.receiveBank = receiveBank;
	}
	public BigDecimal getTranAmt() {
		return tranAmt;
	}
	public void setTranAmt(BigDecimal tranAmt) {
		this.tranAmt = tranAmt;
	}
	public MConnectRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	public MConnectRequest(String initiateAcc, String initiateBank, String receiveAcc, String receiveBank,
			BigDecimal tranAmt) {
		super();
		this.initiateAcc = initiateAcc;
		this.initiateBank = initiateBank;
		this.receiveAcc = receiveAcc;
		this.receiveBank = receiveBank;
		this.tranAmt = tranAmt;
	}
	
	

}
