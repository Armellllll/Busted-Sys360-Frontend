package com.bustedsys.busted_sys_360.visite;

import com.bustedsys.busted_sys_360.visite.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/visites")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class VisiteController {

    private final VisiteService visiteService;
    private final CreneauService creneauService;

    // ================================
    // POST /api/visites/creneaux
    // Créer un créneau (Directeur)
    // ================================
    @PostMapping("/creneaux")
    @PreAuthorize("hasRole('DIRECTEUR')")
    public ResponseEntity<CreneauResponse> creerCreneau(
            @Valid @RequestBody CreneauRequest request
    ) {
        Creneau creneau = creneauService.creerCreneau(
                request.getDate(),
                request.getHeureDebut(),
                request.getHeureFin()
        );
        return ResponseEntity.ok(toCreneauResponse(creneau));
    }

    // ================================
    // GET /api/visites/creneaux
    // Lister tous les créneaux
    // ================================
    @GetMapping("/creneaux")
    @PreAuthorize("hasAnyRole('DIRECTEUR', 'RESPONSABLE_VISITE')")
    public ResponseEntity<List<CreneauResponse>> listerCreneaux() {
        List<CreneauResponse> creneaux = creneauService.listerTous()
                .stream()
                .map(this::toCreneauResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(creneaux);
    }

    // ================================
    // GET /api/visites/creneaux/disponibles
    // Créneaux disponibles
    // ================================
    @GetMapping("/creneaux/disponibles")
    @PreAuthorize("hasAnyRole('DIRECTEUR', 'RESPONSABLE_VISITE')")
    public ResponseEntity<List<CreneauResponse>> listerCreneauxDisponibles() {
        List<CreneauResponse> creneaux = creneauService.listerDisponibles()
                .stream()
                .map(this::toCreneauResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(creneaux);
    }

    // ================================
    // GET /api/visites/creneaux/date/{date}
    // Créneaux disponibles par date
    // ================================
    @GetMapping("/creneaux/date/{date}")
    @PreAuthorize("hasAnyRole('DIRECTEUR', 'RESPONSABLE_VISITE')")
    public ResponseEntity<List<CreneauResponse>> listerCreneauxParDate(
            @PathVariable LocalDate date
    ) {
        List<CreneauResponse> creneaux = creneauService
                .listerDisponiblesParDate(date)
                .stream()
                .map(this::toCreneauResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(creneaux);
    }

    // ================================
    // POST /api/visites
    // Planifier une visite (ResponsableVisite)
    // ================================
    @PostMapping
    @PreAuthorize("hasRole('RESPONSABLE_VISITE')")
    public ResponseEntity<VisiteResponse> planifier(
            @Valid @RequestBody VisiteRequest request
    ) {
        Visite visite = visiteService.planifierVisite(
                request.getDetenuId(),
                request.getResponsableId(),
                request.getCreneauId(),
                request.getTypeVisite(),
                request.getNomVisiteur(),
                request.getLienDeclare()
        );
        return ResponseEntity.ok(toVisiteResponse(visite));
    }

    // ================================
    // GET /api/visites
    // Lister toutes les visites
    // ================================
    @GetMapping
    @PreAuthorize("hasAnyRole('RESPONSABLE_VISITE', 'DIRECTEUR')")
    public ResponseEntity<List<VisiteResponse>> listerToutes() {
        List<VisiteResponse> visites = visiteService.listerToutes()
                .stream()
                .map(this::toVisiteResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(visites);
    }

    // ================================
    // GET /api/visites/{id}
    // Trouver une visite par ID
    // ================================
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('RESPONSABLE_VISITE', 'DIRECTEUR')")
    public ResponseEntity<VisiteResponse> trouverParId(
            @PathVariable Long id
    ) {
        Visite visite = visiteService.trouverParId(id);
        return ResponseEntity.ok(toVisiteResponse(visite));
    }

    // ================================
    // GET /api/visites/detenu/{detenuId}
    // Visites d'un détenu
    // ================================
    @GetMapping("/detenu/{detenuId}")
    @PreAuthorize("hasAnyRole('RESPONSABLE_VISITE', 'DIRECTEUR', 'GREFFIER')")
    public ResponseEntity<List<VisiteResponse>> listerParDetenu(
            @PathVariable Long detenuId
    ) {
        List<VisiteResponse> visites = visiteService.listerParDetenu(detenuId)
                .stream()
                .map(this::toVisiteResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(visites);
    }

    // ================================
    // GET /api/visites/responsable/{responsableId}
    // Visites d'un responsable
    // ================================
    @GetMapping("/responsable/{responsableId}")
    @PreAuthorize("hasAnyRole('RESPONSABLE_VISITE', 'DIRECTEUR')")
    public ResponseEntity<List<VisiteResponse>> listerParResponsable(
            @PathVariable Long responsableId
    ) {
        List<VisiteResponse> visites = visiteService
                .listerParResponsable(responsableId)
                .stream()
                .map(this::toVisiteResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(visites);
    }

    // ================================
    // PUT /api/visites/{id}/annuler
    // Annuler une visite
    // ================================
    @PutMapping("/{id}/annuler")
    @PreAuthorize("hasAnyRole('RESPONSABLE_VISITE', 'DIRECTEUR')")
    public ResponseEntity<VisiteResponse> annuler(@PathVariable Long id) {
        Visite visite = visiteService.annuler(id);
        return ResponseEntity.ok(toVisiteResponse(visite));
    }

    // ================================
    // PUT /api/visites/{id}/terminer
    // Terminer une visite
    // ================================
    @PutMapping("/{id}/terminer")
    @PreAuthorize("hasAnyRole('RESPONSABLE_VISITE', 'DIRECTEUR')")
    public ResponseEntity<VisiteResponse> terminer(@PathVariable Long id) {
        Visite visite = visiteService.terminer(id);
        return ResponseEntity.ok(toVisiteResponse(visite));
    }

    // ================================
    // Convertir Creneau → CreneauResponse
    // ================================
    private CreneauResponse toCreneauResponse(Creneau creneau) {
        return new CreneauResponse(
                creneau.getId(),
                creneau.getDate(),
                creneau.getHeureDebut(),
                creneau.getHeureFin(),
                creneau.estDisponible()
        );
    }

    // ================================
    // Convertir Visite → VisiteResponse
    // ================================
    private VisiteResponse toVisiteResponse(Visite visite) {
        return new VisiteResponse(
                visite.getId(),
                visite.getTypeVisite(),
                visite.getStatut(),
                visite.getNomVisiteur(),
                visite.getLienDeclare(),
                visite.getDetenu().getNom(),
                visite.getDetenu().getPrenom(),
                visite.getResponsable().getNom(),
                visite.getResponsable().getPrenom(),
                visite.getCreneau() != null ? visite.getCreneau().getDate() : null,
                visite.getCreneau() != null ? visite.getCreneau().getHeureDebut() : null,
                visite.getCreneau() != null ? visite.getCreneau().getHeureFin() : null,
                visite.estObligatoire()
        );
    }
}
