package net.centroweg.gerenciamentocompras.integration;

import jakarta.persistence.EntityManager;
import jakarta.servlet.http.Cookie;
import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.*;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.ApprovalDelegationOverlapException;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.*;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.scheduler.ApprovalDelegationScheduler;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CreateApprovalDelegationRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.ApprovalDelegationResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.approvaldelegationservice.approvaldelegationinterface.ApprovalDelegationService;
import net.centroweg.gerenciamentocompras.modules.cr.service.approvaldelegationservice.functionality.ActivateApprovalDelegation;
import net.centroweg.gerenciamentocompras.modules.cr.service.approvaldelegationservice.functionality.FinishApprovalDelegation;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.StatusRepository;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.RoleRepository;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.LogIn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.web.context.WebApplicationContext;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ApprovalDelegationIntegrationTest {

    @Autowired private WebApplicationContext context;
    @Autowired private ApprovalDelegationService delegationService;
    @Autowired private FinishApprovalDelegation finishApprovalDelegation;
    @Autowired private ActivateApprovalDelegation activateApprovalDelegation;
    @Autowired private ApprovalDelegationScheduler scheduler;
    @Autowired private ApprovalDelegationRepository delegationRepository;
    @Autowired private CrBranchRepository crBranchRepository;
    @Autowired private BranchRepository branchRepository;
    @Autowired private CrRepository crRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private RequestRepository requestRepository;
    @Autowired private StatusRepository statusRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private EntityManager entityManager;
    @Autowired private ObjectMapper objectMapper;

    private MockMvc mockMvc;
    private Role supervisorRole;
    private User delegator;
    private User delegate;
    private CrBranch firstBranch;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        supervisorRole = roleRepository.save(new Role("SUPERVISOR"));
        delegator = saveUser("Titular", "titular.delegacao@teste.com", "11144477735");
        delegate = saveUser("Substituto", "substituto.delegacao@teste.com", "52998224725");
        firstBranch = saveCrBranch("Filial Centro", "CR-1", List.of(delegator));
        authenticate(delegator);
    }

    @Test
    void shouldCreateImmediateDelegationForEveryBranch() {
        CrBranch secondBranch = saveCrBranch("Filial Norte", "CR-2", List.of(delegator));

        ApprovalDelegationResponse response = delegationService.create(immediateRequest());

        assertThat(response.status()).isEqualTo(ApprovalDelegationStatus.ACTIVE);
        assertThat(response.crBranchIds()).containsExactlyInAnyOrder(firstBranch.getId(), secondBranch.getId());
        assertThat(userRepository.findById(delegator.getId()).orElseThrow().getActive()).isFalse();
        assertThat(responsibleIds(firstBranch.getId())).contains(delegate.getId());
        assertThat(responsibleIds(secondBranch.getId())).contains(delegate.getId());
    }

    @Test
    void shouldKeepFutureDelegationPendingWithoutChangingUsersOrBranches() {
        ApprovalDelegationResponse response = delegationService.create(new CreateApprovalDelegationRequest(
                delegate.getId(), LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2)
        ));

        assertThat(response.status()).isEqualTo(ApprovalDelegationStatus.PENDING);
        assertThat(userRepository.findById(delegator.getId()).orElseThrow().getActive()).isTrue();
        assertThat(responsibleIds(firstBranch.getId())).doesNotContain(delegate.getId());
    }

    @Test
    void shouldActivateAndFinishOverdueDelegationThroughScheduler() {
        ApprovalDelegationResponse response = delegationService.create(new CreateApprovalDelegationRequest(
                delegate.getId(), LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2)
        ));
        ApprovalDelegation delegation = delegationRepository.findById(response.id()).orElseThrow();
        delegation.setStartAt(LocalDateTime.now().minusMinutes(1));
        delegation.setEndAt(LocalDateTime.now().plusDays(1));
        delegationRepository.saveAndFlush(delegation);

        scheduler.processDueDelegations();

        assertThat(delegationRepository.findById(response.id()).orElseThrow().getStatus())
                .isEqualTo(ApprovalDelegationStatus.ACTIVE);
        assertThat(userRepository.findById(delegator.getId()).orElseThrow().getActive()).isFalse();

        delegation.setEndAt(LocalDateTime.now().minusSeconds(1));
        delegationRepository.saveAndFlush(delegation);
        scheduler.processDueDelegations();

        assertThat(delegationRepository.findById(response.id()).orElseThrow().getStatus())
                .isEqualTo(ApprovalDelegationStatus.FINISHED);
        assertThat(userRepository.findById(delegator.getId()).orElseThrow().getActive()).isTrue();
        assertThat(responsibleIds(firstBranch.getId())).doesNotContain(delegate.getId());
        assertThat(userRepository.existsById(delegate.getId())).isTrue();
    }

    @Test
    void shouldPreservePreexistingResponsibleRelationshipAfterFinish() {
        firstBranch.setResponsibleUsers(new ArrayList<>(List.of(delegator, delegate)));
        crBranchRepository.saveAndFlush(firstBranch);
        ApprovalDelegationResponse response = delegationService.create(immediateRequest());

        expireAndFinish(response.id());

        assertThat(responsibleIds(firstBranch.getId())).contains(delegate.getId());
        ApprovalDelegation delegation = delegationRepository.findById(response.id()).orElseThrow();
        assertThat(delegation.getBranches()).singleElement()
                .satisfies(branch -> assertThat(branch.isTemporaryRelationshipCreated()).isFalse());
    }

    @Test
    void shouldRejectOverlappingDelegation() {
        delegationService.create(new CreateApprovalDelegationRequest(
                delegate.getId(), LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(3)
        ));

        assertThatThrownBy(() -> delegationService.create(new CreateApprovalDelegationRequest(
                delegate.getId(), LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(4)
        ))).isInstanceOf(ApprovalDelegationOverlapException.class);
    }

    @Test
    void shouldCreateAndListDelegationThroughEndpointsAndRejectNonSupervisor() throws Exception {
        String body = """
                {
                  "delegateUserId": %d,
                  "startAt": "%s",
                  "endAt": "%s"
                }
                """.formatted(
                delegate.getId(),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );

        mockMvc.perform(post("/approval-delegations")
                        .with(user(new UserPrincipal(delegator)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.delegatorUserId").value(delegator.getId()))
                .andExpect(jsonPath("$.status").value("PENDING"));

        mockMvc.perform(get("/approval-delegations/me")
                        .with(user(new UserPrincipal(delegator))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].delegateUserId").value(delegate.getId()));

        User nonSupervisor = saveUser("Docente", "docente.delegacao@teste.com", "12345678909");
        nonSupervisor.setRole(roleRepository.save(new Role("DOCENTE")));
        userRepository.saveAndFlush(nonSupervisor);
        mockMvc.perform(post("/approval-delegations")
                        .with(user(new UserPrincipal(nonSupervisor)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldInvalidateOldJwtAndAllowLoginAgainAfterFinish() throws Exception {
        SecurityContextHolder.clearContext();
        LogIn login = new LogIn(delegator.getEmail(), "Senha@123");
        Cookie oldJwt = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getCookie("jwt");
        assertThat(oldJwt).isNotNull();

        authenticate(delegator);
        ApprovalDelegationResponse response = delegationService.create(immediateRequest());
        SecurityContextHolder.clearContext();

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(get("/users/me").cookie(oldJwt))
                .andExpect(status().isUnauthorized());

        expireAndFinish(response.id());
        SecurityContextHolder.clearContext();
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("jwt"));
    }

    @Test
    void shouldGrantApprovalOnlyWhileTemporaryRelationshipIsActive() throws Exception {
        User requester = saveUser("Solicitante", "solicitante.delegacao@teste.com", "98765432100");
        requester.setRole(roleRepository.save(new Role("DOCENTE")));
        userRepository.save(requester);
        Status pending = statusRepository.save(new Status("Pendente", "Solicitação pendente"));
        statusRepository.save(new Status("Aprovado", "Solicitação aprovada"));
        ApprovalDelegationResponse response = delegationService.create(immediateRequest());

        Request approvedRequest = saveRequest(requester, pending);
        mockMvc.perform(patch("/requests/{id}/status", approvedRequest.getId())
                        .with(user(new UserPrincipal(delegate)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"statusName\":\"Aprovado\"}"))
                .andExpect(status().isOk());

        expireAndFinish(response.id());
        Request deniedRequest = saveRequest(requester, pending);
        mockMvc.perform(patch("/requests/{id}/status", deniedRequest.getId())
                        .with(user(new UserPrincipal(delegate)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"statusName\":\"Aprovado\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void shouldActivateOnlyOnceUnderConcurrentExecutions() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            ApprovalDelegationResponse response = delegationService.create(new CreateApprovalDelegationRequest(
                    delegate.getId(), LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2)
            ));
            ApprovalDelegation delegation = delegationRepository.findById(response.id()).orElseThrow();
            delegation.setStartAt(LocalDateTime.now().minusMinutes(1));
            delegation.setEndAt(LocalDateTime.now().plusDays(1));
            delegationRepository.saveAndFlush(delegation);

            CountDownLatch start = new CountDownLatch(1);
            Future<?> firstExecution = executor.submit(() -> activateAfter(start, response.id()));
            Future<?> secondExecution = executor.submit(() -> activateAfter(start, response.id()));
            start.countDown();
            firstExecution.get();
            secondExecution.get();

            ApprovalDelegation persisted = delegationRepository.findById(response.id()).orElseThrow();
            assertThat(persisted.getStatus()).isEqualTo(ApprovalDelegationStatus.ACTIVE);
            CrBranch branch = crBranchRepository.findAllByResponsibleUserId(delegate.getId()).stream()
                    .filter(candidate -> candidate.getId().equals(firstBranch.getId()))
                    .findFirst()
                    .orElseThrow();
            assertThat(branch.getResponsibleUsers().stream()
                    .filter(user -> user.getId().equals(delegate.getId())))
                    .hasSize(1);
        } finally {
            executor.shutdownNow();
            delegationRepository.deleteAll();
            crBranchRepository.deleteAll();
            crRepository.deleteAll();
            branchRepository.deleteAll();
            userRepository.deleteAll();
            roleRepository.deleteAll();
            SecurityContextHolder.clearContext();
        }
    }

    private User saveUser(String name, String email, String cpf) {
        User user = new User(
                name, cpf, email, passwordEncoder.encode("Senha@123"), "1234", true
        );
        user.setRole(supervisorRole);
        return userRepository.save(user);
    }

    private CrBranch saveCrBranch(String branchName, String crCode, List<User> responsibleUsers) {
        Branch branch = branchRepository.save(new Branch(branchName));
        Cr cr = crRepository.save(new Cr("CR " + crCode, crCode, false));
        return crBranchRepository.save(new CrBranch(branch, cr, new ArrayList<>(responsibleUsers)));
    }

    private CreateApprovalDelegationRequest immediateRequest() {
        return new CreateApprovalDelegationRequest(
                delegate.getId(), LocalDateTime.now().minusMinutes(1), LocalDateTime.now().plusDays(1)
        );
    }

    private void authenticate(User user) {
        UserPrincipal principal = new UserPrincipal(user);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities())
        );
    }

    private void expireAndFinish(Long delegationId) {
        ApprovalDelegation delegation = delegationRepository.findById(delegationId).orElseThrow();
        delegation.setEndAt(LocalDateTime.now().minusSeconds(1));
        delegationRepository.saveAndFlush(delegation);
        finishApprovalDelegation.finish(delegationId);
    }

    private List<Long> responsibleIds(Long crBranchId) {
        entityManager.flush();
        entityManager.clear();
        return crBranchRepository.findById(crBranchId).orElseThrow().getResponsibleUsers().stream()
                .map(User::getId)
                .toList();
    }

    private Request saveRequest(User requester, Status pending) {
        Request request = new Request(firstBranch, pending);
        request.setRequestDate(LocalDateTime.now());
        request.setUpdatedAt(LocalDateTime.now());
        request.setActive(true);
        request.getCreatedByUsers().add(requester);
        return requestRepository.saveAndFlush(request);
    }

    private void activateAfter(CountDownLatch start, Long delegationId) {
        try {
            start.await();
            activateApprovalDelegation.activate(delegationId);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Teste de concorrência interrompido.", exception);
        }
    }
}
