package com.bornfire.entity;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.el.stream.Optional;
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


	
	
	
	@Query(value = "select nvl(sum(tran_amount),0) from BIPS_OUTWARD_TRANSACTION_MONITORING_TABLE where ipsx_account=?1 and tran_status='SUCCESS' and  ( msg_type='OUTGOING' or msg_type='OUTWARD_BULK_RTP')", nativeQuery = true)
	String getMaxTranAmt(String acctNumber);

	
	@Query(value = "select sum(tran) from ( select nvl(sum(tran_amount),0) as tran from BIPS_OUTWARD_TRANSACTION_MONITORING_TABLE where ipsx_account=?1 and tran_status='SUCCESS' and  ( msg_type='OUTGOING' or msg_type='OUTWARD_BULK_RTP') union all select nvl(sum(tran_amount),0) as tran from BIPS_OUTWARD_TRANSACTION_hist_MONITORING_TABLE where tran_date >= sysdate-6 and  ipsx_account=?1 and tran_status='SUCCESS' and  ( msg_type='OUTGOING' or msg_type='OUTWARD_BULK_RTP'))", nativeQuery = true)
	String getMaxTranAmtweekly(String acctNumber);
	
	@Query(value = "select sum(tran) from ( select nvl(sum(tran_amount),0) as tran from BIPS_OUTWARD_TRANSACTION_MONITORING_TABLE where ipsx_account=?1 and tran_status='SUCCESS' and  ( msg_type='OUTGOING' or msg_type='OUTWARD_BULK_RTP') union all select nvl(sum(tran_amount),0) as tran from BIPS_OUTWARD_TRANSACTION_hist_MONITORING_TABLE where trim(to_char(tran_date,'Mon-YYYY'))=?2 and  ipsx_account=?1 and tran_status='SUCCESS' and  ( msg_type='OUTGOING' or msg_type='OUTWARD_BULK_RTP'))", nativeQuery = true)
	String getMaxTranAmtmonthly(String acctNumber, String trandate);

	@Query(value="select aa.cdtr_agt ben_swift_code,(select a.bank_name from bips_other_bank_agent_table a where a.bank_agent = aa.cdtr_agt) ben_bank_name," +
			"(select a.BANK_CODE from bips_other_bank_agent_table a where a.bank_agent = aa.cdtr_agt) ben_bank_code,aa.dbtr_agt rem_swift_code," +
			"(select a.bank_name from bips_other_bank_agent_table a where a.bank_agent = aa.dbtr_agt) rem_bank_name,(select a.BANK_CODE from bips_other_bank_agent_table a where a.bank_agent = aa.dbtr_agt) rem_bank_code," +
			"aa.IPSX_STATUS_CODE error_code,aa.IPSX_STATUS_ERROR error_message,bb.MASTER_REF_ID,aa.TRAN_STATUS,aa.CBS_STATUS post_to_cbs," +
			"aa.TRAN_AMOUNT transaction_amount,aa.TRAN_CURRENCY transaction_currency,aa.TRAN_AUDIT_NUMBER transaction_number,aa.ipsx_account_name customer_name," +
			"aa.ipsx_account from_account_no,aa.cim_account to_account_no,aa.MSG_TYPE tran_part_code,bb.RESV_FIELD1 init_transaction_no, bb.REQ_UNIQUE_ID init_sub_transaction_no," +
			"bb.INIT_CHANNEL_ID,cc.P_ID,cc.PSU_DEVICE_ID,cc.PSU_CHANNEL from BIPS_TRANSACTION_MONITORING_TABLE aa join (select * from BIPS_OUTWARD_TRANSACTION_HIST_MONITORING_TABLE union all select * from BIPS_OUTWARD_TRANSACTION_MONITORING_TABLE) bb " +
			"on aa.INSTR_ID = bb.CIM_MESSAGE_ID join BIPS_MERCHANT_QRCODE_GEN_TABLE cc on bb.MERCHANT_REF_LABEL = cc.REFERENCE_LABEL " +
			"where aa.INSTR_ID = ?1",nativeQuery = true)
	List<Object[]> getNotifyTranDetails(String instr_Id);
}
