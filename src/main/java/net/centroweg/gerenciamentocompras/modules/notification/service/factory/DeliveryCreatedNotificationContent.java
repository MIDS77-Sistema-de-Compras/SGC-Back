package net.centroweg.gerenciamentocompras.modules.notification.service.factory;

public record DeliveryCreatedNotificationContent(String title, String internalMessage, String emailSubject, String emailHtml) {
}
