package com.bornfire.entity;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;




@Transactional
@Repository
public interface MerchantTransactionRep extends JpaRepository<Merchant_transaction_entity, String> {

	
}