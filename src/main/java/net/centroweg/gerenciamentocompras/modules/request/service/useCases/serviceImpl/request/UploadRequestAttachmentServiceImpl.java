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
import net.centroweg.gerenciamentocompras.shared.claudinary.ClaudinaryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UploadRequestAttachmentServiceImpl {

    private static final long MAX_FILE_SIZE =
            10L * 1024 * 1024;

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "application/pdf",
            "image/png",
            "image/jpeg",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    );

    private final RequestRepository requestRepository;
    private final RequestAttachmentRepository attachmentRepository;
    private final ClaudinaryService cloudinaryService;
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
                    "É necessário enviar pelo menos um arquivo."
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
        validateFile(file);

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

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new InvalidAttachmentException(
                    "O arquivo enviado está vazio."
            );
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new InvalidAttachmentException(
                    "O arquivo deve possuir no máximo 10 MB."
            );
        }

        if (!ALLOWED_CONTENT_TYPES.contains(file.getContentType())) {
            throw new InvalidAttachmentException(
                    "Tipo de arquivo não permitido."
            );
        }
    }
}