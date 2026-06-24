package net.centroweg.gerenciamentocompras.modules.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secret;

    @Value("${security.jwt.expiration-time}")
    private int expirationHours;

    private Key getSigningKey() {
        byte[] keyBytes = this.secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(UserPrincipal principal) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + ((long) expirationHours * 60 * 60 * 1000));

        String role = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("");

        String cpf = principal.getCpf();

        return Jwts.builder()
                .setIssuer("sgs-api")
                .setSubject(principal.getUsername())
                .claim("role", role)
                .claim("nome", principal.getName())
                .claim("cpf", cpf)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }
}
