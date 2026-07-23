package net.centroweg.gerenciamentocompras.modules.user.service.api;

import java.util.Optional;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;

/**
 * Interface de API responsável por compartilhar métodos específicos do módulo {@link User}
 */
public interface UserPublicApi {

    /**
     * Verifica se existe um usuário cadastrado com o endereço de email informado.
     * @param email endereço de email do usuário.
     * @return {@code true} caso exista um usuário com o endereço de email informado, {@code false} caso contrário.
     */
    Boolean existsByEmail(String email);

    /**
     * Busca um usuário pelo endereço de email ou pelo CPF.
     * @param email endereço de email do usuário.
     * @param cpf CPF do usuário.
     * @return o usuário encontrado, caso exista.
     */
    Optional<User> findByEmailOrCpf(String email, String cpf);
    
}
