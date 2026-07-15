package net.centroweg.gerenciamentocompras.modules.user.service.api;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.modules.user.service.api.dto.UserNotificationData;

import java.util.Collection;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserPublicApiImpl implements UserPublicApi {
    
    private final UserRepository userRepository;

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
    public List<UserNotificationData> findNotificationDataByIds(Collection<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return List.of();
        }
        return userRepository.findAllById(userIds).stream()
                .map(user -> new UserNotificationData(user.getId(), user.getName(), user.getEmail()))
                .toList();
    }

}
