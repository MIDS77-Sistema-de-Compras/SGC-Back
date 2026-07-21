package net.centroweg.gerenciamentocompras.modules.notification.infrastructure.persistence;

import net.centroweg.gerenciamentocompras.modules.notification.domain.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repositório de acesso a dados da entidade {@link Notification}.
 */
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /**
     * Lista todas as notificações cadastradas no banco de dados pelo identificador do usuário.
     * @param userId identificador do usuário.
     * @return lista com todas as notificações encontradas, caso exista.
     */
    List<Notification> findByUserId(Long userId);

    /**
     * Lista todas as notificações cadastradas no banco de dados não visualizadas pelo identificador do usuário.
     * @param userId identificador do usuário.
     * @return lista com todas as notificações encontradas, caso exista.
     */
    List<Notification> findByUserIdAndViewedFalse(Long userId);
}
