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
	
	@Query(value = "select * from MERCHANT_MASTER_TABLE where merchant_id= ?1 and del_flg ='N'", nativeQuery = true)
	List<MerchantMaster> checkexistingcurrency(String Id);
	
	
	@Query(value = "select * from MERCHANT_MASTER_TABLE  where entity_flg ='Y' and del_flg ='N' UNION ALL select * from MERCHANT_MASTER_TABLE_MOD  where entity_flg ='N'", nativeQuery = true)
	List<MerchantMaster> ALLDATA();
}
