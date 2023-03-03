package com.maktabsharif.homeservices.repository;


import com.maktabsharif.homeservices.domain.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServicesRepository extends JpaRepository<Services, Long> {

    Optional<Services> findByServiceTitle(String serviceTitle);
}
