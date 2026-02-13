package com.example.backend_security.service;

import com.example.backend_security.constants.StatusConstants;
import com.example.backend_security.entity.Token;
import com.example.backend_security.entity.User;
import com.example.backend_security.exception.ResourceNotFoundException;
import com.example.backend_security.repository.TokenRepository;
import com.example.backend_security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class TokenService {

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    @Autowired
    public TokenService(TokenRepository tokenRepository, UserRepository userRepository) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    // ✅ 1️⃣ Crear un token nuevo para un usuario
    public Token createToken(Long userId, String jwt) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Token token = new Token();
        token.setUser(user);
        token.setToken(jwt);
        LocalDateTime expirationDateLocal = LocalDateTime.now().plusDays(7);
        token.setExpirationDate(expirationDateLocal);
        token.setValid(StatusConstants.ACTIVE);
        token.setCreationDate(LocalDateTime.now());
        return tokenRepository.save(token);
    }


    // ✅ 3⃣ Listar todos los tokens de un usuario
    public List<Token> getTokensByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return tokenRepository.findByUser(user);
    }

    // ✅ 4️⃣ Invalidar (marcar como no válido) un token al cerrar sesión
    public void invalidateToken(String jwt) {
        Token token = tokenRepository.findByToken(jwt)
                .orElseThrow(() -> new ResourceNotFoundException("Token not found"));

        token.setValid(StatusConstants.INACTIVE);
        tokenRepository.save(token);
    }

    // Obtener tokens inactivos
    public List<Token> getInactiveTokens() {
        return tokenRepository.findByValid(StatusConstants.INACTIVE);
    }

    // 🗓 Borrar tokens inactivos cada domingo a medianoche
    @Scheduled(cron = "0 0 0 ? * SUN") // segundos, minutos, hora, día del mes, mes, día de la semana
    public void deleteInactiveTokensWeekly() {
        List<Token> inactiveTokens = getInactiveTokens();
        if (!inactiveTokens.isEmpty()) {
            tokenRepository.deleteAll(inactiveTokens);
        }
    }


}
