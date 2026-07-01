package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.user;

import java.io.IOException;
import java.util.List;

import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.ChangePassword;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.CreateUser;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.UserResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceIntrf.UserIntrf;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Classe que implementa todos os serviços do usuário
 */

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserIntrf {

    /**
     * Injeção de dependências
     */

    private final CreateUserImpl createUser;
    private final ListUserImpl listUser;
    private final FindUserByIdImpl findUserById;
    private final FindUserByNameImpl findUserByName;
    private final UpdateUserAllImpl updateUserAll;
    private final DeleteUserImpl deleteUser;
    private final UploadProfilePicture uploadProfilePicture;
    private final FindLoggedUser findLoggedUser;
    private final ChangeUserPwdImpl changeUserPwd;

    /**
     * Implementa inteface de criação do usuário
     * @param user DTO vindo com os dados da requisição
     * @return usuário já cadastrado
     */
    @Override
    public UserResponse createUser(CreateUser user){
        return createUser.createUser(user);
    }

    /**
     * Implementa interface para listar todos os usuários
     * @return lista de usuários
     */
    @Override
    public Page<UserResponse> listUser(Pageable pageable){
        return listUser.listUser(pageable);
    }

    /**
     * Implementa interface para encontrar usuário pelo identificador único
     * @param id identificador único do usuário
     * @return usuário encontrado
     */
    @Override
    public UserResponse findUserById(Long id){
        return findUserById.findUserById(id);
    }

    /**
     * Implementa interface para encontrar usuário pelo nome
     * @param name nome do usuário
     * @return usuário encontrado
     */
    @Override
    public List<UserResponse> findUserByName(String name){
        return findUserByName.findUserByName(name);
    }

    /**
     * Implementa interface de atualizção do usuário
     * @param id identificador único do usuário
     * @param user nome do usuário
     * @return usuário já atualizado
     */
    @Override
    public UserResponse updateUserAll(Long id, CreateUser user){
        return updateUserAll.updateUserAll(id, user);
    }

    /**
     * Implementa interface para deletar usuário
     * @param id identificador único do usuário
     */
    @Override
    public void deleteUser(Long id){
        deleteUser.deleteUser(id);
    }

    @Override
    public UserResponse uploadProfilePicture(long id, MultipartFile file) throws IOException {
        return uploadProfilePicture.uploadProfilePicture(id, file);
    }

    @Override
    public UserResponse findLoggedUser(UserPrincipal userPrincipal) {
        return findLoggedUser.findLoggedUser(userPrincipal);
    }

    @Override
    public MessageDTO updatePwd(Long id, ChangePassword changePasswordDTO) {
        return changeUserPwd.changePassword(id, changePasswordDTO);
    }
}
