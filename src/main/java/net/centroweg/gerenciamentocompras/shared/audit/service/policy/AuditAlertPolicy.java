package net.centroweg.gerenciamentocompras.shared.audit.service.policy;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

/**
 * Centraliza a mesma regra usada pelo painel administrativo para identificar alertas.
 */
@Component
public class AuditAlertPolicy {

    private static final List<String> ALERT_MARKERS = List.of(
            "DESATI",
            "EXCLU",
            "REMOV",
            "ATUALI",
            "STATUS_ATIVACAO"
    );

    public boolean isAlert(String action) {
        if (action == null || action.isBlank()) {
            return false;
        }

        String normalizedAction = action.toUpperCase(Locale.ROOT);
        return ALERT_MARKERS.stream().anyMatch(normalizedAction::contains);
    }
}
