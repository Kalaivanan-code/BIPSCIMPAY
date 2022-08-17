package com.bornfire.upiqrcodeentity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class NpciupiReqcls {

	@JsonProperty("txn")
	private NpciupiReqtransaction txn;
	
	@JsonProperty("qrPayLoad")
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
	
	
}
