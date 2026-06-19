package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.request;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.RequestAttachment;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.AttachmentUploadException;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.InvalidAttachmentException;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestAttachmentRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestAttachmentResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.request.RequestMapper;
import net.centroweg.gerenciamentocompras.shared.cloudinary.CloudinaryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UploadRequestAttachmentServiceImpl {

    private final RequestRepository requestRepository;
    private final RequestAttachmentRepository attachmentRepository;
    private final CloudinaryService cloudinaryService;
    private final RequestMapper requestMapper;

    @Transactional
    public List<RequestAttachmentResponse> uploadAttachments(
            Long requestId,
            List<MultipartFile> files
    ) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(RequestNotFoundException::new);

        if (files == null || files.isEmpty()) {
            throw new InvalidAttachmentException(
                    "E necessario enviar pelo menos um arquivo."
            );
        }

        return files.stream()
                .map(file -> uploadFile(request, file))
                .toList();
    }

    private RequestAttachmentResponse uploadFile(
            Request request,
            MultipartFile file
    ) {
        try {
            Map<?, ?> result = cloudinaryService.uploadFile(file);

            RequestAttachment attachment = new RequestAttachment();

            String originalName = Objects.requireNonNullElse(
                    file.getOriginalFilename(),
                    "arquivo"
            );

            attachment.setOriginalName(
                    StringUtils.cleanPath(originalName)
            );

            attachment.setUrl(
                    (String) result.get("secure_url")
            );

            attachment.setPublicId(
                    (String) result.get("public_id")
            );

            attachment.setResourceType(
                    (String) result.get("resource_type")
            );

            attachment.setContentType(file.getContentType());
            attachment.setSize(file.getSize());
            attachment.setRequest(request);

            RequestAttachment savedAttachment =
                    attachmentRepository.save(attachment);

            return requestMapper.toAttachmentDTO(savedAttachment);

        } catch (IOException exception) {
            throw new AttachmentUploadException();
        }
    }
}
