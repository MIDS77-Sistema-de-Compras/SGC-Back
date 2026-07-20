package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceIntrf;

import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.ChangeUserActivationStatus;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.ChangePassword;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.CreateUser;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.UpdateUser;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.UserResponse;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Contrato de serviço para gerenciamento de usuários.
 */

public interface UserIntrf {

    /**
     * Cria um novo usuário.
     * @param user DTO vindo com os dados da requisição
     * @return um DTO depois de ter criado o usuário
     */
    UserResponse createUser(CreateUser user);
    /**
     * Lista todos os usuários.
     * @return uma lista com todos os usuários cadastrados no sistema.
     */
    Page<UserResponse> listUser(Pageable pageable);
    /**
     * Busca usuário pelo identificador único dele.
     * @param id identificador único do usuário
     * @return um DTO com o usuário encontrado
     */
    UserResponse findUserById(Long id);
    /**
     * Busca usuário pelo nome dele.
     * @param name nome do usuário
     * @return um DTO com os usuários encontrados
     */
    List<UserResponse> findUserByName(String name);
    /**
     * Atualiza usuário que tem o identificador único informado.
     * @param id identificador único do usuário
     * @param user nome do usuário
     * @return um DTO com o usuário já atualizado
     */
    UserResponse updateUserAll(Long id, UpdateUser user);
    /**
     * Deleta usuário que tem o identificador único informado.
     * @param id identificador único do usuário
     * @return vazio/nulo
     */
    void deleteUser(Long id);
    /**
     * Altera o estado de ativação do usuário informado.
     * @param userId identificador único do usuário
     * @param request novo estado de ativação
     * @return usuário com o estado atualizado
     */
    UserResponse changeActivationStatus(Long userId, ChangeUserActivationStatus request);
    UserResponse uploadProfilePicture(long id, MultipartFile file) throws IOException;
    UserResponse uploadLoggedUserProfilePicture(UserPrincipal userPrincipal, MultipartFile file) throws IOException;
    UserResponse findLoggedUser(UserPrincipal userPrincipal);

    MessageDTO updatePwd(Long id, ChangePassword changePasswordDTO);
}
