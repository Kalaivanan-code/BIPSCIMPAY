package com.bornfire.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QrNotificationRequestData {
	
	@JsonProperty("postToCBS")
	public Boolean postToCBS;
	
	@JsonProperty("transactionNo")
	public String transactionNo;
	
	@JsonProperty("initiatingChannel")
	public String initiatingChannel;
	
	@JsonProperty("notificationChannel")
	public String notificationChannel;
	
	@JsonProperty("initatorTransactionNo")
	public String initatorTransactionNo;
	
	@JsonProperty("initatorSubTransactionNo")
	public String initatorSubTransactionNo;
	
	@JsonProperty("customerName")
	public String customerName;
	
	@JsonProperty("fromAccountNo")
	public String fromAccountNo;
	
	@JsonProperty("toAccountNo")
	public String toAccountNo;
	
	@JsonProperty("transactionAmount")
	public String transactionAmount;
	
	@JsonProperty("transactionDate")
	public Date transactionDate;
	
	@JsonProperty("transactionCurrency")
	public String transactionCurrency;
	
	@JsonProperty("transactionParticularCode")
	public String transactionParticularCode;
	
	@JsonProperty("creditRemarks")
	public String creditRemarks;
	
	@JsonProperty("debitRemarks")
	public String debitRemarks;
	
	@JsonProperty("reservedField1")
	public String reservedField1;
	
	@JsonProperty("reservedField2")
	public String reservedField2;
	
	@JsonProperty("errorCode")
	public String errorCode;
	
	@JsonProperty("errorMessage")
	public String errorMessage;
	
	@JsonProperty("ipsMasterRefId")
	public String ipsMasterRefId;
	
	@JsonProperty("remitterBank")
	public String remitterBank;
	
	@JsonProperty("remitterBankCode")
	public String remitterBankCode;
	
	@JsonProperty("remitterSwiftCode")
	public String remitterSwiftCode;
	
	@JsonProperty("beneficiaryBank")
	public String beneficiaryBank;
	
	@JsonProperty("beneficiaryBankCode")
	public String beneficiaryBankCode;
	
	@JsonProperty("beneficiarySwiftCode")
	public String beneficiarySwiftCode;
	
	@JsonProperty("ipsxTranStatus")
	public Boolean ipsxTranStatus;
	
	@JsonProperty("transactionNoToCBS")
	public String transactionNoToCBS;
	
	@JsonProperty("pId")
	public String pId;
	
	@JsonProperty("psuDeviceId")
	public String psuDeviceId;
	
	
	public Boolean getPostToCBS() {
		return postToCBS;
	}
	public void setPostToCBS(Boolean postToCBS) {
		this.postToCBS = postToCBS;
	}
	public String getTransactionNo() {
		return transactionNo;
	}
	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}
	public String getInitiatingChannel() {
		return initiatingChannel;
	}
	public void setInitiatingChannel(String initiatingChannel) {
		this.initiatingChannel = initiatingChannel;
	}
	public String getInitatorTransactionNo() {
		return initatorTransactionNo;
	}
	public void setInitatorTransactionNo(String initatorTransactionNo) {
		this.initatorTransactionNo = initatorTransactionNo;
	}
	public String getInitatorSubTransactionNo() {
		return initatorSubTransactionNo;
	}
	public void setInitatorSubTransactionNo(String initatorSubTransactionNo) {
		this.initatorSubTransactionNo = initatorSubTransactionNo;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getFromAccountNo() {
		return fromAccountNo;
	}
	public void setFromAccountNo(String fromAccountNo) {
		this.fromAccountNo = fromAccountNo;
	}
	public String getToAccountNo() {
		return toAccountNo;
	}
	public void setToAccountNo(String toAccountNo) {
		this.toAccountNo = toAccountNo;
	}
	public String getTransactionAmount() {
		return transactionAmount;
	}
	public void setTransactionAmount(String transactionAmount) {
		this.transactionAmount = transactionAmount;
	}
	public Date getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}
	public String getTransactionCurrency() {
		return transactionCurrency;
	}
	public void setTransactionCurrency(String transactionCurrency) {
		this.transactionCurrency = transactionCurrency;
	}
	public String getTransactionParticularCode() {
		return transactionParticularCode;
	}
	public void setTransactionParticularCode(String transactionParticularCode) {
		this.transactionParticularCode = transactionParticularCode;
	}
	public String getCreditRemarks() {
		return creditRemarks;
	}
	public void setCreditRemarks(String creditRemarks) {
		this.creditRemarks = creditRemarks;
	}
	public String getDebitRemarks() {
		return debitRemarks;
	}
	public void setDebitRemarks(String debitRemarks) {
		this.debitRemarks = debitRemarks;
	}
	public String getReservedField1() {
		return reservedField1;
	}
	public void setReservedField1(String reservedField1) {
		this.reservedField1 = reservedField1;
	}
	public String getReservedField2() {
		return reservedField2;
	}
	public void setReservedField2(String reservedField2) {
		this.reservedField2 = reservedField2;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getIpsMasterRefId() {
		return ipsMasterRefId;
	}
	public void setIpsMasterRefId(String ipsMasterRefId) {
		this.ipsMasterRefId = ipsMasterRefId;
	}
	public String getRemitterBank() {
		return remitterBank;
	}
	public void setRemitterBank(String remitterBank) {
		this.remitterBank = remitterBank;
	}
	public String getRemitterBankCode() {
		return remitterBankCode;
	}
	public void setRemitterBankCode(String remitterBankCode) {
		this.remitterBankCode = remitterBankCode;
	}
	public String getRemitterSwiftCode() {
		return remitterSwiftCode;
	}
	public void setRemitterSwiftCode(String remitterSwiftCode) {
		this.remitterSwiftCode = remitterSwiftCode;
	}
	public String getBeneficiaryBank() {
		return beneficiaryBank;
	}
	public void setBeneficiaryBank(String beneficiaryBank) {
		this.beneficiaryBank = beneficiaryBank;
	}
	public String getBeneficiaryBankCode() {
		return beneficiaryBankCode;
	}
	public void setBeneficiaryBankCode(String beneficiaryBankCode) {
		this.beneficiaryBankCode = beneficiaryBankCode;
	}
	public Boolean getIpsxTranStatus() {
		return ipsxTranStatus;
	}
	public void setIpsxTranStatus(Boolean ipsxTranStatus) {
		this.ipsxTranStatus = ipsxTranStatus;
	}
	public String getTransactionNoToCBS() {
		return transactionNoToCBS;
	}
	public void setTransactionNoToCBS(String transactionNoToCBS) {
		this.transactionNoToCBS = transactionNoToCBS;
	}
	public String getpId() {
		return pId;
	}
	public void setpId(String pId) {
		this.pId = pId;
	}
	public String getPsuDeviceId() {
		return psuDeviceId;
	}
	public void setPsuDeviceId(String psuDeviceId) {
		this.psuDeviceId = psuDeviceId;
	}

	public String getBeneficiarySwiftCode() {
		return beneficiarySwiftCode;
	}

	public void setBeneficiarySwiftCode(String beneficiarySwiftCode) {
		this.beneficiarySwiftCode = beneficiarySwiftCode;
	}
	
	

	public String getNotificationChannel() {
		return notificationChannel;
	}
	public void setNotificationChannel(String notificationChannel) {
		this.notificationChannel = notificationChannel;
	}
	public QrNotificationRequestData(Boolean postToCBS, String transactionNo, String initiatingChannel,String notificationChannel,
									 String initatorTransactionNo, String initatorSubTransactionNo, String customerName, String fromAccountNo,
									 String toAccountNo, String transactionAmount, Date transactionDate, String transactionCurrency,
									 String transactionParticularCode, String creditRemarks, String debitRemarks, String reservedField1,
									 String reservedField2, String errorCode, String errorMessage, String ipsMasterRefId, String remitterBank,
									 String remitterBankCode, String remitterSwiftCode, String beneficiaryBank, String beneficiaryBankCode,
									 Boolean ipsxTranStatus, String transactionNoToCBS, String pId, String psuDeviceId,String beneficiarySwiftCode) {
		super();
		this.postToCBS = postToCBS;
		this.transactionNo = transactionNo;
		this.initiatingChannel = initiatingChannel;
		this.notificationChannel = notificationChannel;
		this.initatorTransactionNo = initatorTransactionNo;
		this.initatorSubTransactionNo = initatorSubTransactionNo;
		this.customerName = customerName;
		this.fromAccountNo = fromAccountNo;
		this.toAccountNo = toAccountNo;
		this.transactionAmount = transactionAmount;
		this.transactionDate = transactionDate;
		this.transactionCurrency = transactionCurrency;
		this.transactionParticularCode = transactionParticularCode;
		this.creditRemarks = creditRemarks;
		this.debitRemarks = debitRemarks;
		this.reservedField1 = reservedField1;
		this.reservedField2 = reservedField2;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.ipsMasterRefId = ipsMasterRefId;
		this.remitterBank = remitterBank;
		this.remitterBankCode = remitterBankCode;
		this.remitterSwiftCode = remitterSwiftCode;
		this.beneficiaryBank = beneficiaryBank;
		this.beneficiaryBankCode = beneficiaryBankCode;
		this.ipsxTranStatus = ipsxTranStatus;
		this.transactionNoToCBS = transactionNoToCBS;
		this.pId = pId;
		this.psuDeviceId = psuDeviceId;
		this.beneficiarySwiftCode = beneficiarySwiftCode;
	}
	public QrNotificationRequestData() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "QrNotificationRequestData [postToCBS=" + postToCBS + ", transactionNo=" + transactionNo
				+ ", initiatingChannel=" + initiatingChannel +", notificationChannel=" + notificationChannel+ ", initatorTransactionNo=" + initatorTransactionNo
				+ ", initatorSubTransactionNo=" + initatorSubTransactionNo + ", customerName=" + customerName
				+ ", fromAccountNo=" + fromAccountNo + ", toAccountNo=" + toAccountNo + ", transactionAmount="
				+ transactionAmount + ", transactionDate=" + transactionDate + ", transactionCurrency="
				+ transactionCurrency + ", transactionParticularCode=" + transactionParticularCode + ", creditRemarks="
				+ creditRemarks + ", debitRemarks=" + debitRemarks + ", reservedField1=" + reservedField1
				+ ", reservedField2=" + reservedField2 + ", errorCode=" + errorCode + ", errorMessage=" + errorMessage
				+ ", ipsMasterRefId=" + ipsMasterRefId + ", remitterBank=" + remitterBank + ", remitterBankCode="
				+ remitterBankCode + ", remitterSwiftCode=" + remitterSwiftCode + ", beneficiaryBank=" + beneficiaryBank
				+ ", beneficiaryBankCode=" + beneficiaryBankCode + ", ipsxTranStatus=" + ipsxTranStatus
				+ ", transactionNoToCBS=" + transactionNoToCBS + ", pId=" + pId + ", psuDeviceId=" + psuDeviceId
				+ ", beneficiarySwiftCode=" + beneficiarySwiftCode + "]";
	}
	
	


}
