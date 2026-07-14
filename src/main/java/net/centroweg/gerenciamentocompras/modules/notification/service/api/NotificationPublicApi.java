package net.centroweg.gerenciamentocompras.modules.notification.service.api;

import java.util.Collection;

/**
 * Fachada pública para criação de notificações internas por outros módulos.
 */
public interface NotificationPublicApi {

    void createInternalNotifications(
            String title,
            String message,
            Long requestId,
            Collection<Long> userIds
    );
}
