package com.bornfire.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.ldap.query.SearchScope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.bornfire.controller.Connect24Service;
import com.bornfire.controller.IPSConnection;
import com.bornfire.controller.IPSDao;
import com.bornfire.entity.AccountContactResponse;
import com.bornfire.entity.BusinessHoursRep;
import com.bornfire.entity.C24FTResponse;
import com.bornfire.entity.RegPublicKey;
import com.bornfire.entity.SettlementAccount;
import com.bornfire.entity.SettlementAccountAmtRep;
import com.bornfire.entity.SettlementAccountAmtTable;
import com.bornfire.entity.SettlementAccountRep;
import com.bornfire.entity.TranMonitorStatus;
import com.bornfire.entity.TransactionMonitor;

@Component
@Service
public class CronJobScheduler {
	private static final Logger logger = LoggerFactory.getLogger(CronJobScheduler.class);

	
	@Autowired
	IPSDao ipsDao;

	@Autowired
	Connect24Service connect24Service;

	@Autowired
	SettlementAccountRep settlAccountRep;

	@Autowired
	SettlementAccountAmtRep settlAccountAmtRep;

	@Autowired
	IPSConnection ipsConnection;

	@Autowired
	SettlementAccountRep settlAccRep;

	@Autowired
	BusinessHoursRep bussinessHoursRep;

	@Autowired
	Environment env;
	
	
	private final LdapTemplate ldapTemplate;

	private List<X509Certificate> LdapCert;
	
	private int count11;
	
	

	public int getCount11() {
		return count11;
	}

	public void setCount11(int count11) {
		this.count11 = count11;
	}

	public List<X509Certificate> getLdapCert() {
		return LdapCert;
	}

	public void setLdapCert(List<X509Certificate> ldapCert) {
		LdapCert = ldapCert;
	}

	private Date settlDate;

	private String settleFlg;

	public Date getSettlDate() {
		return settlDate;
	}

	public void setSettlDate(Date SettlDate) {
		settlDate = SettlDate;
	}

	public String getSettleFlg() {
		return settleFlg;
	}

	public void setSettleFlg(String SettleFlg) {
		settleFlg = SettleFlg;
	}
	
	
	public int requestUUId;

	public int getRequestUUId() {
		return requestUUId;
	}

	public void setRequestUUId(int requestUUId) {
		this.requestUUId = requestUUId;
	}

	@Autowired
	public CronJobScheduler(LdapTemplate ldapTemplate) {
		this.ldapTemplate = ldapTemplate;
	};

	// @Scheduled(cron = "0 0 */4 ? * *")
	/*
	 * public void getReversalTransactionList() {
	 * logger.info("Schedular start for Reversal Transaction :"+new Date());
	 * List<TransactionMonitor> tranList = ipsDao.getReverseTranList();
	 * logger.info("Schedular"+tranList.size()); for(TransactionMonitor
	 * tranData:tranList) {
	 * 
	 * if(tranData.getMsg_type().equals(TranMonitorStatus.OUTGOING.toString())) {
	 * connect24Service.dbtReverseFundRequest(tranData.getBob_account(),tranData.
	 * getTran_audit_number(),
	 * tranData.getTran_currency(),tranData.getTran_amount().toString(),tranData.
	 * getSequence_unique_id()); }else
	 * if(tranData.getMsg_type().equals(TranMonitorStatus.INCOMING.toString())) {
	 * connect24Service.cdtReverseFundRequest(tranData.getBob_account(),tranData.
	 * getTran_amount().toString(),
	 * tranData.getTran_currency(),tranData.getTran_audit_number(),tranData.
	 * getSequence_unique_id()); }else
	 * if(tranData.getMsg_type().equals(TranMonitorStatus.RTP.toString())) {
	 * connect24Service.rtpReverseFundRequest(tranData.getBob_account(),tranData.
	 * getTran_audit_number(),
	 * tranData.getTran_currency(),tranData.getTran_amount().toString(),tranData.
	 * getSequence_unique_id()); } } }
	 */

	
	@Scheduled(cron = "0 0/1 * 1/1 * ?")
	public List<X509Certificate> getCertificates() {
		LdapQuery query = LdapQueryBuilder.query().searchScope(SearchScope.SUBTREE).timeLimit(10000).countLimit(10)
				.base("CN=cert_publisher_test,OU=Participants_test").where("cn").is("cert_publisher_test");

		List<X509Certificate> cert = ldapTemplate.search(query, new CertMapper());

		//this.setLdapCert(cert);
		

		if (cert != null) {
			this.setLdapCert(cert);
			logger.info("LDAP Cert:Success");

		} 

		return cert;

	}
	 

	//@Scheduled(cron = "0 0/1 * 1/1 * ?")
//	public List<X509Certificate> getCertificates() {
//
//		if (ldapTemplate != null) {
//			logger.info("template:OK");
//		} else {
//			logger.info("template:empty");
//		}
//		LdapQuery query = LdapQueryBuilder.query().searchScope(SearchScope.SUBTREE).timeLimit(90000).countLimit(10)
//				.base("CN=cert_publisher,OU=Participants").where("CN").is("cert_publisher");
//		logger.info("get LDAP");
//
//		List<X509Certificate> cert = ldapTemplate.search(query, new CertMapper());
//
//		if (cert != null) {
//			this.setLdapCert(cert);
//			logger.info("LDAP Cert:Success");
//
//		} else {
//			logger.info("LDAP Cert:Failure");
//		}
//
//		return cert;
//
//	}

	private class CertMapper implements AttributesMapper<X509Certificate> {

		@Override
		public X509Certificate mapFromAttributes(Attributes attributes) throws NamingException {
			//logger.info("Mapper");

			//attributes.get("user").size();
			//logger.info(String.valueOf(attributes.get("userCertificate").size()));
			
			X509Certificate cert = null;
			for (int i = 0; i < 1; i++) {
				logger.info(i+"------");
				byte[] octetString = (byte[]) attributes.get("userCertificate").get(i);
				logger.info("Mapper" + octetString);
				FileOutputStream fos;
				File file;

				try {
					 file = ResourceUtils.getFile("classpath:IPS.cer");
					//file = new File("D:\\Certificates\\IPSX_PROD\\IPSPROD.cer");
					fos = new FileOutputStream(file);
					fos.write(octetString);
					fos.close();

					CertificateFactory cf = CertificateFactory.getInstance("X.509");
					FileInputStream in = new FileInputStream(file);
					cert = (X509Certificate) cf.generateCertificate(in);
					logger.info(cert.getSubjectDN().toString());
				} catch (IOException | CertificateException e) {
					// e.printStackTrace();
				}

			}
			return cert;
		}

	}

	///// Get Settlement Amount from Connect24
	@Scheduled(cron = "0 0/1 * 1/1 * ?")
	public void getSettlBal() {

		try {
			
			//logger.info("Get Settlement Account Balance");
			if (settlDate == null) {
				this.setSettlDate(new Date());
				this.setSettleFlg("Y");
			} else {
				DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");

				if (dateFormat.format(getSettlDate()).equals(dateFormat.format(new Date()))) {
					if (getSettleFlg().equals("Y")) {

						logger.info("Get Settlement Account Balance");

						//ResponseEntity<C24FTResponse> balannce = connect24Service
							//	.getBalance(settlAccountRep.findById(env.getProperty("settl.settlment")).get().getAccount_number());

						//if (balannce.getStatusCode() == HttpStatus.OK) {
							Optional<SettlementAccount> settlAccount = settlAccountRep.findById(env.getProperty("settl.settlment"));
							Optional<SettlementAccount> payableAccount = settlAccountRep.findById(env.getProperty("settl.payable"));
							Optional<SettlementAccount> receivableAccount = settlAccountRep.findById(env.getProperty("settl.receivable"));

							if (settlAccount.isPresent()) {

								logger.info("Settlement Account Balance Updated Successfully");

								SettlementAccount setAccount = settlAccount.get();
								SettlementAccount setAccountPayable = payableAccount.get();
								SettlementAccount setAccountReceivable = receivableAccount.get();
								if (setAccount.getAcct_bal_time() == null) {

									setAccount.setPrev_acct_bal(setAccount.getNot_bal());
									setAccount.setAcct_bal(setAccount.getNot_bal());
									setAccount.setNot_bal(
											setAccount.getNot_bal());

									setAccount.setAcct_bal_time(new Date());

									settlAccountRep.save(setAccount);
									
									setAccountPayable.setPrev_acct_bal(setAccountPayable.getNot_bal());
									setAccountPayable.setAcct_bal(setAccountPayable.getNot_bal());
									setAccountPayable.setNot_bal(
											setAccountPayable.getNot_bal());

									setAccountPayable.setAcct_bal_time(new Date());

									settlAccountRep.save(setAccountPayable);
									
									setAccountReceivable.setPrev_acct_bal(setAccountReceivable.getNot_bal());
									setAccountReceivable.setAcct_bal(setAccountReceivable.getNot_bal());
									setAccountReceivable.setNot_bal(
											setAccountReceivable.getNot_bal());

									setAccountReceivable.setAcct_bal_time(new Date());

									settlAccountRep.save(setAccountReceivable);

									this.setSettleFlg("N");

									Optional<SettlementAccountAmtTable> acctExist = settlAccountAmtRep
											.customfindById(new SimpleDateFormat("dd-MMM-yyyy").format(previousDay()));

									SettlementAccountAmtTable settlAmt = new SettlementAccountAmtTable();

									if (acctExist.isPresent()) {
										SettlementAccountAmtTable dd = acctExist.get();
										dd.setPrev_acct_bal(setAccount.getPrev_acct_bal());
										dd.setPayable_acct_bal(new BigDecimal(ipsConnection.payableAmt()));
										dd.setReceivable_acct_bal(new BigDecimal(ipsConnection.receivalbleAmt()));
										dd.setIncome_acct_bal(new BigDecimal(ipsConnection.incomeAmt()));
										dd.setExpense_acct_bal(new BigDecimal(ipsConnection.expenseAmt()));
										settlAccountAmtRep.save(dd);
									}

									settlAmt.setAcct_type(setAccount.getAcct_type());
									settlAmt.setCategory(setAccount.getCategory());
									settlAmt.setAccount_number(setAccount.getAccount_number());
									settlAmt.setName(setAccount.getName());
									settlAmt.setCrncy(setAccount.getCrncy());
									settlAmt.setAcct_bal_time(new Date());
									settlAmt.setAcct_bal(setAccount.getAcct_bal());
									settlAmt.setNot_bal(setAccount.getNot_bal());
									settlAmt.setEntry_time(new Date());
									settlAccountAmtRep.save(settlAmt);

								} else {
									if (dateFormat.format(getSettlDate())
											.equals(dateFormat.format(setAccount.getAcct_bal_time()))) {
										// setAccount.setAcct_bal_time(new Date());
										// settlAccountRep.save(setAccount);
										this.setSettleFlg("N");
									} else {
										setAccount.setPrev_acct_bal(setAccount.getNot_bal());
										setAccount.setAcct_bal(setAccount.getNot_bal());
										setAccount.setNot_bal(setAccount.getNot_bal());

										setAccount.setAcct_bal_time(new Date());
										settlAccountRep.save(setAccount);
										
										setAccountPayable.setPrev_acct_bal(setAccountPayable.getNot_bal());
										setAccountPayable.setAcct_bal(setAccountPayable.getNot_bal());
										setAccountPayable.setNot_bal(
												setAccountPayable.getNot_bal());

										setAccountPayable.setAcct_bal_time(new Date());

										settlAccountRep.save(setAccountPayable);
										
										setAccountReceivable.setPrev_acct_bal(setAccountReceivable.getNot_bal());
										setAccountReceivable.setAcct_bal(setAccountReceivable.getNot_bal());
										setAccountReceivable.setNot_bal(
												setAccountReceivable.getNot_bal());

										setAccountReceivable.setAcct_bal_time(new Date());
										settlAccountRep.save(setAccountReceivable);

										this.setSettleFlg("N");

										Optional<SettlementAccountAmtTable> acctExist = settlAccountAmtRep
												.customfindById(
														new SimpleDateFormat("dd-MMM-yyyy").format(previousDay()));

										SettlementAccountAmtTable settlAmt = new SettlementAccountAmtTable();
										if (acctExist.isPresent()) {
											
											SettlementAccountAmtTable dd = acctExist.get();
											dd.setPrev_acct_bal(setAccount.getPrev_acct_bal());
											dd.setPayable_acct_bal(new BigDecimal(ipsConnection.payableAmt()));
											dd.setReceivable_acct_bal(new BigDecimal(ipsConnection.receivalbleAmt()));
											dd.setIncome_acct_bal(new BigDecimal(ipsConnection.incomeAmt()));
											dd.setExpense_acct_bal(new BigDecimal(ipsConnection.expenseAmt()));
											settlAccountAmtRep.save(dd);
										}
										
										settlAmt.setAcct_type(setAccount.getAcct_type());
										settlAmt.setCategory(setAccount.getCategory());
										settlAmt.setAccount_number(setAccount.getAccount_number());
										settlAmt.setName(setAccount.getName());
										settlAmt.setCrncy(setAccount.getCrncy());
										settlAmt.setAcct_bal_time(new Date());
										settlAmt.setAcct_bal(setAccount.getAcct_bal());
										settlAmt.setNot_bal(setAccount.getNot_bal());
										settlAmt.setEntry_time(new Date());
										settlAccountAmtRep.save(settlAmt);
									}

								}

							}
						//}

					}
				} else {
					this.setSettlDate(new Date());
					this.setSettleFlg("Y");
				}
			}
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
	}

	@Scheduled(cron = "0 0/5 00-01 * * *")
	public void initIncGLPayment() {
		logger.info("GL Payment : Start"+new Date());
		
		ipsConnection.initGLPayment(new SimpleDateFormat("dd-MMM-yyyy").format(previousDay()));
	}
	
///// Get Settlement Amount from Connect24
	//@Scheduled(cron = "0 0/1 * 1/1 * ?")
	public void getSettlBalTest() {

		try {
			logger.info("Get Settlement Account Balance");
			if (settlDate == null) {
				this.setSettlDate(new Date());
				this.setSettleFlg("Y");
			} else {
				DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");

				if (dateFormat.format(getSettlDate()).equals(dateFormat.format(new Date()))) {
					if (getSettleFlg().equals("Y")) {

						logger.info("Get Settlement Account Balance");

						ResponseEntity<C24FTResponse> balannce = connect24Service
								.getBalance(settlAccountRep.findById(env.getProperty("settl.settlment")).get().getAccount_number());

						if (balannce.getStatusCode() == HttpStatus.OK) {
							Optional<SettlementAccount> settlAccount = settlAccountRep.findById(env.getProperty("settl.settlment"));

							if (settlAccount.isPresent()) {

								logger.info("Settlement Account Balance Updated Successfully");

								SettlementAccount setAccount = settlAccount.get();
								if (setAccount.getAcct_bal_time() == null) {

									setAccount.setPrev_acct_bal(setAccount.getNot_bal());
									setAccount.setAcct_bal(
											new BigDecimal(balannce.getBody().getBalance().getAvailableBalance()));
									setAccount.setNot_bal(
											new BigDecimal(balannce.getBody().getBalance().getAvailableBalance()));

									setAccount.setAcct_bal_time(new Date());

									settlAccountRep.save(setAccount);

									this.setSettleFlg("N");

									Optional<SettlementAccountAmtTable> acctExist = settlAccountAmtRep
											.customfindById(new SimpleDateFormat("dd-MMM-yyyy").format(previousDay()));

									if (acctExist.isPresent()) {
										SettlementAccountAmtTable dd = acctExist.get();
										dd.setPrev_acct_bal(setAccount.getPrev_acct_bal());
										dd.setPayable_acct_bal(new BigDecimal(ipsConnection.payableAmt()));
										dd.setReceivable_acct_bal(new BigDecimal(ipsConnection.receivalbleAmt()));
										dd.setIncome_acct_bal(new BigDecimal(ipsConnection.incomeAmt()));
										dd.setExpense_acct_bal(new BigDecimal(ipsConnection.expenseAmt()));
										settlAccountAmtRep.save(dd);
									}

									SettlementAccountAmtTable settlAmt = new SettlementAccountAmtTable();
									settlAmt.setAcct_type(setAccount.getAcct_type());
									settlAmt.setCategory(setAccount.getCategory());
									settlAmt.setAccount_number(setAccount.getAccount_number());
									settlAmt.setName(setAccount.getName());
									settlAmt.setCrncy(setAccount.getCrncy());
									settlAmt.setAcct_bal_time(new Date());
									settlAmt.setAcct_bal(setAccount.getAcct_bal());
									settlAmt.setNot_bal(setAccount.getNot_bal());
									settlAmt.setEntry_time(new Date());
									settlAccountAmtRep.save(settlAmt);

								} else {
									if (dateFormat.format(getSettlDate())
											.equals(dateFormat.format(setAccount.getAcct_bal_time()))) {
										// setAccount.setAcct_bal_time(new Date());
										// settlAccountRep.save(setAccount);
										this.setSettleFlg("N");
									} else {
										setAccount.setPrev_acct_bal(setAccount.getNot_bal());
										setAccount.setAcct_bal(
												new BigDecimal(balannce.getBody().getBalance().getAvailableBalance()));
										setAccount.setNot_bal(
												new BigDecimal(balannce.getBody().getBalance().getAvailableBalance()));

										setAccount.setAcct_bal_time(new Date());
										settlAccountRep.save(setAccount);

										this.setSettleFlg("N");

										Optional<SettlementAccountAmtTable> acctExist = settlAccountAmtRep
												.customfindById(
														new SimpleDateFormat("dd-MMM-yyyy").format(previousDay()));

										if (acctExist.isPresent()) {
											SettlementAccountAmtTable dd = acctExist.get();
											dd.setPrev_acct_bal(setAccount.getPrev_acct_bal());
											dd.setPayable_acct_bal(new BigDecimal(ipsConnection.payableAmt()));
											dd.setReceivable_acct_bal(new BigDecimal(ipsConnection.receivalbleAmt()));
											dd.setIncome_acct_bal(new BigDecimal(ipsConnection.incomeAmt()));
											dd.setExpense_acct_bal(new BigDecimal(ipsConnection.expenseAmt()));
											settlAccountAmtRep.save(dd);
										}
										SettlementAccountAmtTable settlAmt = new SettlementAccountAmtTable();
										settlAmt.setAcct_type(setAccount.getAcct_type());
										settlAmt.setCategory(setAccount.getCategory());
										settlAmt.setAccount_number(setAccount.getAccount_number());
										settlAmt.setName(setAccount.getName());
										settlAmt.setCrncy(setAccount.getCrncy());
										settlAmt.setAcct_bal_time(new Date());
										settlAmt.setAcct_bal(setAccount.getAcct_bal());
										settlAmt.setNot_bal(setAccount.getNot_bal());
										settlAmt.setEntry_time(new Date());
										settlAccountAmtRep.save(settlAmt);
									}

								}

							}
						}

					}
				} else {
					this.setSettlDate(new Date());
					this.setSettleFlg("Y");
				}
			}
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
	}

	private Date previousDay() {
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		return cal.getTime();
	}

	///// MYT Charge Failed Transaction
   // @Scheduled(cron = "0 0 */1 ? * *")
	public void getChargeFailedInMyt() {
		logger.info("Schedular start for Myt Charge :" + new Date());
		List<RegPublicKey> regList = ipsDao.getChargeFailedMytList();
		for (RegPublicKey regData : regList) {
			ipsConnection.ipsChargesAndFeeMyt(regData.getRecord_id(), "MYT_Registration");
		}
	}

	/*
	 * public void TranSettlAcct() {
	 * 
	 * ////Get Payable Account String
	 * payableAcct=settlAccRep.findById("04").get().getAccount_number(); String
	 * receivableAcct=settlAccRep.findById("03").get().getAccount_number(); String
	 * settleAcct=settlAccRep.findById("06").get().getAccount_number();
	 * 
	 * String
	 * settleCutOffTime=bussinessHoursRep.findById("04").get().getIps_start_hrs();
	 * 
	 * 
	 * logger.info("Get Settlement Account Balance"); if(settlDate==null) {
	 * this.setSettlDate(new Date()); this.setSettleFlg("Y"); }else { DateFormat
	 * dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
	 * 
	 * if(dateFormat.format(getSettlDate()).equals(dateFormat.format(new Date()))) {
	 * if(getSettleFlg().equals("Y")) {
	 * 
	 * Optional<SettlementAccountAmtTable> acctExist = settlAccountAmtRep
	 * .customfindById(new SimpleDateFormat("dd-MMM-yyyy").format(new Date()));
	 * 
	 * if (!acctExist.isPresent()) { Optional<SettlementAccount> settlAccount =
	 * settlAccountRep.findById("06"); if (settlAccount.isPresent()) {
	 * 
	 * SettlementAccount setAccount = settlAccount.get();
	 * 
	 * SettlementAccountAmtTable settlAmt=new SettlementAccountAmtTable();
	 * settlAmt.setAcct_type(setAccount.getAcct_type());
	 * settlAmt.setCategory(setAccount.getCategory());
	 * settlAmt.setAccount_number(setAccount.getAccount_number());
	 * settlAmt.setName(setAccount.getName());
	 * settlAmt.setCrncy(setAccount.getCrncy()); settlAmt.setAcct_bal_time(new
	 * Date()); //settlAmt.setAcct_bal(setAccount.getAcct_bal());
	 * //settlAmt.setNot_bal(setAccount.getNot_bal()); settlAmt.setEntry_time(new
	 * Date()); settlAccountAmtRep.save(settlAmt); }
	 * 
	 * }else { if(!acctExist.get().getPayable_flg().equals("Y")) {
	 * ResponseEntity<C24FTResponse> balannce
	 * =connect24Service.getBalance(payableAcct);
	 * 
	 * if (balannce.getStatusCode() == HttpStatus.OK) {
	 * 
	 * BigDecimal amt= if(balannce.getBody().getBalance().getAvailableBalance()) } }
	 * }
	 * 
	 * logger.info("Get Settlement Account Balance");
	 * 
	 * ResponseEntity<C24FTResponse> balannce
	 * =connect24Service.getBalance(settlAccountRep.findById("06").get().
	 * getAccount_number());
	 * 
	 * if (balannce.getStatusCode() == HttpStatus.OK) { Optional<SettlementAccount>
	 * settlAccount=settlAccountRep.findById("06");
	 * 
	 * if(settlAccount.isPresent()) {
	 * 
	 * logger.info("Settlement Account Balance Updated Successfully");
	 * 
	 * SettlementAccount setAccount=settlAccount.get();
	 * if(setAccount.getAcct_bal_time()==null) {
	 * 
	 * setAccount.setPrev_acct_bal(setAccount.getNot_bal());
	 * setAccount.setAcct_bal(new
	 * BigDecimal(balannce.getBody().getBalance().getAvailableBalance()));
	 * setAccount.setNot_bal(new
	 * BigDecimal(balannce.getBody().getBalance().getAvailableBalance()));
	 * 
	 * setAccount.setAcct_bal_time(new Date());
	 * 
	 * settlAccountRep.save(setAccount);
	 * 
	 * this.setSettleFlg("N");
	 * 
	 * Optional<SettlementAccountAmtTable> acctExist=settlAccountAmtRep.
	 * customfindById(new SimpleDateFormat("dd-MMM-yyyy").format(previousDay()));
	 * 
	 * if(acctExist.isPresent()) { SettlementAccountAmtTable dd=acctExist.get();
	 * dd.setPrev_acct_bal(setAccount.getPrev_acct_bal());
	 * settlAccountAmtRep.save(dd); }
	 * 
	 * SettlementAccountAmtTable settlAmt=new SettlementAccountAmtTable();
	 * settlAmt.setAcct_type(setAccount.getAcct_type());
	 * settlAmt.setCategory(setAccount.getCategory());
	 * settlAmt.setAccount_number(setAccount.getAccount_number());
	 * settlAmt.setName(setAccount.getName());
	 * settlAmt.setCrncy(setAccount.getCrncy()); settlAmt.setAcct_bal_time(new
	 * Date()); settlAmt.setAcct_bal(setAccount.getAcct_bal());
	 * settlAmt.setNot_bal(setAccount.getNot_bal()); settlAmt.setEntry_time(new
	 * Date()); settlAccountAmtRep.save(settlAmt);
	 * 
	 * }else {
	 * if(dateFormat.format(getSettlDate()).equals(dateFormat.format(setAccount.
	 * getAcct_bal_time()))) { //setAccount.setAcct_bal_time(new Date());
	 * //settlAccountRep.save(setAccount); this.setSettleFlg("N"); }else {
	 * setAccount.setPrev_acct_bal(setAccount.getNot_bal());
	 * setAccount.setAcct_bal(new
	 * BigDecimal(balannce.getBody().getBalance().getAvailableBalance()));
	 * setAccount.setNot_bal(new
	 * BigDecimal(balannce.getBody().getBalance().getAvailableBalance()));
	 * 
	 * setAccount.setAcct_bal_time(new Date()); settlAccountRep.save(setAccount);
	 * 
	 * this.setSettleFlg("N");
	 * 
	 * Optional<SettlementAccountAmtTable>
	 * acctExist=settlAccountAmtRep.customfindById(new
	 * SimpleDateFormat("dd-MMM-yyyy").format(previousDay()));
	 * 
	 * if(acctExist.isPresent()) { SettlementAccountAmtTable dd=acctExist.get();
	 * dd.setPrev_acct_bal(setAccount.getPrev_acct_bal());
	 * settlAccountAmtRep.save(dd); } SettlementAccountAmtTable settlAmt=new
	 * SettlementAccountAmtTable();
	 * settlAmt.setAcct_type(setAccount.getAcct_type());
	 * settlAmt.setCategory(setAccount.getCategory());
	 * settlAmt.setAccount_number(setAccount.getAccount_number());
	 * settlAmt.setName(setAccount.getName());
	 * settlAmt.setCrncy(setAccount.getCrncy()); settlAmt.setAcct_bal_time(new
	 * Date()); settlAmt.setAcct_bal(setAccount.getAcct_bal());
	 * settlAmt.setNot_bal(setAccount.getNot_bal()); settlAmt.setEntry_time(new
	 * Date()); settlAccountAmtRep.save(settlAmt); }
	 * 
	 * }
	 * 
	 * } }
	 * 
	 * } }else { this.setSettlDate(new Date()); this.setSettleFlg("Y"); } } }
	 */

	
}
