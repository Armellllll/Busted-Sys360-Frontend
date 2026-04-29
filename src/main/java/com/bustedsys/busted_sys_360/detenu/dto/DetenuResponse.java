package com.bustedsys.busted_sys_360.detenu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class DetenuResponse {

    private Long id;
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private String sexe;
    private LocalDate dateEcrou;
    private int dureePeine;
    private LocalDate dateLiberationPrevisionnelle;
    private String statutJuridique;
    private String statut;
    private String celluleNumero;
    private String celluleBloc;
    private String celluleAile;
}
