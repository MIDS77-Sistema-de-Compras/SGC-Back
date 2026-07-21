package net.centroweg.gerenciamentocompras.modules.request.service.usecases.serviceImpl.request;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.RequestRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.request.RequestMapper;

@Service
@RequiredArgsConstructor
public class CreateRequestWithAttachmentsServiceImpl {

    private final CreateRequestServiceImpl createRequestService;
    private final UploadRequestAttachmentServiceImpl uploadRequestAttachmentService;
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;

    @Transactional
    public RequestResponse create(
            RequestRequest request,
            List<MultipartFile> files,
            UserPrincipal userPrincipal
    ) {
        RequestResponse created = createRequestService.createRequest(request, userPrincipal);
        uploadRequestAttachmentService.uploadInitialAttachments(created.id(), files, userPrincipal);

        Request completeRequest = requestRepository.findById(created.id())
                .orElseThrow(RequestNotFoundException::new);
        return requestMapper.toDTO(completeRequest);
    }
}
