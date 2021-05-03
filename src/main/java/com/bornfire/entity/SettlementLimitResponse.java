package com.bornfire.entity;

public class SettlementLimitResponse {
	private String amt;
	private String usedAmt;
	private String rmngAmt;
	private String usedPcdg;
	public SettlementLimitResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public SettlementLimitResponse(String amt, String usedAmt, String rmngAmt, String usedPcdg) {
		super();
		this.amt = amt;
		this.usedAmt = usedAmt;
		this.rmngAmt = rmngAmt;
		this.usedPcdg = usedPcdg;
	}
	public String getAmt() {
		return amt;
	}
	public void setAmt(String amt) {
		this.amt = amt;
	}
	public String getUsedAmt() {
		return usedAmt;
	}
	public void setUsedAmt(String usedAmt) {
		this.usedAmt = usedAmt;
	}
	public String getRmngAmt() {
		return rmngAmt;
	}
	public void setRmngAmt(String rmngAmt) {
		this.rmngAmt = rmngAmt;
	}
	public String getUsedPcdg() {
		return usedPcdg;
	}
	public void setUsedPcdg(String usedPcdg) {
		this.usedPcdg = usedPcdg;
	}
	
	
}
