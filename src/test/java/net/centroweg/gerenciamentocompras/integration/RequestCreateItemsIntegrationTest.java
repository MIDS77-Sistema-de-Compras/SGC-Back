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
import net.centroweg.gerenciamentocompras.modules.product.domain.MeasurementUnit;
import net.centroweg.gerenciamentocompras.modules.product.domain.Product;
import net.centroweg.gerenciamentocompras.modules.product.infrastructure.persistence.MeasurementUnitRepository;
import net.centroweg.gerenciamentocompras.modules.product.infrastructure.persistence.ProductRepository;
import net.centroweg.gerenciamentocompras.modules.provision.domain.Provision;
import net.centroweg.gerenciamentocompras.modules.provision.infrastructure.persistence.ProvisionRepository;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProduct;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.ItemRequestProductRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.ItemRequestProvisionRepository;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class RequestCreateItemsIntegrationTest {

    private MockMvc mockMvc;

    @Autowired private WebApplicationContext context;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private RequestRepository requestRepository;
    @Autowired private ItemRequestProductRepository itemRequestProductRepository;
    @Autowired private ItemRequestProvisionRepository itemRequestProvisionRepository;
    @Autowired private StatusRepository statusRepository;
    @Autowired private CrBranchRepository crBranchRepository;
    @Autowired private BranchRepository branchRepository;
    @Autowired private CrRepository crRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private MeasurementUnitRepository measurementUnitRepository;
    @Autowired private ProvisionRepository provisionRepository;
    @Autowired private NotificationRepository notificationRepository;

    @MockitoBean
    private NotificationEmailService notificationEmailService;

    private User requester;
    private User responsible;
    private CrBranch crBranch;
    private Provision provision;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .defaultRequest(get("/").with(user("admin").roles("USER", "ADMIN")))
                .build();

        deleteData();

        requester = saveUser("Solicitante", "11144477735", "solicitante@teste.com", "1111");
        responsible = saveUser("Responsavel", "52998224725", "responsavel@teste.com", "2222");

        Branch branch = branchRepository.save(new Branch("Filial Centro"));
        Cr cr = crRepository.save(new Cr("TI", "7940", false));
        crBranch = crBranchRepository.save(new CrBranch(branch, cr, List.of(responsible)));

        statusRepository.save(new Status("EM_ANDAMENTO", "Solicitacao em andamento"));
        productRepository.save(new Product(null, "Parafuso", "Parafuso de teste", 1.0, "Insumo", "PAR-001"));
        measurementUnitRepository.save(new MeasurementUnit("Quilograma", "KG"));
        provision = provisionRepository.save(new Provision("Manutencao", 150.0, "Servico de manutencao"));
    }

    @AfterEach
    void tearDown() {
        deleteData();
    }

    @Test
    @DisplayName("Deve criar Request com produtos")
    void shouldCreateRequestWithProducts() throws Exception {
        String response = mockMvc.perform(post("/requests")
                        .with(authentication(authAs(requester)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productRequestJson("Parafuso", "KG")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statusName").value("EM_ANDAMENTO"))
                .andExpect(jsonPath("$.products.length()").value(1))
                .andExpect(jsonPath("$.products[0].productName").value("Parafuso"))
                .andExpect(jsonPath("$.products[0].measurementUnit").value("Quilograma"))
                .andExpect(jsonPath("$.provisions.length()").value(0))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long requestId = objectMapper.readTree(response).get("id").asLong();
        Request savedRequest = requestRepository.findById(requestId).orElseThrow();
        List<ItemRequestProduct> savedItems = itemRequestProductRepository.findAll();

        assertEquals(requestId, savedRequest.getId());
        assertEquals(1, savedItems.size());
        assertEquals(1, productRepository.count());
        assertEquals(requestId, savedItems.get(0).getRequest().getId());
        assertEquals("EM_ANDAMENTO", savedItems.get(0).getStatus_id().getName());
        assertEquals(1, notificationRepository.findByUserId(responsible.getId()).size());
    }

    @Test
    @DisplayName("Deve criar Request com servicos")
    void shouldCreateRequestWithProvisions() throws Exception {
        String response = mockMvc.perform(post("/requests")
                        .with(authentication(authAs(requester)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(provisionRequestJson(provision.getId())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statusName").value("EM_ANDAMENTO"))
                .andExpect(jsonPath("$.products.length()").value(0))
                .andExpect(jsonPath("$.provisions.length()").value(1))
                .andExpect(jsonPath("$.provisions[0].provisionId").value(provision.getId()))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long requestId = objectMapper.readTree(response).get("id").asLong();

        assertNotNull(requestRepository.findById(requestId).orElseThrow());
        assertEquals(1, itemRequestProvisionRepository.findAllByRequestId(requestId).size());
        assertEquals(1, notificationRepository.findByUserId(responsible.getId()).size());
        verify(notificationEmailService).sendNotificationEmail(anyString(), anyString(), anyString(), anyString(), anyLong());
    }

    @Test
    @DisplayName("Deve retornar erro ao enviar produtos e servicos juntos")
    void shouldRejectProductsAndProvisionsTogether() throws Exception {
        mockMvc.perform(post("/requests")
                        .with(authentication(authAs(requester)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
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
                                    "provisions": [
                                        {
                                            "provisionId": %d,
                                            "additionalInformation": "Servico necessario"
                                        }
                                    ]
                                }
                                """.formatted(crBranch.getId(), provision.getId())))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar erro ao enviar sem produtos e sem servicos")
    void shouldRejectRequestWithoutItems() throws Exception {
        mockMvc.perform(post("/requests")
                        .with(authentication(authAs(requester)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "crBranchId": %d,
                                    "userIds": [],
                                    "products": [],
                                    "provisions": []
                                }
                                """.formatted(crBranch.getId())))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve criar Request e cadastrar produto quando produto nao existir")
    void shouldCreateRequestAndProductWhenProductDoesNotExist() throws Exception {
        String productName = "Produto novo";

        String response = mockMvc.perform(post("/requests")
                        .with(authentication(authAs(requester)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productRequestJson(productName, "KG")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.products.length()").value(1))
                .andExpect(jsonPath("$.products[0].productName").value(productName))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long requestId = objectMapper.readTree(response).get("id").asLong();
        Product createdProduct = productRepository.findByNameIgnoreCase(productName).orElseThrow();
        ItemRequestProduct savedItem = itemRequestProductRepository.findAll().get(0);

        assertEquals(2, productRepository.count());
        assertEquals("Solicitacao", createdProduct.getType());
        assertNotNull(createdProduct.getCode());
        assertEquals(requestId, savedItem.getRequest().getId());
        assertEquals(createdProduct.getId(), savedItem.getProduct().getId());
    }

    @Test
    @DisplayName("Deve retornar erro quando unidade de medida nao existir")
    void shouldRejectUnknownMeasurementUnit() throws Exception {
        mockMvc.perform(post("/requests")
                        .with(authentication(authAs(requester)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productRequestJson("Parafuso", "CX")))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve criar Request e cadastrar servico quando servico nao existir")
    void shouldCreateRequestAndProvisionWhenProvisionDoesNotExist() throws Exception {
        String response = mockMvc.perform(post("/requests")
                        .with(authentication(authAs(requester)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newProvisionRequestJson(99999L)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.products.length()").value(0))
                .andExpect(jsonPath("$.provisions.length()").value(1))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long requestId = objectMapper.readTree(response).get("id").asLong();
        Provision createdProvision = provisionRepository.findAll()
                .stream()
                .filter(savedProvision -> "Instalacao eletrica".equals(savedProvision.getName()))
                .findFirst()
                .orElseThrow();

        assertEquals(2, provisionRepository.count());
        assertEquals("Servico de instalacao eletrica", createdProvision.getDescription());
        assertEquals(1, itemRequestProvisionRepository.findAllByRequestId(requestId).size());
        assertEquals(createdProvision.getId(), itemRequestProvisionRepository.findAllByRequestId(requestId).get(0).getProvision().getId());
        assertEquals(1, notificationRepository.findByUserId(responsible.getId()).size());
        verify(notificationEmailService).sendNotificationEmail(anyString(), anyString(), anyString(), anyString(), anyLong());
    }

    @Test
    @DisplayName("Deve retornar erro quando servico nao existir e dados forem insuficientes")
    void shouldRejectUnknownProvisionWithoutCreationData() throws Exception {
        mockMvc.perform(post("/requests")
                        .with(authentication(authAs(requester)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(provisionRequestJson(99999L)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Dados insuficientes para criar o servi\u00E7o."));
    }

    @Test
    @DisplayName("Deve criar Request e cadastrar servico novo sem provisionId")
    void shouldCreateRequestAndProvisionWhenProvisionIdIsNotProvided() throws Exception {
        String response = mockMvc.perform(post("/requests")
                        .with(authentication(authAs(requester)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newProvisionRequestJsonWithoutId()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.products.length()").value(0))
                .andExpect(jsonPath("$.provisions.length()").value(1))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long requestId = objectMapper.readTree(response).get("id").asLong();
        Provision createdProvision = provisionRepository.findAll()
                .stream()
                .filter(savedProvision -> "Servico de pintura".equals(savedProvision.getName()))
                .findFirst()
                .orElseThrow();

        assertEquals(2, provisionRepository.count());
        assertEquals(1, itemRequestProvisionRepository.findAllByRequestId(requestId).size());
        assertEquals(createdProvision.getId(), itemRequestProvisionRepository.findAllByRequestId(requestId).get(0).getProvision().getId());
    }

    private User saveUser(String name, String cpf, String email, String extension) {
        User user = new User(name, cpf, email, "Senha@123", extension, true);
        user.setRole(roleRepository.save(new Role("ROLE_ADMIN")));
        return userRepository.save(user);
    }

    private UsernamePasswordAuthenticationToken authAs(User user) {
        return new UsernamePasswordAuthenticationToken(
                new UserPrincipal(user),
                null,
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );
    }

    private String productRequestJson(String productName, String measurementUnit) {
        return """
                {
                    "crBranchId": %d,
                    "userIds": [],
                    "products": [
                        {
                            "productName": "%s",
                            "measurementUnit": "%s",
                            "quantity": 10,
                            "additionalInformations": "Comprar com urgencia"
                        }
                    ],
                    "provisions": null
                }
                """.formatted(crBranch.getId(), productName, measurementUnit);
    }

    private String provisionRequestJson(Long provisionId) {
        return """
                {
                    "crBranchId": %d,
                    "userIds": [],
                    "products": null,
                    "provisions": [
                        {
                            "provisionId": %d,
                            "additionalInformation": "Servico necessario para manutencao"
                        }
                    ]
                }
                """.formatted(crBranch.getId(), provisionId);
    }

    private String newProvisionRequestJson(Long provisionId) {
        return """
                {
                    "crBranchId": %d,
                    "userIds": [],
                    "products": null,
                    "provisions": [
                        {
                            "provisionId": %d,
                            "name": "Instalacao eletrica",
                            "totalValue": 1500.00,
                            "description": "Servico de instalacao eletrica",
                            "additionalInformation": "Servico novo necessario para manutencao"
                        }
                    ]
                }
                """.formatted(crBranch.getId(), provisionId);
    }

    private String newProvisionRequestJsonWithoutId() {
        return """
                {
                    "crBranchId": %d,
                    "userIds": [],
                    "products": null,
                    "provisions": [
                        {
                            "name": "Servico de pintura",
                            "totalValue": 800.00,
                            "description": "Pintura da sala de reuniao",
                            "additionalInformation": "Servico novo sem ID informado"
                        }
                    ]
                }
                """.formatted(crBranch.getId());
    }

    private void deleteData() {
        notificationRepository.deleteAll();
        itemRequestProductRepository.deleteAll();
        itemRequestProvisionRepository.deleteAll();
        requestRepository.deleteAll();
        crBranchRepository.deleteAll();
        statusRepository.deleteAll();
        productRepository.deleteAll();
        measurementUnitRepository.deleteAll();
        provisionRepository.deleteAll();
        crRepository.deleteAll();
        branchRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }
}