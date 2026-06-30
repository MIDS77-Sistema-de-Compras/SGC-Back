package net.centroweg.gerenciamentocompras.shared.audit.service.usecase.impl;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.shared.audit.presentation.dto.response.AuditLogDTOResponse;
import net.centroweg.gerenciamentocompras.shared.audit.service.usecase.interfaces.AuditLogService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {

    private final ListAuditLogAll listAuditLogAll;

    @Override
    public List<AuditLogDTOResponse> findAll() {
        return listAuditLogAll.findAll();
    }
}