package net.centroweg.gerenciamentocompras.integration;

import net.centroweg.gerenciamentocompras.modules.product.infrastructure.persistence.ProductRepository;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request.CreateProductRequest;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request.UpdateProductRequest;
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
public class ProductIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .defaultRequest(get("/").with(user("test-user").roles("ADMIN")))
                .build();

        productRepository.deleteAll();
    }

    private Long createProductAndGetId() throws Exception {
        CreateProductRequest request = new CreateProductRequest(
                "Produto Teste",
                "Descrição do Produto Teste",
                100.0,
                "Tipo Teste",
                "COD-INTEG-01"
        );

        String response = mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readTree(response).get("id").asLong();
    }

    @Test
    @DisplayName("[Integração] Deve criar um produto com sucesso")
    void shouldCreateProductSuccessfully() throws Exception {
        CreateProductRequest request = new CreateProductRequest(
                "Produto Teste",
                "Descrição do Produto Teste",
                100.0,
                "Tipo Teste",
                "COD-INTEG-02"
        );

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Produto Teste"))
                .andExpect(jsonPath("$.price").value(100.0))
                .andExpect(jsonPath("$.code").value("COD-INTEG-02"));

        assertEquals(1, productRepository.count());
    }

    @Test
    @DisplayName("[Integração] Deve retornar 400 (Bad Request) ao criar produto inválido")
    void shouldReturnBadRequestWhenCreatingInvalidProduct() throws Exception {
        CreateProductRequest request = new CreateProductRequest(
                "", // Nome em branco
                "Descrição",
                -5.0, // Preço negativo
                "", // Tipo vazio
                "" // Código vazio
        );

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("[Integração] Deve listar todos os produtos")
    void shouldListAllProducts() throws Exception {
        createProductAndGetId();

        mockMvc.perform(get("/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Produto Teste"));
    }

    @Test
    @DisplayName("[Integração] Deve buscar produto por ID")
    void shouldFindProductById() throws Exception {
        Long id = createProductAndGetId();

        mockMvc.perform(get("/products/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Produto Teste"));
    }

    @Test
    @DisplayName("[Integração] Deve retornar 404 ao buscar produto com ID inexistente")
    void shouldReturnNotFoundForNonExistentProduct() throws Exception {
        mockMvc.perform(get("/products/{id}", 9999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("[Integração] Deve atualizar produto com sucesso")
    void shouldUpdateProductSuccessfully() throws Exception {
        Long id = createProductAndGetId();

        UpdateProductRequest updateRequest = new UpdateProductRequest(
                "Produto Atualizado",
                "Descrição Atualizada",
                150.0,
                "Tipo Atualizado",
                "COD-INTEG-ALT"
        );

        mockMvc.perform(put("/products/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Produto Atualizado"))
                .andExpect(jsonPath("$.price").value(150.0))
                .andExpect(jsonPath("$.code").value("COD-INTEG-ALT"));
    }

    @Test
    @DisplayName("[Integração] Deve deletar produto com sucesso")
    void shouldDeleteProductSuccessfully() throws Exception {
        Long id = createProductAndGetId();

        mockMvc.perform(delete("/products/{id}", id))
                .andExpect(status().isNoContent());

        assertEquals(0, productRepository.count());
    }
}
