package com.bornfire.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name="BIPS_SETTLEMENT_REPORT")
@IdClass(SettlementReportDetailID.class)
public class SettlementReport implements Serializable{
	
	private String	msg_id;
	@Id
	private String	msg_date;
	
	private String	stat_id;
	private String	acct_id;
	private String	acct_owner;
	private String	balance;
	@Id
	private String	entry_acct_svcr_ref;
	private String	entry_status_code;
	private String	entry_bk_tx_cd;
	private String	entry_amount;
	private String	entry_currency;
	private String	entry_entrydtl_btch_msg_id;
	private String	entry_entrydtl_btch_no_tx;
	private String	entry_entrydtl_btch_tot_amt;
	private String	entry_entrydtl_btch_cncy;
	private String	entry_entrydtl_btch_cd_dbt_ind;
	private String	entry_entrydtl_txdts_refs_acctsvcrref;
	private String	entry_entrydtl_txdts_refs_instr_id;
	private String	entry_entrydtl_txdts_refs_endtoend_id;
	private String	entry_entrydtl_txdts_refs_tx_id;
	private String	entry_entrydtl_txdts_amt;
	private String	entry_entrydtl_txdts_cncy;
	private String	entry_entrydtl_txdts_cd_dbt_ind;
	private Date	entry_time;
	private Date	verify_time;
	private Date	modify_time;
	private String	entry_user;
	private String	modify_user;
	private String	verify_user;
	private String	entity_flg;
	private String	modify_flg;
	private String	del_flg;
	private Date value_date;
	
	

	public SettlementReport() {
		super();
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

	public String getStat_id() {
		return stat_id;
	}

	public void setStat_id(String stat_id) {
		this.stat_id = stat_id;
	}

	public String getAcct_id() {
		return acct_id;
	}

	public void setAcct_id(String acct_id) {
		this.acct_id = acct_id;
	}

	public String getAcct_owner() {
		return acct_owner;
	}

	public void setAcct_owner(String acct_owner) {
		this.acct_owner = acct_owner;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getEntry_acct_svcr_ref() {
		return entry_acct_svcr_ref;
	}

	public void setEntry_acct_svcr_ref(String entry_acct_svcr_ref) {
		this.entry_acct_svcr_ref = entry_acct_svcr_ref;
	}

	public String getEntry_status_code() {
		return entry_status_code;
	}

	public void setEntry_status_code(String entry_status_code) {
		this.entry_status_code = entry_status_code;
	}

	public String getEntry_bk_tx_cd() {
		return entry_bk_tx_cd;
	}

	public void setEntry_bk_tx_cd(String entry_bk_tx_cd) {
		this.entry_bk_tx_cd = entry_bk_tx_cd;
	}

	public String getEntry_amount() {
		return entry_amount;
	}

	public void setEntry_amount(String entry_amount) {
		this.entry_amount = entry_amount;
	}

	public String getEntry_currency() {
		return entry_currency;
	}

	public void setEntry_currency(String entry_currency) {
		this.entry_currency = entry_currency;
	}

	public String getEntry_entrydtl_btch_msg_id() {
		return entry_entrydtl_btch_msg_id;
	}

	public void setEntry_entrydtl_btch_msg_id(String entry_entrydtl_btch_msg_id) {
		this.entry_entrydtl_btch_msg_id = entry_entrydtl_btch_msg_id;
	}

	public String getEntry_entrydtl_btch_no_tx() {
		return entry_entrydtl_btch_no_tx;
	}

	public void setEntry_entrydtl_btch_no_tx(String entry_entrydtl_btch_no_tx) {
		this.entry_entrydtl_btch_no_tx = entry_entrydtl_btch_no_tx;
	}

	public String getEntry_entrydtl_btch_tot_amt() {
		return entry_entrydtl_btch_tot_amt;
	}

	public void setEntry_entrydtl_btch_tot_amt(String entry_entrydtl_btch_tot_amt) {
		this.entry_entrydtl_btch_tot_amt = entry_entrydtl_btch_tot_amt;
	}

	public String getEntry_entrydtl_btch_cncy() {
		return entry_entrydtl_btch_cncy;
	}

	public void setEntry_entrydtl_btch_cncy(String entry_entrydtl_btch_cncy) {
		this.entry_entrydtl_btch_cncy = entry_entrydtl_btch_cncy;
	}

	public String getEntry_entrydtl_btch_cd_dbt_ind() {
		return entry_entrydtl_btch_cd_dbt_ind;
	}

	public void setEntry_entrydtl_btch_cd_dbt_ind(String entry_entrydtl_btch_cd_dbt_ind) {
		this.entry_entrydtl_btch_cd_dbt_ind = entry_entrydtl_btch_cd_dbt_ind;
	}

	public String getEntry_entrydtl_txdts_refs_acctsvcrref() {
		return entry_entrydtl_txdts_refs_acctsvcrref;
	}

	public void setEntry_entrydtl_txdts_refs_acctsvcrref(String entry_entrydtl_txdts_refs_acctsvcrref) {
		this.entry_entrydtl_txdts_refs_acctsvcrref = entry_entrydtl_txdts_refs_acctsvcrref;
	}

	public String getEntry_entrydtl_txdts_refs_instr_id() {
		return entry_entrydtl_txdts_refs_instr_id;
	}

	public void setEntry_entrydtl_txdts_refs_instr_id(String entry_entrydtl_txdts_refs_instr_id) {
		this.entry_entrydtl_txdts_refs_instr_id = entry_entrydtl_txdts_refs_instr_id;
	}

	public String getEntry_entrydtl_txdts_refs_endtoend_id() {
		return entry_entrydtl_txdts_refs_endtoend_id;
	}

	public void setEntry_entrydtl_txdts_refs_endtoend_id(String entry_entrydtl_txdts_refs_endtoend_id) {
		this.entry_entrydtl_txdts_refs_endtoend_id = entry_entrydtl_txdts_refs_endtoend_id;
	}

	public String getEntry_entrydtl_txdts_refs_tx_id() {
		return entry_entrydtl_txdts_refs_tx_id;
	}

	public void setEntry_entrydtl_txdts_refs_tx_id(String entry_entrydtl_txdts_refs_tx_id) {
		this.entry_entrydtl_txdts_refs_tx_id = entry_entrydtl_txdts_refs_tx_id;
	}

	public String getEntry_entrydtl_txdts_amt() {
		return entry_entrydtl_txdts_amt;
	}

	public void setEntry_entrydtl_txdts_amt(String entry_entrydtl_txdts_amt) {
		this.entry_entrydtl_txdts_amt = entry_entrydtl_txdts_amt;
	}

	public String getEntry_entrydtl_txdts_cncy() {
		return entry_entrydtl_txdts_cncy;
	}

	public void setEntry_entrydtl_txdts_cncy(String entry_entrydtl_txdts_cncy) {
		this.entry_entrydtl_txdts_cncy = entry_entrydtl_txdts_cncy;
	}

	public String getEntry_entrydtl_txdts_cd_dbt_ind() {
		return entry_entrydtl_txdts_cd_dbt_ind;
	}

	public void setEntry_entrydtl_txdts_cd_dbt_ind(String entry_entrydtl_txdts_cd_dbt_ind) {
		this.entry_entrydtl_txdts_cd_dbt_ind = entry_entrydtl_txdts_cd_dbt_ind;
	}

	public Date getEntry_time() {
		return entry_time;
	}

	public void setEntry_time(Date entry_time) {
		this.entry_time = entry_time;
	}

	public String getEntry_user() {
		return entry_user;
	}

	public void setEntry_user(String entry_user) {
		this.entry_user = entry_user;
	}

	public Date getVerify_time() {
		return verify_time;
	}

	public void setVerify_time(Date verify_time) {
		this.verify_time = verify_time;
	}

	public Date getModify_time() {
		return modify_time;
	}

	public void setModify_time(Date modify_time) {
		this.modify_time = modify_time;
	}

	public String getModify_user() {
		return modify_user;
	}

	public void setModify_user(String modify_user) {
		this.modify_user = modify_user;
	}

	public String getVerify_user() {
		return verify_user;
	}

	public void setVerify_user(String verify_user) {
		this.verify_user = verify_user;
	}

	public String getEntity_flg() {
		return entity_flg;
	}

	public void setEntity_flg(String entity_flg) {
		this.entity_flg = entity_flg;
	}

	public String getModify_flg() {
		return modify_flg;
	}

	public void setModify_flg(String modify_flg) {
		this.modify_flg = modify_flg;
	}

	public String getDel_flg() {
		return del_flg;
	}

	public void setDel_flg(String del_flg) {
		this.del_flg = del_flg;
	}

	public Date getValue_date() {
		return value_date;
	}

	public void setValue_date(Date value_date) {
		this.value_date = value_date;
	} 
	
	
	
}
