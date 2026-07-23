package net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceImpl;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.notification.domain.enums.NotificationType;
import net.centroweg.gerenciamentocompras.modules.notification.infrastructure.persistence.NotificationRepository;
import net.centroweg.gerenciamentocompras.modules.notification.service.mapper.NotificationMapper;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;

@ExtendWith(MockitoExtension.class)
class FindNotificationByOwnUserTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationMapper notificationMapper;

    @InjectMocks
    private FindNotificationByOwnUser service;

    @Test
    void shouldReturnOnlyAdministrativeAlertsForAdministrator() {
        var pageable = PageRequest.of(0, 20);
        var userPrincipal = principal(10L, "ADMIN");
        when(notificationRepository.findByUserIdAndNotificationType(
                10L,
                NotificationType.ALERTA_ADMINISTRATIVO,
                pageable
        )).thenReturn(new PageImpl<>(List.of()));

        service.findNotificationsByOwnUser(userPrincipal, pageable);

        verify(notificationRepository).findByUserIdAndNotificationType(
                10L,
                NotificationType.ALERTA_ADMINISTRATIVO,
                pageable
        );
        verify(notificationRepository, never()).findByUserId(10L, pageable);
    }

    @Test
    void shouldPreserveAllNotificationsForOtherRoles() {
        var pageable = PageRequest.of(0, 20);
        var userPrincipal = principal(20L, "DOCENTE");
        when(notificationRepository.findByUserId(20L, pageable))
                .thenReturn(new PageImpl<>(List.of()));

        service.findNotificationsByOwnUser(userPrincipal, pageable);

        verify(notificationRepository).findByUserId(20L, pageable);
        verify(notificationRepository, never()).findByUserIdAndNotificationType(
                20L,
                NotificationType.ALERTA_ADMINISTRATIVO,
                pageable
        );
    }

    private UserPrincipal principal(Long id, String roleName) {
        Role role = new Role();
        role.setName(roleName);

        User user = new User();
        user.setId(id);
        user.setRole(role);

        return new UserPrincipal(user);
    }
}
