package net.centroweg.gerenciamentocompras.modules.auth.service.api;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AuthPublicApiImpl implements AuthPublicApi{

    private final UserRepository userRepository;

    @Override
    public Optional<User> findByEmailOrCpf(String email, String cpf) {
        return userRepository.findByEmailOrCpf(email, cpf);
    }

    @Override 
    public Boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }
}
