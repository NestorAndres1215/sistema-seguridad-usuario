package com.example.backend_security.controller;

import com.example.backend_security.entity.UserStatus;
import com.example.backend_security.service.UserStatusService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/statuses")
@Tag(name = "User Status")
public class UserStatusController {

    private final UserStatusService statusService;

    public UserStatusController(UserStatusService statusService) {
        this.statusService = statusService;
    }

    // =========================
    // Obtener todos los Status
    // =========================
    @GetMapping("/list")
    public ResponseEntity<List<UserStatus>> getAllStatuses() {
        List<UserStatus> statuses = statusService.getAllStatuses();
        return ResponseEntity.ok(statuses);
    }

    // =========================
    // Obtener Status por ID
    // =========================
    @GetMapping("/list/{id}")
    public ResponseEntity<UserStatus> getStatusById(@PathVariable Long id) {
        Optional<UserStatus> statusOpt = statusService.getStatusById(id);
        return statusOpt
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
