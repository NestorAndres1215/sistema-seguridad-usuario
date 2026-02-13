package com.example.backend_security.exception;

import com.example.backend_security.constants.ErrorGlobalConstants;
import jakarta.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;



@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String error, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }

    // 🔹 JWT inválido o expirado
    @ExceptionHandler(JwtAuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleJwtException(JwtAuthenticationException ex) {
        logger.warn("🔒 Error JWT: {}", ex.getMessage());
        return buildResponse(HttpStatus.UNAUTHORIZED, ErrorGlobalConstants.NO_AUTORIZADO, ex.getMessage());
    }

    // 🔹 Usuario duplicado
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleUserExists(ResourceAlreadyExistsException ex) {
        logger.warn("⚠️ Recurso duplicado: {}", ex.getMessage());
        return buildResponse(HttpStatus.CONFLICT, ErrorGlobalConstants.CONFLICTO, ex.getMessage());
    }

    // 🔹 Recurso no encontrado
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(ResourceNotFoundException ex) {
        logger.warn("🔎 Recurso no encontrado: {}", ex.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, ErrorGlobalConstants.NO_ENCONTRADO, ex.getMessage());
    }

    // 🔹 Acceso denegado
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDenied(AccessDeniedException ex) {
        logger.warn("🚫 Acceso denegado: {}", ex.getMessage());
        return buildResponse(HttpStatus.FORBIDDEN, ErrorGlobalConstants.PROHIBIDO, ex.getMessage());
    }

    // 🔹 Solicitud inválida
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(BadRequestException ex) {
        logger.warn("❗ Solicitud inválida: {}", ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, ErrorGlobalConstants.SOLICITUD_INVALIDA, ex.getMessage());
    }

    // 🔹 Error en integración con Google
    @ExceptionHandler(GoogleServiceException.class)
    public ResponseEntity<Map<String, Object>> handleGoogleServiceException(GoogleServiceException ex) {
        logger.error("🌐 Error de comunicación con Google: {}", ex.getMessage());
        return buildResponse(HttpStatus.BAD_GATEWAY, ErrorGlobalConstants.ERROR_API_GOOGLE, ex.getMessage());
    }

    // 🔹 Error en validaciones
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(ValidationException ex) {
        logger.warn("📋 Error de validación: {}", ex.getMessage());
        return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, ErrorGlobalConstants.ERROR_VALIDACION, ex.getMessage());
    }

    // 🔹 Errores internos o inesperados (Runtime)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        logger.error("💥 Error en tiempo de ejecución: ", ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ErrorGlobalConstants.ERROR_INTERNO, ex.getMessage());
    }

    // 🔹 Captura genérica (última barrera)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
        logger.error("⚠️ Error no controlado: ", ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ErrorGlobalConstants.ERROR_PROCESO, ex.getMessage());
    }
}
