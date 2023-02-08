package com.bornfire.entity;

import java.math.BigDecimal;
import java.util.Date;

public class CIMUPIMerchantResponse {

	private String vers;
	private String modes;
	private String purpose;
	private String orgid;
	private String tid;
	private String tr;
	private String tn;
	
	private String pa;
	private String pn;
	private String mc;


	private String mid;
	private String msid;
	private String mtid;
	private String ccs;
	private BigDecimal bam;
	private String curr;
	private String qrmedium;
	private String invoiceno;


	private Date invoicedate;
	private String qrexpire;
	private String signs;
	private String categorys;
	private String urls;
	private BigDecimal am;
	private String cu;


	private Date qrts;

	private String splits;
	private String pincode;
	private String tiers;
	private String txntype;
	private String consent;
	private String querys;


	

	private String 	mtype;
	private String 	mgr;
	private String 	merchant_onboarding;
	private String 	merchant_location;
	private String 	brand;
	private String 	entips;
	public String getVers() {
		return vers;
	}
	public void setVers(String vers) {
		this.vers = vers;
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
	public String getPn() {
		return pn;
	}
	public void setPn(String pn) {
		this.pn = pn;
	}
	public String getMc() {
		return mc;
	}
	public void setMc(String mc) {
		this.mc = mc;
	}
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
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
	public String getCcs() {
		return ccs;
	}
	public void setCcs(String ccs) {
		this.ccs = ccs;
	}
	public BigDecimal getBam() {
		return bam;
	}
	public void setBam(BigDecimal bam) {
		this.bam = bam;
	}
	public String getCurr() {
		return curr;
	}
	public void setCurr(String curr) {
		this.curr = curr;
	}
	public String getQrmedium() {
		return qrmedium;
	}
	public void setQrmedium(String qrmedium) {
		this.qrmedium = qrmedium;
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
	public String getQrexpire() {
		return qrexpire;
	}
	public void setQrexpire(String qrexpire) {
		this.qrexpire = qrexpire;
	}
	public String getSigns() {
		return signs;
	}
	public void setSigns(String signs) {
		this.signs = signs;
	}
	public String getCategorys() {
		return categorys;
	}
	public void setCategorys(String categorys) {
		this.categorys = categorys;
	}
	public String getUrls() {
		return urls;
	}
	public void setUrls(String urls) {
		this.urls = urls;
	}
	public BigDecimal getAm() {
		return am;
	}
	public void setAm(BigDecimal am) {
		this.am = am;
	}
	public String getCu() {
		return cu;
	}
	public void setCu(String cu) {
		this.cu = cu;
	}
	public Date getQrts() {
		return qrts;
	}
	public void setQrts(Date qrts) {
		this.qrts = qrts;
	}
	public String getSplits() {
		return splits;
	}
	public void setSplits(String splits) {
		this.splits = splits;
	}
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	public String getTiers() {
		return tiers;
	}
	public void setTiers(String tiers) {
		this.tiers = tiers;
	}
	public String getTxntype() {
		return txntype;
	}
	public void setTxntype(String txntype) {
		this.txntype = txntype;
	}
	public String getConsent() {
		return consent;
	}
	public void setConsent(String consent) {
		this.consent = consent;
	}
	public String getQuerys() {
		return querys;
	}
	public void setQuerys(String querys) {
		this.querys = querys;
	}
	public String getMtype() {
		return mtype;
	}
	public void setMtype(String mtype) {
		this.mtype = mtype;
	}
	public String getMgr() {
		return mgr;
	}
	public void setMgr(String mgr) {
		this.mgr = mgr;
	}
	public String getMerchant_onboarding() {
		return merchant_onboarding;
	}
	public void setMerchant_onboarding(String merchant_onboarding) {
		this.merchant_onboarding = merchant_onboarding;
	}
	public String getMerchant_location() {
		return merchant_location;
	}
	public void setMerchant_location(String merchant_location) {
		this.merchant_location = merchant_location;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getEntips() {
		return entips;
	}
	public void setEntips(String entips) {
		this.entips = entips;
	}
	public CIMUPIMerchantResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CIMUPIMerchantResponse(String vers, String modes, String purpose, String orgid, String tid, String tr,
			String tn, String pa, String pn, String mc, String mid, String msid, String mtid, String ccs,
			BigDecimal bam, String curr, String qrmedium, String invoiceno, Date invoicedate, String qrexpire,
			String signs, String categorys, String urls, BigDecimal am, String cu, Date qrts, String splits,
			String pincode, String tiers, String txntype, String consent, String querys, String mtype, String mgr,
			String merchant_onboarding, String merchant_location, String brand, String entips) {
		super();
		this.vers = vers;
		this.modes = modes;
		this.purpose = purpose;
		this.orgid = orgid;
		this.tid = tid;
		this.tr = tr;
		this.tn = tn;
		this.pa = pa;
		this.pn = pn;
		this.mc = mc;
		this.mid = mid;
		this.msid = msid;
		this.mtid = mtid;
		this.ccs = ccs;
		this.bam = bam;
		this.curr = curr;
		this.qrmedium = qrmedium;
		this.invoiceno = invoiceno;
		this.invoicedate = invoicedate;
		this.qrexpire = qrexpire;
		this.signs = signs;
		this.categorys = categorys;
		this.urls = urls;
		this.am = am;
		this.cu = cu;
		this.qrts = qrts;
		this.splits = splits;
		this.pincode = pincode;
		this.tiers = tiers;
		this.txntype = txntype;
		this.consent = consent;
		this.querys = querys;
		this.mtype = mtype;
		this.mgr = mgr;
		this.merchant_onboarding = merchant_onboarding;
		this.merchant_location = merchant_location;
		this.brand = brand;
		this.entips = entips;
	}
	
	
	
}
