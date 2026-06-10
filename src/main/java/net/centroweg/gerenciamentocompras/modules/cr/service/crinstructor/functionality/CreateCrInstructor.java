package net.centroweg.gerenciamentocompras.modules.cr.service.crinstructor.functionality;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.CrBranchNotFoundException;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.CrBranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.CrInstructorRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrInstructorRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrInstructorResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.CrInstructorMapper;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.domain.exception.UserNotFoundException;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;

@Service
@RequiredArgsConstructor
public class CreateCrInstructor {
    
    private final CrInstructorRepository crInstructorRepository;
    private final CrInstructorMapper crInstructorMapper;

    private final UserRepository userRepository;
    private final CrBranchRepository crBranchRepository;

    public CrInstructorResponse addCrInstructor(CrInstructorRequest request){
        User user = userRepository.findById(request.instructorId()).orElseThrow(() -> new UserNotFoundException(request.instructorId()));
        CrBranch crBranch = crBranchRepository.findById(request.crBranchId()).orElseThrow(() -> new CrBranchNotFoundException(request.crBranchId()));

        return crInstructorMapper.toResponse(
            crInstructorRepository.save(
                crInstructorMapper.toEntity(user, crBranch)
            )
        );
    }

}
