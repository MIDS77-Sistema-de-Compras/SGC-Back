package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.request;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.request.RequestMapper;
import org.springframework.stereotype.Service;

/**
 * Caso de uso responsável por buscar uma {@link Request} pelo seu identificador.
 */
@Service
@RequiredArgsConstructor
public class FindRequestByIdServiceImpl {

    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;

    /**
     * Busca uma solicitação no banco de dados pelo ID informado.
     * @param id identificador da solicitação.
     * @return solicitação encontrada, caso exista.
     * @throws RequestNotFoundException caso nenhuma solicitação seja encontrada.
     */
    public RequestResponse findRequestById(Long id) {

        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new RequestNotFoundException());
        return requestMapper.toDTO(request);
    }
}
