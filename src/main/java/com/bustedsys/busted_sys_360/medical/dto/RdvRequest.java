package com.bustedsys.busted_sys_360.medical.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RdvRequest {

    @NotNull(message = "L'identifiant du detenu est obligatoire")
    private Long detenuId;

    @NotNull(message = "L'identifiant de l'agent est obligatoire")
    private Long agentId;

    @NotBlank(message = "Le motif est obligatoire")
    private String motif;
}
