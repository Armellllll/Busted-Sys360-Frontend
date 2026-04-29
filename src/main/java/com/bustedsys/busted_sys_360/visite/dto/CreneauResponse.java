package com.bustedsys.busted_sys_360.visite.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
public class CreneauResponse {

    private Long id;
    private LocalDate date;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private boolean disponible;
}
