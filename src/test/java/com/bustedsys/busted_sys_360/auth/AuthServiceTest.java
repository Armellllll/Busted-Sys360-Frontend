package com.bustedsys.busted_sys_360.auth;

import com.bustedsys.busted_sys_360.auth.dto.LoginRequest;
import com.bustedsys.busted_sys_360.auth.dto.LoginResponse;
import com.bustedsys.busted_sys_360.journal.JournalService;
import com.bustedsys.busted_sys_360.utilisateur.Role;
import com.bustedsys.busted_sys_360.utilisateur.Utilisateur;
import com.bustedsys.busted_sys_360.utilisateur.UtilisateurRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JournalService journalService;

    @InjectMocks
    private AuthService authService;

    private LoginRequest loginRequest;
    private Utilisateur utilisateur;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest();
        loginRequest.setLogin("directeur");
        loginRequest.setMotDePasse("directeur123");

        utilisateur = new com.bustedsys.busted_sys_360.utilisateur.Directeur();
        utilisateur.setLogin("directeur");
        utilisateur.setNom("Kouassi");
        utilisateur.setPrenom("Directeur");
        utilisateur.setRole(Role.DIRECTEUR);
        utilisateur.setActif(true);
    }

    // ================================
    // Test connexion réussie
    // ================================
    @Test
    void connexion_reussie() {
        // Préparer
        when(utilisateurRepository.findByLogin("directeur"))
                .thenReturn(Optional.of(utilisateur));
        when(jwtService.genererToken(utilisateur))
                .thenReturn("token_jwt_test");

        // Exécuter
        LoginResponse response = authService.connecter(loginRequest, "127.0.0.1");

        // Vérifier
        assertNotNull(response);
        assertEquals("directeur", response.getLogin());
        assertEquals("Kouassi", response.getNom());
        assertEquals("DIRECTEUR", response.getRole());
        assertEquals("token_jwt_test", response.getToken());
    }

    // ================================
    // Test login incorrect
    // ================================
    @Test
    void connexion_login_incorrect() {
        // Préparer
        doThrow(new BadCredentialsException("Bad credentials"))
                .when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        // Vérifier
        assertThrows(RuntimeException.class, () ->
                authService.connecter(loginRequest, "127.0.0.1")
        );
    }

    // ================================
    // Test compte désactivé
    // ================================
    @Test
    void connexion_compte_desactive() {
        // Préparer
        utilisateur.setActif(false);
        when(utilisateurRepository.findByLogin("directeur"))
                .thenReturn(Optional.of(utilisateur));

        // Vérifier
        assertThrows(RuntimeException.class, () ->
                authService.connecter(loginRequest, "127.0.0.1")
        );
    }
}