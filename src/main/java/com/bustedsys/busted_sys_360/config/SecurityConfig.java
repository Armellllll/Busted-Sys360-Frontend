package com.bustedsys.busted_sys_360.config;

import com.bustedsys.busted_sys_360.auth.JwtFilter;
import com.bustedsys.busted_sys_360.utilisateur.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final UtilisateurRepository utilisateurRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return login -> utilisateurRepository
                .findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Utilisateur introuvable : " + login
                ));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        // ✅ Seuls login et register sont publics
                        .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()

                        // ❌ /api/auth/me nécessite une authentification (token)
                        // → Ne pas le mettre dans permitAll

                        // Endpoints protégés par rôle
                        .requestMatchers("/api/rapports/**").hasRole("DIRECTEUR")
                        .requestMatchers("/api/journal/**").hasRole("DIRECTEUR")
                        .requestMatchers("/api/detenus/**").hasAnyRole("GREFFIER", "AGENT", "DIRECTEUR")
                        .requestMatchers("/api/cellules/**").hasAnyRole("DIRECTEUR", "GREFFIER", "AGENT")
                        .requestMatchers("/api/infractions/**").hasRole("AGENT")
                        .requestMatchers("/api/medical/**").hasRole("AGENT")
                        .requestMatchers("/api/visites/**").hasAnyRole("RESPONSABLE_VISITE", "DIRECTEUR")

                        // Toute autre requête nécessite une authentification
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}