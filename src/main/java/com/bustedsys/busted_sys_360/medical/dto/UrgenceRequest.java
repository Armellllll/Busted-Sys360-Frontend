package com.bustedsys.busted_sys_360.medical.dto;

import com.bustedsys.busted_sys_360.medical.NiveauGraviteMedical;
import com.bustedsys.busted_sys_360.medical.TypeUrgence;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UrgenceRequest {

    @NotNull(message = "L'identifiant du detenu est obligatoire")
    private Long detenuId;

    @NotNull(message = "L'identifiant de l'agent est obligatoire")
    private Long agentId;

    @NotNull(message = "Le type d'urgence est obligatoire")
    private TypeUrgence typeUrgence;

    @NotNull(message = "Le niveau de gravite est obligatoire")
    private NiveauGraviteMedical gravite;

    @NotBlank(message = "La description est obligatoire")
    private String description;
}