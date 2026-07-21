package net.centroweg.gerenciamentocompras.modules.user.service.api;

import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.modules.user.service.api.dto.UserSummaryPublicResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceImpl.user.ChangeUserActivationStatusImpl;
import net.centroweg.gerenciamentocompras.shared.security.CurrentUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserPublicApiImplTest {

    @Mock private UserRepository userRepository;
    @Mock private CurrentUserService currentUserService;
    @Mock private ChangeUserActivationStatusImpl changeUserActivationStatus;

    private UserPublicApiImpl publicApi;
    private User user;

    @BeforeEach
    void setUp() {
        publicApi = new UserPublicApiImpl(
                userRepository, currentUserService, changeUserActivationStatus
        );
        user = new User();
        user.setId(10L);
        user.setName("Supervisor");
        user.setActive(true);
        user.setCpf("cpf não exposto");
        user.setPassword("senha não exposta");
        user.setRole(new Role("SUPERVISOR"));
    }

    @Test
    void shouldExposeOnlyRequiredUserSummary() {
        when(userRepository.findById(10L)).thenReturn(Optional.of(user));

        Optional<UserSummaryPublicResponse> result = publicApi.findUserSummaryById(10L);

        assertThat(result).contains(new UserSummaryPublicResponse(
                10L, "Supervisor", true, "SUPERVISOR"
        ));
    }

    @Test
    void shouldObtainAuthenticatedUserThroughCurrentUserService() {
        when(currentUserService.getCurrentUser()).thenReturn(user);

        UserSummaryPublicResponse result = publicApi.getAuthenticatedUserSummary();

        assertThat(result.id()).isEqualTo(10L);
        verify(currentUserService).getCurrentUser();
    }

    @Test
    void shouldDelegateActivationChangeToExistingUseCase() {
        publicApi.changeUserActivationStatus(10L, false);

        verify(changeUserActivationStatus).changeActivationStatusFromPublicApi(10L, false);
        verifyNoInteractions(userRepository);
    }

    @Test
    void shouldReturnOnlyIdsOfActiveBuyers() {
        User firstBuyer = new User();
        firstBuyer.setId(20L);
        User secondBuyer = new User();
        secondBuyer.setId(21L);
        when(userRepository.findByRole_NameIgnoreCaseAndActiveTrueAndDeletedFalse("COMPRADOR"))
                .thenReturn(List.of(firstBuyer, secondBuyer));

        assertThat(publicApi.findActiveUserIdsByRole("COMPRADOR"))
                .containsExactly(20L, 21L);
    }
}
