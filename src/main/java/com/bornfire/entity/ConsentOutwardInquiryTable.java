package com.bornfire.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="BIPS_CONSENT_OUTWARD_INQUIRY_TABLE")
public class ConsentOutwardInquiryTable {

	@Id
	private String x_request_id;
	private String consent_id;
	private String account_id;
	private String inquiry_type;
	private String senderparticipant_bic;
	private String senderparticipant_memberid;
	private String receiverparticipant_bic;
	private String receiverparticipant_memberid;
	private String psu_device_id;
	private String psu_ip_address;
	private String psu_id;
	private String psu_id_country;
	private String psu_id_type;
	private String status;
	private String status_error;
	private Date entry_time;
	private String entry_user;
	private String del_user;
	private Date del_time;
	private String del_flg;
	private String psu_channel;
	
	
	public String getPsu_channel() {
		return psu_channel;
	}
	public void setPsu_channel(String psu_channel) {
		this.psu_channel = psu_channel;
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
	public String getAccount_id() {
		return account_id;
	}
	public void setAccount_id(String account_id) {
		this.account_id = account_id;
	}
	public String getInquiry_type() {
		return inquiry_type;
	}
	public void setInquiry_type(String inquiry_type) {
		this.inquiry_type = inquiry_type;
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
	
	
}
