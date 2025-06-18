package com.bornfire.entity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantQrGenTablerep extends JpaRepository<MerchantQrGenTable, String> {

	@Query(value = "select * from BIPS_MERCHANT_QRCODE_GEN_TABLE where p_id=?1 Union all select * from BIPS_MERCHANT_QRCODE_GEN_HIST_TABLE where p_id=?1", nativeQuery = true)
	List<Object[]> existsByPID(String p_id);

	@Query(value = "select * from BIPS_MERCHANT_QRCODE_GEN_TABLE where p_id=?1 Union all select * from BIPS_MERCHANT_QRCODE_GEN_HIST_TABLE where p_id=?1", nativeQuery = true)
	List<MerchantQrGenTable> findByPId(String p_id);
	
	
	@Query(value = "select * from BIPS_MERCHANT_QRCODE_GEN_TABLE where MERCHANT_ID=?1 and REFERENCE_LABEL =?2 and ROWNUM='1' order by ENTRY_TIME desc", nativeQuery = true)
	Optional<MerchantQrGenTable> findByCustomBankName(String Merchant_id,String Reference);
	
	@Query(value = "select replace(replace(regexp_substr(?1,'/05/[^/]+/',1,1),'/05/'),'/') from dual", nativeQuery = true)
	String getrefnum(String Merchant_id);
}
