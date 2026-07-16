package net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceIntrf;

import net.centroweg.gerenciamentocompras.modules.delivery.service.api.dto.DeliveryCreatedNotificationData;
import net.centroweg.gerenciamentocompras.modules.delivery.service.event.DeliveryCreatedEvent;
import net.centroweg.gerenciamentocompras.modules.notification.service.factory.DeliveryCreatedNotificationContent;

public interface DeliveryCreatedEmailSender {
    void sendEmails(DeliveryCreatedEvent event, DeliveryCreatedNotificationData delivery, DeliveryCreatedNotificationContent content);
}
