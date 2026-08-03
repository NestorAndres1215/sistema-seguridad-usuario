package com.example.backend_security.controller;

import com.example.backend_security.entity.UserStatus;
import com.example.backend_security.service.UserStatusService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/statuses")
@RequiredArgsConstructor
@Tag(name = "User Status")
public class UserStatusController {

    private final UserStatusService statusService;

    @GetMapping("/list")
    public ResponseEntity<List<UserStatus>> getAllStatuses() {
        return ResponseEntity.ok(statusService.getAllStatuses());
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<UserStatus> getStatusById(@PathVariable Long id) {
        return ResponseEntity.ok(statusService.getStatusById(id));
    }

}