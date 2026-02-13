package com.example.backend_security.controller;

import com.example.backend_security.dto.LoginRequest;
import com.example.backend_security.entity.Token;
import com.example.backend_security.service.TokenService;
import com.example.backend_security.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/auth") // Ruta base común
@Tag(name = "Authentication")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;



    @Autowired
    private UserService usuarioService;

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) throws Exception {
        String jwt = authHeader.replace("Bearer ", "");
        tokenService.invalidateToken(jwt);
        return ResponseEntity.ok("Logout successful. Token invalidated.");
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Token>> getTokensByUser(@PathVariable Long userId) {
        try {
            List<Token> tokens = tokenService.getTokensByUser(userId);
            return ResponseEntity.ok(tokens);
        } catch (Exception e) {
            // Retorna 404 si el usuario no existe
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.emptyList());
        }
    }


    @PostMapping("/generate-token")
    public ResponseEntity<?> generarToken(@RequestBody LoginRequest jwtRequest) throws Exception {
        return ResponseEntity.ok(userService.login(jwtRequest));
    }

    @GetMapping("/actual-usuario")
    public ResponseEntity<?> obtenerUsuarioActual(Principal principal) {
        try {
            System.out.println(principal);
            return ResponseEntity.ok(usuarioService.actualUsuario(principal));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("ERROR DE USUARIO ACTUAL");
        }
    }

}

