package net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository;

import jakarta.persistence.LockModeType;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.ApprovalDelegation;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.ApprovalDelegationStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApprovalDelegationRepository extends JpaRepository<ApprovalDelegation, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select delegation from ApprovalDelegation delegation where delegation.id = :id")
    Optional<ApprovalDelegation> findByIdForUpdate(@Param("id") Long id);

    @EntityGraph(attributePaths = {"branches", "branches.crBranch"})
    List<ApprovalDelegation> findAllByDelegatorUserIdOrderByCreatedAtDesc(Long delegatorUserId);

    @Query("""
            select count(delegation) > 0
            from ApprovalDelegation delegation
            where delegation.delegatorUserId = :delegatorUserId
              and delegation.status in :statuses
              and :startAt < delegation.endAt
              and :endAt > delegation.startAt
            """)
    boolean existsOverlappingDelegation(
            @Param("delegatorUserId") Long delegatorUserId,
            @Param("statuses") Collection<ApprovalDelegationStatus> statuses,
            @Param("startAt") LocalDateTime startAt,
            @Param("endAt") LocalDateTime endAt
    );

    @Query("""
            select delegation.id
            from ApprovalDelegation delegation
            where delegation.status = :status
              and delegation.startAt <= :now
            order by delegation.startAt, delegation.id
            """)
    List<Long> findIdsReadyForActivation(
            @Param("status") ApprovalDelegationStatus status,
            @Param("now") LocalDateTime now
    );

    @Query("""
            select delegation.id
            from ApprovalDelegation delegation
            where delegation.status = :status
              and delegation.endAt <= :now
            order by delegation.endAt, delegation.id
            """)
    List<Long> findIdsReadyForFinish(
            @Param("status") ApprovalDelegationStatus status,
            @Param("now") LocalDateTime now
    );

    @Query("""
            select count(delegation) > 0
            from ApprovalDelegation delegation
            join delegation.branches delegationBranch
            where delegation.id <> :delegationId
              and delegation.status = :status
              and delegation.delegateUserId = :delegateUserId
              and delegationBranch.crBranch.id = :crBranchId
            """)
    boolean existsOtherActiveDelegationForRelationship(
            @Param("delegationId") Long delegationId,
            @Param("status") ApprovalDelegationStatus status,
            @Param("delegateUserId") Long delegateUserId,
            @Param("crBranchId") Long crBranchId
    );

    @Query("""
            select count(delegation) > 0
            from ApprovalDelegation delegation
            join delegation.branches delegationBranch
            where delegation.id <> :delegationId
              and delegation.status = :status
              and delegation.delegateUserId = :delegateUserId
              and delegationBranch.crBranch.id = :crBranchId
              and delegationBranch.temporaryRelationshipCreated = true
            """)
    boolean existsOtherActiveTemporaryDelegationForRelationship(
            @Param("delegationId") Long delegationId,
            @Param("status") ApprovalDelegationStatus status,
            @Param("delegateUserId") Long delegateUserId,
            @Param("crBranchId") Long crBranchId
    );
}
