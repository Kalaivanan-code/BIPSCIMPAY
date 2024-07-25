package com.bornfire.entity;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;




@Transactional
@Repository
public interface Feestranpartion extends JpaRepository<Feestranparition, String> {
	
	
	@Query(value="select sum(tran_amt_loc) from bips_tran_fees_parition_table where part_tran_type='D' and trunc(TRAN_DATE) =?1",nativeQuery = true)
	String sumofdebit(String id);
	
	@Query(value="select sum(tran_amt_loc) from bips_tran_fees_parition_table where (part_tran_type='C' or part_tran_type='QR') and  trunc(TRAN_DATE) =?1",nativeQuery = true)
	String sumofcredit(String id);
	
	@Query(value="select count(*) from bips_tran_fees_parition_table where trunc(TRAN_DATE) =?1",nativeQuery = true)
	int countvalues(String id);
	
/*	@Query(value="select merchant_acct_no,Tran_amt_loc,Partition_detail,Tran_date,Ipsx_account_number,Part_tran_type,Tran_ref_cur,Value_date,Ipsx_acct_name from bips_tran_fees_parition_table",nativeQuery = true)
	List<Object[]> feestran();

	*/
	
	@Query(value="SELECT merchant_acct_no,Tran_amt_loc,Partition_detail,Tran_date,Ipsx_account_number,Part_tran_type,Tran_ref_cur,Value_date,Ipsx_acct_name\r\n" + 
	",tran_id,part_tran_id,PARTICIPANT_BANK,srl_num,business_date,posting\r\n" + 
	"FROM bips_tran_fees_parition_table where  trunc(TRAN_DATE) =?1 and TRAN_AMT_LOC <>0 \r\n" + 
	"ORDER BY tran_id,part_tran_id,\r\n" + 
	"merchant_acct_no,Tran_amt_loc,Partition_detail,Tran_date,Ipsx_account_number,Part_tran_type,Tran_ref_cur,Value_date,Ipsx_acct_name,PARTICIPANT_BANK\r\n" + 
	"",nativeQuery = true)
	List<Object[]> feestran(String id);
}
