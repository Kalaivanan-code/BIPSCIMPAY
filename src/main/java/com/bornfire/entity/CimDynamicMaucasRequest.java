package com.bornfire.entity;

import javax.validation.constraints.NotBlank;

import com.bornfire.exception.TranAmount;

public class CimDynamicMaucasRequest {

	@NotBlank(message="Merchant ID Required")
	private String Merchant_ID;
	
	@NotBlank(message="Transaction Amount Required")
	@TranAmount
	private String Tran_amt;
	private String Mob_num;
	private String Loy_num;
	private String Sto_label;
	private String Cust_label;
	@NotBlank(message="Reference Label Required")
	private String Ref_label;
	private String Ter_label;
	private String Pur_tran;
	private String Add_det;
	@NotBlank(message="Bill Number Required")
	private String Bill_num;
	public String getMerchant_ID() {
		return Merchant_ID;
	}
	public void setMerchant_ID(String merchant_ID) {
		Merchant_ID = merchant_ID;
	}
	public String getTran_amt() {
		return Tran_amt;
	}
	public void setTran_amt(String tran_amt) {
		Tran_amt = tran_amt;
	}
	public String getMob_num() {
		return Mob_num;
	}
	public void setMob_num(String mob_num) {
		Mob_num = mob_num;
	}
	public String getLoy_num() {
		return Loy_num;
	}
	public void setLoy_num(String loy_num) {
		Loy_num = loy_num;
	}
	public String getSto_label() {
		return Sto_label;
	}
	public void setSto_label(String sto_label) {
		Sto_label = sto_label;
	}
	public String getCust_label() {
		return Cust_label;
	}
	public void setCust_label(String cust_label) {
		Cust_label = cust_label;
	}
	public String getRef_label() {
		return Ref_label;
	}
	public void setRef_label(String ref_label) {
		Ref_label = ref_label;
	}
	public String getTer_label() {
		return Ter_label;
	}
	public void setTer_label(String ter_label) {
		Ter_label = ter_label;
	}
	public String getPur_tran() {
		return Pur_tran;
	}
	public void setPur_tran(String pur_tran) {
		Pur_tran = pur_tran;
	}
	public String getAdd_det() {
		return Add_det;
	}
	public void setAdd_det(String add_det) {
		Add_det = add_det;
	}
	public String getBill_num() {
		return Bill_num;
	}
	public void setBill_num(String bill_num) {
		Bill_num = bill_num;
	}
	public CimDynamicMaucasRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "CimDynamicMaucasRequest [Merchant_ID=" + Merchant_ID + ", Tran_amt=" + Tran_amt + ", Mob_num=" + Mob_num
				+ ", Loy_num=" + Loy_num + ", Sto_label=" + Sto_label + ", Cust_label=" + Cust_label + ", Ref_label="
				+ Ref_label + ", Ter_label=" + Ter_label + ", Pur_tran=" + Pur_tran + ", Add_det=" + Add_det
				+ ", Bill_num=" + Bill_num + "]";
	}
	
	
	

}
