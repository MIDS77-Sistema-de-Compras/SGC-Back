package net.centroweg.gerenciamentocompras.integration;

import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
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
import net.centroweg.gerenciamentocompras.modules.product.domain.MeasurementUnit;
import net.centroweg.gerenciamentocompras.modules.product.domain.Product;
import net.centroweg.gerenciamentocompras.modules.product.infrastructure.persistence.MeasurementUnitRepository;
import net.centroweg.gerenciamentocompras.modules.product.infrastructure.persistence.ProductRepository;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.StatusRepository;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@WithMockUser
class NotificationIntegrationTest {

    private MockMvc mockMvc;

    @Autowired private WebApplicationContext context;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private NotificationRepository notificationRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private RequestRepository requestRepository;
    @Autowired private StatusRepository statusRepository;
    @Autowired private CrBranchRepository crBranchRepository;
    @Autowired private BranchRepository branchRepository;
    @Autowired private SectorRepository sectorRepository;
    @Autowired private CrRepository crRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private MeasurementUnitRepository measurementUnitRepository;

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

        deleteData();

        user = new User("Admin Teste", CPF_VALIDO, "admin@teste.com", "Senha@123", "1234", true);
        user.setRole(new Role("ROLE_ADMIN"));
        user = userRepository.save(user);

        Branch branch = branchRepository.save(new Branch("Filial Centro"));
        Sector sector = sectorRepository.save(new Sector("Aprendizagem Industrial"));
        Cr cr = crRepository.save(new Cr("TI", "7940", false));
        crBranch = crBranchRepository.save(new CrBranch(branch, cr, List.of(user)));

        waitingStatus = statusRepository.save(new Status("Aguardando aprovacao", "Solicitacao aguardando aprovacao"));
        statusRepository.save(new Status("EM_ANDAMENTO", "Solicitacao em andamento"));

        productRepository.save(new Product(null, "Parafuso", "Parafuso de teste", 1.0, "Insumo", "PAR-001"));
        measurementUnitRepository.save(new MeasurementUnit("UN", "UN"));

        request = requestRepository.save(new Request(crBranch, waitingStatus));
    }

    @AfterEach
    void tearDown() {
        deleteData();
    }

    private void deleteData() {
        notificationRepository.deleteAll();
        requestRepository.deleteAll();
        productRepository.deleteAll();
        measurementUnitRepository.deleteAll();
        crBranchRepository.deleteAll();
        statusRepository.deleteAll();
        crRepository.deleteAll();
        sectorRepository.deleteAll();
        branchRepository.deleteAll();
        userRepository.deleteAll();
    }

    private UsernamePasswordAuthenticationToken authAs(User u) {
        return new UsernamePasswordAuthenticationToken(
                new UserPrincipal(u),
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
    @DisplayName("[Integracao] Deve listar as notificacoes de um usuario")
    void deveListarNotificacoesPorUsuario() throws Exception {
        criarNotificacao("Status atualizado", "Sua solicitacao foi aprovada");
        criarNotificacao("Nova solicitacao", "Ha uma nova solicitacao no seu CR");

        mockMvc.perform(get("/notifications/user/{userId}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].userId").value(user.getId()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("[Integracao] Deve retornar lista vazia quando usuario nao tem notificacoes")
    void deveRetornarListaVaziaSemNotificacoes() throws Exception {
        mockMvc.perform(get("/notifications/user/{userId}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("[Integracao] Deve marcar uma notificacao como lida")
    void deveMarcarNotificacaoComoLida() throws Exception {
        Notification saved = criarNotificacao("Status atualizado", "Sua solicitacao foi aprovada");

        mockMvc.perform(patch("/notifications/{id}/viewed", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.viewed").value(true));

        Notification updated = notificationRepository.findById(saved.getId()).orElseThrow();
        assertTrue(updated.getViewed());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("[Integracao] Deve retornar 404 ao marcar notificacao inexistente como lida")
    void deveRetornarNotFoundParaNotificacaoInexistente() throws Exception {
        mockMvc.perform(patch("/notifications/{id}/viewed", 9999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("[Integracao] Deve gerar notificacao ao criar uma solicitacao")
    void deveGerarNotificacaoAoCriarSolicitacao() throws Exception {
        mockMvc.perform(post("/requests")
                        .with(authentication(authAs(user)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productRequestJson(crBranch.getId())))
                .andExpect(status().isCreated());

        assertEquals(1, notificationRepository.findByUserId(user.getId()).size());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("[Integracao] Deve gerar notificacao ao mudar o status de uma solicitacao")
    void deveGerarNotificacaoAoMudarStatus() throws Exception {
        statusRepository.save(new Status("Em atendimento", "Compra em andamento"));

        String response = mockMvc.perform(post("/requests")
                        .with(authentication(authAs(user)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productRequestJson(crBranch.getId())))
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

    private String productRequestJson(Long crBranchId) {
        return """
                {
                    "crBranchId": %d,
                    "userIds": [],
                    "products": [
                        {
                            "productName": "Parafuso",
                            "measurementUnit": "UN",
                            "quantity": 10,
                            "additionalInformations": "Comprar com urgencia"
                        }
                    ],
                    "provisions": null
                }
                """.formatted(crBranchId);
    }
}