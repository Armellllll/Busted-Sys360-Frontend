package com.bustedsys.busted_sys_360.utilisateur;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    // Trouver un utilisateur par son login
    Optional<Utilisateur> findByLogin(String login);
    Optional<Utilisateur> findByEmail(String email);

    // Vérifier si un login existe déjà
    boolean existsByLogin(String login);

}
