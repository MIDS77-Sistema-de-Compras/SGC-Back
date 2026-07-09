package net.centroweg.gerenciamentocompras.modules.notification.service.useCases.serviceIntrf;

import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.request.NotificationRequest;
import net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.response.NotificationResponse;
import java.util.List;
import net.centroweg.gerenciamentocompras.modules.notification.domain.entity.Notification;

/**
 * Interface de serviço para operações de gerenciamento de {@link Notification}
 */
public interface NotificationService {

    /**
     * Cria uma nova notificação.
     * @param request dados da notificação.
     * @return notificação criada.
     */
    NotificationResponse createNotification(NotificationRequest request);

    /**
     * Lista notificações por identificador do usuário.
     * @param userId identificador do usuário.
     * @return lista de notificações encontradas.
     */
    List<NotificationResponse> findNotificationsByUser(Long userId);

    /**
     * Lista de notificações não visualizadas por identificador do usuário.
     * @param userId identificador do usuário.
     * @return lista de notificações encontradas.
     */
    List<NotificationResponse> findUnviewedNotificationsByUser(Long userId);

    /**
     * Marcar notificação como visualizada.
     * @param id identificador da notificação.
     * @return notificação com a visualização já atualizada.
     */
    NotificationResponse markAsViewed(Long id);

    /**
     * Listar notificações pelo dono da solicitação.
     * @param userPrincipal usuário que fez a solicitação.
     * @return lista com as notificações encontradas.
     */
    List<NotificationResponse> findByOwnUser (UserPrincipal userPrincipal);
}
