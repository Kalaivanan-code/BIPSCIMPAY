package com.bornfire.entity;

import java.util.List;

public class WalletStatementResponse {
	private String WalletAccountNumber;
	private String WalletBalance;
	private List<WalletStatement> WalletStatement;
	
	public String getWalletAccountNumber() {
		return WalletAccountNumber;
	}
	public void setWalletAccountNumber(String walletAccountNumber) {
		WalletAccountNumber = walletAccountNumber;
	}
	public String getWalletBalance() {
		return WalletBalance;
	}
	public void setWalletBalance(String walletBalance) {
		WalletBalance = walletBalance;
	}
	public List<WalletStatement> getWalletStatement() {
		return WalletStatement;
	}
	public void setWalletStatement(List<WalletStatement> walletStatement) {
		WalletStatement = walletStatement;
	}
	
	
	
}
