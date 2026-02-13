package com.example.backend_security.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tokens")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 512, nullable = false)
    private String token;

    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate;

    @Column(length = 512, nullable = false)
    private String valid;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    public Token() {
    }

    public Token(Long id, User user, String token, LocalDateTime expirationDate, String valid, LocalDateTime creationDate) {
        this.id = id;
        this.user = user;
        this.token = token;
        this.expirationDate = expirationDate;
        this.valid = valid;
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return "Token{" +
                "id=" + id +
                ", user=" + user +
                ", token='" + token + '\'' +
                ", expirationDate=" + expirationDate +
                ", valid='" + valid + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
}
