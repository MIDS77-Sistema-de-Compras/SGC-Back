package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.request;

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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UploadRequestAttachmentServiceImplTest {

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private RequestAttachmentRepository attachmentRepository;

    @Mock
    private CloudinaryService cloudinaryService;

    @Mock
    private RequestMapper requestMapper;

    @InjectMocks
    private UploadRequestAttachmentServiceImpl uploadRequestAttachmentService;

    @Captor
    private ArgumentCaptor<RequestAttachment> attachmentCaptor;

    @Test
    @DisplayName("Deve enviar anexo valido, salvar entidade e retornar DTO")
    void shouldUploadValidAttachmentAndReturnResponse() throws IOException {
        Long requestId = 1L;
        Request request = new Request();
        request.setId(requestId);

        MockMultipartFile file = pdfFile("teste.pdf", "conteudo fake do pdf");

        Map<?, ?> cloudinaryResult = Map.of(
                "secure_url", "https://res.cloudinary.com/demo/raw/upload/request-attachments/teste.pdf",
                "public_id", "request-attachments/teste_abc123",
                "resource_type", "raw"
        );

        RequestAttachment savedAttachment = new RequestAttachment();
        savedAttachment.setId(10L);
        savedAttachment.setOriginalName("teste.pdf");
        savedAttachment.setUrl((String) cloudinaryResult.get("secure_url"));
        savedAttachment.setPublicId((String) cloudinaryResult.get("public_id"));
        savedAttachment.setResourceType((String) cloudinaryResult.get("resource_type"));
        savedAttachment.setContentType("application/pdf");
        savedAttachment.setSize(file.getSize());
        savedAttachment.setUploadedAt(LocalDateTime.of(2026, 6, 18, 10, 0));
        savedAttachment.setRequest(request);

        RequestAttachmentResponse expectedResponse = new RequestAttachmentResponse(
                savedAttachment.getId(),
                savedAttachment.getOriginalName(),
                savedAttachment.getUrl(),
                savedAttachment.getContentType(),
                savedAttachment.getSize(),
                savedAttachment.getUploadedAt()
        );

        when(requestRepository.findById(requestId)).thenReturn(Optional.of(request));
        doReturn(cloudinaryResult).when(cloudinaryService).uploadFile(file);
        when(attachmentRepository.save(any(RequestAttachment.class))).thenReturn(savedAttachment);
        when(requestMapper.toAttachmentDTO(savedAttachment)).thenReturn(expectedResponse);

        List<RequestAttachmentResponse> responses =
                uploadRequestAttachmentService.uploadAttachments(requestId, List.of(file));

        assertEquals(List.of(expectedResponse), responses);

        verify(requestRepository).findById(requestId);
        verify(cloudinaryService).uploadFile(file);
        verify(attachmentRepository).save(attachmentCaptor.capture());
        verify(requestMapper).toAttachmentDTO(savedAttachment);

        RequestAttachment attachmentToSave = attachmentCaptor.getValue();
        assertEquals("teste.pdf", attachmentToSave.getOriginalName());
        assertEquals(cloudinaryResult.get("secure_url"), attachmentToSave.getUrl());
        assertEquals(cloudinaryResult.get("public_id"), attachmentToSave.getPublicId());
        assertEquals(cloudinaryResult.get("resource_type"), attachmentToSave.getResourceType());
        assertEquals("application/pdf", attachmentToSave.getContentType());
        assertEquals(file.getSize(), attachmentToSave.getSize());
        assertEquals(request, attachmentToSave.getRequest());

        verifyNoMoreInteractions(requestRepository, cloudinaryService, attachmentRepository, requestMapper);
    }

    @Test
    @DisplayName("Deve lancar RequestNotFoundException quando solicitacao nao existe")
    void shouldThrowRequestNotFoundWhenRequestDoesNotExist() {
        Long requestId = 999L;
        MockMultipartFile file = pdfFile("teste.pdf", "conteudo fake do pdf");

        when(requestRepository.findById(requestId)).thenReturn(Optional.empty());

        assertThrows(
                RequestNotFoundException.class,
                () -> uploadRequestAttachmentService.uploadAttachments(requestId, List.of(file))
        );

        verify(requestRepository).findById(requestId);
        verifyNoMoreInteractions(requestRepository);
        verifyNoInteractions(cloudinaryService, attachmentRepository, requestMapper);
    }

    @Test
    @DisplayName("Deve lancar InvalidAttachmentException quando lista de arquivos for nula")
    void shouldThrowInvalidAttachmentWhenFilesAreNull() {
        Long requestId = 1L;
        Request request = new Request();

        when(requestRepository.findById(requestId)).thenReturn(Optional.of(request));

        assertThrows(
                InvalidAttachmentException.class,
                () -> uploadRequestAttachmentService.uploadAttachments(requestId, null)
        );

        verify(requestRepository).findById(requestId);
        verifyNoMoreInteractions(requestRepository);
        verifyNoInteractions(cloudinaryService, attachmentRepository, requestMapper);
    }

    @Test
    @DisplayName("Deve lancar InvalidAttachmentException quando lista de arquivos for vazia")
    void shouldThrowInvalidAttachmentWhenFilesAreEmpty() {
        Long requestId = 1L;
        Request request = new Request();

        when(requestRepository.findById(requestId)).thenReturn(Optional.of(request));

        assertThrows(
                InvalidAttachmentException.class,
                () -> uploadRequestAttachmentService.uploadAttachments(requestId, List.of())
        );

        verify(requestRepository).findById(requestId);
        verifyNoMoreInteractions(requestRepository);
        verifyNoInteractions(cloudinaryService, attachmentRepository, requestMapper);
    }

    @Test
    @DisplayName("Deve lancar AttachmentUploadException quando Cloudinary falhar")
    void shouldThrowAttachmentUploadWhenCloudinaryThrowsIOException() throws IOException {
        Long requestId = 1L;
        Request request = new Request();
        MockMultipartFile file = pdfFile("teste.pdf", "conteudo fake do pdf");

        when(requestRepository.findById(requestId)).thenReturn(Optional.of(request));
        when(cloudinaryService.uploadFile(file)).thenThrow(new IOException("cloudinary unavailable"));

        assertThrows(
                AttachmentUploadException.class,
                () -> uploadRequestAttachmentService.uploadAttachments(requestId, List.of(file))
        );

        verify(requestRepository).findById(requestId);
        verify(cloudinaryService).uploadFile(file);
        verifyNoInteractions(attachmentRepository, requestMapper);
    }

    private MockMultipartFile pdfFile(String originalName, String content) {
        return new MockMultipartFile(
                "files",
                originalName,
                "application/pdf",
                content.getBytes(StandardCharsets.UTF_8)
        );
    }
}
