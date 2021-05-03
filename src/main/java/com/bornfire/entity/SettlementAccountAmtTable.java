package com.bornfire.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "BIPS_SETTL_ACCTS_AMT_TABLE")
public class SettlementAccountAmtTable implements Serializable{
	
	private String acct_type;
	private String category;
	private String account_number;
	private String name;
	private String crncy;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date entry_time;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date modify_time;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date verify_time;
	private String entry_user;
	private String modify_user;
	private String verify_user;
	private String del_flg;
	private String entity_flg;
	private String modify_flg;
	private BigDecimal acct_bal;
	private BigDecimal not_bal;
	@Id
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Temporal(TemporalType.DATE)
	private Date acct_bal_time;
	private BigDecimal prev_acct_bal;
	private String payable_flg;
	private String receivable_flg;
	private BigDecimal payable_acct_bal;
	private BigDecimal receivable_acct_bal;
	
	private String income_flg;
	private String expense_flg;
	private BigDecimal income_acct_bal;
	private BigDecimal expense_acct_bal;
	
	public String getAcct_type() {
		return acct_type;
	}
	public void setAcct_type(String acct_type) {
		this.acct_type = acct_type;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getAccount_number() {
		return account_number;
	}
	public void setAccount_number(String account_number) {
		this.account_number = account_number;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCrncy() {
		return crncy;
	}
	public void setCrncy(String crncy) {
		this.crncy = crncy;
	}
	public Date getEntry_time() {
		return entry_time;
	}
	public void setEntry_time(Date entry_time) {
		this.entry_time = entry_time;
	}
	public Date getModify_time() {
		return modify_time;
	}
	public void setModify_time(Date modify_time) {
		this.modify_time = modify_time;
	}
	public Date getVerify_time() {
		return verify_time;
	}
	public void setVerify_time(Date verify_time) {
		this.verify_time = verify_time;
	}
	public String getEntry_user() {
		return entry_user;
	}
	public void setEntry_user(String entry_user) {
		this.entry_user = entry_user;
	}
	public String getModify_user() {
		return modify_user;
	}
	public void setModify_user(String modify_user) {
		this.modify_user = modify_user;
	}
	public String getVerify_user() {
		return verify_user;
	}
	public void setVerify_user(String verify_user) {
		this.verify_user = verify_user;
	}
	public String getDel_flg() {
		return del_flg;
	}
	public void setDel_flg(String del_flg) {
		this.del_flg = del_flg;
	}
	public String getEntity_flg() {
		return entity_flg;
	}
	public void setEntity_flg(String entity_flg) {
		this.entity_flg = entity_flg;
	}
	public String getModify_flg() {
		return modify_flg;
	}
	public void setModify_flg(String modify_flg) {
		this.modify_flg = modify_flg;
	}
	public BigDecimal getAcct_bal() {
		return acct_bal;
	}
	public void setAcct_bal(BigDecimal acct_bal) {
		this.acct_bal = acct_bal;
	}
	public BigDecimal getNot_bal() {
		return not_bal;
	}
	public void setNot_bal(BigDecimal not_bal) {
		this.not_bal = not_bal;
	}
	public Date getAcct_bal_time() {
		return acct_bal_time;
	}
	public void setAcct_bal_time(Date acct_bal_time) {
		this.acct_bal_time = acct_bal_time;
	}
	public BigDecimal getPrev_acct_bal() {
		return prev_acct_bal;
	}
	public void setPrev_acct_bal(BigDecimal prev_acct_bal) {
		this.prev_acct_bal = prev_acct_bal;
	}
	public String getPayable_flg() {
		return payable_flg;
	}
	public void setPayable_flg(String payable_flg) {
		this.payable_flg = payable_flg;
	}
	public String getReceivable_flg() {
		return receivable_flg;
	}
	public void setReceivable_flg(String receivable_flg) {
		this.receivable_flg = receivable_flg;
	}
	public BigDecimal getPayable_acct_bal() {
		return payable_acct_bal;
	}
	public void setPayable_acct_bal(BigDecimal payable_acct_bal) {
		this.payable_acct_bal = payable_acct_bal;
	}
	public BigDecimal getReceivable_acct_bal() {
		return receivable_acct_bal;
	}
	public void setReceivable_acct_bal(BigDecimal receivable_acct_bal) {
		this.receivable_acct_bal = receivable_acct_bal;
	}
	public String getIncome_flg() {
		return income_flg;
	}
	public void setIncome_flg(String income_flg) {
		this.income_flg = income_flg;
	}
	public String getExpense_flg() {
		return expense_flg;
	}
	public void setExpense_flg(String expense_flg) {
		this.expense_flg = expense_flg;
	}
	public BigDecimal getIncome_acct_bal() {
		return income_acct_bal;
	}
	public void setIncome_acct_bal(BigDecimal income_acct_bal) {
		this.income_acct_bal = income_acct_bal;
	}
	public BigDecimal getExpense_acct_bal() {
		return expense_acct_bal;
	}
	public void setExpense_acct_bal(BigDecimal expense_acct_bal) {
		this.expense_acct_bal = expense_acct_bal;
	}
	
	
	
}
