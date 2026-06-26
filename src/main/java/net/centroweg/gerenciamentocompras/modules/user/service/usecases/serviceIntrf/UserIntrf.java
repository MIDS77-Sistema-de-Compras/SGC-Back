package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceIntrf;

import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.CreateUser;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.UserResponse;
import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties;
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
    List<UserResponse> listUser();
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
    UserResponse updateUserAll(Long id, CreateUser user);
    /**
     * Deleta usuário que tem o identificador único informado.
     * @param id identificador único do usuário
     * @return vazio/nulo
     */
    void deleteUser(Long id);
    UserResponse uploadProfilePicture(long id, MultipartFile file) throws IOException;
    UserResponse findLoggedUser(UserPrincipal userPrincipal);
}
