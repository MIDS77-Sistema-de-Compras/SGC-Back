package net.centroweg.gerenciamentocompras.modules.notification.service.factory;

import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class ItemStatusContentFormatter {

    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy 'as' HH:mm");
    private static final String DELIVERED_STATUS = "Entregue";

    public boolean isDelivered(String statusName) {
        return normalize(statusName).equals(normalize(DELIVERED_STATUS));
    }

    public String formatDateTime(LocalDateTime dateTime) {
        return dateTime == null ? "Nao informado" : dateTime.format(DATE_TIME_FORMAT);
    }

    public String formatQuantity(Double quantity) {
        if (quantity == null) {
            return "Quantidade nao informada";
        }
        if (quantity % 1 == 0) {
            return String.valueOf(quantity.longValue());
        }
        return BigDecimal.valueOf(quantity).stripTrailingZeros().toPlainString().replace(".", ",");
    }

    public String valueOrNotInformed(String value) {
        return hasText(value) ? plainText(value) : "Nao informado";
    }

    public String plainText(String value) {
        return value == null ? "" : HtmlUtils.htmlEscape(value.trim());
    }

    public String resolveWithdrawalType(String deliveryLocation) {
        String normalized = normalize(deliveryLocation);
        if (normalized.contains("fornecedor")) {
            return "Retirada no fornecedor";
        }
        if (normalized.contains("senai")) {
            return "Retirada no SENAI";
        }
        return "Nao informado";
    }

    public boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private String normalize(String value) {
        if (value == null) {
            return "";
        }
        return Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .trim()
                .toLowerCase();
    }
}
