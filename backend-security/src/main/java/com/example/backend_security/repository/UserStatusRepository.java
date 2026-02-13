package com.example.backend_security.repository;

import com.example.backend_security.entity.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserStatusRepository extends JpaRepository<UserStatus,Long> {
    Optional<UserStatus> findByCode(String code);
}
