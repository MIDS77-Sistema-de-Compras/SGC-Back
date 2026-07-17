package net.centroweg.gerenciamentocompras.config.security;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.filter.SecurityFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Classe responsável por fazer a configuração da segurança da API
 * @author hugo_paim
 * @version 0.1
 * */

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final SecurityFilter securityFilter;
    private final Environment env;

    /**
     * Método que gerencia os filtros de segurança, como CORS, CSRF, autorização para determinados endpoints
     * @param http parâmetro que representa a requisição do cliente HTTP
     * @return Classe que representa os filtros de segurança para a requisição
     * */
    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorizeRequests -> {
                    authorizeRequests
                            .requestMatchers("/auth/**").permitAll();

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
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                response.sendError(HttpServletResponse.SC_FORBIDDEN))
                )
                .sessionManagement( session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Cadeia de segurança dedicada ao endpoint de métricas ({@code /actuator/**}), usado pelo
     * agente de scraping do Grafana/Prometheus. Fica isolada da cadeia principal (JWT via cookie)
     * e usa Basic Auth com credencial própria, configurada via variáveis de ambiente.
     * O {@link UserDetailsService} da métrica é construído localmente (não é {@code @Bean}) para
     * não virar um segundo bean desse tipo no contexto — isso faz o Spring Security "desistir" de
     * montar o {@code AuthenticationManager} global usado pelo login normal (ver {@code authenticationManager()}).
     * @param http parâmetro que representa a requisição do cliente HTTP
     * @return Classe que representa os filtros de segurança para a requisição
     * */
    @Bean
    @Order(1)
    public SecurityFilterChain actuatorSecurityFilterChain(
            HttpSecurity http,
            PasswordEncoder passwordEncoder,
            @Value("${actuator.metrics.username}") String metricsUsername,
            @Value("${actuator.metrics.password}") String metricsPassword
    ) throws Exception {
        UserDetails metricsUser = User.builder()
                .username(metricsUsername)
                .password(passwordEncoder.encode(metricsPassword))
                .roles("METRICS")
                .build();
        UserDetailsService actuatorUserDetailsService = new InMemoryUserDetailsManager(metricsUser);

        DaoAuthenticationProvider actuatorAuthenticationProvider = new DaoAuthenticationProvider(actuatorUserDetailsService);
        actuatorAuthenticationProvider.setPasswordEncoder(passwordEncoder);

        return http
                .securityMatcher("/actuator/**")
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests.anyRequest().hasRole("METRICS"))
                .authenticationManager(new ProviderManager(actuatorAuthenticationProvider))
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    /**
     * Método que mostra as configurações do CORS (quem pode fazer a requisição, métodos HTTP permitidos, etc)
     * @return Classe que representa a configuração do CORS e local de aplicação
     * */

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "http://localhost:3001",
                "http://localhost:3002",
                "https://localhost:3001",
                "https://sgc-front-nine.vercel.app"
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

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Criptografia de senha
     * @return Classe que representa o decodificador da criptografia
     * */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
