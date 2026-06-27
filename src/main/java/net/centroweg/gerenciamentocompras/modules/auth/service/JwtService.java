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

/**
 * Serviço responsável pela geração e validação de tokens JWT utilizados na autenticação.
 */
@Service
public class JwtService {

    /**
     * Chave secreta utilizada para assinar e validar tokens JWT.
     */
    @Value("${security.jwt.secret-key}")
    private String secret;

    /**
     * Tempo de expiração do token, em horas.
     */
    @Value("${security.jwt.expiration-time}")
    private int expirationHours;

    /**
     * Constrói a chave criptográfica utilizada para assinar e validar os tokens JWT.
     * @return chave criptográfica baseada na chave secreta configurada.
     */
    private Key getSigningKey() {
        byte[] keyBytes = this.secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Gera um token JWT para o usuário autenticado.
     * @param principal usuário autenticado.
     * @return token JWT assinado.
     */
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

    /**
     * Valida um token JWT.
     * @param token token JWT recebido na requisição.
     * @return identificador do usuário caso o token seja válido.
     */
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
