package com.bornfire.entity;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public interface TranCimCBSTableRep extends JpaRepository<TranCimCBSTable, String> {

	////Generate Request_UUID
	@Query(value = "SELECT request_uuid.NEXTVAL FROM dual", nativeQuery = true)
	Long getRequestUUID();
	
    ////Generate Request_UUID
	@Query(value = "SELECT CBS_TRAN_NO.NEXTVAL FROM dual", nativeQuery = true)
	Long getCBSTranNo();

	@Query(value = "select NVL(sum(tran_amt),0)as Count from (select * from bips_tran_cim_cbs_table where  POST_TO_CBS='True' and tran_type='DR' and isreversal='N' and status='SUCCESS' and status_code='000' and trunc(value_date) between  (select nvl(trunc(max(acct_bal_time+1)),sysdate-1)date1 from bips_settl_accts_amt_table where payable_flg='Y') and sysdate-1 UNION ALL select * from bips_tran_cim_cbs_hist_table where  POST_TO_CBS='True' and tran_type='DR' and isreversal='N' and status='SUCCESS' and status_code='000' and trunc(value_date) between  (select nvl(trunc(max(acct_bal_time+1)),sysdate-1)date1 from bips_settl_accts_amt_table where payable_flg='Y') and sysdate-1)", nativeQuery = true)
	String payableAmt();
	
	@Query(value = "select NVL(sum(tran_amt),0)as Count from (select * from bips_tran_cim_cbs_table where  POST_TO_CBS='True' and tran_type='CR' and isreversal='Y' and status='SUCCESS' and status_code='000' and trunc(value_date) between  (select nvl(trunc(max(acct_bal_time+1)),sysdate-1)date1 from bips_settl_accts_amt_table where payable_flg='Y') and sysdate-1UNION ALL select * from bips_tran_cim_cbs_hist_table where  POST_TO_CBS='True' and tran_type='CR' and isreversal='Y' and status='SUCCESS' and status_code='000' and trunc(value_date) between  (select nvl(trunc(max(acct_bal_time+1)),sysdate-1)date1 from bips_settl_accts_amt_table where payable_flg='Y') and sysdate-1)", nativeQuery = true)
	String payableReverseAmt();
	
	@Query(value = "select NVL(sum(tran_amt),0)as Count from (select * from bips_tran_cim_cbs_table where  POST_TO_CBS='True' and tran_type='CR' and isreversal='N' and status='SUCCESS' and status_code='000' and trunc(value_date) between  (select nvl(trunc(max(acct_bal_time+1)),sysdate-1)date1 from bips_settl_accts_amt_table where payable_flg='Y') and sysdate-1 UNION ALL select * from bips_tran_cim_cbs_hist_table where  POST_TO_CBS='True' and tran_type='CR' and isreversal='N' and status='SUCCESS' and status_code='000' and trunc(value_date) between  (select nvl(trunc(max(acct_bal_time+1)),sysdate-1)date1 from bips_settl_accts_amt_table where payable_flg='Y') and sysdate-1)", nativeQuery = true)
	String receivableAmt();
	
	@Query(value = "select NVL(sum(tran_amt),0)as Count from (select * from bips_tran_cim_cbs_table where  POST_TO_CBS='True' and tran_type='DR' and isreversal='Y' and status='SUCCESS' and status_code='000' and trunc(value_date) between  (select nvl(trunc(max(acct_bal_time+1)),sysdate-1)date1 from bips_settl_accts_amt_table where payable_flg='Y') and sysdate-1 UNION ALL select * from bips_tran_cim_cbs_hist_table where  POST_TO_CBS='True' and tran_type='DR' and isreversal='Y' and status='SUCCESS' and status_code='000' and trunc(value_date) between  (select nvl(trunc(max(acct_bal_time+1)),sysdate-1)date1 from bips_settl_accts_amt_table where payable_flg='Y') and sysdate-1)", nativeQuery = true)
	String receivableReverseAmt();
	
	@Query(value = "select * from (select * from BIPS_TRAN_CIM_CBS_TABLE Where tran_no=?1 UNION ALL select * from BIPS_TRAN_CIM_CBS_HIST_TABLE Where tran_no=?1)", nativeQuery = true)
	TranCimCBSTable getTranData(String tran_no);

	@Query(value = "select * from (select * from BIPS_TRAN_CIM_CBS_TABLE Where sequence_unique_id=?1 and POST_TO_CBS='True' and tran_type='CR' and isreversal='Y' and status='SUCCESS' UNION ALL select * from BIPS_TRAN_CIM_CBS_HIST_TABLE Where sequence_unique_id=?1 and POST_TO_CBS='True' and tran_type='CR' and isreversal='Y' and status='SUCCESS')", nativeQuery = true)
	List<TranCimCBSTable> reverseDebitExist(String seqNumber);
	
	@Query(value = "select * from (select * from BIPS_TRAN_CIM_CBS_TABLE Where sequence_unique_id=?1 and POST_TO_CBS='True' and tran_type='DR' and isreversal='Y' and status='SUCCESS' UNION ALL select * from BIPS_TRAN_CIM_CBS_HIST_TABLE Where sequence_unique_id=?1 and POST_TO_CBS='True' and tran_type='DR' and isreversal='Y' and status='SUCCESS')", nativeQuery = true)
	List<TranCimCBSTable> reverseCreditExist(String seqNumber);
	
	
	@Query(value = "select * from (select * from BIPS_TRAN_CIM_CBS_TABLE Where request_uuid=?1 UNION ALL select * from BIPS_TRAN_CIM_CBS_HIST_TABLE Where request_uuid=?1)", nativeQuery = true)
	Optional<TranCimCBSTable> findByIdCustomReUUID(String requestUUID);
	
}
