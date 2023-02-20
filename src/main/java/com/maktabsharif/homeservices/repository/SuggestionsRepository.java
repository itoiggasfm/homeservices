package com.maktabsharif.homeservices.repository;

import com.maktabsharif.homeservices.domain.Orders;
import com.maktabsharif.homeservices.domain.Suggestions;
import com.maktabsharif.homeservices.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SuggestionsRepository extends JpaRepository<Suggestions, Long> {

    Optional<Suggestions> findByUserAndOrders(User user, Orders orders);

}
