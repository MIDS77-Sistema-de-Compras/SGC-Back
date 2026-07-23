package net.centroweg.gerenciamentocompras.integration;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Branch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Cr;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.BranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrBranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrRepository;
import net.centroweg.gerenciamentocompras.modules.product.domain.MeasurementUnit;
import net.centroweg.gerenciamentocompras.modules.product.domain.Product;
import net.centroweg.gerenciamentocompras.modules.product.infrastructure.persistence.MeasurementUnitRepository;
import net.centroweg.gerenciamentocompras.modules.product.infrastructure.persistence.ProductRepository;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProduct;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.ItemRequestProductRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.StatusRepository;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.RoleRepository;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.modules.notification.infrastructure.email.NotificationEmailService;
import net.centroweg.gerenciamentocompras.shared.email.service.EmailSenderService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

/**
 * Reproducao isolada do fluxo real do frontend do comprador: PUT /item-request-products/{id}
 * mudando o status de um item, verificando se o item E a solicitacao refletem o novo status.
 */
@SpringBootTest
@ActiveProfiles("test")
class ItemRequestProductStatusFlowIntegrationTest {

    @Autowired private WebApplicationContext context;
    @Autowired private RequestRepository requestRepository;
    @Autowired private StatusRepository statusRepository;
    @Autowired private CrBranchRepository crBranchRepository;
    @Autowired private BranchRepository branchRepository;
    @Autowired private CrRepository crRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private MeasurementUnitRepository measurementUnitRepository;
    @Autowired private ItemRequestProductRepository itemRequestProductRepository;
    @Autowired private JdbcTemplate jdbcTemplate;

    @MockitoBean private NotificationEmailService notificationEmailService;
    @MockitoBean private EmailSenderService emailSenderService;

    private MockMvc mockMvc;
    private CrBranch crBranch;
    private Status approved;
    private Status delivered;
    private User buyer;
    private Product product;
    private MeasurementUnit unit;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        cleanDatabase();

        User requester = saveUser("Solicitante", "solicitante@teste.com", "52998224725", "DOCENTE");
        buyer = saveUser("Comprador", "comprador@teste.com", "98765432101", "COMPRADOR");

        Branch branch = branchRepository.save(new Branch("Filial Centro"));
        Cr cr = crRepository.save(new Cr("TI", "7940", false));
        crBranch = crBranchRepository.save(new CrBranch(branch, cr, List.of(requester)));

        approved = statusRepository.save(new Status("Aprovado", "Solicitacao aprovada"));
        delivered = statusRepository.save(new Status("Entregue", "Solicitacao entregue"));

        product = productRepository.save(Product.builder()
                .name("Parafuso M8")
                .description("desc")
                .price(1.0)
                .type("Insumo")
                .code("PRD-001")
                .build());
        unit = measurementUnitRepository.save(new MeasurementUnit("Unidade", "UN"));

        Request request = new Request(crBranch, approved);
        request.setRequestDate(LocalDateTime.of(2026, 6, 26, 10, 0));
        request.setUpdatedAt(LocalDateTime.of(2026, 6, 26, 10, 5));
        request.setActive(true);
        request.getCreatedByUsers().add(requester);
        this.request = requestRepository.save(request);

        ItemRequestProduct item = new ItemRequestProduct();
        item.setRequest(this.request);
        item.setProduct(product);
        item.setMeasurementUnit(unit);
        item.setQuantity(10.0);
        item.setStatus_id(approved);
        item.setAdditionalInformations(null);
        this.item = itemRequestProductRepository.save(item);
    }

    private Request request;
    private ItemRequestProduct item;

    @AfterEach
    void tearDown() {
        cleanDatabase();
    }

    @Test
    @DisplayName("[Integracao] PUT /item-request-products/{id} para Entregue deve persistir no item e refletir na solicitacao")
    void shouldPersistItemStatusAndCascadeToRequest() throws Exception {
        String payload = """
                {
                    "requestId": %d,
                    "productName": "%s",
                    "measurementUnit": "%s",
                    "quantity": 10.0,
                    "statusName": "Entregue",
                    "additionalInformations": ""
                }
                """.formatted(request.getId(), product.getName(), unit.getName());

        mockMvc.perform(put("/item-request-products/{id}", item.getId())
                        .with(user(new UserPrincipal(buyer)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusName").value("Entregue"));

        Long itemStatusId = jdbcTemplate.queryForObject(
                "select status_id_id from item_request_product where id = ?", Long.class, item.getId());
        assertThat(itemStatusId).isEqualTo(delivered.getId());

        Long requestStatusId = jdbcTemplate.queryForObject(
                "select status_id from requests where id = ?", Long.class, request.getId());
        assertThat(requestStatusId).isEqualTo(delivered.getId());
    }

    private User saveUser(String name, String email, String cpf, String roleName) {
        User user = new User(name, cpf, email, "Senha@123", "1234", true);
        user.setRole(roleRepository.save(new Role(roleName)));
        return userRepository.save(user);
    }

    private void cleanDatabase() {
        itemRequestProductRepository.deleteAll();
        requestRepository.deleteAll();
        crBranchRepository.deleteAll();
        statusRepository.deleteAll();
        crRepository.deleteAll();
        branchRepository.deleteAll();
        productRepository.deleteAll();
        measurementUnitRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }
}
