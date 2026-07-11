package net.centroweg.gerenciamentocompras.modules.delivery.service.notification;

import net.centroweg.gerenciamentocompras.modules.delivery.domain.entity.Delivery;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProduct;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProvision;
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

@Component
public class DeliveryCreatedEmailMessageFactory {

    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy 'as' HH:mm");

    public DeliveryCreatedNotificationContent build(Delivery delivery) {
        String subject = "Voce foi definido como recebedor - Solicitacao #" + delivery.getRequest().getId();
        String message = buildMessage(delivery);

        EmailLayout layout = new EmailLayout(
                subject,
                List.<EmailBuilder>of(
                        new EmailTitle(subject),
                        new EmailParagraph(message, "#333333", 14),
                        new EmailFooter()
                )
        );

        return new DeliveryCreatedNotificationContent(subject, message, subject, layout.buildHtml());
    }

    private String buildMessage(Delivery delivery) {
        StringBuilder builder = new StringBuilder();

        builder.append("<b>Solicitacao:</b> #").append(delivery.getRequest().getId()).append("<br>");
        builder.append("<b>Data prevista:</b> ").append(formatDateTime(delivery.getExpectedDeliveryAt())).append("<br>");
        builder.append("<b>Local de entrega/retirada:</b> ").append(formatText(delivery.getDeliveryLocation())).append("<br>");
        builder.append("<b>Tipo de retirada:</b> ").append(resolveWithdrawalType(delivery.getDeliveryLocation())).append("<br>");
        builder.append("<b>Status da entrega:</b> ").append(formatText(delivery.getStatus().getName())).append("<br>");

        if (delivery.getDeliveredAt() != null) {
            builder.append("<b>Data efetiva:</b> ").append(formatDateTime(delivery.getDeliveredAt())).append("<br>");
        }

        builder.append("<br><b>Itens da entrega:</b><br>");
        appendItems(builder, delivery);

        if (hasText(delivery.getDescription())) {
            builder.append("<br><b>Observacoes:</b> ").append(escape(delivery.getDescription())).append("<br>");
        }

        return builder.toString();
    }

    private void appendItems(StringBuilder builder, Delivery delivery) {
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
