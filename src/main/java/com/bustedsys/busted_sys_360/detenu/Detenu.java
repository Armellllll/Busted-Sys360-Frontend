package com.bustedsys.busted_sys_360.detenu;

import com.bustedsys.busted_sys_360.cellule.Cellule;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "detenus")
@Getter
@Setter
public class Detenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "prenom", nullable = false)
    private String prenom;

    @Column(name = "numero_ecrou")
    private String numeroEcrou;

    @Column(name = "date_naissance", nullable = false)
    private LocalDate dateNaissance;

    @Column(name = "lieu_naissance")
    private String lieuNaissance;

    @Column(name = "sexe", nullable = false)
    private String sexe;

    @Column(name = "statut_juridique", nullable = false)
    private String statutJuridique;

    @Column(name = "statut", nullable = false)
    private String statut;

    @Column(name = "date_entree")
    private LocalDate dateEntree;

    @Column(name = "date_sortie")
    private LocalDate dateSortie;

    @Column(name = "motif")
    private String motif;

    @Column(name = "duree_peine", nullable = false)
    private Integer dureePeine;

    @Column(name = "date_ecrou", nullable = false)
    private LocalDate dateEcrou;

    @Column(name = "date_liberation_previsionnelle")
    private LocalDate dateLiberationPrevisionnelle;

    @Column(name = "isolement")
    private Boolean isolement = false;

    @ManyToOne
    @JoinColumn(name = "cellule_id")
    private Cellule cellule;

    // ================================
    // MÉTHODES
    // ================================

    public void calculerDateLiberation() {
        if (dateEntree != null && dureePeine != null) {
            this.dateLiberationPrevisionnelle = dateEntree.plusMonths(dureePeine);
        }
    }

    public LocalDate getDateLiberationPrevisionnelle() {
        return dateLiberationPrevisionnelle;
    }

    public String getNomComplet() {
        return nom + " " + prenom;
    }

    public boolean estAutorise() {
        if ("LIBERE".equals(statut)) {
            return false;
        }
        if (isolement != null && isolement) {
            return false;
        }
        return true;
    }
}