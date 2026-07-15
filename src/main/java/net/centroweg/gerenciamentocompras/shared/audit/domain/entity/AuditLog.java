package net.centroweg.gerenciamentocompras.shared.audit.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_log")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User userAgent;

    @ManyToOne(optional = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User userTarget;

    @Column(nullable = false)
    private String typeAction;

    /**
     * Descrição complementar da ação. Preenchida, por exemplo, quando um
     * administrador executa a ação logado na conta de outro usuário.
     */
    @Column(length = 500)
    private String description;

    @ManyToOne(optional = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Request request;

    @CreationTimestamp
    private LocalDateTime timestamp;

    public AuditLog(User userAgent, User userTarget, String typeAction, Request request) {
        this.userAgent = userAgent;
        this.userTarget = userTarget;
        this.typeAction = typeAction;
        this.request = request;
    }

    public AuditLog(User userAgent, String typeAction) {
        this.userAgent = userAgent;
        this.typeAction = typeAction;
    }


}
