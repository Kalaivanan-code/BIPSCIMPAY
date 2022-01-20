package com.bornfire.entity;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface UpdateCBSIncProceRep extends CrudRepository<UpdateCBSIncProcedure, String> {

	@Procedure(name = "UPDATECBSRESINCCREDIT", procedureName = "UPDATECBSRESINCCREDIT")
	public void updateCbsRTP(@Param("sequenceUniqueID") String sequenceUniqueId,
			@Param("instrID") String instrID,
			@Param("tranAuditNumber") String tranAuditNumber,
			@Param("cbsStatus") String cbsStatus,
			@Param("cbsStatusError") String cbsStatusError,
			@Param("cimStatus") String cimStatus,
			@Param("cimStatusCode") String cimStatusCode,
			@Param("cimMessage") String cimMessage,
			@Param("tranNoFromCbs") String tranNoFromCbs);

}
