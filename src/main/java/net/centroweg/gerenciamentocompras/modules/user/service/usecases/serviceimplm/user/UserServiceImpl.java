package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.user;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.CreateUser;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.UserResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceIntrf.UserIntrf;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;

/**
 * Classe de serviço do {@link User} que delega cada operação à sua respectiva classe de funcionalidade.
 * Implementa {@link UserIntrf} que segue o princípio de responsabilidade única, onde cada método apenas repassa a chamada para uma classe especializada responsável por uma única operação.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserIntrf {

    /**
     * Componente responsável pela criação de um usuário.
     */
    private final CreateUserImpl createUser;

    /**
     * Componente responsável por qualquer tipo de busca de usuário.
     */
    private final ListUserImpl listUser;

    /**
     * Componente responsável pela busca de um usuário pelo seu identificador.
     */
    private final FindUserByIdImpl findUserById;

    /**
     * Componente responsável pela busca de um usuário pelo seu nome.
     */
    private final FindUserByNameImpl findUserByName;

    /**
     * Componente responsável pela atualização de um usuário.
     */
    private final UpdateUserAllImpl updateUserAll;

    /**
     * Componente responsável por remover um usuário.
     */
    private final DeleteUserImpl deleteUser;

    /**
     * Componente responsável pela atualização da foto de perfil de um usuário.
     */
    private final UploadProfilePicture uploadProfilePicture;

    /**
     * Componente responsável pela busca do usuário autenticado.
     */
    private final FindLoggedUser findLoggedUser;

    /**
     * Cria e persiste um novo usuário no banco de dados.
     * @param user dados do usuário.
     * @return usuário criado.
     */
    @Override
    public UserResponse createUser(CreateUser user){
        return createUser.createUser(user);
    }

    /**
     * Lista todos os usuários cadastrados no banco de dados.
     * @return lista com todos os usuários encontrados, caso exista.
     */
    @Override
    public List<UserResponse> listUser(){
        return listUser.listUser();
    }

    /**
     * Busca um usuário no banco de dados pelo ID informado.
     * @param id identificador do usuário.
     * @return usuário encontrado, caso exista.
     */
    @Override
    public UserResponse findUserById(Long id){
        return findUserById.findUserById(id);
    }

    /**
     * Lista todos os usuários cadastrados no banco de dados pelo nome informado.
     * @param name nome do usuário.
     * @return lista com todos os usuários encontrados, caso exista.
     */
    @Override
    public List<UserResponse> findUserByName(String name){
        return findUserByName.findUserByName(name);
    }

    /**
     * Atualiza um usuário existente no banco de dados.
     * @param id identificador do usuário.
     * @param user novos dados do usuário.
     * @return usuário já atualizado.
     */
    @Override
    public UserResponse updateUserAll(Long id, CreateUser user){
        return updateUserAll.updateUserAll(id, user);
    }

    /**
     * Remove um usuário do banco de dados.
     * @param id identificador do usuário.
     */
    @Override
    public void deleteUser(Long id){
        deleteUser.deleteUser(id);
    }

    /**
     * Realiza o upload da foto de perfil de um usuário existente no banco de dados.
     * @param id identificador do usuário.
     * @param file arquivo da foto de perfil.
     * @return usuário já atualizado.
     * @throws IOException caso ocorra um erro durante o upload do arquivo.
     */
    @Override
    public UserResponse uploadProfilePicture(long id, MultipartFile file) throws IOException {
        return uploadProfilePicture.uploadProfilePicture(id, file);
    }

    /**
     * Busca o usuário autenticado no banco de dados.
     * @param userPrincipal dados do usuário autenticado.
     * @return usuário encontrado, caso exista.
     */
    @Override
    public UserResponse findLoggedUser(UserPrincipal userPrincipal) {
        return findLoggedUser.findLoggedUser(userPrincipal);
    }
}
