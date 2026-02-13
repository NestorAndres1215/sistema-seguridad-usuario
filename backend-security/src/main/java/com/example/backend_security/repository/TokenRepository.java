package com.example.backend_security.repository;

import com.example.backend_security.entity.Token;
import com.example.backend_security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token,Long> {

    Optional<Token> findByToken(String token);

    List<Token> findByUser(User user);

    List<Token> findByValid(String valid);

}
