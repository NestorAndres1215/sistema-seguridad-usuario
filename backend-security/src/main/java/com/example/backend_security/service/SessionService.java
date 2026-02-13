package com.example.backend_security.service;

import com.example.backend_security.entity.Session;
import com.example.backend_security.entity.User;
import com.example.backend_security.exception.ResourceNotFoundException;
import com.example.backend_security.repository.SessionRepository;
import com.example.backend_security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    public Session createSession(Long userId, Session session)  {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        session.setUser(user);
        return sessionRepository.save(session);
    }

    public Optional<Session> getSessionById(Long id) {
        return sessionRepository.findById(id);
    }

    public List<Session> getSessionsByUser(String usuario)  {
        User user = userRepository.findByUsername(usuario)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return sessionRepository.findByUser(user);
    }

    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    public void deleteSession(Long id) throws Exception {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));
        sessionRepository.delete(session);
    }
}
