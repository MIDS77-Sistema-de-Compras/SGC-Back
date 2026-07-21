package net.centroweg.gerenciamentocompras.modules.request.service.event;

public record RequestAttachmentRemovedEvent(
        String publicId,
        String resourceType
) {
}
