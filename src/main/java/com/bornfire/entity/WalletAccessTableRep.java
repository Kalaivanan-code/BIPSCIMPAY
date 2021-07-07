package com.bornfire.entity;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Transactional
@Repository
public interface WalletAccessTableRep extends JpaRepository<WalletAccessTable, String> {

	@Query(value = "select * from BIPS_WALLET_ACCESS_TABLE where identification=?1", nativeQuery = true)
	List<WalletAccessTable> getAccountNumber(String acctNumber);

	@Query(value = "select * from BIPS_WALLET_ACCESS_TABLE where phone_number=?1 and psu_id=?2", nativeQuery = true)
	List<WalletAccessTable> findByData(String phoneNumber, String psuID);
	


}
