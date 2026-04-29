package com.bustedsys.busted_sys_360.detenu.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CelluleRequest {

    @NotBlank(message = "Le bloc est obligatoire")
    private String bloc;

    @NotBlank(message = "L'aile est obligatoire")
    private String aile;

    @NotBlank(message = "Le numéro est obligatoire")
    private String numero;

    @NotNull(message = "La capacité est obligatoire")
    @Min(value = 1, message = "La capacité doit être au moins 1")
    private int capacite;
}
