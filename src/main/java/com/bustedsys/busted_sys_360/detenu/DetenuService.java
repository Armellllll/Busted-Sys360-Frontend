package com.bustedsys.busted_sys_360.detenu;

import com.bustedsys.busted_sys_360.cellule.Cellule;
import com.bustedsys.busted_sys_360.cellule.CelluleService;
import com.bustedsys.busted_sys_360.journal.JournalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DetenuService {

    private final DetenuRepository detenuRepository;
    private final CelluleService celluleService;
    private final JournalService journalService;

    @Transactional
    public Detenu enregistrerDetenu(Detenu detenu) {

        // 2. Trouver une cellule disponible
        Cellule cellule = celluleService.trouverPremiereCelluleDisponible();
        if (cellule == null) {
            throw new RuntimeException("Aucune cellule disponible");
        }

        // 3. Affecter la cellule
        detenu.setCellule(cellule);

        // 4. Incrémenter l'occupation
        cellule.incrementerOccupation();

        // 5. Sauvegarder
        Detenu sauvegarde = detenuRepository.save(detenu);

        // 6. Journaliser
        journalService.enregistrer(
                null,
                "ENREGISTREMENT_DETENU",
                "GESTION_DETENUS",
                "Detenu " + detenu.getNom() + " " + detenu.getPrenom() + " enregistre en cellule " + cellule.getNumero(),
                "127.0.0.1"
        );

        return sauvegarde;
    }

    public List<Detenu> listerDetenus() {
        return detenuRepository.findAll();
    }

    public Detenu trouverParId(Long id) {
        return detenuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Detenu introuvable avec l'id : " + id));
    }

    public List<Detenu> rechercher(String recherche) {
        return detenuRepository.findByNomContainingIgnoreCase(recherche);
    }

    public List<Detenu> listerParStatut(String statut) {
        return detenuRepository.findByStatut(statut);
    }

    public List<Detenu> listerParCellule(Long celluleId) {
        return detenuRepository.findByCelluleId(celluleId);
    }

    public List<Detenu> listerParBloc(String bloc) {
        return detenuRepository.findByCelluleBloc(bloc);
    }

    public LocalDate getDateLiberation(Long id) {
        Detenu detenu = trouverParId(id);

        journalService.enregistrer(
                null,
                "CONSULTATION_LIBERATION",
                "GESTION_DETENUS",
                "Consultation date liberation detenu : " + detenu.getNom() + " " + detenu.getPrenom(),
                "127.0.0.1"
        );

        if (detenu.getDateLiberationPrevisionnelle() == null) {
            detenu.calculerDateLiberation();
            detenuRepository.save(detenu);
        }
        return detenu.getDateLiberationPrevisionnelle();  // ← Correction 1: Ajout du return
    }

    @Transactional
    public Detenu libererDetenu(Long id) {
        Detenu detenu = trouverParId(id);

        if (detenu.getCellule() != null) {
            detenu.getCellule().decrementerOccupation();
        }

        detenu.setStatut("LIBERE");
        detenu.setDateSortie(LocalDate.now());
        detenu.setCellule(null);

        Detenu sauvegarde = detenuRepository.save(detenu);  // ← Correction 2: Déplacé avant le return

        journalService.enregistrer(
                null,
                "LIBERATION_DETENU",
                "GESTION_DETENUS",
                "Detenu " + detenu.getNom() + " " + detenu.getPrenom() + " libere",
                "127.0.0.1"
        );

        return sauvegarde;
    }

    public Detenu consulterDossier(Long id) {
        Detenu detenu = trouverParId(id);

        journalService.enregistrer(
                null,
                "CONSULTATION_DOSSIER",
                "GESTION_DETENUS",
                "Consultation dossier juridique detenu : " + detenu.getNom() + " " + detenu.getPrenom(),
                "127.0.0.1"
        );

        return detenu;
    }

    @Transactional
    public Detenu mettreAJour(Long id, Detenu detenuMaj) {
        Detenu detenu = trouverParId(id);
        detenu.setNom(detenuMaj.getNom());
        detenu.setPrenom(detenuMaj.getPrenom());
        detenu.setDateNaissance(detenuMaj.getDateNaissance());
        detenu.setSexe(detenuMaj.getSexe());
        detenu.setStatutJuridique(detenuMaj.getStatutJuridique());
        detenu.setMotif(detenuMaj.getMotif());


        Detenu sauvegarde = detenuRepository.save(detenu);

        journalService.enregistrer(
                null,
                "MODIFICATION_DETENU",
                "GESTION_DETENUS",
                "Dossier detenu " + detenu.getNom() + " " + detenu.getPrenom() + " mis a jour",
                "127.0.0.1"
        );

        return sauvegarde;
    }

    public long compterDetenusIncarceres() {
        return detenuRepository.countByStatut("EN_DETENTION");
    }
}