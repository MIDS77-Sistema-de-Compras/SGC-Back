package net.centroweg.gerenciamentocompras.modules.request.service.usecases.serviceImpl.request;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.request.RequestMapper;
import net.centroweg.gerenciamentocompras.modules.request.service.validator.CompradorRequestAccessValidator;

/**
 * Marca uma solicitação como visualizada pelo comprador, na primeira vez que ele
 * abre o detalhe dela. Idempotente: chamadas repetidas não regravam o registro.
 */
@Service
@RequiredArgsConstructor
public class MarkRequestViewedByCompradorServiceImpl {

    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final CompradorRequestAccessValidator compradorRequestAccessValidator;

    @Transactional
    public RequestResponse markViewed(Long id) {
        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new RequestNotFoundException());

        compradorRequestAccessValidator.validate(request);

        if (!Boolean.TRUE.equals(request.getViewedByComprador())) {
            request.setViewedByComprador(true);
            request = requestRepository.save(request);
        }

        return requestMapper.toDTO(request);
    }
}
