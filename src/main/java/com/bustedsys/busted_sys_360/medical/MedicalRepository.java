package com.bustedsys.busted_sys_360.medical;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalRepository extends JpaRepository<RendezVousMedical, Long> {

    // RDV d'un détenu
    List<RendezVousMedical> findByDetenuId(Long detenuId);

    // RDV d'un agent
    List<RendezVousMedical> findByAgentId(Long agentId);

    // RDV par statut
    List<RendezVousMedical> findByStatut(String statut);

    // RDV d'un détenu par statut
    List<RendezVousMedical> findByDetenuIdAndStatut(Long detenuId, String statut);

    // Compter les RDV par statut
    long countByStatut(String statut);
}
