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

	@JsonProperty("Links")
	public Links getLinks() {
		return Links;
	}

	public void setLinks(Links links) {
		Links = links;
	}

	@JsonProperty("SCAStatus")
	public String getSCAStatus() {
		return SCAStatus;
	}

	public void setSCAStatus(String sCAStatus) {
		SCAStatus = sCAStatus;
	}

	

	

}