package com.bornfire.entity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OutwardTransHistMonitoringTableRep extends JpaRepository<OutwardTransHistMonitorTable, String> {

	@Query(value = "select * from BIPS_OUTWARD_TRANSACTION_HIST_MONITORING_TABLE where p_id=?1 Union all select * from BIPS_OUTWARD_TRANSACTION_HIST_MONITORING_TABLE where p_id=?1", nativeQuery = true)
	List<Object[]> existsByPID(String p_id);

	@Query(value = "select * from BIPS_OUTWARD_TRANSACTION_HIST_MONITORING_TABLE where sequence_unique_id=?1", nativeQuery = true)
	List<OutwardTransactionMonitoringTable> getExistData(String seqID);

	
	@Query(value="select * from BIPS_OUTWARD_TRANSACTION_HIST_MONITORING_TABLE where master_ref_id=?1",nativeQuery = true)
	List<OutwardTransHistMonitorTable> findBulkDebitID(String master_ref_id);

}
