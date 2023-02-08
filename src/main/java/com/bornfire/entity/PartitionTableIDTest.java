package com.bornfire.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Embeddable;

@Embeddable
public class PartitionTableIDTest implements Serializable {

	private String	merchant_id;
	private String	tran_id;
	private BigDecimal	part_tran_id;
	public String getMerchant_id() {
		return merchant_id;
	}
	public void setMerchant_id(String merchant_id) {
		this.merchant_id = merchant_id;
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
	public PartitionTableIDTest() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
