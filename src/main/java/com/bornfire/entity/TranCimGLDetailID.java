package com.bornfire.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.Id;

@Embeddable
public class TranCimGLDetailID implements Serializable{

	@Id
	private String tran_no;
	@Id
	private String srl_no1;
	public String getTran_no() {
		return tran_no;
	}
	public void setTran_no(String tran_no) {
		this.tran_no = tran_no;
	}
	public String getSrl_no1() {
		return srl_no1;
	}
	public void setSrl_no1(String srl_no1) {
		this.srl_no1 = srl_no1;
	}
	
}
