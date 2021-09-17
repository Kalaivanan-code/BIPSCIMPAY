package com.bornfire.entity;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;



@Transactional
@Repository
public interface SettlementAccountRep extends JpaRepository<SettlementAccount, String> {

	@Modifying
	@Query(value = "update BIPS_SETTL_ACCTS set not_bal=?1  where acc_code='06'", nativeQuery = true)
	void updateNotBalData(String amount);
	
	@Query(value = "select * from BIPS_SETTL_ACCTS  where acc_code='06'", nativeQuery = true)
	SettlementAccount getNotBalData();
	
	
}
