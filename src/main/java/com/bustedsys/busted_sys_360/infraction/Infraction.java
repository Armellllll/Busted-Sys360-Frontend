package com.bustedsys.busted_sys_360.infraction;

import com.bustedsys.busted_sys_360.detenu.Detenu;
import com.bustedsys.busted_sys_360.sanction.Sanction;
import com.bustedsys.busted_sys_360.utilisateur.Agent;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "infractions")
public class Infraction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String lieu;

    @Column(nullable = false)
    private LocalTime heure;

    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NiveauGravite niveauGravite;

    @Column(nullable = false)
    private String description;

    // ================================
    // Relations
    // ================================
    @ManyToOne
    @JoinColumn(name = "detenu_id", nullable = false)
    private Detenu detenu;

    @ManyToOne
    @JoinColumn(name = "agent_id", nullable = false)
    private Agent agent;

    @OneToOne
    @JoinColumn(name = "sanction_id")
    private Sanction sanction;

    // ================================
    // Méthodes métier
    // ================================
    public NiveauGravite determinerGravite() {
        return switch (type.toUpperCase()) {
            case "VIOLENCE", "AGRESSION", "REVOLTE" -> NiveauGravite.TRES_GRAVE;
            case "INSUBORDINATION", "MENACE" -> NiveauGravite.GRAVE;
            case "INSULTE", "BAGARRE" -> NiveauGravite.MOYEN;
            default -> NiveauGravite.FAIBLE;
        };
    }
}