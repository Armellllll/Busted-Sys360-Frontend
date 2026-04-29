package com.bustedsys.busted_sys_360.detenu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DetenuRequest {

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    @NotNull(message = "La date de naissance est obligatoire")
    private LocalDate dateNaissance;

    @NotBlank(message = "Le sexe est obligatoire")
    private String sexe;

    @NotNull(message = "La date d'écrou est obligatoire")
    private LocalDate dateEcrou;

    @NotNull(message = "La durée de peine est obligatoire")
    private int dureePeine;

    @NotBlank(message = "Le statut juridique est obligatoire")
    private String statutJuridique;
}
