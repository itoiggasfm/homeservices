package com.maktabsharif.homeservices.repository;

import com.maktabsharif.homeservices.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByUsername(String username);
}
