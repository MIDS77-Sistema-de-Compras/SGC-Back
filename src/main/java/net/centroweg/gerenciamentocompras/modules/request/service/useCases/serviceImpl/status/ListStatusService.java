package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.status;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.StatusRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.StatusResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.status.IStatusMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListStatusService {

    private final StatusRepository statusRepository;
    private final IStatusMapper statusMapper;

    public List<StatusResponse> listStatus () {
        List<Status> list = statusRepository.findAll();
        return list
                .stream()
                .map(statusMapper::toResponse)
                .toList();
    }
}
