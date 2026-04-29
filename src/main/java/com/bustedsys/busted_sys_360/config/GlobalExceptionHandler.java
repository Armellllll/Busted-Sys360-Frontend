package com.bustedsys.busted_sys_360.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ================================
    // Erreur générique
    // ================================
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(
            RuntimeException ex
    ) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // ================================
    // Erreur authentification
    // ================================
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentials(
            BadCredentialsException ex
    ) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Login ou mot de passe incorrect");
    }

    // ================================
    // Erreur accès refusé
    // ================================
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDenied(
            AccessDeniedException ex
    ) {
        return buildResponse(HttpStatus.FORBIDDEN, "Acces refuse - permissions insuffisantes");
    }

    // ================================
    // Erreur validation des champs
    // ================================
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            MethodArgumentNotValidException ex
    ) {
        Map<String, String> erreurs = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                erreurs.put(error.getField(), error.getDefaultMessage())
        );

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("message", "Erreur de validation");
        body.put("erreurs", erreurs);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // ================================
    // Erreur serveur interne
    // ================================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(
            Exception ex
    ) {
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erreur interne du serveur"
        );
    }

    // ================================
    // Construire la réponse d'erreur
    // ================================
    private ResponseEntity<Map<String, Object>> buildResponse(
            HttpStatus status,
            String message
    ) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("message", message);
        return ResponseEntity.status(status).body(body);
    }
}
