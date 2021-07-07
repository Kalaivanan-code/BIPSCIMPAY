package com.bornfire.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="BIPS_WALLET_REQUEST_REGISTER_TABLE")
public class WalletRequestRegisterTable {
	
	@Id
	private String w_request_id;
	private String wallet_id;
	private String wallet_acct_no;
	private String sequence_unique_id;
	private Date tran_date;
	private Date value_date;
	private String other_account;
	private String other_account_name;
	private String other_bank_code;
	private String tran_type;
	private String tran_sub_type;
	private String part_tran_type;
	private BigDecimal tran_amount;
	private String tran_currency;
	private BigDecimal tran_conv_rate;
	private String tran_status;
	private String pan;
	private String tran_remarks;
	private String purpose;
	private String psu_device_id;
	private String psu_ip_address;
	private String psu_id;
	private String psu_id_country;
	private String psu_id_type;
	private String entry_user;
	private Date entry_time;
	private String entity_cre_flg;
	private String del_flg;
	private String auth_user;
	private Date auth_time;
	private String modify_user;
	private Date modify_time;
	private String modify_flg;
	private String charge_app_flg;
	private String tran_status_error;
	
	public WalletRequestRegisterTable() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getW_request_id() {
		return w_request_id;
	}

	public void setW_request_id(String w_request_id) {
		this.w_request_id = w_request_id;
	}

	public String getWallet_id() {
		return wallet_id;
	}

	public void setWallet_id(String wallet_id) {
		this.wallet_id = wallet_id;
	}

	public String getWallet_acct_no() {
		return wallet_acct_no;
	}

	public void setWallet_acct_no(String wallet_acct_no) {
		this.wallet_acct_no = wallet_acct_no;
	}

	public String getSequence_unique_id() {
		return sequence_unique_id;
	}

	public void setSequence_unique_id(String sequence_unique_id) {
		this.sequence_unique_id = sequence_unique_id;
	}

	public Date getTran_date() {
		return tran_date;
	}

	public void setTran_date(Date tran_date) {
		this.tran_date = tran_date;
	}

	public Date getValue_date() {
		return value_date;
	}

	public void setValue_date(Date value_date) {
		this.value_date = value_date;
	}

	public String getOther_account() {
		return other_account;
	}

	public void setOther_account(String other_account) {
		this.other_account = other_account;
	}

	public String getOther_account_name() {
		return other_account_name;
	}

	public void setOther_account_name(String other_account_name) {
		this.other_account_name = other_account_name;
	}

	public String getTran_type() {
		return tran_type;
	}

	public void setTran_type(String tran_type) {
		this.tran_type = tran_type;
	}

	public String getTran_sub_type() {
		return tran_sub_type;
	}

	public void setTran_sub_type(String tran_sub_type) {
		this.tran_sub_type = tran_sub_type;
	}

	public String getPart_tran_type() {
		return part_tran_type;
	}

	public void setPart_tran_type(String part_tran_type) {
		this.part_tran_type = part_tran_type;
	}

	public BigDecimal getTran_amount() {
		return tran_amount;
	}

	public void setTran_amount(BigDecimal tran_amount) {
		this.tran_amount = tran_amount;
	}

	public String getTran_currency() {
		return tran_currency;
	}

	public void setTran_currency(String tran_currency) {
		this.tran_currency = tran_currency;
	}

	public BigDecimal getTran_conv_rate() {
		return tran_conv_rate;
	}

	public void setTran_conv_rate(BigDecimal tran_conv_rate) {
		this.tran_conv_rate = tran_conv_rate;
	}

	public String getTran_status() {
		return tran_status;
	}

	public void setTran_status(String tran_status) {
		this.tran_status = tran_status;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public String getTran_remarks() {
		return tran_remarks;
	}

	public void setTran_remarks(String tran_remarks) {
		this.tran_remarks = tran_remarks;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getPsu_device_id() {
		return psu_device_id;
	}

	public void setPsu_device_id(String psu_device_id) {
		this.psu_device_id = psu_device_id;
	}

	public String getPsu_ip_address() {
		return psu_ip_address;
	}

	public void setPsu_ip_address(String psu_ip_address) {
		this.psu_ip_address = psu_ip_address;
	}

	public String getPsu_id() {
		return psu_id;
	}

	public void setPsu_id(String psu_id) {
		this.psu_id = psu_id;
	}

	public String getPsu_id_country() {
		return psu_id_country;
	}

	public void setPsu_id_country(String psu_id_country) {
		this.psu_id_country = psu_id_country;
	}

	public String getPsu_id_type() {
		return psu_id_type;
	}

	public void setPsu_id_type(String psu_id_type) {
		this.psu_id_type = psu_id_type;
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

	public String getEntity_cre_flg() {
		return entity_cre_flg;
	}

	public void setEntity_cre_flg(String entity_cre_flg) {
		this.entity_cre_flg = entity_cre_flg;
	}

	public String getDel_flg() {
		return del_flg;
	}

	public void setDel_flg(String del_flg) {
		this.del_flg = del_flg;
	}

	public String getAuth_user() {
		return auth_user;
	}

	public void setAuth_user(String auth_user) {
		this.auth_user = auth_user;
	}

	public Date getAuth_time() {
		return auth_time;
	}

	public void setAuth_time(Date auth_time) {
		this.auth_time = auth_time;
	}

	public String getModify_user() {
		return modify_user;
	}

	public void setModify_user(String modify_user) {
		this.modify_user = modify_user;
	}

	public Date getModify_time() {
		return modify_time;
	}

	public void setModify_time(Date modify_time) {
		this.modify_time = modify_time;
	}

	public String getModify_flg() {
		return modify_flg;
	}

	public void setModify_flg(String modify_flg) {
		this.modify_flg = modify_flg;
	}

	public String getCharge_app_flg() {
		return charge_app_flg;
	}

	public void setCharge_app_flg(String charge_app_flg) {
		this.charge_app_flg = charge_app_flg;
	}

	public String getOther_bank_code() {
		return other_bank_code;
	}

	public void setOther_bank_code(String other_bank_code) {
		this.other_bank_code = other_bank_code;
	}

	public String getTran_status_error() {
		return tran_status_error;
	}

	public void setTran_status_error(String tran_status_error) {
		this.tran_status_error = tran_status_error;
	}
		
	
	
}
