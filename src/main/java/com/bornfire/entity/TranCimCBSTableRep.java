package com.bornfire.entity;

import java.util.Date;
import java.util.List;

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

	@Query(value = "select NVL(sum(tran_amt),0)as Count from bips_tran_cim_cbs_table where  POST_TO_CBS='True' and tran_type='DR' and isreversal='N' and status='SUCCESS' and status_code='000' and trunc(message_date_time) between  (select nvl(trunc(max(acct_bal_time+1)),sysdate-1)date1 from bips_settl_accts_amt_table where payable_flg='Y') and sysdate-1", nativeQuery = true)
	String payableAmt();
	
	@Query(value = "select NVL(sum(tran_amt),0)as Count from bips_tran_cim_cbs_table where  POST_TO_CBS='True' and tran_type='CR' and isreversal='R' and status='SUCCESS' and status_code='000' and trunc(message_date_time) between  (select nvl(trunc(max(acct_bal_time+1)),sysdate-1)date1 from bips_settl_accts_amt_table where payable_flg='Y') and sysdate-1", nativeQuery = true)
	String payableReverseAmt();
	
	@Query(value = "select NVL(sum(tran_amt),0)as Count from bips_tran_cim_cbs_table where  POST_TO_CBS='True' and tran_type='CR' and isreversal='N' and status='SUCCESS' and status_code='000' and trunc(message_date_time) between  (select nvl(trunc(max(acct_bal_time+1)),sysdate-1)date1 from bips_settl_accts_amt_table where payable_flg='Y') and sysdate-1", nativeQuery = true)
	String receivableAmt();
	
	@Query(value = "select NVL(sum(tran_amt),0)as Count from bips_tran_cim_cbs_table where  POST_TO_CBS='True' and tran_type='DR' and isreversal='R' and status='SUCCESS' and status_code='000' and trunc(message_date_time) between  (select nvl(trunc(max(acct_bal_time+1)),sysdate-1)date1 from bips_settl_accts_amt_table where payable_flg='Y') and sysdate-1", nativeQuery = true)
	String receivableReverseAmt();
	
	
}
