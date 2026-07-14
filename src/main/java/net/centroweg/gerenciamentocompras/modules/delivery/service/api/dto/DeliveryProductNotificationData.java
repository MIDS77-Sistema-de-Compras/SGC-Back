package net.centroweg.gerenciamentocompras.modules.delivery.service.api.dto;

public record DeliveryProductNotificationData(
        String name,
        String code,
        Double quantity,
        String measurementUnit
) {
}
