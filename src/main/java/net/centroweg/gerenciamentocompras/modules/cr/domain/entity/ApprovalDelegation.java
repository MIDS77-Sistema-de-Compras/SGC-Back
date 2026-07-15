package net.centroweg.gerenciamentocompras.modules.cr.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Registra a substituição temporária de um supervisor em todas as suas filiais.
 */
@Entity
@Table(
        name = "approval_delegation",
        indexes = {
                @Index(name = "idx_approval_delegation_status", columnList = "status"),
                @Index(name = "idx_approval_delegation_start", columnList = "status,start_at"),
                @Index(name = "idx_approval_delegation_end", columnList = "status,end_at"),
                @Index(name = "idx_approval_delegation_delegator", columnList = "delegator_user_id,status")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class ApprovalDelegation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "delegator_user_id", nullable = false)
    private Long delegatorUserId;

    @Column(name = "delegate_user_id", nullable = false)
    private Long delegateUserId;

    @Column(name = "start_at", nullable = false)
    private LocalDateTime startAt;

    @Column(name = "end_at", nullable = false)
    private LocalDateTime endAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ApprovalDelegationStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "activated_at")
    private LocalDateTime activatedAt;

    @Column(name = "finished_at")
    private LocalDateTime finishedAt;

    @Version
    private Long version;

    @BatchSize(size = 30)
    @OneToMany(mappedBy = "approvalDelegation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApprovalDelegationBranch> branches = new ArrayList<>();

    public ApprovalDelegation(
            Long delegatorUserId,
            Long delegateUserId,
            LocalDateTime startAt,
            LocalDateTime endAt,
            LocalDateTime createdAt
    ) {
        this.delegatorUserId = delegatorUserId;
        this.delegateUserId = delegateUserId;
        this.startAt = startAt;
        this.endAt = endAt;
        this.createdAt = createdAt;
        this.status = ApprovalDelegationStatus.PENDING;
    }

    public void addBranch(CrBranch crBranch) {
        branches.add(new ApprovalDelegationBranch(this, crBranch));
    }
}
