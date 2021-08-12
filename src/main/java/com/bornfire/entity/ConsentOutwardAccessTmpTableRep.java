package com.bornfire.entity;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Transactional
@Repository
public interface ConsentOutwardAccessTmpTableRep extends JpaRepository<ConsentOutwardAccessTmpTable, String> {

	@Query(value = "select * from BIPS_CONSENT_OUTWARD_ACCESS_TMP_TABLE where CONSENT_ID=?1", nativeQuery = true)
	List<ConsentOutwardAccessTmpTable> finByConsentID(String consentID);
	
	
	@Query(value = "select * from BIPS_CONSENT_OUTWARD_ACCESS_TMP_TABLE where X_REQUEST_ID=?1", nativeQuery = true)
	List<Object[]> existsByX_Request_ID(String x_request_id);

}