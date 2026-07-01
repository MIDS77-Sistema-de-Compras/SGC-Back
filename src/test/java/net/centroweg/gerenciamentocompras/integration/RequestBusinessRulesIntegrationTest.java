package net.centroweg.gerenciamentocompras.integration;

import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Branch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Cr;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.BranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrBranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrRepository;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.StatusRepository;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.RoleRepository;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
class RequestBusinessRulesIntegrationTest {

    @Autowired private WebApplicationContext context;
    @Autowired private RequestRepository requestRepository;
    @Autowired private StatusRepository statusRepository;
    @Autowired private CrBranchRepository crBranchRepository;
    @Autowired private BranchRepository branchRepository;
    @Autowired private CrRepository crRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;

    private MockMvc mockMvc;
    private CrBranch crBranch;
    private Status pending;
    private Status approved;
    private Status inService;
    private User creator;
    private User anotherUser;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        cleanDatabase();
        creator = saveUser("Criador", "criador@teste.com", "52998224725");
        anotherUser = saveUser("Outro usuário", "outro@teste.com", "12345678909");
        Branch branch = branchRepository.save(new Branch("Filial Centro"));
        Cr cr = crRepository.save(new Cr("TI", "7940", false));
        crBranch = crBranchRepository.save(new CrBranch(branch, cr, null));
        pending = statusRepository.save(new Status("Pendente", "solicitação pendente"));
        approved = statusRepository.save(new Status("Aprovado", "solicitação aprovada"));
        inService = statusRepository.save(new Status("Em atendimento", "solicitação em atendimento"));
    }

    @AfterEach
    void tearDown() {
        cleanDatabase();
    }
    @Test
    @DisplayName("[Integração] Deve inativar solicitação ativa do próprio criador e persistir active como falso")
    void shouldInactivateActiveRequestCreatedByAuthenticatedUser() throws Exception {
        Request request = saveRequest(pending, creator, true);

        mockMvc.perform(delete("/requests/{id}", request.getId()).with(user(principalOf(creator))))
                .andExpect(status().isNoContent());

        assertThat(requestRepository.findById(request.getId()).orElseThrow().getActive()).isFalse();
    }

    @Test
    @DisplayName("[Integração] Deve bloquear inativação de solicitação aprovada")
    void shouldBlockInactivationWhenRequestIsApproved() throws Exception {
        Request request = saveRequest(approved, creator, true);

        mockMvc.perform(delete("/requests/{id}", request.getId()).with(user(principalOf(creator))))
                .andExpect(status().isUnprocessableContent());
    }

    @Test
    @DisplayName("[Integração] Deve bloquear inativação de solicitação em atendimento")
    void shouldBlockInactivationWhenRequestIsInService() throws Exception {
        Request request = saveRequest(inService, creator, true);

        mockMvc.perform(delete("/requests/{id}", request.getId()).with(user(principalOf(creator))))
                .andExpect(status().isUnprocessableContent());
    }

    @Test
    @DisplayName("[Integração] Deve bloquear inativação por usuário que não criou a solicitação")
    void shouldBlockInactivationWhenAuthenticatedUserIsNotCreator() throws Exception {
        Request request = saveRequest(pending, creator, true);

        mockMvc.perform(delete("/requests/{id}", request.getId()).with(user(principalOf(anotherUser))))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    @DisplayName("[Integração] Deve editar solicitação ativa do próprio criador antes da etapa operacional")
    void shouldEditActiveRequestCreatedByAuthenticatedUser() throws Exception {
        Request request = saveRequest(pending, creator, true);

        mockMvc.perform(put("/requests/{id}", request.getId())
                        .with(user(principalOf(creator)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateBody()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusName").value("Pendente"));
    }
    @Test
    @DisplayName("[Integração] Deve bloquear edição de solicitação em atendimento")
    void shouldBlockEditWhenRequestIsInService() throws Exception {
        Request request = saveRequest(inService, creator, true);

        mockMvc.perform(put("/requests/{id}", request.getId())
                        .with(user(principalOf(creator)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateBody()))
                .andExpect(status().isUnprocessableContent());
    }

    @Test
    @DisplayName("[Integração] Deve bloquear edição por usuário que não criou a solicitação")
    void shouldBlockEditWhenAuthenticatedUserIsNotCreator() throws Exception {
        Request request = saveRequest(pending, creator, true);

        mockMvc.perform(put("/requests/{id}", request.getId())
                        .with(user(principalOf(anotherUser)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateBody()))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    @DisplayName("[Integração] Deve bloquear edição de solicitação inativa")
    void shouldBlockEditWhenRequestIsInactive() throws Exception {
        Request request = saveRequest(pending, creator, false);

        mockMvc.perform(put("/requests/{id}", request.getId())
                        .with(user(principalOf(creator)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateBody()))
                .andExpect(status().isConflict());
    }

    private User saveUser(String name, String email, String cpf) {
        User user = new User(name, cpf, email, "Senha@123", "1234", true);
        user.setRole(roleRepository.save(new Role("USER")));
        return userRepository.save(user);
    }

    private Request saveRequest(Status status, User requestCreator, boolean active) {
        Request request = new Request(crBranch, status);
        request.setRequestDate(LocalDateTime.now());
        request.setUpdatedAt(LocalDateTime.now());
        request.setActive(active);
        request.getCreatedByUsers().add(requestCreator);
        return requestRepository.save(request);
    }

    private UserPrincipal principalOf(User user) {
        return new UserPrincipal(user);
    }

    private String updateBody() {
        return "{\"crBranchId\": %d, \"statusName\": \"Pendente\", \"userIds\": []}".formatted(crBranch.getId());
    }

    private void cleanDatabase() {
        requestRepository.deleteAll();
        crBranchRepository.deleteAll();
        statusRepository.deleteAll();
        crRepository.deleteAll();
        branchRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }
}