package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.request;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.RequestFilterRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.RequestRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.UpdateFeedback;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceIntrf.RequestService;
import org.springframework.stereotype.Service;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestAttachmentResponse;
import org.springframework.web.multipart.MultipartFile;

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


    private final UploadRequestAttachmentServiceImpl uploadRequestAttachmentService;

    @Override
    public RequestResponse createRequest(RequestRequest request){
        return createRequestService.createRequest(request);
    }

    @Override
    public List<RequestResponse> findAllRequest(RequestFilterRequest filter) {
        return findAllRequestService.findAllRequest(filter);
    }

    @Override
    public RequestResponse findRequestById(Long id) {
        return findRequestByIdService.findRequestById(id);
    }

    @Override
    public RequestResponse updateRequest(RequestRequest request, Long id){
        return updateRequestService.updateRequest(request, id);
    }

    @Override
    public List<RequestResponse> findAllByUser(RequestFilterRequest filter, UserPrincipal userPrincipal) {
        return findAllByUser.findAllByUser(filter, userPrincipal);
    }

    @Override
    public void deleteRequest(Long id){
        deleteRequestService.deleteRequest(id);
    }

    @Override
    public RequestResponse updateFeedback(UpdateFeedback feedback, Long id){
        return updateFeedbackService.updateFeedback(feedback, id);
    }

    @Override
    public List<RequestAttachmentResponse> uploadAttachments(
            Long requestId,
            List<MultipartFile> files
    ) {
        return uploadRequestAttachmentService.uploadAttachments(
                requestId,
                files
        );
    }

}
