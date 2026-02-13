package com.example.backend_security.controller;

import com.example.backend_security.entity.Session;
import com.example.backend_security.service.SessionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/sessions")
@Tag(name = "Sessions")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    // =========================
    // Listar todas las sesiones
    // =========================
    @GetMapping
    public ResponseEntity<List<Session>> getAllSessions() {
        List<Session> sessions = sessionService.getAllSessions();
        return ResponseEntity.ok(sessions);
    }

    // =========================
    // Obtener sesión por ID
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<Session> getSessionById(@PathVariable Long id) {
        Optional<Session> sessionOpt = sessionService.getSessionById(id);
        return sessionOpt
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // =========================
    // Listar todas las sesiones de un usuario
    // =========================
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Session>> getSessionsByUser(@PathVariable Long userId) {
        List<Session> sessions = sessionService.getSessionsByUser(userId);
        return ResponseEntity.ok(sessions);
    }
}

