package com.bornfire.entity;

import java.util.Date;

import javax.xml.datatype.XMLGregorianCalendar;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QrNotificationRequestHeader {
	
	@JsonProperty("requestUUId")
	public String RequestUUId;
	
	@JsonProperty("channelId")
	public String ChannelId;
	
	@JsonProperty("serviceRequestVersion")
	public String serviceRequestVersion;
	
	@JsonProperty("serviceRequestId")
	public String serviceRequestId;
	
	@JsonProperty("messageDateTime")
	public XMLGregorianCalendar messageDateTime;
	
	@JsonProperty("countryCode")
	public String countryCode;
	
	
	public String getRequestUUId() {
		return RequestUUId;
	}
	public void setRequestUUId(String requestUUId) {
		RequestUUId = requestUUId;
	}
	public String getChannelId() {
		return ChannelId;
	}
	public void setChannelId(String channelId) {
		ChannelId = channelId;
	}
	public String getServiceRequestVersion() {
		return serviceRequestVersion;
	}
	public void setServiceRequestVersion(String serviceRequestVersion) {
		this.serviceRequestVersion = serviceRequestVersion;
	}
	public String getServiceRequestId() {
		return serviceRequestId;
	}
	public void setServiceRequestId(String serviceRequestId) {
		this.serviceRequestId = serviceRequestId;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public XMLGregorianCalendar getMessageDateTime() {
		return messageDateTime;
	}
	public void setMessageDateTime(XMLGregorianCalendar messageDateTime) {
		this.messageDateTime = messageDateTime;
	}
	public QrNotificationRequestHeader() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "QrNotificationRequestHeader [RequestUUId=" + RequestUUId + ", ChannelId=" + ChannelId
				+ ", serviceRequestVersion=" + serviceRequestVersion + ", serviceRequestId=" + serviceRequestId
				+ ", messageDateTime=" + messageDateTime + ", countryCode=" + countryCode + "]";
	}
	public QrNotificationRequestHeader(String requestUUId, String channelId, String serviceRequestVersion,
			String serviceRequestId, XMLGregorianCalendar messageDateTime, String countryCode) {
		super();
		RequestUUId = requestUUId;
		ChannelId = channelId;
		this.serviceRequestVersion = serviceRequestVersion;
		this.serviceRequestId = serviceRequestId;
		this.messageDateTime = messageDateTime;
		this.countryCode = countryCode;
	}
	
	
	
	
	
	
	

}
