package net.centroweg.gerenciamentocompras.modules.user.service.mapper;

import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.CreateUser;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.UserResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

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

    public UserResponse toDTO(User user){
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getCpf(),
                user.getEmail(),
                user.getExtensionNumber(),
                user.isActive(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getProfilePicture());
    }

    public List<UserResponse> toDTOList(List<User> users){
        return users.stream()
                    .map(this::toDTO)
                    .toList();
    }
}
