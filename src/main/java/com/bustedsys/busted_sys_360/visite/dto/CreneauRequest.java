package com.bustedsys.busted_sys_360.visite.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class CreneauRequest {

    @NotNull(message = "La date est obligatoire")
    private LocalDate date;

    @NotNull(message = "L'heure de debut est obligatoire")
    private LocalTime heureDebut;

    @NotNull(message = "L'heure de fin est obligatoire")
    private LocalTime heureFin;
}
