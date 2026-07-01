package net.centroweg.gerenciamentocompras.integration;

import com.jayway.jsonpath.JsonPath;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Branch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Cr;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.BranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrBranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrRepository;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@WithMockUser
class RequestFilterIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private NotificationRepository notificationRepository;

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

    @MockitoBean
    private NotificationEmailService notificationEmailService;

    private MockMvc mockMvc;

    private Request matchingRequest;
    private Request differentCrRequest;
    private Request differentStatusRequest;
    private Request differentSupervisorRequest;
    private Request beforeRangeRequest;
    private Request endDateLateRequest;
    private Request afterRangeRequest;
    private Request withoutSupervisorRequest;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .defaultRequest(get("/").with(user("admin").roles("USER", "ADMIN")))
                .build();

        cleanDatabase();

        User joao = userRepository.save(new User(
                "Joao Silva",
                "52998224725",
                "joao@teste.com",
                "Senha@123",
                "1234",
                true
        ));
        User maria = userRepository.save(new User(
                "Maria Souza",
                "12345678909",
                "maria@teste.com",
                "Senha@123",
                "4321",
                true
        ));

        Branch branch = branchRepository.save(new Branch("Filial Centro"));

        Cr cr7940 = crRepository.save(new Cr("CR Compras", "7940", false));
        Cr cr1234 = crRepository.save(new Cr("CR Tecnologia", "1234", false));
        Cr cr7999 = crRepository.save(new Cr("CR Qualidade", "7999", false));
        Cr cr7900 = crRepository.save(new Cr("CR Sem Responsavel", "7900", false));

        CrBranch joao7940 = crBranchRepository.save(new CrBranch(branch, cr7940, List.of(joao)));
        CrBranch joao1234 = crBranchRepository.save(new CrBranch(branch, cr1234, List.of(joao)));
        CrBranch maria7940 = crBranchRepository.save(new CrBranch(branch, cr7940, List.of(maria)));
        CrBranch joao7999 = crBranchRepository.save(new CrBranch(branch, cr7999, List.of(joao)));
        CrBranch noSupervisor7900 = crBranchRepository.save(new CrBranch(branch, cr7900, null));

        Status approved = statusRepository.save(new Status("Aprovado", "Solicitação aprovada"));
        Status pending = statusRepository.save(new Status("Pendente", "Solicitação pendente"));

        matchingRequest = saveRequest(
                joao7940,
                approved,
                LocalDateTime.of(2026, 6, 15, 10, 30)
        );
        differentCrRequest = saveRequest(
                joao1234,
                approved,
                LocalDateTime.of(2026, 6, 15, 11, 0)
        );
        differentStatusRequest = saveRequest(
                joao7940,
                pending,
                LocalDateTime.of(2026, 6, 15, 12, 0)
        );
        differentSupervisorRequest = saveRequest(
                maria7940,
                approved,
                LocalDateTime.of(2026, 6, 15, 13, 0)
        );
        beforeRangeRequest = saveRequest(
                joao7940,
                approved,
                LocalDateTime.of(2026, 5, 31, 23, 59)
        );
        endDateLateRequest = saveRequest(
                joao7999,
                approved,
                LocalDateTime.of(2026, 6, 30, 23, 59, 59)
        );
        afterRangeRequest = saveRequest(
                joao7940,
                approved,
                LocalDateTime.of(2026, 7, 1, 0, 0)
        );
        withoutSupervisorRequest = saveRequest(
                noSupervisor7900,
                approved,
                LocalDateTime.of(2026, 6, 20, 9, 0)
        );
    }

    @AfterEach
    void tearDown() {
        cleanDatabase();
    }

    @Test
    @DisplayName("[Integração] GET /requests sem filtros retorna todas as solicitações")
    void shouldReturnAllRequestsWithoutFilters() throws Exception {
        assertRequestIds(
                get("/requests"),
                matchingRequest,
                differentCrRequest,
                differentStatusRequest,
                differentSupervisorRequest,
                beforeRangeRequest,
                endDateLateRequest,
                afterRangeRequest,
                withoutSupervisorRequest
        );
    }

    @Test
    @DisplayName("[Integração] Deve filtrar parcialmente por código do CR")
    void shouldFilterByPartialCrCode() throws Exception {
        assertRequestIds(
                get("/requests").param("crCode", "79"),
                matchingRequest,
                differentStatusRequest,
                differentSupervisorRequest,
                beforeRangeRequest,
                endDateLateRequest,
                afterRangeRequest,
                withoutSupervisorRequest
        );
    }

    @Test
    @DisplayName("[Integração] Deve filtrar exatamente por status ignorando maiúsculas e minúsculas")
    void shouldFilterByExactStatusNameIgnoringCase() throws Exception {
        assertRequestIds(
                get("/requests").param("statusName", "APROVADO"),
                matchingRequest,
                differentCrRequest,
                differentSupervisorRequest,
                beforeRangeRequest,
                endDateLateRequest,
                afterRangeRequest,
                withoutSupervisorRequest
        );
    }

    @Test
    @DisplayName("[Integração] Deve filtrar parcialmente por supervisor ignorando maiúsculas e minúsculas")
    void shouldFilterByPartialSupervisorNameIgnoringCase() throws Exception {
        assertRequestIds(
                get("/requests").param("supervisorName", "joao"),
                matchingRequest,
                differentCrRequest,
                differentStatusRequest,
                beforeRangeRequest,
                endDateLateRequest,
                afterRangeRequest
        );
    }

    @Test
    @DisplayName("[Integração] Deve filtrar por data inicial")
    void shouldFilterByStartDate() throws Exception {
        assertRequestIds(
                get("/requests").param("startDate", "2026-06-01"),
                matchingRequest,
                differentCrRequest,
                differentStatusRequest,
                differentSupervisorRequest,
                endDateLateRequest,
                afterRangeRequest,
                withoutSupervisorRequest
        );
    }

    @Test
    @DisplayName("[Integração] Deve filtrar por data final")
    void shouldFilterByEndDate() throws Exception {
        assertRequestIds(
                get("/requests").param("endDate", "2026-06-30"),
                matchingRequest,
                differentCrRequest,
                differentStatusRequest,
                differentSupervisorRequest,
                beforeRangeRequest,
                endDateLateRequest,
                withoutSupervisorRequest
        );
    }

    @Test
    @DisplayName("[Integração] Deve filtrar por intervalo de data")
    void shouldFilterByStartDateAndEndDate() throws Exception {
        assertRequestIds(
                get("/requests")
                        .param("startDate", "2026-06-01")
                        .param("endDate", "2026-06-30"),
                matchingRequest,
                differentCrRequest,
                differentStatusRequest,
                differentSupervisorRequest,
                endDateLateRequest,
                withoutSupervisorRequest
        );
    }

    @Test
    @DisplayName("[Integração] Deve incluir todo o dia informado na data final")
    void shouldIncludeWholeEndDate() throws Exception {
        assertRequestIds(
                get("/requests")
                        .param("startDate", "2026-06-30")
                        .param("endDate", "2026-06-30"),
                endDateLateRequest
        );
    }

    @Test
    @DisplayName("[Integração] Deve combinar todos os filtros usando AND")
    void shouldCombineAllFiltersUsingAnd() throws Exception {
        assertRequestIds(
                get("/requests")
                        .param("crCode", "7940")
                        .param("statusName", "aprovado")
                        .param("supervisorName", "joao")
                        .param("startDate", "2026-06-01")
                        .param("endDate", "2026-06-30"),
                matchingRequest
        );
    }

    @Test
    @DisplayName("[Integração] Deve retornar lista vazia quando filtros não têm correspondência")
    void shouldReturnEmptyListWhenFiltersDoNotMatch() throws Exception {
        assertRequestIds(
                get("/requests")
                        .param("crCode", "555")
                        .param("statusName", "Recusado")
                        .param("supervisorName", "inexistente"),
                new Request[0]
        );
    }

    @Test
    @DisplayName("[Integração] Solicitação sem supervisor não deve provocar erro")
    void shouldNotFailWhenRequestHasNoSupervisor() throws Exception {
        assertRequestIds(
                get("/requests").param("crCode", "7900"),
                withoutSupervisorRequest
        );
    }

    @Test
    @DisplayName("[Integração] Formato inválido de data retorna erro esperado")
    void shouldReturnExpectedStatusWhenDateFormatIsInvalid() throws Exception {
        mockMvc.perform(get("/requests")
                        .param("startDate", "2026/06/01")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    private Request saveRequest(
            CrBranch crBranch,
            Status status,
            LocalDateTime requestDate
    ) {
        Request request = new Request(crBranch, status);
        request.setRequestDate(requestDate);
        request.setUpdatedAt(requestDate);
        request.setActive(true);
        return requestRepository.save(request);
    }

    private void assertRequestIds(
            MockHttpServletRequestBuilder requestBuilder,
            Request... expectedRequests
    ) throws Exception {
        MvcResult result = mockMvc.perform(requestBuilder.contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andReturn();

        List<Long> responseIds = responseIds(result);
        List<Long> expectedIds = Arrays.stream(expectedRequests)
                .map(Request::getId)
                .toList();

        assertThat(responseIds).containsExactlyInAnyOrderElementsOf(expectedIds);
    }

    private List<Long> responseIds(MvcResult result) throws Exception {
        List<Number> ids = JsonPath.read(
                result.getResponse().getContentAsString(),
                "$.content[*].id"
        );

        return ids.stream()
                .map(Number::longValue)
                .toList();
    }

    private void cleanDatabase() {
        notificationRepository.deleteAll();
        requestRepository.deleteAll();
        crBranchRepository.deleteAll();
        statusRepository.deleteAll();
        crRepository.deleteAll();
        branchRepository.deleteAll();
        userRepository.deleteAll();
    }
}
