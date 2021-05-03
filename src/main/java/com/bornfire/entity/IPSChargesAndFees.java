package com.bornfire.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "BIPS_CHARGES_AND_FEES_TABLE")
public class IPSChargesAndFees implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BigDecimal srl_no;
	@Id
	private String reference_number;
	private String description;
	private String type;
	private String criteria;
	private BigDecimal fees;
	private BigDecimal percentage;
	private BigDecimal min;
	private BigDecimal max;
	private String periodicity;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date last_tried_date;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date next_due_date;
	private String borne_by;
	private String payable_to;
	private BigDecimal amount;
	private String script_name;
	private String currency;
	private String entity_flg;
	private String modify_flg;
	private String del_flg;
	private String entry_user;
	private String modify_user;
	private String verify_user;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date entry_time;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date modify_time;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date verify_time;

	
	
	
	public BigDecimal getSrl_no() {
		return srl_no;
	}

	public String getReference_number() {
		return reference_number;
	}

	public String getDescription() {
		return description;
	}

	public String getType() {
		return type;
	}

	public String getCriteria() {
		return criteria;
	}

	public BigDecimal getFees() {
		return fees;
	}

	public BigDecimal getPercentage() {
		return percentage;
	}

	public BigDecimal getMin() {
		return min;
	}

	public BigDecimal getMax() {
		return max;
	}

	public String getPeriodicity() {
		return periodicity;
	}

	public Date getLast_tried_date() {
		return last_tried_date;
	}

	public Date getNext_due_date() {
		return next_due_date;
	}

	public String getBorne_by() {
		return borne_by;
	}

	public String getPayable_to() {
		return payable_to;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public String getScript_name() {
		return script_name;
	}

	public String getCurrency() {
		return currency;
	}

	public void setSrl_no(BigDecimal srl_no) {
		this.srl_no = srl_no;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setReference_number(String reference_number) {
		this.reference_number = reference_number;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public void setFees(BigDecimal fees) {
		this.fees = fees;
	}

	public void setPercentage(BigDecimal percentage) {
		this.percentage = percentage;
	}

	public void setMin(BigDecimal min) {
		this.min = min;
	}

	public void setMax(BigDecimal max) {
		this.max = max;
	}

	public void setPeriodicity(String periodicity) {
		this.periodicity = periodicity;
	}

	public void setLast_tried_date(Date last_tried_date) {
		this.last_tried_date = last_tried_date;
	}

	public void setNext_due_date(Date next_due_date) {
		this.next_due_date = next_due_date;
	}

	public void setBorne_by(String borne_by) {
		this.borne_by = borne_by;
	}

	public void setPayable_to(String payable_to) {
		this.payable_to = payable_to;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public void setScript_name(String script_name) {
		this.script_name = script_name;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getEntity_flg() {
		return entity_flg;
	}

	public String getModify_flg() {
		return modify_flg;
	}

	public String getDel_flg() {
		return del_flg;
	}

	public String getEntry_user() {
		return entry_user;
	}

	public String getModify_user() {
		return modify_user;
	}

	public String getVerify_user() {
		return verify_user;
	}

	public Date getEntry_time() {
		return entry_time;
	}

	public Date getModify_time() {
		return modify_time;
	}

	public Date getVerify_time() {
		return verify_time;
	}

	public void setEntity_flg(String entity_flg) {
		this.entity_flg = entity_flg;
	}

	public void setModify_flg(String modify_flg) {
		this.modify_flg = modify_flg;
	}

	public void setDel_flg(String del_flg) {
		this.del_flg = del_flg;
	}

	public void setEntry_user(String entry_user) {
		this.entry_user = entry_user;
	}

	public void setModify_user(String modify_user) {
		this.modify_user = modify_user;
	}

	public void setVerify_user(String verify_user) {
		this.verify_user = verify_user;
	}

	public void setEntry_time(Date entry_time) {
		this.entry_time = entry_time;
	}

	public void setModify_time(Date modify_time) {
		this.modify_time = modify_time;
	}

	public void setVerify_time(Date verify_time) {
		this.verify_time = verify_time;
	}

	public IPSChargesAndFees() {
		super();
		// TODO Auto-generated constructor stub
	}

	public IPSChargesAndFees(BigDecimal srl_no, String reference_number, String description, String type,
			String criteria, BigDecimal fees, BigDecimal percentage, BigDecimal min, BigDecimal max, String periodicity,
			Date last_tried_date, Date next_due_date, String borne_by, String payable_to, BigDecimal amount,
			String script_name, String currency, String entity_flg, String modify_flg, String del_flg,
			String entry_user, String modify_user, String verify_user, Date entry_time, Date modify_time,
			Date verify_time) {
		super();
		this.srl_no = srl_no;
		this.reference_number = reference_number;
		this.description = description;
		this.type = type;
		this.criteria = criteria;
		this.fees = fees;
		this.percentage = percentage;
		this.min = min;
		this.max = max;
		this.periodicity = periodicity;
		this.last_tried_date = last_tried_date;
		this.next_due_date = next_due_date;
		this.borne_by = borne_by;
		this.payable_to = payable_to;
		this.amount = amount;
		this.script_name = script_name;
		this.currency = currency;
		this.entity_flg = entity_flg;
		this.modify_flg = modify_flg;
		this.del_flg = del_flg;
		this.entry_user = entry_user;
		this.modify_user = modify_user;
		this.verify_user = verify_user;
		this.entry_time = entry_time;
		this.modify_time = modify_time;
		this.verify_time = verify_time;
	}

}
