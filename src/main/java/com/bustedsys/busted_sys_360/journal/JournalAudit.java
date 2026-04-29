package com.bustedsys.busted_sys_360.journal;

import com.bustedsys.busted_sys_360.utilisateur.Utilisateur;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "journal_audit")
public class JournalAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String action;

    @Column(nullable = false)
    private String module;

    @Column
    private String detail;

    @Column
    private String ipAdresse;

    @Column(nullable = false)
    private LocalDateTime dateAction;

    // ================================
    // Relation avec Utilisateur
    // ================================
    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;
}
