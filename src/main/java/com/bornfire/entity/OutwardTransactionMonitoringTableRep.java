package com.bornfire.entity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface OutwardTransactionMonitoringTableRep extends JpaRepository<OutwardTransactionMonitoringTable, String> {

	
	
}
