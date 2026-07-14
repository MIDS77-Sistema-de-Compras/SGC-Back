package net.centroweg.gerenciamentocompras.modules.auth.service.api;

import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import java.util.Optional;

/**
 * Interface de serviço que disponibiliza operações públicas relacionadas à autenticação e consulta de usuários.
 */
public interface AuthPublicApi {

    /**
     * Busca um usuário por e-mail ou CPF.
     * @param email e-mail do usuário a ser pesquisado.
     * @param cpf CPF do usuário a ser pesquisado.
     * @return se encontrado retorna o usuário, caso não encontrado retorna excessão.
     */
    Optional<User> findByEmailOrCpf(String email, String cpf);

    /**
     * Verifica se o usuário existe por e-mail e retona se existe ou não.
     * @param email e-mail do usuário a ser procurado.
     * @return booleano com o resultado da busca pelo usuário.
     */
    Boolean existsByEmail(String email);
}
