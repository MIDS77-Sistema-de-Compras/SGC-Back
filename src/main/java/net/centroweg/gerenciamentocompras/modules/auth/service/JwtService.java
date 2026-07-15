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
        return baseTokenBuilder(principal).compact();
    }

    /**
     * Gera um token de impersonação: o token pertence ao usuário-alvo (subject,
     * role, nome), mas carrega claims identificando o administrador que iniciou
     * a impersonação — usadas pela auditoria e pelo retorno à conta original.
     *
     * @param target     usuário cuja conta será usada
     * @param adminEmail e-mail do administrador que está impersonando
     * @param adminName  nome do administrador que está impersonando
     */
    public String generateImpersonationToken(UserPrincipal target, String adminEmail, String adminName) {
        return baseTokenBuilder(target)
                .claim("impersonatedBy", adminEmail)
                .claim("impersonatedByName", adminName)
                .compact();
    }

    private io.jsonwebtoken.JwtBuilder baseTokenBuilder(UserPrincipal principal) {
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
                .signWith(getSigningKey(), SignatureAlgorithm.HS256);
    }

    public String validateToken(String token) {
        Claims claims = parseClaims(token);
        return claims != null ? claims.getSubject() : null;
    }

    /**
     * Retorna as claims do token, ou {@code null} se o token for inválido.
     */
    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }
}
