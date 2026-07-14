package net.centroweg.gerenciamentocompras.modules.notification.service.useCases.serviceIntrf;

import net.centroweg.gerenciamentocompras.modules.request.service.event.RequestStatusChangedEvent;

public interface HandleRequestStatusChangedNotificationUseCase {
    void handle(RequestStatusChangedEvent event);
}
