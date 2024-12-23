package com.bornfire.entity.Outgoing;


import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@IdClass(BulkTransactionDetailID.class)
public class BipsOutgoingGLEntryEntity {


	@Id
	private String doc_sub_id;
	
	@Id
	private String doc_ref_id;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date tran_date;
	
	private String tran_ref_id;
	private String account_no;
	private String acct_name;
	private String ben_bank_code;
	private String tran_crncy_code;
	private BigDecimal tran_amt;
	
	private String ips_ref_no;
	
	private String tran_type;
	private String tran_particular;
	private String tran_code;
	private String acct_type;
	public String getDoc_sub_id() {
		return doc_sub_id;
	}
	public void setDoc_sub_id(String doc_sub_id) {
		this.doc_sub_id = doc_sub_id;
	}
	public String getDoc_ref_id() {
		return doc_ref_id;
	}
	public void setDoc_ref_id(String doc_ref_id) {
		this.doc_ref_id = doc_ref_id;
	}
	public Date getTran_date() {
		return tran_date;
	}
	public void setTran_date(Date tran_date) {
		this.tran_date = tran_date;
	}
	public String getTran_ref_id() {
		return tran_ref_id;
	}
	public void setTran_ref_id(String tran_ref_id) {
		this.tran_ref_id = tran_ref_id;
	}
	public String getAccount_no() {
		return account_no;
	}
	public void setAccount_no(String account_no) {
		this.account_no = account_no;
	}
	public String getAcct_name() {
		return acct_name;
	}
	public void setAcct_name(String acct_name) {
		this.acct_name = acct_name;
	}
	public String getBen_bank_code() {
		return ben_bank_code;
	}
	public void setBen_bank_code(String ben_bank_code) {
		this.ben_bank_code = ben_bank_code;
	}
	public String getTran_crncy_code() {
		return tran_crncy_code;
	}
	public void setTran_crncy_code(String tran_crncy_code) {
		this.tran_crncy_code = tran_crncy_code;
	}
	public BigDecimal getTran_amt() {
		return tran_amt;
	}
	public void setTran_amt(BigDecimal tran_amt) {
		this.tran_amt = tran_amt;
	}
	public String getIps_ref_no() {
		return ips_ref_no;
	}
	public void setIps_ref_no(String ips_ref_no) {
		this.ips_ref_no = ips_ref_no;
	}
	public String getTran_type() {
		return tran_type;
	}
	public void setTran_type(String tran_type) {
		this.tran_type = tran_type;
	}
	public String getTran_particular() {
		return tran_particular;
	}
	public void setTran_particular(String tran_particular) {
		this.tran_particular = tran_particular;
	}
	public String getTran_code() {
		return tran_code;
	}
	public void setTran_code(String tran_code) {
		this.tran_code = tran_code;
	}
	public String getAcct_type() {
		return acct_type;
	}
	public void setAcct_type(String acct_type) {
		this.acct_type = acct_type;
	}
	@Override
	public String toString() {
		return "BipsOutgoingGLEntryEntity [doc_sub_id=" + doc_sub_id + ", doc_ref_id=" + doc_ref_id + ", tran_date="
				+ tran_date + ", tran_ref_id=" + tran_ref_id + ", account_no=" + account_no + ", acct_name=" + acct_name
				+ ", ben_bank_code=" + ben_bank_code + ", tran_crncy_code=" + tran_crncy_code + ", tran_amt=" + tran_amt
				+ ", ips_ref_no=" + ips_ref_no + ", tran_type=" + tran_type + ", tran_particular=" + tran_particular
				+ ", tran_code=" + tran_code + ", acct_type=" + acct_type + "]";
	}
	public BipsOutgoingGLEntryEntity(String doc_sub_id, String doc_ref_id, Date tran_date, String tran_ref_id,
			String account_no, String acct_name, String ben_bank_code, String tran_crncy_code, BigDecimal tran_amt,
			String ips_ref_no, String tran_type, String tran_particular, String tran_code, String acct_type) {
		super();
		this.doc_sub_id = doc_sub_id;
		this.doc_ref_id = doc_ref_id;
		this.tran_date = tran_date;
		this.tran_ref_id = tran_ref_id;
		this.account_no = account_no;
		this.acct_name = acct_name;
		this.ben_bank_code = ben_bank_code;
		this.tran_crncy_code = tran_crncy_code;
		this.tran_amt = tran_amt;
		this.ips_ref_no = ips_ref_no;
		this.tran_type = tran_type;
		this.tran_particular = tran_particular;
		this.tran_code = tran_code;
		this.acct_type = acct_type;
	}
	public BipsOutgoingGLEntryEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
	

	
}
