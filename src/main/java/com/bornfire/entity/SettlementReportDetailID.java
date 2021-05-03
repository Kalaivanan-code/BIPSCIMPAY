package com.bornfire.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.Id;

@Embeddable
public class SettlementReportDetailID implements Serializable{
	private String	msg_date;
	private String entry_acct_svcr_ref;
	public String getMsg_date() {
		return msg_date;
	}
	public void setMsg_date(String msg_date) {
		this.msg_date = msg_date;
	}
	public String getEntry_acct_svcr_ref() {
		return entry_acct_svcr_ref;
	}
	public void setEntry_acct_svcr_ref(String entry_acct_svcr_ref) {
		this.entry_acct_svcr_ref = entry_acct_svcr_ref;
	}
	
	


}
