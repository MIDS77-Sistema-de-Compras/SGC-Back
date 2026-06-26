package net.centroweg.gerenciamentocompras.modules.notification.infrastructure.email;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
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

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationEmailService {

    private final EmailSenderService emailSenderService;


    @Value("http://localhost:3000/coordenador/solicitacoes")
    private String frontendUrl;

    @Async
    public void sendNotificationEmail(
            User user,
            String subject,
            String message,
            Request request
    ) {

        EmailLayout layout = new EmailLayout(
                subject,
                List.<EmailBuilder>of(
                        new EmailTitle(subject),
                        new EmailParagraph("Olá, " + user.getName() + ".", "#666666", 14),
                        new EmailParagraph(message, "#666666", 14),
                        new EmailParagraph(buildRequestSummary(request), "#333333", 14),
                        new EmailParagraph("Clique na opção abaixo para analisar a solicitação.", "#666666", 14),
                        new EmailButton(frontendUrl, "Acessar solicitação"),
                        new EmailFooter()
                )
        );

        try {
            emailSenderService.sendEmail(
                    new DefaultEmail(subject, user.getEmail()),
                    layout.buildHtml()
            );
        } catch (MessagingException exception) {
            log.error(
                    "Erro ao enviar e-mail de notificação para {}",
                    user.getEmail(),
                    exception
            );
        }
    }

    private String buildRequestSummary(Request request) {
        String requesterName = getRequesterName(request);
        String itemsSummary = buildItemsSummary(request);

        return """
            <b>Resumo da solicitação</b><br>
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
                request.getStatus().getName(),
                requesterName,
                request.getRequestDate(),
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

        builder.append("<b>Itens de produto</b><br>");

        request.getItemRequestProducts().forEach(item -> {
            builder.append("- ")
                    .append(item.getProduct().getName())
                    .append(" | Código: ")
                    .append(item.getProduct().getCode())
                    .append(" | Quantidade: ")
                    .append(item.getQuantity());

            if (item.getMeasurementUnit() != null) {
                builder.append(" ")
                        .append(item.getMeasurementUnit().getName());
            }

            if (item.getAdditionalInformations() != null && !item.getAdditionalInformations().isBlank()) {
                builder.append(" | Info adicional: ")
                        .append(item.getAdditionalInformations());
            }

            builder.append("<br>");
        });

        return builder.toString();
    }

    private String buildProvisionItemsSummary(Request request) {
        StringBuilder builder = new StringBuilder();

        builder.append("<b>Itens de serviço</b><br>");

        request.getItemRequestProvisions().forEach(item -> {
            builder.append("- ")
                    .append(item.getProvision().getName())
                    .append(" | Valor total: R$ ")
                    .append(item.getProvision().getTotalValue());

            if (item.getProvision().getDescription() != null && !item.getProvision().getDescription().isBlank()) {
                builder.append(" | Descrição: ")
                        .append(item.getProvision().getDescription());
            }

            if (item.getAdditionalInformation() != null && !item.getAdditionalInformation().isBlank()) {
                builder.append(" | Info adicional: ")
                        .append(item.getAdditionalInformation());
            }

            builder.append("<br>");
        });

        return builder.toString();
    }

    private String getRequesterName(Request request) {
        if (request.getCreatedByUsers() == null || request.getCreatedByUsers().isEmpty()) {
            return "Não informado";
        }

        return request.getCreatedByUsers().get(0).getName();
    }
}