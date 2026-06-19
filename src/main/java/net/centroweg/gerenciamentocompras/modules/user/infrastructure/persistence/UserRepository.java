package net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence;

import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repositório de acesso aos dados da entidade
 * @see User
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    /**
     * Consulta personalizada que busca o usuário por e-mail ou CPF.
     * @param email endereço de email do usuário
     * @param cpf cpf do usuário
     * @return Opcional o retorno de um usuário, só retorna se encontrar.
     */
    Optional<User> findByEmailOrCpf(String email, String cpf);

    /**
     * Consulta personalizada que busca usuário por nome mesmo não estando completo.
     * @param name nome do usuário
     * @return uma lista de usuário dos quais o nome correspondem a pesquisa.
     */
    List<User> findByNameIgnoringCase(String name);
    Boolean existsByEmail(String email);
    Boolean existsByCpf(String cpf);

}
