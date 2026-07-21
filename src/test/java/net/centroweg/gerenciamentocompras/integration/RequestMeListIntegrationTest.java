package net.centroweg.gerenciamentocompras.integration;

import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Branch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Cr;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.BranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrBranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrRepository;
import net.centroweg.gerenciamentocompras.modules.notification.infrastructure.email.NotificationEmailService;
import net.centroweg.gerenciamentocompras.modules.notification.infrastructure.persistence.NotificationRepository;
import net.centroweg.gerenciamentocompras.modules.product.domain.MeasurementUnit;
import net.centroweg.gerenciamentocompras.modules.product.domain.Product;
import net.centroweg.gerenciamentocompras.modules.product.infrastructure.persistence.MeasurementUnitRepository;
import net.centroweg.gerenciamentocompras.modules.product.infrastructure.persistence.ProductRepository;
import net.centroweg.gerenciamentocompras.modules.provision.domain.Provision;
import net.centroweg.gerenciamentocompras.modules.provision.infrastructure.persistence.ProvisionRepository;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProvision;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProduct;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.ItemRequestProductRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.ItemRequestProvisionRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.StatusRepository;
import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
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
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
class RequestMeListIntegrationTest {

    private static final String REQUESTER_EMAIL = "joao@teste.com";

    @Autowired private WebApplicationContext context;
    @Autowired private NotificationRepository notificationRepository;
    @Autowired private ItemRequestProductRepository itemRequestProductRepository;
    @Autowired private ItemRequestProvisionRepository itemRequestProvisionRepository;
    @Autowired private RequestRepository requestRepository;
    @Autowired private StatusRepository statusRepository;
    @Autowired private CrBranchRepository crBranchRepository;
    @Autowired private BranchRepository branchRepository;
    @Autowired private CrRepository crRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private ProvisionRepository provisionRepository;
    @Autowired private MeasurementUnitRepository measurementUnitRepository;
    @Autowired private RoleRepository roleRepository;

    @MockitoBean private NotificationEmailService notificationEmailService;

    private MockMvc mockMvc;
    private User joao;
    private User maria;
    private CrBranch crBranch;
    private Status pending;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        cleanDatabase();

        Role docente = roleRepository.save(new Role("DOCENTE"));

        joao = new User("Joao Silva", "52998224725", REQUESTER_EMAIL, "Senha@123", "1234", true);
        joao.setRole(docente);
        joao = userRepository.save(joao);
        // Solicitante distinto, para garantir que /me filtra pelo usuário logado.
        maria = new User("Maria Souza", "12345678909", "maria@teste.com", "Senha@123", "4321", true);
        maria.setRole(docente);
        maria = userRepository.save(maria);

        Branch branch = branchRepository.save(new Branch("Filial Centro"));
        Cr cr = crRepository.save(new Cr("CR Tecnologia", "7940", false));
        crBranch = crBranchRepository.save(new CrBranch(branch, cr, List.of(joao)));

        pending = statusRepository.save(new Status("Pendente", "Solicitacao pendente"));

        Product product = productRepository.save(
                new Product(null, "Parafuso M8", "Rosca fina", 1.0, "Insumo", "PRD-001")
        );
        MeasurementUnit unit = measurementUnitRepository.save(new MeasurementUnit("Unidade", "UN"));

        Request request = new Request(crBranch, pending);
        request.setRequestDate(LocalDateTime.of(2026, 6, 15, 10, 30));
        request.setUpdatedAt(LocalDateTime.of(2026, 6, 15, 10, 30));
        request.setActive(true);
        request.setCreatedByUsers(new ArrayList<>(List.of(joao)));
        Request savedRequest = requestRepository.save(request);

        itemRequestProductRepository.save(
                new ItemRequestProduct(null, savedRequest, product, unit, 2.0, null, pending, "obs")
        );
    }

    @AfterEach
    void tearDown() {
        cleanDatabase();
    }

    @Test
    @DisplayName("[Integração] GET /requests/me devolve o DTO enxuto com nomes dos itens e sem coleções pesadas")
    void shouldReturnLeanListItemsForLoggedUser() throws Exception {
        mockMvc.perform(get("/requests/me")
                        .with(user(new UserPrincipal(joao)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].crCode").value("7940"))
                .andExpect(jsonPath("$.content[0].statusName").value("Pendente"))
                .andExpect(jsonPath("$.content[0].productNames[0]").value("Parafuso M8"))
                .andExpect(jsonPath("$.content[0].provisionNames").isEmpty())
                // O DTO enxuto não expõe as coleções pesadas da listagem.
                .andExpect(jsonPath("$.content[0].provisions").doesNotExist())
                .andExpect(jsonPath("$.content[0].attachments").doesNotExist());
    }

    @Test
    @DisplayName("[Integração] GET /requests/me devolve nomes e quantidade de serviços")
    void shouldReturnProvisionNamesForServiceRequest() throws Exception {
        itemRequestProductRepository.deleteAll();
        requestRepository.deleteAll();

        Request serviceRequest = new Request(crBranch, pending);
        serviceRequest.setRequestDate(LocalDateTime.of(2026, 6, 16, 10, 30));
        serviceRequest.setActive(true);
        serviceRequest.setCreatedByUsers(new ArrayList<>(List.of(joao)));
        serviceRequest = requestRepository.save(serviceRequest);

        for (String name : List.of("Instalação", "Manutenção", "Treinamento")) {
            Provision provision = provisionRepository.save(
                    new Provision(name, 100.0, "Serviço solicitado")
            );
            itemRequestProvisionRepository.save(
                    new ItemRequestProvision(serviceRequest, provision, pending, null)
            );
        }

        mockMvc.perform(get("/requests/me")
                        .with(user(new UserPrincipal(joao)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].productNames").isEmpty())
                .andExpect(jsonPath("$.content[0].provisionNames.length()").value(3))
                .andExpect(jsonPath("$.content[0].provisionNames[0]").value("Instalação"))
                .andExpect(jsonPath("$.content[0].provisionNames[1]").value("Manutenção"))
                .andExpect(jsonPath("$.content[0].provisionNames[2]").value("Treinamento"));
    }

    @Test
    @DisplayName("[Integração] GET /requests/me só traz as solicitações do próprio usuário")
    void shouldNotReturnRequestsFromOtherUsers() throws Exception {
        mockMvc.perform(get("/requests/me")
                        .with(user(new UserPrincipal(maria)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(0));
    }

    private void cleanDatabase() {
        itemRequestProvisionRepository.deleteAll();
        itemRequestProductRepository.deleteAll();
        notificationRepository.deleteAll();
        requestRepository.deleteAll();
        crBranchRepository.deleteAll();
        statusRepository.deleteAll();
        crRepository.deleteAll();
        branchRepository.deleteAll();
        productRepository.deleteAll();
        provisionRepository.deleteAll();
        measurementUnitRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }
}
