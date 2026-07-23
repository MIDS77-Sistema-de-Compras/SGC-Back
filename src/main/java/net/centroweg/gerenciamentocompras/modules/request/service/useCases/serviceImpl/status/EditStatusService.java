package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.status;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.StatusNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.StatusRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.StatusRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.StatusResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.status.IStatusMapper;
import org.springframework.stereotype.Service;

/**
 * Caso de uso responsável pela atualização de um {@link Status}.
 */
@Service
@RequiredArgsConstructor
public class EditStatusService {

    private final IStatusMapper statusMapper;
    private final StatusRepository statusRepository;

    /**
     * Atualiza um status existente no banco de dados.
     * @param id identificador do status.
     * @param statusRequest novos dados do status.
     * @return status já atualizado.
     * @throws StatusNotFoundException caso nenhum status seja encontrado.
     */
    public StatusResponse editStatus (Long id, StatusRequest statusRequest) {
        Status status = statusRepository.findById(id)
                .orElseThrow(StatusNotFoundException::new);

        status.setName(statusRequest.name());
        status.setDescription(statusRequest.description());

        Status updatedStatus = statusRepository.save(status);
        return statusMapper.toResponse(updatedStatus);
    }
}
