package net.centroweg.gerenciamentocompras.modules.request.service.notification;

import net.centroweg.gerenciamentocompras.modules.request.service.event.RequestStatusChangedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RequestStatusChangedEventListenerTest {

    @Mock private CreateRequestStatusNotificationServiceImpl createNotificationService;
    @Mock private SendRequestStatusChangedEmailServiceImpl sendEmailService;

    @InjectMocks
    private RequestStatusChangedEventListener listener;

    @Test
    void shouldDelegateInternalNotificationAndEmail() {
        RequestStatusChangedEvent event = new RequestStatusChangedEvent(
                10L,
                "Aprovado",
                "Em atendimento",
                null,
                20L,
                "Responsável",
                LocalDateTime.now()
        );

        listener.onRequestStatusChanged(event);

        verify(createNotificationService).createNotifications(event);
        verify(sendEmailService).sendEmails(event);
    }
}
