package com.example.backend_security.controller;

import com.example.backend_security.dto.RegisterRequest;
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

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.createUser(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + id + " not found"));
    }


    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("User with username " + username + " not found"));
    }


    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " not found"));
    }


    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody RegisterRequest updatedUser) {
        return ResponseEntity.ok(userService.updateUser(userId, updatedUser));
    }

    @GetMapping("/status-active")
    public List<User> listActive() {
        return userService.getActiveUsers();
    }

    @GetMapping("/status-inactive")
    public List<User> listInactive() {
        return userService.getInactiveUsers();
    }

    @GetMapping("/status-suspend")
    public List<User> listSuspend() {
        return userService.getSuspendUsers();
    }

    @GetMapping("/status-blocked")
    public List<User> listBlocked() {
        return userService.getBlockedUsers();
    }

    @GetMapping("/role-user")
    public List<User> listUsersByRole() {
        return userService.getUsersByRoleUser();
    }

    @GetMapping("/role-admin")
    public List<User> listUsersByRoleAdmin() {
        return userService.getUsersByRoleAdmin();
    }

    @GetMapping("/role-user/active")
    public List<User> listActiveUsersByRoleUser() {
        return userService.getActiveUsersByRoleUser();
    }

    @GetMapping("/role-user/suspend")
    public List<User> listSuspendedUsersByRoleUser() {
        return userService.getSuspendedUsersByRoleUser();
    }

    @GetMapping("/role-user/inactive")
    public List<User> listInactiveUsersByRoleUser() {
        return userService.getInactiveUsersByRoleUser();
    }

    @GetMapping("/role-user/blocked")
    public List<User> listBlockedUsersByRoleUser() {
        return userService.getBlockedUsersByRoleUser();
    }

    @GetMapping("/role-admin/active")
    public List<User> listActiveUsersByRoleAdmin() {
        return userService.getActiveUsersByRoleAdmin();
    }

    @GetMapping("/role-admin/suspend")
    public List<User> listSuspendedUsersByRoleAdmin() {
        return userService.getSuspendedUsersByRoleAdmin();
    }

    @GetMapping("/role-admin/inactive")
    public List<User> listInactiveUsersByRoleAdmin() {
        return userService.getInactiveUsersByRoleAdmin();
    }

    @GetMapping("/role-admin/blocked")
    public List<User> listBlockedUsersByRoleAdmin() {
        return userService.getBlockedUsersByRoleAdmin();
    }

    @PutMapping("/inactive/{userId}")
    public ResponseEntity<?> inactivarUsuario(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.InactiveUser(userId));
    }

    @PutMapping("/active/{userId}")
    public ResponseEntity<?> activarUsuario(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.ActiveUser(userId));
    }

    @PutMapping("/suspend/{userId}")
    public ResponseEntity<?> suspendedUsuario(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.SuspendUser(userId));
    }

    @PutMapping("/blocked/{userId}")
    public ResponseEntity<?> blockedUsuario(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.BlockedUser(userId));
    }

    @GetMapping("/status-percentages")
    public List<UserStatusPercentageDTO> getStatusPercentages() {
        return userService.getStatusPercentages();
    }
}
