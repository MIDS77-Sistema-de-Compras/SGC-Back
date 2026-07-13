package net.centroweg.gerenciamentocompras.modules.request.service.notification;

import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.service.event.RequestStatusChangedEvent;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.shared.email.components.EmailButton;
import net.centroweg.gerenciamentocompras.shared.email.components.EmailFooter;
import net.centroweg.gerenciamentocompras.shared.email.components.EmailLayout;
import net.centroweg.gerenciamentocompras.shared.email.components.EmailParagraph;
import net.centroweg.gerenciamentocompras.shared.email.components.EmailTitle;
import net.centroweg.gerenciamentocompras.shared.email.intrf.EmailBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Component
public class RequestStatusEmailMessageFactory {

    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");
    private static final Locale PORTUGUESE = Locale.forLanguageTag("pt-BR");
    private static final String REFUSED_STATUS = "recusado";

    private final String requesterRequestUrlTemplate;

    public RequestStatusEmailMessageFactory(
            @Value("${app.frontend.requester-request-url-template}") String requesterRequestUrlTemplate
    ) {
        this.requesterRequestUrlTemplate = requesterRequestUrlTemplate;
    }

    public String notificationTitle() {
        return "Status da solicitação atualizado";
    }

    public String buildNotificationMessage(RequestStatusChangedEvent event) {
        String message = "A solicitação #%d teve o status alterado de “%s” para “%s”."
                .formatted(
                        event.requestId(),
                        formatStatusName(event.previousStatusName()),
                        formatStatusName(event.newStatusName())
                );

        if (isRefused(event.newStatusName()) && hasText(event.justification())) {
            return message + " Justificativa: " + event.justification().trim() + ".";
        }

        return message;
    }

    public RequestStatusEmailContent build(
            RequestStatusChangedEvent event,
            Request request,
            User requester
    ) {
        String formattedNewStatus = formatStatusName(event.newStatusName());
        String subject = "Solicitação #%d atualizada: %s".formatted(event.requestId(), formattedNewStatus);
        String escapedSubject = escape(subject);

        EmailLayout layout = new EmailLayout(
                escapedSubject,
                List.<EmailBuilder>of(
                        new EmailTitle(escapedSubject),
                        new EmailParagraph("Olá, " + formatText(requester.getName()) + ".", "#666666", 14),
                        new EmailParagraph(buildChangeDetails(event), "#333333", 14),
                        new EmailParagraph(buildRequestSummary(request), "#333333", 14),
                        new EmailParagraph("Clique abaixo para acompanhar a solicitação.", "#666666", 14),
                        new EmailButton(buildRequesterUrl(event.requestId()), "Acompanhar solicitação"),
                        new EmailFooter()
                )
        );

        return new RequestStatusEmailContent(subject, layout.buildHtml());
    }

    public String formatStatusName(String statusName) {
        if (!hasText(statusName)) {
            return "Não informado";
        }

        String normalized = statusName
                .replace('_', ' ')
                .trim()
                .replaceAll("\\s+", " ")
                .toLowerCase(PORTUGUESE);

        return normalized.substring(0, 1).toUpperCase(PORTUGUESE) + normalized.substring(1);
    }

    private String buildChangeDetails(RequestStatusChangedEvent event) {
        StringBuilder builder = new StringBuilder();
        builder.append("A solicitação <b>#").append(event.requestId()).append("</b> teve seu status atualizado.<br><br>");
        builder.append("<b>Status anterior:</b> ").append(formatText(formatStatusName(event.previousStatusName()))).append("<br>");
        builder.append("<b>Novo status:</b> ").append(formatText(formatStatusName(event.newStatusName()))).append("<br>");
        builder.append("<b>Alterado por:</b> ").append(formatText(event.changedByUserName())).append("<br>");
        builder.append("<b>Data da alteração:</b> ").append(formatDateTime(event.changedAt())).append("<br>");

        if (isRefused(event.newStatusName()) && hasText(event.justification())) {
            builder.append("<b>Justificativa:</b> ").append(escape(event.justification().trim())).append("<br>");
        }

        return builder.toString();
    }

    private String buildRequestSummary(Request request) {
        String crName = request.getCrBranch() != null && request.getCrBranch().getCr() != null
                ? request.getCrBranch().getCr().getName()
                : null;
        String crCode = request.getCrBranch() != null && request.getCrBranch().getCr() != null
                ? request.getCrBranch().getCr().getCode()
                : null;
        String branchName = request.getCrBranch() != null && request.getCrBranch().getBranch() != null
                ? request.getCrBranch().getBranch().getName()
                : null;
        String statusName = request.getStatus() != null ? request.getStatus().getName() : null;

        return """
                <b style="color: #333333; font-size: 17px; line-height: 1.6;">Resumo da solicitação</b><br>
                <b>ID:</b> #%d<br>
                <b>CR:</b> %s<br>
                <b>Código do CR:</b> %s<br>
                <b>Filial:</b> %s<br>
                <b>Status atual:</b> %s<br>
                <b>Data da solicitação:</b> %s
                """.formatted(
                request.getId(),
                formatText(crName),
                formatText(crCode),
                formatText(branchName),
                formatText(formatStatusName(statusName)),
                formatDateTime(request.getRequestDate())
        );
    }

    private String buildRequesterUrl(Long requestId) {
        String url = requesterRequestUrlTemplate.replace("{requestId}", String.valueOf(requestId));
        return HtmlUtils.htmlEscape(url);
    }

    private String formatDateTime(LocalDateTime dateTime) {
        return dateTime == null ? "Não informado" : escape(dateTime.format(DATE_TIME_FORMAT));
    }

    private String formatText(String value) {
        return hasText(value) ? escape(value.trim()) : "Não informado";
    }

    private boolean isRefused(String statusName) {
        return formatStatusName(statusName).equalsIgnoreCase(REFUSED_STATUS);
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private String escape(String value) {
        return value == null ? "" : HtmlUtils.htmlEscape(value);
    }
}
