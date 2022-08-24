package com.bornfire.upiqrcodeentity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class NpciupiReqcls {

	@JsonProperty("Txn")
	private NpciupiReqtransaction txn;
	
	@JsonProperty("QrPayLoad")
	private String QrPayLoad;

	public NpciupiReqtransaction getTxn() {
		return txn;
	}

	public void setTxn(NpciupiReqtransaction txn) {
		this.txn = txn;
	}

	public String getQrPayLoad() {
		return QrPayLoad;
	}

	public void setQrPayLoad(String qrPayLoad) {
		QrPayLoad = qrPayLoad;
	}

	@Override
	public String toString() {
		return "NpciupiReqcls [Txn=" + txn + ", QrPayLoad=" + QrPayLoad + "]";
	}
	
	
}
