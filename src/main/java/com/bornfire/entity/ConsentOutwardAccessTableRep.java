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

}