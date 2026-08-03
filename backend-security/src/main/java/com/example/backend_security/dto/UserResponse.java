package com.example.backend_security.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private Long id;

    private String name;

    private String username;

    private String email;

    private String provider;

    private String photoUrl;

    private String profilePhoto;

    private String status;

    private String role;

    private String lastLogin;

    private String creationDate;

}