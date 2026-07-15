package net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceIntrf;

import net.centroweg.gerenciamentocompras.modules.request.service.event.ItemStatusChangedEvent;

public interface HandleItemStatusChangedNotificationUseCase {

    void handle(ItemStatusChangedEvent event);
}
