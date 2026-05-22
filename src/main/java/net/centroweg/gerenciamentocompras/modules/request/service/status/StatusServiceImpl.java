package net.centroweg.gerenciamentocompras.modules.request.service.status;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.StatusRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.StatusResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatusServiceImpl implements StatusService {

    private final AddStatusService addStatusService;

    @Override
    public StatusResponse createStatus(StatusRequest statusRequest) {
        return addStatusService.addStatus(statusRequest);
    }
}
