package com.bornfire.entity.Outgoing;



import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Table(name="BIPS_BUSINESS_CENTRAL_ACCOUNT_MONITORING_TABLE")
public class Business_central_bank_detail_entity {

	
	private String	acct_number;
	private String	acct_name;
	private String	acct_crncy_code;
	private String	bank_code;
	private String	bank_name;
	private String bank_agent;
	@Id
	private String	bc_gl_id;
	private String	bc_acct_type;
	private String	bc_dimension_id;
	private String	tran_type;
	private String	remarks;
	private BigDecimal	debit_cap;
	private String	entry_user;
	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
	private Date	entry_time;
	private String	modify_user;
	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
	private Date	modify_time;
	private String	verify_user;
	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
	private Date	verify_time;
	private String	user_field_1;
	private String	user_field_2;
	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
	private Date	user_field_3;
	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
	private Date	user_field_4;
	private String entry_flag;
	private String modify_flag;
	private String verify_flag;
	private String del_flg;
	private String disable_flg;
	private String del_user;
	private String disable_user;
	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
	private Date del_time;
	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
	private Date disable_time;
	private String new_bank_flg;
	
	
	public String getDel_user() {
		return del_user;
	}
	public void setDel_user(String del_user) {
		this.del_user = del_user;
	}
	public String getDisable_user() {
		return disable_user;
	}
	public void setDisable_user(String disable_user) {
		this.disable_user = disable_user;
	}
	public Date getDel_time() {
		return del_time;
	}
	public void setDel_time(Date del_time) {
		this.del_time = del_time;
	}
	public Date getDisable_time() {
		return disable_time;
	}
	public void setDisable_time(Date disable_time) {
		this.disable_time = disable_time;
	}
	public String getAcct_number() {
		return acct_number;
	}
	public void setAcct_number(String acct_number) {
		this.acct_number = acct_number;
	}
	public String getAcct_name() {
		return acct_name;
	}
	public void setAcct_name(String acct_name) {
		this.acct_name = acct_name;
	}
	public String getAcct_crncy_code() {
		return acct_crncy_code;
	}
	public void setAcct_crncy_code(String acct_crncy_code) {
		this.acct_crncy_code = acct_crncy_code;
	}
	public String getBank_code() {
		return bank_code;
	}
	public void setBank_code(String bank_code) {
		this.bank_code = bank_code;
	}
	public String getBank_name() {
		return bank_name;
	}
	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}
	public String getBc_gl_id() {
		return bc_gl_id;
	}
	public void setBc_gl_id(String bc_gl_id) {
		this.bc_gl_id = bc_gl_id;
	}
	public String getBc_acct_type() {
		return bc_acct_type;
	}
	public void setBc_acct_type(String bc_acct_type) {
		this.bc_acct_type = bc_acct_type;
	}
	public String getBc_dimension_id() {
		return bc_dimension_id;
	}
	public void setBc_dimension_id(String bc_dimension_id) {
		this.bc_dimension_id = bc_dimension_id;
	}
	public String getTran_type() {
		return tran_type;
	}
	public void setTran_type(String tran_type) {
		this.tran_type = tran_type;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public BigDecimal getDebit_cap() {
		return debit_cap;
	}
	public void setDebit_cap(BigDecimal debit_cap) {
		this.debit_cap = debit_cap;
	}
	public String getEntry_user() {
		return entry_user;
	}
	public void setEntry_user(String entry_user) {
		this.entry_user = entry_user;
	}
	public Date getEntry_time() {
		return entry_time;
	}
	public void setEntry_time(Date entry_time) {
		this.entry_time = entry_time;
	}
	public String getModify_user() {
		return modify_user;
	}
	public void setModify_user(String modify_user) {
		this.modify_user = modify_user;
	}
	public Date getModify_time() {
		return modify_time;
	}
	public void setModify_time(Date modify_time) {
		this.modify_time = modify_time;
	}
	public String getVerify_user() {
		return verify_user;
	}
	public void setVerify_user(String verify_user) {
		this.verify_user = verify_user;
	}
	public Date getVerify_time() {
		return verify_time;
	}
	public void setVerify_time(Date verify_time) {
		this.verify_time = verify_time;
	}
	public String getUser_field_1() {
		return user_field_1;
	}
	public void setUser_field_1(String user_field_1) {
		this.user_field_1 = user_field_1;
	}
	public String getUser_field_2() {
		return user_field_2;
	}
	public void setUser_field_2(String user_field_2) {
		this.user_field_2 = user_field_2;
	}
	public Date getUser_field_3() {
		return user_field_3;
	}
	public void setUser_field_3(Date user_field_3) {
		this.user_field_3 = user_field_3;
	}
	public Date getUser_field_4() {
		return user_field_4;
	}
	public void setUser_field_4(Date user_field_4) {
		this.user_field_4 = user_field_4;
	}
	public String getEntry_flag() {
		return entry_flag;
	}
	public void setEntry_flag(String entry_flag) {
		this.entry_flag = entry_flag;
	}
	public String getModify_flag() {
		return modify_flag;
	}
	public void setModify_flag(String modify_flag) {
		this.modify_flag = modify_flag;
	}
	public String getVerify_flag() {
		return verify_flag;
	}
	public void setVerify_flag(String verify_flag) {
		this.verify_flag = verify_flag;
	}
	public String getDel_flg() {
		return del_flg;
	}
	public void setDel_flg(String del_flg) {
		this.del_flg = del_flg;
	}
	
	
	
	public String getBank_agent() {
		return bank_agent;
	}
	public void setBank_agent(String bank_agent) {
		this.bank_agent = bank_agent;
	}




	public String getDisable_flg() {
		return disable_flg;
	}
	public void setDisable_flg(String disable_flg) {
		this.disable_flg = disable_flg;
	}
	public Business_central_bank_detail_entity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public String getNew_bank_flg() {
		return new_bank_flg;
	}
	public void setNew_bank_flg(String new_bank_flg) {
		this.new_bank_flg = new_bank_flg;
	}
	public Business_central_bank_detail_entity(String acct_number, String acct_name, String acct_crncy_code,
			String bank_code, String bank_name, String bank_agent, String bc_gl_id, String bc_acct_type,
			String bc_dimension_id, String tran_type, String remarks, BigDecimal debit_cap, String entry_user,
			Date entry_time, String modify_user, Date modify_time, String verify_user, Date verify_time,
			String user_field_1, String user_field_2, Date user_field_3, Date user_field_4, String entry_flag,
			String modify_flag, String verify_flag, String del_flg, String disable_flg, String del_user,
			String disable_user, Date del_time, Date disable_time,String new_bank_flg) {
		super();
		this.acct_number = acct_number;
		this.acct_name = acct_name;
		this.acct_crncy_code = acct_crncy_code;
		this.bank_code = bank_code;
		this.bank_name = bank_name;
		this.bank_agent = bank_agent;
		this.bc_gl_id = bc_gl_id;
		this.bc_acct_type = bc_acct_type;
		this.bc_dimension_id = bc_dimension_id;
		this.tran_type = tran_type;
		this.remarks = remarks;
		this.debit_cap = debit_cap;
		this.entry_user = entry_user;
		this.entry_time = entry_time;
		this.modify_user = modify_user;
		this.modify_time = modify_time;
		this.verify_user = verify_user;
		this.verify_time = verify_time;
		this.user_field_1 = user_field_1;
		this.user_field_2 = user_field_2;
		this.user_field_3 = user_field_3;
		this.user_field_4 = user_field_4;
		this.entry_flag = entry_flag;
		this.modify_flag = modify_flag;
		this.verify_flag = verify_flag;
		this.del_flg = del_flg;
		this.disable_flg = disable_flg;
		this.del_user = del_user;
		this.disable_user = disable_user;
		this.del_time = del_time;
		this.disable_time = disable_time;
		this.new_bank_flg=new_bank_flg;
	}
	public Business_central_bank_detail_entity(Business_central_bank_detail_entity bc_bank_detail_entity) {
		// TODO Auto-generated constructor stub
	}
	 
	
	public Business_central_bank_detail_entity(CreditBankAgentTmpTable bc_bank_detail_entity) {
		// TODO Auto-generated constructor stub
		this.bank_code = bc_bank_detail_entity.getBank_code();
		this.acct_number = bc_bank_detail_entity.getAcct_number();
		this.acct_name = bc_bank_detail_entity.getAcct_name();
		this.acct_crncy_code = bc_bank_detail_entity.getAcct_crncy_code();
		this.bank_code = bc_bank_detail_entity.getBank_code();
		this.bank_name = bc_bank_detail_entity.getBank_name();
		this.bc_gl_id = bc_bank_detail_entity.getBc_gl_id();
		this.bc_acct_type = bc_bank_detail_entity.getBc_acct_type();
		this.bc_dimension_id = bc_bank_detail_entity.getBc_dimension_id();
		this.tran_type = bc_bank_detail_entity.getTran_type();
		this.remarks = bc_bank_detail_entity.getRemarks();
		this.debit_cap = bc_bank_detail_entity.getDebit_cap();
		this.entry_user = bc_bank_detail_entity.getEntry_user();
		this.entry_time = bc_bank_detail_entity.getEntry_time();
		this.modify_user = bc_bank_detail_entity.getModify_user();
		this.modify_time = bc_bank_detail_entity.getModify_time();
		this.verify_user = bc_bank_detail_entity.getVerify_user();
		this.verify_time = bc_bank_detail_entity.getVerify_time();
		this.user_field_1 = bc_bank_detail_entity.getUser_field_1();
		this.user_field_2 = bc_bank_detail_entity.getUser_field_2();
		this.user_field_3 = bc_bank_detail_entity.getUser_field_3();
		this.user_field_4 = bc_bank_detail_entity.getUser_field_4();
		this.entry_flag = bc_bank_detail_entity.getEntry_flag();
		this.modify_flag = bc_bank_detail_entity.getModify_flag();
		this.verify_flag = bc_bank_detail_entity.getVerify_flag();
		this.del_flg = bc_bank_detail_entity.getDel_flg();
		this.bank_agent = bc_bank_detail_entity.getBank_agent();
		this.disable_flg = bc_bank_detail_entity.getDisable_flg();
		this.del_user = bc_bank_detail_entity.getDel_user();
		this.disable_user = bc_bank_detail_entity.getDisable_user();
		this.del_time = bc_bank_detail_entity.getDel_time();
		this.disable_time = bc_bank_detail_entity.getDisable_time();
		this.new_bank_flg= bc_bank_detail_entity.getNew_bank_flg();
	}

}
