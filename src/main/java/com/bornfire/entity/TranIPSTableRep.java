package com.bornfire.entity;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
@Transactional

public interface TranIPSTableRep extends JpaRepository<TranIPSTable, String> {

	@Query(value = "select * from BIPS_TRAN_IPS_TABLE where msg_id=?1", nativeQuery = true)
	List<TranIPSTable> findByIdCustom(String string);

	@Query(value = "select * from BIPS_TRAN_IPS_TABLE where sequence_unique_id=?1 and msg_id=?2 and msg_sub_type=?3", nativeQuery = true)
	List<TranIPSTable> updateAckStatus(String sequenceUniqueID, String msgID, String msg_sub_type);

	@Query(value = "select * from BIPS_TRAN_IPS_TABLE where sequence_unique_id=?1 and msg_code='pacs.002.001.10' and msg_sub_type='I'", nativeQuery = true)
	List<TranIPSTable> getCustomResult(String seqUniqueID008);

	@Query(value = "select * from BIPS_TRAN_IPS_TABLE where sequence_unique_id=?1 and msg_code='pain.002.001.10' and msg_sub_type='I'", nativeQuery = true)
	List<TranIPSTable> getCustomResultPain002(String seqUniqueID);

	

	


	//@Query(value = "select * from TRANSACTION_MONITORING_TABLE where msg_type='RTP' and  trunc(tran_date)=?1 and tran_date is not null order by tran_date desc ", nativeQuery = true)
	//List<TranIPSTable> findByIdCustom(String tranDate);

}

