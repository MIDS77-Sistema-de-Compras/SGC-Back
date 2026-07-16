package net.centroweg.gerenciamentocompras.modules.cr.service.approvaldelegation;

import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.*;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.ApprovalDelegationTransitionException;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.ApprovalDelegationRepository;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrBranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.service.approvaldelegationservice.functionality.ActivateApprovalDelegation;
import net.centroweg.gerenciamentocompras.modules.cr.service.approvaldelegationservice.functionality.ApprovalDelegationParticipantValidator;
import net.centroweg.gerenciamentocompras.modules.cr.service.crbranchservice.functionality.AssignCrBranchResponsible;
import net.centroweg.gerenciamentocompras.modules.cr.service.event.ApprovalDelegationActivatedEvent;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.service.api.UserPublicApi;
import net.centroweg.gerenciamentocompras.modules.user.service.api.dto.UserSummaryPublicResponse;
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
class ActivateApprovalDelegationTest {

    private static final Instant NOW = Instant.parse("2026-08-01T12:00:00Z");

    @Mock private ApprovalDelegationRepository delegationRepository;
    @Mock private CrBranchRepository crBranchRepository;
    @Mock private UserPublicApi userPublicApi;
    @Mock private AssignCrBranchResponsible assignResponsible;
    @Mock private ApplicationEventPublisher eventPublisher;

    private ActivateApprovalDelegation useCase;

    @BeforeEach
    void setUp() {
        useCase = new ActivateApprovalDelegation(
                delegationRepository,
                crBranchRepository,
                userPublicApi,
                new ApprovalDelegationParticipantValidator(),
                assignResponsible,
                eventPublisher,
                Clock.fixed(NOW, ZoneOffset.UTC)
        );
        lenient().when(userPublicApi.findUserSummaryById(1L))
                .thenReturn(Optional.of(summary(1L, true, "SUPERVISOR")));
        lenient().when(userPublicApi.findUserSummaryById(2L))
                .thenReturn(Optional.of(summary(2L, true, "SUPERVISOR")));
        lenient().when(delegationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void shouldDeactivateDelegatorAddDelegateAndPublishSingleEvent() {
        CrBranch first = branch(10L);
        CrBranch second = branch(20L);
        ApprovalDelegation delegation = pending(first, second);
        when(delegationRepository.findByIdForUpdate(100L)).thenReturn(Optional.of(delegation));
        when(crBranchRepository.findAllByIdForUpdate(List.of(10L, 20L)))
                .thenReturn(List.of(first, second));

        ApprovalDelegation result = useCase.activate(100L);

        assertEquals(ApprovalDelegationStatus.ACTIVE, result.getStatus());
        assertEquals(now(), result.getActivatedAt());
        assertTrue(result.getBranches().stream()
                .allMatch(ApprovalDelegationBranch::isTemporaryRelationshipCreated));
        verify(userPublicApi).changeUserActivationStatus(1L, false);
        verify(assignResponsible).assignCrBranchResponsible(10L, 2L);
        verify(assignResponsible).assignCrBranchResponsible(20L, 2L);
        verify(eventPublisher, times(1)).publishEvent(any(ApprovalDelegationActivatedEvent.class));
    }

    @Test
    void shouldPreservePreexistingRelationshipWithoutDuplicateAssignment() {
        CrBranch branch = branch(10L);
        User delegate = new User();
        delegate.setId(2L);
        branch.getResponsibleUsers().add(delegate);
        ApprovalDelegation delegation = pending(branch);
        when(delegationRepository.findByIdForUpdate(100L)).thenReturn(Optional.of(delegation));
        when(crBranchRepository.findAllByIdForUpdate(List.of(10L))).thenReturn(List.of(branch));

        useCase.activate(100L);

        assertFalse(delegation.getBranches().getFirst().isTemporaryRelationshipCreated());
        verify(assignResponsible, never()).assignCrBranchResponsible(any(), any());
    }

    @Test
    void shouldInheritCleanupWhenExistingRelationshipCameFromAnotherDelegation() {
        CrBranch branch = branch(10L);
        User delegate = new User();
        delegate.setId(2L);
        branch.getResponsibleUsers().add(delegate);
        ApprovalDelegation delegation = pending(branch);
        when(delegationRepository.findByIdForUpdate(100L)).thenReturn(Optional.of(delegation));
        when(crBranchRepository.findAllByIdForUpdate(List.of(10L))).thenReturn(List.of(branch));
        when(delegationRepository.existsOtherActiveTemporaryDelegationForRelationship(
                100L, ApprovalDelegationStatus.ACTIVE, 2L, 10L
        )).thenReturn(true);

        useCase.activate(100L);

        assertTrue(delegation.getBranches().getFirst().isTemporaryRelationshipCreated());
        verify(assignResponsible, never()).assignCrBranchResponsible(any(), any());
    }

    @Test
    void shouldBeIdempotentWhenAlreadyActive() {
        ApprovalDelegation delegation = pending(branch(10L));
        delegation.setStatus(ApprovalDelegationStatus.ACTIVE);
        delegation.setActivatedAt(now().minusHours(1));
        when(delegationRepository.findByIdForUpdate(100L)).thenReturn(Optional.of(delegation));

        ApprovalDelegation result = useCase.activate(100L);

        assertSame(delegation, result);
        verifyNoInteractions(crBranchRepository, assignResponsible, eventPublisher);
        verify(userPublicApi, never()).changeUserActivationStatus(any(), anyBoolean());
    }

    @Test
    void shouldRejectActivationBeforeStartOrAfterFinish() {
        ApprovalDelegation future = pending(branch(10L));
        future.setStartAt(now().plusHours(1));
        when(delegationRepository.findByIdForUpdate(100L)).thenReturn(Optional.of(future));
        assertThrows(ApprovalDelegationTransitionException.class, () -> useCase.activate(100L));

        ApprovalDelegation finished = pending(branch(10L));
        finished.setStatus(ApprovalDelegationStatus.FINISHED);
        when(delegationRepository.findByIdForUpdate(101L)).thenReturn(Optional.of(finished));
        assertThrows(ApprovalDelegationTransitionException.class, () -> useCase.activate(101L));
    }

    @Test
    void shouldRejectDelegateThatBecameInactive() {
        ApprovalDelegation delegation = pending(branch(10L));
        when(delegationRepository.findByIdForUpdate(100L)).thenReturn(Optional.of(delegation));
        when(userPublicApi.findUserSummaryById(2L))
                .thenReturn(Optional.of(summary(2L, false, "SUPERVISOR")));

        assertThrows(RuntimeException.class, () -> useCase.activate(100L));

        verify(userPublicApi, never()).changeUserActivationStatus(any(), anyBoolean());
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    void shouldPublishEventWithAllBranchIds() {
        CrBranch first = branch(20L);
        CrBranch second = branch(10L);
        ApprovalDelegation delegation = pending(first, second);
        when(delegationRepository.findByIdForUpdate(100L)).thenReturn(Optional.of(delegation));
        when(crBranchRepository.findAllByIdForUpdate(List.of(10L, 20L)))
                .thenReturn(List.of(second, first));

        useCase.activate(100L);

        ArgumentCaptor<ApprovalDelegationActivatedEvent> captor =
                ArgumentCaptor.forClass(ApprovalDelegationActivatedEvent.class);
        verify(eventPublisher).publishEvent(captor.capture());
        assertEquals(List.of(10L, 20L), captor.getValue().branchIds());
    }

    private ApprovalDelegation pending(CrBranch... branches) {
        ApprovalDelegation delegation = new ApprovalDelegation(
                1L, 2L, now().minusHours(1), now().plusHours(2), now().minusDays(1)
        );
        delegation.setId(100L);
        for (CrBranch branch : branches) {
            delegation.addBranch(branch);
        }
        return delegation;
    }

    private CrBranch branch(Long id) {
        CrBranch branch = new CrBranch();
        branch.setId(id);
        branch.setResponsibleUsers(new ArrayList<>());
        return branch;
    }

    private UserSummaryPublicResponse summary(Long id, boolean active, String role) {
        return new UserSummaryPublicResponse(id, "Usuário " + id, active, role);
    }

    private LocalDateTime now() {
        return LocalDateTime.ofInstant(NOW, ZoneOffset.UTC);
    }
}
