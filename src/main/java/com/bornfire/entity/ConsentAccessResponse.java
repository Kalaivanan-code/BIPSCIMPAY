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
	@JsonProperty("ConsentId")
	private String consentID;
	@JsonProperty("Status")
	private String status;
	@JsonProperty("StatusUpdateDateTime")
	private XMLGregorianCalendar statusUpdateDateTime;
	@JsonProperty("CreationDateTime")
	private XMLGregorianCalendar creationDateTime;
	@JsonProperty("Permissions")
	private List<String> permissions;
	@JsonProperty("ExpirationDateTime")
	private XMLGregorianCalendar expirationDateTime;
	@JsonProperty("TransactionFromDateTime")
	private XMLGregorianCalendar transactionFromDateTime;
	@JsonProperty("TransactionToDateTime")
	private XMLGregorianCalendar transactionToDateTime;
	@JsonProperty("Links")
	private Links Links;
	
	
	@JsonProperty("ErrorCode")
	private Integer ErrorCode;
	@JsonProperty("Description")

	private String Description;
	
	
	
	@JsonProperty("ConsentId")
	public String getConsentID() {
		return consentID;
	}
	@JsonProperty("ConsentId")
	public void setConsentID(String consentID) {
		this.consentID = consentID;
	}
	
	@JsonProperty("Status")
	public String getStatus() {
		return status;
	}
	@JsonProperty("Status")
	public void setStatus(String status) {
		this.status = status;
	}
	
	@JsonProperty("StatusUpdateDateTime")
	public XMLGregorianCalendar getStatusUpdateDateTime() {
		return statusUpdateDateTime;
	}
	@JsonProperty("StatusUpdateDateTime")
	public void setStatusUpdateDateTime(XMLGregorianCalendar statusUpdateDateTime) {
		this.statusUpdateDateTime = statusUpdateDateTime;
	}
	
	@JsonProperty("CreationDateTime")
	public XMLGregorianCalendar getCreationDateTime() {
		return creationDateTime;
	}
	@JsonProperty("CreationDateTime")
	public void setCreationDateTime(XMLGregorianCalendar creationDateTime) {
		this.creationDateTime = creationDateTime;
	}
	
	@JsonProperty("Permissions")
	public List<String> getPermissions() {
		return permissions;
	}
	
	@JsonProperty("Permissions")
	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}
	
	@JsonProperty("ExpirationDateTime")
	public XMLGregorianCalendar getExpirationDateTime() {
		return expirationDateTime;
	}
	
	@JsonProperty("ExpirationDateTime")
	public void setExpirationDateTime(XMLGregorianCalendar expirationDateTime) {
		this.expirationDateTime = expirationDateTime;
	}
	
	@JsonProperty("TransactionFromDateTime")
	public XMLGregorianCalendar getTransactionFromDateTime() {
		return transactionFromDateTime;
	}
	@JsonProperty("TransactionFromDateTime")
	public void setTransactionFromDateTime(XMLGregorianCalendar transactionFromDateTime) {
		this.transactionFromDateTime = transactionFromDateTime;
	}
	
	@JsonProperty("TransactionToDateTime")
	public XMLGregorianCalendar getTransactionToDateTime() {
		return transactionToDateTime;
	}
	@JsonProperty("TransactionToDateTime")
	public void setTransactionToDateTime(XMLGregorianCalendar transactionToDateTime) {
		this.transactionToDateTime = transactionToDateTime;
	}

	
	@JsonProperty("Links")
	public Links getLinks() {
		return Links;
	}
	@JsonProperty("Links")
	public void setLinks(Links links) {
		Links = links;
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
	
	@Override
	public String toString() {
		return "ConsentAccessResponse [consentID=" + consentID + ", status=" + status + ", statusUpdateDateTime="
				+ statusUpdateDateTime + ", creationDateTime=" + creationDateTime + ", permissions=" + permissions
				+ ", expirationDateTime=" + expirationDateTime + ", transactionFromDateTime=" + transactionFromDateTime
				+ ", transactionToDateTime=" + transactionToDateTime + ", Links=" + Links + ", ErrorCode=" + ErrorCode
				+ ", Description=" + Description + "]";
	}
	public ConsentAccessResponse(Integer errorCode, String description) {
		super();
		ErrorCode = errorCode;
		Description = description;
	}
	public ConsentAccessResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
