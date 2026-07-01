package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.request;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.RequestFilterRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.RequestRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.UpdateFeedback;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.UpdateRequestRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.UpdateRequestStatus;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceIntrf.RequestService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
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
    private final DeleteRequestByOwnUser deleteRequestByOwnUser;
    private final UpdateRequestByOwnUser updateRequestByOwnUser;
    private final FindRequestByIdOwnUser findRequestByIdOwnUser;
    private final UpdateRequestStatusServiceImpl updateRequestStatusService;


    private final UploadRequestAttachmentServiceImpl uploadRequestAttachmentService;

    @Override
    public RequestResponse createRequest(RequestRequest request, UserPrincipal userPrincipal){
        return createRequestService.createRequest(request, userPrincipal);
    }

    @Override
    public Page<RequestResponse> findAllRequest(RequestFilterRequest filter, Pageable pageable) {
        return findAllRequestService.findAllRequest(filter, pageable);
    }

    @Override
    public RequestResponse findRequestById(Long id) {
        return findRequestByIdService.findRequestById(id);
    }

    @Override
    public RequestResponse updateRequest(UpdateRequestRequest request, Long id){
        return updateRequestService.updateRequest(request, id);
    }

    @Override
    public Page<RequestResponse> findAllByUser(RequestFilterRequest filter, UserPrincipal userPrincipal, Pageable pageable) {
        return findAllByUser.findAllByUser(filter, userPrincipal, pageable);
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

    @Override
    public void deleteRequestByOwnUser(long id, UserPrincipal userPrincipal){
        this.deleteRequestByOwnUser.deleteRequestByOwnUser(id, userPrincipal);
    }

    @Override
    public RequestResponse updateRequestByOwnUser(RequestRequest request, Long id, UserPrincipal userPrincipal){
        return this.updateRequestByOwnUser.updateRequest(request, id, userPrincipal);
    }

    @Override
    public RequestResponse findRequestByIdOwnUser(Long id, UserPrincipal userPrincipal){
        return this.findRequestByIdOwnUser.findRequestByIdOwnUser(id, userPrincipal);
    }

    @Override
    public RequestResponse updateStatus(Long id, UpdateRequestStatus request) {
        return updateRequestStatusService.updateStatus(id, request);
    }

}
