package com.example.backend_security.repository;

import com.example.backend_security.entity.Token;
import com.example.backend_security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token,Long> {
    // Buscar un token por su valor
    Optional<Token> findByToken(String token);

    // Listar tokens de un usuario
    List<Token> findByUser(User user);

    // Listar tokens válidos de un usuario
    List<Token> findByUserAndValidTrue(User user);

    // Listar tokens inválidos (valid = "false")
    List<Token> findByValid(String valid);

    // Eliminar todos los tokens inválidos
    void deleteByValidFalse();
}
