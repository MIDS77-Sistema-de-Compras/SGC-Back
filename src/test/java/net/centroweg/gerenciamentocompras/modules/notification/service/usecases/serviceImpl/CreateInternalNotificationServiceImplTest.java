package net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.centroweg.gerenciamentocompras.modules.notification.domain.entity.Notification;
import net.centroweg.gerenciamentocompras.modules.notification.domain.enums.NotificationType;
import net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.request.NotificationRequest;
import net.centroweg.gerenciamentocompras.modules.notification.service.mapper.NotificationMapper;
import net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceIntrf.CreateInternalNotificationUseCase;
import net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceImpl.CreateInternalNotificationServiceImpl;
import net.centroweg.gerenciamentocompras.modules.notification.infrastructure.persistence.NotificationRepository;
import net.centroweg.gerenciamentocompras.modules.user.service.api.UserPublicApi;
import net.centroweg.gerenciamentocompras.modules.request.service.api.RequestPublicApi;
import net.centroweg.gerenciamentocompras.modules.user.service.api.dto.UserNotificationData;
import net.centroweg.gerenciamentocompras.modules.request.service.api.dto.RequestNotificationData;

@ExtendWith(MockitoExtension.class)
class CreateInternalNotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserPublicApi userPublicApi;

    @Mock
    private RequestPublicApi requestPublicApi;

    @Mock
    private NotificationMapper notificationMapper;

    @InjectMocks
    private CreateInternalNotificationServiceImpl createInternalNotificationService;

    @Test
    @DisplayName("Should persist notifications with correct ITEM_PARA_RETIRADA type")
    void shouldPersistNotificationsWithCorrectItemParaRetiradaType() {
        // Given
        String title = "Item disponível para retirada";
        String message = "Seu item está disponível para retirada na loja";
        NotificationType notificationType = NotificationType.ITEM_PARA_RETIRADA;
        Long requestId = 10L;
        Long userId = 456L;
        UserNotificationData userData = new UserNotificationData(userId, "João Silva", "joao@email.com");
        Notification expectedNotification = new Notification();
        expectedNotification.setTitle(title);
        expectedNotification.setMessage(message);
        expectedNotification.setNotificationType(notificationType);
        expectedNotification.setUserId(userId);
        expectedNotification.setRequestId(requestId);

        when(requestPublicApi.findNotificationDataById(eq(requestId)))
                .thenReturn(new RequestNotificationData(
                        requestId,
                        List.of()
                ));
        when(userPublicApi.findNotificationDataByIds(eq(new LinkedHashSet<>(List.of(userId)))))
                .thenReturn(List.of(userData));
        when(notificationMapper.toEntity(any(NotificationRequest.class)))
                .thenReturn(expectedNotification);
        when(notificationRepository.saveAll(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // When
        createInternalNotificationService.createNotifications(
                title,
                message,
                notificationType,
                requestId,
                List.of(userId)
        );

        // Then
        @SuppressWarnings("unchecked")
        var notificationsCaptor = ArgumentCaptor.forClass(List.class);
        verify(notificationRepository).saveAll(notificationsCaptor.capture());

        List<Notification> savedNotifications = notificationsCaptor.getValue();
        assertThat(savedNotifications).hasSize(1);

        Notification savedNotification = savedNotifications.get(0);
        assertThat(savedNotification.getTitle()).isEqualTo(title);
        assertThat(savedNotification.getMessage()).isEqualTo(message);
        assertThat(savedNotification.getUserId()).isEqualTo(userId);
        assertThat(savedNotification.getRequestId()).isEqualTo(requestId);
        assertThat(savedNotification.getNotificationType()).isEqualTo(notificationType);
    }

    @Test
    @DisplayName("Should persist administrative alert without a linked request")
    void shouldPersistAdministrativeAlertWithoutLinkedRequest() {
        Long administratorId = 99L;
        UserNotificationData administrator = new UserNotificationData(
                administratorId,
                "Administrador",
                "admin@email.com"
        );
        Notification expectedNotification = new Notification();
        expectedNotification.setNotificationType(NotificationType.ALERTA_ADMINISTRATIVO);
        expectedNotification.setUserId(administratorId);

        when(userPublicApi.findNotificationDataByIds(eq(new LinkedHashSet<>(List.of(administratorId)))))
                .thenReturn(List.of(administrator));
        when(notificationMapper.toEntity(any(NotificationRequest.class)))
                .thenReturn(expectedNotification);
        when(notificationRepository.saveAll(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        List<Notification> notifications = createInternalNotificationService.createNotifications(
                "Alerta administrativo",
                "Uma ação administrativa foi realizada.",
                NotificationType.ALERTA_ADMINISTRATIVO,
                null,
                List.of(administratorId)
        );

        verify(requestPublicApi, never()).findNotificationDataById(any());
        assertThat(notifications).singleElement().satisfies(notification -> {
            assertThat(notification.getNotificationType()).isEqualTo(NotificationType.ALERTA_ADMINISTRATIVO);
            assertThat(notification.getUserId()).isEqualTo(administratorId);
            assertThat(notification.getRequestId()).isNull();
        });
    }

    @Test
    @DisplayName("Should exclude administrators from ordinary notifications")
    void shouldExcludeAdministratorsFromOrdinaryNotifications() {
        Long administratorId = 99L;
        when(userPublicApi.findActiveUserIdsByRole("ADMIN")).thenReturn(List.of(administratorId));
        when(requestPublicApi.findNotificationDataById(10L))
                .thenReturn(new RequestNotificationData(10L, List.of()));

        List<Notification> notifications = createInternalNotificationService.createNotifications(
                "Status atualizado",
                "A solicitação foi atualizada.",
                NotificationType.STATUS_ALTERADO,
                10L,
                List.of(administratorId)
        );

        assertThat(notifications).isEmpty();
        verify(userPublicApi, never()).findNotificationDataByIds(any());
        verify(notificationRepository, never()).saveAll(any());
    }
}
