package com.example.backend_security.controller;

import com.example.backend_security.dto.RegisterRequest;
import com.example.backend_security.dto.UserStatusPercentageDTO;
import com.example.backend_security.entity.User;
import com.example.backend_security.exception.ResourceNotFoundException;
import com.example.backend_security.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "Users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // =========================
    // Listar todos los usuarios
    // =========================
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // =========================
    // Crear un usuario
    // =========================
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody RegisterRequest request) {
        System.out.println("------ NUEVA SOLICITUD: createUser ------");
        System.out.println("Request body: " + request);

        try {
            System.out.println("Intentando crear usuario...");
            User newUser = userService.createUser(request);
            System.out.println("Usuario creado correctamente: " + newUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);

        } catch (IllegalArgumentException e) {
            System.out.println("Error de validación: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error de validación: " + e.getMessage());

        } catch (Exception e) {
            System.out.println("Error inesperado al crear usuario: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ocurrió un error al crear el usuario.");
        } finally {
            System.out.println("----- FIN Solicitud createUser -----");
        }
    }


    // =========================
    // Buscar usuario por ID
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + id + " not found"));
    }

    // =========================
    // Buscar usuario por username
    // =========================
    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("User with username " + username + " not found"));
    }

    // =========================
    // Buscar usuario por email
    // =========================
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " not found"));
    }

    // =========================
    // Actualizar usuario
    // =========================
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody RegisterRequest updatedUser) {
        User user = userService.updateUser(userId, updatedUser);
        return ResponseEntity.ok(user);
    }

    // =========================
    // Listar por estados
    // =========================

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

    // =========================
    // Listar por roles
    // =========================
    @GetMapping("/role-user")
    public List<User> listUsersByRole() {
        return userService.getUsersByRoleUser();
    }

    @GetMapping("/role-admin")
    public List<User> listUsersByRoleAdmin() {
        return userService.getUsersByRoleAdmin();
    }

    // ------------------ ROLE_USER ------------------
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

    // ------------------ ROLE_ADMIN ------------------
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
        try {
            System.out.println("HOLA "+userId);
            return ResponseEntity.ok(userService.InactiveUser(userId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }
    @PutMapping("/active/{userId}")
    public ResponseEntity<?> activarUsuario(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(userService.ActiveUser(userId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @PutMapping("/suspend/{userId}")
    public ResponseEntity<?> suspendedUsuario(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(userService.SuspendUser(userId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @PutMapping("/blocked/{userId}")
    public ResponseEntity<?> blockedUsuario(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(userService.BlockedUser(userId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    // =========================
    // Porcentaje usuario
    // =========================

    @GetMapping("/status-percentages")
    public List<UserStatusPercentageDTO> getStatusPercentages() {
        return userService.getStatusPercentages();
    }
}
