package net.centroweg.gerenciamentocompras.integration;

import net.centroweg.gerenciamentocompras.modules.product.domain.MeasurementUnit;
import net.centroweg.gerenciamentocompras.modules.provision.domain.Provision;
import net.centroweg.gerenciamentocompras.modules.provision.infrastructure.persistence.ProvisionRepository;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProduct;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProvision;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.ItemRequestProvisionRepository;
import net.centroweg.gerenciamentocompras.modules.product.domain.Product;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.StatusRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.ItemRequestProductRepository;
import net.centroweg.gerenciamentocompras.modules.product.infrastructure.persistence.ProductRepository;
import net.centroweg.gerenciamentocompras.modules.product.infrastructure.persistence.MeasurementUnitRepository;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Branch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Cr;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.BranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrRepository;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrBranchRepository;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.RoleRepository;
import org.springframework.context.ApplicationEventPublisher;
import net.centroweg.gerenciamentocompras.modules.request.service.event.ItemStatusChangedEvent;
import net.centroweg.gerenciamentocompras.modules.request.service.event.RequestItemType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
class MonitorItemStatusChangedIntegrationTest {

    @Autowired private ApplicationEventPublisher eventPublisher;
    @Autowired private RequestRepository requestRepository;
    @Autowired private StatusRepository statusRepository;
    @Autowired private ItemRequestProductRepository itemRequestProductRepository;
    @Autowired private ItemRequestProvisionRepository itemRequestProvisionRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private ProvisionRepository provisionRepository;
    @Autowired private MeasurementUnitRepository measurementUnitRepository;
    @Autowired private BranchRepository branchRepository;
    @Autowired private CrRepository crRepository;
    @Autowired private CrBranchRepository crBranchRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private PlatformTransactionManager transactionManager;

    private Branch branch;
    private Cr cr;
    private CrBranch crBranch;
    private User requester;
    private Status pending;
    private Status approved;
    private Status rejected;
    private Status mixedStatus; // PARCIALMENTE_ATENDIDA
    private Product product;
    private Product secondProduct;
    private MeasurementUnit unit;

    @BeforeEach
    void setUp() {
        cleanDatabase();

        requester = saveUser("Solicitante", "solicitante@teste.com", "52998224725", "DOCENTE");

        branch = branchRepository.save(new Branch("Filial Centro"));
        cr = crRepository.save(new Cr("TI", "7940", false));
        crBranch = crBranchRepository.save(new CrBranch(branch, cr, List.of(requester)));

        pending = statusRepository.save(new Status("Pendente", "Solicitacao pendente"));
        approved = statusRepository.save(new Status("Aprovado", "Solicitacao aprovada"));
        rejected = statusRepository.save(new Status("Recusado", "Solicitacao recusada"));
        mixedStatus = statusRepository.save(new Status("PARCIALMENTE_ATENDIDA", "Status para itens com status divergentes"));

        product = productRepository.save(new Product(null, "Produto Teste", "Descrição", 10.0, "TIPO", "PRD-001"));
        secondProduct = productRepository.save(new Product(null, "Segundo Produto", "Descrição", 10.0, "TIPO", "PRD-002"));
        unit = measurementUnitRepository.save(new MeasurementUnit("Unidade", "UN"));
    }

    @AfterEach
    void tearDown() {
        cleanDatabase();
    }

    @Test
    @DisplayName("[Integracao] Quando todos os itens têm o mesmo status após o evento, o status da solicitação passa a ser esse status")
    void shouldUpdateRequestStatusToMatchWhenAllItemsHaveSameStatusAfterEvent() {
        // dado: solicitação com dois itens, ambos com status PENDENTE
        Request request = saveRequest(pending);
        ItemRequestProduct item1 = saveItemRequestProduct(request, product, unit, pending);
        ItemRequestProduct item2 = saveItemRequestProduct(request, secondProduct, unit, pending);

        // quando: status do primeiro item muda para APROVADO (persistimos a alteração)
        updateItemStatus(item1, approved);
        publishItemStatusChangedEvent(item1.getId(), request.getId(), approved.getName());

        // então: como nem todos os itens têm o mesmo status, o status da solicitação deve ser PARCIALMENTE_ATENDIDA
        Request updated = requestRepository.findById(request.getId()).orElseThrow();
        assertThat(updated.getStatus().getName()).isEqualTo("PARCIALMENTE_ATENDIDA");

        // quando: status do segundo item também muda para APROVADO (agora todos os itens são APROVADO)
        updateItemStatus(item2, approved);
        publishItemStatusChangedEvent(item2.getId(), request.getId(), approved.getName());

        // então: agora todos os itens têm status APROVADO, o status da solicitação deve ser APROVADO
        updated = requestRepository.findById(request.getId()).orElseThrow();
        assertThat(updated.getStatus().getName()).isEqualTo("Aprovado");
    }

    @Test
    @DisplayName("[Integracao] Quando os itens têm status diferentes após o evento, o status da solicitação passa a ser PARCIALMENTE_ATENDIDA")
    void shouldSetRequestStatusToMixedWhenItemsHaveDifferentStatusesAfterEvent() {
        // dado: solicitação com dois itens, ambos com status PENDENTE
        Request request = saveRequest(pending);
        ItemRequestProduct item1 = saveItemRequestProduct(request, product, unit, pending);
        ItemRequestProduct item2 = saveItemRequestProduct(request, secondProduct, unit, pending);

        // quando: status do primeiro item muda para APROVADO, segundo permanece PENDENTE
        updateItemStatus(item1, approved);
        publishItemStatusChangedEvent(item1.getId(), request.getId(), approved.getName());

        // então: status da solicitação deve ser PARCIALMENTE_ATENDIDA
        Request updated = requestRepository.findById(request.getId()).orElseThrow();
        assertThat(updated.getStatus().getName()).isEqualTo("PARCIALMENTE_ATENDIDA");

        // quando: status do segundo item muda para RECUSADO (agora itens são APROVADO e RECUSADO)
        updateItemStatus(item2, rejected);
        publishItemStatusChangedEvent(item2.getId(), request.getId(), rejected.getName());

        // então: status da solicitação continua PARCIALMENTE_ATENDIDA
        updated = requestRepository.findById(request.getId()).orElseThrow();
        assertThat(updated.getStatus().getName()).isEqualTo("PARCIALMENTE_ATENDIDA");
    }

    @Test
    @DisplayName("[Integracao] Se o status PARCIALMENTE_ATENDIDA não existir, o listener lança exceção quando necessário")
    void shouldThrowWhenMixedStatusMissingAndItemsDiffer() {
        // Remove the mixed status to simulate missing configuration
        statusRepository.delete(mixedStatus);

        Request request = saveRequest(pending);
        ItemRequestProduct item1 = saveItemRequestProduct(request, product, unit, pending);
        ItemRequestProduct item2 = saveItemRequestProduct(request, secondProduct, unit, pending);

        // Fazer os itens ficarem com status diferentes -> necessidade de PARCIALMENTE_ATENDIDA
        updateItemStatus(item1, approved);
        // Esperamos que uma exceção StatusNotFoundException seja propagada assim que o listener tentar definir o status misto
        assertThrows(
                net.centroweg.gerenciamentocompras.modules.request.domain.exception.StatusNotFoundException.class,
                () -> publishItemStatusChangedEvent(item1.getId(), request.getId(), approved.getName())
        );

        // Se chegarmos aqui, significa que a exceção ocorreu no primeiro evento, como esperado.
        // Não precisamos testar o segundo evento porque a exceção já interrompeu o fluxo.
    }

    @Test
    @DisplayName("[Integracao] Atualiza a solicitacao de servico em uma nova transacao depois do commit do item")
    void shouldUpdateProvisionRequestStatusAfterItemTransactionCommit() {
        Status delivered = statusRepository.save(
                new Status("Entregue", "Solicitacao entregue"));
        Request request = saveRequest(pending);
        Provision provision = provisionRepository.save(
                new Provision("Servico Teste", 100.0, "Descricao do servico"));
        ItemRequestProvision item = itemRequestProvisionRepository.save(
                new ItemRequestProvision(request, provision, pending, null));

        new TransactionTemplate(transactionManager).executeWithoutResult(transactionStatus -> {
            ItemRequestProvision managedItem = itemRequestProvisionRepository.findById(item.getId())
                    .orElseThrow();
            managedItem.setStatus(delivered);
            itemRequestProvisionRepository.save(managedItem);

            eventPublisher.publishEvent(new ItemStatusChangedEvent(
                    request.getId(),
                    managedItem.getId(),
                    RequestItemType.PROVISION,
                    provision.getName(),
                    null,
                    null,
                    null,
                    pending.getName(),
                    delivered.getName(),
                    null,
                    LocalDateTime.now()
            ));
        });

        Request updated = requestRepository.findById(request.getId()).orElseThrow();
        assertThat(updated.getStatus().getName()).isEqualTo(delivered.getName());
    }

    private Request saveRequest(Status status) {
        Request request = new Request(crBranch, status);
        request.setRequestDate(LocalDateTime.of(2026, 6, 26, 10, 0));
        request.setUpdatedAt(LocalDateTime.of(2026, 6, 26, 10, 5));
        request.setActive(true);
        request.getCreatedByUsers().add(requester);
        return requestRepository.save(request);
    }

    private ItemRequestProduct saveItemRequestProduct(Request request, Product product, MeasurementUnit unit, Status status) {
        ItemRequestProduct item = new ItemRequestProduct();
        item.setRequest(request);
        item.setProduct(product);
        item.setMeasurementUnit(unit);
        item.setQuantity(1.0);
        item.setStatus_id(status);
        return itemRequestProductRepository.save(item);
    }

    private void updateItemStatus(ItemRequestProduct item, Status newStatus) {
        item.setStatus_id(newStatus);
        itemRequestProductRepository.save(item);
    }

    private void publishItemStatusChangedEvent(Long itemId, Long requestId, String newStatusName) {
        var event = new ItemStatusChangedEvent(
                requestId,
                itemId,
                RequestItemType.PRODUCT,
                product.getName(),
                product.getCode(),
                1.0,
                unit.getAbbreviation(),
                "Pendente", // previous status name (not used by listener)
                newStatusName,
                "Observação teste",
                java.time.LocalDateTime.now()
        );
        eventPublisher.publishEvent(event);
    }

    private User saveUser(String name, String email, String cpf, String roleName) {
        User user = new User(name, cpf, email, "Senha@123", "1234", true);
        user.setRole(roleRepository.save(new Role(roleName)));
        return userRepository.save(user);
    }

    private void cleanDatabase() {
        itemRequestProductRepository.deleteAll();
        itemRequestProvisionRepository.deleteAll();
        requestRepository.deleteAll();
        crBranchRepository.deleteAll();
        statusRepository.deleteAll();
        crRepository.deleteAll();
        branchRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
        productRepository.deleteAll();
        provisionRepository.deleteAll();
        measurementUnitRepository.deleteAll();
    }
}
