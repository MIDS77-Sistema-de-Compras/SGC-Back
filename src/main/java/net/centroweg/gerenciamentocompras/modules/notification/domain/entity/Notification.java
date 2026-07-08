package net.centroweg.gerenciamentocompras.modules.notification.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import java.time.LocalDateTime;

/**
 * Entidade que representa uma notificação(notification) no sistema de gerenciamento de compras.
 */
@Entity
@Table(name = "notification")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Notification {

    /**
     * Identificador único da notificação, gerado automaticamente pelo banco de dados.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Titulo da notificação, não pode ser nulo.
     */
    @Column(nullable = false)
    private String title;

    /**
     * Mensagem a ser passada na notificação, não pode ser nulo.
     */
    @Column(nullable = false)
    private String message;

    /**
     * Visualização da notificação, não pode ser nulo.
     */
    @Column(nullable = false)
    private Boolean viewed = false;

    /**
     * Data e hora da criação da notificação, não pode ser nulo e nem alterado.
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Relacionamento com a entidade usuário, uma notificação tem apenas um usuário.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Relacionamento com a entidade requisição, uma notificação tem apenas uma requisição.
     */
    @ManyToOne
    @JoinColumn(name = "request_id")
    private Request request;


    /**
     * Pega o momento atual(data e hora) e incrementa no atributo de criado em.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

}
