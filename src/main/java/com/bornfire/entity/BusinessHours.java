package com.bornfire.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "BIPS_BUS_HOURS_TABLE")
public class BusinessHours {

	@Id
	private String srl_no;
	private String ips_start_hrs;
	private String ips_start_hrs_criteria;
	private String ips_start_hrs_code;
	private String ips_start_hrs_remarks;
	private String ips_cls_hrs;
	private String ips_cls_hrs_criteria;
	private String ips_cls_hrs_code;
	private String ips_cls_hrs_remarks;
	private String ips_clg_hrs;
	private String ips_clg_hrs_criteria;
	private String ips_clg_hrs_code;
	private String ips_clg_hrs_remarks;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date entry_time;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date verify_time;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date modify_time;
	private String entry_user;
	private String modify_user;
	private String verify_user;
	private String entity_flg;
	private String modify_flg;
	private String del_flg;
	private String ips_start_description;
	private String ips_cls_description;
	private String ips_clg_description;

	public String getIps_start_hrs() {
		return ips_start_hrs;
	}

	public void setIps_start_hrs(String ips_start_hrs) {
		this.ips_start_hrs = ips_start_hrs;
	}

	public String getIps_start_hrs_criteria() {
		return ips_start_hrs_criteria;
	}

	public void setIps_start_hrs_criteria(String ips_start_hrs_criteria) {
		this.ips_start_hrs_criteria = ips_start_hrs_criteria;
	}

	public String getIps_start_hrs_code() {
		return ips_start_hrs_code;
	}

	public void setIps_start_hrs_code(String ips_start_hrs_code) {
		this.ips_start_hrs_code = ips_start_hrs_code;
	}

	public String getIps_start_hrs_remarks() {
		return ips_start_hrs_remarks;
	}

	public void setIps_start_hrs_remarks(String ips_start_hrs_remarks) {
		this.ips_start_hrs_remarks = ips_start_hrs_remarks;
	}

	public String getIps_cls_hrs() {
		return ips_cls_hrs;
	}

	public void setIps_cls_hrs(String ips_cls_hrs) {
		this.ips_cls_hrs = ips_cls_hrs;
	}

	public String getIps_cls_hrs_criteria() {
		return ips_cls_hrs_criteria;
	}

	public void setIps_cls_hrs_criteria(String ips_cls_hrs_criteria) {
		this.ips_cls_hrs_criteria = ips_cls_hrs_criteria;
	}

	public String getIps_cls_hrs_code() {
		return ips_cls_hrs_code;
	}

	public void setIps_cls_hrs_code(String ips_cls_hrs_code) {
		this.ips_cls_hrs_code = ips_cls_hrs_code;
	}

	public String getIps_cls_hrs_remarks() {
		return ips_cls_hrs_remarks;
	}

	public void setIps_cls_hrs_remarks(String ips_cls_hrs_remarks) {
		this.ips_cls_hrs_remarks = ips_cls_hrs_remarks;
	}

	public String getIps_clg_hrs() {
		return ips_clg_hrs;
	}

	public void setIps_clg_hrs(String ips_clg_hrs) {
		this.ips_clg_hrs = ips_clg_hrs;
	}

	public String getIps_clg_hrs_criteria() {
		return ips_clg_hrs_criteria;
	}

	public void setIps_clg_hrs_criteria(String ips_clg_hrs_criteria) {
		this.ips_clg_hrs_criteria = ips_clg_hrs_criteria;
	}

	public String getIps_clg_hrs_code() {
		return ips_clg_hrs_code;
	}

	public void setIps_clg_hrs_code(String ips_clg_hrs_code) {
		this.ips_clg_hrs_code = ips_clg_hrs_code;
	}

	public String getIps_clg_hrs_remarks() {
		return ips_clg_hrs_remarks;
	}

	public void setIps_clg_hrs_remarks(String ips_clg_hrs_remarks) {
		this.ips_clg_hrs_remarks = ips_clg_hrs_remarks;
	}

	public Date getEntry_time() {
		return entry_time;
	}

	public void setEntry_time(Date entry_time) {
		this.entry_time = entry_time;
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

	public String getEntry_user() {
		return entry_user;
	}

	public void setEntry_user(String entry_user) {
		this.entry_user = entry_user;
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

	public String getIps_start_description() {
		return ips_start_description;
	}

	public String getIps_cls_description() {
		return ips_cls_description;
	}

	public String getIps_clg_description() {
		return ips_clg_description;
	}

	public void setIps_start_description(String ips_start_description) {
		this.ips_start_description = ips_start_description;
	}

	public void setIps_cls_description(String ips_cls_description) {
		this.ips_cls_description = ips_cls_description;
	}

	public void setIps_clg_description(String ips_clg_description) {
		this.ips_clg_description = ips_clg_description;
	}

	public String getSrl_no() {
		return srl_no;
	}

	public void setSrl_no(String srl_no) {
		this.srl_no = srl_no;
	}

	public BusinessHours() {
		super();
	}

	public BusinessHours(String srl_no, String ips_start_hrs, String ips_start_hrs_criteria, String ips_start_hrs_code,
			String ips_start_hrs_remarks, String ips_cls_hrs, String ips_cls_hrs_criteria, String ips_cls_hrs_code,
			String ips_cls_hrs_remarks, String ips_clg_hrs, String ips_clg_hrs_criteria, String ips_clg_hrs_code,
			String ips_clg_hrs_remarks, Date entry_time, Date verify_time, Date modify_time, String entry_user,
			String modify_user, String verify_user, String entity_flg, String modify_flg, String del_flg,
			String ips_start_description, String ips_cls_description, String ips_clg_description) {
		super();
		this.srl_no = srl_no;
		this.ips_start_hrs = ips_start_hrs;
		this.ips_start_hrs_criteria = ips_start_hrs_criteria;
		this.ips_start_hrs_code = ips_start_hrs_code;
		this.ips_start_hrs_remarks = ips_start_hrs_remarks;
		this.ips_cls_hrs = ips_cls_hrs;
		this.ips_cls_hrs_criteria = ips_cls_hrs_criteria;
		this.ips_cls_hrs_code = ips_cls_hrs_code;
		this.ips_cls_hrs_remarks = ips_cls_hrs_remarks;
		this.ips_clg_hrs = ips_clg_hrs;
		this.ips_clg_hrs_criteria = ips_clg_hrs_criteria;
		this.ips_clg_hrs_code = ips_clg_hrs_code;
		this.ips_clg_hrs_remarks = ips_clg_hrs_remarks;
		this.entry_time = entry_time;
		this.verify_time = verify_time;
		this.modify_time = modify_time;
		this.entry_user = entry_user;
		this.modify_user = modify_user;
		this.verify_user = verify_user;
		this.entity_flg = entity_flg;
		this.modify_flg = modify_flg;
		this.del_flg = del_flg;
		this.ips_start_description = ips_start_description;
		this.ips_cls_description = ips_cls_description;
		this.ips_clg_description = ips_clg_description;
	}

}
