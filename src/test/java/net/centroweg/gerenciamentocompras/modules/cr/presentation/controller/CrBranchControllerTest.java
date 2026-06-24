package net.centroweg.gerenciamentocompras.modules.cr.presentation.controller;

import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Branch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Cr;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.BranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrBranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrRepository;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@WithMockUser
class CrBranchControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private CrBranchRepository crBranchRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private CrRepository crRepository;

    @Autowired
    private UserRepository userRepository;

    private MockMvc mockMvc;

    private Branch branch;
    private Cr cr;
    private User user;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        crBranchRepository.deleteAll();
        userRepository.deleteAll();
        crRepository.deleteAll();
        branchRepository.deleteAll();

        branch = branchRepository.save(new Branch("Filial Centro"));
        cr = crRepository.save(new Cr("TI", "7940", false));

        user = new User();
        user.setName("João");
        user.setCpf("12345678900");
        user.setEmail("joao@centroweg.com.br");
        user.setPassword("Senha@123");
        user.setExtensionNumber("1234");
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user = userRepository.save(user);
    }

    @Test
    void shouldCreateCrBranch() throws Exception {
        mockMvc.perform(post("/cr-branches")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "branchId": %d,
                                    "crId": %d
                                }
                                """.formatted(branch.getId(), cr.getId())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.branchName").value("Filial Centro"))
                .andExpect(jsonPath("$.crName").value("TI"))
                .andExpect(jsonPath("$.crCode").value("7940"));
    }

    @Test
    void shouldNotCreateDuplicateCrBranch() throws Exception {
        crBranchRepository.save(new CrBranch(branch, cr, null));

        mockMvc.perform(post("/cr-branches")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "branchId": %d,
                                    "crId": %d
                                }
                                """.formatted(branch.getId(), cr.getId())))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldFindAllCrBranches() throws Exception {
        crBranchRepository.save(new CrBranch(branch, cr, null));

        mockMvc.perform(get("/cr-branches"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void shouldFindCrBranchById() throws Exception {
        CrBranch saved = crBranchRepository.save(new CrBranch(branch, cr, null));

        mockMvc.perform(get("/cr-branches/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.branchName").value("Filial Centro"))
                .andExpect(jsonPath("$.crName").value("TI"));
    }

    @Test
    void shouldUpdateCrBranch() throws Exception {
        CrBranch saved = crBranchRepository.save(new CrBranch(branch, cr, null));
        Branch newBranch = branchRepository.save(new Branch("Filial Norte"));

        mockMvc.perform(put("/cr-branches/{id}", saved.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "branchId": %d,
                                    "crId": %d
                                }
                                """.formatted(newBranch.getId(), cr.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.branchName").value("Filial Norte"));
    }

    @Test
    void shouldDeleteCrBranch() throws Exception {
        CrBranch saved = crBranchRepository.save(new CrBranch(branch, cr, null));

        mockMvc.perform(delete("/cr-branches/{id}", saved.getId())
                        .with(csrf()))
                .andExpect(status().isOk());

        assertThat(crBranchRepository.findById(saved.getId())).isEmpty();
    }

    @Test
    void shouldFindCrBranchByBranch() throws Exception {
        crBranchRepository.save(new CrBranch(branch, cr, null));

        mockMvc.perform(get("/cr-branches/branch/{branchId}", branch.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].branchName").value("Filial Centro"));
    }

    @Test
    void shouldAssignResponsible() throws Exception {
        CrBranch saved = crBranchRepository.save(new CrBranch(branch, cr, null));

        mockMvc.perform(put("/cr-branches/{crBranchId}/responsible/{userId}", saved.getId(), user.getId())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responsibleUsersName[0]").value("João"));
    }

    @Test
    void shouldRemoveResponsible() throws Exception {
        CrBranch saved = crBranchRepository.save(new CrBranch(branch, cr, List.of(user)));

        mockMvc.perform(delete("/cr-branches/{crBranchId}/responsible", saved.getId())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responsibleUsersName").isEmpty());
    }
}