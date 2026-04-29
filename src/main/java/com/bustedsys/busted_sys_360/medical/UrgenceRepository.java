package com.bustedsys.busted_sys_360.medical;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UrgenceRepository extends JpaRepository<UrgenceMedicale, Long> {

    // Urgences d'un détenu
    List<UrgenceMedicale> findByDetenuId(Long detenuId);

    // Urgences d'un agent
    List<UrgenceMedicale> findByAgentId(Long agentId);

    // Urgences par gravité
    List<UrgenceMedicale> findByGravite(NiveauGraviteMedical gravite);

    // Urgences par statut
    List<UrgenceMedicale> findByStatut(String statut);

    // Urgences critiques en cours
    List<UrgenceMedicale> findByGraviteAndStatut(
            NiveauGraviteMedical gravite,
            String statut
    );

    // Compter par gravité
    long countByGravite(NiveauGraviteMedical gravite);
}
