package net.centroweg.gerenciamentocompras.integration;

import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Branch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Cr;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.BranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrBranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrRepository;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.entity.Delivery;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.entity.DeliveryReceiver;
import net.centroweg.gerenciamentocompras.modules.delivery.infrastructure.persistence.DeliveryRepository;
import net.centroweg.gerenciamentocompras.modules.delivery.infrastructure.persistence.DeliveryReceiverRepository;
import net.centroweg.gerenciamentocompras.modules.product.domain.MeasurementUnit;
import net.centroweg.gerenciamentocompras.modules.product.domain.Product;
import net.centroweg.gerenciamentocompras.modules.product.infrastructure.persistence.MeasurementUnitRepository;
import net.centroweg.gerenciamentocompras.modules.product.infrastructure.persistence.ProductRepository;
import net.centroweg.gerenciamentocompras.modules.provision.domain.Provision;
import net.centroweg.gerenciamentocompras.modules.provision.infrastructure.persistence.ProvisionRepository;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProduct;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProvision;
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
import net.centroweg.gerenciamentocompras.shared.security.authority.Authorities;
import net.centroweg.gerenciamentocompras.shared.audit.infrastructure.persistence.AuditLogRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class DeliveryIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private DeliveryReceiverRepository deliveryReceiverRepository;

    @Autowired
    private DataSource dataSource;

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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MeasurementUnitRepository measurementUnitRepository;

    @Autowired
    private ProvisionRepository provisionRepository;

    @Autowired
    private ItemRequestProductRepository itemRequestProductRepository;

    @Autowired
    private ItemRequestProvisionRepository itemRequestProvisionRepository;

    private Status pendingStatus;
    private Status deliveredStatus;
    private Request request;
    private User comprador;
    private User docente;
    private User firstReceiver;
    private User secondReceiver;
    private User outsider;

    @BeforeEach
    void setUp() {
        cleanDatabase();

        Role compradorRole = roleRepository.save(new Role(Authorities.COMPRADOR));
        Role docenteRole = roleRepository.save(new Role(Authorities.DOCENTE));

        comprador = userRepository.save(userEntity("Comprador", "comprador@teste.com", "52998224725", compradorRole));
        docente = userRepository.save(userEntity("Docente", "docente@teste.com", "11144477735", docenteRole));
        firstReceiver = userRepository.save(userEntity("Primeiro", "primeiro@teste.com", "15350946056", docenteRole));
        secondReceiver = userRepository.save(userEntity("Segundo", "segundo@teste.com", "98765432000", docenteRole));
        outsider = userRepository.save(userEntity("Fora", "fora@teste.com", "93541134780", docenteRole));

        Branch branch = branchRepository.save(new Branch("Filial Centro"));
        Cr cr = crRepository.save(new Cr("TI", "7940", false));
        CrBranch crBranch = crBranchRepository.save(new CrBranch(branch, cr, null));

        pendingStatus = statusRepository.save(new Status("EM_ANDAMENTO", "Em andamento"));
        deliveredStatus = statusRepository.save(new Status("Entregue", "Entrega concluida"));

        request = requestRepository.save(new Request(crBranch, pendingStatus));
    }

    @AfterEach
    void tearDown() {
        cleanDatabase();
    }

    @Test
    @DisplayName("POST valido retorna 201 e persiste dois recebedores")
    void shouldCreateDelivery() throws Exception {
        mockMvc.perform(post("/deliveries")
                        .with(user(new UserPrincipal(comprador)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody(firstReceiver.getId(), secondReceiver.getId())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.requestId").value(request.getId()))
                .andExpect(jsonPath("$.receivers.length()").value(2));

        Long deliveryId = deliveryRepository.findAll().get(0).getId();
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow();
        assertThat(delivery.getReceivers()).hasSize(2);
    }

    @Test
    @DisplayName("GET por ID retorna 200 e GET inexistente retorna 404")
    void shouldFindDeliveryById() throws Exception {
        Delivery delivery = createDelivery();

        mockMvc.perform(get("/deliveries/{id}", delivery.getId())
                        .with(user(new UserPrincipal(comprador))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(delivery.getId()));

        mockMvc.perform(get("/deliveries/{id}", 9999L)
                        .with(user(new UserPrincipal(comprador))))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT atualiza dados e recebedores")
    void shouldUpdateDelivery() throws Exception {
        Delivery delivery = createDelivery();

        mockMvc.perform(put("/deliveries/{id}", delivery.getId())
                        .with(user(new UserPrincipal(comprador)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateBody(firstReceiver.getId(), outsider.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deliveryLocation").value("Almoxarifado"))
                .andExpect(jsonPath("$.receivers.length()").value(2));
    }

    @Test
    @DisplayName("Recebedor associado confirma entrega")
    void shouldConfirmReceiver() throws Exception {
        Delivery delivery = createDelivery();

        mockMvc.perform(patch("/deliveries/{deliveryId}/receivers/{userId}/confirm", delivery.getId(), firstReceiver.getId())
                        .with(user(new UserPrincipal(firstReceiver)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(confirmBody()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.receivers[0].confirmed").value(true));

        Delivery persisted = deliveryRepository.findById(delivery.getId()).orElseThrow();
        assertThat(persisted.getReceivers().getFirst().getConfirmedAt()).isNotNull();
        assertThat(persisted.getDeliveredAt()).isNull();
        assertThat(auditLogRepository.findAll()).anyMatch(log ->
                "CONFIRMAR_RECEBIMENTO_ENTREGA".equals(log.getTypeAction()));
    }

    @Test
    @DisplayName("Ultimo recebedor finaliza entrega e repeticao preserva timestamps")
    void shouldCompleteDeliveryAndKeepTimestampsOnRepeatedConfirmation() throws Exception {
        Delivery delivery = createDelivery();
        confirm(delivery, firstReceiver, " primeiro ").andExpect(status().isOk());
        confirm(delivery, secondReceiver, " segundo ").andExpect(status().isOk())
                .andExpect(jsonPath("$.statusName").value("Entregue"))
                .andExpect(jsonPath("$.deliveredAt").isNotEmpty());

        Delivery completed = deliveryRepository.findById(delivery.getId()).orElseThrow();
        LocalDateTime deliveredAt = completed.getDeliveredAt();
        LocalDateTime confirmedAt = completed.getReceivers().stream()
                .filter(receiver -> receiver.getUser().getId().equals(secondReceiver.getId()))
                .findFirst().orElseThrow().getConfirmedAt();

        confirm(delivery, secondReceiver, "alterar").andExpect(status().isOk());
        Delivery repeated = deliveryRepository.findById(delivery.getId()).orElseThrow();
        assertThat(repeated.getDeliveredAt()).isEqualTo(deliveredAt);
        assertThat(repeated.getReceivers().stream()
                .filter(receiver -> receiver.getUser().getId().equals(secondReceiver.getId()))
                .findFirst().orElseThrow().getConfirmedAt()).isEqualTo(confirmedAt);
    }

    @Test
    @DisplayName("Status Entregue ausente retorna 404 e nao deixa confirmacao parcial")
    void shouldRollbackLastConfirmationWhenDeliveredStatusIsMissing() throws Exception {
        Delivery delivery = createDelivery();
        confirm(delivery, firstReceiver, null).andExpect(status().isOk());
        statusRepository.deleteById(deliveredStatus.getId());

        confirm(delivery, secondReceiver, null).andExpect(status().isNotFound());

        Delivery persisted = deliveryRepository.findById(delivery.getId()).orElseThrow();
        assertThat(persisted.getDeliveredAt()).isNull();
        assertThat(persisted.getReceivers().stream()
                .filter(receiver -> receiver.getUser().getId().equals(secondReceiver.getId()))
                .findFirst().orElseThrow().getConfirmed()).isFalse();
    }

    @Test
    @DisplayName("Usuario autenticado nao confirma por outro recebedor")
    void shouldRejectConfirmationOnBehalfOfAnotherReceiver() throws Exception {
        Delivery delivery = createDelivery();
        mockMvc.perform(patch("/deliveries/{deliveryId}/receivers/{userId}/confirm", delivery.getId(), secondReceiver.getId())
                        .with(user(new UserPrincipal(firstReceiver)))
                        .contentType(MediaType.APPLICATION_JSON).content(confirmBody()))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Confirmacoes concorrentes finalizam a entrega exatamente uma vez")
    void shouldCompleteDeliveryWithConcurrentConfirmations() throws Exception {
        Delivery delivery = createDelivery();
        CountDownLatch start = new CountDownLatch(1);
        try (var executor = Executors.newFixedThreadPool(2)) {
            CompletableFuture<Integer> first = concurrentConfirmation(delivery, firstReceiver, start, executor);
            CompletableFuture<Integer> second = concurrentConfirmation(delivery, secondReceiver, start, executor);
            start.countDown();
            assertThat(first.join()).isEqualTo(200);
            assertThat(second.join()).isEqualTo(200);
        }

        Delivery persisted = deliveryRepository.findById(delivery.getId()).orElseThrow();
        assertThat(persisted.getReceivers()).allMatch(receiver -> Boolean.TRUE.equals(receiver.getConfirmed()));
        assertThat(persisted.getStatusId()).isEqualTo(deliveredStatus.getId());
        assertThat(persisted.getDeliveredAt()).isNotNull();
    }

    @Test
    @DisplayName("Usuario nao associado nao pode confirmar")
    void shouldRejectConfirmationByUserNotAssociated() throws Exception {
        Delivery delivery = createDelivery();

        mockMvc.perform(patch("/deliveries/{deliveryId}/receivers/{userId}/confirm", delivery.getId(), outsider.getId())
                        .with(user(new UserPrincipal(outsider)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(confirmBody()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE realiza soft delete")
    void shouldSoftDeleteDelivery() throws Exception {
        Delivery delivery = createDelivery();

        mockMvc.perform(delete("/deliveries/{id}", delivery.getId())
                        .with(user(new UserPrincipal(comprador))))
                .andExpect(status().isNoContent());

        Delivery inactive = deliveryRepository.findById(delivery.getId()).orElseThrow();
        assertThat(inactive.getActive()).isFalse();
        assertThat(inactive.getReceivers()).hasSize(2);
    }

    @Test
    @DisplayName("Restricao unica impede usuario duplicado na mesma entrega")
    void shouldRejectDuplicatedReceiverUniqueConstraint() {
        Delivery delivery = createDelivery();
        delivery.getReceivers().add(new DeliveryReceiver(delivery, firstReceiver));

        assertThatThrownBy(() -> deliveryRepository.saveAndFlush(delivery))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("Chave composta localiza recebedores e permite o mesmo usuario em entregas diferentes")
    void shouldPersistAndFindCompositeReceiverIdsAcrossDeliveries() {
        Delivery firstDelivery = createDelivery();
        Delivery secondDelivery = createDelivery();

        var firstLink = deliveryReceiverRepository
                .findByIdDeliveryIdAndIdUserId(firstDelivery.getId(), firstReceiver.getId())
                .orElseThrow();
        var secondLink = deliveryReceiverRepository
                .findByIdDeliveryIdAndIdUserId(secondDelivery.getId(), firstReceiver.getId())
                .orElseThrow();

        assertThat(firstLink.getId().getDeliveryId()).isEqualTo(firstDelivery.getId());
        assertThat(firstLink.getId().getUserId()).isEqualTo(firstReceiver.getId());
        assertThat(secondLink.getId().getDeliveryId()).isEqualTo(secondDelivery.getId());
        assertThat(deliveryReceiverRepository.findAll()).hasSize(4);
    }

    @Test
    @DisplayName("Hibernate cria chave composta e tabelas de associacao no schema limpo")
    void shouldCreateCompositeKeyAndItemLinkTablesFromJpaMappings() throws Exception {
        try (var connection = dataSource.getConnection()) {
            var metadata = connection.getMetaData();
            var primaryKeyColumns = new HashSet<String>();
            try (var keys = metadata.getPrimaryKeys(null, null, "DELIVERY_RECEIVER")) {
                while (keys.next()) {
                    primaryKeyColumns.add(keys.getString("COLUMN_NAME").toLowerCase(Locale.ROOT));
                }
            }

            assertThat(primaryKeyColumns).containsExactlyInAnyOrder("delivery_id", "user_id");
            assertThat(tableExists(metadata, "DELIVERY_ITEM_REQUEST_PRODUCT")).isTrue();
            assertThat(tableExists(metadata, "DELIVERY_ITEM_REQUEST_SERVICE")).isTrue();
        }
    }

    @Test
    @DisplayName("Entrega persiste produtos e servicos sem duplicar associacoes")
    void shouldPersistSelectedProductAndProvisionItemsWithoutDuplicates() throws Exception {
        ItemRequestProduct productItem = createProductItem(request, "PRD-ENTREGA");
        ItemRequestProvision provisionItem = createProvisionItem(request, "Instalacao");

        mockMvc.perform(post("/deliveries")
                        .with(user(new UserPrincipal(comprador)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBodyWithItems(
                                request.getId(),
                                productItem.getId() + ", " + productItem.getId(),
                                provisionItem.getId() + ", " + provisionItem.getId()
                        )))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productItemIds.length()").value(1))
                .andExpect(jsonPath("$.provisionItemIds.length()").value(1));

        assertThat(linkCount("delivery_item_request_product")).isEqualTo(1);
        assertThat(linkCount("delivery_item_request_service")).isEqualTo(1);
    }

    @Test
    @DisplayName("Entrega rejeita item inexistente ou pertencente a outra solicitacao")
    void shouldRejectMissingOrForeignRequestItems() throws Exception {
        Request otherRequest = requestRepository.save(new Request(request.getCrBranch(), pendingStatus));
        ItemRequestProduct foreignItem = createProductItem(otherRequest, "PRD-OUTRA");

        mockMvc.perform(post("/deliveries")
                        .with(user(new UserPrincipal(comprador)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBodyWithItems(request.getId(), foreignItem.getId().toString(), "")))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/deliveries")
                        .with(user(new UserPrincipal(comprador)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBodyWithItems(request.getId(), "999999", "")))
                .andExpect(status().isBadRequest());

        assertThat(deliveryRepository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("Seguranca diferencia 401, 403 e role correta")
    void shouldProtectDeliveryCreation() throws Exception {
        mockMvc.perform(post("/deliveries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody(firstReceiver.getId(), secondReceiver.getId())))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(post("/deliveries")
                        .with(user(new UserPrincipal(docente)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody(firstReceiver.getId(), secondReceiver.getId())))
                .andExpect(status().isForbidden());

        mockMvc.perform(post("/deliveries")
                        .with(user(new UserPrincipal(comprador)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody(firstReceiver.getId(), secondReceiver.getId())))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Validacao rejeita quantidade diferente de dois recebedores")
    void shouldRejectInvalidReceiverCount() throws Exception {
        mockMvc.perform(post("/deliveries")
                        .with(user(new UserPrincipal(comprador)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "requestId": %d,
                                    "statusId": %d,
                                    "expectedDeliveryAt": "%s",
                                    "deliveryLocation": "Portaria",
                                    "receiverIds": [%d]
                                }
                                """.formatted(request.getId(), pendingStatus.getId(), LocalDateTime.now().plusDays(1), firstReceiver.getId())))
                .andExpect(status().isBadRequest());
    }

    private Delivery createDelivery() {
        Delivery delivery = new Delivery();
        delivery.setRequest(request);
        delivery.setStatusId(pendingStatus.getId());
        delivery.setExpectedDeliveryAt(LocalDateTime.now().plusDays(1));
        delivery.setDeliveryLocation("Portaria");
        Delivery saved = deliveryRepository.saveAndFlush(delivery);
        saved.addReceiver(firstReceiver);
        saved.addReceiver(secondReceiver);
        return deliveryRepository.saveAndFlush(saved);
    }

    private String createBody(Long firstReceiverId, Long secondReceiverId) {
        return """
                {
                    "requestId": %d,
                    "statusId": %d,
                    "expectedDeliveryAt": "%s",
                    "deliveryLocation": "Portaria",
                    "description": "Entrega de materiais",
                    "proofUrl": "https://example.com/prova.pdf",
                    "receiverIds": [%d, %d]
                }
                """.formatted(request.getId(), pendingStatus.getId(), LocalDateTime.now().plusDays(1), firstReceiverId, secondReceiverId);
    }

    private String updateBody(Long firstReceiverId, Long secondReceiverId) {
        return """
                {
                    "statusId": %d,
                    "expectedDeliveryAt": "%s",
                    "deliveredAt": null,
                    "deliveryLocation": "Almoxarifado",
                    "description": "Entrega atualizada",
                    "proofUrl": "https://example.com/prova-atualizada.pdf",
                    "receiverIds": [%d, %d]
                }
                """.formatted(pendingStatus.getId(), LocalDateTime.now().plusDays(2), firstReceiverId, secondReceiverId);
    }

    private String createBodyWithItems(Long requestId, String productItemIds, String provisionItemIds) {
        return """
                {
                    "requestId": %d,
                    "statusId": %d,
                    "expectedDeliveryAt": "%s",
                    "deliveryLocation": "Portaria",
                    "description": "Entrega com itens",
                    "receiverIds": [%d, %d],
                    "productItemIds": [%s],
                    "provisionItemIds": [%s]
                }
                """.formatted(requestId, pendingStatus.getId(), LocalDateTime.now().plusDays(1),
                firstReceiver.getId(), secondReceiver.getId(), productItemIds, provisionItemIds);
    }

    private String confirmBody() {
        return """
                {
                    "observation": "Recebimento conferido e sem avarias."
                }
                """;
    }

    private org.springframework.test.web.servlet.ResultActions confirm(Delivery delivery, User receiver, String observation) throws Exception {
        String body = observation == null ? "{\"observation\": null}" : "{\"observation\": \"" + observation + "\"}";
        return mockMvc.perform(patch("/deliveries/{deliveryId}/receivers/{userId}/confirm", delivery.getId(), receiver.getId())
                .with(user(new UserPrincipal(receiver)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));
    }

    private CompletableFuture<Integer> concurrentConfirmation(
            Delivery delivery,
            User receiver,
            CountDownLatch start,
            java.util.concurrent.Executor executor
    ) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                start.await();
                return mockMvc.perform(patch("/deliveries/{deliveryId}/receivers/{userId}/confirm", delivery.getId(), receiver.getId())
                                .with(user(new UserPrincipal(receiver)))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"observation\": \"concorrente\"}"))
                        .andReturn().getResponse().getStatus();
            } catch (Exception exception) {
                throw new IllegalStateException(exception);
            }
        }, executor);
    }

    private User userEntity(String name, String email, String cpf, Role role) {
        User user = new User(name, cpf, email, "Senha@123", "1234", true);
        user.setRole(role);
        return user;
    }

    private boolean tableExists(java.sql.DatabaseMetaData metadata, String tableName) throws Exception {
        try (var tables = metadata.getTables(null, null, tableName, new String[]{"TABLE"})) {
            return tables.next();
        }
    }

    private int linkCount(String tableName) throws Exception {
        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement();
             var result = statement.executeQuery("select count(*) from " + tableName)) {
            result.next();
            return result.getInt(1);
        }
    }

    private ItemRequestProduct createProductItem(Request owner, String code) {
        Product product = productRepository.save(Product.builder()
                .name("Produto " + code)
                .description("Produto de teste")
                .price(10.0)
                .type("Material")
                .code(code)
                .build());
        MeasurementUnit unit = measurementUnitRepository.save(new MeasurementUnit("Unidade " + code, "UN"));
        ItemRequestProduct item = new ItemRequestProduct();
        item.setRequest(owner);
        item.setProduct(product);
        item.setMeasurementUnit(unit);
        item.setQuantity(2.0);
        item.setStatus_id(pendingStatus);
        return itemRequestProductRepository.save(item);
    }

    private ItemRequestProvision createProvisionItem(Request owner, String name) {
        Provision provision = provisionRepository.save(new Provision(name, 100.0, "Servico de teste"));
        return itemRequestProvisionRepository.save(new ItemRequestProvision(owner, provision, pendingStatus, "Agendar"));
    }

    private void cleanDatabase() {
        auditLogRepository.deleteAll();
        deliveryRepository.deleteAll();
        itemRequestProductRepository.deleteAll();
        itemRequestProvisionRepository.deleteAll();
        requestRepository.deleteAll();
        productRepository.deleteAll();
        measurementUnitRepository.deleteAll();
        provisionRepository.deleteAll();
        crBranchRepository.deleteAll();
        crRepository.deleteAll();
        branchRepository.deleteAll();
        statusRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }
}
