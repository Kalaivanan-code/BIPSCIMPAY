package com.bornfire.entity.Outgoing;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "BIPS_BULK_OUTGOING_TRAN_MASTER")
@IdClass(BulkTransactionOutgoingDetailID.class)
public class BipsBulkOutgoingTransaction implements Serializable {
	
	@Id
	private String doc_sub_id;
	
	@Id
	private String doc_ref_id;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date tran_date;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date value_date;
	private String tran_ref_id;
	private String tran_type;
	private BigDecimal tran_ref_part_tran_id;
	private String remit_account_no;
	private String rem_name;
	private String rem_mobile_no;
	private String rem_nic_no;
	private String ben_bank_code;
	private String ben_branch_code;
	private String ben_acct_no;
	private String ben_acct_name;
	private String ben_nick_name;
	private String tran_crncy_code;
	private BigDecimal tran_amt;
	private String chk_flg;
	private BigDecimal chg_amt;
	private BigDecimal def_chg_amt;
	private String split_flg;
	private String split_mast_tran_ref;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date entry_time_level1;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date entry_time_level2;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date verify_time;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date modify_time;
	private String entry_user_level1;
	private String entry_user_level2;
	private String modify_user;
	private String verify_user;
	private String entity_flg;
	private String modify_flg;
	private String del_flg;
	private String master_ref_no;
	private String ips_ref_no;
	private String bulk_type;
	private String tran_particular;
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
	public Date getValue_date() {
		return value_date;
	}
	public void setValue_date(Date value_date) {
		this.value_date = value_date;
	}
	public String getTran_ref_id() {
		return tran_ref_id;
	}
	public void setTran_ref_id(String tran_ref_id) {
		this.tran_ref_id = tran_ref_id;
	}
	public String getTran_type() {
		return tran_type;
	}
	public void setTran_type(String tran_type) {
		this.tran_type = tran_type;
	}
	public BigDecimal getTran_ref_part_tran_id() {
		return tran_ref_part_tran_id;
	}
	public void setTran_ref_part_tran_id(BigDecimal tran_ref_part_tran_id) {
		this.tran_ref_part_tran_id = tran_ref_part_tran_id;
	}
	public String getRemit_account_no() {
		return remit_account_no;
	}
	public void setRemit_account_no(String remit_account_no) {
		this.remit_account_no = remit_account_no;
	}
	public String getRem_name() {
		return rem_name;
	}
	public void setRem_name(String rem_name) {
		this.rem_name = rem_name;
	}
	public String getRem_mobile_no() {
		return rem_mobile_no;
	}
	public void setRem_mobile_no(String rem_mobile_no) {
		this.rem_mobile_no = rem_mobile_no;
	}
	public String getRem_nic_no() {
		return rem_nic_no;
	}
	public void setRem_nic_no(String rem_nic_no) {
		this.rem_nic_no = rem_nic_no;
	}
	public String getBen_bank_code() {
		return ben_bank_code;
	}
	public void setBen_bank_code(String ben_bank_code) {
		this.ben_bank_code = ben_bank_code;
	}
	public String getBen_branch_code() {
		return ben_branch_code;
	}
	public void setBen_branch_code(String ben_branch_code) {
		this.ben_branch_code = ben_branch_code;
	}
	public String getBen_acct_no() {
		return ben_acct_no;
	}
	public void setBen_acct_no(String ben_acct_no) {
		this.ben_acct_no = ben_acct_no;
	}
	public String getBen_acct_name() {
		return ben_acct_name;
	}
	public void setBen_acct_name(String ben_acct_name) {
		this.ben_acct_name = ben_acct_name;
	}
	public String getBen_nick_name() {
		return ben_nick_name;
	}
	public void setBen_nick_name(String ben_nick_name) {
		this.ben_nick_name = ben_nick_name;
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
	public String getChk_flg() {
		return chk_flg;
	}
	public void setChk_flg(String chk_flg) {
		this.chk_flg = chk_flg;
	}
	public BigDecimal getChg_amt() {
		return chg_amt;
	}
	public void setChg_amt(BigDecimal chg_amt) {
		this.chg_amt = chg_amt;
	}
	public BigDecimal getDef_chg_amt() {
		return def_chg_amt;
	}
	public void setDef_chg_amt(BigDecimal def_chg_amt) {
		this.def_chg_amt = def_chg_amt;
	}
	public String getSplit_flg() {
		return split_flg;
	}
	public void setSplit_flg(String split_flg) {
		this.split_flg = split_flg;
	}
	public String getSplit_mast_tran_ref() {
		return split_mast_tran_ref;
	}
	public void setSplit_mast_tran_ref(String split_mast_tran_ref) {
		this.split_mast_tran_ref = split_mast_tran_ref;
	}
	public Date getEntry_time_level1() {
		return entry_time_level1;
	}
	public void setEntry_time_level1(Date entry_time_level1) {
		this.entry_time_level1 = entry_time_level1;
	}
	public Date getEntry_time_level2() {
		return entry_time_level2;
	}
	public void setEntry_time_level2(Date entry_time_level2) {
		this.entry_time_level2 = entry_time_level2;
	}
	public Date getVerify_time() {
		return verify_time;
	}
	public void setVerify_time(Date verify_time) {
		this.verify_time = verify_time;
	}
	public Date getModify_time() {
		return modify_time;
	}
	public void setModify_time(Date modify_time) {
		this.modify_time = modify_time;
	}
	public String getEntry_user_level1() {
		return entry_user_level1;
	}
	public void setEntry_user_level1(String entry_user_level1) {
		this.entry_user_level1 = entry_user_level1;
	}
	public String getEntry_user_level2() {
		return entry_user_level2;
	}
	public void setEntry_user_level2(String entry_user_level2) {
		this.entry_user_level2 = entry_user_level2;
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
	public String getDel_flg() {
		return del_flg;
	}
	public void setDel_flg(String del_flg) {
		this.del_flg = del_flg;
	}
	public String getMaster_ref_no() {
		return master_ref_no;
	}
	public void setMaster_ref_no(String master_ref_no) {
		this.master_ref_no = master_ref_no;
	}
	public String getIps_ref_no() {
		return ips_ref_no;
	}
	public void setIps_ref_no(String ips_ref_no) {
		this.ips_ref_no = ips_ref_no;
	}
	public String getBulk_type() {
		return bulk_type;
	}
	public void setBulk_type(String bulk_type) {
		this.bulk_type = bulk_type;
	}
	public String getTran_particular() {
		return tran_particular;
	}
	public void setTran_particular(String tran_particular) {
		this.tran_particular = tran_particular;
	}
	public BipsBulkOutgoingTransaction(String doc_sub_id, String doc_ref_id, Date tran_date, Date value_date,
			String tran_ref_id, String tran_type, BigDecimal tran_ref_part_tran_id, String remit_account_no,
			String rem_name, String rem_mobile_no, String rem_nic_no, String ben_bank_code, String ben_branch_code,
			String ben_acct_no, String ben_acct_name, String ben_nick_name, String tran_crncy_code, BigDecimal tran_amt,
			String chk_flg, BigDecimal chg_amt, BigDecimal def_chg_amt, String split_flg, String split_mast_tran_ref,
			Date entry_time_level1, Date entry_time_level2, Date verify_time, Date modify_time,
			String entry_user_level1, String entry_user_level2, String modify_user, String verify_user,
			String entity_flg, String modify_flg, String del_flg, String master_ref_no, String ips_ref_no,
			String bulk_type, String tran_particular) {
		super();
		this.doc_sub_id = doc_sub_id;
		this.doc_ref_id = doc_ref_id;
		this.tran_date = tran_date;
		this.value_date = value_date;
		this.tran_ref_id = tran_ref_id;
		this.tran_type = tran_type;
		this.tran_ref_part_tran_id = tran_ref_part_tran_id;
		this.remit_account_no = remit_account_no;
		this.rem_name = rem_name;
		this.rem_mobile_no = rem_mobile_no;
		this.rem_nic_no = rem_nic_no;
		this.ben_bank_code = ben_bank_code;
		this.ben_branch_code = ben_branch_code;
		this.ben_acct_no = ben_acct_no;
		this.ben_acct_name = ben_acct_name;
		this.ben_nick_name = ben_nick_name;
		this.tran_crncy_code = tran_crncy_code;
		this.tran_amt = tran_amt;
		this.chk_flg = chk_flg;
		this.chg_amt = chg_amt;
		this.def_chg_amt = def_chg_amt;
		this.split_flg = split_flg;
		this.split_mast_tran_ref = split_mast_tran_ref;
		this.entry_time_level1 = entry_time_level1;
		this.entry_time_level2 = entry_time_level2;
		this.verify_time = verify_time;
		this.modify_time = modify_time;
		this.entry_user_level1 = entry_user_level1;
		this.entry_user_level2 = entry_user_level2;
		this.modify_user = modify_user;
		this.verify_user = verify_user;
		this.entity_flg = entity_flg;
		this.modify_flg = modify_flg;
		this.del_flg = del_flg;
		this.master_ref_no = master_ref_no;
		this.ips_ref_no = ips_ref_no;
		this.bulk_type = bulk_type;
		this.tran_particular = tran_particular;
	}
	public BipsBulkOutgoingTransaction() {
		
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "BipsBulkOutgoingTransaction [doc_sub_id=" + doc_sub_id + ", doc_ref_id=" + doc_ref_id + ", tran_date="
				+ tran_date + ", value_date=" + value_date + ", tran_ref_id=" + tran_ref_id + ", tran_type=" + tran_type
				+ ", tran_ref_part_tran_id=" + tran_ref_part_tran_id + ", remit_account_no=" + remit_account_no
				+ ", rem_name=" + rem_name + ", rem_mobile_no=" + rem_mobile_no + ", rem_nic_no=" + rem_nic_no
				+ ", ben_bank_code=" + ben_bank_code + ", ben_branch_code=" + ben_branch_code + ", ben_acct_no="
				+ ben_acct_no + ", ben_acct_name=" + ben_acct_name + ", ben_nick_name=" + ben_nick_name
				+ ", tran_crncy_code=" + tran_crncy_code + ", tran_amt=" + tran_amt + ", chk_flg=" + chk_flg
				+ ", chg_amt=" + chg_amt + ", def_chg_amt=" + def_chg_amt + ", split_flg=" + split_flg
				+ ", split_mast_tran_ref=" + split_mast_tran_ref + ", entry_time_level1=" + entry_time_level1
				+ ", entry_time_level2=" + entry_time_level2 + ", verify_time=" + verify_time + ", modify_time="
				+ modify_time + ", entry_user_level1=" + entry_user_level1 + ", entry_user_level2=" + entry_user_level2
				+ ", modify_user=" + modify_user + ", verify_user=" + verify_user + ", entity_flg=" + entity_flg
				+ ", modify_flg=" + modify_flg + ", del_flg=" + del_flg + ", master_ref_no=" + master_ref_no
				+ ", ips_ref_no=" + ips_ref_no + ", bulk_type=" + bulk_type + ", tran_particular=" + tran_particular
				+ "]";
	}
	
	
}
