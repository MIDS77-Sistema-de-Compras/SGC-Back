package net.centroweg.gerenciamentocompras.modules.request.service.notification;

public record ItemStatusNotificationContent(
        String title,
        String message,
        String emailSubject,
        String emailHtml
) {
}
