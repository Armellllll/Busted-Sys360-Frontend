package com.bustedsys.busted_sys_360.detenu;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detenus")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class DetenuController {

    private final DetenuService detenuService;

    // ================================
    // 1. ENDPOINTS AVEC CHEMINS FIXES (SPÉCIFIQUES) D'ABORD
    // ================================

    // GET /api/detenus/search?nom=xxx
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('GREFFIER', 'AGENT', 'DIRECTEUR')")
    public ResponseEntity<List<Detenu>> rechercher(@RequestParam String nom) {
        return ResponseEntity.ok(detenuService.rechercher(nom));
    }

    // GET /api/detenus/statut/EN_DETENTION
    @GetMapping("/statut/{statut}")
    @PreAuthorize("hasAnyRole('GREFFIER', 'AGENT', 'DIRECTEUR')")
    public ResponseEntity<List<Detenu>> listerParStatut(@PathVariable String statut) {
        return ResponseEntity.ok(detenuService.listerParStatut(statut));
    }

    // GET /api/detenus/cellule/1
    @GetMapping("/cellule/{celluleId}")
    @PreAuthorize("hasAnyRole('GREFFIER', 'AGENT', 'DIRECTEUR')")
    public ResponseEntity<List<Detenu>> listerParCellule(@PathVariable Long celluleId) {
        return ResponseEntity.ok(detenuService.listerParCellule(celluleId));
    }

    // GET /api/detenus/en-detention
    @GetMapping("/en-detention")
    @PreAuthorize("hasAnyRole('GREFFIER', 'AGENT', 'DIRECTEUR')")
    public ResponseEntity<List<Detenu>> getEnDetention() {
        return ResponseEntity.ok(detenuService.listerParStatut("EN_DETENTION"));
    }

    // GET /api/detenus/compter
    @GetMapping("/compter")
    @PreAuthorize("hasAnyRole('GREFFIER', 'AGENT', 'DIRECTEUR')")
    public ResponseEntity<Long> compter() {
        return ResponseEntity.ok(detenuService.compterDetenusIncarceres());
    }

    // ================================
    // 2. ENDPOINT /liberer AJOUTÉ ICI
    // ================================

    // PUT /api/detenus/1/liberer
    @PutMapping("/{id}/liberer")
    @PreAuthorize("hasRole('DIRECTEUR')")
    public ResponseEntity<Detenu> liberer(@PathVariable Long id) {
        return ResponseEntity.ok(detenuService.libererDetenu(id));
    }

    // ================================
    // 3. ENDPOINT AVEC ID
    // ================================

    // GET /api/detenus/1
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('GREFFIER', 'AGENT', 'DIRECTEUR')")
    public ResponseEntity<Detenu> trouverParId(@PathVariable Long id) {
        return ResponseEntity.ok(detenuService.trouverParId(id));
    }

    // GET /api/detenus/1/dossier
    @GetMapping("/{id}/dossier")
    @PreAuthorize("hasAnyRole('GREFFIER', 'AGENT', 'DIRECTEUR')")
    public ResponseEntity<Detenu> consulterDossier(@PathVariable Long id) {
        return ResponseEntity.ok(detenuService.consulterDossier(id));
    }

    // ================================
    // 4. ENDPOINT DE BASE
    // ================================

    // GET /api/detenus
    @GetMapping
    @PreAuthorize("hasAnyRole('GREFFIER', 'AGENT', 'DIRECTEUR')")
    public ResponseEntity<List<Detenu>> listerTous() {
        return ResponseEntity.ok(detenuService.listerDetenus());
    }

    // ================================
    // 5. ENDPOINTS POST, PUT, DELETE
    // ================================

    // POST /api/detenus
    @PostMapping
    @PreAuthorize("hasAnyRole('GREFFIER', 'DIRECTEUR')")
    public ResponseEntity<Detenu> creer(@RequestBody Detenu detenu) {
        Detenu nouveau = detenuService.enregistrerDetenu(detenu);
        return ResponseEntity.status(HttpStatus.CREATED).body(nouveau);
    }

    // PUT /api/detenus/1
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('GREFFIER', 'DIRECTEUR')")
    public ResponseEntity<Detenu> mettreAJour(@PathVariable Long id, @RequestBody Detenu detenu) {
        return ResponseEntity.ok(detenuService.mettreAJour(id, detenu));
    }

    // DELETE /api/detenus/1
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('DIRECTEUR')")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        detenuService.libererDetenu(id);
        return ResponseEntity.noContent().build();
    }
}