package com.bornfire.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)

public class RTPbulkTransferResponse {
	private String TranID;
	private String TranDateTime;
	private String Status;
	
	
	public RTPbulkTransferResponse() {
		super();
		// TODO Auto-generated constructor stub
	}


	


	public RTPbulkTransferResponse(String tranID, String tranDateTime) {
		super();
		TranID = tranID;
		TranDateTime = tranDateTime;
		
	}





	




	public String getTranID() {
		return TranID;
	}


	public void setTranID(String tranID) {
		TranID = tranID;
	}


	public String getTranDateTime() {
		return TranDateTime;
	}


	public void setTranDateTime(String tranDateTime) {
		TranDateTime = tranDateTime;
	}


	public String getStatus() {
		return Status;
	}


	public void setStatus(String status) {
		Status = status;
	}





	@Override
	public String toString() {
		return "RTPbulkTransferResponse [TranID=" + TranID + ", TranDateTime=" + TranDateTime + ", Status=" + Status
				+ "]";
	}






}
