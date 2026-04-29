package com.bustedsys.busted_sys_360.detenu;

import com.bustedsys.busted_sys_360.cellule.Cellule;
import com.bustedsys.busted_sys_360.cellule.CelluleService;
import com.bustedsys.busted_sys_360.journal.JournalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DetenuServiceTest {

    @Mock
    private DetenuRepository detenuRepository;

    @Mock
    private CelluleService celluleService;

    @Mock
    private JournalService journalService;

    @InjectMocks
    private DetenuService detenuService;

    private Detenu detenu;
    private Cellule cellule;

    @BeforeEach
    void setUp() {
        detenu = new Detenu();
        detenu.setNom("Kouassi");
        detenu.setPrenom("Jean");
        detenu.setDateEcrou(LocalDate.of(2026, 1, 1));
        detenu.setDureePeine(24);
        detenu.setStatut("INCARCERE");

        cellule = new Cellule();
        cellule.setId(1L);
        cellule.setNumero("A-01");
        cellule.setCapacite(4);
        cellule.setOccupation(0);
    }

    // ================================
    // Test enregistrement détenu
    // ================================
    @Test
    void enregistrement_detenu_succes() {
        // Préparer
        when(celluleService.trouverPremiereCelluleDisponible())
                .thenReturn(cellule);
        when(detenuRepository.save(any(Detenu.class)))
                .thenReturn(detenu);

        // Exécuter
        Detenu resultat = detenuService.enregistrerDetenu(detenu);

        // Vérifier
        assertNotNull(resultat);
        assertEquals("Kouassi", resultat.getNom());
        assertNotNull(detenu.getDateLiberationPrevisionnelle());
        assertEquals(
                LocalDate.of(2028, 1, 1),
                detenu.getDateLiberationPrevisionnelle()
        );
        verify(detenuRepository, times(1)).save(any(Detenu.class));
    }

    // ================================
    // Test calcul date libération
    // ================================
    @Test
    void calcul_date_liberation_correct() {
        // Préparer
        detenu.setDateEcrou(LocalDate.of(2026, 1, 1));
        detenu.setDureePeine(12);

        // Exécuter
        detenu.calculerDateLiberation();

        // Vérifier
        assertEquals(
                LocalDate.of(2027, 1, 1),
                detenu.getDateLiberationPrevisionnelle()
        );
    }

    // ================================
    // Test détenu introuvable
    // ================================
    @Test
    void detenu_introuvable() {
        // Préparer
        when(detenuRepository.findById(999L))
                .thenReturn(Optional.empty());

        // Vérifier
        assertThrows(RuntimeException.class, () ->
                detenuService.trouverParId(999L)
        );
    }

    // ================================
    // Test libération détenu
    // ================================
    @Test
    void liberation_detenu_succes() {
        // Préparer
        detenu.setId(1L);
        detenu.setCellule(cellule);
        when(detenuRepository.findById(1L))
                .thenReturn(Optional.of(detenu));
        when(detenuRepository.save(any(Detenu.class)))
                .thenReturn(detenu);

        // Exécuter
        Detenu resultat = detenuService.libererDetenu(1L);

        // Vérifier
        assertEquals("LIBERE", resultat.getStatut());
        assertNull(resultat.getCellule());
    }
}