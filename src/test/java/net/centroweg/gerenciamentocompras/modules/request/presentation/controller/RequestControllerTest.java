package net.centroweg.gerenciamentocompras.modules.request.presentation.controller;

import net.centroweg.gerenciamentocompras.modules.cr.domain.Branch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.Cr;
import net.centroweg.gerenciamentocompras.modules.cr.domain.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.BranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.CrBranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.CrRepository;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.StatusRepository;
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

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@WithMockUser
class RequestControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private CrBranchRepository crBranchRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private CrRepository crRepository;

    private MockMvc mockMvc;

    private CrBranch crBranch;
    private Status statusAberto;
    private Status statusFechado;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        requestRepository.deleteAll();
        crBranchRepository.deleteAll();
        statusRepository.deleteAll();
        branchRepository.deleteAll();
        crRepository.deleteAll();

        Branch branch = branchRepository.save(new Branch("Filial Centro"));
        Cr cr = crRepository.save(new Cr("TI", "7940", false));
        crBranch = crBranchRepository.save(new CrBranch(branch, cr, null));

        statusAberto = statusRepository.save(new Status("ABERTO", "Solicitação em aberto"));
        statusFechado = statusRepository.save(new Status("FECHADO", "Solicitação finalizada"));
    }

    @Test
    void shouldCreateRequest() throws Exception {
        mockMvc.perform(post("/requests")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "crBranchId": %d,
                                    "statusName": "%s"
                                }
                                """.formatted(crBranch.getId(), statusAberto.getName())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.crBranchId").value(crBranch.getId()));
    }

    @Test
    void shouldReturn404WhenStatusNotFoundOnCreate() throws Exception {
        mockMvc.perform(post("/requests")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "crBranchId": %d,
                                    "statusName": "STATUS_INEXISTENTE"
                                }
                                """.formatted(crBranch.getId())))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldFindAllRequests() throws Exception {
        Request request = new Request(crBranch, statusAberto);
        request.setUpdatedAt(LocalDateTime.now());
        requestRepository.save(request);

        mockMvc.perform(get("/requests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].crBranchId").value(crBranch.getId()));
    }

    @Test
    void shouldFindRequestById() throws Exception {
        Request request = new Request(crBranch, statusAberto);
        request.setUpdatedAt(LocalDateTime.now());
        Request saved = requestRepository.save(request);

        mockMvc.perform(get("/requests/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.crBranchId").value(crBranch.getId()));
    }

    @Test
    void shouldReturn404WhenRequestNotFound() throws Exception {
        mockMvc.perform(get("/requests/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateRequest() throws Exception {
        Request request = new Request(crBranch, statusAberto);
        request.setUpdatedAt(LocalDateTime.now());
        Request saved = requestRepository.save(request);

        mockMvc.perform(put("/requests/{id}", saved.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "crBranchId": %d,
                                    "statusName": "%s"
                                }
                                """.formatted(crBranch.getId(), statusFechado.getName())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()));
    }

    @Test
    void shouldDeleteRequest() throws Exception {
        Request request = new Request(crBranch, statusAberto);
        request.setUpdatedAt(LocalDateTime.now());
        Request saved = requestRepository.save(request);

        mockMvc.perform(delete("/requests/{id}", saved.getId())
                        .with(csrf()))
                .andExpect(status().isNoContent());

        assertThat(requestRepository.findById(saved.getId())).isEmpty();
    }
}