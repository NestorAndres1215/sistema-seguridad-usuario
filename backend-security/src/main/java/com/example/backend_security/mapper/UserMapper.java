package com.example.backend_security.mapper;

import com.example.backend_security.dto.UserResponse;
import com.example.backend_security.entity.User;
import org.springframework.stereotype.Component;

import java.util.Arrays;


@Component
public class UserMapper {


    public UserResponse toResponse(User user) {

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .email(user.getEmail())
                .provider(user.getProvider())
                .photoUrl(user.getPhotoUrl())
                .status(
                        user.getStatus() != null
                                ? user.getStatus().getCode()
                                : null
                )
                .role(
                        user.getRole() != null
                                ? user.getRole().getName()
                                : null
                )
                .lastLogin(
                        user.getLastLogin() != null
                                ? user.getLastLogin().toString()
                                : null
                )
                .creationDate(
                        user.getCreationDate() != null
                                ? user.getCreationDate().toString()
                                : null
                )
                .build();
    }

}