package net.centroweg.gerenciamentocompras.shared.audit.domain.entity.infrastructure.persistence;

import net.centroweg.gerenciamentocompras.shared.audit.domain.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {


}
