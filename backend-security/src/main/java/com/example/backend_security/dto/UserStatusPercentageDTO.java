package com.example.backend_security.dto;

public class UserStatusPercentageDTO {
    private String statusCode;
    private Long totalUsuarios;
    private Double porcentaje;

    public UserStatusPercentageDTO(String statusCode, Long totalUsuarios, Double porcentaje) {
        this.statusCode = statusCode;
        this.totalUsuarios = totalUsuarios;
        this.porcentaje = porcentaje;
    }


    // Getters y setters
    public String getStatusCode() { return statusCode; }
    public void setStatusCode(String statusCode) { this.statusCode = statusCode; }

    public Long getTotalUsuarios() { return totalUsuarios; }
    public void setTotalUsuarios(Long totalUsuarios) { this.totalUsuarios = totalUsuarios; }

    public Double getPorcentaje() { return porcentaje; }
    public void setPorcentaje(Double porcentaje) { this.porcentaje = porcentaje; }
}