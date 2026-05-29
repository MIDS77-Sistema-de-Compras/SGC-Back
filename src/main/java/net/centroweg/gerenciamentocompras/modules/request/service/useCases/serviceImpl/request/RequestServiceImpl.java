package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.request;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.CreateRequestRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceIntrf.RequestService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final CreateRequestServiceImpl createRequestService;
    private final UpdateRequestServiceImpl updateRequestService;

    public RequestResponse createRequest(CreateRequestRequest request){
        return createRequestService.createRequest(request);
    }

    public RequestResponse updateRequest(CreateRequestRequest request, Long id){
        return updateRequestService.updateRequest(request, id);
    }

}
