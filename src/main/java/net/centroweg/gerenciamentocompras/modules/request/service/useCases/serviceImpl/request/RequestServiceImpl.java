package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.request;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
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

    public RequestResponse createRequest(RequestRequest request){
        return createRequestService.createRequest(request);
    }

    public List<RequestResponse> findAllRequest(UserPrincipal userPrincipal) {
        return findAllRequestService.findAllRequest(userPrincipal);
    }

    public RequestResponse findRequestById(Long id, UserPrincipal userPrincipal) {
        return findRequestByIdService.findRequestById(id, userPrincipal);
    }

    public RequestResponse updateRequest(RequestRequest request, Long id, UserPrincipal userPrincipal){
        return updateRequestService.updateRequest(request, id,  userPrincipal);
    }

    public void deleteRequest(Long id, UserPrincipal userPrincipal){
        deleteRequestService.deleteRequest(id, userPrincipal);
    }

    public RequestResponse updateFeedback(UpdateFeedback feedback, Long id){
        return updateFeedbackService.updateFeedback(feedback, id);
    }

}
