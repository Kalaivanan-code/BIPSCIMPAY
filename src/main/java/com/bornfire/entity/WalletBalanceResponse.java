package com.bornfire.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WalletBalanceResponse {

	private String WalletAcctNumber;
	C24FTResponseBalance Balance;
	
	
	
	public WalletBalanceResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	

	public WalletBalanceResponse(String walletAcctNumber, C24FTResponseBalance balance) {
		super();
		WalletAcctNumber = walletAcctNumber;
		Balance = balance;
	}



	@JsonProperty("WalletAccountNumber")

	public String getWalletAcctNumber() {
		return WalletAcctNumber;
	}
	
	
	public void setWalletAcctNumber(String walletAcctNumber) {
		WalletAcctNumber = walletAcctNumber;
	}
	
	@JsonProperty("Balance")

	public C24FTResponseBalance getBalance() {
		return Balance;
	}
	public void setBalance(C24FTResponseBalance balance) {
		Balance = balance;
	}
	
	
}
