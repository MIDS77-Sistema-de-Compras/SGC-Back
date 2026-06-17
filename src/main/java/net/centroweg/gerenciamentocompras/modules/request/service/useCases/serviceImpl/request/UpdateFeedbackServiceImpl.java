package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.request;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.AcessDeniedException;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestNotEditableException;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestNotRefusedException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.UpdateFeedback;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.request.RequestMapper;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateFeedbackServiceImpl {

    private final RequestMapper mapper;
    private final RequestRepository repository;

    public RequestResponse updateFeedback(UpdateFeedback feedback, Long id){


        Request request = repository.findById(id)
                .orElseThrow(() -> new RequestNotFoundException());


        if (!request.getStatus().getName().equalsIgnoreCase("recusado")){
            throw new RequestNotRefusedException();
        }

        request.setFeedback(feedback.feedback());
        return mapper.toDTO(repository.save(request));
    }
}
