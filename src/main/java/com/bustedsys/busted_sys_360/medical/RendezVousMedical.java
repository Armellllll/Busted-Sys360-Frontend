package com.bustedsys.busted_sys_360.medical;

import com.bustedsys.busted_sys_360.detenu.Detenu;
import com.bustedsys.busted_sys_360.utilisateur.Agent;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "rendez_vous_medicaux")
public class RendezVousMedical {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String motif;

    @Column
    private LocalDateTime date;

    @Column(nullable = false)
    private String statut = "EN_ATTENTE";

    // ================================
    // Relations
    // ================================
    @ManyToOne
    @JoinColumn(name = "detenu_id", nullable = false)
    private Detenu detenu;

    @ManyToOne
    @JoinColumn(name = "agent_id", nullable = false)
    private Agent agent;

    // ================================
    // Méthodes métier
    // ================================
    public void confirmer() {
        this.statut = "CONFIRME";
    }

    public void annuler() {
        this.statut = "ANNULE";
    }
}
