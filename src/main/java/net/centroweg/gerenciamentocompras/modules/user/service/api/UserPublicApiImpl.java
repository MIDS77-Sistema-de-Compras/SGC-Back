package net.centroweg.gerenciamentocompras.modules.user.service.api;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.modules.user.service.api.dto.UserNotificationData;
import net.centroweg.gerenciamentocompras.modules.user.service.api.dto.UserSummaryPublicResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceImpl.user.ChangeUserActivationStatusImpl;
import net.centroweg.gerenciamentocompras.shared.security.CurrentUserService;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserPublicApiImpl implements UserPublicApi {

    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;
    private final ChangeUserActivationStatusImpl changeUserActivationStatus;

    @Override
    public Set<Long> findUserIdsWithEmailNotificationsDisabled(Collection<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) return Set.of();
        return userRepository.findAllByIdInAndEmailNotificationsEnabledFalse(userIds).stream()
                .map(User::getId)
                .collect(Collectors.toSet());
    }

    @Override
    public Boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    @Override
    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByEmailOrCpf(String email, String cpf){
        return userRepository.findByEmailOrCpf(email, cpf);
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> findUsersByIds(Collection<Long> ids) {
        return userRepository.findAllById(ids);
    }

    @Override
    public Optional<UserSummaryPublicResponse> findUserSummaryById(Long id) {
        return userRepository.findById(id).map(this::toSummary);
    }

    @Override
    public UserSummaryPublicResponse getAuthenticatedUserSummary() {
        return toSummary(currentUserService.getCurrentUser());
    }

    @Override
    public void changeUserActivationStatus(Long userId, boolean active) {
        changeUserActivationStatus.changeActivationStatusFromPublicApi(userId, active);
    }

    @Override
    public List<UserNotificationData> findNotificationDataByIds(Collection<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return List.of();
        }
        return userRepository.findAllById(userIds).stream()
                .map(user -> new UserNotificationData(user.getId(), user.getName(), user.getEmail()))
                .toList();
    }

    private UserSummaryPublicResponse toSummary(User user) {
        String role = user.getRole() == null ? null : user.getRole().getName();
        return new UserSummaryPublicResponse(
                user.getId(),
                user.getName(),
                Boolean.TRUE.equals(user.getActive()),
                role
        );
    }

}