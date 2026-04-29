package com.bustedsys.busted_sys_360.journal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class JournalResponse {

    private Long id;
    private String action;
    private String module;
    private String detail;
    private String ipAdresse;
    private LocalDateTime dateAction;
    private String utilisateurLogin;
    private String utilisateurNom;
    private String utilisateurPrenom;
    private String utilisateurRole;
}
