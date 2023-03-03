package com.maktabsharif.homeservices.repository;

import com.maktabsharif.homeservices.domain.User;
import com.maktabsharif.homeservices.domain.enumeration.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
    Optional<User> findByUsername(String username);

    List<User> findAllByUserRoleIs(UserRole userRole);

    List<User> findAllByNameContainingIgnoreCase(String name);

    List<User> findAllByFamilyNameContainingIgnoreCase(String name);

    List<User> findAllByEmailContainingIgnoreCase(String email);

    List<User> findAllByExpertPointBetween(Integer minPoint, Integer maxPoint);
}
