package com.bornfire.entity;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;



@Transactional
@Repository
public interface UPIQREntityRep extends JpaRepository<QRUrlGlobalEntity, String> {

	@Query(value = "select * from BIPS_QR_UPI_GLOBAL where mid=?1", nativeQuery = true)
	QRUrlGlobalEntity findBymid(String bankAgent);
	
	
}
