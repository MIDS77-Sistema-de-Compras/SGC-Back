package net.centroweg.gerenciamentocompras.modules.notification.service.factory;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.delivery.service.api.dto.DeliveryNotificationData;
import net.centroweg.gerenciamentocompras.modules.delivery.service.api.dto.DeliveryProductNotificationData;
import net.centroweg.gerenciamentocompras.modules.request.service.event.ItemStatusChangedEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ItemStatusInternalNotificationFactory {

    private final ItemStatusContentFormatter formatter;

    public ItemStatusInternalNotificationContent build(
            ItemStatusChangedEvent event,
            Optional<DeliveryNotificationData> delivery
    ) {
        String title = formatter.isDelivered(event.newStatusName())
                ? "Item disponivel para retirada - Solicitacao #" + event.requestId()
                : "Status de item atualizado - Solicitacao #" + event.requestId();

        StringBuilder message = new StringBuilder();
        message.append("Solicitacao atualizada: #").append(event.requestId()).append('\n');
        message.append("Item atualizado: ").append(formatter.valueOrNotInformed(event.itemName()));
        if (formatter.hasText(event.itemCode())) {
            message.append(" (").append(event.itemCode()).append(')');
        }
        message.append('\n');
        message.append("Status anterior: ").append(formatter.valueOrNotInformed(event.previousStatusName())).append('\n');
        message.append("Novo status: ").append(formatter.valueOrNotInformed(event.newStatusName())).append('\n');
        message.append("Data da alteracao: ").append(formatter.formatDateTime(event.changedAt())).append('\n');

        if (formatter.hasText(event.observation())) {
            message.append("Observacoes: ").append(event.observation()).append('\n');
        }

        if (formatter.isDelivered(event.newStatusName())) {
            delivery.ifPresentOrElse(
                    value -> appendDeliveryDetails(message, value, event.changedAt()),
                    () -> message.append("Entrega: dados de entrega ainda nao vinculados a este item.\n")
            );
        }

        return new ItemStatusInternalNotificationContent(title, message.toString().stripTrailing());
    }

    private void appendDeliveryDetails(
            StringBuilder message,
            DeliveryNotificationData delivery,
            LocalDateTime changedAt
    ) {
        LocalDateTime arrivalDate = delivery.deliveredAt() != null ? delivery.deliveredAt() : changedAt;
        message.append("Data de chegada/entrega: ").append(formatter.formatDateTime(arrivalDate)).append('\n');
        message.append("Local de retirada: ").append(formatter.valueOrNotInformed(delivery.deliveryLocation())).append('\n');
        message.append("Tipo de retirada: ").append(formatter.resolveWithdrawalType(delivery.deliveryLocation())).append('\n');
        message.append("Itens a recolher:\n");
        appendItems(message, delivery);
        if (formatter.hasText(delivery.description())) {
            message.append("Observacoes da entrega: ").append(delivery.description()).append('\n');
        }
        message.append("Recebedores: ").append(formatReceivers(delivery)).append('\n');
    }

    private void appendItems(StringBuilder message, DeliveryNotificationData delivery) {
        boolean hasItems = false;
        for (DeliveryProductNotificationData item : delivery.productItems()) {
            hasItems = true;
            message.append("- ").append(formatter.valueOrNotInformed(item.name()));
            if (formatter.hasText(item.code())) {
                message.append(" (").append(item.code()).append(')');
            }
            message.append(" - ").append(formatter.formatQuantity(item.quantity()));
            if (formatter.hasText(item.measurementUnit())) {
                message.append(' ').append(item.measurementUnit());
            }
            message.append('\n');
        }
        for (String itemName : delivery.provisionItemNames()) {
            hasItems = true;
            message.append("- ").append(formatter.valueOrNotInformed(itemName)).append('\n');
        }
        if (!hasItems) {
            message.append("- Nenhum item vinculado a entrega.\n");
        }
    }

    private String formatReceivers(DeliveryNotificationData delivery) {
        String receivers = delivery.receiverNames().stream()
                .filter(formatter::hasText)
                .distinct()
                .reduce((first, second) -> first + ", " + second)
                .orElse("Nao informado");
        return formatter.valueOrNotInformed(receivers);
    }
}
