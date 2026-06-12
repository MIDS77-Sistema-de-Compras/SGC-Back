package net.centroweg.gerenciamentocompras.modules.notification.infrastructure.persistence;

import net.centroweg.gerenciamentocompras.modules.notification.domain.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserId(Long userId);
    List<Notification> findByUserIdAndViewedFalse(Long userId);
}
