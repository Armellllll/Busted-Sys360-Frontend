package com.bustedsys.busted_sys_360.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponse {

    private String token;
    private String login;
    private String nom;
    private String prenom;
    private String role;
}
