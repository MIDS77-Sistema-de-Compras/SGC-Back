package net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository;

import net.centroweg.gerenciamentocompras.modules.request.domain.entity.RequestAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestAttachmentRepository
        extends JpaRepository<RequestAttachment, Long> {

    List<RequestAttachment> findByRequestId(Long requestId);
}