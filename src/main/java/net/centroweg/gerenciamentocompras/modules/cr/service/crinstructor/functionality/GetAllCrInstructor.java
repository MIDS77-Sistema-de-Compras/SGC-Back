package net.centroweg.gerenciamentocompras.modules.cr.service.crinstructor.functionality;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrInstructorRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrInstructorResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.CrInstructorMapper;

@Service
@RequiredArgsConstructor
public class GetAllCrInstructor {
    
    private final CrInstructorRepository crInstructorRepository;
    private final CrInstructorMapper crInstructorMapper;

    public List<CrInstructorResponse> getAll(){
        return crInstructorMapper.toResponseList(crInstructorRepository.findAll());
    }

}
