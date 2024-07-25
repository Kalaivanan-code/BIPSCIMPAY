package com.bornfire.entity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface OutwardTransactionNotifiactionRep extends JpaRepository<RTPTranStatusResponseAPI, String> {

	
	@Query(value = "select tran_status,DECODE(IPSX_STATUS_ERROR,'',CBS_STATUS_ERROR,IPSX_STATUS_ERROR),MASTER_REF_ID,P_ID,RESV_FIELD1,DECODE(RESV_FIELD2,'',RESV_FIELD1) from BIPS_OUTWARD_TRANSACTION_MONITORING_TABLE where MASTER_REF_ID=?1 Union all select tran_status,DECODE(IPSX_STATUS_ERROR,'',CBS_STATUS_ERROR,IPSX_STATUS_ERROR),MASTER_REF_ID,P_ID,RESV_FIELD1,DECODE(RESV_FIELD2,'',RESV_FIELD1) from BIPS_OUTWARD_TRANSACTION_HIST_MONITORING_TABLE where MASTER_REF_ID=?1", nativeQuery = true)
	List<Object[]> existsByTranID(String p_id);
	
	@Query(value = "select tran_status,DECODE(IPSX_STATUS_ERROR,'',CBS_STATUS_ERROR,IPSX_STATUS_ERROR),MASTER_REF_ID,P_ID,RESV_FIELD1,DECODE(RESV_FIELD2,'',RESV_FIELD1) from BIPS_OUTWARD_TRANSACTION_MONITORING_TABLE where P_ID=?1 Union all select tran_status,DECODE(IPSX_STATUS_ERROR,'',CBS_STATUS_ERROR,IPSX_STATUS_ERROR),MASTER_REF_ID,P_ID,RESV_FIELD1,DECODE(RESV_FIELD2,'',RESV_FIELD1)  from BIPS_OUTWARD_TRANSACTION_HIST_MONITORING_TABLE where P_ID=?1", nativeQuery = true)
	List<Object[]> existsByRefID(String p_id);

	
	@Query(value = "select RESV_FIELD1 as TransactionNo,MASTER_REF_ID as TranId,tran_date as TransactionDate,req_unique_id as ReferenceId,cim_account as ToAccountNumber,tran_amount as TransactionAmount,'TEST' as ReceiptNumber,tran_status as IsSuccess,IPSX_STATUS_CODE as StatusCode ,DECODE(IPSX_STATUS_ERROR,'',CBS_STATUS_ERROR,IPSX_STATUS_ERROR) as Message,CTGY_PURP as ProductType from BIPS_OUTWARD_TRANSACTION_MONITORING_TABLE where MASTER_REF_ID=?1 Union all select RESV_FIELD1 as TransactionNo,MASTER_REF_ID as TranId,tran_date as TransactionDate,req_unique_id as ReferenceId,cim_account as ToAccountNumber,tran_amount as TransactionAmount,'TEST' as ReceiptNumber,tran_status as IsSuccess,IPSX_STATUS_CODE as StatusCode ,DECODE(IPSX_STATUS_ERROR,'',CBS_STATUS_ERROR,IPSX_STATUS_ERROR) as Message,CTGY_PURP as ProductType from BIPS_OUTWARD_TRANSACTION_HIST_MONITORING_TABLE where MASTER_REF_ID=?1", nativeQuery = true)
	List<RTPTranStatusResponseAPI> existsByTranIDNew(String p_id);
	
	@Query(value = "select RESV_FIELD1 as TransactionNo,MASTER_REF_ID as TranId,tran_date as TransactionDate,req_unique_id as ReferenceId,cim_account as ToAccountNumber,tran_amount as TransactionAmount,'TEST' as ReceiptNumber,tran_status as IsSuccess,IPSX_STATUS_CODE as StatusCode ,DECODE(IPSX_STATUS_ERROR,'',CBS_STATUS_ERROR,IPSX_STATUS_ERROR) as Message,CTGY_PURP as ProductType from BIPS_OUTWARD_TRANSACTION_MONITORING_TABLE where P_ID=?1 Union all select RESV_FIELD1 as TransactionNo,MASTER_REF_ID as TranId,tran_date as TransactionDate,req_unique_id as ReferenceId,cim_account as ToAccountNumber,tran_amount as TransactionAmount,'TEST' as ReceiptNumber,tran_status as IsSuccess,IPSX_STATUS_CODE as StatusCode ,DECODE(IPSX_STATUS_ERROR,'',CBS_STATUS_ERROR,IPSX_STATUS_ERROR) as Message,CTGY_PURP as ProductType  from BIPS_OUTWARD_TRANSACTION_HIST_MONITORING_TABLE where P_ID=?1", nativeQuery = true)
	List<RTPTranStatusResponseAPI> existsByRefIDNew(String p_id);


}
