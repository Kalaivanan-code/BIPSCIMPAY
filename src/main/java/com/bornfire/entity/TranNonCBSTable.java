package com.bornfire.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="BIPS_TRAN_NON_CBS_TABLE")
public class TranNonCBSTable {

	private String consent_id;
	private String x_request_id;
	
	@Id
	private String tran_audit_number;
	private Date audit_date;
	private String operation;
	private String status;
	private String status_error;
	private String entry_user;
	private String acct_number;
	private Date entry_time;
	
	
	public TranNonCBSTable() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public String getConsent_id() {
		return consent_id;
	}
	public void setConsent_id(String consent_id) {
		this.consent_id = consent_id;
	}
	public String getX_request_id() {
		return x_request_id;
	}
	public void setX_request_id(String x_request_id) {
		this.x_request_id = x_request_id;
	}
	public String getTran_audit_number() {
		return tran_audit_number;
	}
	public void setTran_audit_number(String tran_audit_number) {
		this.tran_audit_number = tran_audit_number;
	}
	public Date getAudit_date() {
		return audit_date;
	}
	public void setAudit_date(Date audit_date) {
		this.audit_date = audit_date;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatus_error() {
		return status_error;
	}
	public void setStatus_error(String status_error) {
		this.status_error = status_error;
	}
	public String getEntry_user() {
		return entry_user;
	}
	public void setEntry_user(String entry_user) {
		this.entry_user = entry_user;
	}
	public Date getEntry_time() {
		return entry_time;
	}
	public void setEntry_time(Date entry_time) {
		this.entry_time = entry_time;
	}

	public String getAcct_number() {
		return acct_number;
	}

	public void setAcct_number(String acct_number) {
		this.acct_number = acct_number;
	}
	
	
}
