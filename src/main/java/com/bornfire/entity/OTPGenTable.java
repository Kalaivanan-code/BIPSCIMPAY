package com.bornfire.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="BIPS_OTP_GENERATION_TABLE")
public class OTPGenTable {

	@Id
	private String auth_id;
	private String record_id;
	private String otp;
	private String screen_id;
	private Date generated_date;
	private Date expired_date;
	
	public String getAuth_id() {
		return auth_id;
	}
	public void setAuth_id(String auth_id) {
		this.auth_id = auth_id;
	}
	public String getRecord_id() {
		return record_id;
	}
	public void setRecord_id(String record_id) {
		this.record_id = record_id;
	}
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}
	public String getScreen_id() {
		return screen_id;
	}
	public void setScreen_id(String screen_id) {
		this.screen_id = screen_id;
	}
	public Date getGenerated_date() {
		return generated_date;
	}
	public void setGenerated_date(Date generated_date) {
		this.generated_date = generated_date;
	}
	public Date getExpired_date() {
		return expired_date;
	}
	public void setExpired_date(Date expired_date) {
		this.expired_date = expired_date;
	}
	
	
}
