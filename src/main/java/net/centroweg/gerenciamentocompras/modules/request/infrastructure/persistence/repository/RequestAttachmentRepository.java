package net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository;

import net.centroweg.gerenciamentocompras.modules.request.domain.entity.RequestAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositório de acesso a dados da entidade {@link RequestAttachment}.
 */
@Repository
public interface RequestAttachmentRepository extends JpaRepository<RequestAttachment, Long> {

    /**
     * Lista todos os anexos cadastrados no banco de dados associados a uma requisição específica.
     * @param requestId identificador da requisição.
     * @return lista com todos os anexos encontrados, caso exista.
     */
    List<RequestAttachment> findByRequestId(Long requestId);
}