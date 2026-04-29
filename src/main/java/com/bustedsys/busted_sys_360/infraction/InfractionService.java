package com.bustedsys.busted_sys_360.infraction;

import com.bustedsys.busted_sys_360.detenu.Detenu;
import com.bustedsys.busted_sys_360.detenu.DetenuRepository;
import com.bustedsys.busted_sys_360.journal.JournalService;
import com.bustedsys.busted_sys_360.sanction.Sanction;
import com.bustedsys.busted_sys_360.sanction.SanctionService;
import com.bustedsys.busted_sys_360.utilisateur.Agent;
import com.bustedsys.busted_sys_360.utilisateur.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InfractionService {

    private final InfractionRepository infractionRepository;
    private final DetenuRepository detenuRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final SanctionService sanctionService;
    private final JournalService journalService;

    // ================================
    // Déclarer une infraction
    // ================================
    @Transactional
    public Infraction declarerInfraction(
            Long detenuId,
            Long agentId,
            String type,
            String lieu,
            String description
    ) {
        // 1. Charger le détenu
        Detenu detenu = detenuRepository.findById(detenuId)
                .orElseThrow(() -> new RuntimeException("Detenu introuvable"));

        // 2. Charger l'agent
        Agent agent = (Agent) utilisateurRepository.findById(agentId)
                .orElseThrow(() -> new RuntimeException("Agent introuvable"));

        // 3. Créer l'infraction
        Infraction infraction = new Infraction();
        infraction.setDetenu(detenu);
        infraction.setAgent(agent);
        infraction.setType(type);
        infraction.setLieu(lieu);
        infraction.setDescription(description);
        infraction.setDate(LocalDate.now());
        infraction.setHeure(LocalTime.now());

        // 4. Déterminer le niveau de gravité
        NiveauGravite gravite = infraction.determinerGravite();
        infraction.setNiveauGravite(gravite);

        // 5. Sauvegarder l'infraction
        Infraction infractionSauvegarde = infractionRepository.save(infraction);

        // 6. Créer la sanction automatiquement
        Sanction sanction = sanctionService.creerSanctionAutomatique(detenu, gravite);

        // 7. Lier la sanction
        infractionSauvegarde.setSanction(sanction);
        infractionRepository.save(infractionSauvegarde);

        // 8. Journaliser
        journalService.enregistrer(
                agentId,
                "DECLARATION_INFRACTION",
                "GESTION_INFRACTIONS",
                "Infraction " + type + " declaree par agent " + agent.getNom()
                        + " contre detenu " + detenu.getNomComplet()
                        + " - Gravite: " + gravite.name()
                        + " - Sanction: " + sanction.getType().name(),
                "127.0.0.1"
        );

        return infractionSauvegarde;
    }

    // ================================
    // Lister toutes les infractions
    // ================================
    public List<Infraction> listerToutes() {
        return infractionRepository.findAll();
    }

    // ================================
    // Trouver par ID
    // ================================
    public Infraction trouverParId(Long id) {
        return infractionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Infraction introuvable"));
    }

    // ================================
    // Infractions d'un détenu
    // ================================
    public List<Infraction> listerParDetenu(Long detenuId) {
        return infractionRepository.findByDetenuId(detenuId);
    }

    // ================================
    // Infractions d'un agent
    // ================================
    public List<Infraction> listerParAgent(Long agentId) {
        return infractionRepository.findByAgentId(agentId);
    }

    // ================================
    // Infractions par période
    // ================================
    public List<Infraction> listerParPeriode(LocalDate debut, LocalDate fin) {
        return infractionRepository.findByDateBetween(debut, fin);
    }

    // ================================
    // Infractions par gravité
    // ================================
    public List<Infraction> listerParGravite(NiveauGravite gravite) {
        return infractionRepository.findByNiveauGravite(gravite);
    }

    // ================================
    // Annuler une sanction
    // ================================
    public Sanction annulerSanction(Long sanctionId, Long directeurId) {
        Sanction sanction = sanctionService.annuler(sanctionId);

        // Journaliser
        journalService.enregistrer(
                directeurId,
                "ANNULATION_SANCTION",
                "GESTION_INFRACTIONS",
                "Sanction " + sanction.getType().name()
                        + " annulee pour detenu " + sanction.getDetenu().getNomComplet(),
                "127.0.0.1"
        );

        return sanction;
    }

    // ================================
    // Compter par détenu
    // ================================
    public long compterParDetenu(Long detenuId) {
        return infractionRepository.countByDetenuId(detenuId);
    }
}
