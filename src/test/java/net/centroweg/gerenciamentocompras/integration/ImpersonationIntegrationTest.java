package net.centroweg.gerenciamentocompras.integration;

import jakarta.servlet.http.Cookie;
import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.auth.service.JwtService;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.RoleRepository;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.shared.audit.domain.entity.AuditLog;
import net.centroweg.gerenciamentocompras.shared.audit.infrastructure.persistence.AuditLogRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
class ImpersonationIntegrationTest {

    @Autowired private WebApplicationContext context;
    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private AuditLogRepository auditLogRepository;
    @Autowired private JwtService jwtService;

    private MockMvc mockMvc;
    private User admin;
    private User docente;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        cleanDatabase();

        admin = saveUser("Admin Teste", "93541134780", "admin@teste.com", "ADMIN");
        docente = saveUser("Docente Teste", "52998224725", "docente@teste.com", "DOCENTE");
    }

    @AfterEach
    void tearDown() {
        cleanDatabase();
    }

    @Test
    @DisplayName("[Integracao] Admin deve logar na conta de outro usuario e receber token de impersonacao")
    void shouldImpersonateUser() throws Exception {
        MvcResult result = mockMvc.perform(post("/auth/impersonate/{userId}", docente.getId())
                        .with(user(principalOf(admin))))
                .andExpect(status().isOk())
                .andReturn();

        String token = result.getResponse().getCookie("jwt").getValue();
        var claims = jwtService.parseClaims(token);

        assertThat(claims.getSubject()).isEqualTo("docente@teste.com");
        assertThat(claims.get("role", String.class)).isEqualTo("DOCENTE");
        assertThat(claims.get("impersonatedBy", String.class)).isEqualTo("admin@teste.com");
        assertThat(claims.get("impersonatedByName", String.class)).isEqualTo("Admin Teste");

        List<AuditLog> logs = auditLogRepository.findAll();
        assertThat(logs)
                .anySatisfy(log -> {
                    assertThat(log.getTypeAction()).isEqualTo("LOGAR_COMO_USUARIO");
                    assertThat(log.getUserAgent().getId()).isEqualTo(admin.getId());
                    assertThat(log.getUserTarget().getId()).isEqualTo(docente.getId());
                });
    }

    @Test
    @DisplayName("[Integracao] Status deve indicar impersonacao ativa quando o token e de impersonacao")
    void shouldReportImpersonationStatus() throws Exception {
        String token = impersonationToken();

        mockMvc.perform(get("/auth/impersonate/status")
                        .cookie(new Cookie("jwt", token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.impersonating").value(true))
                .andExpect(jsonPath("$.adminName").value("Admin Teste"))
                .andExpect(jsonPath("$.userName").value("Docente Teste"));
    }

    @Test
    @DisplayName("[Integracao] Acao auditada durante impersonacao deve registrar descricao com o admin")
    void shouldAuditActionsWithImpersonationDescription() throws Exception {
        String token = impersonationToken();

        // O "voltar para a conta do admin" é uma ação auditada executada
        // enquanto a sessão ainda é do usuário impersonado.
        mockMvc.perform(post("/auth/impersonate/stop")
                        .cookie(new Cookie("jwt", token)))
                .andExpect(status().isOk());

        List<AuditLog> logs = auditLogRepository.findAll();
        assertThat(logs)
                .anySatisfy(log -> {
                    assertThat(log.getTypeAction()).isEqualTo("VOLTAR_PARA_CONTA_ADMIN");
                    assertThat(log.getUserAgent().getId()).isEqualTo(docente.getId());
                    assertThat(log.getDescription())
                            .contains("Admin Teste")
                            .contains("Docente Teste");
                });
    }

    @Test
    @DisplayName("[Integracao] Voltar para a conta do admin deve devolver token sem impersonacao")
    void shouldStopImpersonation() throws Exception {
        String token = impersonationToken();

        MvcResult result = mockMvc.perform(post("/auth/impersonate/stop")
                        .cookie(new Cookie("jwt", token)))
                .andExpect(status().isOk())
                .andReturn();

        String newToken = result.getResponse().getCookie("jwt").getValue();
        var claims = jwtService.parseClaims(newToken);

        assertThat(claims.getSubject()).isEqualTo("admin@teste.com");
        assertThat(claims.get("role", String.class)).isEqualTo("ADMIN");
        assertThat(claims.get("impersonatedBy", String.class)).isNull();
    }

    @Test
    @DisplayName("[Integracao] Usuario comum nao pode impersonar")
    void shouldForbidNonAdminImpersonation() throws Exception {
        mockMvc.perform(post("/auth/impersonate/{userId}", admin.getId())
                        .with(user(principalOf(docente))))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("[Integracao] Admin nao pode impersonar outro administrador")
    void shouldForbidImpersonatingAnotherAdmin() throws Exception {
        User otherAdmin = saveUser("Outro Admin", "11144477735", "outro.admin@teste.com", "ADMIN");

        mockMvc.perform(post("/auth/impersonate/{userId}", otherAdmin.getId())
                        .with(user(principalOf(admin))))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("[Integracao] Sessao impersonada assume as permissoes do usuario (docente nao impersona)")
    void shouldRestrictImpersonatedSessionToUserPermissions() throws Exception {
        String token = impersonationToken();

        mockMvc.perform(post("/auth/impersonate/{userId}", admin.getId())
                        .cookie(new Cookie("jwt", token)))
                .andExpect(status().isForbidden());
    }

    private String impersonationToken() {
        return jwtService.generateImpersonationToken(
                new UserPrincipal(docente), admin.getEmail(), admin.getName());
    }

    private User saveUser(String name, String cpf, String email, String roleName) {
        User user = new User(name, cpf, email, "Senha@123", "1234", true);
        user.setRole(roleRepository.save(new Role(roleName)));
        return userRepository.save(user);
    }

    private UserPrincipal principalOf(User user) {
        return new UserPrincipal(user);
    }

    private void cleanDatabase() {
        auditLogRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }
}
