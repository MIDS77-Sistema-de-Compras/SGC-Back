package net.centroweg.gerenciamentocompras.integration;

import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Branch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Cr;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.BranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrBranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrRepository;
import net.centroweg.gerenciamentocompras.modules.notification.infrastructure.email.NotificationEmailService;
import net.centroweg.gerenciamentocompras.modules.notification.infrastructure.persistence.NotificationRepository;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.StatusRepository;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.RoleRepository;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.shared.email.model.DefaultEmail;
import net.centroweg.gerenciamentocompras.shared.email.service.EmailSenderService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
class RequestStatusIntegrationTest {

    @Autowired private WebApplicationContext context;
    @Autowired private RequestRepository requestRepository;
    @Autowired private StatusRepository statusRepository;
    @Autowired private CrBranchRepository crBranchRepository;
    @Autowired private BranchRepository branchRepository;
    @Autowired private CrRepository crRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private NotificationRepository notificationRepository;

    @MockitoBean
    private NotificationEmailService notificationEmailService;

    @MockitoBean
    private EmailSenderService emailSenderService;

    private MockMvc mockMvc;
    private CrBranch crBranch;
    private Status pending;
    private Status approved;
    private Status refused;
    private Status inService;
    private Status delivered;
    private Status cancelled;
    private User requester;
    private User responsible;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        cleanDatabase();

        requester = saveUser("Solicitante", "solicitante@teste.com", "52998224725", "DOCENTE");
        responsible = saveUser("Responsavel", "responsavel@teste.com", "12345678909", "SUPERVISOR");

        Branch branch = branchRepository.save(new Branch("Filial Centro"));
        Cr cr = crRepository.save(new Cr("TI", "7940", false));
        crBranch = crBranchRepository.save(new CrBranch(branch, cr, List.of(responsible)));

        pending = statusRepository.save(new Status("Pendente", "Solicitacao pendente"));
        approved = statusRepository.save(new Status("Aprovado", "Solicitacao aprovada"));
        refused = statusRepository.save(new Status("Recusado", "Solicitacao recusada"));
        inService = statusRepository.save(new Status("Em atendimento", "Solicitacao em atendimento"));
        delivered = statusRepository.save(new Status("Entregue", "Solicitacao entregue"));
        cancelled = statusRepository.save(new Status("Cancelado", "Solicitacao cancelada"));
    }

    @AfterEach
    void tearDown() {
        cleanDatabase();
    }

    @Test
    @DisplayName("[Integracao] PATCH /requests/{id}/status com Aprovado deve alterar status no banco")
    void shouldApproveRequestAndPersistStatus() throws Exception {
        Request request = saveRequest(pending);

        mockMvc.perform(patch("/requests/{id}/status", request.getId())
                        .with(user(principalOf(responsible)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "statusName": "Aprovado"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(request.getId()))
                .andExpect(jsonPath("$.statusName").value("Aprovado"));

        Request updated = requestRepository.findById(request.getId()).orElseThrow();
        assertThat(updated.getStatus().getId()).isEqualTo(approved.getId());
        assertThat(updated.getFeedback()).isNull();
        awaitAnyEmail();
    }

    @Test
    @DisplayName("[Integracao] PATCH /requests/{id}/status com Recusado deve alterar status e salvar feedback")
    void shouldRefuseRequestAndPersistFeedback() throws Exception {
        Request request = saveRequest(pending);

        mockMvc.perform(patch("/requests/{id}/status", request.getId())
                        .with(user(principalOf(responsible)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "statusName": "Recusado",
                                    "justification": "  Faltam informacoes  "
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(request.getId()))
                .andExpect(jsonPath("$.statusName").value("Recusado"))
                .andExpect(jsonPath("$.feedback").value("Faltam informacoes"));

        Request updated = requestRepository.findById(request.getId()).orElseThrow();
        assertThat(updated.getStatus().getId()).isEqualTo(refused.getId());
        assertThat(updated.getFeedback()).isEqualTo("Faltam informacoes");
        awaitAnyEmail();
    }

    @Test
    @DisplayName("[Integracao] PATCH /requests/{id}/status com Recusado sem justificativa deve retornar bad request")
    void shouldReturnBadRequestWhenRefusingWithoutJustification() throws Exception {
        Request request = saveRequest(pending);

        mockMvc.perform(patch("/requests/{id}/status", request.getId())
                        .with(user(principalOf(responsible)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "statusName": "Recusado"
                                }
                                """))
                .andExpect(status().isBadRequest());

        Request unchanged = requestRepository.findById(request.getId()).orElseThrow();
        assertThat(unchanged.getStatus().getId()).isEqualTo(pending.getId());
        assertThat(unchanged.getFeedback()).isNull();
    }

    @Test
    @DisplayName("[Integracao] PATCH /requests/{id}/status com request inexistente deve retornar not found")
    void shouldReturnNotFoundWhenRequestDoesNotExist() throws Exception {
        mockMvc.perform(patch("/requests/{id}/status", 99999L)
                        .with(user(principalOf(responsible)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "statusName": "Aprovado"
                                }
                                """))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("[Integracao] PATCH /requests/{id}/status com status inexistente deve retornar not found")
    void shouldReturnNotFoundWhenStatusDoesNotExist() throws Exception {
        Request request = saveRequest(pending);

        mockMvc.perform(patch("/requests/{id}/status", request.getId())
                        .with(user(principalOf(responsible)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "statusName": "Inexistente"
                                }
                                """))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("[Integracao] Endpoint de status nao deve exigir crBranchId")
    void shouldNotRequireCrBranchId() throws Exception {
        Request request = saveRequest(pending);

        mockMvc.perform(patch("/requests/{id}/status", request.getId())
                        .with(user(principalOf(responsible)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "statusName": "Aprovado"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.crBranchId").value(crBranch.getId()))
                .andExpect(jsonPath("$.statusName").value("Aprovado"));
        awaitAnyEmail();
    }

    @Test
    @DisplayName("[Integracao] Deve notificar solicitante ao aprovar")
    void shouldCreateNotificationWhenApproved() throws Exception {
        Request request = saveRequest(pending);

        mockMvc.perform(patch("/requests/{id}/status", request.getId())
                        .with(user(principalOf(responsible)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "statusName": "Aprovado"
                                }
                                """))
                .andExpect(status().isOk());

        assertThat(notificationRepository.findByUserId(requester.getId()))
                .hasSize(1)
                .first()
                .satisfies(notification -> {
                    assertThat(notification.getRequestId()).isEqualTo(request.getId());
                    assertThat(notification.getTitle()).isEqualTo("Status da solicitação atualizado");
                    assertThat(notification.getMessage()).contains("Pendente", "Aprovado");
                });
        awaitAnyEmail();
    }

    @Test
    @DisplayName("[Integracao] Deve notificar solicitante ao recusar")
    void shouldCreateNotificationWhenRefused() throws Exception {
        Request request = saveRequest(pending);

        mockMvc.perform(patch("/requests/{id}/status", request.getId())
                        .with(user(principalOf(responsible)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "statusName": "Recusado",
                                    "justification": "Sem verba"
                                }
                                """))
                .andExpect(status().isOk());

        assertThat(notificationRepository.findByUserId(requester.getId()))
                .hasSize(1)
                .first()
                .satisfies(notification -> {
                    assertThat(notification.getRequestId()).isEqualTo(request.getId());
                    assertThat(notification.getTitle()).isEqualTo("Status da solicitação atualizado");
                    assertThat(notification.getMessage()).contains("Sem verba");
                });
        awaitAnyEmail();
    }

    @Test
    @DisplayName("[Integracao] Em atendimento deve persistir status, notificação e e-mail do solicitante")
    void shouldNotifyAndEmailRequesterForInServiceStatus() throws Exception {
        Request request = saveRequest(approved);
        clearInvocations(emailSenderService);

        mockMvc.perform(patch("/requests/{id}/status", request.getId())
                        .with(user(principalOf(responsible)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "statusName": "Em atendimento",
                                    "justification": null
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusName").value("Em atendimento"));

        assertThat(requestRepository.findById(request.getId()).orElseThrow().getStatus().getId())
                .isEqualTo(inService.getId());
        assertThat(notificationRepository.findByUserId(requester.getId()))
                .singleElement()
                .satisfies(notification -> assertThat(notification.getMessage())
                        .contains("Aprovado", "Em atendimento"));

        org.mockito.ArgumentCaptor<DefaultEmail> emailCaptor = org.mockito.ArgumentCaptor.forClass(DefaultEmail.class);
        org.mockito.ArgumentCaptor<String> htmlCaptor = org.mockito.ArgumentCaptor.forClass(String.class);
        verify(emailSenderService, timeout(5000)).sendEmail(emailCaptor.capture(), htmlCaptor.capture());
        assertThat(emailCaptor.getValue().getSendTo()).isEqualTo("solicitante@teste.com");
        assertThat(emailCaptor.getValue().getSubject()).contains("Em atendimento");
        assertThat(htmlCaptor.getValue()).contains("Em atendimento", "/docente/solicitacoes/" + request.getId());
    }

    @Test
    @DisplayName("[Integracao] Status igual não deve criar notificação nem enviar e-mail")
    void shouldNotNotifyWhenStatusIsUnchanged() throws Exception {
        Request request = saveRequest(inService);
        clearInvocations(emailSenderService);

        mockMvc.perform(patch("/requests/{id}/status", request.getId())
                        .with(user(principalOf(responsible)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "statusName": "Em atendimento"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusName").value("Em atendimento"));

        assertThat(notificationRepository.findByUserId(requester.getId())).isEmpty();
        verify(emailSenderService, org.mockito.Mockito.after(500).never())
                .sendEmail(org.mockito.ArgumentMatchers.any(DefaultEmail.class), anyString());
    }

    @Test
    @DisplayName("[Integracao] Entregue e Cancelado são status válidos para o fluxo de evento")
    void shouldNotifyForDeliveredAndCancelledStatuses() throws Exception {
        Request deliveredRequest = saveRequest(inService);

        patchStatus(deliveredRequest.getId(), "Entregue");
        assertThat(requestRepository.findById(deliveredRequest.getId()).orElseThrow().getStatus().getId())
                .isEqualTo(delivered.getId());
        awaitAnyEmail();

        clearInvocations(emailSenderService);
        Request cancelledRequest = saveRequest(approved);
        patchStatus(cancelledRequest.getId(), "Cancelado");
        assertThat(requestRepository.findById(cancelledRequest.getId()).orElseThrow().getStatus().getId())
                .isEqualTo(cancelled.getId());
        verify(emailSenderService, timeout(5000)).sendEmail(
                org.mockito.ArgumentMatchers.any(DefaultEmail.class),
                anyString()
        );
    }

    private Request saveRequest(Status status) {
        Request request = new Request(crBranch, status);
        request.setRequestDate(LocalDateTime.of(2026, 6, 26, 10, 0));
        request.setUpdatedAt(LocalDateTime.of(2026, 6, 26, 10, 5));
        request.setActive(true);
        request.getCreatedByUsers().add(requester);
        return requestRepository.save(request);
    }

    private User saveUser(String name, String email, String cpf, String roleName) {
        User user = new User(name, cpf, email, "Senha@123", "1234", true);
        user.setRole(roleRepository.save(new Role(roleName)));
        return userRepository.save(user);
    }

    private UserPrincipal principalOf(User user) {
        return new UserPrincipal(user);
    }

    private void patchStatus(Long requestId, String statusName) throws Exception {
        mockMvc.perform(patch("/requests/{id}/status", requestId)
                        .with(user(principalOf(responsible)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "statusName": "%s"
                                }
                                """.formatted(statusName)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusName").value(statusName));
    }

    private void awaitAnyEmail() throws Exception {
        verify(emailSenderService, timeout(5000).atLeastOnce()).sendEmail(
                org.mockito.ArgumentMatchers.any(DefaultEmail.class),
                anyString()
        );
    }

    private void cleanDatabase() {
        notificationRepository.deleteAll();
        requestRepository.deleteAll();
        crBranchRepository.deleteAll();
        statusRepository.deleteAll();
        crRepository.deleteAll();
        branchRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }
}
