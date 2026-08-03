package com.example.backend_security.controller;

import com.example.backend_security.dto.RegisterRequest;
import com.example.backend_security.dto.UserResponse;
import com.example.backend_security.dto.UserStatusPercentageDTO;
import com.example.backend_security.entity.User;
import com.example.backend_security.exception.ResourceNotFoundException;
import com.example.backend_security.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.createUser(request));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody RegisterRequest updatedUser) {
        return ResponseEntity.ok(userService.updateUser(userId, updatedUser));
    }

    @GetMapping("/status-active")
    public ResponseEntity<List<UserResponse>> listActive() {
        return ResponseEntity.ok(userService.getActiveUsers());
    }

    @GetMapping("/status-inactive")
    public ResponseEntity<List<UserResponse>> listInactive() {
        return ResponseEntity.ok(userService.getInactiveUsers());
    }

    @GetMapping("/status-suspend")
    public ResponseEntity<List<UserResponse>> listSuspend() {
        return ResponseEntity.ok(userService.getSuspendUsers());
    }

    @GetMapping("/status-blocked")
    public ResponseEntity<List<UserResponse>> listBlocked() {
        return ResponseEntity.ok(userService.getBlockedUsers());
    }

    @GetMapping("/role-user")
    public List<UserResponse> listUsersByRole() {
        return userService.getUsersByRoleUser();
    }

    @GetMapping("/role-admin")
    public List<UserResponse> listUsersByRoleAdmin() {
        return userService.getUsersByRoleAdmin();
    }

    @GetMapping("/role-user/active")
    public List<UserResponse> listActiveUsersByRoleUser() {
        return userService.getActiveUsersByRoleUser();
    }

    @GetMapping("/role-user/suspend")
    public List<UserResponse> listSuspendedUsersByRoleUser() {
        return userService.getSuspendedUsersByRoleUser();
    }

    @GetMapping("/role-user/inactive")
    public List<UserResponse> listInactiveUsersByRoleUser() {
        return userService.getInactiveUsersByRoleUser();
    }

    @GetMapping("/role-user/blocked")
    public List<UserResponse> listBlockedUsersByRoleUser() {
        return userService.getBlockedUsersByRoleUser();
    }

    @GetMapping("/role-admin/active")
    public List<UserResponse> listActiveUsersByRoleAdmin() {
        return userService.getActiveUsersByRoleAdmin();
    }

    @GetMapping("/role-admin/suspend")
    public List<UserResponse> listSuspendedUsersByRoleAdmin() {
        return userService.getSuspendedUsersByRoleAdmin();
    }

    @GetMapping("/role-admin/inactive")
    public List<UserResponse> listInactiveUsersByRoleAdmin() {
        return userService.getInactiveUsersByRoleAdmin();
    }

    @GetMapping("/role-admin/blocked")
    public List<UserResponse> listBlockedUsersByRoleAdmin() {
        return userService.getBlockedUsersByRoleAdmin();
    }

    @PutMapping("/inactive/{userId}")
    public ResponseEntity<UserResponse> inactiveUser(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.inactiveUser(userId));
    }

    @PutMapping("/active/{userId}")
    public ResponseEntity<UserResponse> activeUser(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.activeUser(userId));
    }

    @PutMapping("/suspend/{userId}")
    public ResponseEntity<UserResponse> suspendUser(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.suspendUser(userId));
    }

    @PutMapping("/blocked/{userId}")
    public ResponseEntity<UserResponse> blockedUser(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.blockedUser(userId));
    }

    @GetMapping("/status-percentages")
    public List<UserStatusPercentageDTO> getStatusPercentages() {
        return userService.getStatusPercentages();
    }
}
