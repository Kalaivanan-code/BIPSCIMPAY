package com.bornfire.upiqrcodeentity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Invoice {

	@JsonProperty("date")
	private String date;
	
	@JsonProperty("num")
	private String num;
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("FxList")
	private List<BaseCurr> FxList;
	
	@JsonProperty("creditBIC")
	private String creditBIC;
	
	@JsonProperty("creditAccount")
	private String creditAccount;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<BaseCurr> getFxList() {
		return FxList;
	}

	public void setFxList(List<BaseCurr> fxList) {
		FxList = fxList;
	}

	public String getCreditBIC() {
		return creditBIC;
	}

	public void setCreditBIC(String creditBIC) {
		this.creditBIC = creditBIC;
	}

	public String getCreditAccount() {
		return creditAccount;
	}

	public void setCreditAccount(String creditAccount) {
		this.creditAccount = creditAccount;
	}

	@Override
	public String toString() {
		return "Invoice [date=" + date + ", num=" + num + ", name=" + name + ", FxList=" + FxList + ", creditBIC="
				+ creditBIC + ", creditAccount=" + creditAccount + "]";
	}
	
	
	
}
