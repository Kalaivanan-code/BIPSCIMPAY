package com.bornfire.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="BIPS_REG_PUBLIC_KEY")
public class RegPublicKey {

	@Id
	private String record_id;
	private String auth_id;
	private String device_id;
	private String ip_address;
	private String national_id;
	private String schm_name;
	private String acct_number;
	private String public_key;
	private String phone_number;
	private Date entry_time;
	private String entry_user;
	private Date modify_time;
	private String modify_user;
	private String otp;
	private String sender_participant_bic;
	private String sender_participant_member_id;
	private String receiver_participant_bic;
	private String receiver_participant_member_id;
	private String entity_flg;
	private String modify_flg;
	private String del_flg;
	private String del_user;
	private Date del_time;
	private String charge_app_flg;
	
	
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
	public String getRecord_id() {
		return record_id;
	}
	public void setRecord_id(String record_id) {
		this.record_id = record_id;
	}
	public String getAuth_id() {
		return auth_id;
	}
	public void setAuth_id(String auth_id) {
		this.auth_id = auth_id;
	}
	public String getDevice_id() {
		return device_id;
	}
	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}
	public String getIp_address() {
		return ip_address;
	}
	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}
	public String getNational_id() {
		return national_id;
	}
	public void setNational_id(String national_id) {
		this.national_id = national_id;
	}
	
	public String getPublic_key() {
		return public_key;
	}
	public void setPublic_key(String public_key) {
		this.public_key = public_key;
	}
	public String getPhone_number() {
		return phone_number;
	}
	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
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
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}
	public String getSender_participant_bic() {
		return sender_participant_bic;
	}
	public void setSender_participant_bic(String sender_participant_bic) {
		this.sender_participant_bic = sender_participant_bic;
	}
	public String getSender_participant_member_id() {
		return sender_participant_member_id;
	}
	public void setSender_participant_member_id(String sender_participant_member_id) {
		this.sender_participant_member_id = sender_participant_member_id;
	}
	public String getReceiver_participant_bic() {
		return receiver_participant_bic;
	}
	public void setReceiver_participant_bic(String receiver_participant_bic) {
		this.receiver_participant_bic = receiver_participant_bic;
	}
	public String getReceiver_participant_member_id() {
		return receiver_participant_member_id;
	}
	public void setReceiver_participant_member_id(String receiver_participant_member_id) {
		this.receiver_participant_member_id = receiver_participant_member_id;
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
	public String getCharge_app_flg() {
		return charge_app_flg;
	}
	public void setCharge_app_flg(String charge_app_flg) {
		this.charge_app_flg = charge_app_flg;
	}
	
	
	
}
