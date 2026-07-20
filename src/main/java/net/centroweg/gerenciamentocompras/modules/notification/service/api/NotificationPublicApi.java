package net.centroweg.gerenciamentocompras.modules.notification.service.api;

import java.util.Collection;

import net.centroweg.gerenciamentocompras.modules.notification.domain.enums.NotificationType;

/**
 * Fachada pública para criação de notificações internas por outros módulos.
 */
public interface NotificationPublicApi {

    void createInternalNotifications(
            String title,
            String message,
            NotificationType notificationType,
            Long requestId,
            Collection<Long> userIds
    );
}
