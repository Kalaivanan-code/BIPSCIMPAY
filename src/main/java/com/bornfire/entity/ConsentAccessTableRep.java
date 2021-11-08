package com.bornfire.entity;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public interface ConsentAccessTableRep extends JpaRepository<ConsentAccessTable, String> {

	@Query(value = "select * from BIPS_CONSENT_ACCESS_TABLE where identification=?1", nativeQuery = true)
	List<ConsentAccessTable> getAccountNumber(String acctNumber);
	
	@Query(value = "select * from BIPS_CONSENT_ACCESS_TABLE where identification=?1  and senderparticipant_bic=?2 and del_flg='N'", nativeQuery = true)
	List<ConsentAccessTable> getAccountNumber1(String acctNumber,String senderBic);
	

}

