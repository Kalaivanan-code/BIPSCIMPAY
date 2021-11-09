package com.bornfire.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CimCBSrequestGLData {

	@JsonProperty("transactionNo")
	private String transactionNo;
	
	@JsonProperty("batchNo")
	private String batchNo;
	
	@JsonProperty("module")
	private String module;
	
	@JsonProperty("transactionDetails")
	private List<CimCBSrequestGLDataTranDet> transactionDetails;

	@JsonProperty("transactionNo")
	public String getTransactionNo() {
		return transactionNo;
	}

	@JsonProperty("transactionNo")
	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}

	@JsonProperty("batchNo")
	public String getBatchNo() {
		return batchNo;
	}

	@JsonProperty("batchNo")
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	@JsonProperty("module")
	public String getModule() {
		return module;
	}

	@JsonProperty("module")
	public void setModule(String module) {
		this.module = module;
	}

	@JsonProperty("transactionDetails")
	public List<CimCBSrequestGLDataTranDet> getTransactionDetails() {
		return transactionDetails;
	}

	@JsonProperty("transactionDetails")
	public void setTransactionDetails(List<CimCBSrequestGLDataTranDet> transactionDetails) {
		this.transactionDetails = transactionDetails;
	}

	@Override
	public String toString() {
		return "CimCBSrequestGLData [transactionNo=" + transactionNo + ", batchNo=" + batchNo + ", module=" + module
				+ ", transactionDetails=" + transactionDetails + "]";
	}
	
	
	
}
