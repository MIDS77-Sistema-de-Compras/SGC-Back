package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.user;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.config.security.CpfHasher;
import net.centroweg.gerenciamentocompras.modules.user.domain.exception.UserAlreadyExistedException;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.CreateUser;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniquenessValidator {

    private final UserRepository userRepository;
    private final CpfHasher cpfHasher;

    public void checkInfo(CreateUser user){

        if(userRepository.existsByEmail(user.email())){
            throw new UserAlreadyExistedException("email");
        }

        if(userRepository.existsByCpf(cpfHasher.hash(user.cpf()))){
            throw new UserAlreadyExistedException("cpf");
        }
    }
}
