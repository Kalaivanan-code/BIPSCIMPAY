package com.bornfire.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name="BIPS_TRAN_CIM_GL_TABLE")

@IdClass(TranCimGLDetailID.class)
public class TranCimGLTable implements Serializable{

	private String request_uuid;
	private String channel_id;
	private String service_request_version;
	private String service_request_id;
	private Date message_date_time;
	private String country_code;
	@Id
	private String tran_no;
	private String batch_no;
	private String module;
	@Id
	private String srl_no1;
	private String tran_type1;
	private String acct_no1;
	private String acct_type1;
	private BigDecimal tran_amt1;
	private String currency_code1;
	private Date posting_date1;
	private String tran_code1;
	private String tran_desc1;
	private String tran_remarks1;
	private String rate1;
	private Date value_date;
	
	
	private String srl_no2;
	private String tran_type2;
	private String acct_no2;
	private String acct_type2;
	private BigDecimal tran_amt2;
	private String currency_code2;
	private Date posting_date2;
	private String tran_code2;
	private String tran_desc2;
	private String tran_remarks2;
	private String rate2;
	private String status;
	private String status_code;
	private String message;
	public String getRequest_uuid() {
		return request_uuid;
	}
	public void setRequest_uuid(String request_uuid) {
		this.request_uuid = request_uuid;
	}
	public String getChannel_id() {
		return channel_id;
	}
	public void setChannel_id(String channel_id) {
		this.channel_id = channel_id;
	}
	public String getService_request_version() {
		return service_request_version;
	}
	public void setService_request_version(String service_request_version) {
		this.service_request_version = service_request_version;
	}
	public String getService_request_id() {
		return service_request_id;
	}
	public void setService_request_id(String service_request_id) {
		this.service_request_id = service_request_id;
	}
	public Date getMessage_date_time() {
		return message_date_time;
	}
	public void setMessage_date_time(Date message_date_time) {
		this.message_date_time = message_date_time;
	}
	public String getCountry_code() {
		return country_code;
	}
	public void setCountry_code(String country_code) {
		this.country_code = country_code;
	}
	public String getTran_no() {
		return tran_no;
	}
	public void setTran_no(String tran_no) {
		this.tran_no = tran_no;
	}
	public String getBatch_no() {
		return batch_no;
	}
	public void setBatch_no(String batch_no) {
		this.batch_no = batch_no;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getSrl_no1() {
		return srl_no1;
	}
	public void setSrl_no1(String srl_no1) {
		this.srl_no1 = srl_no1;
	}
	public String getTran_type1() {
		return tran_type1;
	}
	public void setTran_type1(String tran_type1) {
		this.tran_type1 = tran_type1;
	}
	public String getAcct_no1() {
		return acct_no1;
	}
	public void setAcct_no1(String acct_no1) {
		this.acct_no1 = acct_no1;
	}
	public String getAcct_type1() {
		return acct_type1;
	}
	public void setAcct_type1(String acct_type1) {
		this.acct_type1 = acct_type1;
	}
	public BigDecimal getTran_amt1() {
		return tran_amt1;
	}
	public void setTran_amt1(BigDecimal tran_amt1) {
		this.tran_amt1 = tran_amt1;
	}
	public String getCurrency_code1() {
		return currency_code1;
	}
	public void setCurrency_code1(String currency_code1) {
		this.currency_code1 = currency_code1;
	}
	public Date getPosting_date1() {
		return posting_date1;
	}
	public void setPosting_date1(Date posting_date1) {
		this.posting_date1 = posting_date1;
	}
	public String getTran_code1() {
		return tran_code1;
	}
	public void setTran_code1(String tran_code1) {
		this.tran_code1 = tran_code1;
	}
	public String getTran_desc1() {
		return tran_desc1;
	}
	public void setTran_desc1(String tran_desc1) {
		this.tran_desc1 = tran_desc1;
	}
	public String getTran_remarks1() {
		return tran_remarks1;
	}
	public void setTran_remarks1(String tran_remarks1) {
		this.tran_remarks1 = tran_remarks1;
	}
	public String getRate1() {
		return rate1;
	}
	public void setRate1(String rate1) {
		this.rate1 = rate1;
	}
	public String getSrl_no2() {
		return srl_no2;
	}
	public void setSrl_no2(String srl_no2) {
		this.srl_no2 = srl_no2;
	}
	public String getTran_type2() {
		return tran_type2;
	}
	public void setTran_type2(String tran_type2) {
		this.tran_type2 = tran_type2;
	}
	public String getAcct_no2() {
		return acct_no2;
	}
	public void setAcct_no2(String acct_no2) {
		this.acct_no2 = acct_no2;
	}
	public String getAcct_type2() {
		return acct_type2;
	}
	public void setAcct_type2(String acct_type2) {
		this.acct_type2 = acct_type2;
	}
	public BigDecimal getTran_amt2() {
		return tran_amt2;
	}
	public void setTran_amt2(BigDecimal tran_amt2) {
		this.tran_amt2 = tran_amt2;
	}
	public String getCurrency_code2() {
		return currency_code2;
	}
	public void setCurrency_code2(String currency_code2) {
		this.currency_code2 = currency_code2;
	}
	public Date getPosting_date2() {
		return posting_date2;
	}
	public void setPosting_date2(Date posting_date2) {
		this.posting_date2 = posting_date2;
	}
	public String getTran_code2() {
		return tran_code2;
	}
	public void setTran_code2(String tran_code2) {
		this.tran_code2 = tran_code2;
	}
	public String getTran_desc2() {
		return tran_desc2;
	}
	public void setTran_desc2(String tran_desc2) {
		this.tran_desc2 = tran_desc2;
	}
	public String getTran_remarks2() {
		return tran_remarks2;
	}
	public void setTran_remarks2(String tran_remarks2) {
		this.tran_remarks2 = tran_remarks2;
	}
	public String getRate2() {
		return rate2;
	}
	public void setRate2(String rate2) {
		this.rate2 = rate2;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatus_code() {
		return status_code;
	}
	public void setStatus_code(String status_code) {
		this.status_code = status_code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
	public Date getValue_date() {
		return value_date;
	}
	public void setValue_date(Date value_date) {
		this.value_date = value_date;
	}
	@Override
	public String toString() {
		return "TranCimGLTable [request_uuid=" + request_uuid + ", channel_id=" + channel_id
				+ ", service_request_version=" + service_request_version + ", service_request_id=" + service_request_id
				+ ", message_date_time=" + message_date_time + ", country_code=" + country_code + ", tran_no=" + tran_no
				+ ", batch_no=" + batch_no + ", module=" + module + ", srl_no1=" + srl_no1 + ", tran_type1="
				+ tran_type1 + ", acct_no1=" + acct_no1 + ", acct_type1=" + acct_type1 + ", tran_amt1=" + tran_amt1
				+ ", currency_code1=" + currency_code1 + ", posting_date1=" + posting_date1 + ", tran_code1="
				+ tran_code1 + ", tran_desc1=" + tran_desc1 + ", tran_remarks1=" + tran_remarks1 + ", rate1=" + rate1
				+ ", srl_no2=" + srl_no2 + ", tran_type2=" + tran_type2 + ", acct_no2=" + acct_no2 + ", acct_type2="
				+ acct_type2 + ", tran_amt2=" + tran_amt2 + ", currency_code2=" + currency_code2 + ", posting_date2="
				+ posting_date2 + ", tran_code2=" + tran_code2 + ", tran_desc2=" + tran_desc2 + ", tran_remarks2="
				+ tran_remarks2 + ", rate2=" + rate2 + ", status=" + status + ", status_code=" + status_code
				+ ", message=" + message + "]";
	}
	
	
	
}
