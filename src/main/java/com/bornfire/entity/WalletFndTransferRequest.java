package com.bornfire.entity;


public class WalletFndTransferRequest {
	private FrAccount FrAccount;
	private ToAccount ToAccount;
	private String CurrencyCode;
	private String Pan;
	private String TrAmt;
	private String TrRmks;
	private String Purpose;
	private String WalletAccountNumber;
	
	public WalletFndTransferRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public WalletFndTransferRequest(com.bornfire.entity.FrAccount frAccount, String currencyCode, String pan,
			String trAmt, String trRmks, String purpose) {
		super();
		FrAccount = frAccount;
		CurrencyCode = currencyCode;
		Pan = pan;
		TrAmt = trAmt;
		TrRmks = trRmks;
		Purpose = purpose;
	}

	public FrAccount getFrAccount() {
		return FrAccount;
	}

	public void setFrAccount(FrAccount frAccount) {
		FrAccount = frAccount;
	}

	public String getCurrencyCode() {
		return CurrencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		CurrencyCode = currencyCode;
	}

	public String getPan() {
		return Pan;
	}

	public void setPan(String pan) {
		Pan = pan;
	}

	public String getTrAmt() {
		return TrAmt;
	}

	public void setTrAmt(String trAmt) {
		TrAmt = trAmt;
	}

	public String getTrRmks() {
		return TrRmks;
	}

	public void setTrRmks(String trRmks) {
		TrRmks = trRmks;
	}

	public String getPurpose() {
		return Purpose;
	}

	public void setPurpose(String purpose) {
		Purpose = purpose;
	}
	
	

	public ToAccount getToAccount() {
		return ToAccount;
	}

	public void setToAccount(ToAccount toAccount) {
		ToAccount = toAccount;
	}
	
	

	public String getWalletAccountNumber() {
		return WalletAccountNumber;
	}

	public void setWalletAccountNumber(String walletAccountNumber) {
		WalletAccountNumber = walletAccountNumber;
	}

	@Override
	public String toString() {
		return "WalletFndTransferRequest [FrAccount=" + FrAccount + ", CurrencyCode=" + CurrencyCode + ", Pan=" + Pan
				+ ", TrAmt=" + TrAmt + ", TrRmks=" + TrRmks + ", Purpose=" + Purpose + "]";
	}
	

	
}
