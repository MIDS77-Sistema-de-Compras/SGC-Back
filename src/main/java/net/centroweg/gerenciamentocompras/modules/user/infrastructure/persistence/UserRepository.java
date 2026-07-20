package net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;

/**
 * Repositório de acesso aos dados da entidade
 * @see User
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    /**
     * Consulta personalizada que busca o usuário por e-mail ou CPF.
     * Carrega a role junto (JOIN) porque este método é usado pelo filtro de
     * segurança a cada requisição — evita um select extra por request.
     * @param email endereço de email do usuário
     * @param cpf cpf do usuário
     * @return Opcional o retorno de um usuário, só retorna se encontrar.
     */
    @EntityGraph(attributePaths = "role")
    Optional<User> findByEmailOrCpf(String email, String cpf);

    /**
     * Consulta personalizada que busca usuário por nome mesmo não estando completo.
     * @param name nome do usuário
     * @return uma lista de usuário dos quais o nome correspondem a pesquisa.
     */
    List<User> findByNameIgnoringCase(String name);
    Boolean existsByEmail(String email);
    Boolean existsByCpf(String cpf);
    Optional<User> findByName(String name);

    /**
     * Busca paginada de usuários que não foram excluídos.
     * Usuários com atividade temporariamente desativada (ex: férias) são
     * incluídos aqui; apenas usuários marcados como excluídos são omitidos.
     * @param pageable configuração de paginação
     * @return página de usuários não excluídos
     */
    Page<User> findByDeletedFalse(Pageable pageable);

}