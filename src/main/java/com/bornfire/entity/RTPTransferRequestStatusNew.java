package com.bornfire.entity;

public class RTPTransferRequestStatusNew {

 private String TransactionNo;
 private String TranId;
 private String TransactionDate;
 private String ReferenceId;
 private String ToAccountNumber;
 private String TransactionAmount;
 private String ReceiptNumber;
 private String IsSuccess;
 private String StatusCode;
 private String Message;
 private String ProductType;
public String getTransactionNo() {
	return TransactionNo;
}
public void setTransactionNo(String transactionNo) {
	TransactionNo = transactionNo;
}
public String getTranId() {
	return TranId;
}
public void setTranId(String tranId) {
	TranId = tranId;
}
public String getTransactionDate() {
	return TransactionDate;
}
public void setTransactionDate(String transactionDate) {
	TransactionDate = transactionDate;
}
public String getReferenceId() {
	return ReferenceId;
}
public void setReferenceId(String referenceId) {
	ReferenceId = referenceId;
}
public String getToAccountNumber() {
	return ToAccountNumber;
}
public void setToAccountNumber(String toAccountNumber) {
	ToAccountNumber = toAccountNumber;
}
public String getTransactionAmount() {
	return TransactionAmount;
}
public void setTransactionAmount(String transactionAmount) {
	TransactionAmount = transactionAmount;
}
public String getReceiptNumber() {
	return ReceiptNumber;
}
public void setReceiptNumber(String receiptNumber) {
	ReceiptNumber = receiptNumber;
}
public String getIsSuccess() {
	return IsSuccess;
}
public void setIsSuccess(String isSuccess) {
	IsSuccess = isSuccess;
}
public String getStatusCode() {
	return StatusCode;
}
public void setStatusCode(String statusCode) {
	StatusCode = statusCode;
}
public String getMessage() {
	return Message;
}
public void setMessage(String message) {
	Message = message;
}
public String getProductType() {
	return ProductType;
}
public void setProductType(String productType) {
	ProductType = productType;
}
@Override
public String toString() {
	return "RTPTransferRequestStatusNew [TransactionNo=" + TransactionNo + ", TranId=" + TranId + ", TransactionDate="
			+ TransactionDate + ", ReferenceId=" + ReferenceId + ", ToAccountNumber=" + ToAccountNumber
			+ ", TransactionAmount=" + TransactionAmount + ", ReceiptNumber=" + ReceiptNumber + ", IsSuccess="
			+ IsSuccess + ", StatusCode=" + StatusCode + ", Message=" + Message + ", ProductType=" + ProductType + "]";
}
 
 
 
}

