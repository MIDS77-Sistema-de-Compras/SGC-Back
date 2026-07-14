package net.centroweg.gerenciamentocompras.modules.notification.service.useCases.serviceImpl;

import net.centroweg.gerenciamentocompras.modules.delivery.service.api.DeliveryPublicApi;
import net.centroweg.gerenciamentocompras.modules.delivery.service.api.dto.DeliveryCreatedNotificationData;
import net.centroweg.gerenciamentocompras.modules.delivery.service.api.dto.DeliveryNotificationRecipient;
import net.centroweg.gerenciamentocompras.modules.delivery.service.api.dto.DeliveryProductNotificationData;
import net.centroweg.gerenciamentocompras.modules.delivery.service.event.DeliveryCreatedEvent;
import net.centroweg.gerenciamentocompras.modules.notification.service.factory.DeliveryCreatedNotificationContentFactory;
import net.centroweg.gerenciamentocompras.modules.notification.service.recipient.DeliveryNotificationRecipientDeduplicator;
import net.centroweg.gerenciamentocompras.modules.notification.service.useCases.serviceIntrf.CreateInternalNotificationUseCase;
import net.centroweg.gerenciamentocompras.modules.notification.service.useCases.serviceIntrf.DeliveryCreatedEmailSender;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeliveryCreatedNotificationFlowTest {

    @Mock DeliveryPublicApi deliveryPublicApi;
    @Mock CreateInternalNotificationUseCase createInternalNotificationUseCase;
    @Mock DeliveryCreatedEmailSender emailSender;
    @Mock EmailSenderService externalEmailSender;

    @Test
    void shouldUseCentralPersistenceForDistinctReceivers() {
        var event = event();
        var data = data(List.of(
                new DeliveryNotificationRecipient(1L, "Ana", "ana@teste.com"),
                new DeliveryNotificationRecipient(1L, "Ana", "ana@teste.com"),
                new DeliveryNotificationRecipient(2L, "Bia", "bia@teste.com")));
        when(deliveryPublicApi.findNotificationDataById(100L)).thenReturn(data);
        var service = new HandleDeliveryCreatedNotificationServiceImpl(
                deliveryPublicApi, createInternalNotificationUseCase, new DeliveryCreatedNotificationContentFactory(),
                new DeliveryNotificationRecipientDeduplicator(), emailSender);

        service.handle(event);

        ArgumentCaptor<String> message = ArgumentCaptor.forClass(String.class);
        verify(createInternalNotificationUseCase).createNotifications(anyString(), message.capture(), eq(10L), eq(List.of(1L, 2L)));
        assertThat(message.getValue()).doesNotContain("<b>", "<br>", "<table>");
        verify(emailSender).sendEmails(eq(event), same(data), any());
    }

    @Test
    void shouldDeduplicateNormalizedDeliveryEmails() throws Exception {
        var data = data(List.of(
                new DeliveryNotificationRecipient(1L, "Ana", " ANA@TESTE.COM "),
                new DeliveryNotificationRecipient(2L, "Clone", "ana@teste.com")));
        var content = new DeliveryCreatedNotificationContentFactory().build(data);
        var sender = new SendDeliveryCreatedEmailServiceImpl(
                externalEmailSender, new DeliveryNotificationRecipientDeduplicator());

        sender.sendEmails(event(), data, content);

        ArgumentCaptor<DefaultEmail> email = ArgumentCaptor.forClass(DefaultEmail.class);
        verify(externalEmailSender).sendEmail(email.capture(), anyString());
        assertThat(email.getValue().getSendTo()).isEqualTo("ana@teste.com");
    }

    @Test
    void shouldEscapeDeliveryDynamicHtml() {
        var data = new DeliveryCreatedNotificationData(100L, 10L, 20L, "Em andamento",
                LocalDateTime.now(), null, "<script>local</script>", "<b>obs</b>", List.of(),
                List.of(new DeliveryProductNotificationData("<img>", "P<1", 2.0, "UN")), List.of());
        var content = new DeliveryCreatedNotificationContentFactory().build(data);
        assertThat(content.emailHtml()).contains("&lt;script&gt;local&lt;/script&gt;", "&lt;img&gt;", "&lt;b&gt;obs&lt;/b&gt;")
                .doesNotContain("<script>local</script>");
        assertThat(content.internalMessage())
                .contains("&lt;script&gt;local&lt;/script&gt;", "&lt;img&gt;", "&lt;b&gt;obs&lt;/b&gt;")
                .doesNotContain("<script>", "<b>", "<br>", "<table>");
    }

    private DeliveryCreatedEvent event() {
        return new DeliveryCreatedEvent(100L, 10L, LocalDateTime.now());
    }

    private DeliveryCreatedNotificationData data(List<DeliveryNotificationRecipient> recipients) {
        return new DeliveryCreatedNotificationData(100L, 10L, 20L, "Em andamento",
                LocalDateTime.now().plusDays(1), null, "SENAI", "Entrega", recipients,
                List.of(new DeliveryProductNotificationData("Parafuso", "P-1", 2.0, "UN")), List.of());
    }
}
