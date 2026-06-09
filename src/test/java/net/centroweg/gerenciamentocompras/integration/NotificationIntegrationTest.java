package net.centroweg.gerenciamentocompras.integration;

import net.centroweg.gerenciamentocompras.modules.cr.domain.Branch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.Cr;
import net.centroweg.gerenciamentocompras.modules.cr.domain.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.BranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.CrBranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.CrRepository;
import net.centroweg.gerenciamentocompras.modules.notification.domain.entity.Notification;
import net.centroweg.gerenciamentocompras.modules.notification.infrastructure.email.NotificationEmailService;
import net.centroweg.gerenciamentocompras.modules.notification.infrastructure.persistence.NotificationRepository;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.StatusRepository;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class NotificationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

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

    @MockitoBean
    private NotificationEmailService notificationEmailService;

    private static final String CPF_VALIDO = "52998224725";

    private User user;

    @BeforeEach
    void setUp() {
        notificationRepository.deleteAll();
        requestRepository.deleteAll();
        crBranchRepository.deleteAll();
        statusRepository.deleteAll();
        crRepository.deleteAll();
        branchRepository.deleteAll();
        userRepository.deleteAll();

        user = userRepository.save(
                new User("Admin Teste", CPF_VALIDO, "admin@teste.com", "Senha@123", "1234", true)
        );
    }

    private Notification criarNotificacao(String title, String message) {
        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setViewed(false);
        notification.setUser(user);
        notification.setRequest(null);
        return notificationRepository.save(notification);
    }

    private CrBranch criarCrBranchComResponsavel() {
        Branch branch = branchRepository.save(new Branch("Filial Centro"));
        Cr cr = crRepository.save(new Cr("TI", "7940", false));
        return crBranchRepository.save(new CrBranch(branch, cr, user));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("[Integração] Deve listar as notificações de um usuário")
    void deveListarNotificacoesPorUsuario() throws Exception {
        criarNotificacao("Status atualizado", "Sua solicitação foi aprovada");
        criarNotificacao("Nova solicitação", "Há uma nova solicitação no seu CR");

        mockMvc.perform(get("/notifications/user/{userId}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].userId").value(user.getId()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("[Integração] Deve retornar lista vazia quando usuário não tem notificações")
    void deveRetornarListaVaziaSemNotificacoes() throws Exception {
        mockMvc.perform(get("/notifications/user/{userId}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("[Integração] Deve marcar uma notificação como lida")
    void deveMarcarNotificacaoComoLida() throws Exception {
        Notification saved = criarNotificacao("Status atualizado", "Sua solicitação foi aprovada");

        mockMvc.perform(patch("/notifications/{id}/viewed", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.viewed").value(true));

        Notification updated = notificationRepository.findById(saved.getId()).orElseThrow();
        assertTrue(updated.getViewed());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("[Integração] Deve retornar 404 ao marcar notificação inexistente como lida")
    void deveRetornarNotFoundParaNotificacaoInexistente() throws Exception {
        mockMvc.perform(patch("/notifications/{id}/viewed", 9999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("[Integração] Deve gerar notificação ao criar uma solicitação (RN-NOT02)")
    void deveGerarNotificacaoAoCriarSolicitacao() throws Exception {
        CrBranch crBranch = criarCrBranchComResponsavel();
        statusRepository.save(new Status("Aguardando aprovação", "Solicitação aguardando aprovação"));

        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "crBranchId": %d,
                                    "statusName": "Aguardando aprovação"
                                }
                                """.formatted(crBranch.getId())))
                .andExpect(status().isCreated());

        assertEquals(1, notificationRepository.findByUserId(user.getId()).size());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("[Integração] Deve gerar notificação ao mudar o status de uma solicitação (RN-NOT01)")
    void deveGerarNotificacaoAoMudarStatus() throws Exception {
        CrBranch crBranch = criarCrBranchComResponsavel();
        statusRepository.save(new Status("Aguardando aprovação", "Solicitação aguardando aprovação"));
        statusRepository.save(new Status("Em atendimento", "Compra em andamento"));

        String response = mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "crBranchId": %d,
                                    "statusName": "Aguardando aprovação"
                                }
                                """.formatted(crBranch.getId())))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long requestId = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(put("/requests/{id}", requestId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "crBranchId": %d,
                                    "statusName": "Em atendimento"
                                }
                                """.formatted(crBranch.getId())))
                .andExpect(status().isOk());

        assertEquals(2, notificationRepository.findByUserId(user.getId()).size());
    }
}