package net.centroweg.gerenciamentocompras.shared.audit.service.mapper;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.shared.audit.domain.entity.AuditLog;
import net.centroweg.gerenciamentocompras.shared.audit.presentation.dto.request.AuditLogDTORequest;
import net.centroweg.gerenciamentocompras.shared.audit.presentation.dto.response.AuditLogDTOResponse;
import net.centroweg.gerenciamentocompras.shared.audit.service.api.AuditLogPublicApi;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuditLogMapper {

    private AuditLogPublicApi publicApi;

    public AuditLog toEntity(AuditLogDTORequest dto){
        return new AuditLog(
                publicApi.findByUserName(dto.userAgentName()),
                publicApi.findByUserName(dto.userTargetName()),
                dto.typeAction(),
                publicApi.findByRequestId(dto.request())
        );
    }

    public AuditLogDTOResponse toResponse(AuditLog entity){
        return new AuditLogDTOResponse(
                entity.getId(),
                entity.getUserAgent().getName(),
                entity.getUserAgent().getRole().getName(),
                entity.getTypeAction(),
                entity.getDescription(),
                (entity.getUserTarget() != null)? entity.getUserTarget().getName() : null,
                (entity.getRequest() != null)? entity.getRequest().getId() : null,
                entity.getTimestamp()
        );
    }
}
