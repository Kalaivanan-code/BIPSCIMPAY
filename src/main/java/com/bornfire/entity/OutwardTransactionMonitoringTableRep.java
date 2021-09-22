package com.bornfire.entity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface OutwardTransactionMonitoringTableRep extends JpaRepository<OutwardTransactionMonitoringTable, String> {

	@Query(value = "select * from BIPS_OUTWARD_TRANSACTION_MONITORING_TABLE where p_id=?1 Union all select * from BIPS_OUTWARD_TRANSACTION_HIST_MONITORING_TABLE where p_id=?1", nativeQuery = true)
	List<Object[]> existsByPID(String p_id);

	@Query(value = "select * from BIPS_OUTWARD_TRANSACTION_MONITORING_TABLE where sequence_unique_id=?1", nativeQuery = true)
	List<OutwardTransactionMonitoringTable> getExistData(String seqID);

	@Query(value="select * from BIPS_OUTWARD_TRANSACTION_MONITORING_TABLE where master_ref_id=?1",nativeQuery = true)
	List<OutwardTransactionMonitoringTable> findBulkCreditID(String master_ref_id);
	
	@Query(value="update BIPS_OUTWARD_TRANSACTION_MONITORING_TABLE set  where master_ref_id=?1",nativeQuery = true)
	List<OutwardTransactionMonitoringTable> updateBulkCreditCBSStatusError(String master_ref_id);

	@Query(value="select * from BIPS_OUTWARD_TRANSACTION_MONITORING_TABLE where master_ref_id=?1",nativeQuery = true)
	List<OutwardTransactionMonitoringTable> findBulkDebitID(String master_ref_id);
	
	@Query(value = "select * from BIPS_OUTWARD_TRANSACTION_MONITORING_TABLE where instr_id=?1 or end_end_id=?2", nativeQuery = true)
	List<OutwardTransactionMonitoringTable> getRTPIncomindCreditExist(String instrID,String endToEndID008);
	
}
