package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.request;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.RequestRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceIntrf.RequestService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final CreateRequestServiceImpl createRequestService;
    private final UpdateRequestServiceImpl updateRequestService;
    private final DeleteRequestServiceImpl deleteRequestService;
    private final FindAllRequestServiceImpl findAllRequestService;
    private final FindRequestByIdServiceImpl findRequestByIdService;

    public RequestResponse createRequest(RequestRequest request){
        return createRequestService.createRequest(request);
    }

    public List<RequestResponse> findAllRequest() {
        return findAllRequestService.findAllRequest();
    }

    public RequestResponse findRequestById(Long id) {
        return findRequestByIdService.findRequestById(id);
    }

    public RequestResponse updateRequest(RequestRequest request, Long id){
        return updateRequestService.updateRequest(request, id);
    }

    public void deleteRequest(Long id){
        deleteRequestService.deleteRequest(id);
    }

}
