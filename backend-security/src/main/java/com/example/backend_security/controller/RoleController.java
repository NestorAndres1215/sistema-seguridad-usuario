package com.example.backend_security.controller;

import com.example.backend_security.entity.Role;
import com.example.backend_security.service.RoleService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/role")
@Tag(name = "Role")
@SecurityRequirement(name = "bearerAuth") // JWT requerido para todo el controller
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }


    @GetMapping("/list")
    public ResponseEntity<List<Role>> listRoles() {
        List<Role> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
        Optional<Role> role = roleService.getRoleById(id);

        return role.map(ResponseEntity::ok) // Si existe, devuelve 200 OK con el rol
                .orElseGet(() -> ResponseEntity.notFound().build()); // Si no existe, devuelve 404
    }

    @PostMapping("/create")
    public ResponseEntity<Role> createRole(@RequestBody Role role) throws Exception {
        Role newRole = roleService.createRole(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(newRole);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) throws Exception {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }

}
