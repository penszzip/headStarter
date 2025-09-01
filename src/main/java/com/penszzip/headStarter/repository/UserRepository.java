package com.penszzip.headStarter.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.penszzip.headStarter.model.User;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
