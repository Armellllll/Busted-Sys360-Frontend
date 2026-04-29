package com.bustedsys.busted_sys_360.visite;

import com.bustedsys.busted_sys_360.detenu.Detenu;
import com.bustedsys.busted_sys_360.utilisateur.ResponsableVisite;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "visites")
public class Visite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeVisite typeVisite;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutVisite statut = StatutVisite.EN_ATTENTE;

    @Column(nullable = false)
    private String nomVisiteur;

    @Column(nullable = false)
    private String lienDeclare;

    // ================================
    // Relations
    // ================================
    @ManyToOne
    @JoinColumn(name = "detenu_id", nullable = false)
    private Detenu detenu;

    @ManyToOne
    @JoinColumn(name = "responsable_id", nullable = false)
    private ResponsableVisite responsable;

    @OneToOne
    @JoinColumn(name = "creneau_id")
    private Creneau creneau;

    // ================================
    // Méthodes métier
    // ================================
    public boolean estObligatoire() {
        return typeVisite == TypeVisite.AVOCAT ||
                typeVisite == TypeVisite.OFFICIEL ||
                typeVisite == TypeVisite.INTERROGATOIRE ||
                typeVisite == TypeVisite.AUTRE_OBLIGATOIRE;
    }

    public void confirmer() {
        this.statut = StatutVisite.CONFIRMEE;
    }

    public void refuser() {
        this.statut = StatutVisite.REFUSEE;
    }

    public void annuler() {
        this.statut = StatutVisite.ANNULEE;
    }

    public void terminer() {
        this.statut = StatutVisite.TERMINEE;
    }
}
