package com.bustedsys.busted_sys_360.rapport.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RapportPersonnelResponse {

    private Long utilisateurId;
    private String login;
    private String nom;
    private String prenom;
    private String role;

    // Statistiques par rôle
    private long nombreInfractionsDeclares;   // Agent
    private long nombreUrgencesDeclares;       // Agent
    private long nombreRdvDemandes;            // Agent
    private long nombreVisitesPlanifiees;      // ResponsableVisite
    private long nombreActionsJournal;         // tous
}
