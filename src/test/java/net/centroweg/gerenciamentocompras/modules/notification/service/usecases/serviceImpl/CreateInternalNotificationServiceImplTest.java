package net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import net.centroweg.gerenciamentocompras.modules.notification.domain.entity.Notification;
import net.centroweg.gerenciamentocompras.modules.notification.domain.enums.NotificationType;
import net.centroweg.gerenciamentocompras.modules.notification.infrastructure.persistence.NotificationRepository;
import net.centroweg.gerenciamentocompras.modules.notification.service.mapper.NotificationMapper;
import net.centroweg.gerenciamentocompras.modules.request.service.api.RequestPublicApi;
import net.centroweg.gerenciamentocompras.modules.request.service.api.dto.RequestNotificationData;
import net.centroweg.gerenciamentocompras.modules.user.service.api.UserPublicApi;
import net.centroweg.gerenciamentocompras.modules.user.service.api.dto.UserNotificationData;

@ExtendWith(MockitoExtension.class)
class CreateInternalNotificationServiceImplTest {

    @Mock NotificationRepository notificationRepository;
    @Mock UserPublicApi userPublicApi;
    @Mock RequestPublicApi requestPublicApi;

    @Test
    void shouldPersistOneNotificationPerDistinctNonNullUserId() {
        when(requestPublicApi.findNotificationDataById(10L)).thenReturn(new RequestNotificationData(10L, List.of()));
        when(userPublicApi.findNotificationDataByIds(any())).thenReturn(List.of(
                new UserNotificationData(1L, "Ana", "ana@teste.com"),
                new UserNotificationData(2L, "Bia", "bia@teste.com")
        ));
        when(notificationRepository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));
        var service = new CreateInternalNotificationServiceImpl(
                notificationRepository, userPublicApi, requestPublicApi, new NotificationMapper());

        service.createNotifications("Titulo", "Mensagem", NotificationType.NOTIFICACAO_TESTE, 10L, java.util.Arrays.asList(1L, 1L, null, 2L));

        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<Notification>> notifications = ArgumentCaptor.forClass(List.class);
        verify(notificationRepository).saveAll(notifications.capture());
        assertThat(notifications.getValue()).extracting(Notification::getUserId).containsExactly(1L, 2L);
        assertThat(notifications.getValue()).extracting(Notification::getRequestId).containsOnly(10L);
    }
}
