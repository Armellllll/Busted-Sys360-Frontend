package com.bustedsys.busted_sys_360.infraction;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InfractionRepository extends JpaRepository<Infraction, Long> {

    // Trouver les infractions d'un détenu
    List<Infraction> findByDetenuId(Long detenuId);

    // Trouver les infractions d'un agent
    List<Infraction> findByAgentId(Long agentId);

    // Trouver par niveau de gravité
    List<Infraction> findByNiveauGravite(NiveauGravite niveauGravite);

    // Trouver par date
    List<Infraction> findByDate(LocalDate date);

    // Trouver par période
    List<Infraction> findByDateBetween(LocalDate debut, LocalDate fin);

    // Compter par niveau de gravité
    long countByNiveauGravite(NiveauGravite niveauGravite);

    // Compter total infractions
    long countByDetenuId(Long detenuId);
}