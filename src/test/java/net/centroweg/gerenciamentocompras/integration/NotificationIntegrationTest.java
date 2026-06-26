package net.centroweg.gerenciamentocompras.integration;

import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Branch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Cr;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Sector;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.BranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrBranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrRepository;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.SectorRepository;
import net.centroweg.gerenciamentocompras.modules.notification.domain.entity.Notification;
import net.centroweg.gerenciamentocompras.modules.notification.infrastructure.email.NotificationEmailService;
import net.centroweg.gerenciamentocompras.modules.notification.infrastructure.persistence.NotificationRepository;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.StatusRepository;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.jackson.databind.ObjectMapper;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@WithMockUser
public class NotificationIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

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
    private SectorRepository sectorRepository;

    @Autowired
    private CrRepository crRepository;

    @MockitoBean
    private NotificationEmailService notificationEmailService;

    private static final String CPF_VALIDO = "52998224725";

    private User user;
    private CrBranch crBranch;
    private Status waitingStatus;
    private Request request;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .defaultRequest(get("/").with(user("admin").roles("USER", "ADMIN")))
                .build();

        notificationRepository.deleteAll();
        requestRepository.deleteAll();
        crBranchRepository.deleteAll();
        statusRepository.deleteAll();
        crRepository.deleteAll();
        sectorRepository.deleteAll();
        branchRepository.deleteAll();
        userRepository.deleteAll();

        user = userRepository.save(
                new User("Admin Teste", CPF_VALIDO, "admin@teste.com", "Senha@123", "1234", true)
        );

        Branch branch = branchRepository.save(new Branch("Filial Centro"));
        Sector sector = sectorRepository.save(new Sector("Aprendizagem Industrial"));
        Cr cr = crRepository.save(new Cr("TI", "7940", false));
        crBranch = crBranchRepository.save(new CrBranch(branch, cr, List.of(user)));

        waitingStatus = statusRepository.save(new Status("Aguardando aprovação", "Solicitação aguardando aprovação"));
        statusRepository.save(new Status("EM_ANDAMENTO", "Solicitação em andamento"));

        request = requestRepository.save(new Request(crBranch, waitingStatus));
    }

    @AfterEach
    void tearDown() {
        notificationRepository.deleteAll();
        requestRepository.deleteAll();
        crBranchRepository.deleteAll();
        statusRepository.deleteAll();
        crRepository.deleteAll();
        sectorRepository.deleteAll();
        branchRepository.deleteAll();
        userRepository.deleteAll();
    }

    private UsernamePasswordAuthenticationToken authAs(User u) {
        return new UsernamePasswordAuthenticationToken(
                new net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal(u),
                null,
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );
    }

    private Notification criarNotificacao(String title, String message) {
        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setViewed(false);
        notification.setUser(user);
        notification.setRequest(request);
        return notificationRepository.save(notification);
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
        mockMvc.perform(post("/requests")
                        .with(authentication(authAs(user)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "crBranchId": %d,
                                    "statusName": "Aguardando aprovação",
                                    "userIds": []
                                }
                                """.formatted(crBranch.getId())))
                .andExpect(status().isCreated());

        assertEquals(1, notificationRepository.findByUserId(user.getId()).size());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("[Integração] Deve gerar notificação ao mudar o status de uma solicitação (RN-NOT01)")
    void deveGerarNotificacaoAoMudarStatus() throws Exception {
        statusRepository.save(new Status("Em atendimento", "Compra em andamento"));

        String response = mockMvc.perform(post("/requests")
                        .with(authentication(authAs(user)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "crBranchId": %d,
                                    "statusName": "Aguardando aprovação",
                                    "userIds": []
                                }
                                """.formatted(crBranch.getId())))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long requestId = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(put("/requests/{id}", requestId)
                        .with(authentication(authAs(user)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "crBranchId": %d,
                                    "statusName": "Em atendimento",
                                    "userIds": []
                                }
                                """.formatted(crBranch.getId())))
                .andExpect(status().isOk());

        assertEquals(2, notificationRepository.findByUserId(user.getId()).size());
    }
}