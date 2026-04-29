package com.bustedsys.busted_sys_360.sanction;

import com.bustedsys.busted_sys_360.detenu.Detenu;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "sanctions")
public class Sanction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeSanction type;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDate dateAttribution;

    @Column(nullable = false)
    private int duree; // en jours

    @Column(nullable = false)
    private String statut = "ACTIVE";

    // ================================
    // Relation avec Detenu
    // ================================
    @ManyToOne
    @JoinColumn(name = "detenu_id")
    private Detenu detenu;

    // ================================
    // Méthodes métier
    // ================================
    public void appliquer() {
        this.statut = "ACTIVE";
    }

    public void annuler() {
        this.statut = "ANNULEE";
    }

    public LocalDate getDateFin() {
        return dateAttribution.plusDays(duree);
    }
}
