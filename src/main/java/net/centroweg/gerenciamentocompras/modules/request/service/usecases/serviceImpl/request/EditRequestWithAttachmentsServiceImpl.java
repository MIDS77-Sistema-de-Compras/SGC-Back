package net.centroweg.gerenciamentocompras.modules.request.service.usecases.serviceImpl.request;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.EditRequestRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.request.RequestMapper;

@Service
@RequiredArgsConstructor
public class EditRequestWithAttachmentsServiceImpl {

    private final EditRequestContentServiceImpl editRequestContentService;
    private final UploadRequestAttachmentServiceImpl uploadRequestAttachmentService;
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;

    @Transactional
    public RequestResponse edit(Long id, EditRequestRequest dto, List<MultipartFile> files) {
        editRequestContentService.edit(id, dto);
        if (files != null && !files.isEmpty()) {
            uploadRequestAttachmentService.uploadAttachments(id, files);
        }

        Request request = requestRepository.findById(id)
                .orElseThrow(RequestNotFoundException::new);
        return requestMapper.toDTO(request);
    }
}
