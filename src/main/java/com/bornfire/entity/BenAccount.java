package com.bornfire.entity;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.bornfire.exception.TranAmount;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BenAccount {
	
	@NotBlank(message="Request UID Required")
	@JsonProperty("reqUniqueID")
	private String ReqUniqueId;
	
	@NotBlank(message="Beneficiary Name Required")
	@JsonProperty("benName")
	private String BenName;
	
	
	@JsonProperty("benAcctNumber")
	@NotBlank(message="Benefiiary Account Number Required") 
	private String BenAcctNumber;
	
	@JsonProperty("trAmt")
	@NotBlank(message="Transaction Amount Required")
	@TranAmount
	private String TrAmt;
	
	@JsonProperty("currencyCode")
	@NotBlank(message="Currency Code Required")
	private String CurrencyCode;
	
	@JsonProperty("trRmks")
	private String TrRmks;
	
	@JsonProperty("source")
	@NotBlank(message="Source Data Required")
	private String Source;
	
	
	@NotBlank(message="Beneficiary Bank Code Required")
	private String BankCode;

	public String getReqUniqueId() {
		return ReqUniqueId;
	}

	public void setReqUniqueId(String reqUniqueId) {
		ReqUniqueId = reqUniqueId;
	}

	public String getBenName() {
		return BenName;
	}

	public void setBenName(String benName) {
		BenName = benName;
	}

	public String getBenAcctNumber() {
		return BenAcctNumber;
	}

	public void setBenAcctNumber(String benAcctNumber) {
		BenAcctNumber = benAcctNumber;
	}

	public String getTrAmt() {
		return TrAmt;
	}

	public void setTrAmt(String trAmt) {
		TrAmt = trAmt;
	}

	public String getCurrencyCode() {
		return CurrencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		CurrencyCode = currencyCode;
	}

	public String getTrRmks() {
		return TrRmks;
	}

	public void setTrRmks(String trRmks) {
		TrRmks = trRmks;
	}

	public String getBankCode() {
		return BankCode;
	}

	public void setBankCode(String bankCode) {
		BankCode = bankCode;
	}

	
	
	public String getSource() {
		return Source;
	}

	public void setSource(String source) {
		Source = source;
	}

	@Override
	public String toString() {
		return "BenAccount [ReqUniqueId=" + ReqUniqueId + ", BenName=" + BenName + ", BenAcctNumber=" + BenAcctNumber
				+ ", TrAmt=" + TrAmt + ", CurrencyCode=" + CurrencyCode + ", TrRmks=" + TrRmks + ", Source=" + Source
				+ ", BankCode=" + BankCode + "]";
	}




	
	
	
}
