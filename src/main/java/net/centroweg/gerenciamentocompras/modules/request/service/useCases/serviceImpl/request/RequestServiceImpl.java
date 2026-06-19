package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.request;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.RequestFilterRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.RequestRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.UpdateFeedback;
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
    private final UpdateFeedbackServiceImpl updateFeedbackService;
    private final FindAllByUser findAllByUser;



    public RequestResponse createRequest(RequestRequest request){
        return createRequestService.createRequest(request);
    }

    public List<RequestResponse> findAllRequest(RequestFilterRequest filter) {
        return findAllRequestService.findAllRequest(filter);
    }

    public RequestResponse findRequestById(Long id) {
        return findRequestByIdService.findRequestById(id);
    }

    public RequestResponse updateRequest(RequestRequest request, Long id){
        return updateRequestService.updateRequest(request, id);
    }


    public List<RequestResponse> findAllByUser(RequestFilterRequest filter, UserPrincipal userPrincipal) {
        return findAllByUser.findAllByUser(filter, userPrincipal);
    }

    public void deleteRequest(Long id){
        deleteRequestService.deleteRequest(id);
    }

    public RequestResponse updateFeedback(UpdateFeedback feedback, Long id){
        return updateFeedbackService.updateFeedback(feedback, id);
    }

}
