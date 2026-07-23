package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceIntrf;

import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.CreateUser;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.UserResponse;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;

/**
 * Interface de serviço para operações de gerenciamento do {@link User}.
 */
public interface UserIntrf {

    /**
     * Cria e persiste um novo usuário no banco de dados.
     * @param user dados do usuário.
     * @return usuário criado.
     */
    UserResponse createUser(CreateUser user);

    /**
     * Lista todos os usuários cadastrados no banco de dados.
     * @return lista com todos os usuários encontrados, caso exista.
     */
    List<UserResponse> listUser();

    /**
     * Busca um usuário no banco de dados pelo ID informado.
     * @param id identificador do usuário.
     * @return usuário encontrado, caso exista.
     */
    UserResponse findUserById(Long id);

    /**
     * Lista todos os usuários cadastrados no banco de dados pelo nome informado.
     * @param name nome do usuário.
     * @return lista com todos os usuários encontrados, caso exista.
     */
    List<UserResponse> findUserByName(String name);

    /**
     * Atualiza um usuário existente no banco de dados.
     * @param id identificador do usuário.
     * @param user novos dados do usuário.
     * @return usuário já atualizado.
     */
    UserResponse updateUserAll(Long id, CreateUser user);

    /**
     * Remove um usuário do banco de dados.
     * @param id identificador do usuário.
     */
    void deleteUser(Long id);

    /**
     * Realiza o upload da foto de perfil de um usuário existente no banco de dados.
     * @param id identificador do usuário.
     * @param file arquivo da foto de perfil.
     * @return usuário já atualizado.
     * @throws IOException caso ocorra um erro durante o upload do arquivo.
     */
    UserResponse uploadProfilePicture(long id, MultipartFile file) throws IOException;

    /**
     * Busca o usuário autenticado no banco de dados.
     * @param userPrincipal dados do usuário autenticado.
     * @return usuário encontrado, caso exista.
     */
    UserResponse findLoggedUser(UserPrincipal userPrincipal);
}
