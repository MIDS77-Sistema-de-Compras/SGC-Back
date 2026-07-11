package net.centroweg.gerenciamentocompras.modules.delivery.service.notification;

public record DeliveryCreatedNotificationContent(
        String title,
        String message,
        String emailSubject,
        String emailHtml
) {
}
