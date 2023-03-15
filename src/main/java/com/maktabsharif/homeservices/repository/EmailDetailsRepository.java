package com.maktabsharif.homeservices.repository;

import com.maktabsharif.homeservices.domain.EmailDetailes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailDetailsRepository extends JpaRepository<EmailDetailes, Long> {
}
