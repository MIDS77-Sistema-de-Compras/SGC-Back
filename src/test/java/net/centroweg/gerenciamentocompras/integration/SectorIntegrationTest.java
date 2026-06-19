package net.centroweg.gerenciamentocompras.integration;

import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Sector;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.SectorRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.SectorRequest;
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
public class SectorIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SectorRepository sectorRepository;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .defaultRequest(get("/").with(user("test-user").roles("ADMIN")))
                .build();

        sectorRepository.deleteAll();
    }

    private Long createSectorAndGetId(String name) throws Exception {
        SectorRequest request = new SectorRequest(name);

        String response = mockMvc.perform(post("/sector")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readTree(response).get("id").asLong();
    }

    @Test
    @DisplayName("[Integração] Deve criar um setor com sucesso")
    void shouldCreateSectorSuccessfully() throws Exception {
        SectorRequest request = new SectorRequest("TI");

        mockMvc.perform(post("/sector")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("TI"));

        assertEquals(1, sectorRepository.count());
    }

    @Test
    @DisplayName("[Integração] Deve retornar erro ao criar setor com nome em branco")
    void shouldReturnBadRequestWhenNameIsBlank() throws Exception {
        SectorRequest request = new SectorRequest("");

        mockMvc.perform(post("/sector")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("[Integração] Deve listar setores (simples)")
    void shouldListSectorsSimple() throws Exception {
        createSectorAndGetId("Financeiro");
        createSectorAndGetId("Recursos Humanos");

        mockMvc.perform(get("/sector/simple")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Financeiro"))
                .andExpect(jsonPath("$[1].name").value("Recursos Humanos"));
    }

    @Test
    @DisplayName("[Integração] Deve listar setores (composto)")
    void shouldListSectorsCompound() throws Exception {
        createSectorAndGetId("Financeiro");

        mockMvc.perform(get("/sector/compound")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Financeiro"))
                .andExpect(jsonPath("$[0].crs").isArray());
    }

    @Test
    @DisplayName("[Integração] Deve buscar setor por ID (simples)")
    void shouldFindSectorByIdSimple() throws Exception {
        Long id = createSectorAndGetId("Logística");

        mockMvc.perform(get("/sector/simple/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Logística"));
    }

    @Test
    @DisplayName("[Integração] Deve buscar setor por ID (composto)")
    void shouldFindSectorByIdCompound() throws Exception {
        Long id = createSectorAndGetId("Logística");

        mockMvc.perform(get("/sector/compound/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Logística"))
                .andExpect(jsonPath("$.crs").isArray());
    }

    @Test
    @DisplayName("[Integração] Deve retornar 404 ao buscar setor inexistente (simples)")
    void shouldReturnNotFoundForNonExistentSectorSimple() throws Exception {
        mockMvc.perform(get("/sector/simple/{id}", 9999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("[Integração] Deve retornar 404 ao buscar setor inexistente (composto)")
    void shouldReturnNotFoundForNonExistentSectorCompound() throws Exception {
        mockMvc.perform(get("/sector/compound/{id}", 9999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("[Integração] Deve atualizar setor com sucesso")
    void shouldUpdateSectorSuccessfully() throws Exception {
        Long id = createSectorAndGetId("Vendas");

        SectorRequest updateRequest = new SectorRequest("Comercial");

        mockMvc.perform(put("/sector/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Comercial"));

        Sector sector = sectorRepository.findById(id).orElseThrow();
        assertEquals("Comercial", sector.getName());
    }

    @Test
    @DisplayName("[Integração] Deve retornar 404 ao atualizar setor inexistente")
    void shouldReturnNotFoundWhenUpdatingNonExistentSector() throws Exception {
        SectorRequest updateRequest = new SectorRequest("Comercial");

        mockMvc.perform(put("/sector/{id}", 9999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("[Integração] Deve deletar setor com sucesso")
    void shouldDeleteSectorSuccessfully() throws Exception {
        Long id = createSectorAndGetId("Marketing");

        mockMvc.perform(delete("/sector/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertEquals(0, sectorRepository.count());
    }
}
