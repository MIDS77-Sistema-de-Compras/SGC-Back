package net.centroweg.gerenciamentocompras.integration;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;
import org.junit.jupiter.api.AfterEach;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.Cookie;
import net.centroweg.gerenciamentocompras.config.security.CpfHasher;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.RoleRepository;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.LogIn;
import tools.jackson.databind.ObjectMapper;

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

    @Autowired
    private CpfHasher cpfHasher;

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
        user.setCpf(cpfHasher.hash(CPF));
        user.setPassword(passwordEncoder.encode(PASSWORD));
        user.setExtensionNumber("1234");
        user.setActive(true);
        user.setRole(role);

        userRepository.save(user);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    @DisplayName("[Integration] Should authenticate user with valid email and return JWT token in cookie")
    void shouldAuthenticateWithValidEmailAndPassword() throws Exception {
        LogIn loginDto = new LogIn(EMAIL, PASSWORD);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(cookie().value("jwt", notNullValue()))
                .andExpect(cookie().value("jwt", startsWith("eyJ")));
    }

    @Test
    @DisplayName("[Integration] Should authenticate user with valid CPF and return JWT token in cookie")
    void shouldAuthenticateWithValidCpfAndPassword() throws Exception {
        LogIn loginDto = new LogIn(CPF, PASSWORD);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(cookie().value("jwt", notNullValue()))
                .andExpect(cookie().value("jwt", startsWith("eyJ")));
    }

    @Test
    @DisplayName("[Integration] Should allow access to protected route with valid JWT token")
    void shouldAllowAccessToProtectedRouteWithValidJwtToken() throws Exception {
        LogIn loginDto = new LogIn(EMAIL, PASSWORD);

        Cookie jwtCookie = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getCookie("jwt");

        mockMvc.perform(get("/users")
                        .cookie(jwtCookie))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("[Integration] Should deny access to protected route with invalid JWT token")
    void shouldDenyAccessToProtectedRouteWithInvalidJwtToken() throws Exception {
        mockMvc.perform(get("/users")
                        .cookie(new Cookie("jwt", "invalidtoken")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("[Integration] Should deny access to protected route without JWT token")
    void shouldDenyAccessToProtectedRouteWithoutJwtToken() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isForbidden());
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

    @Test
    @DisplayName("[Integration] Should block authentication and return unauthorized when user is inactive (by Email)")
    void shouldBlockAuthenticationWhenUserIsInactiveByEmail() throws Exception {
        User user = userRepository.findByEmail(EMAIL)
                .orElseThrow(() -> new AssertionError("User from setUp not found"));
        user.setActive(false);
        userRepository.save(user);

        LogIn loginDto = new LogIn(EMAIL, PASSWORD);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("[Integration] Should block authentication and return unauthorized when user is inactive (by CPF)")
    void shouldBlockAuthenticationWhenUserIsInactiveByCpf() throws Exception {
        User user = userRepository.findByEmail(EMAIL)
                .orElseThrow(() -> new AssertionError("User from setUp not found"));
        user.setActive(false);
        userRepository.save(user);

        LogIn loginDto = new LogIn(CPF, PASSWORD);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isUnauthorized());
    }
}
