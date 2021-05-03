package com.bornfire.entity;

public class LDAPCrl {

	private String serialNumber;
	private String revocationDate;
	
	
	public LDAPCrl() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public LDAPCrl(String serialNumber, String revocationDate) {
		super();
		this.serialNumber = serialNumber;
		this.revocationDate = revocationDate;
	}


	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getRevocationDate() {
		return revocationDate;
	}
	public void setRevocationDate(String revocationDate) {
		this.revocationDate = revocationDate;
	}
	
}
