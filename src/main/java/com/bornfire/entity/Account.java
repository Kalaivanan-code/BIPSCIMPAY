package com.bornfire.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Account {
	@JsonProperty("SchemeName")
	private String SchemeName;

	@JsonProperty("Identification")
	private String Identification;

	@JsonProperty("SchemeName")
	public String getSchemeName() {
		return SchemeName;
	}

	public void setSchemeName(String SchemeName) {
		this.SchemeName = SchemeName;
	}

	@JsonProperty("Identification")
	public String getIdentification() {
		return Identification;
	}
	public void setIdentification(String Identification) {
		this.Identification = Identification;
	}

	@Override
	public String toString() {
		return "Account [SchemeName=" + SchemeName + ", Identification=" + Identification + "]";
	}

	

}
