package com.bornfire.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;


public class SCAAthenticationResponse implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -810040690818347587L;

	@JsonProperty("Links")
	private Links Links;

	@JsonProperty("SCAStatus")
	private String SCAStatus;

	@JsonProperty("ErrorCode")
	private Integer ErrorCode;
	@JsonProperty("Description")

	private String Description;
	
	
	
	@JsonProperty("Links")
	public Links getLinks() {
		return Links;
	}
	@JsonProperty("Links")
	public void setLinks(Links links) {
		Links = links;
	}

	@JsonProperty("SCAStatus")
	public String getSCAStatus() {
		return SCAStatus;
	}

	@JsonProperty("SCAStatus")
	public void setSCAStatus(String sCAStatus) {
		SCAStatus = sCAStatus;
	}

	@JsonProperty("ErrorCode")
	public Integer getErrorCode() {
		return ErrorCode;
	}

	@JsonProperty("ErrorCode")
	public void setErrorCode(Integer errorCode) {
		ErrorCode = errorCode;
	}

	@JsonProperty("Description")

	public String getDescription() {
		return Description;
	}
	@JsonProperty("Description")

	public void setDescription(String description) {
		Description = description;
	}

	public SCAAthenticationResponse(Integer errorCode, String description) {
		super();
		ErrorCode = errorCode;
		Description = description;
	}

	public SCAAthenticationResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "SCAAthenticationResponse [Links=" + Links + ", SCAStatus=" + SCAStatus + ", ErrorCode=" + ErrorCode
				+ ", Description=" + Description + "]";
	}

	

	

}