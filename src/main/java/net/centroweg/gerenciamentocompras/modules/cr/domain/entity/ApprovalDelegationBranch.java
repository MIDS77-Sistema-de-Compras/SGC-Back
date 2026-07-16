package net.centroweg.gerenciamentocompras.modules.cr.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Registra uma filial coberta pela delegação e se o vínculo do substituto foi criado por ela.
 */
@Entity
@Table(
        name = "approval_delegation_branch",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_approval_delegation_branch",
                columnNames = {"approval_delegation_id", "cr_branch_id"}
        ),
        indexes = @Index(name = "idx_approval_delegation_branch_cr", columnList = "cr_branch_id")
)
@Getter
@Setter
@NoArgsConstructor
public class ApprovalDelegationBranch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "approval_delegation_id", nullable = false)
    private ApprovalDelegation approvalDelegation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cr_branch_id", nullable = false)
    private CrBranch crBranch;

    @Column(name = "temporary_relationship_created", nullable = false)
    private boolean temporaryRelationshipCreated;

    public ApprovalDelegationBranch(ApprovalDelegation approvalDelegation, CrBranch crBranch) {
        this.approvalDelegation = approvalDelegation;
        this.crBranch = crBranch;
    }
}
