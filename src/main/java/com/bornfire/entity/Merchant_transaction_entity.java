package com.bornfire.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name="BIPSTRAN_DAILY_TABLE")
public class Merchant_transaction_entity {

	private String	tran_type;
	private Date	tran_date;
	private Date	value_date;
	private String	tran_id;
	private BigDecimal	part_tran_id;
	private String	merchant_id;
	private String	merchant_acct_no;
	private String	merchant_trading_name;
	private String	part_tran_type;
	private String	tran_ref_cur;
	private BigDecimal	tran_ref_cur_amt;
	private BigDecimal	tran_rate;
	private BigDecimal	tran_amt_loc;
	private String	tran_particular;
	private String	tran_remarks;
	private String	tran_ref_no;
	private String	tran_ref_detail;
	private String	partition_type;
	private String	partition_detail;
	private String	tran_status;
	private Date	tran_entry_time;
	private String	tran_entry_user;
	private Date	tran_modify_time;
	private String	tran_modify_user;
	private String	tran_post_flg;
	private String	tran_post_user;
	private String	del_flg;
	private String	modify_flg;
	private String	entity_flg;
	private String	tran_upload_flg;
	private String	tran_channel;
	private String	tran_web_url;
	private String	cust_ref;
	private String	loan_ref;
	public String getTran_type() {
		return tran_type;
	}
	public void setTran_type(String tran_type) {
		this.tran_type = tran_type;
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
	public String getMerchant_id() {
		return merchant_id;
	}
	public void setMerchant_id(String merchant_id) {
		this.merchant_id = merchant_id;
	}
	public String getMerchant_acct_no() {
		return merchant_acct_no;
	}
	public void setMerchant_acct_no(String merchant_acct_no) {
		this.merchant_acct_no = merchant_acct_no;
	}
	public String getMerchant_trading_name() {
		return merchant_trading_name;
	}
	public void setMerchant_trading_name(String merchant_trading_name) {
		this.merchant_trading_name = merchant_trading_name;
	}
	public String getPart_tran_type() {
		return part_tran_type;
	}
	public void setPart_tran_type(String part_tran_type) {
		this.part_tran_type = part_tran_type;
	}
	public String getTran_ref_cur() {
		return tran_ref_cur;
	}
	public void setTran_ref_cur(String tran_ref_cur) {
		this.tran_ref_cur = tran_ref_cur;
	}
	public BigDecimal getTran_ref_cur_amt() {
		return tran_ref_cur_amt;
	}
	public void setTran_ref_cur_amt(BigDecimal tran_ref_cur_amt) {
		this.tran_ref_cur_amt = tran_ref_cur_amt;
	}
	public BigDecimal getTran_rate() {
		return tran_rate;
	}
	public void setTran_rate(BigDecimal tran_rate) {
		this.tran_rate = tran_rate;
	}
	public BigDecimal getTran_amt_loc() {
		return tran_amt_loc;
	}
	public void setTran_amt_loc(BigDecimal tran_amt_loc) {
		this.tran_amt_loc = tran_amt_loc;
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
	public String getTran_ref_no() {
		return tran_ref_no;
	}
	public void setTran_ref_no(String tran_ref_no) {
		this.tran_ref_no = tran_ref_no;
	}
	public String getTran_ref_detail() {
		return tran_ref_detail;
	}
	public void setTran_ref_detail(String tran_ref_detail) {
		this.tran_ref_detail = tran_ref_detail;
	}
	public String getPartition_type() {
		return partition_type;
	}
	public void setPartition_type(String partition_type) {
		this.partition_type = partition_type;
	}
	public String getPartition_detail() {
		return partition_detail;
	}
	public void setPartition_detail(String partition_detail) {
		this.partition_detail = partition_detail;
	}
	public String getTran_status() {
		return tran_status;
	}
	public void setTran_status(String tran_status) {
		this.tran_status = tran_status;
	}
	public Date getTran_entry_time() {
		return tran_entry_time;
	}
	public void setTran_entry_time(Date tran_entry_time) {
		this.tran_entry_time = tran_entry_time;
	}
	public String getTran_entry_user() {
		return tran_entry_user;
	}
	public void setTran_entry_user(String tran_entry_user) {
		this.tran_entry_user = tran_entry_user;
	}
	public Date getTran_modify_time() {
		return tran_modify_time;
	}
	public void setTran_modify_time(Date tran_modify_time) {
		this.tran_modify_time = tran_modify_time;
	}
	public String getTran_modify_user() {
		return tran_modify_user;
	}
	public void setTran_modify_user(String tran_modify_user) {
		this.tran_modify_user = tran_modify_user;
	}
	public String getTran_post_flg() {
		return tran_post_flg;
	}
	public void setTran_post_flg(String tran_post_flg) {
		this.tran_post_flg = tran_post_flg;
	}
	public String getTran_post_user() {
		return tran_post_user;
	}
	public void setTran_post_user(String tran_post_user) {
		this.tran_post_user = tran_post_user;
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
	public String getEntity_flg() {
		return entity_flg;
	}
	public void setEntity_flg(String entity_flg) {
		this.entity_flg = entity_flg;
	}
	public String getTran_upload_flg() {
		return tran_upload_flg;
	}
	public void setTran_upload_flg(String tran_upload_flg) {
		this.tran_upload_flg = tran_upload_flg;
	}
	public String getTran_channel() {
		return tran_channel;
	}
	public void setTran_channel(String tran_channel) {
		this.tran_channel = tran_channel;
	}
	public String getTran_web_url() {
		return tran_web_url;
	}
	public void setTran_web_url(String tran_web_url) {
		this.tran_web_url = tran_web_url;
	}
	public String getCust_ref() {
		return cust_ref;
	}
	public void setCust_ref(String cust_ref) {
		this.cust_ref = cust_ref;
	}
	public String getLoan_ref() {
		return loan_ref;
	}
	public void setLoan_ref(String loan_ref) {
		this.loan_ref = loan_ref;
	}
	public Merchant_transaction_entity(String tran_type, Date tran_date, Date value_date, String tran_id,
			BigDecimal part_tran_id, String merchant_id, String merchant_acct_no, String merchant_trading_name,
			String part_tran_type, String tran_ref_cur, BigDecimal tran_ref_cur_amt, BigDecimal tran_rate,
			BigDecimal tran_amt_loc, String tran_particular, String tran_remarks, String tran_ref_no,
			String tran_ref_detail, String partition_type, String partition_detail, String tran_status,
			Date tran_entry_time, String tran_entry_user, Date tran_modify_time, String tran_modify_user,
			String tran_post_flg, String tran_post_user, String del_flg, String modify_flg, String entity_flg,
			String tran_upload_flg, String tran_channel, String tran_web_url, String cust_ref, String loan_ref) {
		super();
		this.tran_type = tran_type;
		this.tran_date = tran_date;
		this.value_date = value_date;
		this.tran_id = tran_id;
		this.part_tran_id = part_tran_id;
		this.merchant_id = merchant_id;
		this.merchant_acct_no = merchant_acct_no;
		this.merchant_trading_name = merchant_trading_name;
		this.part_tran_type = part_tran_type;
		this.tran_ref_cur = tran_ref_cur;
		this.tran_ref_cur_amt = tran_ref_cur_amt;
		this.tran_rate = tran_rate;
		this.tran_amt_loc = tran_amt_loc;
		this.tran_particular = tran_particular;
		this.tran_remarks = tran_remarks;
		this.tran_ref_no = tran_ref_no;
		this.tran_ref_detail = tran_ref_detail;
		this.partition_type = partition_type;
		this.partition_detail = partition_detail;
		this.tran_status = tran_status;
		this.tran_entry_time = tran_entry_time;
		this.tran_entry_user = tran_entry_user;
		this.tran_modify_time = tran_modify_time;
		this.tran_modify_user = tran_modify_user;
		this.tran_post_flg = tran_post_flg;
		this.tran_post_user = tran_post_user;
		this.del_flg = del_flg;
		this.modify_flg = modify_flg;
		this.entity_flg = entity_flg;
		this.tran_upload_flg = tran_upload_flg;
		this.tran_channel = tran_channel;
		this.tran_web_url = tran_web_url;
		this.cust_ref = cust_ref;
		this.loan_ref = loan_ref;
	}
	public Merchant_transaction_entity() {
		super();
		// TODO Auto-generated constructor stub
	}

	
}
