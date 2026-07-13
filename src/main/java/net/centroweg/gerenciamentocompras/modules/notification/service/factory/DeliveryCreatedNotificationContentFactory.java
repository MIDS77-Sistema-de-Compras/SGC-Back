package net.centroweg.gerenciamentocompras.modules.notification.service.factory;

import net.centroweg.gerenciamentocompras.modules.delivery.service.api.dto.DeliveryCreatedNotificationData;
import net.centroweg.gerenciamentocompras.shared.email.components.EmailFooter;
import net.centroweg.gerenciamentocompras.shared.email.components.EmailLayout;
import net.centroweg.gerenciamentocompras.shared.email.components.EmailParagraph;
import net.centroweg.gerenciamentocompras.shared.email.components.EmailTitle;
import net.centroweg.gerenciamentocompras.shared.email.intrf.EmailBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Component
public class DeliveryCreatedNotificationContentFactory {

    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy 'as' HH:mm");

    public DeliveryCreatedNotificationContent build(DeliveryCreatedNotificationData delivery) {
        String subject = "Voce foi definido como recebedor - Solicitacao #" + delivery.requestId();
        String internal = buildInternalMessage(delivery);
        String html = buildEmailMessage(delivery);
        EmailLayout layout = new EmailLayout(
                subject,
                List.<EmailBuilder>of(
                        new EmailTitle(subject),
                        new EmailParagraph(html, "#333333", 14),
                        new EmailFooter()
                )
        );
        return new DeliveryCreatedNotificationContent(subject, internal, subject, layout.buildHtml());
    }

    private String buildInternalMessage(DeliveryCreatedNotificationData delivery) {
        StringBuilder message = new StringBuilder()
                .append("Entrega #").append(delivery.deliveryId())
                .append(" da solicitacao #").append(delivery.requestId()).append('\n')
                .append("Data prevista: ").append(formatDate(delivery.expectedDeliveryAt())).append('\n')
                .append("Local de entrega/retirada: ").append(safeText(delivery.deliveryLocation())).append('\n')
                .append("Tipo de retirada: ").append(resolveWithdrawalType(delivery.deliveryLocation())).append('\n')
                .append("Status da entrega: ").append(safeText(delivery.statusName())).append('\n');
        if (delivery.deliveredAt() != null) {
            message.append("Data efetiva: ").append(formatDate(delivery.deliveredAt())).append('\n');
        }
        message.append("Itens da entrega:\n");
        appendPlainItems(message, delivery);
        if (hasText(delivery.description())) {
            message.append("Observacoes: ").append(safeText(delivery.description())).append('\n');
        }
        return message.toString().stripTrailing();
    }

    private String buildEmailMessage(DeliveryCreatedNotificationData delivery) {
        StringBuilder html = new StringBuilder()
                .append("<b>Solicitacao:</b> #").append(delivery.requestId()).append("<br>")
                .append("<b>Data prevista:</b> ").append(formatDate(delivery.expectedDeliveryAt())).append("<br>")
                .append("<b>Local de entrega/retirada:</b> ").append(escape(delivery.deliveryLocation())).append("<br>")
                .append("<b>Tipo de retirada:</b> ").append(resolveWithdrawalType(delivery.deliveryLocation())).append("<br>")
                .append("<b>Status da entrega:</b> ").append(escape(delivery.statusName())).append("<br>");
        if (delivery.deliveredAt() != null) {
            html.append("<b>Data efetiva:</b> ").append(formatDate(delivery.deliveredAt())).append("<br>");
        }
        html.append("<br><b>Itens da entrega:</b><br>");
        appendHtmlItems(html, delivery);
        if (hasText(delivery.description())) {
            html.append("<br><b>Observacoes:</b> ").append(escape(delivery.description())).append("<br>");
        }
        return html.toString();
    }

    private void appendPlainItems(StringBuilder message, DeliveryCreatedNotificationData delivery) {
        delivery.productItems().forEach(item -> message.append("- ").append(safeText(item.name()))
                .append(hasText(item.code()) ? " (" + safeText(item.code()) + ")" : "")
                .append(" - ").append(formatQuantity(item.quantity()))
                .append(hasText(item.measurementUnit()) ? " " + safeText(item.measurementUnit()) : "")
                .append('\n'));
        delivery.provisionItemNames().forEach(item -> message.append("- ").append(safeText(item)).append('\n'));
        if (delivery.productItems().isEmpty() && delivery.provisionItemNames().isEmpty()) {
            message.append("- Nenhum item vinculado a entrega.\n");
        }
    }

    private void appendHtmlItems(StringBuilder html, DeliveryCreatedNotificationData delivery) {
        delivery.productItems().forEach(item -> html.append("- ").append(escape(item.name()))
                .append(hasText(item.code()) ? " (" + escape(item.code()) + ")" : "")
                .append(" - ").append(formatQuantity(item.quantity()))
                .append(hasText(item.measurementUnit()) ? " " + escape(item.measurementUnit()) : "")
                .append("<br>"));
        delivery.provisionItemNames().forEach(item -> html.append("- ").append(escape(item)).append("<br>"));
        if (delivery.productItems().isEmpty() && delivery.provisionItemNames().isEmpty()) {
            html.append("- Nenhum item vinculado a entrega.<br>");
        }
    }

    private String resolveWithdrawalType(String location) {
        String normalized = normalize(location);
        if (normalized.contains("fornecedor")) return "Retirada no fornecedor";
        if (normalized.contains("senai")) return "Retirada no SENAI";
        return "Nao informado";
    }

    private String normalize(String value) {
        if (value == null) return "";
        return Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "").trim().toLowerCase(Locale.ROOT);
    }

    private String formatQuantity(Double quantity) {
        if (quantity == null) return "Quantidade nao informada";
        if (quantity % 1 == 0) return String.valueOf(quantity.longValue());
        return BigDecimal.valueOf(quantity).stripTrailingZeros().toPlainString().replace(".", ",");
    }

    private String formatDate(java.time.LocalDateTime value) {
        return value == null ? "Nao informado" : value.format(FORMAT);
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private String text(String value) {
        return hasText(value) ? value.trim() : "Nao informado";
    }

    private String safeText(String value) {
        return HtmlUtils.htmlEscape(text(value));
    }

    private String escape(String value) {
        return HtmlUtils.htmlEscape(text(value));
    }
}
