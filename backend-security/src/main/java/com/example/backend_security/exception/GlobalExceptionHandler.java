package com.example.backend_security.exception;

import com.example.backend_security.constants.ErrorGlobalConstants;
import com.example.backend_security.dto.ApiResponse;
import jakarta.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;


@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ApiResponse> buildResponse(HttpStatus status, String error, String message) {

        ApiResponse response = ApiResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(error)
                .message(message)
                .build();

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(JwtAuthenticationException.class)
    public ResponseEntity<ApiResponse> handleJwtException(JwtAuthenticationException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, ErrorGlobalConstants.NO_AUTORIZADO, ex.getMessage());
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ApiResponse> handleUserExists(ResourceAlreadyExistsException ex) {
        return buildResponse(HttpStatus.CONFLICT, ErrorGlobalConstants.CONFLICTO, ex.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ErrorGlobalConstants.NO_ENCONTRADO, ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse> handleAccessDenied(AccessDeniedException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, ErrorGlobalConstants.PROHIBIDO, ex.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse> handleBadRequest(BadRequestException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ErrorGlobalConstants.SOLICITUD_INVALIDA, ex.getMessage());
    }

    @ExceptionHandler(GoogleServiceException.class)
    public ResponseEntity<ApiResponse> handleGoogleServiceException(GoogleServiceException ex) {
        return buildResponse(HttpStatus.BAD_GATEWAY, ErrorGlobalConstants.ERROR_API_GOOGLE, ex.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse> handleValidationException(ValidationException ex) {
        return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, ErrorGlobalConstants.ERROR_VALIDACION, ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse> handleRuntimeException(RuntimeException ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ErrorGlobalConstants.ERROR_INTERNO, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGeneralException(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ErrorGlobalConstants.ERROR_PROCESO, ex.getMessage());
    }

}