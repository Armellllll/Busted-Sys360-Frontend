package com.bustedsys.busted_sys_360.cellule;


import com.bustedsys.busted_sys_360.detenu.dto.CelluleRequest;
import com.bustedsys.busted_sys_360.detenu.dto.CelluleResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cellules")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class CelluleController {

    private final CelluleService celluleService;

    // ================================
    // POST /api/cellules
    // Créer une cellule (Directeur)
    // ================================
    @PostMapping
    @PreAuthorize("hasRole('DIRECTEUR')")
    public ResponseEntity<CelluleResponse> creer(
            @Valid @RequestBody CelluleRequest request
    ) {
        Cellule cellule = new Cellule();
        cellule.setBloc(request.getBloc());
        cellule.setAile(request.getAile());
        cellule.setNumero(request.getNumero());
        cellule.setCapacite(request.getCapacite());

        Cellule sauvegarde = celluleService.creerCellule(cellule);
        return ResponseEntity.ok(toResponse(sauvegarde));
    }

    // ================================
    // GET /api/cellules
    // Lister toutes les cellules
    // ================================
    @GetMapping
    @PreAuthorize("hasAnyRole('DIRECTEUR', 'GREFFIER', 'AGENT')")
    public ResponseEntity<List<CelluleResponse>> lister() {
        List<CelluleResponse> cellules = celluleService.listerCellules()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(cellules);
    }

    // ================================
    // GET /api/cellules/disponibles
    // Lister les cellules disponibles
    // ================================
    @GetMapping("/disponibles")
    @PreAuthorize("hasAnyRole('DIRECTEUR', 'GREFFIER')")
    public ResponseEntity<List<CelluleResponse>> listerDisponibles() {
        List<CelluleResponse> cellules = celluleService.listerCellulesDisponibles()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(cellules);
    }

    // ================================
    // GET /api/cellules/{id}
    // Trouver une cellule par ID
    // ================================
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('DIRECTEUR', 'GREFFIER', 'AGENT')")
    public ResponseEntity<CelluleResponse> trouverParId(
            @PathVariable Long id
    ) {
        Cellule cellule = celluleService.trouverParId(id);
        return ResponseEntity.ok(toResponse(cellule));
    }

    // ================================
    // GET /api/cellules/bloc/{bloc}
    // Trouver les cellules d'un bloc
    // ================================
    @GetMapping("/bloc/{bloc}")
    @PreAuthorize("hasAnyRole('DIRECTEUR', 'GREFFIER', 'AGENT')")
    public ResponseEntity<List<CelluleResponse>> trouverParBloc(
            @PathVariable String bloc
    ) {
        List<CelluleResponse> cellules = celluleService.trouverParBloc(bloc)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(cellules);
    }

    // ================================
    // PUT /api/cellules/{id}
    // Mettre à jour une cellule
    // ================================
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DIRECTEUR')")
    public ResponseEntity<CelluleResponse> mettreAJour(
            @PathVariable Long id,
            @Valid @RequestBody CelluleRequest request
    ) {
        Cellule celluleMaj = new Cellule();
        celluleMaj.setBloc(request.getBloc());
        celluleMaj.setAile(request.getAile());
        celluleMaj.setNumero(request.getNumero());
        celluleMaj.setCapacite(request.getCapacite());

        Cellule cellule = celluleService.mettreAJour(id, celluleMaj);
        return ResponseEntity.ok(toResponse(cellule));
    }

    // ================================
    // DELETE /api/cellules/{id}
    // Supprimer une cellule
    // ================================
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('DIRECTEUR')")
    public ResponseEntity<String> supprimer(@PathVariable Long id) {
        celluleService.supprimer(id);
        return ResponseEntity.ok("Cellule supprimée avec succès");
    }

    // ================================
    // GET /api/cellules/taux-occupation
    // Taux d'occupation global
    // ================================
    @GetMapping("/taux-occupation")
    @PreAuthorize("hasRole('DIRECTEUR')")
    public ResponseEntity<Float> getTauxOccupationGlobal() {
        return ResponseEntity.ok(celluleService.getTauxOccupationGlobal());
    }

    // ================================
    // Convertir Cellule → CelluleResponse
    // ================================
    private CelluleResponse toResponse(Cellule cellule) {
        return new CelluleResponse(
                cellule.getId(),
                cellule.getBloc(),
                cellule.getAile(),
                cellule.getNumero(),
                cellule.getCapacite(),
                cellule.getOccupation(),
                cellule.getTauxOccupation(),
                cellule.estDisponible()
        );
    }
}
