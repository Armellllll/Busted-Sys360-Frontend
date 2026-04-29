package com.bustedsys.busted_sys_360.infraction;

import com.bustedsys.busted_sys_360.detenu.Detenu;
import com.bustedsys.busted_sys_360.detenu.DetenuRepository;
import com.bustedsys.busted_sys_360.journal.JournalService;
import com.bustedsys.busted_sys_360.sanction.Sanction;
import com.bustedsys.busted_sys_360.sanction.SanctionService;
import com.bustedsys.busted_sys_360.sanction.TypeSanction;
import com.bustedsys.busted_sys_360.utilisateur.Agent;
import com.bustedsys.busted_sys_360.utilisateur.UtilisateurRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InfractionServiceTest {

    @Mock
    private InfractionRepository infractionRepository;

    @Mock
    private DetenuRepository detenuRepository;

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private SanctionService sanctionService;

    @Mock
    private JournalService journalService;

    @InjectMocks
    private InfractionService infractionService;

    private Detenu detenu;
    private Agent agent;
    private Sanction sanction;

    @BeforeEach
    void setUp() {
        detenu = new Detenu();
        detenu.setId(1L);
        detenu.setNom("Kouassi");
        detenu.setPrenom("Jean");
        detenu.setStatut("INCARCERE");

        agent = new Agent();
        agent.setId(3L);
        agent.setNom("Gouni");
        agent.setPrenom("Agent");

        sanction = new Sanction();
        sanction.setId(1L);
        sanction.setType(TypeSanction.TRANSFREMENT);
        sanction.setDuree(30);
        sanction.setStatut("ACTIVE");
        sanction.setDetenu(detenu);
    }

    // ================================
    // Test déclaration infraction VIOLENCE
    // ================================
    @Test
    void declaration_infraction_violence_tres_grave() {
        // Préparer
        when(detenuRepository.findById(1L))
                .thenReturn(Optional.of(detenu));
        when(utilisateurRepository.findById(3L))
                .thenReturn(Optional.of(agent));
        when(infractionRepository.save(any(Infraction.class)))
                .thenAnswer(i -> i.getArgument(0));
        when(sanctionService.creerSanctionAutomatique(any(), any()))
                .thenReturn(sanction);

        // Exécuter
        Infraction infraction = infractionService.declarerInfraction(
                1L, 3L, "VIOLENCE", "Cour", "Bagarre"
        );

        // Vérifier
        assertNotNull(infraction);
        assertEquals(NiveauGravite.TRES_GRAVE, infraction.getNiveauGravite());
        assertEquals("VIOLENCE", infraction.getType());
        verify(sanctionService, times(1))
                .creerSanctionAutomatique(detenu, NiveauGravite.TRES_GRAVE);
    }

    // ================================
    // Test gravité INSULTE = MOYEN
    // ================================
    @Test
    void infraction_insulte_gravite_moyen() {
        // Préparer
        when(detenuRepository.findById(1L))
                .thenReturn(Optional.of(detenu));
        when(utilisateurRepository.findById(3L))
                .thenReturn(Optional.of(agent));
        when(infractionRepository.save(any(Infraction.class)))
                .thenAnswer(i -> i.getArgument(0));
        when(sanctionService.creerSanctionAutomatique(any(), any()))
                .thenReturn(sanction);

        // Exécuter
        Infraction infraction = infractionService.declarerInfraction(
                1L, 3L, "INSULTE", "Cellule", "Insulte garde"
        );

        // Vérifier
        assertEquals(NiveauGravite.MOYEN, infraction.getNiveauGravite());
    }

    // ================================
    // Test détenu introuvable
    // ================================
    @Test
    void declaration_infraction_detenu_introuvable() {
        // Préparer
        when(detenuRepository.findById(999L))
                .thenReturn(Optional.empty());

        // Vérifier
        assertThrows(RuntimeException.class, () ->
                infractionService.declarerInfraction(
                        999L, 3L, "VIOLENCE", "Cour", "Test"
                )
        );
    }
}
