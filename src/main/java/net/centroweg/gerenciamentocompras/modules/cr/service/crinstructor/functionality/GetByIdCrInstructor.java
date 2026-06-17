package net.centroweg.gerenciamentocompras.modules.cr.service.crinstructor.functionality;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.CrInstructorNotFoundException;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrInstructorRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrInstructorResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.CrInstructorMapper;

@Service
@RequiredArgsConstructor
public class GetByIdCrInstructor {
    
    private final CrInstructorRepository crInstructorRepository;
    private final CrInstructorMapper crInstructorMapper;

    public CrInstructorResponse getById(Long id){
        return crInstructorMapper.toResponse(
            crInstructorRepository.findById(id).orElseThrow(() -> new CrInstructorNotFoundException("Nenhum supervisor vinculado à filial foi encontrado."))
        );
    }

}
