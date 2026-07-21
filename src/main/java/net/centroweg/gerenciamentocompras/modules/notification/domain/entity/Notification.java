package net.centroweg.gerenciamentocompras.modules.notification.domain.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.centroweg.gerenciamentocompras.modules.notification.domain.enums.NotificationType;

@Entity
@Table(name = "notification")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 4000)
    private String message;

    @Column(nullable = false)
    private Boolean viewed = false;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    // Persistido como texto (nome do enum). ORDINAL é frágil: adicionar/reordenar
    // um valor do enum quebra os dados e a check constraint existentes no banco.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType notificationType;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "request_id")
    private Long requestId;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

}
