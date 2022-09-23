package com.bornfire.entity;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class RTPTransferRequestStatus {

	
	@Valid
	private String tranID;
	
	private String reqId;
	
	private String tranAmt;

	public String getTranID() {
		return tranID;
	}

	public void setTranId(String tranID) {
		this.tranID = tranID;
	}

	public String getReqId() {
		return reqId;
	}

	public void setRefId(String reqId) {
		this.reqId = reqId;
	}

	public String getTranAmt() {
		return tranAmt;
	}

	public void setTranAmt(String tranAmt) {
		this.tranAmt = tranAmt;
	}
	
	
	
	
	
	
	
}
