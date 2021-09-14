package com.bornfire.entity;

import java.util.Date;
import java.util.Optional;

import javax.transaction.Transactional;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Transactional
@Repository

public interface SettlementAccountAmtRep extends JpaRepository<SettlementAccountAmtTable, Date> {

	@Query(value = "select * from BIPS_SETTL_ACCTS_AMT_TABLE where trunc(acct_bal_time)=?1", nativeQuery = true)
	Optional<SettlementAccountAmtTable> customfindById(String previousDay);
	
	@Modifying
	@Query(value = "update BIPS_SETTL_ACCTS_AMT_TABLE set payable_acct_bal=?2 where trunc(acct_bal_time)=?1", nativeQuery = true)
	void updatePayableAmount(String previousDay,String amount);
	
	@Modifying
	@Query(value = "update BIPS_SETTL_ACCTS_AMT_TABLE set receivable_acct_bal=?2 where trunc(acct_bal_time)=?1", nativeQuery = true)
	void updateReceivableAmount(String previousDay,String amount);
	
	

}
