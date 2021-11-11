package com.bornfire.entity;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;



@Transactional
@Repository
public interface TranCimGLRep extends JpaRepository<TranCimGLTable, String> {

	@Query(value = "select * from BIPS_TRAN_CIM_GL_TABLE  where request_uuid=?1", nativeQuery = true)
	List<TranCimGLTable> getRequestUUIDData(String requestUUID);

	@Modifying
	@Query(value = "update BIPS_TRAN_CIM_GL_TABLE set status=?2,status_code=?3,message=?4  where request_uuid=?1", nativeQuery = true)
	void updateGlResponse(String requestUUID, String status, String statusCode, String message);

	
}