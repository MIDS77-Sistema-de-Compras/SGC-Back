package net.centroweg.gerenciamentocompras.integration;

import net.centroweg.gerenciamentocompras.modules.notification.domain.entity.Notification;
import net.centroweg.gerenciamentocompras.modules.notification.infrastructure.email.NotificationEmailService;
import net.centroweg.gerenciamentocompras.modules.notification.infrastructure.persistence.NotificationRepository;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class NotificationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @MockitoBean
    private NotificationEmailService notificationEmailService;

    private static final String CPF_VALIDO = "52998224725";

    private User user;

    @BeforeEach
    void setUp() {
        notificationRepository.deleteAll();
        userRepository.deleteAll();

        user = userRepository.save(
                new User("Admin Teste", CPF_VALIDO, "admin@teste.com", "Senha@123", "1234", true)
        );
    }

    private Notification criarNotificacao(String title, String message) {
        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setViewed(false);
        notification.setUser(user);
        notification.setRequest(null);
        return notificationRepository.save(notification);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("[Integração] Deve listar as notificações de um usuário")
    void deveListarNotificacoesPorUsuario() throws Exception {
        criarNotificacao("Status atualizado", "Sua solicitação foi aprovada");
        criarNotificacao("Nova solicitação", "Há uma nova solicitação no seu CR");

        mockMvc.perform(get("/notifications/user/{userId}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].userId").value(user.getId()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("[Integração] Deve retornar lista vazia quando usuário não tem notificações")
    void deveRetornarListaVaziaSemNotificacoes() throws Exception {
        mockMvc.perform(get("/notifications/user/{userId}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("[Integração] Deve marcar uma notificação como lida")
    void deveMarcarNotificacaoComoLida() throws Exception {
        Notification saved = criarNotificacao("Status atualizado", "Sua solicitação foi aprovada");

        mockMvc.perform(patch("/notifications/{id}/viewed", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.viewed").value(true));

        Notification updated = notificationRepository.findById(saved.getId()).orElseThrow();
        assertTrue(updated.getViewed());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("[Integração] Deve retornar 404 ao marcar notificação inexistente como lida")
    void deveRetornarNotFoundParaNotificacaoInexistente() throws Exception {
        mockMvc.perform(patch("/notifications/{id}/viewed", 9999L))
                .andExpect(status().isNotFound());
    }
}