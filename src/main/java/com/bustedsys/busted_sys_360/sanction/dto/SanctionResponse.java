package com.bustedsys.busted_sys_360.sanction.dto;

import com.bustedsys.busted_sys_360.sanction.TypeSanction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class SanctionResponse {

    private Long id;
    private TypeSanction type;
    private String description;
    private LocalDate dateAttribution;
    private LocalDate dateFin;
    private int duree;
    private String statut;
    private String detenuNom;
    private String detenuPrenom;
}
