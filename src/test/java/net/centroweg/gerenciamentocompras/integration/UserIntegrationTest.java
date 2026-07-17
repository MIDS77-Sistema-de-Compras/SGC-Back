package net.centroweg.gerenciamentocompras.integration;

import jakarta.persistence.EntityManager;
import jakarta.servlet.http.Cookie;
import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.RoleRepository;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.CreateUser;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.LogIn;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class UserIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    private MockMvc realAuthMockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EntityManager entityManager;

    private static final String CPF_VALIDO = "52998224725";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .defaultRequest(get("/").with(user("actor@teste.com").authorities(
                        new org.springframework.security.core.authority.SimpleGrantedAuthority("ADMIN")
                )))
                .build();
        realAuthMockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        // Cleanup em ordem segura: filhos antes dos pais (FK)
        userRepository.deleteAll();
        roleRepository.deleteAll();
        Role adminRole = roleRepository.save(new Role("ADMIN"));
        roleRepository.save(new Role("COMPRADOR"));
        roleRepository.save(new Role("DOCENTE"));

        User actor = new User(
                "Ator Admin",
                "11144477735",
                "actor@teste.com",
                "Senha@123",
                "0001",
                true
        );
        actor.setRole(adminRole);
        userRepository.save(actor);
        userRepository.flush();
    }

    @AfterEach
    void tearDown() {
        // MockMvc não reverte @Transactional (cada request HTTP tem sua própria transação).
        // Por isso limpamos explicitamente após cada teste para não vazar dados para outros testes da Suite.
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    private Long criarUsuarioEObterIdRetornado() throws Exception {
        CreateUser request = new CreateUser(
                "Admin Teste",
                "admin@sc.senai.br",
                CPF_VALIDO,
                "Senha@123",
                "1234",
                true,
                "COMPRADOR"
        );

        String response = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readTree(response).get("id").asLong();
    }

    private User criarUsuarioAutenticavel(boolean active) {
        Role docenteRole = roleRepository.findByNameIgnoreCase("DOCENTE").orElseThrow();
        User user = new User(
                "Usuário Autenticável",
                "cpf-hash-autenticavel",
                "autenticavel@teste.com",
                passwordEncoder.encode("Senha@123"),
                "4321",
                active
        );
        user.setRole(docenteRole);
        User saved = userRepository.save(user);
        userRepository.flush();
        return saved;
    }

    @Test
    @WithMockUser(username = "actor@teste.com", authorities = "ADMIN")
    @DisplayName("[Integração] Deve criar um usuário com sucesso")
    void deveCriarUsuarioComSucesso() throws Exception {
        CreateUser request = new CreateUser(
                "Admin Teste",
                "admin@sc.senai.br",
                CPF_VALIDO,
                "Senha@123",
                "1234",
                true,
                "COMPRADOR"
        );

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Admin Teste"))
                .andExpect(jsonPath("$.email").value("admin@sc.senai.br"));

        assertEquals(2, userRepository.count());
    }

    @Test
    @WithMockUser(username = "actor@teste.com", authorities = "ADMIN")
    @DisplayName("[Integração] Deve rejeitar criação de usuário com e-mail não institucional")
    void deveRejeitarEmailNaoInstitucionalNaCriacao() throws Exception {
        CreateUser request = new CreateUser(
                "Admin Teste",
                "admin@gmail.com",
                CPF_VALIDO,
                "Senha@123",
                "1234",
                true,
                "COMPRADOR"
        );

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        // Só o ator do setUp deve existir — nenhum usuário novo foi criado
        assertEquals(1, userRepository.count());
    }

    @Test
    @WithMockUser(username = "actor@teste.com", authorities = "ADMIN")
    @DisplayName("[Integração] Deve rejeitar atualização de usuário para e-mail não institucional")
    void deveRejeitarEmailNaoInstitucionalNaAtualizacao() throws Exception {
        Long id = criarUsuarioEObterIdRetornado();

        CreateUser updateRequest = new CreateUser(
                "Admin Atualizado",
                "atualizado@gmail.com",
                CPF_VALIDO,
                "Senha@123",
                "9999",
                true,
                "COMPRADOR"
        );

        mockMvc.perform(put("/users/userId/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "actor@teste.com", authorities = "ADMIN")
    @DisplayName("[Integração] Deve listar todos os usuários")
    void deveListarUsuarios() throws Exception {
        criarUsuarioEObterIdRetornado();

        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    @WithMockUser(username = "actor@teste.com", authorities = "ADMIN")
    @DisplayName("[Integração] Deve buscar usuário por ID")
    void deveBuscarUsuarioPorId() throws Exception {
        Long id = criarUsuarioEObterIdRetornado();

        mockMvc.perform(get("/users/userId/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Admin Teste"));
    }

    @Test
    @WithMockUser(username = "actor@teste.com", authorities = "ADMIN")
    @DisplayName("[Integração] Deve retornar 404 ao buscar usuário com ID inexistente")
    void deveRetornarNotFoundParaIdInexistente() throws Exception {
        mockMvc.perform(get("/users/userId/{id}", 9999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "actor@teste.com", authorities = "ADMIN")
    @DisplayName("[Integração] Deve buscar usuários por nome")
    void deveBuscarUsuarioPorNome() throws Exception {
        criarUsuarioEObterIdRetornado();

        mockMvc.perform(get("/users/userName/{name}", "Admin Teste")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Admin Teste"));
    }

    @Test
    @WithMockUser(username = "actor@teste.com", authorities = "ADMIN")
    @DisplayName("[Integração] Deve atualizar usuário com sucesso")
    void deveAtualizarUsuario() throws Exception {
        Long id = criarUsuarioEObterIdRetornado();

        CreateUser updateRequest = new CreateUser(
                "Admin Atualizado",
                "atualizado@fiesc.com.br",
                CPF_VALIDO,
                "Senha@123",
                "9999",
                true,
                "COMPRADOR"
        );

        mockMvc.perform(put("/users/userId/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Admin Atualizado"))
                .andExpect(jsonPath("$.email").value("atualizado@fiesc.com.br"));
    }

    @Test
    @WithMockUser(username = "actor@teste.com", authorities = "ADMIN")
    @DisplayName("[Integração] Deve deletar usuário com sucesso")
    void deveDeletarUsuario() throws Exception {
        Long id = criarUsuarioEObterIdRetornado();

        mockMvc.perform(delete("/users/userId/{id}", id))
                .andExpect(status().isNoContent());

        // O delete é lógico (soft delete): o usuário permanece, porém inativo
        User deletado = userRepository.findById(id).orElseThrow();
        assertFalse(deletado.getActive());
    }

    @Test
    @DisplayName("[Integração] Deve buscar o usuário logado com sucesso")
    void deveBuscarUsuarioLogado() throws Exception {
        Long id = criarUsuarioEObterIdRetornado();

        User userEntity = userRepository.findById(id).orElseThrow();
        UserPrincipal userPrincipal = new UserPrincipal(userEntity);

        mockMvc.perform(get("/users/me")
                        .with(user(userPrincipal))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Admin Teste"))
                .andExpect(jsonPath("$.email").value("admin@sc.senai.br"));
    }

    @Test
    @WithMockUser(username = "actor@teste.com", authorities = "ADMIN")
    @DisplayName("[Integração] Deve desativar um usuário pelo PATCH")
    void shouldDeactivateUserWithPatch() throws Exception {
        User target = criarUsuarioAutenticavel(true);

        mockMvc.perform(patch("/users/userId/{id}/active", target.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"active\":false}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(false));

        entityManager.clear();
        User persisted = userRepository.findById(target.getId()).orElseThrow();
        assertFalse(persisted.getActive());
        assertEquals(2, userRepository.count());
    }

    @Test
    @WithMockUser(username = "actor@teste.com", authorities = "ADMIN")
    @DisplayName("[Integração] Deve ativar um usuário pelo PATCH")
    void shouldActivateUserWithPatch() throws Exception {
        User target = criarUsuarioAutenticavel(false);

        mockMvc.perform(patch("/users/userId/{id}/active", target.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"active\":true}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(true));

        entityManager.clear();
        assertTrue(userRepository.findById(target.getId()).orElseThrow().getActive());
    }

    @Test
    @WithMockUser(username = "actor@teste.com", authorities = "ADMIN")
    @DisplayName("[Integração] Deve retornar 400 quando active não for informado")
    void shouldRejectActivationRequestWithoutActive() throws Exception {
        User target = criarUsuarioAutenticavel(true);

        mockMvc.perform(patch("/users/userId/{id}/active", target.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.active")
                        .value("O estado de ativação do usuário é obrigatório"));
    }

    @Test
    @WithMockUser(username = "actor@teste.com", authorities = "ADMIN")
    @DisplayName("[Integração] Deve retornar 404 ao alterar usuário inexistente")
    void shouldReturnNotFoundWhenChangingMissingUser() throws Exception {
        mockMvc.perform(patch("/users/userId/{id}/active", 999999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"active\":false}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("[Integração] Deve retornar 403 para usuário sem permissão")
    void shouldRejectActivationChangeWithoutPermission() throws Exception {
        User target = criarUsuarioAutenticavel(true);

        realAuthMockMvc.perform(patch("/users/userId/{id}/active", target.getId())
                        .with(user("docente@teste.com").authorities(
                                new org.springframework.security.core.authority.SimpleGrantedAuthority("DOCENTE")
                        ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"active\":false}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("[Integração] Usuário inativo não deve realizar login nem receber cookie JWT")
    void shouldRejectLoginForInactiveUser() throws Exception {
        criarUsuarioAutenticavel(false);
        LogIn login = new LogIn("autenticavel@teste.com", "Senha@123");

        var result = realAuthMockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isUnauthorized())
                .andReturn();

        assertNull(result.getResponse().getCookie("jwt"));
    }

    @Test
    @DisplayName("[Integração] Usuário deve realizar login depois de ser reativado")
    void shouldLoginAfterReactivation() throws Exception {
        User target = criarUsuarioAutenticavel(false);

        mockMvc.perform(patch("/users/userId/{id}/active", target.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"active\":true}"))
                .andExpect(status().isOk());

        LogIn login = new LogIn("autenticavel@teste.com", "Senha@123");
        var result = realAuthMockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andReturn();

        assertNotNull(result.getResponse().getCookie("jwt"));
        assertFalse(objectMapper.readTree(result.getResponse().getContentAsString())
                .get("text").asText().isBlank());
    }

    @Test
    @DisplayName("[Integração] JWT emitido antes da desativação deve ser rejeitado")
    void shouldRejectPreviouslyIssuedJwtAfterDeactivation() throws Exception {
        User target = criarUsuarioAutenticavel(true);
        LogIn login = new LogIn("autenticavel@teste.com", "Senha@123");
        var loginResult = realAuthMockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andReturn();
        Cookie jwtCookie = loginResult.getResponse().getCookie("jwt");
        assertNotNull(jwtCookie);

        mockMvc.perform(patch("/users/userId/{id}/active", target.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"active\":false}"))
                .andExpect(status().isOk());

        realAuthMockMvc.perform(get("/users/me").cookie(jwtCookie))
                .andExpect(status().isUnauthorized());
    }
}
