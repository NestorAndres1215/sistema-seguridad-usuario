package com.example.backend_security.service;

import com.example.backend_security.constants.GoogleConstants;
import com.example.backend_security.dto.GoogleResponse;
import com.example.backend_security.exception.GoogleServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class GoogleService {

    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;

    private final RestTemplate restTemplate = new RestTemplate();

    // Constructor para inyectar las propiedades
    public GoogleService(
            @Value("${spring.security.oauth2.client.registration.google.client-id}") String clientId,
            @Value("${spring.security.oauth2.client.registration.google.client-secret}") String clientSecret,
            @Value("${spring.security.oauth2.client.registration.google.redirect-uri}") String redirectUri
    ) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
    }


    // 1️⃣ Intercambiar code por token
    public GoogleResponse exchangeCodeForToken(String code) {
        String tokenUrl = "https://oauth2.googleapis.com/token";
        System.out.println("CODE = " + code);
        System.out.println("CLIENT_ID = " + clientId);
        System.out.println("REDIRECT_URI = " + redirectUri);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<GoogleResponse> response = restTemplate.postForEntity(
                tokenUrl,
                request,
                GoogleResponse.class
        );

        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new GoogleServiceException(GoogleConstants.ERROR_CODE_GOOGLE);
        }


        return response.getBody();
    }

    // 2️⃣ Obtener info del usuario usando access_token
    public Map<String, Object> getUserInfo(String accessToken) {
        String userInfoUrl = "https://openidconnect.googleapis.com/v1/userinfo";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                userInfoUrl,
                HttpMethod.GET,
                entity,
                Map.class
        );

        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new GoogleServiceException(GoogleConstants.ERROR_INFO_GOOGLE);
        }

        return response.getBody();


    }
}