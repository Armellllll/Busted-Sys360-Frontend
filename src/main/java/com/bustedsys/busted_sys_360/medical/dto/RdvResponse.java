package com.bustedsys.busted_sys_360.medical.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class RdvResponse {

    private Long id;
    private String motif;
    private LocalDateTime date;
    private String statut;
    private String detenuNom;
    private String detenuPrenom;
    private String agentNom;
    private String agentPrenom;
}
