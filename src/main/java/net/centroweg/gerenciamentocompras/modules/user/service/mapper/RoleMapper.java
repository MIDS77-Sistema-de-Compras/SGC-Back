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
 * Classe responsável por transformar entidade para DTO e DTO para entidade
 * @see UserMapper
 */

@Component
@RequiredArgsConstructor
public class RoleMapper {

    private final UserMapper mapper;

    /**
     * Transforma DTO de request em entidade
     * @param role DTO de request
     * @return nova role
     * @see CreateRole
     * @see Role
     */
    public Role toEntity(CreateRole role){
        return new Role(role.name());
    }

    /**
     * Transforma entidade em DTO de response
     * @param role entidade
     * @return novo response DTO
     * @see RoleResponse
     * @see Role
     */
    public RoleResponse toDTO(Role role){
        return new RoleResponse(role.getId(), role.getName());
    }

    /**
     * Transforma lista de entidades em lista de response DTO
     * @param roles lista de entidade
     * @return nova lista de response DTO
     * @see Role
     * @see RoleResponse
     */
    public List<RoleResponse> toDTOList(List<Role> roles){
        return roles.stream()
                    .map(this::toDTO)
                    .toList();
    }

    /**
     * Tranforma role em DTO de atribuição de role a user
     * @param role role a ser atribuida a usuário
     * @return uma lista com os usuários que possuem determinada role
     * @see UserMapper
     * @see UserRole
     * @see Role
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
