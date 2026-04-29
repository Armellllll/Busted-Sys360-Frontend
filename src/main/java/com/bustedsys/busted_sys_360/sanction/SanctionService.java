package com.bustedsys.busted_sys_360.sanction;

import com.bustedsys.busted_sys_360.detenu.Detenu;
import com.bustedsys.busted_sys_360.infraction.NiveauGravite;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SanctionService {

    private final SanctionRepository sanctionRepository;

    // ================================
    // Créer une sanction automatiquement
    // ================================
    public Sanction creerSanctionAutomatique(Detenu detenu, NiveauGravite gravite) {

        Sanction sanction = new Sanction();
        sanction.setDetenu(detenu);
        sanction.setDateAttribution(LocalDate.now());
        sanction.setStatut("ACTIVE");

        switch (gravite) {
            case FAIBLE -> {
                sanction.setType(TypeSanction.AVERTISSEMENT);
                sanction.setDescription("Avertissement officiel");
                sanction.setDuree(3);
            }
            case MOYEN -> {
                sanction.setType(TypeSanction.PRIVATION_ACTIVITES);
                sanction.setDescription("Privation des activites collectives");
                sanction.setDuree(7);
            }
            case GRAVE -> {
                sanction.setType(TypeSanction.ISOLEMENT);
                sanction.setDescription("Mise en isolement");
                sanction.setDuree(15);
            }
            case TRES_GRAVE -> {
                sanction.setType(TypeSanction.TRANSFREMENT);
                sanction.setDescription("Transferement vers un autre etablissement");
                sanction.setDuree(30);
            }
        }

        return sanctionRepository.save(sanction);
    }

    // ================================
    // Lister les sanctions d'un détenu
    // ================================
    public List<Sanction> listerParDetenu(Long detenuId) {
        return sanctionRepository.findByDetenuId(detenuId);
    }

    // ================================
    // Lister les sanctions actives
    // ================================
    public List<Sanction> listerSanctionsActives(Long detenuId) {
        return sanctionRepository.findByDetenuIdAndStatut(detenuId, "ACTIVE");
    }

    // ================================
    // Annuler une sanction
    // ================================
    public Sanction annuler(Long id) {
        Sanction sanction = sanctionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sanction introuvable"));
        sanction.annuler();
        return sanctionRepository.save(sanction);
    }

    // ================================
    // Toutes les sanctions
    // ================================
    public List<Sanction> listerToutes() {
        return sanctionRepository.findAll();
    }
}
