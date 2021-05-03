package com.bornfire.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserRegistrationResponse implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -810040690818347587L;

	
	private Links Links;

	private String RecordId;

	@JsonProperty("Links")
	public Links getLinks() {
		return Links;
	}

	public void setLinks(Links Links) {
		this.Links = Links;
	}
	@JsonProperty("RecordId")
	public String getRecordId() {
		return RecordId;
	}

	public void setRecordId(String RecordId) {
		this.RecordId = RecordId;
	}

	@Override
	public String toString() {
		return "ClassPojo [Links = " + Links + ", RecordId = " + RecordId + "]";
	}

}
