package com.bornfire.entity;

import java.util.Date;

import javax.xml.datatype.XMLGregorianCalendar;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

public class CimCBSrequestHeader {
	
	@JsonProperty("requestUUId")
	private String requestUUId;
	
	@JsonProperty("channelId")
	private String channelId;
	
	@JsonProperty("serviceRequestVersion")
	private String serviceRequestVersion;
	
	@JsonProperty("serviceRequestId")
	private String serviceRequestId;
	
	@JsonProperty("messageDateTime")
	private XMLGregorianCalendar messageDateTime;

	
	@JsonProperty("requestUUId")
	public String getRequestUUId() {
		return requestUUId;
	}

	@JsonProperty("requestUUId")
	public void setRequestUUId(String requestUUId) {
		this.requestUUId = requestUUId;
	}

	@JsonProperty("channelId")
	public String getChannelId() {
		return channelId;
	}

	@JsonProperty("channelId")
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	@JsonProperty("serviceRequestVersion")
	public String getServiceRequestVersion() {
		return serviceRequestVersion;
	}

	@JsonProperty("serviceRequestVersion")
	public void setServiceRequestVersion(String serviceRequestVersion) {
		this.serviceRequestVersion = serviceRequestVersion;
	}

	@JsonProperty("serviceRequestId")
	public String getServiceRequestId() {
		return serviceRequestId;
	}

	@JsonProperty("serviceRequestId")
	public void setServiceRequestId(String serviceRequestId) {
		this.serviceRequestId = serviceRequestId;
	}

	@JsonProperty("messageDateTime")
	public XMLGregorianCalendar getMessageDateTime() {
		return messageDateTime;
	}

	@JsonProperty("messageDateTime")
	public void setMessageDateTime(XMLGregorianCalendar messageDateTime) {
		this.messageDateTime = messageDateTime;
	}

	@Override
	public String toString() {
		return "CimCBSrequestHeader [requestUUId=" + requestUUId + ", channelId=" + channelId
				+ ", serviceRequestVersion=" + serviceRequestVersion + ", serviceRequestId=" + serviceRequestId
				+ ", messageDateTime=" + messageDateTime + "]";
	}
	
	
}
