package com.bustedsys.busted_sys_360.journal;

import com.bustedsys.busted_sys_360.utilisateur.Utilisateur;
import com.bustedsys.busted_sys_360.utilisateur.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JournalService {

    private final JournalAuditRepository journalRepository;
    private final UtilisateurRepository utilisateurRepository;

    // ================================
    // Enregistrer une action
    // ================================
    public JournalAudit enregistrer(
            Long utilisateurId,
            String action,
            String module,
            String detail,
            String ipAdresse
    ) {
        JournalAudit log = new JournalAudit();

        // Charger l'utilisateur si disponible
        if (utilisateurId != null) {
            utilisateurRepository.findById(utilisateurId)
                    .ifPresent(log::setUtilisateur);
        }

        log.setAction(action);
        log.setModule(module);
        log.setDetail(detail);
        log.setIpAdresse(ipAdresse);
        log.setDateAction(LocalDateTime.now());

        return journalRepository.save(log);
    }

    // ================================
    // Enregistrer avec objet Utilisateur
    // ================================
    public JournalAudit enregistrerAvecUtilisateur(
            Utilisateur utilisateur,
            String action,
            String module,
            String detail,
            String ipAdresse
    ) {
        JournalAudit log = new JournalAudit();
        log.setUtilisateur(utilisateur);
        log.setAction(action);
        log.setModule(module);
        log.setDetail(detail);
        log.setIpAdresse(ipAdresse);
        log.setDateAction(LocalDateTime.now());

        return journalRepository.save(log);
    }

    // ================================
    // Consulter tous les logs
    // ================================
    public List<JournalAudit> consulterTous() {
        return journalRepository.findDerniersLogs();
    }

    // ================================
    // Logs d'un utilisateur
    // ================================
    public List<JournalAudit> consulterParUtilisateur(Long utilisateurId) {
        return journalRepository.findByUtilisateurId(utilisateurId);
    }

    // ================================
    // Logs par module
    // ================================
    public List<JournalAudit> consulterParModule(String module) {
        return journalRepository.findByModule(module);
    }

    // ================================
    // Logs par période
    // ================================
    public List<JournalAudit> consulterParPeriode(
            LocalDateTime debut,
            LocalDateTime fin
    ) {
        return journalRepository.findByDateActionBetween(debut, fin);
    }

    // ================================
    // Tentatives d'accès refusées
    // ================================
    public List<JournalAudit> consulterAccesRefuses() {
        return journalRepository.findByActionOrderByDateActionDesc("ACCES_REFUSE");
    }

    // ================================
    // Logs par action
    // ================================
    public List<JournalAudit> consulterParAction(String action) {
        return journalRepository.findByAction(action);
    }

    // ================================
    // Compter logs d'un utilisateur
    // ================================
    public long compterParUtilisateur(Long utilisateurId) {
        return journalRepository.countByUtilisateurId(utilisateurId);
    }
}
