package com.bustedsys.busted_sys_360.infraction;

import com.bustedsys.busted_sys_360.infraction.dto.InfractionRequest;
import com.bustedsys.busted_sys_360.infraction.dto.InfractionResponse;
import com.bustedsys.busted_sys_360.sanction.Sanction;
import com.bustedsys.busted_sys_360.sanction.SanctionService;
import com.bustedsys.busted_sys_360.sanction.dto.SanctionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/infractions")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class InfractionController {

    private final InfractionService infractionService;
    private final SanctionService sanctionService;

    // ================================
    // POST /api/infractions
    // Déclarer une infraction (Agent)
    // ================================
    @PostMapping
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<InfractionResponse> declarer(
            @Valid @RequestBody InfractionRequest request
    ) {
        Infraction infraction = infractionService.declarerInfraction(
                request.getDetenuId(),
                request.getAgentId(),
                request.getType(),
                request.getLieu(),
                request.getDescription()
        );
        return ResponseEntity.ok(toResponse(infraction));
    }

    // ================================
    // GET /api/infractions
    // Lister toutes les infractions
    // ================================
    @GetMapping
    @PreAuthorize("hasAnyRole('AGENT', 'DIRECTEUR')")
    public ResponseEntity<List<InfractionResponse>> lister() {
        List<InfractionResponse> infractions = infractionService.listerToutes()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(infractions);
    }

    // ================================
    // GET /api/infractions/{id}
    // Trouver une infraction par ID
    // ================================
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('AGENT', 'DIRECTEUR')")
    public ResponseEntity<InfractionResponse> trouverParId(
            @PathVariable Long id
    ) {
        Infraction infraction = infractionService.trouverParId(id);
        return ResponseEntity.ok(toResponse(infraction));
    }

    // ================================
    // GET /api/infractions/detenu/{detenuId}
    // Infractions d'un détenu
    // ================================
    @GetMapping("/detenu/{detenuId}")
    @PreAuthorize("hasAnyRole('AGENT', 'DIRECTEUR', 'GREFFIER')")
    public ResponseEntity<List<InfractionResponse>> listerParDetenu(
            @PathVariable Long detenuId
    ) {
        List<InfractionResponse> infractions = infractionService
                .listerParDetenu(detenuId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(infractions);
    }

    // ================================
    // GET /api/infractions/agent/{agentId}
    // Infractions déclarées par un agent
    // ================================
    @GetMapping("/agent/{agentId}")
    @PreAuthorize("hasAnyRole('AGENT', 'DIRECTEUR')")
    public ResponseEntity<List<InfractionResponse>> listerParAgent(
            @PathVariable Long agentId
    ) {
        List<InfractionResponse> infractions = infractionService
                .listerParAgent(agentId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(infractions);
    }

    // ================================
    // GET /api/infractions/gravite/{gravite}
    // Infractions par gravité
    // ================================
    @GetMapping("/gravite/{gravite}")
    @PreAuthorize("hasAnyRole('AGENT', 'DIRECTEUR')")
    public ResponseEntity<List<InfractionResponse>> listerParGravite(
            @PathVariable NiveauGravite gravite
    ) {
        List<InfractionResponse> infractions = infractionService
                .listerParGravite(gravite)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(infractions);
    }

    // ================================
    // GET /api/infractions/periode
    // Infractions par période
    // ================================
    @GetMapping("/periode")
    @PreAuthorize("hasAnyRole('AGENT', 'DIRECTEUR')")
    public ResponseEntity<List<InfractionResponse>> listerParPeriode(
            @RequestParam LocalDate debut,
            @RequestParam LocalDate fin
    ) {
        List<InfractionResponse> infractions = infractionService
                .listerParPeriode(debut, fin)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(infractions);
    }

    // ================================
    // GET /api/infractions/sanctions
    // Toutes les sanctions
    // ================================
    @GetMapping("/sanctions")
    @PreAuthorize("hasAnyRole('AGENT', 'DIRECTEUR')")
    public ResponseEntity<List<SanctionResponse>> listerSanctions() {
        List<SanctionResponse> sanctions = sanctionService.listerToutes()
                .stream()
                .map(this::toSanctionResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(sanctions);
    }

    // ================================
    // PUT /api/infractions/sanctions/{id}/annuler
    // Annuler une sanction (Directeur)
    // ================================
    @PutMapping("/sanctions/{id}/annuler")
    @PreAuthorize("hasRole('DIRECTEUR')")
    public ResponseEntity<SanctionResponse> annulerSanction(
            @PathVariable Long id
    ) {
        Sanction sanction = sanctionService.annuler(id);
        return ResponseEntity.ok(toSanctionResponse(sanction));
    }

    // ================================
    // Convertir Infraction → InfractionResponse
    // ================================
    private InfractionResponse toResponse(Infraction infraction) {
        return new InfractionResponse(
                infraction.getId(),
                infraction.getType(),
                infraction.getLieu(),
                infraction.getHeure(),
                infraction.getDate(),
                infraction.getNiveauGravite(),
                infraction.getDescription(),
                infraction.getDetenu().getNom(),
                infraction.getDetenu().getPrenom(),
                infraction.getAgent().getNom(),
                infraction.getAgent().getPrenom(),
                infraction.getSanction() != null ? infraction.getSanction().getType().name() : null,
                infraction.getSanction() != null ? infraction.getSanction().getDuree() : 0,
                infraction.getSanction() != null ? infraction.getSanction().getStatut() : null
        );
    }

    // ================================
    // Convertir Sanction → SanctionResponse
    // ================================
    private SanctionResponse toSanctionResponse(Sanction sanction) {
        return new SanctionResponse(
                sanction.getId(),
                sanction.getType(),
                sanction.getDescription(),
                sanction.getDateAttribution(),
                sanction.getDateFin(),
                sanction.getDuree(),
                sanction.getStatut(),
                sanction.getDetenu().getNom(),
                sanction.getDetenu().getPrenom()
        );
    }
}
