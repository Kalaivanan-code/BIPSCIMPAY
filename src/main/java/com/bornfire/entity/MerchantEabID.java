package com.bornfire.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Embeddable;



@Embeddable
public class MerchantEabID implements Serializable {

	private String	merchant_id;
	private Date	tran_date;
	public String getMerchant_id() {
		return merchant_id;
	}
	public void setMerchant_id(String merchant_id) {
		this.merchant_id = merchant_id;
	}
	public Date getTran_date() {
		return tran_date;
	}
	public void setTran_date(Date tran_date) {
		this.tran_date = tran_date;
	}
	public MerchantEabID() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}