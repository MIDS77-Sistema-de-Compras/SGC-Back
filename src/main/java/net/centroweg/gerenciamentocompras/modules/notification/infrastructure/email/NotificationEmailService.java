package net.centroweg.gerenciamentocompras.modules.notification.infrastructure.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
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
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationEmailService {

    private final EmailSenderService emailSenderService;
    private final RequestRepository requestRepository;


    @Value("http://localhost:3000/coordenador/solicitacoes")
    private String frontendUrl;

    @Async
    @Transactional(readOnly = true)
    public void sendNotificationEmail(
            String userName,
            String userEmail,
            String subject,
            String message,
            Long requestId
    ) {
        try {
            log.info("NotificationEmailService - Iniciando envio para {}", userEmail);

            Request request = requestRepository.findById(requestId)
                    .orElseThrow(RequestNotFoundException::new);

            EmailLayout layout = new EmailLayout(
                    subject,
                    List.<EmailBuilder>of(
                            new EmailTitle(subject),
                            new EmailParagraph("Ol\u00E1, " + userName + ".", "#666666", 14),
                            new EmailParagraph(message, "#666666", 14),
                            new EmailParagraph(buildRequestSummary(request), "#333333", 14),
                            new EmailParagraph("Clique na op\u00E7\u00E3o abaixo para analisar a solicita\u00E7\u00E3o.", "#666666", 14),
                            new EmailButton(frontendUrl, "Acessar solicita\u00E7\u00E3o"),
                            new EmailFooter()
                    )
            );

            emailSenderService.sendEmail(
                    new DefaultEmail(subject, userEmail),
                    layout.buildHtml()
            );

            log.info("NotificationEmailService - E-mail enviado com sucesso para {}", userEmail);
        } catch (Exception exception) {
            log.error("Erro inesperado ao enviar e-mail de notifica\u00E7\u00E3o para {}", userEmail, exception);
        }
    }

    @Async
    @Transactional(readOnly = true)
    public void sendPendingReminderEmail(String supervisorName, String supervisorEmail, Long requestId) {
        try {
            Request request = requestRepository.findById(requestId)
                    .orElseThrow(RequestNotFoundException::new);

            String subject = "Solicitação pendente há mais de uma semana";

            EmailLayout layout = new EmailLayout(
                    subject,
                    List.<EmailBuilder>of(
                            new EmailTitle(subject),
                            new EmailParagraph("Olá, " + supervisorName + ".", "#666666", 14),
                            new EmailParagraph(
                                    "A solicitação abaixo está pendente de análise há mais de 7 dias. "
                                            + "Por favor, avalie-a o quanto antes.", "#666666", 14),
                            new EmailParagraph(buildRequestSummary(request), "#333333", 14),
                            new EmailButton(frontendUrl, "Analisar solicitação"),
                            new EmailFooter()
                    )
            );

            emailSenderService.sendEmail(new DefaultEmail(subject, supervisorEmail), layout.buildHtml());
            log.info("E-mail de solicitação pendente enviado para {}", supervisorEmail);
        } catch (Exception exception) {
            log.error("Erro ao enviar e-mail de solicitação pendente para {}", supervisorEmail, exception);
        }
    }


    private String buildRequestSummary(Request request) {
        String requesterName = getRequesterName(request);
        String itemsSummary = buildItemsSummary(request);

        return """
            <b style="color: #333333; font-size: 17px; line-height: 1.6;" >Resumo da solicitação</b><br>
            <b>ID:</b> #%d<br>
            <b>CR:</b> %s<br>
            <b>Código do CR:</b> %s<br>
            <b>Filial:</b> %s<br>
            <b>Status:</b> %s<br>
            <b>Solicitante:</b> %s<br>
            <b>Data:</b> %s<br><br>
            %s
            """.formatted(
                request.getId(),
                request.getCrBranch().getCr().getName(),
                request.getCrBranch().getCr().getCode(),
                request.getCrBranch().getBranch().getName(),
                formatStatusName(request.getStatus().getName()),
                requesterName,
                formatRequestDate(request.getRequestDate()),
                itemsSummary
        );
    }

    private String buildItemsSummary(Request request) {
        if (request.getItemRequestProducts() != null && !request.getItemRequestProducts().isEmpty()) {
            return buildProductItemsSummary(request);
        }

        if (request.getItemRequestProvisions() != null && !request.getItemRequestProvisions().isEmpty()) {
            return buildProvisionItemsSummary(request);
        }

        return "<b>Itens:</b><br>Nenhum item informado.";
    }

    private String buildProductItemsSummary(Request request) {
        StringBuilder builder = new StringBuilder();

        builder.append("<b style='color: #333333; font-size: 17px; line-height: 1.6;' >Itens de produto</b>");

        request.getItemRequestProducts().forEach(item -> {
            builder.append("<br><b>- ")
                    .append(" Name: </b>")
                    .append(item.getProduct().getName())
                    .append("<br>")
                    .append("<b>- Código: </b>")
                    .append(item.getProduct().getCode())
                    .append("<br>")
                    .append("<b>- Quantidade: </b>")
                    .append(formatQuantity(item.getQuantity()));

            if (item.getMeasurementUnit() != null) {
                builder.append(" ")
                        .append(item.getMeasurementUnit().getName());
            }

            if (item.getAdditionalInformations() != null && !item.getAdditionalInformations().isBlank()) {
                builder.append("<br><b>- Info adicional: </b>")
                        .append(item.getAdditionalInformations());
            }

            builder.append("<br>");
        });

        return builder.toString();
    }

    private String buildProvisionItemsSummary(Request request) {
        StringBuilder builder = new StringBuilder();

        builder.append("<b style='color: #333333; font-size: 17px; line-height: 1.6;'>Itens de serviço</b><br>");

        request.getItemRequestProvisions().forEach(item -> {
            builder.append("<br><b> - ")
                    .append(" Nome: </b>")
                    .append(item.getProvision().getName())
                    .append("<br><b> - Valor total:</b> R$ ")
                    .append(item.getProvision().getTotalValue());

            if (item.getProvision().getDescription() != null && !item.getProvision().getDescription().isBlank()) {
                builder.append("<br><b> - Descrição: </b>")
                        .append(item.getProvision().getDescription());
            }

            if (item.getAdditionalInformation() != null && !item.getAdditionalInformation().isBlank()) {
                builder.append("<br><b> - Info adicional: </b>")
                        .append(item.getAdditionalInformation());
            }

            builder.append("<br>");
        });

        return builder.toString();
    }

    private String formatRequestDate(LocalDateTime date) {
        if (date == null) {
            return "N\u00E3o informado";
        }

        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy '\u00E0s' HH:mm"));
    }

    private String formatStatusName(String statusName) {
        if (statusName == null || statusName.isBlank()) {
            return "N\u00E3o informado";
        }

        String formatted = statusName.replace("_", " ").toLowerCase();

        return formatted.substring(0, 1).toUpperCase() + formatted.substring(1);
    }

    private String formatQuantity(Double quantity) {
        if (quantity == null) {
            return "N\u00E3o informado";
        }

        if (quantity % 1 == 0) {
            return String.valueOf(quantity.longValue());
        }

        return BigDecimal.valueOf(quantity)
                .stripTrailingZeros()
                .toPlainString()
                .replace(".", ",");
    }

    private String getRequesterName(Request request) {
        if (request.getCreatedByUsers() == null || request.getCreatedByUsers().isEmpty()) {
            return "Não informado";
        }

        return request.getCreatedByUsers().get(0).getName();
    }
}