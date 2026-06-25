package net.centroweg.gerenciamentocompras.modules.cr.presentation.controller;

import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Branch;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.BranchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@WithMockUser
class BranchControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private BranchRepository branchRepository;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        branchRepository.deleteAll();
    }

    @Test
    void shouldCreateBranch() throws Exception {
        mockMvc.perform(post("/branches")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Filial Centro\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Filial Centro"))
                .andExpect(jsonPath("$.id").isNumber());
    }

    @Test
    void shouldFindAllBranches() throws Exception {
        branchRepository.save(new Branch("Filial Norte"));
        branchRepository.save(new Branch("Filial Sul"));

        mockMvc.perform(get("/branches"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void shouldFindBranchById() throws Exception {
        Branch saved = branchRepository.save(new Branch("Filial Leste"));

        mockMvc.perform(get("/branches/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.name").value("Filial Leste"));
    }

    @Test
    void shouldUpdateBranch() throws Exception {
        Branch saved = branchRepository.save(new Branch("Filial Antiga"));

        mockMvc.perform(put("/branches/{id}", saved.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Filial Nova\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.name").value("Filial Nova"));
    }

    @Test
    void shouldDeleteBranch() throws Exception {
        Branch saved = branchRepository.save(new Branch("Filial a Deletar"));

        mockMvc.perform(delete("/branches/{id}", saved.getId())
                        .with(csrf()))
                .andExpect(status().isNoContent());

        assertThat(branchRepository.findById(saved.getId())).isEmpty();
    }
}
