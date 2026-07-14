package net.centroweg.gerenciamentocompras.modules.notification.service.factory;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.delivery.service.api.dto.DeliveryNotificationData;
import net.centroweg.gerenciamentocompras.modules.delivery.service.api.dto.DeliveryProductNotificationData;
import net.centroweg.gerenciamentocompras.modules.request.service.event.ItemStatusChangedEvent;
import net.centroweg.gerenciamentocompras.shared.email.components.EmailFooter;
import net.centroweg.gerenciamentocompras.shared.email.components.EmailLayout;
import net.centroweg.gerenciamentocompras.shared.email.components.EmailParagraph;
import net.centroweg.gerenciamentocompras.shared.email.components.EmailTitle;
import net.centroweg.gerenciamentocompras.shared.email.intrf.EmailBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ItemStatusEmailContentFactory {

    private final ItemStatusContentFormatter formatter;

    public boolean isDelivered(String statusName) {
        return formatter.isDelivered(statusName);
    }

    public ItemStatusEmailContent build(ItemStatusChangedEvent event, Optional<DeliveryNotificationData> delivery) {
        String subject = formatter.isDelivered(event.newStatusName())
                ? "Item disponivel para retirada - Solicitacao #" + event.requestId()
                : "Status de item atualizado - Solicitacao #" + event.requestId();
        String message = buildMessage(event, delivery);
        EmailLayout layout = new EmailLayout(
                subject,
                List.<EmailBuilder>of(
                        new EmailTitle(subject),
                        new EmailParagraph(message, "#333333", 14),
                        new EmailFooter()
                )
        );
        return new ItemStatusEmailContent(subject, layout.buildHtml());
    }

    private String buildMessage(ItemStatusChangedEvent event, Optional<DeliveryNotificationData> delivery) {
        StringBuilder builder = new StringBuilder();
        builder.append("<b>Solicitacao atualizada:</b> #").append(event.requestId()).append("<br>");
        builder.append("<b>Item atualizado:</b> ").append(escape(event.itemName()));
        if (formatter.hasText(event.itemCode())) {
            builder.append(" (").append(escape(event.itemCode())).append(")");
        }
        builder.append("<br>");
        builder.append("<b>Status anterior:</b> ").append(formatText(event.previousStatusName())).append("<br>");
        builder.append("<b>Novo status:</b> ").append(formatText(event.newStatusName())).append("<br>");
        builder.append("<b>Data da alteracao:</b> ").append(formatter.formatDateTime(event.changedAt())).append("<br>");
        if (formatter.hasText(event.observation())) {
            builder.append("<b>Observacoes:</b> ").append(escape(event.observation())).append("<br>");
        }
        if (formatter.isDelivered(event.newStatusName())) {
            builder.append("<br>");
            delivery.ifPresentOrElse(
                    value -> appendDeliveryDetails(builder, value, event.changedAt()),
                    () -> builder.append("<b>Entrega:</b> dados de entrega ainda nao vinculados a este item.<br>")
            );
        }
        return builder.toString();
    }

    private void appendDeliveryDetails(
            StringBuilder builder,
            DeliveryNotificationData delivery,
            LocalDateTime changedAt
    ) {
        LocalDateTime arrivalDate = delivery.deliveredAt() != null ? delivery.deliveredAt() : changedAt;
        builder.append("<b>Data de chegada/entrega:</b> ").append(formatter.formatDateTime(arrivalDate)).append("<br>");
        builder.append("<b>Local de retirada:</b> ").append(formatText(delivery.deliveryLocation())).append("<br>");
        builder.append("<b>Tipo de retirada:</b> ").append(formatter.resolveWithdrawalType(delivery.deliveryLocation())).append("<br>");
        builder.append("<b>Itens a recolher:</b><br>");
        appendItems(builder, delivery);
        if (formatter.hasText(delivery.description())) {
            builder.append("<b>Observacoes da entrega:</b> ").append(escape(delivery.description())).append("<br>");
        }
        builder.append("<b>Recebedores:</b> ").append(formatReceivers(delivery)).append("<br>");
    }

    private void appendItems(StringBuilder builder, DeliveryNotificationData delivery) {
        boolean hasItems = false;
        for (DeliveryProductNotificationData item : delivery.productItems()) {
            hasItems = true;
            builder.append("- ").append(escape(item.name()));
            if (formatter.hasText(item.code())) {
                builder.append(" (").append(escape(item.code())).append(")");
            }
            builder.append(" - ").append(formatter.formatQuantity(item.quantity()));
            if (formatter.hasText(item.measurementUnit())) {
                builder.append(" ").append(escape(item.measurementUnit()));
            }
            builder.append("<br>");
        }
        for (String itemName : delivery.provisionItemNames()) {
            hasItems = true;
            builder.append("- ").append(escape(itemName)).append("<br>");
        }
        if (!hasItems) {
            builder.append("- Nenhum item vinculado a entrega.<br>");
        }
    }

    private String formatReceivers(DeliveryNotificationData delivery) {
        return delivery.receiverNames().stream()
                .filter(formatter::hasText)
                .map(this::escape)
                .distinct()
                .reduce((first, second) -> first + ", " + second)
                .orElse("Nao informado");
    }

    private String formatText(String value) {
        return formatter.hasText(value) ? escape(value) : "Nao informado";
    }

    private String escape(String value) {
        return value == null ? "" : HtmlUtils.htmlEscape(value);
    }
}
