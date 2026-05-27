package net.centroweg.gerenciamentocompras.integration;

import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.CreateUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // Isso fará o Spring usar o H2 em vez do Postgres
public class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Deve criar um usuário e salvar no banco de dados real (H2)")
    void deveCriarUsuarioComSucesso() throws Exception {
        // Preparando o DTO
        CreateUser request = new CreateUser(
                "Admin Teste",
                "admin@teste.com",
                "03310475043", // Use um CPF válido se tiver validação!
                "Senha@123",
                "1234",
                true,
                "ADMIN"
        );

        // Executando a chamada HTTP
        mockMvc.perform(post("/users") // Verifique se o @RequestMapping do seu Controller é /users
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated()) // Espera 201 Created
                .andExpect(jsonPath("$.name").value("Admin Teste"));

        // Verificando se realmente salvou no banco
        assertEquals(1, userRepository.count());
    }
}