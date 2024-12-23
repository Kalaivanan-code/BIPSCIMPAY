package com.bornfire.entity.Outgoing;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface BipsOutgoingGLEntryRep extends JpaRepository<BipsOutgoingGLEntryEntity, String> {
	
	 Optional<BipsOutgoingGLEntryEntity> findById( String directorId);



	@Query(value = "SELECT DOC_REF_ID,DOC_SUB_ID,TRAN_DATE,TRAN_REF_ID,ACCOUNT_NO,ACCT_NAME,BEN_BANK_CODE,TRAN_CRNCY_CODE,TRAN_AMT,IPS_REF_NO,TRAN_TYPE,TRAN_PARTICULAR,TRAN_CODE,ACCT_TYPE FROM TABLE(GETOUTGOINGFUNDTRANSFERIND(?1,?2))", nativeQuery = true)
	List<BipsOutgoingGLEntryEntity> getOutgoingCreditList(String tran_date,String ref_sub);
	
	

}