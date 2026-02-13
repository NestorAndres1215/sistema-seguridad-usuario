package com.example.backend_security.controller;

import com.example.backend_security.dto.LoginRequest;
import com.example.backend_security.entity.Token;
import com.example.backend_security.entity.User;
import com.example.backend_security.service.TokenService;
import com.example.backend_security.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthController {

    private final UserService userService;
    private final TokenService tokenService;
    private final UserService usuarioService;

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) throws Exception {
        String jwt = authHeader.replace("Bearer ", "");
        tokenService.invalidateToken(jwt);
        return ResponseEntity.ok("Logout successful. Token invalidated.");
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Token>> getTokensByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(tokenService.getTokensByUser(userId));
    }


    @PostMapping("/generate-token")
    public ResponseEntity<?> generarToken(@RequestBody LoginRequest jwtRequest) throws Exception {
        return ResponseEntity.ok(userService.login(jwtRequest));
    }

    @GetMapping("/actual-usuario")
    public ResponseEntity<User> obtenerUsuarioActual(Principal principal) {
        return ResponseEntity.ok(usuarioService.actualUsuario(principal));
    }

}

