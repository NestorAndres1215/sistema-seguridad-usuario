package com.example.backend_security.service;

import com.example.backend_security.entity.Token;
import com.example.backend_security.util.JwtCookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenService tokenService;
    private final JwtCookieUtil jwtCookieUtil;

    public List<Token> getTokensByUser(Long userId) {

        return tokenService.getTokensByUser(userId);
    }


    public void logout(String jwt, HttpServletResponse response) {

        if (jwt != null) {
            tokenService.invalidateToken(jwt);
        }

        jwtCookieUtil.deleteToken(response);
    }
}