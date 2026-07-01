package net.centroweg.gerenciamentocompras.modules.cr.service.crinstructor.functionality;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrInstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.CrBranchNotFoundException;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.CrInstructorNotFoundException;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrBranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrInstructorRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrInstructorRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrInstructorResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.CrInstructorMapper;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;

@Service
@RequiredArgsConstructor
public class UpdateCrInstructor {
    
    private final CrInstructorRepository crInstructorRepository;
    private final CrInstructorMapper crInstructorMapper;

    private final UserRepository userRepository;
    private final CrBranchRepository crBranchRepository;

    public CrInstructorResponse update(Long id, CrInstructorRequest request){
        CrInstructor crInstructor = crInstructorRepository.findById(id).orElseThrow(
            () -> new CrInstructorNotFoundException("Nenhum supervisor vinculado à filial foi encontrado."));

        crInstructor.setCrBranch(
            crBranchRepository.findById(request.crBranchId()).orElseThrow(() -> new CrBranchNotFoundException(request.crBranchId()))
        );

        crInstructor.setInstructors(
            userRepository.findAllById(request.instructorIds())
        );

        return crInstructorMapper.toResponse(crInstructorRepository.save(crInstructor));
    }

}
