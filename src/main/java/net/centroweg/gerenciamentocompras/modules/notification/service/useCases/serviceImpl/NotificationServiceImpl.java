package net.centroweg.gerenciamentocompras.modules.notification.service.useCases.serviceImpl;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.request.NotificationRequest;
import net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.response.NotificationResponse;
import net.centroweg.gerenciamentocompras.modules.notification.service.useCases.serviceIntrf.NotificationService;
import org.springframework.stereotype.Service;
import java.util.List;
import net.centroweg.gerenciamentocompras.modules.notification.domain.entity.Notification;

/**
 * Classe de serviço da {@link Notification} que delega cada operação à sua respectiva classe de funcionalidade.
 * Implementa {@link NotificationService} que segue o princípio de responsabilidade única, onde cada método apenas repassa a chamada para uma classe especializada responsável por uma única operação.
 */
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    /**
     * Componente responsável pela criação de uma notificação.
     */
    private final CreateNotificationServiceImpl createNotificationService;

    /**
     * Componente responsável pela listagem de notificações por usuário.
     */
    private final FindNotificationsByUserServiceImpl findNotificationsByUserService;

    /**
     * Componente responsável pela listagem de notificações não visualizadas por usuário.
     */
    private final FindUnviewedNotificationsByUserServiceImpl findUnviewedNotificationsByUserService;

    /**
     * Componente responsável por marcar uma notificação como lida.
     */
    private final MarkAsViewedServiceImpl markAsViewedService;

    /**
     * Componente responsável pela listagem de notificações pelo dono da solicitação.
     */
    private final FindNotificationByOwnUser findNotificationByOwnUser;

    /**
     * Cria uma notificação.
     * @param request dados da notificação.
     * @return notificação criada.
     */
    @Override
    public NotificationResponse createNotification(NotificationRequest request) {
        return createNotificationService.createNotification(request);
    }

    /**
     * Lista notificações pelo identificador do usuário.
     * @param userId identificador do usuário.
     * @return lista de notificações encontradas.
     */
    @Override
    public List<NotificationResponse> findNotificationsByUser(Long userId) {
        return findNotificationsByUserService.findNotificationsByUser(userId);
    }

    /**
     * Lista notificações não visualizadas pelo identificador do usuário.
     * @param userId identificador do usuário.
     * @return lista de notificações encontradas.
     */
    @Override
    public List<NotificationResponse> findUnviewedNotificationsByUser(Long userId) {
        return findUnviewedNotificationsByUserService.findUnviewedByUser(userId);
    }

    /**
     * Marca uma notificação como visualizada.
     * @param id identificador da notificação.
     * @return notificação atualizada.
     */
    @Override
    public NotificationResponse markAsViewed(Long id) {
        return markAsViewedService.markAsViewed(id);
    }

    /**
     * Lista notificações que pertencem ao dono da solicitação.
     * @param userPrincipal usuário que fez a solicitação.
     * @return lista de notificações encontradas.
     */
    @Override
    public List<NotificationResponse> findByOwnUser(UserPrincipal userPrincipal){
        return findNotificationByOwnUser.findNotificationsByOwnUser(userPrincipal);
    }
}
