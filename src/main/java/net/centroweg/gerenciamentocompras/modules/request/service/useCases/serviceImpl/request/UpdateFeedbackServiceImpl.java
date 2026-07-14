package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.request;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestNotRefusedException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.UpdateFeedback;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.request.RequestMapper;
import net.centroweg.gerenciamentocompras.modules.request.service.validator.CompradorRequestAccessValidator;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateFeedbackServiceImpl {

    private final RequestMapper mapper;
    private final RequestRepository repository;
    private final CompradorRequestAccessValidator compradorRequestAccessValidator;

    public RequestResponse updateFeedback(UpdateFeedback feedback, Long id){
        Request request = repository.findById(id)
                .orElseThrow(() -> new RequestNotFoundException());

        compradorRequestAccessValidator.validate(request);
        if (!request.getStatus().getName().equalsIgnoreCase("recusado")){
            throw new RequestNotRefusedException();
        }
        request.setFeedback(feedback.feedback());
        return mapper.toDTO(repository.save(request));
    }
}
