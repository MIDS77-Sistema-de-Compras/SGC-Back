package net.centroweg.gerenciamentocompras.shared.audit.service.usecases.serviceIntrf;

import net.centroweg.gerenciamentocompras.shared.audit.presentation.dto.request.AuditLogFilterRequest;
import net.centroweg.gerenciamentocompras.shared.audit.presentation.dto.response.AuditLogDTOResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuditLogService {

    Page<AuditLogDTOResponse> findAll(AuditLogFilterRequest filter, Pageable pageable);
}
