package com.bornfire.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Links {
	@JsonProperty("AuthoriseTransaction")
	private String AuthoriseTransaction;

	@JsonProperty("Self")
	private String Self;
	
	@JsonProperty("SCAStatus")
	private String SCAStatus;

	@JsonProperty("AuthoriseTransaction")
	public String getAuthoriseTransaction() {
		return AuthoriseTransaction;
	}

	@JsonProperty("AuthoriseTransaction")
	public void setAuthoriseTransaction(String AuthoriseTransaction) {
		this.AuthoriseTransaction = AuthoriseTransaction;
	}
	@JsonProperty("Self")
	public String getSelf() {
		return Self;
	}

	@JsonProperty("Self")
	public void setSelf(String Self) {
		this.Self = Self;
	}

	@JsonProperty("SCAStatus")
	public String getSCAStatus() {
		return SCAStatus;
	}

	@JsonProperty("SCAStatus")
	public void setSCAStatus(String sCAStatus) {
		SCAStatus = sCAStatus;
	}

	@Override
	public String toString() {
		return "ClassPojo [AuthoriseTransaction = " + AuthoriseTransaction + ", Self = " + Self + "]";
	}
}
