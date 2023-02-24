package com.maktabsharif.homeservices.repository;

import com.maktabsharif.homeservices.domain.Services;
import com.maktabsharif.homeservices.domain.User;
import com.maktabsharif.homeservices.domain.enumeration.UserRole;
import com.maktabsharif.homeservices.service.UserService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
//    boolean existsUserByUsername(String username);

    Optional<User> findByUsername(String username);

    List<User> findAllByUserRoleIs(UserRole userRole);

    List<User> findAllByNameContaining(String name);

    List<User> findAllByFamilyNameContaining(String name);

    List<User> findAllByEmailContaining(String email);

    List<User> findAllByExpertPointBetween(Integer minPoint, Integer maxPoint);

}
