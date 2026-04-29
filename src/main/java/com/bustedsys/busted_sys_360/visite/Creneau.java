package com.bustedsys.busted_sys_360.visite;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "creneaux")
public class Creneau {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime heureDebut;

    @Column(nullable = false)
    private LocalTime heureFin;

    @Column(nullable = false)
    private boolean disponible = true;

    // ================================
    // Méthodes métier
    // ================================
    public void reserver() {
        this.disponible = false;
    }

    public void liberer() {
        this.disponible = true;
    }

    public boolean estDisponible() {
        return disponible;
    }
}