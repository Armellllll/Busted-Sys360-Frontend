package com.bustedsys.busted_sys_360.cellule;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cellules")
public class Cellule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String bloc;

    @Column(nullable = false)
    private String aile;

    @Column(nullable = false)
    private String numero;

    @Column(nullable = false)
    private int capacite;

    @Column(nullable = false)
    private int occupation = 0;

    // ================================
    // Méthodes métier
    // ================================

    public boolean estDisponible() {
        return occupation < capacite;
    }

    public float getTauxOccupation() {
        if (capacite == 0) return 0;
        return ((float) occupation / capacite) * 100;
    }

    public void incrementerOccupation() {
        if (occupation < capacite) {
            occupation++;
        }
    }

    public void decrementerOccupation() {
        if (occupation > 0) {
            occupation--;
        }
    }
}
