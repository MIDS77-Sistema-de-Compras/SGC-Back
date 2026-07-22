package net.centroweg.gerenciamentocompras.modules.notification.service.factory;

import net.centroweg.gerenciamentocompras.modules.request.service.event.RequestStatusChangedEvent;
import net.centroweg.gerenciamentocompras.modules.request.service.util.RequestStatusNames;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

@Component
public class RequestStatusInternalNotificationFactory {

    public RequestStatusInternalNotificationContent build(RequestStatusChangedEvent event) {
        String message = "A solicitacao #%d teve o status alterado de \"%s\" para \"%s\"."
                .formatted(event.requestId(), value(event.previousStatusName()), value(event.newStatusName()));
        if (isRefused(event.newStatusName()) && hasText(event.justification())) {
            message += " Justificativa: " + HtmlUtils.htmlEscape(event.justification().trim()) + ".";
        }
        return new RequestStatusInternalNotificationContent("Status da solicita\u00E7\u00E3o atualizado", message);
    }

    private boolean isRefused(String value) {
        return hasText(value) && value.trim().equalsIgnoreCase("Recusado");
    }

    private String value(String value) {
        return HtmlUtils.htmlEscape(RequestStatusNames.toDisplayName(value));
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
