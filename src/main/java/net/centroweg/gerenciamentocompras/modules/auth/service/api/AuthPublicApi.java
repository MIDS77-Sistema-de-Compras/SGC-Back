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
     * @return usuário encontrado, caso exista.
     */
    Optional<User> findByEmailOrCpf(String email, String cpf);

    /**
     * Verifica se o usuário existe por e-mail.
     * @param email e-mail do usuário.
     * @return booleano informando se existe ou não.
     */
    Boolean existsByEmail(String email);
}
