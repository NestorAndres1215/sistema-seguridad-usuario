package com.example.backend_security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserStatusPercentageDTO {
    private String statusCode;
    private Long totalUsuarios;
    private Double porcentaje;
}