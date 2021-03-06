package com.bornfire.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConsentOutwardAccessAuthResponse {

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
