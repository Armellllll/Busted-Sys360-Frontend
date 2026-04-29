package com.bustedsys.busted_sys_360.visite;

import com.bustedsys.busted_sys_360.detenu.Detenu;
import com.bustedsys.busted_sys_360.detenu.DetenuRepository;
import com.bustedsys.busted_sys_360.journal.JournalService;
import com.bustedsys.busted_sys_360.utilisateur.ResponsableVisite;
import com.bustedsys.busted_sys_360.utilisateur.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VisiteService {

    private final VisiteRepository visiteRepository;
    private final DetenuRepository detenuRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final CreneauService creneauService;
    private final JournalService journalService;

    // ================================
    // Planifier une visite
    // ================================
    @Transactional
    public Visite planifierVisite(
            Long detenuId,
            Long responsableId,
            Long creneauId,
            TypeVisite typeVisite,
            String nomVisiteur,
            String lienDeclare
    ) {
        // 1. Charger le détenu
        Detenu detenu = detenuRepository.findById(detenuId)
                .orElseThrow(() -> new RuntimeException("Detenu introuvable"));

        // 2. Charger le responsable
        ResponsableVisite responsable = (ResponsableVisite) utilisateurRepository
                .findById(responsableId)
                .orElseThrow(() -> new RuntimeException("Responsable introuvable"));

        // 3. Vérifier que le détenu est incarcéré
        if (!detenu.estAutorise()) {
            throw new RuntimeException("Le detenu n'est pas incarcere");
        }

        // 4. Créer la visite
        Visite visite = new Visite();
        visite.setDetenu(detenu);
        visite.setResponsable(responsable);
        visite.setTypeVisite(typeVisite);
        visite.setNomVisiteur(nomVisiteur);
        visite.setLienDeclare(lienDeclare);

        // 5. Vérifier autorisation selon type
        if (!visite.estObligatoire()) {
            if (!verifierAutorisation(detenu, lienDeclare)) {
                visite.refuser();
                Visite visiteRefusee = visiteRepository.save(visite);

                // Journaliser refus
                journalService.enregistrer(
                        responsableId,
                        "VISITE_REFUSEE",
                        "GESTION_VISITES",
                        "Visite refusee pour detenu " + detenu.getNomComplet()
                                + " - Visiteur: " + nomVisiteur
                                + " - Lien: " + lienDeclare,
                        "127.0.0.1"
                );

                return visiteRefusee;
            }
        }

        // 6. Réserver le créneau
        Creneau creneau = creneauService.reserver(creneauId);
        visite.setCreneau(creneau);
        visite.confirmer();

        Visite visiteSauvegarde = visiteRepository.save(visite);

        // 7. Journaliser confirmation
        journalService.enregistrer(
                responsableId,
                "PLANIFICATION_VISITE",
                "GESTION_VISITES",
                "Visite " + typeVisite.name()
                        + " planifiee par " + responsable.getNom()
                        + " pour detenu " + detenu.getNomComplet()
                        + " - Visiteur: " + nomVisiteur
                        + " - Creneau: " + creneau.getDate(),
                "127.0.0.1"
        );

        return visiteSauvegarde;
    }

    // ================================
    // Vérifier autorisation visite
    // ================================
    private boolean verifierAutorisation(Detenu detenu, String lien) {
        if ("LIBERE".equals(detenu.getStatut())) {
            return false;
        }
        List<String> liensAutorises = List.of(
                "Pere", "Mere", "Frere", "Soeur",
                "Conjoint", "Enfant", "Ami", "Proche"
        );
        return liensAutorises.stream()
                .anyMatch(l -> l.equalsIgnoreCase(lien));
    }

    // ================================
    // Lister toutes les visites
    // ================================
    public List<Visite> listerToutes() {
        return visiteRepository.findAll();
    }

    // ================================
    // Trouver par ID
    // ================================
    public Visite trouverParId(Long id) {
        return visiteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Visite introuvable"));
    }

    // ================================
    // Visites d'un détenu
    // ================================
    public List<Visite> listerParDetenu(Long detenuId) {
        return visiteRepository.findByDetenuId(detenuId);
    }

    // ================================
    // Visites par statut
    // ================================
    public List<Visite> listerParStatut(StatutVisite statut) {
        return visiteRepository.findByStatut(statut);
    }

    // ================================
    // Visites par responsable
    // ================================
    public List<Visite> listerParResponsable(Long responsableId) {
        return visiteRepository.findByResponsableId(responsableId);
    }

    // ================================
    // Annuler une visite
    // ================================
    @Transactional
    public Visite annuler(Long id) {
        Visite visite = trouverParId(id);

        // Libérer le créneau
        if (visite.getCreneau() != null) {
            creneauService.liberer(visite.getCreneau().getId());
        }

        visite.annuler();
        Visite sauvegarde = visiteRepository.save(visite);

        // Journaliser
        journalService.enregistrer(
                null,
                "ANNULATION_VISITE",
                "GESTION_VISITES",
                "Visite annulee pour detenu "
                        + visite.getDetenu().getNomComplet()
                        + " - Visiteur: " + visite.getNomVisiteur(),
                "127.0.0.1"
        );

        return sauvegarde;
    }

    // ================================
    // Terminer une visite
    // ================================
    public Visite terminer(Long id) {
        Visite visite = trouverParId(id);
        visite.terminer();
        Visite sauvegarde = visiteRepository.save(visite);

        // Journaliser
        journalService.enregistrer(
                null,
                "VISITE_TERMINEE",
                "GESTION_VISITES",
                "Visite terminee pour detenu "
                        + visite.getDetenu().getNomComplet()
                        + " - Visiteur: " + visite.getNomVisiteur(),
                "127.0.0.1"
        );

        return sauvegarde;
    }

    // ================================
    // Compter visites par responsable
    // ================================
    public long compterParResponsable(Long responsableId) {
        return visiteRepository.countByResponsableId(responsableId);
    }
}
