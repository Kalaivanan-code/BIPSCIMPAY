package com.bornfire.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="BIPS_SETTLEMENT_LIMIT_REPORT")
public class SettlementLimitReportTable {
	
	@Id
	private String bob_msg_id;
	private String ipsx_msg_id;
	private String ipsx_response_status;
	private Date response_time;
	private String ipsx_status_error;
	private String ipsx_status_code;
	private BigDecimal amt;
	private BigDecimal used_amt;
	private BigDecimal rmng_amt;
	private String used_pctg;
	private Date entry_time;
	
	
	public SettlementLimitReportTable() {
		super();
		// TODO Auto-generated constructor stub
	}


	public SettlementLimitReportTable(String bob_msg_id, String ipsx_msg_id, String ipsx_response_status,
			Date response_time, String ipsx_status_error, BigDecimal amt, BigDecimal used_amt, BigDecimal rmng_amt,
			String used_pctg, Date entry_time) {
		super();
		this.bob_msg_id = bob_msg_id;
		this.ipsx_msg_id = ipsx_msg_id;
		this.ipsx_response_status = ipsx_response_status;
		this.response_time = response_time;
		this.ipsx_status_error = ipsx_status_error;
		this.amt = amt;
		this.used_amt = used_amt;
		this.rmng_amt = rmng_amt;
		this.used_pctg = used_pctg;
		this.entry_time = entry_time;
	}


	public String getBob_msg_id() {
		return bob_msg_id;
	}


	public void setBob_msg_id(String bob_msg_id) {
		this.bob_msg_id = bob_msg_id;
	}


	public String getIpsx_msg_id() {
		return ipsx_msg_id;
	}


	public void setIpsx_msg_id(String ipsx_msg_id) {
		this.ipsx_msg_id = ipsx_msg_id;
	}


	public String getIpsx_response_status() {
		return ipsx_response_status;
	}


	public void setIpsx_response_status(String ipsx_response_status) {
		this.ipsx_response_status = ipsx_response_status;
	}


	public Date getResponse_time() {
		return response_time;
	}


	public void setResponse_time(Date response_time) {
		this.response_time = response_time;
	}


	public String getIpsx_status_error() {
		return ipsx_status_error;
	}


	public void setIpsx_status_error(String ipsx_status_error) {
		this.ipsx_status_error = ipsx_status_error;
	}


	public BigDecimal getAmt() {
		return amt;
	}


	public void setAmt(BigDecimal amt) {
		this.amt = amt;
	}


	public BigDecimal getUsed_amt() {
		return used_amt;
	}


	public void setUsed_amt(BigDecimal used_amt) {
		this.used_amt = used_amt;
	}


	public BigDecimal getRmng_amt() {
		return rmng_amt;
	}


	public void setRmng_amt(BigDecimal rmng_amt) {
		this.rmng_amt = rmng_amt;
	}


	public String getUsed_pctg() {
		return used_pctg;
	}


	public void setUsed_pctg(String used_pctg) {
		this.used_pctg = used_pctg;
	}


	public Date getEntry_time() {
		return entry_time;
	}


	public void setEntry_time(Date entry_time) {
		this.entry_time = entry_time;
	}


	public String getIpsx_status_code() {
		return ipsx_status_code;
	}


	public void setIpsx_status_code(String ipsx_status_code) {
		this.ipsx_status_code = ipsx_status_code;
	}
	
	
	
	
}
