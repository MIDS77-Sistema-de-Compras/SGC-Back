package net.centroweg.gerenciamentocompras.modules.cr.service.approvaldelegation;

import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.*;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.ApprovalDelegationTransitionException;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.ApprovalDelegationRepository;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrBranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.service.approvaldelegationservice.functionality.FinishApprovalDelegation;
import net.centroweg.gerenciamentocompras.modules.cr.service.crbranchservice.functionality.RemoveCrBranchResponsible;
import net.centroweg.gerenciamentocompras.modules.cr.service.event.ApprovalDelegationFinishedEvent;
import net.centroweg.gerenciamentocompras.modules.user.service.api.UserPublicApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FinishApprovalDelegationTest {

    private static final Instant NOW = Instant.parse("2026-08-15T18:00:00Z");

    @Mock private ApprovalDelegationRepository delegationRepository;
    @Mock private CrBranchRepository crBranchRepository;
    @Mock private UserPublicApi userPublicApi;
    @Mock private RemoveCrBranchResponsible removeResponsible;
    @Mock private ApplicationEventPublisher eventPublisher;

    private FinishApprovalDelegation useCase;

    @BeforeEach
    void setUp() {
        useCase = new FinishApprovalDelegation(
                delegationRepository,
                crBranchRepository,
                userPublicApi,
                removeResponsible,
                eventPublisher,
                Clock.fixed(NOW, ZoneOffset.UTC)
        );
        lenient().when(delegationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void shouldReactivateDelegatorRemoveTemporaryRelationshipAndPublishEvent() {
        CrBranch branch = branch(10L);
        ApprovalDelegation delegation = active(branch, true);
        when(delegationRepository.findByIdForUpdate(100L)).thenReturn(Optional.of(delegation));
        when(crBranchRepository.findAllByIdForUpdate(List.of(10L))).thenReturn(List.of(branch));

        ApprovalDelegation result = useCase.finish(100L);

        assertEquals(ApprovalDelegationStatus.FINISHED, result.getStatus());
        assertEquals(now(), result.getFinishedAt());
        verify(userPublicApi).changeUserActivationStatus(1L, true);
        verify(removeResponsible).removeCrBranchResponsible(10L, 2L);
        verify(eventPublisher, times(1)).publishEvent(any(ApprovalDelegationFinishedEvent.class));
    }

    @Test
    void shouldKeepPreexistingPermanentRelationship() {
        CrBranch branch = branch(10L);
        ApprovalDelegation delegation = active(branch, false);
        when(delegationRepository.findByIdForUpdate(100L)).thenReturn(Optional.of(delegation));
        when(crBranchRepository.findAllByIdForUpdate(List.of(10L))).thenReturn(List.of(branch));

        useCase.finish(100L);

        verify(removeResponsible, never()).removeCrBranchResponsible(any(), any());
    }

    @Test
    void shouldKeepRelationshipRequiredByAnotherActiveDelegation() {
        CrBranch branch = branch(10L);
        ApprovalDelegation delegation = active(branch, true);
        when(delegationRepository.findByIdForUpdate(100L)).thenReturn(Optional.of(delegation));
        when(crBranchRepository.findAllByIdForUpdate(List.of(10L))).thenReturn(List.of(branch));
        when(delegationRepository.existsOtherActiveDelegationForRelationship(
                100L, ApprovalDelegationStatus.ACTIVE, 2L, 10L
        )).thenReturn(true);

        useCase.finish(100L);

        verify(removeResponsible, never()).removeCrBranchResponsible(any(), any());
    }

    @Test
    void shouldBeIdempotentWhenAlreadyFinished() {
        ApprovalDelegation delegation = active(branch(10L), true);
        delegation.setStatus(ApprovalDelegationStatus.FINISHED);
        delegation.setFinishedAt(now().minusHours(1));
        when(delegationRepository.findByIdForUpdate(100L)).thenReturn(Optional.of(delegation));

        ApprovalDelegation result = useCase.finish(100L);

        assertSame(delegation, result);
        verifyNoInteractions(crBranchRepository, userPublicApi, removeResponsible, eventPublisher);
    }

    @Test
    void shouldRejectPendingOrNotYetExpiredDelegation() {
        ApprovalDelegation pending = active(branch(10L), true);
        pending.setStatus(ApprovalDelegationStatus.PENDING);
        when(delegationRepository.findByIdForUpdate(100L)).thenReturn(Optional.of(pending));
        assertThrows(ApprovalDelegationTransitionException.class, () -> useCase.finish(100L));

        ApprovalDelegation notExpired = active(branch(20L), true);
        notExpired.setEndAt(now().plusMinutes(1));
        when(delegationRepository.findByIdForUpdate(101L)).thenReturn(Optional.of(notExpired));
        assertThrows(ApprovalDelegationTransitionException.class, () -> useCase.finish(101L));
        verify(userPublicApi, never()).changeUserActivationStatus(any(), anyBoolean());
    }

    @Test
    void shouldPublishSortedBranchIdsOnce() {
        CrBranch second = branch(20L);
        CrBranch first = branch(10L);
        ApprovalDelegation delegation = active(List.of(second, first), List.of(true, true));
        when(delegationRepository.findByIdForUpdate(100L)).thenReturn(Optional.of(delegation));
        when(crBranchRepository.findAllByIdForUpdate(List.of(10L, 20L)))
                .thenReturn(List.of(first, second));

        useCase.finish(100L);

        ArgumentCaptor<ApprovalDelegationFinishedEvent> captor =
                ArgumentCaptor.forClass(ApprovalDelegationFinishedEvent.class);
        verify(eventPublisher).publishEvent(captor.capture());
        assertEquals(List.of(10L, 20L), captor.getValue().branchIds());
    }

    private ApprovalDelegation active(CrBranch branch, boolean temporary) {
        return active(List.of(branch), List.of(temporary));
    }

    private ApprovalDelegation active(List<CrBranch> branches, List<Boolean> temporary) {
        ApprovalDelegation delegation = new ApprovalDelegation(
                1L, 2L, now().minusDays(2), now(), now().minusDays(3)
        );
        delegation.setId(100L);
        delegation.setStatus(ApprovalDelegationStatus.ACTIVE);
        delegation.setActivatedAt(now().minusDays(2));
        for (int index = 0; index < branches.size(); index++) {
            delegation.addBranch(branches.get(index));
            delegation.getBranches().get(index).setTemporaryRelationshipCreated(temporary.get(index));
        }
        return delegation;
    }

    private CrBranch branch(Long id) {
        CrBranch branch = new CrBranch();
        branch.setId(id);
        branch.setResponsibleUsers(new ArrayList<>());
        return branch;
    }

    private LocalDateTime now() {
        return LocalDateTime.ofInstant(NOW, ZoneOffset.UTC);
    }
}
