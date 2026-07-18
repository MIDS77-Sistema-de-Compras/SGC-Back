package net.centroweg.gerenciamentocompras.shared.audit.service.api;

import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;

public interface AuditLogPublicApi {

    User findByUserName(String name);

    Request findByRequestId(Long id);

    User findByUserEmail(String email);

    User findByUserId(Long id);

}
