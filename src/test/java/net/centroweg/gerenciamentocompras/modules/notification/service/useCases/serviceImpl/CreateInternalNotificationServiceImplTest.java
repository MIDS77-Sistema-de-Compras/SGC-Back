package net.centroweg.gerenciamentocompras.modules.notification.service.useCases.serviceImpl;

import net.centroweg.gerenciamentocompras.modules.notification.domain.entity.Notification;
import net.centroweg.gerenciamentocompras.modules.notification.infrastructure.persistence.NotificationRepository;
import net.centroweg.gerenciamentocompras.modules.notification.service.mapper.NotificationMapper;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateInternalNotificationServiceImplTest {

    @Mock private NotificationRepository notificationRepository;
    @Mock private UserRepository userRepository;
    @Mock private RequestRepository requestRepository;
    @Mock private NotificationMapper notificationMapper;

    @Test
    void shouldPersistOneNotificationPerDistinctNonNullUserId() {
        Request request = new Request();
        request.setId(10L);
        User ana = user(1L, "Ana");
        User bia = user(2L, "Bia");
        when(requestRepository.findById(10L)).thenReturn(Optional.of(request));
        when(userRepository.findAllById(any())).thenReturn(List.of(ana, bia));
        when(notificationMapper.toEntity(any(), any(), any(), any())).thenAnswer(invocation -> {
            Notification notification = new Notification();
            notification.setTitle(invocation.getArgument(0));
            notification.setMessage(invocation.getArgument(1));
            notification.setUser(invocation.getArgument(2));
            notification.setRequest(invocation.getArgument(3));
            return notification;
        });
        when(notificationRepository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));
        CreateInternalNotificationServiceImpl service = new CreateInternalNotificationServiceImpl(
                notificationRepository, userRepository, requestRepository, notificationMapper
        );

        service.createNotifications("Titulo", "Mensagem", 10L, java.util.Arrays.asList(1L, 1L, null, 2L));

        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<Notification>> notifications = ArgumentCaptor.forClass(List.class);
        verify(notificationRepository).saveAll(notifications.capture());
        assertThat(notifications.getValue())
                .extracting(notification -> notification.getUser().getId())
                .containsExactly(1L, 2L);
    }

    private User user(Long id, String name) {
        User user = new User(name, "52998224725", name.toLowerCase() + "@teste.com", "Senha@123", "1234", true);
        user.setId(id);
        return user;
    }
}
