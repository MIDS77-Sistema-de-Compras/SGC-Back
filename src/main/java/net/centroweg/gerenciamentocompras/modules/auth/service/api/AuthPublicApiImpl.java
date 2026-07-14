package net.centroweg.gerenciamentocompras.modules.auth.service.api;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.product.domain.entity.MeasurementUnit;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Classe de serviço da {@link AuthPublicApi} e sobreescreve os query methods.
 */
@Repository
@RequiredArgsConstructor
public class AuthPublicApiImpl implements AuthPublicApi{

    private final UserRepository userRepository;

    /**
     * Procura usuário por e-mail ou CPF.
     * @param email e-mail do usuário a ser pesquisado.
     * @param cpf CPF do usuário a ser pesquisado.
     * @return resultado da busca no repositório com o usuário encontrado ou valor nulo.
     */
    @Override
    public Optional<User> findByEmailOrCpf(String email, String cpf) {
        return userRepository.findByEmailOrCpf(email, cpf);
    }

    /**
     * Verifica se o usuário existe.
     * @param email e-mail do usuário a ser procurado.
     * @return booleano, se o usuário foi encontrado ou não.
     */
    @Override 
    public Boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }
}
