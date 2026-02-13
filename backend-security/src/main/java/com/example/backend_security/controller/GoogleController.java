package com.example.backend_security.controller;

import com.example.backend_security.dto.GoogleResponse;
import com.example.backend_security.entity.User;
import com.example.backend_security.security.JwtUtil;
import com.example.backend_security.service.GoogleService;
import com.example.backend_security.service.TokenService;
import com.example.backend_security.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Map;

@RestController
@RequestMapping("/google")
@RequiredArgsConstructor
@Tag(name = "Authentication with GoogAutle")
public class GoogleController {


    private final GoogleService googleService;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final TokenService tokenService;


    @PostMapping("/loginWithGoogle")
    public ResponseEntity<?> loginWithGoogle(@RequestBody Map<String, String> body) throws Exception {

        String code = body.get("code");

        GoogleResponse tokenResponse = googleService.exchangeCodeForToken(code);

        Map<String, Object> userInfo = googleService.getUserInfo(tokenResponse.getAccess_token());
        String name = (String) userInfo.get("name");

        User user = userService.registerOrUpdateOAuthUser(userInfo);

        String jwt = jwtUtil.generateToken(user);

        tokenService.createToken(user.getId(), jwt);

        System.out.println(jwt);
        return ResponseEntity.ok(Map.of(
                "token", jwt,
                "email", user.getEmail(),
                "name", user.getName(),
                "picture", user.getPhotoUrl()
        ));


    }


    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    @GetMapping("/login-url")
    public ResponseEntity<Map<String, String>> getLoginUrl() {

        String scope = "openid email profile";
        String responseType = "code";

        String url = "https://accounts.google.com/o/oauth2/v2/auth"
                + "?client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&response_type=" + responseType
                + "&scope=" + scope;

        return ResponseEntity.ok(Map.of("url", url));

    }


}
