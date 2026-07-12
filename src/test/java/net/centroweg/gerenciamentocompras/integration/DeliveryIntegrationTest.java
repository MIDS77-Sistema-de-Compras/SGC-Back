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
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.StatusRepository;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.RoleRepository;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.shared.security.authority.Authorities;
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

import java.time.LocalDateTime;

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

    private Status pendingStatus;
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
        statusRepository.save(new Status("Entregue", "Entrega concluida"));

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
        delivery.setStatus(pendingStatus);
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

    private String confirmBody() {
        return """
                {
                    "observation": "Recebimento conferido e sem avarias."
                }
                """;
    }

    private User userEntity(String name, String email, String cpf, Role role) {
        User user = new User(name, cpf, email, "Senha@123", "1234", true);
        user.setRole(role);
        return user;
    }

    private void cleanDatabase() {
        deliveryRepository.deleteAll();
        requestRepository.deleteAll();
        crBranchRepository.deleteAll();
        crRepository.deleteAll();
        branchRepository.deleteAll();
        statusRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }
}
