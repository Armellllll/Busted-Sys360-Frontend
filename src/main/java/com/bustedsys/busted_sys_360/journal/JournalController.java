package com.bustedsys.busted_sys_360.journal;


import com.bustedsys.busted_sys_360.journal.dto.JournalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/journal")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class JournalController {

    private final JournalService journalService;

    // ================================
    // GET /api/journal
    // Tous les logs (Directeur)
    // ================================
    @GetMapping
    @PreAuthorize("hasRole('DIRECTEUR')")
    public ResponseEntity<List<JournalResponse>> consulterTous() {
        List<JournalResponse> logs = journalService.consulterTous()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(logs);
    }

    // ================================
    // GET /api/journal/utilisateur/{id}
    // Logs d'un utilisateur (Directeur)
    // ================================
    @GetMapping("/utilisateur/{id}")
    @PreAuthorize("hasRole('DIRECTEUR')")
    public ResponseEntity<List<JournalResponse>> consulterParUtilisateur(
            @PathVariable Long id
    ) {
        List<JournalResponse> logs = journalService
                .consulterParUtilisateur(id)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(logs);
    }

    // ================================
    // GET /api/journal/module/{module}
    // Logs par module (Directeur)
    // ================================
    @GetMapping("/module/{module}")
    @PreAuthorize("hasRole('DIRECTEUR')")
    public ResponseEntity<List<JournalResponse>> consulterParModule(
            @PathVariable String module
    ) {
        List<JournalResponse> logs = journalService
                .consulterParModule(module)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(logs);
    }

    // ================================
    // GET /api/journal/action/{action}
    // Logs par action (Directeur)
    // ================================
    @GetMapping("/action/{action}")
    @PreAuthorize("hasRole('DIRECTEUR')")
    public ResponseEntity<List<JournalResponse>> consulterParAction(
            @PathVariable String action
    ) {
        List<JournalResponse> logs = journalService
                .consulterParAction(action)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(logs);
    }

    // ================================
    // GET /api/journal/acces-refuses
    // Tentatives d'accès refusées (Directeur)
    // ================================
    @GetMapping("/acces-refuses")
    @PreAuthorize("hasRole('DIRECTEUR')")
    public ResponseEntity<List<JournalResponse>> consulterAccesRefuses() {
        List<JournalResponse> logs = journalService
                .consulterAccesRefuses()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(logs);
    }

    // ================================
    // GET /api/journal/periode
    // Logs par période (Directeur)
    // ================================
    @GetMapping("/periode")
    @PreAuthorize("hasRole('DIRECTEUR')")
    public ResponseEntity<List<JournalResponse>> consulterParPeriode(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime fin
    ) {
        List<JournalResponse> logs = journalService
                .consulterParPeriode(debut, fin)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(logs);
    }

    // ================================
    // Convertir JournalAudit → JournalResponse
    // ================================
    private JournalResponse toResponse(JournalAudit log) {
        return new JournalResponse(
                log.getId(),
                log.getAction(),
                log.getModule(),
                log.getDetail(),
                log.getIpAdresse(),
                log.getDateAction(),
                log.getUtilisateur() != null ? log.getUtilisateur().getLogin() : "SYSTEME",
                log.getUtilisateur() != null ? log.getUtilisateur().getNom() : "SYSTEME",
                log.getUtilisateur() != null ? log.getUtilisateur().getPrenom() : "",
                log.getUtilisateur() != null ? log.getUtilisateur().getRole().name() : ""
        );
    }
}