package com.example.backend_security.security;

import com.example.backend_security.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (user.getRole() != null) {
            return Collections.singletonList(
                    new SimpleGrantedAuthority(user.getRole().getName())
            );
        }
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }


    @Override
    public String getUsername() {
  
        if (user.getUsername() != null && !user.getUsername().isEmpty()) {
            return user.getUsername();
        }
        return user.getEmail();
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        if (user.getStatus() != null) {
            return !"BLOCKED".equalsIgnoreCase(user.getStatus().getCode());
        }
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        if (user.getStatus() != null) {
            return !"BLOCKED".equalsIgnoreCase(user.getStatus().getCode());
        }
        return true;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "CustomUserDetails{" +
                "username='" + getUsername() + '\'' +
                ", role=" + (user.getRole() != null ? user.getRole().getName() : "sin rol") +
                ", status=" + (user.getStatus() != null ? user.getStatus().getCode() : "N/A") +
                '}';
    }
}