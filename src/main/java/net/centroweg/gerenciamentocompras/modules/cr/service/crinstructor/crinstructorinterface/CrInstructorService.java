package net.centroweg.gerenciamentocompras.modules.cr.service.crinstructor.crinstructorinterface;

import java.util.List;

import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrInstructorRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrInstructorResponse;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;

public interface CrInstructorService {
    
    CrInstructorResponse create(CrInstructorRequest request);
    List<CrInstructorResponse> findAll();
    CrInstructorResponse findById(Long id);
    CrInstructorResponse update(Long id, CrInstructorRequest request);
    MessageDTO delete(Long id);
    
}
