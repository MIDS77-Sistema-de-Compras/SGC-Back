package net.centroweg.gerenciamentocompras.integration;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import net.centroweg.gerenciamentocompras.modules.auth.service.CustomUserDetailsService;
import net.centroweg.gerenciamentocompras.modules.auth.service.JwtService;
import net.centroweg.gerenciamentocompras.config.security.WebSecurityConfig;
import net.centroweg.gerenciamentocompras.modules.auth.filter.SecurityFilter;
import net.centroweg.gerenciamentocompras.modules.provision.domain.Provision;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProvision;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.presentation.controller.ItemRequestProvisionController;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.ItemRequestProvisionRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.ItemRequestProvisionResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceIntrf.ItemRequestProvisionService;
import tools.jackson.databind.ObjectMapper;

// 1. Explicitly isolate the web slice to ONLY load your targeted controller type
@WebMvcTest(
        controllers = ItemRequestProvisionController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class, UserDetailsServiceAutoConfiguration.class}
)
@AutoConfigureMockMvc(addFilters = false)
// 2. Override default scanning to strictly include ONLY this controller, bypassing all others
@ComponentScan(
        basePackageClasses = ItemRequestProvisionController.class,
        useDefaultFilters = false, // Stop automatic package parsing
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = ItemRequestProvisionController.class
        )
)
class ItemRequestProvisionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ItemRequestProvisionService itemRequestProvisionService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private SecurityFilter securityFilter;

    @Autowired
    private ObjectMapper objectMapper;

    private ItemRequestProvisionRequest validRequest;
    private ItemRequestProvisionResponse validResponse;
    private Request mockRequest;
    private Provision mockProvision;
    private Status mockStatus;
    private ItemRequestProvision mockItemRequestProvision;
    private CrBranch crBranch;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup Request with minimal required fields
        mockRequest = new Request();
        mockRequest.setId(1L);
        mockRequest.setRequestDate(LocalDateTime.now());
        // Set required relationships (would normally come from DB)
        mockRequest.setCrBranch(crBranch);
        mockRequest.setStatus(new Status()); // This is the Request's status, different from ItemRequestProvision's status

        // Setup Provision with required fields
        mockProvision = new Provision();
        mockProvision.setId(10L);
        mockProvision.setName("Test Provision");
        mockProvision.setTotalValue(100.0);
        mockProvision.setDescription("Test description");

        // Setup Status for ItemRequestProvision
        mockStatus = new Status();
        mockStatus.setId(100L);
        mockStatus.setName("PENDING");
        mockStatus.setDescription("Pending status");

        // Setup the join entity
        mockItemRequestProvision = new ItemRequestProvision();
        mockItemRequestProvision.setId(1000L);
        mockItemRequestProvision.setRequest(mockRequest);
        mockItemRequestProvision.setProvision(mockProvision);
        mockItemRequestProvision.setStatus(mockStatus);
        mockItemRequestProvision.setAdditionalInformation("Test info");

        // Setup the response DTO (matches your record structure)
        validResponse = new ItemRequestProvisionResponse(
            1000L,           // id
            mockRequest.getId(),     // request (full object)
            mockProvision.getId(),   // provision (full object)
            mockStatus.getName(),      // status (full object)
            "Test info"      // additionalInformation
        );

        validRequest = new ItemRequestProvisionRequest(
            1L,      // requestId
            10L,     // provisionId
            100L,    // statusId
            "Test info"
        );
    }

    @Test
    @DisplayName("POST /item-provision-requests - Should create new item and return CREATED")
    void addItem_ShouldReturnCreated() throws Exception {
        // Arrange
        when(itemRequestProvisionService.addItemToProvisionRequest(any(ItemRequestProvisionRequest.class)))
            .thenReturn(validResponse);

        // Act & Assert
        mockMvc.perform(post("/item-provision-requests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1000L))
                .andExpect(jsonPath("$.requestId").value(1L))
                .andExpect(jsonPath("$.provisionId").value(10L))
                .andExpect(jsonPath("$.statusName").value("PENDING"))
                .andExpect(jsonPath("$.additionalInformation").value("Test info"));

        // Verify service was called
        verify(itemRequestProvisionService, times(1))
            .addItemToProvisionRequest(any(ItemRequestProvisionRequest.class));
    }

    @Test
    @DisplayName("GET /item-provision-requests/request/{requestId} - Should return list of items for request")
    void findAllItems_ShouldReturnOkWithList() throws Exception {
        // Arrange
        List<ItemRequestProvisionResponse> responseList = Arrays.asList(validResponse);
        when(itemRequestProvisionService.findAllProvisionRequestItems(1L))
            .thenReturn(responseList);

        // Act & Assert
        mockMvc.perform(get("/item-provision-requests/request/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1000L))
                .andExpect(jsonPath("$[0].requestId").value(1L))
                .andExpect(jsonPath("$[0].provisionId").value(10L))
                .andExpect(jsonPath("$[0].statusName").value("PENDING"))
                .andExpect(jsonPath("$[0].additionalInformation").value("Test info"));

        // Verify service was called
        verify(itemRequestProvisionService, times(1))
            .findAllProvisionRequestItems(1L);
    }

    @Test
    @DisplayName("GET /item-provision-requests/request/{requestId}/{itemId} - Should return item by IDs")
    void findItemByIdAndRequestId_ShouldReturnOk() throws Exception {
        // Arrange
        when(itemRequestProvisionService.findProvisionRequestItemById(1L, 1000L))
            .thenReturn(validResponse);

        // Act & Assert
        mockMvc.perform(get("/item-provision-requests/request/1/1000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1000L))
                .andExpect(jsonPath("$.requestId").value(1L))
                .andExpect(jsonPath("$.provisionId").value(10L))
                .andExpect(jsonPath("$.statusName").value("PENDING"))
                .andExpect(jsonPath("$.additionalInformation").value("Test info"));

        // Verify service was called
        verify(itemRequestProvisionService, times(1))
            .findProvisionRequestItemById(1L, 1000L);
    }

    @Test
    @DisplayName("PUT /item-provision-requests/request/{itemId} - Should update item and return OK")
    void updateItem_ShouldReturnOk() throws Exception {
        // Arrange
        ItemRequestProvisionRequest request = new ItemRequestProvisionRequest(
                1L,
                10L,
                100L,
                "Test info"
        );
        when(itemRequestProvisionService.updateItemFromProvisionRequest(eq(1000L), any(ItemRequestProvisionRequest.class)))
                .thenReturn(validResponse);

        // Act & Assert
        mockMvc.perform(put("/item-provision-requests/request/1000")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1000L))
                .andExpect(jsonPath("$.requestId").value(1L))
                .andExpect(jsonPath("$.provisionId").value(10L))
                .andExpect(jsonPath("$.statusName").value("PENDING"))
                .andExpect(jsonPath("$.additionalInformation").value("Test info"));

        verify(itemRequestProvisionService, times(1))
                .updateItemFromProvisionRequest(eq(1000L), argThat(r ->
                        Long.valueOf(1L).equals(r.requestId()) // 1L is never null
                                && Long.valueOf(10L).equals(r.provisionId())
                                && Long.valueOf(100L).equals(r.statusId())
                                && "Test info".equals(r.additionalInformation())
                ));
    }

    @Test
    @DisplayName("DELETE /item-provision-requests/request/{itemId} - Should delete item and return NO_CONTENT")
    void deleteItem_ShouldReturnNoContent() throws Exception {
        // Arrange
        doNothing().when(itemRequestProvisionService).deleteItemFromProvisionRequest(1000L);

        // Act & Assert
        mockMvc.perform(delete("/item-provision-requests/request/1000"))
                .andExpect(status().isNoContent());

        // Verify service was called
        verify(itemRequestProvisionService, times(1))
            .deleteItemFromProvisionRequest(1000L);
    }
}