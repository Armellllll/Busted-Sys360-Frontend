package com.bustedsys.busted_sys_360.rapport.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RapportStatResponse {

    // Détenus
    private long totalDetenusIncarceres;
    private long totalDetenusLiberes;

    // Cellules
    private float tauxOccupationGlobal;
    private long totalCellules;
    private long totalCellulesDisponibles;

    // Infractions
    private long totalInfractions;
    private long infractionsGraves;
    private long infractionsTresGraves;

    // Medical
    private long totalUrgences;
    private long urgencesCritiques;
    private long totalRdvMedicaux;
    private long rdvEnAttente;

    // Visites
    private long totalVisites;
    private long visitesConfirmees;
    private long visitesRefusees;
}
