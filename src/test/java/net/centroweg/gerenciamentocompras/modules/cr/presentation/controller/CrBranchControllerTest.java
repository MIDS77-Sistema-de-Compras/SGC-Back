package net.centroweg.gerenciamentocompras.modules.cr.presentation.controller;

import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Branch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Cr;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.BranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrBranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrRepository;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.RoleRepository;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.shared.security.authority.Authorities;
import org.junit.jupiter.api.AfterEach;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@WithMockUser(authorities = "ADMIN")
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

    @Autowired
    private RoleRepository roleRepository;

    private MockMvc mockMvc;

    private Branch branch;
    private Cr cr;
    private User user;
    private Role supervisorRole;
    private Role coordinatorRole;
    private Role docenteRole;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        crBranchRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
        crRepository.deleteAll();
        branchRepository.deleteAll();

        branch = branchRepository.save(new Branch("Filial Centro"));
        cr = crRepository.save(new Cr("TI", "7940", false));

        supervisorRole = roleRepository.save(new Role(Authorities.SUPERVISOR));
        coordinatorRole = roleRepository.save(new Role(Authorities.COORDENADOR));
        docenteRole = roleRepository.save(new Role(Authorities.DOCENTE));

        user = new User();
        user.setName("João");
        user.setCpf("12345678900");
        user.setEmail("joao@centroweg.com.br");
        user.setPassword("Senha@123");
        user.setExtensionNumber("1234");
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setRole(supervisorRole);
        user = userRepository.save(user);
    }

    @AfterEach
    void tearDown() {
        crBranchRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
        crRepository.deleteAll();
        branchRepository.deleteAll();
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
    void shouldCreateCrBranchWithTwoSupervisorsAndOneCoordinator() throws Exception {
        User secondSupervisor = createUser("Maria", "98765432100", "maria@centroweg.com.br", supervisorRole);
        User coordinator = createUser("Carlos", "98765432101", "carlos@centroweg.com.br", coordinatorRole);

        mockMvc.perform(post("/cr-branches")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody(branch, cr, List.of(user, secondSupervisor, coordinator))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.responsibleUsersName.length()").value(3));
    }

    @Test
    void shouldRejectThreeActiveSupervisorsOnCreate() throws Exception {
        User secondSupervisor = createUser("Maria", "98765432100", "maria@centroweg.com.br", supervisorRole);
        User thirdSupervisor = createUser("Pedro", "98765432101", "pedro@centroweg.com.br", supervisorRole);

        mockMvc.perform(post("/cr-branches")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody(branch, cr, List.of(user, secondSupervisor, thirdSupervisor))))
                .andExpect(status().isConflict());

        assertThat(crBranchRepository.findAll()).isEmpty();
    }

    @Test
    void shouldRejectTwoCoordinatorsOnCreate() throws Exception {
        User firstCoordinator = createUser("Carlos", "98765432100", "carlos@centroweg.com.br", coordinatorRole);
        User secondCoordinator = createUser("Ana", "98765432101", "ana@centroweg.com.br", coordinatorRole);

        mockMvc.perform(post("/cr-branches")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody(branch, cr, List.of(firstCoordinator, secondCoordinator))))
                .andExpect(status().isConflict());

        assertThat(crBranchRepository.findAll()).isEmpty();
    }

    @Test
    void shouldRejectForbiddenResponsibleRoleOnCreate() throws Exception {
        User docente = createUser("Docente", "98765432100", "docente@centroweg.com.br", docenteRole);

        mockMvc.perform(post("/cr-branches")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody(branch, cr, List.of(docente))))
                .andExpect(status().isBadRequest());

        assertThat(crBranchRepository.findAll()).isEmpty();
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
                .andExpect(jsonPath("$.content.length()").value(1));
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
    void shouldUpdateCrBranchToValidResponsibles() throws Exception {
        CrBranch saved = crBranchRepository.save(new CrBranch(branch, cr, new ArrayList<>(List.of(user))));
        User secondSupervisor = createUser("Maria", "98765432100", "maria@centroweg.com.br", supervisorRole);
        User coordinator = createUser("Carlos", "98765432101", "carlos@centroweg.com.br", coordinatorRole);

        mockMvc.perform(put("/cr-branches/{id}", saved.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody(branch, cr, List.of(user, secondSupervisor, coordinator))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responsibleUsersName.length()").value(3));
    }

    @Test
    void shouldNotChangeCrBranchWhenUpdateResponsiblesAreInvalid() throws Exception {
        CrBranch saved = crBranchRepository.save(new CrBranch(branch, cr, new ArrayList<>(List.of(user))));
        User docente = createUser("Docente", "98765432100", "docente@centroweg.com.br", docenteRole);

        mockMvc.perform(put("/cr-branches/{id}", saved.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody(branch, cr, List.of(docente))))
                .andExpect(status().isBadRequest());

        CrBranch persisted = crBranchRepository.findAllByResponsibleUserId(user.getId()).stream()
                .filter(candidate -> candidate.getId().equals(saved.getId()))
                .findFirst()
                .orElseThrow();
        assertThat(persisted.getResponsibleUsers()).extracting(User::getId).containsExactly(user.getId());
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
    void shouldAssignSecondSupervisorWithinLimit() throws Exception {
        CrBranch saved = crBranchRepository.save(new CrBranch(branch, cr, new ArrayList<>(List.of(user))));
        User secondSupervisor = createUser("Maria", "98765432100", "maria@centroweg.com.br", supervisorRole);

        mockMvc.perform(put("/cr-branches/{crBranchId}/responsible/{userId}", saved.getId(), secondSupervisor.getId())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responsibleUsersName.length()").value(2));
    }

    @Test
    void shouldRejectThirdSupervisorAssignment() throws Exception {
        User secondSupervisor = createUser("Maria", "98765432100", "maria@centroweg.com.br", supervisorRole);
        User thirdSupervisor = createUser("Pedro", "98765432101", "pedro@centroweg.com.br", supervisorRole);
        CrBranch saved = crBranchRepository.save(new CrBranch(
                branch, cr, new ArrayList<>(List.of(user, secondSupervisor))
        ));

        mockMvc.perform(put("/cr-branches/{crBranchId}/responsible/{userId}", saved.getId(), thirdSupervisor.getId())
                        .with(csrf()))
                .andExpect(status().isConflict());

        assertThat(crBranchRepository.findAllByResponsibleUserId(user.getId()).stream()
                .filter(candidate -> candidate.getId().equals(saved.getId()))
                .findFirst()
                .orElseThrow()
                .getResponsibleUsers())
                .extracting(User::getId)
                .containsExactly(user.getId(), secondSupervisor.getId());
    }

    @Test
    void shouldAssignFirstCoordinator() throws Exception {
        CrBranch saved = crBranchRepository.save(new CrBranch(branch, cr, new ArrayList<>()));
        User coordinator = createUser("Carlos", "98765432100", "carlos@centroweg.com.br", coordinatorRole);

        mockMvc.perform(put("/cr-branches/{crBranchId}/responsible/{userId}", saved.getId(), coordinator.getId())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responsibleUsersName[0]").value("Carlos"));
    }

    @Test
    void shouldRejectSecondCoordinatorAssignment() throws Exception {
        User firstCoordinator = createUser("Carlos", "98765432100", "carlos@centroweg.com.br", coordinatorRole);
        User secondCoordinator = createUser("Ana", "98765432101", "ana@centroweg.com.br", coordinatorRole);
        CrBranch saved = crBranchRepository.save(new CrBranch(
                branch, cr, new ArrayList<>(List.of(firstCoordinator))
        ));

        mockMvc.perform(put("/cr-branches/{crBranchId}/responsible/{userId}", saved.getId(), secondCoordinator.getId())
                        .with(csrf()))
                .andExpect(status().isConflict());

        assertThat(crBranchRepository.findAllByResponsibleUserId(firstCoordinator.getId()).stream()
                .filter(candidate -> candidate.getId().equals(saved.getId()))
                .findFirst()
                .orElseThrow()
                .getResponsibleUsers())
                .extracting(User::getId)
                .containsExactly(firstCoordinator.getId());
    }

    @Test
    void shouldRejectDocenteAssignment() throws Exception {
        CrBranch saved = crBranchRepository.save(new CrBranch(branch, cr, new ArrayList<>()));
        User docente = createUser("Docente", "98765432100", "docente@centroweg.com.br", docenteRole);

        mockMvc.perform(put("/cr-branches/{crBranchId}/responsible/{userId}", saved.getId(), docente.getId())
                        .with(csrf()))
                .andExpect(status().isBadRequest());

        assertThat(crBranchRepository.findAllByResponsibleUserId(docente.getId())).isEmpty();
    }

    @Test
    void shouldRemoveResponsible() throws Exception {
        CrBranch saved = crBranchRepository.save(new CrBranch(branch, cr, List.of(user)));

        mockMvc.perform(delete("/cr-branches/{crBranchId}/responsible/{userId}", saved.getId(), user.getId())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responsibleUsersName").isEmpty());
    }

    @Test
    void shouldAddResponsibleWithoutReplacingExisting() throws Exception {
        CrBranch saved = crBranchRepository.save(new CrBranch(branch, cr, List.of(user)));
        User secondUser = createUser("Maria", "98765432100", "maria@centroweg.com.br", supervisorRole);

        mockMvc.perform(put("/cr-branches/{crBranchId}/responsible/{userId}", saved.getId(), secondUser.getId())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responsibleUsersName.length()").value(2));
    }

    @Test
    void shouldRemoveOnlyGivenResponsible() throws Exception {
        User secondUser = createUser("Maria", "98765432100", "maria@centroweg.com.br", supervisorRole);
        CrBranch saved = crBranchRepository.save(new CrBranch(branch, cr, List.of(user, secondUser)));

        mockMvc.perform(delete("/cr-branches/{crBranchId}/responsible/{userId}", saved.getId(), user.getId())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responsibleUsersName.length()").value(1))
                .andExpect(jsonPath("$.responsibleUsersName[0]").value("Maria"));
    }

    private User createUser(String name, String cpf, String email, Role role) {
        User newUser = new User();
        newUser.setName(name);
        newUser.setCpf(cpf);
        newUser.setEmail(email);
        newUser.setPassword("Senha@123");
        newUser.setExtensionNumber("4321");
        newUser.setActive(true);
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setUpdatedAt(LocalDateTime.now());
        newUser.setRole(role);
        return userRepository.save(newUser);
    }

    private String requestBody(Branch requestBranch, Cr requestCr, List<User> responsibles) {
        String responsibleIds = responsibles.stream()
                .map(User::getId)
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        return """
                {
                    "branchId": %d,
                    "crId": %d,
                    "responsibleUsersId": [%s]
                }
                """.formatted(requestBranch.getId(), requestCr.getId(), responsibleIds);
    }
}
