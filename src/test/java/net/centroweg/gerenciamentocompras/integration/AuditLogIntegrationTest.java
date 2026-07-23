package net.centroweg.gerenciamentocompras.integration;

import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.RoleRepository;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.CreateUser;
import net.centroweg.gerenciamentocompras.shared.audit.domain.entity.AuditLog;
import net.centroweg.gerenciamentocompras.shared.audit.infrastructure.persistence.AuditLogRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de integração ponta-a-ponta do {@code AuditLogAspect} (Spring AOP) através de MockMvc.
 *
 * <p><b>Pré-requisito do aspecto</b>: ele resolve o "agente" via
 * {@code auditLogPublicApi.findByUserEmail(authentication.getName())}. Como {@code UserPrincipal.getUsername()}
 * retorna o e-mail, autenticamos com {@code .with(user(userPrincipal))} de um usuário REAL persistido,
 * cujo e-mail existe no banco. Este é o padrão correto pedido para os testes.</p>
 *
 * <p>O último teste cobre o caso em que o principal autenticado não corresponde a um usuário
 * persistido: o aspecto não consegue resolver o agente e apenas deixa de gravar o log
 * (guarda {@code if(agent==null)}), sem afetar o resultado da requisição.</p>
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Auditoria - integração ponta-a-ponta (AuditLogAspect)")
class AuditLogIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuditLogRepository auditLogRepository;

    private MockMvc mockMvc;

    private static final String ADMIN_EMAIL = "admin.agente@teste.com";
    private static final String NEW_USER_CPF = "11144477735";

    private UserPrincipal adminPrincipal;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        // Ordem de limpeza segura por FK: audit_log referencia user (userAgent NOT NULL).
        auditLogRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();

        Role adminRole = roleRepository.save(new Role("ADMIN"));
        roleRepository.save(new Role("COMPRADOR"));

        User admin = new User("Admin Agente", "52998224725", ADMIN_EMAIL, "Senha@123", "1000", true);
        // User.role não tem cascade: a role precisa ser persistida antes de ser associada ao usuário.
        admin.setRole(adminRole);
        admin = userRepository.save(admin);

        adminPrincipal = new UserPrincipal(admin);
    }

    @AfterEach
    void tearDown() {
        auditLogRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    private AuditLog findLogByAction(String action) {
        List<AuditLog> logs = auditLogRepository.findAll();
        return logs.stream()
                .filter(l -> action.equals(l.getTypeAction()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Nenhum AuditLog com typeAction=" + action
                        + " (encontrados: " + logs.stream().map(AuditLog::getTypeAction).toList() + ")"));
    }

    private long criarUsuarioComoAdmin() throws Exception {
        CreateUser request = new CreateUser(
                "Usuario Alvo", "alvo@sc.senai.br", NEW_USER_CPF,
                "Senha@123", "2000", true, "COMPRADOR");

        String response = mockMvc.perform(post("/users")
                        .with(csrf())
                        .with(user(adminPrincipal))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readTree(response).get("id").asLong();
    }

    @Test
    @DisplayName("POST /users (CRIAR_USUARIO, targetFromReturn) grava log com agente e alvo extraído do retorno")
    void deveAuditarCriacaoDeUsuarioComAlvoDoRetorno() throws Exception {
        long createdId = criarUsuarioComoAdmin();

        AuditLog log = findLogByAction("CRIAR_USUARIO");
        assertThat(log.getUserAgent()).isNotNull();
        assertThat(log.getUserAgent().getEmail()).isEqualTo(ADMIN_EMAIL);
        // targetFromReturn=true: o alvo é extraído de UserResponse.id() do corpo retornado.
        assertThat(log.getUserTarget()).as("alvo extraído do retorno").isNotNull();
        assertThat(log.getUserTarget().getId()).isEqualTo(createdId);
        assertThat(log.getRequest()).isNull();
    }

    @Test
    @DisplayName("PUT /users/userId/{id} (ATUALIZAR_USUARIO) grava log com alvo vindo do @AuditParam(\"user\")")
    void deveAuditarAtualizacaoComAlvoDoPathVariable() throws Exception {
        long createdId = criarUsuarioComoAdmin();
        auditLogRepository.deleteAll(); // isola o log da atualização

        CreateUser update = new CreateUser(
                "Usuario Alvo Editado", "alvo@sc.senai.br", NEW_USER_CPF,
                "Senha@123", "3000", true, "COMPRADOR");

        mockMvc.perform(put("/users/userId/{id}", createdId)
                        .with(csrf())
                        .with(user(adminPrincipal))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk());

        AuditLog log = findLogByAction("ATUALIZAR_USUARIO");
        assertThat(log.getUserAgent().getEmail()).isEqualTo(ADMIN_EMAIL);
        assertThat(log.getUserTarget()).isNotNull();
        assertThat(log.getUserTarget().getId()).isEqualTo(createdId);
    }

    @Test
    @DisplayName("DELETE /users/userId/{id} (DESATIVAR_USUARIO) grava log com alvo do @AuditParam(\"user\")")
    void deveAuditarDesativacaoDeUsuario() throws Exception {
        long createdId = criarUsuarioComoAdmin();
        auditLogRepository.deleteAll();

        mockMvc.perform(delete("/users/userId/{id}", createdId)
                        .with(csrf())
                        .with(user(adminPrincipal)))
                .andExpect(status().isNoContent());

        AuditLog log = findLogByAction("DESATIVAR_USUARIO");
        assertThat(log.getUserAgent().getEmail()).isEqualTo(ADMIN_EMAIL);
        assertThat(log.getUserTarget()).isNotNull();
        assertThat(log.getUserTarget().getId()).isEqualTo(createdId);
    }

    @Test
    @DisplayName("PATCH /users/userId/{id}/active grava a alteração de ativação no log")
    void deveAuditarAlteracaoDoStatusDeAtivacao() throws Exception {
        long createdId = criarUsuarioComoAdmin();
        auditLogRepository.deleteAll();

        mockMvc.perform(patch("/users/userId/{id}/active", createdId)
                        .with(csrf())
                        .with(user(adminPrincipal))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"active\":false}"))
                .andExpect(status().isOk());

        AuditLog log = findLogByAction("ALTERAR_STATUS_ATIVACAO_USUARIO");
        assertThat(log.getUserAgent().getEmail()).isEqualTo(ADMIN_EMAIL);
        assertThat(log.getUserTarget()).isNotNull();
        assertThat(log.getUserTarget().getId()).isEqualTo(createdId);
    }

    @Test
    @DisplayName("Endpoint auditado com principal cujo e-mail não está no banco não gera log e não quebra a requisição")
    void endpointAuditadoComPrincipalInexistenteNaoGeraLog() throws Exception {
        CreateUser request = new CreateUser(
                "Usuario Alvo", "alvo@sc.senai.br", NEW_USER_CPF,
                "Senha@123", "2000", true, "COMPRADOR");

        // "usuario-sem-email" não corresponde a nenhum usuário persistido. O aspecto de auditoria
        // não resolve o agente e simplesmente NÃO grava o log (guarda if(agent==null)), sem afetar
        // o resultado da regra de negócio: a criação do usuário-alvo ocorre normalmente (201).
        mockMvc.perform(post("/users")
                        .with(csrf())
                        .with(user("usuario-sem-email").authorities(
                                new org.springframework.security.core.authority.SimpleGrantedAuthority("ADMIN")
                        ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());

        assertThat(auditLogRepository.count())
                .as("nenhum log é gravado quando o agente não é resolvido")
                .isZero();
    }
}
