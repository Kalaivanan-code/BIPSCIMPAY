package com.bornfire.entity;

import java.util.List;

public class BulkDebitFndTransferRequest {

	private List<BulkDebitRemitterAccount> RemitterAccount;
	private ToAccount BenAccount;
	
	public BulkDebitFndTransferRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BulkDebitFndTransferRequest(List<BulkDebitRemitterAccount> remitterAccount, ToAccount benAccount) {
		super();
		RemitterAccount = remitterAccount;
		BenAccount = benAccount;
	}

	public List<BulkDebitRemitterAccount> getRemitterAccount() {
		return RemitterAccount;
	}

	public void setRemitterAccount(List<BulkDebitRemitterAccount> remitterAccount) {
		RemitterAccount = remitterAccount;
	}

	public ToAccount getBenAccount() {
		return BenAccount;
	}

	public void setBenAccount(ToAccount benAccount) {
		BenAccount = benAccount;
	}
	
	
}
