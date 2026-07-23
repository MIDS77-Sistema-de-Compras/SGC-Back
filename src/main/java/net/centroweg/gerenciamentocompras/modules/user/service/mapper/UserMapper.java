package net.centroweg.gerenciamentocompras.modules.user.service.mapper;

import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.CreateUser;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.UserResponse;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * Componente responsável pela conversão entre a entidade({@link User}) e seus DTOs de entrada({@link CreateUser}) e saída({@link UserResponse}).
 */
@Component
public class UserMapper {

    /**
     * Converte um DTO de entrada do usuário em uma entidade usuário.
     * @param user dados do usuário.
     * @return dados convertidos para entidade.
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
     * Converte uma entidade usuário em um DTO de saída do usuário.
     * @param user entidade com os dados do usuário.
     * @return dados convertidos para DTO de saída.
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
     * Converte uma lista de entidades usuário em uma lista de DTOs de saída do usuário.
     * @param users lista de entidades com os dados do usuário.
     * @return dados convertido para uma lista de DTOs de saída.
     */
    public List<UserResponse> toDTOList(List<User> users){
        return users.stream()
                    .map(this::toDTO)
                    .toList();
    }
}
