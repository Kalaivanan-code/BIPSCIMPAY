package com.bornfire.upiqrcodeentity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UPIRespEntity {

	@JsonProperty("Resp")
	private ResponseEntity resp;
	
	@JsonProperty("Payee")
	private Payee payee;
	
	@JsonProperty("Invoice")
	private Invoice invoice;
	
	@JsonProperty("QrPayLoad")
	private String qrPayLoad;

	public ResponseEntity getResp() {
		return resp;
	}

	public void setResp(ResponseEntity resp) {
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
	
	
}
