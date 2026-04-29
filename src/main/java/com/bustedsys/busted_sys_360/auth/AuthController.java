package com.bustedsys.busted_sys_360.auth;

import com.bustedsys.busted_sys_360.auth.dto.LoginRequest;
import com.bustedsys.busted_sys_360.auth.dto.LoginResponse;
import com.bustedsys.busted_sys_360.auth.dto.RegisterRequest;  // ← AJOUTER CET IMPORT
import com.bustedsys.busted_sys_360.utilisateur.Utilisateur;   // ← AJOUTER CET IMPORT
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;                        // ← AJOUTER CET IMPORT
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;                                            // ← AJOUTER CET IMPORT

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest
    ) {
        String ip = httpRequest.getRemoteAddr();
        LoginResponse response = authService.connecter(request, ip);
        return ResponseEntity.ok(response);
    }

    // ================================
    // AJOUTER CETTE MÉTHODE
    // ================================
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            Utilisateur utilisateur = authService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(utilisateur);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<String> me() {
        return ResponseEntity.ok("Connecte avec succes");
    }
}