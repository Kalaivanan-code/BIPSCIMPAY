package com.bornfire.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name="BIPS_OTP_GENERATION_TABLE")
@IdClass(MerchantEabID.class)
public class Balance_table_entity {

	@Id
	private String	merchant_id;
	private String	merchant_acct_no;
	private String	merchant_trading_name;
	@Id
	private Date	tran_date;
	private Date	end_tran_date;
	private BigDecimal	tran_cr_bal;
	private BigDecimal	tran_dr_bal;
	private BigDecimal	tran_tot_net;
	private BigDecimal	tran_date_bal;
	private Date	record_time;
	private String	record_user;
	private String	merchant_particular;
	private String	merchant_remarks;
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
	public Date getTran_date() {
		return tran_date;
	}
	public void setTran_date(Date tran_date) {
		this.tran_date = tran_date;
	}
	public Date getEnd_tran_date() {
		return end_tran_date;
	}
	public void setEnd_tran_date(Date end_tran_date) {
		this.end_tran_date = end_tran_date;
	}
	public BigDecimal getTran_cr_bal() {
		return tran_cr_bal;
	}
	public void setTran_cr_bal(BigDecimal tran_cr_bal) {
		this.tran_cr_bal = tran_cr_bal;
	}
	public BigDecimal getTran_dr_bal() {
		return tran_dr_bal;
	}
	public void setTran_dr_bal(BigDecimal tran_dr_bal) {
		this.tran_dr_bal = tran_dr_bal;
	}
	public BigDecimal getTran_tot_net() {
		return tran_tot_net;
	}
	public void setTran_tot_net(BigDecimal tran_tot_net) {
		this.tran_tot_net = tran_tot_net;
	}
	public BigDecimal getTran_date_bal() {
		return tran_date_bal;
	}
	public void setTran_date_bal(BigDecimal tran_date_bal) {
		this.tran_date_bal = tran_date_bal;
	}
	public Date getRecord_time() {
		return record_time;
	}
	public void setRecord_time(Date record_time) {
		this.record_time = record_time;
	}
	public String getRecord_user() {
		return record_user;
	}
	public void setRecord_user(String record_user) {
		this.record_user = record_user;
	}
	public String getMerchant_particular() {
		return merchant_particular;
	}
	public void setMerchant_particular(String merchant_particular) {
		this.merchant_particular = merchant_particular;
	}
	public String getMerchant_remarks() {
		return merchant_remarks;
	}
	public void setMerchant_remarks(String merchant_remarks) {
		this.merchant_remarks = merchant_remarks;
	}
	public Balance_table_entity(String merchant_id, String merchant_acct_no, String merchant_trading_name,
			Date tran_date, Date end_tran_date, BigDecimal tran_cr_bal, BigDecimal tran_dr_bal, BigDecimal tran_tot_net,
			BigDecimal tran_date_bal, Date record_time, String record_user, String merchant_particular,
			String merchant_remarks) {
		super();
		this.merchant_id = merchant_id;
		this.merchant_acct_no = merchant_acct_no;
		this.merchant_trading_name = merchant_trading_name;
		this.tran_date = tran_date;
		this.end_tran_date = end_tran_date;
		this.tran_cr_bal = tran_cr_bal;
		this.tran_dr_bal = tran_dr_bal;
		this.tran_tot_net = tran_tot_net;
		this.tran_date_bal = tran_date_bal;
		this.record_time = record_time;
		this.record_user = record_user;
		this.merchant_particular = merchant_particular;
		this.merchant_remarks = merchant_remarks;
	}
	public Balance_table_entity() {
		super();
		// TODO Auto-generated constructor stub
	}

	
}
