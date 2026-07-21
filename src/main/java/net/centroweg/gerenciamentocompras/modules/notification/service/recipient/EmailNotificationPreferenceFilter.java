package net.centroweg.gerenciamentocompras.modules.notification.service.recipient;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.service.api.UserPublicApi;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * Filtra destinatários de e-mail de acordo com a preferência
 * emailNotificationsEnabled de cada usuário.
 *
 * Aplica-se apenas a e-mails de NOTIFICAÇÃO — e-mails transacionais
 * (ex.: recuperação de senha) não passam por este filtro.
 */
@Component
@RequiredArgsConstructor
public class EmailNotificationPreferenceFilter {

    private final UserPublicApi userPublicApi;

    /**
     * Retorna apenas os destinatários cujo usuário aceita receber e-mail.
     * Destinatários sem userId são mantidos (não há como consultar a preferência).
     */
    public <T> List<T> filterEnabled(Collection<T> recipients, Function<T, Long> userIdExtractor) {
        if (recipients == null || recipients.isEmpty()) return List.of();

        List<Long> userIds = recipients.stream()
                .map(userIdExtractor)
                .filter(java.util.Objects::nonNull)
                .toList();

        Set<Long> disabled = userPublicApi.findUserIdsWithEmailNotificationsDisabled(userIds);

        return recipients.stream()
                .filter(recipient -> {
                    Long userId = userIdExtractor.apply(recipient);
                    return userId == null || !disabled.contains(userId);
                })
                .toList();
    }

    /** Consulta a preferência de um único usuário. */
    public boolean isEnabled(Long userId) {
        if (userId == null) return true;
        return userPublicApi.findUserIdsWithEmailNotificationsDisabled(List.of(userId)).isEmpty();
    }

}