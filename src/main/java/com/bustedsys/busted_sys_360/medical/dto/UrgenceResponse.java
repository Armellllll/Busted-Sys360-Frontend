package com.bustedsys.busted_sys_360.medical.dto;

import com.bustedsys.busted_sys_360.medical.NiveauGraviteMedical;
import com.bustedsys.busted_sys_360.medical.TypeUrgence;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class UrgenceResponse {

    private Long id;
    private TypeUrgence typeUrgence;
    private NiveauGraviteMedical gravite;
    private String description;
    private LocalDateTime date;
    private String statut;
    private String detenuNom;
    private String detenuPrenom;
    private String agentNom;
    private String agentPrenom;
    private boolean critique;
}
