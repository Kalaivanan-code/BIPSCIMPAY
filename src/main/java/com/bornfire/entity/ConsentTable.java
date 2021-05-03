package com.bornfire.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="BIPS_CONSENT_TABLE")
public class ConsentTable {

	@Id
	private String consent_id;
	private String auth_id;
	private String schm_name;
	private String acct_number;
	private String permissions;
	private Date exp_datetime;
	private Date tran_from_datetime;
	private Date tran_to_datetime;
	private String otp;
	private String entry_user;
	private Date entry_time;
	private String entity_cre_flg;
	private String del_user;
	private Date del_time;
	private String del_flg;
	private String psu_device_id;
	private String psu_ip_address;
	private String psu_id;
	public String getConsent_id() {
		return consent_id;
	}
	public void setConsent_id(String consent_id) {
		this.consent_id = consent_id;
	}
	public String getSchm_name() {
		return schm_name;
	}
	public void setSchm_name(String schm_name) {
		this.schm_name = schm_name;
	}
	public String getAcct_number() {
		return acct_number;
	}
	public void setAcct_number(String acct_number) {
		this.acct_number = acct_number;
	}
	public String getPermissions() {
		return permissions;
	}
	public void setPermissions(String permissions) {
		this.permissions = permissions;
	}
	public Date getExp_datetime() {
		return exp_datetime;
	}
	public void setExp_datetime(Date exp_datetime) {
		this.exp_datetime = exp_datetime;
	}
	public Date getTran_from_datetime() {
		return tran_from_datetime;
	}
	public void setTran_from_datetime(Date tran_from_datetime) {
		this.tran_from_datetime = tran_from_datetime;
	}
	public Date getTran_to_datetime() {
		return tran_to_datetime;
	}
	public void setTran_to_datetime(Date tran_to_datetime) {
		this.tran_to_datetime = tran_to_datetime;
	}
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
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
	public String getAuth_id() {
		return auth_id;
	}
	public void setAuth_id(String auth_id) {
		this.auth_id = auth_id;
	}
	
	
	
	
}
