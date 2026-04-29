package com.bustedsys.busted_sys_360.visite.dto;

import com.bustedsys.busted_sys_360.visite.StatutVisite;
import com.bustedsys.busted_sys_360.visite.TypeVisite;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
public class VisiteResponse {

    private Long id;
    private TypeVisite typeVisite;
    private StatutVisite statut;
    private String nomVisiteur;
    private String lienDeclare;
    private String detenuNom;
    private String detenuPrenom;
    private String responsableNom;
    private String responsablePrenom;
    private LocalDate creneauDate;
    private LocalTime creneauHeureDebut;
    private LocalTime creneauHeureFin;
    private boolean obligatoire;
}
