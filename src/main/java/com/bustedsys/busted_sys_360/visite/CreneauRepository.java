package com.bustedsys.busted_sys_360.visite;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CreneauRepository extends JpaRepository<Creneau, Long> {

    // Créneaux disponibles
    List<Creneau> findByDisponibleTrue();

    // Créneaux disponibles par date
    List<Creneau> findByDateAndDisponibleTrue(LocalDate date);

    // Créneaux par date
    List<Creneau> findByDate(LocalDate date);

    // Créneaux disponibles entre deux dates
    @Query("SELECT c FROM Creneau c WHERE c.disponible = true AND c.date BETWEEN :debut AND :fin")
    List<Creneau> findDisponiblesEntreDates(LocalDate debut, LocalDate fin);
}
