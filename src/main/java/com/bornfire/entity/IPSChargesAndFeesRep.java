package com.bornfire.entity;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface IPSChargesAndFeesRep extends JpaRepository<IPSChargesAndFees, String> {


}
