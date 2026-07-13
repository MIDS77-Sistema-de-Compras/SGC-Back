package net.centroweg.gerenciamentocompras.modules.notification.service.useCases.serviceImpl;

import jakarta.mail.MessagingException;
import net.centroweg.gerenciamentocompras.modules.notification.service.factory.ItemStatusEmailContent;
import net.centroweg.gerenciamentocompras.modules.notification.service.recipient.ItemStatusRecipientDeduplicator;
import net.centroweg.gerenciamentocompras.modules.request.service.api.dto.RequestNotificationRecipient;
import net.centroweg.gerenciamentocompras.modules.request.service.event.ItemStatusChangedEvent;
import net.centroweg.gerenciamentocompras.modules.request.service.event.RequestItemType;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SendItemStatusChangedEmailServiceImplTest {

    @Mock private EmailSenderService emailSenderService;

    @Test
    void shouldDeduplicateNormalizedEmailsSkipMissingAndContinueAfterFailure() throws Exception {
        SendItemStatusChangedEmailServiceImpl service = new SendItemStatusChangedEmailServiceImpl(
                emailSenderService,
                new ItemStatusRecipientDeduplicator()
        );
        List<RequestNotificationRecipient> recipients = List.of(
                new RequestNotificationRecipient(1L, "Ana", " ANA@TESTE.COM "),
                new RequestNotificationRecipient(2L, "Ana duplicada", "ana@teste.com"),
                new RequestNotificationRecipient(3L, "Sem email", "  "),
                new RequestNotificationRecipient(4L, "Bia", "bia@teste.com")
        );
        doAnswer(invocation -> {
            DefaultEmail email = invocation.getArgument(0);
            if (email.getSendTo().equals("ana@teste.com")) {
                throw new MessagingException("SMTP indisponivel");
            }
            return null;
        }).when(emailSenderService).sendEmail(org.mockito.ArgumentMatchers.any(), anyString());

        service.sendEmails(event(), recipients, new ItemStatusEmailContent("Assunto", "<b>HTML</b>"));

        ArgumentCaptor<DefaultEmail> emails = ArgumentCaptor.forClass(DefaultEmail.class);
        verify(emailSenderService, times(2)).sendEmail(emails.capture(), anyString());
        assertThat(emails.getAllValues())
                .extracting(DefaultEmail::getSendTo)
                .containsExactly("ana@teste.com", "bia@teste.com");
    }

    private ItemStatusChangedEvent event() {
        return new ItemStatusChangedEvent(
                10L, 99L, RequestItemType.PRODUCT, "Parafuso", "PRD-1", 2.0, "UN",
                "Aprovado", "Entregue", null, LocalDateTime.now()
        );
    }
}
