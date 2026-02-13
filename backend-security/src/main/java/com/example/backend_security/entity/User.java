package com.example.backend_security.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 255, nullable = false)
    private String username;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column(length = 255, nullable = false)
    private String password;

    @Column(length = 50, nullable = false)
    private String provider;

    @Column(length = 255)
    private String photoUrl;

    @Lob
    private byte[] profilePhoto;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private UserStatus status;

    @ManyToOne
    @JoinColumn(name = "role_id")

    private Role role;

    private LocalDateTime lastLogin;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @OneToMany(mappedBy = "user")
    @JsonIgnore  // Ignorar la relación para evitar ciclo
    private Set<Token> tokens = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnore  // Ignorar la relación para evitar ciclo
    private Set<Session> sessions = new HashSet<>();

    public User() {}

    public User(Long id, String name, String username, String email, String password, String provider, String photoUrl, byte[] profilePhoto, LocalDateTime lastLogin, LocalDateTime creationDate) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.provider = provider;
        this.photoUrl = photoUrl;
        this.profilePhoto = profilePhoto;
        this.lastLogin = lastLogin;
        this.creationDate = creationDate;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }

    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }

    public byte[] getProfilePhoto() { return profilePhoto; }
    public void setProfilePhoto(byte[] profilePhoto) { this.profilePhoto = profilePhoto; }

    public UserStatus getStatus() { return status; }
    public void setStatus(UserStatus status) { this.status = status; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }

    public LocalDateTime getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate; }

    public Set<Token> getTokens() { return tokens; }
    public void setTokens(Set<Token> tokens) { this.tokens = tokens; }

    public Set<Session> getSessions() { return sessions; }
    public void setSessions(Set<Session> sessions) { this.sessions = sessions; }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", provider='" + provider + '\'' +
                ", photoUrl='" + photoUrl + '\'' +

                ", status=" + status +
                ", role=" + role +
                ", lastLogin=" + lastLogin +
                ", creationDate=" + creationDate +
                '}';
    }
}