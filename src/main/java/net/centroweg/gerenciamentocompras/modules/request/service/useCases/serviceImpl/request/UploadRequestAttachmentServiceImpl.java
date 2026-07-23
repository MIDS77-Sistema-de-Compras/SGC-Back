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

/**
 * Caso de uso responsável pelo upload de um {@link RequestAttachment}.
 */
@Service
@RequiredArgsConstructor
public class UploadRequestAttachmentServiceImpl {

    private final RequestRepository requestRepository;
    private final RequestAttachmentRepository attachmentRepository;
    private final CloudinaryService cloudinaryService;
    private final RequestMapper requestMapper;

    /**
     * Realiza o upload de anexos de uma solicitação no banco de dados.
     * @param requestId identificador da solicitação.
     * @param files arquivos a serem anexados.
     * @return lista com todos os anexos da solicitação criados.
     * @throws RequestNotFoundException caso nenhuma solicitação seja encontrada.
     * @throws InvalidAttachmentException caso nenhum arquivo seja enviado.
     */
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

    /**
     * Realiza o upload de um único arquivo e persiste o anexo da solicitação no banco de dados.
     * @param request dados da solicitação.
     * @param file arquivo a ser anexado.
     * @return anexo da solicitação criado.
     * @throws AttachmentUploadException caso ocorra um erro durante o upload do arquivo.
     */
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
