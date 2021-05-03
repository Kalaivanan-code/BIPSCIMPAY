package com.bornfire.entity;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;



@Transactional
@Repository
public interface SettlementAccountRep extends JpaRepository<SettlementAccount, String> {

}
