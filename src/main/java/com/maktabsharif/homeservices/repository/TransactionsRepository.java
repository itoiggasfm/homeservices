package com.maktabsharif.homeservices.repository;

import com.maktabsharif.homeservices.domain.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionsRepository extends JpaRepository<Transactions, Long> {

}
