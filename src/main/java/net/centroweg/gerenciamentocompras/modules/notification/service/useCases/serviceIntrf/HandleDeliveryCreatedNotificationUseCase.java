package net.centroweg.gerenciamentocompras.modules.notification.service.useCases.serviceIntrf;

import net.centroweg.gerenciamentocompras.modules.delivery.service.event.DeliveryCreatedEvent;

public interface HandleDeliveryCreatedNotificationUseCase {
    void handle(DeliveryCreatedEvent event);
}
