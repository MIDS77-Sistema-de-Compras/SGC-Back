package net.centroweg.gerenciamentocompras.modules.notification.infrastructure.listener;

import net.centroweg.gerenciamentocompras.modules.notification.domain.enums.NotificationType;
import net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceIntrf.CreateInternalNotificationUseCase;
import net.centroweg.gerenciamentocompras.modules.user.service.api.UserPublicApi;
import net.centroweg.gerenciamentocompras.shared.audit.service.event.AuditAlertCreatedEvent;
import net.centroweg.gerenciamentocompras.shared.security.authority.Authorities;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminAuditAlertNotificationListenerTest {

    @Mock
    private UserPublicApi userPublicApi;

    @Mock
    private CreateInternalNotificationUseCase createInternalNotificationUseCase;

    @InjectMocks
    private AdminAuditAlertNotificationListener listener;

    @Test
    void shouldNotifyEveryActiveAdministrator() {
        when(userPublicApi.findActiveUserIdsByRole(Authorities.ADMIN)).thenReturn(List.of(10L, 20L));

        listener.onAuditAlertCreated(new AuditAlertCreatedEvent(
                1L,
                "DESATIVAR_USUARIO",
                "Gestor",
                null
        ));

        verify(createInternalNotificationUseCase).createNotifications(
                eq("Alerta administrativo"),
                contains("Gestor"),
                eq(NotificationType.ALERTA_ADMINISTRATIVO),
                eq(null),
                eq(List.of(10L, 20L))
        );
    }

    @Test
    void shouldDoNothingWhenThereIsNoActiveAdministrator() {
        when(userPublicApi.findActiveUserIdsByRole(Authorities.ADMIN)).thenReturn(List.of());

        listener.onAuditAlertCreated(new AuditAlertCreatedEvent(
                1L,
                "EXCLUIR_CR",
                "Gestor",
                null
        ));

        verify(createInternalNotificationUseCase, never()).createNotifications(
                anyString(),
                anyString(),
                any(),
                any(),
                any()
        );
    }
}
