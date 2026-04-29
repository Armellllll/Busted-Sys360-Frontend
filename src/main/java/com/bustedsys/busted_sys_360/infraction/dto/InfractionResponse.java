package com.bustedsys.busted_sys_360.infraction.dto;

import com.bustedsys.busted_sys_360.infraction.NiveauGravite;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
public class InfractionResponse {

    private Long id;
    private String type;
    private String lieu;
    private LocalTime heure;
    private LocalDate date;
    private NiveauGravite niveauGravite;
    private String description;
    private String detenuNom;
    private String detenuPrenom;
    private String agentNom;
    private String agentPrenom;
    private String sanctionType;
    private int sanctionDuree;
    private String sanctionStatut;
}
