package net.centroweg.gerenciamentocompras.integration;

import net.centroweg.gerenciamentocompras.modules.product.infrastructure.persistence.MeasurementUnitRepository;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request.MeasurementUnitRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class MeasurementUnitIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MeasurementUnitRepository measurementUnitRepository;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .defaultRequest(get("/").with(user("test-user").roles("ADMIN")))
                .build();

        measurementUnitRepository.deleteAll();
    }

    private Long createMeasurementUnitAndGetId() throws Exception {
        MeasurementUnitRequest request = new MeasurementUnitRequest("Litros", "L");

        String response = mockMvc.perform(post("/measurement-unit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readTree(response).get("id").asLong();
    }

    @Test
    @DisplayName("[Integração] Deve criar uma unidade de medida com sucesso")
    void shouldCreateMeasurementUnitSuccessfully() throws Exception {
        MeasurementUnitRequest request = new MeasurementUnitRequest("Quilograma", "KG");

        mockMvc.perform(post("/measurement-unit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Quilograma"))
                .andExpect(jsonPath("$.abbreviation").value("KG"));

        assertEquals(1, measurementUnitRepository.count());
    }

    @Test
    @DisplayName("[Integração] Deve listar todas as unidades de medida")
    void shouldListAllMeasurementUnits() throws Exception {
        createMeasurementUnitAndGetId();

        mockMvc.perform(get("/measurement-unit")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Litros"));
    }

    @Test
    @DisplayName("[Integração] Deve buscar unidade de medida por ID")
    void shouldFindMeasurementUnitById() throws Exception {
        Long id = createMeasurementUnitAndGetId();

        mockMvc.perform(get("/measurement-unit/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Litros"));
    }

    @Test
    @DisplayName("[Integração] Deve buscar unidade de medida por abreviação")
    void shouldFindMeasurementUnitByAbbreviation() throws Exception {
        createMeasurementUnitAndGetId();

        mockMvc.perform(get("/measurement-unit/search")
                        .param("abbreviation", "L")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.abbreviation").value("L"))
                .andExpect(jsonPath("$.name").value("Litros"));
    }

    @Test
    @DisplayName("[Integração] Deve retornar 404 ao buscar unidade com ID inexistente")
    void shouldReturnNotFoundForNonExistentId() throws Exception {
        mockMvc.perform(get("/measurement-unit/{id}", 9999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("[Integração] Deve atualizar unidade de medida com sucesso")
    void shouldUpdateMeasurementUnitSuccessfully() throws Exception {
        Long id = createMeasurementUnitAndGetId();

        MeasurementUnitRequest updateRequest = new MeasurementUnitRequest("Litros Alterado", "L-ALT");

        mockMvc.perform(put("/measurement-unit/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Litros Alterado"))
                .andExpect(jsonPath("$.abbreviation").value("L-ALT"));
    }
}
