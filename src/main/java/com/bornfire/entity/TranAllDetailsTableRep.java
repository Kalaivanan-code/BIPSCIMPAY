package com.bornfire.entity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;



@Repository

public interface TranAllDetailsTableRep extends JpaRepository<TransAllDetailsTable, String> {

	@Query(value = "select INSERT_TRAN_DATA(?1,?2,?3,?4,?5,?6,?7,?8,?9,?10,?11,?12,?13,?14) from dual", nativeQuery = true)
	String registerTranData(String ipsrefno,String payerAcctNumber,String payeeAcctNumber,String parkingAcctNumber,String tranDate,String tran_type,String tran_amt,String tran_curreny,String tran_partiular,String tran_remarks,String tran_purpose,String tran_status,String tran_status_error,String entry_user);

    @Modifying
	@Query(value = "update BIPS_TRANS_ALL_DETAILS SET TRAN_STATUS=?2 ,TRAN_STATUS_ERROR=?3 WHERE IPS_REF_NO=?1", nativeQuery = true)
	int updateResponseData(String wRequestID, String status, String status_error);

    
	@Query(value = "select * FROM BIPS_TRANS_ALL_DETAILS WHERE ACCT_NUM=?1", nativeQuery = true)
	List<TransAllDetailsTable> getMiniStatment(String acctNumber);


}
