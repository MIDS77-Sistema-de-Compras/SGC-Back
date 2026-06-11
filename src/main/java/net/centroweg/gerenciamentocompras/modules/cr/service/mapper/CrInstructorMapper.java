package net.centroweg.gerenciamentocompras.modules.cr.service.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import net.centroweg.gerenciamentocompras.modules.cr.domain.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.CrInstructor;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrInstructorResponse;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;

@Component
public class CrInstructorMapper {
    
    public CrInstructor toEntity(User user, CrBranch crBranch){
        return new CrInstructor(user, crBranch);
    }

    public CrInstructorResponse toResponse(CrInstructor instructor){
        return new CrInstructorResponse(instructor.getId(), instructor.getInstructor(), instructor.getCrBranch().getId());
    }

    public List<CrInstructorResponse> toResponseList(List<CrInstructor> instructors){
        return instructors.stream().map(instructor -> new CrInstructorMapper().toResponse(instructor)).toList();
    }

}
