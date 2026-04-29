package com.bustedsys.busted_sys_360.sanction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SanctionRepository extends JpaRepository<Sanction, Long> {

    // Trouver les sanctions d'un détenu
    List<Sanction> findByDetenuId(Long detenuId);

    // Trouver les sanctions actives d'un détenu
    List<Sanction> findByDetenuIdAndStatut(Long detenuId, String statut);

    // Compter les sanctions par type
    long countByType(TypeSanction type);

    // Trouver par statut
    List<Sanction> findByStatut(String statut);
}
