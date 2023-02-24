package com.maktabsharif.homeservices.repository;

import com.maktabsharif.homeservices.domain.Orders;
import com.maktabsharif.homeservices.domain.Services;
import com.maktabsharif.homeservices.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

    Optional<Orders> findById(Long id);
    Optional<Orders> findByUserAndServices(User user, Services services);

}
