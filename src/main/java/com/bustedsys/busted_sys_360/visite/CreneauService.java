package com.bustedsys.busted_sys_360.visite;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreneauService {

    private final CreneauRepository creneauRepository;

    // ================================
    // Créer un créneau
    // ================================
    public Creneau creerCreneau(LocalDate date,
                                LocalTime heureDebut,
                                LocalTime heureFin) {
        Creneau creneau = new Creneau();
        creneau.setDate(date);
        creneau.setHeureDebut(heureDebut);
        creneau.setHeureFin(heureFin);
        creneau.setDisponible(true);
        return creneauRepository.save(creneau);
    }

    // ================================
    // Lister tous les créneaux
    // ================================
    public List<Creneau> listerTous() {
        return creneauRepository.findAll();
    }

    // ================================
    // Lister créneaux disponibles
    // ================================
    public List<Creneau> listerDisponibles() {
        return creneauRepository.findByDisponibleTrue();
    }

    // ================================
    // Lister disponibles par date
    // ================================
    public List<Creneau> listerDisponiblesParDate(LocalDate date) {
        return creneauRepository.findByDateAndDisponibleTrue(date);
    }

    // ================================
    // Trouver par ID
    // ================================
    public Creneau trouverParId(Long id) {
        return creneauRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Creneau introuvable"));
    }

    // ================================
    // Réserver un créneau
    // ================================
    public Creneau reserver(Long id) {
        Creneau creneau = trouverParId(id);
        if (!creneau.estDisponible()) {
            throw new RuntimeException("Ce creneau est deja reserve");
        }
        creneau.reserver();
        return creneauRepository.save(creneau);
    }

    // ================================
    // Libérer un créneau
    // ================================
    public Creneau liberer(Long id) {
        Creneau creneau = trouverParId(id);
        creneau.liberer();
        return creneauRepository.save(creneau);
    }

    // ================================
    // Supprimer un créneau
    // ================================
    public void supprimer(Long id) {
        Creneau creneau = trouverParId(id);
        if (!creneau.estDisponible()) {
            throw new RuntimeException(
                    "Impossible de supprimer un creneau reserve"
            );
        }
        creneauRepository.delete(creneau);
    }
}
