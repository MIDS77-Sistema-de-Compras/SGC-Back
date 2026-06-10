package net.centroweg.gerenciamentocompras.modules.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.domain.exception.InvalidTokenException;
import net.centroweg.gerenciamentocompras.modules.auth.service.CustomUserDetailsService;
import net.centroweg.gerenciamentocompras.modules.auth.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

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
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            resolver.resolveException(request, response, null, e);
        }

    }

    private String extractJwt(HttpServletRequest request) {
        String jwtHeader = request.getHeader("Authorization");

        if (jwtHeader == null || !jwtHeader.startsWith("Bearer ")) return null;

        return jwtHeader.replace("Bearer", "");
    }
}
