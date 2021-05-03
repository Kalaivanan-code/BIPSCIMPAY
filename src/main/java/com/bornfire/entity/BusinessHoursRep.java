package com.bornfire.entity;



import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessHoursRep extends JpaRepository<BusinessHours, String> {
	
	
	Optional<BusinessHours> findById(String directorId);

	@Query(value = "select * from BIPS_BUS_HOURS_TABLE ", nativeQuery = true)
	List<BusinessHours> findAllCustom();

	
	@Query(value = "select * from BIPS_BUS_HOURS_TABLE where srl_no=?1", nativeQuery = true)
	BusinessHours findByIdSrlNo(String Id);
}