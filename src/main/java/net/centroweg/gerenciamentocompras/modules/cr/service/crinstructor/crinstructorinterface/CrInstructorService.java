package net.centroweg.gerenciamentocompras.modules.cr.service.crinstructor.crinstructorinterface;

import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrInstructorRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrInstructorResponse;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CrInstructorService {
    
    CrInstructorResponse create(CrInstructorRequest request);
    Page<CrInstructorResponse> findAll(Pageable pageable);
    CrInstructorResponse findById(Long id);
    CrInstructorResponse update(Long id, CrInstructorRequest request);
    MessageDTO delete(Long id);
    
}
