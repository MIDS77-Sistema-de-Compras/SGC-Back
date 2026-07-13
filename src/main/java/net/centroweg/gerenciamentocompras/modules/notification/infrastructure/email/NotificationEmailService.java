package net.centroweg.gerenciamentocompras.modules.notification.infrastructure.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.centroweg.gerenciamentocompras.modules.request.service.api.RequestPublicApi;
import net.centroweg.gerenciamentocompras.modules.request.service.api.dto.RequestEmailNotificationData;
import net.centroweg.gerenciamentocompras.shared.email.components.EmailButton;
import net.centroweg.gerenciamentocompras.shared.email.components.EmailFooter;
import net.centroweg.gerenciamentocompras.shared.email.components.EmailLayout;
import net.centroweg.gerenciamentocompras.shared.email.components.EmailParagraph;
import net.centroweg.gerenciamentocompras.shared.email.components.EmailTitle;
import net.centroweg.gerenciamentocompras.shared.email.intrf.EmailBuilder;
import net.centroweg.gerenciamentocompras.shared.email.model.DefaultEmail;
import net.centroweg.gerenciamentocompras.shared.email.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationEmailService {

    private final EmailSenderService emailSenderService;
    private final RequestPublicApi requestPublicApi;

    @Value("${app.frontend.coordinator-requests-url}")
    private String frontendUrl;

    @Async
    public void sendNotificationEmail(String userName, String userEmail, String subject, String message, Long requestId) {
        try {
            RequestEmailNotificationData request = requestPublicApi.findEmailNotificationDataById(requestId);
            EmailLayout layout = new EmailLayout(
                    escape(subject),
                    List.<EmailBuilder>of(
                            new EmailTitle(escape(subject)),
                            new EmailParagraph("Ol\u00E1, " + escape(userName) + ".", "#666666", 14),
                            new EmailParagraph(escape(message), "#666666", 14),
                            new EmailParagraph(buildRequestSummary(request), "#333333", 14),
                            new EmailParagraph("Clique na op\u00E7\u00E3o abaixo para analisar a solicita\u00E7\u00E3o.", "#666666", 14),
                            new EmailButton(HtmlUtils.htmlEscape(frontendUrl), "Acessar solicita\u00E7\u00E3o"),
                            new EmailFooter()
                    )
            );
            emailSenderService.sendEmail(new DefaultEmail(subject, userEmail), layout.buildHtml());
        } catch (Exception exception) {
            log.error("Erro inesperado ao enviar e-mail de notificacao. requestId={}, email={}", requestId, userEmail, exception);
        }
    }

    private String buildRequestSummary(RequestEmailNotificationData request) {
        return """
                <b style="color: #333333; font-size: 17px; line-height: 1.6;">Resumo da solicitacao</b><br>
                <b>ID:</b> #%d<br>
                <b>CR:</b> %s<br>
                <b>Codigo do CR:</b> %s<br>
                <b>Filial:</b> %s<br>
                <b>Status:</b> %s<br>
                <b>Solicitante:</b> %s<br>
                <b>Data:</b> %s<br><br>
                %s
                """.formatted(
                request.requestId(), escape(request.crName()), escape(request.crCode()), escape(request.branchName()),
                escape(formatStatusName(request.statusName())), escape(request.requesterName()),
                formatRequestDate(request.requestDate()), buildItemsSummary(request)
        );
    }

    private String buildItemsSummary(RequestEmailNotificationData request) {
        if (!request.productItems().isEmpty()) {
            StringBuilder builder = new StringBuilder("<b style='color: #333333; font-size: 17px;'>Itens de produto</b>");
            request.productItems().forEach(item -> {
                builder.append("<br><b>- Nome: </b>").append(escape(item.name()))
                        .append("<br><b>- Codigo: </b>").append(escape(item.code()))
                        .append("<br><b>- Quantidade: </b>").append(formatQuantity(item.quantity()));
                if (hasText(item.measurementUnit())) {
                    builder.append(' ').append(escape(item.measurementUnit()));
                }
                if (hasText(item.additionalInformation())) {
                    builder.append("<br><b>- Info adicional: </b>").append(escape(item.additionalInformation()));
                }
                builder.append("<br>");
            });
            return builder.toString();
        }
        if (!request.provisionItems().isEmpty()) {
            StringBuilder builder = new StringBuilder("<b style='color: #333333; font-size: 17px;'>Itens de servico</b><br>");
            request.provisionItems().forEach(item -> {
                builder.append("<br><b>- Nome: </b>").append(escape(item.name()))
                        .append("<br><b>- Valor total:</b> R$ ").append(item.totalValue());
                if (hasText(item.description())) {
                    builder.append("<br><b>- Descricao: </b>").append(escape(item.description()));
                }
                if (hasText(item.additionalInformation())) {
                    builder.append("<br><b>- Info adicional: </b>").append(escape(item.additionalInformation()));
                }
                builder.append("<br>");
            });
            return builder.toString();
        }
        return "<b>Itens:</b><br>Nenhum item informado.";
    }

    private String formatRequestDate(LocalDateTime date) {
        return date == null ? "Nao informado" : date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy 'as' HH:mm"));
    }

    private String formatStatusName(String statusName) {
        if (!hasText(statusName)) {
            return "Nao informado";
        }
        String formatted = statusName.replace("_", " ").toLowerCase();
        return formatted.substring(0, 1).toUpperCase() + formatted.substring(1);
    }

    private String formatQuantity(Double quantity) {
        if (quantity == null) {
            return "Nao informado";
        }
        if (quantity % 1 == 0) {
            return String.valueOf(quantity.longValue());
        }
        return BigDecimal.valueOf(quantity).stripTrailingZeros().toPlainString().replace(".", ",");
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private String escape(String value) {
        return hasText(value) ? HtmlUtils.htmlEscape(value.trim()) : "Nao informado";
    }
}
