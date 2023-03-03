package com.maktabsharif.homeservices.repository;

import com.maktabsharif.homeservices.domain.Services;
import com.maktabsharif.homeservices.domain.Subservices;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubservicesRepository extends JpaRepository<Subservices, Long> {

    Optional<Subservices> findBySubserviceTitle(String subserviceTitle);

    Optional<Subservices> findBySubserviceTitleAndServices(String subserviceTitle, Services services);
}
