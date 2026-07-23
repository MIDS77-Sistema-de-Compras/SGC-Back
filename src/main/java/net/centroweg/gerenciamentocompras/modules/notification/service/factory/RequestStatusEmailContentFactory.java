package net.centroweg.gerenciamentocompras.modules.notification.service.factory;

import net.centroweg.gerenciamentocompras.modules.notification.infrastructure.url.RequestFrontendUrlBuilder;
import net.centroweg.gerenciamentocompras.modules.request.service.api.dto.RequestStatusNotificationData;
import net.centroweg.gerenciamentocompras.modules.request.service.event.RequestStatusChangedEvent;
import net.centroweg.gerenciamentocompras.modules.request.service.util.RequestStatusNames;
import net.centroweg.gerenciamentocompras.shared.email.components.EmailButton;
import net.centroweg.gerenciamentocompras.shared.email.components.EmailFooter;
import net.centroweg.gerenciamentocompras.shared.email.components.EmailLayout;
import net.centroweg.gerenciamentocompras.shared.email.components.EmailParagraph;
import net.centroweg.gerenciamentocompras.shared.email.components.EmailTitle;
import net.centroweg.gerenciamentocompras.shared.email.intrf.EmailBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class RequestStatusEmailContentFactory {

    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy 'as' HH:mm");
    private final RequestFrontendUrlBuilder requestFrontendUrlBuilder;

    public RequestStatusEmailContentFactory(RequestFrontendUrlBuilder requestFrontendUrlBuilder) {
        this.requestFrontendUrlBuilder = requestFrontendUrlBuilder;
    }

    public RequestStatusEmailContent build(
            RequestStatusChangedEvent event,
            RequestStatusNotificationData request,
            String recipientName
    ) {
        String subject = "Solicita\u00E7\u00E3o #%d atualizada: %s".formatted(event.requestId(), formatStatus(event.newStatusName()));
        EmailLayout layout = new EmailLayout(
                escape(subject),
                List.<EmailBuilder>of(
                        new EmailTitle(escape(subject)),
                        new EmailParagraph("Ola, " + text(recipientName) + ".", "#666666", 14),
                        new EmailParagraph(changeDetails(event), "#333333", 14),
                        new EmailParagraph(requestSummary(request), "#333333", 14),
                        new EmailParagraph("Clique abaixo para acompanhar a solicitacao.", "#666666", 14),
                        new EmailButton(HtmlUtils.htmlEscape(
                                requestFrontendUrlBuilder.buildRequesterRequestUrl(event.requestId())
                        ), "Acompanhar solicitacao"),
                        new EmailFooter()
                )
        );
        return new RequestStatusEmailContent(subject, layout.buildHtml());
    }

    private String changeDetails(RequestStatusChangedEvent event) {
        StringBuilder builder = new StringBuilder();
        builder.append("A solicitacao <b>#").append(event.requestId()).append("</b> teve seu status atualizado.<br><br>")
                .append("<b>Status anterior:</b> ").append(text(formatStatus(event.previousStatusName()))).append("<br>")
                .append("<b>Novo status:</b> ").append(text(formatStatus(event.newStatusName()))).append("<br>")
                .append("<b>Alterado por:</b> ").append(text(event.changedByUserName())).append("<br>")
                .append("<b>Data da alteracao:</b> ").append(formatDate(event.changedAt())).append("<br>");
        if (isRefused(event.newStatusName()) && hasText(event.justification())) {
            builder.append("<b>Justificativa:</b> ").append(escape(event.justification().trim())).append("<br>");
        }
        return builder.toString();
    }

    private String requestSummary(RequestStatusNotificationData request) {
        return """
                <b style="color: #333333; font-size: 17px; line-height: 1.6;">Resumo da solicitacao</b><br>
                <b>ID:</b> #%d<br>
                <b>CR:</b> %s<br>
                <b>Codigo do CR:</b> %s<br>
                <b>Filial:</b> %s<br>
                <b>Status atual:</b> %s<br>
                <b>Data da solicitacao:</b> %s
                """.formatted(request.requestId(), text(request.crName()), text(request.crCode()),
                text(request.branchName()), text(formatStatus(request.currentStatusName())), formatDate(request.requestDate()));
    }

    private String formatStatus(String value) {
        return RequestStatusNames.toDisplayName(value);
    }

    private String formatDate(LocalDateTime value) {
        return value == null ? "Nao informado" : escape(value.format(DATE_TIME_FORMAT));
    }

    private boolean isRefused(String value) {
        return hasText(value) && value.trim().equalsIgnoreCase("Recusado");
    }

    private String text(String value) {
        return hasText(value) ? escape(value.trim()) : "Nao informado";
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private String escape(String value) {
        return value == null ? "" : HtmlUtils.htmlEscape(value);
    }
}
