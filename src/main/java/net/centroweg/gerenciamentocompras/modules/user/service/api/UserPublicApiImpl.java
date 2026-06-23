package net.centroweg.gerenciamentocompras.modules.user.service.api;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;

@Repository
@RequiredArgsConstructor
public class UserPublicApiImpl implements UserPublicApi {
    
    private final UserRepository userRepository;

    @Override
    public Boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }
    
    @Override
    public Optional<User> findByEmailOrCpf(String email, String cpf){
        return userRepository.findByEmailOrCpf(email, cpf);
    }

}
