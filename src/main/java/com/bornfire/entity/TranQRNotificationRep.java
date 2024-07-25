package com.bornfire.entity;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public interface TranQRNotificationRep extends JpaRepository<TranQRNotificationTable, String> {


	@Query(value = "select * from (select * from BIPS_TRAN_QR_NOTIFICATION_TABLE Where request_uuid=?1 UNION ALL select * from BIPS_TRAN_QR_NOTIFICATION_HIST_TABLE Where request_uuid=?1)", nativeQuery = true)
	Optional<TranQRNotificationTable> findByIdCustomReUUID(String requestUUID);

	
	@Modifying
	@Query(value = "update BIPS_TRAN_QR_NOTIFICATION_TABLE set status=?2,status_code=?3,message=?4,tran_no_from_cbs=?5,message_res_time=?6 Where request_uuid=?1", nativeQuery = true)
	void updateCIMcbsData(String requestUUID, String status, String statusCode, String message, String tranNoFromCBS,String dateOfRes);

}
