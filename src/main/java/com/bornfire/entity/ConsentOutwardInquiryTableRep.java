package com.bornfire.entity;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Transactional
@Repository
public interface ConsentOutwardInquiryTableRep extends JpaRepository<ConsentOutwardInquiryTable, String> {

	

	@Query(value = "select * from BIPS_CONSENT_OUTWARD_INQUIRY_TABLE where X_REQUEST_ID=?1", nativeQuery = true)
	List<Object[]> existsByX_Request_ID(String x_request_id);

	
}