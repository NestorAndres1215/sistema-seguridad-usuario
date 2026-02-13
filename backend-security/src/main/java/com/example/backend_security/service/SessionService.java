package com.example.backend_security.service;

import com.example.backend_security.entity.Session;
import com.example.backend_security.entity.User;
import com.example.backend_security.exception.ResourceNotFoundException;
import com.example.backend_security.repository.SessionRepository;
import com.example.backend_security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    @Autowired
    public SessionService(SessionRepository sessionRepository, UserRepository userRepository) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
    }

    // Crear sesión para un usuario
    public Session createSession(Long userId, Session session)  {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        session.setUser(user);
        return sessionRepository.save(session);
    }

    // Obtener sesión por ID
    public Optional<Session> getSessionById(Long id) {
        return sessionRepository.findById(id);
    }

    // Obtener todas las sesiones de un usuario
    public List<Session> getSessionsByUser(Long userId)  {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return sessionRepository.findByUser(user);
    }

    // Obtener todas las sesiones
    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    // Eliminar sesión
    public void deleteSession(Long id) throws Exception {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));
        sessionRepository.delete(session);
    }
}
