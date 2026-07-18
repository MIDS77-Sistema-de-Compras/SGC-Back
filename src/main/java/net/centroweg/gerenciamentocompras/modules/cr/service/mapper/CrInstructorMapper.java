package net.centroweg.gerenciamentocompras.modules.cr.service.mapper;

import java.util.List;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.service.mapper.UserMapper;
import org.springframework.stereotype.Component;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrInstructor;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrInstructorResponse;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrInstructorRequest;

/**
 * Componente responsável pela conversão entre a entidade({@link CrInstructor}) e seus DTOs de entrada({@link CrInstructorRequest}) e saída({@link CrInstructorResponse}).
 */
@Component
@RequiredArgsConstructor
public class CrInstructorMapper {

    private final UserMapper userMapper;

    /**
     * Converte um DTO de entrada do CR-instrutor em uma entidade CR-instrutor.
     * @param user usuário.
     * @param crBranch dados da CR-filial.
     * @return dados convertidos para entidade.
     */
    public CrInstructor toEntity(List<User> user, CrBranch crBranch){
        return new CrInstructor(user, crBranch);
    }

    /**
     * Converte uma entidade CR-instrutor em um DTO de saída do CR-instrutor.
     * @param instructor entidade com os dados.
     * @return dados convertidos para DTO de saída.
     */
    public CrInstructorResponse toResponse(CrInstructor instructor){
        return new CrInstructorResponse(
                instructor.getId(),
                userMapper.toDTOList(instructor.getInstructors()),
                instructor.getCrBranch().getId()
        );
    }

    /**
     * Converte uma lista de entidades CR-instrutor em uma lista de DTOs de saída do CR-instrutor.
     * @param instructors lista de entidades com os dados do CR-instrutor.
     * @return dados convertidos para uma lista de DTOs de saída.
     */
    public List<CrInstructorResponse> toResponseList(List<CrInstructor> instructors){
        return instructors.stream().map(
                this::toResponse).toList();
    }

}
