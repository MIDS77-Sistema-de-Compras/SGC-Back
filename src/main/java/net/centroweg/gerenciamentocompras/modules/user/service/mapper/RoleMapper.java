package net.centroweg.gerenciamentocompras.modules.user.service.mapper;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.CreateRole;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.RoleResponse;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.UserResponse;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.UserRole;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * Componente responsável pela conversão entre a entidade({@link Role}) e seus DTOs de entrada({@link CreateRole}) e saída({@link RoleResponse}).
 */
@Component
@RequiredArgsConstructor
public class RoleMapper {

    private final UserMapper mapper;

    /**
     * Converte um DTO de entrada do nível de acesso em uma entidade nível de acesso.
     * @param role dados do nível de acesso.
     * @return dados convertidos para entidade.
     */
    public Role toEntity(CreateRole role){
        return new Role(role.name());
    }

    /**
     * Converte uma entidade nível de acesso em um DTO de saída do nível de acesso.
     * @param role entidade com os dados do nível de acesso.
     * @return dados convertidos para DTO de saída.
     */
    public RoleResponse toDTO(Role role){
        return new RoleResponse(role.getId(), role.getName());
    }

    /**
     * Converte uma lista de entidades nível de acesso em uma lista de DTOs de saída do nível de acesso.
     * @param roles lista de entidades com os dados do nível de acesso.
     * @return dados convertido para uma lista de DTOs de saída.
     */
    public List<RoleResponse> toDTOList(List<Role> roles){
        return roles.stream()
                    .map(this::toDTO)
                    .toList();
    }

    /**
     * Converte uma entidade nível de acesso em um DTO de saída do nível de acesso com seus usuários.
     * @param role entidade com os dados do nível de acesso.
     * @return dados convertidos para DTO de saída.
     */
    public UserRole toDTOUserRole(Role role){
        List<UserResponse> users = role.getUsers()
                .stream()
                .map(mapper::toDTO)
                .toList();
        RoleResponse roles = toDTO(role);

        return new UserRole(users, roles);
    }
}
