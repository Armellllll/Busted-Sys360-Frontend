package com.bustedsys.busted_sys_360.config;

import com.bustedsys.busted_sys_360.utilisateur.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        // Créer les utilisateurs de test seulement s'ils n'existent pas
        if (utilisateurRepository.count() == 0) {

            // ================================
            // Directeur
            // ================================
            Directeur directeur = new Directeur();
            directeur.setLogin("directeur");
            directeur.setMotDePasse(passwordEncoder.encode("directeur123"));
            directeur.setNom("Kouassi");
            directeur.setPrenom("Directeur");
            directeur.setRole(Role.DIRECTEUR);
            directeur.setActif(true);
            utilisateurRepository.save(directeur);

            // ================================
            // Greffier
            // ================================
            Greffier greffier = new Greffier();
            greffier.setLogin("greffier");
            greffier.setMotDePasse(passwordEncoder.encode("greffier123"));
            greffier.setNom("Adja");
            greffier.setPrenom("Greffier");
            greffier.setRole(Role.GREFFIER);
            greffier.setActif(true);
            utilisateurRepository.save(greffier);

            // ================================
            // Agent
            // ================================
            Agent agent = new Agent();
            agent.setLogin("agent");
            agent.setMotDePasse(passwordEncoder.encode("agent123"));
            agent.setNom("Gouni");
            agent.setPrenom("Agent");
            agent.setRole(Role.AGENT);
            agent.setActif(true);
            utilisateurRepository.save(agent);

            // ================================
            // Responsable Visite
            // ================================
            ResponsableVisite responsable = new ResponsableVisite();
            responsable.setLogin("responsable");
            responsable.setMotDePasse(passwordEncoder.encode("responsable123"));
            responsable.setNom("Yassoi");
            responsable.setPrenom("Responsable");
            responsable.setRole(Role.RESPONSABLE_VISITE);
            responsable.setActif(true);
            utilisateurRepository.save(responsable);

            System.out.println("✅ Utilisateurs de test créés avec succès !");
            System.out.println("================================");
            System.out.println("directeur   / directeur123");
            System.out.println("greffier    / greffier123");
            System.out.println("agent       / agent123");
            System.out.println("responsable / responsable123");
            System.out.println("================================");
        }
    }
}
