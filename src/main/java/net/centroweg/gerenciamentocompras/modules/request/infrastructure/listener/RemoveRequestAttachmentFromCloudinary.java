package net.centroweg.gerenciamentocompras.modules.request.infrastructure.listener;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.centroweg.gerenciamentocompras.modules.request.service.event.RequestAttachmentRemovedEvent;
import net.centroweg.gerenciamentocompras.shared.cloudinary.CloudinaryService;

@Slf4j
@Component
@RequiredArgsConstructor
public class RemoveRequestAttachmentFromCloudinary {

    private final CloudinaryService cloudinaryService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void remove(RequestAttachmentRemovedEvent event) {
        try {
            cloudinaryService.deleteFile(event.publicId(), event.resourceType());
        } catch (IOException exception) {
            log.error("Falha ao remover anexo {} do Cloudinary", event.publicId(), exception);
        }
    }
}
