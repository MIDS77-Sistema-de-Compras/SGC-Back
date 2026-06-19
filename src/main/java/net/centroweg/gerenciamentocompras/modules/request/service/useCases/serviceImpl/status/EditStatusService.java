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
 * Serviço responsável pela atualização de status.
 *
 * @author André
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class EditStatusService {

    private final IStatusMapper statusMapper;
    private final StatusRepository statusRepository;
    /**
     * Atualiza os dados de um status existente.
     *
     * @param id identificador do status
     * @param statusRequest novos dados do status
     * @return status atualizado
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
