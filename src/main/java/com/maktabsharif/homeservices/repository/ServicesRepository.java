package com.maktabsharif.homeservices.repository;

import com.maktabsharif.homeservices.domain.Services;
import com.maktabsharif.homeservices.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServicesRepository extends JpaRepository<Services, Long> {

    Optional<Services> findByServiceTitle(String serviceTitle);

    Optional<Services> findBySubserviceTitle(String subserviceTitle);

    Optional<Services> findByServiceTitleAndAndSubserviceTitle(String serviceTitle, String subserviceTitle);

}
