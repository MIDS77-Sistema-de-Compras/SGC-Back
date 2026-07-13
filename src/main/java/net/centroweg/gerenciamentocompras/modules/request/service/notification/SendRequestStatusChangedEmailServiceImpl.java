package net.centroweg.gerenciamentocompras.modules.request.service.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.service.event.RequestStatusChangedEvent;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.shared.email.model.DefaultEmail;
import net.centroweg.gerenciamentocompras.shared.email.service.EmailSenderService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendRequestStatusChangedEmailServiceImpl {

    private final RequestRepository requestRepository;
    private final EmailSenderService emailSenderService;
    private final RequestStatusEmailMessageFactory messageFactory;

    @Async
    @Transactional(readOnly = true)
    public void sendEmails(RequestStatusChangedEvent event) {
        Request request = requestRepository.findForStatusNotificationById(event.requestId())
                .orElseThrow(RequestNotFoundException::new);

        requesterMap(request).values().forEach(user -> sendToUser(event, request, user));
    }

    private void sendToUser(RequestStatusChangedEvent event, Request request, User user) {
        if (!hasText(user.getEmail())) {
            log.info(
                    "Solicitante sem e-mail; notificação interna mantida. requestId={}, userId={}, novoStatus={}",
                    event.requestId(),
                    user.getId(),
                    event.newStatusName()
            );
            return;
        }

        try {
            RequestStatusEmailContent content = messageFactory.build(event, request, user);
            emailSenderService.sendEmail(
                    new DefaultEmail(content.subject(), user.getEmail().trim()),
                    content.html()
            );
        } catch (Exception exception) {
            log.error(
                    "Falha ao enviar e-mail de status. requestId={}, userId={}, novoStatus={}",
                    event.requestId(),
                    user.getId(),
                    event.newStatusName(),
                    exception
            );
        }
    }

    private Map<String, User> requesterMap(Request request) {
        Map<String, User> users = new LinkedHashMap<>();
        request.getCreatedByUsers().forEach(user -> {
            String key = hasText(user.getEmail())
                    ? user.getEmail().trim().toLowerCase(Locale.ROOT)
                    : "user:" + user.getId();
            users.putIfAbsent(key, user);
        });
        return users;
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
