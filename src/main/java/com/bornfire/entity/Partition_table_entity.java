package com.bornfire.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name="BIPS_TRAN_PARITION_TABLE")
@IdClass(PartitionTableIDTest.class)
public class Partition_table_entity {

	@Id
	private String	merchant_id;
	private String	merchant_acct_no;
	private String	merchant_trading_name;
	private String	tran_type;
	private Date	tran_date;
	private Date	value_date;
	@Id
	private String	tran_id;
	@Id
	private BigDecimal	part_tran_id;
	private String	part_tran_type;
	private String	tran_ref_cur;
	private BigDecimal	tran_ref_cur_amt;
	private BigDecimal	tran_rate;
	private BigDecimal	tran_amt_loc;
	private String	tran_particular;
	private String	tran_remarks;
	private String	partition_type;
	private String	partition_detail;
	private String	cust_ref;
	private String	loan_ref;
	private String ipsx_account_number;
	private String ipsx_acct_name;
	private String tran_type_code;
	private String dbtr_agt;
	private String cdtr_agt;
	private String tran_rmks;
	private String instr_id;
	private String endtoendid;


	
	
	
	
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


	public String getIpsx_account_number() {
		return ipsx_account_number;
	}
	public void setIpsx_account_number(String ipsx_account_number) {
		this.ipsx_account_number = ipsx_account_number;
	}
	public String getIpsx_acct_name() {
		return ipsx_acct_name;
	}
	public void setIpsx_acct_name(String ipsx_acct_name) {
		this.ipsx_acct_name = ipsx_acct_name;
	}
	public String getTran_type_code() {
		return tran_type_code;
	}
	public void setTran_type_code(String tran_type_code) {
		this.tran_type_code = tran_type_code;
	}
	public String getDbtr_agt() {
		return dbtr_agt;
	}
	public void setDbtr_agt(String dbtr_agt) {
		this.dbtr_agt = dbtr_agt;
	}
	public String getCdtr_agt() {
		return cdtr_agt;
	}
	public void setCdtr_agt(String cdtr_agt) {
		this.cdtr_agt = cdtr_agt;
	}
	public String getTran_rmks() {
		return tran_rmks;
	}
	public void setTran_rmks(String tran_rmks) {
		this.tran_rmks = tran_rmks;
	}
	public String getInstr_id() {
		return instr_id;
	}
	public void setInstr_id(String instr_id) {
		this.instr_id = instr_id;
	}
	public String getEndtoendid() {
		return endtoendid;
	}
	public void setEndtoendid(String endtoendid) {
		this.endtoendid = endtoendid;
	}
	public Partition_table_entity() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Partition_table_entity(String merchant_id, String merchant_acct_no, String merchant_trading_name,
			String tran_type, Date tran_date, Date value_date, String tran_id, BigDecimal part_tran_id,
			String part_tran_type, String tran_ref_cur, BigDecimal tran_ref_cur_amt, BigDecimal tran_rate,
			BigDecimal tran_amt_loc, String tran_particular, String tran_remarks, String partition_type,
			String partition_detail, String cust_ref, String loan_ref, String ipsx_account_number,
			String ipsx_acct_name, String tran_type_code, String dbtr_agt, String cdtr_agt, String tran_rmks,
			String instr_id, String endtoendid) {
		super();
		this.merchant_id = merchant_id;
		this.merchant_acct_no = merchant_acct_no;
		this.merchant_trading_name = merchant_trading_name;
		this.tran_type = tran_type;
		this.tran_date = tran_date;
		this.value_date = value_date;
		this.tran_id = tran_id;
		this.part_tran_id = part_tran_id;
		this.part_tran_type = part_tran_type;
		this.tran_ref_cur = tran_ref_cur;
		this.tran_ref_cur_amt = tran_ref_cur_amt;
		this.tran_rate = tran_rate;
		this.tran_amt_loc = tran_amt_loc;
		this.tran_particular = tran_particular;
		this.tran_remarks = tran_remarks;
		this.partition_type = partition_type;
		this.partition_detail = partition_detail;
		this.cust_ref = cust_ref;
		this.loan_ref = loan_ref;
		this.ipsx_account_number = ipsx_account_number;
		this.ipsx_acct_name = ipsx_acct_name;
		this.tran_type_code = tran_type_code;
		this.dbtr_agt = dbtr_agt;
		this.cdtr_agt = cdtr_agt;
		this.tran_rmks = tran_rmks;
		this.instr_id = instr_id;
		this.endtoendid = endtoendid;
	}

	
}
