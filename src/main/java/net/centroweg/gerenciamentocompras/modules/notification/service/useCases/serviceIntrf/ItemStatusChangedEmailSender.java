package net.centroweg.gerenciamentocompras.modules.notification.service.useCases.serviceIntrf;

import net.centroweg.gerenciamentocompras.modules.notification.service.factory.ItemStatusEmailContent;
import net.centroweg.gerenciamentocompras.modules.request.service.api.dto.RequestNotificationRecipient;
import net.centroweg.gerenciamentocompras.modules.request.service.event.ItemStatusChangedEvent;

import java.util.List;

public interface ItemStatusChangedEmailSender {

    void sendEmails(
            ItemStatusChangedEvent event,
            List<RequestNotificationRecipient> recipients,
            ItemStatusEmailContent content
    );
}
