package net.centroweg.gerenciamentocompras.integration;

import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.RoleRepository;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.LogIn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String EMAIL = "maria@gmail.com";
    private static final String CPF = "52998224725";
    private static final String PASSWORD = "Senha@123";

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        roleRepository.deleteAll();

        Role role = new Role("ADMIN");
        role = roleRepository.save(role);

        User user = new User();
        user.setName("Maria Eduarda");
        user.setEmail(EMAIL);
        user.setCpf(CPF);
        user.setPassword(passwordEncoder.encode(PASSWORD));
        user.setExtensionNumber("1234");
        user.setActive(true);
        user.setRole(role);

        userRepository.save(user);
    }

    @Test
    @DisplayName("[Integration] Should authenticate user with valid email and password")
    void shouldAuthenticateWithValidEmailAndPassword() throws Exception {
        LogIn loginDto = new LogIn(EMAIL, PASSWORD);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Usuário autenticado com sucesso!"));
    }

    @Test
    @DisplayName("[Integration] Should authenticate user with valid CPF and password")
    void shouldAuthenticateWithValidCpfAndPassword() throws Exception {
        LogIn loginDto = new LogIn(CPF, PASSWORD);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Usuário autenticado com sucesso!"));
    }

    @Test
    @DisplayName("[Integration] Should return 401 error with incorrect password")
    void shouldReturnUnauthorizedWithIncorrectPassword() throws Exception {
        LogIn loginDto = new LogIn(EMAIL, "WrongPassword@123");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("[Integration] Should return error with nonexistent user")
    void shouldReturnUnauthorizedWithNonexistentUser() throws Exception {
        LogIn loginDto = new LogIn("nonexistent@gmail.com", PASSWORD);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("[Integration] Should return 400 error when userName is blank")
    void shouldReturnBadRequestWhenUserNameIsBlank() throws Exception {
        LogIn loginDto = new LogIn("", PASSWORD);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("[Integration] Should return 400 error when password is blank")
    void shouldReturnBadRequestWhenPasswordIsBlank() throws Exception {
        LogIn loginDto = new LogIn(EMAIL, "");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isBadRequest());
    }
}
