package com.example.backend_security.service;

import com.example.backend_security.constants.StatusConstants;
import com.example.backend_security.entity.Token;
import com.example.backend_security.entity.User;
import com.example.backend_security.exception.ResourceNotFoundException;
import com.example.backend_security.repository.TokenRepository;
import com.example.backend_security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;



    public Token createToken(Long userId, String jwt) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Token token = Token.builder()
                .user(user)
                .token(jwt)
                .expirationDate(LocalDateTime.now().plusDays(7))
                .valid(StatusConstants.ACTIVE)
                .creationDate(LocalDateTime.now())
                .build();

        return tokenRepository.save(token);
    }


    public List<Token> getTokensByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return tokenRepository.findByUser(user);
    }

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

    @Scheduled(cron = "0 0 0 ? * SUN")
    public void deleteInactiveTokensWeekly() {
        List<Token> inactiveTokens = getInactiveTokens();
        if (!inactiveTokens.isEmpty()) {
            tokenRepository.deleteAll(inactiveTokens);
        }
    }


}
