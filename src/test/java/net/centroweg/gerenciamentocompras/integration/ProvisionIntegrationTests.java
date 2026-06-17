package net.centroweg.gerenciamentocompras.integration;


import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import net.centroweg.gerenciamentocompras.modules.provision.domain.exception.ProvisionNotFoundException;
import net.centroweg.gerenciamentocompras.modules.auth.service.CustomUserDetailsService;
import net.centroweg.gerenciamentocompras.modules.auth.service.JwtService;
import net.centroweg.gerenciamentocompras.config.security.WebSecurityConfig;
import net.centroweg.gerenciamentocompras.modules.auth.filter.SecurityFilter;
import net.centroweg.gerenciamentocompras.modules.provision.presentation.controller.ProvisionController;
import net.centroweg.gerenciamentocompras.modules.provision.presentation.dto.request.ProvisionRequest;
import net.centroweg.gerenciamentocompras.modules.provision.presentation.dto.response.ProvisionResponse;
import net.centroweg.gerenciamentocompras.modules.provision.service.interfaces.ProvisionService;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@WebMvcTest(ProvisionController.class)
@Import({WebSecurityConfig.class, SecurityFilter.class})
class ProvisionIntegrationTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProvisionService provisionService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    private ProvisionRequest validRequest;
    private ProvisionResponse mockResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .defaultRequest(get("/").with(user("test-user").roles("ADMIN")))
                .build();

        validRequest = new ProvisionRequest(
            "Office Supplies",
            1500.00,
            "Monthly office supplies provision"
        );

        mockResponse = new ProvisionResponse(
            1L,
            "Office Supplies",
            1500.00,
            "Monthly office supplies provision"
        );
    }

    // POST /provisions

    @Test
    @DisplayName("Create Provision Test - Should return 201")
    void createProvision_shouldReturn201_whenRequestIsValid() throws Exception {
        when(provisionService.createProvision(any(ProvisionRequest.class)))
            .thenReturn(mockResponse);

        mockMvc.perform(post("/provisions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.name").value("Office Supplies"))
            .andExpect(jsonPath("$.totalValue").value(1500.00))
            .andExpect(jsonPath("$.description").value("Monthly office supplies provision"));

        verify(provisionService, times(1)).createProvision(any(ProvisionRequest.class));
    }

    @Test
    @DisplayName("Create Provision Test - Should return 400 if name is blank")
    void createProvision_shouldReturn400_whenNameIsBlank() throws Exception {
        ProvisionRequest invalidRequest = new ProvisionRequest(
            "",
            1500.00,
            "Some description"
        );

        mockMvc.perform(post("/provisions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
            .andExpect(status().isBadRequest());

        verify(provisionService, never()).createProvision(any());
    }

    @Test
    @DisplayName("Create Provision Test - Should return 400 if name is null")
    void createProvision_shouldReturn400_whenNameIsNull() throws Exception {
        ProvisionRequest invalidRequest = new ProvisionRequest(
            null,
            1500.00,
            "Some description"
        );

        mockMvc.perform(post("/provisions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
            .andExpect(status().isBadRequest());

        verify(provisionService, never()).createProvision(any());
    }

    @Test
    @DisplayName("Create Provision Test - Should return 400 is total value is null")
    void createProvision_shouldReturn400_whenTotalValueIsNull() throws Exception {
        ProvisionRequest invalidRequest = new ProvisionRequest(
            "Office Supplies",
            null,
            "Some description"
        );

        mockMvc.perform(post("/provisions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
            .andExpect(status().isBadRequest());

        verify(provisionService, never()).createProvision(any());
    }

    @Test
    @DisplayName("Create Provision Test - Should return 400 if description is blank")
    void createProvision_shouldReturn400_whenDescriptionIsBlank() throws Exception {
        ProvisionRequest invalidRequest = new ProvisionRequest(
            "Office Supplies",
            1500.00,
            ""
        );

        mockMvc.perform(post("/provisions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
            .andExpect(status().isBadRequest());

        verify(provisionService, never()).createProvision(any());
    }

    // GET /provisions

    @Test
    @DisplayName("Get Provision Test - Should return 200 with full list")
    void getAllProvisions_shouldReturn200_withListOfProvisions() throws Exception {
        ProvisionResponse second = new ProvisionResponse(
            2L, "IT Equipment", 5000.00, "Quarterly IT provision"
        );

        when(provisionService.getAllProvisions()).thenReturn(List.of(mockResponse, second));

        mockMvc.perform(get("/provisions"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].id").value(1L))
            .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    @DisplayName("Get Provision Test - Should return 200 with empty list")
    void getAllProvisions_shouldReturn200_withEmptyList() throws Exception {
        when(provisionService.getAllProvisions()).thenReturn(List.of());

        mockMvc.perform(get("/provisions"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(0));
    }

    // GET /provisions/{id}

    @Test
    @DisplayName("Get Provision By ID Test - Should return 200 if provision exists")
    void getProvisionById_shouldReturn200_whenProvisionExists() throws Exception {
        when(provisionService.getProvisionById(1L)).thenReturn(mockResponse);

        mockMvc.perform(get("/provisions/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.name").value("Office Supplies"));
    }

    @Test
    @DisplayName("Get Provision By ID Test - Should return 404 if not found")
    void getProvisionById_shouldReturn404_whenProvisionDoesNotExist() throws Exception {
        when(provisionService.getProvisionById(99L))
            .thenThrow(new ProvisionNotFoundException());

        mockMvc.perform(get("/provisions/99"))
            .andExpect(status().isNotFound());
    }

    // PUT /provisions/{id}

    @Test
    @DisplayName("Update Provision Test - Should return 200 if request is valid")
    void updateProvision_shouldReturn200_whenRequestIsValid() throws Exception {
        ProvisionRequest updateRequest = new ProvisionRequest(
            "Updated Supplies",
            2000.00,
            "Updated description"
        );

        ProvisionResponse updatedResponse = new ProvisionResponse(
            1L, "Updated Supplies", 2000.00, "Updated description"
        );

        when(provisionService.updateProvision(eq(1L), any(ProvisionRequest.class)))
            .thenReturn(updatedResponse);

        mockMvc.perform(put("/provisions/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Updated Supplies"))
            .andExpect(jsonPath("$.totalValue").value(2000.00));
    }

    @Test
    @DisplayName("Update Provision Test - Should return 404 if not found")
    void updateProvision_shouldReturn404_whenProvisionDoesNotExist() throws Exception {
        when(provisionService.updateProvision(eq(99L), any(ProvisionRequest.class)))
            .thenThrow(new ProvisionNotFoundException());

        mockMvc.perform(put("/provisions/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
            .andExpect(status().isNotFound());
    }

    // DELETE /provisions/{id}

    @Test
    @DisplayName("Delete Provision Test - Should return 204 if exists")
    void deleteProvision_shouldReturn204_whenProvisionExists() throws Exception {
        doNothing().when(provisionService).deleteProvision(1L);

        mockMvc.perform(delete("/provisions/1"))
            .andExpect(status().isNoContent());

        verify(provisionService, times(1)).deleteProvision(1L);
    }

    @Test
    @DisplayName("Delete Provision Test - Should return 404 is doesn't exists")
    void deleteProvision_shouldReturn404_whenProvisionDoesNotExist() throws Exception {
        doThrow(new ProvisionNotFoundException())
            .when(provisionService).deleteProvision(99L);

        mockMvc.perform(delete("/provisions/99"))
            .andExpect(status().isNotFound());
    }
}
