package com.bornfire.entity.Outgoing;


import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Transactional
@Repository
public interface CreditBankAgentTableRep extends JpaRepository<Business_central_bank_detail_entity, String> {
	Optional<Business_central_bank_detail_entity> findById(String directorId);


	@Query(value = "select * from BIPS_BUSINESS_CENTRAL_ACCOUNT_MONITORING_TABLE where   del_flg<>'Y' and  disable_flg<>'Y' and acct_number=?1", nativeQuery = true)
	Business_central_bank_detail_entity Findaccountindividual(String id);
}

