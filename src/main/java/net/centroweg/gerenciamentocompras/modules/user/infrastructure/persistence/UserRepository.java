package net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence;

import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repositório de acesso a dados da entidade {@link User}.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Busca um usuário pelo endereço de email.
     * @param email endereço de email do usuário.
     * @return o usuário encontrado, caso exista.
     */
    Optional<User> findByEmail(String email);

    /**
     * Busca um usuário pelo endereço de email ou pelo CPF.
     * @param email endereço de email do usuário.
     * @param cpf CPF do usuário.
     * @return o usuário encontrado, caso exista.
     */
    Optional<User> findByEmailOrCpf(String email, String cpf);

    /**
     * Lista todos os usuários cadastrados no banco de dados pelo nome, ignorando maiúsculas e minúsculas.
     * @param name nome do usuário.
     * @return lista com todos os usuários encontrados, caso exista.
     */
    List<User> findByNameIgnoringCase(String name);

    /**
     * Verifica se existe um usuário cadastrado com o endereço de email informado.
     * @param email endereço de email do usuário.
     * @return {@code true} caso exista um usuário com o endereço de email informado, {@code false} caso contrário.
     */
    Boolean existsByEmail(String email);

    /**
     * Verifica se existe um usuário cadastrado com o CPF informado.
     * @param cpf CPF do usuário.
     * @return {@code true} caso exista um usuário com o CPF informado, {@code false} caso contrário.
     */
    Boolean existsByCpf(String cpf);

}
