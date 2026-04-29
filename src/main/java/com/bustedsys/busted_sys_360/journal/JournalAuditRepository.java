package com.bustedsys.busted_sys_360.journal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JournalAuditRepository extends JpaRepository<JournalAudit, Long> {

    // Logs d'un utilisateur
    List<JournalAudit> findByUtilisateurId(Long utilisateurId);

    // Logs par action
    List<JournalAudit> findByAction(String action);

    // Logs par module
    List<JournalAudit> findByModule(String module);

    // Logs par période
    List<JournalAudit> findByDateActionBetween(
            LocalDateTime debut,
            LocalDateTime fin
    );

    // Logs d'un utilisateur par module
    List<JournalAudit> findByUtilisateurIdAndModule(
            Long utilisateurId,
            String module
    );

    // Derniers logs
    @Query("SELECT j FROM JournalAudit j ORDER BY j.dateAction DESC")
    List<JournalAudit> findDerniersLogs();

    // Logs par IP
    List<JournalAudit> findByIpAdresse(String ipAdresse);

    // Compter logs par utilisateur
    long countByUtilisateurId(Long utilisateurId);

    // Tentatives d'accès refusées
    List<JournalAudit> findByActionOrderByDateActionDesc(String action);
}
