package com.bornfire.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Table(name="MERCHANT_MASTER_TABLE")
@Entity
public class MerchantMaster {
	@Id
	private String	merchant_id;
	private String	merchant_pow_ca_no;
	private String	merchant_acc_no;
	private String	merchant_addr;
	private String	merchant_legal_id;
	private String	merchant_cont_per;
	private String	merchant_name;
	private String	merchant_cont_details;
	private String	company_name;
	private String	trading_name;
	private String	bill_acc_no;
	private String	tran_curr;
	private String	tran_amount;
	private String	remitter;
	private String	remitter_acc_no;
	private String	remitter_ref_no;
	private String	tran_type;
	private String	tran_remarks1;
	private String	merchant_corp_name;
	private String	merchant_mob_no;
	private String	merchant_type;
	private String	type_of_qr;
	private String	merchant_cat_code;
	private String	merchant_fees;
	private String	merchant_city;
	private String	merchant_posting;
	private String	amount_percentage1;
	private String	periodicity1;
	private String	merchant_terminal;
	private String	merchant_number;
	private String	outlet_no;
	private String	entry_user;
	private Date	entry_time;
	private Date	modify_time;
	private String	auth_user;
	private String	modify_user;
	private Date	auth_time;
	private Character	entity_flg;
	private Character	del_flg;
	private String	freeze_flg;
	private String	freeze_type;
	private String	add_details_req;
	private String	paritioning_type;
	private Character	modify_flg;
	private String	amount_percentage2;
	private String	periodicity2;
	private String	tran_remarks2;
	private String	amount_percentage3;
	private String	periodicity3;
	private String	tran_remarks3;
	private String	tran_remarks;
	
	private String	version;
	private String	modes;
	private String	purpose;
	private String	orgid;
	private String	tid;
	private String	tr;
	private String	tn;
	private String	pa;
	private String	msid;
	private String	mtid;
	private String	bam;
	private String	curr;
	private String	invoiceno;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date	invoicedate;
	private String	invoicename;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date	qrexpire;
	private String	am;
	private String	pincode;
	private String	tier;
	private String	txntype;
	private String	qrmedium;
	private String	tranasactio_crncy;
	private String	tip_or_conv_indicator;
	private String conv_fees_type;
	private String	value_conv_fees;
	private String	zip_code;
	private String	bill_number;
	private String	loyalty_number;
	private String	customer_label;
	private String	store_label;
	private String	terminal_label;
	private String	reference_label;
	private String	purpose_of_tran;
	private String	status;
	private String merchant_coun_cod;
	private String merchant_brand;
	private String merchant_location;
	private String merchant_genre;
	private String merchant_onboard;
	
	public String getMerchant_id() {
		return merchant_id;
	}
	public void setMerchant_id(String merchant_id) {
		this.merchant_id = merchant_id;
	}
	public String getMerchant_pow_ca_no() {
		return merchant_pow_ca_no;
	}
	public void setMerchant_pow_ca_no(String merchant_pow_ca_no) {
		this.merchant_pow_ca_no = merchant_pow_ca_no;
	}
	public String getMerchant_acc_no() {
		return merchant_acc_no;
	}
	public void setMerchant_acc_no(String merchant_acc_no) {
		this.merchant_acc_no = merchant_acc_no;
	}
	public String getMerchant_addr() {
		return merchant_addr;
	}
	public void setMerchant_addr(String merchant_addr) {
		this.merchant_addr = merchant_addr;
	}
	public String getMerchant_legal_id() {
		return merchant_legal_id;
	}
	public void setMerchant_legal_id(String merchant_legal_id) {
		this.merchant_legal_id = merchant_legal_id;
	}
	public String getMerchant_cont_per() {
		return merchant_cont_per;
	}
	public void setMerchant_cont_per(String merchant_cont_per) {
		this.merchant_cont_per = merchant_cont_per;
	}
	public String getMerchant_name() {
		return merchant_name;
	}
	public void setMerchant_name(String merchant_name) {
		this.merchant_name = merchant_name;
	}
	public String getMerchant_cont_details() {
		return merchant_cont_details;
	}
	public void setMerchant_cont_details(String merchant_cont_details) {
		this.merchant_cont_details = merchant_cont_details;
	}
	public String getCompany_name() {
		return company_name;
	}
	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}
	public String getTrading_name() {
		return trading_name;
	}
	public void setTrading_name(String trading_name) {
		this.trading_name = trading_name;
	}
	public String getBill_acc_no() {
		return bill_acc_no;
	}
	public void setBill_acc_no(String bill_acc_no) {
		this.bill_acc_no = bill_acc_no;
	}
	public String getTran_curr() {
		return tran_curr;
	}
	public void setTran_curr(String tran_curr) {
		this.tran_curr = tran_curr;
	}
	public String getTran_amount() {
		return tran_amount;
	}
	public void setTran_amount(String tran_amount) {
		this.tran_amount = tran_amount;
	}
	public String getRemitter() {
		return remitter;
	}
	public void setRemitter(String remitter) {
		this.remitter = remitter;
	}
	public String getRemitter_acc_no() {
		return remitter_acc_no;
	}
	public void setRemitter_acc_no(String remitter_acc_no) {
		this.remitter_acc_no = remitter_acc_no;
	}
	public String getRemitter_ref_no() {
		return remitter_ref_no;
	}
	public void setRemitter_ref_no(String remitter_ref_no) {
		this.remitter_ref_no = remitter_ref_no;
	}
	public String getTran_type() {
		return tran_type;
	}
	public void setTran_type(String tran_type) {
		this.tran_type = tran_type;
	}
	public String getTran_remarks1() {
		return tran_remarks1;
	}
	public void setTran_remarks1(String tran_remarks1) {
		this.tran_remarks1 = tran_remarks1;
	}
	public String getMerchant_corp_name() {
		return merchant_corp_name;
	}
	public void setMerchant_corp_name(String merchant_corp_name) {
		this.merchant_corp_name = merchant_corp_name;
	}
	public String getMerchant_mob_no() {
		return merchant_mob_no;
	}
	public void setMerchant_mob_no(String merchant_mob_no) {
		this.merchant_mob_no = merchant_mob_no;
	}
	public String getMerchant_type() {
		return merchant_type;
	}
	public void setMerchant_type(String merchant_type) {
		this.merchant_type = merchant_type;
	}
	public String getType_of_qr() {
		return type_of_qr;
	}
	public void setType_of_qr(String type_of_qr) {
		this.type_of_qr = type_of_qr;
	}
	public String getMerchant_cat_code() {
		return merchant_cat_code;
	}
	public void setMerchant_cat_code(String merchant_cat_code) {
		this.merchant_cat_code = merchant_cat_code;
	}
	public String getMerchant_fees() {
		return merchant_fees;
	}
	public void setMerchant_fees(String merchant_fees) {
		this.merchant_fees = merchant_fees;
	}
	public String getMerchant_city() {
		return merchant_city;
	}
	public void setMerchant_city(String merchant_city) {
		this.merchant_city = merchant_city;
	}
	public String getMerchant_posting() {
		return merchant_posting;
	}
	public void setMerchant_posting(String merchant_posting) {
		this.merchant_posting = merchant_posting;
	}
	public String getAmount_percentage1() {
		return amount_percentage1;
	}
	public void setAmount_percentage1(String amount_percentage1) {
		this.amount_percentage1 = amount_percentage1;
	}
	public String getPeriodicity1() {
		return periodicity1;
	}
	public void setPeriodicity1(String periodicity1) {
		this.periodicity1 = periodicity1;
	}
	public String getMerchant_terminal() {
		return merchant_terminal;
	}
	public void setMerchant_terminal(String merchant_terminal) {
		this.merchant_terminal = merchant_terminal;
	}
	public String getMerchant_number() {
		return merchant_number;
	}
	public void setMerchant_number(String merchant_number) {
		this.merchant_number = merchant_number;
	}
	public String getOutlet_no() {
		return outlet_no;
	}
	public void setOutlet_no(String outlet_no) {
		this.outlet_no = outlet_no;
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
	public Date getModify_time() {
		return modify_time;
	}
	public void setModify_time(Date modify_time) {
		this.modify_time = modify_time;
	}
	public String getAuth_user() {
		return auth_user;
	}
	public void setAuth_user(String auth_user) {
		this.auth_user = auth_user;
	}
	public String getModify_user() {
		return modify_user;
	}
	public void setModify_user(String modify_user) {
		this.modify_user = modify_user;
	}
	public Date getAuth_time() {
		return auth_time;
	}
	public void setAuth_time(Date auth_time) {
		this.auth_time = auth_time;
	}
	public Character getEntity_flg() {
		return entity_flg;
	}
	public void setEntity_flg(Character entity_flg) {
		this.entity_flg = entity_flg;
	}
	public Character getDel_flg() {
		return del_flg;
	}
	public void setDel_flg(Character del_flg) {
		this.del_flg = del_flg;
	}
	public String getFreeze_flg() {
		return freeze_flg;
	}
	public void setFreeze_flg(String freeze_flg) {
		this.freeze_flg = freeze_flg;
	}
	public String getFreeze_type() {
		return freeze_type;
	}
	public void setFreeze_type(String freeze_type) {
		this.freeze_type = freeze_type;
	}
	public String getAdd_details_req() {
		return add_details_req;
	}
	public void setAdd_details_req(String add_details_req) {
		this.add_details_req = add_details_req;
	}
	public String getParitioning_type() {
		return paritioning_type;
	}
	public void setParitioning_type(String paritioning_type) {
		this.paritioning_type = paritioning_type;
	}
	public Character getModify_flg() {
		return modify_flg;
	}
	public void setModify_flg(Character modify_flg) {
		this.modify_flg = modify_flg;
	}
	public String getAmount_percentage2() {
		return amount_percentage2;
	}
	public void setAmount_percentage2(String amount_percentage2) {
		this.amount_percentage2 = amount_percentage2;
	}
	public String getPeriodicity2() {
		return periodicity2;
	}
	public void setPeriodicity2(String periodicity2) {
		this.periodicity2 = periodicity2;
	}
	public String getTran_remarks2() {
		return tran_remarks2;
	}
	public void setTran_remarks2(String tran_remarks2) {
		this.tran_remarks2 = tran_remarks2;
	}
	public String getAmount_percentage3() {
		return amount_percentage3;
	}
	public void setAmount_percentage3(String amount_percentage3) {
		this.amount_percentage3 = amount_percentage3;
	}
	public String getPeriodicity3() {
		return periodicity3;
	}
	public void setPeriodicity3(String periodicity3) {
		this.periodicity3 = periodicity3;
	}
	public String getTran_remarks3() {
		return tran_remarks3;
	}
	public void setTran_remarks3(String tran_remarks3) {
		this.tran_remarks3 = tran_remarks3;
	}
	public String getTran_remarks() {
		return tran_remarks;
	}
	public void setTran_remarks(String tran_remarks) {
		this.tran_remarks = tran_remarks;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getModes() {
		return modes;
	}
	public void setModes(String modes) {
		this.modes = modes;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getOrgid() {
		return orgid;
	}
	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getTr() {
		return tr;
	}
	public void setTr(String tr) {
		this.tr = tr;
	}
	public String getTn() {
		return tn;
	}
	public void setTn(String tn) {
		this.tn = tn;
	}
	public String getPa() {
		return pa;
	}
	public void setPa(String pa) {
		this.pa = pa;
	}
	public String getMsid() {
		return msid;
	}
	public void setMsid(String msid) {
		this.msid = msid;
	}
	public String getMtid() {
		return mtid;
	}
	public void setMtid(String mtid) {
		this.mtid = mtid;
	}
	public String getBam() {
		return bam;
	}
	public void setBam(String bam) {
		this.bam = bam;
	}
	public String getCurr() {
		return curr;
	}
	public void setCurr(String curr) {
		this.curr = curr;
	}
	public String getInvoiceno() {
		return invoiceno;
	}
	public void setInvoiceno(String invoiceno) {
		this.invoiceno = invoiceno;
	}
	public Date getInvoicedate() {
		return invoicedate;
	}
	public void setInvoicedate(Date invoicedate) {
		this.invoicedate = invoicedate;
	}
	public String getInvoicename() {
		return invoicename;
	}
	public void setInvoicename(String invoicename) {
		this.invoicename = invoicename;
	}
	public Date getQrexpire() {
		return qrexpire;
	}
	public void setQrexpire(Date qrexpire) {
		this.qrexpire = qrexpire;
	}
	public String getAm() {
		return am;
	}
	public void setAm(String am) {
		this.am = am;
	}
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	public String getTier() {
		return tier;
	}
	public void setTier(String tier) {
		this.tier = tier;
	}
	public String getTxntype() {
		return txntype;
	}
	public void setTxntype(String txntype) {
		this.txntype = txntype;
	}
	public String getQrmedium() {
		return qrmedium;
	}
	public void setQrmedium(String qrmedium) {
		this.qrmedium = qrmedium;
	}
	public String getTranasactio_crncy() {
		return tranasactio_crncy;
	}
	public void setTranasactio_crncy(String tranasactio_crncy) {
		this.tranasactio_crncy = tranasactio_crncy;
	}
	public String getTip_or_conv_indicator() {
		return tip_or_conv_indicator;
	}
	public void setTip_or_conv_indicator(String tip_or_conv_indicator) {
		this.tip_or_conv_indicator = tip_or_conv_indicator;
	}
	public String getConv_fees_type() {
		return conv_fees_type;
	}
	public void setConv_fees_type(String conv_fees_type) {
		this.conv_fees_type = conv_fees_type;
	}
	public String getValue_conv_fees() {
		return value_conv_fees;
	}
	public void setValue_conv_fees(String value_conv_fees) {
		this.value_conv_fees = value_conv_fees;
	}
	public String getZip_code() {
		return zip_code;
	}
	public void setZip_code(String zip_code) {
		this.zip_code = zip_code;
	}
	public String getBill_number() {
		return bill_number;
	}
	public void setBill_number(String bill_number) {
		this.bill_number = bill_number;
	}
	public String getLoyalty_number() {
		return loyalty_number;
	}
	public void setLoyalty_number(String loyalty_number) {
		this.loyalty_number = loyalty_number;
	}
	public String getCustomer_label() {
		return customer_label;
	}
	public void setCustomer_label(String customer_label) {
		this.customer_label = customer_label;
	}
	public String getStore_label() {
		return store_label;
	}
	public void setStore_label(String store_label) {
		this.store_label = store_label;
	}
	public String getTerminal_label() {
		return terminal_label;
	}
	public void setTerminal_label(String terminal_label) {
		this.terminal_label = terminal_label;
	}
	public String getReference_label() {
		return reference_label;
	}
	public void setReference_label(String reference_label) {
		this.reference_label = reference_label;
	}
	public String getPurpose_of_tran() {
		return purpose_of_tran;
	}
	public void setPurpose_of_tran(String purpose_of_tran) {
		this.purpose_of_tran = purpose_of_tran;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getMerchant_coun_cod() {
		return merchant_coun_cod;
	}
	public void setMerchant_coun_cod(String merchant_coun_cod) {
		this.merchant_coun_cod = merchant_coun_cod;
	}
	public String getMerchant_brand() {
		return merchant_brand;
	}
	public void setMerchant_brand(String merchant_brand) {
		this.merchant_brand = merchant_brand;
	}
	public String getMerchant_location() {
		return merchant_location;
	}
	public void setMerchant_location(String merchant_location) {
		this.merchant_location = merchant_location;
	}
	public String getMerchant_genre() {
		return merchant_genre;
	}
	public void setMerchant_genre(String merchant_genre) {
		this.merchant_genre = merchant_genre;
	}
	public String getMerchant_onboard() {
		return merchant_onboard;
	}
	public void setMerchant_onboard(String merchant_onboard) {
		this.merchant_onboard = merchant_onboard;
	}
	
	public MerchantMaster(String merchant_id, String merchant_pow_ca_no, String merchant_acc_no, String merchant_addr,
			String merchant_legal_id, String merchant_cont_per, String merchant_name, String merchant_cont_details,
			String company_name, String trading_name, String bill_acc_no, String tran_curr, String tran_amount,
			String remitter, String remitter_acc_no, String remitter_ref_no, String tran_type, String tran_remarks1,
			String merchant_corp_name, String merchant_mob_no, String merchant_type, String type_of_qr,
			String merchant_cat_code, String merchant_fees, String merchant_city, String merchant_posting,
			String amount_percentage1, String periodicity1, String merchant_terminal, String merchant_number,
			String outlet_no, String entry_user, Date entry_time, Date modify_time, String auth_user,
			String modify_user, Date auth_time, Character entity_flg, Character del_flg, String freeze_flg,
			String freeze_type, String add_details_req, String paritioning_type, Character modify_flg,
			String amount_percentage2, String periodicity2, String tran_remarks2, String amount_percentage3,
			String periodicity3, String tran_remarks3, String tran_remarks, String version, String modes,
			String purpose, String orgid, String tid, String tr, String tn, String pa, String msid, String mtid,
			String bam, String curr, String invoiceno, Date invoicedate, String invoicename, Date qrexpire, String am,
			String pincode, String tier, String txntype, String qrmedium, String tranasactio_crncy,
			String tip_or_conv_indicator, String conv_fees_type, String value_conv_fees, String zip_code,
			String bill_number, String loyalty_number, String customer_label, String store_label, String terminal_label,
			String reference_label, String purpose_of_tran, String status, String merchant_coun_cod,
			String merchant_brand, String merchant_location, String merchant_genre, String merchant_onboard) {
		super();
		this.merchant_id = merchant_id;
		this.merchant_pow_ca_no = merchant_pow_ca_no;
		this.merchant_acc_no = merchant_acc_no;
		this.merchant_addr = merchant_addr;
		this.merchant_legal_id = merchant_legal_id;
		this.merchant_cont_per = merchant_cont_per;
		this.merchant_name = merchant_name;
		this.merchant_cont_details = merchant_cont_details;
		this.company_name = company_name;
		this.trading_name = trading_name;
		this.bill_acc_no = bill_acc_no;
		this.tran_curr = tran_curr;
		this.tran_amount = tran_amount;
		this.remitter = remitter;
		this.remitter_acc_no = remitter_acc_no;
		this.remitter_ref_no = remitter_ref_no;
		this.tran_type = tran_type;
		this.tran_remarks1 = tran_remarks1;
		this.merchant_corp_name = merchant_corp_name;
		this.merchant_mob_no = merchant_mob_no;
		this.merchant_type = merchant_type;
		this.type_of_qr = type_of_qr;
		this.merchant_cat_code = merchant_cat_code;
		this.merchant_fees = merchant_fees;
		this.merchant_city = merchant_city;
		this.merchant_posting = merchant_posting;
		this.amount_percentage1 = amount_percentage1;
		this.periodicity1 = periodicity1;
		this.merchant_terminal = merchant_terminal;
		this.merchant_number = merchant_number;
		this.outlet_no = outlet_no;
		this.entry_user = entry_user;
		this.entry_time = entry_time;
		this.modify_time = modify_time;
		this.auth_user = auth_user;
		this.modify_user = modify_user;
		this.auth_time = auth_time;
		this.entity_flg = entity_flg;
		this.del_flg = del_flg;
		this.freeze_flg = freeze_flg;
		this.freeze_type = freeze_type;
		this.add_details_req = add_details_req;
		this.paritioning_type = paritioning_type;
		this.modify_flg = modify_flg;
		this.amount_percentage2 = amount_percentage2;
		this.periodicity2 = periodicity2;
		this.tran_remarks2 = tran_remarks2;
		this.amount_percentage3 = amount_percentage3;
		this.periodicity3 = periodicity3;
		this.tran_remarks3 = tran_remarks3;
		this.tran_remarks = tran_remarks;
		this.version = version;
		this.modes = modes;
		this.purpose = purpose;
		this.orgid = orgid;
		this.tid = tid;
		this.tr = tr;
		this.tn = tn;
		this.pa = pa;
		this.msid = msid;
		this.mtid = mtid;
		this.bam = bam;
		this.curr = curr;
		this.invoiceno = invoiceno;
		this.invoicedate = invoicedate;
		this.invoicename = invoicename;
		this.qrexpire = qrexpire;
		this.am = am;
		this.pincode = pincode;
		this.tier = tier;
		this.txntype = txntype;
		this.qrmedium = qrmedium;
		this.tranasactio_crncy = tranasactio_crncy;
		this.tip_or_conv_indicator = tip_or_conv_indicator;
		this.conv_fees_type = conv_fees_type;
		this.value_conv_fees = value_conv_fees;
		this.zip_code = zip_code;
		this.bill_number = bill_number;
		this.loyalty_number = loyalty_number;
		this.customer_label = customer_label;
		this.store_label = store_label;
		this.terminal_label = terminal_label;
		this.reference_label = reference_label;
		this.purpose_of_tran = purpose_of_tran;
		this.status = status;
		this.merchant_coun_cod = merchant_coun_cod;
		this.merchant_brand = merchant_brand;
		this.merchant_location = merchant_location;
		this.merchant_genre = merchant_genre;
		this.merchant_onboard = merchant_onboard;
	}
	public MerchantMaster() {
		super();
		// TODO Auto-generated constructor stub
	}
	

	
}
