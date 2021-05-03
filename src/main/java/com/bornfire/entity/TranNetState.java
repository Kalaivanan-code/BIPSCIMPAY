package com.bornfire.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="BIPS_TRAN_NET_STAT_TABLE")

public class TranNetState {
	@Id
	private String msg_id;
	private String msg_date;
	private String addtl_info;
	private String elctrnseqnb;
	private String acct_id;
	private String acct_ownr;
	private String tot_no_of_entries;
	private String tot_amt;
	private String cdt_no_of_entries;
	private String cdt_amt;
	private String dbt_no_of_entries;
	private String dbt_amt;
	public TranNetState() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TranNetState(String msg_id, String msg_date, String addtl_info, String elctrnseqnb, String acct_id,
			String acct_ownr, String tot_no_of_entries, String tot_amt, String cdt_no_of_entries, String cdt_amt,
			String dbt_no_of_entries, String dbt_amt) {
		super();
		this.msg_id = msg_id;
		this.msg_date = msg_date;
		this.addtl_info = addtl_info;
		this.elctrnseqnb = elctrnseqnb;
		this.acct_id = acct_id;
		this.acct_ownr = acct_ownr;
		this.tot_no_of_entries = tot_no_of_entries;
		this.tot_amt = tot_amt;
		this.cdt_no_of_entries = cdt_no_of_entries;
		this.cdt_amt = cdt_amt;
		this.dbt_no_of_entries = dbt_no_of_entries;
		this.dbt_amt = dbt_amt;
	}
	public String getMsg_id() {
		return msg_id;
	}
	public void setMsg_id(String msg_id) {
		this.msg_id = msg_id;
	}
	public String getMsg_date() {
		return msg_date;
	}
	public void setMsg_date(String msg_date) {
		this.msg_date = msg_date;
	}
	public String getAddtl_info() {
		return addtl_info;
	}
	public void setAddtl_info(String addtl_info) {
		this.addtl_info = addtl_info;
	}
	public String getElctrnseqnb() {
		return elctrnseqnb;
	}
	public void setElctrnseqnb(String elctrnseqnb) {
		this.elctrnseqnb = elctrnseqnb;
	}
	public String getAcct_id() {
		return acct_id;
	}
	public void setAcct_id(String acct_id) {
		this.acct_id = acct_id;
	}
	public String getAcct_ownr() {
		return acct_ownr;
	}
	public void setAcct_ownr(String acct_ownr) {
		this.acct_ownr = acct_ownr;
	}
	public String getTot_no_of_entries() {
		return tot_no_of_entries;
	}
	public void setTot_no_of_entries(String tot_no_of_entries) {
		this.tot_no_of_entries = tot_no_of_entries;
	}
	public String getTot_amt() {
		return tot_amt;
	}
	public void setTot_amt(String tot_amt) {
		this.tot_amt = tot_amt;
	}
	public String getCdt_no_of_entries() {
		return cdt_no_of_entries;
	}
	public void setCdt_no_of_entries(String cdt_no_of_entries) {
		this.cdt_no_of_entries = cdt_no_of_entries;
	}
	public String getCdt_amt() {
		return cdt_amt;
	}
	public void setCdt_amt(String cdt_amt) {
		this.cdt_amt = cdt_amt;
	}
	public String getDbt_no_of_entries() {
		return dbt_no_of_entries;
	}
	public void setDbt_no_of_entries(String dbt_no_of_entries) {
		this.dbt_no_of_entries = dbt_no_of_entries;
	}
	public String getDbt_amt() {
		return dbt_amt;
	}
	public void setDbt_amt(String dbt_amt) {
		this.dbt_amt = dbt_amt;
	}
	
	

}
