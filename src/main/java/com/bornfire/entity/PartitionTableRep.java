package com.bornfire.entity;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Transactional
@Repository
public interface PartitionTableRep extends JpaRepository<Partition_table_entity, String> {

	
	@Query(value = "select * from BIPS_TRAN_PARITION_TABLE where TRAN_ID=?1", nativeQuery = true)
	List<Partition_table_entity> getpartitiondata(String Id);
	
	
	
}