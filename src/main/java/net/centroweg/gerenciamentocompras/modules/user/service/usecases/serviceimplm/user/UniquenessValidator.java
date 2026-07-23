package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.user;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.config.security.CpfHasher;
import net.centroweg.gerenciamentocompras.modules.user.domain.exception.UserAlreadyExistedException;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.CreateUser;
import org.springframework.stereotype.Component;

/**
 * Componente responsável pela validação de unicidade dos dados de um {@link User}.
 */
@Component
@RequiredArgsConstructor
public class UniquenessValidator {

    private final UserRepository userRepository;
    private final CpfHasher cpfHasher;

    /**
     * Valida se o endereço de email e o CPF informados já estão cadastrados para outro usuário.
     * @param user dados do usuário.
     * @throws UserAlreadyExistedException caso já exista um usuário com o endereço de email ou o CPF informados.
     */
    public void checkInfo(CreateUser user){

        if(userRepository.existsByEmail(user.email())){
            throw new UserAlreadyExistedException("email");
        }

        if(userRepository.existsByCpf(cpfHasher.hash(user.cpf()))){
            throw new UserAlreadyExistedException("cpf");
        }
    }
}
