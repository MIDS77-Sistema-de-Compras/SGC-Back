package net.centroweg.gerenciamentocompras.modules.cr.service.crinstructor.crinstructorimpl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrInstructorRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrInstructorResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.crinstructor.crinstructorinterface.CrInstructorService;
import net.centroweg.gerenciamentocompras.modules.cr.service.crinstructor.functionality.CreateCrInstructor;
import net.centroweg.gerenciamentocompras.modules.cr.service.crinstructor.functionality.DeleteCrInstructor;
import net.centroweg.gerenciamentocompras.modules.cr.service.crinstructor.functionality.GetAllCrInstructor;
import net.centroweg.gerenciamentocompras.modules.cr.service.crinstructor.functionality.GetByIdCrInstructor;
import net.centroweg.gerenciamentocompras.modules.cr.service.crinstructor.functionality.UpdateCrInstructor;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;

@Service
@RequiredArgsConstructor
public class CrInstructorServiceImpl implements CrInstructorService {

    private final CreateCrInstructor createCrInstructor;
    private final GetAllCrInstructor getAllCrInstructor;
    private final GetByIdCrInstructor getByIdCrInstructor;
    private final UpdateCrInstructor updateCrInstructor;
    private final DeleteCrInstructor deleteCrInstructor;

    @Override
    public CrInstructorResponse create(CrInstructorRequest request) {
        return createCrInstructor.addCrInstructor(request);
    }

    @Override
    public Page<CrInstructorResponse> findAll(Pageable pageable) {
        return getAllCrInstructor.getAll(pageable);
    }

    @Override
    public CrInstructorResponse findById(Long id) {
        return getByIdCrInstructor.getById(id);
    }

    @Override
    public CrInstructorResponse update(Long id, CrInstructorRequest request) {
        return updateCrInstructor.update(id, request);
    }

    @Override
    public MessageDTO delete(Long id) {
        return deleteCrInstructor.delete(id);
    }
    
}
