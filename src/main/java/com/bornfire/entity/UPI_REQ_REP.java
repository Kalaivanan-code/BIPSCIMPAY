package com.bornfire.entity;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

 

@Transactional
@Repository
public interface UPI_REQ_REP extends JpaRepository<UPI_REQ_QRCODE, String> {

	@Query(value = "select * from BIPS_UPI_REQUEST where req_id=?1", nativeQuery = true)
	UPI_REQ_QRCODE findBymid(String bankAgent);
	
	
}
