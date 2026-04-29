package com.bustedsys.busted_sys_360.cellule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CelluleService {

    private final CelluleRepository celluleRepository;

    // ================================
    // Créer une cellule
    // ================================
    public Cellule creerCellule(Cellule cellule) {
        if (celluleRepository.existsByNumero(cellule.getNumero())) {
            throw new RuntimeException("Une cellule avec ce numéro existe déjà");
        }
        return celluleRepository.save(cellule);
    }

    // ================================
    // Lister toutes les cellules
    // ================================
    public List<Cellule> listerCellules() {
        return celluleRepository.findAll();
    }

    // ================================
    // Lister les cellules disponibles
    // ================================
    public List<Cellule> listerCellulesDisponibles() {
        return celluleRepository.findCellulesDisponibles();
    }

    // ================================
    // Trouver par ID
    // ================================
    public Cellule trouverParId(Long id) {
        return celluleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cellule introuvable"));
    }

    // ================================
    // Trouver la première cellule disponible
    // ================================
    public Cellule trouverPremiereCelluleDisponible() {
        return celluleRepository.findPremiereCelluleDisponible()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "Aucune cellule disponible"
                ));
    }

    // ================================
    // Trouver par bloc
    // ================================
    public List<Cellule> trouverParBloc(String bloc) {
        return celluleRepository.findByBloc(bloc);
    }

    // ================================
    // Mettre à jour une cellule
    // ================================
    public Cellule mettreAJour(Long id, Cellule celluleMaj) {
        Cellule cellule = trouverParId(id);
        cellule.setBloc(celluleMaj.getBloc());
        cellule.setAile(celluleMaj.getAile());
        cellule.setNumero(celluleMaj.getNumero());
        cellule.setCapacite(celluleMaj.getCapacite());
        return celluleRepository.save(cellule);
    }

    // ================================
    // Supprimer une cellule
    // ================================
    public void supprimer(Long id) {
        Cellule cellule = trouverParId(id);
        if (cellule.getOccupation() > 0) {
            throw new RuntimeException(
                    "Impossible de supprimer une cellule occupée"
            );
        }
        celluleRepository.delete(cellule);
    }

    // ================================
    // Taux d'occupation global
    // ================================
    public float getTauxOccupationGlobal() {
        List<Cellule> cellules = celluleRepository.findAll();
        if (cellules.isEmpty()) return 0;

        int totalCapacite = cellules.stream()
                .mapToInt(Cellule::getCapacite)
                .sum();

        int totalOccupation = cellules.stream()
                .mapToInt(Cellule::getOccupation)
                .sum();

        if (totalCapacite == 0) return 0;
        return ((float) totalOccupation / totalCapacite) * 100;
    }
}
