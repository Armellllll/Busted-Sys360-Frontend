package com.bustedsys.busted_sys_360.rapport;

import com.bustedsys.busted_sys_360.cellule.CelluleRepository;
import com.bustedsys.busted_sys_360.detenu.DetenuRepository;
import com.bustedsys.busted_sys_360.infraction.InfractionRepository;
import com.bustedsys.busted_sys_360.infraction.NiveauGravite;
import com.bustedsys.busted_sys_360.journal.JournalAuditRepository;
import com.bustedsys.busted_sys_360.journal.JournalService;
import com.bustedsys.busted_sys_360.medical.MedicalRepository;
import com.bustedsys.busted_sys_360.medical.NiveauGraviteMedical;
import com.bustedsys.busted_sys_360.medical.UrgenceRepository;
import com.bustedsys.busted_sys_360.rapport.dto.RapportPersonnelResponse;
import com.bustedsys.busted_sys_360.rapport.dto.RapportStatResponse;
import com.bustedsys.busted_sys_360.utilisateur.Utilisateur;
import com.bustedsys.busted_sys_360.utilisateur.UtilisateurRepository;
import com.bustedsys.busted_sys_360.visite.StatutVisite;
import com.bustedsys.busted_sys_360.visite.VisiteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RapportService {

    private final DetenuRepository detenuRepository;
    private final CelluleRepository celluleRepository;
    private final InfractionRepository infractionRepository;
    private final MedicalRepository medicalRepository;
    private final UrgenceRepository urgenceRepository;
    private final VisiteRepository visiteRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final JournalAuditRepository journalRepository;
    private final JournalService journalService;

    // ================================
    // Rapport statistique global
    // ================================
    public RapportStatResponse getRapportStatistique(Long directeurId) {

        // Détenus
        long totalIncarceres = detenuRepository.countByStatut("INCARCERE");
        long totalLiberes = detenuRepository.countByStatut("LIBERE");

        // Cellules
        long totalCellules = celluleRepository.count();
        long cellulesDisponibles = celluleRepository
                .findCellulesDisponibles().size();

        float tauxOccupation = 0;
        if (totalCellules > 0) {
            tauxOccupation = ((float)(totalCellules - cellulesDisponibles)
                    / totalCellules) * 100;
        }

        // Infractions
        long totalInfractions = infractionRepository.count();
        long infractionsGraves = infractionRepository
                .countByNiveauGravite(NiveauGravite.GRAVE);
        long infractionsTresGraves = infractionRepository
                .countByNiveauGravite(NiveauGravite.TRES_GRAVE);

        // Medical
        long totalUrgences = urgenceRepository.count();
        long urgencesCritiques = urgenceRepository
                .countByGravite(NiveauGraviteMedical.CRITIQUE);
        long totalRdv = medicalRepository.count();
        long rdvEnAttente = medicalRepository.countByStatut("EN_ATTENTE");

        // Visites
        long totalVisites = visiteRepository.count();
        long visitesConfirmees = visiteRepository
                .countByStatut(StatutVisite.CONFIRMEE);
        long visitesRefusees = visiteRepository
                .countByStatut(StatutVisite.REFUSEE);

        // Journaliser
        journalService.enregistrer(
                directeurId,
                "CONSULTATION_STATS",
                "RAPPORTS",
                "Rapport statistique global consulte",
                "127.0.0.1"
        );

        return new RapportStatResponse(
                totalIncarceres,
                totalLiberes,
                tauxOccupation,
                totalCellules,
                cellulesDisponibles,
                totalInfractions,
                infractionsGraves,
                infractionsTresGraves,
                totalUrgences,
                urgencesCritiques,
                totalRdv,
                rdvEnAttente,
                totalVisites,
                visitesConfirmees,
                visitesRefusees
        );
    }

    // ================================
    // Rapport du personnel
    // ================================
    public List<RapportPersonnelResponse> getRapportPersonnel(Long directeurId) {

        List<Utilisateur> utilisateurs = utilisateurRepository.findAll();

        // Journaliser
        journalService.enregistrer(
                directeurId,
                "CONSULTATION_PERSONNEL",
                "RAPPORTS",
                "Rapport du personnel consulte",
                "127.0.0.1"
        );

        return utilisateurs.stream()
                .map(u -> new RapportPersonnelResponse(
                        u.getId(),
                        u.getLogin(),
                        u.getNom(),
                        u.getPrenom(),
                        u.getRole().name(),
                        infractionRepository.countByDetenuId(u.getId()),
                        urgenceRepository.countByGravite(NiveauGraviteMedical.CRITIQUE),
                        medicalRepository.countByStatut("EN_ATTENTE"),
                        visiteRepository.countByResponsableId(u.getId()),
                        journalRepository.countByUtilisateurId(u.getId())
                ))
                .collect(Collectors.toList());
    }
}