package net.centroweg.gerenciamentocompras.modules.notification.infrastructure.url;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RequestFrontendUrlBuilder {

    private static final String REQUEST_ID_PLACEHOLDER = "{requestId}";

    private final String requesterRequestUrlTemplate;
    private final String managementRequestUrlTemplate;

    public RequestFrontendUrlBuilder(
            @Value("${app.frontend.requester-request-url-template}") String requesterRequestUrlTemplate,
            @Value("${app.frontend.coordinator-request-url-template}") String managementRequestUrlTemplate
    ) {
        this.requesterRequestUrlTemplate = validateTemplate(
                "app.frontend.requester-request-url-template", requesterRequestUrlTemplate
        );
        this.managementRequestUrlTemplate = validateTemplate(
                "app.frontend.coordinator-request-url-template", managementRequestUrlTemplate
        );
    }

    public String buildRequesterRequestUrl(Long requestId) {
        return buildUrl(requesterRequestUrlTemplate, requestId);
    }

    public String buildManagementRequestUrl(Long requestId) {
        return buildUrl(managementRequestUrlTemplate, requestId);
    }

    private String validateTemplate(String propertyName, String template) {
        if (template == null || template.isBlank() || !template.contains(REQUEST_ID_PLACEHOLDER)) {
            throw new IllegalArgumentException(
                    "A propriedade '%s' deve conter o marcador '%s' e nao pode ser vazia."
                            .formatted(propertyName, REQUEST_ID_PLACEHOLDER)
            );
        }
        return template.trim();
    }

    private String buildUrl(String template, Long requestId) {
        if (requestId == null) {
            throw new IllegalArgumentException("requestId nao pode ser nulo.");
        }
        return template.replace(REQUEST_ID_PLACEHOLDER, requestId.toString());
    }
}
