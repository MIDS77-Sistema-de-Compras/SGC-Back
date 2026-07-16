package net.centroweg.gerenciamentocompras.shared.audit.service.usecases.serviceIntrf;

import net.centroweg.gerenciamentocompras.shared.audit.domain.entity.AuditLog;
import net.centroweg.gerenciamentocompras.shared.audit.presentation.dto.request.AuditLogFilterRequest;
import net.centroweg.gerenciamentocompras.shared.audit.presentation.dto.response.AuditLogDTOResponse;

import java.util.List;

public interface AuditLogService {

    List<AuditLogDTOResponse> findAll(AuditLogFilterRequest filter);
}
