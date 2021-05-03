package com.bornfire.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConsentAccessResponse implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 939876431961625127L;
	private String consentID;
	private String status;
	private XMLGregorianCalendar statusUpdateDateTime;
	private XMLGregorianCalendar creationDateTime;
	private List<String> permissions;
	private XMLGregorianCalendar expirationDateTime;
	private XMLGregorianCalendar transactionFromDateTime;
	private XMLGregorianCalendar transactionToDateTime;
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
	
	@JsonProperty("StatusUpdateDateTime")
	public XMLGregorianCalendar getStatusUpdateDateTime() {
		return statusUpdateDateTime;
	}
	public void setStatusUpdateDateTime(XMLGregorianCalendar statusUpdateDateTime) {
		this.statusUpdateDateTime = statusUpdateDateTime;
	}
	
	@JsonProperty("CreationDateTime")
	public XMLGregorianCalendar getCreationDateTime() {
		return creationDateTime;
	}
	public void setCreationDateTime(XMLGregorianCalendar creationDateTime) {
		this.creationDateTime = creationDateTime;
	}
	
	@JsonProperty("Permissions")
	public List<String> getPermissions() {
		return permissions;
	}
	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}
	
	@JsonProperty("ExpirationDateTime")
	public XMLGregorianCalendar getExpirationDateTime() {
		return expirationDateTime;
	}
	public void setExpirationDateTime(XMLGregorianCalendar expirationDateTime) {
		this.expirationDateTime = expirationDateTime;
	}
	
	@JsonProperty("TransactionFromDateTime")
	public XMLGregorianCalendar getTransactionFromDateTime() {
		return transactionFromDateTime;
	}
	public void setTransactionFromDateTime(XMLGregorianCalendar transactionFromDateTime) {
		this.transactionFromDateTime = transactionFromDateTime;
	}
	
	@JsonProperty("TransactionToDateTime")
	public XMLGregorianCalendar getTransactionToDateTime() {
		return transactionToDateTime;
	}
	public void setTransactionToDateTime(XMLGregorianCalendar transactionToDateTime) {
		this.transactionToDateTime = transactionToDateTime;
	}
	
	@JsonProperty("Links")
	public Links getLinks() {
		return Links;
	}
	public void setLinks(Links links) {
		Links = links;
	}
	
	
}
