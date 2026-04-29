package com.bustedsys.busted_sys_360.medical;

import com.bustedsys.busted_sys_360.medical.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/medical")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class MedicalController {

    private final MedicalService medicalService;

    // ================================
    // POST /api/medical/rdv
    // Demander un RDV médical (Agent)
    // ================================
    @PostMapping("/rdv")
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<RdvResponse> demanderRdv(
            @Valid @RequestBody RdvRequest request
    ) {
        RendezVousMedical rdv = medicalService.demanderRdv(
                request.getDetenuId(),
                request.getAgentId(),
                request.getMotif()
        );
        return ResponseEntity.ok(toRdvResponse(rdv));
    }

    // ================================
    // GET /api/medical/rdv
    // Lister tous les RDV
    // ================================
    @GetMapping("/rdv")
    @PreAuthorize("hasAnyRole('AGENT', 'DIRECTEUR')")
    public ResponseEntity<List<RdvResponse>> listerRdv() {
        List<RdvResponse> rdvs = medicalService.listerTousRdv()
                .stream()
                .map(this::toRdvResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(rdvs);
    }

    // ================================
    // GET /api/medical/rdv/detenu/{detenuId}
    // RDV d'un détenu
    // ================================
    @GetMapping("/rdv/detenu/{detenuId}")
    @PreAuthorize("hasAnyRole('AGENT', 'DIRECTEUR', 'GREFFIER')")
    public ResponseEntity<List<RdvResponse>> listerRdvParDetenu(
            @PathVariable Long detenuId
    ) {
        List<RdvResponse> rdvs = medicalService.listerRdvParDetenu(detenuId)
                .stream()
                .map(this::toRdvResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(rdvs);
    }

    // ================================
    // PUT /api/medical/rdv/{id}/confirmer
    // Confirmer un RDV
    // ================================
    @PutMapping("/rdv/{id}/confirmer")
    @PreAuthorize("hasAnyRole('AGENT', 'DIRECTEUR')")
    public ResponseEntity<RdvResponse> confirmerRdv(
            @PathVariable Long id,
            @RequestParam LocalDateTime date
    ) {
        RendezVousMedical rdv = medicalService.confirmerRdv(id, date);
        return ResponseEntity.ok(toRdvResponse(rdv));
    }

    // ================================
    // PUT /api/medical/rdv/{id}/annuler
    // Annuler un RDV
    // ================================
    @PutMapping("/rdv/{id}/annuler")
    @PreAuthorize("hasAnyRole('AGENT', 'DIRECTEUR')")
    public ResponseEntity<RdvResponse> annulerRdv(@PathVariable Long id) {
        RendezVousMedical rdv = medicalService.annulerRdv(id);
        return ResponseEntity.ok(toRdvResponse(rdv));
    }

    // ================================
    // POST /api/medical/urgence
    // Déclarer une urgence (Agent)
    // ================================
    @PostMapping("/urgence")
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<UrgenceResponse> declarerUrgence(
            @Valid @RequestBody UrgenceRequest request
    ) {
        UrgenceMedicale urgence = medicalService.declarerUrgence(
                request.getDetenuId(),
                request.getAgentId(),
                request.getTypeUrgence(),
                request.getGravite(),
                request.getDescription()
        );
        return ResponseEntity.ok(toUrgenceResponse(urgence));
    }

    // ================================
    // GET /api/medical/urgence
    // Lister toutes les urgences
    // ================================
    @GetMapping("/urgence")
    @PreAuthorize("hasAnyRole('AGENT', 'DIRECTEUR')")
    public ResponseEntity<List<UrgenceResponse>> listerUrgences() {
        List<UrgenceResponse> urgences = medicalService.listerToutesUrgences()
                .stream()
                .map(this::toUrgenceResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(urgences);
    }

    // ================================
    // GET /api/medical/urgence/critiques
    // Urgences critiques (Directeur)
    // ================================
    @GetMapping("/urgence/critiques")
    @PreAuthorize("hasRole('DIRECTEUR')")
    public ResponseEntity<List<UrgenceResponse>> listerUrgencesCritiques() {
        List<UrgenceResponse> urgences = medicalService.listerUrgencesCritiques()
                .stream()
                .map(this::toUrgenceResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(urgences);
    }

    // ================================
    // GET /api/medical/urgence/detenu/{detenuId}
    // Urgences d'un détenu
    // ================================
    @GetMapping("/urgence/detenu/{detenuId}")
    @PreAuthorize("hasAnyRole('AGENT', 'DIRECTEUR', 'GREFFIER')")
    public ResponseEntity<List<UrgenceResponse>> listerUrgencesParDetenu(
            @PathVariable Long detenuId
    ) {
        List<UrgenceResponse> urgences = medicalService
                .listerUrgencesParDetenu(detenuId)
                .stream()
                .map(this::toUrgenceResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(urgences);
    }

    // ================================
    // PUT /api/medical/urgence/{id}/statut
    // Mettre à jour statut urgence
    // ================================
    @PutMapping("/urgence/{id}/statut")
    @PreAuthorize("hasAnyRole('AGENT', 'DIRECTEUR')")
    public ResponseEntity<UrgenceResponse> mettreAJourStatut(
            @PathVariable Long id,
            @RequestParam String statut
    ) {
        UrgenceMedicale urgence = medicalService.mettreAJourStatutUrgence(id, statut);
        return ResponseEntity.ok(toUrgenceResponse(urgence));
    }

    // ================================
    // Convertir RendezVousMedical → RdvResponse
    // ================================
    private RdvResponse toRdvResponse(RendezVousMedical rdv) {
        return new RdvResponse(
                rdv.getId(),
                rdv.getMotif(),
                rdv.getDate(),
                rdv.getStatut(),
                rdv.getDetenu().getNom(),
                rdv.getDetenu().getPrenom(),
                rdv.getAgent().getNom(),
                rdv.getAgent().getPrenom()
        );
    }

    // ================================
    // Convertir UrgenceMedicale → UrgenceResponse
    // ================================
    private UrgenceResponse toUrgenceResponse(UrgenceMedicale urgence) {
        return new UrgenceResponse(
                urgence.getId(),
                urgence.getTypeUrgence(),
                urgence.getGravite(),
                urgence.getDescription(),
                urgence.getDate(),
                urgence.getStatut(),
                urgence.getDetenu().getNom(),
                urgence.getDetenu().getPrenom(),
                urgence.getAgent().getNom(),
                urgence.getAgent().getPrenom(),
                urgence.estCritique()
        );
    }
}
