package com.bornfire.entity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface MerchantMasterRep extends JpaRepository<MerchantMaster, String> {
Optional<MerchantMaster> findById(String directorId);
	
	@Query(value = "select * from MERCHANT_MASTER_TABLE ", nativeQuery = true)
	List<MerchantMaster> findAllCustom();
	
	@Query(value = "select * from MERCHANT_MASTER_TABLE where del_flg='N' ", nativeQuery = true)
	List<MerchantMaster> findAllData();

	@Query(value = "select * from MERCHANT_MASTER_TABLE where merchant_id= ?1 and del_flg ='N'", nativeQuery = true)
	MerchantMaster findByIdCustom(String Id);
	
	@Query(value = "select * from MERCHANT_MASTER_TABLE where merchant_id= ?1 ", nativeQuery = true)
	List<MerchantMaster> checkexistingcurrency(String Id);
	
	
	@Query(value = "select * from MERCHANT_MASTER_TABLE  where entity_flg ='Y' and del_flg ='N' UNION ALL select * from MERCHANT_MASTER_TABLE_MOD  where entity_flg ='N'", nativeQuery = true)
	List<MerchantMaster> ALLDATA();
	
	@Query(value = "select fees from BIPS_MERCHANT_CHARGES_AND_FEES_TABLE where DESCRIPTION in (select MERCHANT_FEES from MERCHANT_MASTER_TABLE where MERCHANT_ID=?1)", nativeQuery = true)
	String getMerchantfees(String Id);

}
