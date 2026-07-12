package net.centroweg.gerenciamentocompras.modules.request.service.notification;

import net.centroweg.gerenciamentocompras.modules.delivery.domain.entity.Delivery;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.entity.DeliveryReceiver;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProduct;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProvision;
import net.centroweg.gerenciamentocompras.modules.request.domain.strategy.DeliveredStatusImpl;
import net.centroweg.gerenciamentocompras.modules.request.service.event.ItemStatusChangedEvent;
import net.centroweg.gerenciamentocompras.modules.request.service.event.RequestItemType;
import net.centroweg.gerenciamentocompras.shared.email.components.EmailFooter;
import net.centroweg.gerenciamentocompras.shared.email.components.EmailLayout;
import net.centroweg.gerenciamentocompras.shared.email.components.EmailParagraph;
import net.centroweg.gerenciamentocompras.shared.email.components.EmailTitle;
import net.centroweg.gerenciamentocompras.shared.email.intrf.EmailBuilder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Component
public class ItemStatusEmailMessageFactory {

    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy 'as' HH:mm");
    private static final String DELIVERED_STATUS = new DeliveredStatusImpl().getName();

    public ItemStatusNotificationContent build(ItemStatusChangedEvent event, Optional<Delivery> delivery) {
        boolean delivered = isDelivered(event.newStatusName());
        String subject = delivered
                ? "Item disponivel para retirada - Solicitacao #" + event.requestId()
                : "Status de item atualizado - Solicitacao #" + event.requestId();

        String message = buildMessage(event, delivered ? delivery : Optional.empty());

        EmailLayout layout = new EmailLayout(
                subject,
                List.<EmailBuilder>of(
                        new EmailTitle(subject),
                        new EmailParagraph(message, "#333333", 14),
                        new EmailFooter()
                )
        );

        return new ItemStatusNotificationContent(subject, message, subject, layout.buildHtml());
    }

    public boolean isDelivered(String statusName) {
        return normalize(statusName).equals(normalize(DELIVERED_STATUS));
    }

    private String buildMessage(ItemStatusChangedEvent event, Optional<Delivery> delivery) {
        StringBuilder builder = new StringBuilder();

        builder.append("<b>Solicitacao atualizada:</b> #").append(event.requestId()).append("<br>");
        builder.append("<b>Item atualizado:</b> ").append(escape(event.itemName()));

        if (hasText(event.itemCode())) {
            builder.append(" (").append(escape(event.itemCode())).append(")");
        }

        builder.append("<br>");
        builder.append("<b>Status anterior:</b> ").append(formatText(event.previousStatusName())).append("<br>");
        builder.append("<b>Novo status:</b> ").append(formatText(event.newStatusName())).append("<br>");
        builder.append("<b>Data da alteracao:</b> ").append(formatDateTime(event.changedAt())).append("<br>");

        if (hasText(event.observation())) {
            builder.append("<b>Observacoes:</b> ").append(escape(event.observation())).append("<br>");
        }

        if (isDelivered(event.newStatusName())) {
            builder.append("<br>");
            delivery.ifPresentOrElse(
                    value -> appendDeliveryDetails(builder, value, event),
                    () -> builder.append("<b>Entrega:</b> dados de entrega ainda nao vinculados a este item.<br>")
            );
        }

        return builder.toString();
    }

    private void appendDeliveryDetails(StringBuilder builder, Delivery delivery, ItemStatusChangedEvent event) {
        LocalDateTime arrivalDate = delivery.getDeliveredAt() != null ? delivery.getDeliveredAt() : event.changedAt();

        builder.append("<b>Data de chegada/entrega:</b> ").append(formatDateTime(arrivalDate)).append("<br>");
        builder.append("<b>Local de retirada:</b> ").append(formatText(delivery.getDeliveryLocation())).append("<br>");
        builder.append("<b>Tipo de retirada:</b> ").append(resolveWithdrawalType(delivery.getDeliveryLocation())).append("<br>");
        builder.append("<b>Itens a recolher:</b><br>");
        appendItemsToCollect(builder, delivery);

        if (hasText(delivery.getDescription())) {
            builder.append("<b>Observacoes da entrega:</b> ").append(escape(delivery.getDescription())).append("<br>");
        }

        builder.append("<b>Recebedores:</b> ").append(formatReceivers(delivery)).append("<br>");
    }

    private void appendItemsToCollect(StringBuilder builder, Delivery delivery) {
        boolean hasItems = false;

        for (ItemRequestProduct item : delivery.getProductItems()) {
            hasItems = true;
            builder.append("- ").append(escape(item.getProduct().getName()));
            if (hasText(item.getProduct().getCode())) {
                builder.append(" (").append(escape(item.getProduct().getCode())).append(")");
            }
            builder.append(" - ").append(formatQuantity(item.getQuantity()));
            if (item.getMeasurementUnit() != null) {
                builder.append(" ").append(escape(item.getMeasurementUnit().getName()));
            }
            builder.append("<br>");
        }

        for (ItemRequestProvision item : delivery.getProvisionItems()) {
            hasItems = true;
            builder.append("- ").append(escape(item.getProvision().getName()));
            builder.append("<br>");
        }

        if (!hasItems) {
            builder.append("- Nenhum item vinculado a entrega.<br>");
        }
    }

    private String formatReceivers(Delivery delivery) {
        if (delivery.getReceivers() == null || delivery.getReceivers().isEmpty()) {
            return "Nao informado";
        }

        return delivery.getReceivers()
                .stream()
                .map(DeliveryReceiver::getUser)
                .map(user -> escape(user.getName()))
                .distinct()
                .reduce((first, second) -> first + ", " + second)
                .orElse("Nao informado");
    }

    private String resolveWithdrawalType(String deliveryLocation) {
        String normalized = normalize(deliveryLocation);
        if (normalized.contains("fornecedor")) {
            return "Retirada no fornecedor";
        }

        if (normalized.contains("senai")) {
            return "Retirada no SENAI";
        }

        return "Nao informado";
    }

    private String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "Nao informado";
        }
        return dateTime.format(DATE_TIME_FORMAT);
    }

    private String formatQuantity(Double quantity) {
        if (quantity == null) {
            return "Quantidade nao informada";
        }

        if (quantity % 1 == 0) {
            return String.valueOf(quantity.longValue());
        }

        return BigDecimal.valueOf(quantity)
                .stripTrailingZeros()
                .toPlainString()
                .replace(".", ",");
    }

    private String formatText(String text) {
        if (!hasText(text)) {
            return "Nao informado";
        }
        return escape(text);
    }

    private boolean hasText(String text) {
        return text != null && !text.isBlank();
    }

    private String normalize(String text) {
        if (text == null) {
            return "";
        }

        return Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .trim()
                .toLowerCase();
    }

    private String escape(String text) {
        if (text == null) {
            return "";
        }

        return text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
