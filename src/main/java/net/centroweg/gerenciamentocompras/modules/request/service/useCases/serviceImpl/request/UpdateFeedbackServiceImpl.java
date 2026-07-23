package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.request;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestNotRefusedException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.UpdateFeedback;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.request.RequestMapper;
import org.springframework.stereotype.Service;

/**
 * Caso de uso responsável pela atualização do feedback de uma {@link Request}.
 */
@Service
@RequiredArgsConstructor
public class UpdateFeedbackServiceImpl {

    private final RequestMapper mapper;
    private final RequestRepository repository;

    /**
     * Atualiza o feedback de uma solicitação existente no banco de dados.
     * @param feedback novos dados de feedback da solicitação.
     * @param id identificador da solicitação.
     * @return solicitação já atualizada.
     * @throws RequestNotFoundException caso nenhuma solicitação seja encontrada.
     * @throws RequestNotRefusedException caso a solicitação não esteja recusada.
     */
    public RequestResponse updateFeedback(UpdateFeedback feedback, Long id){
        Request request = repository.findById(id)
                .orElseThrow(() -> new RequestNotFoundException());
        if (!request.getStatus().getName().equalsIgnoreCase("Recusado")){
            throw new RequestNotRefusedException();
        }
        request.setFeedback(feedback.feedback());
        return mapper.toDTO(repository.save(request));
    }
}
