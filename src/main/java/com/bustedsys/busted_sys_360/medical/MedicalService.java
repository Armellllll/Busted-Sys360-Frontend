package com.bustedsys.busted_sys_360.medical;

import com.bustedsys.busted_sys_360.detenu.Detenu;
import com.bustedsys.busted_sys_360.detenu.DetenuRepository;
import com.bustedsys.busted_sys_360.journal.JournalService;
import com.bustedsys.busted_sys_360.utilisateur.Agent;
import com.bustedsys.busted_sys_360.utilisateur.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalService {

    private final MedicalRepository medicalRepository;
    private final UrgenceRepository urgenceRepository;
    private final DetenuRepository detenuRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final JournalService journalService;

    // ================================
    // Demander un RDV médical
    // ================================
    @Transactional
    public RendezVousMedical demanderRdv(
            Long detenuId,
            Long agentId,
            String motif
    ) {
        // 1. Charger le détenu
        Detenu detenu = detenuRepository.findById(detenuId)
                .orElseThrow(() -> new RuntimeException("Detenu introuvable"));

        // 2. Charger l'agent
        Agent agent = (Agent) utilisateurRepository.findById(agentId)
                .orElseThrow(() -> new RuntimeException("Agent introuvable"));

        // 3. Créer le RDV
        RendezVousMedical rdv = new RendezVousMedical();
        rdv.setDetenu(detenu);
        rdv.setAgent(agent);
        rdv.setMotif(motif);
        rdv.setStatut("EN_ATTENTE");

        RendezVousMedical sauvegarde = medicalRepository.save(rdv);

        // 4. Journaliser
        journalService.enregistrer(
                agentId,
                "DEMANDE_RDV",
                "GESTION_MEDICALE",
                "RDV medical demande par agent " + agent.getNom()
                        + " pour detenu " + detenu.getNomComplet()
                        + " - Motif: " + motif,
                "127.0.0.1"
        );

        return sauvegarde;
    }

    // ================================
    // Confirmer un RDV
    // ================================
    public RendezVousMedical confirmerRdv(Long id, LocalDateTime date) {
        RendezVousMedical rdv = medicalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("RDV introuvable"));
        rdv.confirmer();
        rdv.setDate(date);

        RendezVousMedical sauvegarde = medicalRepository.save(rdv);

        // Journaliser
        journalService.enregistrer(
                null,
                "CONFIRMATION_RDV",
                "GESTION_MEDICALE",
                "RDV medical confirme pour detenu "
                        + rdv.getDetenu().getNomComplet()
                        + " - Date: " + date,
                "127.0.0.1"
        );

        return sauvegarde;
    }

    // ================================
    // Annuler un RDV
    // ================================
    public RendezVousMedical annulerRdv(Long id) {
        RendezVousMedical rdv = medicalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("RDV introuvable"));
        rdv.annuler();

        RendezVousMedical sauvegarde = medicalRepository.save(rdv);

        // Journaliser
        journalService.enregistrer(
                null,
                "ANNULATION_RDV",
                "GESTION_MEDICALE",
                "RDV medical annule pour detenu "
                        + rdv.getDetenu().getNomComplet(),
                "127.0.0.1"
        );

        return sauvegarde;
    }

    // ================================
    // Lister RDV d'un détenu
    // ================================
    public List<RendezVousMedical> listerRdvParDetenu(Long detenuId) {
        return medicalRepository.findByDetenuId(detenuId);
    }

    // ================================
    // Lister tous les RDV
    // ================================
    public List<RendezVousMedical> listerTousRdv() {
        return medicalRepository.findAll();
    }

    // ================================
    // Déclarer une urgence médicale
    // ================================
    @Transactional
    public UrgenceMedicale declarerUrgence(
            Long detenuId,
            Long agentId,
            TypeUrgence typeUrgence,
            NiveauGraviteMedical gravite,
            String description
    ) {
        // 1. Charger le détenu
        Detenu detenu = detenuRepository.findById(detenuId)
                .orElseThrow(() -> new RuntimeException("Detenu introuvable"));

        // 2. Charger l'agent
        Agent agent = (Agent) utilisateurRepository.findById(agentId)
                .orElseThrow(() -> new RuntimeException("Agent introuvable"));

        // 3. Créer l'urgence
        UrgenceMedicale urgence = new UrgenceMedicale();
        urgence.setDetenu(detenu);
        urgence.setAgent(agent);
        urgence.setTypeUrgence(typeUrgence);
        urgence.setGravite(gravite);
        urgence.setDescription(description);
        urgence.setDate(LocalDateTime.now());
        urgence.setStatut("EN_COURS");

        UrgenceMedicale sauvegarde = urgenceRepository.save(urgence);

        // 4. Journaliser
        journalService.enregistrer(
                agentId,
                "DECLARATION_URGENCE",
                "GESTION_MEDICALE",
                "Urgence " + typeUrgence.name()
                        + " declaree par agent " + agent.getNom()
                        + " pour detenu " + detenu.getNomComplet()
                        + " - Gravite: " + gravite.name(),
                "127.0.0.1"
        );

        // 5. Si critique notifier le Directeur
        if (urgence.estCritique()) {
            journalService.enregistrer(
                    null,
                    "NOTIFICATION_DIRECTEUR",
                    "GESTION_MEDICALE",
                    "URGENCE CRITIQUE - Detenu: " + detenu.getNomComplet()
                            + " - Type: " + typeUrgence.name(),
                    "127.0.0.1"
            );
            System.out.println("URGENCE CRITIQUE - Notification Directeur : "
                    + detenu.getNomComplet()
                    + " - " + typeUrgence.name());
        }

        return sauvegarde;
    }

    // ================================
    // Mettre à jour statut urgence
    // ================================
    public UrgenceMedicale mettreAJourStatutUrgence(Long id, String statut) {
        UrgenceMedicale urgence = urgenceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Urgence introuvable"));
        urgence.mettreAJourStatut(statut);

        UrgenceMedicale sauvegarde = urgenceRepository.save(urgence);

        // Journaliser
        journalService.enregistrer(
                null,
                "MAJ_STATUT_URGENCE",
                "GESTION_MEDICALE",
                "Statut urgence mis a jour : " + statut
                        + " pour detenu " + urgence.getDetenu().getNomComplet(),
                "127.0.0.1"
        );

        return sauvegarde;
    }

    // ================================
    // Lister urgences d'un détenu
    // ================================
    public List<UrgenceMedicale> listerUrgencesParDetenu(Long detenuId) {
        return urgenceRepository.findByDetenuId(detenuId);
    }

    // ================================
    // Lister toutes les urgences
    // ================================
    public List<UrgenceMedicale> listerToutesUrgences() {
        return urgenceRepository.findAll();
    }

    // ================================
    // Lister urgences critiques
    // ================================
    public List<UrgenceMedicale> listerUrgencesCritiques() {
        return urgenceRepository.findByGravite(NiveauGraviteMedical.CRITIQUE);
    }
}
