package com.bornfire.entity;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionMonitorRep extends JpaRepository<TransactionMonitor, String> {

	@Query(value="select * from BIPS_TRANSACTION_MONITORING_TABLE where tran_status='REVERSE_FAILURE'",nativeQuery = true)
	List<TransactionMonitor> findAllReversalList();
	
	@Query(value="select * from BIPS_TRANSACTION_MONITORING_TABLE where master_ref_id=?1",nativeQuery = true)
	List<TransactionMonitor> findBulkCreditID(String master_ref_id);
	
	@Query(value="update BIPS_TRANSACTION_MONITORING_TABLE set  where master_ref_id=?1",nativeQuery = true)
	List<TransactionMonitor> updateBulkCreditCBSStatusError(String master_ref_id);

	@Query(value="select * from BIPS_TRANSACTION_MONITORING_TABLE where master_ref_id=?1",nativeQuery = true)
	List<TransactionMonitor> findBulkDebitID(String master_ref_id);

	@Modifying
	@Query(value="update BIPS_TRANSACTION_MONITORING_TABLE set cbs_status=?2 where sequence_unique_id=?1",nativeQuery = true)
	void updateCBSStatus(String seqUniqueID, String cbsStatus, String tranStatus);

	@Modifying
	@Query(value="update BIPS_TRANSACTION_MONITORING_TABLE set ipsx_status=?2,ipsx_response_time=?3 where sequence_unique_id=?1",nativeQuery = true)
	void updateIPSXStatus(String seqUniqueID, String ipsStatus, String ipsStatusTime);


	
	
	

	
}
