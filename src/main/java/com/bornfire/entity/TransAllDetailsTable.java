package com.bornfire.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;


@Entity
@Table(name = "BIPS_TRANS_ALL_DETAILS")
public class TransAllDetailsTable {
	
	private String	ips_ref_no;
	@Id
	private String	tran_ref_no;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date	tran_date;
	private Date	value_date;
	private String	acct_num;
	private String	parking_acct_num;
	private String tran_type;
	private String	tran_id;
	private BigDecimal	part_tran_id;
	private String	part_tran_type;
	private BigDecimal	tran_amt;
	private String	tran_particular;
	private String	tran_remarks;
	private String	tran_purpose;
	private String	tran_status;
	private String	tran_status_error;
	private String	tran_crncy;
	private String	ref_crncy;
	private String	rate_code;
	private BigDecimal	rate;
	private String	home_crncy_code;
	private BigDecimal	home_crncy_amt;
	private String	tran_report_code;
	private String	settlement_account_no;
	private String	entity_cre_flg;
	private String	del_flg;
	private String	modify_flg;
	private String	entry_user;
	private String	modify_user;
	private String	auth_user;
	private Date	entry_time;
	private Date	modify_time;
	private Date	auth_time;
	private String	login_mode;
	private String	channel_id;
	private String	status_flg;
	private String	restrict_flg;
	private String	remarks;
	public String getIps_ref_no() {
		return ips_ref_no;
	}
	public void setIps_ref_no(String ips_ref_no) {
		this.ips_ref_no = ips_ref_no;
	}
	public String getTran_ref_no() {
		return tran_ref_no;
	}
	public void setTran_ref_no(String tran_ref_no) {
		this.tran_ref_no = tran_ref_no;
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
	public String getAcct_num() {
		return acct_num;
	}
	public void setAcct_num(String acct_num) {
		this.acct_num = acct_num;
	}
	public String getParking_acct_num() {
		return parking_acct_num;
	}
	public void setParking_acct_num(String parking_acct_num) {
		this.parking_acct_num = parking_acct_num;
	}
	public String getTran_type() {
		return tran_type;
	}
	public void setTran_type(String tran_type) {
		this.tran_type = tran_type;
	}
	public String getTran_id() {
		return tran_id;
	}
	public void setTran_id(String tran_id) {
		this.tran_id = tran_id;
	}
	public BigDecimal getPart_tran_id() {
		return part_tran_id;
	}
	public void setPart_tran_id(BigDecimal part_tran_id) {
		this.part_tran_id = part_tran_id;
	}
	public String getPart_tran_type() {
		return part_tran_type;
	}
	public void setPart_tran_type(String part_tran_type) {
		this.part_tran_type = part_tran_type;
	}
	public BigDecimal getTran_amt() {
		return tran_amt;
	}
	public void setTran_amt(BigDecimal tran_amt) {
		this.tran_amt = tran_amt;
	}
	public String getTran_particular() {
		return tran_particular;
	}
	public void setTran_particular(String tran_particular) {
		this.tran_particular = tran_particular;
	}
	public String getTran_remarks() {
		return tran_remarks;
	}
	public void setTran_remarks(String tran_remarks) {
		this.tran_remarks = tran_remarks;
	}
	public String getTran_purpose() {
		return tran_purpose;
	}
	public void setTran_purpose(String tran_purpose) {
		this.tran_purpose = tran_purpose;
	}
	public String getTran_status() {
		return tran_status;
	}
	public void setTran_status(String tran_status) {
		this.tran_status = tran_status;
	}
	public String getTran_status_error() {
		return tran_status_error;
	}
	public void setTran_status_error(String tran_status_error) {
		this.tran_status_error = tran_status_error;
	}
	public String getTran_crncy() {
		return tran_crncy;
	}
	public void setTran_crncy(String tran_crncy) {
		this.tran_crncy = tran_crncy;
	}
	public String getRef_crncy() {
		return ref_crncy;
	}
	public void setRef_crncy(String ref_crncy) {
		this.ref_crncy = ref_crncy;
	}
	public String getRate_code() {
		return rate_code;
	}
	public void setRate_code(String rate_code) {
		this.rate_code = rate_code;
	}
	public BigDecimal getRate() {
		return rate;
	}
	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}
	public String getHome_crncy_code() {
		return home_crncy_code;
	}
	public void setHome_crncy_code(String home_crncy_code) {
		this.home_crncy_code = home_crncy_code;
	}
	public BigDecimal getHome_crncy_amt() {
		return home_crncy_amt;
	}
	public void setHome_crncy_amt(BigDecimal home_crncy_amt) {
		this.home_crncy_amt = home_crncy_amt;
	}
	public String getTran_report_code() {
		return tran_report_code;
	}
	public void setTran_report_code(String tran_report_code) {
		this.tran_report_code = tran_report_code;
	}
	public String getSettlement_account_no() {
		return settlement_account_no;
	}
	public void setSettlement_account_no(String settlement_account_no) {
		this.settlement_account_no = settlement_account_no;
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
	public String getModify_flg() {
		return modify_flg;
	}
	public void setModify_flg(String modify_flg) {
		this.modify_flg = modify_flg;
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
	public String getAuth_user() {
		return auth_user;
	}
	public void setAuth_user(String auth_user) {
		this.auth_user = auth_user;
	}
	public Date getEntry_time() {
		return entry_time;
	}
	public void setEntry_time(Date entry_time) {
		this.entry_time = entry_time;
	}
	public Date getModify_time() {
		return modify_time;
	}
	public void setModify_time(Date modify_time) {
		this.modify_time = modify_time;
	}
	public Date getAuth_time() {
		return auth_time;
	}
	public void setAuth_time(Date auth_time) {
		this.auth_time = auth_time;
	}
	public String getLogin_mode() {
		return login_mode;
	}
	public void setLogin_mode(String login_mode) {
		this.login_mode = login_mode;
	}
	public String getChannel_id() {
		return channel_id;
	}
	public void setChannel_id(String channel_id) {
		this.channel_id = channel_id;
	}
	public String getStatus_flg() {
		return status_flg;
	}
	public void setStatus_flg(String status_flg) {
		this.status_flg = status_flg;
	}
	public String getRestrict_flg() {
		return restrict_flg;
	}
	public void setRestrict_flg(String restrict_flg) {
		this.restrict_flg = restrict_flg;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	
}
