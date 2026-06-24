package net.centroweg.gerenciamentocompras.config.security;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.filter.SecurityFilter;
import org.springframework.core.env.Environment;
import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Classe responsável por fazer a configuração da segurança da API.
 * @see SecurityFilter
 * */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final SecurityFilter securityFilter;
    private final Environment env;

    /**
     * Método que gerencia os filtros de segurança, como CORS, CSRF, autorização para determinados endpoints.
     * @param http parâmetro que representa a requisição do cliente HTTP.
     * @return classe que representa os filtros de segurança para a requisição.
     * */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorizeRequests -> {
                    authorizeRequests
                            .requestMatchers(HttpMethod.POST, "/users", "/auth/**").permitAll();

                    if (Arrays.asList(env.getActiveProfiles()).contains("dev")) {
                        authorizeRequests.requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/swagger-ui.html",
                                "/webjars/**"
                        ).permitAll();
                    }

                    authorizeRequests.anyRequest().authenticated();
                })
                .sessionManagement( session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Método que mostra as configurações do CORS (quem pode fazer a requisição, métodos HTTP permitidos, etc).
     * @return classe que representa a configuração do CORS e local de aplicação.
     * */
    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of(
                "http://localhost:3000"
        ));

        configuration.setAllowedMethods(List.of(
                "POST", "GET", "PUT", "PATCH", "DELETE"
        ));

        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Expõe o {@link AuthenticationManager} como um bean gerenciado pelo Spring.
     *
     * @param authenticationConfiguration configuração de autenticação fornecida automaticamente pelo Spring Security.
     * @return instância do {@link AuthenticationManager} configurado.
     * @throws Exception caso ocorra algum erro durante o processo de obter {@link AuthenticationManager}.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Criptografia de senha.
     * @return classe que representa o decodificador da criptografia.
     * */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
