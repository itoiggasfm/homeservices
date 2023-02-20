package com.maktabsharif.homeservices.repository;

import com.maktabsharif.homeservices.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
//    boolean existsUserByUsername(String username);

    Optional<User> findByUsername(String username);

//    Optional<User> findByIdAndPassword(Long id, String password);
}
