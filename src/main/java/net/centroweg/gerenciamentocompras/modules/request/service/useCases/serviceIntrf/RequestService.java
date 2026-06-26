package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceIntrf;


import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.RequestFilterRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.RequestRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.UpdateFeedback;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.UpdateRequestRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestAttachmentResponse;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RequestService {

    RequestResponse createRequest(RequestRequest request, UserPrincipal userPrincipal);
    List<RequestResponse> findAllRequest(RequestFilterRequest filter);
    RequestResponse findRequestById(Long id);
    List<RequestResponse> findAllByUser(RequestFilterRequest filter, UserPrincipal userPrincipal);
    RequestResponse updateRequest(UpdateRequestRequest request, Long id);
    void deleteRequest(Long id);
    RequestResponse updateFeedback(UpdateFeedback feedback, Long id);
    List<RequestAttachmentResponse> uploadAttachments(
            Long requestId,
            List<MultipartFile> files
    );
    void deleteRequestByOwnUser(long id, UserPrincipal userPrincipal);
    RequestResponse updateRequestByOwnUser(RequestRequest request, Long id, UserPrincipal userPrincipal);
    RequestResponse findRequestByIdOwnUser(Long id, UserPrincipal userPrincipal);

}
