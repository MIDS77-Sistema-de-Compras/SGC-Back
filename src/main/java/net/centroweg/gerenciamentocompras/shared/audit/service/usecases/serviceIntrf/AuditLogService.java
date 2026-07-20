package net.centroweg.gerenciamentocompras.shared.audit.service.usecases.serviceIntrf;

import java.util.List;

import net.centroweg.gerenciamentocompras.shared.audit.presentation.dto.request.AuditLogFilterRequest;
import net.centroweg.gerenciamentocompras.shared.audit.presentation.dto.response.AuditLogDTOResponse;

public interface AuditLogService {

    List<AuditLogDTOResponse> findAll(AuditLogFilterRequest filter, int limit);
}
