package net.centroweg.gerenciamentocompras.modules.notification.infrastructure.persistence;

import net.centroweg.gerenciamentocompras.modules.notification.domain.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repositório de acesso a dados da entidade {@link Notification}.
 */
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /**
     * Busca todas as notificações pelo identificador do usuário.
     * @param userId identificador do usuário.
     * @return uma lista com as notificações encontradas.
     */
    List<Notification> findByUserId(Long userId);

    /**
     * Busca todas as notificações não visualizadas pelo identificador do usuário.
     * @param userId identificador do usuário.
     * @return uma lista com as notificações encontradas.
     */
    List<Notification> findByUserIdAndViewedFalse(Long userId);
}
