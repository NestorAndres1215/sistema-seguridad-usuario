package com.example.backend_security.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "permissions")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false, unique = true)
    private String code;

    @Column(columnDefinition = "TEXT")
    private String description;
    
}
