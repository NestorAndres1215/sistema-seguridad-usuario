package com.example.backend_security.repository;


import com.example.backend_security.entity.Session;
import com.example.backend_security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SessionRepository extends JpaRepository<Session,Long> {
    List<Session> findByUser(User user);
}
