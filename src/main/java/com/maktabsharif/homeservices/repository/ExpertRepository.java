package com.maktabsharif.homeservices.repository;

import com.maktabsharif.homeservices.domain.Expert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExpertRepository extends JpaRepository<Expert, Long> {

    Optional<Expert> findByUsername(String username);
}
