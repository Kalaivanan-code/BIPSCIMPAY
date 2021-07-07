package com.bornfire.entity;

import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonInclude(JsonInclude.Include.NON_NULL)

public class McConsentOutwardAccessResponse {
	private static final long serialVersionUID = 939876431961625127L;
	
	@JsonProperty("ConsentId")
	private String consentID;
	
	@JsonProperty("Status")
	private String status;
	
	@JsonProperty("Permissions")
	private List<String> permissions;
	
	@JsonProperty("Links")
	private Links Links;
	
	@JsonProperty("ConsentId")
	public String getConsentID() {
		return consentID;
	}
	public void setConsentID(String consentID) {
		this.consentID = consentID;
	}
	
	@JsonProperty("Status")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	@JsonProperty("Permissions")
	public List<String> getPermissions() {
		return permissions;
	}
	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}
	

	
	@JsonProperty("Links")
	public Links getLinks() {
		return Links;
	}
	public void setLinks(Links links) {
		Links = links;
	}

	
}
