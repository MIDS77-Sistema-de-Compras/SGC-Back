package net.centroweg.gerenciamentocompras.modules.cr.service.approvaldelegation;

import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.ApprovalDelegation;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.ApprovalDelegationStatus;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.*;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.ApprovalDelegationRepository;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrBranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CreateApprovalDelegationRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.ApprovalDelegationResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.approvaldelegationservice.functionality.*;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.ApprovalDelegationMapper;
import net.centroweg.gerenciamentocompras.modules.user.service.api.UserPublicApi;
import net.centroweg.gerenciamentocompras.modules.user.service.api.dto.UserSummaryPublicResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.*;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateApprovalDelegationTest {

    private static final Instant NOW = Instant.parse("2026-08-01T12:00:00Z");

    @Mock private ApprovalDelegationRepository delegationRepository;
    @Mock private CrBranchRepository crBranchRepository;
    @Mock private UserPublicApi userPublicApi;
    @Mock private ActivateApprovalDelegation activateApprovalDelegation;

    private CreateApprovalDelegation useCase;
    private UserSummaryPublicResponse delegator;
    private UserSummaryPublicResponse delegate;

    @BeforeEach
    void setUp() {
        Clock clock = Clock.fixed(NOW, ZoneOffset.UTC);
        useCase = new CreateApprovalDelegation(
                delegationRepository,
                crBranchRepository,
                userPublicApi,
                new ApprovalDelegationParticipantValidator(),
                activateApprovalDelegation,
                new ApprovalDelegationMapper(),
                clock
        );
        delegator = new UserSummaryPublicResponse(1L, "Titular", true, "SUPERVISOR");
        delegate = new UserSummaryPublicResponse(2L, "Substituto", true, "SUPERVISOR");
        when(userPublicApi.getAuthenticatedUserSummary()).thenReturn(delegator);
        lenient().when(userPublicApi.findUserSummaryById(2L)).thenReturn(Optional.of(delegate));
    }

    @Test
    void shouldCreatePendingDelegationWithAllBranches() {
        CrBranch first = branch(10L);
        CrBranch second = branch(20L);
        when(crBranchRepository.findAllByResponsibleUserId(1L)).thenReturn(List.of(first, second));
        when(delegationRepository.saveAndFlush(any())).thenAnswer(invocation -> {
            ApprovalDelegation delegation = invocation.getArgument(0);
            delegation.setId(100L);
            return delegation;
        });

        ApprovalDelegationResponse response = useCase.create(request(now().plusHours(1), now().plusDays(2)));

        assertEquals(ApprovalDelegationStatus.PENDING, response.status());
        assertEquals(1L, response.delegatorUserId());
        assertEquals(List.of(10L, 20L), response.crBranchIds());
        verify(userPublicApi).getAuthenticatedUserSummary();
        verify(crBranchRepository).findAllByResponsibleUserId(1L);
        verify(activateApprovalDelegation, never()).activate(anyLong());
    }

    @Test
    void shouldActivateImmediatelyWhenStartHasArrived() {
        CrBranch branch = branch(10L);
        when(crBranchRepository.findAllByResponsibleUserId(1L)).thenReturn(List.of(branch));
        when(delegationRepository.saveAndFlush(any())).thenAnswer(invocation -> {
            ApprovalDelegation delegation = invocation.getArgument(0);
            delegation.setId(100L);
            return delegation;
        });
        when(activateApprovalDelegation.activate(100L)).thenAnswer(invocation -> {
            ApprovalDelegation delegation = new ApprovalDelegation(1L, 2L, now(), now().plusDays(1), now());
            delegation.setId(100L);
            delegation.addBranch(branch);
            delegation.setStatus(ApprovalDelegationStatus.ACTIVE);
            delegation.setActivatedAt(now());
            return delegation;
        });

        ApprovalDelegationResponse response = useCase.create(request(now(), now().plusDays(1)));

        assertEquals(ApprovalDelegationStatus.ACTIVE, response.status());
        verify(activateApprovalDelegation).activate(100L);
    }

    @Test
    void shouldRejectSupervisorWithoutBranch() {
        when(crBranchRepository.findAllByResponsibleUserId(1L)).thenReturn(List.of());

        assertThrows(
                SupervisorWithoutCrBranchException.class,
                () -> useCase.create(request(now().plusHours(1), now().plusDays(1)))
        );
        verify(delegationRepository, never()).saveAndFlush(any());
    }

    @Test
    void shouldRejectMissingInactiveOrNonSupervisorDelegate() {
        when(userPublicApi.findUserSummaryById(2L)).thenReturn(Optional.empty());
        assertThrows(InvalidApprovalDelegationParticipantException.class,
                () -> useCase.create(request(now().plusHours(1), now().plusDays(1))));

        when(userPublicApi.findUserSummaryById(2L))
                .thenReturn(Optional.of(new UserSummaryPublicResponse(2L, "Inativo", false, "SUPERVISOR")));
        assertThrows(InvalidApprovalDelegationParticipantException.class,
                () -> useCase.create(request(now().plusHours(1), now().plusDays(1))));

        when(userPublicApi.findUserSummaryById(2L))
                .thenReturn(Optional.of(new UserSummaryPublicResponse(2L, "Docente", true, "DOCENTE")));
        assertThrows(InvalidApprovalDelegationParticipantException.class,
                () -> useCase.create(request(now().plusHours(1), now().plusDays(1))));
    }

    @Test
    void shouldRejectSelfDelegation() {
        assertThrows(
                InvalidApprovalDelegationParticipantException.class,
                () -> useCase.create(new CreateApprovalDelegationRequest(
                        1L, now().plusHours(1), now().plusDays(1)
                ))
        );
        verify(userPublicApi, never()).findUserSummaryById(anyLong());
    }

    @Test
    void shouldRejectInvalidOrExpiredPeriod() {
        assertThrows(InvalidApprovalDelegationPeriodException.class,
                () -> useCase.create(request(now().plusDays(2), now().plusDays(1))));
        assertThrows(InvalidApprovalDelegationPeriodException.class,
                () -> useCase.create(request(now(), now())));
        assertThrows(InvalidApprovalDelegationPeriodException.class,
                () -> useCase.create(request(now().minusDays(2), now().minusDays(1))));
    }

    @Test
    void shouldRejectOverlappingDelegation() {
        when(delegationRepository.existsOverlappingDelegation(eq(1L), anyCollection(), any(), any()))
                .thenReturn(true);

        assertThrows(
                ApprovalDelegationOverlapException.class,
                () -> useCase.create(request(now().plusHours(1), now().plusDays(1)))
        );
        verify(crBranchRepository, never()).findAllByResponsibleUserId(anyLong());
    }

    private CreateApprovalDelegationRequest request(LocalDateTime startAt, LocalDateTime endAt) {
        return new CreateApprovalDelegationRequest(2L, startAt, endAt);
    }

    private CrBranch branch(Long id) {
        CrBranch branch = new CrBranch();
        branch.setId(id);
        branch.setResponsibleUsers(new java.util.ArrayList<>());
        return branch;
    }

    private LocalDateTime now() {
        return LocalDateTime.ofInstant(NOW, ZoneOffset.UTC);
    }
}
