package net.centroweg.gerenciamentocompras.modules.request.service.status;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.domain.Status;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.StatusRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.StatusRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.StatusResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.IStatusMapper;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AddStatusService {

    private final IStatusMapper statusMapper;
    private final StatusRepository statusRepository;

    public StatusResponse addStatus (StatusRequest statusRequest) {
        Status status = statusRepository.save(statusMapper.toEntity(statusRequest));

        return statusMapper.toResponse(status);
    }
}
