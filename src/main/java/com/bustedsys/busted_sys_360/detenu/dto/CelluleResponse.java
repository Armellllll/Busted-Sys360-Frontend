package com.bustedsys.busted_sys_360.detenu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CelluleResponse {

    private Long id;
    private String bloc;
    private String aile;
    private String numero;
    private int capacite;
    private int occupation;
    private float tauxOccupation;
    private boolean disponible;
}
