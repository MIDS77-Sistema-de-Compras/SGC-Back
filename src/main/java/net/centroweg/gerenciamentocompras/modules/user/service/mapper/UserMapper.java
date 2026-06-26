package net.centroweg.gerenciamentocompras.modules.user.service.mapper;

import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.CreateUser;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.UserResponse;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Classe responsável pela conversão entr entidade e DTO.
 */

@Component
public class UserMapper {

    /**
     * Converte a requisição em entidade.
     * @param user requisição da API
     * @return usuário entidade com dados convertidos
     */

    public User toEntity(CreateUser user){
        return new User(
                user.name(),
                user.cpf(),
                user.email(),
                user.password(),
                user.extensionNumber(),
                user.active()
        );
    }

    /**
     * Converte a entidade em requisição.
     * @param user entidade que vai se transformar em resposta
     * @return usuário com dados convertidos para DTO
     */

    public UserResponse toDTO(User user){
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getCpf(),
                user.getEmail(),
                user.getExtensionNumber(),
                user.getActive(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getProfilePicture());
    }

    /**
     * Converte a entidade em lista.
     * @param users entidade que vai se transformar em uma lista de resposta
     * @return lista de usuários com dados convertidos para DTO
     */

    public List<UserResponse> toDTOList(List<User> users){
        return users.stream()
                    .map(this::toDTO)
                    .toList();
    }
}
