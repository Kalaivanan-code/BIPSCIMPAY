package com.bornfire.upiqrcodeentity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UPIRespEntity {

	@JsonProperty("Resp")
	private RespEntity resp;
	
	@JsonProperty("Payee")
	private Payee payee;
	
	@JsonProperty("Invoice")
	private Invoice invoice;
	
	@JsonProperty("QrPayLoad")
	private String qrPayLoad;
	
	@JsonProperty("Txn")
	private NpciupiReqtransaction txn;

	public RespEntity getResp() {
		return resp;
	}

	public void setResp(RespEntity resp) {
		this.resp = resp;
	}

	public Payee getPayee() {
		return payee;
	}

	public void setPayee(Payee payee) {
		this.payee = payee;
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public String getQrPayLoad() {
		return qrPayLoad;
	}

	public void setQrPayLoad(String qrPayLoad) {
		this.qrPayLoad = qrPayLoad;
	}

	public NpciupiReqtransaction getTxn() {
		return txn;
	}

	public void setTxn(NpciupiReqtransaction txn) {
		this.txn = txn;
	}

	@Override
	public String toString() {
		return "UPIRespEntity [Resp=" + resp + ", Payee=" + payee + ", Invoice=" + invoice + ", QrPayLoad=" + qrPayLoad
				+ ", Txn=" + txn + "]";
	}
	
	
	
}
