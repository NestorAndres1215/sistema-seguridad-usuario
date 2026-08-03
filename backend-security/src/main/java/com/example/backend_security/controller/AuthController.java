package com.example.backend_security.controller;

import com.example.backend_security.dto.LoginRequest;
import com.example.backend_security.dto.TokenResponse;
import com.example.backend_security.entity.Token;
import com.example.backend_security.entity.User;
import com.example.backend_security.service.AuthService;
import com.example.backend_security.service.TokenService;
import com.example.backend_security.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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
    private final UserService usuarioService;
    private final AuthService authService;

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@CookieValue(value = "jwt", required = false) String jwt, HttpServletResponse response) {
        authService.logout(jwt, response);
        return ResponseEntity.ok("Logout successful");
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Token>> getTokensByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(authService.getTokensByUser(userId));
    }

    @PostMapping("/generate-token")
    public ResponseEntity<TokenResponse> generarToken(@RequestBody LoginRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(userService.login(request, response));
    }

    @GetMapping("/actual-usuario")
    public ResponseEntity<User> obtenerUsuarioActual(Principal principal) {
        return ResponseEntity.ok(usuarioService.actualUsuario(principal));
    }

}

