package com.bornfire.entity.Outgoing;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BipsBulkOutgoingTransactionRepository extends JpaRepository<BipsBulkOutgoingTransaction, String> {

	Optional<BipsBulkOutgoingTransaction> findById(String directorId);

	@Query(value = "select * from BIPS_BULK_OUTGOING_TRAN_MASTER where doc_ref_id=?1 and doc_sub_id=?2", nativeQuery = true)
	List<BipsBulkOutgoingTransaction> findByIdDocRefID(String Id, String ref_sub_id);

	@Query(value = "select * from BIPS_BULK_OUTGOING_TRAN_MASTER order by tran_ref_id desc", nativeQuery = true)
	List<BipsBulkOutgoingTransaction> findAllCustom();

	@Query(value = "select * from BIPS_BULK_OUTGOING_TRAN_MASTER where DEL_FLAG='N' and TRAN_REF_ID=?1 ", nativeQuery = true)
	BipsBulkOutgoingTransaction findByIdCustom(String Id);
}
