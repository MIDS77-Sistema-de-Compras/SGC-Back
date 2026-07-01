package net.centroweg.gerenciamentocompras.modules.cr.service.crinstructor.functionality;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<CrInstructorResponse> getAll(Pageable pageable){
        return crInstructorRepository.findAll(pageable).map(crInstructorMapper::toResponse);
    }

}
