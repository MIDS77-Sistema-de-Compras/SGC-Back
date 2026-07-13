package net.centroweg.gerenciamentocompras.modules.request.service.api.dto;

public record RequestNotificationRecipient(
        Long userId,
        String userName,
        String email
) {
}
