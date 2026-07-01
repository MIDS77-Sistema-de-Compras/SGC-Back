package net.centroweg.gerenciamentocompras.integration;

import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.RoleRepository;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.CreateUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private static final String CPF_VALIDO = "52998224725";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .defaultRequest(get("/").with(user("test-admin").roles("ADMIN")))
                .build();

        // Cleanup em ordem segura: filhos antes dos pais (FK)
        userRepository.deleteAll();
        roleRepository.deleteAll();
        roleRepository.save(new Role("COMPRADOR"));
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
                "admin@teste.com",
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

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("[Integração] Deve criar um usuário com sucesso")
    void deveCriarUsuarioComSucesso() throws Exception {
        CreateUser request = new CreateUser(
                "Admin Teste",
                "admin@teste.com",
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
                .andExpect(jsonPath("$.email").value("admin@teste.com"));

        assertEquals(1, userRepository.count());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("[Integração] Deve listar todos os usuários")
    void deveListarUsuarios() throws Exception {
        criarUsuarioEObterIdRetornado();

        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].name").value("Admin Teste"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
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
    @WithMockUser(roles = "ADMIN")
    @DisplayName("[Integração] Deve retornar 404 ao buscar usuário com ID inexistente")
    void deveRetornarNotFoundParaIdInexistente() throws Exception {
        mockMvc.perform(get("/users/userId/{id}", 9999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
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
    @WithMockUser(roles = "ADMIN")
    @DisplayName("[Integração] Deve atualizar usuário com sucesso")
    void deveAtualizarUsuario() throws Exception {
        Long id = criarUsuarioEObterIdRetornado();

        CreateUser updateRequest = new CreateUser(
                "Admin Atualizado",
                "atualizado@teste.com",
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
                .andExpect(jsonPath("$.email").value("atualizado@teste.com"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("[Integração] Deve deletar usuário com sucesso")
    void deveDeletarUsuario() throws Exception {
        Long id = criarUsuarioEObterIdRetornado();

        mockMvc.perform(delete("/users/userId/{id}", id))
                .andExpect(status().isNoContent());

        assertEquals(0, userRepository.count());
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
                .andExpect(jsonPath("$.email").value("admin@teste.com"));
    }
}
