package com.bustedsys.busted_sys_360.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank(message = "Le login est obligatoire")
    private String login;

    @NotBlank(message = "Le mot de passe est obligatoire")
    private String motDePasse;

    @Email(message = "Email doit être valide")
    @NotBlank(message = "L'email est obligatoire")
    private String email;

    private String nom;
    private String prenom;
}