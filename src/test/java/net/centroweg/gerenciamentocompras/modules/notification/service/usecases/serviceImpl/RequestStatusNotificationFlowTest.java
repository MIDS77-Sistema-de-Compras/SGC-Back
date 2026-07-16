package net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceImpl;

import jakarta.mail.MessagingException;
import net.centroweg.gerenciamentocompras.modules.notification.infrastructure.listener.RequestStatusChangedEventListener;
import net.centroweg.gerenciamentocompras.modules.notification.service.factory.RequestStatusEmailContentFactory;
import net.centroweg.gerenciamentocompras.modules.notification.service.factory.RequestStatusInternalNotificationFactory;
import net.centroweg.gerenciamentocompras.modules.notification.service.recipient.RequestNotificationRecipientDeduplicator;
import net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceIntrf.CreateInternalNotificationUseCase;
import net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceIntrf.HandleRequestStatusChangedNotificationUseCase;
import net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceIntrf.RequestStatusChangedEmailSender;
import net.centroweg.gerenciamentocompras.modules.request.service.api.RequestPublicApi;
import net.centroweg.gerenciamentocompras.modules.request.service.api.dto.RequestNotificationRecipient;
import net.centroweg.gerenciamentocompras.modules.request.service.api.dto.RequestStatusNotificationData;
import net.centroweg.gerenciamentocompras.modules.request.service.event.RequestStatusChangedEvent;
import net.centroweg.gerenciamentocompras.shared.email.model.DefaultEmail;
import net.centroweg.gerenciamentocompras.shared.email.service.EmailSenderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestStatusNotificationFlowTest {

    @Mock RequestPublicApi requestPublicApi;
    @Mock CreateInternalNotificationUseCase createInternalNotificationUseCase;
    @Mock RequestStatusChangedEmailSender emailSender;
    @Mock EmailSenderService externalEmailSender;
    @Mock HandleRequestStatusChangedNotificationUseCase handler;

    @Test
    void shouldPersistForDistinctUsersAndDelegateSpecificEmail() {
        var data = data(List.of(
                new RequestNotificationRecipient(1L, "Ana", "ana@teste.com"),
                new RequestNotificationRecipient(1L, "Ana", "ana@teste.com"),
                new RequestNotificationRecipient(2L, "Bia", "bia@teste.com")));
        when(requestPublicApi.findStatusNotificationDataById(10L)).thenReturn(data);
        var service = new HandleRequestStatusChangedNotificationServiceImpl(
                requestPublicApi, createInternalNotificationUseCase, new RequestStatusInternalNotificationFactory(),
                new RequestNotificationRecipientDeduplicator(), emailSender);

        service.handle(event("Em atendimento", null));

        verify(createInternalNotificationUseCase).createNotifications(anyString(), anyString(), eq(10L), eq(List.of(1L, 2L)));
        verify(emailSender).sendEmails(any(), same(data));
    }

    @Test
    void shouldKeepInternalMessagePlainAndIncludeTrimmedRefusalReason() {
        var content = new RequestStatusInternalNotificationFactory().build(event("Recusado", "  <b>sem verba</b>  "));
        assertThat(content.message()).contains("Justificativa: &lt;b&gt;sem verba&lt;/b&gt;")
                .doesNotContain("<br>", "<div>", "<table>");
    }

    @Test
    void shouldOmitRefusalReasonWhenItIsAbsent() {
        var internal = new RequestStatusInternalNotificationFactory().build(event("Recusado", null));
        var email = new RequestStatusEmailContentFactory("http://localhost/requests/{requestId}")
                .build(event("Recusado", "   "), data(List.of()), "Ana");

        assertThat(internal.message()).doesNotContain("Justificativa", "null");
        assertThat(email.html()).doesNotContain("Justificativa", ">null<");
    }

    @Test
    void shouldEscapeDynamicValuesInEmail() {
        var factory = new RequestStatusEmailContentFactory("http://localhost/requests/{requestId}");
        var html = factory.build(event("Recusado", "<script>alert(1)</script>"), data(List.of()), "<b>Ana</b>").html();
        assertThat(html).contains("&lt;script&gt;alert(1)&lt;/script&gt;", "&lt;b&gt;Ana&lt;/b&gt;")
                .doesNotContain("<script>alert(1)</script>");
    }

    @Test
    void shouldDeduplicateEmailAndContinueAfterSmtpFailure() throws Exception {
        var sender = new SendRequestStatusChangedEmailServiceImpl(
                externalEmailSender, new RequestNotificationRecipientDeduplicator(),
                new RequestStatusEmailContentFactory("http://localhost/requests/{requestId}"));
        var recipients = List.of(
                new RequestNotificationRecipient(1L, "Ana", " ANA@TESTE.COM "),
                new RequestNotificationRecipient(2L, "Clone", "ana@teste.com"),
                new RequestNotificationRecipient(3L, "Bia", "bia@teste.com"));
        doThrow(new MessagingException("SMTP")).doNothing().when(externalEmailSender)
                .sendEmail(any(DefaultEmail.class), anyString());

        sender.sendEmails(event("Em atendimento", null), data(recipients));

        verify(externalEmailSender, times(2)).sendEmail(any(DefaultEmail.class), anyString());
    }

    @Test
    void listenerShouldDependOnlyOnHandlerInterface() {
        var listener = new RequestStatusChangedEventListener(handler);
        var event = event("Entregue", null);
        listener.onRequestStatusChanged(event);
        verify(handler).handle(event);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Em atendimento", "Entregue", "Cancelado"})
    void shouldNotifyForEveryValidChangedStatus(String status) {
        var data = data(List.of(new RequestNotificationRecipient(1L, "Ana", "ana@teste.com")));
        when(requestPublicApi.findStatusNotificationDataById(10L)).thenReturn(data);
        var service = new HandleRequestStatusChangedNotificationServiceImpl(
                requestPublicApi, createInternalNotificationUseCase, new RequestStatusInternalNotificationFactory(),
                new RequestNotificationRecipientDeduplicator(), emailSender);
        service.handle(event(status, null));
        verify(createInternalNotificationUseCase).createNotifications(anyString(), contains(status), eq(10L), eq(List.of(1L)));
        verify(emailSender).sendEmails(any(), same(data));
    }

    private RequestStatusNotificationData data(List<RequestNotificationRecipient> recipients) {
        return new RequestStatusNotificationData(10L, "TI", "7940", "Centro", "Em atendimento",
                LocalDateTime.of(2026, 7, 10, 9, 0), recipients);
    }

    private RequestStatusChangedEvent event(String newStatus, String justification) {
        return new RequestStatusChangedEvent(10L, 1L, "Aprovado", 2L, newStatus, justification,
                20L, "Responsavel", LocalDateTime.of(2026, 7, 13, 15, 30));
    }
}
