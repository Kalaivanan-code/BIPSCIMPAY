package com.bornfire.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QrPaymentStatusData {

	@JsonProperty("transactionNo")
	private String TransactionNo;
	
	@JsonProperty("seqUniqueId")
	private String SeqUniqueId;

	public String getTransactionNo() {
		return TransactionNo;
	}

	public void setTransactionNo(String transactionNo) {
		TransactionNo = transactionNo;
	}

	public String getSeqUniqueId() {
		return SeqUniqueId;
	}

	public void setSeqUniqueId(String seqUniqueId) {
		SeqUniqueId = seqUniqueId;
	}

	@Override
	public String toString() {
		return "QrPaymentStatusData [TransactionNo=" + TransactionNo + ", SeqUniqueId=" + SeqUniqueId + "]";
	}

	public QrPaymentStatusData(String transactionNo, String seqUniqueId) {
		super();
		TransactionNo = transactionNo;
		SeqUniqueId = seqUniqueId;
	}



	public QrPaymentStatusData() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
