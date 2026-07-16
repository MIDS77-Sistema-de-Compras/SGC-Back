package net.centroweg.gerenciamentocompras.modules.notification.service.useCases.serviceIntrf;

import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.request.NotificationRequest;
import net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.response.NotificationResponse;
import java.util.List;
import net.centroweg.gerenciamentocompras.modules.notification.domain.entity.Notification;

/**
 * Interface de serviço para operações de gerenciamento de {@link Notification}.
 */
public interface NotificationService {

    /**
     * Cria e persiste uma nova notificação no banco de dados.
     * @param request dados da notificação.
     * @return notificação criada.
     */
    NotificationResponse createNotification(NotificationRequest request);

    /**
     * Lista todas as notificações cadastradas no banco de dados por identificador do usuário.
     * @param userId identificador do usuário.
     * @return lista todas as notificações encontradas.
     */
    List<NotificationResponse> findNotificationsByUser(Long userId);

    /**
     * Lista todas as notificações cadastradas no banco de dados não visualizadas por identificador do usuário.
     * @param userId identificador do usuário.
     * @return lista todas as notificações encontradas.
     */
    List<NotificationResponse> findUnviewedNotificationsByUser(Long userId);

    /**
     * Marcar uma notificação existente como visualizada no banco de dados.
     * @param id identificador da notificação.
     * @return notificação já atualizada.
     */
    NotificationResponse markAsViewed(Long id);

    /**
     * Listar todas as notificações cadastradas no banco de dados pelo dono da solicitação.
     * @param userPrincipal usuário.
     * @return lista com todas as notificações encontradas.
     */
    List<NotificationResponse> findByOwnUser (UserPrincipal userPrincipal);
}
