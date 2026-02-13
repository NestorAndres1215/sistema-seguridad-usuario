package com.example.backend_security.security;

import com.example.backend_security.constants.AuthConstants;
import com.example.backend_security.entity.User;
import com.example.backend_security.exception.ResourceNotFoundException;
import com.example.backend_security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String login)  {

        User user = usuarioRepository.findByUsername(login)
                .orElseGet(() -> usuarioRepository.findByEmail(login)
                        .orElseThrow(() -> new ResourceNotFoundException(AuthConstants.USUARIO_NO_VALIDO)));
        return new CustomUserDetails(user);
    }

}