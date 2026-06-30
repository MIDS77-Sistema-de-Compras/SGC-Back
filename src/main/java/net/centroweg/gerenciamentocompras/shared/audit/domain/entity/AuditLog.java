package net.centroweg.gerenciamentocompras.shared.audit.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import org.hibernate.annotations.CreationTimestamp;

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
    private User userAgent;

    @ManyToOne(optional = true)
    private User userTarget;

    @Column(nullable = false)
    private String typeAction;

    @ManyToOne(optional = true)
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
