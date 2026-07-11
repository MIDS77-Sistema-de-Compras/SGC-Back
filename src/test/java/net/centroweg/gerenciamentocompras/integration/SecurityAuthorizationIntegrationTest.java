package net.centroweg.gerenciamentocompras.integration;

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
import org.springframework.test.web.servlet.ResultMatcher;

import java.time.LocalDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityAuthorizationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

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
    private ProductRepository productRepository;

    @Autowired
    private MeasurementUnitRepository measurementUnitRepository;

    private CrBranch crBranch;
    private Map<String, UserPrincipal> principalsByRole;

    @BeforeEach
    void setUp() {
        cleanDatabase();

        Role docente = roleRepository.save(new Role(Authorities.DOCENTE));
        Role comprador = roleRepository.save(new Role(Authorities.COMPRADOR));
        Role supervisor = roleRepository.save(new Role(Authorities.SUPERVISOR));
        Role coordenador = roleRepository.save(new Role(Authorities.COORDENADOR));
        Role admin = roleRepository.save(new Role(Authorities.ADMIN));

        principalsByRole = Map.of(
                Authorities.DOCENTE, principalFor("Docente Teste", "docente@teste.com", "52998224725", docente),
                Authorities.COMPRADOR, principalFor("Comprador Teste", "comprador@teste.com", "11144477735", comprador),
                Authorities.SUPERVISOR, principalFor("Supervisor Teste", "supervisor@teste.com", "15350946056", supervisor),
                Authorities.COORDENADOR, principalFor("Coordenador Teste", "coordenador@teste.com", "98765432000", coordenador),
                Authorities.ADMIN, principalFor("Admin Teste", "admin@teste.com", "93541134780", admin)
        );

        Branch branch = branchRepository.save(new Branch("Filial Centro"));
        Cr cr = crRepository.save(new Cr("TI", "7940", false));
        crBranch = crBranchRepository.save(new CrBranch(branch, cr, null));

        statusRepository.save(new net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status(
                "EM_ANDAMENTO",
                "Solicitacao aguardando aprovacao"
        ));
        measurementUnitRepository.save(new MeasurementUnit("UN", "UN"));
        productRepository.save(new Product(null, "Parafuso", "Parafuso de teste", 1.0, "Insumo", "PAR-001"));
    }

    @AfterEach
    void tearDown() {
        cleanDatabase();
    }

    @Test
    @DisplayName("POST /users exige autenticacao e role de gerenciamento de usuarios")
    void shouldProtectUserCreationByRole() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createUserBody("sem.auth@teste.com", "52998224725", Authorities.DOCENTE)))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(post("/users")
                        .with(user(principalsByRole.get(Authorities.DOCENTE)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createUserBody("docente.negado@teste.com", "93541134780", Authorities.DOCENTE)))
                .andExpect(status().isForbidden());

        mockMvc.perform(post("/users")
                        .with(user(principalsByRole.get(Authorities.SUPERVISOR)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createUserBody("docente.criado@teste.com", "93541134780", Authorities.DOCENTE)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("POST /requests permite solicitantes oficiais e bloqueia comprador")
    void shouldProtectRequestCreationByRole() throws Exception {
        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createRequestBody()))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(post("/requests")
                        .with(user(principalsByRole.get(Authorities.COMPRADOR)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createRequestBody()))
                .andExpect(status().isForbidden());

        mockMvc.perform(post("/requests")
                        .with(user(principalsByRole.get(Authorities.DOCENTE)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createRequestBody()))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("PATCH /requests/{id}/status exige role de aprovacao")
    void shouldProtectRequestApprovalByRole() throws Exception {
        String body = """
                {
                    "statusName": "APROVADO",
                    "justification": "ok"
                }
                """;

        mockMvc.perform(patch("/requests/{id}/status", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(patch("/requests/{id}/status", 999L)
                        .with(user(principalsByRole.get(Authorities.DOCENTE)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isForbidden());

        mockMvc.perform(patch("/requests/{id}/status", 999L)
                        .with(user(principalsByRole.get(Authorities.SUPERVISOR)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(notUnauthorizedOrForbidden());
    }

    @Test
    @DisplayName("PUT /cr/{id} exige role de gerenciamento de CR")
    void shouldProtectCrManagementByRole() throws Exception {
        String body = """
                {
                    "name": "TI",
                    "code": "7941",
                    "master": false,
                    "sectorName": "Tecnologia"
                }
                """;

        mockMvc.perform(put("/cr/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(put("/cr/{id}", 999L)
                        .with(user(principalsByRole.get(Authorities.SUPERVISOR)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isForbidden());

        mockMvc.perform(put("/cr/{id}", 999L)
                        .with(user(principalsByRole.get(Authorities.COORDENADOR)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(notUnauthorizedOrForbidden());
    }

    @Test
    @DisplayName("GET /requests exige role de relatorio")
    void shouldProtectReportsByRole() throws Exception {
        mockMvc.perform(get("/requests"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/requests")
                        .with(user(principalsByRole.get(Authorities.DOCENTE))))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/requests")
                        .with(user(principalsByRole.get(Authorities.COMPRADOR))))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("RoleController permite apenas ADMIN")
    void shouldProtectRoleControllerWithAdminOnly() throws Exception {
        mockMvc.perform(get("/role"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/role")
                        .with(user(principalsByRole.get(Authorities.COORDENADOR))))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/role")
                        .with(user(principalsByRole.get(Authorities.ADMIN))))
                .andExpect(status().isOk());
    }

    private UserPrincipal principalFor(String name, String email, String cpf, Role role) {
        User user = new User(name, cpf, email, "Senha@123", "1234", true);
        user.setRole(role);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return new UserPrincipal(userRepository.save(user));
    }

    private ResultMatcher notUnauthorizedOrForbidden() {
        return result -> assertThat(result.getResponse().getStatus()).isNotIn(
                HttpStatusCodes.UNAUTHORIZED,
                HttpStatusCodes.FORBIDDEN
        );
    }

    private String createUserBody(String email, String cpf, String role) {
        return """
                {
                    "name": "Usuario Criado",
                    "email": "%s",
                    "cpf": "%s",
                    "password": "Senha@123",
                    "extensionNumber": "1234",
                    "active": true,
                    "nameRole": "%s"
                }
                """.formatted(email, cpf, role);
    }

    private String createRequestBody() {
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
                """.formatted(crBranch.getId());
    }

    private void cleanDatabase() {
        requestRepository.deleteAll();
        productRepository.deleteAll();
        measurementUnitRepository.deleteAll();
        crBranchRepository.deleteAll();
        statusRepository.deleteAll();
        crRepository.deleteAll();
        branchRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    private static final class HttpStatusCodes {
        private static final int UNAUTHORIZED = 401;
        private static final int FORBIDDEN = 403;

        private HttpStatusCodes() {
        }
    }
}
