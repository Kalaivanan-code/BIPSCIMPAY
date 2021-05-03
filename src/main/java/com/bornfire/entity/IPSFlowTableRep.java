package com.bornfire.entity;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IPSFlowTableRep extends JpaRepository<IPSFlowTable, String> {

}