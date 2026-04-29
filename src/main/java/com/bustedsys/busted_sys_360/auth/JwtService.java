package com.bustedsys.busted_sys_360.auth;

import com.bustedsys.busted_sys_360.utilisateur.Utilisateur;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    // ================================
    // Générer un token
    // ================================
    public String genererToken(Utilisateur utilisateur) {
        return Jwts.builder()
                .subject(utilisateur.getLogin())
                .claim("role", utilisateur.getRole().name())
                .claim("nom", utilisateur.getNom())
                .claim("prenom", utilisateur.getPrenom())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getCle())
                .compact();
    }

    // ================================
    // Extraire le login du token
    // ================================
    public String extraireLogin(String token) {
        return extraireClaims(token).getSubject();
    }

    // ================================
    // Extraire le rôle du token
    // ================================
    public String extraireRole(String token) {
        return extraireClaims(token).get("role", String.class);
    }

    // ================================
    // Vérifier si le token est valide
    // ================================
    public boolean estValide(String token, Utilisateur utilisateur) {
        String login = extraireLogin(token);
        return login.equals(utilisateur.getLogin()) && !estExpire(token);
    }

    // ================================
    // Méthodes privées
    // ================================
    private Claims extraireClaims(String token) {
        return Jwts.parser()
                .verifyWith(getCle())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean estExpire(String token) {
        return extraireClaims(token)
                .getExpiration()
                .before(new Date());
    }

    private SecretKey getCle() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}