package net.centroweg.gerenciamentocompras.modules.request.service.usecases.serviceImpl.request;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.request.RequestMapper;
import net.centroweg.gerenciamentocompras.modules.request.service.validator.CompradorRequestAccessValidator;

@Service
@RequiredArgsConstructor
public class FindRequestByIdServiceImpl {

    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final CompradorRequestAccessValidator compradorRequestAccessValidator;

    public RequestResponse findRequestById(Long id) {

        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new RequestNotFoundException());

        compradorRequestAccessValidator.validate(request);

        return requestMapper.toDTO(request);
    }
}
