package com.bornfire.entity;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Transactional
@Repository
public interface WalletRequestRegisterTableRep extends JpaRepository<WalletRequestRegisterTable, String> {

	@Query(value = "select * from BIPS_WALLET_REQUEST_REGISTER_TABLE where wallet_id=?1 order by entry_time desc", nativeQuery = true)
	List<WalletRequestRegisterTable> findByStatement(String walletID);

}
