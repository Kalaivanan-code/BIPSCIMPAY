package com.bornfire.upiqrcodeentity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class NpciupiReqtransaction {

	
	@JsonProperty("id")
	private String ID;
	
	@JsonProperty("note")
	private String Note;
	
	@JsonProperty("refId")
	private String RefId;
	
	@JsonProperty("refUrl")
	private String RefUrl;
	
	@JsonProperty("ts")
	private String ts;
	
	@JsonProperty("custRef")
	private String CustRef;

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getNote() {
		return Note;
	}

	public void setNote(String note) {
		Note = note;
	}

	public String getRefId() {
		return RefId;
	}

	public void setRefId(String refId) {
		RefId = refId;
	}

	public String getRefUrl() {
		return RefUrl;
	}

	public void setRefUrl(String refUrl) {
		RefUrl = refUrl;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	public String getCustRef() {
		return CustRef;
	}

	public void setCustRef(String custRef) {
		CustRef = custRef;
	}

	@Override
	public String toString() {
		return "NpciupiReqtransaction [ID=" + ID + ", Note=" + Note + ", RefId=" + RefId + ", RefUrl=" + RefUrl
				+ ", ts=" + ts + ", CustRef=" + CustRef + "]";
	}
	


}
