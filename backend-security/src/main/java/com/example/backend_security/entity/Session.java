package com.example.backend_security.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "sessions")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 100)
    private String ip;

    @Column(columnDefinition = "TEXT")
    private String userAgent;

    private LocalDateTime start;

    private LocalDateTime end;

    @Column(length = 512, nullable = false)
    private String active;

    public Session() {
    }

    public Session(Long id, User user, String ip, String userAgent, LocalDateTime start, LocalDateTime end, String active) {
        this.id = id;
        this.user = user;
        this.ip = ip;
        this.userAgent = userAgent;
        this.start = start;
        this.end = end;
        this.active = active;
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
}