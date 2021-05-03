package com.bornfire.entity;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Transactional
@Repository
public interface BankAgentTableRep extends JpaRepository<BankAgentTable, String> {

	@Query(value = "select * from BIPS_OTHER_BANK_AGENT_TABLE where bank_agent=?1", nativeQuery = true)
	Optional<BankAgentTable> findByCustomBankName(String bankAgent);

}

