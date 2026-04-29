package com.bustedsys.busted_sys_360.detenu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DetenuRepository extends JpaRepository<Detenu, Long> {

    Optional<Detenu> findByNumeroEcrou(String numeroEcrou);

    List<Detenu> findByStatut(String statut);

    List<Detenu> findByCelluleId(Long celluleId);

    @Query("SELECT d FROM Detenu d WHERE d.cellule.bloc = :bloc")
    List<Detenu> findByCelluleBloc(@Param("bloc") String bloc);

    @Query("SELECT d FROM Detenu d WHERE LOWER(d.nom) LIKE LOWER(CONCAT('%', :recherche, '%')) OR LOWER(d.prenom) LIKE LOWER(CONCAT('%', :recherche, '%'))")
    List<Detenu> findByNomContainingIgnoreCase(@Param("recherche") String recherche);

    long countByStatut(String statut);
}