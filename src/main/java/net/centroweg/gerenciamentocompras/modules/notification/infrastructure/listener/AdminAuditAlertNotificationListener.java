package net.centroweg.gerenciamentocompras.modules.notification.infrastructure.listener;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.notification.domain.enums.NotificationType;
import net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceIntrf.CreateInternalNotificationUseCase;
import net.centroweg.gerenciamentocompras.modules.user.service.api.UserPublicApi;
import net.centroweg.gerenciamentocompras.shared.audit.service.event.AuditAlertCreatedEvent;
import net.centroweg.gerenciamentocompras.shared.security.authority.Authorities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

/**
 * Transforma alertas de auditoria em notificações internas para administradores ativos.
 */
@Component
@RequiredArgsConstructor
public class AdminAuditAlertNotificationListener {

    private static final Logger log = LoggerFactory.getLogger(AdminAuditAlertNotificationListener.class);
    private static final String TITLE = "Alerta administrativo";

    private final UserPublicApi userPublicApi;
    private final CreateInternalNotificationUseCase createInternalNotificationUseCase;

    @EventListener
    public void onAuditAlertCreated(AuditAlertCreatedEvent event) {
        try {
            List<Long> administratorIds = userPublicApi.findActiveUserIdsByRole(Authorities.ADMIN);
            if (administratorIds.isEmpty()) {
                return;
            }

            createInternalNotificationUseCase.createNotifications(
                    TITLE,
                    buildMessage(event),
                    NotificationType.ALERTA_ADMINISTRATIVO,
                    event.requestId(),
                    administratorIds
            );
        } catch (RuntimeException exception) {
            log.warn("Não foi possível criar notificações para o alerta de auditoria {}.", event.auditLogId(), exception);
        }
    }

    private String buildMessage(AuditAlertCreatedEvent event) {
        String readableAction = event.action()
                .toLowerCase(Locale.ROOT)
                .replace('_', ' ');
        return "A ação " + readableAction + " foi realizada por " + event.actorName() + ".";
    }
}
