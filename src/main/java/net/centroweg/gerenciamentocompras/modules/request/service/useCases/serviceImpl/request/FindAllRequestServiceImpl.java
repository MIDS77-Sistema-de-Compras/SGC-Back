package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.request;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.request.RequestMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindAllRequestServiceImpl {

    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;

    public List<RequestResponse> findAllRequest() {
        return requestRepository.findAll()
                .stream()
                .map(requestMapper::toDTO)
                .toList();
    }
}
