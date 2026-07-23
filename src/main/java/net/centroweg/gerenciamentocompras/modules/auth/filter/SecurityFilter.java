package net.centroweg.gerenciamentocompras.modules.auth.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.http.HttpHeaders;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import io.jsonwebtoken.Claims;
import net.centroweg.gerenciamentocompras.modules.auth.domain.exception.InvalidTokenException;
import net.centroweg.gerenciamentocompras.modules.auth.service.CustomUserDetailsService;
import net.centroweg.gerenciamentocompras.modules.auth.service.JwtService;
import net.centroweg.gerenciamentocompras.shared.security.ImpersonationDetails;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final HandlerExceptionResolver resolver;

    public SecurityFilter(
            JwtService jwtService,
            CustomUserDetailsService customUserDetailsService,
            @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
        this.resolver = resolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            String token = extractJwt(request);

            if (token != null) {
                String tokenValidated = jwtService.validateToken(token);

                if (tokenValidated == null) {
                    throw new InvalidTokenException("Token inválido");
                }

                UserDetails user = customUserDetailsService.loadUserByUsername(tokenValidated);
                if (!user.isEnabled()) {
                    throw new DisabledException("Usuário inativo");
                }
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                // Token de impersonação: expõe quem é o administrador original
                // para que a auditoria registre quem realmente executou a ação.
                Claims claims = jwtService.parseClaims(token);
                String impersonatedBy = claims != null ? claims.get("impersonatedBy", String.class) : null;
                if (impersonatedBy != null) {
                    authenticationToken.setDetails(new ImpersonationDetails(
                            impersonatedBy,
                            claims.get("impersonatedByName", String.class)
                    ));
                }

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            }

            filterChain.doFilter(request, response);

        } catch (DisabledException e) {
            SecurityContextHolder.clearContext();
            resolver.resolveException(request, response, null, e);
        } catch (AccessDeniedException | AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            resolver.resolveException(request, response, null, e);
        }

    }

    private String extractJwt(HttpServletRequest request) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorization != null
                && authorization.regionMatches(true, 0, "Bearer ", 0, 7)) {

            String token = authorization.substring(7).trim();

            if (!token.isEmpty()) {
                return token;
            }
        }

        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if ("jwt".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }
}
