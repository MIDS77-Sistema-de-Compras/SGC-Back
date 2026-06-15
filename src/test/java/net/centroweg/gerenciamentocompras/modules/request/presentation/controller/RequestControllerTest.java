package net.centroweg.gerenciamentocompras.modules.request.presentation.controller;

import net.centroweg.gerenciamentocompras.modules.cr.domain.Cr;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Branch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
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
    private Status waitingStatus;
    private Status approvedStatus;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        requestRepository.deleteAll();
        crBranchRepository.deleteAll();
        statusRepository.deleteAll();
        crRepository.deleteAll();
        branchRepository.deleteAll();

        Branch branch = branchRepository.save(new Branch("Filial Centro"));
        Cr cr = crRepository.save(new Cr("TI", "7940", false));
        crBranch = crBranchRepository.save(new CrBranch(branch, cr, null));

        waitingStatus = statusRepository.save(new Status("EM_ANDAMENTO", "Solicitação aguardando aprovação"));
        approvedStatus = statusRepository.save(new Status("Aprovado", "Solicitação aprovada pelo supervisor"));
    }

    @Test
    void shouldCreateRequest() throws Exception {
        mockMvc.perform(post("/requests")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "crBranchId": %d,
                                    "statusName": "EM_ANDAMENTO"
                                }
                                """.formatted(crBranch.getId())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.crBranchId").value(crBranch.getId()))
                .andExpect(jsonPath("$.statusName").value("EM_ANDAMENTO"));
    }

    @Test
    void shouldFindAllRequests() throws Exception {
        requestRepository.save(buildRequest(waitingStatus));

        mockMvc.perform(get("/requests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void shouldFindRequestById() throws Exception {
        Request saved = requestRepository.save(buildRequest(waitingStatus));

        mockMvc.perform(get("/requests/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.crBranchId").value(crBranch.getId()))
                .andExpect(jsonPath("$.statusName").value("EM_ANDAMENTO"));
    }

    @Test
    void shouldReturnNotFoundWhenRequestDoesNotExist() throws Exception {
        mockMvc.perform(get("/requests/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateRequest() throws Exception {
        Request saved = requestRepository.save(buildRequest(waitingStatus));

        mockMvc.perform(put("/requests/{id}", saved.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "crBranchId": %d,
                                    "statusName": "EM_ANDAMENTO"
                                }
                                """.formatted(crBranch.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusName").value("EM_ANDAMENTO"));
    }

    @Test
    void shouldInactivateRequestOnDelete() throws Exception {
        Request saved = requestRepository.save(buildRequest(waitingStatus));

        mockMvc.perform(delete("/requests/{id}", saved.getId())
                        .with(csrf()))
                .andExpect(status().isNoContent());

        Request inactivated = requestRepository.findById(saved.getId()).orElseThrow();
        assertThat(inactivated.getActive()).isFalse();
    }

    @Test
    void shouldNotDeleteApprovedRequest() throws Exception {
        Request saved = requestRepository.save(buildRequest(approvedStatus));

        mockMvc.perform(delete("/requests/{id}", saved.getId())
                        .with(csrf()))
                .andExpect(status().isConflict());
    }

    private Request buildRequest(Status status) {
        Request request = new Request(crBranch, status);
        request.setRequestDate(LocalDateTime.now());
        request.setUpdatedAt(LocalDateTime.now());
        request.setActive(true);
        return request;
    }
}