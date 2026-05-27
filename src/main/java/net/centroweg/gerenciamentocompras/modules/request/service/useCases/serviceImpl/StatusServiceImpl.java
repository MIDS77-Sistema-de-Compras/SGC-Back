package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.StatusRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.StatusResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceIntrf.StatusService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatusServiceImpl implements StatusService {

    private final AddStatusService addStatusService;
    private final ListStatusService listStatusService;
    private final FindStatusByIdService findStatusByIdService;
    private final EditStatusService editStatusService;
    private final DeleteStatusService deleteStatusService;

    @Override
    public StatusResponse createStatus(StatusRequest statusRequest) {
        return addStatusService.addStatus(statusRequest);
    }

    @Override
    public StatusResponse findStatusById(Long id) {
        return findStatusByIdService.findStatusById(id);
    }

    @Override
    public List<StatusResponse> findAllStatus() {
        return listStatusService.listStatus();
    }

    @Override
    public StatusResponse editStatus(Long id, StatusRequest statusRequest) {
        return editStatusService.editStatus(id, statusRequest);
    }

    @Override
    public void deleteStatus(Long id) {
        deleteStatusService.deleteStatus(id);
    }

}
