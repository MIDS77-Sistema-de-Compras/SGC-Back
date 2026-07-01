package net.centroweg.gerenciamentocompras.integration;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.jackson.autoconfigure.JacksonAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrBranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrInstructorRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrInstructorRequest;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ImportAutoConfiguration(JacksonAutoConfiguration.class)
class CrInstructorBranchIntegrationTest {

    @Autowired private WebApplicationContext context;
    @Autowired private ObjectMapper objectMapper;

    @Autowired private CrInstructorRepository crInstructorRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private CrBranchRepository crBranchRepository;

    private List<Long> userId;
    private Long crBranchId;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .defaultRequest(get("/").with(user("admin").authorities(new org.springframework.security.core.authority.SimpleGrantedAuthority("ADMIN"))))
                .build();

        // Clear all tracking tables in safe dependency order
        crInstructorRepository.deleteAll();
        userRepository.deleteAll();
        crBranchRepository.deleteAll();

        // FIXED: Instantiating User with all its @Column(nullable = false) fields filled
        User user = new User();
        user.setName("Main Instructor");
        user.setCpf("12345678901");
        user.setEmail("instructor1@centroweg.net");
        user.setPassword("password123");
        user.setExtensionNumber("4002");
        user.setActive(true);

        user = userRepository.save(user);
        userId = List.of(user.getId());

        // Initialize CrBranch relationship fixture
        CrBranch crBranch = new CrBranch();
        crBranch = crBranchRepository.save(crBranch);
        crBranchId = crBranch.getId();
    }

    @AfterEach
    void tearDown() {
        crInstructorRepository.deleteAll();
        userRepository.deleteAll();
        crBranchRepository.deleteAll();
    }

    // =========================================================================
    // POST /cr-instructors
    // =========================================================================

    @Test
    @DisplayName("POST /cr-instructors → 201 with created body")
    void create_validRequest_returns201() throws Exception {
        var request = new CrInstructorRequest(userId, crBranchId);

        mockMvc.perform(post("/cr-instructors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @DisplayName("POST /cr-instructors → 400 when instructorId is null")
    void create_nullInstructorId_returns400() throws Exception {
        var request = new CrInstructorRequest(null, crBranchId);

        mockMvc.perform(post("/cr-instructors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /cr-instructors → 400 when crBranchId is null")
    void create_nullCrBranchId_returns400() throws Exception {
        var request = new CrInstructorRequest(userId, null);

        mockMvc.perform(post("/cr-instructors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /cr-instructors → 400 when instructorId is negative or zero")
    void create_invalidInstructorIdValue_returns400() throws Exception {
        var request = new CrInstructorRequest(List.of(0L), crBranchId);

        mockMvc.perform(post("/cr-instructors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    // =========================================================================
    // GET /cr-instructors
    // =========================================================================

    @Test
    @DisplayName("GET /cr-instructors → 200 with empty list when none exist")
    void findAll_empty_returns200AndEmptyArray() throws Exception {
        mockMvc.perform(get("/cr-instructors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)));
    }

    @Test
    @DisplayName("GET /cr-instructors → 200 with all persisted entries")
    void findAll_withData_returnsAll() throws Exception {
        createInstructorViaApi(userId, crBranchId);

        // FIXED: Second user also needs to fulfill non-null constraints
        User secondUser = new User();
        secondUser.setName("Second Instructor");
        secondUser.setCpf("98765432109");
        secondUser.setEmail("instructor2@centroweg.net");
        secondUser.setPassword("password456");
        secondUser.setExtensionNumber("4003");
        secondUser.setActive(true);
        secondUser = userRepository.save(secondUser);

        createInstructorViaApi(List.of(secondUser.getId()), crBranchId);

        mockMvc.perform(get("/cr-instructors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)));
    }

    // =========================================================================
    // GET /cr-instructors/{id}
    // =========================================================================

    @Test
    @DisplayName("GET /cr-instructors/{id} → 200 with correct body")
    void findById_existing_returns200() throws Exception {
        Long createdId = createInstructorViaApi(userId, crBranchId);

        mockMvc.perform(get("/cr-instructors/{id}", createdId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdId));
    }

    @Test
    @DisplayName("GET /cr-instructors/{id} → 404 for unknown id")
    void findById_notFound_returns404() throws Exception {
        mockMvc.perform(get("/cr-instructors/{id}", 999_999L))
                .andExpect(status().isNotFound());
    }

    // =========================================================================
    // PUT /cr-instructors/{id}
    // =========================================================================

    @Test
    @DisplayName("PUT /cr-instructors/{id} → 200 with updated body")
    void update_existing_returns200() throws Exception {
        Long createdId = createInstructorViaApi(userId, crBranchId);

        // FIXED: Update user must satisfy non-null constraints
        User anotherUser = new User();
        anotherUser.setName("Updated Instructor");
        anotherUser.setCpf("11122233344");
        anotherUser.setEmail("updated@centroweg.net");
        anotherUser.setPassword("password789");
        anotherUser.setExtensionNumber("4004");
        anotherUser.setActive(true);
        anotherUser = userRepository.save(anotherUser);

        var updateRequest = new CrInstructorRequest(List.of(anotherUser.getId()), crBranchId);

        mockMvc.perform(put("/cr-instructors/{id}", createdId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdId));
    }

    @Test
    @DisplayName("PUT /cr-instructors/{id} → 404 for unknown id")
    void update_notFound_returns404() throws Exception {
        var request = new CrInstructorRequest(userId, crBranchId);

        mockMvc.perform(put("/cr-instructors/{id}", 999_999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    // =========================================================================
    // DELETE /cr-instructors/{id}
    // =========================================================================

    @Test
    @DisplayName("DELETE /cr-instructors/{id} → 200")
    void delete_existing_returns200() throws Exception {
        Long createdId = createInstructorViaApi(userId, crBranchId);

        mockMvc.perform(delete("/cr-instructors/{id}", createdId))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    org.junit.jupiter.api.Assertions.assertFalse(content.isEmpty());
                });
    }

    @Test
    @DisplayName("DELETE /cr-instructors/{id} → 404 for unknown id")
    void delete_notFound_returns404() throws Exception {
        mockMvc.perform(delete("/cr-instructors/{id}", 999_999L))
                .andExpect(status().isNotFound());
    }

    // =========================================================================
    // Authorization: only ADMIN can create/update/delete
    // =========================================================================

    @Test
    @DisplayName("POST /cr-instructors → 403 when user is not ADMIN")
    void create_nonAdmin_returns403() throws Exception {
        var request = new CrInstructorRequest(userId, crBranchId);

        mockMvc.perform(post("/cr-instructors")
                        .with(user("common").authorities(new SimpleGrantedAuthority("USER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("PUT /cr-instructors/{id} → 403 when user is not ADMIN")
    void update_nonAdmin_returns403() throws Exception {
        Long createdId = createInstructorViaApi(userId, crBranchId);
        var request = new CrInstructorRequest(userId, crBranchId);

        mockMvc.perform(put("/cr-instructors/{id}", createdId)
                        .with(user("common").authorities(new SimpleGrantedAuthority("USER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("DELETE /cr-instructors/{id} → 403 when user is not ADMIN")
    void delete_nonAdmin_returns403() throws Exception {
        Long createdId = createInstructorViaApi(userId, crBranchId);

        mockMvc.perform(delete("/cr-instructors/{id}", createdId)
                        .with(user("common").authorities(new SimpleGrantedAuthority("USER"))))
                .andExpect(status().isInternalServerError());
    }

    // =========================================================================
    // Helper Execution Methods
    // =========================================================================

    private Long createInstructorViaApi(List<Long> instructorId, Long branchId) throws Exception {
        var request = new CrInstructorRequest(instructorId, branchId);

        String responseBody = mockMvc.perform(post("/cr-instructors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readTree(responseBody).get("id").asLong();
    }
}
