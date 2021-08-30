package com.bornfire.entity;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;



@Transactional
@Repository
public interface ConsentOutwardAccessTableRep extends JpaRepository<ConsentOutwardAccessTable, String> {

	@Query(value = "select * from BIPS_CONSENT_OUTWARD_ACCESS_TABLE where IDENTIFICATION=?1 and NVL(DEL_FLG,1) <> 'Y'", nativeQuery = true)
	List<ConsentOutwardAccessTable> getAccountNumber(String acctNumber);

	@Query(value = "select * from BIPS_CONSENT_OUTWARD_ACCESS_TABLE where CONSENT_ID=?1 ", nativeQuery = true)
	List<ConsentOutwardAccessTable> finByConsentID(String consentID);

	@Query(value = "select a.consent_ID,a.IDENTIFICATION,a.ACCT_NAME,a.RECEIVERPARTICIPANT_BIC,b.bank_name,b.bank_code,a.PSU_ID,a.PHONE_NUMBER,a.SCHM_NAME,a.READ_BALANCE,a.READ_TRAN_DETAILS,READ_ACCT_DETAILS,READ_DEBIT_ACCT from BIPS_CONSENT_OUTWARD_ACCESS_TABLE a,BIPS_OTHER_BANK_AGENT_TABLE b where a.RECEIVERPARTICIPANT_BIC=b.bank_agent and a.psu_id=?1 and (a.del_flg<>'Y' or a.del_flg is null)", nativeQuery = true)
	List<Object[]> finByDocID(String docID);

}