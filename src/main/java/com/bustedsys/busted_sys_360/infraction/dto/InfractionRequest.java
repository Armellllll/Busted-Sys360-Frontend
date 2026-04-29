package com.bustedsys.busted_sys_360.infraction.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InfractionRequest {

    @NotNull(message = "L'identifiant du detenu est obligatoire")
    private Long detenuId;

    @NotNull(message = "L'identifiant de l'agent est obligatoire")
    private Long agentId;

    @NotBlank(message = "Le type est obligatoire")
    private String type;

    @NotBlank(message = "Le lieu est obligatoire")
    private String lieu;

    @NotBlank(message = "La description est obligatoire")
    private String description;
}
