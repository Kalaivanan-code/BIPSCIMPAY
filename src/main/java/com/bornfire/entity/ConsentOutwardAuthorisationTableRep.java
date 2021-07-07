package com.bornfire.entity;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Transactional
@Repository
public interface ConsentOutwardAuthorisationTableRep extends JpaRepository<ConsentOutwardAuthorisationTable, String> {

	
}