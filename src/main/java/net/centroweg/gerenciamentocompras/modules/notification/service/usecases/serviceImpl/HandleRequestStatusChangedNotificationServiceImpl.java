package net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceImpl;

import java.text.Normalizer;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.notification.domain.enums.NotificationType;
import net.centroweg.gerenciamentocompras.modules.notification.service.factory.RequestStatusInternalNotificationFactory;
import net.centroweg.gerenciamentocompras.modules.notification.service.recipient.RequestNotificationRecipientDeduplicator;
import net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceIntrf.CreateInternalNotificationUseCase;
import net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceIntrf.HandleRequestStatusChangedNotificationUseCase;
import net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceIntrf.RequestStatusChangedEmailSender;
import net.centroweg.gerenciamentocompras.modules.request.service.api.RequestPublicApi;
import net.centroweg.gerenciamentocompras.modules.request.service.api.dto.RequestStatusNotificationData;
import net.centroweg.gerenciamentocompras.modules.request.service.event.RequestStatusChangedEvent;
import net.centroweg.gerenciamentocompras.modules.user.service.api.UserPublicApi;
import net.centroweg.gerenciamentocompras.shared.security.authority.Authorities;

@Service
@RequiredArgsConstructor
public class HandleRequestStatusChangedNotificationServiceImpl implements HandleRequestStatusChangedNotificationUseCase {

    private static final Set<String> PURCHASE_FLOW_ENTRY_STATUSES = Set.of(
            "aprovado",
            "auto aprovado",
            "parcialmente aprovada"
    );

    private final RequestPublicApi requestPublicApi;
    private final UserPublicApi userPublicApi;
    private final CreateInternalNotificationUseCase createInternalNotificationUseCase;
    private final RequestStatusInternalNotificationFactory internalFactory;
    private final RequestNotificationRecipientDeduplicator recipientDeduplicator;
    private final RequestStatusChangedEmailSender emailSender;

    @Override
    public void handle(RequestStatusChangedEvent event) {
        var request = requestPublicApi.findStatusNotificationDataById(event.requestId());
        var internalContent = internalFactory.build(event);
        createInternalNotificationUseCase.createNotifications(
                internalContent.title(), internalContent.message(), NotificationType.STATUS_ALTERADO, event.requestId(),
                internalRecipientIds(event, request)
        );
        emailSender.sendEmails(event, request);
    }

    private List<Long> internalRecipientIds(
            RequestStatusChangedEvent event,
            RequestStatusNotificationData request
    ) {
        LinkedHashSet<Long> recipientIds = new LinkedHashSet<>(
                recipientDeduplicator.distinctUserIds(request.recipients())
        );

        if (PURCHASE_FLOW_ENTRY_STATUSES.contains(normalizeStatus(event.newStatusName()))) {
            recipientIds.addAll(userPublicApi.findActiveUserIdsByRole(Authorities.COMPRADOR));
        }

        return recipientIds.stream().toList();
    }

    private String normalizeStatus(String statusName) {
        if (statusName == null) {
            return "";
        }

        return Normalizer.normalize(statusName, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replace('_', ' ')
                .replace('-', ' ')
                .trim()
                .toLowerCase(Locale.ROOT)
                .replaceAll("\\s+", " ");
    }
}
