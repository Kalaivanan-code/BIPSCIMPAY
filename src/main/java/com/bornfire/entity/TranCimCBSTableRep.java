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
}
