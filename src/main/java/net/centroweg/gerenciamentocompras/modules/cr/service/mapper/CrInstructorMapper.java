package net.centroweg.gerenciamentocompras.modules.cr.service.mapper;

import java.util.List;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.UserResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.mapper.UserMapper;
import org.springframework.stereotype.Component;

import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrInstructor;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrInstructorResponse;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;

@Component
@RequiredArgsConstructor
public class CrInstructorMapper {

    private final UserMapper userMapper;

    public CrInstructor toEntity(List<User> user, CrBranch crBranch){
        return new CrInstructor(user, crBranch);
    }

    public CrInstructorResponse toResponse(CrInstructor instructor){
        return new CrInstructorResponse(
                instructor.getId(),
                userMapper.toDTOList(instructor.getInstructors()),
                instructor.getCrBranch().getId()
        );
    }

    public List<CrInstructorResponse> toResponseList(List<CrInstructor> instructors){
        return instructors.stream().map(
                this::toResponse).toList();
    }

}
