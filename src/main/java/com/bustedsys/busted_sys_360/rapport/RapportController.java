package com.bustedsys.busted_sys_360.rapport;

import com.bustedsys.busted_sys_360.rapport.dto.RapportPersonnelResponse;
import com.bustedsys.busted_sys_360.rapport.dto.RapportStatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rapports")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class RapportController {

    private final RapportService rapportService;

    // ================================
    // GET /api/rapports/statistiques
    // ================================
    @GetMapping("/statistiques")
    @PreAuthorize("hasRole('DIRECTEUR')")
    public ResponseEntity<RapportStatResponse> getRapportStatistique(
            @RequestParam(required = false) Long directeurId
    ) {
        return ResponseEntity.ok(
                rapportService.getRapportStatistique(directeurId)
        );
    }

    // ================================
    // GET /api/rapports/personnel
    // ================================
    @GetMapping("/personnel")
    @PreAuthorize("hasRole('DIRECTEUR')")
    public ResponseEntity<List<RapportPersonnelResponse>> getRapportPersonnel(
            @RequestParam(required = false) Long directeurId
    ) {
        return ResponseEntity.ok(
                rapportService.getRapportPersonnel(directeurId)
        );
    }
}
