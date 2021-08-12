package com.bornfire.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="BIPS_CONSENT_OUTWARD_ACCESS_TMP_TABLE")
public class ConsentOutwardAccessTmpTable {
	
	@Id
	private String x_request_id;
	private String consent_id;
	private String auth_id;
	private String senderparticipant_bic;
	private String senderparticipant_memberid;
	private String receiverparticipant_bic;
	private String receiverparticipant_memberid;
	private String psu_device_id;
	private String psu_ip_address;
	private String psu_id;
	private String psu_id_country;
	private String psu_id_type;
	private String phone_number;
	private String public_key;
	private String schm_name;
	private String identification;
	private String permission;
	private String read_balance;
	private String read_tran_details;
	private String read_acct_details;
	private String read_debit_acct;
	private Date expire_date;
	private Date tran_from_date;
	private Date tran_to_date;
	private String status;
	private Date status_update_date;
	private Date create_date;
	private Date entry_time;
	private String entry_user;
	private String del_user;
	private Date del_time;
	private String del_flg;
	private String account_status;
	private String private_key;
	private String custom_device_id;

	
	
	public ConsentOutwardAccessTmpTable() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getX_request_id() {
		return x_request_id;
	}
	public void setX_request_id(String x_request_id) {
		this.x_request_id = x_request_id;
	}
	public String getConsent_id() {
		return consent_id;
	}
	public void setConsent_id(String consent_id) {
		this.consent_id = consent_id;
	}
	public String getAuth_id() {
		return auth_id;
	}
	public void setAuth_id(String auth_id) {
		this.auth_id = auth_id;
	}
	public String getSenderparticipant_bic() {
		return senderparticipant_bic;
	}
	public void setSenderparticipant_bic(String senderparticipant_bic) {
		this.senderparticipant_bic = senderparticipant_bic;
	}
	public String getSenderparticipant_memberid() {
		return senderparticipant_memberid;
	}
	public void setSenderparticipant_memberid(String senderparticipant_memberid) {
		this.senderparticipant_memberid = senderparticipant_memberid;
	}
	public String getReceiverparticipant_bic() {
		return receiverparticipant_bic;
	}
	public void setReceiverparticipant_bic(String receiverparticipant_bic) {
		this.receiverparticipant_bic = receiverparticipant_bic;
	}
	public String getReceiverparticipant_memberid() {
		return receiverparticipant_memberid;
	}
	public void setReceiverparticipant_memberid(String receiverparticipant_memberid) {
		this.receiverparticipant_memberid = receiverparticipant_memberid;
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
	public String getPermission() {
		return permission;
	}
	public void setPermission(String permission) {
		this.permission = permission;
	}
	public String getRead_balance() {
		return read_balance;
	}
	public void setRead_balance(String read_balance) {
		this.read_balance = read_balance;
	}
	public String getRead_tran_details() {
		return read_tran_details;
	}
	public void setRead_tran_details(String read_tran_details) {
		this.read_tran_details = read_tran_details;
	}
	public String getRead_acct_details() {
		return read_acct_details;
	}
	public void setRead_acct_details(String read_acct_details) {
		this.read_acct_details = read_acct_details;
	}
	public String getRead_debit_acct() {
		return read_debit_acct;
	}
	public void setRead_debit_acct(String read_debit_acct) {
		this.read_debit_acct = read_debit_acct;
	}
	public Date getExpire_date() {
		return expire_date;
	}
	public void setExpire_date(Date expire_date) {
		this.expire_date = expire_date;
	}
	public Date getTran_from_date() {
		return tran_from_date;
	}
	public void setTran_from_date(Date tran_from_date) {
		this.tran_from_date = tran_from_date;
	}
	public Date getTran_to_date() {
		return tran_to_date;
	}
	public void setTran_to_date(Date tran_to_date) {
		this.tran_to_date = tran_to_date;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getStatus_update_date() {
		return status_update_date;
	}
	public void setStatus_update_date(Date status_update_date) {
		this.status_update_date = status_update_date;
	}
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
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
	public String getPrivate_key() {
		return private_key;
	}
	public void setPrivate_key(String private_key) {
		this.private_key = private_key;
	}
	public String getCustom_device_id() {
		return custom_device_id;
	}
	public void setCustom_device_id(String custom_device_id) {
		this.custom_device_id = custom_device_id;
	}
	
	
}
