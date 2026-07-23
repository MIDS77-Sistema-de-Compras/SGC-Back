package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.status;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.StatusNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.StatusRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.StatusResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.status.IStatusMapper;
import org.springframework.stereotype.Service;

/**
 * Caso de uso responsável por buscar um {@link Status} pelo seu identificador.
 */
@Service
@RequiredArgsConstructor
public class FindStatusByIdService {

    private final IStatusMapper statusMapper;
    private final StatusRepository statusRepository;

    /**
     * Busca um status no banco de dados pelo ID informado.
     * @param id identificador do status.
     * @return status encontrado, caso exista.
     * @throws StatusNotFoundException caso nenhum status seja encontrado.
     */
    public StatusResponse findStatusById (Long id) {
        Status status = statusRepository.findById(id)
                .orElseThrow(StatusNotFoundException::new);

        return statusMapper.toResponse(status);
    }

}
