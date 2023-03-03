package com.maktabsharif.homeservices.repository;

import com.maktabsharif.homeservices.domain.Orders;
import com.maktabsharif.homeservices.domain.Subservices;
import com.maktabsharif.homeservices.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {

    Optional<Orders> findById(Long id);
    Optional<Orders> findByUserAndSubservices(User user, Subservices subservices);

}
