package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.domain.Status;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.StatusNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.StatusRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.StatusRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.StatusResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.IStatusMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EditStatusService {

    private final IStatusMapper statusMapper;
    private final StatusRepository statusRepository;

    public StatusResponse editStatus (Long id, StatusRequest statusRequest) {
        Status status = statusRepository.findById(id)
                .orElseThrow(StatusNotFoundException::new);

        status.setName(statusRequest.name());
        status.setDescription(statusRequest.description());

        Status updatedStatus = statusRepository.save(status);
        return statusMapper.toResponse(updatedStatus);
    }
}
