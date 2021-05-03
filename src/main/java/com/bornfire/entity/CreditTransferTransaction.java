package com.bornfire.entity;

public class CreditTransferTransaction {

	private String instrID;
	private String endToEndID;
	private String txID;
	private String trCurrency;
	private String trAmt;
	private String debrAgent;
	private String debrAgentAccount;
	private String credrAgent;
	private String credrAgentAccount;
	private String debrName;
	private String debrAccountNumber;
	private String credrName;
	private String credrAccountNumber;
	
	
	
	public CreditTransferTransaction() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CreditTransferTransaction(String instrID, String endToEndID, String txID, String trCurrency, String trAmt,
			String debrAgent, String debrAgentAccount, String credrAgent, String credrAgentAccount, String debrName,
			String debrAccountNumber, String credrName, String credrAccountNumber) {
		super();
		this.instrID = instrID;
		this.endToEndID = endToEndID;
		this.txID = txID;
		this.trCurrency = trCurrency;
		this.trAmt = trAmt;
		this.debrAgent = debrAgent;
		this.debrAgentAccount = debrAgentAccount;
		this.credrAgent = credrAgent;
		this.credrAgentAccount = credrAgentAccount;
		this.debrName = debrName;
		this.debrAccountNumber = debrAccountNumber;
		this.credrName = credrName;
		this.credrAccountNumber = credrAccountNumber;
	}
	
	

	@Override
	public String toString() {
		return "CreditTransferTransaction [instrID=" + instrID + ", endToEndID=" + endToEndID + ", txID=" + txID
				+ ", trCurrency=" + trCurrency + ", trAmt=" + trAmt + ", debrAgent=" + debrAgent + ", debrAgentAccount="
				+ debrAgentAccount + ", credrAgent=" + credrAgent + ", credrAgentAccount=" + credrAgentAccount
				+ ", debrName=" + debrName + ", debrAccountNumber=" + debrAccountNumber + ", credrName=" + credrName
				+ ", credrAccountNumber=" + credrAccountNumber + "]";
	}

	public String getInstrID() {
		return instrID;
	}

	public void setInstrID(String instrID) {
		this.instrID = instrID;
	}

	public String getEndToEndID() {
		return endToEndID;
	}

	public void setEndToEndID(String endToEndID) {
		this.endToEndID = endToEndID;
	}

	public String getTxID() {
		return txID;
	}

	public void setTxID(String txID) {
		this.txID = txID;
	}

	public String getTrCurrency() {
		return trCurrency;
	}

	public void setTrCurrency(String trCurrency) {
		this.trCurrency = trCurrency;
	}

	public String getTrAmt() {
		return trAmt;
	}

	public void setTrAmt(String trAmt) {
		this.trAmt = trAmt;
	}

	public String getDebrAgent() {
		return debrAgent;
	}

	public void setDebrAgent(String debrAgent) {
		this.debrAgent = debrAgent;
	}

	public String getDebrAgentAccount() {
		return debrAgentAccount;
	}

	public void setDebrAgentAccount(String debrAgentAccount) {
		this.debrAgentAccount = debrAgentAccount;
	}

	public String getCredrAgent() {
		return credrAgent;
	}

	public void setCredrAgent(String credrAgent) {
		this.credrAgent = credrAgent;
	}

	public String getCredrAgentAccount() {
		return credrAgentAccount;
	}

	public void setCredrAgentAccount(String credrAgentAccount) {
		this.credrAgentAccount = credrAgentAccount;
	}

	public String getDebrName() {
		return debrName;
	}

	public void setDebrName(String debrName) {
		this.debrName = debrName;
	}

	public String getDebrAccountNumber() {
		return debrAccountNumber;
	}

	public void setDebrAccountNumber(String debrAccountNumber) {
		this.debrAccountNumber = debrAccountNumber;
	}

	public String getCredrName() {
		return credrName;
	}

	public void setCredrName(String credrName) {
		this.credrName = credrName;
	}

	public String getCredrAccountNumber() {
		return credrAccountNumber;
	}

	public void setCredrAccountNumber(String credrAccountNumber) {
		this.credrAccountNumber = credrAccountNumber;
	}
	
	
}
