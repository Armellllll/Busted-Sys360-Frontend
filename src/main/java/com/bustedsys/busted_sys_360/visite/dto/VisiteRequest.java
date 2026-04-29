package com.bustedsys.busted_sys_360.visite.dto;

import com.bustedsys.busted_sys_360.visite.TypeVisite;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VisiteRequest {

    @NotNull(message = "L'identifiant du detenu est obligatoire")
    private Long detenuId;

    @NotNull(message = "L'identifiant du responsable est obligatoire")
    private Long responsableId;

    @NotNull(message = "L'identifiant du creneau est obligatoire")
    private Long creneauId;

    @NotNull(message = "Le type de visite est obligatoire")
    private TypeVisite typeVisite;

    @NotBlank(message = "Le nom du visiteur est obligatoire")
    private String nomVisiteur;

    @NotBlank(message = "Le lien declare est obligatoire")
    private String lienDeclare;
}
