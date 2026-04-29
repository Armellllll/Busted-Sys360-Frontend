package com.bustedsys.busted_sys_360.auth;

import com.bustedsys.busted_sys_360.auth.dto.LoginRequest;
import com.bustedsys.busted_sys_360.auth.dto.LoginResponse;
import com.bustedsys.busted_sys_360.auth.dto.RegisterRequest;
import com.bustedsys.busted_sys_360.journal.JournalService;
import com.bustedsys.busted_sys_360.utilisateur.Role;
import com.bustedsys.busted_sys_360.utilisateur.Utilisateur;
import com.bustedsys.busted_sys_360.utilisateur.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final JournalService journalService;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse connecter(LoginRequest request, String ip) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getLogin(),
                            request.getMotDePasse()
                    )
            );
        } catch (AuthenticationException e) {
            journalService.enregistrer(
                    null,
                    "LOGIN_ECHEC",
                    "AUTH",
                    "Echec connexion pour login : " + request.getLogin(),
                    ip
            );
            throw new RuntimeException("Login ou mot de passe incorrect");
        }

        Utilisateur utilisateur = utilisateurRepository
                .findByLogin(request.getLogin())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        if (!utilisateur.isActif()) {
            throw new RuntimeException("Compte desactive");
        }

        String token = jwtService.genererToken(utilisateur);

        journalService.enregistrerAvecUtilisateur(
                utilisateur,
                "LOGIN",
                "AUTH",
                utilisateur.getNom() + " connecte avec role " + utilisateur.getRole(),
                ip
        );

        return new LoginResponse(
                token,
                utilisateur.getLogin(),
                utilisateur.getNom(),
                utilisateur.getPrenom(),
                utilisateur.getRole().name()
        );
    }

    // ================================
    // MÉTHODE REGISTER AJOUTÉE
    // ================================
    public Utilisateur register(RegisterRequest request) {
        // Vérifier si l'utilisateur existe déjà
        if (utilisateurRepository.findByLogin(request.getLogin()).isPresent()) {
            throw new RuntimeException("Un utilisateur avec ce login existe déjà");
        }

        if (utilisateurRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Un utilisateur avec cet email existe déjà");
        }

        // Créer le nouvel utilisateur
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setLogin(request.getLogin());
        utilisateur.setMotDePasse(passwordEncoder.encode(request.getMotDePasse()));
        utilisateur.setEmail(request.getEmail());
        utilisateur.setNom(request.getNom());
        utilisateur.setPrenom(request.getPrenom());
        utilisateur.setRole(Role.valueOf("USER"));
        utilisateur.setActif(true);

        return utilisateurRepository.save(utilisateur);
    }
}