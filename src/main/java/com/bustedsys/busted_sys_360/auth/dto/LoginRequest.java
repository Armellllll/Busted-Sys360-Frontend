package com.bustedsys.busted_sys_360.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "Le login est obligatoire")
    private String login;

    @NotBlank(message = "Le mot de passe est obligatoire")
    private String motDePasse;
}
