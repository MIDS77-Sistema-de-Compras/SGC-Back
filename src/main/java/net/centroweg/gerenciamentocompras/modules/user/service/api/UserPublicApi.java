package net.centroweg.gerenciamentocompras.modules.user.service.api;

import java.util.Optional;

import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.service.api.dto.UserNotificationData;
import net.centroweg.gerenciamentocompras.modules.user.service.api.dto.UserSummaryPublicResponse;

import java.util.Collection;
import java.util.List;

public interface UserPublicApi {

    Boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailOrCpf(String email, String cpf);
    List<UserNotificationData> findNotificationDataByIds(Collection<Long> userIds);
    Optional<User> findUserById(Long id);
    List<User> findUsersByIds(Collection<Long> ids);
    Optional<UserSummaryPublicResponse> findUserSummaryById(Long id);
    UserSummaryPublicResponse getAuthenticatedUserSummary();
    void changeUserActivationStatus(Long userId, boolean active);

}
