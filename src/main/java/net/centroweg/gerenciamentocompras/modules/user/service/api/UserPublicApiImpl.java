package net.centroweg.gerenciamentocompras.modules.user.service.api;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;

/**
 * A classe responsável por implementar os métodos da interface {@link UserPublicApi}
 */
@Repository
@RequiredArgsConstructor
public class UserPublicApiImpl implements UserPublicApi {
    
    private final UserRepository userRepository;

    /**
     * Verifica se existe um usuário cadastrado com o endereço de email informado.
     * @param email endereço de email do usuário.
     * @return {@code true} caso exista um usuário com o endereço de email informado, {@code false} caso contrário.
     */
    @Override
    public Boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    /**
     * Busca um usuário pelo endereço de email ou pelo CPF.
     * @param email endereço de email do usuário.
     * @param cpf CPF do usuário.
     * @return o usuário encontrado, caso exista.
     */
    @Override
    public Optional<User> findByEmailOrCpf(String email, String cpf){
        return userRepository.findByEmailOrCpf(email, cpf);
    }

}
