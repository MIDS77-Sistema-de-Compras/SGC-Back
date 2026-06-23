package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.status;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.StatusNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.StatusRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.StatusResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.status.IStatusMapper;
import org.springframework.stereotype.Service;
/**
 * Serviço responsável pela busca de status por identificador.
 *
 * @author André
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class FindStatusByIdService {

    private final IStatusMapper statusMapper;
    private final StatusRepository statusRepository;
    /**
     * Busca um status pelo identificador.
     *
     * @param id identificador do status
     * @return status encontrado
     */
    public StatusResponse findStatusById (Long id) {
        Status status = statusRepository.findById(id)
                .orElseThrow(StatusNotFoundException::new);

        return statusMapper.toResponse(status);
    }

}
