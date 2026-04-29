package com.bustedsys.busted_sys_360.cellule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CelluleRepository extends JpaRepository<Cellule, Long> {

    // Trouver toutes les cellules disponibles
    @Query("SELECT c FROM Cellule c WHERE c.occupation < c.capacite")
    List<Cellule> findCellulesDisponibles();

    // Trouver par bloc
    List<Cellule> findByBloc(String bloc);

    // Trouver par bloc et aile
    List<Cellule> findByBlocAndAile(String bloc, String aile);

    // Trouver par numéro
    Optional<Cellule> findByNumero(String numero);

    // Vérifier si numéro existe déjà
    boolean existsByNumero(String numero);

    // Trouver la première cellule disponible
    @Query("SELECT c FROM Cellule c WHERE c.occupation < c.capacite ORDER BY c.id ASC")
    List<Cellule> findPremiereCelluleDisponible();
}
