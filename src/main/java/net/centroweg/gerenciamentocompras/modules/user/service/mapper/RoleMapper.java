package net.centroweg.gerenciamentocompras.modules.user.service.mapper;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.CreateRole;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.RoleResponse;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.UserResponse;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.UserRole;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RoleMapper {

    private final UserMapper mapper;

    public Role toEntity(CreateRole role){
        return new Role(role.name());
    }

    public RoleResponse toDTO(Role role){
        return new RoleResponse(role.getId(), role.getName());
    }

    public List<RoleResponse> toDTOList(List<Role> roles){
        return roles.stream()
                    .map(this::toDTO)
                    .toList();
    }

    public UserRole toDTOUserRole(Role role){
        List<UserResponse> users = role.getUsers()
                .stream()
                .map(mapper::toDTO)
                .toList();
        RoleResponse roles = toDTO(role);

        return new UserRole(users, roles);
    }
}
