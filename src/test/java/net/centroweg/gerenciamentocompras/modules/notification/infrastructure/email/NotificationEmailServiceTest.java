package net.centroweg.gerenciamentocompras.modules.notification.infrastructure.email;

import net.centroweg.gerenciamentocompras.modules.notification.infrastructure.url.RequestFrontendUrlBuilder;
import net.centroweg.gerenciamentocompras.modules.request.service.api.RequestPublicApi;
import net.centroweg.gerenciamentocompras.modules.request.service.api.dto.RequestEmailNotificationData;
import net.centroweg.gerenciamentocompras.shared.email.model.DefaultEmail;
import net.centroweg.gerenciamentocompras.shared.email.service.EmailSenderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationEmailServiceTest {

    @Mock
    private EmailSenderService emailSenderService;
    @Mock
    private RequestPublicApi requestPublicApi;

    @Test
    void shouldSendManagementEmailWithTheSpecificRequestUrl() throws Exception {
        // Arrange
        var urlBuilder = new RequestFrontendUrlBuilder(
                "https://sgc-front-nine.vercel.app/solicitacoes/{requestId}",
                "https://sgc-front-nine.vercel.app/solicitacoes/gestao/{requestId}"
        );
        var service = new NotificationEmailService(emailSenderService, requestPublicApi, urlBuilder);
        when(requestPublicApi.findEmailNotificationDataById(10L)).thenReturn(requestData(10L));
        when(requestPublicApi.findEmailNotificationDataById(25L)).thenReturn(requestData(25L));

        // Act
        service.sendNotificationEmail(
                "Responsavel", "responsavel@teste.com", "Nova solicitacao",
                "Existe uma nova solicitacao.", 10L
        );
        service.sendNotificationEmail(
                "Responsavel", "responsavel@teste.com", "Nova solicitacao",
                "Existe uma nova solicitacao.", 25L
        );

        // Assert
        var htmlCaptor = ArgumentCaptor.forClass(String.class);
        verify(emailSenderService, times(2)).sendEmail(any(DefaultEmail.class), htmlCaptor.capture());
        assertThat(htmlCaptor.getAllValues().get(0))
                .contains("https://sgc-front-nine.vercel.app/solicitacoes/gestao/10")
                .doesNotContain("{requestId}", "/coordenador/solicitacoes", "/docente/solicitacoes", "/login");
        assertThat(htmlCaptor.getAllValues().get(1))
                .contains("https://sgc-front-nine.vercel.app/solicitacoes/gestao/25")
                .doesNotContain("{requestId}");
    }

    private RequestEmailNotificationData requestData(Long requestId) {
        return new RequestEmailNotificationData(
                requestId, "TI", "7940", "Centro", "EM_ATENDIMENTO", "Solicitante",
                LocalDateTime.of(2026, 7, 13, 15, 30), List.of(), List.of()
        );
    }
}
