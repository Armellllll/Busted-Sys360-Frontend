package com.bustedsys.busted_sys_360.visite;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisiteRepository extends JpaRepository<Visite, Long> {

    // Visites d'un détenu
    List<Visite> findByDetenuId(Long detenuId);

    // Visites par statut
    List<Visite> findByStatut(StatutVisite statut);

    // Visites d'un responsable
    List<Visite> findByResponsableId(Long responsableId);

    // Visites d'un détenu par statut
    List<Visite> findByDetenuIdAndStatut(Long detenuId, StatutVisite statut);

    // Visites par type
    List<Visite> findByTypeVisite(TypeVisite typeVisite);

    // Compter visites par statut
    long countByStatut(StatutVisite statut);

    // Compter visites par responsable
    long countByResponsableId(Long responsableId);
}
