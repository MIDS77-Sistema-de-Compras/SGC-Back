package net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceIntrf;

import net.centroweg.gerenciamentocompras.modules.request.service.api.dto.RequestStatusNotificationData;
import net.centroweg.gerenciamentocompras.modules.request.service.event.RequestStatusChangedEvent;

public interface RequestStatusChangedEmailSender {
    void sendEmails(RequestStatusChangedEvent event, RequestStatusNotificationData request);
}
