package com.bornfire.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="BIPS_WALLET_ACCESS_TABLE")
public class WalletAccessTable {
	private String w_request_id;
	@Id
	private String wallet_id;
	private String auth_id;
	private String psu_device_id;
	private String psu_ip_address;
	private String psu_id;
	private String psu_id_country;
	private String psu_id_type;
	private String phone_number;
	private String public_key;
	private String schm_name;
	private String identification;
	private Date entry_time;
	private String entry_user;
	private String del_user;
	private Date del_time;
	private String del_flg;
	private String account_status;
	private String wallet_acct_no;
	private BigDecimal wallet_balance;
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
	public String getAuth_id() {
		return auth_id;
	}
	public void setAuth_id(String auth_id) {
		this.auth_id = auth_id;
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
	public String getPhone_number() {
		return phone_number;
	}
	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}
	public String getPublic_key() {
		return public_key;
	}
	public void setPublic_key(String public_key) {
		this.public_key = public_key;
	}
	public String getSchm_name() {
		return schm_name;
	}
	public void setSchm_name(String schm_name) {
		this.schm_name = schm_name;
	}
	public String getIdentification() {
		return identification;
	}
	public void setIdentification(String identification) {
		this.identification = identification;
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
	public String getDel_user() {
		return del_user;
	}
	public void setDel_user(String del_user) {
		this.del_user = del_user;
	}
	public Date getDel_time() {
		return del_time;
	}
	public void setDel_time(Date del_time) {
		this.del_time = del_time;
	}
	public String getDel_flg() {
		return del_flg;
	}
	public void setDel_flg(String del_flg) {
		this.del_flg = del_flg;
	}
	public String getAccount_status() {
		return account_status;
	}
	public void setAccount_status(String account_status) {
		this.account_status = account_status;
	}
	public String getWallet_acct_no() {
		return wallet_acct_no;
	}
	public void setWallet_acct_no(String wallet_acct_no) {
		this.wallet_acct_no = wallet_acct_no;
	}
	public BigDecimal getWallet_balance() {
		return wallet_balance;
	}
	public void setWallet_balance(BigDecimal wallet_balance) {
		this.wallet_balance = wallet_balance;
	}

	
}
