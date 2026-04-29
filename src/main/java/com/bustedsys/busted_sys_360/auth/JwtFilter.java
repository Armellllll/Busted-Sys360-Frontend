package com.bustedsys.busted_sys_360.auth;

import com.bustedsys.busted_sys_360.utilisateur.Utilisateur;
import com.bustedsys.busted_sys_360.utilisateur.UtilisateurRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UtilisateurRepository utilisateurRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Récupérer le header Authorization
        String authHeader = request.getHeader("Authorization");

        // 2. Vérifier que le header commence par "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extraire le token
        String token = authHeader.substring(7);

        // 4. Extraire le login depuis le token
        String login = jwtService.extraireLogin(token);

        // 5. Vérifier que l'utilisateur n'est pas déjà authentifié
        if (login != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 6. Charger l'utilisateur depuis la base
            Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findByLogin(login);

            if (utilisateurOpt.isPresent()) {
                Utilisateur utilisateur = utilisateurOpt.get();

                // 7. Vérifier que le token est valide
                if (jwtService.estValide(token, utilisateur)) {

                    // 8. Créer l'authentification Spring Security
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    utilisateur,
                                    null,
                                    utilisateur.getAuthorities()
                            );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    // 9. Mettre l'authentification dans le contexte
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }

        // 10. Continuer la chaîne de filtres
        filterChain.doFilter(request, response);
    }
}
