package net.centroweg.gerenciamentocompras.modules.request.service.notification;

public record RequestStatusEmailContent(
        String subject,
        String html
) {
}
