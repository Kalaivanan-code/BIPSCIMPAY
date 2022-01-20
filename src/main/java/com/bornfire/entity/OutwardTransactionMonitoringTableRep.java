package com.bornfire.entity;

import java.util.List;
import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional

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

	@Modifying
	@Query(value="update BIPS_OUTWARD_TRANSACTION_MONITORING_TABLE set cbs_status=?2,tran_audit_number=?3 where sequence_unique_id=?1",nativeQuery = true)
	void updateCbsData(String endToEndID008, String cbsStatus,String auditNumber);

	@Modifying
	@Query(value="update BIPS_OUTWARD_TRANSACTION_MONITORING_TABLE set tran_audit_number=?2 where sequence_unique_id=?1",nativeQuery = true)
	void updateAuditTranID(String endToEndID008, String sysTraceNumber008);

	@Modifying
	@Query(value="update BIPS_OUTWARD_TRANSACTION_MONITORING_TABLE set cim_cnf_request_uid=?2,cim_cnf_status=?3,cim_cnf_status_error=?4 where sequence_unique_id=?1",nativeQuery = true)
	void updateCIMCNFData(String seqUniqueID, String requestUUID, String status, String statusError);

	@Modifying
	@Query(value="update BIPS_OUTWARD_TRANSACTION_MONITORING_TABLE set ipsx_status=?2,ipsx_response_time=?3 where sequence_unique_id=?1",nativeQuery = true)
	void updateIPSXStatusBulkRTP(String seqUniqueID, String ipsStatus, String ipsStatusDate);

	@Modifying
	@Query(value="update BIPS_OUTWARD_TRANSACTION_MONITORING_TABLE set tran_audit_number=?1,Cbs_status=?3,cbs_status_error=?4,cbs_response_time=?5 where sequence_unique_id=?2",nativeQuery = true)
	void updateCBSStatusRTPError(String sysTraceNumber008, String endToEndID008, String cbsStatus,
			String error_desc,String cbsResTime);
	
}
