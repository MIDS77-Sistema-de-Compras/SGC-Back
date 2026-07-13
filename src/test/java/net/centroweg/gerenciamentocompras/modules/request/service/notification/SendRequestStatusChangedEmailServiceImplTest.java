package net.centroweg.gerenciamentocompras.modules.request.service.notification;

import jakarta.mail.MessagingException;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Branch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Cr;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.service.event.RequestStatusChangedEvent;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.shared.email.model.DefaultEmail;
import net.centroweg.gerenciamentocompras.shared.email.service.EmailSenderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SendRequestStatusChangedEmailServiceImplTest {

    @Mock private RequestRepository requestRepository;
    @Mock private EmailSenderService emailSenderService;

    private SendRequestStatusChangedEmailServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new SendRequestStatusChangedEmailServiceImpl(
                requestRepository,
                emailSenderService,
                new RequestStatusEmailMessageFactory("http://localhost/docente/solicitacoes/{requestId}")
        );
    }

    @Test
    void shouldSendOneEmailForEachDistinctAddress() throws Exception {
        Request request = request(
                user(1L, "Ana", "ana@teste.com"),
                user(2L, "Bia", "bia@teste.com")
        );
        mockRequest(request);

        service.sendEmails(event("Aprovado", "Em atendimento", null));

        ArgumentCaptor<DefaultEmail> captor = ArgumentCaptor.forClass(DefaultEmail.class);
        verify(emailSenderService, times(2)).sendEmail(captor.capture(), anyString());
        assertThat(captor.getAllValues())
                .extracting(DefaultEmail::getSendTo)
                .containsExactly("ana@teste.com", "bia@teste.com");
    }

    @Test
    void shouldDeduplicateEmailsAfterTrimAndLowercase() throws Exception {
        Request request = request(
                user(1L, "Ana", " ana@teste.com "),
                user(2L, "Ana duplicada", "ANA@TESTE.COM")
        );
        mockRequest(request);

        service.sendEmails(event("Em atendimento", "Entregue", null));

        ArgumentCaptor<DefaultEmail> captor = ArgumentCaptor.forClass(DefaultEmail.class);
        verify(emailSenderService, times(1)).sendEmail(captor.capture(), anyString());
        assertThat(captor.getValue().getSendTo()).isEqualTo("ana@teste.com");
    }

    @Test
    void shouldKeepInternalNotificationAndSkipRequesterWithoutEmail() throws Exception {
        Request request = request(user(1L, "Ana", "   "));
        mockRequest(request);

        service.sendEmails(event("Em atendimento", "Cancelado", null));

        verify(emailSenderService, never()).sendEmail(any(), anyString());
    }

    @Test
    void shouldContinueSendingAfterOneRecipientFails() throws Exception {
        Request request = request(
                user(1L, "Ana", "ana@teste.com"),
                user(2L, "Bia", "bia@teste.com")
        );
        mockRequest(request);
        doThrow(new MessagingException("SMTP indisponível"))
                .doNothing()
                .when(emailSenderService)
                .sendEmail(any(DefaultEmail.class), anyString());

        service.sendEmails(event("Aprovado", "Em atendimento", null));

        verify(emailSenderService, times(2)).sendEmail(any(DefaultEmail.class), anyString());
    }

    private void mockRequest(Request request) {
        when(requestRepository.findForStatusNotificationById(10L)).thenReturn(Optional.of(request));
    }

    private RequestStatusChangedEvent event(String previousStatus, String newStatus, String justification) {
        return new RequestStatusChangedEvent(
                10L,
                previousStatus,
                newStatus,
                justification,
                50L,
                "Responsável",
                LocalDateTime.of(2026, 7, 13, 15, 30)
        );
    }

    private Request request(User... users) {
        Request request = new Request(
                new CrBranch(new Branch("Filial Centro"), new Cr("TI", "7940", false), List.of()),
                new Status("Em atendimento", "Status")
        );
        request.setId(10L);
        request.setRequestDate(LocalDateTime.of(2026, 7, 10, 9, 0));
        request.getCreatedByUsers().addAll(List.of(users));
        return request;
    }

    private User user(Long id, String name, String email) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        return user;
    }
}
