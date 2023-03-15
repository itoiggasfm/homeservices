package com.maktabsharif.homeservices.repository;

import com.maktabsharif.homeservices.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findByUsername(String username);

}
