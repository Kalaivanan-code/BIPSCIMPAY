package com.bornfire.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="BIPS_UPI_REQUEST")
public class UPI_REQ_QRCODE {

	private String ids;
	private String note;
	private String refId;
	private String refUrl;
	private Date ts;
	private String custRef;
	private String qrPayLoad;
	@Id
	private String req_id;
	private String response;
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getRefId() {
		return refId;
	}
	public void setRefId(String refId) {
		this.refId = refId;
	}
	public String getRefUrl() {
		return refUrl;
	}
	public void setRefUrl(String refUrl) {
		this.refUrl = refUrl;
	}
	public Date getTs() {
		return ts;
	}
	public void setTs(Date ts) {
		this.ts = ts;
	}
	public String getCustRef() {
		return custRef;
	}
	public void setCustRef(String custRef) {
		this.custRef = custRef;
	}
	public String getQrPayLoad() {
		return qrPayLoad;
	}
	public void setQrPayLoad(String qrPayLoad) {
		this.qrPayLoad = qrPayLoad;
	}
	public String getReq_id() {
		return req_id;
	}
	public void setReq_id(String req_id) {
		this.req_id = req_id;
	}
	
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}


	public UPI_REQ_QRCODE(String ids, String note, String refId, String refUrl, Date ts, String custRef,
			String qrPayLoad, String req_id, String response) {
		super();
		this.ids = ids;
		this.note = note;
		this.refId = refId;
		this.refUrl = refUrl;
		this.ts = ts;
		this.custRef = custRef;
		this.qrPayLoad = qrPayLoad;
		this.req_id = req_id;
		this.response = response;
	}
	public UPI_REQ_QRCODE() {
		super();
		// TODO Auto-generated constructor stub
	}

	
}
