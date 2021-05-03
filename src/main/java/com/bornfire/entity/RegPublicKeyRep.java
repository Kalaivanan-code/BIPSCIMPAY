package com.bornfire.entity;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Transactional
@Repository
public interface RegPublicKeyRep extends JpaRepository<RegPublicKey, String> {

	@Query(value = "select * from BIPS_REG_PUBLIC_KEY where acct_number=?1 and NVL(DEL_FLG,1) <> 'Y'", nativeQuery = true)
	List<RegPublicKey> getAccountNumber(String acctNumber);

	@Query(value = "select * from BIPS_REG_PUBLIC_KEY where charge_app_flg='N'", nativeQuery = true)
	List<RegPublicKey> getChargeFailedMytList();

}
